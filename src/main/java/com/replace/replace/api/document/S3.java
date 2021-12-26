package com.replace.replace.api.document;

import com.replace.replace.api.environment.Environment;
import com.replace.replace.configuration.environment.Variable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class S3 implements DocumentStorageHandler {

    protected S3Client    s3Client = null;
    protected Environment environment;


    public S3( final Environment environment ) {
        this.environment = environment;
    }


    @Override
    public boolean create( final String path, final File file ) {
        final PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                                .bucket( this.environment.getEnv( Variable.DOCUMENT_AWS_BUCKET ) )
                                .key( path )
                                .build();

        try {

            final byte[] content = Files.readAllBytes( Path.of( file.getPath() ) );

            this.connect().putObject( putObjectRequest, RequestBody.fromBytes( content ) );

            return true;
        } catch ( final IOException e ) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean create( final String path, final ByteBuffer byteBuffer ) {
        final PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                                .bucket( this.environment.getEnv( Variable.DOCUMENT_AWS_BUCKET ) )
                                .key( path )
                                .build();

        this.connect().putObject( putObjectRequest, RequestBody.fromByteBuffer( byteBuffer ) );

        return true;
    }


    @Override
    public boolean create( final String path, final byte[] bytes ) {
        final PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                                .bucket( this.environment.getEnv( Variable.DOCUMENT_AWS_BUCKET ) )
                                .key( path )
                                .build();

        this.connect().putObject( putObjectRequest, RequestBody.fromBytes( bytes ) );

        return true;
    }


    @Override
    public boolean remove( final String path ) {

        final DeleteObjectRequest deleteObjectRequest =
                DeleteObjectRequest.builder()
                                   .bucket( this.environment.getEnv( Variable.DOCUMENT_AWS_BUCKET ) )
                                   .key( path )
                                   .build();

        this.connect().deleteObject( deleteObjectRequest );

        return true;
    }


    @Override
    public boolean move( final String path, final String newPath ) {
        final byte[] content = this.getContent( path );


        if ( this.create( newPath, content ) ) {

            this.remove( path );

            return true;
        }


        return false;
    }


    @Override
    public byte[] getContent( final String path ) {

        final GetObjectRequest getObjectRequest = this.getObjectRequest( path );

        try {
            return this.connect().getObject( getObjectRequest ).readAllBytes();
        } catch ( final Throwable e ) {
            return null;
        }
    }


    @Override
    public String getUrl( final String path, final Integer time ) {
        final GetObjectPresignRequest getObjectPresignRequest =
                GetObjectPresignRequest.builder()
                                       .signatureDuration( Duration.ofMinutes( time ) )
                                       .getObjectRequest( this.getObjectRequest( path ) )
                                       .build();


        final AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                this.environment.getEnv( Variable.DOCUMENT_PUBLIC_KEY ),
                this.environment.getEnv( Variable.DOCUMENT_PRIVATE_KEY )
        );

        final S3Presigner s3Presigner =
                S3Presigner.builder()
                           .credentialsProvider( StaticCredentialsProvider.create( awsBasicCredentials ) )
                           .region( Region.EU_WEST_3 )
                           .build();

        final URL    response = s3Presigner.presignGetObject( getObjectPresignRequest ).url();
        final String url      = response.toString();

        s3Presigner.close();

        return url;
    }


    @Override
    public String getUrl( final String path ) {
        return this.getUrl( path, 20 );
    }


    protected GetObjectRequest getObjectRequest( final String path ) {
        return GetObjectRequest.builder()
                               .bucket( this.environment.getEnv( Variable.DOCUMENT_AWS_BUCKET ) )
                               .key( path )
                               .build();
    }


    protected S3Client connect() {
        if ( this.s3Client != null ) {
            return this.s3Client;
        }

        final AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                this.environment.getEnv( Variable.DOCUMENT_PUBLIC_KEY ),
                this.environment.getEnv( Variable.DOCUMENT_PRIVATE_KEY )
        );

        return this.s3Client =
                S3Client.builder()
                        .credentialsProvider( StaticCredentialsProvider.create( awsBasicCredentials ) )
                        .region( Region.EU_WEST_3 )
                        .build();
    }
}

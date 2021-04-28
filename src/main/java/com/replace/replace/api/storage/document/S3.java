package com.replace.replace.api.storage.document;

import com.replace.replace.api.environment.Environment;
import com.replace.replace.api.environment.EnvironmentVariable;
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

    public S3( Environment environment ) {
        this.environment = environment;
    }

    @Override
    public boolean create( String path, File file ) {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                                .bucket( this.environment.getEnv( EnvironmentVariable.DOCUMENT_AWS_BUCKET ) )
                                .key( path )
                                .build();

        try {

            byte[] content = Files.readAllBytes( Path.of( file.getPath() ) );

            this.connect().putObject( putObjectRequest, RequestBody.fromBytes( content ) );

            return true;
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean create( String path, ByteBuffer byteBuffer ) {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                                .bucket( this.environment.getEnv( EnvironmentVariable.DOCUMENT_AWS_BUCKET ) )
                                .key( path )
                                .build();

        this.connect().putObject( putObjectRequest, RequestBody.fromByteBuffer( byteBuffer ) );

        return true;
    }

    @Override
    public boolean create( String path, byte[] bytes ) {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                                .bucket( this.environment.getEnv( EnvironmentVariable.DOCUMENT_AWS_BUCKET ) )
                                .key( path )
                                .build();

        this.connect().putObject( putObjectRequest, RequestBody.fromBytes( bytes ) );

        return true;
    }

    @Override
    public boolean remove( String path ) {
        path = this.fixPath( path );

        DeleteObjectRequest deleteObjectRequest =
                DeleteObjectRequest.builder()
                                   .bucket( this.environment.getEnv( EnvironmentVariable.DOCUMENT_AWS_BUCKET ) )
                                   .key( path )
                                   .build();

        this.connect().deleteObject( deleteObjectRequest );

        return true;
    }

    @Override
    public byte[] getContent( String path ) {
        path = this.fixPath( path );


        GetObjectRequest getObjectRequest = this.getObjectRequest( path );

        try {
            return this.connect().getObject( getObjectRequest ).readAllBytes();
        } catch ( IOException e ) {
            return null;
        }
    }

    @Override
    public String getUrl( String path, Integer time ) {
        GetObjectPresignRequest getObjectPresignRequest =
                GetObjectPresignRequest.builder()
                                       .signatureDuration( Duration.ofMinutes( time ) )
                                       .getObjectRequest( this.getObjectRequest( path ) )
                                       .build();


        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                this.environment.getEnv( EnvironmentVariable.DOCUMENT_PUBLIC_KEY ),
                this.environment.getEnv( EnvironmentVariable.DOCUMENT_PRIVATE_KEY )
        );

        S3Presigner s3Presigner =
                S3Presigner.builder()
                           .credentialsProvider( StaticCredentialsProvider.create( awsBasicCredentials ) )
                           .region( Region.EU_WEST_3 )
                           .build();

        URL    response = s3Presigner.presignGetObject( getObjectPresignRequest ).url();
        String url      = response.toString();

        s3Presigner.close();

        return url;
    }

    @Override
    public String getUrl( String path ) {
        return this.getUrl( path, 20 );
    }

    protected GetObjectRequest getObjectRequest( String path ) {
        return GetObjectRequest.builder()
                               .bucket( this.environment.getEnv( EnvironmentVariable.DOCUMENT_AWS_BUCKET ) )
                               .key( path )
                               .build();
    }

    protected S3Client connect() {
        if ( this.s3Client != null ) {
            return this.s3Client;
        }

        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                this.environment.getEnv( EnvironmentVariable.DOCUMENT_PUBLIC_KEY ),
                this.environment.getEnv( EnvironmentVariable.DOCUMENT_PRIVATE_KEY )
        );

        return this.s3Client =
                S3Client.builder()
                        .credentialsProvider( StaticCredentialsProvider.create( awsBasicCredentials ) )
                        .region( Region.EU_WEST_3 )
                        .build();

    }

    protected String fixPath( String path ) {
        return path.replaceFirst( "^/", "" );
    }
}

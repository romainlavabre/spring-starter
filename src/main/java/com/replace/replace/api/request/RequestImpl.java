package com.replace.replace.api.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.replace.replace.api.upload.UploadedFile;
import com.replace.replace.api.upload.UploadedFileImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Service
@Scope( value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES )
public class RequestImpl implements Request {

    private final HttpServletRequest    request;
    private final Map< String, Object > parameters;


    public RequestImpl() throws JsonProcessingException {
        this.parameters = new HashMap<>();
        this.request    = (( ServletRequestAttributes ) RequestContextHolder.getRequestAttributes()).getRequest();
        this.parseJson();
    }


    @Override
    public Object getParameter( final String name ) {

        return this.parameters.get( name );
    }


    @Override
    public void setParameter( final String name, final Object value ) {
        this.parameters.put( name, value );
    }


    @Override
    public List< Object > getParameters( final String name ) {
        return ( List< Object > ) this.parameters.get( name );
    }


    @Override
    public String getQueryString( final String name ) {
        return ( String ) this.request.getAttribute( name );
    }


    @Override
    public void setQueryString( final String name, final Object value ) {
        this.request.setAttribute( name, value );
    }


    @Override
    public String getClientIp() {
        return this.request.getRemoteAddr();
    }


    @Override
    public UploadedFile getFile( final String name ) {
        return ( UploadedFile ) this.parameters.get( name );
    }


    @Override
    public List< UploadedFile > getFiles( final String name ) {
        return ( List< UploadedFile > ) this.parameters.get( name );
    }


    @Override
    public void setUploadedFile( final String name, final UploadedFile uploadedFile ) {
        if ( this.parameters.get( name ) instanceof List ) {
            final List< UploadedFile > uploadedFiles = ( List< UploadedFile > ) this.parameters.get( name );

            uploadedFiles.add( uploadedFile );

            return;
        }

        this.parameters.put( name, uploadedFile );

    }


    @Override
    public String getHeader( final String name ) {
        return this.request.getHeader( name );
    }


    @Override
    public String getContentType() {
        return this.request.getContentType();
    }


    @Override
    public Integer getPort() {
        return this.request.getServerPort();
    }


    @Override
    public String getHost() {
        return this.request.getRemoteHost();
    }


    @Override
    public String getScheme() {
        return this.request.getScheme();
    }


    @Override
    public String getUri() {
        return this.request.getRequestURI();
    }


    @Override
    public String getBaseUrl() {
        return this.request
                .getRequestURL()
                .toString()
                .replaceAll( this.getUri(), "" );
    }


    @Override
    public String getMethod() {
        return this.request.getMethod();
    }


    private void parseJson() throws JsonProcessingException {

        final StringBuffer json = new StringBuffer();

        String         line   = null;
        BufferedReader reader = null;

        try {
            reader = this.request.getReader();

            while ( (line = reader.readLine()) != null ) {
                json.append( line );
            }
        } catch ( final IOException e ) {
            e.printStackTrace();
        }


        final ObjectMapper objectMapper = new ObjectMapper();

        final Map< String, Object > map = objectMapper.readValue( json.toString(), HashMap.class );

        for ( final Map.Entry< String, Object > input : map.entrySet() ) {

            if ( input.getKey().equals( "uploaded_file" ) ) {

                for ( final Map.Entry< String, Map< String, Object > > entry : (( Map< String, Map< String, Object > > ) input.getValue()).entrySet() ) {

                    if ( entry.getValue() instanceof Map ) {
                        this.setUploadedFile( entry.getKey(), this.getUploadedFile( entry.getValue() ) );
                    }

                    if ( entry.getValue() instanceof List ) {
                        for ( final Map< String, Object > uploadedFile : ( List< Map< String, Object > > ) entry.getValue() ) {

                            if ( this.parameters.containsKey( entry.getKey() ) ) {
                                final List< UploadedFile > list = ( List< UploadedFile > ) this.parameters.get( entry.getKey() );
                                list.add( this.getUploadedFile( uploadedFile ) );

                                continue;
                            }

                            final List< UploadedFile > list = new ArrayList<>();

                            list.add( this.getUploadedFile( uploadedFile ) );

                            this.parameters.put( entry.getKey(), list );
                        }
                    }
                }


                continue;
            }

            if ( input.getValue() instanceof Map ) {
                final Map< String, Object > secondLevel = ( Map< String, Object > ) input.getValue();

                for ( final Map.Entry< String, Object > content : secondLevel.entrySet() ) {
                    this.parameters.put( input.getKey() + "_" + content.getKey(), content.getValue() );
                }
            }

            if ( input.getValue() instanceof List ) {

                for ( final Map< String, Object > thirdLevel : ( List< HashMap< String, Object > > ) input.getValue() ) {
                    for ( final Map.Entry< String, Object > content : thirdLevel.entrySet() ) {
                        final String key = input.getKey() + "_" + content.getKey();

                        if ( this.parameters.containsKey( key ) ) {
                            final List< Object > list = ( List< Object > ) this.parameters.get( key );
                            list.add( content.getValue() );

                        } else {
                            final List< Object > list = new ArrayList<>();
                            list.add( content.getValue() );

                            this.parameters.put( key, list );
                        }
                    }
                }
            }
        }
    }


    protected UploadedFile getUploadedFile( final Map< String, Object > map ) {
        final UploadedFile uploadedFile = new UploadedFileImpl();
        uploadedFile.setName( ( String ) map.get( "name" ) );
        uploadedFile.setContent( Base64.getDecoder().decode( ( String ) map.get( "content" ) ) );
        uploadedFile.setContentType( ( String ) map.get( "content-type" ) );
        uploadedFile.setSize( uploadedFile.getContent().length );
        uploadedFile.setInfos( ( Map< String, Object > ) map.get( "infos" ) );

        return uploadedFile;
    }

}

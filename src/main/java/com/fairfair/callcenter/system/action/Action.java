package com.fairfair.callcenter.system.action;

import com.fairfair.callcenter.api.json.annotation.Group;
import com.fairfair.callcenter.api.json.annotation.Json;
import com.fairfair.callcenter.exception.HttpInternalServerErrorException;
import com.fairfair.callcenter.system.action.converter.JsonConverter;
import jakarta.persistence.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Action {

    @Json( groups = {
            @Group( key = "request_payload" )
    } )
    @Convert( converter = JsonConverter.class )
    @Column( name = "request_payload", columnDefinition = "JSON" )
    private final Map< String, Object > requestPayload;
    private final ZonedDateTime createdAt;
    @Json( groups = {
            @Group( key = "external_id" )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;
    @Json( groups = {
            @Group
    } )
    @Column( nullable = false )
    private String method;
    @Json( groups = {
            @Group
    } )
    @Column( nullable = false )
    private String uri;
    @Json( groups = {
            @Group( key = "max_retry" )
    } )
    @Column( name = "max_retry" )
    private Short maxRetry;
    @Json( groups = {
            @Group( key = "time_out" )
    } )
    @Column( name = "time_out" )
    private Long timeOut;
    @Json( groups = {
            @Group
    } )
    private String webhook;
    private String tracer;
    private boolean treated;
    private boolean webhookTreated;
    private boolean removable;


    public Action() {
        requestPayload = new HashMap<>();
        treated        = false;
        webhookTreated = false;
        removable      = false;
        createdAt      = ZonedDateTime.now( ZoneId.of( "UTC" ) );
    }


    public Action( Action action ) {
        method         = action.getMethod().toString();
        uri            = action.getUri();
        requestPayload = new HashMap<>( action.getRequestPayload() );
        maxRetry       = action.getMaxRetry();
        timeOut        = action.getTimeOut();
        webhook        = action.getWebhook();
        tracer         = action.getTracer();
        treated        = false;
        removable      = false;
        createdAt      = action.getCreatedAt();
    }


    public long getId() {
        return id;
    }


    public RequestMethodType getMethod() {

        switch ( method ) {
            case "GET":
                return RequestMethodType.GET;
            case "POST":
                return RequestMethodType.POST;
            case "PUT":
                return RequestMethodType.PUT;
            case "PATCH":
                return RequestMethodType.PATCH;
            case "DELETE":
                return RequestMethodType.DELETE;
            default:
                return null;
        }
    }


    public Action setMethod( RequestMethodType method ) {
        this.method = method.toString();

        return this;
    }


    @Deprecated( forRemoval = true )
    public String getUri() {
        return getUrl();
    }


    @Deprecated( forRemoval = true )
    public Action setUri( String uri ) {
        return setUrl( uri );
    }


    public String getUrl() {
        return uri;
    }


    public Action setUrl( String uri ) {
        if ( uri == null || uri.isBlank() ) {
            throw new HttpInternalServerErrorException( ErrorMessage.ACTION_INVALID_URI );
        }

        this.uri = uri;

        return this;
    }


    public Action routeParam( String key, Object value ) {
        if ( uri == null ) {
            throw new HttpInternalServerErrorException( ErrorMessage.ACTION_ROUTE_PARAM_SET_BEFORE_URI );
        }

        if ( key.contains( "{" ) && key.contains( "}" ) ) {
            uri = uri.replace( key, String.valueOf( value ) );
        } else {
            uri = uri.replace( "{" + key + "}", String.valueOf( value ) );
        }

        return this;
    }


    public Map< String, Object > getRequestPayload() {
        return requestPayload;
    }


    public Action setRequestPayload( Map< String, Object > requestPayload ) {
        this.requestPayload.putAll( requestPayload );

        return this;
    }


    public Action addRequestPayload( String key, Object value ) {
        requestPayload.put( key, value );

        return this;
    }


    public Action removeRequestPayload( String key ) {
        requestPayload.remove( key );

        return this;
    }


    public Short getMaxRetry() {
        return maxRetry;
    }


    public Action setMaxRetry( Short maxRetry ) {
        this.maxRetry = maxRetry;

        return this;
    }


    public Long getTimeOut() {
        return timeOut;
    }


    public Action setTimeOut( Long timeOut ) {
        this.timeOut = timeOut;

        return this;
    }


    public Action setTimeOut( TimeOut timeOut ) {
        switch ( timeOut ) {
            case ONE_MINUTE:
                this.timeOut = 60L;
                break;
            case TWO_MINUTES:
                this.timeOut = 60L * 2;
                break;
            case THREE_MINUTES:
                this.timeOut = 60L * 3;
                break;
            case FOUR_MINUTES:
                this.timeOut = 60L * 4;
                break;
            case FIVE_MINUTES:
                this.timeOut = 60L * 5;
                break;
            case TEN_MINUTES:
                this.timeOut = 60L * 10;
                break;
            case FIFTEEN_MINUTES:
                this.timeOut = 60L * 15;
                break;
            case TWENTY_MINUTES:
                this.timeOut = 60L * 20;
                break;
            case TWENTY_FIVE_MINUTES:
                this.timeOut = 60L * 25;
                break;
            case THIRTY_MINUTES:
                this.timeOut = 60L * 30;
                break;
            case ONE_HOUR:
                this.timeOut = 60L * 60;
                break;
            case TWO_HOURS:
                this.timeOut = 60L * 60 * 2;
                break;
            case THREE_HOURS:
                this.timeOut = 60L * 60 * 3;
                break;
            case FOUR_HOURS:
                this.timeOut = 60L * 60 * 4;
                break;
            case FIVE_HOURS:
                this.timeOut = 60L * 60 * 5;
                break;
            case SIX_HOURS:
                this.timeOut = 60L * 60 * 6;
                break;
        }

        return this;
    }


    public String getWebhook() {
        return webhook;
    }


    public Action setWebhook( String webhook ) {
        this.webhook = webhook;

        return this;
    }


    public String getTracer() {
        return tracer;
    }


    public Action setTracer( String tracer ) {
        this.tracer = tracer;

        return this;
    }


    public boolean isTreated() {
        return treated;
    }


    public Action setTreated( boolean treated ) {
        this.treated = treated;

        return this;
    }


    public boolean isWebhookTreated() {
        return webhookTreated;
    }


    public Action setWebhookTreated( boolean webhookTreated ) {
        this.webhookTreated = webhookTreated;

        return this;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public boolean isRemovable() {
        return removable;
    }


    public Action setRemovable( boolean remove ) {
        this.removable = remove;

        return this;
    }
}


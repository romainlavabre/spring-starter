package com.fairfair.callcenter.api.history;

import com.fairfair.callcenter.api.json.annotation.Group;
import com.fairfair.callcenter.api.json.annotation.Json;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
public class History {

    public static final String TYPE_CREATE = "CREATE";
    public static final String TYPE_UPDATE = "UPDATE";
    public static final String TYPE_DELETE = "DELETE";

    @Json( groups = {
            @Group
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Json( groups = {
            @Group( key = "author_type" )
    } )
    @Column( name = "author_type" )
    private String authorType;

    @Json( groups = {
            @Group( key = "author_id" )
    } )
    @Column( name = "author_id" )
    private int authorId;

    @Json( groups = {
            @Group( key = "subject_type" )
    } )
    @Column( name = "subject_type", nullable = false )
    private String subjectType;

    @Json( groups = {
            @Group( key = "subject_id" )
    } )
    @Column( name = "subject_id", nullable = false )
    private int subjectId;

    @Json( groups = {
            @Group( key = "subject_property" )
    } )
    @Column( name = "subject_property" )
    private String subjectProperty;

    @Json( groups = {
            @Group( key = "new_value" )
    } )
    @Column( name = "new_value", columnDefinition = "TEXT" )
    private String newValue;

    @Json( groups = {
            @Group( key = "log_type" )
    } )
    @Column( name = "log_type", nullable = false )
    private String logType;
    @Column( name = "ip_address" )
    private String ipAddress;
    private String uri;

    @Json( groups = {
            @Group( key = "created_at" )
    } )
    @Column( name = "created_at", nullable = false )
    private Timestamp createdAt;


    public History() {
        createdAt = new Timestamp( (new Date().getTime()) );
    }


    public long getId() {
        return id;
    }


    public void setId( final long id ) {
        this.id = id;
    }


    public String getAuthorType() {
        return authorType;
    }


    public void setAuthorType( final String authorType ) {
        this.authorType = authorType;
    }


    public int getAuthorId() {
        return authorId;
    }


    public void setAuthorId( final int authorId ) {
        this.authorId = authorId;
    }


    public String getSubjectType() {
        return subjectType;
    }


    public void setSubjectType( final String subjectType ) {
        this.subjectType = subjectType;
    }


    public int getSubjectId() {
        return subjectId;
    }


    public void setSubjectId( final int subjectId ) {
        this.subjectId = subjectId;
    }


    public String getSubjectProperty() {
        return subjectProperty;
    }


    public void setSubjectProperty( final String subjectProperty ) {
        this.subjectProperty = subjectProperty;
    }


    public String getNewValue() {
        return newValue;
    }


    public void setNewValue( final String newValue ) {
        this.newValue = newValue;
    }


    public String getLogType() {
        return logType;
    }


    public void setLogType( final String logType ) {
        this.logType = logType;
    }


    public String getIpAddress() {
        return ipAddress;
    }


    public void setIpAddress( final String ipAddress ) {
        this.ipAddress = ipAddress;
    }


    public String getUri() {
        return uri;
    }


    public History setUri( String uri ) {
        this.uri = uri;

        return this;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt( final Timestamp createdAt ) {
        this.createdAt = createdAt;
    }
}

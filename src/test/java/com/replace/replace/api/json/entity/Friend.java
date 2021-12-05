package com.replace.replace.api.json.entity;

import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

import javax.persistence.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Friend {

    @Json( groups = {
            @Group( name = "test1" )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Json( groups = {
            @Group( name = "test1" )
    } )
    private String name;

    @Json( groups = {
            @Group( name = "test1" )
    } )
    private byte level;

    @Json( groups = {
            @Group( name = "test1" )
    } )
    @ManyToOne
    private Person person;


    public long getId() {
        return id;
    }


    public Friend setId( long id ) {
        this.id = id;

        return this;
    }


    public String getName() {
        return name;
    }


    public Friend setName( String name ) {
        this.name = name;

        return this;
    }


    public byte getLevel() {
        return level;
    }


    public Friend setLevel( byte level ) {
        this.level = level;

        return this;
    }


    public Person getPerson() {
        return person;
    }


    public Friend setPerson( Person person ) {
        this.person = person;

        return this;
    }
}

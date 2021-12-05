package com.replace.replace.api.json.entity;

import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Person {

    @Json( groups = {
            @Group( name = "test1" ),
            @Group( name = "test2" )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Json( groups = {
            @Group( name = "test1" ),
            @Group( name = "test2" )
    } )
    private String name;

    @Json( groups = {
            @Group( name = "test1" ),
            @Group( name = "test2" )
    } )
    private double yearOfPerson;

    @Json( groups = {
            @Group( name = "test1" ),
            @Group( name = "test2", onlyId = false )
    } )
    @OneToMany( mappedBy = "person" )
    private List< Friend > friends;


    public Person() {
        friends = new ArrayList<>();
    }


    public long getId() {
        return id;
    }


    public Person setId( long id ) {
        this.id = id;

        return this;
    }


    public String getName() {
        return name;
    }


    public Person setName( String name ) {
        this.name = name;

        return this;
    }


    public double getYearOfPerson() {
        return yearOfPerson;
    }


    public Person setYearOfPerson( double yearOfPerson ) {
        this.yearOfPerson = yearOfPerson;

        return this;
    }


    public List< Friend > getFriends() {
        return friends;
    }


    public Person addFriend( Friend friend ) {
        this.friends.add( friend );

        return this;
    }
}

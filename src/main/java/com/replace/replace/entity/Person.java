package com.replace.replace.entity;

import com.replace.replace.api.dynamic.annotation.DynamicEnabled;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@DynamicEnabled( repository = null )
@Entity
public class Person {

    @Id
    private long id;

    private String name;

    private String phone;

    private int age;

    private byte status;


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


    public String getPhone() {
        return phone;
    }


    public Person setPhone( String phone ) {
        this.phone = phone;

        return this;
    }


    public int getAge() {
        return age;
    }


    public Person setAge( int age ) {
        this.age = age;

        return this;
    }


    public byte getStatus() {
        return status;
    }


    public Person setStatus( byte status ) {
        this.status = status;

        return this;
    }
}

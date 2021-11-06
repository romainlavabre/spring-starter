package com.replace.replace.entity;

import com.replace.replace.api.dynamic.annotation.DynamicEnabled;
import com.replace.replace.exception.HttpUnprocessableEntityException;
import com.replace.replace.repository.PersonRepository;

import javax.persistence.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@DynamicEnabled( repository = PersonRepository.class )
@Entity
public class Person {

    public static final byte STATUS_CREATED  = 0;
    public static final byte STATUS_ACCEPTED = 1;
    public static final byte STATUS_REFUSED  = 2;


    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Column( nullable = false )
    private String name;

    @Column( nullable = false )
    private String phone;

    @Column( nullable = false )
    private int age;

    @Column( nullable = false )
    private byte status;


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Person setName( String name ) {

        if ( name == null || name.isBlank() ) {
            throw new HttpUnprocessableEntityException( "PERSON_NAME_REQUIRED" );
        }

        this.name = name;

        return this;
    }


    public String getPhone() {
        return phone;
    }


    public Person setPhone( String phone ) {
        if ( phone == null || phone.isBlank() ) {
            throw new HttpUnprocessableEntityException( "PERSON_PHONE_REQUIRED" );
        }

        this.phone = phone;

        return this;
    }


    public int getAge() {
        return age;
    }


    public Person setAge( Integer age ) {
        if ( age == null ) {
            throw new HttpUnprocessableEntityException( "PERSON_AGE_REQUIRED" );
        }

        this.age = age;

        return this;
    }


    public byte getStatus() {
        return status;
    }


    public Person setStatus( Byte status ) {
        if ( status == null ) {
            throw new HttpUnprocessableEntityException( "PERSON_STATUS_REQUIRED" );
        }

        if ( !status.equals( STATUS_ACCEPTED )
                && !status.equals( STATUS_REFUSED ) ) {
            throw new HttpUnprocessableEntityException( "PERSON_STATUS_INVALID" );
        }

        this.status = status;

        return this;
    }
}

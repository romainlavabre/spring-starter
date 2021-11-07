package com.replace.replace.entity;

import com.replace.replace.api.dynamic.annotation.*;
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;
import com.replace.replace.configuration.dynamic.TriggerIdentifier;
import com.replace.replace.configuration.json.GroupType;
import com.replace.replace.exception.HttpUnprocessableEntityException;
import com.replace.replace.repository.FriendRepository;

import javax.persistence.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@DynamicEnabled( repository = FriendRepository.class )
@Entity
public class Friend {

    @EntryPoint(
            getOne = @GetOne( enabled = true ),
            getAllBy = {@GetAllBy( enabled = true, entity = Person.class )},
            post = {@Post( fields = {"name", "person"} )},
            delete = {@Delete},
            createTriggers = {
                    @CreateTrigger( id = TriggerIdentifier.ATTACH_FRIEND_TO_PERSON, fields = {"name"}, setByArray = false )
            }
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @EntryPoint(
            patch = {
                    @Patch
            }
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( nullable = false )
    private String name;

    @Json( groups = {
            @Group( name = GroupType.ADMIN, object = true )
    } )
    @ManyToOne( cascade = {CascadeType.PERSIST} )
    private Person person;


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Friend setName( String name ) {
        if ( name == null || name.isBlank() ) {
            throw new HttpUnprocessableEntityException( "FRIEND_NAME_REQUIRED" );
        }

        this.name = name;

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

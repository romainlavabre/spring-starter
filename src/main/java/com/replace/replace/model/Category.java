package com.replace.replace.model;

import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;
import com.replace.replace.exception.HttpUnprocessableEntityException;

import javax.persistence.*;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Category {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Json( groups = {
            @Group
    } )
    private int    id;
    @Json( groups = {
            @Group
    } )
    @Column( nullable = false )
    private String name;

    @Json( groups = {
            @Group( key = "products_id", object = true, onlyId = false )
    } )
    @OneToMany( cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "category", orphanRemoval = true )
    private List< Product > products;

    public int getId() {
        return this.id;
    }

    public void setId( final int id ) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName( final String name ) {
        if ( name == null || name.isEmpty() || name.isBlank() ) {
            throw new HttpUnprocessableEntityException( "Merci de renseigner un nom valide" );
        }

        this.name = name;
    }
}

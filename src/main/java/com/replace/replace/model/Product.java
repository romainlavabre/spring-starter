package com.replace.replace.model;

import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;
import com.replace.replace.api.json.formatter.ToUpperFormatter;
import com.replace.replace.exception.HttpUnprocessableEntityException;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Json( groups = {
            @Group
    } )
    private int id;

    @Json( groups = {
            @Group( formatter = ToUpperFormatter.class )
    } )
    @Column( nullable = false )
    private String name;

    @Json( groups = {
            @Group
    } )
    private double price;

    @Json( groups = {
            @Group
    } )
    @Column( name = "buy_price" )
    private double buyPrice;

    @Json( groups = {
            @Group( key = "category_id", object = true )
    } )
    @ManyToOne()
    @JoinColumn( nullable = false )
    private Category category;

    public Product() {
    }

    public int getId() {
        return this.id;
    }

    public void setId( final int id ) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName( final String name ) throws HttpUnprocessableEntityException {
        if ( name == null || name.isEmpty() || name.isBlank() ) {
            throw new HttpUnprocessableEntityException( "Merci de renseigner le nom du produit" );
        }

        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice( final Double price ) throws HttpUnprocessableEntityException {
        if ( price == null ) {
            throw new HttpUnprocessableEntityException( "Merci de renseigner le prix du produit" );
        }

        this.price = price;
    }

    public double getBuyPrice() {
        return this.buyPrice;
    }

    public void setBuyPrice( final Double buyPrice ) throws HttpUnprocessableEntityException {
        if ( buyPrice == null ) {
            throw new HttpUnprocessableEntityException( "Merci de renseigner le prix d'achat du produit" );
        }

        this.buyPrice = buyPrice;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory( final Category category ) {
        this.category = category;
    }
}

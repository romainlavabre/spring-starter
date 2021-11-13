package com.replace.replace.entity;

import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;
import com.replace.replace.api.top.annotation.*;
import com.replace.replace.configuration.dynamic.TriggerIdentifier;
import com.replace.replace.configuration.json.GroupType;
import com.replace.replace.exception.HttpUnprocessableEntityException;
import com.replace.replace.module.car.trigger.OwnerResolver;
import com.replace.replace.repository.CarRepository;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
@DynamicEnabled( repository = CarRepository.class )
public class Car {

    @EntryPoint(
            getOne = @GetOne( enabled = true ),
            getOneBy = {@GetOneBy( entity = Person.class )},
            post = {
                    @Post(
                            fields = {"mark", "power", "color", "creditPurchase", "person"},
                            triggers = {@Trigger( triggerId = TriggerIdentifier.CAR_RESOLVE_OWNER, provideMe = true )}
                    )
            },
            put = {
                    @Put(
                            fields = {"mark", "power", "color", "creditPurchase"},
                            triggers = {@Trigger( triggerId = TriggerIdentifier.CAR_RESOLVE_OWNER, provideMe = true )}
                    )
            },
            delete = {@Delete},
            createTriggers = {
                    @CreateTrigger(
                            id = TriggerIdentifier.ATTACH_CAR_TO_PERSON,
                            fields = {"mark", "power", "color", "creditPurchase"},
                            triggers = {@Trigger( triggerId = TriggerIdentifier.CAR_RESOLVE_OWNER, provideMe = true )}
                    )
            }
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @EntryPoint(
            patch = {@Patch}
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( nullable = false )
    private String mark;

    @EntryPoint(
            patch = {@Patch}
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( nullable = false )
    private float power;

    @EntryPoint(
            patch = {@Patch}
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( nullable = false )
    private String color;

    @EntryPoint(
            patch = {@Patch( triggers = {@Trigger( triggerId = TriggerIdentifier.CAR_RESOLVE_OWNER, provideMe = true )} )}
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( name = "credit_purchase", nullable = false )
    private boolean creditPurchase;

    @EntryPoint(
            unmanagedTriggers = {
                    @UnmanagedTrigger( id = TriggerIdentifier.CAR_RESOLVE_OWNER, updateExecutor = OwnerResolver.class )
            }
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( nullable = false )
    private boolean owner;

    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( name = "created_at", nullable = false )
    private final ZonedDateTime createdAt;

    @Json( groups = {
            @Group( name = GroupType.ADMIN, object = true )
    } )
    @OneToOne
    @JoinColumn( name = "person_id", nullable = false, unique = true )
    private Person person;


    public Car() {
        createdAt = ZonedDateTime.now( ZoneId.of( "UTC" ) );
    }


    public long getId() {
        return id;
    }


    public String getMark() {
        return mark;
    }


    public Car setMark( String mark ) {
        if ( mark == null || mark.isBlank() ) {
            throw new HttpUnprocessableEntityException( "CAR_MARK_REQUIRED" );
        }

        this.mark = mark;

        return this;
    }


    public float getPower() {
        return power;
    }


    public Car setPower( Float power ) {
        if ( power == null ) {
            throw new HttpUnprocessableEntityException( "CAR_POWER_REQUIRED" );
        }

        this.power = power;

        return this;
    }


    public String getColor() {
        return color;
    }


    public Car setColor( String color ) {

        if ( color == null || color.isBlank() ) {
            throw new HttpUnprocessableEntityException( "CAR_COLOR_REQUIRED" );
        }

        this.color = color;

        return this;
    }


    public boolean isCreditPurchase() {
        return creditPurchase;
    }


    public Car setCreditPurchase( Boolean creditPurchase ) {
        if ( creditPurchase == null ) {
            throw new HttpUnprocessableEntityException( "CAR_CREDIT_PURCHASE_REQUIRED" );
        }

        this.creditPurchase = creditPurchase;

        return this;
    }


    public boolean isOwner() {
        return owner;
    }


    public Car setOwner( Boolean owner ) {

        if ( owner == null ) {
            throw new HttpUnprocessableEntityException( "CAR_OWNER_REQUIRED" );
        }

        this.owner = owner;

        return this;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public Person getPerson() {
        return person;
    }


    public Car setPerson( Person person ) {
        this.person = person;

        if ( person.getCar() != this ) {
            person.setCar( this );
        }

        return this;
    }
}

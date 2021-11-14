package com.replace.replace.entity;

import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;
import com.replace.replace.api.poc.annotation.*;
import com.replace.replace.configuration.dynamic.TriggerIdentifier;
import com.replace.replace.configuration.json.GroupType;
import com.replace.replace.configuration.security.Role;
import com.replace.replace.exception.HttpUnprocessableEntityException;
import com.replace.replace.module.person.CategoryResolver;
import com.replace.replace.module.person.constraint.UniquePhone;
import com.replace.replace.repository.PersonRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@PocEnabled( repository = PersonRepository.class )
@Entity
public class Person {

    public static final byte STATUS_CREATED  = 0;
    public static final byte STATUS_ACCEPTED = 1;
    public static final byte STATUS_REFUSED  = 2;

    public static final byte CATEGORY_YOUNG  = 0;
    public static final byte CATEGORY_NORMAL = 1;
    public static final byte CATEGORY_OLD    = 2;

    @EntryPoint(
            getOne = @GetOne( enabled = true ),
            getAll = @GetAll( enabled = true, authenticated = false ),
            getOneBy = {@GetOneBy( entity = Friend.class ), @GetOneBy( entity = Car.class )},
            post = {
                    @Post(
                            fields = {"name", "phone", "age"},
                            triggers = {
                                    @Trigger( triggerId = TriggerIdentifier.ATTACH_FRIEND_TO_PERSON, attachToField = "friends" ),
                                    @Trigger( triggerId = TriggerIdentifier.ATTACH_CAR_TO_PERSON, attachToField = "car" ),
                                    @Trigger( triggerId = TriggerIdentifier.PERSON_CATEGORY, provideMe = true )
                            }
                    )
            },
            delete = {
                    @Delete( roles = {Role.ADMIN} )
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

    @Constraint( {UniquePhone.class} )
    @EntryPoint(
            patch = {
                    @Patch
            }
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( nullable = false, unique = true )
    private String phone;

    @EntryPoint(
            patch = {
                    @Patch( triggers = {
                            @Trigger( triggerId = TriggerIdentifier.PERSON_CATEGORY, provideMe = true )
                    } )
            }
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( nullable = false )
    private int age;

    @EntryPoint(
            patch = {
                    @Patch
            },
            updateTriggers = {
                    @UpdateTrigger( id = TriggerIdentifier.PERSON_STATUS, fields = {"status"} )
            }
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( nullable = false )
    private byte status;

    @EntryPoint(
            patch = {
                    @Patch
            },
            unmanagedTriggers = {
                    @UnmanagedTrigger(
                            id = TriggerIdentifier.PERSON_CATEGORY,
                            updateExecutor = CategoryResolver.class,
                            triggers = {@Trigger( triggerId = TriggerIdentifier.PERSON_STATUS, provideMe = true )}
                    )
            }
    )
    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Column( nullable = false )
    private byte category;

    @Json( groups = {
            @Group( name = GroupType.ADMIN, object = true )
    } )
    @OneToMany( mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.REMOVE} )
    private final List< Friend > friends;

    @Json( groups = {
            @Group( name = GroupType.ADMIN, object = true )
    } )
    @OneToOne( mappedBy = "person", cascade = {CascadeType.PERSIST} )
    private Car car;


    public Person() {
        friends = new ArrayList<>();
    }


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


    public byte getCategory() {
        return category;
    }


    public Person setCategory( Byte category ) {
        if ( category == null ) {
            throw new HttpUnprocessableEntityException( "PERSON_CATEGORY_REQUIRED" );
        }

        this.category = category;

        return this;
    }


    public List< Friend > getFriends() {
        return friends;
    }


    public Person addFriend( Friend friend ) {
        if ( !friends.contains( friend ) ) {
            friends.add( friend );

            if ( friend.getPerson() != this ) {
                friend.setPerson( this );
            }
        }

        return this;
    }


    public Car getCar() {
        return car;
    }


    public Person setCar( Car car ) {
        this.car = car;

        if ( car.getPerson() != this ) {
            car.setPerson( this );
        }

        return this;
    }
}

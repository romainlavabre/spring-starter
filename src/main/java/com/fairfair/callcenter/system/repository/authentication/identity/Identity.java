package com.fairfair.callcenter.system.repository.authentication.identity;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Identity {

    private long id;

    private String username;

    private String email;

    private String phone;

    private boolean twoFARequired;

    private boolean enabled;

    private String firstAuthenticationToken;

    private ZonedDateTime registeredAt;

    private ZonedDateTime lastLoginAt;

    private ZonedDateTime lastCheckAt;

    private Long password;

    private List< Long > attributes;

    private List< Long > roles;

    private Long twoFactorAuthentication;


    protected static Identity build( Map< String, Object > fields ) {
        Identity identity = new Identity();

        identity.id                       = Cast.getLong( fields.get( "id" ) );
        identity.email                    = ( String ) fields.get( "email" );
        identity.phone                    = ( String ) fields.get( "phone" );
        identity.enabled                  = Cast.getBoolean( fields.get( "enabled" ) );
        identity.username                 = ( String ) fields.get( "username" );
        identity.attributes               = ( List< Long > ) fields.get( "attributes_id" );
        identity.password                 = Cast.getLong( fields.get( "password_id" ) );
        identity.lastCheckAt              = fields.get( "last_check_at" ) != null ? ZonedDateTime.parse( fields.get( "last_check_at" ).toString() ) : null;
        identity.lastLoginAt              = fields.get( "last_login_at" ) != null ? ZonedDateTime.parse( fields.get( "last_login_at" ).toString() ) : null;
        identity.firstAuthenticationToken = ( String ) fields.get( "first_authentication_token_id" );
        identity.registeredAt             = fields.get( "registered_at" ) != null ? ZonedDateTime.parse( fields.get( "registered_at" ).toString() ) : null;
        identity.roles                    = ( List< Long > ) fields.get( "roles_id" );
        identity.twoFARequired            = Cast.getBoolean( fields.get( "two_f_a_required" ) );
        identity.twoFactorAuthentication  = Cast.getLong( fields.get( "two_factor_authentication_id" ) );

        return identity;
    }


    public long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }


    public String getPhone() {
        return phone;
    }


    public ZonedDateTime getLastCheckAt() {
        return lastCheckAt;
    }


    public ZonedDateTime getLastLoginAt() {
        return lastLoginAt;
    }


    public ZonedDateTime getRegisteredAt() {
        return registeredAt;
    }


    public String getFirstAuthenticationToken() {
        return firstAuthenticationToken;
    }


    public List< Long > getAttributes() {
        return attributes;
    }


    public List< Long > getRoles() {
        return roles;
    }


    public Long getPassword() {
        return password;
    }


    public Long getTwoFactorAuthentication() {
        return twoFactorAuthentication;
    }


    public boolean isEnabled() {
        return enabled;
    }


    public boolean isTwoFARequired() {
        return twoFARequired;
    }
}


package com.replace.replace.controller;

import com.replace.replace.api.environment.Environment;
import com.replace.replace.api.environment.EnvironmentVariable;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.security.AuthenticationHandler;
import com.replace.replace.api.security.JwtTokenHandler;
import com.replace.replace.api.security.Security;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController
public class SecurityController {

    protected JwtTokenHandler       jwtTokenHandler;
    protected AuthenticationHandler authenticationHandler;
    protected Request               request;
    protected UserDetailsService    userDetailsService;
    protected Environment           environment;
    protected Security              security;

    public SecurityController(
            final JwtTokenHandler jwtTokenHandler,
            final AuthenticationHandler authenticationHandler,
            final Request request,
            final Environment environment,
            final Security security,
            @Qualifier( "userDetailsService" ) final UserDetailsService userDetailsService ) {
        this.jwtTokenHandler       = jwtTokenHandler;
        this.authenticationHandler = authenticationHandler;
        this.request               = request;
        this.environment           = environment;
        this.userDetailsService    = userDetailsService;
        this.security              = security;
    }

    @PostMapping( path = "/auth" )
    public ResponseEntity< Object > authenticate() {

        Authentication authentication = null;
        String         message        = null;

        try {
            authentication = this.authenticationHandler.authenticate( this.request );
        } catch ( final Throwable e ) {
            message = e.getMessage();
        }

        if ( authentication != null && authentication.isAuthenticated() ) {
            return ResponseEntity.ok().body( Map.of(
                    "access_token", this.jwtTokenHandler.createToken( this.userDetailsService.loadUserByUsername( ( String ) this.request.getParameter( "auth_username" ) ) ),
                    "token_type", "Bearer",
                    "expire_in", Integer.valueOf( this.environment.getEnv( EnvironmentVariable.JWT_LIFE_TIME ) )
            ) );
        }

        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( Map.of( "message", message ) );
    }

    @GetMapping( path = "/info" )
    public ResponseEntity< Map< String, Object > > userInfo() {

        return ResponseEntity.ok(
                Map.of(
                        "id", this.security.getId(),
                        "username", this.security.getUsername(),
                        "roles", this.security.getRoles()
                )
        );
    }
}

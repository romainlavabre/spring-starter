package com.fairfair.callcenter.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ClientIpFilter extends OncePerRequestFilter {

    public static final Map< String, Map< String, Object > > BAD_IP_DETECTED = new HashMap<>();


    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException {
        String fullAccessToken = request.getHeader( "Authorization" );

        if ( fullAccessToken == null || !fullAccessToken.contains( "Bearer" ) ) {
            filterChain.doFilter( request, response );
            return;
        }

        fullAccessToken = fullAccessToken.split( "\\." )[ 1 ];

        Map< String, Object > body = new ObjectMapper().readValue( new String( Base64.getDecoder().decode( fullAccessToken ), StandardCharsets.UTF_8 ), Map.class );

        if ( body.get( "cip" ) != null
                && !body.get( "cip" ).equals( request.getHeader( "Cf-Connecting-Ip" ) ) ) {

            ZonedDateTime now = ZonedDateTime.now( ZoneOffset.UTC );

            String uniqueId = body.get( "preferred_username" ) + String.valueOf( now.getYear() ) + now.getMonth() + now.getDayOfMonth() + now.getMinute() + now.getSecond();

            Map< String, Object > badIpDetected = new HashMap<>();
            badIpDetected.put( "username", body.get( "preferred_username" ) );
            badIpDetected.put( "token_ip", body.get( "cip" ) );
            badIpDetected.put( "current_ip", request.getHeader( "Cf-Connecting-Ip" ) );
            badIpDetected.put( "at", ZonedDateTime.now( ZoneOffset.UTC ) );

            BAD_IP_DETECTED.putIfAbsent( uniqueId, badIpDetected );

            response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "INVALID_JWT_TOKEN" );
            return;
        }

        filterChain.doFilter( request, response );
    }
}

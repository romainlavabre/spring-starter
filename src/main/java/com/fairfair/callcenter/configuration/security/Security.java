package com.fairfair.callcenter.configuration.security;

import com.fairfair.callcenter.api.security.ClientIpFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Configuration
@EnableWebSecurity
public class Security {
    private static final String   TOKEN_ROLE_CLAIM = "roles";
    private static final String[] PUBLIC_ENDPOINT  = {
            "/api/documentation/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/guest/**",
            "/actuator/health"
    };


    @Bean
    public SecurityFilterChain filterChain( final HttpSecurity http ) throws Exception {
        http
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                .and()
                .anonymous()
                .and()
                .authorizeHttpRequests()
                .dispatcherTypeMatchers( DispatcherType.ERROR ).permitAll()
                .requestMatchers( HttpMethod.OPTIONS ).permitAll()
                .requestMatchers( PUBLIC_ENDPOINT ).permitAll()
                .requestMatchers( "/admin/**" ).hasRole( new SecurityRole( Role.ROLE_ADMIN ).toString() )
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer( OAuth2ResourceServerConfigurer::jwt )
                .addFilterAfter( new ClientIpFilter(), BasicAuthenticationFilter.class );

        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName( TOKEN_ROLE_CLAIM );
        grantedAuthoritiesConverter.setAuthorityPrefix( "" );

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter( grantedAuthoritiesConverter );
        return jwtAuthenticationConverter;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins( List.of( "*" ) );
        configuration.setAllowedMethods( List.of( "HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH" ) );
        configuration.setAllowCredentials( false );
        configuration.setAllowedHeaders( List.of( "Authorization", "Cache-Control", "Content-Type" ) );

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", configuration );
        return source;
    }


    private class SecurityRole {
        private final String ROLE;


        public SecurityRole( String role ) {
            ROLE = role;
        }


        @Override
        public String toString() {
            return ROLE.replace( "ROLE_", "" );
        }
    }
}

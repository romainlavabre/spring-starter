package com.fairfair.callcenter.system.event;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.configuration.environment.Variable;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 * <p>
 * WARNING: This class build event for the <strong>system</strong>
 */
public interface EventBuilder {

    /**
     * @param eventName Event name
     * @param entity    Entity to parse (in request payload)
     * @param timeout   Time before crash
     */
    void build( String eventName, Object entity, long timeout );


    /**
     * Default time out is 1 hours
     *
     * @param eventName Event name
     * @param entity    Entity to parse (in request payload)
     */
    void build( String eventName, Object entity );


    /**
     * @param environment
     * @param entity      Subject of event
     * @param suffix      Name of event
     * @return SERVICE_{NAME}_{ENTITY_SNACK_CASE}_{SUFFIX}
     */
    static String getEventName( Environment environment, Object entity, String suffix ) {
        String event = environment.getEnv( Variable.APPLICATION_NAME ).replace( "-", "_" ).toUpperCase();

        char[] letters = entity.getClass().getSimpleName().toCharArray();

        StringBuilder entityName = new StringBuilder();

        for ( int i = 0; i < letters.length; i++ ) {
            String letter = String.valueOf( letters[ i ] );

            if ( i > 0 && letter.matches( "[A-Z]" ) ) {
                entityName.append( "_" )
                        .append( letter.toUpperCase() );
                continue;
            }

            entityName.append( letter.toUpperCase() );
        }

        return "EVENT::" + event + "_" + entityName.toString().split( "\\$" )[ 0 ] + suffix;
    }
}

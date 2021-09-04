package com.replace.replace.api.pagination.query;

import com.replace.replace.api.pagination.Constraint;
import com.replace.replace.api.pagination.condition.Condition;
import com.replace.replace.api.pagination.exception.NotSupportedKey;
import com.replace.replace.api.pagination.exception.NotSupportedOperator;
import com.replace.replace.api.request.Request;

import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class QueryBuilder {

    public static Query build( final Request request, final List< Condition > conditions, final String view )
            throws NotSupportedKey, NotSupportedOperator {
        final Query query = new Query();

        final StringBuilder sqlQuery = new StringBuilder( "SELECT {SELECTED} FROM " + view + " WHERE" );

        for ( int i = 0; i < conditions.size(); i++ ) {
            final Condition condition = conditions.get( i );

            if ( i == 0 ) {
                sqlQuery.append( " " );
            } else {
                sqlQuery.append( " AND " );
            }

            sqlQuery.append( condition.consume( i + 1 ) );

            for ( final Map.Entry< String, String > entry : condition.getParameters().entrySet() ) {
                query.addParameter( entry.getKey(), entry.getValue() );
            }
        }

        final String orderBy = request.getQueryString( "sortBy" );
        Constraint.assertValidKey( orderBy );
        query.setCountQuery( sqlQuery.toString().replace( "{SELECTED}", "COUNT(" + orderBy + ") AS total" ) );
        final String order = request.getQueryString( "asc" ) != null ? "ASC" : "DESC";


        sqlQuery.append( " ORDER BY " )
                .append( orderBy )
                .append( " " )
                .append( order )
                .append( " LIMIT :limit OFFSET :offset" )
                .append( ";" );

        query.setDataQuery( sqlQuery.toString().replace( "{SELECTED}", "*" ) );

        return query;
    }
}

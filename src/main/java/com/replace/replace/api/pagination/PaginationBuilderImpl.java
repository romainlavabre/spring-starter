package com.replace.replace.api.pagination;

import com.replace.replace.api.pagination.condition.Condition;
import com.replace.replace.api.pagination.condition.ConditionBuilder;
import com.replace.replace.api.pagination.exception.NotSupportedKey;
import com.replace.replace.api.pagination.exception.NotSupportedOperator;
import com.replace.replace.api.pagination.query.Query;
import com.replace.replace.api.pagination.query.QueryBuilder;
import com.replace.replace.api.request.Request;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class PaginationBuilderImpl implements PaginationBuilder {

    protected final EntityManager entityManager;


    public PaginationBuilderImpl( final EntityManager entityManager ) {
        this.entityManager = entityManager;
    }


    @Override
    public < T extends AbstractDto > Pagination getResult( final Request request, final Class< T > dtoType, final String view )
            throws NotSupportedOperator, NotSupportedKey, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final List< Condition > conditions = ConditionBuilder.getConditions( request );
        final Query             query      = QueryBuilder.build( request, conditions, view );

        final int perPage = request.getQueryString( "per_page" ) != null ? Integer.parseInt( request.getQueryString( "per_page" ) ) : 20;
        final int page    = request.getQueryString( "page" ) != null ? Integer.parseInt( request.getQueryString( "page" ) ) : 1;

        final Pagination pagination = new Pagination();
        pagination.setTotal( this.executeCountQuery( query ) )
                  .setFrom( this.getOffset( perPage, page ) )
                  .setTo( pagination.getFrom() + perPage )
                  .setCurrentPage( page )
                  .setLastPage( this.getLastPage( perPage, pagination.getTotal() ) );

        query.setOffset( pagination.getFrom() );
        query.setLimit( pagination.getTo() );

        pagination.setData( this.executeDataQuery( query, ( Class< AbstractDto > ) dtoType ) );


        return pagination;
    }


    protected < T extends AbstractDto > List< T > executeDataQuery( final Query query, final Class< T > type )
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final javax.persistence.Query persistentQuery = this.entityManager.createNativeQuery( query.getDataQuery() );

        for ( final Map.Entry< String, String > entry : query.getParameters().entrySet() ) {
            persistentQuery.setParameter( entry.getKey(), entry.getValue() );
        }

        final List< T > result = new ArrayList<>();

        for ( final Map< String, Object > data : ( List< Map< String, Object > > ) persistentQuery.getResultList() ) {
            final Constructor< T > constructor = type.getConstructor( type.getClass() );
            final T                instance    = constructor.newInstance();

            instance.hydrate( data );
        }

        return result;
    }


    protected int executeCountQuery( final Query query )
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final javax.persistence.Query persistentQuery = this.entityManager.createNativeQuery( query.getDataQuery() );

        for ( final Map.Entry< String, String > entry : query.getParameters().entrySet() ) {
            persistentQuery.setParameter( entry.getKey(), entry.getValue() );
        }

        final Map< String, Object > result = ( Map< String, Object > ) persistentQuery.getResultList().get( 0 );

        return Integer.parseInt( result.get( "total" ).toString() );
    }


    protected int getOffset( final int perPage, final int currentPage ) {
        return perPage * currentPage;
    }


    protected int getLastPage( final int perPage, final long totalLines ) {
        final int totalPage = ( int ) ((totalLines - 1) / perPage);
        final int modulo    = (totalPage - 1) % perPage;

        if ( modulo > 0 ) {
            return totalPage + 1;
        }

        return totalPage;
    }
}

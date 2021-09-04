package com.replace.replace.api.pagination;

import com.replace.replace.api.pagination.exception.NotSupportedKey;
import com.replace.replace.api.pagination.exception.NotSupportedOperator;
import com.replace.replace.api.request.Request;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface PaginationBuilder {
    < T extends AbstractDto > Pagination getResult( Request request, Class< T > dtoType, String view )
            throws NotSupportedOperator, NotSupportedKey, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}

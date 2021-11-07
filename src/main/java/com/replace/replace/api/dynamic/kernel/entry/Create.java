package com.replace.replace.api.dynamic.kernel.entry;

import com.replace.replace.api.dynamic.api.CreateEntry;
import com.replace.replace.api.dynamic.kernel.entity.EntityHandler;
import com.replace.replace.api.dynamic.kernel.router.RouteHandler;
import com.replace.replace.api.dynamic.kernel.setter.SetterHandler;
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.repository.DefaultRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Create implements CreateEntry {


    protected final HistoryHandler historyHandler;


    public Create( HistoryHandler historyHandler ) {
        this.historyHandler = historyHandler;
    }


    @Override
    public void create( Request request, Object subject, RouteHandler.Route route )
            throws Throwable {

        for ( SetterHandler.Setter setter : route.getSetters() ) {

            try {
                setter.invoke( request, subject );
            } catch ( InvocationTargetException e ) {
                throw e.getCause();
            }
        }

        historyHandler.create( subject );

        DefaultRepository defaultRepository = EntityHandler.getEntity( route.getSubject() ).getDefaultRepository();

        defaultRepository.persist( subject );
    }
}

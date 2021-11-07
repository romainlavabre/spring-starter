package com.replace.replace.api.dynamic.kernel.entry;

import com.replace.replace.api.dynamic.api.UpdateEntry;
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
public class Update implements UpdateEntry {

    protected final HistoryHandler historyHandler;


    public Update( HistoryHandler historyHandler ) {
        this.historyHandler = historyHandler;
    }


    @Override
    public void update( Request request, Object subject, RouteHandler.Route route ) throws Throwable {
        for ( SetterHandler.Setter setter : route.getSetters() ) {

            try {
                setter.invoke( request, subject );
                historyHandler.update( subject, setter.getField().getName() );
            } catch ( InvocationTargetException e ) {
                throw e.getCause();
            }
        }


        DefaultRepository defaultRepository = EntityHandler.getEntity( route.getSubject() ).getDefaultRepository();

        defaultRepository.persist( subject );
    }
}

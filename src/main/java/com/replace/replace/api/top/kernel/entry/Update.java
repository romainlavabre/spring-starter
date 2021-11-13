package com.replace.replace.api.top.kernel.entry;

import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.top.kernel.entity.EntityHandler;
import com.replace.replace.api.top.kernel.router.RouteHandler;
import com.replace.replace.api.top.kernel.setter.SetterHandler;
import com.replace.replace.api.top.kernel.trigger.TriggerHandler;
import com.replace.replace.repository.DefaultRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Update implements UpdateEntry {

    protected final  HistoryHandler historyHandler;
    protected final  TriggerHandler triggerHandler;
    protected static Update         instance;


    public Update( HistoryHandler historyHandler, TriggerHandler triggerHandler ) {
        this.historyHandler = historyHandler;
        this.triggerHandler = triggerHandler;
        instance            = this;
    }


    @Override
    public void update( Request request, Object subject, RouteHandler.Route route ) throws Throwable {
        update( request, subject, route.getSetters(), route.getTriggers() );
    }


    protected void update( Request request, Object subject, List< SetterHandler.Setter > setters, List< com.replace.replace.api.top.annotation.Trigger > nextTriggers )
            throws Throwable {

        update( request, subject, setters, nextTriggers, null );
    }


    protected void update( Request request, Object subject, List< SetterHandler.Setter > setters, List< com.replace.replace.api.top.annotation.Trigger > nextTriggers, Object executor )
            throws Throwable {

        if ( executor != null ) {
            (( com.replace.replace.api.crud.Update ) executor).update( request, subject );
        } else {
            for ( SetterHandler.Setter setter : setters ) {

                try {
                    setter.invoke( request, subject );
                } catch ( InvocationTargetException e ) {
                    throw e.getCause();
                }

                historyHandler.update( subject, setter.getField().getName() );
            }
        }


        Trigger.getInstance().handleTriggers( request, nextTriggers, subject );

        DefaultRepository defaultRepository = EntityHandler.getEntity( subject.getClass() ).getDefaultRepository();

        defaultRepository.persist( subject );
    }
}

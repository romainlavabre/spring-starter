package com.replace.replace.api.poc.kernel.entry;

import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.poc.kernel.entity.EntityHandler;
import com.replace.replace.api.poc.kernel.router.RouteHandler;
import com.replace.replace.api.poc.kernel.setter.SetterHandler;
import com.replace.replace.api.poc.kernel.trigger.TriggerHandler;
import com.replace.replace.api.request.Request;
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
    protected final  EntityHandler  entityHandler;
    protected static Update         instance;


    public Update(
            HistoryHandler historyHandler,
            TriggerHandler triggerHandler,
            EntityHandler entityHandler ) {
        this.historyHandler = historyHandler;
        this.triggerHandler = triggerHandler;
        this.entityHandler  = entityHandler;
        instance            = this;
    }


    @Override
    public void update( Request request, Object subject, RouteHandler.Route route ) throws Throwable {
        update( request, subject, route.getSetters(), route.getTriggers() );
    }


    protected void update( Request request, Object subject, List< SetterHandler.Setter > setters, List< com.replace.replace.api.poc.annotation.Trigger > nextTriggers )
            throws Throwable {

        update( request, subject, setters, nextTriggers, null );
    }


    protected void update( Request request, Object subject, List< SetterHandler.Setter > setters, List< com.replace.replace.api.poc.annotation.Trigger > nextTriggers, Object executor )
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

        DefaultRepository defaultRepository = entityHandler.getEntity( subject.getClass() ).getDefaultRepository();

        defaultRepository.persist( subject );
    }
}

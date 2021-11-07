package com.replace.replace.api.dynamic.kernel.entry;

import com.replace.replace.api.dynamic.kernel.entity.EntityHandler;
import com.replace.replace.api.dynamic.kernel.router.RouteHandler;
import com.replace.replace.api.dynamic.kernel.setter.SetterHandler;
import com.replace.replace.api.dynamic.kernel.trigger.TriggerHandler;
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.repository.DefaultRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Create implements CreateEntry {


    protected final  HistoryHandler historyHandler;
    protected final  TriggerHandler triggerHandler;
    protected final  SetterHandler  setterHandler;
    protected static Create         instance;


    public Create(
            HistoryHandler historyHandler,
            TriggerHandler triggerHandler,
            SetterHandler setterHandler ) {
        this.historyHandler = historyHandler;
        this.triggerHandler = triggerHandler;
        this.setterHandler  = setterHandler;
        instance            = this;
    }


    @Override
    public void create( Request request, Object subject, RouteHandler.Route route )
            throws Throwable {

        create( request, subject, route.getSetters(), route.getTriggers() );
    }


    protected void create( Request request, Object subject, List< SetterHandler.Setter > setters, List< com.replace.replace.api.dynamic.annotation.Trigger > nextTriggers )
            throws Throwable {

        create( request, subject, setters, nextTriggers, null );
    }


    protected void create( Request request, Object subject, List< SetterHandler.Setter > setters, List< com.replace.replace.api.dynamic.annotation.Trigger > nextTriggers, Object executor )
            throws Throwable {

        if ( executor != null ) {
            (( com.replace.replace.api.crud.Create ) executor).create( request, subject );
        } else {
            for ( SetterHandler.Setter setter : setters ) {

                try {
                    setter.invoke( request, subject );
                } catch ( InvocationTargetException e ) {
                    throw e.getCause();
                }
            }
            
            historyHandler.create( subject );
        }


        Trigger.getInstance().handleTriggers( request, nextTriggers, subject );

        DefaultRepository defaultRepository = EntityHandler.getEntity( subject.getClass() ).getDefaultRepository();

        defaultRepository.persist( subject );
    }
}

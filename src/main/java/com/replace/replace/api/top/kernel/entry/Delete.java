package com.replace.replace.api.top.kernel.entry;

import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.top.kernel.entity.EntityHandler;
import com.replace.replace.api.top.kernel.router.RouteHandler;
import com.replace.replace.repository.DefaultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Delete implements DeleteEntry {

    protected final HistoryHandler historyHandler;


    public Delete( HistoryHandler historyHandler ) {
        this.historyHandler = historyHandler;
    }


    @Override
    public void delete( Request request, Object subject, RouteHandler.Route route )
            throws Throwable {
        delete( request, subject, route.getTriggers() );
    }


    protected void delete( Request request, Object subject, List< com.replace.replace.api.top.annotation.Trigger > nextTriggers )
            throws Throwable {
        delete( request, subject, nextTriggers, null );
    }


    protected void delete( Request request, Object subject, List< com.replace.replace.api.top.annotation.Trigger > nextTriggers, Object executor )
            throws Throwable {

        if ( executor != null ) {
            (( com.replace.replace.api.crud.Delete ) executor).delete( request, subject );
        } else {
            EntityHandler.Entity entity = EntityHandler.getEntity( subject.getClass() );
            historyHandler.delete( subject );

            DefaultRepository defaultRepository = entity.getDefaultRepository();
            defaultRepository.remove( subject );
        }


        Trigger.getInstance().handleTriggers( request, nextTriggers, subject );
    }
}

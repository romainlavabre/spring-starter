package com.replace.replace.api.dynamic.kernel.entry;

import com.replace.replace.api.dynamic.api.DeleteEntry;
import com.replace.replace.api.dynamic.kernel.entity.EntityHandler;
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.repository.DefaultRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Delete implements DeleteEntry {

    protected final ApplicationContext applicationContext;
    protected final HistoryHandler     historyHandler;


    public Delete( ApplicationContext applicationContext, HistoryHandler historyHandler ) {
        this.applicationContext = applicationContext;
        this.historyHandler     = historyHandler;
    }


    @Override
    public void delete( Request request, Object subject ) {
        EntityHandler.Entity entity = EntityHandler.getEntity( subject.getClass() );

        DefaultRepository defaultRepository = applicationContext.getBean( entity.getRepository() );

        defaultRepository.remove( subject );

        historyHandler.delete( subject );
    }
}

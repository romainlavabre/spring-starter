package com.fairfair.callcenter.system.action;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.context.annotation.RequestScope;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "ActionRequestScope" )
@RequestScope
public class OnTransactionSuccess {
    protected final List< Action >     ACTIONS;
    protected final ActionRepository   actionRepository;
    protected final ApplicationContext applicationContext;
    protected final TaskExecutor       taskExecutor;
    protected       boolean            CLEARED = false;


    public OnTransactionSuccess(
            ActionRepository actionRepository,
            ApplicationContext applicationContext,
            @Qualifier( "taskExecutorAction" ) TaskExecutor taskExecutor ) {
        this.actionRepository   = actionRepository;
        this.applicationContext = applicationContext;
        this.taskExecutor       = taskExecutor;
        ACTIONS                 = new ArrayList<>();
    }


    @TransactionalEventListener( phase = TransactionPhase.AFTER_COMMIT )
    public void onTransactionSuccess( ApplicationEvent unused ) {
        if ( ACTIONS.size() == 0 ) {
            return;
        }

        MessageBrokerThread messageBrokerThread = applicationContext.getBean( MessageBrokerThread.class );

        List< Long > ids = new ArrayList<>();

        for ( Action action : ACTIONS ) {
            ids.add( action.getId() );
        }

        messageBrokerThread.setActions( ids );

        taskExecutor.execute( messageBrokerThread );
    }


    public void put( Action action ) {
        ACTIONS.add( action );

        ZonedDateTime now = ZonedDateTime.now( ZoneOffset.UTC );

        if ( !CLEARED && now.getMinute() >= 0 && now.getMinute() <= 5 && now.getSecond() >= 0 && now.getSecond() <= 10 ) {
            try {
                actionRepository.deleteByRemovableIsTrue();
                actionRepository.deleteByTreatedIsTrueAndRemovableIsFalseAndCreatedAtIsLessThan( ZonedDateTime.now( ZoneId.of( "UTC" ) ).minusHours( 24 ) );
            } catch ( Throwable ignored ) {

            } finally {
                CLEARED = true;
            }
        }
    }
}

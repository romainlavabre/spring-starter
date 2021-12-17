package com.replace.replace.api.poc.kernel.entry;

import com.replace.replace.api.poc.annotation.CreateTrigger;
import com.replace.replace.api.poc.annotation.DeleteTrigger;
import com.replace.replace.api.poc.annotation.UnmanagedTrigger;
import com.replace.replace.api.poc.annotation.UpdateTrigger;
import com.replace.replace.api.poc.kernel.setter.SetterHandler;
import com.replace.replace.api.poc.kernel.trigger.TriggerHandler;
import com.replace.replace.api.request.Request;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Trigger {

    protected final Create             create;
    protected final Update             update;
    protected final Delete             delete;
    protected final SetterHandler      setterHandler;
    protected final TriggerHandler     triggerHandler;
    protected final ApplicationContext applicationContext;
    private static  Trigger            instance;


    public Trigger(
            Create create,
            Update update,
            Delete delete,
            SetterHandler setterHandler,
            TriggerHandler triggerHandler,
            ApplicationContext applicationContext ) {
        this.create             = create;
        this.update             = update;
        this.delete             = delete;
        this.setterHandler      = setterHandler;
        this.triggerHandler     = triggerHandler;
        this.applicationContext = applicationContext;
        instance                = this;
    }


    protected void handleCreateTrigger( Request request, TriggerHandler.Trigger targetTrigger, com.replace.replace.api.poc.annotation.Trigger trigger, Object lastSubject )
            throws Throwable {

        SetterHandler.Setter lastSubjectRelationSetter = null;

        if ( !trigger.attachToField().isBlank() ) {
            lastSubjectRelationSetter = instance.setterHandler.toSetter( lastSubject.getClass().getDeclaredField( trigger.attachToField() ) );
        }

        if ( lastSubjectRelationSetter != null ) {
            if ( !(( CreateTrigger ) targetTrigger.getTrigger()).setByArray()
                    || lastSubjectRelationSetter.getField().getType().isPrimitive()
                    || !Collection.class.isAssignableFrom( lastSubjectRelationSetter.getField().getType() ) ) {
                Object newInstance = targetTrigger.getSubject().getDeclaredConstructor().newInstance();
                create.create( request, newInstance, targetTrigger.getSetters(), targetTrigger.getTriggers(), targetTrigger.getExecutor() );

                if ( !trigger.attachToField().isBlank() ) {
                    lastSubjectRelationSetter.invokeWithValue( lastSubject, newInstance );
                }

                return;
            }
        }

        Map< String, List< Object > > parameters = new HashMap<>();

        for ( SetterHandler.Setter setter : targetTrigger.getSetters() ) {
            parameters.put( setter.getRequestParameter(), request.getParameters( setter.getRequestParameter() ) );
        }

        if ( parameters.size() == 0
                || (targetTrigger.getSetters() != null && targetTrigger.getSetters().size() == 0)
                || parameters.get( targetTrigger.getSetters().get( 0 ).getRequestParameter() ) == null ) {
            if ( trigger.optional() ) {
                return;
            } else {
                Object newInstance = targetTrigger.getSubject().getDeclaredConstructor().newInstance();
                create.create( request, newInstance, targetTrigger.getSetters(), targetTrigger.getTriggers(), targetTrigger.getExecutor() );

                if ( !trigger.attachToField().isBlank() ) {
                    lastSubjectRelationSetter.invokeWithValue( lastSubject, newInstance );
                }
            }
        }

        for ( int i = 0; i < parameters.get( targetTrigger.getSetters().get( 0 ).getRequestParameter() ).size(); i++ ) {
            for ( Map.Entry< String, List< Object > > entry : parameters.entrySet() ) {
                request.setParameter( entry.getKey(), entry.getValue().get( i ) );
            }

            Object newInstance = targetTrigger.getSubject().getDeclaredConstructor().newInstance();
            create.create( request, newInstance, targetTrigger.getSetters(), targetTrigger.getTriggers(), targetTrigger.getExecutor() );

            if ( !trigger.attachToField().isBlank() ) {
                lastSubjectRelationSetter.invokeWithValue( lastSubject, newInstance );
            }
        }
    }


    protected void handleUpdateTrigger( Request request, TriggerHandler.Trigger targetTrigger, com.replace.replace.api.poc.annotation.Trigger trigger, Object lastSubject )
            throws Throwable {
        if ( trigger.provideMe() ) {
            update.update( request, lastSubject, targetTrigger.getSetters(), targetTrigger.getTriggers(), targetTrigger.getExecutor() );
            return;
        }

        if ( !trigger.providerField().isBlank() ) {
            Field field = lastSubject.getClass().getDeclaredField( trigger.providerField() );

            if ( field.getType().isArray() || field.getType().isAssignableFrom( Collection.class ) ) {
                List< Object > entities = ( List< Object > ) field.get( lastSubject );

                if ( entities == null ) {
                    return;
                }

                for ( Object entity : entities ) {
                    update.update( request, entity, targetTrigger.getSetters(), targetTrigger.getTriggers(), targetTrigger.getExecutor() );
                }
            } else {
                Object entity = field.get( lastSubject );

                if ( entity == null ) {
                    return;
                }

                update.update( request, entity, targetTrigger.getSetters(), targetTrigger.getTriggers(), targetTrigger.getExecutor() );
            }

            return;
        }

        for ( Object entity : applicationContext.getBean( trigger.customProvider() ).getResources( lastSubject ) ) {
            update.update( request, entity, targetTrigger.getSetters(), targetTrigger.getTriggers(), targetTrigger.getExecutor() );
        }
    }


    protected void handleDeleteTrigger( Request request, TriggerHandler.Trigger targetTrigger, com.replace.replace.api.poc.annotation.Trigger trigger, Object lastSubject )
            throws Throwable {

        if ( trigger.provideMe() ) {
            delete.delete( request, lastSubject, targetTrigger.getTriggers(), targetTrigger.getExecutor() );
            return;
        }

        if ( !trigger.providerField().isBlank() ) {
            Field field = lastSubject.getClass().getDeclaredField( trigger.providerField() );

            if ( field.getType().isArray() || field.getType().isAssignableFrom( Collection.class ) ) {
                List< Object > entities = ( List< Object > ) field.get( lastSubject );

                if ( entities == null ) {
                    return;
                }

                for ( Object entity : entities ) {
                    delete.delete( request, entity, targetTrigger.getTriggers(), targetTrigger.getExecutor() );
                }
            } else {
                Object entity = field.get( lastSubject );

                if ( entity == null ) {
                    return;
                }

                delete.delete( request, entity, targetTrigger.getTriggers(), targetTrigger.getExecutor() );
            }

            return;
        }

        for ( Object entity : applicationContext.getBean( trigger.customProvider() ).getResources( lastSubject ) ) {
            delete.delete( request, entity, targetTrigger.getTriggers(), targetTrigger.getExecutor() );
        }
    }


    protected void handleUnmanagedTrigger( Request request, TriggerHandler.Trigger targetTrigger, com.replace.replace.api.poc.annotation.Trigger trigger, Object lastSubject )
            throws Throwable {

        if ( trigger.provideMe() ) {
            (( com.replace.replace.api.poc.api.UnmanagedTrigger ) targetTrigger.getExecutor()).handle( request, lastSubject );
            handleTriggers( request, targetTrigger.getTriggers(), lastSubject );
            return;
        }

        if ( !trigger.providerField().isBlank() ) {
            Field field = lastSubject.getClass().getDeclaredField( trigger.providerField() );

            if ( field.getType().isArray() || field.getType().isAssignableFrom( Collection.class ) ) {
                List< Object > entities = ( List< Object > ) field.get( lastSubject );

                if ( entities == null ) {
                    return;
                }

                for ( Object entity : entities ) {
                    (( com.replace.replace.api.poc.api.UnmanagedTrigger ) targetTrigger.getExecutor()).handle( request, entity );
                    handleTriggers( request, targetTrigger.getTriggers(), entity );
                }
            } else {
                Object entity = field.get( lastSubject );

                if ( entity == null ) {
                    return;
                }

                (( com.replace.replace.api.poc.api.UnmanagedTrigger ) targetTrigger.getExecutor()).handle( request, entity );
                handleTriggers( request, targetTrigger.getTriggers(), entity );
            }

            return;
        }

        for ( Object entity : applicationContext.getBean( trigger.customProvider() ).getResources( lastSubject ) ) {
            (( com.replace.replace.api.poc.api.UnmanagedTrigger ) targetTrigger.getExecutor()).handle( request, entity );
            handleTriggers( request, targetTrigger.getTriggers(), entity );
        }
    }


    protected void handleTriggers( Request request, List< com.replace.replace.api.poc.annotation.Trigger > triggers, Object lastSubject ) throws Throwable {
        for ( com.replace.replace.api.poc.annotation.Trigger trigger : triggers ) {
            TriggerHandler.Trigger target = triggerHandler.getTrigger( trigger.triggerId() );

            if ( target.getTriggerType().isAssignableFrom( CreateTrigger.class ) ) {
                handleCreateTrigger( request, target, trigger, lastSubject );
            }

            if ( target.getTriggerType().isAssignableFrom( UpdateTrigger.class ) ) {
                handleUpdateTrigger( request, target, trigger, lastSubject );
            }

            if ( target.getTriggerType().isAssignableFrom( DeleteTrigger.class ) ) {
                handleDeleteTrigger( request, target, trigger, lastSubject );
            }

            if ( target.getTriggerType().isAssignableFrom( UnmanagedTrigger.class ) ) {
                if ( target.isCreateExecutor() ) {
                    handleCreateTrigger( request, target, trigger, lastSubject );
                } else if ( target.isUpdateExecutor() ) {
                    handleUpdateTrigger( request, target, trigger, lastSubject );
                } else if ( target.isDeleteExecutor() ) {
                    handleDeleteTrigger( request, target, trigger, lastSubject );
                } else if ( target.isUnmanagedExecutor() ) {
                    handleUnmanagedTrigger( request, target, trigger, lastSubject );
                }
            }
        }
    }


    public static Trigger getInstance() {
        return instance;
    }
}

package com.replace.replace.api.poc.kernel.trigger;

import com.replace.replace.api.crud.Create;
import com.replace.replace.api.crud.Delete;
import com.replace.replace.api.crud.Update;
import com.replace.replace.api.poc.annotation.*;
import com.replace.replace.api.poc.kernel.entity.EntityHandler;
import com.replace.replace.api.poc.kernel.exception.*;
import com.replace.replace.api.poc.kernel.setter.SetterHandler;
import com.replace.replace.configuration.poc.TriggerIdentifier;
import com.replace.replace.repository.DefaultRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class TriggerHandler {

    protected final Map< Class< ? >, List< Trigger > > storage = new HashMap<>();
    protected final ApplicationContext                 applicationContext;
    protected final SetterHandler                      setterHandler;
    protected final EntityHandler                      entityHandler;


    public TriggerHandler(
            ApplicationContext applicationContext,
            SetterHandler setterHandler,
            EntityHandler entityHandler ) {
        this.applicationContext = applicationContext;
        this.setterHandler      = setterHandler;
        this.entityHandler      = entityHandler;
        storage.put( CreateTrigger.class, new ArrayList<>() );
        storage.put( UpdateTrigger.class, new ArrayList<>() );
        storage.put( DeleteTrigger.class, new ArrayList<>() );
        storage.put( UnmanagedTrigger.class, new ArrayList<>() );
    }


    public void load( Field field, EntryPoint entryPoint )
            throws NoSuchMethodException, SetterNotFoundException, NoSuchFieldException, ToManySetterParameterException, InvalidSetterParameterType, MultipleSetterFoundException, UnmanagedTriggerMissingExecutorException {
        for ( CreateTrigger createTrigger : entryPoint.createTriggers() ) {
            load( field, createTrigger );
        }

        for ( UpdateTrigger updateTrigger : entryPoint.updateTriggers() ) {
            load( field, updateTrigger );
        }

        for ( DeleteTrigger deleteTrigger : entryPoint.deleteTriggers() ) {
            load( field, deleteTrigger );
        }

        for ( UnmanagedTrigger unmanagedTrigger : entryPoint.unmanagedTriggers() ) {
            load( field, unmanagedTrigger );
        }
    }


    public Trigger getTrigger( Class< ? > type, TriggerIdentifier triggerIdentifier ) {
        for ( Trigger trigger : storage.get( type ) ) {
            if ( trigger.getTriggerIdentifier() == triggerIdentifier ) {
                return trigger;
            }
        }

        return null;
    }


    public Trigger getTrigger( TriggerIdentifier triggerIdentifier ) {
        for ( Map.Entry< Class< ? >, List< Trigger > > entry : storage.entrySet() ) {
            for ( Trigger trigger : entry.getValue() ) {
                if ( trigger.getTriggerIdentifier() == triggerIdentifier ) {
                    return trigger;
                }
            }
        }

        return null;
    }


    private void load( Field field, CreateTrigger createTrigger )
            throws NoSuchMethodException, SetterNotFoundException, MultipleSetterFoundException, ToManySetterParameterException, InvalidSetterParameterType, NoSuchFieldException {
        storage.get( CreateTrigger.class ).add( new Trigger( field, createTrigger ) );
    }


    private void load( Field field, UpdateTrigger updateTrigger )
            throws NoSuchMethodException, SetterNotFoundException, MultipleSetterFoundException, ToManySetterParameterException, InvalidSetterParameterType, NoSuchFieldException {
        storage.get( UpdateTrigger.class ).add( new Trigger( field, updateTrigger ) );
    }


    private void load( Field field, DeleteTrigger deleteTrigger ) {
        storage.get( DeleteTrigger.class ).add( new Trigger( field, deleteTrigger ) );
    }


    private void load( Field field, UnmanagedTrigger unmanagedTrigger )
            throws UnmanagedTriggerMissingExecutorException {
        storage.get( UnmanagedTrigger.class ).add( new Trigger( field, unmanagedTrigger ) );
    }


    public class Trigger {

        private final TriggerIdentifier triggerIdentifier;

        private final Object trigger;

        private final EntityHandler.Entity entity;

        private final List< SetterHandler.Setter > setters;

        private final List< com.replace.replace.api.poc.annotation.Trigger > triggers;

        private final Class< ? > triggerType;

        private Object executor;


        public Trigger( Field field, CreateTrigger createTrigger )
                throws NoSuchFieldException, SetterNotFoundException, ToManySetterParameterException, MultipleSetterFoundException, InvalidSetterParameterType, NoSuchMethodException {
            triggerIdentifier = createTrigger.id();
            trigger           = createTrigger;
            entity            = entityHandler.getEntity( field.getDeclaringClass() );
            triggerType       = CreateTrigger.class;
            triggers          = new ArrayList<>( Arrays.asList( createTrigger.triggers() ) );
            setters           = loadSetters( createTrigger.fields() );
            executor          = createTrigger.executor() != DefaultCreate.class ? applicationContext.getBean( createTrigger.executor() ) : null;
        }


        public Trigger( Field field, UpdateTrigger updateTrigger )
                throws NoSuchFieldException, SetterNotFoundException, ToManySetterParameterException, MultipleSetterFoundException, InvalidSetterParameterType, NoSuchMethodException {
            triggerIdentifier = updateTrigger.id();
            trigger           = updateTrigger;
            entity            = entityHandler.getEntity( field.getDeclaringClass() );
            triggerType       = UpdateTrigger.class;
            triggers          = new ArrayList<>( Arrays.asList( updateTrigger.triggers() ) );
            setters           = loadSetters( updateTrigger.fields() );
            executor          = updateTrigger.executor() != DefaultUpdate.class ? applicationContext.getBean( updateTrigger.executor() ) : null;
        }


        public Trigger( Field field, DeleteTrigger deleteTrigger ) {
            triggerIdentifier = deleteTrigger.id();
            trigger           = deleteTrigger;
            entity            = entityHandler.getEntity( field.getDeclaringClass() );
            triggerType       = DeleteTrigger.class;
            triggers          = new ArrayList<>( Arrays.asList( deleteTrigger.triggers() ) );
            setters           = new ArrayList<>();
            executor          = deleteTrigger.executor() != DefaultDelete.class ? applicationContext.getBean( deleteTrigger.executor() ) : null;
        }


        public Trigger( Field field, UnmanagedTrigger unmanagedTrigger )
                throws UnmanagedTriggerMissingExecutorException {
            triggerIdentifier = unmanagedTrigger.id();
            trigger           = unmanagedTrigger;
            entity            = entityHandler.getEntity( field.getDeclaringClass() );
            triggerType       = UnmanagedTrigger.class;
            triggers          = new ArrayList<>( Arrays.asList( unmanagedTrigger.triggers() ) );
            setters           = new ArrayList<>();

            if ( unmanagedTrigger.createExecutor() != DefaultCreate.class ) {
                executor = applicationContext.getBean( unmanagedTrigger.createExecutor() );
            } else if ( unmanagedTrigger.updateExecutor() != DefaultUpdate.class ) {
                executor = applicationContext.getBean( unmanagedTrigger.updateExecutor() );
            } else if ( unmanagedTrigger.deleteExecutor() != DefaultDelete.class ) {
                executor = applicationContext.getBean( unmanagedTrigger.deleteExecutor() );
            } else {
                executor = applicationContext.getBean( unmanagedTrigger.unmanagedExecutor() );
            }

            if ( executor == null ) {
                throw new UnmanagedTriggerMissingExecutorException();
            }
        }


        public TriggerIdentifier getTriggerIdentifier() {
            return triggerIdentifier;
        }


        public Object getTrigger() {
            return trigger;
        }


        public List< SetterHandler.Setter > getSetters() {
            return setters;
        }


        public Class< ? > getTriggerType() {
            return triggerType;
        }


        public List< com.replace.replace.api.poc.annotation.Trigger > getTriggers() {
            return triggers;
        }


        public boolean isCustomExecutor() {
            return executor != null;
        }


        public Object getExecutor() {
            return executor;
        }


        public Class< ? > getSubject() {
            return entity.getSubject();
        }


        public DefaultRepository< ? extends DefaultRepository< ? > > getDefaultRepository() {
            return entity.getDefaultRepository();
        }


        public boolean isCreateExecutor() {
            return executor != null
                    && executor instanceof Create;
        }


        public boolean isUpdateExecutor() {
            return executor != null
                    && executor instanceof Update;
        }


        public boolean isDeleteExecutor() {
            return executor != null
                    && executor instanceof Delete;
        }


        public boolean isUnmanagedExecutor() {
            return executor != null
                    && executor instanceof com.replace.replace.api.poc.api.UnmanagedTrigger;
        }


        private List< SetterHandler.Setter > loadSetters( String[] fields )
                throws NoSuchFieldException, SetterNotFoundException, ToManySetterParameterException, MultipleSetterFoundException, InvalidSetterParameterType, NoSuchMethodException {
            List< SetterHandler.Setter > setters = new ArrayList<>();

            if ( fields.length == 1 && fields[ 0 ] == "*" ) {
                for ( Field fieldToSet : entity.getSubject().getDeclaredFields() ) {
                    setters.add( setterHandler.toSetter( fieldToSet ) );
                }
            } else {
                for ( String fieldToSet : fields ) {
                    setters.add( setterHandler.toSetter( entity.getSubject().getDeclaredField( fieldToSet ) ) );
                }
            }

            return setters;
        }
    }
}

package com.replace.replace.api.container;


import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class ContainerImpl implements Container {

    protected ApplicationContext applicationContext;

    public ContainerImpl(
            ApplicationContext applicationContext ) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object getInstance( String classname ) {
        if ( this.applicationContext.containsBean( classname ) ) {
            return this.applicationContext.getBean( classname );
        }

        return null;
    }
}

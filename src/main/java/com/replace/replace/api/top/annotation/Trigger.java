package com.replace.replace.api.top.annotation;

import com.replace.replace.api.top.api.ResourceProvider;
import com.replace.replace.configuration.dynamic.TriggerIdentifier;

public @interface Trigger {

    /**
     * @return Target trigger
     */
    TriggerIdentifier triggerId();


    /**
     * @return Field to attach result of CreateTrigger
     */
    String attachToField() default "";


    /**
     * @return Relation creation is optional
     */
    boolean optional() default true;


    /**
     * @return Fields that provide the resource
     */
    String providerField() default "";


    boolean provideMe() default false;


    /**
     * @return Custom provider for Update / Delete & Unmanaged Triggers
     */
    Class< ? extends ResourceProvider > customProvider() default ResourceProvider.class;
}

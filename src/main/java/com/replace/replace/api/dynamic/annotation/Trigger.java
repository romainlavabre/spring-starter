package com.replace.replace.api.dynamic.annotation;

import com.replace.replace.api.dynamic.api.CustomProvider;
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
     * @return Fields that provide the resource
     */
    String providerField() default "";


    /**
     * @return Custom provider for Update / Delete & Unmanaged Triggers
     */
    Class< ? extends CustomProvider > customProvider() default CustomProvider.class;
}

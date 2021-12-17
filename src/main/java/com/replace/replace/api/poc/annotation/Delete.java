package com.replace.replace.api.poc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Delete {

    String route() default "";


    String[] roles() default {"*"};


    boolean authenticated() default true;


    Trigger[] triggers() default {};


    Class< ? extends com.replace.replace.api.crud.Delete< ? > > executor() default DefaultDelete.class;
}

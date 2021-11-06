package com.replace.replace.api.dynamic.annotation;

import com.replace.replace.api.dynamic.api.RedirectResolver;
import com.replace.replace.configuration.dynamic.RouteIdentifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Put {
    RouteIdentifier id();


    String[] roles() default {"*"};


    boolean authenticated() default true;


    String[] fields() default {"*"};


    Class< RedirectResolver< ? > >[] redirectTo();


    Class< com.replace.replace.api.crud.Update< ? > > executor();
}

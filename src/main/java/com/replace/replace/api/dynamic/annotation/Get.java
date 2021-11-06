package com.replace.replace.api.dynamic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Get {
    boolean one() default true;


    boolean all() default true;


    String[] byRelation() default {};
}

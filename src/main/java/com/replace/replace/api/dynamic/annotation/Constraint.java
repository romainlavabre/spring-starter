package com.replace.replace.api.dynamic.annotation;

import com.replace.replace.api.dynamic.api.CustomConstraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Constraint {
    Class< ? extends CustomConstraint >[] value();
}

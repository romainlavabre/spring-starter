package com.replace.replace.api.json.annotation;

import com.replace.replace.api.json.formatter.Formatter;
import com.replace.replace.api.json.overwritter.Overwrite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Group {
    String name() default "_default";


    boolean onlyId() default true;


    boolean ascent() default false;


    boolean object() default false;


    String key() default "";


    Class formatter() default Formatter.class;


    Class overwrite() default Overwrite.class;


    Row[] row() default {};
}

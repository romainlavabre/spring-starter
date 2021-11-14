package com.replace.replace.api.poc.loader.mock;

import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class MockApplicationContext implements ApplicationContext {

    @Override
    public String getId() {
        return null;
    }


    @Override
    public String getApplicationName() {
        return null;
    }


    @Override
    public String getDisplayName() {
        return null;
    }


    @Override
    public long getStartupDate() {
        return 0;
    }


    @Override
    public ApplicationContext getParent() {
        return null;
    }


    @Override
    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return null;
    }


    @Override
    public BeanFactory getParentBeanFactory() {
        return null;
    }


    @Override
    public boolean containsLocalBean( String s ) {
        return false;
    }


    @Override
    public boolean containsBeanDefinition( String s ) {
        return false;
    }


    @Override
    public int getBeanDefinitionCount() {
        return 0;
    }


    @Override
    public String[] getBeanDefinitionNames() {
        return new String[ 0 ];
    }


    @Override
    public < T > ObjectProvider< T > getBeanProvider( Class< T > aClass, boolean b ) {
        return null;
    }


    @Override
    public < T > ObjectProvider< T > getBeanProvider( ResolvableType resolvableType, boolean b ) {
        return null;
    }


    @Override
    public String[] getBeanNamesForType( ResolvableType resolvableType ) {
        return new String[ 0 ];
    }


    @Override
    public String[] getBeanNamesForType( ResolvableType resolvableType, boolean b, boolean b1 ) {
        return new String[ 0 ];
    }


    @Override
    public String[] getBeanNamesForType( Class< ? > aClass ) {
        return new String[ 0 ];
    }


    @Override
    public String[] getBeanNamesForType( Class< ? > aClass, boolean b, boolean b1 ) {
        return new String[ 0 ];
    }


    @Override
    public < T > Map< String, T > getBeansOfType( Class< T > aClass ) throws BeansException {
        return null;
    }


    @Override
    public < T > Map< String, T > getBeansOfType( Class< T > aClass, boolean b, boolean b1 ) throws BeansException {
        return null;
    }


    @Override
    public String[] getBeanNamesForAnnotation( Class< ? extends Annotation > aClass ) {
        return new String[ 0 ];
    }


    @Override
    public Map< String, Object > getBeansWithAnnotation( Class< ? extends Annotation > aClass ) throws BeansException {
        return null;
    }


    @Override
    public < A extends Annotation > A findAnnotationOnBean( String s, Class< A > aClass ) throws NoSuchBeanDefinitionException {
        return null;
    }


    @Override
    public Object getBean( String s ) throws BeansException {
        return null;
    }


    @Override
    public < T > T getBean( String s, Class< T > aClass ) throws BeansException {
        return null;
    }


    @Override
    public Object getBean( String s, Object... objects ) throws BeansException {
        return null;
    }


    @Override
    public < T > T getBean( Class< T > aClass ) throws BeansException {
        return getMock( aClass );
    }


    @Override
    public < T > T getBean( Class< T > aClass, Object... objects ) throws BeansException {
        return null;
    }


    @Override
    public < T > ObjectProvider< T > getBeanProvider( Class< T > aClass ) {
        return null;
    }


    @Override
    public < T > ObjectProvider< T > getBeanProvider( ResolvableType resolvableType ) {
        return null;
    }


    @Override
    public boolean containsBean( String s ) {
        return false;
    }


    @Override
    public boolean isSingleton( String s ) throws NoSuchBeanDefinitionException {
        return false;
    }


    @Override
    public boolean isPrototype( String s ) throws NoSuchBeanDefinitionException {
        return false;
    }


    @Override
    public boolean isTypeMatch( String s, ResolvableType resolvableType ) throws NoSuchBeanDefinitionException {
        return false;
    }


    @Override
    public boolean isTypeMatch( String s, Class< ? > aClass ) throws NoSuchBeanDefinitionException {
        return false;
    }


    @Override
    public Class< ? > getType( String s ) throws NoSuchBeanDefinitionException {
        return null;
    }


    @Override
    public Class< ? > getType( String s, boolean b ) throws NoSuchBeanDefinitionException {
        return null;
    }


    @Override
    public String[] getAliases( String s ) {
        return new String[ 0 ];
    }


    @Override
    public void publishEvent( Object o ) {

    }


    @Override
    public String getMessage( String s, Object[] objects, String s1, Locale locale ) {
        return null;
    }


    @Override
    public String getMessage( String s, Object[] objects, Locale locale ) throws NoSuchMessageException {
        return null;
    }


    @Override
    public String getMessage( MessageSourceResolvable messageSourceResolvable, Locale locale ) throws NoSuchMessageException {
        return null;
    }


    @Override
    public Environment getEnvironment() {
        return null;
    }


    @Override
    public Resource[] getResources( String s ) throws IOException {
        return new Resource[ 0 ];
    }


    @Override
    public Resource getResource( String s ) {
        return null;
    }


    @Override
    public ClassLoader getClassLoader() {
        return null;
    }


    private Map< Class< ? >, Object > userMocks = new HashMap<>();


    public < T > T getMock( Class< ? > type ) {
        if ( userMocks.containsKey( type ) ) {
            return ( T ) userMocks.get( type );
        }

        userMocks.put( type, Mockito.mock( type ) );

        return ( T ) userMocks.get( type );
    }


    public List< Object > getAllMock() {
        return Arrays.asList( userMocks.values().toArray() );
    }
}

package com.replace.replace.api.json;

import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;
import com.replace.replace.api.json.annotation.JsonPut;
import com.replace.replace.api.json.annotation.Row;
import com.replace.replace.api.json.formatter.Formatter;
import com.replace.replace.api.json.overwritter.Overwrite;
import com.replace.replace.api.json.put.Put;
import com.replace.replace.configuration.json.GroupType;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Encoder {

    public static < T > Map< String, Object > encode( final T entity ) {

        return Encoder.encode( entity, GroupType.DEFAULT );
    }


    public static Map< String, Object > encode( final Map< String, Object > entity ) {
        return Encoder.encode( entity, GroupType.DEFAULT );
    }


    public static < T > List< Map< String, Object > > encode( final List< T > entity ) {
        return Encoder.encode( entity, GroupType.DEFAULT );
    }


    public static < T > List< Map< String, Object > > encode( final T[] entity ) {
        return Encoder.encode( entity, GroupType.DEFAULT );
    }


    public static < T > Map< String, Object > encode( final T entity, final String group ) {
        return Encoder.core( entity, group );
    }


    public static < T > Map< String, Object > encode( final Map< String, T > entity, final String group ) {
        final Map< String, Object > map = new HashMap<>();

        for ( final Map.Entry< String, T > entry : entity.entrySet() ) {
            map.put( entry.getKey(), Encoder.core( entry.getValue(), group ) );
        }

        return map;
    }


    public static < T > List< Map< String, Object > > encode( final List< T > entity, final String group ) {

        final List< Map< String, Object > > list = new ArrayList<>();

        for ( final T unit : entity ) {
            list.add( Encoder.core( unit, group ) );
        }

        return list;
    }


    public static < T > List< Map< String, Object > > encode( final T[] entities, final String group ) {
        final List< Map< String, Object > > list = new ArrayList<>();

        for ( final T unit : entities ) {
            list.add( Encoder.core( unit, group ) );
        }

        return list;
    }


    protected static Map< String, Object > core( final Object entity, final String targetGroup ) {
        assert entity != null : "variable entity should not be null";

        final Map< String, Object > mapped = new HashMap<>();

        for ( final Field field : Encoder.getAllField( entity ) ) {
            final Json json = field.getAnnotation( Json.class );

            if ( json == null ) {
                continue;
            }

            searchGroup:
            for ( final Group group : List.of( json.groups() ) ) {

                if ( !group.name().equals( targetGroup ) ) {
                    continue;
                }

                field.setAccessible( true );

                final String key    = group.key().isBlank() || group.key().isEmpty() ? field.getName() : group.key();
                Object       object = null;

                try {
                    object = field.get( entity );
                } catch ( final IllegalAccessException e ) {
                    e.printStackTrace();
                }

                if ( object == null ) {
                    mapped.put( key, null );
                    break searchGroup;
                }


                if ( !group.object() || group.onlyId() ) {

                    if ( group.object() ) {

                        boolean onArray = false;

                        final List< Object > ids;
                        final List< Long >   nextIds = new ArrayList<>();

                        if ( object instanceof Map ) {
                            onArray = true;
                            final Map< Object, Object > map = ( Map< Object, Object > ) object;

                            ids = new ArrayList( map.values() );
                        } else if ( object instanceof Collection ) {
                            onArray = true;
                            ids     = new ArrayList<>();
                            (( Collection< ? > ) object).forEach( ids::add );
                        } else {
                            ids = List.of( object );
                        }


                        for ( final Object data : ids ) {

                            try {
                                final Field relationId = data.getClass().getDeclaredField( "id" );
                                relationId.setAccessible( true );
                                nextIds.add( ( Long ) relationId.get( data ) );
                            } catch ( final IllegalAccessException | NoSuchFieldException e ) {
                                e.printStackTrace();
                            }
                        }

                        if ( onArray ) {
                            object = nextIds;
                        } else {
                            if ( nextIds.size() > 0 ) {
                                object = nextIds.get( 0 );
                            } else {
                                object = nextIds;
                            }
                        }
                    }

                    if ( !group.overwrite().isInterface() ) {

                        try {
                            object = (( Overwrite ) group.overwrite().newInstance()).overwrite( object );
                        } catch ( final InstantiationException | IllegalAccessException e ) {
                            e.printStackTrace();
                        }
                    }

                    if ( !group.formatter().isInterface() ) {
                        try {
                            object = (( Formatter ) group.formatter().newInstance()).format( object );
                        } catch ( final InstantiationException | IllegalAccessException e ) {
                            e.printStackTrace();
                        }
                    }

                    mapped.put( key, object );
                    break searchGroup;
                }

                if ( object instanceof List ) {
                    object = Encoder.< List >encode( ( List ) object, targetGroup );
                } else if ( object instanceof Map ) {
                    object = Encoder.< Map >encode( ( Map ) object, targetGroup );
                } else {
                    object = Encoder.encode( object, targetGroup );
                }

                if ( !group.ascent() ) {
                    mapped.put( key, object );
                } else {
                    final Map< String, Object > objectMap = ( Map< String, Object > ) object;

                    for ( final Map.Entry< String, Object > entry : objectMap.entrySet() ) {
                        mapped.put( entry.getKey(), entry.getValue() );
                    }
                }
            }
        }

        final JsonPut jsonPut = entity.getClass().getAnnotation( JsonPut.class );

        if ( jsonPut != null ) {
            for ( final Group group : jsonPut.group() ) {
                if ( !group.name().equals( targetGroup ) ) {
                    continue;
                }

                for ( final Row row : group.row() ) {
                    try {
                        mapped.put( row.key(), (( Put ) row.handler().newInstance()).build( entity ) );
                    } catch ( final InstantiationException | IllegalAccessException e ) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return mapped;
    }


    private static List< Field > getAllField( final Object entity ) {

        final List< Field > fields = new ArrayList<>( Arrays.asList( entity.getClass().getDeclaredFields() ) );

        Class superClass = entity.getClass().getSuperclass();

        while ( superClass != null ) {

            fields.addAll( Arrays.asList( superClass.getDeclaredFields() ) );

            superClass = superClass.getSuperclass();
        }

        return fields;
    }
}

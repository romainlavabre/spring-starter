package com.replace.replace.api.json;

import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;
import com.replace.replace.api.json.formatter.Formatter;
import com.replace.replace.api.json.overwritter.Overwrite;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Encoder {

    public static < T > Map< String, Object > encode( T entity ) {

        return encode( entity, GroupType.DEFAULT );
    }

    public static Map< String, Object > encode( Map< String, Object > entity ) {
        return encode( entity, GroupType.DEFAULT );
    }

    public static < T > List< Map< String, Object > > encode( List< T > entity ) {
        return encode( entity, GroupType.DEFAULT );
    }

    public static < T > List< Map< String, Object > > encode( T[] entity ) {
        return encode( entity, GroupType.DEFAULT );
    }

    public static < T > Map< String, Object > encode( T entity, String group ) {
        return core( entity, group );
    }

    public static < T > Map< String, Object > encode( Map< String, T > entity, String group ) {
        Map< String, Object > map = new HashMap<>();

        for ( Map.Entry< String, T > entry : entity.entrySet() ) {
            map.put( entry.getKey(), Encoder.core( entry.getValue(), group ) );
        }

        return map;
    }

    public static < T > List< Map< String, Object > > encode( List< T > entity, String group ) {

        List< Map< String, Object > > list = new ArrayList<>();

        for ( T unit : entity ) {
            list.add( Encoder.core( unit, group ) );
        }

        return list;
    }

    public static < T > List< Map< String, Object > > encode( T[] entities, String group ) {
        List< Map< String, Object > > list = new ArrayList<>();

        for ( T unit : entities ) {
            list.add( Encoder.core( unit, group ) );
        }

        return list;
    }

    protected static Map< String, Object > core( Object entity, String targetGroup ) {
        Map< String, Object > mapped = new HashMap<>();

        for ( Field field : entity.getClass().getDeclaredFields() ) {
            Json json = field.getAnnotation( Json.class );

            if ( json == null ) {
                continue;
            }

            searchGroup:
            for ( Group group : List.of( json.groups() ) ) {

                if ( !group.name().equals( targetGroup ) ) {
                    continue;
                }

                field.setAccessible( true );

                String key    = group.key().isBlank() || group.key().isEmpty() ? field.getName() : group.key();
                Object object = null;

                try {
                    object = field.get( entity );
                } catch ( IllegalAccessException e ) {
                    e.printStackTrace();
                }

                if ( object == null ) {
                    mapped.put( key, null );
                    break searchGroup;
                }


                if ( !group.object() || group.onlyId() ) {

                    if ( group.object() ) {

                        boolean onArray = false;

                        List< Object >  ids;
                        List< Integer > nextIds = new ArrayList<>();

                        if ( object instanceof Map ) {
                            onArray = true;
                            Map< Object, Object > map = ( Map< Object, Object > ) object;

                            ids = new ArrayList( map.values() );
                        } else if ( object instanceof Collection ) {
                            onArray = true;
                            ids     = ( List< Object > ) object;
                        } else {
                            ids = List.of( object );
                        }


                        for ( Object data : ids ) {

                            try {
                                Field relationId = data.getClass().getDeclaredField( "id" );
                                relationId.setAccessible( true );
                                nextIds.add( ( Integer ) relationId.get( data ) );
                            } catch ( IllegalAccessException | NoSuchFieldException e ) {
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
                        } catch ( InstantiationException | IllegalAccessException e ) {
                            e.printStackTrace();
                        }
                    }

                    if ( !group.formatter().isInterface() ) {
                        try {
                            object = (( Formatter ) group.formatter().newInstance()).format( object );
                        } catch ( InstantiationException | IllegalAccessException e ) {
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
                    Map< String, Object > objectMap = ( Map< String, Object > ) object;

                    for ( Map.Entry< String, Object > entry : objectMap.entrySet() ) {
                        mapped.put( entry.getKey(), entry.getValue() );
                    }
                }

            }
        }

        return mapped;
    }

}

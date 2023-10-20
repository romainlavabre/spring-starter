package com.fairfair.callcenter.system.action.converter;

import jakarta.persistence.AttributeConverter;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class JsonConverter implements AttributeConverter< Map< String, Object >, String > {

    @Override
    public String convertToDatabaseColumn( Map< String, Object > map ) {
        return JSONObject.valueToString( map );
    }


    @Override
    public Map< String, Object > convertToEntityAttribute( String map ) {
        JSONObject jsonObject = new JSONObject( map );

        return jsonObject.toMap();
    }
}

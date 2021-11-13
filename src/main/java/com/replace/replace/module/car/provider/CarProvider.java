package com.replace.replace.module.car.provider;

import com.replace.replace.api.top.api.ResourceProvider;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "CarProvider" )
public class CarProvider implements ResourceProvider {

    @Override
    public List< Object > getResources( Object subject ) {
        return List.of( subject );
    }
}

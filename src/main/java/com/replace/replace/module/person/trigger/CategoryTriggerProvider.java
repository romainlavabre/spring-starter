package com.replace.replace.module.person.trigger;

import com.replace.replace.api.dynamic.api.CustomProvider;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class CategoryTriggerProvider implements CustomProvider {


    @Override
    public List< Object > getResources( Object subject ) {
        return List.of( subject );
    }
}

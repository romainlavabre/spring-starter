package com.replace.replace.api.dynamic.kernel.entry;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Controller {

    public ResponseEntity< Map< String, Object > > get() {
        return ResponseEntity.ok( Map.of( "result", "ca marche mec" ) );
    }
}

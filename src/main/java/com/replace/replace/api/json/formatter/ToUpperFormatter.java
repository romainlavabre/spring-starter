package com.replace.replace.api.json.formatter;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class ToUpperFormatter implements Formatter< String > {

    public ToUpperFormatter() {
    }


    @Override
    public String format( String data ) {
        return data.toUpperCase();
    }
}

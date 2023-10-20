package com.fairfair.callcenter.util;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Price {

    public static Long toLong( Double price ) {
        if ( price == null ) {
            return null;
        }

        if ( true ) {
            return Math.round( price * 100d );
        }

        String dec = BigDecimal.valueOf( price ).movePointRight( 2 ).round( MathContext.UNLIMITED ).movePointLeft( 2 ).toString();


        if ( !dec.contains( "." ) ) {
            return Cast.getLong( price + "00" );
        }

        if ( dec.split( "\\." )[ 1 ].length() > 2 ) {
            dec = dec.split( "\\." )[ 0 ] + "." + dec.split( "\\." )[ 1 ].substring( 0, 2 );
        }

        if ( dec.split( "\\." )[ 1 ].length() == 2 ) {
            return Cast.getLong( dec.replace( ".", "" ) );
        }

        return Cast.getLong( dec.replace( ".", "" ) + "0" );
    }


    public static Double toDouble( Long value ) {
        if ( value == null ) {
            return null;
        }

        return value / 100d;
    }


    public static long roundMicroCents( double value ) {
        return Math.round( value );
    }


    public static double toTaxed( long amount, short vat ) {
        return toTaxed( toDouble( amount ), vat );
    }


    public static double toTaxed( long amount, float vat ) {
        return toTaxed( toDouble( amount ), vat );
    }


    public static double toTaxed( long amount, double vat ) {
        return toTaxed( toDouble( amount ), vat );
    }


    public static double toTaxed( double amount, short vat ) {
        return toTaxed( amount, ( ( vat / 100d ) + 1 ) );
    }


    public static double toTaxed( double amount, float vat ) {
        return toTaxed( amount, Double.valueOf( vat ) );
    }


    public static double toTaxed( double amount, double vat ) {
        double result = toDouble( roundMicroCents( toLong( amount ) * vat ) );

        return result;
    }
}

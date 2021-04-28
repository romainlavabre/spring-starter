package com.replace.replace.api.sms;

import com.replace.replace.api.environment.Environment;
import com.replace.replace.api.environment.EnvironmentVariable;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Twilio implements SmsSender {

    protected Environment environment;

    public Twilio( final Environment environment ) {
        this.environment = environment;
    }

    @Override
    public boolean send( final String to, final String message ) {

        return this.core( to, message );
    }

    @Override
    public Boolean[] send( final List< String > to, final String message ) {
        final Boolean[] results = new Boolean[ to.size() ];

        int i = 0;

        for ( final String recipient : to ) {
            results[ i ] = this.core( recipient, message );

            i++;
        }

        return results;
    }

    protected boolean core( final String to, final String message ) {
        com.twilio.Twilio.init(
                this.environment.getEnv( EnvironmentVariable.SMS_TWILIO_SID ),
                this.environment.getEnv( EnvironmentVariable.SMS_PRIVATE_KEY )
        );


        final Message response =
                Message
                        .creator(
                                new PhoneNumber( to ),
                                new PhoneNumber( this.environment.getEnv( EnvironmentVariable.SMS_FROM ) ),
                                message
                        )
                        .create();

        return true;
    }
}

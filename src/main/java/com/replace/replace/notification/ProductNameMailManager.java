package com.replace.replace.notification;

import com.replace.replace.api.event.Event;
import com.replace.replace.api.event.EventDispatcher;
import com.replace.replace.api.event.EventSubscriber;
import com.replace.replace.api.mail.MailSender;
import com.replace.replace.api.mail.TemplateBuilder;
import com.replace.replace.model.Product;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class ProductNameMailManager implements EventSubscriber {

    protected MailSender      mailSender;
    protected TemplateBuilder templateBuilder;

    public ProductNameMailManager(
            MailSender mailSender,
            TemplateBuilder templateBuilder,
            EventDispatcher eventDispatcher
    ) {
        this.mailSender      = mailSender;
        this.templateBuilder = templateBuilder;
        eventDispatcher.follow( Event.PRODUCT_NAME_ALTERED, this );
    }


    protected void send( Product product ) {

        String template = this.templateBuilder.build( "test", Map.of(
                "name", "Romain Lavabre",
                "work", "Dévelopeur"
        ) );

        this.mailSender
                .send(
                        "romain.lavabre@fairfair.com",
                        "Modification d'un produit",
                        template
                );
    }

    @Override
    public void receiveEvent( String event, Map< String, Object > params ) throws RuntimeException {
        this.send( ( Product ) params.get( Product.class.getName() ) );
    }
}

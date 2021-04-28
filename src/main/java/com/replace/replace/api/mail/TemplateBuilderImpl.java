package com.replace.replace.api.mail;

import com.replace.replace.api.environment.Environment;
import com.replace.replace.api.environment.EnvironmentVariable;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class TemplateBuilderImpl implements TemplateBuilder {

    protected final static String         ENCODING = "UTF-8";
    protected              VelocityEngine velocityEngine;
    protected              Environment    environment;

    public TemplateBuilderImpl(
            Environment environment
    ) {
        this.environment    = environment;
        this.velocityEngine = new VelocityEngine();
    }

    @Override
    public String build( String name ) {
        return this.build( name, Map.of() );
    }


    @Override
    public String build( String name, Map< String, Object > parameters ) {

        Template template = this.velocityEngine.getTemplate( this.getPath( name ), ENCODING );

        VelocityContext velocityContext = new VelocityContext();

        parameters.forEach( velocityContext::put );

        return this.getContent( velocityContext, template );
    }

    protected String getContent( VelocityContext velocityContext, Template template ) {
        StringWriter content = new StringWriter();

        template.merge( velocityContext, content );

        return content.toString();
    }

    protected String getPath( String name ) {

        return this.environment.getEnv( EnvironmentVariable.MAIL_TEMPLATE_PATH ) +
                name +
                ".vm";
    }
}

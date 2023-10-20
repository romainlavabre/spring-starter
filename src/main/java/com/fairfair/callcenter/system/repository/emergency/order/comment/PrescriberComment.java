package com.fairfair.callcenter.system.repository.emergency.order.comment;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class PrescriberComment {

    private long id;

    private String content;

    private ZonedDateTime createdAt;

    private Long order;


    protected static PrescriberComment build( Map< String, Object > fields ) {
        PrescriberComment prescriberComment = new PrescriberComment();

        prescriberComment.id        = Cast.getLong( fields.get( "id" ) );
        prescriberComment.content   = String.valueOf( fields.get( "content" ) );
        prescriberComment.createdAt = ZonedDateTime.parse( ( String ) fields.get( "created_at" ) );
        prescriberComment.order     = Cast.getLong( fields.get( "order_id" ) );


        return prescriberComment;
    }


    public Long getId() {
        return id;
    }


    public String getContent() {
        return content;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public Long getOrder() {
        return order;
    }
}

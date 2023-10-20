package com.fairfair.callcenter.system.repository.user.company.artisan;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Artisan {

    protected Long id;

    protected Short maxDistance;

    protected Boolean enable;

    protected String phoneMobile;

    protected ZonedDateTime createdAt;

    protected Long company;

    protected List< Long > works;

    protected List< Long > skills;

    protected Long artisanAddress;


    protected Artisan() {
    }


    protected static Artisan build( Map< String, Object > fields ) {
        Artisan artisan = new Artisan();

        artisan.id             = Cast.getLong( fields.get( "id" ) );
        artisan.maxDistance    = Cast.getShort( fields.get( "max_distance" ) );
        artisan.enable         = ( Boolean ) fields.get( "enable" );
        artisan.phoneMobile    = ( String ) fields.get( "phone_mobile" );
        artisan.createdAt      = ZonedDateTime.parse( ( String ) fields.get( "created_at" ) );
        artisan.company        = Cast.getLong( fields.get( "company_id" ) );
        artisan.works          = ( List< Long > ) fields.get( "works_id" );
        artisan.skills         = ( List< Long > ) fields.get( "skills_id" );
        artisan.artisanAddress = Cast.getLong( fields.get( "artisan_address_id" ) );

        return artisan;
    }


    public Long getId() {
        return id;
    }


    public Short getMaxDistance() {
        return maxDistance;
    }


    public Boolean isEnable() {
        return enable;
    }


    public String getPhoneMobile() {
        return phoneMobile;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public Long getCompany() {
        return company;
    }


    public List< Long > getWorks() {
        return works;
    }


    public List< Long > getSkills() {
        return skills;
    }


    public Long getArtisanAddress() {
        return artisanAddress;
    }
}

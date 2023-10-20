package com.fairfair.callcenter.system.action;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Repository
public interface ActionRepository extends JpaRepository< Action, Long > {

    List< Action > findAllByIdGreaterThan( long id );


    List< Action > findAllByTreated( boolean treated );


    List< Action > findAllByTreatedAndCreatedAtLessThan( boolean treated, ZonedDateTime createdAt );


    List< Action > findAllByIdIn( List< Long > ids );


    void deleteByRemovableIsTrue();


    void deleteByTreatedIsTrueAndRemovableIsFalseAndCreatedAtIsLessThan( ZonedDateTime zonedDateTime );
}

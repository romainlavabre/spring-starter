package com.fairfair.callcenter.system.repository.assignment;


import java.util.List;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface SuggestionRepository {
    Optional< Suggestion > findById( Long id );
    
    Suggestion findOrFail( Long id );

    Optional< Suggestion > findByToken( String token );

    Suggestion findOrFailByToken( String token );

    Optional< List< Suggestion > > findAllByOrder( Long id );

    List< Suggestion > findAllByB2cOrder( Long id );
}

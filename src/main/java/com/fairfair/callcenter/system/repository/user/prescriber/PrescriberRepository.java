package com.fairfair.callcenter.system.repository.user.prescriber;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface PrescriberRepository {
    long FIDELIA        = 1;
    long QUI_OUVRE      = 2;
    long DYNAREN        = 3;
    long FIL_ASSISTANCE = 4;
    long HSVD           = 5;
    long HSVM           = 6;
    long OPTEVEN        = 7;
    long MUTUAIDE       = 8;
    long ALLO_MARCEL    = 9;
    long AXA            = 10;
    long EAF            = 11;
    long LOCAGESTION    = 12;
    long SANTONI        = 13;
    long SOLUSGESTION   = 14;
    long NOUS_GERONS    = 15;
    long FMB            = 24;
    long FONCIA         = 25;
    long GMF            = 26;


    Optional< Prescriber > findById( long id );


    Prescriber findOrFail( long id );


    Optional< Prescriber > findByReferentId( long id );


    Prescriber findOrFailByReferent( long id );
}

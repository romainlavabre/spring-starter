package com.replace.replace.module.car.trigger;

import com.replace.replace.api.crud.Update;
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.entity.Car;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class OwnerResolver implements Update< Car > {

    protected final HistoryHandler historyHandler;


    public OwnerResolver( HistoryHandler historyHandler ) {
        this.historyHandler = historyHandler;
    }


    @Override
    public void update( Request request, Car car ) {
        car.setOwner( !car.isCreditPurchase() );

        if ( car.getId() > 0 ) {
            historyHandler.update( car, "owner" );
        }
    }
}

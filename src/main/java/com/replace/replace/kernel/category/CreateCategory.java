package com.replace.replace.kernel.category;

import com.replace.replace.api.crud.Create;
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.storage.data.DataStorageHandler;
import com.replace.replace.model.Category;
import com.replace.replace.parameter.CategoryParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@Qualifier( value = "createCategory" )
public class CreateCategory implements Create< Category > {

    protected DataStorageHandler dataStorageHandler;
    protected HistoryHandler     historyHandler;

    public CreateCategory(
            DataStorageHandler dataStorageHandler,
            HistoryHandler historyHandler
    ) {
        this.dataStorageHandler = dataStorageHandler;
        this.historyHandler     = historyHandler;
    }

    @Override
    public void create( Request request, Category category ) {

        String name = ( String ) request.getParameter( CategoryParameter.NAME );

        category.setName( name );

        this.historyHandler.create( category );

        this.dataStorageHandler.persist( category );
    }
}

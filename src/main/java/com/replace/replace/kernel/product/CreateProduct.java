package com.replace.replace.kernel.product;

import com.replace.replace.api.crud.Create;
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.storage.data.DataStorageHandler;
import com.replace.replace.dao.CategoryDao;
import com.replace.replace.model.Product;
import com.replace.replace.parameter.ProductParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@Qualifier( value = "createProduct" )
public class CreateProduct implements Create< Product > {

    protected DataStorageHandler dataStorageHandler;
    protected HistoryHandler     historyHandler;
    protected CategoryDao        categoryDao;

    public CreateProduct(
            DataStorageHandler dataStorageHandler,
            HistoryHandler historyHandler,
            CategoryDao categoryDao
    ) {
        this.dataStorageHandler = dataStorageHandler;
        this.historyHandler     = historyHandler;
        this.categoryDao        = categoryDao;
    }

    @Override
    public void create( Request request, Product product ) {
        String  name       = ( String ) request.getParameter( ProductParameter.NAME );
        Double  price      = ( Double ) request.getParameter( ProductParameter.PRICE );
        Double  buyPrice   = ( Double ) request.getParameter( ProductParameter.BUY_PRICE );
        Integer categoryId = ( Integer ) request.getParameter( ProductParameter.CATEGORY );

        product.setName( name );
        product.setBuyPrice( buyPrice );
        product.setPrice( price );
        product.setCategory( this.categoryDao.findById( categoryId ).get() );


        this.historyHandler.create( product );

        this.dataStorageHandler.persist( product );

    }
}

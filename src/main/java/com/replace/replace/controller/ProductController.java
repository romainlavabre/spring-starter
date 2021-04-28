package com.replace.replace.controller;

import com.replace.replace.api.crud.Create;
import com.replace.replace.api.crud.Update;
import com.replace.replace.api.json.Encoder;
import com.replace.replace.api.json.GroupType;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.storage.data.DataStorageHandler;
import com.replace.replace.dao.ProductDao;
import com.replace.replace.exception.HttpNotFoundException;
import com.replace.replace.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    protected final DataStorageHandler dataStorageHandler;
    protected final Request            request;
    protected final ProductDao         productDao;
    protected final Create< Product >  createProduct;
    protected final Update< Product >  updateProductName;

    public ProductController(
            final DataStorageHandler dataStorageHandler,
            final Request request,
            final ProductDao productDao,
            final Create< Product > createProduct,
            final Update< Product > updateProductName ) {
        this.dataStorageHandler = dataStorageHandler;
        this.request            = request;
        this.productDao         = productDao;
        this.createProduct      = createProduct;
        this.updateProductName  = updateProductName;
    }

    @RequestMapping( path = "/products/{id}", method = RequestMethod.GET )
    public ResponseEntity< Map< String, Object > > getOne( @PathVariable final int id ) {
        final Product product = this.productDao.findById( id );

        if ( product == null ) {
            throw new HttpNotFoundException( "Produit introuvable" );
        }

        return ResponseEntity
                .ok( ( Map< String, Object > ) Encoder.encode( product, GroupType.DEFAULT ) );
    }

    @GetMapping( path = "/products" )
    public ResponseEntity< List< Map< String, Object > > > findAll() {

        return ResponseEntity
                .ok( Encoder.encode( this.productDao.findAll() ) );
    }


    @Transactional
    @PostMapping( path = "/products" )
    public ResponseEntity< Map< String, Object > > create() {

        final Product product = new Product();

        this.createProduct.create( this.request, product );

        this.dataStorageHandler.save();

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( Encoder.encode( product ) );
    }

    @PatchMapping( path = "/products/{id:[0-9]+}/name" )
    @Transactional
    public ResponseEntity< Object > updateName( @PathVariable( "id" ) final Product product ) {

        this.updateProductName.update( this.request, product );

        this.dataStorageHandler.save();

        return ResponseEntity
                .ok()
                .build();
    }

}

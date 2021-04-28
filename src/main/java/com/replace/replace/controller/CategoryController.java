package com.replace.replace.controller;

import com.replace.replace.api.crud.Create;
import com.replace.replace.api.json.Encoder;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.storage.data.DataStorageHandler;
import com.replace.replace.dao.CategoryDao;
import com.replace.replace.exception.HttpNotFoundException;
import com.replace.replace.model.Category;
import kong.unirest.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController
public class CategoryController {

    protected final DataStorageHandler dataStorageHandler;
    protected final CategoryDao        categoryDao;
    protected final Request            request;
    protected final Create< Category > createCategory;

    public CategoryController(
            DataStorageHandler dataStorageHandler,
            CategoryDao categoryDao,
            Create< Category > createCategory,
            Request request
    ) {
        this.dataStorageHandler = dataStorageHandler;
        this.categoryDao        = categoryDao;
        this.createCategory     = createCategory;
        this.request            = request;
    }
    
    @GetMapping( path = "/categories/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > findOne( @PathVariable Integer id ) {

        Optional< Category > optionalCategory = this.categoryDao.findById( id );

        if ( !optionalCategory.isPresent() ) {
            throw new HttpNotFoundException( "Categorie introuvable" );
        }

        return ResponseEntity
                .ok( Encoder.encode( optionalCategory.get() ) );

    }


    @PostMapping( path = "/categories" )
    @Transactional
    public ResponseEntity< Map< String, Object > > create() {
        Category category = new Category();

        this.createCategory.create( this.request, category );

        this.dataStorageHandler.save();

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( Encoder.encode( category ) );
    }
}

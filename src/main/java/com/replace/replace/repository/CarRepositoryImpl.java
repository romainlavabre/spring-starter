package com.replace.replace.repository;

import com.replace.replace.entity.Car;
import com.replace.replace.entity.Person;
import com.replace.replace.exception.HttpNotFoundException;
import com.replace.replace.repository.jpa.CarJpa;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class CarRepositoryImpl extends AbstractRepository< Car > implements CarRepository {

    protected final CarJpa carJpa;


    public CarRepositoryImpl(
            EntityManager entityManager,
            CarJpa carJpa ) {
        super( entityManager, carJpa );
        this.carJpa = carJpa;
    }


    @Override
    public Optional< Car > findByPerson( Person person ) {
        return carJpa.findByPerson( person );
    }


    @Override
    public Car findOrFailByPerson( Person person ) {
        Optional< Car > optionalCar = findByPerson( person );

        if ( optionalCar.isPresent() ) {
            return optionalCar.get();
        }

        throw new HttpNotFoundException( "CAR_NOT_FOUND" );
    }


    @Override
    protected Class< Car > getClassType() {
        return Car.class;
    }
}

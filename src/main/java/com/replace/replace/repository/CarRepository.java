package com.replace.replace.repository;

import com.replace.replace.entity.Car;
import com.replace.replace.entity.Person;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface CarRepository extends DefaultRepository< Car > {

    Optional< Car > findByPerson( Person person );


    Car findOrFailByPerson( Person person );
}

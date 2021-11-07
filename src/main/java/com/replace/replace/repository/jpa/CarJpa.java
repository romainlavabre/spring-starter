package com.replace.replace.repository.jpa;

import com.replace.replace.entity.Car;
import com.replace.replace.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Repository
public interface CarJpa extends JpaRepository< Car, Long > {
    
    Optional< Car > findByPerson( Person person );
}

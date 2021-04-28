package com.replace.replace.dao;

import com.replace.replace.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface CategoryDao extends JpaRepository< Category, Integer > {
}

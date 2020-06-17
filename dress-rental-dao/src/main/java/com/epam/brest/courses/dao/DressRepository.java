package com.epam.brest.courses.dao;


import com.epam.brest.courses.model.Dress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Dress repository.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface DressRepository extends JpaRepository<Dress, Integer> {

    Optional<Dress> findByDressName(String dressName);
}

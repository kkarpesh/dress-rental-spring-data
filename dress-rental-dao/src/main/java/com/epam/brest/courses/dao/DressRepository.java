package com.epam.brest.courses.dao;


import com.epam.brest.courses.model.Dress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    /**
     * Finds dress by dress name.
     * @param dressName dress name.
     * @return optional of dress.
     */
    Optional<Dress> findByDressName(String dressName);

    /**
     * Find dress by dress name except given id.
     * @param dressName dress name.
     * @param dressId dress id.
     * @return optional of dress.
     */
    @Query("select d from #{#entityName} d where d.dressName = ?1 and d.dressId <> ?2" )
    Optional<Dress> findByDressNameWhereDressIdNot(String dressName, Integer dressId);
}

package com.epam.brest.courses.service_api;

import com.epam.brest.courses.model.dto.DressDto;

import java.util.List;
import java.util.Optional;

/**
 * A service interface that defines the methods
 * of working with the Dress model.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
public interface DressService {

    /**
     * Finds all dresses with number of orders.
     *
     * @return dresses list.
     */
    List<DressDto> findAllWithNumberOfOrders();

    /**
     * Finds dress by Id.
     *
     * @param dressId dress Id.
     * @return dress.
     */
    Optional<DressDto> findById(Integer dressId);

    /**
     * Creates a new dress or updates an existing one.
     *
     * @param dressDto dressDto.
     * @return created dress Id.
     */
    Integer createOrUpdate(DressDto dressDto);

    /**
     * Deletes dress.
     *
     * @param dressId dress Id.
     * @return number of deleted records in the database.
     */
    Integer delete(Integer dressId);

    /**
     * Checks if the name of the dress is already exist.
     *
     * @param dressDto dressDto.
     * @return the boolean value of the existence of a name.
     */
    Boolean isNameAlreadyExist(DressDto dressDto);

    /**
     * Checks if the dress with a given ID has orders.
     *
     * @param dressId dress ID.
     * @return the boolean value is there a dress orders.
     */
    Boolean isDressHasRents(Integer dressId);
}

package com.epam.brest.courses.dao;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Rent repository.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface RentRepository extends JpaRepository<Rent, Integer> {

    /**
     * Counts rents by dress.
     * @param dress dress.
     * @return count of rents for dress.
     */
    Integer countByDress(Dress dress);

    /**
     * Finds rents between dates.
     * @param dateFrom date from.
     * @param dateTo date to.
     * @return list of rents.
     */
    List<Rent> findByRentDateBetween(LocalDate dateFrom, LocalDate dateTo);

    /**
     * Finds rent with certain dress on date.
     * @param rentDate rent date.
     * @param dress dress.
     * @return optional rent.
     */
    Optional<Rent> findByRentDateAndDress(LocalDate rentDate, Dress dress);

}

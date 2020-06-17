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

    Integer countByDress(Dress dress);

    List<Rent> findByRentDateBetween(LocalDate dateFrom, LocalDate dateTo);

    Optional<Rent> findByRentDateAndDress(LocalDate rentDate, Dress dress);

}

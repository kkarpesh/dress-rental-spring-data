package com.epam.brest.courses.service_api;

import com.epam.brest.courses.model.dto.RentDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * A service interface that defines the methods
 * of working with the Rent model.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
public interface RentService {

    /**
     * Finds rents with dress name for a given period of time.
     *
     * @param dateFrom period start date.
     * @param dateTo period finish date.
     * @return rents with dress name for a given period of time.
     */
    List<RentDto> findAllByDate(LocalDate dateFrom,
                                LocalDate dateTo);

    /**
     * Finds rent by Id.
     *
     * @param rentId rent Id.
     * @return dress.
     */
    Optional<RentDto> findById(Integer rentId);

    /**
     * Creates new rent.
     *
     * @param rentDto rentDto.
     * @return created rent Id.
     */
    Integer createOrUpdate(RentDto rentDto);

//    /**
//     * Updates rent.
//     *
//     * @param rent rent.
//     * @return number of updated records in the database.
//     */
//    Integer update(Rent rent);

    /**
     * Deletes rent.
     *
     * @param rentId rent Id.
     * @return number of deleted records in the database.
     */
    Integer delete(Integer rentId);

    /**
     * Checks if dress rented for this date.
     *
     * @param rentDto rentDto.
     * @return true if dress has already been rented
     * for this date and false if not.
     */
    Boolean isDressRented(RentDto rentDto);
}

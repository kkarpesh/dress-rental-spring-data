package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.DressRepository;
import com.epam.brest.courses.dao.RentRepository;
import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.RentDto;
import com.epam.brest.courses.service_api.RentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A service class that defines how to work
 * with the Rent model.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional
public class RentServiceImpl implements RentService {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(RentServiceImpl.class);

    /**
     * A rent repository.
     */
    private final RentRepository rentRepository;

    /**
     * A dress repository.
     */
    private final DressRepository dressRepository;

    /**
     * Constructs new object with given repository.
     *
     * @param rentRepository  rent repository.
     * @param dressRepository dress repository.
     */
    @Autowired
    public RentServiceImpl(RentRepository rentRepository,
                           DressRepository dressRepository) {
        this.rentRepository = rentRepository;
        this.dressRepository = dressRepository;
    }

    /**
     * Finds rents with dress name for a given period of time.
     *
     * @param dateFrom period start date.
     * @param dateTo   period finish date.
     * @return rents with dress name for a given period of time.
     */
    @Override
    public List<RentDto> findAllByDate(LocalDate dateFrom,
                                       LocalDate dateTo) {
        LOGGER.debug("Find all rents with dress name from {} to {}",
                dateFrom,
                dateTo);

        List<Rent> rents =
                rentRepository.findByRentDateBetween(dateFrom, dateTo);
        List<RentDto> rentDtos = new ArrayList<>();
        for (Rent rent : rents) {
            RentDto rentDto = new RentDto();
            rentDto.setRentId(rent.getRentId());
            rentDto.setClient(rent.getClient());
            rentDto.setRentDate(rent.getRentDate());
            rentDto.setDressName(rent.getDress().getDressName());
            rentDtos.add(rentDto);
        }
        return rentDtos;
    }

    /**
     * Finds rent by Id.
     *
     * @param rentId rent Id.
     * @return a Optional description of the rent found.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RentDto> findById(Integer rentId) {
        LOGGER.debug("Find rent by id {}", rentId);
        Optional<Rent> rent = rentRepository.findById(rentId);
        if (rent.isEmpty()) {
            return Optional.empty();
        } else {
            RentDto rentDto = new RentDto();
            rentDto.setRentId(rent.get().getRentId());
            rentDto.setClient(rent.get().getClient());
            rentDto.setRentDate(rent.get().getRentDate());
            rentDto.setDressName(rent.get().getDress().getDressName());
            return Optional.of(rentDto);
        }
    }

    /**
     * Creates new rent.
     *
     * @param rentDto rentDto.
     * @return created rent Id.
     */
    @Override
    public Integer createOrUpdate(RentDto rentDto) {
        LOGGER.debug("Create new rent {}", rentDto);
        Optional<Dress> dress =
                dressRepository.findByDressName(rentDto.getDressName());
        if (dress.isEmpty()) {
            throw new IllegalArgumentException("Dress is not exist");
        }

        boolean isRented = isDressRented(rentDto);
        if (isRented) {
            throw new IllegalArgumentException("Dress is "
                    + "already rented on this date");
        } else {
            Rent rent = new Rent();
            rent.setRentId(rentDto.getRentId());
            rent.setClient(rentDto.getClient());
            rent.setRentDate(rentDto.getRentDate());
            rent.setDress(dress.get());
            Rent savedRent = rentRepository.save(rent);
            return savedRent.getRentId();
        }
    }

    /**
     * Deletes rent.
     *
     * @param rentId rent Id.
     * @return number of deleted records in the database.
     */
    @Override
    public Integer delete(Integer rentId) {
        LOGGER.debug("Delete rent with id = {}", rentId);
        Optional<Rent> rent = rentRepository.findById(rentId);
        if (rent.isEmpty()) {
            throw new IllegalArgumentException("Rent not exist");
        } else {
            rentRepository.deleteById(rentId);
            return 1;
        }
    }

    /**
     * Checks if dress rented for this date.
     *
     * @param rentDto rentDto.
     * @return true if dress has already been rented
     * for this date and false if not.
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean isDressRented(RentDto rentDto) {
        LOGGER.debug("is rent exists - {}", rentDto);
        Optional<Dress> dress =
                dressRepository.findByDressName(rentDto.getDressName());
        if (dress.isEmpty()) {
            throw new IllegalArgumentException("Dress is not exist");
        } else {
            Optional<Rent> rent = rentRepository
                    .findByRentDateAndDress(rentDto.getRentDate(), dress.get());
            return rent.isPresent();
        }
    }

}

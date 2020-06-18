package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.DressRepository;
import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.service_api.DressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A service class that defines how to work
 * with the Dress model.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional
public class DressServiceImpl implements DressService {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(DressServiceImpl.class);

    /**
     * A dress data access object.
     */
    private final DressRepository dressRepository;

    /**
     * Constructs new object with given DAO object.
     *
     * @param dressRepository dress repository.
     */
    @Autowired
    public DressServiceImpl(DressRepository dressRepository) {
        this.dressRepository = dressRepository;
    }

    /**
     * Finds all dresses.
     *
     * @return dresses list.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DressDto> findAllWithNumberOfOrders() {
        LOGGER.debug("Find all dresses with number of orders");
        List<Dress> dresses = dressRepository.findAll();
        List<DressDto> dressDtos = new ArrayList<>();
        for (Dress dress : dresses){
            DressDto dressDto = new DressDto();
            dressDto.setDressId(dress.getDressId());
            dressDto.setDressName(dress.getDressName());
            dressDto.setNumberOfOrders(dress.getRents().size());
            dressDtos.add(dressDto);
        }
        return dressDtos;
    }

    /**
     * Finds dress by Id.
     *
     * @param dressId dress Id.
     * @return a Optional description of the dress found.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DressDto> findById(Integer dressId) {
        LOGGER.debug("Find dress with id = {}", dressId);
        Optional<Dress> dress = dressRepository.findById(dressId);
        DressDto dressDto;
        if (dress.isEmpty()){
            return Optional.empty();
        } else {
            dressDto = new DressDto();
            dressDto.setDressId(dress.get().getDressId());
            dressDto.setDressName(dress.get().getDressName());
            dressDto.setNumberOfOrders(dress.get().getRents().size());
            return Optional.of(dressDto);
        }
    }

    /**
     * Creates new dress.
     *
     * @param dressDto dress.
     * @return created dress Id.
     */
    @Override
    public Integer createOrUpdate(DressDto dressDto) {
        LOGGER.debug("Create new dress {}", dressDto);
        Optional<Dress> optionalDress =
                dressRepository.findByDressName(dressDto.getDressName());

        if (optionalDress.isEmpty()) {
            Dress dress = new Dress();
            dress.setDressId(dressDto.getDressId());
            dress.setDressName(dressDto.getDressName());
            Dress savedDress = dressRepository.save(dress);
            return savedDress.getDressId();
        } else {
            throw new IllegalArgumentException("Dress with the same name " +
                    "is already exist in DB");
        }
    }

    /**
     * Deletes dress.
     *
     * @param dressId dress Id.
     * @return number of deleted records in the database.
     */
    @Override
    public Integer delete(Integer dressId) {
        LOGGER.debug("Delete dress by id = {}", dressId);
        Optional<Dress> dress = dressRepository.findById(dressId);
        if (dress.isEmpty()){
           throw new IllegalArgumentException("Dress not exist");
        }
        if (dress.get().getRents().size()>0){
            throw new UnsupportedOperationException("This dress has orders" +
                    " and cannot be removed.");
        } else {
            dressRepository.deleteById(dressId);
            return 1;
        }
    }

    /**
     * Checks if the name of the dress is already exist.
     *
     * @param dressDto dressDto.
     * @return the boolean value of the existence of a name.
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean isNameAlreadyExist(DressDto dressDto) {
        LOGGER.debug("is name exists - {}", dressDto);
        Optional<Dress> foundDress =
                dressRepository.findByDressName(dressDto.getDressName());
        return foundDress.isPresent();
    }

    /**
     * Checks if the dress with a given ID has orders.
     *
     * @param dressId dress ID.
     * @return the boolean value is there a dress orders.
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean isDressHasRents(Integer dressId) {
        LOGGER.debug("is dress id={} has rents", dressId);
        Optional<Dress> foundDress = dressRepository.findById(dressId);
        if (foundDress.isEmpty()){
            throw new IllegalArgumentException("Dress is not exist.");
        } else {
            return foundDress.get().getRents().size() > 0;
        }
    }
}

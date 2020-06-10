package com.epam.brest.courses.service.dto;

import com.epam.brest.courses.dao.DressRepository;
import com.epam.brest.courses.dao.dto.DressDtoDao;
import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.service_api.dto.DressDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * A service class that defines how to work
 * with the DressDto model.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
public class DressDtoServiceImpl implements DressDtoService {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(DressDtoServiceImpl.class);

    /**
     * A dressDto data access object.
     */
    private final DressRepository dressRepository;

    /**
     * Constructs new object with given DAO object.
     *
     * @param dressRepository dress repository.
     */
    public DressDtoServiceImpl(DressRepository dressRepository) {
        this.dressRepository = dressRepository;
    }

    /**
     * Finds dresses with number of orders.
     *
     * @return dresses with number of orders.
     */
    @Override
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
}

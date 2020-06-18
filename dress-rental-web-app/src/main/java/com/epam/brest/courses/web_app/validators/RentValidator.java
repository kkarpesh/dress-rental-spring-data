package com.epam.brest.courses.web_app.validators;

import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.RentDto;
import com.epam.brest.courses.service_api.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.epam.brest.courses.constants.RentConstants.RENT_CLIENT_SIZE;

/**
 * This class validate dress objects.
 */
@Component
public class RentValidator implements Validator {

    /**
     * Service layer object to get information of rent.
     */
    @Autowired
    private RentService rentService;

    /**
     * Can this Validator validate instances of the supplied clazz?
     *
     * @param clazz the Class that this Validator
     *              is being asked if it can validate
     * @return true if this Validator can indeed
     * validate instances of the supplied clazz.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return RentDto.class.isAssignableFrom(clazz);
    }

    /**
     * Validate the supplied target object,
     * which must be of a Class for which the supports(Class) method
     * typically has (or would) return true.
     *
     * @param target the object that is to be validated.
     * @param errors contextual state about the validation process.
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "client", "client.empty");
        ValidationUtils.rejectIfEmpty(errors, "rentDate", "rentDate.empty");

        RentDto rentDto = (RentDto) target;

        if (StringUtils.hasLength(rentDto.getClient())
                && rentDto.getClient().length() > RENT_CLIENT_SIZE) {
            errors.rejectValue("client", "client.maxSize");
        }

        if (rentService.isDressRented(rentDto)) {
            errors.rejectValue("dressName", "dressName.rented");
        }
    }
}

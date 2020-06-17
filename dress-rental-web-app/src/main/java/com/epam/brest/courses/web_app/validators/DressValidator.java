package com.epam.brest.courses.web_app.validators;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.service_api.DressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.epam.brest.courses.constants.DressConstants.DRESS_NAME_SIZE;

/**
 * This class validate dress objects.
 */
@Component
public class DressValidator implements Validator {

    /**
     * Service layer object to get information of dress.
     */
    @Autowired
    private DressService dressService;

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
        return Dress.class.isAssignableFrom(clazz);
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
        ValidationUtils.rejectIfEmpty(errors,
                "dressName", "dressName.empty");
        DressDto dressDto = (DressDto) target;

        if (StringUtils.hasLength(dressDto.getDressName())
                && dressDto.getDressName().length() > DRESS_NAME_SIZE) {
            errors.rejectValue("dressName",
                    "dressName.maxSize");
        }

        if (dressService.isNameAlreadyExist(dressDto)) {
            errors.rejectValue("dressName", "dressName.exist");
        }
    }
}

package com.epam.brest.courses.dao;


import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.epam.brest.courses.constants.DressConstants.*;

/**
 * Dress repository.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface DressRepository extends JpaRepository<Dress, Integer> {

    Optional<Dress> findByDressName(String dressName);
}

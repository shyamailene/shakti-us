package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Appconfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Appconfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppconfigRepository extends JpaRepository<Appconfig, Long>, JpaSpecificationExecutor<Appconfig> {
    @Cacheable(cacheNames = "appconfig")
    Optional<Appconfig> findOneByKey(String key);
}

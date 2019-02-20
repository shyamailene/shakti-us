package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Rangoli;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Spring Data  repository for the Rangoli entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RangoliRepository extends JpaRepository<Rangoli, Long> {
	Optional<Rangoli> findById(Long id);
	Optional<Rangoli> deleteById(Long id);
}

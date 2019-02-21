package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Fundraiser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Spring Data  repository for the Fundraiser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FundraiserRepository extends JpaRepository<Fundraiser, Long> {
	Optional<Fundraiser> findById(Long id);
	Optional<Fundraiser> deleteById(Long id);
}

package com.vemuri.shaktius.service;

import com.vemuri.shaktius.domain.Appconfig;
import com.vemuri.shaktius.repository.AppconfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Service Implementation for managing Appconfig.
 */
@Service
@Transactional
public class AppconfigService {

    private final Logger log = LoggerFactory.getLogger(AppconfigService.class);

    private final AppconfigRepository appconfigRepository;

    public AppconfigService(AppconfigRepository appconfigRepository) {
        this.appconfigRepository = appconfigRepository;
    }

    /**
     * Save a appconfig.
     *
     * @param appconfig the entity to save
     * @return the persisted entity
     */
    public Appconfig save(Appconfig appconfig) {
        log.debug("Request to save Appconfig : {}", appconfig);
        return appconfigRepository.save(appconfig);
    }

    /**
     * Get all the appconfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Appconfig> findAll(Pageable pageable) {
        log.debug("Request to get all Appconfigs");
        return appconfigRepository.findAll(pageable);
    }

    /**
     * Get one appconfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Appconfig findOne(Long id) {
        log.debug("Request to get Appconfig : {}", id);
        return appconfigRepository.findOne(id);
    }

    /**
     * Delete the appconfig by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Appconfig : {}", id);
        appconfigRepository.delete(id);
    }

    /**
     * Get one appconfig by id.
     *
     * @param key the key of the record
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Appconfig> findByKey(String key) {
        log.debug("Request to get Appconfig : {}", key);
        return appconfigRepository.findOneByKey(key);
    }
}

package com.vemuri.shaktius.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vemuri.shaktius.domain.Locations;

import com.vemuri.shaktius.repository.LocationsRepository;
import com.vemuri.shaktius.web.rest.errors.BadRequestAlertException;
import com.vemuri.shaktius.web.rest.util.HeaderUtil;
import com.vemuri.shaktius.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Locations.
 */
@RestController
@RequestMapping("/api")
public class LocationsResource {

    private final Logger log = LoggerFactory.getLogger(LocationsResource.class);

    private static final String ENTITY_NAME = "locations";

    private final LocationsRepository locationsRepository;

    public LocationsResource(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    /**
     * POST  /locations : Create a new locations.
     *
     * @param locations the locations to create
     * @return the ResponseEntity with status 201 (Created) and with body the new locations, or with status 400 (Bad Request) if the locations has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/locations")
    @Timed
    public ResponseEntity<Locations> createLocations(@Valid @RequestBody Locations locations) throws URISyntaxException {
        log.debug("REST request to save Locations : {}", locations);
        if (locations.getId() != null) {
            throw new BadRequestAlertException("A new locations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Locations result = locationsRepository.save(locations);
        return ResponseEntity.created(new URI("/api/locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /locations : Updates an existing locations.
     *
     * @param locations the locations to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated locations,
     * or with status 400 (Bad Request) if the locations is not valid,
     * or with status 500 (Internal Server Error) if the locations couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/locations")
    @Timed
    public ResponseEntity<Locations> updateLocations(@Valid @RequestBody Locations locations) throws URISyntaxException {
        log.debug("REST request to update Locations : {}", locations);
        if (locations.getId() == null) {
            return createLocations(locations);
        }
        Locations result = locationsRepository.save(locations);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, locations.getId().toString()))
            .body(result);
    }

    /**
     * GET  /locations : get all the locations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of locations in body
     */
    @GetMapping("/locations")
    @Timed
    public ResponseEntity<List<Locations>> getAllLocations(Pageable pageable) {
        log.debug("REST request to get a page of Locations");
        Page<Locations> page = locationsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/locations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /locations/:id : get the "id" locations.
     *
     * @param id the id of the locations to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the locations, or with status 404 (Not Found)
     */
    @GetMapping("/locations/{id}")
    @Timed
    public ResponseEntity<Locations> getLocations(@PathVariable Long id) {
        log.debug("REST request to get Locations : {}", id);
        Locations locations = locationsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(locations));
    }

    /**
     * DELETE  /locations/:id : delete the "id" locations.
     *
     * @param id the id of the locations to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/locations/{id}")
    @Timed
    public ResponseEntity<Void> deleteLocations(@PathVariable Long id) {
        log.debug("REST request to delete Locations : {}", id);
        locationsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

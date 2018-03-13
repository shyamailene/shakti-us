package com.vemuri.shaktius.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vemuri.shaktius.domain.Activities;

import com.vemuri.shaktius.repository.ActivitiesRepository;
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
 * REST controller for managing Activities.
 */
@RestController
@RequestMapping("/api")
public class ActivitiesResource {

    private final Logger log = LoggerFactory.getLogger(ActivitiesResource.class);

    private static final String ENTITY_NAME = "activities";

    private final ActivitiesRepository activitiesRepository;

    public ActivitiesResource(ActivitiesRepository activitiesRepository) {
        this.activitiesRepository = activitiesRepository;
    }

    /**
     * POST  /activities : Create a new activities.
     *
     * @param activities the activities to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activities, or with status 400 (Bad Request) if the activities has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activities")
    @Timed
    public ResponseEntity<Activities> createActivities(@Valid @RequestBody Activities activities) throws URISyntaxException {
        log.debug("REST request to save Activities : {}", activities);
        if (activities.getId() != null) {
            throw new BadRequestAlertException("A new activities cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Activities result = activitiesRepository.save(activities);
        return ResponseEntity.created(new URI("/api/activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activities : Updates an existing activities.
     *
     * @param activities the activities to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activities,
     * or with status 400 (Bad Request) if the activities is not valid,
     * or with status 500 (Internal Server Error) if the activities couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activities")
    @Timed
    public ResponseEntity<Activities> updateActivities(@Valid @RequestBody Activities activities) throws URISyntaxException {
        log.debug("REST request to update Activities : {}", activities);
        if (activities.getId() == null) {
            return createActivities(activities);
        }
        Activities result = activitiesRepository.save(activities);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activities.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activities : get all the activities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of activities in body
     */
    @GetMapping("/activities")
    @Timed
    public ResponseEntity<List<Activities>> getAllActivities(Pageable pageable) {
        log.debug("REST request to get a page of Activities");
        Page<Activities> page = activitiesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activities/:id : get the "id" activities.
     *
     * @param id the id of the activities to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activities, or with status 404 (Not Found)
     */
    @GetMapping("/activities/{id}")
    @Timed
    public ResponseEntity<Activities> getActivities(@PathVariable Long id) {
        log.debug("REST request to get Activities : {}", id);
        Activities activities = activitiesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(activities));
    }

    /**
     * DELETE  /activities/:id : delete the "id" activities.
     *
     * @param id the id of the activities to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activities/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivities(@PathVariable Long id) {
        log.debug("REST request to delete Activities : {}", id);
        activitiesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

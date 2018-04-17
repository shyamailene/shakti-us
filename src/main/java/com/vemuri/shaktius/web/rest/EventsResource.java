package com.vemuri.shaktius.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vemuri.shaktius.domain.Events;

import com.vemuri.shaktius.repository.EventsRepository;
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
 * REST controller for managing Events.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://www.shakti-us.org")
public class EventsResource {

    private final Logger log = LoggerFactory.getLogger(EventsResource.class);

    private static final String ENTITY_NAME = "events";

    private final EventsRepository eventsRepository;

    public EventsResource(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    /**
     * POST  /events : Create a new events.
     *
     * @param events the events to create
     * @return the ResponseEntity with status 201 (Created) and with body the new events, or with status 400 (Bad Request) if the events has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/events")
    @Timed
    public ResponseEntity<Events> createEvents(@Valid @RequestBody Events events) throws URISyntaxException {
        log.debug("REST request to save Events : {}", events);
        if (events.getId() != null) {
            throw new BadRequestAlertException("A new events cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Events result = eventsRepository.save(events);
        return ResponseEntity.created(new URI("/api/events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /events : Updates an existing events.
     *
     * @param events the events to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated events,
     * or with status 400 (Bad Request) if the events is not valid,
     * or with status 500 (Internal Server Error) if the events couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/events")
    @Timed
    public ResponseEntity<Events> updateEvents(@Valid @RequestBody Events events) throws URISyntaxException {
        log.debug("REST request to update Events : {}", events);
        if (events.getId() == null) {
            return createEvents(events);
        }
        Events result = eventsRepository.save(events);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, events.getId().toString()))
            .body(result);
    }

    /**
     * GET  /events : get all the events.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of events in body
     */
    @GetMapping("/events")
    @Timed
    public ResponseEntity<List<Events>> getAllEvents(Pageable pageable) {
        log.debug("REST request to get a page of Events");
        Page<Events> page = eventsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /events/:id : get the "id" events.
     *
     * @param id the id of the events to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the events, or with status 404 (Not Found)
     */
    @GetMapping("/events/{id}")
    @Timed
    public ResponseEntity<Events> getEvents(@PathVariable Long id) {
        log.debug("REST request to get Events : {}", id);
        Events events = eventsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(events));
    }

    /**
     * DELETE  /events/:id : delete the "id" events.
     *
     * @param id the id of the events to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/events/{id}")
    @Timed
    public ResponseEntity<Void> deleteEvents(@PathVariable Long id) {
        log.debug("REST request to delete Events : {}", id);
        eventsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

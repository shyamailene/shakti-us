package com.vemuri.shaktius.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vemuri.shaktius.domain.Committee;

import com.vemuri.shaktius.repository.CommitteeRepository;
import com.vemuri.shaktius.repository.search.CommitteeSearchRepository;
import com.vemuri.shaktius.web.rest.errors.BadRequestAlertException;
import com.vemuri.shaktius.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Committee.
 */
@RestController
@RequestMapping("/api")
public class CommitteeResource {

    private final Logger log = LoggerFactory.getLogger(CommitteeResource.class);

    private static final String ENTITY_NAME = "committee";

    private final CommitteeRepository committeeRepository;

    private final CommitteeSearchRepository committeeSearchRepository;

    public CommitteeResource(CommitteeRepository committeeRepository, CommitteeSearchRepository committeeSearchRepository) {
        this.committeeRepository = committeeRepository;
        this.committeeSearchRepository = committeeSearchRepository;
    }

    /**
     * POST  /committees : Create a new committee.
     *
     * @param committee the committee to create
     * @return the ResponseEntity with status 201 (Created) and with body the new committee, or with status 400 (Bad Request) if the committee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/committees")
    @Timed
    public ResponseEntity<Committee> createCommittee(@Valid @RequestBody Committee committee) throws URISyntaxException {
        log.debug("REST request to save Committee : {}", committee);
        if (committee.getId() != null) {
            throw new BadRequestAlertException("A new committee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Committee result = committeeRepository.save(committee);
        committeeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/committees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /committees : Updates an existing committee.
     *
     * @param committee the committee to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated committee,
     * or with status 400 (Bad Request) if the committee is not valid,
     * or with status 500 (Internal Server Error) if the committee couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/committees")
    @Timed
    public ResponseEntity<Committee> updateCommittee(@Valid @RequestBody Committee committee) throws URISyntaxException {
        log.debug("REST request to update Committee : {}", committee);
        if (committee.getId() == null) {
            return createCommittee(committee);
        }
        Committee result = committeeRepository.save(committee);
        committeeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, committee.getId().toString()))
            .body(result);
    }

    /**
     * GET  /committees : get all the committees.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of committees in body
     */
    @GetMapping("/committees")
    @Timed
    public List<Committee> getAllCommittees() {
        log.debug("REST request to get all Committees");
        return committeeRepository.findAll();
        }

    /**
     * GET  /committees/:id : get the "id" committee.
     *
     * @param id the id of the committee to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the committee, or with status 404 (Not Found)
     */
    @GetMapping("/committees/{id}")
    @Timed
    public ResponseEntity<Committee> getCommittee(@PathVariable Long id) {
        log.debug("REST request to get Committee : {}", id);
        Committee committee = committeeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(committee));
    }

    /**
     * DELETE  /committees/:id : delete the "id" committee.
     *
     * @param id the id of the committee to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/committees/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommittee(@PathVariable Long id) {
        log.debug("REST request to delete Committee : {}", id);
        committeeRepository.delete(id);
        committeeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/committees?query=:query : search for the committee corresponding
     * to the query.
     *
     * @param query the query of the committee search
     * @return the result of the search
     */
    @GetMapping("/_search/committees")
    @Timed
    public List<Committee> searchCommittees(@RequestParam String query) {
        log.debug("REST request to search Committees for query {}", query);
        return StreamSupport
            .stream(committeeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}

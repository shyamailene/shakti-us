package com.vemuri.shaktius.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vemuri.shaktius.config.AppConstants;
import com.vemuri.shaktius.domain.Appconfig;
import com.vemuri.shaktius.domain.Contactus;

import com.vemuri.shaktius.domain.User;
import com.vemuri.shaktius.repository.ContactusRepository;
import com.vemuri.shaktius.repository.UserRepository;
import com.vemuri.shaktius.security.SecurityUtils;
import com.vemuri.shaktius.service.AppconfigService;
import com.vemuri.shaktius.service.MailService;
import com.vemuri.shaktius.web.rest.errors.BadRequestAlertException;
import com.vemuri.shaktius.web.rest.util.HeaderUtil;
import com.vemuri.shaktius.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Contactus.
 */
@RestController
@RequestMapping("/api")
public class ContactusResource {

    private final Logger log = LoggerFactory.getLogger(ContactusResource.class);

    private static final String ENTITY_NAME = "contactus";

    private final ContactusRepository contactusRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private AppconfigService appconfigService;

    private final UserRepository userRepository;

    public ContactusResource(ContactusRepository contactusRepository,UserRepository userRepository) {
        this.contactusRepository = contactusRepository;
        this.userRepository=userRepository;
    }

    /**
     * POST  /contactuses : Create a new contactus.
     *
     * @param contactus the contactus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactus, or with status 400 (Bad Request) if the contactus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contactuses")
    @Timed
    public ResponseEntity<Contactus> createContactus(@RequestBody Contactus contactus) throws URISyntaxException {
        log.debug("REST request to save Contactus : {}", contactus);
        if (contactus.getId() != null) {
            throw new BadRequestAlertException("A new contactus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contactus result = contactusRepository.save(contactus);
        Optional<String> userid= SecurityUtils.getCurrentUserLogin();
        User user=userRepository.findOneByLogin(userid.get()).get();
        Optional<Appconfig> appConfig=appconfigService.findByKey(AppConstants.ADMIN_EMAIL);
        if(appConfig!=null) {
            String admin_mail_id = appConfig.get().getValue();
            mailService.sendEmail(user, result, admin_mail_id);
        }
        return ResponseEntity.created(new URI("/api/contactuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contactuses : Updates an existing contactus.
     *
     * @param contactus the contactus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactus,
     * or with status 400 (Bad Request) if the contactus is not valid,
     * or with status 500 (Internal Server Error) if the contactus couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contactuses")
    @Timed
    public ResponseEntity<Contactus> updateContactus(@RequestBody Contactus contactus) throws URISyntaxException {
        log.debug("REST request to update Contactus : {}", contactus);
        if (contactus.getId() == null) {
            return createContactus(contactus);
        }
        Contactus result = contactusRepository.save(contactus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contactuses : get all the contactuses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contactuses in body
     */
    @GetMapping("/contactuses")
    @Timed
    public ResponseEntity<List<Contactus>> getAllContactuses(Pageable pageable) {
        log.debug("REST request to get a page of Contactuses");
        Page<Contactus> page = contactusRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contactuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contactuses/:id : get the "id" contactus.
     *
     * @param id the id of the contactus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactus, or with status 404 (Not Found)
     */
    @GetMapping("/contactuses/{id}")
    @Timed
    public ResponseEntity<Contactus> getContactus(@PathVariable Long id) {
        log.debug("REST request to get Contactus : {}", id);
        Contactus contactus = contactusRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contactus));
    }

    /**
     * DELETE  /contactuses/:id : delete the "id" contactus.
     *
     * @param id the id of the contactus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contactuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteContactus(@PathVariable Long id) {
        log.debug("REST request to delete Contactus : {}", id);
        contactusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

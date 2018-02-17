package com.vemuri.shaktius.web.rest;

import com.vemuri.shaktius.ShaktiusApp;

import com.vemuri.shaktius.domain.Contactus;
import com.vemuri.shaktius.repository.ContactusRepository;
import com.vemuri.shaktius.repository.UserRepository;
import com.vemuri.shaktius.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.vemuri.shaktius.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContactusResource REST controller.
 *
 * @see ContactusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShaktiusApp.class)
public class ContactusResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_RELATEDTO = "AAAAAAAAAA";
    private static final String UPDATED_RELATEDTO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private ContactusRepository contactusRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactusMockMvc;

    private Contactus contactus;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactusResource contactusResource = new ContactusResource(contactusRepository,userRepository);
        this.restContactusMockMvc = MockMvcBuilders.standaloneSetup(contactusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contactus createEntity(EntityManager em) {
        Contactus contactus = new Contactus()
            .email(DEFAULT_EMAIL)
            .mobile(DEFAULT_MOBILE)
            .relatedto(DEFAULT_RELATEDTO)
            .content(DEFAULT_CONTENT);
        return contactus;
    }

    @Before
    public void initTest() {
        contactus = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactus() throws Exception {
        int databaseSizeBeforeCreate = contactusRepository.findAll().size();

        // Create the Contactus
        restContactusMockMvc.perform(post("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactus)))
            .andExpect(status().isCreated());

        // Validate the Contactus in the database
        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeCreate + 1);
        Contactus testContactus = contactusList.get(contactusList.size() - 1);
        assertThat(testContactus.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactus.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testContactus.getRelatedto()).isEqualTo(DEFAULT_RELATEDTO);
        assertThat(testContactus.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createContactusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactusRepository.findAll().size();

        // Create the Contactus with an existing ID
        contactus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactusMockMvc.perform(post("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactus)))
            .andExpect(status().isBadRequest());

        // Validate the Contactus in the database
        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContactuses() throws Exception {
        // Initialize the database
        contactusRepository.saveAndFlush(contactus);

        // Get all the contactusList
        restContactusMockMvc.perform(get("/api/contactuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactus.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].relatedto").value(hasItem(DEFAULT_RELATEDTO.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getContactus() throws Exception {
        // Initialize the database
        contactusRepository.saveAndFlush(contactus);

        // Get the contactus
        restContactusMockMvc.perform(get("/api/contactuses/{id}", contactus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactus.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.relatedto").value(DEFAULT_RELATEDTO.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactus() throws Exception {
        // Get the contactus
        restContactusMockMvc.perform(get("/api/contactuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactus() throws Exception {
        // Initialize the database
        contactusRepository.saveAndFlush(contactus);
        int databaseSizeBeforeUpdate = contactusRepository.findAll().size();

        // Update the contactus
        Contactus updatedContactus = contactusRepository.findOne(contactus.getId());
        // Disconnect from session so that the updates on updatedContactus are not directly saved in db
        em.detach(updatedContactus);
        updatedContactus
            .email(UPDATED_EMAIL)
            .mobile(UPDATED_MOBILE)
            .relatedto(UPDATED_RELATEDTO)
            .content(UPDATED_CONTENT);

        restContactusMockMvc.perform(put("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactus)))
            .andExpect(status().isOk());

        // Validate the Contactus in the database
        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeUpdate);
        Contactus testContactus = contactusList.get(contactusList.size() - 1);
        assertThat(testContactus.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactus.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testContactus.getRelatedto()).isEqualTo(UPDATED_RELATEDTO);
        assertThat(testContactus.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingContactus() throws Exception {
        int databaseSizeBeforeUpdate = contactusRepository.findAll().size();

        // Create the Contactus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContactusMockMvc.perform(put("/api/contactuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactus)))
            .andExpect(status().isCreated());

        // Validate the Contactus in the database
        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContactus() throws Exception {
        // Initialize the database
        contactusRepository.saveAndFlush(contactus);
        int databaseSizeBeforeDelete = contactusRepository.findAll().size();

        // Get the contactus
        restContactusMockMvc.perform(delete("/api/contactuses/{id}", contactus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contactus> contactusList = contactusRepository.findAll();
        assertThat(contactusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contactus.class);
        Contactus contactus1 = new Contactus();
        contactus1.setId(1L);
        Contactus contactus2 = new Contactus();
        contactus2.setId(contactus1.getId());
        assertThat(contactus1).isEqualTo(contactus2);
        contactus2.setId(2L);
        assertThat(contactus1).isNotEqualTo(contactus2);
        contactus1.setId(null);
        assertThat(contactus1).isNotEqualTo(contactus2);
    }
}

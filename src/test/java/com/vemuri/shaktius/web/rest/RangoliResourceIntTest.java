package com.vemuri.shaktius.web.rest;

import com.vemuri.shaktius.ShaktiusApp;

import com.vemuri.shaktius.domain.Rangoli;
import com.vemuri.shaktius.repository.RangoliRepository;
import com.vemuri.shaktius.service.RangoliService;
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
 * Test class for the RangoliResource REST controller.
 *
 * @see RangoliResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShaktiusApp.class)
public class RangoliResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String DEFAULT_EMAIL_2 = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INTERESTED = false;
    private static final Boolean UPDATED_INTERESTED = true;

    private static final String DEFAULT_VOLUNTEER = "AAAAAAAAAA";
    private static final String UPDATED_VOLUNTEER = "BBBBBBBBBB";

    @Autowired
    private RangoliRepository rangoliRepository;

    

    @Autowired
    private RangoliService rangoliService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRangoliMockMvc;

    private Rangoli rangoli;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RangoliResource rangoliResource = new RangoliResource(rangoliService);
        this.restRangoliMockMvc = MockMvcBuilders.standaloneSetup(rangoliResource)
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
    public static Rangoli createEntity(EntityManager em) {
        Rangoli rangoli = new Rangoli()
            .email(DEFAULT_EMAIL)
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE)
            .email2(DEFAULT_EMAIL_2)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .interested(DEFAULT_INTERESTED)
            .volunteer(DEFAULT_VOLUNTEER);
        return rangoli;
    }

    @Before
    public void initTest() {
        rangoli = createEntity(em);
    }

    @Test
    @Transactional
    public void createRangoli() throws Exception {
        int databaseSizeBeforeCreate = rangoliRepository.findAll().size();

        // Create the Rangoli
        restRangoliMockMvc.perform(post("/api/rangolis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rangoli)))
            .andExpect(status().isCreated());

        // Validate the Rangoli in the database
        List<Rangoli> rangoliList = rangoliRepository.findAll();
        assertThat(rangoliList).hasSize(databaseSizeBeforeCreate + 1);
        Rangoli testRangoli = rangoliList.get(rangoliList.size() - 1);
        assertThat(testRangoli.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRangoli.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRangoli.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testRangoli.getEmail2()).isEqualTo(DEFAULT_EMAIL_2);
        assertThat(testRangoli.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testRangoli.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testRangoli.isInterested()).isEqualTo(DEFAULT_INTERESTED);
        assertThat(testRangoli.getVolunteer()).isEqualTo(DEFAULT_VOLUNTEER);
    }

    @Test
    @Transactional
    public void createRangoliWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rangoliRepository.findAll().size();

        // Create the Rangoli with an existing ID
        rangoli.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRangoliMockMvc.perform(post("/api/rangolis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rangoli)))
            .andExpect(status().isBadRequest());

        // Validate the Rangoli in the database
        List<Rangoli> rangoliList = rangoliRepository.findAll();
        assertThat(rangoliList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = rangoliRepository.findAll().size();
        // set the field null
        rangoli.setEmail(null);

        // Create the Rangoli, which fails.

        restRangoliMockMvc.perform(post("/api/rangolis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rangoli)))
            .andExpect(status().isBadRequest());

        List<Rangoli> rangoliList = rangoliRepository.findAll();
        assertThat(rangoliList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRangolis() throws Exception {
        // Initialize the database
        rangoliRepository.saveAndFlush(rangoli);

        // Get all the rangoliList
        restRangoliMockMvc.perform(get("/api/rangolis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rangoli.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].interested").value(hasItem(DEFAULT_INTERESTED.booleanValue())))
            .andExpect(jsonPath("$.[*].volunteer").value(hasItem(DEFAULT_VOLUNTEER.toString())));
    }
    

    @Test
    @Transactional
    public void getRangoli() throws Exception {
        // Initialize the database
        rangoliRepository.saveAndFlush(rangoli);

        // Get the rangoli
        restRangoliMockMvc.perform(get("/api/rangolis/{id}", rangoli.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rangoli.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.email2").value(DEFAULT_EMAIL_2.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.interested").value(DEFAULT_INTERESTED.booleanValue()))
            .andExpect(jsonPath("$.volunteer").value(DEFAULT_VOLUNTEER.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRangoli() throws Exception {
        // Get the rangoli
        restRangoliMockMvc.perform(get("/api/rangolis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRangoli() throws Exception {
        // Initialize the database
        rangoliService.save(rangoli);

        int databaseSizeBeforeUpdate = rangoliRepository.findAll().size();

        // Update the rangoli
        Rangoli updatedRangoli = rangoliRepository.findById(rangoli.getId()).get();
        // Disconnect from session so that the updates on updatedRangoli are not directly saved in db
        em.detach(updatedRangoli);
        updatedRangoli
            .email(UPDATED_EMAIL)
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .email2(UPDATED_EMAIL_2)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .interested(UPDATED_INTERESTED)
            .volunteer(UPDATED_VOLUNTEER);

        restRangoliMockMvc.perform(put("/api/rangolis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRangoli)))
            .andExpect(status().isOk());

        // Validate the Rangoli in the database
        List<Rangoli> rangoliList = rangoliRepository.findAll();
        assertThat(rangoliList).hasSize(databaseSizeBeforeUpdate);
        Rangoli testRangoli = rangoliList.get(rangoliList.size() - 1);
        assertThat(testRangoli.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRangoli.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRangoli.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testRangoli.getEmail2()).isEqualTo(UPDATED_EMAIL_2);
        assertThat(testRangoli.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testRangoli.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRangoli.isInterested()).isEqualTo(UPDATED_INTERESTED);
        assertThat(testRangoli.getVolunteer()).isEqualTo(UPDATED_VOLUNTEER);
    }

    @Test
    @Transactional
    public void updateNonExistingRangoli() throws Exception {
        int databaseSizeBeforeUpdate = rangoliRepository.findAll().size();

        // Create the Rangoli

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restRangoliMockMvc.perform(put("/api/rangolis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rangoli)))
            .andExpect(status().isBadRequest());

        // Validate the Rangoli in the database
        List<Rangoli> rangoliList = rangoliRepository.findAll();
        assertThat(rangoliList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRangoli() throws Exception {
        // Initialize the database
        rangoliService.save(rangoli);

        int databaseSizeBeforeDelete = rangoliRepository.findAll().size();

        // Get the rangoli
        restRangoliMockMvc.perform(delete("/api/rangolis/{id}", rangoli.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rangoli> rangoliList = rangoliRepository.findAll();
        assertThat(rangoliList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rangoli.class);
        Rangoli rangoli1 = new Rangoli();
        rangoli1.setId(1L);
        Rangoli rangoli2 = new Rangoli();
        rangoli2.setId(rangoli1.getId());
        assertThat(rangoli1).isEqualTo(rangoli2);
        rangoli2.setId(2L);
        assertThat(rangoli1).isNotEqualTo(rangoli2);
        rangoli1.setId(null);
        assertThat(rangoli1).isNotEqualTo(rangoli2);
    }
}

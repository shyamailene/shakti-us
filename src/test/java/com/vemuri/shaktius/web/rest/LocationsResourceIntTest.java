package com.vemuri.shaktius.web.rest;

import com.vemuri.shaktius.ShaktiusApp;

import com.vemuri.shaktius.domain.Locations;
import com.vemuri.shaktius.repository.LocationsRepository;
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
 * Test class for the LocationsResource REST controller.
 *
 * @see LocationsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShaktiusApp.class)
public class LocationsResourceIntTest {

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    @Autowired
    private LocationsRepository locationsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLocationsMockMvc;

    private Locations locations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LocationsResource locationsResource = new LocationsResource(locationsRepository);
        this.restLocationsMockMvc = MockMvcBuilders.standaloneSetup(locationsResource)
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
    public static Locations createEntity(EntityManager em) {
        Locations locations = new Locations()
            .country(DEFAULT_COUNTRY)
            .state(DEFAULT_STATE)
            .city(DEFAULT_CITY);
        return locations;
    }

    @Before
    public void initTest() {
        locations = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocations() throws Exception {
        int databaseSizeBeforeCreate = locationsRepository.findAll().size();

        // Create the Locations
        restLocationsMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locations)))
            .andExpect(status().isCreated());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeCreate + 1);
        Locations testLocations = locationsList.get(locationsList.size() - 1);
        assertThat(testLocations.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testLocations.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testLocations.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    @Transactional
    public void createLocationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = locationsRepository.findAll().size();

        // Create the Locations with an existing ID
        locations.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationsMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locations)))
            .andExpect(status().isBadRequest());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationsRepository.findAll().size();
        // set the field null
        locations.setCountry(null);

        // Create the Locations, which fails.

        restLocationsMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locations)))
            .andExpect(status().isBadRequest());

        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationsRepository.findAll().size();
        // set the field null
        locations.setState(null);

        // Create the Locations, which fails.

        restLocationsMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locations)))
            .andExpect(status().isBadRequest());

        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationsRepository.findAll().size();
        // set the field null
        locations.setCity(null);

        // Create the Locations, which fails.

        restLocationsMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locations)))
            .andExpect(status().isBadRequest());

        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocations() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList
        restLocationsMockMvc.perform(get("/api/locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locations.getId().intValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())));
    }

    @Test
    @Transactional
    public void getLocations() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get the locations
        restLocationsMockMvc.perform(get("/api/locations/{id}", locations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(locations.getId().intValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLocations() throws Exception {
        // Get the locations
        restLocationsMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocations() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);
        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();

        // Update the locations
        Locations updatedLocations = locationsRepository.findOne(locations.getId());
        // Disconnect from session so that the updates on updatedLocations are not directly saved in db
        em.detach(updatedLocations);
        updatedLocations
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .city(UPDATED_CITY);

        restLocationsMockMvc.perform(put("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLocations)))
            .andExpect(status().isOk());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate);
        Locations testLocations = locationsList.get(locationsList.size() - 1);
        assertThat(testLocations.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testLocations.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testLocations.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    public void updateNonExistingLocations() throws Exception {
        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();

        // Create the Locations

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLocationsMockMvc.perform(put("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locations)))
            .andExpect(status().isCreated());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLocations() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);
        int databaseSizeBeforeDelete = locationsRepository.findAll().size();

        // Get the locations
        restLocationsMockMvc.perform(delete("/api/locations/{id}", locations.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Locations.class);
        Locations locations1 = new Locations();
        locations1.setId(1L);
        Locations locations2 = new Locations();
        locations2.setId(locations1.getId());
        assertThat(locations1).isEqualTo(locations2);
        locations2.setId(2L);
        assertThat(locations1).isNotEqualTo(locations2);
        locations1.setId(null);
        assertThat(locations1).isNotEqualTo(locations2);
    }
}

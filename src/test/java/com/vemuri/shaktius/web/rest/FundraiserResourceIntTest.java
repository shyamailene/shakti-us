package com.vemuri.shaktius.web.rest;

import com.vemuri.shaktius.ShaktiusApp;

import com.vemuri.shaktius.domain.Fundraiser;
import com.vemuri.shaktius.repository.FundraiserRepository;
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
 * Test class for the FundraiserResource REST controller.
 *
 * @see FundraiserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShaktiusApp.class)
public class FundraiserResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_2 = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONENUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONENUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_VOLUNTEER = "AAAAAAAAAA";
    private static final String UPDATED_VOLUNTEER = "BBBBBBBBBB";

    @Autowired
    private FundraiserRepository fundraiserRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFundraiserMockMvc;

    private Fundraiser fundraiser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FundraiserResource fundraiserResource = new FundraiserResource(fundraiserRepository);
        this.restFundraiserMockMvc = MockMvcBuilders.standaloneSetup(fundraiserResource)
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
    public static Fundraiser createEntity(EntityManager em) {
        Fundraiser fundraiser = new Fundraiser()
            .email(DEFAULT_EMAIL)
            .name(DEFAULT_NAME)
            .email2(DEFAULT_EMAIL_2)
            .address(DEFAULT_ADDRESS)
            .phonenumber(DEFAULT_PHONENUMBER)
            .volunteer(DEFAULT_VOLUNTEER);
        return fundraiser;
    }

    @Before
    public void initTest() {
        fundraiser = createEntity(em);
    }

    @Test
    @Transactional
    public void createFundraiser() throws Exception {
        int databaseSizeBeforeCreate = fundraiserRepository.findAll().size();

        // Create the Fundraiser
        restFundraiserMockMvc.perform(post("/api/fundraisers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fundraiser)))
            .andExpect(status().isCreated());

        // Validate the Fundraiser in the database
        List<Fundraiser> fundraiserList = fundraiserRepository.findAll();
        assertThat(fundraiserList).hasSize(databaseSizeBeforeCreate + 1);
        Fundraiser testFundraiser = fundraiserList.get(fundraiserList.size() - 1);
        assertThat(testFundraiser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFundraiser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFundraiser.getEmail2()).isEqualTo(DEFAULT_EMAIL_2);
        assertThat(testFundraiser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testFundraiser.getPhonenumber()).isEqualTo(DEFAULT_PHONENUMBER);
        assertThat(testFundraiser.getVolunteer()).isEqualTo(DEFAULT_VOLUNTEER);
    }

    @Test
    @Transactional
    public void createFundraiserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fundraiserRepository.findAll().size();

        // Create the Fundraiser with an existing ID
        fundraiser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFundraiserMockMvc.perform(post("/api/fundraisers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fundraiser)))
            .andExpect(status().isBadRequest());

        // Validate the Fundraiser in the database
        List<Fundraiser> fundraiserList = fundraiserRepository.findAll();
        assertThat(fundraiserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFundraisers() throws Exception {
        // Initialize the database
        fundraiserRepository.saveAndFlush(fundraiser);

        // Get all the fundraiserList
        restFundraiserMockMvc.perform(get("/api/fundraisers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fundraiser.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phonenumber").value(hasItem(DEFAULT_PHONENUMBER.toString())))
            .andExpect(jsonPath("$.[*].volunteer").value(hasItem(DEFAULT_VOLUNTEER.toString())));
    }
    

    @Test
    @Transactional
    public void getFundraiser() throws Exception {
        // Initialize the database
        fundraiserRepository.saveAndFlush(fundraiser);

        // Get the fundraiser
        restFundraiserMockMvc.perform(get("/api/fundraisers/{id}", fundraiser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fundraiser.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email2").value(DEFAULT_EMAIL_2.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phonenumber").value(DEFAULT_PHONENUMBER.toString()))
            .andExpect(jsonPath("$.volunteer").value(DEFAULT_VOLUNTEER.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingFundraiser() throws Exception {
        // Get the fundraiser
        restFundraiserMockMvc.perform(get("/api/fundraisers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFundraiser() throws Exception {
        // Initialize the database
        fundraiserRepository.saveAndFlush(fundraiser);

        int databaseSizeBeforeUpdate = fundraiserRepository.findAll().size();

        // Update the fundraiser
        Fundraiser updatedFundraiser = fundraiserRepository.findById(fundraiser.getId()).get();
        // Disconnect from session so that the updates on updatedFundraiser are not directly saved in db
        em.detach(updatedFundraiser);
        updatedFundraiser
            .email(UPDATED_EMAIL)
            .name(UPDATED_NAME)
            .email2(UPDATED_EMAIL_2)
            .address(UPDATED_ADDRESS)
            .phonenumber(UPDATED_PHONENUMBER)
            .volunteer(UPDATED_VOLUNTEER);

        restFundraiserMockMvc.perform(put("/api/fundraisers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFundraiser)))
            .andExpect(status().isOk());

        // Validate the Fundraiser in the database
        List<Fundraiser> fundraiserList = fundraiserRepository.findAll();
        assertThat(fundraiserList).hasSize(databaseSizeBeforeUpdate);
        Fundraiser testFundraiser = fundraiserList.get(fundraiserList.size() - 1);
        assertThat(testFundraiser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFundraiser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFundraiser.getEmail2()).isEqualTo(UPDATED_EMAIL_2);
        assertThat(testFundraiser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testFundraiser.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testFundraiser.getVolunteer()).isEqualTo(UPDATED_VOLUNTEER);
    }

    @Test
    @Transactional
    public void updateNonExistingFundraiser() throws Exception {
        int databaseSizeBeforeUpdate = fundraiserRepository.findAll().size();

        // Create the Fundraiser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restFundraiserMockMvc.perform(put("/api/fundraisers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fundraiser)))
            .andExpect(status().isBadRequest());

        // Validate the Fundraiser in the database
        List<Fundraiser> fundraiserList = fundraiserRepository.findAll();
        assertThat(fundraiserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFundraiser() throws Exception {
        // Initialize the database
        fundraiserRepository.saveAndFlush(fundraiser);

        int databaseSizeBeforeDelete = fundraiserRepository.findAll().size();

        // Get the fundraiser
        restFundraiserMockMvc.perform(delete("/api/fundraisers/{id}", fundraiser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Fundraiser> fundraiserList = fundraiserRepository.findAll();
        assertThat(fundraiserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fundraiser.class);
        Fundraiser fundraiser1 = new Fundraiser();
        fundraiser1.setId(1L);
        Fundraiser fundraiser2 = new Fundraiser();
        fundraiser2.setId(fundraiser1.getId());
        assertThat(fundraiser1).isEqualTo(fundraiser2);
        fundraiser2.setId(2L);
        assertThat(fundraiser1).isNotEqualTo(fundraiser2);
        fundraiser1.setId(null);
        assertThat(fundraiser1).isNotEqualTo(fundraiser2);
    }
}

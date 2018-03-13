package com.vemuri.shaktius.web.rest;

import com.vemuri.shaktius.ShaktiusApp;

import com.vemuri.shaktius.domain.Events;
import com.vemuri.shaktius.repository.EventsRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.vemuri.shaktius.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EventsResource REST controller.
 *
 * @see EventsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShaktiusApp.class)
public class EventsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventsMockMvc;

    private Events events;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventsResource eventsResource = new EventsResource(eventsRepository);
        this.restEventsMockMvc = MockMvcBuilders.standaloneSetup(eventsResource)
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
    public static Events createEntity(EntityManager em) {
        Events events = new Events()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION)
            .date(DEFAULT_DATE);
        return events;
    }

    @Before
    public void initTest() {
        events = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvents() throws Exception {
        int databaseSizeBeforeCreate = eventsRepository.findAll().size();

        // Create the Events
        restEventsMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(events)))
            .andExpect(status().isCreated());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeCreate + 1);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEvents.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEvents.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvents.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testEvents.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createEventsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventsRepository.findAll().size();

        // Create the Events with an existing ID
        events.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventsMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(events)))
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        // set the field null
        events.setName(null);

        // Create the Events, which fails.

        restEventsMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(events)))
            .andExpect(status().isBadRequest());

        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        // set the field null
        events.setType(null);

        // Create the Events, which fails.

        restEventsMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(events)))
            .andExpect(status().isBadRequest());

        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList
        restEventsMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(events.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get the events
        restEventsMockMvc.perform(get("/api/events/{id}", events.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(events.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvents() throws Exception {
        // Get the events
        restEventsMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();

        // Update the events
        Events updatedEvents = eventsRepository.findOne(events.getId());
        // Disconnect from session so that the updates on updatedEvents are not directly saved in db
        em.detach(updatedEvents);
        updatedEvents
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .date(UPDATED_DATE);

        restEventsMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvents)))
            .andExpect(status().isOk());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEvents.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEvents.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvents.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEvents.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();

        // Create the Events

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEventsMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(events)))
            .andExpect(status().isCreated());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);
        int databaseSizeBeforeDelete = eventsRepository.findAll().size();

        // Get the events
        restEventsMockMvc.perform(delete("/api/events/{id}", events.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Events.class);
        Events events1 = new Events();
        events1.setId(1L);
        Events events2 = new Events();
        events2.setId(events1.getId());
        assertThat(events1).isEqualTo(events2);
        events2.setId(2L);
        assertThat(events1).isNotEqualTo(events2);
        events1.setId(null);
        assertThat(events1).isNotEqualTo(events2);
    }
}

package pl.edu.podwozka.podwozkasrv.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.podwozka.podwozkasrv.PodwozkaSrvApplication;
import pl.edu.podwozka.podwozkasrv.domain.Travel;
import pl.edu.podwozka.podwozkasrv.repository.TravelRepository;
import pl.edu.podwozka.podwozkasrv.service.TravelService;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelDTO;
import pl.edu.podwozka.podwozkasrv.service.mapper.TravelMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PodwozkaSrvApplication.class)
public class TravelResourceIntTest {

    private static final String DEFAULT_LOGIN = "jan123";
    private static final String UPDATED_LOGIN = "janUpdated123";

    private static final Long DEFAULT_ID = 1L;

    private static final String DEFAULT_START_PLACE = "Home";
    private static final String UPDATED_START_PLACE = "HomeUpdated";

    private static final String DEFAULT_END_PLACE = "University";
    private static final String UPDATED_END_PLACE = "UniversityUpdated";

    private static final String DEFAULT_FIRSTNAME = "jan";
    private static final String UPDATED_FIRSTNAME = "janUpdated";

    private static final String DEFAULT_LASTNAME = "kowal";
    private static final String UPDATED_LASTNAME = "kowalUpdated";

    private static final Long DEFAULT_PASSENGERS_COUNT = 1L;
    private static final Long UPDATED_PASSENGERS_COUNT = 2L;

    private static final LocalDateTime DEFAULT_LOCAL_DATETIME = LocalDateTime.parse("2018-04-06T09:01:10");
    private static final Timestamp DEFAULT_TIMESTAMP = Timestamp.valueOf(DEFAULT_LOCAL_DATETIME);

    private static final LocalDateTime UPDATED_LOCAL_DATETIME = LocalDateTime.parse("2008-04-10T10:10:10");
    private static final Timestamp UPDATED_TIMESTAMP = Timestamp.valueOf(UPDATED_LOCAL_DATETIME);

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private TravelMapper travelMapper;

    @Autowired
    private TravelService travelService;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    @Qualifier("podwozkaJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    private MockMvc restTravelMockMvc;

    private Travel travel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TravelResource travelResource = new TravelResource(travelService);
        this.restTravelMockMvc = MockMvcBuilders
                .standaloneSetup(travelResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jackson2HttpMessageConverter)
                .build();

        travel = createEntity();
    }

    /**
     * Create a Travel.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the Travel entity.
     */
    public static Travel createEntity() {
        Travel travel = new Travel();
        travel.setLogin(DEFAULT_LOGIN);
        travel.setStartPlace(DEFAULT_START_PLACE);
        travel.setEndPlace(DEFAULT_END_PLACE);
        travel.setFirstName(DEFAULT_FIRSTNAME);
        travel.setLastName(DEFAULT_LASTNAME);
        travel.setPassengersCount(DEFAULT_PASSENGERS_COUNT);
        travel.setPickUpDatetime(DEFAULT_TIMESTAMP);
        return travel;
    }

    @Test
    @Transactional
    public void createTravel() throws Exception {
        int databaseSizeBeforeCreate = travelRepository.findAll().size();

        TravelDTO travelDTO = new TravelDTO(travel);

        restTravelMockMvc.perform(post("/api/travels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(travelDTO)))
                .andExpect(status().isCreated());

        // Validate the Travel in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeCreate + 1);
        Travel testTravel = travelList.get(travelList.size() - 1);
        assertThat(testTravel.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testTravel.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testTravel.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testTravel.getStartPlace()).isEqualTo(DEFAULT_START_PLACE);
        assertThat(testTravel.getEndPlace()).isEqualTo(DEFAULT_END_PLACE);
        assertThat(testTravel.getPassengersCount()).isEqualTo(DEFAULT_PASSENGERS_COUNT);
        assertThat(testTravel.getPickUpDatetime()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createTravelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = travelRepository.findAll().size();

        TravelDTO travelDTO = new TravelDTO(travel);
        // A travel with id 1 was inserted in test createTravel
        travelDTO.setId(DEFAULT_ID);

        restTravelMockMvc.perform(post("/api/travels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(travelDTO)))
                .andExpect(status().isBadRequest());

        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTravelsByLogin() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        // Get all the travels
        restTravelMockMvc.perform(get("/api/travels?login=" + DEFAULT_LOGIN)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRSTNAME)))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LASTNAME)))
                .andExpect(jsonPath("$.[*].startPlace").value(hasItem(DEFAULT_START_PLACE)))
                .andExpect(jsonPath("$.[*].endPlace").value(hasItem(DEFAULT_END_PLACE)))
                .andExpect(jsonPath("$.[*].passengersCount").value(hasItem(
                        DEFAULT_PASSENGERS_COUNT.intValue())))
                .andExpect(jsonPath("$.[*].pickUpDatetime").value(hasItem(
                        DEFAULT_LOCAL_DATETIME.toString())));
    }


    @Test
    @Transactional
    public void getTravel() throws Exception {
        // Initialize the database
        Travel savedTravel = travelRepository.saveAndFlush(travel);

        // Get the travel
        restTravelMockMvc.perform(get("/api/travels/" + savedTravel.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
                .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(DEFAULT_LASTNAME))
                .andExpect(jsonPath("$.startPlace").value(DEFAULT_START_PLACE))
                .andExpect(jsonPath("$.endPlace").value(DEFAULT_END_PLACE))
                .andExpect(jsonPath("$.passengersCount").value(DEFAULT_PASSENGERS_COUNT))
                .andExpect(jsonPath("$.pickUpDatetime").value(is(DEFAULT_LOCAL_DATETIME.toString())));
    }

    @Test
    @Transactional
    public void getNonExistingTravel() throws Exception {
        restTravelMockMvc.perform(get("/api/travels/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTravel() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);
        int databaseSizeBeforeUpdate = travelRepository.findAll().size();

        // Update the travel
        travelRepository.findById(travel.getId()).get();
        TravelDTO updatedTravel = new TravelDTO(travel);

        updatedTravel.setLogin(UPDATED_LOGIN);
        updatedTravel.setStartPlace(UPDATED_START_PLACE);
        updatedTravel.setEndPlace(UPDATED_END_PLACE);
        updatedTravel.setFirstName(UPDATED_FIRSTNAME);
        updatedTravel.setLastName(UPDATED_LASTNAME);
        updatedTravel.setPassengersCount(UPDATED_PASSENGERS_COUNT);
        updatedTravel.setPickUpDatetime(UPDATED_LOCAL_DATETIME);


        restTravelMockMvc.perform(put("/api/travels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTravel)))
                .andExpect(status().isOk());

        // Validate the travel in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeUpdate);

        Travel testTravel = travelList.get(travelList.size() - 1);
        assertThat(testTravel.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testTravel.getStartPlace()).isEqualTo(UPDATED_START_PLACE);
        assertThat(testTravel.getEndPlace()).isEqualTo(UPDATED_END_PLACE);
        assertThat(testTravel.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testTravel.getLastName()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testTravel.getPassengersCount()).isEqualTo(UPDATED_PASSENGERS_COUNT);
        assertThat(testTravel.getPickUpDatetime()).isEqualTo(UPDATED_TIMESTAMP);

    }

    @Test
    @Transactional
    public void deleteTravel() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);
        int databaseSizeBeforeDelete = travelRepository.findAll().size();

        // Delete the travel
        restTravelMockMvc.perform(delete("/api/travels/delete/{id}", travel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void testTravelEquals() {
        Travel travel1 = new Travel();
        travel1.setId(1L);
        Travel travel2 = new Travel();
        travel2.setId(travel1.getId());
        assertThat(travel1).isEqualTo(travel2);
        travel2.setId(2L);
        assertThat(travel1).isNotEqualTo(travel2);
        travel1.setId(null);
        assertThat(travel1).isNotEqualTo(travel2);
    }

    @Test
    public void testTravelFromId() {
        assertThat(travelMapper.travelFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        assertThat(travelMapper.travelFromId(null)).isNull();
    }

    @Test
    public void testTraveltoTravelDTO() {
        Travel defaultTravel = new Travel();
        defaultTravel.setLogin(DEFAULT_LOGIN);
        defaultTravel.setStartPlace(DEFAULT_START_PLACE);
        defaultTravel.setEndPlace(DEFAULT_END_PLACE);
        defaultTravel.setFirstName(DEFAULT_FIRSTNAME);
        defaultTravel.setLastName(DEFAULT_LASTNAME);
        defaultTravel.setPassengersCount(DEFAULT_PASSENGERS_COUNT);
        defaultTravel.setPickUpDatetime(DEFAULT_TIMESTAMP);

        TravelDTO travelDTO = travelMapper.travelToTravelDTO(defaultTravel);

        assertThat(travelDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(travelDTO.getStartPlace()).isEqualTo(DEFAULT_START_PLACE);
        assertThat(travelDTO.getEndPlace()).isEqualTo(DEFAULT_END_PLACE);
        assertThat(travelDTO.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(travelDTO.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(travelDTO.getPassengersCount()).isEqualTo(DEFAULT_PASSENGERS_COUNT);
        assertThat(travelDTO.getPickUpDatetime()).isEqualTo(DEFAULT_LOCAL_DATETIME);
    }


    @Test
    public void testTravelDTOtoTravel() {
        TravelDTO travelDTO = new TravelDTO();
        travelDTO.setLogin(DEFAULT_LOGIN);
        travelDTO.setStartPlace(DEFAULT_START_PLACE);
        travelDTO.setEndPlace(DEFAULT_END_PLACE);
        travelDTO.setFirstName(DEFAULT_FIRSTNAME);
        travelDTO.setLastName(DEFAULT_LASTNAME);
        travelDTO.setPassengersCount(DEFAULT_PASSENGERS_COUNT);
        travelDTO.setPickUpDatetime(DEFAULT_LOCAL_DATETIME);

        Travel travel = travelMapper.travelDTOToTravel(travelDTO);

        assertThat(travel.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(travel.getStartPlace()).isEqualTo(DEFAULT_START_PLACE);
        assertThat(travel.getEndPlace()).isEqualTo(DEFAULT_END_PLACE);
        assertThat(travel.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(travel.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(travel.getPassengersCount()).isEqualTo(DEFAULT_PASSENGERS_COUNT);
        assertThat(travel.getPickUpDatetime()).isEqualTo(DEFAULT_TIMESTAMP);
    }
}

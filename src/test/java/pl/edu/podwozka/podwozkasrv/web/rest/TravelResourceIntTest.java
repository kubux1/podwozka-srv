package pl.edu.podwozka.podwozkasrv.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.podwozka.podwozkasrv.PodwozkaSrvApplication;
import pl.edu.podwozka.podwozkasrv.domain.Address;
import pl.edu.podwozka.podwozkasrv.domain.Place;
import pl.edu.podwozka.podwozkasrv.domain.Travel;
import pl.edu.podwozka.podwozkasrv.repository.TravelRepository;
import pl.edu.podwozka.podwozkasrv.service.TravelService;
import pl.edu.podwozka.podwozkasrv.service.dto.PlaceDTO;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelDTO;
import pl.edu.podwozka.podwozkasrv.service.mapper.TravelMapper;
import pl.edu.podwozka.podwozkasrv.time.TimeUtil;
import pl.edu.podwozka.podwozkasrv.web.rest.exception.ExceptionTranslator;

import java.time.Instant;
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
import static pl.edu.podwozka.podwozkasrv.config.Constants.getDefaultObjectMapper;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PodwozkaSrvApplication.class)
public class TravelResourceIntTest {

    private static final String DEFAULT_LOGIN = "jan123";
    private static final String UPDATED_LOGIN = "janUpdated123";

    private static final Long DEFAULT_ID = 1L;

    private static final String DEFAULT_FIRSTNAME = "jan";
    private static final String UPDATED_FIRSTNAME = "janUpdated";

    private static final String DEFAULT_LASTNAME = "kowal";
    private static final String UPDATED_LASTNAME = "kowalUpdated";

    private static final Long DEFAULT_PASSENGERS_COUNT = 1L;
    private static final Long UPDATED_PASSENGERS_COUNT = 2L;

    private static final LocalDateTime DEFAULT_LOCAL_DATETIME = LocalDateTime.parse("2018-04-06T09:01:10");
    private static final Instant DEFAULT_INSTANT = TimeUtil.localDateTimeToInstant(DEFAULT_LOCAL_DATETIME);

    private static final LocalDateTime UPDATED_LOCAL_DATETIME = LocalDateTime.parse("2008-04-10T10:10:10");
    private static final Instant UPDATED_INSTANT = TimeUtil.localDateTimeToInstant(UPDATED_LOCAL_DATETIME);

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private TravelMapper travelMapper;

    @Autowired
    private TravelService travelService;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    @Qualifier("podwozkaJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    private MockMvc restTravelMockMvc;

    private Travel travel;

    private static Place defaultStart;

    private static Place defaultEnd;

    private static Place updatedStart;

    private static Place updatedEnd;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TravelResource travelResource = new TravelResource(travelService);
        this.restTravelMockMvc = MockMvcBuilders
                .standaloneSetup(travelResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jackson2HttpMessageConverter)
                .setControllerAdvice(exceptionTranslator)
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
        Address startAdress = new Address();
        startAdress.setCountry("Polska");
        startAdress.setLocality("Gdańsk");
        startAdress.setPostcode("12-345");
        startAdress.setBuildingNumber(12L);
        startAdress.setStreet("testowa");

        defaultStart = new Place();
        defaultStart.setLatitude(52.0);
        defaultStart.setLongitude(42.0);
        defaultStart.setName("Dom");
        defaultStart.setAddress(startAdress);

        Address endAddress = new Address();
        endAddress.setCountry("Polska");
        endAddress.setLocality("Gdańsk");
        endAddress.setPostcode("12-345");
        endAddress.setBuildingNumber(13L);
        endAddress.setStreet("testowa");

        defaultEnd = new Place();
        defaultEnd.setLatitude(52.0);
        defaultEnd.setLongitude(43.0);
        defaultEnd.setName("Dom");
        defaultEnd.setAddress(endAddress);

        Address updatedStartAdress = new Address();
        updatedStartAdress.setCountry("updatedPolska");
        updatedStartAdress.setLocality("updatedGdańsk");
        updatedStartAdress.setPostcode("updated12-345");
        updatedStartAdress.setBuildingNumber(112L);
        updatedStartAdress.setStreet("updatedtestowa");

        updatedStart = new Place();
        updatedStart.setLatitude(152.0);
        updatedStart.setLongitude(142.0);
        updatedStart.setName("updatedDom");
        updatedStart.setAddress(updatedStartAdress);

        Address updatedEndAddress = new Address();
        updatedEndAddress.setCountry("updatedPolska");
        updatedEndAddress.setLocality("updatedGdańsk");
        updatedEndAddress.setPostcode("updated12-345");
        updatedEndAddress.setBuildingNumber(113L);
        updatedEndAddress.setStreet("updatedtestowa");

        updatedEnd = new Place();
        updatedEnd.setLatitude(152.0);
        updatedEnd.setLongitude(143.0);
        updatedEnd.setName("updatedDom");
        updatedEnd.setAddress(updatedEndAddress);

        Travel travel = new Travel();
        travel.setDriverLogin(DEFAULT_LOGIN);
        travel.setStartPlace(defaultStart);
        travel.setEndPlace(defaultEnd);
        travel.setFirstName(DEFAULT_FIRSTNAME);
        travel.setLastName(DEFAULT_LASTNAME);
        travel.setPassengersCount(DEFAULT_PASSENGERS_COUNT);
        travel.setPickUpDatetime(DEFAULT_INSTANT);

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
        assertThat(testTravel.getDriverLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testTravel.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testTravel.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testTravel.getStartPlace()).isEqualTo(defaultStart);
        assertThat(testTravel.getEndPlace()).isEqualTo(defaultEnd);
        assertThat(testTravel.getPassengersCount()).isEqualTo(DEFAULT_PASSENGERS_COUNT);
        assertThat(testTravel.getPickUpDatetime()).isEqualTo(DEFAULT_INSTANT);
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
    public void getAllTravelsByDriverLogin() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        // Get all the travels
        restTravelMockMvc.perform(get("/api/travels?login=" + DEFAULT_LOGIN)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].driverLogin").value(hasItem(DEFAULT_LOGIN)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRSTNAME)))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LASTNAME)))
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
                .andExpect(jsonPath("$.driverLogin").value(DEFAULT_LOGIN))
                .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(DEFAULT_LASTNAME))
                .andExpect(jsonPath("$.startPlace").value(defaultStart))
                .andExpect(jsonPath("$.endPlace").value(defaultEnd))
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

        updatedTravel.setDriverLogin(UPDATED_LOGIN);
        updatedTravel.setStartPlace(new PlaceDTO(updatedStart));
        updatedTravel.setEndPlace(new PlaceDTO(updatedEnd));
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
        assertThat(testTravel.getDriverLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testTravel.getStartPlace()).isEqualTo(updatedStart);
        assertThat(testTravel.getEndPlace()).isEqualTo(updatedEnd);
        assertThat(testTravel.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testTravel.getLastName()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testTravel.getPassengersCount()).isEqualTo(UPDATED_PASSENGERS_COUNT);
        assertThat(testTravel.getPickUpDatetime()).isEqualTo(UPDATED_INSTANT);

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
        defaultTravel.setDriverLogin(DEFAULT_LOGIN);
        defaultTravel.setStartPlace(defaultStart);
        defaultTravel.setEndPlace(defaultEnd);
        defaultTravel.setFirstName(DEFAULT_FIRSTNAME);
        defaultTravel.setLastName(DEFAULT_LASTNAME);
        defaultTravel.setPassengersCount(DEFAULT_PASSENGERS_COUNT);
        defaultTravel.setPickUpDatetime(DEFAULT_INSTANT);

        TravelDTO travelDTO = travelMapper.travelToTravelDTO(defaultTravel);

        assertThat(travelDTO.getDriverLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(travelDTO.getStartPlace()).isEqualTo(new PlaceDTO(defaultStart));
        assertThat(travelDTO.getEndPlace()).isEqualTo(new PlaceDTO(defaultEnd));
        assertThat(travelDTO.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(travelDTO.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(travelDTO.getPassengersCount()).isEqualTo(DEFAULT_PASSENGERS_COUNT);
        assertThat(travelDTO.getPickUpDatetime()).isEqualTo(DEFAULT_LOCAL_DATETIME);
    }


    @Test
    public void testTravelDTOtoTravel() {
        TravelDTO travelDTO = new TravelDTO();
        travelDTO.setDriverLogin(DEFAULT_LOGIN);
        travelDTO.setStartPlace(new PlaceDTO(defaultStart));
        travelDTO.setEndPlace(new PlaceDTO(defaultEnd));
        travelDTO.setFirstName(DEFAULT_FIRSTNAME);
        travelDTO.setLastName(DEFAULT_LASTNAME);
        travelDTO.setPassengersCount(DEFAULT_PASSENGERS_COUNT);
        travelDTO.setPickUpDatetime(DEFAULT_LOCAL_DATETIME);

        Travel travel = travelMapper.travelDTOToTravel(travelDTO);

        assertThat(travel.getDriverLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(travel.getStartPlace()).isEqualTo(defaultStart);
        assertThat(travel.getEndPlace()).isEqualTo(defaultEnd);
        assertThat(travel.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(travel.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(travel.getPassengersCount()).isEqualTo(DEFAULT_PASSENGERS_COUNT);
        assertThat(travel.getPickUpDatetime()).isEqualTo(DEFAULT_INSTANT);
    }
}

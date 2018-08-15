package pl.edu.podwozka.podwozkasrv.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private static final Long DEFAULT_PASSANGERS_COUNT = 1L;
    private static final Long UPDATED_PASSANGERS_COUNT = 2L;

    private static final Timestamp DEFAULT_TIMESTAMP = Timestamp.valueOf ("2018-04-06 09:01:10");
    private static final Timestamp UPDATED_TIMESTAMP = Timestamp.valueOf ("2008-04-10 10:10:10");

    @Autowired
    private TravelRepository travelRepository;

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
     * Create a User.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User entity.
     */
    public static Travel createEntity() {
        Travel travel = new Travel();
        travel.setStartPlace(DEFAULT_START_PLACE);
        travel.setEndPlace(DEFAULT_END_PLACE);
        travel.setLogin(DEFAULT_LOGIN);
        travel.setFirstName(DEFAULT_FIRSTNAME);
        travel.setLastName(DEFAULT_LASTNAME);
        travel.setPassengersCount(DEFAULT_PASSANGERS_COUNT);
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

        // Validate the User in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeCreate + 1);
        Travel testTravel = travelList.get(travelList.size() - 1);
        assertThat(testTravel.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testTravel.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testTravel.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testTravel.getStartPlace()).isEqualTo(DEFAULT_START_PLACE);
        assertThat(testTravel.getEndPlace()).isEqualTo(DEFAULT_END_PLACE);
        assertThat(testTravel.getPassengersCount()).isEqualTo(DEFAULT_PASSANGERS_COUNT);
        assertThat(testTravel.getPickUpDatetime()).isEqualTo(DEFAULT_TIMESTAMP);
    }

}

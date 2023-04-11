package com.cake.ordering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cake.ordering.IntegrationTest;
import com.cake.ordering.domain.Cake;
import com.cake.ordering.domain.enumeration.CakeSize;
import com.cake.ordering.domain.enumeration.Shape;
import com.cake.ordering.repository.CakeRepository;
import com.cake.ordering.service.CakeService;
import com.cake.ordering.service.dto.CakeDTO;
import com.cake.ordering.service.mapper.CakeMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CakeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CakeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final Shape DEFAULT_SHAPE = Shape.ROUND;
    private static final Shape UPDATED_SHAPE = Shape.SQUARE;

    private static final CakeSize DEFAULT_CAKE_SIZE = CakeSize.SMALL;
    private static final CakeSize UPDATED_CAKE_SIZE = CakeSize.MEDIUM;

    private static final String ENTITY_API_URL = "/api/cakes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CakeRepository cakeRepository;

    @Mock
    private CakeRepository cakeRepositoryMock;

    @Autowired
    private CakeMapper cakeMapper;

    @Mock
    private CakeService cakeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCakeMockMvc;

    private Cake cake;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cake createEntity(EntityManager em) {
        Cake cake = new Cake()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .shape(DEFAULT_SHAPE)
            .cakeSize(DEFAULT_CAKE_SIZE);
        return cake;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cake createUpdatedEntity(EntityManager em) {
        Cake cake = new Cake()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .shape(UPDATED_SHAPE)
            .cakeSize(UPDATED_CAKE_SIZE);
        return cake;
    }

    @BeforeEach
    public void initTest() {
        cake = createEntity(em);
    }

    @Test
    @Transactional
    void createCake() throws Exception {
        int databaseSizeBeforeCreate = cakeRepository.findAll().size();
        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);
        restCakeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cakeDTO)))
            .andExpect(status().isCreated());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeCreate + 1);
        Cake testCake = cakeList.get(cakeList.size() - 1);
        assertThat(testCake.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCake.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCake.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCake.getShape()).isEqualTo(DEFAULT_SHAPE);
        assertThat(testCake.getCakeSize()).isEqualTo(DEFAULT_CAKE_SIZE);
    }

    @Test
    @Transactional
    void createCakeWithExistingId() throws Exception {
        // Create the Cake with an existing ID
        cake.setId(1L);
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        int databaseSizeBeforeCreate = cakeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCakeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cakeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCakes() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        // Get all the cakeList
        restCakeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cake.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].shape").value(hasItem(DEFAULT_SHAPE.toString())))
            .andExpect(jsonPath("$.[*].cakeSize").value(hasItem(DEFAULT_CAKE_SIZE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCakesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cakeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCakeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cakeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCakesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cakeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCakeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cakeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCake() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        // Get the cake
        restCakeMockMvc
            .perform(get(ENTITY_API_URL_ID, cake.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cake.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.shape").value(DEFAULT_SHAPE.toString()))
            .andExpect(jsonPath("$.cakeSize").value(DEFAULT_CAKE_SIZE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCake() throws Exception {
        // Get the cake
        restCakeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCake() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();

        // Update the cake
        Cake updatedCake = cakeRepository.findById(cake.getId()).get();
        // Disconnect from session so that the updates on updatedCake are not directly saved in db
        em.detach(updatedCake);
        updatedCake
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .shape(UPDATED_SHAPE)
            .cakeSize(UPDATED_CAKE_SIZE);
        CakeDTO cakeDTO = cakeMapper.toDto(updatedCake);

        restCakeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cakeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cakeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
        Cake testCake = cakeList.get(cakeList.size() - 1);
        assertThat(testCake.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCake.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCake.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCake.getShape()).isEqualTo(UPDATED_SHAPE);
        assertThat(testCake.getCakeSize()).isEqualTo(UPDATED_CAKE_SIZE);
    }

    @Test
    @Transactional
    void putNonExistingCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cakeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cakeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCakeWithPatch() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();

        // Update the cake using partial update
        Cake partialUpdatedCake = new Cake();
        partialUpdatedCake.setId(cake.getId());

        partialUpdatedCake.name(UPDATED_NAME).price(UPDATED_PRICE).shape(UPDATED_SHAPE);

        restCakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCake.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCake))
            )
            .andExpect(status().isOk());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
        Cake testCake = cakeList.get(cakeList.size() - 1);
        assertThat(testCake.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCake.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCake.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCake.getShape()).isEqualTo(UPDATED_SHAPE);
        assertThat(testCake.getCakeSize()).isEqualTo(DEFAULT_CAKE_SIZE);
    }

    @Test
    @Transactional
    void fullUpdateCakeWithPatch() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();

        // Update the cake using partial update
        Cake partialUpdatedCake = new Cake();
        partialUpdatedCake.setId(cake.getId());

        partialUpdatedCake
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .shape(UPDATED_SHAPE)
            .cakeSize(UPDATED_CAKE_SIZE);

        restCakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCake.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCake))
            )
            .andExpect(status().isOk());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
        Cake testCake = cakeList.get(cakeList.size() - 1);
        assertThat(testCake.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCake.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCake.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCake.getShape()).isEqualTo(UPDATED_SHAPE);
        assertThat(testCake.getCakeSize()).isEqualTo(UPDATED_CAKE_SIZE);
    }

    @Test
    @Transactional
    void patchNonExistingCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cakeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cakeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCake() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        int databaseSizeBeforeDelete = cakeRepository.findAll().size();

        // Delete the cake
        restCakeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cake.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

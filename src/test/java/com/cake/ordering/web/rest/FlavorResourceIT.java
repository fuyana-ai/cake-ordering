package com.cake.ordering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cake.ordering.IntegrationTest;
import com.cake.ordering.domain.Flavor;
import com.cake.ordering.repository.FlavorRepository;
import com.cake.ordering.service.dto.FlavorDTO;
import com.cake.ordering.service.mapper.FlavorMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FlavorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlavorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/flavors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlavorRepository flavorRepository;

    @Autowired
    private FlavorMapper flavorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlavorMockMvc;

    private Flavor flavor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flavor createEntity(EntityManager em) {
        Flavor flavor = new Flavor().name(DEFAULT_NAME);
        return flavor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flavor createUpdatedEntity(EntityManager em) {
        Flavor flavor = new Flavor().name(UPDATED_NAME);
        return flavor;
    }

    @BeforeEach
    public void initTest() {
        flavor = createEntity(em);
    }

    @Test
    @Transactional
    void createFlavor() throws Exception {
        int databaseSizeBeforeCreate = flavorRepository.findAll().size();
        // Create the Flavor
        FlavorDTO flavorDTO = flavorMapper.toDto(flavor);
        restFlavorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flavorDTO)))
            .andExpect(status().isCreated());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeCreate + 1);
        Flavor testFlavor = flavorList.get(flavorList.size() - 1);
        assertThat(testFlavor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createFlavorWithExistingId() throws Exception {
        // Create the Flavor with an existing ID
        flavor.setId(1L);
        FlavorDTO flavorDTO = flavorMapper.toDto(flavor);

        int databaseSizeBeforeCreate = flavorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlavorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flavorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFlavors() throws Exception {
        // Initialize the database
        flavorRepository.saveAndFlush(flavor);

        // Get all the flavorList
        restFlavorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flavor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getFlavor() throws Exception {
        // Initialize the database
        flavorRepository.saveAndFlush(flavor);

        // Get the flavor
        restFlavorMockMvc
            .perform(get(ENTITY_API_URL_ID, flavor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flavor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingFlavor() throws Exception {
        // Get the flavor
        restFlavorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFlavor() throws Exception {
        // Initialize the database
        flavorRepository.saveAndFlush(flavor);

        int databaseSizeBeforeUpdate = flavorRepository.findAll().size();

        // Update the flavor
        Flavor updatedFlavor = flavorRepository.findById(flavor.getId()).get();
        // Disconnect from session so that the updates on updatedFlavor are not directly saved in db
        em.detach(updatedFlavor);
        updatedFlavor.name(UPDATED_NAME);
        FlavorDTO flavorDTO = flavorMapper.toDto(updatedFlavor);

        restFlavorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flavorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flavorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeUpdate);
        Flavor testFlavor = flavorList.get(flavorList.size() - 1);
        assertThat(testFlavor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingFlavor() throws Exception {
        int databaseSizeBeforeUpdate = flavorRepository.findAll().size();
        flavor.setId(count.incrementAndGet());

        // Create the Flavor
        FlavorDTO flavorDTO = flavorMapper.toDto(flavor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlavorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flavorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flavorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlavor() throws Exception {
        int databaseSizeBeforeUpdate = flavorRepository.findAll().size();
        flavor.setId(count.incrementAndGet());

        // Create the Flavor
        FlavorDTO flavorDTO = flavorMapper.toDto(flavor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlavorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flavorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlavor() throws Exception {
        int databaseSizeBeforeUpdate = flavorRepository.findAll().size();
        flavor.setId(count.incrementAndGet());

        // Create the Flavor
        FlavorDTO flavorDTO = flavorMapper.toDto(flavor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlavorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flavorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlavorWithPatch() throws Exception {
        // Initialize the database
        flavorRepository.saveAndFlush(flavor);

        int databaseSizeBeforeUpdate = flavorRepository.findAll().size();

        // Update the flavor using partial update
        Flavor partialUpdatedFlavor = new Flavor();
        partialUpdatedFlavor.setId(flavor.getId());

        partialUpdatedFlavor.name(UPDATED_NAME);

        restFlavorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlavor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlavor))
            )
            .andExpect(status().isOk());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeUpdate);
        Flavor testFlavor = flavorList.get(flavorList.size() - 1);
        assertThat(testFlavor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFlavorWithPatch() throws Exception {
        // Initialize the database
        flavorRepository.saveAndFlush(flavor);

        int databaseSizeBeforeUpdate = flavorRepository.findAll().size();

        // Update the flavor using partial update
        Flavor partialUpdatedFlavor = new Flavor();
        partialUpdatedFlavor.setId(flavor.getId());

        partialUpdatedFlavor.name(UPDATED_NAME);

        restFlavorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlavor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlavor))
            )
            .andExpect(status().isOk());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeUpdate);
        Flavor testFlavor = flavorList.get(flavorList.size() - 1);
        assertThat(testFlavor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFlavor() throws Exception {
        int databaseSizeBeforeUpdate = flavorRepository.findAll().size();
        flavor.setId(count.incrementAndGet());

        // Create the Flavor
        FlavorDTO flavorDTO = flavorMapper.toDto(flavor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlavorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flavorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flavorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlavor() throws Exception {
        int databaseSizeBeforeUpdate = flavorRepository.findAll().size();
        flavor.setId(count.incrementAndGet());

        // Create the Flavor
        FlavorDTO flavorDTO = flavorMapper.toDto(flavor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlavorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flavorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlavor() throws Exception {
        int databaseSizeBeforeUpdate = flavorRepository.findAll().size();
        flavor.setId(count.incrementAndGet());

        // Create the Flavor
        FlavorDTO flavorDTO = flavorMapper.toDto(flavor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlavorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flavorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flavor in the database
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlavor() throws Exception {
        // Initialize the database
        flavorRepository.saveAndFlush(flavor);

        int databaseSizeBeforeDelete = flavorRepository.findAll().size();

        // Delete the flavor
        restFlavorMockMvc
            .perform(delete(ENTITY_API_URL_ID, flavor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Flavor> flavorList = flavorRepository.findAll();
        assertThat(flavorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

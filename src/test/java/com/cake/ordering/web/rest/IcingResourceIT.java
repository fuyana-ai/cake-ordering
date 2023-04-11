package com.cake.ordering.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cake.ordering.IntegrationTest;
import com.cake.ordering.domain.Icing;
import com.cake.ordering.repository.IcingRepository;
import com.cake.ordering.service.dto.IcingDTO;
import com.cake.ordering.service.mapper.IcingMapper;
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
 * Integration tests for the {@link IcingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IcingResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/icings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IcingRepository icingRepository;

    @Autowired
    private IcingMapper icingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIcingMockMvc;

    private Icing icing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Icing createEntity(EntityManager em) {
        Icing icing = new Icing().name(DEFAULT_NAME);
        return icing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Icing createUpdatedEntity(EntityManager em) {
        Icing icing = new Icing().name(UPDATED_NAME);
        return icing;
    }

    @BeforeEach
    public void initTest() {
        icing = createEntity(em);
    }

    @Test
    @Transactional
    void createIcing() throws Exception {
        int databaseSizeBeforeCreate = icingRepository.findAll().size();
        // Create the Icing
        IcingDTO icingDTO = icingMapper.toDto(icing);
        restIcingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icingDTO)))
            .andExpect(status().isCreated());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeCreate + 1);
        Icing testIcing = icingList.get(icingList.size() - 1);
        assertThat(testIcing.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createIcingWithExistingId() throws Exception {
        // Create the Icing with an existing ID
        icing.setId(1L);
        IcingDTO icingDTO = icingMapper.toDto(icing);

        int databaseSizeBeforeCreate = icingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIcingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIcings() throws Exception {
        // Initialize the database
        icingRepository.saveAndFlush(icing);

        // Get all the icingList
        restIcingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(icing.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getIcing() throws Exception {
        // Initialize the database
        icingRepository.saveAndFlush(icing);

        // Get the icing
        restIcingMockMvc
            .perform(get(ENTITY_API_URL_ID, icing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(icing.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingIcing() throws Exception {
        // Get the icing
        restIcingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIcing() throws Exception {
        // Initialize the database
        icingRepository.saveAndFlush(icing);

        int databaseSizeBeforeUpdate = icingRepository.findAll().size();

        // Update the icing
        Icing updatedIcing = icingRepository.findById(icing.getId()).get();
        // Disconnect from session so that the updates on updatedIcing are not directly saved in db
        em.detach(updatedIcing);
        updatedIcing.name(UPDATED_NAME);
        IcingDTO icingDTO = icingMapper.toDto(updatedIcing);

        restIcingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, icingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeUpdate);
        Icing testIcing = icingList.get(icingList.size() - 1);
        assertThat(testIcing.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingIcing() throws Exception {
        int databaseSizeBeforeUpdate = icingRepository.findAll().size();
        icing.setId(count.incrementAndGet());

        // Create the Icing
        IcingDTO icingDTO = icingMapper.toDto(icing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIcingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, icingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIcing() throws Exception {
        int databaseSizeBeforeUpdate = icingRepository.findAll().size();
        icing.setId(count.incrementAndGet());

        // Create the Icing
        IcingDTO icingDTO = icingMapper.toDto(icing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIcing() throws Exception {
        int databaseSizeBeforeUpdate = icingRepository.findAll().size();
        icing.setId(count.incrementAndGet());

        // Create the Icing
        IcingDTO icingDTO = icingMapper.toDto(icing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIcingWithPatch() throws Exception {
        // Initialize the database
        icingRepository.saveAndFlush(icing);

        int databaseSizeBeforeUpdate = icingRepository.findAll().size();

        // Update the icing using partial update
        Icing partialUpdatedIcing = new Icing();
        partialUpdatedIcing.setId(icing.getId());

        partialUpdatedIcing.name(UPDATED_NAME);

        restIcingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIcing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIcing))
            )
            .andExpect(status().isOk());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeUpdate);
        Icing testIcing = icingList.get(icingList.size() - 1);
        assertThat(testIcing.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateIcingWithPatch() throws Exception {
        // Initialize the database
        icingRepository.saveAndFlush(icing);

        int databaseSizeBeforeUpdate = icingRepository.findAll().size();

        // Update the icing using partial update
        Icing partialUpdatedIcing = new Icing();
        partialUpdatedIcing.setId(icing.getId());

        partialUpdatedIcing.name(UPDATED_NAME);

        restIcingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIcing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIcing))
            )
            .andExpect(status().isOk());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeUpdate);
        Icing testIcing = icingList.get(icingList.size() - 1);
        assertThat(testIcing.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingIcing() throws Exception {
        int databaseSizeBeforeUpdate = icingRepository.findAll().size();
        icing.setId(count.incrementAndGet());

        // Create the Icing
        IcingDTO icingDTO = icingMapper.toDto(icing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIcingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, icingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(icingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIcing() throws Exception {
        int databaseSizeBeforeUpdate = icingRepository.findAll().size();
        icing.setId(count.incrementAndGet());

        // Create the Icing
        IcingDTO icingDTO = icingMapper.toDto(icing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(icingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIcing() throws Exception {
        int databaseSizeBeforeUpdate = icingRepository.findAll().size();
        icing.setId(count.incrementAndGet());

        // Create the Icing
        IcingDTO icingDTO = icingMapper.toDto(icing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(icingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Icing in the database
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIcing() throws Exception {
        // Initialize the database
        icingRepository.saveAndFlush(icing);

        int databaseSizeBeforeDelete = icingRepository.findAll().size();

        // Delete the icing
        restIcingMockMvc
            .perform(delete(ENTITY_API_URL_ID, icing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Icing> icingList = icingRepository.findAll();
        assertThat(icingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

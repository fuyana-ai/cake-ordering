package com.cake.ordering.service;

import com.cake.ordering.service.dto.CakeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cake.ordering.domain.Cake}.
 */
public interface CakeService {
    /**
     * Save a cake.
     *
     * @param cakeDTO the entity to save.
     * @return the persisted entity.
     */
    CakeDTO save(CakeDTO cakeDTO);

    /**
     * Updates a cake.
     *
     * @param cakeDTO the entity to update.
     * @return the persisted entity.
     */
    CakeDTO update(CakeDTO cakeDTO);

    /**
     * Partially updates a cake.
     *
     * @param cakeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CakeDTO> partialUpdate(CakeDTO cakeDTO);

    /**
     * Get all the cakes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CakeDTO> findAll(Pageable pageable);

    /**
     * Get all the cakes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CakeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cake.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CakeDTO> findOne(Long id);

    /**
     * Delete the "id" cake.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

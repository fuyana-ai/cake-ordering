package com.cake.ordering.service;

import com.cake.ordering.service.dto.FlavorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cake.ordering.domain.Flavor}.
 */
public interface FlavorService {
    /**
     * Save a flavor.
     *
     * @param flavorDTO the entity to save.
     * @return the persisted entity.
     */
    FlavorDTO save(FlavorDTO flavorDTO);

    /**
     * Updates a flavor.
     *
     * @param flavorDTO the entity to update.
     * @return the persisted entity.
     */
    FlavorDTO update(FlavorDTO flavorDTO);

    /**
     * Partially updates a flavor.
     *
     * @param flavorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FlavorDTO> partialUpdate(FlavorDTO flavorDTO);

    /**
     * Get all the flavors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FlavorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" flavor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FlavorDTO> findOne(Long id);

    /**
     * Delete the "id" flavor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

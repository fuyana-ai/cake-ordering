package com.cake.ordering.service;

import com.cake.ordering.service.dto.IcingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cake.ordering.domain.Icing}.
 */
public interface IcingService {
    /**
     * Save a icing.
     *
     * @param icingDTO the entity to save.
     * @return the persisted entity.
     */
    IcingDTO save(IcingDTO icingDTO);

    /**
     * Updates a icing.
     *
     * @param icingDTO the entity to update.
     * @return the persisted entity.
     */
    IcingDTO update(IcingDTO icingDTO);

    /**
     * Partially updates a icing.
     *
     * @param icingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IcingDTO> partialUpdate(IcingDTO icingDTO);

    /**
     * Get all the icings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IcingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" icing.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IcingDTO> findOne(Long id);

    /**
     * Delete the "id" icing.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

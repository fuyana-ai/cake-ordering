package com.cake.ordering.web.rest;

import com.cake.ordering.repository.FlavorRepository;
import com.cake.ordering.service.FlavorService;
import com.cake.ordering.service.dto.FlavorDTO;
import com.cake.ordering.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cake.ordering.domain.Flavor}.
 */
@RestController
@RequestMapping("/api")
public class FlavorResource {

    private final Logger log = LoggerFactory.getLogger(FlavorResource.class);

    private static final String ENTITY_NAME = "flavor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlavorService flavorService;

    private final FlavorRepository flavorRepository;

    public FlavorResource(FlavorService flavorService, FlavorRepository flavorRepository) {
        this.flavorService = flavorService;
        this.flavorRepository = flavorRepository;
    }

    /**
     * {@code POST  /flavors} : Create a new flavor.
     *
     * @param flavorDTO the flavorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flavorDTO, or with status {@code 400 (Bad Request)} if the flavor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flavors")
    public ResponseEntity<FlavorDTO> createFlavor(@RequestBody FlavorDTO flavorDTO) throws URISyntaxException {
        log.debug("REST request to save Flavor : {}", flavorDTO);
        if (flavorDTO.getId() != null) {
            throw new BadRequestAlertException("A new flavor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlavorDTO result = flavorService.save(flavorDTO);
        return ResponseEntity
            .created(new URI("/api/flavors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flavors/:id} : Updates an existing flavor.
     *
     * @param id the id of the flavorDTO to save.
     * @param flavorDTO the flavorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flavorDTO,
     * or with status {@code 400 (Bad Request)} if the flavorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flavorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flavors/{id}")
    public ResponseEntity<FlavorDTO> updateFlavor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FlavorDTO flavorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Flavor : {}, {}", id, flavorDTO);
        if (flavorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flavorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flavorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FlavorDTO result = flavorService.update(flavorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flavorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flavors/:id} : Partial updates given fields of an existing flavor, field will ignore if it is null
     *
     * @param id the id of the flavorDTO to save.
     * @param flavorDTO the flavorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flavorDTO,
     * or with status {@code 400 (Bad Request)} if the flavorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the flavorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the flavorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flavors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FlavorDTO> partialUpdateFlavor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FlavorDTO flavorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Flavor partially : {}, {}", id, flavorDTO);
        if (flavorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flavorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flavorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlavorDTO> result = flavorService.partialUpdate(flavorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flavorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /flavors} : get all the flavors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flavors in body.
     */
    @GetMapping("/flavors")
    public ResponseEntity<List<FlavorDTO>> getAllFlavors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Flavors");
        Page<FlavorDTO> page = flavorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flavors/:id} : get the "id" flavor.
     *
     * @param id the id of the flavorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flavorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flavors/{id}")
    public ResponseEntity<FlavorDTO> getFlavor(@PathVariable Long id) {
        log.debug("REST request to get Flavor : {}", id);
        Optional<FlavorDTO> flavorDTO = flavorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flavorDTO);
    }

    /**
     * {@code DELETE  /flavors/:id} : delete the "id" flavor.
     *
     * @param id the id of the flavorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flavors/{id}")
    public ResponseEntity<Void> deleteFlavor(@PathVariable Long id) {
        log.debug("REST request to delete Flavor : {}", id);
        flavorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

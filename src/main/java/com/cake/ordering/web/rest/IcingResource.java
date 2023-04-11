package com.cake.ordering.web.rest;

import com.cake.ordering.repository.IcingRepository;
import com.cake.ordering.service.IcingService;
import com.cake.ordering.service.dto.IcingDTO;
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
 * REST controller for managing {@link com.cake.ordering.domain.Icing}.
 */
@RestController
@RequestMapping("/api")
public class IcingResource {

    private final Logger log = LoggerFactory.getLogger(IcingResource.class);

    private static final String ENTITY_NAME = "icing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IcingService icingService;

    private final IcingRepository icingRepository;

    public IcingResource(IcingService icingService, IcingRepository icingRepository) {
        this.icingService = icingService;
        this.icingRepository = icingRepository;
    }

    /**
     * {@code POST  /icings} : Create a new icing.
     *
     * @param icingDTO the icingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new icingDTO, or with status {@code 400 (Bad Request)} if the icing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/icings")
    public ResponseEntity<IcingDTO> createIcing(@RequestBody IcingDTO icingDTO) throws URISyntaxException {
        log.debug("REST request to save Icing : {}", icingDTO);
        if (icingDTO.getId() != null) {
            throw new BadRequestAlertException("A new icing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IcingDTO result = icingService.save(icingDTO);
        return ResponseEntity
            .created(new URI("/api/icings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /icings/:id} : Updates an existing icing.
     *
     * @param id the id of the icingDTO to save.
     * @param icingDTO the icingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated icingDTO,
     * or with status {@code 400 (Bad Request)} if the icingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the icingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/icings/{id}")
    public ResponseEntity<IcingDTO> updateIcing(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IcingDTO icingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Icing : {}, {}", id, icingDTO);
        if (icingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, icingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!icingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IcingDTO result = icingService.update(icingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, icingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /icings/:id} : Partial updates given fields of an existing icing, field will ignore if it is null
     *
     * @param id the id of the icingDTO to save.
     * @param icingDTO the icingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated icingDTO,
     * or with status {@code 400 (Bad Request)} if the icingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the icingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the icingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/icings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IcingDTO> partialUpdateIcing(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IcingDTO icingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Icing partially : {}, {}", id, icingDTO);
        if (icingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, icingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!icingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IcingDTO> result = icingService.partialUpdate(icingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, icingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /icings} : get all the icings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of icings in body.
     */
    @GetMapping("/icings")
    public ResponseEntity<List<IcingDTO>> getAllIcings(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Icings");
        Page<IcingDTO> page = icingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /icings/:id} : get the "id" icing.
     *
     * @param id the id of the icingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the icingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/icings/{id}")
    public ResponseEntity<IcingDTO> getIcing(@PathVariable Long id) {
        log.debug("REST request to get Icing : {}", id);
        Optional<IcingDTO> icingDTO = icingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(icingDTO);
    }

    /**
     * {@code DELETE  /icings/:id} : delete the "id" icing.
     *
     * @param id the id of the icingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/icings/{id}")
    public ResponseEntity<Void> deleteIcing(@PathVariable Long id) {
        log.debug("REST request to delete Icing : {}", id);
        icingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

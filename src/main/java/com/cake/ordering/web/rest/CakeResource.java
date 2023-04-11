package com.cake.ordering.web.rest;

import com.cake.ordering.repository.CakeRepository;
import com.cake.ordering.service.CakeService;
import com.cake.ordering.service.dto.CakeDTO;
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
 * REST controller for managing {@link com.cake.ordering.domain.Cake}.
 */
@RestController
@RequestMapping("/api")
public class CakeResource {

    private final Logger log = LoggerFactory.getLogger(CakeResource.class);

    private static final String ENTITY_NAME = "cake";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CakeService cakeService;

    private final CakeRepository cakeRepository;

    public CakeResource(CakeService cakeService, CakeRepository cakeRepository) {
        this.cakeService = cakeService;
        this.cakeRepository = cakeRepository;
    }

    /**
     * {@code POST  /cakes} : Create a new cake.
     *
     * @param cakeDTO the cakeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cakeDTO, or with status {@code 400 (Bad Request)} if the cake has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cakes")
    public ResponseEntity<CakeDTO> createCake(@RequestBody CakeDTO cakeDTO) throws URISyntaxException {
        log.debug("REST request to save Cake : {}", cakeDTO);
        if (cakeDTO.getId() != null) {
            throw new BadRequestAlertException("A new cake cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CakeDTO result = cakeService.save(cakeDTO);
        return ResponseEntity
            .created(new URI("/api/cakes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cakes/:id} : Updates an existing cake.
     *
     * @param id the id of the cakeDTO to save.
     * @param cakeDTO the cakeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cakeDTO,
     * or with status {@code 400 (Bad Request)} if the cakeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cakeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cakes/{id}")
    public ResponseEntity<CakeDTO> updateCake(@PathVariable(value = "id", required = false) final Long id, @RequestBody CakeDTO cakeDTO)
        throws URISyntaxException {
        log.debug("REST request to update Cake : {}, {}", id, cakeDTO);
        if (cakeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cakeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cakeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CakeDTO result = cakeService.update(cakeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cakeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cakes/:id} : Partial updates given fields of an existing cake, field will ignore if it is null
     *
     * @param id the id of the cakeDTO to save.
     * @param cakeDTO the cakeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cakeDTO,
     * or with status {@code 400 (Bad Request)} if the cakeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cakeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cakeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cakes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CakeDTO> partialUpdateCake(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CakeDTO cakeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cake partially : {}, {}", id, cakeDTO);
        if (cakeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cakeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cakeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CakeDTO> result = cakeService.partialUpdate(cakeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cakeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cakes} : get all the cakes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cakes in body.
     */
    @GetMapping("/cakes")
    public ResponseEntity<List<CakeDTO>> getAllCakes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Cakes");
        Page<CakeDTO> page;
        if (eagerload) {
            page = cakeService.findAllWithEagerRelationships(pageable);
        } else {
            page = cakeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cakes/:id} : get the "id" cake.
     *
     * @param id the id of the cakeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cakeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cakes/{id}")
    public ResponseEntity<CakeDTO> getCake(@PathVariable Long id) {
        log.debug("REST request to get Cake : {}", id);
        Optional<CakeDTO> cakeDTO = cakeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cakeDTO);
    }

    /**
     * {@code DELETE  /cakes/:id} : delete the "id" cake.
     *
     * @param id the id of the cakeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cakes/{id}")
    public ResponseEntity<Void> deleteCake(@PathVariable Long id) {
        log.debug("REST request to delete Cake : {}", id);
        cakeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

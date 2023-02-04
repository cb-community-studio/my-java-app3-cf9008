package com.codebridgecommunity.myjavaapp3.web.rest;

import com.codebridgecommunity.myjavaapp3.repository.ClassesRepository;
import com.codebridgecommunity.myjavaapp3.service.ClassesService;
import com.codebridgecommunity.myjavaapp3.service.dto.ClassesDTO;
import com.codebridgecommunity.myjavaapp3.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.codebridgecommunity.myjavaapp3.domain.Classes}.
 */
@RestController
@RequestMapping("/api")
public class ClassesResource {

    private final Logger log = LoggerFactory.getLogger(ClassesResource.class);

    private static final String ENTITY_NAME = "classes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassesService classesService;

    private final ClassesRepository classesRepository;

    public ClassesResource(ClassesService classesService, ClassesRepository classesRepository) {
        this.classesService = classesService;
        this.classesRepository = classesRepository;
    }

    /**
     * {@code POST  /classes} : Create a new classes.
     *
     * @param classesDTO the classesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classesDTO, or with status {@code 400 (Bad Request)} if the classes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classes")
    public Mono<ResponseEntity<ClassesDTO>> createClasses(@RequestBody ClassesDTO classesDTO) throws URISyntaxException {
        log.debug("REST request to save Classes : {}", classesDTO);
        if (classesDTO.getId() != null) {
            throw new BadRequestAlertException("A new classes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return classesService
            .save(classesDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/classes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /classes/:id} : Updates an existing classes.
     *
     * @param id the id of the classesDTO to save.
     * @param classesDTO the classesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classesDTO,
     * or with status {@code 400 (Bad Request)} if the classesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classes/{id}")
    public Mono<ResponseEntity<ClassesDTO>> updateClasses(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ClassesDTO classesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Classes : {}, {}", id, classesDTO);
        if (classesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return classesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return classesService
                    .update(classesDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /classes/:id} : Partial updates given fields of an existing classes, field will ignore if it is null
     *
     * @param id the id of the classesDTO to save.
     * @param classesDTO the classesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classesDTO,
     * or with status {@code 400 (Bad Request)} if the classesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the classesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the classesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/classes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ClassesDTO>> partialUpdateClasses(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ClassesDTO classesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Classes partially : {}, {}", id, classesDTO);
        if (classesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return classesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ClassesDTO> result = classesService.partialUpdate(classesDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /classes} : get all the classes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classes in body.
     */
    @GetMapping("/classes")
    public Mono<ResponseEntity<List<ClassesDTO>>> getAllClasses(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Classes");
        return classesService
            .countAll()
            .zipWith(classesService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /classes/:id} : get the "id" classes.
     *
     * @param id the id of the classesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classes/{id}")
    public Mono<ResponseEntity<ClassesDTO>> getClasses(@PathVariable String id) {
        log.debug("REST request to get Classes : {}", id);
        Mono<ClassesDTO> classesDTO = classesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classesDTO);
    }

    /**
     * {@code DELETE  /classes/:id} : delete the "id" classes.
     *
     * @param id the id of the classesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classes/{id}")
    public Mono<ResponseEntity<Void>> deleteClasses(@PathVariable String id) {
        log.debug("REST request to delete Classes : {}", id);
        return classesService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
                        .build()
                )
            );
    }
}

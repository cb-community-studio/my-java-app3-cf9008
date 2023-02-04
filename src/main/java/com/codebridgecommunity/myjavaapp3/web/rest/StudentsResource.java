package com.codebridgecommunity.myjavaapp3.web.rest;

import com.codebridgecommunity.myjavaapp3.repository.StudentsRepository;
import com.codebridgecommunity.myjavaapp3.service.StudentsService;
import com.codebridgecommunity.myjavaapp3.service.dto.StudentsDTO;
import com.codebridgecommunity.myjavaapp3.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.codebridgecommunity.myjavaapp3.domain.Students}.
 */
@RestController
@RequestMapping("/api")
public class StudentsResource {

    private final Logger log = LoggerFactory.getLogger(StudentsResource.class);

    private static final String ENTITY_NAME = "students";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentsService studentsService;

    private final StudentsRepository studentsRepository;

    public StudentsResource(StudentsService studentsService, StudentsRepository studentsRepository) {
        this.studentsService = studentsService;
        this.studentsRepository = studentsRepository;
    }

    /**
     * {@code POST  /students} : Create a new students.
     *
     * @param studentsDTO the studentsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentsDTO, or with status {@code 400 (Bad Request)} if the students has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/students")
    public Mono<ResponseEntity<StudentsDTO>> createStudents(@Valid @RequestBody StudentsDTO studentsDTO) throws URISyntaxException {
        log.debug("REST request to save Students : {}", studentsDTO);
        if (studentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new students cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return studentsService
            .save(studentsDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/students/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /students/:id} : Updates an existing students.
     *
     * @param id the id of the studentsDTO to save.
     * @param studentsDTO the studentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentsDTO,
     * or with status {@code 400 (Bad Request)} if the studentsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/students/{id}")
    public Mono<ResponseEntity<StudentsDTO>> updateStudents(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody StudentsDTO studentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Students : {}, {}", id, studentsDTO);
        if (studentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return studentsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return studentsService
                    .update(studentsDTO)
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
     * {@code PATCH  /students/:id} : Partial updates given fields of an existing students, field will ignore if it is null
     *
     * @param id the id of the studentsDTO to save.
     * @param studentsDTO the studentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentsDTO,
     * or with status {@code 400 (Bad Request)} if the studentsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/students/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<StudentsDTO>> partialUpdateStudents(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody StudentsDTO studentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Students partially : {}, {}", id, studentsDTO);
        if (studentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return studentsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<StudentsDTO> result = studentsService.partialUpdate(studentsDTO);

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
     * {@code GET  /students} : get all the students.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of students in body.
     */
    @GetMapping("/students")
    public Mono<ResponseEntity<List<StudentsDTO>>> getAllStudents(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Students");
        return studentsService
            .countAll()
            .zipWith(studentsService.findAll(pageable).collectList())
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
     * {@code GET  /students/:id} : get the "id" students.
     *
     * @param id the id of the studentsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/students/{id}")
    public Mono<ResponseEntity<StudentsDTO>> getStudents(@PathVariable String id) {
        log.debug("REST request to get Students : {}", id);
        Mono<StudentsDTO> studentsDTO = studentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentsDTO);
    }

    /**
     * {@code DELETE  /students/:id} : delete the "id" students.
     *
     * @param id the id of the studentsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/students/{id}")
    public Mono<ResponseEntity<Void>> deleteStudents(@PathVariable String id) {
        log.debug("REST request to delete Students : {}", id);
        return studentsService
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

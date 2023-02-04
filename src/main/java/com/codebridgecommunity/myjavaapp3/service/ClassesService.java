package com.codebridgecommunity.myjavaapp3.service;

import com.codebridgecommunity.myjavaapp3.service.dto.ClassesDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.codebridgecommunity.myjavaapp3.domain.Classes}.
 */
public interface ClassesService {
    /**
     * Save a classes.
     *
     * @param classesDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ClassesDTO> save(ClassesDTO classesDTO);

    /**
     * Updates a classes.
     *
     * @param classesDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ClassesDTO> update(ClassesDTO classesDTO);

    /**
     * Partially updates a classes.
     *
     * @param classesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ClassesDTO> partialUpdate(ClassesDTO classesDTO);

    /**
     * Get all the classes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ClassesDTO> findAll(Pageable pageable);

    /**
     * Returns the number of classes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" classes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ClassesDTO> findOne(String id);

    /**
     * Delete the "id" classes.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}

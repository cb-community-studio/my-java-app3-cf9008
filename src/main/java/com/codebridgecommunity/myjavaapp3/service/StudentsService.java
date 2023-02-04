package com.codebridgecommunity.myjavaapp3.service;

import com.codebridgecommunity.myjavaapp3.service.dto.StudentsDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.codebridgecommunity.myjavaapp3.domain.Students}.
 */
public interface StudentsService {
    /**
     * Save a students.
     *
     * @param studentsDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<StudentsDTO> save(StudentsDTO studentsDTO);

    /**
     * Updates a students.
     *
     * @param studentsDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<StudentsDTO> update(StudentsDTO studentsDTO);

    /**
     * Partially updates a students.
     *
     * @param studentsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<StudentsDTO> partialUpdate(StudentsDTO studentsDTO);

    /**
     * Get all the students.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<StudentsDTO> findAll(Pageable pageable);

    /**
     * Returns the number of students available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" students.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<StudentsDTO> findOne(String id);

    /**
     * Delete the "id" students.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}

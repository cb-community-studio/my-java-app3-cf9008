package com.codebridgecommunity.myjavaapp3.service.impl;

import com.codebridgecommunity.myjavaapp3.domain.Students;
import com.codebridgecommunity.myjavaapp3.repository.StudentsRepository;
import com.codebridgecommunity.myjavaapp3.service.StudentsService;
import com.codebridgecommunity.myjavaapp3.service.dto.StudentsDTO;
import com.codebridgecommunity.myjavaapp3.service.mapper.StudentsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Students}.
 */
@Service
public class StudentsServiceImpl implements StudentsService {

    private final Logger log = LoggerFactory.getLogger(StudentsServiceImpl.class);

    private final StudentsRepository studentsRepository;

    private final StudentsMapper studentsMapper;

    public StudentsServiceImpl(StudentsRepository studentsRepository, StudentsMapper studentsMapper) {
        this.studentsRepository = studentsRepository;
        this.studentsMapper = studentsMapper;
    }

    @Override
    public Mono<StudentsDTO> save(StudentsDTO studentsDTO) {
        log.debug("Request to save Students : {}", studentsDTO);
        return studentsRepository.save(studentsMapper.toEntity(studentsDTO)).map(studentsMapper::toDto);
    }

    @Override
    public Mono<StudentsDTO> update(StudentsDTO studentsDTO) {
        log.debug("Request to update Students : {}", studentsDTO);
        return studentsRepository.save(studentsMapper.toEntity(studentsDTO)).map(studentsMapper::toDto);
    }

    @Override
    public Mono<StudentsDTO> partialUpdate(StudentsDTO studentsDTO) {
        log.debug("Request to partially update Students : {}", studentsDTO);

        return studentsRepository
            .findById(studentsDTO.getId())
            .map(existingStudents -> {
                studentsMapper.partialUpdate(existingStudents, studentsDTO);

                return existingStudents;
            })
            .flatMap(studentsRepository::save)
            .map(studentsMapper::toDto);
    }

    @Override
    public Flux<StudentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Students");
        return studentsRepository.findAllBy(pageable).map(studentsMapper::toDto);
    }

    public Mono<Long> countAll() {
        return studentsRepository.count();
    }

    @Override
    public Mono<StudentsDTO> findOne(String id) {
        log.debug("Request to get Students : {}", id);
        return studentsRepository.findById(id).map(studentsMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Students : {}", id);
        return studentsRepository.deleteById(id);
    }
}

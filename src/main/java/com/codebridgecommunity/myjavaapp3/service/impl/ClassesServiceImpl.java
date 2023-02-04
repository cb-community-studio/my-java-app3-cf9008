package com.codebridgecommunity.myjavaapp3.service.impl;

import com.codebridgecommunity.myjavaapp3.domain.Classes;
import com.codebridgecommunity.myjavaapp3.repository.ClassesRepository;
import com.codebridgecommunity.myjavaapp3.service.ClassesService;
import com.codebridgecommunity.myjavaapp3.service.dto.ClassesDTO;
import com.codebridgecommunity.myjavaapp3.service.mapper.ClassesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Classes}.
 */
@Service
public class ClassesServiceImpl implements ClassesService {

    private final Logger log = LoggerFactory.getLogger(ClassesServiceImpl.class);

    private final ClassesRepository classesRepository;

    private final ClassesMapper classesMapper;

    public ClassesServiceImpl(ClassesRepository classesRepository, ClassesMapper classesMapper) {
        this.classesRepository = classesRepository;
        this.classesMapper = classesMapper;
    }

    @Override
    public Mono<ClassesDTO> save(ClassesDTO classesDTO) {
        log.debug("Request to save Classes : {}", classesDTO);
        return classesRepository.save(classesMapper.toEntity(classesDTO)).map(classesMapper::toDto);
    }

    @Override
    public Mono<ClassesDTO> update(ClassesDTO classesDTO) {
        log.debug("Request to update Classes : {}", classesDTO);
        return classesRepository.save(classesMapper.toEntity(classesDTO)).map(classesMapper::toDto);
    }

    @Override
    public Mono<ClassesDTO> partialUpdate(ClassesDTO classesDTO) {
        log.debug("Request to partially update Classes : {}", classesDTO);

        return classesRepository
            .findById(classesDTO.getId())
            .map(existingClasses -> {
                classesMapper.partialUpdate(existingClasses, classesDTO);

                return existingClasses;
            })
            .flatMap(classesRepository::save)
            .map(classesMapper::toDto);
    }

    @Override
    public Flux<ClassesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Classes");
        return classesRepository.findAllBy(pageable).map(classesMapper::toDto);
    }

    public Mono<Long> countAll() {
        return classesRepository.count();
    }

    @Override
    public Mono<ClassesDTO> findOne(String id) {
        log.debug("Request to get Classes : {}", id);
        return classesRepository.findById(id).map(classesMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Classes : {}", id);
        return classesRepository.deleteById(id);
    }
}

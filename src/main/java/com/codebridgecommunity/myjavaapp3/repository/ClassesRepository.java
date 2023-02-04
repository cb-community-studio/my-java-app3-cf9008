package com.codebridgecommunity.myjavaapp3.repository;

import com.codebridgecommunity.myjavaapp3.domain.Classes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Classes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassesRepository extends ReactiveMongoRepository<Classes, String> {
    Flux<Classes> findAllBy(Pageable pageable);
}

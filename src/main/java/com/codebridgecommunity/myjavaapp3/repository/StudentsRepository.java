package com.codebridgecommunity.myjavaapp3.repository;

import com.codebridgecommunity.myjavaapp3.domain.Students;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Students entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentsRepository extends ReactiveMongoRepository<Students, String> {
    Flux<Students> findAllBy(Pageable pageable);
}

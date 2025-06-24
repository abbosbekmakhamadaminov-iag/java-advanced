package com.epam.javadvanced.etl.repo;

import com.epam.javadvanced.shared.model.Sport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RakutenRepository extends ReactiveCrudRepository<Sport, Integer> {
    
    @Query("SELECT * FROM sport WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Flux<Sport> findByNameContainingIgnoreCase(String name);
    
    Mono<Sport> findByName(String name);
}

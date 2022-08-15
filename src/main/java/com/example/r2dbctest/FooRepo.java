package com.example.r2dbctest;

import java.util.Collection;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FooRepo extends ReactiveCrudRepository<Foo, Long> {
    @Query("SELECT `id`, `test_key`, `name` FROM `foo` WHERE `name` IN (:names)")
    Flux<Foo> findByName(Collection<String> names);

    @Query("SELECT `id`, `test_key`, `name` FROM `foo` WHERE `test_key` = :key AND `name` IN (:names)")
    Flux<Foo> findByKeyName(String key, Collection<String> names);

    @Query("SELECT `id`, `test_key`, `name` FROM `foo` WHERE (`test_key` = :key AND `name` IN (:names)) "
        + "OR (`test_key` IN (:names) AND `name` = :key)")
    Flux<Foo> findByKeyOrName(String key, Collection<String> names);

    @Modifying
    @Query("TRUNCATE `foo`")
    Mono<Long> clear();
}

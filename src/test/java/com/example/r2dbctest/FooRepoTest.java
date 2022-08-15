package com.example.r2dbctest;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest(properties = "spring.r2dbc.password=yOuRpAsSwOrD")
class FooRepoTest {
    @Autowired
    FooRepo fooRepo;

    @BeforeEach
    void setUp() {
        StepVerifier.create(fooRepo.clear()
                .then(fooRepo.save(new Foo(null, "key1", "test1")))
                .then(fooRepo.save(new Foo(null, "key2", "test2")))
                .then(fooRepo.save(new Foo(null, "key3", "test3")))
                .then(fooRepo.save(new Foo(null, "key4", "test4")))
                .then(fooRepo.save(new Foo(null, "key5", "test5"))))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void findByName() {
        StepVerifier.create(fooRepo.findByName(List.of("test2", "test3"))
                .doOnNext(System.out::println))
            .expectNextMatches(t -> "test2".equals(t.name()))
            .expectNextMatches(t -> "test3".equals(t.name()))
            .verifyComplete();
    }

    @Test
    void findByKeyName() {
        StepVerifier.create(fooRepo.findByKeyName("key2", List.of("test2", "test3"))
                .doOnNext(System.out::println))
            .expectNextMatches(t -> "test2".equals(t.name()))
            .verifyComplete();
    }

    @Test
    void findByKeyOrName() {
        // This will fail
        StepVerifier.create(fooRepo.findByKeyOrName("key2", List.of("test2", "test3"))
                .doOnError(Throwable::printStackTrace)
                .doOnNext(System.out::println))
            //.expectNextMatches(t -> "test2".equals(t.name()))
            .verifyError();
    }
}

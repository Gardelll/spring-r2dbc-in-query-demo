package com.example.r2dbctest;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public record Foo(@Id Long id, String testKey, String name) {
    public Foo withId(Long id) {
        return new Foo(id, testKey(), name());
    }
}

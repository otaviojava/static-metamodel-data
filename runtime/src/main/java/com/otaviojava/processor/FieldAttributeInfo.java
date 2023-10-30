package com.otaviojava.processor;

import jakarta.data.Sort;
import jakarta.data.metamodel.AttributeInfo;
import jakarta.nosql.Column;
import jakarta.nosql.Id;

import java.lang.reflect.Field;
import java.util.Objects;

public record FieldAttributeInfo(String name) implements AttributeInfo {
    @Override
    public Sort asc() {
        return Sort.asc(name);
    }

    @Override
    public Sort ascIgnoreCase() {
        return Sort.ascIgnoreCase(name);
    }

    @Override
    public Sort desc() {
        return Sort.desc(name);
    }

    @Override
    public Sort descIgnoreCase() {
        return Sort.descIgnoreCase(name);
    }

    @Override
    public String name() {
        return name;
    }

    public static FieldAttributeInfo of(Field field){
        Objects.requireNonNull(field, "field cannot be null");
        var id = field.getAnnotation(Id.class);
        if (id != null) {
            return new FieldAttributeInfo(id.value().isBlank() ? field.getName() : id.value());
        }
        var column = field.getAnnotation(Column.class);
        if (column != null) {
            return new FieldAttributeInfo(column.value().isBlank() ? field.getName() : column.value());
        }
        return new FieldAttributeInfo(field.getName());
    }
}

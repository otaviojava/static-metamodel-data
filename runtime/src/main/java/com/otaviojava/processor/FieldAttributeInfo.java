package com.otaviojava.processor;

import jakarta.data.Sort;
import jakarta.data.metamodel.AttributeInfo;

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
}

package com.otaviojava.entities;

import jakarta.data.Sort;
import jakarta.data.metamodel.Attribute;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class AnimalTest {

    @Test
    public void shouldCreateValidColorField() {
        Attribute attribute = Animal_.color;
        String fieldName = "color";
        assertSoftly(soft -> {
            soft.assertThat(attribute.name()).isEqualTo(fieldName);
            soft.assertThat(attribute.asc()).isEqualTo(Sort.asc(fieldName));
            soft.assertThat(attribute.desc()).isEqualTo(Sort.desc(fieldName));
            soft.assertThat(attribute.ascIgnoreCase()).isEqualTo(Sort.ascIgnoreCase(fieldName));
            soft.assertThat(attribute.descIgnoreCase()).isEqualTo(Sort.descIgnoreCase(fieldName));
        });
    }
}

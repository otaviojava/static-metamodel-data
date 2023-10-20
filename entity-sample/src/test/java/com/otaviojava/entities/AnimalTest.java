package com.otaviojava.entities;

import jakarta.data.Sort;
import jakarta.data.metamodel.Attribute;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class AnimalTest {

    @ParameterizedTest(name = "should to run the attribute {1}")
    @MethodSource("fields")
    public void shouldGenerateValidAttribute(Attribute attribute, String fieldName) {
        assertSoftly(soft -> {
            soft.assertThat(attribute.name()).isEqualTo(fieldName);
            soft.assertThat(attribute.asc()).isEqualTo(Sort.asc(fieldName));
            soft.assertThat(attribute.desc()).isEqualTo(Sort.desc(fieldName));
            soft.assertThat(attribute.ascIgnoreCase()).isEqualTo(Sort.ascIgnoreCase(fieldName));
            soft.assertThat(attribute.descIgnoreCase()).isEqualTo(Sort.descIgnoreCase(fieldName));
        });
    }

    public static Stream<Arguments> fields(){
        return Stream.of(Arguments.arguments(Animal_.color, "color"), Arguments.arguments(Animal_.name, "_id"));
    }
}

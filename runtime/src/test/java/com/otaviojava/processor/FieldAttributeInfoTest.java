package com.otaviojava.processor;

import jakarta.data.Sort;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class FieldAttributeInfoTest {

    @Test
    public void shouldReturnNPE(){
        Assertions.assertThatNullPointerException().isThrownBy(() -> FieldAttributeInfo.of(null));
    }

    @Test
    public void shouldShouldFieldAttributeInfo() throws NoSuchFieldException {
        var name = Person.class.getDeclaredField("name");
        var field = FieldAttributeInfo.of(name);

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(field.name()).isEqualTo("name");
            soft.assertThat(field.asc()).isEqualTo(Sort.asc("name"));
            soft.assertThat(field.ascIgnoreCase()).isEqualTo(Sort.ascIgnoreCase("name"));
            soft.assertThat(field.desc()).isEqualTo(Sort.desc("name"));
            soft.assertThat(field.descIgnoreCase()).isEqualTo(Sort.descIgnoreCase("name"));
        });
    }

}
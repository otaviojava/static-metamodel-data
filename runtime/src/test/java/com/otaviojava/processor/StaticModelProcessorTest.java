package com.otaviojava.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaticModelProcessorTest {
    @Test
    public void shouldReturnNPE() {
        assertThrows(NullPointerException.class, () -> {
            StaticModelProcessor.INSTANCE.accept(null);
        });
    }

    @Test
    public void shouldInitializeStaticModelProcessor() {
        Assertions.assertNull(Person_.id);
        Assertions.assertNull(Person_.name);
        StaticModelProcessor.INSTANCE.accept(Person_.class);
        Assertions.assertNotNull(Person_.id);
        Assertions.assertNotNull(Person_.name);
    }

}
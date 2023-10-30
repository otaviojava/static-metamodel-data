package com.otaviojava.processor;

import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(WeldJunit5Extension.class)
@AddPackages(MetaDataProcessor.class)
@AddExtensions(MetaDataProcessor.class)
@AddPackages(Person.class)
class MetaDataProcessorTest {

    @Test
    public void shouldLoadClasses() {
        assertTrue(true);
    }
}
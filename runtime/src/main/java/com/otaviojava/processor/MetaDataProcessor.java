package com.otaviojava.processor;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import jakarta.data.metamodel.StaticMetamodel;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class MetaDataProcessor implements Extension {

    private static final Logger LOGGER = Logger.getLogger(MetaDataProcessor.class.getName());

    private static final Set<Class<?>> CLASSES = new HashSet<>();
    void beforeBeanDiscovery(@Observes final BeforeBeanDiscovery event) {
        try (ScanResult result = new ClassGraph().enableAllInfo().scan()) {
            CLASSES.addAll(result.getClassesWithAnnotation(StaticMetamodel.class).loadClasses());
            LOGGER.info("Loaded classes: " + CLASSES);
        }
    }
}

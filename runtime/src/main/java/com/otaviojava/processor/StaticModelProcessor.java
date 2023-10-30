package com.otaviojava.processor;

import jakarta.data.metamodel.StaticMetamodel;

import java.util.logging.Logger;

class StaticModelProcessor {

    private static final Logger LOGGER = Logger.getLogger(StaticModelProcessor.class.getName());
    public <T> T process(Class<T> type) {
        StaticMetamodel metamodel = type.getAnnotation(StaticMetamodel.class);
        Class<?> entity = metamodel.value();


        return null;
    }
}

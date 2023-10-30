package com.otaviojava.processor;

import jakarta.data.metamodel.AttributeInfo;
import jakarta.data.metamodel.StaticMetamodel;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

class StaticModelProcessor<T> implements Consumer<Class<T>> {

    private static final Logger LOGGER = Logger.getLogger(StaticModelProcessor.class.getName());

    @Override
    public void accept(Class<T> type) {
        StaticMetamodel metamodel = type.getAnnotation(StaticMetamodel.class);
        Class<?> entity = metamodel.value();
        Field[] fields = type.getDeclaredFields();
        Map<String, Field> fieldMap = Stream.of(entity.getDeclaredFields())
                .collect(toMap(Field::getName, identity()));
        for (Field field : fields) {
            if (AttributeInfo.class.equals(field.getType())) {
                String name = field.getName();

            }

            LOGGER.info("Processing entity: " + entity);


        }
    }
}

package com.otaviojava.processor;

import jakarta.data.metamodel.AttributeInfo;
import jakarta.data.metamodel.StaticMetamodel;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

class StaticModelProcessor<T> implements Consumer<Class<T>> {

    private static final Logger LOGGER = Logger.getLogger(StaticModelProcessor.class.getName());

    static final StaticModelProcessor<?> INSTANCE = new StaticModelProcessor<>();

    private StaticModelProcessor() {
    }

    @Override
    public void accept(Class<T> type) {
        LOGGER.info("Processing entity: " + type);
        StaticMetamodel metamodel = type.getAnnotation(StaticMetamodel.class);
        Class<?> entity = metamodel.value();
        Map<String, Field> fieldMap = Stream.of(entity.getDeclaredFields())
                .collect(toMap(Field::getName, identity()));
        for (Field field : type.getDeclaredFields()) {
            if (AttributeInfo.class.equals(field.getType())) {
                var attribute = fieldMap.get(field.getName());
                if(attribute!=null){
                    setStaticField(field, attribute);
                }

            }




        }
    }

    private static void setStaticField(Field field, Field attribute) {
        attribute.setAccessible(true);
        FieldAttributeInfo.of(attribute);
        try {
            field.set(null, FieldAttributeInfo.of(attribute));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

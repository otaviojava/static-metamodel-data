/*
 *  Copyright (c) 2020 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package com.otaviojava.processor;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import org.eclipse.jnosql.mapping.Embeddable;
import org.eclipse.jnosql.mapping.metadata.MappingType;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;
import java.util.function.Supplier;

class FieldAnalyzer implements Supplier<FieldResult> {

    private static final String DEFAULT_TEMPLATE = "attribute.mustache";
    private static final String NULL = "null";
    private final Element field;
    private final Mustache template;

    private final ProcessingEnvironment processingEnv;
    private final TypeElement entity;

    FieldAnalyzer(Element field, ProcessingEnvironment processingEnv,
                  TypeElement entity) {
        this.field = field;
        this.processingEnv = processingEnv;
        this.entity = entity;
        this.template = createTemplate();
    }

    @Override
    public FieldResult get() {
        FieldModel metadata = getMetaData();
        Filer filer = processingEnv.getFiler();
        JavaFileObject fileObject = getFileObject(metadata, filer);
        try (Writer writer = fileObject.openWriter()) {
            template.execute(writer, metadata);
        } catch (IOException exception) {
            throw new ValidationException("An error to compile the class: " +
                    metadata.getQualified(), exception);
        }
        return new FieldResult(metadata.getQualified(), metadata.getFieldName());
    }

    private JavaFileObject getFileObject(FieldModel metadata, Filer filer) {
        try {
            return filer.createSourceFile(metadata.getQualified(), entity);
        } catch (IOException exception) {
            throw new ValidationException("An error to create the class: " +
                    metadata.getQualified(), exception);
        }

    }

    private FieldModel getMetaData() {
        final String fieldName = field.getSimpleName().toString();
        Column column = field.getAnnotation(Column.class);
        Id id = field.getAnnotation(Id.class);

        final String packageName = ProcessorUtil.getPackageName(entity);
        final String entityName = ProcessorUtil.getSimpleNameAsString(this.entity);
        final String name = getName(fieldName, column, id);

        return FieldModel.builder()
                .packageName(packageName)
                .name(name)
                .fieldName(fieldName)
                .entity(entityName)
                .build();
    }

    private String getName(String fieldName, Column column, Id id) {
        if (id == null) {
            return column.value().isBlank() ? fieldName : column.value();
        } else {
            return id.value().isBlank() ? fieldName : id.value();
        }
    }

    private Supplier<ValidationException> generateGetterError(String fieldName, String packageName, String entity, String s) {
        return () -> new ValidationException(s + fieldName + " in the class: " + packageName + "." + entity);
    }

    private Mustache createTemplate() {
        MustacheFactory factory = new DefaultMustacheFactory();
        return factory.compile(FieldAnalyzer.DEFAULT_TEMPLATE);
    }


    private static MappingType of(TypeMirror type, String collection, String fieldType) {

        if (type.getAnnotation(Embeddable.class) != null) {
            return MappingType.EMBEDDED;
        }
        if (type.getAnnotation(Entity.class) != null) {
            return MappingType.ENTITY;
        }
        if (!collection.equals(CollectionUtil.DEFAULT)) {
            return MappingType.COLLECTION;
        }
        if (fieldType.equals("java.util.Map")) {
            return MappingType.MAP;
        }
        return MappingType.DEFAULT;
    }

    private static MappingType of(Element element, String collection, String fieldType) {
        if (element.getAnnotation(Embeddable.class) != null) {
            return MappingType.EMBEDDED;
        }
        if (element.getAnnotation(Entity.class) != null) {
            return MappingType.ENTITY;
        }
        if (!collection.equals(CollectionUtil.DEFAULT)) {
            return MappingType.COLLECTION;
        }
        if (fieldType.equals("java.util.Map")) {
            return MappingType.MAP;
        }
        return MappingType.DEFAULT;
    }

    private String elementType(DeclaredType declaredType) {
        Optional<? extends TypeMirror> genericMirrorOptional = declaredType.getTypeArguments().stream().findFirst();
        if (genericMirrorOptional.isPresent()) {
            TypeMirror genericMirror = genericMirrorOptional.get();
            return genericMirror + ".class";
        } else {
            return NULL;
        }
    }

    private boolean isEmbeddable(DeclaredType declaredType) {
        return declaredType.getTypeArguments().stream()
                .filter(DeclaredType.class::isInstance).map(DeclaredType.class::cast)
                .map(DeclaredType::asElement).findFirst().map(e -> e.getAnnotation(Embeddable.class) != null ||
                e.getAnnotation(Entity.class) != null).orElse(false);
    }

}

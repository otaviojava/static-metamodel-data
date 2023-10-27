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
import jakarta.nosql.Id;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
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



    private Mustache createTemplate() {
        MustacheFactory factory = new DefaultMustacheFactory();
        return factory.compile(FieldAnalyzer.DEFAULT_TEMPLATE);
    }

}

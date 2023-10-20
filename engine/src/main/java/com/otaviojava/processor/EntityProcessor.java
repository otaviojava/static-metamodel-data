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

import jakarta.nosql.Column;
import jakarta.nosql.Id;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;

@SupportedAnnotationTypes({"jakarta.nosql.Entity",
        "org.eclipse.jnosql.mapping.Embeddable",
        "org.eclipse.jnosql.mapping.MappedSuperclass"})
public class EntityProcessor extends AbstractProcessor {

    private static final Logger LOGGER = Logger.getLogger(EntityProcessor.class.getName());
    private static final EnumSet<Modifier> MODIFIERS = EnumSet.of(PUBLIC, PROTECTED);
    static final Predicate<Element> IS_CONSTRUCTOR = el -> el.getKind() == ElementKind.CONSTRUCTOR;
    static final Predicate<String> IS_BLANK = String::isBlank;
    static final Predicate<String> IS_NOT_BLANK = IS_BLANK.negate();
    static final Predicate<Element> PUBLIC_PRIVATE = el -> el.getModifiers().stream().anyMatch(MODIFIERS::contains);
    static final Predicate<Element> DEFAULT_MODIFIER = el -> el.getModifiers().isEmpty();
    static final Predicate<Element> HAS_ACCESS = PUBLIC_PRIVATE.or(DEFAULT_MODIFIER);
    static final Predicate<Element> HAS_COLUMN_ANNOTATION = el -> el.getAnnotation(Column.class) != null;
    static final Predicate<Element> HAS_ID_ANNOTATION = el -> el.getAnnotation(Id.class) != null;
    static final Predicate<Element> HAS_ANNOTATION = HAS_COLUMN_ANNOTATION.or(HAS_ID_ANNOTATION);
    static final Predicate<Element> IS_FIELD = el -> el.getKind() == ElementKind.FIELD;


    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {

        final List<String> entities = new ArrayList<>();
        final List<String> references = new ArrayList<>();
        for (TypeElement annotation : annotations) {
            roundEnv.getElementsAnnotatedWith(annotation)
                    .stream()
                    .filter(e -> !references.contains(e.toString()))
                    .peek(e -> references.add(e.toString()))
                    .map(e -> new ClassAnalyzer(e, processingEnv))
                    .map(ClassAnalyzer::get)
                    .filter(IS_NOT_BLANK).forEach(entities::add);
        }

        try {
            if (!entities.isEmpty()) {
                MetadataAppender.append(processingEnv);
            }

        } catch (IOException | URISyntaxException exception) {
            error(exception);
        }
        return false;
    }



    private void createResource(String reference, String implementation) throws IOException {
        Filer filer = processingEnv.getFiler();
        FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "",
                "META-INF/services/" + reference);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(file.openOutputStream(), StandardCharsets.UTF_8));
        pw.println(implementation);
        pw.close();
    }


    private void error(Exception exception) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "failed to write extension file: "
                + exception.getMessage());
    }


}

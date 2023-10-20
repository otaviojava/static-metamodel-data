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

import org.eclipse.jnosql.mapping.Convert;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

final class FieldModel extends BaseMappingModel {

    private String packageName;
    private String name;
    private String entity;

    private FieldModel() {
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }


    public String getEntity() {
        return entity;
    }


    public String getQualified() {
        return packageName + "." + getClassName();
    }

    public String getClassName() {
        return entity+"_" + ProcessorUtil.capitalize(name) + "Attribute";
    }


    public static FieldMetaDataBuilder builder() {
        return new FieldMetaDataBuilder();
    }


    public static class FieldMetaDataBuilder {

        private final FieldModel fieldModel;

        private FieldMetaDataBuilder() {
            this.fieldModel = new FieldModel();
        }

        public FieldMetaDataBuilder packageName(String packageName) {
            this.fieldModel.packageName = packageName;
            return this;
        }

        public FieldMetaDataBuilder name(String name) {
            this.fieldModel.name = name;
            return this;
        }

        public FieldMetaDataBuilder entity(String entity) {
            this.fieldModel.entity = entity;
            return this;
        }

        public FieldModel build() {
           return fieldModel;
        }
    }
}

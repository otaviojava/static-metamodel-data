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


final class FieldModel extends BaseMappingModel {

    private String packageName;
    private String name;

    private String fieldName;

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
        return entity+ "_" + ProcessorUtil.capitalize(fieldName) + "Attribute";
    }

    public String getFieldName() {
        return fieldName;
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

        public FieldMetaDataBuilder fieldName(String fieldName) {
            this.fieldModel.fieldName = fieldName;
            return this;
        }

        public FieldModel build() {
           return fieldModel;
        }
    }
}

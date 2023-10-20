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

import java.util.List;

final class EntityModel extends BaseMappingModel {

    private final String packageName;

    private final String entity;

    private final String name;

    private final List<FieldResult> fields;

    EntityModel(String packageName, String entity, String name,
                       List<FieldResult> fields) {
        this.packageName = packageName;
        this.entity = entity;
        this.name = name;
        this.fields = fields;

    }

    public String getPackageName() {
        return packageName;
    }

    public String getEntity() {
        return entity;
    }

    public String getEntityQualified() {
        return packageName + '.' + entity;
    }

    public String getClassName() {
        return entity + "_";
    }

    public String getQualified() {
        return packageName + "." + getClassName();
    }

    public String getName() {
        return name;
    }

    public List<FieldResult> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "EntityModel{" +
                "packageName='" + packageName + '\'' +
                ", entity='" + entity + '\'' +
                ", name='" + name + '\'' +
                ", fields=" + fields +
                '}';
    }
}

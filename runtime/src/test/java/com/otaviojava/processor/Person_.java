package com.otaviojava.processor;

import jakarta.data.metamodel.AttributeInfo;
import jakarta.data.metamodel.StaticMetamodel;
import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@StaticMetamodel(Person.class)
public class Person_ {

    public static  volatile AttributeInfo id;

    public static  volatile AttributeInfo name;
}

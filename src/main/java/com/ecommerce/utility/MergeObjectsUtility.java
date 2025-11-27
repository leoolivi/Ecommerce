package com.ecommerce.utility;

import java.util.Objects;
import java.util.stream.Stream;

public class MergeObjectsUtility {
    public void getNullFields() {
        Stream.of("id", "name", "description").anyMatch(Objects::isNull);
    }
    
    public static void main(String[] args) {
        // var prod = new Product.builder().description("description")
    }
}

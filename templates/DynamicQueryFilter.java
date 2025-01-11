public Specification<Example> dynamicFilter(String name, Integer age) {
    return (root, query, builder) -> {
        Predicate predicate = builder.conjunction();
        if (name != null) predicate = builder.and(predicate, builder.equal(root.get("name"), name));
        if (age != null) predicate = builder.and(predicate, builder.greaterThan(root.get("age"), age));
        return predicate;
    };
}

package com.bilbo.platform.repository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class SpecBuilder<T> {

    private final Map<String, Specification<T>> specMap = new HashMap<>();

    public <R> SpecBuilder<T> with(BiFunction<String, R, Optional<Specification<T>>> supplier,
                                   String attributeName, R value) {
        supplier.apply(attributeName, value)
                .ifPresent(specification -> specMap.put(attributeName, specification));
        return this;
    }

    public Optional<Specification<T>> build(BinaryOperator<Specification<T>> merge) {
        return specMap.values()
                .stream()
                .reduce(merge);
    }


    public static <T, V> Optional<Specification<T>> valueIs(String attributeName, V value) {
        if (Objects.nonNull(value)) {
            return Optional.of(is(attributeName, value));
        }
        return Optional.empty();
    }

    public static <T> Optional<Specification<T>> stringIs(String attributeName, String value) {
        if (StringUtils.hasText(value)) {
            return Optional.of(is(attributeName, value));
        }
        return Optional.empty();
    }

    public static <T> Optional<Specification<T>> dateAfter(String attributeName, LocalDate date) {
        if (Objects.nonNull(date)) {
            return Optional.of((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get(attributeName), date));
        }
        return Optional.empty();
    }

    public static <T> Optional<Specification<T>> dateAfter(String attributeName, LocalDateTime date) {
        if (Objects.nonNull(date)) {
            return Optional.of((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get(attributeName), date));
        }
        return Optional.empty();
    }

    public static <T> Optional<Specification<T>> dateBefore(String attributeName, LocalDate date) {
        if (Objects.nonNull(date)) {
            return Optional.of((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get(attributeName), date));
        }
        return Optional.empty();
    }

    public static <T> Optional<Specification<T>> dateBefore(String attributeName, LocalDateTime date) {
        if (Objects.nonNull(date)) {
            return Optional.of((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get(attributeName), date));
        }
        return Optional.empty();
    }

    public static <T> Optional<Specification<T>> dateRange(String attributeName, Range<LocalDateTime> timeRange) {
        if (Objects.nonNull(timeRange.getFrom()) && Objects.nonNull(timeRange.getTo())) {
            return Optional.of((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get(attributeName), timeRange.getFrom(), timeRange.getTo()));
        }
        return Optional.empty();
    }

    private static <T, V> Specification<T> is(String attributeName, V value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(attributeName), value);
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Range<V> {

        private V from;
        private V to;

        public static <R> Range<R> of(R from, R to) {
            return new Range<>(from, to);
        }
    }
}

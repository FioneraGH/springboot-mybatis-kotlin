package com.fionera.test;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import static com.google.common.collect.Iterators.toArray;

/**
 * Created by fionera on 16-10-11.
 */
public class CustomSpecs {

    public static <T> Specification<T> byAuto(final EntityManager entityManager, final T example) {

        final Class<T> type = (Class<T>) example.getClass();

        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                EntityType<T> entityType = entityManager.getMetamodel().entity(type);

                for (Attribute<T, ?> attr : entityType.getDeclaredAttributes()) {
                    Object attrValue = getValue(example, attr);
                    if (attrValue != null) {
                        if (attr.getJavaType() == String.class) {
                            if (!StringUtils.isEmpty(attrValue)) {
                                predicates.add(criteriaBuilder.like(root
                                                .get(attribute(entityType, attr.getName(), String
                                                        .class)),
                                        pattern((String) attrValue)));
                            }else{
                                predicates.add(criteriaBuilder.like(root
                                                .get(attribute(entityType, attr.getName(), String
                                                        .class)),
                                        pattern("")));
                            }
                        } else {
                            predicates.add(criteriaBuilder.equal(root
                                    .get(attribute(entityType, attr.getName(),
                                            attrValue.getClass())), attrValue));
                        }
                    }
                }
                return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.and(
                        toArray(predicates.iterator(), Predicate.class));
            }

            private Object getValue(T example, Attribute<T, ?> attr) {
                return ReflectionUtils.getField((Field) attr.getJavaMember(), example);
            }

            private <E> SingularAttribute<T, E> attribute(EntityType<T> entityType,
                                                                    String fieldName, Class<E> fieldClass) {
                return entityType.getDeclaredSingularAttribute(fieldName, fieldClass);
            }
        };
    }

    private static String pattern(String str) {
        return "%" + str + "%";
    }
}

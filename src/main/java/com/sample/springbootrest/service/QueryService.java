package com.sample.springbootrest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QueryService<T> {

  @Autowired private ObjectMapper objectMapper;

  public Specification<T> getSpecification(String q) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      Map<String, SearchParameter> convertToDto = new HashMap<>();
      try {
        convertToDto = convertToDto(q);
        for (Map.Entry<String, SearchParameter> pair : convertToDto.entrySet()) {

          String key = pair.getKey();
          SearchParameter search = pair.getValue();
          if (search.getValues() != null && !search.getValues().isEmpty()) {
            String value = search.getValues().get(0);
            log.debug("key: [{}] & value: [{}]", key, value);
            switch (search.op) {
              case "eq":
                if ("boolean".equalsIgnoreCase(root.get(key).getJavaType().getSimpleName())) {
                  predicates.add(criteriaBuilder.equal(root.get(key), value));
                } else {
                  predicates.add(criteriaBuilder.equal(root.get(key), value));
                }
                break;
              case "!eq":
                if ("boolean".equalsIgnoreCase(root.get(key).getJavaType().getSimpleName())) {
                  predicates.add(criteriaBuilder.notEqual(root.get(key), value));
                } else {
                  predicates.add(criteriaBuilder.notEqual(root.get(key), value));
                }
                break;
              case "like":
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(key).as(String.class)),
                        "%" + value.toLowerCase() + "%"));
                break;
              case "!like":
                predicates.add(
                    criteriaBuilder.notLike(
                        criteriaBuilder.lower(root.get(key)), "%" + value.toLowerCase() + "%"));
                break;
              default:
                break;
            }

          } else {
            log.warn("No values provided for key: {}", key);
          }
        }
      } catch (JsonProcessingException e) {
        log.warn(String.format("Unable to build criteria for [%s]", q), e);
      }
      // addded deletedAt null clause
      criteriaBuilder.isNull(root.get("deletedAt"));
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }

  public Pageable getAll(Pageable pageable) {
    Map<String, String> sortMap = new LinkedHashMap<>();

    String sortProperty = "updatedAt";
    Direction sortDir = Direction.fromString("DESC");
    pageable.getSort().stream().forEach(s -> sortMap.put(s.getProperty(), s.getDirection().name()));

    List<Order> orders =
        sortMap.entrySet().stream()
            .map(s -> new Order(Direction.fromString(s.getValue()), snakeToCamel(s.getKey())))
            .collect(Collectors.toCollection(ArrayList::new));

    if (orders.isEmpty()) {
      orders.add(new Order(sortDir, sortProperty));
    }
    log.info("Sort Map: [{},{}]", sortProperty, sortDir.name());
    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
  }

  public Map<String, SearchParameter> convertToDto(String q) throws JsonProcessingException {
    Map<String, SearchParameter> map = new HashMap<>();
    try {
      map =
          Optional.ofNullable(
                  objectMapper.readValue(q, new TypeReference<Map<String, SearchParameter>>() {}))
              .orElseGet(HashMap::new);
    } catch (JsonProcessingException e) {
      log.info("Json mapping exception : {} ", e.getMessage(), e);
    }
    return map.entrySet().stream()
        .collect(Collectors.toMap(e -> snakeToCamel(e.getKey()), Map.Entry::getValue));
  }

  protected static String snakeToCamel(String str) {
    // Capitalize first letter of string
    str = str.substring(0, 1).toUpperCase() + str.substring(1);

    // Convert to StringBuilder
    StringBuilder builder = new StringBuilder(str);

    // Traverse the string character by
    // character and remove underscore
    // and capitalize next letter
    for (int i = 0; i < builder.length(); i++) {

      // Check char is underscore
      if (builder.charAt(i) == '_') {
        builder.deleteCharAt(i);
        builder.replace(i, i + 1, String.valueOf(Character.toUpperCase(builder.charAt(i))));
      }
    }
    if (builder.length() > 0) builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
    // Return in String type
    log.debug("String ---> [{}]", builder.toString());
    return builder.toString();
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SearchParameter {
    private String op;
    private List<String> values;
  }
}

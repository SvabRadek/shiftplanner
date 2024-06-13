package com.cocroachden.planner.constraint.repository;

import com.cocroachden.planner.constraint.api.ConstraintType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ConstraintTypeAttributeConverter implements AttributeConverter<ConstraintType, String> {

  @Override
  public String convertToDatabaseColumn(ConstraintType attribute) {
    return attribute.name();
  }

  @Override
  public ConstraintType convertToEntityAttribute(String dbData) {
    return Stream.of(ConstraintType.values())
        .filter(value -> value.name().equals(dbData))
        .findAny()
        .orElseThrow(IllegalArgumentException::new);
  }
}

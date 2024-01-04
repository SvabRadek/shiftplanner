package com.cocroachden.planner.configuration;

import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ConstraintRequestAttributeConverter implements AttributeConverter<ConstraintRequest, String> {
  @Override
  public String convertToDatabaseColumn(ConstraintRequest constraintRequest) {
    try {
      return new ObjectMapper().writeValueAsString(constraintRequest);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ConstraintRequest convertToEntityAttribute(String json) {
    try {
      return new ObjectMapper()
          .readValue(json, ConstraintRequest.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}

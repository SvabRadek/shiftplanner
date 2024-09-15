package com.cocroachden.planner.constraint.repository;

import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConstraintAttributeConverter implements AttributeConverter<SolverConstraint, String> {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);

    @Override
    public String convertToDatabaseColumn(SolverConstraint solverConstraint) {
        try {
            return mapper.writeValueAsString(solverConstraint);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SolverConstraint convertToEntityAttribute(String json) {
        try {
            return mapper.readValue(json, SolverConstraint.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

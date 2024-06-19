package com.cocroachden.planner.constraint.api;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConstraintRequestDTODeserializer extends JsonDeserializer<ConstraintRequestDTO> {

  private static final Map<ConstraintType, Class<? extends ConstraintRequestDTO>> DESERIALIZATION_MAP = new HashMap<>();

  static {
    DESERIALIZATION_MAP.put(ConstraintType.CONSECUTIVE_WORKING_DAYS, ConsecutiveWorkingDaysRequestDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.EMPLOYEE_SHIFT_REQUEST, EmployeeShiftRequestDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.SHIFT_PATTERN_CONSTRAINT, ShiftPatternRequestDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.EMPLOYEES_PER_SHIFT, EmployeesPerShiftRequestDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION, ShiftFollowupRestrictionRequestDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.SHIFT_PER_SCHEDULE, ShiftsPerScheduleRequestDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.TRIPLE_SHIFTS_CONSTRAINT, TripleShiftConstraintRequestDTO.class);
  }

  @Override
  public ConstraintRequestDTO deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException, JacksonException {
    var node = jsonParser.readValueAsTree();
    var type = node.get("type").toString().replace("\"", "");
    var serializationType = ConstraintType.valueOf(type);
    return JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .disable(MapperFeature.USE_ANNOTATIONS)
        .build()
        .treeToValue(node, DESERIALIZATION_MAP.get(serializationType));
  }
}

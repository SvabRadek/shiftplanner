package com.cocroachden.planner.constraint;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConstraintDTODeserializer extends JsonDeserializer<ConstraintDTO> {

  private static final Map<ConstraintType, Class<? extends ConstraintDTO>> DESERIALIZATION_MAP = new HashMap<>();

  static {
    DESERIALIZATION_MAP.put(ConstraintType.CONSECUTIVE_WORKING_DAYS, ConsecutiveWorkingDaysConstraintDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.REQUESTED_SHIFT_CONSTRAINT, RequestedShiftConstraintDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.SHIFT_PATTERN_CONSTRAINT, ShiftPatternConstraintDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.EMPLOYEES_PER_SHIFT, EmployeesPerShiftConstraintDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION, ShiftFollowupRestrictionConstraintDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.SHIFTS_PER_SCHEDULE, ShiftsPerScheduleConstraintDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.TRIPLE_SHIFTS_CONSTRAINT, TripleShiftConstraintDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.TEAM_ASSIGNMENT, TeamAssignmentConstraintDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.WEEKEND_CONSTRAINT, WeekendConstraintDTO.class);
    DESERIALIZATION_MAP.put(ConstraintType.EVEN_SHIFT_DISTRIBUTION, EvenShiftDistributionConstraintDTO.class);
  }

  @Override
  public ConstraintDTO deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
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

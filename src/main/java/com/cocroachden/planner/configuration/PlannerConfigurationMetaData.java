package com.cocroachden.planner.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlannerConfigurationMetaData {
  private String name;
  private UUID id;
  private LocalDate startDate;
  private LocalDate endDate;
}

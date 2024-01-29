package com.cocroachden.planner.plannerconfiguration.repository;

import com.cocroachden.planner.lib.ConstraintType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class ConfigurationRequestLink {
  @Enumerated(EnumType.STRING)
  private ConstraintType requestType;
  private UUID requestId;
}

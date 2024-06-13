package com.cocroachden.planner.solverconfiguration.repository;

import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.solverconfiguration.ConfigurationRequestLinkDTO;
import jakarta.persistence.Embeddable;
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

  public static ConfigurationRequestLink from(ConfigurationRequestLinkDTO dto) {
    return new ConfigurationRequestLink(
        dto.getRequestType(),
        dto.getRequestId()
    );
  }
  private ConstraintType requestType;
  private UUID requestId;
}

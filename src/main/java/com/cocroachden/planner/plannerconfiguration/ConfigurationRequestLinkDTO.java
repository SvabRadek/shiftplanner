package com.cocroachden.planner.plannerconfiguration;

import com.cocroachden.planner.lib.ConstraintType;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConfigurationRequestLinkDTO {
  @Nonnull
  private ConstraintType requestType;
  @Nonnull
  private UUID requestId;
}

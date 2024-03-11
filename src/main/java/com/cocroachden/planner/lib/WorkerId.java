package com.cocroachden.planner.lib;

import dev.hilla.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
@EqualsAndHashCode
public class WorkerId implements Serializable {
  @Serial
  private static final long serialVersionUID = -3136703610267018121L;
  @Column(name = "worker_id", nullable = false)
  @Nonnull
  private Long id;
}

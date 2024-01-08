package com.cocroachden.planner.lib;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Embeddable
@EqualsAndHashCode
public class ConstraintRequestTypeId implements Serializable {
  @Serial
  private static final long serialVersionUID = 8930579657252155114L;
  @Column(name = "constraint_request_type_id", nullable = false)
  private String id;
}

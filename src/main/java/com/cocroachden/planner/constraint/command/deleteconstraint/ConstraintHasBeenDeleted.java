package com.cocroachden.planner.constraint.command.deleteconstraint;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.constraint.ConstraintId;

public record ConstraintHasBeenDeleted(
        ConstraintId constraintId
) implements Event {
}

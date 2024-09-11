package com.cocroachden.planner.constraint.command.saveconstraint;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;

public record ConstraintHasBeenSaved(
        ConstraintRequestRecord record
) implements Event {
}

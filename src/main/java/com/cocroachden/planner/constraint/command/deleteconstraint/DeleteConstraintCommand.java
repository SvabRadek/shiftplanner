package com.cocroachden.planner.constraint.command.deleteconstraint;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.constraint.ConstraintId;

public record DeleteConstraintCommand(
        ConstraintId constraintId
) implements Command {
}

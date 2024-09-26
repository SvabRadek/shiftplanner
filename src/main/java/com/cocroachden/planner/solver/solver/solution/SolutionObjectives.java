package com.cocroachden.planner.solver.solver.solution;

import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearArgument;
import com.google.ortools.sat.LinearExpr;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SolutionObjectives {
  private final List<IntVar> objectiveIntVars;
  private final List<Integer> objectiveIntCoefficients;
  private final List<BoolVar> objectiveBoolVars;
  private final List<Integer> objectiveBoolCoefficients;

  public SolutionObjectives() {
    this.objectiveBoolVars = new ArrayList<>();
    this.objectiveBoolCoefficients = new ArrayList<>();
    this.objectiveIntVars = new ArrayList<>();
    this.objectiveIntCoefficients = new ArrayList<>();
  }

  public void addIntCost(
      IntVar variable,
      Integer coefficient
  ) {
    objectiveIntVars.add(variable);
    objectiveIntCoefficients.add(coefficient);
  }

  public LinearArgument getObjectiveAsExpression() {
    var expr = LinearExpr.newBuilder();
    for (int i = 0; i < this.objectiveIntVars.size(); i++) {
      expr.addTerm(objectiveIntVars.get(i), objectiveIntCoefficients.get(i));
    }
    for (int i = 0; i < objectiveBoolVars.size(); i++) {
      expr.addTerm(objectiveBoolVars.get(i), objectiveBoolCoefficients.get(i));
    }
    return expr;
  }

  public void addBoolCost(
      BoolVar variable,
      Integer coefficient
  ) {
    objectiveBoolVars.add(variable);
    objectiveBoolCoefficients.add(coefficient);
  }
}

package com.cocroachden.planner.solver.solver;

import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearArgument;
import com.google.ortools.sat.LinearExpr;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
public class Objectives {
  private final List<IntVar> objectiveIntVars;
  private final List<Integer> objectiveIntCoefficients;
  private final List<BoolVar> objectiveBoolVars;
  private final List<Integer> objectiveBoolCoefficients;

  public Objectives() {
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
    IntStream.range(0, this.objectiveIntVars.size()).forEach(index -> {
      expr.addTerm(objectiveIntVars.get(index), objectiveIntCoefficients.get(index));
    });
    IntStream.range(0, this.objectiveBoolVars.size()).forEach(index -> {
      expr.addTerm(objectiveBoolVars.get(index), objectiveBoolCoefficients.get(index));
    });
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

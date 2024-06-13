package com.cocroachden.planner.solver.constraints.specific;


import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearArgument;
import com.google.ortools.sat.LinearExpr;

public class MinMaxConstraint {

  public static void apply(
      AbstractMinMaxRequest minMaxRequest,
      BoolVar[] shifts,
      Integer maxExcess,
      CpModel model,
      SolutionObjectives solutionObjectives,
      Integer weight
  ) {
    var softMax = minMaxRequest.getSoftMax();
    var hardMax = minMaxRequest.getHardMax();
    var maxPenalty = minMaxRequest.getMaxPenalty();
    var softMin = minMaxRequest.getSoftMin();
    var minPenalty = minMaxRequest.getMinPenalty();
    var hardMin = minMaxRequest.getHardMin();
    var zero = model.newConstant(0);
    var sum = model.newIntVar(hardMin, hardMax, "");

    model.addEquality(LinearExpr.sum(shifts), sum);

    if (maxPenalty > 0 && softMax < hardMax) {
      var delta = model.newIntVar(-maxExcess, maxExcess, "");
      var expr = LinearExpr.newBuilder().add(sum).addTerm(model.newConstant(softMax), -1);
      model.addEquality(delta, expr);
      var excess = model.newIntVar(0, maxExcess, "");
      model.addMaxEquality(excess, new LinearArgument[]{ delta, zero });
      solutionObjectives.addIntCost(excess, maxPenalty * weight);
    }

    if (minPenalty > 0 && softMin > hardMin) {
      var delta = model.newIntVar(-maxExcess, maxExcess, "");
      var expr = LinearExpr.newBuilder().add(softMin).addTerm(sum, -1);
      model.addEquality(delta, expr);
      var excess = model.newIntVar(0, maxExcess, "");
      model.addMaxEquality(excess, new LinearArgument[]{ delta, zero });
      solutionObjectives.addIntCost(excess, minPenalty * weight);
    }
  }
}

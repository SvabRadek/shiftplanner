package com.cocroachden.planner.solverconfiguration.validations.employee;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.validations.IssueSeverity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.atmosphere.config.service.Get;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@Getter
public final class EmployeeValidationIssueDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @JsonProperty
    private final @Nonnull String employeeId;
    private final @Nonnull IssueSeverity severity;
    private final @Nonnull String issue;

    public EmployeeValidationIssueDTO(
            @Nonnull EmployeeId employeeId,
            @Nonnull IssueSeverity severity,
            @Nonnull String issue
    ) {
        this.employeeId = employeeId.getId();
        this.severity = severity;
        this.issue = issue;
    }

    @JsonIgnore
    public @Nonnull EmployeeId getEmployeeId() {
        return EmployeeId.from(employeeId);
    }

    @Override
    public String toString() {
        return "EmployeeValidationIssueDTO[" +
                "employeeId=" + employeeId + ", " +
                "severity=" + severity + ", " +
                "issue=" + issue + ']';
    }

}

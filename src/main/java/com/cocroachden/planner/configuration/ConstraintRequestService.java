package com.cocroachden.planner.configuration;

import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class ConstraintRequestService {
  private ConstraintRequestRepository repository;

  @Nonnull
  public List<@Nonnull SpecificShiftRequestResponse> getSpecificShiftRequests(UUID ownerConfiguration) {
    return repository.findByOwnerConfigurationAndTypeIgnoreCase(
            ownerConfiguration,
            SpecificShiftRequest.ID.getId()
        ).stream()
        .map(record -> {
          var request = (SpecificShiftRequest) record.getRequest();
          return new SpecificShiftRequestResponse(
              record.getType(),
              request.getOwner().orElseThrow().getWorkerId(),
              StupidDate.fromDate(request.getDate()),
              request.getRequestedShift()
          );
        }).toList();
  }

  public ConstraintRequestRecord upsert(ConstraintRequestRecord record) {
    return repository.save(record);
  }

  public ConstraintRequestRecord saveAsNew(ConstraintRequestRecord record) {
    record.setId(UUID.randomUUID());
    return repository.save(record);
  }

  @AllArgsConstructor
  @Getter
  public static class SpecificShiftRequestResponse {
    @Nonnull
    private String type;
    @Nonnull
    private String owner;
    @Nonnull
    private StupidDate date;
    @Nonnull
    private WorkShifts requestedShift;
  }

}

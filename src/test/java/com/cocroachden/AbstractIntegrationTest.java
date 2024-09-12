package com.cocroachden;

import com.cocroachden.planner.common.messaging.Command;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.List;

@RecordApplicationEvents
public class AbstractIntegrationTest extends AbstractSpringBootContextTest {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private ApplicationEvents applicationEvents;

    protected void givenCommandHasBeenSent(Command command) {
        publisher.publishEvent(command);
        applicationEvents.clear();
    }

    protected void whenCommandHasBeenSent(Command command) {
        publisher.publishEvent(command);
    }

    protected <T> T thenExactlyOneEventHasBeenDispatched(Class<T> type) {
        var events = applicationEvents.stream(type).toList();
        Assertions.assertThat(events).hasSize(1);
        return events.get(0);
    }

    protected <T> List<T> thenEventsHasBeenDispatched(Class<T> type, int count) {
        var events = applicationEvents.stream(type).toList();
        Assertions.assertThat(events).hasSize(count);
        return events;
    }

}

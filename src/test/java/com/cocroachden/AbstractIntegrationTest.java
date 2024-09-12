package com.cocroachden;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.common.messaging.Event;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void clearEvents() {
        applicationEvents.clear();
    }

    protected void givenCommandHasBeenSent(Command command) {
        publisher.publishEvent(command);
        applicationEvents.clear();
    }

    protected void whenCommandHasBeenSent(Command command) {
        publisher.publishEvent(command);
    }

    protected void thenCommandThrowsException(Command command, Class<? extends Throwable> throwable) {
        Assertions.assertThatExceptionOfType(throwable)
                .isThrownBy(() -> publisher.publishEvent(command));
    }

    protected <T extends Event> T thenExactlyOneEventHasBeenDispatched(Class<T> type) {
        var events = applicationEvents.stream(type).toList();
        Assertions.assertThat(events).hasSize(1);
        return events.get(0);
    }

    protected <T extends Event> void thenNoEventsOfTypeHaveBeenDispatched(Class<T> type) {
        var events = applicationEvents.stream(type).toList();
        Assertions.assertThat(events).isEmpty();
    }


    protected <T extends Event> List<T> thenEventsHasBeenDispatched(Class<T> type, int count) {
        var events = applicationEvents.stream(type).toList();
        Assertions.assertThat(events).hasSize(count);
        return events;
    }

}

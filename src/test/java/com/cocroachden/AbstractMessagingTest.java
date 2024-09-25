package com.cocroachden;

import com.cocroachden.planner.TestEventReceiver;
import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.common.messaging.Event;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

public class AbstractMessagingTest extends AbstractSpringBootContextTest {

    @Autowired
    protected ApplicationEventPublisher publisher;
    @Autowired
    protected TestEventReceiver testEventReceiver;

    protected void thenSomeTimeHasPassed(Integer millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void clearEvents() {
        testEventReceiver.clearAll();
    }

    protected void givenCommandHasBeenSent(Command command) {
        publisher.publishEvent(command);
    }

    protected void whenCommandHasBeenSent(Command command) {
        testEventReceiver.clearAll();
        publisher.publishEvent(command);
    }

    protected void whenMomentHasPassed(Integer miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void thenCommandThrowsException(Command command, Class<? extends Throwable> throwable) {
        Assertions.assertThatExceptionOfType(throwable)
                .isThrownBy(() -> publisher.publishEvent(command));
    }

    protected <T extends Event> T thenExactlyOneEventHasBeenDispatched(Class<T> type) {
        var events = testEventReceiver.getRecordedEvents(type).toList();
        Assertions.assertThat(events).hasSize(1);
        return events.get(0);
    }

    protected <T extends Event> List<T> thenAtLeastOneEventHasBeenDispatched(Class<T> type) {
        var events = testEventReceiver.getRecordedEvents(type).toList();
        Assertions.assertThat(events.size()).isGreaterThan(0);
        return events;
    }

    protected <T extends Event> void thenNoEventsOfTypeHaveBeenDispatched(Class<T> type) {
        var events = testEventReceiver.getRecordedEvents(type).toList();
        Assertions.assertThat(events).isEmpty();
    }

    protected <T extends Event> List<T> thenEventsHasBeenDispatched(Class<T> type, int count) {
        var events = testEventReceiver.getRecordedEvents(type).toList();
        Assertions.assertThat(events).hasSize(count);
        return events;
    }

    protected <T extends Event> void thenNoEventsHasBeenDispatched(Class<T> type) {
        var events = testEventReceiver.getRecordedEvents(type).toList();
        Assertions.assertThat(events).hasSize(0);
    }
}

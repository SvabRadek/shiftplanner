package com.cocroachden.planner;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.common.messaging.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@Slf4j
@Service
public class TestEventReceiver {

    private final List<Event> recordedEvents = new CopyOnWriteArrayList<>();
    private final List<Command> recordedCommands = new CopyOnWriteArrayList<>();

    @EventListener
    public void on(Event event) {
        recordedEvents.add(event);
    }

    @EventListener
    public void on(Command command) {
        recordedCommands.add(command);
    }

    public void clearEvents() {
        this.recordedEvents.clear();
    }

    public void clearCommands() {
        this.recordedCommands.clear();
    }

    public void clearAll() {
        this.recordedEvents.clear();
        this.recordedCommands.clear();
    }

    public <T extends Event> Stream<T> getRecordedEvents(Class<T> eventType) {
        return recordedEvents.stream()
                .filter(e -> e.getClass().isAssignableFrom(eventType))
                .map(eventType::cast);
    }

    public <T extends Command> Stream<T> getRecordedCommands(Class<T> commandType) {
        return recordedCommands.stream()
                .filter(e -> e.getClass().isAssignableFrom(commandType))
                .map(commandType::cast);
    }

}

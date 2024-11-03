package com.cocroachden.planner.sandbox.endpoint;

import com.cocroachden.planner.sandbox.command.createsignal.CreateSignal;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.EndpointSubscription;
import dev.hilla.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@Endpoint
@Slf4j
@AnonymousAllowed
public class SandboxEndpoint {
    private Sinks.Many<CreateSignal> sink;

    public SandboxEndpoint(ApplicationEventPublisher publisher) {
//        Flux.interval(Duration.ofSeconds(1))
//            .doOnNext(aLong -> publisher.publishEvent(new CreateSignal(aLong)))
//            .subscribe();
    }

    public @Nonnull EndpointSubscription<@Nonnull String> openConnection() {
        log.info("Subscribing Sandbox endpoint");
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        return EndpointSubscription.of(
                sink.asFlux()
                    .map(CreateSignal::index)
                    .map(Object::toString)
                    .doOnComplete(() -> log.debug("Sink completed!")),
                () -> log.info("Unsubscribed from Sandbox endpoint")
        );
    }

    @EventListener
    public void handle(CreateSignal command) {
        log.debug("Handling signal");
        if (sink != null) {
            this.sink.tryEmitNext(command);
        }
    }
}

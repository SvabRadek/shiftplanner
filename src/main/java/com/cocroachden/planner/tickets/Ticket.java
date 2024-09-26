package com.cocroachden.planner.tickets;

import java.util.UUID;

public record Ticket(String value) {
    public static Ticket random() {
        return new Ticket(UUID.randomUUID().toString());
    }

    public static Ticket from(String value) {
        return new Ticket(value);
    }
}

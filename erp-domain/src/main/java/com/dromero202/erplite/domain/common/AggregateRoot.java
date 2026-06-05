package com.dromero202.erplite.domain.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base class for all aggregate roots.
 * Manages domain events that are registered during business operations.
 * Events are stored in memory and cleared after being published.
 *
 * @param <I> the type of the aggregate root's identifier
 */
public abstract class AggregateRoot<I> extends Entity<I> {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot() {
        super();
    }

    protected AggregateRoot(I id) {
        super(id);
    }

    /**
     * Registers a domain event to be published after the operation completes.
     */
    protected void registerEvent(DomainEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Domain event cannot be null");
        }
        domainEvents.add(event);
    }

    /**
     * Returns an unmodifiable view of the registered domain events.
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Clears all registered domain events after they have been published.
     */
    public void clearDomainEvents() {
        domainEvents.clear();
    }

    /**
     * Equality is based solely on the aggregate's identity (ID), not on domain events.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregateRoot<?> that = (AggregateRoot<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

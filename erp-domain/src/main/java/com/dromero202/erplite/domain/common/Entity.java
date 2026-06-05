package com.dromero202.erplite.domain.common;

import java.util.Objects;

/**
 * Base class for all domain entities.
 * Entities have identity and equality based on their ID.
 *
 * @param <I> the type of the entity's identifier
 */
public abstract class Entity<I> {

    protected final I id;

    protected Entity() {
        this.id = null;
    }

    protected Entity(I id) {
        if (id == null) {
            throw new IllegalArgumentException("Entity ID cannot be null");
        }
        this.id = id;
    }

    public I getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

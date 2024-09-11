package com.cocroachden.planner.common;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Embeddable
public abstract class AbstractIdentity implements Serializable {
    protected String id;

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

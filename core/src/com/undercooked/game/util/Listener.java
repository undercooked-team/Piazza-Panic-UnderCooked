package com.undercooked.game.util;

/**
 * Listener class to be initialized wherever it is needed
 * through an anonymous class defining the {@link #tell(T)}
 * function to do something.
 * @param <T> The type to tell.
 */
public abstract class Listener<T> {
    /**
     * Tell the {@link Listener} a value.
     * @param value {@link T} : The value to tell the {@link Listener}.
     */
    public abstract void tell(T value);
}

package com.undercooked.game.util;

/**
 * Observer class to be initialized wherever it is needed
 * through an anonymous class defining the {@link #observe()}
 * function return a value.
 * @param <T> The type to return.
 */
public abstract class Observer<T> {
    /**
     * Return a {@link T} value to what is observing.
     * @return T {@link T} : The value to observe.
     */
    public abstract  T observe();
}

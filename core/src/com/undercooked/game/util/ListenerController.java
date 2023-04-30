package com.undercooked.game.util;

import com.badlogic.gdx.utils.Array;

/**
 * A class that allows for easy use of adding and removing listeners.
 * @param <T> The {@link Listener} type.
 */
public class ListenerController<T> {
    /** The {@link Listener}s of the {@link ListenerController}. */
    protected Array<Listener<T>> listeners;

    /**
     * Constructor for the {@link ListenerController}.
     */
    public ListenerController() {
        listeners = new Array<>();
    }

    /**
     * Add a {@link Listener}.
     *
     * @param listener {@link Listener<T>} : The {@link Listener} to add.
     */
    public void addListener(Listener<T> listener) {
        listeners.add(listener);
    }

    /**
     * Remove a {@link Listener}
     *
     * @param listener {@link Listener<T>} : The {@link Listener} to remove.
     */
    public void removeListener(Listener<T> listener) {
        listeners.removeValue(listener, true);
    }

    /**
     * Tell the {@link Listener}s a value.
     *
     * @param value {@link T} : The value to tell.
     */
    public void tellListeners(T value) {
        for (Listener<T> listener : listeners) {
            listener.tell(value);
        }
    }

}

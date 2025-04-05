package org.thinkingstudio.foxifiedlibrary.impl.event;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.thinkingstudio.foxifiedlibrary.api.event.EventPhase;
import org.thinkingstudio.foxifiedlibrary.impl.event.toposort.SortableNode;

/**
 * Data of an {@link ArrayBackedEvent} phase.
 */
class EventPhaseData<T> extends SortableNode<EventPhaseData<T>> {
    final EventPhase phase;
    T[] listeners;

    @SuppressWarnings("unchecked")
    EventPhaseData(EventPhase phase, Class<?> listenerClass) {
        this.phase = phase;
        this.listeners = (T[]) Array.newInstance(listenerClass, 0);
    }

    void addListener(T listener) {
        int oldLength = listeners.length;
        listeners = Arrays.copyOf(listeners, oldLength + 1);
        listeners[oldLength] = listener;
    }

    @Override
    protected String getDescription() {
        return phase.toString();
    }
}

package org.thinkingstudio.foxifiedlibrary.impl.event;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.thinkingstudio.foxifiedlibrary.api.event.Event;
import org.thinkingstudio.foxifiedlibrary.api.event.EventPhase;
import org.thinkingstudio.foxifiedlibrary.impl.event.toposort.NodeSorting;

class ArrayBackedEvent<T> extends Event<T> {
    private final Function<T[], T> invokerFactory;
    private final Object lock = new Object();
    private T[] handlers;
    /**
     * Registered event phases.
     */
    private final Map<EventPhase, EventPhaseData<T>> phases = new LinkedHashMap<>();
    /**
     * Phases sorted in the correct dependency order.
     */
    private final List<EventPhaseData<T>> sortedPhases = new ArrayList<>();

    @SuppressWarnings("unchecked")
    ArrayBackedEvent(Class<? super T> type, Function<T[], T> invokerFactory) {
        this.invokerFactory = invokerFactory;
        this.handlers = (T[]) Array.newInstance(type, 0);
        update();
    }

    void update() {
        this.invoker = invokerFactory.apply(handlers);
    }

    @Override
    public void register(T listener) {
        register(EventPhase.DEFAULT, listener);
    }

    @Override
    public void register(EventPhase phaseIdentifier, T listener) {
        Objects.requireNonNull(phaseIdentifier, "Tried to register a listener for a null phase!");
        Objects.requireNonNull(listener, "Tried to register a null listener!");

        synchronized (lock) {
            getOrCreatePhase(phaseIdentifier, true).addListener(listener);
            rebuildInvoker(handlers.length + 1);
        }
    }

    private EventPhaseData<T> getOrCreatePhase(EventPhase eventPhase, boolean sortIfCreate) {
        EventPhaseData<T> phase = phases.get(eventPhase);

        if (phase == null) {
            phase = new EventPhaseData<>(eventPhase, handlers.getClass().getComponentType());
            phases.put(eventPhase, phase);
            sortedPhases.add(phase);

            if (sortIfCreate) {
                NodeSorting.sort(sortedPhases, "event phases", Comparator.comparing(data -> data.phase));
            }
        }

        return phase;
    }

    private void rebuildInvoker(int newLength) {
        // Rebuild handlers.
        if (sortedPhases.size() == 1) {
            // Special case with a single phase: use the array of the phase directly.
            handlers = sortedPhases.get(0).listeners;
        } else {
            @SuppressWarnings("unchecked")
            T[] newHandlers = (T[]) Array.newInstance(handlers.getClass().getComponentType(), newLength);
            int newHandlersIndex = 0;

            for (EventPhaseData<T> existingPhase : sortedPhases) {
                int length = existingPhase.listeners.length;
                System.arraycopy(existingPhase.listeners, 0, newHandlers, newHandlersIndex, length);
                newHandlersIndex += length;
            }

            handlers = newHandlers;
        }

        // Rebuild invoker.
        update();
    }

    @Override
    public void addPhaseOrdering(EventPhase firstPhase, EventPhase secondPhase) {
        Objects.requireNonNull(firstPhase, "Tried to add an ordering for a null phase.");
        Objects.requireNonNull(secondPhase, "Tried to add an ordering for a null phase.");
        if (firstPhase.equals(secondPhase)) throw new IllegalArgumentException("Tried to add a phase that depends on itself.");

        synchronized (lock) {
            EventPhaseData<T> first = getOrCreatePhase(firstPhase, false);
            EventPhaseData<T> second = getOrCreatePhase(secondPhase, false);
            EventPhaseData.link(first, second);
            NodeSorting.sort(this.sortedPhases, "event phases", Comparator.comparing(data -> data.phase));
            rebuildInvoker(handlers.length);
        }
    }
}

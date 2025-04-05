package org.thinkingstudio.foxifiedlibrary.api.loader.entrypoint;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;

/**
 * A mod initializer ran only on {@link net.neoforged.api.distmarker.Dist#CLIENT}.
 *
 * <p>This entrypoint is suitable for setting up client-specific logic, such as rendering
 * or integrated server tweaks.</p>
 *
 * @see ModInitializer
 * @see DedicatedServerModInitializer
 */
@FunctionalInterface
public interface ClientModInitializer extends IExtensionPoint {
    /**
     * Runs the mod initializer on the client environment.
     */
    void onInitializeClient(ModContainer modContainer, IEventBus modEventBus);
}

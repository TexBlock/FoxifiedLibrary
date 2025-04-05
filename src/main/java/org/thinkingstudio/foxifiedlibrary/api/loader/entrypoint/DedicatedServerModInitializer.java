package org.thinkingstudio.foxifiedlibrary.api.loader.entrypoint;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;

/**
 * A mod initializer ran only on {@link net.neoforged.api.distmarker.Dist#DEDICATED_SERVER}.
 *
 * @see ModInitializer
 * @see ClientModInitializer
 */
@FunctionalInterface
public interface DedicatedServerModInitializer extends IExtensionPoint {
    /**
     * Runs the mod initializer on the server environment.
     */
    void onInitializeServer(ModContainer modContainer, IEventBus modEventBus);
}

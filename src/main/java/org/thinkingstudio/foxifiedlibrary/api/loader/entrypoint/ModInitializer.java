package org.thinkingstudio.foxifiedlibrary.api.loader.entrypoint;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;

/**
 * A mod initializer.
 *
 * @see ClientModInitializer
 * @see DedicatedServerModInitializer
 */
@FunctionalInterface
public interface ModInitializer extends IExtensionPoint {
    /**
     * Runs the mod initializer.
     */
    void onInitialize(ModContainer modContainer, IEventBus modEventBus);
}

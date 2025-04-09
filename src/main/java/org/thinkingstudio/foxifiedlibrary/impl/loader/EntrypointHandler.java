package org.thinkingstudio.foxifiedlibrary.impl.loader;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import org.thinkingstudio.foxifiedlibrary.api.loader.entrypoint.ClientModInitializer;
import org.thinkingstudio.foxifiedlibrary.api.loader.entrypoint.DedicatedServerModInitializer;
import org.thinkingstudio.foxifiedlibrary.api.loader.entrypoint.ModInitializer;

import java.util.Optional;

public class EntrypointHandler {
    public static void init() {
        ModList.get().forEachModContainer((modId, modContainer) -> {
            Optional<ModInitializer> modInitializer = modContainer.getCustomExtension(ModInitializer.class);
            modInitializer.ifPresent(entrypoint -> entrypoint.onInitialize(modContainer, modContainer.getEventBus()));
            if (FMLLoader.getDist().isClient()) {
                Optional<ClientModInitializer> clientModInitializer = modContainer.getCustomExtension(ClientModInitializer.class);
                clientModInitializer.ifPresent(entrypoint -> entrypoint.onInitializeClient(modContainer, modContainer.getEventBus()));
            }

            if (FMLLoader.getDist().isDedicatedServer()) {
                Optional<DedicatedServerModInitializer> serverModInitializer = modContainer.getCustomExtension(DedicatedServerModInitializer.class);
                serverModInitializer.ifPresent(entrypoint -> entrypoint.onInitializeServer(modContainer, modContainer.getEventBus()));
            }
        });
    }
}

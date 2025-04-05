package org.thinkingstudio.foxifiedlibrary;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkingstudio.foxifiedlibrary.api.loader.entrypoint.ClientModInitializer;
import org.thinkingstudio.foxifiedlibrary.api.loader.entrypoint.DedicatedServerModInitializer;
import org.thinkingstudio.foxifiedlibrary.api.loader.entrypoint.ModInitializer;

import java.util.Optional;

@Mod(FoxifiedLibraryMod.MOD_ID)
public final class FoxifiedLibraryMod {
    public static final String MOD_ID = "foxified_library";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public FoxifiedLibraryMod(IEventBus modEventBus) {
        modEventBus.addListener(FMLLoadCompleteEvent.class, event -> {
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
        });
    }
}

package org.thinkingstudio.foxifiedlibrary.impl.loader;

import cpw.mods.modlauncher.ArgumentHandler;
import cpw.mods.modlauncher.Launcher;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import org.thinkingstudio.foxifiedlibrary.api.loader.FoxifiedLoader;
import org.thinkingstudio.foxifiedlibrary.api.loader.ObjectShare;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;

public class FoxifiedLoaderImpl implements FoxifiedLoader {
    public static final FoxifiedLoaderImpl INSTANCE = InitHelper.get();

    private final ObjectShare objectShare = new ObjectShareImpl();

    private String[] launchArgs;

    @Override
    public ObjectShare getObjectShare() {
        return objectShare;
    }

    @Override
    public Optional<ModContainer> getModContainer(String id) {
        return ModList.get().getModContainerById(id).map(modContainer -> modContainer);
    }

    @Override
    public Collection<ModContainer> getAllMods() {
        return ModList.get().getSortedMods();
    }

    @Override
    public boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Dist getEnvironmentType() {
        return FMLLoader.getDist();
    }

    @Override
    public Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public String[] getLaunchArguments(boolean sanitize) {
        if (launchArgs == null) {
            try {
                Field argumentHandlerField = Launcher.class.getDeclaredField("argumentHandler");
                argumentHandlerField.setAccessible(true);
                ArgumentHandler handler = (ArgumentHandler) argumentHandlerField.get(Launcher.INSTANCE);
                launchArgs = handler.buildArgumentList();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        return launchArgs;
    }

    /**
     * Provides singleton for static init assignment regardless of load order.
     */
    public static class InitHelper {
        private static FoxifiedLoaderImpl instance;

        public static FoxifiedLoaderImpl get() {
            if (instance == null) instance = new FoxifiedLoaderImpl();

            return instance;
        }
    }
}

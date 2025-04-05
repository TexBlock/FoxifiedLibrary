package org.thinkingstudio.foxifiedlibrary.api.loader;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;
import org.thinkingstudio.foxifiedlibrary.impl.loader.FoxifiedLoaderImpl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The public-facing FoxifiedLoader instance.
 *
 * <p>To obtain a working instance, call {@link #getInstance()}.</p>
 */
public interface FoxifiedLoader {
    /**
     * Returns the public-facing Fabric Loader instance.
     */
    static FoxifiedLoader getInstance() {
        FoxifiedLoader ret = FoxifiedLoaderImpl.INSTANCE;

        if (ret == null) {
            throw new RuntimeException("Accessed FoxifiedLoader too early!");
        }

        return ret;
    }

    /**
     * Get the object share for inter-mod communication.
     *
     * <p>The share allows mods to exchange data without directly referencing each other. This makes simple interaction
     * easier by eliminating any compile- or run-time dependencies if the shared value type is independent of the mod
     * (only Java/game/Loader types like collections, primitives, String, Consumer, Function, ...).
     *
     * <p>Active interaction is possible as well since the shared values can be arbitrary Java objects. For example
     * exposing a {@code Runnable} or {@code Function} allows the "API" user to directly invoke some program logic.
     *
     * <p>It is required to prefix the share key with the mod id like {@code mymod:someProperty}. Mods should not
     * modify entries by other mods. The share is thread safe.
     *
     * @return the global object share instance
     */
    ObjectShare getObjectShare();

    /**
     * Gets the container for a given mod.
     *
     * @param id the ID of the mod
     * @return the mod container, if present
     */
    Optional<? extends ModContainer> getModContainer(String id);

    /**
     * Gets all mod containers.
     *
     * @return a collection of all loaded mod containers
     */
    Collection<ModContainer> getAllMods();

    /**
     * Checks if a mod with a given ID is loaded.
     *
     * @param id the ID of the mod, as defined in {@code fabric.mod.json}
     * @return whether or not the mod is present in this Fabric Loader instance
     */
    boolean isModLoaded(String id);

    /**
     * Checks if Fabric Loader is currently running in a "development"
     * environment. Can be used for enabling debug mode or additional checks.
     *
     * <p>This should not be used to make assumptions on certain features,
     * such as mappings, but as a toggle for certain functionalities.</p>
     *
     * @return whether or not Loader is currently in a "development"
     * environment
     */
    boolean isDevelopmentEnvironment();

    /**
     * Get the current environment type.
     *
     * @return the current environment type
     */
    Dist getEnvironmentType();

    /**
     * Get the current game working directory.
     *
     * @return the working directory
     */
    Path getGameDir();

    /**
     * Get the current directory for game configuration files.
     *
     * @return the configuration directory
     */
    Path getConfigDir();

    /**
     * Gets the command line arguments used to launch the game.
     *
     * <p>The implementation will try to strip or obscure sensitive data like authentication tokens if {@code sanitize}
     * is set to true. Callers are highly encouraged to enable sanitization as compromising the information can easily
     * happen with logging, exceptions, serialization or other causes.
     *
     * <p>There is no guarantee that {@code sanitize} covers everything, so the launch arguments should still not be
     * logged or otherwise exposed routinely even if the parameter is set to {@code true}. In particular it won't
     * necessarily strip all information that can be used to identify the user.
     *
     * @param sanitize Whether to try to remove or obscure sensitive information
     * @return the launch arguments for the game
     */
    String[] getLaunchArguments(boolean sanitize);
}

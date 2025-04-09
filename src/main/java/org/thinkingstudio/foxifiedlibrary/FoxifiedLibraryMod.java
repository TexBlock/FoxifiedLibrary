package org.thinkingstudio.foxifiedlibrary;

import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkingstudio.foxifiedlibrary.impl.loader.EntrypointHandler;

@Mod(FoxifiedLibraryMod.MOD_ID)
public final class FoxifiedLibraryMod {
    public static final String MOD_ID = "foxified_library";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public FoxifiedLibraryMod() {
        EntrypointHandler.init();
    }
}

package eu.pb4.simpleresourceloader;


import fish.cichlidmc.cichlid.api.loaded.Mod;
import fish.cichlidmc.cichlid.api.mod.entrypoint.PreLaunchEntrypoint;
import net.minecraft.server.packs.PackType;

import java.nio.file.Files;

public class ModInit implements PreLaunchEntrypoint {
    @Override
    public void preLaunch(Mod mod) {
        try {
            Files.createDirectories(SimpleProvider.getPath(PackType.CLIENT_RESOURCES, false));
            Files.createDirectories(SimpleProvider.getPath(PackType.CLIENT_RESOURCES, true));
            Files.createDirectories(SimpleProvider.getPath(PackType.SERVER_DATA, false));
            Files.createDirectories(SimpleProvider.getPath(PackType.SERVER_DATA, true));
            Files.createDirectories(SimpleProvider.getPath(null, false));
            Files.createDirectories(SimpleProvider.getPath(null, true));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

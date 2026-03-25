package eu.pb4.simpleresourceloader;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.packs.PackType;
import java.nio.file.Files;


public class ModInit implements ModInitializer {
    @Override
    public void onInitialize() {
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

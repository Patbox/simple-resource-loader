package eu.pb4.simpleresourceloader;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.level.validation.DirectoryValidator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record SimpleProvider(PackType type, DirectoryValidator symlinkFinder) implements RepositorySource {
    private static final PackSelectionConfig OPTIONAL_POSITION = new PackSelectionConfig(false, Pack.Position.TOP, false);
    private static final PackSelectionConfig REQUIRED_POSITION = new PackSelectionConfig(true, Pack.Position.TOP, false);

    private static final PackSource SOURCE = PackSource.BUILT_IN;

    @Override
    public void loadPacks(Consumer<Pack> profileAdder) {
        for (var sided : List.of(true, false)) {
            var prefix = "srl_" + (sided ? switch (this.type) {
                case SERVER_DATA -> "dp";
                case CLIENT_RESOURCES -> "rp";
            } : "cm");
            for (var required : List.of(true, false)) {
                var position = required ? REQUIRED_POSITION : OPTIONAL_POSITION;
                var basePath = getPath(sided ? this.type : null, required);
                if (Files.exists(basePath)) {
                    try {
                        FolderRepositorySource.discoverPacks(basePath, symlinkFinder, (path, packFactory) -> {
                            PackLocationInfo resourcePackInfo = this.createPackInfo(path, prefix);
                            Pack resourcePackProfile = Pack.readMetaAndCreate(resourcePackInfo, packFactory, this.type, position);
                            if (resourcePackProfile != null) {
                                profileAdder.accept(resourcePackProfile);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static String getFileName(Path path) {
        return path.getFileName().toString();
    }

    private PackLocationInfo createPackInfo(Path path, String prefix) {
        String string = getFileName(path);
        return new PackLocationInfo( prefix + "/" + string, Component.literal(string), SOURCE, Optional.empty());
    }
    public static Path getPath(PackType type, boolean required) {
        var str = switch (type) {
            case SERVER_DATA -> "datapack";
            case CLIENT_RESOURCES -> "resourcepack";
            case null -> "common";
        };
        return FabricLoader.getInstance().getGameDir().resolve("resources/" + str + "/" + (required ? "required" : "optional"));
    }
}

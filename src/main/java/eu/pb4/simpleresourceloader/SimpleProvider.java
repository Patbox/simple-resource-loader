package eu.pb4.simpleresourceloader;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.util.path.SymlinkFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record SimpleProvider(ResourceType type, SymlinkFinder symlinkFinder) implements ResourcePackProvider {
    private static final ResourcePackPosition OPTIONAL_POSITION = new ResourcePackPosition(false, ResourcePackProfile.InsertionPosition.TOP, false);
    private static final ResourcePackPosition REQUIRED_POSITION = new ResourcePackPosition(true, ResourcePackProfile.InsertionPosition.TOP, false);

    private static final ResourcePackSource SOURCE = ResourcePackSource.BUILTIN;

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
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
                        FileResourcePackProvider.forEachProfile(basePath, symlinkFinder, (path, packFactory) -> {
                            ResourcePackInfo resourcePackInfo = this.createPackInfo(path, prefix);
                            ResourcePackProfile resourcePackProfile = ResourcePackProfile.create(resourcePackInfo, packFactory, this.type, position);
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

    private ResourcePackInfo createPackInfo(Path path, String prefix) {
        String string = getFileName(path);
        return new ResourcePackInfo( prefix + "/" + string, Text.literal(string), SOURCE, Optional.empty());
    }
    public static Path getPath(ResourceType type, boolean required) {
        var str = switch (type) {
            case SERVER_DATA -> "datapack";
            case CLIENT_RESOURCES -> "resourcepack";
            case null -> "common";
        };
        return FabricLoader.getInstance().getGameDir().resolve("resources/" + str + "/" + (required ? "required" : "optional"));
    }
}

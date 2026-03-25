package eu.pb4.simpleresourceloader.mixin;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BuiltInPackSource.class)
public interface VanillaResourcePackProviderAccessor {
    @Accessor
    PackType getPackType();

    @Accessor
    DirectoryValidator getValidator();
}

package eu.pb4.simpleresourceloader.mixin;

import net.minecraft.resource.ResourceType;
import net.minecraft.resource.VanillaResourcePackProvider;
import net.minecraft.util.path.SymlinkFinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VanillaResourcePackProvider.class)
public interface VanillaResourcePackProviderAccessor {
    @Accessor
    ResourceType getType();

    @Accessor
    SymlinkFinder getSymlinkFinder();
}

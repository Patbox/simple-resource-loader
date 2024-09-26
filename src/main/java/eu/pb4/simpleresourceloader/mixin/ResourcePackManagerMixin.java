package eu.pb4.simpleresourceloader.mixin;

import eu.pb4.simpleresourceloader.SimpleProvider;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashSet;
import java.util.Set;


@Mixin(value = ResourcePackManager.class, priority = 600)
public abstract class ResourcePackManagerMixin {
	@Mutable
	@Shadow @Final private Set<ResourcePackProvider> providers;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void addCustomProvider(ResourcePackProvider[] resourcePackProviders, CallbackInfo info) {
		for (var x : resourcePackProviders) {
			if (x instanceof VanillaResourcePackProviderAccessor accessor) {
				this.providers = new LinkedHashSet<>(this.providers);
				this.providers.add(new SimpleProvider(accessor.getType(), accessor.getSymlinkFinder()));
				return;
			}
		}
	}
}
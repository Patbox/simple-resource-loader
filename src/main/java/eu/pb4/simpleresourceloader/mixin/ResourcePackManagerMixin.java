package eu.pb4.simpleresourceloader.mixin;

import eu.pb4.simpleresourceloader.SimpleProvider;
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
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;


@Mixin(value = PackRepository.class, priority = 600)
public abstract class ResourcePackManagerMixin {
	@Mutable
	@Shadow @Final private Set<RepositorySource> sources;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void addCustomProvider(RepositorySource[] resourcePackProviders, CallbackInfo info) {
		for (var x : resourcePackProviders) {
			if (x instanceof VanillaResourcePackProviderAccessor accessor) {
				this.sources = new LinkedHashSet<>(this.sources);
				this.sources.add(new SimpleProvider(accessor.getPackType(), accessor.getValidator()));
				return;
			}
		}
	}
}
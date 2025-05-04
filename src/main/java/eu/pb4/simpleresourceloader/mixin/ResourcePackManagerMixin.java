package eu.pb4.simpleresourceloader.mixin;

import com.google.common.collect.ImmutableSet;
import eu.pb4.simpleresourceloader.SimpleProvider;
import fish.cichlidmc.sushi.api.transform.wrap_op.Operation;
import net.minecraft.Util;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.level.validation.DirectoryValidator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


//@Mixin(value = ResourcePackManager.class, priority = 600)
public class ResourcePackManagerMixin {
	private static final Field TYPE_GETTER;
	private static final Field VALIDATOR_GETTER;

	public static ImmutableSet<RepositorySource> addCustomProvider(RepositorySource[] resourcePackProviders, Operation<ImmutableSet<RepositorySource>> operation) {
		var arr = new ArrayList<>(List.of(resourcePackProviders));

		for (var x : resourcePackProviders) {
			if (x instanceof BuiltInPackSource accessor) {
                try {
                    arr.add(new SimpleProvider((PackType) TYPE_GETTER.get(x), (DirectoryValidator) VALIDATOR_GETTER.get(x)));
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
                break;
			}
		}

		return operation.call(arr.toArray(RepositorySource[]::new));
	}

	static {
		try {
			var field = BuiltInPackSource.class.getField("packType");
			field.setAccessible(true);
			TYPE_GETTER = field;

			field = BuiltInPackSource.class.getField("validator");
			field.setAccessible(true);
			VALIDATOR_GETTER = field;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
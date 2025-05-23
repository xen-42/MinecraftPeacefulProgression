package xen42.peacefulitems;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class PeacefulModPotions {
    public static final RegistryEntry<Potion> LEVITATION = register("levitation", new Potion("levitation", new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.LEVITATION, 600) }));

    private static RegistryEntry<Potion> register(String name, Potion potion) {
        return (RegistryEntry<Potion>)Registry.registerReference(Registries.POTION, Identifier.of(PeacefulMod.MOD_ID, name), potion);
    }

    public static void initialize() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(
                    Potions.AWKWARD,
                    PeacefulModItems.ECTOPLASM,
                    LEVITATION
            );
        });
    }
}

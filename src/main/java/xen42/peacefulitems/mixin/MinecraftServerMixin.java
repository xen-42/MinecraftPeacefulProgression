package xen42.peacefulitems.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.spawner.SpecialSpawner;
import xen42.peacefulitems.CustomSpawner;
import xen42.peacefulitems.PeacefulMod;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    // This is what makes the nether also superflat
	@Inject(at = @At("RETURN"), method = "createWorlds")
    private void peacefulprogression_createWorlds(CallbackInfo info) {
        MinecraftServer server = (MinecraftServer) (Object) this;

        ServerWorld nether = server.getWorld(World.NETHER);
        var spawners = new ArrayList<SpecialSpawner>();
        spawners.addAll(nether.spawners);
        spawners.add(new CustomSpawner(PeacefulMod.END_CLAM_ENTITY).setBiome(BiomeSelectors.includeByKey(BiomeKeys.WARPED_FOREST)).setMaxCount(10)); 
        nether.spawners = spawners;
    }
}

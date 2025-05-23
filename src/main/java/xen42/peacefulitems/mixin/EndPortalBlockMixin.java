package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {
    @Inject(at = @At("HEAD"), method = "onEntityCollision", cancellable = true)
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, CallbackInfo info) {
        // Grant the player the "Free the End" advancement
    	if (entity.canUsePortals(false) && !world.isClient && world.getRegistryKey() == World.END && entity instanceof ServerPlayerEntity player && player.getServer() != null)
        {
    		AdvancementEntry freeTheEnd = player.getServer().getAdvancementLoader().get(Identifier.ofVanilla("end/kill_dragon"));
    		if (freeTheEnd != null)
    		{
    			String first = freeTheEnd.value().criteria().keySet().iterator().next();
    			player.getAdvancementTracker().grantCriterion(freeTheEnd, first);
    		}
    	}
    }
}

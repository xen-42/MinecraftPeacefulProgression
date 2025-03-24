package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.boss.dragon.EnderDragonFight;

@Mixin(EnderDragonFight.class)
public interface EnderDragonFight_Invoker {
    @Invoker("generateNewEndGateway")
    public void invokeGenerateNewEndGateway();
}

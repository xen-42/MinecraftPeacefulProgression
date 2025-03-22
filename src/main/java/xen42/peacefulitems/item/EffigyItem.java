package xen42.peacefulitems.item;

import java.util.function.Consumer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import xen42.peacefulitems.EffigyParticlePayload;

public class EffigyItem extends Item {

    private String _particleID;
    private Consumer<ServerPlayerEntity> _onUse;
    private SoundEvent _sound;

    public EffigyItem(Settings settings, String particleID, Consumer<ServerPlayerEntity> onUse, SoundEvent sound) {
        super(settings);
        _particleID = particleID;
        _onUse = onUse;
        _sound = sound;
    }
    
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        world.playSoundFromEntity(user, _sound, SoundCategory.HOSTILE, 0.2f, 1f);
        itemStack.decrement(1);

        if (!world.isClient()) {
            _onUse.accept((ServerPlayerEntity)user);
            ServerPlayNetworking.send((ServerPlayerEntity)user, new EffigyParticlePayload(_particleID));
        }

        return ActionResult.CONSUME;
    }
}

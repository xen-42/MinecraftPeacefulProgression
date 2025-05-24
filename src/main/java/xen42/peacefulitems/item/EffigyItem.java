package xen42.peacefulitems.item;

import java.util.function.Consumer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import xen42.peacefulitems.payloads.EffigyParticlePayload;

public class EffigyItem extends Item {

    private String _particleID;
    private Consumer<ServerPlayerEntity> _onUse;
    private SoundEvent _sound;
    private RegistryEntry.Reference<SoundEvent> _regSound;

    public EffigyItem(Settings settings, String particleID, Consumer<ServerPlayerEntity> onUse, SoundEvent sound) {
        super(settings);
        _particleID = particleID;
        _onUse = onUse;
        _sound = sound;
    }

        public EffigyItem(Settings settings, String particleID, Consumer<ServerPlayerEntity> onUse, RegistryEntry.Reference<SoundEvent> regSound) {
        super(settings);
        _particleID = particleID;
        _onUse = onUse;
        _regSound = regSound;
    }
    
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (_sound != null) {
            world.playSound(user, user.getBlockPos(), _sound, SoundCategory.HOSTILE, 0.2f, 1f);
        }

        if (!user.isCreative()) {
            itemStack.decrement(1);
        }

        if (!world.isClient()) {
            _onUse.accept((ServerPlayerEntity)user);
            ServerPlayNetworking.send((ServerPlayerEntity)user, new EffigyParticlePayload(_particleID));

            // If the sound is provided as a reg key we do this server side
            if (_regSound != null) {
                 var serverPlayerEntity = (ServerPlayerEntity)user;
                serverPlayerEntity.networkHandler.sendPacket(new PlaySoundS2CPacket(_regSound, SoundCategory.NEUTRAL, 
                    serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), 128.0F, 1.0F, 1l));
            }
        }

        return ActionResult.CONSUME;
    }
}

package xen42.peacefulitems.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class EndClamEntity extends AmbientEntity {
    public final AnimationState idleAnimationState;
    public final AnimationState hitAnimationState;
    public final AnimationState yawnAnimationState;

    public EndClamEntity(EntityType<? extends AmbientEntity> entityType, World world) {
        super(entityType, world);
        this.idleAnimationState = new AnimationState();
        this.hitAnimationState = new AnimationState();
        this.yawnAnimationState = new AnimationState();
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_CONDUIT_AMBIENT;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    public static boolean isValidSpawn(EntityType<? extends EndClamEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos).isAir() && !world.getBlockState(pos.down()).isAir() && !world.getBlockState(pos).isOf(Blocks.LAVA);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return AmbientEntity.createMobAttributes()
            .add(EntityAttributes.MAX_HEALTH, 10)
            .add(EntityAttributes.SCALE, 1.5);
    }

    @Override
    public void tick() {
        super.tick();

        updateAnimations();
    }

    public void updateAnimations() {

    }
}

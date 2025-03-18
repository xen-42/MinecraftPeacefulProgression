package xen42.peacefulitems.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
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
    private boolean _wasJustHit;
    private boolean _isYawning;
    private boolean _willYawn;
    private long _revertToIdleTick;
    private long _idleStartTick;

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
    public void onDamaged(DamageSource damageSource) {
        super.onDamaged(damageSource);
        _wasJustHit = true;
        _revertToIdleTick = getWorld().getTime() + (1 * 20);
        playSound(SoundEvents.BLOCK_SHULKER_BOX_OPEN);

        idleAnimationState.stop();
        yawnAnimationState.stop();
        hitAnimationState.stop();
        hitAnimationState.start(this.age);

        this.playSound(SoundEvents.ENTITY_SHULKER_HURT);
    }

    @Override
    public void tick() {
        super.tick();

        // On average every 15 seconds
        if (!_isYawning && !_willYawn && !_wasJustHit && this.getRandom().nextFloat() < (1f / (20f * 15f))) {
            // Will then yawm when the idle animation loops next
            _willYawn = true;
        }

        if (_wasJustHit && this.getWorld().getTime() > _revertToIdleTick) {
            _wasJustHit = false;
        }

        if (_isYawning && this.getWorld().getTime() > _revertToIdleTick) {
            _isYawning = false;
        }

        updateAnimations();
    }

    public void updateAnimations() {
        if (!idleAnimationState.isRunning() && !_wasJustHit && !_isYawning) {
            hitAnimationState.stop();
            yawnAnimationState.stop();
            idleAnimationState.start(this.age);
            _idleStartTick = this.getWorld().getTime();
        }

        // Only start yawning if the idle animation has looped and closed its mouth
        if (_willYawn && !yawnAnimationState.isRunning() && (this.getWorld().getTime() - _idleStartTick) % (20 * 8) == 0) {
            idleAnimationState.stop();
            hitAnimationState.stop();
            yawnAnimationState.start(this.age);

            playSound(SoundEvents.BLOCK_SHULKER_BOX_OPEN, 0.5f, 1f);
            _revertToIdleTick = getWorld().getTime() + (5 * 20);
            _isYawning = true;
            _willYawn = false;
        }
    }
}

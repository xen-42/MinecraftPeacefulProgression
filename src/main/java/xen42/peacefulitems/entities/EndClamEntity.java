package xen42.peacefulitems.entities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import xen42.peacefulitems.PeacefulModItems;

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
        setCanPickUpLoot(true);
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
            .add(EntityAttributes.MAX_HEALTH, 20)
            .add(EntityAttributes.SCALE, 1.5);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (random.nextFloat() < 0.6f) {
            this.teleportRandomly();
        }

        return super.damage(world, source, amount);
    }

    @Override
    public void onDamaged(DamageSource damageSource) {
        super.onDamaged(damageSource);
        // I think this happens on client and damaged happens on server? idk man
        _revertToIdleTick = getWorld().getTime() + (long)(0.6f * 20);

        idleAnimationState.stop();
        yawnAnimationState.stop();
        hitAnimationState.stop();
        hitAnimationState.start(this.age);
        _wasJustHit = true;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SHULKER_HURT;
    }

    @Override
    public void tick() {
        super.tick();

        // On average every 15 seconds
        if (!_isYawning && !_willYawn && !_wasJustHit && this.getRandom().nextFloat() < (1f / (20f * 15f))) {
            // Will then yawn when the idle animation loops next
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

            _revertToIdleTick = getWorld().getTime() + (5 * 20);
            _isYawning = true;
            _willYawn = false;
        }
    }

    protected boolean teleportRandomly() {
        if (getWorld().isClient()) {
            return false; 
        } 

        // Try a few times times
        // Goes up so that when it searchs back down it can go up ledges and stuff
        for (int i = 0; i < 5; i++) {
            var x = getX() + (this.random.nextDouble() - 0.5D) * 8.0D;
            var y = getY() + 6;
            var z = getZ() + (this.random.nextDouble() - 0.5D) * 8.0D;
            if (teleportTo(x, y, z)) {
                return true;
            }
        }
        return false;
    } 

    private boolean teleportTo(double x, double y, double z) {
        var blockPos = new BlockPos.Mutable(x, y, z);

        // Search downwards for an air block with solid block below it
        BlockState blockState, downBlockState;
        boolean flag = false;
        do {
            blockState = getWorld().getBlockState(blockPos);
            downBlockState = getWorld().getBlockState(blockPos.down());
            var isBlockValid = blockState.isAir();
            var isGroundValid = !downBlockState.isAir();
            if (isBlockValid && isGroundValid) {
                flag = true;
                break;
            }
            blockPos.move(Direction.DOWN);
        }
        while(blockPos.getY() - 1 > getWorld().getBottomY());

        if (!flag) {
            return false;
        }

        var successfulTeleport = teleport(blockPos.getX(), blockPos.getY(), blockPos.getZ(), true); 
        if (successfulTeleport) {
            getWorld().emitGameEvent(GameEvent.TELEPORT, getPos(), GameEvent.Emitter.of(this)); 
            if (!isSilent()) {
                getWorld().playSound(null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
                playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            } 
        } 
        return successfulTeleport;    
    } 

    @Override
    public void tickMovement() {
        super.tickMovement();
        if ((getWorld()).isClient && getEquippedStack(EquipmentSlot.MAINHAND).isOf(Items.ENDER_PEARL)) {
            getWorld().addParticle((ParticleEffect)ParticleTypes.PORTAL, 
                getParticleX(0.25D), 
                getRandomBodyY() + 0.25D, 
                getParticleZ(0.25D), 
                (this.random.nextDouble() - 0.5D) * 2.0D, 
                -this.random.nextDouble(), 
                (this.random.nextDouble() - 0.5D) * 2.0D);
        }
    }

    @Override
    protected void loot(ServerWorld world, ItemEntity itemEntity) {
        ItemStack itemStack = itemEntity.getStack();
        if (canPickupItem(itemStack)) {
            if (!getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()) {
                if (!getWorld().isClient) {
                    var thrownItem = new ItemEntity(getWorld(), getX() + (getRotationVector()).x, getY() + 1.0D, getZ() + (getRotationVector()).z, 
                        getEquippedStack(EquipmentSlot.MAINHAND));
                    thrownItem.setPickupDelay(40);
                    thrownItem.setThrower(this);
                    getWorld().spawnEntity(thrownItem);
                }
            }

            int i = itemStack.getCount();
            if (i > 1) {
                dropItem(itemStack.split(i - 1));
            }

            triggerItemPickedUpByEntityCriteria(itemEntity);
            equipStack(EquipmentSlot.MAINHAND, itemStack.split(1));
            updateDropChances(EquipmentSlot.MAINHAND);
            sendPickup(itemEntity, itemStack.getCount());
            itemEntity.discard();
        } 
    }

    private void dropItem(ItemStack stack) {
        var itemEntity = new ItemEntity(getWorld(), getX(), getY(), getZ(), stack);
        getWorld().spawnEntity(itemEntity);
    }

    @Override
    protected void drop(ServerWorld world, DamageSource damageSource) {
        var itemStack = getEquippedStack(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty()) {
            dropStack(world, itemStack);
            equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        super.drop(world, damageSource);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        // Spawn with item
        if (random.nextFloat() < 0.5f) {
            var r = random.nextFloat();
            Item item;
            if (r < 0.5) {
                item = Items.ENDER_PEARL;
            }
            else if (r < 0.75) {
                item = PeacefulModItems.SULPHUR;
            }
            else {
                item = Items.GOLD_NUGGET;
            }

            equipStack(EquipmentSlot.MAINHAND, new ItemStack(item));
        }
        return super.initialize(world, difficulty, spawnReason, entityData);
    }
}

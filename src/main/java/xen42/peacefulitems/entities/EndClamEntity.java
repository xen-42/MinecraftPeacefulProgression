package xen42.peacefulitems.entities;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModItems;

public class EndClamEntity extends AmbientEntity {
    public final AnimationState idleAnimationState;
    public final AnimationState hitAnimationState;
    public final AnimationState yawnAnimationState;
    public final AnimationState openAnimationState;
    private long _revertToIdleTick;
    private long _lastYawn;

    private static final TrackedData<Boolean> IS_OPENING = DataTracker.registerData(EndClamEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_YAWNING = DataTracker.registerData(EndClamEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> WAS_JUST_HIT = DataTracker.registerData(EndClamEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public EndClamEntity(EntityType<? extends AmbientEntity> entityType, World world) {
        super(entityType, world);
        this.idleAnimationState = new AnimationState();
        this.hitAnimationState = new AnimationState();
        this.yawnAnimationState = new AnimationState();
        this.openAnimationState = new AnimationState();
        setCanPickUpLoot(true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
		builder.add(IS_OPENING, false);
		builder.add(IS_YAWNING, false);
		builder.add(WAS_JUST_HIT, false);
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

        if (!this.getWorld().isClient) {
            this.getDataTracker().set(WAS_JUST_HIT, true);
        }

        return super.damage(world, source, amount);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SHULKER_HURT;
    }

    @Override
    public void tick() {
        super.tick();

        // Only yawn when the idle animation has the mouth closed
        if (!getWorld().isClient) {
            if (idleAnimationState.isRunning() && idleAnimationState.getTimeInMilliseconds(this.age) % 4000 == 0 && this.getRandom().nextBoolean()
                    && this.getWorld().getTime() > _lastYawn + 40) {
                this.getDataTracker().set(IS_YAWNING, true);
                this.playSound(SoundEvents.ENTITY_SHULKER_OPEN);
                _lastYawn = this.getWorld().getTime();
            }
        }

        updateAnimations();
    }

    public void updateAnimations() {
        if (!idleAnimationState.isRunning() && !this.getDataTracker().get(WAS_JUST_HIT) 
            && !this.getDataTracker().get(IS_YAWNING) && !this.getDataTracker().get(IS_OPENING)) {
            idleAnimationState.stop();
            yawnAnimationState.stop();
            hitAnimationState.stop();
            openAnimationState.stop();

            idleAnimationState.start(this.age);
        }
        else if (this.getDataTracker().get(WAS_JUST_HIT) && !hitAnimationState.isRunning()) {
            _revertToIdleTick = getWorld().getTime() + (long)(0.6f * 20);

            this.getDataTracker().set(IS_YAWNING, false);
            this.getDataTracker().set(IS_OPENING, false);

            idleAnimationState.stop();
            yawnAnimationState.stop();
            hitAnimationState.stop();
            openAnimationState.stop();

            hitAnimationState.start(this.age);
        }
        else if (this.getDataTracker().get(IS_OPENING) && !openAnimationState.isRunning()) {
            _revertToIdleTick = getWorld().getTime() + (long)(2f * 20);

            this.getDataTracker().set(WAS_JUST_HIT, false);
            this.getDataTracker().set(IS_YAWNING, false);

            idleAnimationState.stop();
            yawnAnimationState.stop();
            hitAnimationState.stop();
            openAnimationState.stop();

            openAnimationState.start(this.age);
        }
        else if (this.getDataTracker().get(IS_YAWNING) && !yawnAnimationState.isRunning()) {
            _revertToIdleTick = getWorld().getTime() + (long)(5f * 20);

            this.getDataTracker().set(WAS_JUST_HIT, false);
            this.getDataTracker().set(IS_OPENING, false);

            idleAnimationState.stop();
            yawnAnimationState.stop();
            hitAnimationState.stop();
            openAnimationState.stop();

            yawnAnimationState.start(this.age);
        }

        if (!this.getWorld().isClient && this.getWorld().getTime() > _revertToIdleTick) {
            this.getDataTracker().set(WAS_JUST_HIT, false);
            this.getDataTracker().set(IS_YAWNING, false);
            this.getDataTracker().set(IS_OPENING, false);
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
        if (!this.getDataTracker().get(IS_OPENING) && canPickupItem(itemStack)) {
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

            if(!this.getWorld().isClient) {
                this.getDataTracker().set(IS_OPENING, true);
                this.playSound(SoundEvents.ENTITY_SHULKER_OPEN);
            }
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

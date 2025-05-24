package xen42.peacefulitems.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.structure.StructureKeys;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModTags;

public class GhastlingEntity extends AnimalEntity implements Flutterer {

    public GhastlingEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2f));
        this.goalSelector.add(2, new GhastlingFleeGoal(this, 16.0F, 1.4D));
        this.goalSelector.add(3, new TemptGoal(this, 1.5f, stack -> stack.isIn(PeacefulModTags.ItemTags.WISP_LIKES), false));
        this.goalSelector.add(4, new FlyGoal(this, 1f));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 3f));
        this.goalSelector.add(6, new LookAroundGoal(this));
    }

    public static boolean isValidSpawn(EntityType<? extends GhastlingEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos).isAir() 
            && LocationPredicate.Builder.createStructure(world.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE).getOrThrow(StructureKeys.FORTRESS)).build()
                .test((ServerWorld)world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(PeacefulModTags.ItemTags.WISP_LIKES);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : world.getPhototaxisFavor(pos);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return (GhastlingEntity)PeacefulMod.GHASTLING_ENTITY.create(world, SpawnReason.BREEDING);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        // Instead of breeding like normal they duplicate
        // Also handle crying
        var item = player.getStackInHand(hand);
        if (!getWorld().isClient && getBreedingAge() == 0 && canEat()) {
            if (isBreedingItem(item)) {
                setBreedingAge(6000);
                eat(player, hand, item);
                playEatSound();
                if (getWorld().getServer().getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                    getWorld().spawnEntity(new ExperienceOrbEntity(getWorld(), getX(), getY(), getZ(), getRandom().nextInt(7) + 1));
                }
                playSound(SoundEvents.ENTITY_GHAST_AMBIENT, 0.5f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.3f);

                var baby = PeacefulMod.GHASTLING_ENTITY.create(getWorld(), SpawnReason.BREEDING);
                baby.refreshPositionAndAngles(getX(), getY(), getZ(), 0.0f, 0.0f);
                getWorld().spawnEntity(baby);
                baby.setBreedingAge(6000);

                return (ActionResult)ActionResult.SUCCESS_SERVER;
            }
            else if (item.isIn(PeacefulModTags.ItemTags.WISP_DISLIKES)) {
                var tear = dropItem((ServerWorld)getWorld(), Items.GHAST_TEAR);
                tear.setPosition(getPos());
                playSound(SoundEvents.ENTITY_GHAST_WARN, 0.5f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.3f);
                // Treating this as breeding for the sake of having a timer and stuff
                setBreedingAge(6000);
                eat(player, hand, item);
                playEatSound();
                // Count this as hurting them so they run from you
                this.damage((ServerWorld)getWorld(), getWorld().getDamageSources().playerAttack(player), 0f);
                return (ActionResult)ActionResult.SUCCESS_SERVER;
            }
        }
        return ActionResult.PASS;
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return AnimalEntity.createAnimalAttributes()
            .add(EntityAttributes.MAX_HEALTH, 10)
            .add(EntityAttributes.FLYING_SPEED, 0.6f)
            .add(EntityAttributes.MOVEMENT_SPEED, 0.3f)
            .add(EntityAttributes.FALL_DAMAGE_MULTIPLIER, 0f)
            .add(EntityAttributes.TEMPT_RANGE, 20f);
    }

    @Override
    public void playHurtSound(DamageSource damageSource) {
        this.playSound(SoundEvents.ENTITY_GHAST_HURT, 1f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.3f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    @Override
    public boolean isInAir() {
        return !isOnGround();
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {}

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    @Override
    public boolean isFlappingWings() {
        return isInAir();
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        var navigation = new BirdNavigation(this, world) {
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        navigation.setCanPathThroughDoors(true);
        navigation.setCanSwim(true);
        navigation.setMaxFollowRange(48f);
        return navigation;
    }
}
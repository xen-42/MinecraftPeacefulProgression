package xen42.peacefulitems.entities;

import java.util.logging.LogManager;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModItems;

public class GhastlingEntity extends AnimalEntity implements Flutterer {

    public GhastlingEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new GhastlingMoveControl(this);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2f));
        this.goalSelector.add(2, new TemptGoal(this, 1.5f, Ingredient.ofItem(PeacefulModItems.SULPHUR), false));
        this.goalSelector.add(3, new FlyGoal(this, 1f));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 3f));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(PeacefulModItems.SULPHUR);
    }

    /*
    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : world.getPhototaxisFavor(pos);
    }
    */

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
            else if (item.isOf(PeacefulModItems.GUANO)) {
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
        return !isOnGround();
    }
}

class GhastlingMoveControl extends MoveControl {

    public GhastlingMoveControl(MobEntity entity) {
        super(entity);
    }

    @Override
    public void tick() {
        PeacefulMod.LOGGER.info("STATE:" + state);

        if (this.state == MoveControl.State.MOVE_TO) {
			this.state = MoveControl.State.WAIT;
			double dx = this.targetX - this.entity.getX();
			double dy = this.targetZ - this.entity.getZ();
			double dz = this.targetY - this.entity.getY();
			double distSquared = dx * dx + dz * dz + dy * dy;
			if (distSquared < 2.5E-7F) {
				this.entity.setForwardSpeed(0.0F);
                this.entity.setUpwardSpeed(0.0F);
				return;
			}

			float yaw = (float)(MathHelper.atan2(dy, dx) * 180.0F / (float)Math.PI) - 90.0F;
			this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), yaw, 90.0F));
            var speed = (float)(this.speed * this.entity.getAttributeValue(entity.isOnGround() ? EntityAttributes.MOVEMENT_SPEED : EntityAttributes.FLYING_SPEED));
			this.entity.setMovementSpeed(speed);		
            
            if (Math.abs(dy) > 1e-5) {
                this.entity.setUpwardSpeed(dy > 0.0 ? speed : -speed);
            }
        }
        else {
            this.entity.setForwardSpeed(0.0F);
            this.entity.setUpwardSpeed(0.0F);
        }
        this.state = MoveControl.State.WAIT;

    }
}
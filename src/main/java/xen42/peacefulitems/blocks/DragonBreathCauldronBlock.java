package xen42.peacefulitems.blocks;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.cauldron.CauldronBehavior.CauldronBehaviorMap;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModBlocks;

public class DragonBreathCauldronBlock extends LeveledCauldronBlock {
    public static final CauldronBehavior.CauldronBehaviorMap DRAGON_BREATH_CAULDRON_BEHAVIOR = CauldronBehavior.createMap("dragon_breath");
    
    public static final IntProperty LEVEL = Properties.LEVEL_3;
    
    public DragonBreathCauldronBlock(Settings settings) {
        super(Biome.Precipitation.NONE, DRAGON_BREATH_CAULDRON_BEHAVIOR, settings);
        this.setDefaultState((this.stateManager.getDefaultState()));
        Item.BLOCK_ITEMS.put(this, Items.CAULDRON);
    }
    
    public static void spawnDragonBreathCloud(World world, BlockPos pos, float radius, int duration) {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(world, pos.toBottomCenterPos().getX(), pos.toBottomCenterPos().getY(), pos.toBottomCenterPos().getZ());
        areaEffectCloudEntity.setParticleType(ParticleTypes.DRAGON_BREATH);
        areaEffectCloudEntity.setRadius(radius);
        areaEffectCloudEntity.setDuration(duration);
        areaEffectCloudEntity.setRadiusGrowth((7.0f - areaEffectCloudEntity.getRadius()) / (float) areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.setPotionDurationScale(0.25F);
        areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1){
            @Override
            public void onEntityDamage(ServerWorld world, LivingEntity livingEntity, DamageSource source, float amount){
                livingEntity.damage(world, world.getDamageSources().dragonBreath(),amount);
            }
        });
        world.syncWorldEvent(WorldEvents.DRAGON_BREATH_CLOUD_SPAWNS, pos, 1);
        world.spawnEntity(areaEffectCloudEntity);
    }
    
    public void spawnDragonBreathCloud(World world, BlockPos pos) {
        spawnDragonBreathCloud(world, pos, this.getStateManager().getDefaultState().get(LEVEL), this.getStateManager().getDefaultState().get(LEVEL) * 200);
    }
    
    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        this.spawnBreakParticles(world, player, pos, state);
        if(!world.isClient() && !player.isCreative()) {
            spawnDragonBreathCloud(world, pos);
        }
        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
        return state;
    }
    
    @Override
    public void onDestroyedByExplosion(ServerWorld world, BlockPos pos, Explosion explosion) {
        if(!world.isClient()) {
            if (explosion.getCausingEntity() instanceof PlayerEntity player && player.isCreative()) {return;}
            spawnDragonBreathCloud(world, pos);
        }
    }
    
    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
        if (world instanceof ServerWorld serverWorld && this.isEntityTouchingFluid(state, pos, entity)) {
            entity.damage(serverWorld, world.getDamageSources().dragonBreath(), 2.5f);
        }
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(LEVEL)*4;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    protected void fillFromDripstone(BlockState state, World world, BlockPos pos, Fluid fluid) {
        if (this.isFull(state)) {
            return;
        }
        BlockState blockState = state.with(LEVEL, state.get(LEVEL) + 1);
        world.setBlockState(pos, blockState);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
        world.syncWorldEvent(WorldEvents.POINTED_DRIPSTONE_DRIPS_WATER_INTO_CAULDRON, pos, 0);
    }

    public static boolean setFluidLevel(BlockState state, World world, BlockPos pos, boolean required, int level) {
        int maxLevel = getMaxFluidLevel(state);
        int actualLevel = Math.max(0, Math.min(level, maxLevel));

        if (maxLevel == -1 || (level != actualLevel && required) || getFluidLevel(state) == actualLevel) return false;

        world.setBlockState(pos, actualLevel == 0 ? Blocks.CAULDRON.getDefaultState() : state.with(LeveledCauldronBlock.LEVEL, actualLevel));
        return true;
    }

    public static void decrementFluidLevel(BlockState state, World world, BlockPos pos, boolean required, int amount) {
        setFluidLevel(state, world, pos, required, getFluidLevel(state) - amount);
    }

    public static boolean incrementFluidLevel(BlockState state, World world, BlockPos pos, boolean required, int amount) {
        return setFluidLevel(state, world, pos, required, getFluidLevel(state) + amount);
    }

    public static void decrementFluidLevel(BlockState state, World world, BlockPos pos) {
        decrementFluidLevel(state, world, pos, true, 1);
    }

    public static boolean incrementFluidLevel(BlockState state, World world, BlockPos pos) {
        return incrementFluidLevel(state, world, pos, true, 1);
    }

    public static int getFluidLevel(BlockState state) {
        if (state.isOf(Blocks.LAVA_CAULDRON)) {
            return 1;
        } else if (state.getBlock() instanceof LeveledCauldronBlock) {
            return state.get(LeveledCauldronBlock.LEVEL);
        } else if (state.isOf(Blocks.CAULDRON)) {
            return 0;
        }
        return -1;
    }

    public static int getMaxFluidLevel(BlockState state) {
        if (state.getBlock() instanceof LeveledCauldronBlock block) {
            return block.MAX_LEVEL;
        } else if (state.isOf(Blocks.LAVA_CAULDRON)) {
            return 1;
        } else if (state.getBlock() instanceof LeveledCauldronBlock) {
            return 3;
        } else if (state.isOf(Blocks.CAULDRON)) {
            return 0;
        }
        return -1;
    }

    public static boolean canSetFluidLevel(BlockState state, int level) {
        int maxLevel = getMaxFluidLevel(state);
        int actualLevel = Math.max(0, Math.min(level, maxLevel));

        return maxLevel != -1 && level == actualLevel && getFluidLevel(state) != actualLevel;
    }

    public static boolean canIncrementFluidLevel(BlockState state, int amount) {
        return canSetFluidLevel(state, getFluidLevel(state) + amount);
    }

    public static boolean canIncrementFluidLevel(BlockState state) {
        return canIncrementFluidLevel(state, 1);
    }

    public static class FillFromEffigyBehavior implements CauldronBehavior {
        @Override
        public ActionResult interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) { 
            if (!world.isClient) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, ItemStack.EMPTY));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.setBlockState(pos, PeacefulModBlocks.DRAGON_BREATH_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3));
                world.playSound(null, pos, PeacefulMod.ITEM_BOTTLE_EMPTY_DRAGONBREATH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }

            return ActionResult.SUCCESS;
        }
    }

    public static class FillFromBottleBehavior implements CauldronBehavior {
        @Override
        public ActionResult interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) { 
            if (!world.isClient) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.setBlockState(pos, PeacefulModBlocks.DRAGON_BREATH_CAULDRON.getDefaultState());
                world.playSound(null, pos, PeacefulMod.ITEM_BOTTLE_EMPTY_DRAGONBREATH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }

            return ActionResult.SUCCESS;
        }
    }

    public static class DecrementFluidLevelBehavior implements CauldronBehavior {
        @Override
        public ActionResult interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
            if (!world.isClient) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.DRAGON_BREATH)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            
            return ActionResult.SUCCESS;
        }
    }

    public static class IncrementFluidLevelBehavior implements CauldronBehavior {
        @Override
        public ActionResult interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
            if (canIncrementFluidLevel(state)) {
                if (!world.isClient) {
                    Item item = stack.getItem();
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(item));
                    incrementFluidLevel(state, world, pos);
                    world.playSound(null, pos, PeacefulMod.ITEM_BOTTLE_EMPTY_DRAGONBREATH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                }

                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
            }
        }
    }
}
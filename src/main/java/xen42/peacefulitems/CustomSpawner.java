package xen42.peacefulitems;

import java.util.ArrayList;
import java.util.function.Predicate;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.impl.biome.modification.BiomeSelectionContextImpl;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.spawner.Spawner;
import net.minecraft.world.Heightmap;


public class CustomSpawner implements Spawner {
	private int cooldown;

    private EntityType<?> type;
    private boolean isHostile;
    private boolean requiresDark;
    private int maxCount = 5;
    private boolean disableDuringDragonFight;
    private Predicate<BiomeSelectionContext> requiredBiome;

    public CustomSpawner(EntityType<?> type) {
        this.type = type;
    }

    public CustomSpawner markIsHostile() {
        this.isHostile = true;
        return this;
    }

    public CustomSpawner markRequiresDark() {
        this.requiresDark = true;
        return this;
    }

    public CustomSpawner setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public CustomSpawner disableDuringDragonFight() {
        this.disableDuringDragonFight = true;
        return this;
    }

    public CustomSpawner setBiome(Predicate<BiomeSelectionContext> biomePredicate) {
        requiredBiome = biomePredicate;
        return this;
    }

	@Override
	public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        if (isHostile && (!spawnMonsters || world.getDifficulty() == Difficulty.PEACEFUL)) {
            return 0;
        }
        else if (!isHostile && !spawnAnimals) {
            return 0;
        }
        else if (!world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
            return 0;
        } else if (disableDuringDragonFight && !world.getAliveEnderDragons().isEmpty()) {
            return 0;
        } else {
			Random random = world.random;
			this.cooldown--;
			if (this.cooldown > 0) {
				return 0;
			} else {
				this.cooldown = 160 + random.nextInt(260);

                int i = world.getPlayers().size();
                if (i < 1) {
                    return 0;
                } else {
                    PlayerEntity playerEntity = (PlayerEntity)world.getPlayers().get(random.nextInt(i));
                    if (playerEntity.isSpectator()) {
                        return 0;
                    } else {
                        int j = (16 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                        int k = (16 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                        BlockPos.Mutable mutable = playerEntity.getBlockPos().mutableCopy().move(j, 0, k);
                        
                        // If we need a particular biome check for it here and if it doesnt match give up
                        if (requiredBiome != null) {
                            var playerBiome = world.getBiome(playerEntity.getBlockPos());
                            var playerBiomeKey = (RegistryKey<Biome>)playerBiome.getKey().orElseThrow();

                            if (!requiredBiome.test(new BiomeSelectionContextImpl(world.getServer().getRegistryManager(), playerBiomeKey, playerBiome.value()))) {
                                PeacefulMod.LOGGER.info("Biome " + playerBiomeKey + " is invalid");
                                return 0;
                            }
                        }

                        var chunkX = mutable.getX() / 16;
                        var chunkZ = mutable.getZ() / 16;

                        var chunk = world.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);

                        var minY = world.getBottomY();
                        var maxY = world.getTopY();

                        Box chunkBox = new Box(
                            chunk.getPos().getStartX(), minY, chunk.getPos().getStartZ(),
                            chunk.getPos().getEndX(), maxY, chunk.getPos().getEndZ()
                        );
                        int mobCount = world.getEntitiesByClass(MobEntity.class, chunkBox, e -> e.getType() == this.type).size();

                        PeacefulMod.LOGGER.info("Trying to spawn " + type.getName());

                        if (mobCount > maxCount) {
                            PeacefulMod.LOGGER.info("Too many already exist");

                            return 0;
                        }

                        int m = 10;
                        if (!world.isRegionLoaded(mutable.getX() - 10, mutable.getZ() - 10, mutable.getX() + 10, mutable.getZ() + 10)) {
                            PeacefulMod.LOGGER.info("Area is not loaded");
                            return 0;
                        }

                        int n = 0;
                        int o = 2 + random.nextBetween(1, 4);

                        for (int p = 0; p < o; p++) {
                            n++;

                            var top = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutable).getY() + 1;
                            var bottom = world.getSeaLevel();

                            var possibleSpawnPoints = new ArrayList<Integer>();
                            for (int i2 = bottom; i2 < top; i2++) {
                                mutable.setY(i2);
                                if (MobEntity.canMobSpawn((EntityType<MobEntity>)type, world, SpawnReason.MOB_SUMMONED, mutable, random) 
                                    && world.getBlockState(mutable).isAir() && world.getBlockState(mutable.down()).isOpaque()) {
                                    if (world.getLightLevel(mutable) > 11 && requiresDark) {
                                        continue;
                                    }
                                    possibleSpawnPoints.add(mutable.getY());
                                }
                            }

                            if (possibleSpawnPoints.isEmpty()) {
                                PeacefulMod.LOGGER.info("Valid spawn point not found");
                                continue;
                            }

                            mutable.setY(possibleSpawnPoints.get(random.nextInt(possibleSpawnPoints.size())));

                            PeacefulMod.LOGGER.info("Spawning mob at " + mutable);

                            spawnMob(world, mutable, random);

                            mutable.setX(mutable.getX() + random.nextInt(5) - random.nextInt(5));
                            mutable.setZ(mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                        }

                        return n;
                    }
                }
			}
		}
	}

	private boolean spawnMob(ServerWorld world, BlockPos pos, Random random) {
		BlockState blockState = world.getBlockState(pos);
		if (!SpawnHelper.isClearForSpawn(world, pos, blockState, blockState.getFluidState(), type)) {
			return false;
		} else {
			var entity = (MobEntity)type.create(world);
			if (entity != null) {
				entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
				entity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.NATURAL, null, null);
				world.spawnEntityAndPassengers(entity);
				return true;
			} else {
				return false;
			}
		}
	}
}


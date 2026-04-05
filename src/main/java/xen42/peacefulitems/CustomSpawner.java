package xen42.peacefulitems;

import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
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

	private int minCooldown = 160;
	private int maxCooldown = 420;

	private int minAttempts = 3;
	private int maxAttempts = 6;

	private int minSpawnDistance = 16;
	private int maxSpawnDistance = 40;

    private EntityType<?> type;
    private boolean isHostile;
    private boolean requiresDark;
    private int maxCount = 5;
	private int regionCheckRadius = 10;
    private boolean disableDuringDragonFight;
    private Predicate<BiomeSelectionContext> requiredBiome;

    public CustomSpawner(EntityType<?> type) {
        this.type = type;
    }

    public CustomSpawner setCooldown(int min, int max) {
        if (min < 0 || max < min) {
            throw new IllegalArgumentException("Invalid cooldown range: " + min + " - " + max);
        }
        this.minCooldown = min;
        this.maxCooldown = max;
        return this;
    }
    
    public CustomSpawner setAttempts(int min, int max) {
        if (min <= 0 || max < min) {
            throw new IllegalArgumentException("Invalid attempt range: " + min + " - " + max);
        }
        this.minAttempts = min;
        this.maxAttempts = max;
        return this;
    }

	public CustomSpawner setSpawnDistance(int min, int max) {
	    if (min < 0 || max < min) {
	        throw new IllegalArgumentException("Invalid spawn distance: " + min + " - " + max);
	    }
	    this.minSpawnDistance = min;
	    this.maxSpawnDistance = max;
	    return this;
	}

	public CustomSpawner setRegionCheckRadius(int radius) {
	    if (radius < 0) {
	        throw new IllegalArgumentException("Radius must be > 0");
	    }
	    this.regionCheckRadius = radius;
	    return this;
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
	    if (maxCount < 0) {
	        throw new IllegalArgumentException("Max count must be > 0");
	    }
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
        if (isHostile && (!spawnMonsters || world.getDifficulty() == Difficulty.PEACEFUL)) return 0;
        if (!isHostile && !spawnAnimals) return 0;
        if (!world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) return 0;
        if (disableDuringDragonFight && !world.getAliveEnderDragons().isEmpty()) return 0;

        Random random = world.random;

        cooldown--;
        if (cooldown > 0) return 0;
        cooldown = random.nextBetween(minCooldown, maxCooldown);

        List<? extends PlayerEntity> players = world.getPlayers();
        if (players.isEmpty()) return 0;

        PlayerEntity player = players.get(random.nextInt(players.size()));
        if (player.isSpectator()) return 0;

        int xOffset = random.nextBetween(minSpawnDistance, maxSpawnDistance) * (random.nextBoolean() ? -1 : 1);
        int zOffset = random.nextBetween(minSpawnDistance, maxSpawnDistance) * (random.nextBoolean() ? -1 : 1);

        BlockPos.Mutable basePos = player.getBlockPos().mutableCopy().move(xOffset, 0, zOffset);
        
        Identifier entityId = EntityType.getId(type);
        PeacefulMod.LOGGER.debug("[CustomSpawner] Attempting to spawn {} at {}", entityId, basePos.toShortString());

        int chunkX = MathHelper.floorDiv(basePos.getX(), 16);
        int chunkZ = MathHelper.floorDiv(basePos.getZ(), 16);
        var chunk = world.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
        if (chunk == null) {
            PeacefulMod.LOGGER.debug("[CustomSpawner] Target chunk is not loaded");
            return 0;
        }

        if (!world.isRegionLoaded(basePos.getX() - regionCheckRadius, basePos.getZ() - regionCheckRadius, basePos.getX() + regionCheckRadius, basePos.getZ() + regionCheckRadius)) {
            PeacefulMod.LOGGER.debug("[CustomSpawner] Area is not loaded");
            return 0;
        }

        var minY = world.getBottomY();
        var maxY = world.getTopY();

        Box chunkBox = new Box(
            chunk.getPos().getStartX(), minY, chunk.getPos().getStartZ(),
            chunk.getPos().getEndX(), maxY, chunk.getPos().getEndZ()
        );

        int mobCount = world.getEntitiesByClass(MobEntity.class, chunkBox, e -> e.getType() == this.type).size();
        if (mobCount >= maxCount) {
            PeacefulMod.LOGGER.debug("[CustomSpawner] Too many already exist in chunk: {}/{}", mobCount, maxCount);
            return 0;
        }

        int spawned = 0;
        int attempts = random.nextBetween(minAttempts, maxAttempts);

        for (int i = 0; i < attempts; i++) {
            BlockPos.Mutable spawnPos = findSpawnPos(world, basePos, random);
            if (spawnPos == null) {
                PeacefulMod.LOGGER.debug("[CustomSpawner] Valid spawn point not found at {}", basePos.toShortString());
                offsetRandomly(basePos, random);
                continue;
            }

            PeacefulMod.LOGGER.debug("[CustomSpawner] Spawning {} at {}", entityId, spawnPos.toShortString());

            if (spawnMob(world, spawnPos, random)) {
                spawned++;
            }

            offsetRandomly(basePos, random);
        }

        return spawned;
	}
	
	private static void offsetRandomly(BlockPos.Mutable pos, Random random) {
	    pos.setX(pos.getX() + random.nextInt(5) - random.nextInt(5));
	    pos.setZ(pos.getZ() + random.nextInt(5) - random.nextInt(5));
	}

    private BlockPos.Mutable findSpawnPos(ServerWorld world, BlockPos center, Random random) {
        int minY = world.getBottomY();
        int maxY = world.getTopY();
        
        BlockPos.Mutable mutable = center.mutableCopy();
        mutable.setY(minY);

        List<BlockPos> validPositions = new ArrayList<>();

        for (int y = minY; y < maxY; y++) {
            mutable.setY(y);

            if (!isValidSpawnPosition(world, mutable, random)) continue;

            validPositions.add(mutable.toImmutable());
        }

        if (validPositions.isEmpty()) {
            return null;
        }

        return validPositions.get(random.nextInt(validPositions.size())).mutableCopy();
    }

    @SuppressWarnings("unchecked")
    private boolean isValidSpawnPosition(ServerWorld world, BlockPos pos, Random random) {
        if (!MobEntity.canMobSpawn((EntityType<MobEntity>) type, world, SpawnReason.NATURAL, pos, random)) {
            return false;
        }
        
        BlockState stateAtPos = world.getBlockState(pos);
        BlockState stateBelow = world.getBlockState(pos.down());

        if (!stateAtPos.isAir()) return false;
        if (!stateBelow.isOpaque()) return false;

        if (requiresDark && world.getLightLevel(pos) > 11) return false;

        if (!isValidBiome(world, pos)) return false;

        return SpawnHelper.isClearForSpawn(world, pos, stateAtPos, stateAtPos.getFluidState(), type);
    }
    
    private boolean isValidBiome(ServerWorld world, BlockPos pos) {
        if (requiredBiome != null) {
            var biomeEntry = world.getBiome(pos);
            RegistryKey<Biome> biomeKey = biomeEntry.getKey().orElseThrow();
            boolean validBiome = requiredBiome.test(
                new BiomeSelectionContextImpl(
                    world.getServer().getRegistryManager(),
                    biomeKey,
                    biomeEntry.value()
                )
            );

            if (!validBiome) {
            	PeacefulMod.LOGGER.debug("[CustomSpawner] Biome {} is invalid at {}", biomeKey.getValue(), pos.toShortString());
            	return false;
            }
        }
        
        return true;
    }

    private boolean spawnMob(ServerWorld world, BlockPos pos, Random random) {
        var entity = (MobEntity) type.create(world);
        if (entity == null) {
            return false;
        }

        entity.refreshPositionAndAngles(
            pos.getX() + 0.5,
            pos.getY(),
            pos.getZ() + 0.5,
            random.nextFloat() * 360.0F,
            0.0F
        );
        entity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.NATURAL, null, null);

		world.spawnEntityAndPassengers(entity);
		return true;
    }
}


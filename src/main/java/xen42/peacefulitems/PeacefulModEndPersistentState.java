package xen42.peacefulitems;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.PersistentState;

public class PeacefulModEndPersistentState extends PersistentState {
	public static PeacefulModEndPersistentState INSTANCE;
	public static final String ID = PeacefulMod.MOD_ID + "-end";

	private boolean isDirty = false;
	private boolean hasSpawnedEgg = false;

	private static PeacefulModEndPersistentState readNbt(NbtCompound nbt) {
		PeacefulModEndPersistentState data = new PeacefulModEndPersistentState();
		data.hasSpawnedEgg = nbt.getBoolean("hasSpawnedEgg");
		return data;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putBoolean("hasSpawnedEgg", hasSpawnedEgg);
		return nbt;
	}

	public boolean getHasSpawnedEgg() {
		return hasSpawnedEgg;
	}

	public void setHasSpawnedEgg(boolean value) {
		this.hasSpawnedEgg = value;
		this.markDirty(); // ensures the data will be saved
	}

	public static PeacefulModEndPersistentState get(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(PeacefulModEndPersistentState::readNbt, PeacefulModEndPersistentState::new, ID);
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	public void markDirty() {
		isDirty = true;
	}
}
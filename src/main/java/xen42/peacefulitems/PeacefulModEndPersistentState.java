package xen42.peacefulitems;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

public class PeacefulModEndPersistentState extends PersistentState {
	public static PeacefulModEndPersistentState INSTANCE;
	public static final String ID = PeacefulMod.MOD_ID + "-end";
	public static final Codec<PeacefulModEndPersistentState> CODEC = Codecs.fromOps(NbtOps.INSTANCE)
		.xmap(nbt -> readNbt((NbtCompound)nbt), PeacefulModEndPersistentState::writeNbt);

	private boolean isDirty = false;
	private boolean hasSpawnedEgg = false;

	private static PeacefulModEndPersistentState readNbt(NbtCompound nbt) {
		PeacefulModEndPersistentState data = new PeacefulModEndPersistentState();
		data.hasSpawnedEgg = nbt.getBoolean("hasSpawnedEgg", false);
		return data;
	}

	private NbtCompound writeNbt() {
		NbtCompound nbt = new NbtCompound();
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

	public static PersistentStateType<PeacefulModEndPersistentState> getType() {
		return new PersistentStateType<PeacefulModEndPersistentState>(
			ID,
			PeacefulModEndPersistentState::new,
			CODEC,
			null
		);
	}

	public static PeacefulModEndPersistentState get(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(PeacefulModEndPersistentState.getType());
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	public void markDirty() {
		isDirty = true;
	}
}
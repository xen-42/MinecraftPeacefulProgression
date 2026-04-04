package xen42.peacefulitems.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.TypedEntityData;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.world.event.GameEvent;
import xen42.peacefulitems.PeacefulMod;

public class DispensibleSpawnEggItem extends SpawnEggItem {
    protected static DispenserBehavior createDispenseItemBehavior() {
        return new ItemDispenserBehavior() {
            @Override
            public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
                Direction direction = source.state().get(DispenserBlock.FACING);
                EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getEntityType(stack);
                
                try {
                    entityType.spawnFromItemStack(source.world(), stack, null, source.pos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
                } catch (Exception ex) {
                    LOGGER.error("Error while dispensing spawn egg from dispenser at {}", source.pos(), ex);
                    return ItemStack.EMPTY;
                }
                
                stack.decrement(1);
                source.world().emitGameEvent(null, GameEvent.ENTITY_PLACE, source.pos());
                return stack;
            }
        };
    }
    
    public DispensibleSpawnEggItem(Settings properties) {
        this(properties, createDispenseItemBehavior());
    }
    
    public DispensibleSpawnEggItem(Settings properties,
                                    @Nullable DispenserBehavior dispenseItemBehavior) {
        super(properties);
        TypedEntityData<EntityType<?>> entityType = this.getComponents().get(DataComponentTypes.ENTITY_DATA);
        SpawnEggItem.SPAWN_EGGS.remove(entityType);
        if (dispenseItemBehavior != null) {
            DispenserBlock.registerBehavior(this, dispenseItemBehavior);
        }
    }
}

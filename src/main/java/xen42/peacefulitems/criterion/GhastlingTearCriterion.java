package xen42.peacefulitems.criterion;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.entities.GhastlingEntity;

public class GhastlingTearCriterion extends AbstractCriterion<GhastlingTearCriterion.Conditions> {
    @Override
    public Codec<GhastlingTearCriterion.Conditions> getConditionsCodec() {
        return GhastlingTearCriterion.Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, GhastlingEntity entity) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
        this.trigger(player, conditions -> conditions.test(lootContext));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<LootContextPredicate> entity) implements AbstractCriterion.Conditions {
        public static final Codec<GhastlingTearCriterion.Conditions> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(GhastlingTearCriterion.Conditions::player),
                    EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("entity").forGetter(GhastlingTearCriterion.Conditions::entity)
                )
                .apply(instance, GhastlingTearCriterion.Conditions::new)
        );

        public static AdvancementCriterion<GhastlingTearCriterion.Conditions> create(Optional<LootContextPredicate> entity) {
            return PeacefulMod.GHASTLING_TEAR_CRITERIA.create(new GhastlingTearCriterion.Conditions(Optional.empty(), entity));
        }

        public boolean test(LootContext entity) {
            return !this.entity.isPresent() || ((LootContextPredicate)this.entity.get()).test(entity);
        }

        @Override
        public void validate(LootContextPredicateValidator validator) {
            AbstractCriterion.Conditions.super.validate(validator);
            validator.validateEntityPredicate(this.entity, ".entity");
        }
    }
}

package xen42.peacefulitems.criterion;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.entities.GhastlingEntity;

public class GhastlingTearCriterion extends AbstractCriterion<GhastlingTearCriterion.Conditions> {
	@Override
	public GhastlingTearCriterion.Conditions conditionsFromJson(
		JsonObject object, Optional<LootContextPredicate> player, AdvancementEntityPredicateDeserializer deserializer
	) {
		Optional<LootContextPredicate> entity = EntityPredicate.contextPredicateFromJson(object, "entity", deserializer);
		return new GhastlingTearCriterion.Conditions(player, entity);
	}

    public void trigger(ServerPlayerEntity player, GhastlingEntity entity) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
        this.trigger(player, conditions -> conditions.test(lootContext));
    }

	public static class Conditions extends AbstractCriterionConditions {
		private final Optional<LootContextPredicate> entity;

		public Conditions(
			Optional<LootContextPredicate> playerPredicate,
			Optional<LootContextPredicate> entityPredicate
		) {
			super(playerPredicate);
			this.entity = entityPredicate;
		}

		public static AdvancementCriterion<GhastlingTearCriterion.Conditions> any() {
            return PeacefulMod.GHASTLING_TEAR_CRITERIA.create(new GhastlingTearCriterion.Conditions(Optional.empty(), Optional.empty()));
		}

        public static AdvancementCriterion<GhastlingTearCriterion.Conditions> create(Optional<LootContextPredicate> entity) {
            return PeacefulMod.GHASTLING_TEAR_CRITERIA.create(new GhastlingTearCriterion.Conditions(Optional.empty(), entity));
        }

        public boolean test(LootContext entity) {
            return !this.entity.isPresent() || ((LootContextPredicate)this.entity.get()).test(entity);
        }

		@Override
		public JsonObject toJson() {
			JsonObject jsonObject = super.toJson();
			this.entity.ifPresent(lootContextPredicate -> jsonObject.add("entity", lootContextPredicate.toJson()));
			return jsonObject;
		}
    }
}

package xen42.peacefulitems.criterion;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.entities.GhastlingEntity;

public class GhastlingTearCriterion extends AbstractCriterion<GhastlingTearCriterion.Conditions> {
	public static final Identifier ID = Identifier.of(PeacefulMod.MOD_ID, "ghastling_tear");

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public GhastlingTearCriterion.Conditions conditionsFromJson(
		JsonObject object, LootContextPredicate player, AdvancementEntityPredicateDeserializer deserializer
	) {
		LootContextPredicate entity = EntityPredicate.contextPredicateFromJson(object, "entity", deserializer);
		return new GhastlingTearCriterion.Conditions(player, entity);
	}

    public void trigger(ServerPlayerEntity player, GhastlingEntity entity) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
        this.trigger(player, conditions -> conditions.test(lootContext));
    }

	public static class Conditions extends AbstractCriterionConditions {
		private final LootContextPredicate entity;

		public Conditions(
			LootContextPredicate playerPredicate,
			LootContextPredicate entityPredicate
		) {
			super(ID, playerPredicate);
			this.entity = entityPredicate;
		}

		public static GhastlingTearCriterion.Conditions any() {
			return new GhastlingTearCriterion.Conditions(LootContextPredicate.EMPTY, LootContextPredicate.EMPTY);
		}

		public static GhastlingTearCriterion.Conditions create(EntityPredicate.Builder entity) {
			return new GhastlingTearCriterion.Conditions(
				LootContextPredicate.EMPTY, EntityPredicate.asLootContextPredicate(entity.build())
			);
		}

        public boolean test(LootContext entity) {
            return this.entity == LootContextPredicate.EMPTY || this.entity.test(entity);
        }

		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("entity", this.entity.toJson(predicateSerializer));
			return jsonObject;
		}
    }
}

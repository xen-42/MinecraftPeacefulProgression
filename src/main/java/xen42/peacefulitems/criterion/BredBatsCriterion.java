package xen42.peacefulitems.criterion;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.BredAnimalsCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;

import org.jetbrains.annotations.Nullable;

public class BredBatsCriterion extends AbstractCriterion<BredBatsCriterion.Conditions> {
	public static final Identifier ID = Identifier.of(PeacefulMod.MOD_ID, "bred_bats");

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public BredBatsCriterion.Conditions conditionsFromJson(
		JsonObject object, LootContextPredicate player, AdvancementEntityPredicateDeserializer deserializer
	) {
		LootContextPredicate parent = EntityPredicate.contextPredicateFromJson(object, "parent", deserializer);
		LootContextPredicate partner = EntityPredicate.contextPredicateFromJson(object, "partner", deserializer);
		LootContextPredicate child = EntityPredicate.contextPredicateFromJson(object, "child", deserializer);
		return new BredBatsCriterion.Conditions(player, parent, partner, child);
	}

	public void trigger(ServerPlayerEntity player, BatEntity parent, BatEntity partner, @Nullable BatEntity child) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, parent);
		LootContext lootContext2 = EntityPredicate.createAdvancementEntityLootContext(player, partner);
		LootContext lootContext3 = child != null ? EntityPredicate.createAdvancementEntityLootContext(player, child) : null;
		this.trigger(player, conditions -> conditions.matches(lootContext, lootContext2, lootContext3));
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final LootContextPredicate parent;
		private final LootContextPredicate partner;
		private final LootContextPredicate child;

		public Conditions(
			LootContextPredicate playerPredicate,
			LootContextPredicate parentPredicate,
			LootContextPredicate partnerPredicate,
			LootContextPredicate childPredicate
		) {
			super(ID, playerPredicate);
			this.parent = parentPredicate;
			this.partner = partnerPredicate;
			this.child = childPredicate;
		}

		public static BredBatsCriterion.Conditions any() {
			return new BredBatsCriterion.Conditions(LootContextPredicate.EMPTY, LootContextPredicate.EMPTY, LootContextPredicate.EMPTY, LootContextPredicate.EMPTY);
		}

		public static BredBatsCriterion.Conditions create(EntityPredicate.Builder child) {
			return new BredBatsCriterion.Conditions(
				LootContextPredicate.EMPTY, LootContextPredicate.EMPTY, LootContextPredicate.EMPTY, EntityPredicate.asLootContextPredicate(child.build())
			);
		}

		public static BredBatsCriterion.Conditions create(EntityPredicate parent, EntityPredicate partner, EntityPredicate child) {
			return new BredBatsCriterion.Conditions(
				LootContextPredicate.EMPTY,
				EntityPredicate.asLootContextPredicate(parent),
				EntityPredicate.asLootContextPredicate(partner),
				EntityPredicate.asLootContextPredicate(child)
			);
		}

		public boolean matches(LootContext parentContext, LootContext partnerContext, @Nullable LootContext childContext) {
			return this.child == LootContextPredicate.EMPTY || childContext != null && this.child.test(childContext)
				? this.parent.test(parentContext) && this.partner.test(partnerContext) || this.parent.test(partnerContext) && this.partner.test(parentContext)
				: false;
		}

		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("parent", this.parent.toJson(predicateSerializer));
			jsonObject.add("partner", this.partner.toJson(predicateSerializer));
			jsonObject.add("child", this.child.toJson(predicateSerializer));
			return jsonObject;
		}
	}
}

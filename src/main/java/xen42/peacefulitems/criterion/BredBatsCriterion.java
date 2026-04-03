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
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import xen42.peacefulitems.PeacefulMod;

import org.jetbrains.annotations.Nullable;

public class BredBatsCriterion extends AbstractCriterion<BredBatsCriterion.Conditions> {
	@Override
	public BredBatsCriterion.Conditions conditionsFromJson(
		JsonObject object, Optional<LootContextPredicate> player, AdvancementEntityPredicateDeserializer deserializer
	) {
		Optional<LootContextPredicate> parent = EntityPredicate.contextPredicateFromJson(object, "parent", deserializer);
		Optional<LootContextPredicate> partner = EntityPredicate.contextPredicateFromJson(object, "partner", deserializer);
		Optional<LootContextPredicate> child = EntityPredicate.contextPredicateFromJson(object, "child", deserializer);
		return new BredBatsCriterion.Conditions(player, parent, partner, child);
	}

	public void trigger(ServerPlayerEntity player, BatEntity parent, BatEntity partner, @Nullable BatEntity child) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, parent);
		LootContext lootContext2 = EntityPredicate.createAdvancementEntityLootContext(player, partner);
		LootContext lootContext3 = child != null ? EntityPredicate.createAdvancementEntityLootContext(player, child) : null;
		this.trigger(player, conditions -> conditions.matches(lootContext, lootContext2, lootContext3));
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final Optional<LootContextPredicate> parent;
		private final Optional<LootContextPredicate> partner;
		private final Optional<LootContextPredicate> child;

		public Conditions(
			Optional<LootContextPredicate> playerPredicate,
			Optional<LootContextPredicate> parentPredicate,
			Optional<LootContextPredicate> partnerPredicate,
			Optional<LootContextPredicate> childPredicate
		) {
			super(playerPredicate);
			this.parent = parentPredicate;
			this.partner = partnerPredicate;
			this.child = childPredicate;
		}

		public static AdvancementCriterion<BredBatsCriterion.Conditions> any() {
			return PeacefulMod.BRED_BATS_CRITERIA
				.create(
					new BredBatsCriterion.Conditions(
						Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()
					)
				);
		}

		public static AdvancementCriterion<BredBatsCriterion.Conditions> create(EntityPredicate.Builder child) {
			return PeacefulMod.BRED_BATS_CRITERIA
				.create(
					new BredBatsCriterion.Conditions(
						Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.contextPredicateFromEntityPredicate(child))
					)
				);
		}

		public static AdvancementCriterion<BredBatsCriterion.Conditions> create(
			Optional<EntityPredicate> parent, Optional<EntityPredicate> partner, Optional<EntityPredicate> child
		) {
			return PeacefulMod.BRED_BATS_CRITERIA
				.create(
					new BredBatsCriterion.Conditions(
						Optional.empty(),
						EntityPredicate.contextPredicateFromEntityPredicate(parent),
						EntityPredicate.contextPredicateFromEntityPredicate(partner),
						EntityPredicate.contextPredicateFromEntityPredicate(child)
					)
				);
		}

		public boolean matches(LootContext parentContext, LootContext partnerContext, @Nullable LootContext childContext) {
			return !this.child.isPresent() || childContext != null && ((LootContextPredicate)this.child.get()).test(childContext)
				? parentMatches(this.parent, parentContext) && parentMatches(this.partner, partnerContext)
					|| parentMatches(this.parent, partnerContext) && parentMatches(this.partner, parentContext)
				: false;
		}

		private static boolean parentMatches(Optional<LootContextPredicate> parent, LootContext parentContext) {
			return parent.isEmpty() || ((LootContextPredicate)parent.get()).test(parentContext);
		}

		@Override
		public JsonObject toJson() {
			JsonObject jsonObject = super.toJson();
			this.parent.ifPresent(lootContextPredicate -> jsonObject.add("parent", lootContextPredicate.toJson()));
			this.partner.ifPresent(lootContextPredicate -> jsonObject.add("partner", lootContextPredicate.toJson()));
			this.child.ifPresent(lootContextPredicate -> jsonObject.add("child", lootContextPredicate.toJson()));
			return jsonObject;
		}
	}
}

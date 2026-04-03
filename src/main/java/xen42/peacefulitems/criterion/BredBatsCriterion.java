package xen42.peacefulitems.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;
import xen42.peacefulitems.PeacefulMod;

import org.jetbrains.annotations.Nullable;

public class BredBatsCriterion extends AbstractCriterion<BredBatsCriterion.Conditions> {
	@Override
	public Codec<BredBatsCriterion.Conditions> getConditionsCodec() {
		return BredBatsCriterion.Conditions.CODEC;
	}

	public void trigger(ServerPlayerEntity player, BatEntity parent, BatEntity partner, @Nullable BatEntity child) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, parent);
		LootContext lootContext2 = EntityPredicate.createAdvancementEntityLootContext(player, partner);
		LootContext lootContext3 = child != null ? EntityPredicate.createAdvancementEntityLootContext(player, child) : null;
		this.trigger(player, conditions -> conditions.matches(lootContext, lootContext2, lootContext3));
	}

	public record Conditions(
		Optional<LootContextPredicate> player, Optional<LootContextPredicate> parent, Optional<LootContextPredicate> partner, Optional<LootContextPredicate> child
	) implements AbstractCriterion.Conditions {
		public static final Codec<BredBatsCriterion.Conditions> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(BredBatsCriterion.Conditions::player),
					EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("parent").forGetter(BredBatsCriterion.Conditions::parent),
					EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("partner").forGetter(BredBatsCriterion.Conditions::partner),
					EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("child").forGetter(BredBatsCriterion.Conditions::child)
				)
				.apply(instance, BredBatsCriterion.Conditions::new)
		);

		public static AdvancementCriterion<BredBatsCriterion.Conditions> any() {
			return PeacefulMod.BRED_BATS_CRITERIA.create(new BredBatsCriterion.Conditions(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
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
		public void validate(LootContextPredicateValidator validator) {
			AbstractCriterion.Conditions.super.validate(validator);
			validator.validateEntityPredicate(this.parent, ".parent");
			validator.validateEntityPredicate(this.partner, ".partner");
			validator.validateEntityPredicate(this.child, ".child");
		}
	}
}

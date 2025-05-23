package xen42.peacefulitems.entities;

import java.util.EnumSet;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import xen42.peacefulitems.PeacefulModTags;

import org.jetbrains.annotations.Nullable;

public class GhastlingFleeGoal extends Goal {
   protected final GhastlingEntity mob;
   private final double fastSpeed;
   protected final float fleeDistance;
   @Nullable
   protected Path fleePath;
   protected final BirdNavigation fleeingEntityNavigation;
   private PlayerEntity target;

    protected double targetX;
    protected double targetY;
    protected double targetZ;

    protected boolean active;

   public GhastlingFleeGoal(GhastlingEntity mob, float distance, double fastSpeed) {
      this.mob = mob;
      this.fleeDistance = distance;
      this.fastSpeed = fastSpeed;
      this.fleeingEntityNavigation = (BirdNavigation)mob.getNavigation();
      this.setControls(EnumSet.of(Control.MOVE));
   }

   public boolean canStart() {
      if (!this.isInDanger()) {
         return false;
      } else {
         return this.findTarget();
      }
   }

   protected boolean isInDanger() {
    TargetPredicate predicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(100f)
        .setPredicate((entity, world) -> ((PlayerEntity)entity).getMainHandStack().isIn(PeacefulModTags.ItemTags.WISP_DISLIKES));
      this.target = getServerWorld(this.mob).getClosestPlayer(predicate, this.mob);
      return this.target != null;
    }

   protected boolean findTarget() {
      Vec3d vec3d = this.mob.getRotationVec(0.0F);
      Vec3d vec3d2 = AboveGroundTargeting.find(this.mob, 8, 7, vec3d.x, vec3d.z, 1.5707964F, 3, 1);
      vec3d = vec3d2 != null ? vec3d2 : NoPenaltySolidTargeting.find(this.mob, 8, 4, -2, vec3d.x, vec3d.z, 1.5707963705062866);

      if (vec3d == null) {
         return false;
      } else {
         this.targetX = vec3d.x;
         this.targetY = vec3d.y;
         this.targetZ = vec3d.z;

         return true;
      }
   }

   public boolean isActive() {
      return this.active;
   }

   public void start() {
      this.mob.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, this.fastSpeed);
      this.active = true;
      this.mob.playHurtSound(null);
   }

   public void stop() {
      this.active = false;
   }

   public boolean shouldContinue() {
      return !this.mob.getNavigation().isIdle();
   }
}

package xen42.peacefulitems.entities;

import net.minecraft.entity.passive.BatEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import xen42.peacefulitems.PeacefulMod;

public class BatHelper {
	public static void FlyTowards(BatEntity bat, Vec3d target) {
		// Fly towards the player using similar logic as going to roost
		double d = (double)target.getX() - bat.getX();
		double e = (double)target.getY() - bat.getY();
		double f = (double)target.getZ() - bat.getZ();
		Vec3d lv3 = bat.getVelocity();
		Vec3d lv4 = lv3.add((Math.signum(d) * 0.5 - lv3.x) * (double)0.1f, (Math.signum(e) * (double)0.7f - lv3.y) * (double)0.1f, (Math.signum(f) * 0.5 - lv3.z) * (double)0.1f);
		bat.setVelocity(lv4);
		float g = (float)(MathHelper.atan2(lv4.z, lv4.x) * 57.2957763671875) - 90.0f;
		float h = MathHelper.wrapDegrees(g - bat.getYaw());
		bat.forwardSpeed = 0.5f;
		bat.setYaw(bat.getYaw() + h);
	}
}

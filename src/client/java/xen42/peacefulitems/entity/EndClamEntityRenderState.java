package xen42.peacefulitems.entity;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.AnimationState;

public class EndClamEntityRenderState extends LivingEntityRenderState {
    public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState hitAnimationState = new AnimationState();
	public final AnimationState yawnAnimationState = new AnimationState();
}

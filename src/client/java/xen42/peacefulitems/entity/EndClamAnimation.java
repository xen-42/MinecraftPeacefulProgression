package xen42.peacefulitems.entity;

import net.minecraft.client.render.entity.animation.*;

public class EndClamAnimation {
    public static final AnimationDefinition YAWN = AnimationDefinition.Builder.create(5.0F)
        .addBoneAnimation("clam", new Transformation(Transformation.Targets.ROTATE, 
            new Keyframe(0.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.875F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(3.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("top_shell", new Transformation(Transformation.Targets.ROTATE, 
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(2.0F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(5.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .build();
    
    public static final AnimationDefinition OPEN = AnimationDefinition.Builder.create(2f)
        .addBoneAnimation("top_shell", new Transformation(Transformation.Targets.ROTATE, 
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .build();

    public static final AnimationDefinition IDLE = AnimationDefinition.Builder.create(8.0F).looping()
        .addBoneAnimation("clam", new Transformation(Transformation.Targets.ROTATE, 
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(6.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(8.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("top_shell", new Transformation(Transformation.Targets.ROTATE, 
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(5.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(6.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(7.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(8.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .build();

    public static final AnimationDefinition HIT = AnimationDefinition.Builder.create(0.5833F)
        .addBoneAnimation("clam", new Transformation(Transformation.Targets.MOVE_ORIGIN, 
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 2.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("bottom_shell", new Transformation(Transformation.Targets.ROTATE, 
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.4583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("top_shell", new Transformation(Transformation.Targets.ROTATE, 
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(-42.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.4583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .build();
}

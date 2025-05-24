package xen42.peacefulitems.entity;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModClient;
import xen42.peacefulitems.entities.EndClamEntity;

public class EndClamEntityRenderer extends MobEntityRenderer<EndClamEntity, EndClamEntityModel> {

    public EndClamEntityRenderer(Context context) {
        super(context, new EndClamEntityModel(context.getPart(PeacefulModClient.MODEL_END_CLAM_LAYER)), 0.2f);
        this.addFeature(new EndClamHeldItemFeatureRenderer(this, context.getItemRenderer()));
    }

    @Override
    public Identifier getTexture(EndClamEntity state) {
        return Identifier.of(PeacefulMod.MOD_ID, "textures/entity/end_clam/end_clam.png");
    }
}

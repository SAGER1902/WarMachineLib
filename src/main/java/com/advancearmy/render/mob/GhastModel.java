package advancearmy.render.mob;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.ERO_Ghast;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
@OnlyIn(Dist.CLIENT)
public class GhastModel<T extends ERO_Ghast>
extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart[] tentacles = new ModelPart[9];

    public GhastModel(ModelPart root) {
        this.root = root;
        for (int $$1 = 0; $$1 < this.tentacles.length; ++$$1) {
            this.tentacles[$$1] = root.getChild(GhastModel.createTentacleName($$1));
        }
    }

    private static String createTentacleName(int index) {
        return "tentacle" + index;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, -8.0f, -8.0f, 16.0f, 16.0f, 16.0f), PartPose.offset(0.0f, 17.6f, 0.0f));
        RandomSource $$2 = RandomSource.create(1660L);
        for (int $$3 = 0; $$3 < 9; ++$$3) {
            float $$4 = (((float)($$3 % 3) - (float)($$3 / 3 % 2) * 0.5f + 0.25f) / 2.0f * 2.0f - 1.0f) * 5.0f;
            float $$5 = ((float)($$3 / 3) / 2.0f * 2.0f - 1.0f) * 5.0f;
            int $$6 = $$2.nextInt(7) + 8;
            $$1.addOrReplaceChild(GhastModel.createTentacleName($$3), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0f, 0.0f, -1.0f, 2.0f, $$6, 2.0f), PartPose.offset($$4, 24.6f, $$5));
        }
        return LayerDefinition.create($$0, 64, 32);
    }

    /**
     * Sets this entity's model rotation angles
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        for (int $$6 = 0; $$6 < this.tentacles.length; ++$$6) {
            this.tentacles[$$6].xRot = 0.2f * Mth.sin(ageInTicks * 0.3f + (float)$$6) + 0.4f;
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
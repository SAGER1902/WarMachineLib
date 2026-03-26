package advancearmy.render.mob;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
@OnlyIn(Dist.CLIENT)
public class CreeperModel<T extends Entity>extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private static final int Y_OFFSET = 6;

    public CreeperModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.leftHindLeg = root.getChild("right_hind_leg");
        this.rightHindLeg = root.getChild("left_hind_leg");
        this.leftFrontLeg = root.getChild("right_front_leg");
        this.rightFrontLeg = root.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation) {
        MeshDefinition $$1 = new MeshDefinition();
        PartDefinition $$2 = $$1.getRoot();
        $$2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, cubeDeformation), PartPose.offset(0.0f, 6.0f, 0.0f));
        $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, cubeDeformation), PartPose.offset(0.0f, 6.0f, 0.0f));
        CubeListBuilder $$3 = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 6.0f, 4.0f, cubeDeformation);
        $$2.addOrReplaceChild("right_hind_leg", $$3, PartPose.offset(-2.0f, 18.0f, 4.0f));
        $$2.addOrReplaceChild("left_hind_leg", $$3, PartPose.offset(2.0f, 18.0f, 4.0f));
        $$2.addOrReplaceChild("right_front_leg", $$3, PartPose.offset(-2.0f, 18.0f, -4.0f));
        $$2.addOrReplaceChild("left_front_leg", $$3, PartPose.offset(2.0f, 18.0f, -4.0f));
        return LayerDefinition.create($$1, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Sets this entity's model rotation angles
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180);
        this.head.xRot = headPitch * ((float)Math.PI / 180);
        this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662f + (float)Math.PI) * 1.4f * limbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662f + (float)Math.PI) * 1.4f * limbSwingAmount;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
    }
}
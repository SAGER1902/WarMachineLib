package advancearmy.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//import advancearmy.entity.mob.ERO_Zombie;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
//import com.mrcrayfish.guns.item.GunItem;
//import advancearmy.entity.mob.ERO_Skeleton;
@OnlyIn(Dist.CLIENT)
public class SkeletonModel<T extends Mob> extends HumanoidModel<T> {
	public SkeletonModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0f);
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f), PartPose.offset(-5.0f, 2.0f, 0.0f));
        $$1.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f), PartPose.offset(5.0f, 2.0f, 0.0f));
        $$1.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f), PartPose.offset(-2.0f, 12.0f, 0.0f));
        $$1.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f), PartPose.offset(2.0f, 12.0f, 0.0f));
        return LayerDefinition.create($$0, 64, 32);
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        ItemStack $$4 = ((LivingEntity)entity).getItemInHand(InteractionHand.MAIN_HAND);
        if ($$4.is(Items.BOW) && ((Mob)entity).isAggressive()) {
            if (((Mob)entity).getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        }
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
    }

    /**
     * Sets this entity's model rotation angles
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ItemStack $$6 = ((LivingEntity)entity).getMainHandItem();
        if (((Mob)entity).isAggressive() && ($$6.isEmpty() || !$$6.is(Items.BOW))) {
            float $$7 = Mth.sin(this.attackTime * (float)Math.PI);
            float $$8 = Mth.sin((1.0f - (1.0f - this.attackTime) * (1.0f - this.attackTime)) * (float)Math.PI);
            this.rightArm.zRot = 0.0f;
            this.leftArm.zRot = 0.0f;
            this.rightArm.yRot = -(0.1f - $$7 * 0.6f);
            this.leftArm.yRot = 0.1f - $$7 * 0.6f;
            this.rightArm.xRot = -1.5707964f;
            this.leftArm.xRot = -1.5707964f;
            this.rightArm.xRot -= $$7 * 1.2f - $$8 * 0.4f;
            this.leftArm.xRot -= $$7 * 1.2f - $$8 * 0.4f;
            AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
        }
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack poseStack) {
        float $$2 = side == HumanoidArm.RIGHT ? 1.0f : -1.0f;
        ModelPart $$3 = this.getArm(side);
        $$3.x += $$2;
        $$3.translateAndRotate(poseStack);
        $$3.x -= $$2;
    }
}
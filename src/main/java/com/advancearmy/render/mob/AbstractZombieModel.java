package advancearmy.render.mob;


//import net.minecraft.world.entity.monster.MonsterEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//import advancearmy.entity.mob.ERO_Zombie;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.PathfinderMob;
@OnlyIn(Dist.CLIENT)
public abstract class AbstractZombieModel<T extends PathfinderMob> extends HumanoidModel<T> {
    protected AbstractZombieModel(ModelPart root) {
        super(root);
    }

    /**
     * Sets this entity's model rotation angles
     */
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(entity), this.attackTime, ageInTicks);
    }
    public abstract boolean isAggressive(T entity);
}
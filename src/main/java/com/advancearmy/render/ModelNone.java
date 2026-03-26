package advancearmy.render;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.Entity;
@OnlyIn(Dist.CLIENT)
public class ModelNone<T extends Entity> extends AgeableListModel<T> {
    //private final ModelPart bone;
    public ModelNone() {
        //this.bone = new ModelPart(this);
        //this.bone.setTexSize(64, 64); // 设置纹理尺寸（根据实际纹理调整）
    }
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
    }
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // 空实现，无动画逻辑
    }
    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of();
    }
}
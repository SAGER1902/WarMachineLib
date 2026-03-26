package wmlib.loader.api.model;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ConcurrentModificationException;
import net.minecraft.client.renderer.RenderType;
import java.util.List;

public abstract class AbstractObjModel {
    public abstract List<ObjModelRenderer> getParts();
    @OnlyIn(Dist.CLIENT)
    public abstract ObjModelRenderer getPart(String name);

    @OnlyIn(Dist.CLIENT)
    public abstract void renderAll(RenderType rtype, VertexConsumer buffer, PoseStack matrixStackIn,int packedLightIn,float red, float green, float blue, float alpha);

    @OnlyIn(Dist.CLIENT)
    public abstract void renderPart(RenderType rtype, String partName, VertexConsumer buffer, PoseStack matrixStackIn,int packedLightIn,float red, float green, float blue, float alpha);

    public abstract void clearDuplications() throws ConcurrentModificationException;
    public abstract boolean hasDuplications();
    protected abstract void addDuplication(ObjModelRenderer renderer);
}
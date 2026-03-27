package wmlib.loader.api.model;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.util.ConcurrentModificationException;
import java.util.List;
import com.mojang.blaze3d.matrix.MatrixStack;
public abstract class AbstractObjModel {
    public abstract List<ObjModelRenderer> getParts();
    @OnlyIn(Dist.CLIENT)
    public abstract ObjModelRenderer getPart(String name);
    @OnlyIn(Dist.CLIENT)
    public abstract void renderAll(int light);
    @OnlyIn(Dist.CLIENT)
    public abstract void renderPart(int light, String partName);
    public abstract void clearDuplications() throws ConcurrentModificationException;
    public abstract boolean hasDuplications();
    protected abstract void addDuplication(ObjModelRenderer renderer);
}
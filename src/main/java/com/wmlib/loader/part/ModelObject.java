package wmlib.loader.part;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.util.ArrayList;
import java.util.List;

public class ModelObject {
    public String name;
    public ArrayList<Face> faces = new ArrayList<>();
    public int glDrawingMode;

    public ModelObject() {
        this("");
    }

    public ModelObject(String name) {
        this(name, -1);
    }

    public ModelObject(String name, int glDrawingMode) {
        this.name = name;
        this.glDrawingMode = glDrawingMode;
    }

    @OnlyIn(Dist.CLIENT)
    public int renderByVAO(List<Float> renderer) {
        int flag = 0;
        if (faces.size() > 0) {
            for (Face face : faces) {
                flag = face.renderByVAO(renderer);
            }
        }
        return flag;
    }
}
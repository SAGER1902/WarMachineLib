package wmlib.loader.part;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.PoseStack;
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
    public void renderMC(VertexConsumer renderer, PoseStack matrixStackIn,int packedLightIn,float red, float green, float blue, float alpha) {
        if (faces.size() > 0) {
            for (Face face : faces) {
				/*Face.setLightMap(packedLightIn);
				Face.setMatrix(matrixStackIn);*/
                face.renderMC(glDrawingMode, renderer, red, green, blue, alpha);
            }
        }
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
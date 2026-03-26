package wmlib.loader;
import net.minecraft.client.renderer.RenderType;
//import wmlib.WarMachineLib;
import wmlib.loader.api.model.AbstractObjModel;
import wmlib.loader.api.model.ObjModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
public class ObjModel extends AbstractObjModel {

    public List<ObjModelRenderer> parts;
    public float modelScale = 1.0f;
    private List<ObjModelRenderer> duplications = new ArrayList<>();

    public ObjModel(List<ObjModelRenderer> parts) {
        this.parts = parts;
    }

    public ObjModel() {
    }
	
    @Override
    public List<ObjModelRenderer> getParts() {
        return parts;
    }

    void setParts(List<ObjModelRenderer> renderers) {
        parts = renderers;
    }

    public ObjModelRenderer getPart(String name) {
        for (ObjModelRenderer part : parts) {
            if (name.equals(part.getName())) {
                return part;
            }
        }
        return null;
    }
	public boolean resetColor = false;
    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderAll(RenderType rtype, VertexConsumer buffer, PoseStack matrixStackIn,int packedLightIn,float red, float green, float blue, float alpha) {
        checkForNoDuplications();
        for (ObjModelRenderer part : parts) {
            part.render(rtype, buffer, matrixStackIn, packedLightIn, red, green, blue, alpha);
			part.resetColor=this.resetColor;
        }
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderPart(RenderType rtype, String partName, VertexConsumer buffer, PoseStack matrixStackIn,int packedLightIn,float red, float green, float blue, float alpha) {
        checkForNoDuplications();
        for (ObjModelRenderer part : parts) {
            if (partName.equals(part.getName())) {//contains
                part.render(rtype, buffer, matrixStackIn, packedLightIn, red, green, blue, alpha);
				part.resetColor=this.resetColor;
            }
        }
    }

    @Override
    public void clearDuplications() throws ConcurrentModificationException {
        try {
            for (ObjModelRenderer renderer : duplications) {
                parts.remove(renderer);
            }
        } catch (ConcurrentModificationException e) {
            throw new ConcurrentModificationException("You must clear duplications ONLY AFTER passing ObjModelRaw#parts!!!\n" + e.getMessage());
        }

        duplications.clear();
    }

    @Override
    public boolean hasDuplications() {
        return !duplications.isEmpty();
    }

    private String[] formDuplicationList() {
        String[] list = new String[duplications.size()];
        for (int i = 0; i < duplications.size(); i++) {
            list[i] = duplications.get(i).getName();
        }

        return list;
    }

    private boolean isExcepted(ObjModelRenderer part, ObjModelRenderer[] excludedList) {
        for (ObjModelRenderer excludedPart : excludedList) {
            if (part.equals(excludedPart)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void addDuplication(ObjModelRenderer renderer) {
        duplications.add(renderer);
    }
    private void checkForNoDuplications() {
    }
}

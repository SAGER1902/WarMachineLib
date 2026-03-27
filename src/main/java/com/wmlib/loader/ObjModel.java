package wmlib.loader;

//import wmlib.WarMachineLib;
import wmlib.loader.api.model.AbstractObjModel;
import wmlib.loader.api.model.ObjModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import com.mojang.blaze3d.matrix.MatrixStack;
public class ObjModel extends AbstractObjModel {
    public List<ObjModelRenderer> parts;
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
    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderAll(int light) {
        checkForNoDuplications();
        for (ObjModelRenderer part : parts) {
            part.render(light);
        }
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderPart(int light, String partName) {
        checkForNoDuplications();
        for (ObjModelRenderer part : parts) {
            if (partName.equals(part.getName())) {//contains
                part.render(light);
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

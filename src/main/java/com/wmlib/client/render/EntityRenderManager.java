package wmlib.client.render;
import com.mojang.blaze3d.shaders.*;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import java.io.IOException;
import wmlib.client.ClientRenderHandler;

public class EntityRenderManager {
    /*private static ShaderInstance entityShader = ClientRenderHandler.THERMAL_IMAGING_SHADER;
    private static ThermalMode currentMode = ThermalMode.THERMAL_BW;
    private static float intensity = 1.5f;
    
    public enum ThermalMode {
        NORMAL(0), HIGHLIGHT(1), THERMAL_BW(2), THERMAL_COLOR(3);
        private final int id;
        ThermalMode(int id) { this.id = id; }
        public int getId() { return id; }
    }
    
    // 加载着色器（在资源重载时调用）
    /*public static void loadShader(ResourceManager manager) throws IOException {
        if (entityShader != null) entityShader.close();
        entityShader = new ShaderInstance(
            manager, 
            new ResourceLocation("yourmodid", "core/entity_control"),
            DefaultVertexFormat.NEW_ENTITY
        );
    }*
    
    // 应用当前渲染模式
    public static void applyMode() {
        //if (entityShader == null || !ThermalConfig.ENABLED) return;
        ShaderInstance shader = ClientRenderHandler.THERMAL_IMAGING_SHADER;
		if (shader != null) {
			shader.apply();
			shader.safeGetUniform("u_Mode").set(2);
			shader.safeGetUniform("u_Intensity").set(intensity);
			shader.safeGetUniform("u_NormalInfluence").set(ThermalConfig.normalInfluence);
		}
    }
    
    // 切换模式的方法（可在按键事件中调用）
    public static void switchMode() {
        currentMode = ThermalMode.values()[(currentMode.ordinal() + 1) % ThermalMode.values().length];
        applyMode();
    }
    
    public static void setMode(ThermalMode mode) {
        currentMode = mode;
        applyMode();
    }
    
    public static ShaderInstance getShader() { return entityShader; }*/
}
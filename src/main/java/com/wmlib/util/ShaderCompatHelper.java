package wmlib.util;
import wmlib.WMConfig;
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import wmlib.WarMachineLib;
import net.irisshaders.iris.Iris;
//import net.irisshaders.iris.config.IrisConfig;*/
public class ShaderCompatHelper {
    /**
     * 方法1：通过检查关键光影类是否存在来判断。
     * 这是最直接的方法，但并不总是100%准确。
     */
    /*public static boolean isShadersModPresent() {
        // 检测Oculus (Fabric光影模组Forge移植版)
        if (ModList.get().isLoaded("oculus")) {
            return true;
        }
        // 检测OptiFine (通常自带光影)
        // 注意：OptiFine并非Forge模组，此检测可能不适用
        // if (ModList.get().isLoaded("optifine")) { return true; }
        return false;
    }*/
    
    /**
     * 方法2：通过检查游戏渲染器的属性来判断是否处于“光影”渲染模式。
     * 这是一个更偏向功能性的检查，可能更可靠。
     * 注意：此方法基于原版及常见光影模组的行为，非官方API。
     */
    public static boolean isShaderPackLoaded() {
		return !WMConfig.useCustomShaders;
        /*Minecraft mc = Minecraft.getInstance();
        //GameRenderer gameRenderer = mc.gameRenderer;
        try {
            // 常见光影模组激活时，会替换GameRenderer中的shader核心组件。
            // 检查当前的“渲染类型”是否处于特殊状态。
            // 这是一种启发式检查，可能需要对特定光影模组调整。
            if (mc.level != null && ModList.get().isLoaded("oculus")) {
                // 例如，检查是否有自定义的后处理效果（光影常见）
                // 或者，直接尝试反射检查（更复杂，此处不展开）
                // 一个简单的标志是：当不是原版默认渲染管线时
                return !Iris.isFallback()true;
            }
        } catch (Exception e) {
            // 安全捕获，避免崩溃
            WarMachineLib.LOGGER.debug("Error checking shader status, assuming false.", e);
        }*/
		/*if(ModList.get().isLoaded("oculus")){
			//return !Iris.isFallback()||Iris.loadedIncompatiblePack();
			return Iris.getIrisConfig().areShadersEnabled();
		}else{
			return false;
		}*/
    }
    
    /**
     * 综合判断：优先使用方法2（功能检查），如果拿不准，再使用方法1（模组存在检查）。
     * 你可以根据你的主要兼容目标（如Oculus）调整逻辑。
     */
    /*public static boolean shouldUseFallbackShader() {
        // 如果你主要想兼容Oculus，可以这样写：
        boolean isOculusPresent = ModList.get().isLoaded("oculus");
        if (isOculusPresent) {
            // 当Oculus存在时，我们假设它可能接管了渲染，使用回退方案。
            // 如果希望更精确，可以在这里添加Oculus特定的API检查。
            return true;
        }
        // 对于其他情况，可以结合功能检查
        return isShaderPackLoaded();
    }*/
}
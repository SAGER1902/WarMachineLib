package wmlib.client.render;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
//import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL13;

import com.mojang.blaze3d.systems.RenderSystem;
public class SARenderHelper {//this from techguns

	protected static float lastBrightnessX=0;
	protected static float lastBrightnessY=0;
	
	protected static int lastBlendFuncSrc=0;
	protected static int lastBlendFuncDest=0;

	public static void enableFXLighting()
    {
    	lastBrightnessX= GlStateManager.lastBrightnessX;
		lastBrightnessY= GlStateManager.lastBrightnessY;
		//GlStateManager._disableLighting();
		GlStateManager._glMultiTexCoord2f(GL13.GL_TEXTURE1, 240f, 240f);
    }
	
    public static void disableFXLighting()
    {
    	//GlStateManager._enableLighting();
    	GlStateManager._glMultiTexCoord2f(GL13.GL_TEXTURE1, lastBrightnessX, lastBrightnessY);
    }
    
    public static void enableFluidGlow(int luminosity) {
    	lastBrightnessX= GlStateManager.lastBrightnessX;
		lastBrightnessY= GlStateManager.lastBrightnessY;
		
		float newLightX = Math.min((luminosity/15.0f)*240.0f + lastBrightnessX, 240.0f);
		float newLightY = Math.min((luminosity/15.0f)*240.0f + lastBrightnessY, 240.0f);
		
    	GlStateManager._glMultiTexCoord2f(GL13.GL_TEXTURE1, newLightX, newLightY);
    }
    
    public static void disableFluidGlow() {
    	GlStateManager._glMultiTexCoord2f(GL13.GL_TEXTURE1, lastBrightnessX, lastBrightnessY);
    }

    public enum RenderTypeSA {
    	ALPHA, ADDITIVE, SOLID, ALPHA_SHADED, NO_Z_TEST;
    }
	
	//protected static boolean lighting=true;
	
    public static void enableBlendMode(RenderTypeSA type) {
		//lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
		//if(!lighting)GL11.glDisable(GL11.GL_LIGHTING);//关闭阴影
    	if (type != RenderTypeSA.SOLID) {
    		GlStateManager._enableBlend();
    		GlStateManager._depthMask(false);
    	}
        if (type == RenderTypeSA.ALPHA) {
        	//GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			//RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, 
			GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			//RenderSystem.alphaFunc(516, 0.003921569F);
        } else if (type == RenderTypeSA.ADDITIVE || type==RenderTypeSA.NO_Z_TEST) {
        	//GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			//RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, 
			GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			//RenderSystem.alphaFunc(516, 0.003921569F);
        }
        
        if (type==RenderTypeSA.NO_Z_TEST){
        	GlStateManager._depthMask(false);
        	GlStateManager._enableDepthTest();//_disableDepth
        }
        if (type != RenderTypeSA.ALPHA_SHADED) SARenderHelper.enableFXLighting();
	}
	
    /**
     * This includes FXLighting!
     */
	public static void disableBlendMode(RenderTypeSA type) {
		if (type != RenderTypeSA.ALPHA_SHADED) SARenderHelper.disableFXLighting();
		if (type != RenderTypeSA.SOLID) {
    		GlStateManager._disableBlend();
    		GlStateManager._depthMask(true);
    	}
		if (type == RenderTypeSA.ALPHA) {
        	//GlStateManager._blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			RenderSystem.defaultBlendFunc();
			//RenderSystem.disableBlend();
        } else if (type == RenderTypeSA.ADDITIVE || type==RenderTypeSA.NO_Z_TEST) {
        	//GlStateManager._blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			RenderSystem.defaultBlendFunc();
			//RenderSystem.disableBlend();
        }
		
        if (type==RenderTypeSA.NO_Z_TEST){
        	GlStateManager._depthMask(true);
        	GlStateManager._enableDepthTest();
        }
		//if(!lighting)GL11.glEnable(GL11.GL_LIGHTING);
	}
}

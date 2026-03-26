package wmlib.client.render;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.*;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import java.awt.*;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import wmlib.client.ClientRenderHandler;
import wmlib.util.ShaderCompatHelper;
public class SARenderState extends RenderStateShard{
    static public VertexFormat POSITION_TEX_LMAP_COL_NORMAL = new VertexFormat(ImmutableMap.<String,VertexFormatElement>builder().put("Position",DefaultVertexFormat.ELEMENT_POSITION).put("Color",DefaultVertexFormat.ELEMENT_COLOR).put("UV0",DefaultVertexFormat.ELEMENT_UV0).put("UV2",DefaultVertexFormat.ELEMENT_UV2).put("Normal",DefaultVertexFormat.ELEMENT_NORMAL).put("Padding",DefaultVertexFormat.ELEMENT_PADDING).build());
	public SARenderState(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
        super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
    }
    public static VertexConsumer getBuffer(MultiBufferSource bufferIn, RenderType renderTypeIn, boolean glintIn) {
        return null;
    }
	protected static final RenderStateShard.TransparencyStateShard LIGHTNING_ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("lightning_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    /*public static RenderType lightning() {
        return LIGHTNING;
    }
	private static final RenderType LIGHTNING = RenderType.create("lightning", DefaultVertexFormat.POSITION_COLOR, 
	VertexFormat.Mode.QUADS, 256, false, true, 
	CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER)
	.setWriteMaskState(COLOR_DEPTH_WRITE)
	.setTransparencyState(LIGHTNING_TRANSPARENCY)
	.setOutputState(WEATHER_TARGET)
	.createCompositeState(false));*/
	
    public static RenderType getLightning() {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_LIGHTNING_SHADER)
                //.setOutputState(RenderStateShard.PARTICLES_TARGET)
				.setCullState(RenderStateShard.NO_CULL)
                .setTransparencyState(LIGHTNING_TRANSPARENCY)
                //.setLightmapState(RenderStateShard.LIGHTMAP)
                .setWriteMaskState(COLOR_DEPTH_WRITE)
				.setOutputState(WEATHER_TARGET)
                .createCompositeState(false);
        return RenderType.create("wmlib_lightning", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, state);
    }
	
    /*static RenderStateShard.ShaderStateShard customShaderState = new RenderStateShard.ShaderStateShard(
        () -> ClientRenderHandler.CUSTOM_PORTAL_SHADER
    );*/

	protected static final RenderStateShard.ShaderStateShard CUSTOM_PORTAL_SHADER_STATE = new RenderStateShard.ShaderStateShard(ClientRenderHandler::getRendertypePortalShader);
	public static RenderType getPortal(ResourceLocation tex1,ResourceLocation tex2) {
			RenderType.CompositeState state = RenderType.CompositeState.builder()
					.setShaderState(ShaderCompatHelper.isShaderPackLoaded() ? RENDERTYPE_END_PORTAL_SHADER:CUSTOM_PORTAL_SHADER_STATE)
					.setTextureState(RenderStateShard.MultiTextureStateShard.builder().
					add(tex1, false, false).
					add(tex2, false, false).build())
					.createCompositeState(false);
			return RenderType.create("wmlib_portal", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }
	
    public static RenderType getBlendDepthWrite(ResourceLocation tex) {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)//高亮
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, true, false))
                .setTransparencyState(LIGHTNING_ADDITIVE_TRANSPARENCY)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setWriteMaskState(COLOR_DEPTH_WRITE)//双面+前后区分
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_depth_write", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }

    public static RenderType getBlendGlowGlint(ResourceLocation tex) {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, true, false))
                .setTransparencyState(LIGHTNING_ADDITIVE_TRANSPARENCY)//LIGHTNING_TRANSPARENCY
                .setLightmapState(RenderStateShard.LIGHTMAP)
                //.setWriteMaskState(COLOR_DEPTH_WRITE)//双面+前后区分
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_glint", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }
	
    public static RenderType getBlendGlow(ResourceLocation tex) {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)//高亮
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, true, false))
                .setTransparencyState(LIGHTNING_TRANSPARENCY)
                .setWriteMaskState(COLOR_DEPTH_WRITE)//双面+前后区分
				.setOutputState(WEATHER_TARGET)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_glow", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, false, true, state);
    }
    public static RenderType getBlend(ResourceLocation tex) {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)
                .setOutputState(PARTICLES_TARGET)
                .setCullState(RenderStateShard.NO_CULL)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, true, false))
                .setTransparencyState(LIGHTNING_TRANSPARENCY)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }

    public static RenderType getBlend_L(ResourceLocation tex,boolean light) {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
                //.setOutputState(PARTICLES_TARGET)
                //.setCullState(RenderStateShard.NO_CULL)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, true, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(light ? RenderStateShard.NO_LIGHTMAP : RenderStateShard.LIGHTMAP)
                //.setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_l", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }

    public static RenderType getPlaneTrail(ResourceLocation tex) {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, true, false))
                .setTransparencyState(LIGHTNING_TRANSPARENCY)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_write_t", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }
	
    public static RenderType getBlendDepthWrite_NoLight(ResourceLocation tex) {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, true, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setWriteMaskState(COLOR_DEPTH_WRITE)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_depth_write1", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }
	
    public static RenderType getBlendWrite_NoLight(ResourceLocation tex) {//GLINT_TRANSPARENCY
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)//RENDERTYPE_ENTITY_CUTOUT_SHADER
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, true, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_write", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }
	
    /*static RenderStateShard.ShaderStateShard customShaderStateTest = new RenderStateShard.ShaderStateShard(
        () -> ClientRenderHandler.THERMAL_IMAGING_SHADER
    );*/
	
    public static RenderType getNightLight(ResourceLocation tex) {// 夜视
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)//
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, true, false))
                .setTransparencyState(CRUMBLING_TRANSPARENCY)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_write", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }
	
    public static RenderType getBlendWrite_Light(ResourceLocation tex) {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, true, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_write2", POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }
}

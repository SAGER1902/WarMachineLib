package wmlib.client.render;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.VertexBuilderUtils;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import java.awt.*;
import java.util.function.Function;

import net.minecraft.util.math.vector.Vector4f;
import java.util.ArrayList;
import java.util.List;

public class SARenderState extends RenderState{
    public SARenderState(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
        super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
    }
    public static IVertexBuilder getBuffer(IRenderTypeBuffer bufferIn, RenderType renderTypeIn, boolean glintIn) {
        return null;
    }
	
    static public VertexFormat POSITION_TEX_LMAP_COL_NORMAL = new VertexFormat(ImmutableList.<VertexFormatElement>builder()
        .add(DefaultVertexFormats.ELEMENT_POSITION)
		.add(DefaultVertexFormats.ELEMENT_COLOR)
        .add(DefaultVertexFormats.ELEMENT_UV0)
        .add(DefaultVertexFormats.ELEMENT_UV2)
        .add(DefaultVertexFormats.ELEMENT_NORMAL)
        .add(DefaultVertexFormats.ELEMENT_PADDING)
        .build());
		
	public static RenderType endPortal(int type,ResourceLocation tex) {
	  RenderState.TransparencyState transparencystate;
	  RenderState.TextureState texturestate;
	  if (type <= 1) {
		 transparencystate = TRANSLUCENT_TRANSPARENCY;
	  } else {
		 transparencystate = ADDITIVE_TRANSPARENCY;
	  }
	  texturestate = new RenderState.TextureState(tex, false, false);
	  return RenderType.create("evil_portal", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, 
	  RenderType.State.builder()
	  .setTransparencyState(transparencystate)
	  .setTextureState(texturestate)
	  .setTexturingState(new RenderState.PortalTexturingState(type))
	  .setFogState(BLACK_FOG)
	  .createCompositeState(false));
	}


	protected static final RenderState.TransparencyState LIGHTNING_ADDITIVE_TRANSPARENCY = new RenderState.TransparencyState("lightning_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
	
    public static RenderType getBlend(ResourceLocation p_228638_0_) {
        RenderType.State state = RenderType.State.builder()
                //.setShadeModelState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                .setOutputState(PARTICLES_TARGET)
                //.setCullState(RenderState.NO_CULL)
                .setTextureState(new RenderState.TextureState(p_228638_0_, true, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(RenderState.LIGHTMAP)
                //.setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend", POSITION_TEX_LMAP_COL_NORMAL, GL11.GL_TRIANGLES, 256, true, false, state);
    }
    public static RenderType getBlendDepthWrite(ResourceLocation p_228638_0_) {
        RenderType.State state = RenderType.State.builder()
                //.setShadeModelState(RENDERTYPE_ENERGY_SWIRL_SHADER)
                .setOutputState(RenderState.PARTICLES_TARGET)
                .setTextureState(new RenderState.TextureState(p_228638_0_, true, false))
                .setTransparencyState(LIGHTNING_ADDITIVE_TRANSPARENCY)
                .setLightmapState(RenderState.LIGHTMAP)
                .setWriteMaskState(COLOR_DEPTH_WRITE)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_depth_write", POSITION_TEX_LMAP_COL_NORMAL, GL11.GL_TRIANGLES, 256, true, false, state);
    }

    public static RenderType getBlendGlow(ResourceLocation p_228638_0_) {
        RenderType.State state = RenderType.State.builder()
                //.setShadeModelState(RENDERTYPE_ENERGY_SWIRL_SHADER)
                .setOutputState(PARTICLES_TARGET)
                .setTextureState(new RenderState.TextureState(p_228638_0_, true, false))
                .setTransparencyState(LIGHTNING_ADDITIVE_TRANSPARENCY)
                .setLightmapState(RenderState.LIGHTMAP)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return RenderType.create("wmlib_blend_glow", DefaultVertexFormats.BLOCK, GL11.GL_QUADS, 256, true, false, state);
    }
}

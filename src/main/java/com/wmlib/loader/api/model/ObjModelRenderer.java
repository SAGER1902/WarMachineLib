package wmlib.loader.api.model;
import wmlib.loader.ObjModel;
import wmlib.loader.part.ModelObject;
import wmlib.loader.part.Vertex;
import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.*;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.nio.IntBuffer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.system.MemoryUtil;
import wmlib.client.render.RenderTypeVehicle;
import wmlib.client.render.SARenderState;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.shaders.Uniform;

import net.minecraft.util.Mth;
import java.awt.Color;
import wmlib.WMConfig;
public class ObjModelRenderer {
	boolean useMC = false;
    public boolean isHidden;
    private ModelObject model;
    private AbstractObjModel parent;
    private boolean glow = false;
    private int vertexCount = 0;
    private boolean compiled;
	private VertexBuffer vertexBuffer;
    public ObjModelRenderer(ObjModel parent, ModelObject modelForRender) {
        this.model = modelForRender;
        this.parent = parent;
    }
    public String getName() {
        return model.name;
    }
	public boolean resetColor = false;
    @OnlyIn(Dist.CLIENT)
    public void render(RenderType rtype, VertexConsumer buffer, PoseStack matrixStackIn,int packedLightIn,float red, float green, float blue, float alpha) {
		if(rtype==null && buffer!=null){
			useMC=true;
		}else{
			useMC=false;
		}
        if (!this.isHidden) {
            if (!this.compiled){
				if(useMC){
					model.renderMC(buffer, matrixStackIn, packedLightIn, red, green, blue, alpha);
				}else{
					setupBuffers(rtype, packedLightIn);
				}
            }
			if(useMC){
			} else {
				if(WMConfig.useCustomModelLight && packedLightIn!=0xF000F0 && red==1 && green==1 && blue==1 && red!=-999){
					int blockLight = (packedLightIn & 0xFFFF) >> 4;
					int skyMask = (packedLightIn >> 20) & 15;
					Minecraft mc = Minecraft.getInstance();
					float partialTicks = mc.getFrameTime();
					float rainLevel = mc.level.getRainLevel(partialTicks);
					float thunderLevel = mc.level.getThunderLevel(partialTicks);
					float sunAngle = mc.level.getSunAngle(partialTicks);
					
					float daylight = Mth.cos(sunAngle) * 0.5f + 0.5f;
					daylight = Mth.clamp(daylight, 0.0f, 1.0f);
					float weatherFactor = 1.0f - (rainLevel * 0.1f) - (thunderLevel * 0.2f);
					float skyLight = (skyMask / 15.0f) * daylight * weatherFactor;
					if(skyLight>1)skyLight=1;
					float blockLight1 = blockLight * (2.2F-skyLight*1.5F) / 15.0f;
					float finalLight = Math.max(skyLight, blockLight1)*1.1F+0.5F;
					RenderSystem.setShaderColor(finalLight, finalLight, finalLight, alpha);
				}
				renderVertex(rtype, matrixStackIn);
				if(WMConfig.useCustomModelLight && packedLightIn!=0xF000F0 && red==1 && green==1 && blue==1 && red!=-999){
					RenderSystem.setShaderColor(1, 1, 1, 1.0f);
				}
			}
        }
    }
    
    //public static ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/m1tank.png");
    public static RenderType renderType/* = SARenderState.getBlend(tex)*/;
    private void setupBuffers(RenderType rtype,int packedLightIn) {
        List<Float> vertexData = new ArrayList<Float>();
        int stride = model.renderByVAO(vertexData);
		vertexCount = vertexData.size() / stride;
		this.vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
		renderType = rtype;
        //renderType = RenderType.create("obj_vb", 
		//DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 2097152, true,
		//false/*isTranslucent*/, SARenderState.createForObject(tex).createCompositeState(false));
        BufferBuilder bufferBuilder = new BufferBuilder(/*vertexData.size()*4*/renderType.format().getIntegerSize() * vertexCount);
        bufferBuilder.begin(VertexFormat.Mode.TRIANGLES, renderType.format());
        // 添加顶点数据
        for (int i = 0; i < vertexData.size(); i += stride) {
            // 手动添加每个属性
            bufferBuilder.vertex(vertexData.get(i), vertexData.get(i+1), vertexData.get(i+2)) // 位置
			.color(255,255,255,255)
			.uv(vertexData.get(i+3), vertexData.get(i+4)) // UV
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(packedLightIn)
			.normal(vertexData.get(i+5), vertexData.get(i+6), vertexData.get(i+7)) // 法线
			.endVertex();
        }
        vertexBuffer.bind();
        // 上传数据
        vertexBuffer.upload(bufferBuilder.end());
        // 解绑
        VertexBuffer.unbind();
		this.compiled = true;
    }
	//int iii;
    public void renderVertex(RenderType rtype,PoseStack stack) {
        if (vertexCount == 0) return;
		//++iii;
        if(rtype!=null)rtype.setupRenderState();
        // 使用实例化绘制代替传统绘制
		//RenderSystem.setShaderTexture(0, tex);
        Matrix4f matrix = stack.last().pose();
        Matrix3f normalMatrix = stack.last().normal();

		ShaderInstance shaderInstance = RenderSystem.getShader();
		for (int k = 0; k < 12; ++k) {
			int i = RenderSystem.getShaderTexture(k);
			shaderInstance.setSampler("Sampler" + k, i);
		}
		if (shaderInstance.PROJECTION_MATRIX != null) {
			shaderInstance.PROJECTION_MATRIX.set(RenderSystem.getProjectionMatrix());
		}
		/*if (shaderInstance.INVERSE_VIEW_ROTATION_MATRIX != null) {//导致没有正常缩放
			shaderInstance.INVERSE_VIEW_ROTATION_MATRIX.set(RenderSystem.getInverseViewRotationMatrix());
		}*/
		if (shaderInstance.COLOR_MODULATOR != null) {
			shaderInstance.COLOR_MODULATOR.set(RenderSystem.getShaderColor());
		}
		if (shaderInstance.FOG_START != null) {
			shaderInstance.FOG_START.set(RenderSystem.getShaderFogStart());
		}
		if (shaderInstance.FOG_END != null) {
			shaderInstance.FOG_END.set(RenderSystem.getShaderFogEnd());
		}
		if (shaderInstance.FOG_COLOR != null) {
			shaderInstance.FOG_COLOR.set(RenderSystem.getShaderFogColor());
		}
		if (shaderInstance.FOG_SHAPE != null) {
			shaderInstance.FOG_SHAPE.set(RenderSystem.getShaderFogShape().getIndex());
		}
		if (shaderInstance.MODEL_VIEW_MATRIX != null) {
			shaderInstance.MODEL_VIEW_MATRIX.set(matrix);
		}
		/*if (shaderInstance.NORMAL_MATRIX != null) {
			shaderInstance.NORMAL_MATRIX.set(normalMatrix);
		}*/
		if (shaderInstance.TEXTURE_MATRIX != null) {
			shaderInstance.TEXTURE_MATRIX.set(RenderSystem.getTextureMatrix());
		}
		/*if(iii<10){
			iii+=1F;
		}else{
			iii=0;
		}
		if(iii<5){
			RenderSystem.setShaderLights(
            new Vector3f(255F, 255F, 255F),  // 环境光
            new Vector3f(255F, 1.0F, 255F)   // 漫反射光
        );
		}*/
		
		RenderSystem.setupShaderLights(shaderInstance);
		shaderInstance.apply();
        // 绑定 VertexBuffer (对应绑定 VBO)
        vertexBuffer.bind();
		vertexBuffer.draw();
        /*// 使用 drawWithShader 方法渲染
        vertexBuffer.drawWithShader(
            matrix, 
            RenderSystem.getProjectionMatrix(), 
            RenderSystem.getShader()
        );*/
		VertexBuffer.unbind();
		shaderInstance.clear();
		if(rtype!=null)rtype.clearRenderState();
    }
}
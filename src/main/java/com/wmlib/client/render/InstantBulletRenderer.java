package wmlib.client.render;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import wmlib.util.vec.Vector3f;
import wmlib.client.obj.SAObjModel;
import wmlib.client.render.SARenderState;
import safx.SagerFX;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.platform.GlStateManager;
public class InstantBulletRenderer {
    private static CopyOnWriteArrayList<InstantShotTrail> trails = new CopyOnWriteArrayList<>();//性能有一定影响
    public static void AddTrail(InstantShotTrail trail) {
        if(trail!=null && trails.size()<40)trails.add(trail);
    }
    public static void RenderAllTrails(PoseStack stack,float partialTicks,double x,double y, double z) {
		if(trails!=null && trails.size()<40){
			for (InstantShotTrail trail : trails) {
				trail.Render(stack, partialTicks,x,y,z);
				//System.out.println("渲染执行1");
			}
		}
    }
    public static void UpdateAllTrails() {
		for (int i = trails.size() - 1; i >= 0; i--) {
			if (trails.get(i).Update()) {
				trails.remove(i);
				//System.out.println("移除"+i);
			}
		}
    }

    public static class InstantShotTrail {
        private Vector3f origin;
        private Vector3f hitPos;
        private float width;
        private float length;
        private float distanceToTarget;
        private float bulletSpeed;
		private float powersize;
        private int ticksExisted;
		private int maxtime;
        private ResourceLocation texture;
		private SAObjModel bulletmodel = null;
		private SAObjModel powermodel = new SAObjModel("wmlib:textures/entity/flash/power.obj");
		private String bullettex = "wmlib:textures/entity/bullet/bullet.png";
		private int type;
		public InstantShotTrail(){
		}
        public InstantShotTrail(Vector3f origin, Vector3f hitPos, float bulletSpeed, String name, int id, int time) {
            this.ticksExisted = 0;
            this.bulletSpeed = bulletSpeed;
			if(id>1){
				this.maxtime = time;
				this.powersize = bulletSpeed;
			}
            this.origin = origin;
            this.hitPos = hitPos;
			this.type = id;
            this.length = 8.0f * new Random().nextFloat();
			this.texture = ResourceLocation.tryParse("wmlib:textures/entity/bullet.png");
			this.width = 0.2f;
			if(id==3||id==6){
			}else{
				if(name!=null){
					this.bulletmodel = new SAObjModel(name+".obj");
					this.bullettex = name+".png";
				}
			}
            Vector3f dPos = Vector3f.sub(hitPos, origin, null);
            this.distanceToTarget = dPos.length();
            if (Math.abs(distanceToTarget) > 300.0f) {
                distanceToTarget = 300.0f;
            }
        }
        public boolean Update() {
            ticksExisted++;
            if (length > 0F) {
                length -= 0.05F;
            }
			if(type>1){
				return ticksExisted>=maxtime;
			}else{
				return (ticksExisted) * bulletSpeed >= distanceToTarget - length / 4;
			}
        }
		
		float time;
		double offset = 1.20; // Distance per bolt vertex
		int SIN_COUNT = 6; // Number of overlapping sin functions
		double WIDTH = 1.5;
		double SIN_DISTANCE = 20.0; //ideal distance for one sinus curve;
        public void Render(PoseStack stack, float partialTicks, double xo, double yo, double zo) {
            stack.pushPose();
			//System.out.println("开渲");
			Minecraft mc = Minecraft.getInstance();
			stack.translate(-(float) xo, -(float) yo, -(float) zo);
            float parametric = ((float) (ticksExisted) + partialTicks) * bulletSpeed;
            Vector3f dPos = Vector3f.sub(hitPos, origin, null);
            dPos.normalise();
            float startParametric = parametric - length * 0.25f;
            Vector3f startPos = new Vector3f(origin.x + dPos.x * startParametric, origin.y + dPos.y * startParametric, origin.z + dPos.z * startParametric);
            float endParametric = parametric + length * 0.25f;
            Vector3f endPos = new Vector3f(origin.x + dPos.x * endParametric, origin.y + dPos.y * endParametric, origin.z + dPos.z * endParametric);
            dPos.normalise();
			
			Vector3f dVec =new Vector3f(hitPos.x-origin.x,0,hitPos.z-origin.z);
			dVec=(Vector3f)dVec.normalise();
			float yaw=(float)Math.acos(dVec.z)/3.1415f*180;
			if(dVec.x<0)yaw=-yaw;
			dVec =new Vector3f(hitPos.x-origin.x,hitPos.y-origin.y,hitPos.z-origin.z);
			dVec=(Vector3f)dVec.normalise();
			float pitch=(float)Math.asin(dVec.y)/3.1415f*180;
			
            if(bulletmodel!=null&&bullettex!=null) {
                stack.pushPose();
				//RenderSystem.disableCull();
				float size = partialTicks * 0.5F + 1;//0.4F
				float size1 = length * 1.25f;
				float size2 = (parametric *powersize) / (20F * bulletSpeed);
				float size3 = partialTicks * 0.8F + 1;//0.4F
				if(type>1){
					size1 = this.distanceToTarget * 1f;
					if(this.distanceToTarget!=0)RenderSystem.setShaderColor(1F, 1F, 1F, (maxtime-parametric/bulletSpeed) / maxtime);
				}
				if(type==4||type==7||type==8){
					RenderType rt1 = SARenderState.getBlendGlowGlint(ResourceLocation.tryParse("wmlib:textures/entity/flash/power1.png"));
					RenderType rt3 = SARenderState.getBlendGlowGlint(ResourceLocation.tryParse("wmlib:textures/entity/flash/power3.png"));
					RenderType rt2 = SARenderState.getBlendGlowGlint(ResourceLocation.tryParse("wmlib:textures/entity/flash/power2.png"));
					float r = 1;
					float g = 1;
					float b = 1;
					if(type==7){
						r=0.1F;
						g=0.3F;
						b=1;
					}
					if(type==8){
						r=0.1F;
						g=0.5F;
						b=1;
					}
					stack.pushPose();
					stack.translate(hitPos.x, hitPos.y, hitPos.z);
					if(parametric/bulletSpeed<=maxtime/2){
						RenderSystem.setShaderColor(r, g, b, (1+parametric/bulletSpeed) * 2 / maxtime);
					}else{
						RenderSystem.setShaderColor(r, g, b, (maxtime-(1+parametric/bulletSpeed)) / maxtime);
					}
					stack.pushPose();
					stack.scale(size2, size2, size2);
					renderEnchantGlint(powermodel,stack,"mat1",rt1);
					renderEnchantGlint(powermodel,stack,"mat3",rt3);
					stack.popPose();
					RenderSystem.setShaderColor(r, g, b, (maxtime-parametric/bulletSpeed) / maxtime);
					stack.pushPose();
					
					RenderRote.setRote(stack,time * 5, 0.0F, 1.0F, 0.0F);
					stack.scale(size2*2F, size2*2F, size2*2F);
					renderEnchantGlint(powermodel,stack,"mat2",rt2);
					
					stack.popPose();
					stack.popPose();
					for (int i = 0; i < 12; i++) {	
						stack.pushPose();
						stack.translate(origin.x + dPos.x * this.distanceToTarget * i/12F, origin.y + dPos.y * this.distanceToTarget * i/12F, origin.z + dPos.z * this.distanceToTarget * i/12F);
						RenderRote.setRote(stack,yaw, 0, 1, 0);
						RenderRote.setRote(stack,pitch, -1, 0, 0);
						RenderRote.setRote(stack,time * 10, 0.0F, 0.0F, 1.0F);
						stack.scale(size2*0.8F, size2*0.8F, size2*0.8F);
						powermodel.renderPart("rote");
						stack.popPose();
					}
				}
				if(type>1){
					stack.translate(origin.x, origin.y, origin.z);
				}else{
					stack.translate(endPos.x, endPos.y, endPos.z);
				}
                RenderRote.setRote(stack,yaw, 0, 1, 0);
                RenderRote.setRote(stack,pitch, -1, 0, 0);
				++time;
				if(bulletmodel != null && time > 1){
					RenderType rt = SARenderState.getBlendDepthWrite(ResourceLocation.tryParse(this.bullettex));
					bulletmodel.setRender(rt,null,stack,15728880);
					{
						stack.pushPose();
						stack.scale(size*bulletSpeed, size*bulletSpeed, size1);
						bulletmodel.renderPart("trail");
						bulletmodel.setRender(rt,null,stack,15728880);
						if(type==2||type==4||type==5||type==7||type==8){
							RenderRote.setRote(stack,time * 10, 0.0F, 0.0F, 1.0F);
							bulletmodel.renderPart("beam1");
						}
						stack.popPose();
						stack.pushPose();
						stack.scale(size2*1.2F, size2*1.2F, size2*1.2F);
						RenderRote.setRote(stack,time * 10, 0.0F, 0.0F, 1.0F);
						bulletmodel.renderPart("start");
						stack.popPose();
					}
					RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
					bulletmodel.setRender(rt,null,stack,15728880);
					{
						stack.pushPose();
						{
							RenderRote.setRote(stack,time * 10, 0.0F, 0.0F, 1.0F);
							bulletmodel.renderPart("bullet");
						}
						bulletmodel.renderPart("mat1");
						stack.popPose();
					}
					{
						{
							RenderRote.setRote(stack,time * 10, 0.0F, 0.0F, 1.0F);
							stack.scale(size3, size3, size3);
							bulletmodel.renderPart("bullet_e_1");
						}
					}
				}
				//RenderSystem.enableCull();
                stack.popPose();
            }
			if(type==3||type == 6){
				stack.translate(origin.x, origin.y, origin.z);
                RenderRote.setRote(stack,yaw, 0, 1, 0);
                RenderRote.setRote(stack,pitch, -1, 0, 0);
				RenderSystem.disableCull();
				RenderSystem.depthMask(false);
				RenderSystem.enableBlend();
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				//RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

				//MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
				//RenderType rtlight = SARenderState.getBlendDepthWrite(ResourceLocation.tryParse("wmlib:textures/entity/flash/tesla.png"));
				RenderSystem.setShaderTexture(0, ResourceLocation.tryParse("wmlib:textures/entity/flash/tesla.png"));
				if(type == 6){
					SIN_COUNT=2;
					//rtlight = SARenderState.getBlendDepthWrite(ResourceLocation.tryParse("wmlib:textures/entity/flash/tesla2.png"));
					RenderSystem.setShaderTexture(0, ResourceLocation.tryParse("wmlib:textures/entity/flash/tesla2.png"));
				}
				//VertexConsumer vb = buffer.getBuffer(rtlight);
				stack.pushPose();
				float progress = /*parametric / 100F*/1;
				double distance = this.distanceToTarget;//长度
				Random rand = new Random();; //.nextFloat()Fixed seed for 1 bolt;
				double[] dY = new double[SIN_COUNT];
				double[] dZ = new double[SIN_COUNT];
				for (int i = 0; i < SIN_COUNT; i++) {	    	
					//TODO: Y/Z random vectors with fixed length, or whatever
					dY[i] = (0.5-rand.nextDouble())*3D; //fixed for this bolt
					dZ[i] = (0.5-rand.nextDouble())*3D;
				}
				int count = (int) Math.round(distance / offset);
				//int count = 20;
				offset = (distance / (double) count);
				float xOffset = 0.0f;
				int xreps = Math.max(1, (int) Math.round(distance / SIN_DISTANCE)); //TODO: get modulo as additional y/z scale?
				double xprev = 0, yprev = 0, zprev = 0, widthprev = 1.0, alphaprev = 1.0;
				for (int i = 0; i <= count; i++) {
					double d = (double)i/(double)count; //distance progress (0-1)
					double x1 = xOffset + (double)i*offset;
					double y1 = 0;
					double z1 = 0;
					double randomness = 0.00;
					if (i > 1) {
						for (int j = 1; j <= SIN_COUNT; j++) {
							double yfactor = ((rand.nextDouble()-0.5) + (progress*dY[j-1] *1.0)) * (2.0/(double)j);
							double zfactor = ((rand.nextDouble()-0.5) + (progress*dZ[j-1] *1.0)) * (2.0/(double)j);
							y1+= Math.sin((d * Math.PI) * (double)( j * xreps))*yfactor;
							z1+= Math.sin((d * Math.PI) * (double)( j * xreps))*zfactor;
						}
					}
					double pulse = 1.0 - Math.sqrt(Math.abs(progress-d)*2.0);
					//double width =  Math.max(0.0, WIDTH*pulse); //WIDTH+(WIDTH*10.0*pulse);
					double width =  Math.max(0.0, WIDTH*(100F-time) / 100F);
					if (i >= 1) {
						drawSegment(stack, (float)xprev, (float)yprev, (float)zprev, (float)x1,(float)y1,(float)z1, (float)widthprev, (float)width, 1/*(100F-time*rand.nextFloat()) / 100F*/, pulse);
					}
					widthprev = width;
					alphaprev = pulse;
					xprev = x1;
					yprev = y1;
					zprev = z1;
				}
				
				RenderSystem.enableCull();
				RenderSystem.depthMask(true);
				RenderSystem.disableBlend();
				stack.popPose();
				//buffer.endBatch();
			}
            stack.popPose();
        }
    }
	
	static void drawSegment(PoseStack stack, float x1, float y1, float z1, float x2, float y2, float z2, float width1, float width2, double a1, double a2) {
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
		double scale = 0.5;
		y1*=scale;
		y2*=scale;
		z1*=scale;
		z2*=scale;
		width1*=scale;
		width2*=scale;
		stack.pushPose();
		Matrix4f matrix4f = stack.last().pose();
		RenderRote.setRote(stack,-90F, 0, 1, 0);
		RenderRote.setRote(stack,45.0f, 1.0f, 0.0f, 0.0f);
		int light = 0xF000F0;
		//set alpha1
		bufferbuilder.begin(VertexFormat.Mode.QUADS, SARenderState.POSITION_TEX_LMAP_COL_NORMAL);			
		bufferbuilder.vertex(matrix4f, x1,  y1- width1,  z1).color(1.0f, 1.0f, 1.0f, (float)a1).uv(0, 0).overlayCoords(0, 240).uv2(light).normal(0,1F,0).endVertex();
		bufferbuilder.vertex(matrix4f, x1,  y1+ width1,  z1).color(1.0f, 1.0f, 1.0f, (float)a1).uv(0, 1).overlayCoords(0, 240).uv2(light).normal(0,1F,0).endVertex();
		bufferbuilder.vertex(matrix4f, x2,  y2+ width2,  z2).color(1.0f, 1.0f, 1.0f, (float)a1).uv(1, 1).overlayCoords(0, 240).uv2(light).normal(0,1F,0).endVertex();
		bufferbuilder.vertex(matrix4f, x2,  y2- width2,  z2).color(1.0f, 1.0f, 1.0f, (float)a1).uv(1, 0).overlayCoords(0, 240).uv2(light).normal(0,1F,0).endVertex();
		Tesselator.getInstance().end();
		//--
		//set alpha2	
		bufferbuilder.begin(VertexFormat.Mode.QUADS, SARenderState.POSITION_TEX_LMAP_COL_NORMAL);
		bufferbuilder.vertex(matrix4f, x1,  y1, z1-width1).color(1.0f, 1.0f, 1.0f, (float)a1).uv(0, 0).overlayCoords(0, 240).uv2(light).normal(0,1F,0).endVertex();
		bufferbuilder.vertex(matrix4f, x1,  y1, z1+width1).color(1.0f, 1.0f, 1.0f, (float)a1).uv(0, 1).overlayCoords(0, 240).uv2(light).normal(0,1F,0).endVertex();
		bufferbuilder.vertex(matrix4f, x2,  y2, z2+width2).color(1.0f, 1.0f, 1.0f, (float)a1).uv(1, 1).overlayCoords(0, 240).uv2(light).normal(0,1F,0).endVertex();
		bufferbuilder.vertex(matrix4f, x2,  y2, z2-width2).color(1.0f, 1.0f, 1.0f, (float)a1).uv(1, 0).overlayCoords(0, 240).uv2(light).normal(0,1F,0).endVertex();
		Tesselator.getInstance().end();
		stack.popPose();
	}
	static void renderEnchantGlint(SAObjModel obj, PoseStack stack, String name, RenderType buffer) {
		obj.setRender(buffer, null, stack, 15728880);
        //stack.scale(1.01001F, 1.01001F, 1.01001F);
        long time = (long)((double)Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 16.0);
        float u = (float)(time % 110000L) / 110000.0f;
        float v = (float)(time % 30000L) / 30000.0f;
		Matrix4f move = new Matrix4f().translation(u, v, 0.0f);
        RenderSystem.setTextureMatrix(move);
        obj.renderPart(name);
		RenderSystem.resetTextureMatrix();
    }
}
package wmlib.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import com.mojang.blaze3d.platform.GlStateManager;
//import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.GL11;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
//import wmlib.WarMachineLib;
import net.minecraft.client.renderer.ActiveRenderInfo;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import wmlib.util.Vector3f;
import safx.SagerFX;
import wmlib.client.obj.SAObjModel;
import net.minecraft.util.math.MathHelper;
import java.util.Iterator;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
public class InstantBulletRenderer {
    private static TextureManager textureManager;
    private static CopyOnWriteArrayList<InstantShotTrail> trails = new CopyOnWriteArrayList<>();//性能有一定影响
    public static void AddTrail(InstantShotTrail trail) {
        if(trail!=null && trails.size()<40)trails.add(trail);
    }
    public static void RenderAllTrails(float partialTicks) {
		if(trails!=null && trails.size()<40){
			for (InstantShotTrail trail : trails) {
				trail.Render(partialTicks);
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
	
   private static void setupGlintTexturing(float p_228548_0_) {
      RenderSystem.matrixMode(5890);
      RenderSystem.pushMatrix();
      RenderSystem.loadIdentity();
      long i = Util.getMillis() * 8L;
      float f = (float)(i % 110000L) / 110000.0F;
      float f1 = (float)(i % 30000L) / 30000.0F;
      RenderSystem.translatef(-f, f1, 0.0F);
      RenderSystem.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
      RenderSystem.scalef(p_228548_0_, p_228548_0_, p_228548_0_);
      RenderSystem.matrixMode(5888);
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
			this.texture = new ResourceLocation("wmlib", "textures/entity/" + "bullet.png");
			this.width = 0.2f;
			if(id==3||id==6){
			}else{
				if(name!=null){
					this.bulletmodel = new SAObjModel(name+".obj");
					this.bullettex = name+".png";
				}
			}

            Vector3f dPos = Vector3f.sub(hitPos, origin, null);
            /*RayTraceResult result = WarMachineLib.INSTANCE.RAY_CASTING.rayTraceBlocks(Minecraft.getInstance().level, origin.toVec3(), hitPos.toVec3(), true, true, false);
            if (result != null) {
                if (result.getLocation() != null) {
                    dPos = Vector3f.sub(new Vector3f(result.getLocation()), origin, null);
					if(id==3)SagerFX.proxy.createFX("TeslaHit", null, result.getLocation().x, result.getLocation().y, result.getLocation().z, 0, 0, 0, 1F);
					if(id==2)SagerFX.proxy.createFX("LaserHit", null, result.getLocation().x, result.getLocation().y, result.getLocation().z, 0, 0, 0, 1F);
					if(id==1)SagerFX.proxy.createFX("GunExp", null, result.getLocation().x, result.getLocation().y, result.getLocation().z, 0, 0, 0, 1F);
                }
            }*/
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
		
		int time;
		double offset = 1.20; // Distance per bolt vertex
		int SIN_COUNT = 6; // Number of overlapping sin functions
		double WIDTH = 1.5;
		double SIN_DISTANCE = 20.0; //ideal distance for one sinus curve;
        public void Render(float partialTicks) {
            GL11.glPushMatrix();//glstart
            if (textureManager == null)textureManager = Minecraft.getInstance().getTextureManager();
            textureManager.bind(texture);

            GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager._pushMatrix();
			//System.out.println("开渲");
			Minecraft mc = Minecraft.getInstance();
			ActiveRenderInfo activeRenderInfoIn = Minecraft.getInstance().getEntityRenderDispatcher().camera;
			
			Vector3d camera = activeRenderInfoIn.getPosition();
			double x = camera.x();
            double y = camera.y()-1.5;
            double z = camera.z();
			
			activeRenderInfoIn.setup(mc.level, (Entity)(mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()), 
			!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(), partialTicks);
			net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
			activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
			GlStateManager._rotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
			GlStateManager._rotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
			GlStateManager._rotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
			
            //Get the camera frustrum for clipping
            /*Entity camera = Minecraft.getInstance().getCameraEntity();
            double x = camera.xOld + (camera.getX() - camera.xOld) * partialTicks;
            double y = camera.yOld + (camera.getY() - camera.yOld) * partialTicks;
            double z = camera.zOld + (camera.getZ() - camera.zOld) * partialTicks;*/

            GL11.glTranslatef(-(float) x, -(float) y, -(float) z);

            float parametric = ((float) (ticksExisted) + partialTicks) * bulletSpeed;
			
            Vector3f dPos = Vector3f.sub(hitPos, origin, null);
            dPos.normalise();

            float startParametric = parametric - length * 0.25f;
            Vector3f startPos = new Vector3f(origin.x + dPos.x * startParametric, origin.y + dPos.y * startParametric, origin.z + dPos.z * startParametric);
            float endParametric = parametric + length * 0.25f;
            Vector3f endPos = new Vector3f(origin.x + dPos.x * endParametric, origin.y + dPos.y * endParametric, origin.z + dPos.z * endParametric);

            dPos.normalise();
            if(bulletmodel!=null||type == 3||type == 6) {
                GlStateManager._pushMatrix();
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//ADDITIVE
				float size = partialTicks * 0.5F + 1;//0.4F
				float size1 = length * 1.25f;
				float size2 = (parametric *powersize) / (20F * bulletSpeed);
				float size3 = partialTicks * 0.8F + 1;//0.4F
				if(type>1){
					size1 = this.distanceToTarget * 1f;
					if(this.distanceToTarget!=0)GlStateManager._color4f(1F, 1F, 1F, (maxtime-parametric/bulletSpeed) / maxtime);
				}
                Vector3f dVec =new Vector3f(hitPos.x-origin.x,0,hitPos.z-origin.z);
                dVec=(Vector3f)dVec.normalise();
                float yaw=(float)Math.acos(dVec.z)/3.1415f*180;
                if(dVec.x<0)yaw=-yaw;
                dVec =new Vector3f(hitPos.x-origin.x,hitPos.y-origin.y,hitPos.z-origin.z);
                dVec=(Vector3f)dVec.normalise();
                float pitch=(float)Math.asin(dVec.y)/3.1415f*180;
				
				GlStateManager._disableCull();//对象剔除
				GlStateManager._depthMask(false);//遮罩
				
				if(type==4||type==7){
					float r = 1;
					float g = 1;
					float b = 1;
					if(type==7){
						r=1F;
						g=0.5F;
						b=0;
					}
					GlStateManager._pushMatrix();
					setupGlintTexturing(3F);
					GlStateManager._translatef(hitPos.x, hitPos.y, hitPos.z);
					textureManager.bind(new ResourceLocation("wmlib:textures/entity/flash/power1.png"));
					if(parametric/bulletSpeed<=maxtime/2){
						GlStateManager._color4f(r, g, b, (1+parametric/bulletSpeed) * 2 / maxtime);
					}else{
						GlStateManager._color4f(r, g, b, (maxtime-(1+parametric/bulletSpeed)) / maxtime);
					}
					GlStateManager._pushMatrix();
					GlStateManager._scalef(size2, size2, size2);
					powermodel.renderPart("mat1");
					textureManager.bind(new ResourceLocation("wmlib:textures/entity/flash/power3.png"));
					powermodel.renderPart("mat3");
					GlStateManager._popMatrix();
					GlStateManager._color4f(r, g, b, (maxtime-parametric/bulletSpeed) / maxtime);
					GlStateManager._pushMatrix();
					RenderSystem.matrixMode(5890);
					RenderSystem.popMatrix();
					RenderSystem.matrixMode(5888);
					
					GlStateManager._rotatef(time * 5, 0.0F, 1.0F, 0.0F);
					textureManager.bind(new ResourceLocation("wmlib:textures/entity/flash/power2.png"));
					GlStateManager._scalef(size2*2F, size2*2F, size2*2F);
					powermodel.renderPart("mat2");
					
					GlStateManager._popMatrix();
					
					GlStateManager._popMatrix();
					for (int i = 0; i < 12; i++) {	
						GlStateManager._pushMatrix();
						GlStateManager._translatef(origin.x + dPos.x * this.distanceToTarget * i/12F, origin.y + dPos.y * this.distanceToTarget * i/12F, origin.z + dPos.z * this.distanceToTarget * i/12F);
						GlStateManager._rotatef(yaw, 0, 1, 0);
						GlStateManager._rotatef(pitch, -1, 0, 0);
						GlStateManager._rotatef(time * 10, 0.0F, 0.0F, 1.0F);
						GlStateManager._scalef(size2*0.8F, size2*0.8F, size2*0.8F);
						powermodel.renderPart("rote");
						GlStateManager._popMatrix();
					}
					GlStateManager._color4f(1F, 1F, 1F, (maxtime-parametric*1.2F/bulletSpeed) / maxtime);
				}
				
				GlStateManager._enableRescaleNormal();
				
				if(type>1){
					GlStateManager._translatef(origin.x, origin.y, origin.z);
				}else{
					GlStateManager._translatef(endPos.x, endPos.y, endPos.z);
				}
                GlStateManager._rotatef(yaw, 0, 1, 0);
                GlStateManager._rotatef(pitch, -1, 0, 0);
				++time;
				if(bulletmodel != null && time > 1){
					{
						GlStateManager._pushMatrix();
						textureManager.bind(new ResourceLocation(this.bullettex));
						
						GlStateManager._pushMatrix();
						GlStateManager._scalef(size2*1.2F, size2*1.2F, size2*1.2F);
						GlStateManager._rotatef(time * 10, 0.0F, 0.0F, 1.0F);
						bulletmodel.renderPart("start");
						GlStateManager._popMatrix();
						
						GlStateManager._scalef(size*bulletSpeed, size*bulletSpeed, size1);
						bulletmodel.renderPart("trail");
						if(type==2||type==4||type==5||type==7){
							if(type!=5)GlStateManager._rotatef(time * 10, 0.0F, 0.0F, 1.0F);
							if(type==5){
								RenderSystem.matrixMode(5890);
								RenderSystem.pushMatrix();
								RenderSystem.loadIdentity();
								RenderSystem.translatef(time*0.005F, time*0.005F, 0.0F);
								RenderSystem.matrixMode(5888);
							}
							bulletmodel.renderPart("beam1");
							if(type==5){
								RenderSystem.matrixMode(5890);
								RenderSystem.popMatrix();
								RenderSystem.matrixMode(5888);
							}
							GlStateManager._color4f(1F, 1F, 1F, 1F);
						}
						GlStateManager._depthMask(true);
						GlStateManager._enableCull();
						GlStateManager._disableRescaleNormal();	
						SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//ALPHA
						GlStateManager._popMatrix();
					}
					{
						GlStateManager._pushMatrix();
						{
							GlStateManager._rotatef(time * 10, 0.0F, 0.0F, 1.0F);
							bulletmodel.renderPart("bullet");
						}
						bulletmodel.renderPart("mat1");
						GlStateManager._popMatrix();
					}
					{
						{
							GlStateManager._rotatef(time * 10, 0.0F, 0.0F, 1.0F);
							GlStateManager._scalef(size3, size3, size3);
							SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//ALPHA
							bulletmodel.renderPart("bullet_e_1");
							SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//ADDITIVE
						}
					}
				}
				GlStateManager._depthMask(true);
				GlStateManager._enableCull();
				GlStateManager._disableRescaleNormal();	
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//ALPHA
				if(type==3||type == 6){
					textureManager.bind(new ResourceLocation("wmlib:textures/entity/flash/tesla.png"));
					if(type == 6){
						SIN_COUNT=2;
						textureManager.bind(new ResourceLocation("wmlib:textures/entity/flash/tesla2.png"));
					}
					GlStateManager._depthMask(false);//遮罩
					GlStateManager._disableCull();//对象剔除
					GlStateManager._pushMatrix();
					SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//ADDITIVE
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
							drawSegment(xprev, yprev, zprev, x1,y1,z1, widthprev, width, (100F-time*rand.nextFloat()) / 100F, pulse);
						}
						widthprev = width;
						alphaprev = pulse;
						xprev = x1;
						yprev = y1;
						zprev = z1;
					}
					SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//ALPHA
					GlStateManager._enableCull();
					GlStateManager._depthMask(true);
					GlStateManager._popMatrix();
				}
                GlStateManager._popMatrix();
            }
            //GL11.glDisable(3042);
            //GL11.glDisable(2832);
            //GlStateManager._disableRescaleNormal();
            GlStateManager._popMatrix();
			GL11.glPopMatrix();//glend
        }
    }
	
	static void drawSegment(double x1, double y1, double z1, double x2, double y2, double z2, double width1, double width2, double a1, double a2) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
		double scale = 0.5;
		y1*=scale;
		y2*=scale;
		z1*=scale;
		z2*=scale;
		width1*=scale;
		width2*=scale;
		GlStateManager._pushMatrix();
		GlStateManager._rotatef(-90F, 0, 1, 0);
		GlStateManager._rotatef(45.0f, 1.0f, 0.0f, 0.0f);
		//set alpha1
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);			
		bufferbuilder.vertex(x1,  y1- width1,  z1).color(1.0f, 1.0f, 1.0f, (float)a1).overlayCoords(0, 240).normal(0.0f, 1.0f, 0.0f).uv(0, 0).endVertex();
		bufferbuilder.vertex(x1,  y1+ width1,  z1).color(1.0f, 1.0f, 1.0f, (float)a1).overlayCoords(0, 240).normal(0.0f, 1.0f, 0.0f).uv(0, 1).endVertex();
		bufferbuilder.vertex(x2,  y2+ width2,  z2).color(1.0f, 1.0f, 1.0f, (float)a1).overlayCoords(0, 240).normal(0.0f, 1.0f, 0.0f).uv(1, 1).endVertex();
		bufferbuilder.vertex(x2,  y2- width2,  z2).color(1.0f, 1.0f, 1.0f, (float)a1).overlayCoords(0, 240).normal(0.0f, 1.0f, 0.0f).uv(1, 0).endVertex();
		Tessellator.getInstance().end();
		//--
		//set alpha2	
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);			
		bufferbuilder.vertex(x1,  y1, z1-width1).color(1.0f, 1.0f, 1.0f, (float)a1).overlayCoords(0, 240).normal(0.0f, 1.0f, 0.0f).uv(0, 0).endVertex();
		bufferbuilder.vertex(x1,  y1, z1+width1).color(1.0f, 1.0f, 1.0f, (float)a1).overlayCoords(0, 240).normal(0.0f, 1.0f, 0.0f).uv(0, 1).endVertex();
		bufferbuilder.vertex(x2,  y2, z2+width2).color(1.0f, 1.0f, 1.0f, (float)a1).overlayCoords(0, 240).normal(0.0f, 1.0f, 0.0f).uv(1, 1).endVertex();
		bufferbuilder.vertex(x2,  y2, z2-width2).color(1.0f, 1.0f, 1.0f, (float)a1).overlayCoords(0, 240).normal(0.0f, 1.0f, 0.0f).uv(1, 0).endVertex();
		Tessellator.getInstance().end();
		GlStateManager._popMatrix();
	}
}
package wmlib.client;

import java.awt.Color;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.ModList;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import wmlib.client.obj.SAObjModel;
import wmlib.api.IRaderItem;
import wmlib.common.bullet.EntityMissile;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import wmlib.api.IArmy;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.EntityWMVehicleBase;

import advancearmy.entity.map.CreatureRespawn;
import advancearmy.entity.map.SupportPoint;
import advancearmy.entity.map.DefencePoint;

import wmlib.WMConfig;
public class OverlayHandler
{
	private static final ResourceLocation aim = new ResourceLocation("wmlib:textures/hud/tank.png");
	private static final SAObjModel icon = new SAObjModel("wmlib:textures/hud/icon.obj");
	private static final SAObjModel box = new SAObjModel("wmlib:textures/hud/box.obj");
	private static final ResourceLocation boxtex = new ResourceLocation("wmlib:textures/hud/box.png");
	private static final ResourceLocation nighttex = new ResourceLocation("wmlib:textures/hud/night.png");
	
	private static final SAObjModel rader = new SAObjModel("wmlib:textures/hud/rader.obj");
	private static final ResourceLocation radert = new ResourceLocation("wmlib:textures/hud/rader.png");
	private static final ResourceLocation serch = new ResourceLocation("wmlib:textures/hud/hou2.png");
	private static final ResourceLocation serch1 = new ResourceLocation("wmlib:textures/hud/serch.png");
	
	private static final ResourceLocation tex = new ResourceLocation("wmlib:textures/hud/count.png");
	private static final SAObjModel unit = new SAObjModel("wmlib:textures/hud/unit.obj");
	private static final SAObjModel digit = new SAObjModel("wmlib:textures/hud/digit.obj");
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/hud/count.obj");
	private static final SAObjModel obj2 = new SAObjModel("wmlib:textures/hud/count2.obj");
	private static final SAObjModel obj3 = new SAObjModel("wmlib:textures/hud/count3.obj");
	
	private static final SAObjModel hudw = new SAObjModel("wmlib:textures/hud/vehicle.obj");
	private static final ResourceLocation white = new ResourceLocation("wmlib:textures/hud/white.png");
	private static final ResourceLocation white2 = new ResourceLocation("wmlib:textures/hud/white2.png");
	private static final ResourceLocation grey = new ResourceLocation("wmlib:textures/hud/grey.png");
	private static final ResourceLocation cloud = new ResourceLocation("wmlib:textures/hud/cloud.png");
	private static final ResourceLocation repair = new ResourceLocation("wmlib:textures/hud/repair.png");
	
	private static final SAObjModel hudh = new SAObjModel("wmlib:textures/hud/hud.obj");
	private static final SAObjModel hudf = new SAObjModel("wmlib:textures/hud/hud2.obj");
	
	private static final ResourceLocation sp = new ResourceLocation("wmlib:textures/hud/sp.png");
	private static final ResourceLocation cz = new ResourceLocation("wmlib:textures/hud/cz.png");
	
	private static final ResourceLocation hudtex = new ResourceLocation("wmlib:textures/hud/hud.png");
	private static final ResourceLocation hud256 = new ResourceLocation("wmlib:textures/hud/256.png");
	private static final SAObjModel health = new SAObjModel("wmlib:textures/marker/teamhealth.obj");
	
	private static final ResourceLocation color = new ResourceLocation("wmlib:textures/marker/color.png");
	
	/*@SubscribeEvent
	public void rendertip(RenderTooltipEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity entityplayer = mc.player;
		if(entityplayer != null){
			if (entityplayer.getVehicle() instanceof EntityWMSeat && entityplayer.getVehicle() != null) {
				event.setCanceled(true);
			}
		}
	}*/
	@SubscribeEvent
	public void renderhand(RenderHandEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity entityplayer = mc.player;
		if(entityplayer != null){
			if (entityplayer.getVehicle() instanceof EntityWMSeat && entityplayer.getVehicle() != null) {
				event.setCanceled(true);
			}
		}
	}
	@SubscribeEvent
	public void renderarm(RenderArmEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity entityplayer = mc.player;
		if(entityplayer != null){
			if (entityplayer.getVehicle() instanceof EntityWMSeat && entityplayer.getVehicle() != null) {
				event.setCanceled(true);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onEvent1(RenderGameOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity entityplayer = mc.player;
		if(entityplayer != null && (event.getType()==ElementType.HELMET||event.getType()==ElementType.ARMOR||event.getType()==ElementType.FOOD||
		event.getType()==ElementType.EXPERIENCE||event.getType()==ElementType.HEALTH||event.getType()==ElementType.HEALTHMOUNT||event.getType()==ElementType.HOTBAR||event.getType()==ElementType.AIR)){
			if (entityplayer.getVehicle() instanceof EntityWMSeat && entityplayer.getVehicle() != null) {
				event.setCanceled(true);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	protected void renderHud(int i, int j, String texture, float partialTicks) {
		RenderSystem.pushMatrix();
		float gunRotX = RenderParameters.GUN_ROT_X_LAST + (RenderParameters.GUN_ROT_X - RenderParameters.GUN_ROT_X_LAST) * partialTicks;
		float gunRotY = RenderParameters.GUN_ROT_Y_LAST + (RenderParameters.GUN_ROT_Y - RenderParameters.GUN_ROT_Y_LAST) * partialTicks;
		//float jump = -RenderParameters.playerRecoilPitch*5*0.08F;
		//float cock = 5*RenderParameters.playerRecoilPitch*0.2F;
		RenderSystem.rotatef(gunRotX*2F, 0, -1, 0);
		RenderSystem.rotatef(gunRotY*2F, 0, 0, -1);
		GL11.glTranslatef(gunRotX*0.1F, 0.0F, 0);
		GL11.glTranslatef(0.0F, gunRotY, 0);
		//GL11.glTranslatef(0.0F, 0.0F, cock);
		//RenderSystem.rotatef(jump, 1.0F, 0.0F, 0.0F);
		
		RenderSystem.enableBlend();
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableAlphaTest();
		mc.getTextureManager().bind(new ResourceLocation(texture));
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.vertex(0.0D, (double)j, -90.0D).uv(0.0F, 1.0F).endVertex();
		bufferbuilder.vertex((double)i, (double)j, -90.0D).uv(1.0F, 1.0F).endVertex();
		bufferbuilder.vertex((double)i, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
		bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
		tessellator.end();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableAlphaTest();
		RenderSystem.popMatrix();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void render_count(Minecraft mc, float i, float j, int x, int y, int count, boolean un, int id, float color) {
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		float size = 1F;
		float size1 = 1F;
		RenderSystem.pushMatrix();
		mc.getTextureManager().bind(tex);
		//GL11.glDisable(GL11.GL_CULL_FACE);
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		GlStateManager._translatef((float)(i - x), (float)(j - y), 15);
		if(id==1){
			size = 1.5F;
			size1 = 1.1F;
		}
		if(id==2){//玩家绿
			GL11.glColor4f(0F+(1-color), 1F-(1-color), 0F, 1);
		}
		if(id==4||id==5){
			if(color>0.7){
				GL11.glColor4f(0F, 0.8F, 1F, 1);
			}else if(color>0.4){
				GL11.glColor4f(0F+(1.2F-color), 0.8F, 0F, 1);
			}else{
				GL11.glColor4f(0.5F+color*0.5F, 0.1F, 0F, 1);
			}
		}
		if(id==6||id==7){
			if(color>0.4){
				GL11.glColor4f(0.8F, 1F, 1F, 1);
			}else{
				GL11.glColor4f(0.8F+(1-color), 1F, 1F, 1);
			}
		}
		GL11.glScalef(15F*(i1/427F)*size, 15F*(j1/240F)*size, 15F);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		String c = String.valueOf(count);
		int num = count;
		int shiwei=0,baiwei=0,qianwei=0,gewei=0;
		qianwei = num / 1000;
		baiwei = (num % 1000) / 100;
		shiwei = (num / 10 ) % 10;
		gewei = (num %100) % 10;
		//System.out.println(qianwei + ":" + baiwei + ":" + shiwei + ":" + gewei);
		String t1 = String.valueOf(gewei);
		String t2 = String.valueOf(shiwei);
		String t3 = String.valueOf(baiwei);
		String t4 = String.valueOf(qianwei);
		
		RenderSystem.enableBlend();
		SAObjModel model = digit;
		//if(id==1)model = obj;
		//if(id==2)model = obj2;
		//if(id==3)model = obj3;
		if(model!=null){
			if(un){
				model.renderPart("un");//
			}else{
				if(count<10){
					if(id==1){
						GL11.glColor4f(0F+(1-color), 0.8F-(1-color), 1F-(1-color), 0.2F);
						model.renderPart("zero");//
						GlStateManager._translatef(0.4F*size1,0,0);
						model.renderPart("zero");//
						GlStateManager._translatef(0.4F*size1,0,0);
						if(color>0.4){
							GL11.glColor4f(0.8F, 1F, 1F, 1);
						}else{
							GL11.glColor4f(0.8F+(1-color), 1F, 1F, 1);
						}
					}
					model.renderPart("obj" + t1);//
				}else if(count<100)
				{
					if(id==1){
						GL11.glColor4f(0F+(1-color), 0.8F-(1-color), 1F-(1-color), 0.2F);
						model.renderPart("zero");//
						GlStateManager._translatef(0.4F*size1,0,0);
						if(color>0.4){
							GL11.glColor4f(0.8F, 1F, 1F, 1);
						}else{
							GL11.glColor4f(0.8F+(1-color), 1F, 1F, 1);
						}
					}
					model.renderPart("obj" + t2);//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t1);//
				}else if(count<1000)
				{
					model.renderPart("obj" + t3);//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t2);//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t1);//
				}else if(count<10000)
				{
					model.renderPart("obj" + t4);//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t3);//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t2);//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj" + t1);//
				}else{
					model.renderPart("obj9");//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj9");//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj9");//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("obj9");//
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("add");//+
				}
				if(id == 0||id == 2){
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("rate");//+
				}
				if(id == 4){
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("alt");//+
				}
				if(id == 5||id == 6){
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("kph");//+
				}
				if(id == 7){
					GlStateManager._translatef(0.4F*size1,0,0);
					model.renderPart("angle");//+
				}
			}
		}
		//RenderSystem.disableBlend();
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.popMatrix();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderNightV(Minecraft mc, int i, int j) {
		mc.getTextureManager().bind(nighttex);
		RenderSystem.pushMatrix();
		GlStateManager._translatef((float)(i / 2), (float)(j / 2), 5);
		GL11.glScalef(15F*(i/427F), 15F*(j/240F), 15F);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		//GL11.glColor4f(1, 1F, 1F, 0.3F);
		
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(
			GlStateManager.SourceFactor.DST_COLOR, 
			GlStateManager.DestFactor.SRC_COLOR, 
			GlStateManager.SourceFactor.ONE, 
			GlStateManager.DestFactor.ZERO
		);
		
		//SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		RenderSystem.depthMask(false);
		box.renderPart("night");
		RenderSystem.depthMask(true);
		//SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		GL11.glColor4f(1, 1F, 1F, 1F);
		RenderSystem.popMatrix();
	}
	
	protected void renderUnit(float i, float j, String texture, Entity entity1, PlayerEntity player, float health, ResourceLocation icon1tex, ResourceLocation icon2tex, boolean select, int count)
	{
		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		Minecraft mc = Minecraft.getInstance();
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();
		float size = 1F;
		{
			RenderSystem.pushMatrix();
			GlStateManager._translatef(i, j, 5);
			GL11.glScalef(15F*(i1/427F)*size, 15F*(j1/240F)*size, 15F*size);
			GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
			GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);

			if(icon1tex!=null){
				mc.getTextureManager().bind(icon1tex);
				unit.renderPart("tank2");
			}
			if(icon2tex!=null){
				RenderSystem.pushMatrix();
				mc.getTextureManager().bind(icon2tex);
				unit.renderPart("tank1");
				RenderSystem.popMatrix();
			}
			mc.getTextureManager().bind(tex);
			
			RenderSystem.pushMatrix();
			if(count>0){
				String c = String.valueOf(count);
				int num = count;
				int gewei=0;
				gewei = (num %100) % 10;
				String t1 = String.valueOf(gewei);
				if(count<10){
					unit.renderPart("obj" + t1);//
				}
			}
			RenderSystem.popMatrix();
			if(select)unit.renderPart("choose");
			if(health>0.7){
				GL11.glColor4f(0F, 0.8F, 1F, 1);
			}else if(health>0.4){
				GL11.glColor4f(0F+(1.2F-health), 0.8F, 0F, 1);
			}else{
				GL11.glColor4f(0.5F+health*0.5F, 0.1F, 0F, 1);
			}
			RenderSystem.pushMatrix();
			//GlStateManager._translatef(9.35F, 0F, 0F);
			GlStateManager._scalef(health, 1, 1);
			//GlStateManager._translatef(-9.35F, 0F, 0F);
			unit.renderPart("health");
			RenderSystem.popMatrix();
			unit.renderPart("base");
			RenderSystem.popMatrix();
		}
		GL11.glColor4f(1, 1F, 1F, 1F);
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableDepthTest();
		RenderSystem.popMatrix();
	}
	
	protected void renderRadarTarget(int i, int j, String texture, Entity entity1, PlayerEntity player, int type, int ai, ResourceLocation icon1tex, ResourceLocation icon2tex, float rhead)
	{	
		int colorid = 0;
		if(entity1!=null){
			if(entity1 instanceof TameableEntity){
				TameableEntity soldier = (TameableEntity)entity1;
				if(player==soldier.getOwner()){
					colorid = 1;
				}else{
					if(player.getTeam()!=null&&player.getTeam()==entity1.getTeam()/*||entity1 instanceof IArmy && entity1.getTeam()==null && player.getTeam()==null*/){
						colorid = 2;
					}else{
						if(entity1 instanceof IMob||entity1.getTeam()!=null && entity1.getTeam()!=player.getTeam()){
							colorid = 3;
						}else{
							colorid = 0;
						}
					}
				}
			}else{
				if(player.getTeam()!=null&&player.getTeam()==entity1.getTeam()){
					colorid = 2;
				}else{
					if(entity1 instanceof IMob||entity1.getTeam()!=null && entity1.getTeam()!=player.getTeam()){
						colorid = 3;
					}else{
						colorid = 0;
					}
				}
			}
		}else{
			colorid = 0;
		}

		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();
		
		if(colorid==1)GL11.glColor4f(1, 1F, 1F, 0.9F);
		if(colorid==1)GL11.glColor4f(0.6F, 1, 0.6F, 0.9F);
        if(colorid==2)GL11.glColor4f(0, 0.8F, 1F, 0.9F);
		if(colorid==3)GL11.glColor4f(1, 0.3F, 0.3F, 0.9F);
		if(ai==2)GL11.glColor4f(1, 0.3F, 0.3F, 0.9F);
		if(ai==3 && colorid!=1)GL11.glColor4f(0, 0.8F, 1F, 0.9F);

		double dx = (player.getX() - entity1.getX());
		double dz = (player.getZ() - entity1.getZ());
		double distance = Math.sqrt(dx * dx + dz * dz);
		double radius = 65*(i/427F)*0.8F;
		{
			if (distance > radius) {
				double scale = radius / distance;
				dx *= scale*0.8F;
				dz *= scale*0.8F;
			}
			RenderSystem.pushMatrix();
			////GL11.glDisable(GL11.GL_CULL_FACE);
			GlStateManager._translatef((float)(i *0.2F*0.74F)-(float)dx, (float)(j *0.74F)-(float)dz, 5);
			GL11.glScalef(15F*(i/427F)*0.8F, 15F*(j/240F)*0.8F, 15F*0.8F);
			
			
			GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
			//GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
			RenderSystem.rotatef(-entity1.yRot, 0.0F, 0.0F, 1.0F);
			if(icon1tex!=null){
				mc.getTextureManager().bind(icon1tex);
				rader.renderPart("tank2");
			}
			if(icon2tex!=null){
				RenderSystem.pushMatrix();
				RenderSystem.rotatef((rhead-entity1.yRot), 0.0F, 0.0F, 1.0F);
				mc.getTextureManager().bind(icon2tex);
				rader.renderPart("tank1");
				RenderSystem.popMatrix();
			}
			if(icon1tex==null && icon2tex==null){
				mc.getTextureManager().bind(tex);
				if(type==0)rader.renderPart("man");
				if(type==1)rader.renderPart("tank");
				if(type==2)rader.renderPart("heli");
				if(type==3)rader.renderPart("plane");
				if(type==4)rader.renderPart("flag");
				if(type==5)rader.renderPart("missile");
				if(type==6)rader.renderPart("support1");
				if(type==7)rader.renderPart("support2");
				if(type==8)rader.renderPart("defend");
			}
			RenderSystem.popMatrix();
		}
		GL11.glColor4f(1, 1F, 1F, 1F);
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableDepthTest();
		RenderSystem.popMatrix();
	}
	 
	int raderrote = 0;
	@OnlyIn(Dist.CLIENT)
	protected void renderRader(int i, int j, PlayerEntity player) {
		if(raderrote<360){
			++raderrote;
		}else{
			raderrote = 0;
		}
		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		float size1 = (float)(player.getHealth()/player.getMaxHealth());
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.8F);
		RenderSystem.disableAlphaTest();
		RenderSystem.pushMatrix();
		////GL11.glDisable(GL11.GL_CULL_FACE);
		GlStateManager._translatef((float)(i *0.2F*0.74F), (float)(j *0.74F), 5);
		GL11.glScalef(15F*(i/427F)*0.8F, 15F*(j/240F)*0.8F, 15F*0.8F);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		mc.getTextureManager().bind(tex);
		rader.renderPart("direct");
		mc.getTextureManager().bind(radert);
		rader.renderPart("base");
		RenderSystem.pushMatrix();
		RenderSystem.rotatef(raderrote, 0.0F, 0.0F, 1.0F);
		mc.getTextureManager().bind(serch1);
		rader.renderPart("serch");
		RenderSystem.popMatrix();
		GlStateManager._rotatef(-180F, 0.0F, 0.0F, 1.0F);
		RenderSystem.rotatef(-player.yRot, 0.0F, 0.0F, 1.0F);
		mc.getTextureManager().bind(serch);
		rader.renderPart("look");
		RenderSystem.popMatrix();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableAlphaTest();
		RenderSystem.popMatrix();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderTankRote(int i, int j, EntityWMVehicleBase entity, PlayerEntity player) {
		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		float size = (float)(entity.getHealth()/entity.getMaxHealth());
		float size1 = (float)(player.getHealth()/player.getMaxHealth());
		Minecraft mc = Minecraft.getInstance();
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		if(size>0.7){
			GL11.glColor4f(0F, 0.8F, 1F, 1);
		}else if(size>0.4){
			GL11.glColor4f(0F+(1.2F-size), 0.8F, 0F, 1);
		}else{
			GL11.glColor4f(0.5F+size*0.5F, 0.1F, 0F, 1);
		}
		RenderSystem.pushMatrix();
		GlStateManager._translatef((float)(i / 2), (float)(j *8/9), 5);
		GL11.glScalef(15F*(i/427F), 15F*(j/240F), 15F);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		if(entity.icon1tex!=null){
			mc.getTextureManager().bind(entity.icon1tex);
			icon.renderPart("tank1");
		}
		GL11.glColor4f(0F+(1-size1), 1F-(1-size1), 0F, 1);
		mc.getTextureManager().bind(new ResourceLocation("wmlib:textures/hud/player_health.png"));
		icon.renderPart("ph");
		if(size>0.7){
			GL11.glColor4f(0F, 0.8F, 1F, 1);
		}else if(size>0.4){
			GL11.glColor4f(0F+(1.2F-size), 0.8F, 0F, 1);
		}else{
			GL11.glColor4f(0.5F+size*0.5F, 0.1F, 0F, 1);
		}
		
		mc.getTextureManager().bind(new ResourceLocation("wmlib:textures/hud/vehicle_health.png"));
		icon.renderPart("vh");
		if(entity.icon2tex!=null){
			RenderSystem.rotatef((entity.turretYaw-entity.yRot), 0.0F, 0.0F, 1.0F);
			mc.getTextureManager().bind(entity.icon2tex);
			icon.renderPart("tank2");
		}
		RenderSystem.popMatrix();
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.popMatrix();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderFighterHud(int i, int j, String texture, EntityWMVehicleBase entity, float rote) {
		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		
		Minecraft mc = Minecraft.getInstance();
		/*RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();*/
		GL11.glColor4f(0F, 0.8F, 1F, 1);
		mc.getTextureManager().bind(hudtex);
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		RenderSystem.pushMatrix();
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GlStateManager._translatef((float)(i / 2), (float)(j / 2), 5);
		GL11.glScalef(15F*(i/427F), 15F*(j/240F), 15F);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		if(entity.is_aa){
			hudf.renderPart("bar");
		}else{
			hudf.renderPart("bar2");
		}
		RenderSystem.pushMatrix();
		
		RenderSystem.pushMatrix();
		GlStateManager._translatef(0, -2F, 0);
		hudf.renderPart("bar1");
		if(entity.getMoveMode()==0)hudf.renderPart("stay");
		RenderSystem.pushMatrix();
		float size = ((float)entity.movePower)/entity.throttleMax;
		GlStateManager._scalef(1, size, 1);
		hudf.renderPart("power1");
		RenderSystem.popMatrix();
		
		RenderSystem.pushMatrix();
		float size2 = ((float)entity.throttle)/entity.throttleMax;
		GlStateManager._translatef(0, size2*5F, 0);
		hudf.renderPart("throttle1");
		RenderSystem.popMatrix();
		RenderSystem.popMatrix();
		
		mc.getTextureManager().bind(sp);
		GlStateManager._pushMatrix();
		RenderSystem.matrixMode(5890);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		RenderSystem.translatef(rote*0.0025F, 0, 0);
		RenderSystem.matrixMode(5888);
		hudf.renderPart("sp");
		RenderSystem.matrixMode(5890);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
		mc.getTextureManager().bind(hudtex);
		RenderSystem.rotatef(entity.flyRoll, 0.0F, 0.0F, 1.0F);
		if(!entity.is_aa)hudf.renderPart("sp2");
		mc.getTextureManager().bind(cz);
		RenderSystem.matrixMode(5890);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		RenderSystem.translatef(0, entity.flyPitch*0.0075F, 0);
		RenderSystem.matrixMode(5888);
		hudf.renderPart("cz");
		RenderSystem.matrixMode(5890);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
		GlStateManager._popMatrix();
		mc.getTextureManager().bind(hudtex);
		
		RenderSystem.popMatrix();
		RenderSystem.pushMatrix();
		hudf.renderPart("aim");
		//GlStateManager._translatef(0, -entity.flyPitch*0.02F, 0F);
		//hudf.renderPart("up");
		RenderSystem.popMatrix();
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.popMatrix();
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		/*RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableAlphaTest();
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();
		RenderSystem.enableAlphaTest();*/
		GL11.glEnable(GL11.GL_CULL_FACE);
		RenderSystem.popMatrix();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderBlack(int i, int j, float time) {
		SARenderHelper.enableBlendMode(RenderTypeSA.ALPHA);//
		GL11.glDisable(GL11.GL_LIGHTING);
		RenderSystem.pushMatrix();
		//RenderSystem.enableBlend();
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, time);
		mc.getTextureManager().bind(boxtex);
		GlStateManager._translatef((float)(i / 2), (float)(j / 2), 5);
		GL11.glScalef(15F*(i/427F), 15F*(j/240F), 15F);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		box.renderPart("black");
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//RenderSystem.disableBlend();
		RenderSystem.popMatrix();
		
		SARenderHelper.disableBlendMode(RenderTypeSA.ALPHA);//
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderHeliHud(int i, int j, String texture, EntityWMVehicleBase entity, float rote) {
		RenderSystem.pushMatrix();
		Minecraft mc = Minecraft.getInstance();
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		/*RenderSystem.enableBlend();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableAlphaTest();*/
		//GL11.glColor4f(0.9F, 0.8F, 0F, 1);
		GL11.glColor4f(0F, 0.8F, 1F, 1);
		mc.getTextureManager().bind(hudtex);

		////GL11.glDisable(GL11.GL_CULL_FACE);
		GlStateManager._translatef((float)(i / 2), (float)(j / 2), 5);
		GL11.glScalef(15F*(i/427F), 15F*(j/240F), 15F);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		
		RenderSystem.pushMatrix();
		
		RenderSystem.pushMatrix();
		RenderSystem.rotatef(entity.flyRoll, 0.0F, 0.0F, 0.8F);
		hudh.renderPart("sp1");
		RenderSystem.popMatrix();
		
		RenderSystem.pushMatrix();
		hudh.renderPart("bar1");
		GlStateManager._translatef(0, -2.5F, 0.6F);
		if(entity.movePower>entity.throttleMax*0.6F){
			hudh.renderPart("up");
		}else if(entity.movePower<=entity.throttleMax*0.45F){
			hudh.renderPart("down");
		}else{
			hudh.renderPart("m");
		}
		
		hudh.renderPart("bar");
		RenderSystem.pushMatrix();
		float size = ((float)entity.movePower)/entity.throttleMax;
		GlStateManager._scalef(1, size, 1);
		hudh.renderPart("power1");
		RenderSystem.popMatrix();
		
		RenderSystem.pushMatrix();
		float size2 = ((float)entity.throttle)/entity.throttleMax;
		GlStateManager._scalef(1, size2, 1);
		hudh.renderPart("throttle1");
		RenderSystem.popMatrix();
		if(entity.getMoveMode()==1)hudh.renderPart("hower");
		RenderSystem.popMatrix();
		
		RenderSystem.pushMatrix();
		
		mc.getTextureManager().bind(sp);
		GlStateManager._pushMatrix();

		RenderSystem.matrixMode(5890);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		RenderSystem.translatef(rote*0.0025F, 0, 0);
		RenderSystem.matrixMode(5888);
		hudh.renderPart("sp");
		RenderSystem.matrixMode(5890);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
		
		RenderSystem.pushMatrix();
		RenderSystem.rotatef(entity.flyRoll, 0.0F, 0.0F, 0.8F);
		mc.getTextureManager().bind(cz);
		RenderSystem.matrixMode(5890);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		RenderSystem.translatef(0, entity.flyPitch*0.0075F, 0);
		RenderSystem.matrixMode(5888);
		hudh.renderPart("cz");
		RenderSystem.matrixMode(5890);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
		RenderSystem.popMatrix();

		GlStateManager._popMatrix();
		mc.getTextureManager().bind(hudtex);
		
		RenderSystem.popMatrix();
		
		RenderSystem.pushMatrix();
		hudh.renderPart("aim");
		/*GlStateManager._translatef(0, -entity.flyPitch*0.02F, 0F);
		hudh.renderPart("bar2");*/
		RenderSystem.popMatrix();
		
		RenderSystem.popMatrix();
		
		/*RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);*/
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		RenderSystem.popMatrix();
	}
	
    private static float angleBetween(Vector2f first, Vector2f second) {
        double dot = first.x * second.x + first.y * second.y;
        double cross = first.x * second.y - second.x * first.y;
        double res = Math.atan2(cross, dot) * 180 / Math.PI;
        return (float)res;
    }
    private static Vector3d calculateViewVector(float pPitch, float pYaw) {
        float f = pPitch * ((float) Math.PI / 180F);
        float f1 = -pYaw * ((float) Math.PI / 180F);
        double f2 = Math.cos(f1);
        double f3 = Math.sin(f1);
        double f4 = Math.cos(f);
        double f5 = Math.sin(f);
        return new Vector3d(f3 * f4, -f5, f2 * f4);
    }
	
	public int mitargetX;
	public int mitargetY;
	public int mitargetZ;
	@OnlyIn(Dist.CLIENT)
	protected void renderBox(String objname, String texture, float partialTicks, Entity seat, Entity target, PlayerEntity player, boolean isAim, float rote, boolean haveMissile,float btime) {
		RenderSystem.enableBlend();
		Minecraft mc = Minecraft.getInstance();
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		SAObjModel obj = new SAObjModel(objname);
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableAlphaTest();

		mc.getTextureManager().bind(new ResourceLocation(texture));
		RenderSystem.pushMatrix();
		GlStateManager._translatef((float)(i1 / 2), (float)(j1 / 2), 5);
		GL11.glScalef(15F*(i1/427F), 15F*(j1/240F), 15F);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		float gunRotX = RenderParameters.GUN_ROT_X_LAST + (RenderParameters.GUN_ROT_X - RenderParameters.GUN_ROT_X_LAST) * partialTicks;
		float gunRotY = RenderParameters.GUN_ROT_Y_LAST + (RenderParameters.GUN_ROT_Y - RenderParameters.GUN_ROT_Y_LAST) * partialTicks;
		//float jump = -RenderParameters.playerRecoilPitch*5*0.08F;
		//float cock = 5*RenderParameters.playerRecoilPitch*0.2F;
		RenderSystem.rotatef(gunRotX*2F, 0, -1, 0);
		RenderSystem.rotatef(gunRotY*2F, 0, 0, -1);
		GL11.glTranslatef(gunRotX*0.1F, 0.0F, 0);
		GL11.glTranslatef(0.0F, gunRotY, 0);
		//GL11.glTranslatef(0.0F, 0.0F, cock);
		//RenderSystem.rotatef(jump, 1.0F, 0.0F, 0.0F);
		if(isAim){
			obj.renderPart("boxaim");
			obj.renderPart("aim2");
			if(haveMissile)obj.renderPart("missile2");
		}else{
			obj.renderPart("box");
			obj.renderPart("aim1");
			if(haveMissile)obj.renderPart("missile1");
		}
		
		float b1 = 25-btime;
		float b2 = 50-btime;
		float b3 = 75-btime;
		float b4 = 100-btime;
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, b1*0.04F);
		obj.renderPart("black1");
		//RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, b2*0.04F);
		obj.renderPart("black2");
		//RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, b3*0.04F);
		obj.renderPart("black3");
		//RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, b4*0.04F);
		obj.renderPart("black4");
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		
		
		mc.getTextureManager().bind(sp);
		GlStateManager._pushMatrix();
		RenderSystem.matrixMode(5890);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		RenderSystem.translatef(rote*0.0025F, 0, 0);
		RenderSystem.matrixMode(5888);
		obj.renderPart("sp");
		RenderSystem.matrixMode(5890);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
		GlStateManager._popMatrix();
		mc.getTextureManager().bind(new ResourceLocation(texture));
		
		obj.renderPart("info");
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.8F);
		obj.renderPart("hud");
		if(target!=null){
			obj.renderPart("lock");
			float d = seat.distanceTo(target);
			Vector3d locken = Vector3d.directionFromRotation(player.getRotationVector());//getLookAngle
			int ix = 0;
			int iy = 0;
			int iz = 0;
			for(int xxx = 0; xxx < d; ++xxx) {
				ix = (int) (seat.getX() + locken.x * xxx);
				iy = (int) (seat.getY() + locken.y * xxx);
				iz = (int) (seat.getZ() + locken.z * xxx);
				if(ix != 0 && iz != 0 && iy != 0) {
					mitargetX = ix;
					mitargetY = iy;
					mitargetZ = iz;
				}
			}
			Vector3d viewVec = calculateViewVector(0, player.getViewYRot(0));
			Vector2f lookVec = new Vector2f((float)viewVec.x, (float)viewVec.z);
			Vector2f playerPos = new Vector2f((float)mitargetX, (float)mitargetZ);
			Vector3d sourceVec3d = new Vector3d(target.getX(), target.getY(), target.getZ());
			Vector2f diff = new Vector2f((float)(sourceVec3d.x - playerPos.x), (float)(sourceVec3d.z - playerPos.y));
			float angleBetween = angleBetween(lookVec, diff);
			double d5 = target.getX() - mitargetX;
			double d6 = target.getY() - mitargetY;
			double d7 = target.getZ() - mitargetZ;
			float dis = MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
			/*if(d6<0)*/angleBetween = -angleBetween-180F;
			RenderSystem.rotatef(-angleBetween, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(0F, 0F, 0F);
			GL11.glScalef(1, dis, 1);
			GL11.glTranslatef(0F, 0F, 0F);
			obj.renderPart("line");
		}
		RenderSystem.popMatrix();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	
	
	@OnlyIn(Dist.CLIENT)
	protected void renderIcon(int i, int j, String texture, String name, float size, float alpha) {
		RenderSystem.enableBlend();
		//RenderSystem.pushMatrix();
		Minecraft mc = Minecraft.getInstance();
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
		RenderSystem.disableAlphaTest();

		mc.getTextureManager().bind(new ResourceLocation(texture));
		RenderSystem.pushMatrix();
		////GL11.glDisable(GL11.GL_CULL_FACE);
		GlStateManager._translatef((float)(i / 2), (float)(j / 2), 5);
		GL11.glScalef(15F*(i1/427F)*size, 15F*(j1/240F)*size, 15F*size);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		icon.renderPart(name);

		RenderSystem.popMatrix();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//RenderSystem.popMatrix();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderTeamCount(float i, float j, float id, float bar) {
		//RenderSystem.enableBlend();
		Minecraft mc = Minecraft.getInstance();
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.9F);
		RenderSystem.disableAlphaTest();

		RenderSystem.pushMatrix();
		////GL11.glDisable(GL11.GL_CULL_FACE);
		GlStateManager._translatef(i / 2,j / 2, 5);
		GL11.glScalef(15F*(i1/427F), 15F*(j1/240F), 15F);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		Minecraft.getInstance().getTextureManager().bind(color);
		RenderSystem.pushMatrix();
		//SARenderHelper.enableBlendMode(RenderTypeSA.ALPHA);//
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		//GlStateManager._disableLighting();
		//GlStateManager._translatef(-10,0,0);
		if(id==0){
			//
			health.renderPart("man");
			GL11.glColor4f(0, 0.8F, 1F, 0.9F);
			health.renderPart("man2");
			GlStateManager._translatef(0,0,0);
			GlStateManager._scalef(bar, 1F, 1F);
			GlStateManager._translatef(0,0,0);
			health.renderPart("man1");
		}else{
			//GlStateManager._translatef(10,0,0);
			health.renderPart("mob");
			GL11.glColor4f(1, 0.3F, 0.3F, 0.9F);
			health.renderPart("mob2");
			GlStateManager._translatef(0,0,0);
			GlStateManager._scalef(bar, 1F, 1F);
			GlStateManager._translatef(0,0,0);
			health.renderPart("mob1");
		}
		
		//GlStateManager._enableLighting();
		//GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		//SARenderHelper.disableBlendMode(RenderTypeSA.ALPHA);//
		RenderSystem.popMatrix();
		GL11.glColor4f(1, 1, 1, 1);

		RenderSystem.popMatrix();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//RenderSystem.disableBlend();
	}

	int x1 = WMConfig.CLIENT.display.hit_icon_x.get();
	int y1 = WMConfig.CLIENT.display.hit_icon_y.get();
	int x2 = WMConfig.CLIENT.display.hit_infor_x.get();
	int y2 = WMConfig.CLIENT.display.hit_infor_y.get();
	int iii;
	static boolean appliedMouseSlow = false;

	static float scaletime = 0;
	static float scaletime2 = 0;
	static float scaletime3 = 0;
	static int skeleton = 0;
	static int blacktime = 0;
	static float blacktime2 = 0;
	static int zoomtime = 0;

	static float h1 = 0;
	static float reload1 = 0;
	static float reloadmax1 = 10;
	static float reload2 = 0;
	static float reloadmax2 = 10;
	static float reload3 = 0;
	static float reloadmax3 = 10;
	static float reload4 = 0;
	static float reloadmax4 = 10;
	static String weaponname = "SELECT-WEAPON";
	float mousespeed = 1F;
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEvent(RenderGameOverlayEvent.Text event) {
		Minecraft mc = Minecraft.getInstance();
		int i = mc.getWindow().getGuiScaledWidth();
		int j = mc.getWindow().getGuiScaledHeight();
		PlayerEntity entityplayer = mc.player;
		MatrixStack stack = new MatrixStack();
		if(entityplayer != null){
			if(WMConfig.COMMON.hit.hitInformation.get()){
				CompoundNBT nbts = entityplayer.getPersistentData();
				int nb = nbts.getInt("hitentity");
				int nbd = nbts.getInt("hitentitydead");
				int nbdh = nbts.getInt("hitentity_headshot");
				int hitid = nbts.getInt("hitentity_id");
				float nbtdamage = nbts.getFloat("hitdamage");
				String nbtname = null;
				if(entityplayer!=null){
					Entity hitTarget = entityplayer.getCommandSenderWorld().getEntity(hitid);
					if(hitTarget!=null)nbtname = hitTarget.getName().getString();
				}
				RenderSystem.pushMatrix();
				if(nb >= 1){
					if(nb>99)scaletime = 0;
					if(scaletime>11)scaletime = 0;
					if(scaletime<5)++scaletime;
					this.renderIcon(i,j, "wmlib:textures/hud/hit.png", "icon", 4F/(10+scaletime),3/(1+scaletime));
					nbts.putInt("hitentity", nb-1);
				}else{
					scaletime = 12;
				}
				RenderSystem.popMatrix();
				
				RenderSystem.pushMatrix();
				if(nbd >= 1){
					if(nbd>99)scaletime2 = 0;
					if(scaletime2>11)scaletime2 = 0;
					if(scaletime2<10)++scaletime2;
					this.renderIcon(i,j, "wmlib:textures/hud/hitdead.png", "icon", scaletime2/16F,3/(1+scaletime2));
					nbts.putInt("hitentitydead", nbd-1);
				}else{
					scaletime2 = 12;
				}
				RenderSystem.popMatrix();
				
				RenderSystem.pushMatrix();
				if(nbd >= 1){
					if(nbd>199){
						scaletime3 = 0;
						++skeleton;
					}
					if(scaletime3>11)scaletime3 = 0;
					if(scaletime3<10)++scaletime3;
					if(skeleton>0){
						GL11.glTranslatef(6-6*skeleton, 0, 0);
						for (int lj = 0; lj < skeleton; lj++) {
							if(nbdh >= 1){
								this.renderIcon(i+x1+lj*25,j-90+y1, "wmlib:textures/hud/skeleton_gold.png", "icon", 3F/(5+scaletime3), 1);
							}else{
								this.renderIcon(i+x1+lj*25,j-90+y1, "wmlib:textures/hud/skeleton.png", "icon", 3F/(5+scaletime3), 1);
							}
						}
					}
				}else{
					scaletime3 = 12;
					skeleton = 0;
				}
				RenderSystem.popMatrix();
				
				RenderSystem.pushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glScalef(1F, 1F, 1);
				if(nbd >= 1)
				{
					if(nbdh >= 1){
						if(nbtname!=null)mc.font.drawShadow(stack, "爆头击杀"+nbtname+"+"+nbtdamage, i/2 - 15+x2,  j/2+30+y2, Color.YELLOW.getRGB());
					}else{
						if(nbtname!=null)mc.font.drawShadow(stack, "击杀"+nbtname+"+"+nbtdamage, i/2 - 15+x2,  j/2+30+y2, Color.RED.getRGB());
					}
				}
				GL11.glScalef(0.0625f, 0.0625f, 1);
				RenderSystem.popMatrix();
				
				RenderSystem.pushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glScalef(1.4F, 1.25F, 1.25F);
				float all = 0;
				if(nb >= 90){//总分
					all=all+nbtdamage;
					mc.font.drawShadow(stack, ""+all/*数量*/,235*i/683/*横坐标*/, 160*j/353/*纵坐标*/, Color.WHITE.getRGB());
				}
				GL11.glScalef(0.0625f, 0.0625f, 1);
				RenderSystem.popMatrix();
				
				RenderSystem.pushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				float roll = 0;//下滑
				float roll2 = 0;//下滑
				if(nb >= 1){
					if(nbdh >= 1)
					{
						if(nbdh<=10){
							roll=nbdh;
						}else{
							roll=10;
						}
						mc.font.drawShadow(stack, "爆头命中"+nbtname+"+"+nbtdamage, i/2 - 15+x2,  j/2+50-roll+y2, Color.RED.getRGB());
						nbts.putInt("hitentity_headshot", nbdh-1);
					}else{
						if(nb<=10){
							roll2=nb;
						}else{
							roll2=10;
						}
						mc.font.drawShadow(stack, "命中"+nbtname+"+"+nbtdamage, i/2 - 15+x2,  j/2+50-roll2+y2, 0xFFFFFF);
					}
				}
			}
			
			
			//GL11.glDisable(GL11.GL_BLEND);
			RenderSystem.popMatrix();
			boolean renderRader = false;
			boolean renderRts = false;
			float size1 = (float)(entityplayer.getHealth()/entityplayer.getMaxHealth());
			if (entityplayer.getVehicle() instanceof EntityWMSeat && entityplayer.getVehicle() != null) {
				EntityWMSeat seat = (EntityWMSeat) entityplayer.getVehicle();
				mousespeed = seat.turretSpeed;
				if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
					if(blacktime2<100)++blacktime2;
					EntityWMVehicleBase ve = (EntityWMVehicleBase) (seat.getVehicle());
					int i1 = ve.getPassengers().indexOf(seat);
					float color = (float)(ve.getHealth()/ve.getMaxHealth());
					int f3 = (int) (entityplayer.yHeadRot - ve.turretYaw);
					while (f3 < -180.0f) {
						f3 += 360.0f;
					}
					while (f3 >= 180.0f) {
						f3 -= 360.0f;
					}
					int f2 = (int) (entityplayer.xRot - ve.turretPitch);
					int yy = (int) (entityplayer.getYHeadRot() - ve.yRot);
					int xx = (int) (entityplayer.xRot - ve.xRot);
					if(mc.options.getCameraType() != PointOfView.THIRD_PERSON_BACK) {//PointOfView.FIRST_PERSON
						if(!this.appliedMouseSlow){
							mc.options.sensitivity *= mousespeed;
							this.appliedMouseSlow = true;
						}
					}else if(this.appliedMouseSlow) {//
						if(1F/mousespeed<0.5F){
							mc.options.sensitivity *= 1F/mousespeed;
						}else{
							mc.options.sensitivity = 0.5F;
						}
						this.appliedMouseSlow = false;
					}

					if(i1==0){
						this.reload1 = ve.reload1;
						this.reloadmax1 = ve.reload_time1;
						this.reload2 = ve.reload2;
						this.reloadmax2 = ve.reload_time2;
						this.reload3 = ve.reload3;
						this.reloadmax3 = ve.reload_time3;
						this.reload4 = ve.reload4;
						this.reloadmax4 = ve.reload_time4;
					}else{
						this.reload1 = seat.reload1;
						this.reloadmax1 = seat.reload_time1;
					}
					
					if(mc.options.getCameraType() == PointOfView.FIRST_PERSON) {
						if(seat.canNightV&&seat.openNightV)this.renderNightV(mc, i, j);
						if(seat.isZoom && seat.renderHudOverlayZoom) {
							this.renderHud(i, j, seat.hudOverlayZoom, event.getPartialTicks());
						}else {
							if(seat.renderHudOverlay)this.renderHud(i, j, seat.hudOverlay, event.getPartialTicks());
							if(seat.renderHudIcon)this.renderIcon(i, j, seat.hudIcon, "icon", 1, 0.9F);
						}
						if(seat.render_hud_box){
							this.renderBox(seat.hud_box_obj, seat.hud_box_tex, event.getPartialTicks(),seat,seat.mitarget,entityplayer, seat.isZoom, seat.turretYawO + (seat.turretYaw - seat.turretYawO) * event.getPartialTicks(),ve.havemissile,blacktime2);
						}
						if(i1==0){
							if(ve.VehicleType == 3)this.renderHeliHud(i, j, seat.hudOverlayZoom, ve, ve.turretYawO + (ve.turretYaw - ve.turretYawO) * event.getPartialTicks());
							if(ve.VehicleType == 4)this.renderFighterHud(i, j, seat.hudOverlayZoom, ve, ve.turretYawO + (ve.turretYaw - ve.turretYawO) * event.getPartialTicks());
							if(ve.VehicleType > 2){
								render_count(mc, i*0.78F, j*0.44F, 0, 0, (int)(ve.getY()), false, 4, color);//
								render_count(mc, i*0.17F, j*0.44F, 0, 0, (int)(ve.speedKPH), false, 5, color);//
							}else{
								render_count(mc, i*0.76F, j*0.25F, 0, 0, (int)(ve.speedKPH), false, 6, color);//
								render_count(mc, i*0.2F, j*0.25F, 0, 0, (int)(90-ve.turretPitch), false, 7, color);//
							}
						}else{
							render_count(mc, i*0.76F, j*0.25F, 0, 0, (int)(ve.speedKPH), false, 6, color);//
							render_count(mc, i*0.2F, j*0.25F, 0, 0, (int)(90-seat.turretPitch), false, 7, color);//
						}
						ForgeIngameGui.renderCrosshairs = false;
						if(ve.getArmyType1()>0){
							if(ve.VehicleType>2){
								this.renderIcon(i-yy*5, j-xx*3, "wmlib:textures/hud/tank64.png", "airlock", 1, 0.9F);
							}else{
								this.renderIcon(i-f3*5, j-f2*3, "wmlib:textures/hud/lock.png", "lock", 1, 0.9F);
							}
						}
					}else if(mc.options.getCameraType() == PointOfView.THIRD_PERSON_BACK){
						ForgeIngameGui.renderCrosshairs = false;
						if(ve.isthrow){
							render_count(mc, i*0.2F, j*0.25F, 0, 0, (int)(90-ve.turretPitch), false, 7, color);
						}else{
							if(ve.VehicleType>2){
								this.renderIcon(i-f3*5, j+12-f2*3, "wmlib:textures/hud/tank64.png", "airlock", 1, 0.9F);
							}else{
								this.renderIcon(i, j+12, "wmlib:textures/hud/aim.png", "aim", 1, 0.9F);
								this.renderIcon(i-f3*5, j-f2*3, "wmlib:textures/hud/lock.png", "lock", 1, 0.9F);
							}
						}
					}
					{
						RenderSystem.pushMatrix();
						GlStateManager._rotatef(8F, 1.0F, 0.0F, 0.0F);
						renderTankRote(i, j, ve, entityplayer);
						renderWeapon(i, j, ve, seat, i1);
						render_count(mc, i*0.6F, j*0.89F, 0, 0, (int)(ve.getHealth()*100/ve.getMaxHealth()), false, 0,color);//
						render_count(mc, i*0.355F, j*0.89F, 0, 0, (int)(entityplayer.getHealth()*100/entityplayer.getMaxHealth()), false, 2,size1);//
						render_count(mc, i*0.95F, j*5/7, 0, 0, 0, true, 1,color);//
						if(i1==0){
							if(ve.getArmyType2()==0){
								this.weaponname = ve.w1name;
								render_count(mc, i*0.86F, j*5/7, 0, 0, ve.getRemain1(), false, 1,color);//
								if(ve.weaponCount==4){
									render_count(mc, i*0.75F, j*0.888F, 0, 0, ve.getRemain2(), false, 3,color);//
									render_count(mc, i*0.835F, j*0.888F, 0, 0, ve.getRemain3(), false, 3,color);//
									render_count(mc, i*0.918F, j*0.888F, 0, 0, ve.getRemain4(), false, 3,color);//
								}
							}else if(ve.getArmyType2()==1){
								this.weaponname = ve.w4name;
								render_count(mc, i*0.86F, j*5/7, 0, 0, ve.getRemain4(), false, 1,color);//
								if(ve.weaponCount==4){
									render_count(mc, i*0.75F, j*0.888F, 0, 0, ve.getRemain2(), false, 3,color);//
									render_count(mc, i*0.835F, j*0.888F, 0, 0, ve.getRemain3(), false, 3,color);//
									render_count(mc, i*0.677F, j*0.888F, 0, 0, ve.getRemain1(), false, 3,color);//
								}
							}else{//2
								this.weaponname = ve.w2name;
								render_count(mc, i*0.86F, j*5/7, 0, 0, ve.getRemain2(), false, 1,color);//
								if(ve.weaponCount==4){
									render_count(mc, i*0.677F, j*0.888F, 0, 0, ve.getRemain1(), false, 3,color);//
									render_count(mc, i*0.835F, j*0.888F, 0, 0, ve.getRemain3(), false, 3,color);//
									render_count(mc, i*0.918F, j*0.888F, 0, 0, ve.getRemain4(), false, 3,color);//
								}
							}
						}else{
							this.weaponname = seat.w1name;
							render_count(mc, i*0.86F, j*5/7, 0, 0, seat.getRemain1(), false, 1,color);//
							if(seat.weaponCount==2){
								render_count(mc, i*0.85F, j*0.888F, 0, 0, seat.getRemain2(), false, 3,color);//
							}
						}
						RenderSystem.popMatrix();
					}
					if(seat.isZoom){
						if(zoomtime>0)zoomtime=0;
						if(blacktime<40){
							++blacktime;
							if(blacktime<20){
								renderBlack(i, j, blacktime*0.05F);
							}else{
								renderBlack(i, j, (40-blacktime)*0.05F);
							}
						}
					}else{
						if(zoomtime<1)blacktime=0;
						if(zoomtime<11)++zoomtime;
						if(blacktime<20){
							++blacktime;
							if(blacktime<10){
								renderBlack(i, j, 1);
							}else{
								renderBlack(i, j, (20-blacktime)*0.1F);
							}
						}
					}
					if(ve.VehicleType>2){
						renderRader=true;
					}
				}
			}else{
				blacktime = 0;
				blacktime2 = 0;
				if(this.h1!=0)this.h1=0;
				ForgeIngameGui.renderCrosshairs = true;
				if(this.appliedMouseSlow) {//
					if(1F/mousespeed<0.5F){
						mc.options.sensitivity *= 1F/mousespeed;
					}else{
						mc.options.sensitivity = 0.5F;
					}
					this.appliedMouseSlow = false;
				}
				ItemStack heldItem = entityplayer.getMainHandItem();
				ItemStack heldItem2 = entityplayer.getOffhandItem();
				if(heldItem.getItem() instanceof IRaderItem||heldItem2.getItem() instanceof IRaderItem){
					renderRader=true;
					renderRts=true;
				}
			}
			RenderSystem.disableBlend();
			
			GlStateManager._disableLighting();
			if(renderRader){
				if(renderRts){
					RenderSystem.pushMatrix();
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glScalef(1F, 1F, 1);
					mc.font.drawShadow(stack, "经验值:"+entityplayer.getScore(), 0 + 40,  10, Color.YELLOW.getRGB());
					GL11.glScalef(0.0625f, 0.0625f, 1);
					RenderSystem.popMatrix();
				}
				renderRader(i, j, entityplayer);
				List<Entity> list = entityplayer.level.getEntities(entityplayer, entityplayer.getBoundingBox().inflate(150D, 100D, 150D));
				int enemy = 0;
				int friend = 0;
				int man = 0;
				int army = 0;
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					if(ent instanceof IArmy && ent instanceof TameableEntity && ent.getVehicle()==null && renderRts){
						TameableEntity en = (TameableEntity)ent;
						IArmy cap = (IArmy)ent;
						if(en.getOwner()==entityplayer){
							++army;
							if(army<21){
								if(army<=10){
									renderUnit((float)i*(0.5F+army*0.05F), j*0.8F, null, ent, entityplayer,en.getHealth()/en.getMaxHealth(),cap.getIcon1(),cap.getIcon2(),cap.getSelect(),cap.getTeamCount());
								}else if(army<=20){
									renderUnit((float)i*(0.5F+(army-10)*0.05F), j*0.9F, null, ent, entityplayer,en.getHealth()/en.getMaxHealth(),cap.getIcon1(),cap.getIcon2(),cap.getSelect(),cap.getTeamCount());
								}
							}
						}
					}
					if(ModList.get().isLoaded("advancearmy")){
						if(ent instanceof CreatureRespawn && ((CreatureRespawn)ent).getHealth()>0){
							CreatureRespawn res = (CreatureRespawn)ent;
							if(res.isEnemyRespawn){
								renderRadarTarget(i, j, null, ent, entityplayer,4,2,null,null,0);
								++enemy;
								renderTeamCount(i*1.1F,  40*enemy,1,res.getRespawnCount()/800F);
								RenderSystem.pushMatrix();
								GL11.glEnable(GL11.GL_BLEND);
								GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
								GL11.glScalef(1F, 1F, 1);
								mc.font.drawShadow(stack, ""+res.getRespawnCount(), i/2 + 40,  10*enemy, Color.RED.getRGB());
								GL11.glScalef(0.0625f, 0.0625f, 1);
								RenderSystem.popMatrix();
							}else{
								renderRadarTarget(i, j, null, ent, entityplayer,4,3,null,null,0);
								++friend;
								renderTeamCount(i*0.9F,  40*friend,0,res.getRespawnCount()/800F);
								RenderSystem.pushMatrix();
								GL11.glEnable(GL11.GL_BLEND);
								GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
								GL11.glScalef(1F, 1F, 1);
								mc.font.drawShadow(stack, ""+res.getRespawnCount(), i/2 - 40,  10*friend, Color.CYAN.getRGB());
								GL11.glScalef(0.0625f, 0.0625f, 1);
								RenderSystem.popMatrix();
							}
						}else if(ent instanceof SupportPoint && ((SupportPoint)ent).getHealth()>0){
							SupportPoint spt = (SupportPoint)ent;
							if(spt.isAttack){
								renderRadarTarget(i, j, null, ent, entityplayer,7,0,null,null,0);
							}else{
								renderRadarTarget(i, j, null, ent, entityplayer,6,0,null,null,0);
							}
						}else if(ent instanceof DefencePoint && ((DefencePoint)ent).getHealth()>0){
							renderRadarTarget(i, j, null, ent, entityplayer,8,0,null,null,0);
						}
					}
					if(ent instanceof EntityWMVehicleBase){
						EntityWMVehicleBase ve = (EntityWMVehicleBase)ent;
						/*if(ent instanceof IArmy && ve.getTargetType()==3){
							IArmy cap = (IArmy)ent;
							if(ve.getOwner()==entityplayer){
								++army;
								if(army<40){
									if(army<20){
										renderUnit(i+army*20, j, null, ent, entityplayer,ve.getHealth()/ve.getMaxHealth(),ve.icon1tex,ve.icon2tex,ve.getChoose());
									}else{
										renderUnit(i+(army-20)*20, j+10, null, ent, entityplayer,ve.getHealth()/ve.getMaxHealth(),ve.icon1tex,ve.icon2tex,ve.getChoose());
									}
								}
							}
						}*/
						if(ve.getHealth()>0){
							if(ve.VehicleType<3)renderRadarTarget(i, j, null, ent, entityplayer,1,ve.getTargetType(),ve.icon1tex,ve.icon2tex,ve.turretYaw);
							if(ve.VehicleType==3)renderRadarTarget(i, j, null, ent, entityplayer,2,ve.getTargetType(),ve.icon1tex,ve.icon2tex,ve.turretYaw);
							if(ve.VehicleType==4)renderRadarTarget(i, j, null, ent, entityplayer,3,ve.getTargetType(),ve.icon1tex,ve.icon2tex,ve.turretYaw);
						}
					}else{
						if(ent instanceof CreatureEntity && ent.getVehicle()==null){
							if(((CreatureEntity)ent).getHealth()>0 && ent.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, ent.blockPosition()).getY() == ent.blockPosition().getY()){
								++man;
								if(man<60)renderRadarTarget(i, j, null, ent, entityplayer,0,0,null,null,0);
							}
						}
						if(ent instanceof EntityMissile){
							++man;
							if(man<60)renderRadarTarget(i, j, null, ent, entityplayer,5,0,null,null,0);
						}
					}
				}
				if(renderRts){
					RenderSystem.pushMatrix();
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glScalef(1F, 1F, 1);
					if(army<20){
						mc.font.drawShadow(stack, "兵力值:"+army, 0 + 40,  20, Color.GREEN.getRGB());
					}else{
						mc.font.drawShadow(stack, "兵力值:"+army, 0 + 40,  20, Color.RED.getRGB());
					}
					GL11.glScalef(0.0625f, 0.0625f, 1);
					RenderSystem.popMatrix();
				}
			}
			//GlStateManager._enableLighting();
		}
	}
	
	private List<ITextComponent> txt = new ArrayList<>();
	private List<ITextComponent> seat = new ArrayList<>();
	private List<ITextComponent> info = new ArrayList<>();
	int showtime = 0;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)//
    {
        if(event.phase != TickEvent.Phase.END)return;
		this.txt.clear();
		this.seat.clear();
		this.info.clear();
        Minecraft mc = Minecraft.getInstance();
        if(!mc.isWindowActive() || mc.options.hideGui)return;
        PlayerEntity player = mc.player;
        if(player == null)return;
        Entity entity = player.getVehicle();
		if(entity!=null && entity instanceof EntityWMSeat){
			EntityWMSeat seat = (EntityWMSeat)entity;
			GL11.glColor4f(0F, 0.8F, 1F, 1);
			if(showtime<100)++showtime;
			if(showtime<50){
				if(!seat.showhelp)this.addText("按C键打开按键帮助");
			}
			if(seat.canNightV && !seat.showhelp)this.addText("按N键开启夜视模式");
			if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
				int i = seat.getVehicle().getPassengers().indexOf(seat);
				EntityWMVehicleBase vehicle = (EntityWMVehicleBase) (seat.getVehicle());
				this.addInfor("");
				this.addInfor(vehicle.getName().getString());
				this.addInfor(this.weaponname);
				String name = "座位";
				int colorid = 0;
				for(int x = 0; x < vehicle.seatMaxCount; ++x) {
					if(vehicle.getAnySeat(x)!=null){
						EntityWMSeat seat1 = (EntityWMSeat)vehicle.getAnySeat(x);
						Entity ent = seat1.getAnyPassenger();
						if(ent!=null){
							name = ent.getName().getString();
							if(ent instanceof TameableEntity){
								TameableEntity soldier = (TameableEntity)ent;
								if(player==soldier.getOwner()){
									colorid = 1;
								}else{
									if(player.getTeam()!=null&&player.getTeam()==ent.getTeam()){
										colorid = 2;
									}else{
										if(ent instanceof IMob||ent.getTeam()!=null && ent.getTeam()!=player.getTeam()){
											colorid = 3;
										}else{
											colorid = 0;
										}
									}
								}
							}else{
								if(player==ent){
									colorid = 1;
								}else
								if(player.getTeam()!=null&&player.getTeam()==ent.getTeam()){
									colorid = 2;
								}else{
									if(ent instanceof IMob||ent.getTeam()!=null && ent.getTeam()!=player.getTeam()){
										colorid = 3;
									}else{
										colorid = 0;
									}
								}
							}
						}else{
							if(x==0){
								name = "驾驶员";
							}else{
								if(seat1.weaponCount==0){
									name = "乘员";
								}else{
									name = "炮手";
								}
							}
							colorid = 0;
						}
						this.addSeat("[" + x + "]" + name, colorid);
					}
				}
				if(vehicle.isthrow){
					if(vehicle.getMoveMode()==0){
						this.addText("火炮模式:关闭");
					}else{
						this.addText("火炮模式:开启");
					}
				}else{
					if(vehicle.VehicleType==4){
						if(vehicle.getMoveMode()==0){
							this.addText("速度控制:开启");
						}else{
							this.addText("速度控制:关闭");
						}
					}
					if(vehicle.VehicleType==3){
						if(vehicle.getMoveMode()==0){
							this.addText("平衡模式:关闭");
						}else{
							this.addText("平衡模式:开启");
						}
					}
				}
				if(seat.showhelp){
					this.addText("通用:");
					this.addText("鼠标左键:1号武器");
					this.addText("鼠标右键:瞄准");
					this.addText("空格键:2号武器");
					this.addText("R:切换座位");
					this.addText("F:切换武器");
					if(vehicle.isturret)this.addText("固定部署武器:蹲下加右键搬起,再次蹲下时放下");
					if(vehicle.VehicleType==4){
						this.addText("固定翼载具");
						this.addText("W:加速");
						this.addText("S:减速");
						this.addText("A:左转向");
						this.addText("D:右转向");
						this.addText("鼠标左右拖动:侧身");
						this.addText("鼠标前后拖动:俯仰");
						this.addText("F:切换1号武器");
						this.addText("X:激活3号武器");
						this.addText("G:切换速度控制");
						this.addText("左Ctrl键:自由视角");
					}
					if(vehicle.VehicleType==3){
						this.addText("直升机载具");
						this.addText("W:增加升力");
						this.addText("S:降低升力");
						this.addText("A:左转向");
						this.addText("D:右转向");
						this.addText("鼠标左右拖动:侧身左右移动");
						this.addText("鼠标前后拖动:俯仰前后移动");
						this.addText("G:保持升力平衡");
						this.addText("X:激活3号武器");
						this.addText("V:激活4号武器");
						this.addText("左Ctrl键:自由视角/炮手视角");
					}
					if(vehicle.VehicleType<3){
						this.addText("常规地面载具");
						this.addText("W:加速");
						this.addText("S:减速");
						this.addText("A:左转向");
						this.addText("D:右转向");
						this.addText("X:激活3号武器");
						this.addText("V:激活4号武器");
						if(vehicle.isthrow)this.addText("G键:切换火炮模式");
					}
					this.addText("载具数据:");
					this.addText("最大血量:"+vehicle.getMaxHealth());
					this.addText("速度:"+vehicle.MoveSpeed*100);
					this.addText("身体装甲值:");
					this.addText("正面="+vehicle.armor_front+" 侧面="+vehicle.armor_side+" 背面="+vehicle.armor_back+" 顶部="+vehicle.armor_top+" 底部="+vehicle.armor_bottom);
					if(vehicle.haveTurretArmor){
						this.addText("炮塔装甲值:");
						this.addText("炮塔判定最小高度:"+vehicle.armor_turret_height);
						this.addText("正面="+vehicle.armor_turret_front+" 侧面="+vehicle.armor_turret_side+" 背面="+vehicle.armor_turret_back);
					}
					this.addText("advancearmy.infor.pickaxe_fix.desc");
				}
			}
		}else{
			showtime=0;
		}
    }
	
	private void addText(String label)
    {
        this.txt.add(new StringTextComponent(label).withStyle(TextFormatting.BOLD).withStyle(TextFormatting.RESET));
    }
	private void addInfor(String label)
    {
        this.info.add(new StringTextComponent(label).withStyle(TextFormatting.AQUA));
    }
	private void addSeat(String label, int color)
    {
		if(color==0)this.seat.add(new StringTextComponent(label).withStyle(TextFormatting.RESET));
		if(color==1)this.seat.add(new StringTextComponent(label).withStyle(TextFormatting.GREEN));
        if(color==2)this.seat.add(new StringTextComponent(label).withStyle(TextFormatting.AQUA));
		if(color==3)this.seat.add(new StringTextComponent(label).withStyle(TextFormatting.RED));
    }
	
    @SubscribeEvent
    public void onFovUpdate(FOVUpdateEvent event)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if(player == null)
            return;
        Entity ridingEntity = player.getVehicle();
		Minecraft mc = Minecraft.getInstance();
        if(ridingEntity instanceof EntityWMSeat)
        {
			EntityWMSeat seat = (EntityWMSeat) player.getVehicle();
			if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
				EntityWMVehicleBase ve = (EntityWMVehicleBase) (seat.getVehicle());
				//if(seat.isZoom)event.setNewfov(event.getFov()*0.3F);
				if(mc.options.getCameraType() == PointOfView.FIRST_PERSON||ve.VehicleType<3) {//THIRD_PERSON_FRONT
					if(seat.isZoom && blacktime>10)event.setNewfov(event.getFov()*0.3F);
				}else{
					//event.setNewfov(event.getFov()*30F);
				}
			}
        }
    }
	
	@OnlyIn(Dist.CLIENT)
	protected void renderWeapon(int i, int j, EntityWMVehicleBase entity, EntityWMSeat seat, int id) {
		RenderSystem.enableBlend();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
		RenderSystem.disableAlphaTest();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		RenderSystem.pushMatrix();
		//GL11.glDisable(GL11.GL_LIGHTING);//关闭阴影
		//SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		//SARenderHelper.enableFXLighting();
		GlStateManager._disableLighting();
		//RenderSystem.enableBlend();
		//RenderSystem.disableDepthTest();
		//RenderSystem.depthMask(false);
		//RenderSystem.defaultBlendFunc();
		//RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//RenderSystem.disableAlphaTest();
		
		float health = (float)(entity.getHealth()/entity.getMaxHealth());
		float size1 = reload1/reloadmax1;
		float size2 = reload2/reloadmax2;
		float size3 = reload3/reloadmax3;
		float size4 = reload4/reloadmax4;
		Minecraft mc = Minecraft.getInstance();
		//GL11.glDisable(GL11.GL_CULL_FACE);
		if(health>0.7){
			GL11.glColor4f(0F, 0.8F, 1F, 1);
		}else if(health>0.4){
			GL11.glColor4f(0F+(1.2F-health), 0.8F, 0F, 1);
		}else{
			GL11.glColor4f(0.5F+health*0.5F, 0.1F, 0F, 1);
		}
		mc.getTextureManager().bind(white);
		RenderSystem.pushMatrix();
		GlStateManager._translatef((float)(i *0.66F), (float)(j *0.95F), 5);
		GL11.glScalef(15F*(i/427F), 15F*(j/240F), 15F);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);
		//
		
		if(entity.getHealth()>0 && h1==0 && entity.hurtTime == 0){
			h1 = entity.getHealth();
		}else{
			if(h1>entity.getHealth())h1-=0.5F;
		}
		
		RenderSystem.pushMatrix();
		GlStateManager._translatef(9.35F, 0F, 0F);
		GlStateManager._scalef(health, 1, 1);
		GlStateManager._translatef(-9.35F, 0F, 0F);
		hudw.renderPart("health");
		RenderSystem.popMatrix();
		RenderSystem.pushMatrix();
		GL11.glColor4f(1F, 1F, 1F, 1);
		GlStateManager._translatef(9.35F, 0F, 0F);
		GlStateManager._scalef(h1/entity.getMaxHealth(), 1, 1);
		GlStateManager._translatef(-9.35F, 0F, 0F);
		hudw.renderPart("health2");
		RenderSystem.popMatrix();
		
		if(size1==0)size1=1;
		if(size2==0)size2=1;
		if(size3==0)size3=1;
		if(size4==0)size4=1;
		float movex = 0;
		String icon = "icon1";

		if(health>0.7){
			GL11.glColor4f(0F, 0.8F, 1F, 1);
		}else if(health>0.4){
			GL11.glColor4f(0F+(1.2F-health), 0.8F, 0F, 1);
		}else{
			GL11.glColor4f(0.5F+health*0.5F, 0.1F, 0F, 1);
		}
		if(id > 0){
			if(seat.weaponCount==1){
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size1, 1);
				hudw.renderPart("c1");
				RenderSystem.popMatrix();
				mc.getTextureManager().bind(grey);
				hudw.renderPart("mat1");
			}else if(seat.weaponCount==2){
				icon = "icon2";
				movex = 4.74F;
				RenderSystem.pushMatrix();
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size1, 1);
				hudw.renderPart("c2");
				RenderSystem.popMatrix();
				mc.getTextureManager().bind(white2);
				GlStateManager._translatef(movex ,0,0);
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size2, 1);
				hudw.renderPart("c2");
				RenderSystem.popMatrix();
				RenderSystem.popMatrix();
				mc.getTextureManager().bind(grey);
				hudw.renderPart("mat2");
			}
		}else{
			if(entity.getArmyType2()>0)mc.getTextureManager().bind(white2);
			if(entity.weaponCount==1){
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size1, 1);
				hudw.renderPart("c1");
				RenderSystem.popMatrix();
				mc.getTextureManager().bind(grey);
				hudw.renderPart("mat1");
			}else
			if(entity.weaponCount==2){
				icon = "icon2";
				movex = 4.74F;
				RenderSystem.pushMatrix();
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size1, 1);
				hudw.renderPart("c2");
				RenderSystem.popMatrix();
				mc.getTextureManager().bind(white2);
				GlStateManager._translatef(movex ,0,0);
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size2, 1);
				hudw.renderPart("c2");
				RenderSystem.popMatrix();
				RenderSystem.popMatrix();
				mc.getTextureManager().bind(grey);
				hudw.renderPart("mat2");
			}else
			if(entity.weaponCount==3){
				icon = "icon3";
				movex = 3.1F;
				RenderSystem.pushMatrix();
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size1, 1);
				hudw.renderPart("c3");
				RenderSystem.popMatrix();
				mc.getTextureManager().bind(white2);
				GlStateManager._translatef(movex ,0,0);
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size2, 1);
				hudw.renderPart("c3");
				RenderSystem.popMatrix();
				GlStateManager._translatef(movex ,0,0);
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size3, 1);
				hudw.renderPart("c3");
				RenderSystem.popMatrix();
				RenderSystem.popMatrix();
				mc.getTextureManager().bind(grey);
				hudw.renderPart("mat3");
			}else
			if(entity.weaponCount>=4){
				icon = "icon4";
				movex = 2.36F;
				RenderSystem.pushMatrix();
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size1, 1);
				hudw.renderPart("c4");
				RenderSystem.popMatrix();
				if(entity.getArmyType2()==0)mc.getTextureManager().bind(white2);
				GlStateManager._translatef(movex ,0,0);
				if(entity.getArmyType2()==2)mc.getTextureManager().bind(white);
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size2, 1);
				hudw.renderPart("c4");
				RenderSystem.popMatrix();
				if(entity.getArmyType2()==2)mc.getTextureManager().bind(white2);
				GlStateManager._translatef(movex ,0,0);
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size3, 1);
				hudw.renderPart("c4");
				RenderSystem.popMatrix();
				if(entity.getArmyType2()==1)mc.getTextureManager().bind(white);
				GlStateManager._translatef(movex ,0,0);
				RenderSystem.pushMatrix();
				GlStateManager._scalef(1, size4, 1);
				hudw.renderPart("c4");
				RenderSystem.popMatrix();
				RenderSystem.popMatrix();
				mc.getTextureManager().bind(grey);
				hudw.renderPart("mat4");
			}
		}
		hudw.renderPart("base");
		if(id > 0){
			if(seat.weaponCount==1)GlStateManager._translatef(movex ,0,0);
			mc.getTextureManager().bind(new ResourceLocation(seat.w1icon));
			hudw.renderPart(icon);
			if(seat.weaponCount>1){
				GlStateManager._translatef(movex ,0,0);
				mc.getTextureManager().bind(new ResourceLocation(seat.w2icon));
				hudw.renderPart(icon);
			}
		}else{
			if(entity.weaponCount==1)GlStateManager._translatef(movex ,0,0);
			mc.getTextureManager().bind(new ResourceLocation(entity.w1icon));
			hudw.renderPart(icon);
			if(entity.weaponCount>1){
				GlStateManager._translatef(movex ,0,0);
				mc.getTextureManager().bind(new ResourceLocation(entity.w2icon));
				hudw.renderPart(icon);
			}
			if(entity.weaponCount>2){
				GlStateManager._translatef(movex ,0,0);
				mc.getTextureManager().bind(new ResourceLocation(entity.w3icon));
				hudw.renderPart(icon);
			}
			if(entity.weaponCount>3){
				GlStateManager._translatef(movex ,0 ,0);
				mc.getTextureManager().bind(new ResourceLocation(entity.w4icon));
				hudw.renderPart(icon);
			}
		}
		RenderSystem.popMatrix();
		GL11.glColor4f(1F, 1F, 1F, 1);
		//RenderSystem.depthMask(true);
		//RenderSystem.enableDepthTest();
		//RenderSystem.enableAlphaTest();
		//RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//SARenderHelper.disableFXLighting();
		GlStateManager._enableLighting();
		//SARenderHelper.disableFXLighting();
		//SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		//GL11.glEnable(GL11.GL_LIGHTING);
		//GL11.glEnable(GL11.GL_CULL_FACE);
		RenderSystem.popMatrix();
	}
	
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)return;
        MatrixStack stack = new MatrixStack();
        Minecraft mc = Minecraft.getInstance();
		//GlStateManager._disableLighting();
		int w = mc.getWindow().getGuiScaledWidth();
		int h = mc.getWindow().getGuiScaledHeight();
		for(int i = 0; i < this.txt.size(); i++)
        {
			if(i<15){
				mc.font.drawShadow(stack, this.txt.get(i), 10, 10 + 15 * i, 0xFFFFFF);
			}else{
				mc.font.drawShadow(stack, this.txt.get(i), 80, 10 + 15 * (i-15), 0xFFFFFF);
			}
        }
		for(int i = 0; i < this.seat.size(); i++)
        {
			mc.font.drawShadow(stack, this.seat.get(i), w-45, 10 + 15 * i, 0xFFFFFF);
        }
		for(int i = 0; i < this.info.size(); i++){
			mc.font.drawShadow(stack, this.info.get(i), w-70, h - i*0.2F*h, 0xFFFFFF);
		}
		//GlStateManager._enableLighting();
    }
	
	/*@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
	public void renderThird(EntityViewRenderEvent.CameraSetup event)
	{
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity entityplayer = mc.player;
		MatrixStack stack = new MatrixStack();
		if(entityplayer != null){
			if (entityplayer.getVehicle() instanceof EntityWMSeat && entityplayer.getVehicle() != null) {
				EntityWMSeat vehicle = (EntityWMSeat) entityplayer.getVehicle();
				if(mc.options.getCameraType().isFirstPerson()) {//THIRD_PERSON_FRONT
					float f1 = entityplayer.yRot * (2 * (float) Math.PI / 360);
					stack.translate(vehicle.seatView1X * (float)Math.sin(f1), vehicle.seatView1Y, vehicle.seatView1Z * (float)Math.sin(f1));
				}
				
				if(!mc.options.getCameraType().isFirstPerson()) {
					float f1 = entityplayer.xRot * (2 * (float) Math.PI / 360);
					stack.translate(vehicle.seatView3X, vehicle.seatView3Y* (float)Math.cos(f1), vehicle.seatView3Z* (float)Math.cos(f1));
				}
			}
		}
	}*/
}

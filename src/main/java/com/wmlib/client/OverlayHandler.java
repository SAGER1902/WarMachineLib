package wmlib.client;

import java.awt.Color;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import com.mojang.math.Axis;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fml.ModList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component; // 替代 ITextComponent
import net.minecraft.network.chat.MutableComponent; // 用于可修改的文本组件
import net.minecraft.ChatFormatting; // 替代 TextFormatting
import org.joml.Vector2f; // 替代 net.minecraft.util.math.vector.Vector2f
import com.mojang.blaze3d.vertex.Tesselator; // 替代 net.minecraft.client.renderer.Tessellator (注意拼写变化)
import com.mojang.blaze3d.vertex.BufferBuilder; // 替代 net.minecraft.client.renderer.BufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat; // 替代 net.minecraft.client.renderer.vertex.DefaultVertexFormats
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ComputeFovModifierEvent; // 替代 FOVUpdateEvent
import net.minecraftforge.client.event.ViewportEvent; // 替代 EntityViewRenderEvent
import net.minecraftforge.client.event.RenderHandEvent; // 保持不变
import net.minecraftforge.client.event.RenderTooltipEvent; // 保持不变
import net.minecraftforge.client.event.RenderArmEvent; // 保持不变
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.CameraType; // 替代 net.minecraft.client.settings.PointOfView
import net.minecraftforge.client.gui.ScreenUtils; // 替代部分ForgeIngameGui功能
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;

import wmlib.rts.RtsMoShiScreen;
import wmlib.rts.XiangJiEntity;
import wmlib.client.render.RenderTypeVehicle;
import wmlib.client.render.SARenderState;
import wmlib.common.bullet.EntityMissile;
import wmlib.api.IRaderItem;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.api.IArmy;
import wmlib.client.obj.SAObjModel;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import wmlib.util.vec.Vec2f;
import wmlib.common.item.ItemGun;
import advancearmy.entity.map.CreatureRespawn;
import advancearmy.entity.map.SupportPoint;
import advancearmy.entity.map.DefencePoint;

import wmlib.WMConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import java.util.HashMap;
import java.util.Map;
public class OverlayHandler
{
	private static final ResourceLocation aim = ResourceLocation.tryParse("wmlib:textures/hud/tank.png");
	private static final SAObjModel icon = new SAObjModel("wmlib:textures/hud/icon.obj");
	private static final SAObjModel box = new SAObjModel("wmlib:textures/hud/box.obj");
	private static final ResourceLocation boxtex = ResourceLocation.tryParse("wmlib:textures/hud/box.png");
	private static final ResourceLocation nighttex = ResourceLocation.tryParse("wmlib:textures/hud/night.png");
	private static final SAObjModel rader = new SAObjModel("wmlib:textures/hud/rader.obj");
	private static final ResourceLocation radert = ResourceLocation.tryParse("wmlib:textures/hud/rader.png");
	private static final ResourceLocation serch = ResourceLocation.tryParse("wmlib:textures/hud/hou2.png");
	private static final ResourceLocation serch1 = ResourceLocation.tryParse("wmlib:textures/hud/serch.png");
	
	private static final SAObjModel rts_icon = new SAObjModel("wmlib:textures/hud/rts.obj");
	private static final ResourceLocation rts_tex = ResourceLocation.tryParse("wmlib:textures/hud/rts.png");
	
	private static final ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/hud/count.png");
	private static final SAObjModel unit = new SAObjModel("wmlib:textures/hud/unit.obj");
	private static final SAObjModel digit = new SAObjModel("wmlib:textures/hud/digit.obj");
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/hud/count.obj");
	private static final SAObjModel obj2 = new SAObjModel("wmlib:textures/hud/count2.obj");
	private static final SAObjModel obj3 = new SAObjModel("wmlib:textures/hud/count3.obj");
	
	private static final SAObjModel hudw = new SAObjModel("wmlib:textures/hud/vehicle.obj");
	private static final ResourceLocation white = ResourceLocation.tryParse("wmlib:textures/hud/white.png");
	private static final ResourceLocation white2 = ResourceLocation.tryParse("wmlib:textures/hud/white2.png");
	private static final ResourceLocation grey = ResourceLocation.tryParse("wmlib:textures/hud/grey.png");
	private static final ResourceLocation cloud = ResourceLocation.tryParse("wmlib:textures/hud/cloud.png");
	private static final ResourceLocation repair = ResourceLocation.tryParse("wmlib:textures/hud/repair.png");
	
	private static final SAObjModel hudh = new SAObjModel("wmlib:textures/hud/hud.obj");
	private static final SAObjModel hudf = new SAObjModel("wmlib:textures/hud/hud2.obj");
	
	private static final ResourceLocation sp = ResourceLocation.tryParse("wmlib:textures/hud/sp.png");
	private static final ResourceLocation cz = ResourceLocation.tryParse("wmlib:textures/hud/cz.png");
	
	private static final ResourceLocation hudtex = ResourceLocation.tryParse("wmlib:textures/hud/hud.png");
	private static final ResourceLocation hud256 = ResourceLocation.tryParse("wmlib:textures/hud/256.png");
	private static final SAObjModel health = new SAObjModel("wmlib:textures/marker/teamhealth.obj");
	
	private static final ResourceLocation color = ResourceLocation.tryParse("wmlib:textures/marker/color.png");
	
	/*@SubscribeEvent
	public void rendertip(RenderTooltipEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		Player entityplayer = mc.player;
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
		Player entityplayer = mc.player;
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
		Player entityplayer = mc.player;
		if(entityplayer != null){
			if (entityplayer.getVehicle() instanceof EntityWMSeat && entityplayer.getVehicle() != null) {
				event.setCanceled(true);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onEvent1(RenderGuiOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		Player entityplayer = mc.player;
		ResourceLocation overlayId = event.getOverlay().id();
        if (overlayId.equals(ResourceLocation.tryParse("minecraft:player_health")) ||
            overlayId.equals(ResourceLocation.tryParse("minecraft:armor_level")) ||
            overlayId.equals(ResourceLocation.tryParse("minecraft:food_level")) ||
            overlayId.equals(ResourceLocation.tryParse("minecraft:experience_bar")) ||
            overlayId.equals(ResourceLocation.tryParse("minecraft:health_mount")) ||
            overlayId.equals(ResourceLocation.tryParse("minecraft:hotbar")) ||
            overlayId.equals(ResourceLocation.tryParse("minecraft:air_level"))) {
			if (entityplayer.getVehicle() instanceof EntityWMSeat && entityplayer.getVehicle() != null) {
				event.setCanceled(true);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	protected void renderHud(GuiGraphics $$0, PoseStack stack, int i, int j, String texture, float partialTicks) {
		stack.pushPose();
		float gunRotX = RenderParameters.GUN_ROT_X_LAST + (RenderParameters.GUN_ROT_X - RenderParameters.GUN_ROT_X_LAST) * partialTicks;
		float gunRotY = RenderParameters.GUN_ROT_Y_LAST + (RenderParameters.GUN_ROT_Y - RenderParameters.GUN_ROT_Y_LAST) * partialTicks;
		//float jump = -RenderParameters.playerRecoilPitch*5*0.08F;
		//float cock = 5*RenderParameters.playerRecoilPitch*0.2F;
		stack.mulPose(Axis.YP.rotationDegrees(gunRotX*-2F));
		stack.mulPose(Axis.ZP.rotationDegrees(gunRotY*-2F));
		stack.translate(gunRotX*0.1F, 0.0F, 0);
		stack.translate(0.0F, gunRotY, 0);
		//stack.translate(0.0F, 0.0F, cock);
		//stack.mulPose(Axis.XP.rotationDegrees(jump));
		
		RenderSystem.enableBlend();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();

        $$0.setColor(1.0f, 1.0f, 1.0f, 1.0F);
        $$0.blit(ResourceLocation.tryParse(texture), 0, 0, -90, 0.0f, 0.0f, i, j, i, j);
        $$0.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		stack.popPose();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void render_count(PoseStack stack, Minecraft mc, float i, float j, int x, int y, int count, boolean un, int id, float color) {
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		float size = 1F;
		float size1 = 1F;
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		RenderType rt = SARenderState.getBlendDepthWrite(tex);

		stack.pushPose();
		stack.translate((float)(i - x), (float)(j - y), 15);
		if(id==1){
			size = 1.5F;
			size1 = 1.1F;
		}
		stack.scale(15F*(i1/427F)*size, 15F*(j1/240F)*size, 15F);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
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
		SAObjModel model = digit;
		//if(id==1)model = obj;
		//if(id==2)model = obj2;
		//if(id==3)model = obj3;
		if(model!=null){
			VertexConsumer vb = bufferSource.getBuffer(rt);
			model.setRender(null, vb, stack, packedLight);
			if(id==2){//玩家绿
				model.setColor(0F+(1-color), 1F-(1-color), 0F, 1);
			}
			if(id==4||id==5){
				if(color>0.7){
					model.setColor(0F, 0.8F, 1F, 1);
				}else if(color>0.4){
					model.setColor(0F+(1.2F-color), 0.8F, 0F, 1);
				}else{
					model.setColor(0.5F+color*0.5F, 0.1F, 0F, 1);
				}
			}
			if(id==6||id==7){
				if(color>0.4){
					model.setColor(0.8F, 1F, 1F, 1);
				}else{
					model.setColor(0.8F+(1-color), 1F, 1F, 1);
				}
			}
			if(un){
				model.renderPart("un");//
			}else{
				if(count<10){
					if(id==1){
						model.setColor(0F+(1-color), 0.8F-(1-color), 1F-(1-color), 0.2F);
						model.renderPart("zero");//
						stack.translate(0.4F*size1,0,0);
						model.renderPart("zero");//
						stack.translate(0.4F*size1,0,0);
						if(color>0.4){
							model.setColor(0.8F, 1F, 1F, 1);
						}else{
							model.setColor(0.8F+(1-color), 1F, 1F, 1);
						}
					}
					model.renderPart("obj" + t1);//
				}else if(count<100)
				{
					if(id==1){
						model.setColor(0F+(1-color), 0.8F-(1-color), 1F-(1-color), 0.2F);
						model.renderPart("zero");//
						stack.translate(0.4F*size1,0,0);
						if(color>0.4){
							model.setColor(0.8F, 1F, 1F, 1);
						}else{
							model.setColor(0.8F+(1-color), 1F, 1F, 1);
						}
					}
					model.renderPart("obj" + t2);//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t1);//
				}else if(count<1000)
				{
					model.renderPart("obj" + t3);//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t2);//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t1);//
				}else if(count<10000)
				{
					model.renderPart("obj" + t4);//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t3);//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t2);//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj" + t1);//
				}else{
					model.renderPart("obj9");//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj9");//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj9");//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("obj9");//
					stack.translate(0.4F*size1,0,0);
					model.renderPart("add");//+
				}
				if(id == 0||id == 2){
					stack.translate(0.4F*size1,0,0);
					model.renderPart("rate");//+
				}
				if(id == 4){
					stack.translate(0.4F*size1,0,0);
					model.renderPart("alt");//+
				}
				if(id == 5||id == 6){
					stack.translate(0.4F*size1,0,0);
					model.renderPart("kph");//+
				}
				if(id == 7){
					stack.translate(0.4F*size1,0,0);
					model.renderPart("angle");//+
				}
			}
			model.setColor(1.0F, 1.0F, 1.0F, 1.0F);
			bufferSource.endBatch();
		}
		stack.popPose();
	}
	
	protected void renderUnit(PoseStack stack, float i, float j, float health, ResourceLocation icon1tex, ResourceLocation icon2tex, boolean select, int count, int count2)
	{
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		float size = 1F;
		
		{
			MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
			int packedLight = 0xF000F0;
			unit.setColor(1F, 1F, 1F, 1F);
			RenderType rt3 = SARenderState.getBlendWrite_NoLight(tex);
			
			stack.pushPose();
			stack.translate(i, j, 5);
			stack.scale(15F*(i1/427F)*size, 15F*(j1/240F)*size, 15F*size);
			stack.mulPose(Axis.YP.rotationDegrees(-180F));
			stack.mulPose(Axis.ZP.rotationDegrees(180F));
			if(icon1tex!=null){
				RenderType rt = SARenderState.getBlendWrite_NoLight(icon1tex);
				VertexConsumer vb = bufferSource.getBuffer(rt);
				unit.setRender(null, vb, stack, packedLight);
				unit.renderPart("tank2");
			}
			if(icon2tex!=null){
				stack.pushPose();
				RenderType rt2 = SARenderState.getBlendWrite_NoLight(icon2tex);
				VertexConsumer vb2 = bufferSource.getBuffer(rt2);
				unit.setRender(null, vb2, stack, packedLight);
				unit.renderPart("tank1");
				stack.popPose();
			}
			VertexConsumer vb3 = bufferSource.getBuffer(rt3);
			unit.setRender(null, vb3, stack, packedLight);
			
			stack.pushPose();
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
			stack.popPose();
			
			stack.pushPose();
			if(count2>0){
				String c = String.valueOf(count2);
				int num = count2;
				int shiwei=0,gewei=0;
				shiwei = (num / 10 ) % 10;
				gewei = (num %100) % 10;
				String t1 = String.valueOf(gewei);
				String t2 = String.valueOf(shiwei);
				stack.translate(0.8F, -1, 0);
				unit.renderPart("obj" + t2);//
				stack.translate(0.4F,0,0);
				unit.renderPart("obj" + t1);//
			}
			stack.popPose();
			
			if(select)unit.renderPart("choose");
			if(health>0.7){
				unit.setColor(0F, 0.8F, 1F, 1);
			}else if(health>0.4){
				unit.setColor(0F+(1.2F-health), 0.8F, 0F, 1);
			}else{
				unit.setColor(0.5F+health*0.5F, 0.1F, 0F, 1);
			}
			stack.pushPose();
			stack.translate(0,-0.2F,0);
			//stack.translate(9.35F, 0F, 0F);
			stack.scale(health, 1, 1);
			//stack.translate(-9.35F, 0F, 0F);
			unit.renderPart("health");
			stack.popPose();
			unit.renderPart("base");
			stack.popPose();
			bufferSource.endBatch();
		}
		unit.setColor(1, 1F, 1F, 1F);
		stack.popPose();
	}
	
	protected void renderRadarTarget(double posx, double posz, PoseStack stack, int i, int j, float width,float height, String texture, Entity entity1, Player player, int type, int ai, ResourceLocation icon1tex, ResourceLocation icon2tex, float rhead)
	{	
		int colorid = 0;
		if(entity1!=null){
			if(entity1 instanceof TamableAnimal){
				TamableAnimal soldier = (TamableAnimal)entity1;
				if(player==soldier.getOwner()){
					colorid = 1;
				}else{
					if(player.getTeam()!=null&&player.getTeam()==entity1.getTeam()/*||entity1 instanceof IArmy && entity1.getTeam()==null && player.getTeam()==null*/){
						colorid = 2;
					}else{
						if(entity1 instanceof Enemy||entity1.getTeam()!=null && entity1.getTeam()!=player.getTeam()){
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
					if(entity1 instanceof Enemy||entity1.getTeam()!=null && entity1.getTeam()!=player.getTeam()){
						colorid = 3;
					}else{
						colorid = 0;
					}
				}
			}
		}else{
			colorid = 0;
		}
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		
		
		RenderType rt3 = SARenderState.getBlendWrite_NoLight(tex);

		if(colorid==1)rader.setColor(1, 1F, 1F, 0.9F);
		if(colorid==1)rader.setColor(0.6F, 1, 0.6F, 0.9F);
        if(colorid==2)rader.setColor(0, 0.8F, 1F, 0.9F);
		if(colorid==3)rader.setColor(1, 0.3F, 0.3F, 0.9F);
		if(ai==2)rader.setColor(1, 0.3F, 0.3F, 0.9F);
		if(ai==3 && colorid!=1)rader.setColor(0, 0.8F, 1F, 0.9F);

		double dx = (posx - entity1.getX());
		double dz = (posz - entity1.getZ());
		double distance = Math.sqrt(dx * dx + dz * dz);
		double radius = 65*(i/427F)*0.8F;
		{
			if (distance > radius) {
				double scale = radius / distance;
				dx *= scale*0.8F;
				dz *= scale*0.8F;
			}
			stack.pushPose();
			
			stack.translate((float)(i *0.2F*(0.74F+width))-(float)dx, (float)(j *(0.74F+height))-(float)dz, 5);
			stack.scale(15F*(i/427F)*0.8F, 15F*(j/240F)*0.8F, 15F*0.8F);
			
			
			stack.mulPose(Axis.YP.rotationDegrees(-180F));
			//stack.mulPose(Axis.ZP.rotationDegrees(180F));
			stack.mulPose(Axis.ZP.rotationDegrees(-entity1.getYRot()));
			if(icon1tex!=null){
				RenderType rt = SARenderState.getBlendWrite_NoLight(icon1tex);
				VertexConsumer vb = bufferSource.getBuffer(rt);
				rader.setRender(null, vb, stack, packedLight);
				rader.renderPart("tank2");
			}
			if(icon2tex!=null){
				RenderType rt2 = SARenderState.getBlendWrite_NoLight(icon2tex);
				stack.pushPose();
				stack.mulPose(Axis.ZP.rotationDegrees((rhead-entity1.getYRot())));
				VertexConsumer vb2 = bufferSource.getBuffer(rt2);
				rader.setRender(null, vb2, stack, packedLight);
				rader.renderPart("tank1");
				stack.popPose();
			}
			if(icon1tex==null && icon2tex==null){
				VertexConsumer vb3 = bufferSource.getBuffer(rt3);
				rader.setRender(null, vb3, stack, packedLight);
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
			stack.popPose();
		}
		rader.setColor(1, 1F, 1F, 1F);
		stack.popPose();
	}
	 
	int raderrote = 0;
	@OnlyIn(Dist.CLIENT)
	protected void renderRaderUI(PoseStack stack, int i, int j, float width, float height, float rote, float size) {
		if(raderrote<360){
			++raderrote;
		}else{
			raderrote = 0;
		}
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		RenderType rt = SARenderState.getBlendWrite_NoLight(tex);
		RenderType rt2 = SARenderState.getBlendDepthWrite(radert);
		RenderType rt3 = SARenderState.getBlendWrite_NoLight(serch1);
		RenderType rt4 = SARenderState.getBlendWrite_NoLight(serch);
		stack.pushPose();
		stack.translate((float)(i *0.2F*(0.74F+width)), (float)(j *(0.74F+height)), 5);
		stack.scale(15F*(i/427F)*size, 15F*(j/240F)*size, 15F*size);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		VertexConsumer vb = bufferSource.getBuffer(rt);
		rader.setRender(null, vb, stack, packedLight);
		rader.renderPart("direct");
		VertexConsumer vb2 = bufferSource.getBuffer(rt2);
		rader.setRender(null, vb2, stack, packedLight);
		rader.renderPart("base");
		stack.pushPose();
		stack.mulPose(Axis.ZP.rotationDegrees(raderrote));
		VertexConsumer vb3 = bufferSource.getBuffer(rt3);
		rader.setRender(null, vb3, stack, packedLight);
		rader.renderPart("serch");
		stack.popPose();
		stack.mulPose(Axis.ZP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(rote));
		VertexConsumer vb4 = bufferSource.getBuffer(rt4);
		rader.setRender(null, vb4, stack, packedLight);
		rader.renderPart("look");
		stack.popPose();
		stack.popPose();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderRtsKey(PoseStack stack, float i, float j, boolean click, int id) {
		float size = 0.4F;
		Minecraft mc = Minecraft.getInstance();
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		RenderType rt = SARenderState.getBlendWrite_Light(rts_tex);
		VertexConsumer vb = bufferSource.getBuffer(rt);
		rts_icon.setRender(null, vb, stack, packedLight);
		//icon.setColor(1.0F, 1.0F, 1.0F, alpha);
		stack.pushPose();
		stack.translate(i, j, 5);
		stack.scale(15F*(i1/427F)*size, 15F*(j1/240F)*size, 15F);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		rts_icon.renderPart("c1");
		rts_icon.renderPart("back");
		if(click){
			rts_icon.renderPart("c2");
		}
		if(id==1){
			rts_icon.renderPart("attack");
			rts_icon.renderPart("ctrl");
			rts_icon.renderPart("add");
			rts_icon.renderPart("left");
		}else if(id==2){
			rts_icon.renderPart("free");
			rts_icon.renderPart("keyg");
		}else if(id==3){
			rts_icon.renderPart("defend");
			rts_icon.renderPart("keyx");
		}else if(id==4){
			rts_icon.renderPart("all");
			rts_icon.renderPart("keyv");
		}else if(id==5){
			rts_icon.renderPart("drone");
			rts_icon.renderPart("shift");
			rts_icon.renderPart("add");
			rts_icon.renderPart("right");
		}else if(id==6){
			rts_icon.renderPart("follow");
			rts_icon.renderPart("keyc");
		}else if(id==7){
			rts_icon.renderPart("leave");
			rts_icon.renderPart("keyb");
		}
		//rts_icon.setColor(1.0F, 1.0F, 1.0F, 1F);
		stack.popPose();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderTankRote(PoseStack stack, int i, int j, EntityWMVehicleBase entity, Player player, EntityWMSeat seat, int i1) {
		float size = (float)(entity.getHealth()/entity.getMaxHealth());
		float size1 = (float)(player.getHealth()/player.getMaxHealth());
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		
		RenderType rt1 = SARenderState.getBlendWrite_Light(ResourceLocation.tryParse("wmlib:textures/hud/player_health.png"));
		RenderType rt2 = SARenderState.getBlendWrite_Light(ResourceLocation.tryParse("wmlib:textures/hud/vehicle_health.png"));
		RenderType rt4 = SARenderState.getBlendWrite_NoLight(ResourceLocation.tryParse("wmlib:textures/hud/tank64.png"));
		stack.pushPose();

		stack.pushPose();
		stack.translate((float)(i / 2), (float)(j *8/9), 5);
		stack.scale(15F*(i/427F), 15F*(j/240F), 15F);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));

		if(size>0.7){
			icon.setColor(0F, 0.8F, 1F, 1);
		}else if(size>0.4){
			icon.setColor(0F+(1.2F-size), 0.8F, 0F, 1);
		}else{
			icon.setColor(0.5F+size*0.5F, 0.1F, 0F, 1);
		}
		
		if(i1==0 && entity.icon1tex!=null){
			RenderType rt = SARenderState.getBlendWrite_Light(entity.icon1tex);
			VertexConsumer vb = bufferSource.getBuffer(rt);
			icon.setRender(null, vb, stack, packedLight);
			icon.renderPart("tank1");
		}
		icon.setColor(0F+(1-size1), 1F-(1-size1), 0F, 1);
		VertexConsumer vb2 = bufferSource.getBuffer(rt1);
		icon.setRender(null, vb2, stack, packedLight);
		icon.renderPart("ph");
		
		if(size>0.7){
			icon.setColor(0F, 0.8F, 1F, 1);
		}else if(size>0.4){
			icon.setColor(0F+(1.2F-size), 0.8F, 0F, 1);
		}else{
			icon.setColor(0.5F+size*0.5F, 0.1F, 0F, 1);
		}
		VertexConsumer vb3 = bufferSource.getBuffer(rt2);
		icon.setRender(null, vb3, stack, packedLight);
		icon.renderPart("vh");
		
		float rote = entity.turretYaw;
		if(i1>0){
			rote = seat.turretYaw;
		}
		stack.mulPose(Axis.ZP.rotationDegrees((rote-entity.getYRot())));
		if(entity.icon2tex!=null){
			RenderType rt3 = SARenderState.getBlendWrite_Light(entity.icon2tex);
			VertexConsumer vb4 = bufferSource.getBuffer(rt3);
			icon.setRender(null, vb4, stack, packedLight);
			icon.renderPart("tank2");
		}
		
		icon.setColor(1, 1, 1, 1);
		VertexConsumer vb5 = bufferSource.getBuffer(rt4);
		icon.setRender(null, vb5, stack, packedLight);
		if(seat.minyaw>-360F){
			stack.pushPose();
			stack.mulPose(Axis.ZP.rotationDegrees(seat.minyaw));
			icon.renderPart("seatyaw");
			stack.popPose();
		}
		if(seat.maxyaw<360F){
			stack.pushPose();
			stack.mulPose(Axis.ZP.rotationDegrees(seat.maxyaw));
			icon.renderPart("seatyaw");
			stack.popPose();
		}
		if(i1>0){
			stack.mulPose(Axis.ZP.rotationDegrees(-(rote-entity.getYRot())));
			icon.renderPart("seataim");
		}
		stack.popPose();
		stack.popPose();
		bufferSource.endBatch();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderFighterHud(PoseStack stack, int i, int j, String texture, EntityWMVehicleBase entity, float rote) {
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		RenderType rt = SARenderState.getBlendDepthWrite(hudtex);
		RenderType rt2 = SARenderState.getBlendDepthWrite(sp);
		RenderType rt3 = SARenderState.getBlendDepthWrite(cz);
		
		hudf.setColor(0F, 0.8F, 1F, 1);
		VertexConsumer vb = bufferSource.getBuffer(rt);
		hudf.setRender(null, vb, stack, packedLight);
		
		stack.pushPose();
		stack.translate((float)(i / 2), (float)(j / 2), 5);
		stack.scale(15F*(i/427F), 15F*(j/240F), 15F);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		if(entity.is_aa){
			hudf.renderPart("bar");
		}else{
			hudf.renderPart("bar2");
		}
		stack.pushPose();
		
		stack.pushPose();
		stack.translate(0, -2F, 0);
		hudf.renderPart("bar1");
		if(entity.getMoveMode()==0)hudf.renderPart("stay");
		stack.pushPose();
		float size = ((float)entity.movePower)/entity.throttleMax;
		stack.scale(1, size, 1);
		hudf.renderPart("power1");
		stack.popPose();
		
		stack.pushPose();
		float size2 = ((float)entity.throttle)/entity.throttleMax;
		stack.translate(0, size2*5F, 0);
		hudf.renderPart("throttle1");
		stack.popPose();
		stack.popPose();
		
		VertexConsumer vb2 = bufferSource.getBuffer(rt2);
		hudf.setRender(null, vb2, stack, packedLight);

		hudf.setMoveTex(rote*0.0025F, 0);
		hudf.renderPart("sp");
		hudf.setMoveTex(0, 0);
		VertexConsumer vb3 = bufferSource.getBuffer(rt);
		hudf.setRender(null, vb3, stack, packedLight);
		stack.mulPose(Axis.ZP.rotationDegrees(entity.flyRoll));
		if(!entity.is_aa)hudf.renderPart("sp2");
		
		VertexConsumer vb4 = bufferSource.getBuffer(rt3);
		hudf.setRender(null, vb4, stack, packedLight);

		hudf.setMoveTex(0, entity.flyPitch*0.0075F);
		hudf.renderPart("cz");
		hudf.setMoveTex(0, 0);
		VertexConsumer vb5 = bufferSource.getBuffer(rt);
		hudf.setRender(null, vb5, stack, packedLight);
		
		stack.popPose();
		stack.pushPose();
		hudf.renderPart("aim");
		//stack.translate(0, -entity.flyPitch*0.02F, 0F);
		//hudf.renderPart("up");
		stack.popPose();
		hudf.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
		stack.popPose();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderHeliHud(PoseStack stack, int i, int j, String texture, EntityWMVehicleBase entity, float rote) {
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		RenderType rt = SARenderState.getBlendDepthWrite(hudtex);
		RenderType rt2 = SARenderState.getBlendDepthWrite(sp);
		RenderType rt3 = SARenderState.getBlendDepthWrite(cz);

		hudh.setColor(0F, 0.8F, 1F, 1);
		VertexConsumer vb = bufferSource.getBuffer(rt);
		hudh.setRender(null, vb, stack, packedLight);

		stack.translate((float)(i / 2), (float)(j / 2), 5);
		stack.scale(15F*(i/427F), 15F*(j/240F), 15F);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		
		stack.pushPose();
		
		stack.pushPose();
		stack.mulPose(Axis.ZP.rotationDegrees(entity.flyRoll));
		hudh.renderPart("sp1");
		stack.popPose();
		
		stack.pushPose();
		hudh.renderPart("bar1");
		stack.translate(0, -2.5F, 0.6F);
		if(entity.movePower>entity.throttleMax*0.6F){
			hudh.renderPart("up");
		}else if(entity.movePower<=entity.throttleMax*0.45F){
			hudh.renderPart("down");
		}else{
			hudh.renderPart("m");
		}
		
		hudh.renderPart("bar");
		stack.pushPose();
		float size = ((float)entity.movePower)/entity.throttleMax;
		stack.scale(1, size, 1);
		hudh.renderPart("power1");
		stack.popPose();
		
		stack.pushPose();
		float size2 = ((float)entity.throttle)/entity.throttleMax;
		stack.scale(1, size2, 1);
		hudh.renderPart("throttle1");
		stack.popPose();
		if(entity.getMoveMode()==1)hudh.renderPart("hower");
		stack.popPose();
		
		stack.pushPose();
		stack.pushPose();
		VertexConsumer vb2 = bufferSource.getBuffer(rt2);
		hudh.setRender(null, vb2, stack, packedLight);
		
		stack.pushPose();
		hudh.setMoveTex(rote*0.0025F, 0);
		hudh.renderPart("sp");
		hudh.setMoveTex(0, 0);
		stack.popPose();
		
		stack.pushPose();
		stack.mulPose(Axis.ZP.rotationDegrees(entity.flyRoll));
		VertexConsumer vb3 = bufferSource.getBuffer(rt3);
		hudh.setRender(null, vb3, stack, packedLight);
		stack.pushPose();
		hudh.setMoveTex(0, entity.flyPitch*0.0075F);
		hudh.renderPart("cz");
		hudh.setMoveTex(0, 0);
		stack.popPose();
		stack.popPose();
		
		stack.popPose();
		stack.popPose();
		
		VertexConsumer vb4 = bufferSource.getBuffer(rt);
		hudh.setRender(null, vb4, stack, packedLight);
		stack.pushPose();
		hudh.renderPart("aim");
		/*stack.translate(0, -entity.flyPitch*0.02F, 0F);
		hudh.renderPart("bar2");*/
		stack.popPose();
		
		stack.popPose();
		hudh.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderNightV(PoseStack stack, int i, int j) {
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		RenderType rt = SARenderState.getNightLight(nighttex);
		VertexConsumer vb = bufferSource.getBuffer(rt);
		box.setRender(null, vb, stack, packedLight);
		stack.translate((float)(i / 2), (float)(j / 2), 5);
		stack.scale(15F*(i/427F), 15F*(j/240F), 15F);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		box.renderPart("night");
		stack.popPose();
		bufferSource.endBatch();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderBlack(PoseStack stack, int i, int j, float time) {
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		RenderType rt = SARenderState.getBlendWrite_NoLight(boxtex);
		VertexConsumer vb = bufferSource.getBuffer(rt);
		box.setRender(null, vb, stack, packedLight);
		box.setColor(1.0F, 1.0F, 1.0F, time);
		stack.translate((float)(i / 2), (float)(j / 2), 5);
		stack.scale(15F*(i/427F), 15F*(j/240F), 15F);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		box.renderPart("black");
		box.setColor(1, 1, 1, 1);
		stack.popPose();
		bufferSource.endBatch();
	}
	
    private static float angleBetween(Vec2f first, Vec2f second) {
        double dot = first.x * second.x + first.y * second.y;
        double cross = first.x * second.y - second.x * first.y;
        double res = Math.atan2(cross, dot) * 180 / Math.PI;
        return (float)res;
    }
    private static Vec3 calculateViewVector(float pPitch, float pYaw) {
        float f = pPitch * ((float) Math.PI / 180F);
        float f1 = -pYaw * ((float) Math.PI / 180F);
        double f2 = Math.cos(f1);
        double f3 = Math.sin(f1);
        double f4 = Math.cos(f);
        double f5 = Math.sin(f);
        return new Vec3(f3 * f4, -f5, f2 * f4);
    }
	
	public int mitargetX;
	public int mitargetY;
	public int mitargetZ;
	@OnlyIn(Dist.CLIENT)
	protected void renderBox(PoseStack stack, String objname, String texture, float partialTicks, Entity seat, Entity target, Player player, boolean isAim, float rote, boolean haveMissile,float btime) {
		Minecraft mc = Minecraft.getInstance();
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		SAObjModel obj = new SAObjModel(objname);
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		MultiBufferSource.BufferSource bufferSource2 = mc.renderBuffers().bufferSource();
		
		int packedLight = 0xF000F0;
		RenderType rt = SARenderState.getBlendWrite_NoLight(ResourceLocation.tryParse(texture));
		RenderType rt2 = SARenderState.getBlendWrite_Light(ResourceLocation.tryParse(texture));
		RenderType rtsp = SARenderState.getBlendWrite_Light(sp);
		
		//RenderSystem.setShaderTexture(0, ResourceLocation.tryParse(texture));
		stack.pushPose();
		stack.translate((float)(i1 / 2), (float)(j1 / 2), 5);
		stack.scale(15F*(i1/427F), 15F*(j1/240F), 15F);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		float gunRotX = RenderParameters.GUN_ROT_X_LAST + (RenderParameters.GUN_ROT_X - RenderParameters.GUN_ROT_X_LAST) * partialTicks;
		float gunRotY = RenderParameters.GUN_ROT_Y_LAST + (RenderParameters.GUN_ROT_Y - RenderParameters.GUN_ROT_Y_LAST) * partialTicks;
		//float jump = -RenderParameters.playerRecoilPitch*5*0.08F;
		//float cock = 5*RenderParameters.playerRecoilPitch*0.2F;
		stack.mulPose(Axis.YP.rotationDegrees(gunRotX*-2F));
		stack.mulPose(Axis.ZP.rotationDegrees(gunRotY*-2F));
		stack.translate(gunRotX*0.1F, 0.0F, 0);
		stack.translate(0.0F, gunRotY, 0);
		//stack.translate(0.0F, 0.0F, cock);
		//stack.mulPose(Axis.XP.rotationDegrees(jump));
		VertexConsumer vb = bufferSource.getBuffer(rt);
		obj.setRender(null, vb, stack, packedLight);
		if(isAim){
			obj.renderPart("boxaim");
		}else{
			obj.renderPart("box");
		}
		
		VertexConsumer vb2 = bufferSource.getBuffer(rt2);
		obj.setRender(null, vb2, stack, packedLight);
		if(isAim){
			obj.renderPart("aim2");
			if(haveMissile)obj.renderPart("missile2");
		}else{
			obj.renderPart("aim1");
			if(haveMissile)obj.renderPart("missile1");
		}
		float alpha1 = (btime < 25) ? 1.0f - (btime / 25.0f) : 0.0f;
		float alpha2 = (btime < 50) ? 1.0f - (Math.max(0, btime - 25) / 25.0f) : 0.0f;
		float alpha3 = (btime < 75) ? 1.0f - (Math.max(0, btime - 50) / 25.0f) : 0.0f;
		float alpha4 = (btime < 100) ? 1.0f - (Math.max(0, btime - 75) / 25.0f) : 0.0f;
		
		obj.setColor(1.0F, 1.0F, 1.0F, alpha1);
		obj.renderPart("black1");
		obj.setColor(1.0F, 1.0F, 1.0F, alpha2);
		obj.renderPart("black2");
		obj.setColor(1.0F, 1.0F, 1.0F, alpha3);
		obj.renderPart("black3");
		obj.setColor(1.0F, 1.0F, 1.0F, alpha4);
		obj.renderPart("black4");
		obj.setColor(1.0F, 1.0F, 1.0F, 1F);
		
		//RenderSystem.setShaderTexture(0, sp);
		VertexConsumer vb3 = bufferSource.getBuffer(rtsp);
		obj.setRender(null, vb3, stack, packedLight);

		obj.setMoveTex(rote*0.0025F, 0);
		obj.renderPart("sp");
		obj.setMoveTex(0, 0);

		//RenderSystem.setShaderTexture(0, ResourceLocation.tryParse(texture));
		VertexConsumer vb4 = bufferSource.getBuffer(rt2);
		obj.setRender(null, vb4, stack, packedLight);
		
		obj.renderPart("info");
		obj.setColor(1.0F, 1.0F, 1.0F, 0.8F);
		obj.renderPart("hud");
		if(target!=null){
			obj.renderPart("lock");
			float d = seat.distanceTo(target);
			Vec3 locken = Vec3.directionFromRotation(player.getRotationVector());//getLookAngle
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
			Vec3 viewVec = calculateViewVector(0, player.getViewYRot(0));
			Vec2f lookVec = new Vec2f((float)viewVec.x, (float)viewVec.z);
			Vec2f playerPos = new Vec2f((float)mitargetX, (float)mitargetZ);
			Vec3 sourceVec3d = new Vec3(target.getX(), target.getY(), target.getZ());
			Vec2f diff = new Vec2f((float)(sourceVec3d.x - playerPos.x), (float)(sourceVec3d.z - playerPos.y));
			float angleBetween = angleBetween(lookVec, diff);
			double d5 = target.getX() - mitargetX;
			double d6 = target.getY() - mitargetY;
			double d7 = target.getZ() - mitargetZ;
			double dis = Math.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
			/*if(d6<0)*/angleBetween = -angleBetween-180F;
			stack.mulPose(Axis.ZP.rotationDegrees(-angleBetween));
			stack.translate(0F, 0F, 0F);
			stack.scale(1, (float)dis, 1);
			stack.translate(0F, 0F, 0F);
			obj.renderPart("line");
		}
		obj.setColor(1.0F, 1.0F, 1.0F, 1F);
		stack.popPose();
		bufferSource.endBatch();
		bufferSource2.endBatch();
	}
	
	
	
	@OnlyIn(Dist.CLIENT)
	protected void renderIcon(PoseStack stack, int i, int j, String texture, String name, float size, float alpha) {
		if(texture!=null){
		Minecraft mc = Minecraft.getInstance();
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
			MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
			int packedLight = 0xF000F0;
			RenderType rt = SARenderState.getBlendWrite_Light(ResourceLocation.tryParse(texture));
			VertexConsumer vb = bufferSource.getBuffer(rt);
			icon.setRender(null, vb, stack, packedLight);
			//icon.setColor(1.0F, 1.0F, 1.0F, alpha);
			stack.pushPose();
			stack.translate((float)(i / 2), (float)(j / 2), 5);
			stack.scale(15F*(i1/427F)*size, 15F*(j1/240F)*size, 15F*size);
			stack.mulPose(Axis.YP.rotationDegrees(-180F));
			stack.mulPose(Axis.ZP.rotationDegrees(180F));
			icon.renderPart(name);
			//icon.setColor(1.0F, 1.0F, 1.0F, 1F);
			stack.popPose();
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void renderTeamCount(PoseStack stack, float i, float j, float id, float bar) {
		Minecraft mc = Minecraft.getInstance();
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		RenderType rt = SARenderState.getBlendWrite_Light(color);
		VertexConsumer vb = bufferSource.getBuffer(rt);
		health.setRender(null, vb, stack, packedLight);
		health.setColor(1.0F, 1.0F, 1.0F, 0.9F);
		stack.pushPose();
		stack.translate(i / 2,j / 2, 5);
		stack.scale(15F*(i1/427F), 15F*(j1/240F), 15F);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		stack.pushPose();
		if(id==0){
			//
			health.renderPart("man");
			health.setColor(0, 0.8F, 1F, 0.9F);
			health.renderPart("man2");
			stack.translate(0,0,0);
			stack.scale(bar, 1F, 1F);
			stack.translate(0,0,0);
			health.renderPart("man1");
		}else{
			//stack.translate(10,0,0);
			health.renderPart("mob");
			health.setColor(1, 0.3F, 0.3F, 0.9F);
			health.renderPart("mob2");
			stack.translate(0,0,0);
			stack.scale(bar, 1F, 1F);
			stack.translate(0,0,0);
			health.renderPart("mob1");
		}
		stack.popPose();
		stack.popPose();
		health.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	int x1 = WMConfig.hit_icon_x;
	int y1 = WMConfig.hit_icon_y;
	int x2 = WMConfig.hit_infor_x;
	int y2 = WMConfig.hit_infor_y;
	int iii;
	static boolean appliedMouseSlow = false;

	static float scaletime = 0;
	static float scaletime2 = 0;
	static float scaletime3 = 0;
	static int skeleton = 0;
	static int blacktime = 0;
	static float blacktime2 = 0;
	static int zoomtime = 0;

    /*private static final int MAP_SIZE = 64; // 小地图尺寸
    private static final int MAP_RADIUS = MAP_SIZE / 2;
    private static final int MAP_SCALE = 2; // 地图缩放比例，每个方块对应的像素数
	private void renderMinimap(GuiGraphics guiGraphics, Level level, double centerX, double centerZ, int screenX, int screenY) {
        //RenderSystem.disableBlend();
		RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.fill(screenX, screenY, screenX + MAP_SIZE, screenY + MAP_SIZE, 0x80000000);
        int startX = (int) centerX - MAP_RADIUS / MAP_SCALE;
        int startZ = (int) centerZ - MAP_RADIUS / MAP_SCALE;
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int z = 0; z < MAP_SIZE; z++) {
                int worldX = startX + x / MAP_SCALE;
                int worldZ = startZ + z / MAP_SCALE;
				int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z)-1;
				if (surfaceY < 255 || surfaceY > -100) {
					return 0;
				}
				BlockPos pos = new BlockPos(worldX, surfaceY, worldZ);
				BlockState state = level.getBlockState(pos);
				MapColor mapColor = state.getMapColor(level, pos);
				int color = 0x666666;
				if (mapColor != null) {
					color = mapColor.col;
				}
                if (color != 0) {
                    guiGraphics.fill(screenX + x, screenY + z, screenX + x + 1, screenY + z + 1, color | 0xFF000000);
                }
            }
        }
        int playerMarkerX = screenX + MAP_RADIUS;
        int playerMarkerY = screenY + MAP_RADIUS;
        guiGraphics.fill(playerMarkerX - 1, playerMarkerY - 1, playerMarkerX + 2, playerMarkerY + 2, 0xFFFF0000);
        //RenderSystem.disableBlend();
    }
    private int getMinecraftBlockColor(Level level, BlockPos pos, BlockState state) {
        try {
            if (Minecraft.getInstance().getBlockColors() != null) {
                int color = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
                if (color != -1) {
                    return color;
                }
            }
			return 0x888888;
        } catch (Exception e) {
            return 0x888888;
        }
    }*/

    /*return getFallbackColor(state.toString());
    private int getFallbackColor(String blockName) {
        // 简单的基于字符串的哈希颜色
        int hash = blockName.hashCode();
        return (hash & 0x00FFFFFF) | 0x80000000; // 确保有一定透明度
    }*/
	public boolean isZoom;
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

	double raderposX=0;
	double raderposZ=0;
	float raderWidth = 0;
	float raderHeight = 0;
	float raderRote = 0;
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEvent(RenderGuiOverlayEvent.Post event) {
		if (event.getOverlay() != VanillaGuiOverlay.ITEM_NAME .type())return;
		Minecraft mc = Minecraft.getInstance();
		int i = mc.getWindow().getGuiScaledWidth();
		int j = mc.getWindow().getGuiScaledHeight();
		Player entityplayer = mc.player;
		GuiGraphics guigraphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());
		PoseStack stack = guigraphics.pose();
		Font fontrender = mc.font;
		float partialTicks = event.getPartialTick()/*mc.getFrameTime()*/;
		if(entityplayer != null){
			/*double playerX = entityplayer.getX();
			double playerZ = entityplayer.getZ();
			int screenX = i - MAP_SIZE - 10;
			int screenY = 10;
			renderMinimap(guigraphics, mc.level, playerX, playerZ, screenX, screenY);*/
			CompoundTag nbts = entityplayer.getPersistentData();
			int nb = nbts.getInt("hitentity");
			int nbd = nbts.getInt("hitentitydead");
			int nbdh = nbts.getInt("hitentity_headshot");
			int hitid = nbts.getInt("hitentity_id");
			float nbtdamage = nbts.getFloat("hitdamage");
			String nbtname = null;
			Entity hitTarget = entityplayer.getCommandSenderWorld().getEntity(hitid);
			if(hitTarget!=null)nbtname = hitTarget.getName().getString();
			
			stack.pushPose();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			stack.scale(1F, 1F, 1);
			if(nbd >= 1)
			{
				if(nbdh >= 1){
					if(nbtname!=null)guigraphics.drawString(fontrender, "爆头击杀"+nbtname+"+"+nbtdamage, i/2 - 15+x2,  j/2+30+y2, Color.YELLOW.getRGB());
				}else{
					if(nbtname!=null)guigraphics.drawString(fontrender, "击杀"+nbtname+"+"+nbtdamage, i/2 - 15+x2,  j/2+30+y2, Color.RED.getRGB());
				}
			}
			stack.scale(0.0625f, 0.0625f, 1);
			stack.popPose();
			
			stack.pushPose();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			stack.scale(1.4F, 1.25F, 1.25F);
			float all = 0;
			if(nb >= 90){//总分
				all=all+nbtdamage;
				guigraphics.drawString(fontrender, ""+all,235*i/683, 160*j/353, Color.WHITE.getRGB());
			}
			stack.scale(0.0625f, 0.0625f, 1);
			stack.popPose();
			
			stack.pushPose();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			int roll = 0;//下滑
			int roll2 = 0;//下滑
			if(nb >= 1){
				if(nbdh >= 1)
				{
					if(nbdh<=10){
						roll=nbdh;
					}else{
						roll=10;
					}
					guigraphics.drawString(fontrender, "爆头命中"+nbtname+"+"+nbtdamage, i/2 - 15+x2,  j/2+50-roll+y2, Color.RED.getRGB());
					nbts.putInt("hitentity_headshot", nbdh-1);
				}else{
					if(nb<=10){
						roll2=nb;
					}else{
						roll2=10;
					}
					guigraphics.drawString(fontrender, "命中"+nbtname+"+"+nbtdamage, i/2 - 15+x2,  j/2+50-roll2+y2, 0xFFFFFF);
				}
			}
			stack.popPose();
			
			
			boolean renderRader = false;
			boolean renderRts = false;
			raderRote=-entityplayer.getYRot();
			float size1 = (float)(entityplayer.getHealth()/entityplayer.getMaxHealth());
			if (entityplayer.getVehicle() instanceof EntityWMSeat && entityplayer.getVehicle() != null && !(mc.screen instanceof RtsMoShiScreen)) {
				EntityWMSeat seat = (EntityWMSeat) entityplayer.getVehicle();
				mousespeed = seat.turretSpeed;
				if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
					if(blacktime>10 && blacktime2<100)++blacktime2;
					EntityWMVehicleBase ve = (EntityWMVehicleBase) (seat.getVehicle());
					int i1 = ve.getPassengers().indexOf(seat);
					float color = (float)(ve.getHealth()/ve.getMaxHealth());
					int landMoveYaw = (int) (entityplayer.yHeadRot - ve.turretYaw);
					while (landMoveYaw < -180.0f) {
						landMoveYaw += 360.0f;
					}
					while (landMoveYaw >= 180.0f) {
						landMoveYaw -= 360.0f;
					}
					int landMovePitch = (int) (entityplayer.getXRot() - ve.turretPitch);
					
					int airMoveYaw = (int) (entityplayer.yHeadRot - ve.yHeadRot);
					while (airMoveYaw < -180.0f) {
						airMoveYaw += 360.0f;
					}
					while (airMoveYaw >= 180.0f) {
						airMoveYaw -= 360.0f;
					}
					int airMovePitch = (int) (entityplayer.getXRot() - ve.getXRot());
					
					int yy = (int) (entityplayer.getYHeadRot() - ve.getYRot());
					int xx = (int) (entityplayer.getXRot() - ve.getXRot());
					if(mc.options.getCameraType() != CameraType.THIRD_PERSON_BACK) {
						if(!this.appliedMouseSlow){
							// 获取当前灵敏度并应用修改
							double currentSensitivity = mc.options.sensitivity().get();
							mc.options.sensitivity().set(currentSensitivity * mousespeed);
							this.appliedMouseSlow = true;
						}
					} else if(this.appliedMouseSlow) {
						double currentSensitivity = mc.options.sensitivity().get();
						if(1F/mousespeed < 0.5F){
							mc.options.sensitivity().set(currentSensitivity * (1F/mousespeed));
						} else {
							mc.options.sensitivity().set(0.5); // 注意: 灵敏度现在是 double 类型
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
					if(mc.options.getCameraType() == CameraType.FIRST_PERSON) {
						if(seat.canNightV&&seat.openNightV)this.renderNightV(stack,i, j);
						if(seat.render_hud_box){
							this.renderBox(stack,seat.hud_box_obj, seat.hud_box_tex, partialTicks,seat,seat.mitarget,entityplayer, seat.isZoom, seat.turretYawO + (seat.turretYaw - seat.turretYawO) * partialTicks, ve.havemissile, blacktime2);
						}
						if(i1==0){
							if(ve.VehicleType == 3)this.renderHeliHud(stack,i, j, seat.hudOverlayZoom, ve, ve.turretYawO + (ve.turretYaw - ve.turretYawO) * partialTicks);
							if(ve.VehicleType == 4)this.renderFighterHud(stack,i, j, seat.hudOverlayZoom, ve, ve.turretYawO + (ve.turretYaw - ve.turretYawO) * partialTicks);
							if(ve.VehicleType > 2){
								render_count(stack,mc, i*0.78F, j*0.44F, 0, 0, (int)(ve.getY()), false, 4, color);//
								render_count(stack,mc, i*0.17F, j*0.44F, 0, 0, (int)(ve.speedKPH), false, 5, color);//
							}else{
								render_count(stack,mc, i*0.76F, j*0.25F, 0, 0, (int)(ve.speedKPH), false, 6, color);//
								render_count(stack,mc, i*0.2F, j*0.25F, 0, 0, (int)(90-ve.turretPitch), false, 7, color);//
							}
						}else{
							render_count(stack,mc, i*0.76F, j*0.25F, 0, 0, (int)(ve.speedKPH), false, 6, color);//
							render_count(stack,mc, i*0.2F, j*0.25F, 0, 0, (int)(90-seat.turretPitch), false, 7, color);//
						}
						//mc.options.hideGui = true;
						if(ve.getArmyType1()>0){
							if(ve.VehicleType>2){
								this.renderIcon(stack,i-yy*5, j-xx*3, "wmlib:textures/hud/tank64.png", "airlock", 1, 0.9F);
								this.renderIcon(stack,i-airMoveYaw*5, j+12-airMovePitch*3, "wmlib:textures/hud/tank64.png", "airaim", 1, 0.9F);
							}else{
								this.renderIcon(stack,i-landMoveYaw*5, j-landMovePitch*3, "wmlib:textures/hud/lock.png", "lock", 1, 0.9F);
							}
						}
						if(seat.isZoom && seat.renderHudOverlayZoom) {
							this.renderHud(guigraphics, stack,i, j, seat.hudOverlayZoom, partialTicks);
						}else {
							if(seat.renderHudOverlay)this.renderHud(guigraphics, stack, i, j, seat.hudOverlay, partialTicks);
							if(seat.renderHudIcon)this.renderIcon(stack,i, j, seat.hudIcon, "icon", 1, 0.9F);
						}
					}else if(mc.options.getCameraType() == CameraType.THIRD_PERSON_BACK){
						//mc.options.hideGui = true;
						if(ve.isthrow){
							render_count(stack,mc, i*0.2F, j*0.25F, 0, 0, (int)(90-ve.turretPitch), false, 7, color);
						}else{
							if(ve.VehicleType>2){
								this.renderIcon(stack,i-landMoveYaw*5, j+12-landMovePitch*3, "wmlib:textures/hud/tank64.png", "airlock", 1, 0.9F);
								this.renderIcon(stack,i-airMoveYaw*5, j+12-airMovePitch*3, "wmlib:textures/hud/tank64.png", "airaim", 1, 0.9F);
							}else{
								this.renderIcon(stack,i, j+12, "wmlib:textures/hud/aim.png", "aim", 1, 0.9F);
								this.renderIcon(stack,i-landMoveYaw*5, j-landMovePitch*3, "wmlib:textures/hud/lock.png", "lock", 1, 0.9F);
							}
						}
					}
					{
						stack.pushPose();
						stack.mulPose(Axis.XP.rotationDegrees(-8F));
						renderTankRote(stack,i, j, ve, entityplayer, seat, i1);
						renderWeapon(stack,i, j, ve, seat, i1);
						render_count(stack,mc, i*0.6F, j*0.89F, 0, 0, (int)(ve.getHealth()*100/ve.getMaxHealth()), false, 0,color);//
						render_count(stack,mc, i*0.355F, j*0.89F, 0, 0, (int)(entityplayer.getHealth()*100/entityplayer.getMaxHealth()), false, 2,size1);//
						render_count(stack,mc, i*0.95F, j*5/7, 0, 0, 0, true, 1,color);//
						if(i1==0){
							if(ve.getArmyType2()==0){
								this.weaponname = ve.w1name;
								render_count(stack,mc, i*0.86F, j*5/7, 0, 0, ve.getRemain1(), false, 1,color);//
								if(ve.weaponCount==4){
									render_count(stack,mc, i*0.75F, j*0.888F, 0, 0, ve.getRemain2(), false, 3,color);//
									render_count(stack,mc, i*0.835F, j*0.888F, 0, 0, ve.getRemain3(), false, 3,color);//
									render_count(stack,mc, i*0.918F, j*0.888F, 0, 0, ve.getRemain4(), false, 3,color);//
								}
							}else if(ve.getArmyType2()==1){
								this.weaponname = ve.w4name;
								render_count(stack,mc, i*0.86F, j*5/7, 0, 0, ve.getRemain4(), false, 1,color);//
								if(ve.weaponCount==4){
									render_count(stack,mc, i*0.75F, j*0.888F, 0, 0, ve.getRemain2(), false, 3,color);//
									render_count(stack,mc, i*0.835F, j*0.888F, 0, 0, ve.getRemain3(), false, 3,color);//
									render_count(stack,mc, i*0.677F, j*0.888F, 0, 0, ve.getRemain1(), false, 3,color);//
								}
							}else{//2
								this.weaponname = ve.w2name;
								render_count(stack,mc, i*0.86F, j*5/7, 0, 0, ve.getRemain2(), false, 1,color);//
								if(ve.weaponCount==4){
									render_count(stack,mc, i*0.677F, j*0.888F, 0, 0, ve.getRemain1(), false, 3,color);//
									render_count(stack,mc, i*0.835F, j*0.888F, 0, 0, ve.getRemain3(), false, 3,color);//
									render_count(stack,mc, i*0.918F, j*0.888F, 0, 0, ve.getRemain4(), false, 3,color);//
								}
							}
						}else{
							this.weaponname = seat.w1name;
							render_count(stack,mc, i*0.86F, j*5/7, 0, 0, seat.getRemain1(), false, 1,color);//
							if(seat.weaponCount==2){
								render_count(stack,mc, i*0.85F, j*0.888F, 0, 0, seat.getRemain2(), false, 3,color);//
							}
						}
						stack.popPose();
					}
					if(ve.VehicleType>2){
						renderRader=true;
					}
					{
						if(seat.isZoom){
							if(zoomtime>0)zoomtime=0;
							if(blacktime<40){
								++blacktime;
								if(ve.VehicleType<3){
									if(blacktime<20){
										renderBlack(stack,i, j, blacktime*0.05F);
									}else{
										renderBlack(stack,i, j, (40-blacktime)*0.05F);
									}
								}
							}
						}else{
							if(zoomtime<1)blacktime=0;
							if(zoomtime<11)++zoomtime;
							if(blacktime<20){
								++blacktime;
								if(ve.VehicleType<3){
									if(blacktime<10){
										renderBlack(stack,i, j, 1);
									}else{
										renderBlack(stack,i, j, (20-blacktime)*0.1F);
									}
								}
							}
						}
					}
				}
			}else{
				blacktime = 0;
				blacktime2 = 0;
				if(this.h1!=0)this.h1=0;
				//mc.options.hideGui = false;
				if(this.appliedMouseSlow) {//
					if(1F/mousespeed<0.5F){
						double currentSensitivity = mc.options.sensitivity().get();
						mc.options.sensitivity().set(currentSensitivity * (1F/mousespeed));
					}else{
						mc.options.sensitivity().set(0.5);
					}
					this.appliedMouseSlow = false;
				}
				ItemStack heldItem = entityplayer.getMainHandItem();
				ItemStack heldItem2 = entityplayer.getOffhandItem();
				if(heldItem.getItem() instanceof IRaderItem){
					IRaderItem ritem1 = (IRaderItem)heldItem.getItem();
					renderRader=true;
					if(ritem1.getType()==1)renderRts=true;
				}
				if(heldItem2.getItem() instanceof IRaderItem){
					IRaderItem ritem2 = (IRaderItem)heldItem2.getItem();
					renderRader=true;
					if(ritem2.getType()==1)renderRts=true;
				}
				
				if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun) {
					ItemGun gun = (ItemGun) heldItem.getItem();
					if (gun.aim_time>=gun.time_aim) {
						if(gun.aim_tex!=null) {
							this.renderHud(guigraphics, stack,i, j, gun.aim_tex, partialTicks);
						}
						//mc.options.hideGui = true;
					} else {
						//mc.options.hideGui = false;
					}
					this.isZoom = true;
				} else {
					if (this.isZoom == true) {
						//mc.options.hideGui = false;
						this.isZoom = false;
					}
				}
			}
			raderposX=entityplayer.getX();
			raderposZ=entityplayer.getZ();
			if(mc.screen!=null && mc.screen instanceof RtsMoShiScreen){
				RtsMoShiScreen rts =(RtsMoShiScreen)mc.screen;
				if(rts.cameraID!=-1){
					Entity ent = entityplayer.level().getEntity(rts.cameraID);
					if(ent!=null && ent instanceof XiangJiEntity){
						raderposX=ent.getX();
						raderposZ=ent.getZ();
						raderRote=-ent.getYRot();
						XiangJiEntity xj = (XiangJiEntity)ent;
						stack.pushPose();
						RenderSystem.enableBlend();
						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
						stack.scale(1F, 1F, 1);
						int h = 15;
						guigraphics.drawString(fontrender, "相机移动X:"+xj.getMoveX(), 0 + 20,  30+h, Color.YELLOW.getRGB());
						guigraphics.drawString(fontrender, "相机移动Y:"+xj.getMoveY(), 0 + 20,  40+h, Color.YELLOW.getRGB());
						guigraphics.drawString(fontrender, "相机移动Z:"+xj.getMoveZ(), 0 + 20,  50+h, Color.YELLOW.getRGB());
						guigraphics.drawString(fontrender, "相机朝向:"+xj.getYRot(), 0 + 20,  70+h, Color.YELLOW.getRGB());
						/*guigraphics.drawString(fontrender, "yHeadRot="+xj.yHeadRot, 0 + 20,  80+h, Color.YELLOW.getRGB());
						guigraphics.drawString(fontrender, "yBodyRot="+xj.yBodyRot, 0 + 20,  90+h, Color.YELLOW.getRGB());*/
						float dis = entityplayer.distanceTo(xj);
						if(dis>100){
							guigraphics.drawString(fontrender, "距离:"+dis, 0 + 20,  60+h, Color.RED.getRGB());
						}else{
							guigraphics.drawString(fontrender, "距离:"+dis, 0 + 20,  60+h, Color.GREEN.getRGB());
						}
						stack.scale(0.0625f, 0.0625f, 1);
						stack.popPose();
					}
				}
			}
			
			
			//-stack.disableLighting();
			if(renderRader){
				if(renderRts){
					stack.pushPose();
					RenderSystem.enableBlend();
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
					stack.scale(1F, 1F, 1);
					guigraphics.drawString(fontrender, "经验值:"+entityplayer.getScore(), 0 + 20,  10, Color.YELLOW.getRGB());
					stack.scale(0.0625f, 0.0625f, 1);
					stack.popPose();
					raderWidth = 3.62F;
					raderHeight = -0.51F;
					/*raderWidth = 3.5F;
					raderHeight = -0.47F;*/
					renderRaderUI(stack,i,j,raderWidth,raderHeight, raderRote, 0.7F);

					boolean lctrol = GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS;
					boolean shiftctrol = GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS;
					
					boolean attack=false;
					boolean drone=false;
					boolean free=GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_G) == GLFW.GLFW_PRESS;
					boolean defend=GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS;
					boolean choose=GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_V) == GLFW.GLFW_PRESS;
					boolean follow=GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_C) == GLFW.GLFW_PRESS;
					boolean leave=GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_B) == GLFW.GLFW_PRESS;
					
					if(lctrol && GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS){
						attack=true;
					}
					if(shiftctrol && GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS){
						drone=true;
					}
					renderRtsKey(stack,i*(0.35F-0.07F),j*0.85F,attack, 1);
					renderRtsKey(stack,i*(0.35F-0.07F*2),j*0.85F,free, 2);
					renderRtsKey(stack,i*(0.35F-0.07F*3),j*0.85F,defend, 3);
					renderRtsKey(stack,i*(0.35F-0.07F*4),j*0.85F,choose, 4);
					if(!(mc.screen instanceof RtsMoShiScreen)){
						renderRtsKey(stack,i*(0.35F-0.07F*4),j*0.7F,drone, 5);
						renderRtsKey(stack,i*(0.35F-0.07F*3),j*0.7F,follow, 6);
					}
					renderRtsKey(stack,i*0.35F,j*0.85F,leave, 7);
				}else{
					raderWidth = 0;
					raderHeight = 0;
					renderRaderUI(stack,i,j,raderWidth,raderHeight, raderRote, 0.8F);
				}
				
				Map<Class<?>, List<Entity>> unitGroups = new HashMap<>();
				Map<Class<?>, Integer> unitCounts = new HashMap<>();
				List<Entity> list = entityplayer.level().getEntities(entityplayer, entityplayer.getBoundingBox().inflate(150D, 100D, 150D));
				int enemy = 0;
				int friend = 0;
				int man = 0;
				int army = 0;
				float rtsY = 0;
				if(mc.screen!=null && mc.screen instanceof RtsMoShiScreen)rtsY=10;
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					if(ent instanceof IArmy && ent instanceof TamableAnimal && renderRts){
						TamableAnimal en = (TamableAnimal)ent;
						if(en.getOwner()==entityplayer && en.getVehicle()==null){
							IArmy cap = (IArmy)ent;
							++army;
							Class<?> unitClass = ent.getClass();
							unitGroups.computeIfAbsent(unitClass, k -> new ArrayList<>()).add(ent);
							unitCounts.put(unitClass, unitCounts.getOrDefault(unitClass, 0) + 1);
						}
					}
					if(ModList.get().isLoaded("advancearmy")){
						if(ent instanceof CreatureRespawn && ((CreatureRespawn)ent).getHealth()>0){
							CreatureRespawn res = (CreatureRespawn)ent;
							if(res.isEnemyRespawn){
								renderRadarTarget(raderposX,raderposZ,stack,i,j,raderWidth,raderHeight, null, ent, entityplayer,4,2,null,null,0);
								++enemy;
								renderTeamCount(stack,i*1.1F,  40*enemy,1,res.getRespawnCount()/800F);
								stack.pushPose();
								RenderSystem.enableBlend();
								RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
								stack.scale(1F, 1F, 1);
								guigraphics.drawString(fontrender, ""+res.getRespawnCount(), i/2 + 40,  10*enemy, Color.RED.getRGB());
								stack.scale(0.0625f, 0.0625f, 1);
								stack.popPose();
							}else{
								renderRadarTarget(raderposX,raderposZ,stack,i,j,raderWidth,raderHeight, null, ent, entityplayer,4,3,null,null,0);
								++friend;
								renderTeamCount(stack,i*0.9F,  40*friend,0,res.getRespawnCount()/800F);
								stack.pushPose();
								RenderSystem.enableBlend();
								RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
								stack.scale(1F, 1F, 1);
								guigraphics.drawString(fontrender, ""+res.getRespawnCount(), i/2 - 40,  10*friend, Color.CYAN.getRGB());
								stack.scale(0.0625f, 0.0625f, 1);
								stack.popPose();
							}
						}else if(ent instanceof SupportPoint && ((SupportPoint)ent).getHealth()>0){
							SupportPoint spt = (SupportPoint)ent;
							if(spt.isAttack){
								renderRadarTarget(raderposX,raderposZ,stack,i,j,raderWidth,raderHeight, null, ent, entityplayer,7,0,null,null,0);
							}else{
								renderRadarTarget(raderposX,raderposZ,stack,i,j,raderWidth,raderHeight, null, ent, entityplayer,6,0,null,null,0);
							}
						}else if(ent instanceof DefencePoint && ((DefencePoint)ent).getHealth()>0){
							renderRadarTarget(raderposX,raderposZ,stack,i,j,raderWidth,raderHeight, null, ent, entityplayer,8,0,null,null,0);
						}
					}
					if(ent instanceof EntityWMVehicleBase){
						EntityWMVehicleBase ve = (EntityWMVehicleBase)ent;
						if(ve.getHealth()>0){
							if(ve.VehicleType<3)renderRadarTarget(raderposX,raderposZ,stack,i,j,raderWidth,raderHeight, null, ent, entityplayer,1,ve.getTargetType(),ve.icon1tex,ve.icon2tex,ve.turretYaw);
							if(ve.VehicleType==3)renderRadarTarget(raderposX,raderposZ,stack,i,j,raderWidth,raderHeight, null, ent, entityplayer,2,ve.getTargetType(),ve.icon1tex,ve.icon2tex,ve.turretYaw);
							if(ve.VehicleType==4)renderRadarTarget(raderposX,raderposZ,stack,i,j,raderWidth,raderHeight, null, ent, entityplayer,3,ve.getTargetType(),ve.icon1tex,ve.icon2tex,ve.turretYaw);
						}
					}else{
						if(ent instanceof PathfinderMob && ent.getVehicle()==null){
							if(((PathfinderMob)ent).getHealth()>0 && ent.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, ent.blockPosition()).getY() == ent.blockPosition().getY()){
								++man;
								if(man<60)renderRadarTarget(raderposX,raderposZ,stack,i,j,raderWidth,raderHeight, null, ent, entityplayer,0,0,null,null,0);
							}
						}
						if(ent instanceof EntityMissile){
							++man;
							if(man<60)renderRadarTarget(raderposX,raderposZ,stack,i,j,raderWidth,raderHeight, null, ent, entityplayer,5,0,null,null,0);
						}
					}
				}
				
				if(army<=20){
					int count = 0;
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity ent = list.get(k2);
						if(ent instanceof IArmy && ent instanceof TamableAnimal && renderRts){
							TamableAnimal en = (TamableAnimal)ent;
							if(en.getOwner()==entityplayer && en.getVehicle()==null){
								IArmy cap = (IArmy)ent;
								if(count < 20) {
									int row = count / 5;      // 计算行号：0,1,2,3
									int col = count % 5;      // 计算列号：0,1,2,3,4
									// 计算位置
									// x坐标：从屏幕右半部分开始，每个图标间隔0.1倍的屏幕宽度
									float xPos = (float)i * (0.75F + col * 0.05F);
									// y坐标：从屏幕一半高度开始，每行间隔0.08倍的屏幕高度
									float yPos = (float)j * (0.65F + row * 0.1F);
									renderUnit(stack, xPos, yPos, 
											  en.getHealth()/en.getMaxHealth(), cap.getIcon1(), 
											  cap.getIcon2(), cap.getSelect(), cap.getTeamCount(), 0);
								}else{
									break;
								}
								count++;
							}
						}
					}
				}else{
					int displayIndex = 0;
					// 遍历所有类型分组
					for(Map.Entry<Class<?>, List<Entity>> entry : unitGroups.entrySet()) {
						Class<?> unitClass = entry.getKey();
						List<Entity> units = entry.getValue();
						int typeCount = unitCounts.get(unitClass);
						
						// 获取代表单位的信息
						Entity representative = units.get(0);
						TamableAnimal tamable = (TamableAnimal)representative;
						IArmy cap = (IArmy)representative;

						if(displayIndex < 20) {
							int row = displayIndex / 5;      // 计算行号：0,1,2,3
							int col = displayIndex % 5;      // 计算列号：0,1,2,3,4
							// 计算位置
							// x坐标：从屏幕右半部分开始，每个图标间隔0.1倍的屏幕宽度
							float xPos = (float)i * (0.75F + col * 0.05F);
							// y坐标：从屏幕一半高度开始，每行间隔0.08倍的屏幕高度
							float yPos = (float)j * (0.65F + row * 0.1F);
							renderUnit(stack, xPos, yPos, 
									  tamable.getHealth()/tamable.getMaxHealth(), cap.getIcon1(), 
									  cap.getIcon2(), cap.getSelect(), cap.getTeamCount(), typeCount);
						}else{
							break;
						}
						displayIndex++;
					}
				}
				
				
				if(renderRts){
					RenderSystem.enableBlend();
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
					
					long worldTime = mc.level.getDayTime();
					long currentDay = worldTime / 24000L;
					
					stack.pushPose();
					stack.scale(1F, 1F, 1);
					if(army<20){
						guigraphics.drawString(fontrender, "兵力值:"+army, 20,  20, Color.GREEN.getRGB());
					}else{
						guigraphics.drawString(fontrender, "兵力值:"+army, 20,  20, Color.RED.getRGB());
					}
					stack.scale(0.0625f, 0.0625f, 1);
					stack.popPose();
					
					stack.pushPose();
					stack.scale(1F, 1F, 1);
					guigraphics.drawString(fontrender, "天数:"+currentDay, 20,  30, Color.BLUE.getRGB());
					stack.scale(0.0625f, 0.0625f, 1);
					stack.popPose();
				}
			}
			if(WMConfig.hitInformation){
				stack.pushPose();
				int w = mc.getWindow().getGuiScaledWidth();
				int h = mc.getWindow().getGuiScaledHeight();
				for(int ii = 0; ii < this.txt.size(); ii++)
				{
					if(ii<15){
						guigraphics.drawString(fontrender, this.txt.get(ii), 10, 10 + 15 * ii, 0xFFFFFF);
					}else{
						guigraphics.drawString(fontrender, this.txt.get(ii), 80, 10 + 15 * (ii-15), 0xFFFFFF);
					}
				}
				for(int ii = 0; ii < this.seat.size(); ii++)
				{
					guigraphics.drawString(fontrender, this.seat.get(ii), w-45, 10 + 15 * ii, 0xFFFFFF);
				}
				for(int ii = 0; ii < this.info.size(); ii++){
					guigraphics.drawString(fontrender, this.info.get(ii), w-70, (int)(h - ii*0.2F*h), 0xFFFFFF);
				}
				stack.popPose();

				stack.pushPose();
				if(nb >= 1){
					if(nb>99)scaletime = 0;
					if(scaletime>11)scaletime = 0;
					if(scaletime<5)++scaletime;
					this.renderIcon(stack,i,j, "wmlib:textures/hud/hit.png", "icon", 4F/(10+scaletime),3/(1+scaletime));
					nbts.putInt("hitentity", nb-1);
				}else{
					scaletime = 12;
				}
				stack.popPose();
				
				stack.pushPose();
				if(nbd >= 1){
					if(nbd>99)scaletime2 = 0;
					if(scaletime2>11)scaletime2 = 0;
					if(scaletime2<10)++scaletime2;
					this.renderIcon(stack,i,j, "wmlib:textures/hud/hitdead.png", "icon", scaletime2/16F,3/(1+scaletime2));
					nbts.putInt("hitentitydead", nbd-1);
				}else{
					scaletime2 = 12;
				}
				stack.popPose();
				
				stack.pushPose();
				if(nbd >= 1){
					if(nbd>199){
						scaletime3 = 0;
						++skeleton;
					}
					if(scaletime3>11)scaletime3 = 0;
					if(scaletime3<10)++scaletime3;
					if(skeleton>0){
						stack.translate(6-6*skeleton, 0, 0);
						for (int lj = 0; lj < skeleton; lj++) {
							if(nbdh >= 1){
								this.renderIcon(stack,i+x1+lj*25,j-90+y1, "wmlib:textures/hud/skeleton_gold.png", "icon", 3F/(5+scaletime3), 1);
							}else{
								this.renderIcon(stack,i+x1+lj*25,j-90+y1, "wmlib:textures/hud/skeleton.png", "icon", 3F/(5+scaletime3), 1);
							}
						}
					}
				}else{
					scaletime3 = 12;
					skeleton = 0;
				}
				stack.popPose();
			}
		}
	}
	
	private List<Component> txt = new ArrayList<>();
	private List<Component> seat = new ArrayList<>();
	private List<Component> info = new ArrayList<>();
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
        Player player = mc.player;
        if(player == null)return;
        Entity entity = player.getVehicle();

		if(entity!=null && entity instanceof EntityWMSeat){
			EntityWMSeat seat = (EntityWMSeat)entity;
			RenderSystem.setShaderColor(0F, 0.8F, 1F, 1);
			if(showtime<100)++showtime;
			if(showtime<50){
				if(!seat.showhelp)this.addText("按C键打开按键帮助");
			}
			if(seat.canNightV)this.addText("按N键开启夜视模式");
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
							if(ent instanceof TamableAnimal){
								TamableAnimal soldier = (TamableAnimal)ent;
								if(player==soldier.getOwner()){
									colorid = 1;
								}else{
									if(player.getTeam()!=null&&player.getTeam()==ent.getTeam()){
										colorid = 2;
									}else{
										if(ent instanceof Enemy||ent.getTeam()!=null && ent.getTeam()!=player.getTeam()){
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
									if(ent instanceof Enemy||ent.getTeam()!=null && ent.getTeam()!=player.getTeam()){
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
					this.addText("使用镐类工具敲击载具来进行维修");
				}
			}
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		}else{
			showtime=0;
		}
    }
	
	private void addText(String label)
    {
        this.txt.add(Component.literal(label).withStyle(ChatFormatting.RESET));
    }
	private void addTextRed(String label)
    {
        this.txt.add(Component.literal(label).withStyle(ChatFormatting.RED));
    }
	private void addInfor(String label)
    {
        this.info.add(Component.literal(label).withStyle(ChatFormatting.AQUA));
    }
	private void addSeat(String label, int color)
    {
		if(color==0)this.seat.add(Component.literal(label).withStyle(ChatFormatting.RESET));
		if(color==1)this.seat.add(Component.literal(label).withStyle(ChatFormatting.GREEN));
        if(color==2)this.seat.add(Component.literal(label).withStyle(ChatFormatting.AQUA));
		if(color==3)this.seat.add(Component.literal(label).withStyle(ChatFormatting.RED));
    }
	
    @SubscribeEvent
    public void onFovUpdate(ComputeFovModifierEvent event)
    {
        Player player = Minecraft.getInstance().player;
        if(player == null)return;
        Entity ridingEntity = player.getVehicle();
		Minecraft mc = Minecraft.getInstance();

		ItemStack itemstack = player.getMainHandItem();
		if(!itemstack.isEmpty() && itemstack.getItem() instanceof ItemGun){
			ItemGun gun = (ItemGun) itemstack.getItem();
			if(gun.scopezoom > 1.0F) {
				if (gun.aim_time>=gun.time_aim) {
					event.setNewFovModifier(event.getFovModifier() / gun.scopezoom);
				}
			}
		}
        if(ridingEntity instanceof EntityWMSeat){
			EntityWMSeat seat = (EntityWMSeat) player.getVehicle();
			if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
				EntityWMVehicleBase ve = (EntityWMVehicleBase) (seat.getVehicle());
				if(mc.options.getCameraType() == CameraType.FIRST_PERSON||ve.VehicleType<3){//THIRD_PERSON_FRONT
					if(seat.isZoom && blacktime>10)event.setNewFovModifier(event.getFovModifier()*0.3F);
				}
			}
        }
    }
	
	@OnlyIn(Dist.CLIENT)
	protected void renderWeapon(PoseStack stack, int i, int j, EntityWMVehicleBase entity, EntityWMSeat seat, int id) {
		stack.pushPose();
		float health = (float)(entity.getHealth()/entity.getMaxHealth());
		float size1 = reload1/reloadmax1;
		float size2 = reload2/reloadmax2;
		float size3 = reload3/reloadmax3;
		float size4 = reload4/reloadmax4;
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		int packedLight = 0xF000F0;
		RenderType rt = SARenderState.getBlendDepthWrite_NoLight(white);
		RenderType rt2 = SARenderState.getBlendDepthWrite_NoLight(white2);
		RenderType rt3 = SARenderState.getBlendDepthWrite_NoLight(grey);
		
		if(health>0.7){
			hudw.setColor(0F, 0.8F, 1F, 1);
		}else if(health>0.4){
			hudw.setColor(0F+(1.2F-health), 0.8F, 0F, 1);
		}else{
			hudw.setColor(0.5F+health*0.5F, 0.1F, 0F, 1);
		}
		VertexConsumer vb = bufferSource.getBuffer(rt);
		hudw.setRender(null, vb, stack, packedLight);
		stack.pushPose();
		stack.translate((float)(i *0.66F), (float)(j *0.95F), 5);
		stack.scale(15F*(i/427F), 15F*(j/240F), 15F);
		stack.mulPose(Axis.YP.rotationDegrees(-180F));
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		//
		
		if(entity.getHealth()>0 && h1==0 && entity.hurtTime == 0){
			h1 = entity.getHealth();
		}else{
			if(h1>entity.getHealth())h1-=0.5F;
		}
		
		stack.pushPose();
		stack.translate(9.35F, 0F, 0F);
		stack.scale(health, 1, 1);
		stack.translate(-9.35F, 0F, 0F);
		hudw.renderPart("health");
		stack.popPose();
		stack.pushPose();
		hudw.setColor(1F, 1F, 1F, 1);
		stack.translate(9.35F, 0F, 0F);
		stack.scale(h1/entity.getMaxHealth(), 1, 1);
		stack.translate(-9.35F, 0F, 0F);
		hudw.renderPart("health2");
		stack.popPose();
		
		if(size1==0)size1=1;
		if(size2==0)size2=1;
		if(size3==0)size3=1;
		if(size4==0)size4=1;
		float movex = 0;
		String icon = "icon1";

		if(health>0.7){
			hudw.setColor(0F, 0.8F, 1F, 1);
		}else if(health>0.4){
			hudw.setColor(0F+(1.2F-health), 0.8F, 0F, 1);
		}else{
			hudw.setColor(0.5F+health*0.5F, 0.1F, 0F, 1);
		}
		if(id > 0){
			if(seat.weaponCount==1){
				stack.pushPose();
				stack.scale(1, size1, 1);
				hudw.renderPart("c1");
				stack.popPose();
				VertexConsumer vb2 = bufferSource.getBuffer(rt3);
				hudw.setRender(null, vb2, stack, packedLight);
				hudw.renderPart("mat1");
			}else if(seat.weaponCount==2){
				icon = "icon2";
				movex = 4.74F;
				stack.pushPose();
				stack.pushPose();
				stack.scale(1, size1, 1);
				hudw.renderPart("c2");
				stack.popPose();
				VertexConsumer vb3 = bufferSource.getBuffer(rt2);
				hudw.setRender(null, vb3, stack, packedLight);
				stack.translate(movex ,0,0);
				stack.pushPose();
				stack.scale(1, size2, 1);
				hudw.renderPart("c2");
				stack.popPose();
				stack.popPose();
				VertexConsumer vb4 = bufferSource.getBuffer(rt3);
				hudw.setRender(null, vb4, stack, packedLight);
				hudw.renderPart("mat2");
			}
		}else{
			if(entity.getArmyType2()>0){
				VertexConsumer vb5 = bufferSource.getBuffer(rt2);
				hudw.setRender(null, vb5, stack, packedLight);
			}
			if(entity.weaponCount==1){
				stack.pushPose();
				stack.scale(1, size1, 1);
				hudw.renderPart("c1");
				stack.popPose();
				VertexConsumer vb6 = bufferSource.getBuffer(rt3);
				hudw.setRender(null, vb6, stack, packedLight);
				hudw.renderPart("mat1");
			}else
			if(entity.weaponCount==2){
				icon = "icon2";
				movex = 4.74F;
				stack.pushPose();
				stack.pushPose();
				stack.scale(1, size1, 1);
				hudw.renderPart("c2");
				stack.popPose();
				VertexConsumer vb7 = bufferSource.getBuffer(rt2);
				hudw.setRender(null, vb7, stack, packedLight);
				stack.translate(movex ,0,0);
				stack.pushPose();
				stack.scale(1, size2, 1);
				hudw.renderPart("c2");
				stack.popPose();
				stack.popPose();
				VertexConsumer vb8 = bufferSource.getBuffer(rt3);
				hudw.setRender(null, vb8, stack, packedLight);
				hudw.renderPart("mat2");
			}else
			if(entity.weaponCount==3){
				icon = "icon3";
				movex = 3.1F;
				stack.pushPose();
				stack.pushPose();
				stack.scale(1, size1, 1);
				hudw.renderPart("c3");
				stack.popPose();
				VertexConsumer vb9 = bufferSource.getBuffer(rt2);
				hudw.setRender(null, vb9, stack, packedLight);
				stack.translate(movex ,0,0);
				stack.pushPose();
				stack.scale(1, size2, 1);
				hudw.renderPart("c3");
				stack.popPose();
				stack.translate(movex ,0,0);
				stack.pushPose();
				stack.scale(1, size3, 1);
				hudw.renderPart("c3");
				stack.popPose();
				stack.popPose();
				VertexConsumer vb10 = bufferSource.getBuffer(rt3);
				hudw.setRender(null, vb10, stack, packedLight);
				hudw.renderPart("mat3");
			}else
			if(entity.weaponCount>=4){
				icon = "icon4";
				movex = 2.36F;
				stack.pushPose();
				stack.pushPose();
				stack.scale(1, size1, 1);
				hudw.renderPart("c4");
				stack.popPose();
				if(entity.getArmyType2()==0){
					VertexConsumer vb11 = bufferSource.getBuffer(rt2);
					hudw.setRender(null, vb11, stack, packedLight);
				}
				stack.translate(movex ,0,0);
				if(entity.getArmyType2()==2){
					VertexConsumer vb12 = bufferSource.getBuffer(rt);
					hudw.setRender(null, vb12, stack, packedLight);
				}
				stack.pushPose();
				stack.scale(1, size2, 1);
				hudw.renderPart("c4");
				stack.popPose();
				if(entity.getArmyType2()==2){
					VertexConsumer vb13 = bufferSource.getBuffer(rt2);
					hudw.setRender(null, vb13, stack, packedLight);
				}
				stack.translate(movex ,0,0);
				stack.pushPose();
				stack.scale(1, size3, 1);
				hudw.renderPart("c4");
				stack.popPose();
				if(entity.getArmyType2()==1){
					VertexConsumer vb14 = bufferSource.getBuffer(rt);
					hudw.setRender(null, vb14, stack, packedLight);
				}
				stack.translate(movex ,0,0);
				stack.pushPose();
				stack.scale(1, size4, 1);
				hudw.renderPart("c4");
				stack.popPose();
				stack.popPose();
				VertexConsumer vb15 = bufferSource.getBuffer(rt3);
				hudw.setRender(null, vb15, stack, packedLight);
				hudw.renderPart("mat4");
			}
		}
		hudw.renderPart("base");
		RenderType s1 = SARenderState.getBlendWrite_Light(ResourceLocation.tryParse(seat.w1icon));
		RenderType s2 = SARenderState.getBlendWrite_Light(ResourceLocation.tryParse(seat.w2icon));
		
		RenderType w1 = SARenderState.getBlendWrite_Light(ResourceLocation.tryParse(entity.w1icon));
		RenderType w2 = SARenderState.getBlendWrite_Light(ResourceLocation.tryParse(entity.w2icon));
		RenderType w3 = SARenderState.getBlendWrite_Light(ResourceLocation.tryParse(entity.w3icon));
		RenderType w4 = SARenderState.getBlendWrite_Light(ResourceLocation.tryParse(entity.w4icon));
		
		
		if(id > 0){
			if(seat.weaponCount==1)stack.translate(movex ,0,0);
			VertexConsumer vw = bufferSource.getBuffer(s1);
			hudw.setRender(null, vw, stack, packedLight);
			hudw.renderPart(icon);
			if(seat.weaponCount>1){
				stack.translate(movex ,0,0);
				VertexConsumer vw2 = bufferSource.getBuffer(s2);
				hudw.setRender(null, vw2, stack, packedLight);
				hudw.renderPart(icon);
			}
		}else{
			if(entity.weaponCount==1)stack.translate(movex ,0,0);
			VertexConsumer vw3 = bufferSource.getBuffer(w1);
			hudw.setRender(null, vw3, stack, packedLight);
			hudw.renderPart(icon);
			if(entity.weaponCount>1){
				stack.translate(movex ,0,0);
				VertexConsumer vw4 = bufferSource.getBuffer(w2);
				hudw.setRender(null, vw4, stack, packedLight);
				hudw.renderPart(icon);
			}
			if(entity.weaponCount>2){
				stack.translate(movex ,0,0);
				VertexConsumer vw5 = bufferSource.getBuffer(w3);
				hudw.setRender(null, vw5, stack, packedLight);
				hudw.renderPart(icon);
			}
			if(entity.weaponCount>3){
				stack.translate(movex ,0 ,0);
				VertexConsumer vw6 = bufferSource.getBuffer(w4);
				hudw.setRender(null, vw6, stack, packedLight);
				hudw.renderPart(icon);
			}
		}
		stack.popPose();
		hudw.setColor(1F, 1F, 1F, 1);
		stack.popPose();
	}
}

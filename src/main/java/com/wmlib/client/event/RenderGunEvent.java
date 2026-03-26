package wmlib.client.event;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.world.effect.MobEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.CameraType;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraft.nbt.CompoundTag;

import wmlib.client.render.RenderRote;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import wmlib.client.obj.SAObjModel;
import wmlib.common.item.ItemGun;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;
public class RenderGunEvent {
	@OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderFirstGun(RenderHandEvent event)//RenderHandEvent
	{
		PoseStack stack = event.getPoseStack();
		stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		Player entityplayer = mc.player;
		ItemStack itemstack = ((Player) (entityplayer)).getMainHandItem();
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		int packedLight = event.getPackedLight();
		if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemGun) {//item
			this.rendermain(stack, packedLight, entityplayer, itemstack, true, event.getPartialTick());
		}
		stack.popPose();
	}
	
	/*RenderType rtpara = RenderTypeVehicle.objrender(parat);
	RenderType rtdef = SARenderState.getBlendDepthWrite(tex);*/
	
	private void rendermain(PoseStack stack, int light, Player entityplayer, ItemStack itemstack, boolean side, float partialTicks){
		int xxx = 1;
		if(!side){
			xxx = -1;
		}
		ItemGun gun = (ItemGun) itemstack.getItem();
		Minecraft minecraft = Minecraft.getInstance();
		CompoundTag nbt = itemstack.getTag();
		{
			if(minecraft.options.getCameraType() == CameraType.FIRST_PERSON && gun.obj_model != null && gun.obj_tex!=null){
				stack.pushPose();//guns
				stack.scale(2F, 2F, 2F);
				stack.translate(0.0F, 0.0F, -playerRecoilPitch*gun.model_cock_z*0.5F);
				float gunRotX = 2*RenderParameters.GUN_ROT_X_LAST + (RenderParameters.GUN_ROT_X - RenderParameters.GUN_ROT_X_LAST) * partialTicks;
				float gunRotY = 2*RenderParameters.GUN_ROT_Y_LAST + (RenderParameters.GUN_ROT_Y - RenderParameters.GUN_ROT_Y_LAST) * partialTicks;
				if (gun.isReload(itemstack)) {
					float halfReload = gun.reloadtime * 0.5F; // 计算重新装填时间的一半
					float change = gun.retime < halfReload ? gun.retime / halfReload : 1 - (gun.retime - halfReload) / halfReload;
					stack.translate(gun.model_x * xxx ,gun.model_y, gun.model_z + 1);//1.5,-2,-2.5
					RenderRote.setRote(stack, 180F, 0.0F, 1.0F, 0.0F);
					RenderRote.setRote(stack, 30F*change, 1.0F, 0.0F, 0.0F);
					RenderRote.setRote(stack, 60F*change, 0.0F, 1.0F, 0.0F);
					RenderRote.setRote(stack, 20F*change, 0.0F, 0.0F, 1.0F);
				}
				else 
				if(gun.aim_time>0){//ADS
					float change = gun.aim_time/gun.time_aim;
					float x1 = gun.model_x_ads - gun.model_x;
					float y1 = gun.model_y_ads - gun.model_y;
					float z1 = gun.model_z_ads - gun.model_z;
					{
						stack.translate(gun.model_x + x1 * change * xxx, gun.model_y + y1 * change, gun.model_z + z1 * change);
						RenderRote.setRote(stack, gunRotX, 0, -1, 0);
						RenderRote.setRote(stack, gunRotY, 0, 0, -1);
						RenderRote.setRote(stack, gun.rotationx* change, 1.0F, 0.0F, 0.0F);
						RenderRote.setRote(stack, gun.rotationy * xxx, 0.0F, 1.0F, 0.0F);
						RenderRote.setRote(stack, gun.rotationz* change * xxx, 0.0F, 0.0F, 1.0F);
						RenderRote.setRote(stack, 5F, 1.0F, 0.0F, 0.0F);
					}
				}else if(entityplayer.isSprinting() && gun.cooltime == 6||gun.open_time != 0){
					float x1 = gun.Sprintoffsetx - gun.model_x;
					float y1 = gun.Sprintoffsety - gun.model_y;
					float z1 = gun.Sprintoffsetz - gun.model_z;
					float change = gun.run_time/gun.time_run;
					stack.translate(gun.model_x * xxx -0.5F,gun.model_y, gun.model_z);
					stack.translate(gun.model_x + x1 * change * xxx, gun.model_y + y1 * change, gun.model_z + z1 * change);
					RenderRote.setRote(stack, gunRotX, 0, -1, 0);
					RenderRote.setRote(stack, gunRotY, 0, 0, -1);
					RenderRote.setRote(stack, -gun.Sprintrotationx* change, 1.0F, 0.0F, 0.0F);
					RenderRote.setRote(stack, gun.Sprintrotationy * xxx+180, 0.0F, 1.0F, 0.0F);
					RenderRote.setRote(stack, gun.Sprintrotationz* change, 0.0F, 0.0F, 1.0F);
				}else{
					stack.translate(gun.model_x * xxx ,gun.model_y, gun.model_z + 1);//1.5,-2,-2.5
					RenderRote.setRote(stack, gunRotX, 0, -1, 0);
					RenderRote.setRote(stack, gunRotY, 0, 0, -1);
					RenderRote.setRote(stack, 180F, 0.0F, 1.0F, 0.0F);
					RenderRote.setRote(stack, 0.0F, 0.0F, 1.0F, 0.0F);
					RenderRote.setRote(stack, -3F, 1.0F, 0.0F, 0.0F);
				}
				RenderRote.setRote(stack, -gun.model_muzz_jump_x*playerRecoilPitch*0.5F, 1.0F, 0.0F, 0.0F);
				{
					stack.pushPose();//guns
					gun.obj_model.setRender(RenderTypeVehicle.objrender(gun.obj_tex),null,stack,light);
					if(nbt!=null){
						boolean recoiled = nbt.getBoolean("Recoiled");
						if(!gun.zoomHide || gun.aim_time<gun.time_aim){
							renderGun(stack, gun, itemstack, entityplayer, recoiled);
							renderArm(stack, light, gun, entityplayer);
						}
					}
					stack.popPose();//gune
				}
				stack.popPose();//gune
			}
		}
	}
	
	public static void renderGun(PoseStack stack, ItemGun gun, ItemStack itemstack, Player entityplayer, boolean recoiled) {
		Minecraft minecraft = Minecraft.getInstance();
		gun.obj_model.renderPart("mat1");
		if(!gun.isReload(itemstack))gun.obj_model.renderPart("mat3");
		gun.obj_model.renderPart("mat20");
		gun.obj_model.renderPart("mat31");
		gun.obj_model.renderPart("mat32");
		gun.obj_model.renderPart("mat25");
		gun.obj_model.renderPart("mat22");
		if (!recoiled){
			gun.obj_model.renderPart("mat2");
			stack.pushPose();//glstrt
			if(gun.flash_model!=null){
				if(gun.fire_tex!=null){
					gun.flash_model.setRender(SARenderState.getBlendDepthWrite(gun.fire_tex),null,stack,0xF000F0);
				}else{
					gun.flash_model.setRender(SARenderState.getBlendDepthWrite(gun.obj_tex),null,stack,0xF000F0);
				}
				float size = gun.fire_posz*0.2F+entityplayer.level().random.nextInt(4)*(0.4F+gun.fire_posz*0.05F);
				stack.translate(gun.fire_posx, gun.fire_posy, gun.fire_posz);
				stack.scale(size, size, size);
				if(entityplayer.level().random.nextInt(2)==1){
					gun.flash_model.renderPart("mat_1");
				}else if(entityplayer.level().random.nextInt(2)==2){
					gun.flash_model.renderPart("mat_2");
				}else{
					gun.flash_model.renderPart("mat_3");
				}
				gun.flash_model.renderPart("flash1");
			}
			stack.popPose();//glend
		}else{
			gun.obj_model.renderPart("mat2");
		}
		gun.obj_model.setRender(SARenderState.getBlendDepthWrite(gun.obj_tex),null,stack,0xF000F0);
		gun.obj_model.renderPart("mat1_light");
		gun.obj_model.renderPart("mat1_light2");
	}
	public static void renderArm(PoseStack stack, int light, ItemGun gun, Player entityplayer){
		ResourceLocation stevetex = ((AbstractClientPlayer)entityplayer).getSkinTextureLocation();
		if (stevetex == null)
		{
			stevetex = ResourceLocation.tryParse("textures/entity/steve.png");
		}
		gun.arm_model.setRender(RenderTypeVehicle.objrender(stevetex),null,stack,light);
		
		stack.pushPose();
		stack.translate(gun.arm_r_posx,gun.arm_r_posy, gun.arm_r_posz);
		gun.arm_model.renderPart("rightarm");
		stack.popPose();
		
		stack.pushPose();
		stack.translate(gun.arm_l_posx,gun.arm_l_posy, gun.arm_l_posz);
		gun.arm_model.renderPart("leftarm");
		stack.popPose();
	}
}

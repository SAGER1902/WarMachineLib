package wmlib.client.event;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wmlib.client.obj.SAObjModel;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import wmlib.common.item.ItemGun;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.eventbus.api.EventPriority;

import net.minecraft.client.renderer.RenderHelper;
public class RenderGunEvent {
	@OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
	public void rendergun(RenderHandEvent event)//RenderHandEvent
	{
		/*if (Minecraft.getInstance().options.bobView)
		{
			this.applyBobbing(event.getPartialTicks());
		}*/
		GL11.glPushMatrix();
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity entityplayer = mc.player;
		ItemStack itemstack = ((PlayerEntity) (entityplayer)).getMainHandItem();
		
		/*IActiveRenderInfo activeRenderInfoIn = Minecraft.getInstance().getEntityRenderDispatcher().camera;
		activeRenderInfoIn.setup(mc.level, (Entity)(mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()), 
		!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(), partialTicks);
		Vector3d avector3d = activeRenderInfoIn.getPosition();
		double camx = avector3d.x();
		double camy = avector3d.y();
		double camz = avector3d.z();
		double d0 = MathHelper.lerp((double)partialTicks, entity.xOld, entity.getX());
		double d1 = MathHelper.lerp((double)partialTicks, entity.yOld, entity.getY());
		double d2 = MathHelper.lerp((double)partialTicks, entity.zOld, entity.getZ());
		double xIn = d0 - camx;
		double yIn = d1 - camy;
		double zIn = d2 - camz;
    	//Minecraft.getInstance().getTextureManager().bind(tex);
		net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
		activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
		GL11.glRotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef((float) xIn, (float) yIn, (float) zIn);*/
		
		int i1 = mc.getWindow().getGuiScaledWidth();
		int j1 = mc.getWindow().getGuiScaledHeight();
		
		/*GlStateManager._translatef((float)(i1 / 2), (float)(j1 / 2), -5);
		GL11.glScalef(15F*(i1/427F)*1, 15F*(j1/240F)*1, 15F*1);
		GlStateManager._rotatef(-180F, 0.0F, 1.0F, 0.0F);
		GlStateManager._rotatef(180F, 0.0F, 0.0F, 1.0F);*/
		GlStateManager._disableLighting();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
		RenderHelper.turnBackOn();
		//ItemStack itemstackl = ((PlayerEntity) (entityplayer)).getOffhandItem();
		//GL11.glColor4f(1F, 1F, 1F, 1F);
		if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemGun) {//item
			{
				this.rendermain(entityplayer, itemstack, true, event.getPartialTicks());
			}
		}
		RenderHelper.turnOff();
		GlStateManager._enableLighting();
		/*if (!itemstackl.isEmpty()  && itemstackl.getItem() instanceof ItemGun && itemstackl.hasTag()) {//item
			{
				this.rendermain(entityplayer, itemstackl, false, event.getPartialTicks());
			}
		}*/
		GL11.glPopMatrix();
	}
	
	 /**
     * Updates the bobbing render effect of the player.
     */
    private void applyBobbing(float partialTicks)
    {
    	MatrixStack matrixstack = new MatrixStack();
        if (Minecraft.getInstance().getCameraEntity() instanceof PlayerEntity)
        {
        	PlayerEntity playerentity = (PlayerEntity)Minecraft.getInstance().getCameraEntity();
        	float f = playerentity.walkDist - playerentity.walkDistO;
            float f1 = -(playerentity.walkDist + f * partialTicks);
            float f2 = MathHelper.lerp(partialTicks, playerentity.oBob, playerentity.bob);
            matrixstack.translate((double)(MathHelper.sin(f1 * (float)Math.PI) * f2 * 0.5F), (double)(-Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2)), 0.0D);
            matrixstack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.sin(f1 * (float)Math.PI) * f2 * 3.0F));
            matrixstack.mulPose(Vector3f.XP.rotationDegrees(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F));
        }
    }
	
	private void rendermain(PlayerEntity entityplayer, ItemStack itemstack, boolean side, float partialTicks){
		int xxx = 1;
		if(!side){
			xxx = -1;
		}
		ItemGun gun = (ItemGun) itemstack.getItem();
		Minecraft minecraft = Minecraft.getInstance();
		CompoundNBT nbt = itemstack.getTag();
		/*if(nbt!=null)*/{
			/*boolean cocking = nbt.getBoolean("Cocking");
			int cockingtime = nbt.getInt("CockingTime");
			
			int recoiledtime = nbt.getInt("RecoiledTime");
			if(gun.obj_model == null) {
				gun.obj_model = new SAObjModel(gun.obj_model_resource);
				gun.arm_model = new SAObjModel(gun.obj_arm_resource);
				//System.out.println(String.format("model_road"));
			}*/
			if(minecraft.options.getCameraType() == PointOfView.FIRST_PERSON && gun.obj_model != null){
				GL11.glPushMatrix();//guns
				GL11.glScalef(2F, 2F, 2F);
				/*GL11.glScalef(0.5F, 0.5F, 0.5F);
				GL11.glScalef(0.5F, 0.5F, 0.5F);*/
				//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				/*if(!recoiled)*/{
					GL11.glTranslatef(0.0F, 0.0F, -playerRecoilPitch*gun.model_cock_z*0.5F);
				}
				
				float gunRotX = 2*RenderParameters.GUN_ROT_X_LAST + (RenderParameters.GUN_ROT_X - RenderParameters.GUN_ROT_X_LAST) * partialTicks;
				float gunRotY = 2*RenderParameters.GUN_ROT_Y_LAST + (RenderParameters.GUN_ROT_Y - RenderParameters.GUN_ROT_Y_LAST) * partialTicks;
				if (gun.isReload(itemstack)) {
					float halfReload = gun.reloadtime * 0.5F; // 计算重新装填时间的一半
					float change = gun.retime < halfReload ? gun.retime / halfReload : 1 - (gun.retime - halfReload) / halfReload;
					GL11.glTranslatef(gun.model_x * xxx ,gun.model_y, gun.model_z + 1);//1.5,-2,-2.5
					GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(30F*change, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(60F*change, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(20F*change, 0.0F, 0.0F, 1.0F);
				}
				else 
				if(gun.aim_time>0){//ADS
					float change = gun.aim_time/gun.time_aim;
					float x1 = gun.model_x_ads - gun.model_x;
					float y1 = gun.model_y_ads - gun.model_y;
					float z1 = gun.model_z_ads - gun.model_z;
					{
						GL11.glTranslatef(gun.model_x + x1 * change * xxx, gun.model_y + y1 * change, gun.model_z + z1 * change);
						GL11.glRotatef(gunRotX, 0, -1, 0);
						GL11.glRotatef(gunRotY, 0, 0, -1);
						GL11.glRotatef(gun.rotationx* change, 1.0F, 0.0F, 0.0F);
						GL11.glRotatef(gun.rotationy * xxx, 0.0F, 1.0F, 0.0F);
						GL11.glRotatef(gun.rotationz* change * xxx, 0.0F, 0.0F, 1.0F);
						GL11.glRotatef(5F, 1.0F, 0.0F, 0.0F);
					}
				}else if(entityplayer.isSprinting() && gun.cooltime == 6||gun.open_time != 0){
					float x1 = gun.Sprintoffsetx - gun.model_x;
					float y1 = gun.Sprintoffsety - gun.model_y;
					float z1 = gun.Sprintoffsetz - gun.model_z;
					float change = gun.run_time/gun.time_run;
					GL11.glTranslatef(gun.model_x * xxx -0.5F,gun.model_y, gun.model_z);
					GL11.glTranslatef(gun.model_x + x1 * change * xxx, gun.model_y + y1 * change, gun.model_z + z1 * change);
					GL11.glRotatef(gunRotX, 0, -1, 0);
					GL11.glRotatef(gunRotY, 0, 0, -1);
					GL11.glRotatef(-gun.Sprintrotationx* change, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(gun.Sprintrotationy * xxx+180, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(gun.Sprintrotationz* change, 0.0F, 0.0F, 1.0F);
				}else{
					GL11.glTranslatef(gun.model_x * xxx ,gun.model_y, gun.model_z + 1);//1.5,-2,-2.5
					GL11.glRotatef(gunRotX, 0, -1, 0);
					GL11.glRotatef(gunRotY, 0, 0, -1);
					GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-3F, 1.0F, 0.0F, 0.0F);
				}
				/*if(!recoiled) */{
					GL11.glRotatef(-gun.model_muzz_jump_x*playerRecoilPitch*0.5F, 1.0F, 0.0F, 0.0F);
				}
				
				{
					GL11.glPushMatrix();//guns
					GL11.glEnable(GL11.GL_DEPTH_TEST);//
					GL11.glEnable(GL12.GL_RESCALE_NORMAL);
					GL11.glEnable(GL11.GL_LIGHTING);//
					GL11.glEnable(GL11.GL_COLOR_MATERIAL);//
					GlStateManager._shadeModel(GL11.GL_SMOOTH);
					//GL11.glEnable(GL11.GL_LIGHT1);
					GL11.glColor3f(240F, 240F, 240F);
					GL11.glColor4f(1F,1F,1F,1F);
					if(!side){
						GL11.glEnable(GL11.GL_DEPTH_TEST);
					}
					minecraft.getEntityRenderDispatcher().textureManager.bind(gun.obj_tex);
					if(nbt!=null){
						boolean recoiled = nbt.getBoolean("Recoiled");
						if(!gun.zoomrender || gun.aim_time<gun.time_aim){
							render_mat(gun, itemstack, entityplayer, recoiled);
							render_arm(gun, entityplayer);
						}
					}
					GL11.glColor4f(1F, 1F, 1F, 1F);
					GlStateManager._shadeModel(GL11.GL_FLAT);
					GL11.glDisable(GL12.GL_RESCALE_NORMAL);
					//GL11.glDisable(GL11.GL_LIGHT1);
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glPopMatrix();//gune
				}
				GL11.glPopMatrix();//gune
			}
		}
	}
	
	public static void render_mat(ItemGun gun, ItemStack itemstack, PlayerEntity entityplayer, boolean recoiled) {
		Minecraft minecraft = Minecraft.getInstance();
		gun.obj_model.renderPart("mat1");
		if (!gun.isReload(itemstack))gun.obj_model.renderPart("mat3");
		gun.obj_model.renderPart("mat20");
		gun.obj_model.renderPart("mat31");
		gun.obj_model.renderPart("mat32");
		gun.obj_model.renderPart("mat25");
		gun.obj_model.renderPart("mat22");
		
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			gun.obj_model.renderPart("mat1_light");
			gun.obj_model.renderPart("mat1_light2");
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		if (!recoiled){
			//GL11.glTranslatef(0.0F, 0.0F, gun.model_cock_z);//0, 0, -0.4
			gun.obj_model.renderPart("mat2");
			GL11.glPushMatrix();//glstrt
			//Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(gun.bulletf_tex));
			if(gun.flash_model!=null){
				if(gun.fire_tex!=null){
					minecraft.getEntityRenderDispatcher().textureManager.bind(gun.fire_tex);
				}
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
				float size = gun.fire_posz*0.2F+entityplayer.level.random.nextInt(4)*(0.4F+gun.fire_posz*0.05F);
				GL11.glTranslatef(gun.fire_posx, gun.fire_posy, gun.fire_posz);
				GlStateManager._scalef(size, size, size);
				if(entityplayer.level.random.nextInt(2)==1){
					gun.flash_model.renderPart("mat_1");
				}else if(entityplayer.level.random.nextInt(2)==2){
					gun.flash_model.renderPart("mat_2");
				}else{
					gun.flash_model.renderPart("mat_3");
				}
				gun.flash_model.renderPart("flash1");
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			}
			GL11.glPopMatrix();//glend
		}else{
			gun.obj_model.renderPart("mat2");
		}
	}
	public static void render_arm(ItemGun gun, PlayerEntity entityplayer){
		GL11.glPushMatrix();//arms
		ResourceLocation resourcelocation = ((AbstractClientPlayerEntity)entityplayer).getSkinTextureLocation();
		if (resourcelocation == null)
		{
			resourcelocation = new ResourceLocation("textures/entity/steve.png");
		}
		Minecraft.getInstance().getEntityRenderDispatcher().textureManager.bind(resourcelocation);
		GL11.glTranslatef(gun.arm_r_posx,gun.arm_r_posy, gun.arm_r_posz);
		gun.arm_model.renderPart("rightarm");
		GL11.glPopMatrix();//arme
		
		GL11.glPushMatrix();//arms
		GL11.glTranslatef(gun.arm_l_posx,gun.arm_l_posy, gun.arm_l_posz);
		gun.arm_model.renderPart("leftarm");
		GL11.glPopMatrix();//arme
	}
}

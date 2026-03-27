package wmlib.client;

import java.util.List;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;

import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wmlib.common.living.EntityWMVehicleBase;

import net.minecraftforge.client.event.InputEvent;
import net.minecraft.client.MouseHelper;
import net.minecraft.util.math.MathHelper;
import wmlib.client.render.InstantBulletRenderer;
//import com.mrcrayfish.guns.Config;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraft.client.settings.PointOfView;

import wmlib.common.living.EntityWMSeat;
import wmlib.common.item.ItemMouse;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.util.math.vector.Vector3d;

import net.minecraft.client.renderer.debug.DebugRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import wmlib.client.obj.SAObjModel;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import wmlib.common.network.message.MessageChoose;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageThrottle;
import wmlib.common.network.message.MessageRote;
import wmlib.common.network.message.MessageCame;
import wmlib.common.network.message.MessageCommander;
import wmlib.common.network.message.MessageShoot;
import net.minecraft.util.CooldownTracker;
import wmlib.common.item.ItemGun;
import net.minecraft.nbt.CompoundNBT;
import wmlib.WMConfig;
public class ClientTickHandler{
    public ClientTickHandler() {
    }
	public int fxtime=0;
    private PlayerEntity player;
    private double xo;
    private double yo;
    private double zo;
	public static int starttime = 0;
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/marker/point1.obj");
    private static final ResourceLocation icon = new ResourceLocation("wmlib:textures/marker/point1.png");
	public static double posXm = 0;
	public static double posYm = 0;
	public static double posZm = 0;
	
	public static double posX1 = 0;
	public static double posY1 = 0;
	public static double posZ1 = 0;
	public static double posX2 = 0;
	public static double posY2 = 0;
	public static double posZ2 = 0;
	public int cooltime=0;
	public int time = 0;
	boolean select = false;
    float iii;
	boolean glow = true;
	float count;
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
		if(fxtime<200)++fxtime;
		if(cooltime<10){
			++cooltime;
		}
		if(fxtime>=200)InstantBulletRenderer.RenderAllTrails(event.getPartialTicks());
		Minecraft mc = Minecraft.getInstance();
		if(mc.player.getVehicle()==null){
			this.player = mc.player;
			ItemStack itemstack = this.player.getMainHandItem();
			ItemStack heldItem2 = this.player.getOffhandItem();
			if(!itemstack.isEmpty() && itemstack.getItem() instanceof ItemMouse||!heldItem2.isEmpty() && heldItem2.getItem() instanceof ItemMouse){
				GlStateManager._pushMatrix();
				ActiveRenderInfo activeRenderInfoIn = Minecraft.getInstance().getEntityRenderDispatcher().camera;
				activeRenderInfoIn.setup(mc.level, (Entity)(mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()), 
				!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(), event.getPartialTicks());
				net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, event.getPartialTicks());
				activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
				GlStateManager._rotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
				GlStateManager._rotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
				GlStateManager._rotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
				this.xo = this.player.xOld + (this.player.getX() - this.player.xOld) * (double)event.getPartialTicks();
				this.yo = this.player.yOld + (this.player.getY() - this.player.yOld) * (double)event.getPartialTicks();
				this.zo = this.player.zOld + (this.player.getZ() - this.player.zOld) * (double)event.getPartialTicks();
				int rendertype = 0;
				boolean lctrol = GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS;
				//boolean shiftctrol = GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS;
				if(player != null && mc.screen==null) {
					if(this.time>0){
						Vector3d lock = Vector3d.directionFromRotation(player.xRot, player.yRot);
						int ix = 0;
						int iy = 0;
						int iz = 0;
						for(int x2 = 0; x2 < 120; ++x2) {
							BlockPos blockpos = new BlockPos(player.getX() + lock.x * x2,player.getY() + 1.5 + lock.y * x2,player.getZ() + lock.z * x2);
							BlockState iblockstate = player.level.getBlockState(blockpos);
							if (!iblockstate.isAir(player.level, blockpos)){
								ix = (int) (player.getX() + lock.x * x2);
								iy = (int) (player.getY() + 1.5 + lock.y * x2);
								iz = (int) (player.getZ() + lock.z * x2);
								if(ix != 0 && iz != 0 && iy != 0) {
									if(this.time==3){
										this.posX1 = ix;
										this.posY1 = iy+2;
										this.posZ1 = iz;
										break;
									}
									if(this.time>3){
										this.posX2 = ix;
										this.posY2 = iy+2;
										this.posZ2 = iz;
									}
								}
							}
						}
					}
					if(cooltime>2){
						if(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS){
							if(this.time <20)++this.time;
							if(this.time==1){
								if(lctrol){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(6));
								}else{
									if(twotime>0){
										PacketHandler.getPlayChannel().sendToServer(new MessageCommander(7));
									}else{
										PacketHandler.getPlayChannel().sendToServer(new MessageCommander(1));
									}
									twotime=5;
								}
								cooltime = 0;
							}
							if(this.time>3){
								this.select = true;
							}
						}else{
							this.time = 0;
							if(this.select){
								PacketHandler.getPlayChannel().sendToServer(new MessageChoose(this.posX1, this.posY1, this.posZ1, this.posX2, this.posY2, this.posZ2));
								this.select = false;
								cooltime = 0;
							}
							if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_V) == GLFW.GLFW_PRESS){
								PacketHandler.getPlayChannel().sendToServer(new MessageCommander(3));//all
								cooltime = 0;
							}
							if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS){
								PacketHandler.getPlayChannel().sendToServer(new MessageCommander(4));//stop
								cooltime = 0;
							}
							if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_G) == GLFW.GLFW_PRESS){
								PacketHandler.getPlayChannel().sendToServer(new MessageCommander(5));//warn
								cooltime = 0;
							}
							if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_C) == GLFW.GLFW_PRESS){
								PacketHandler.getPlayChannel().sendToServer(new MessageCommander(8));//follow
								cooltime = 0;
							}
							if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_B) == GLFW.GLFW_PRESS){
								PacketHandler.getPlayChannel().sendToServer(new MessageCommander(9));//stop ride
								cooltime = 0;
							}
							if(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS){
								PacketHandler.getPlayChannel().sendToServer(new MessageCommander(2));
								cooltime = 0;
							}
							
							if(lctrol){
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_1) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(11));//add team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_2) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(12));//add team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_3) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(13));//add team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_4) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(14));//add team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_5) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(15));//add team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_6) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(16));//add team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_7) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(17));//add team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_8) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(18));//add team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_9) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(19));//add team count
									cooltime = 0;
								}
							}else{
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_1) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(2));
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(21));//choose team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_2) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(2));
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(22));//choose team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_3) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(2));
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(23));//choose team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_4) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(2));
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(24));//choose team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_5) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(2));
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(25));//choose team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_6) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(2));
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(26));//choose team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_7) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(2));
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(27));//choose team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_8) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(2));
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(28));//choose team count
									cooltime = 0;
								}
								if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_9) == GLFW.GLFW_PRESS){
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(2));
									PacketHandler.getPlayChannel().sendToServer(new MessageCommander(29));//choose team count
									cooltime = 0;
								}
							}
						}
					}
					{
						Vector3d lock = Vector3d.directionFromRotation(player.xRot, player.yRot);
						int range = 2;
						int ix = 0;
						int iy = 0;
						int iz = 0;
						for(int x2 = 0; x2 < 120; ++x2) {
							BlockPos blockpos = new BlockPos(player.getX() + lock.x * x2,player.getY() + 1.5 + lock.y * x2,player.getZ() + lock.z * x2);
							BlockState iblockstate = player.level.getBlockState(blockpos);
							if (!iblockstate.isAir(player.level, blockpos)){
								if(ix != 0 && iz != 0 && iy != 0) {
									this.posXm = ix;
									this.posYm = iy;
									this.posZm = iz;
									break;
								}
							}else{
								ix = (int) (player.getX() + lock.x * x2);
								iy = (int) (player.getY() + 1.5 + lock.y * x2);
								iz = (int) (player.getZ() + lock.z * x2);
								AxisAlignedBB axisalignedbb = (new AxisAlignedBB(ix-range, iy-range, iz-range, ix+range, iy+range, iz+range)).inflate(1D);
								List<Entity> llist = player.level.getEntities(player,axisalignedbb);
								if (llist != null) {
									for (int lj = 0; lj < llist.size(); lj++) {
										Entity entity1 = (Entity) llist.get(lj);
										if (entity1 != null && entity1 instanceof LivingEntity) {
											this.posXm = entity1.getX();
											this.posYm = entity1.getY();
											this.posZm = entity1.getZ();
											if(entity1 instanceof IMob){
												rendertype = 1;
												break;
											}else if(lctrol){
												rendertype = 2;
												break;
											}else{
												rendertype = 3;
												break;
											}
										}
									}
								}else{
									rendertype = 0;
								}
							}
						}
					}
				}
				if(lctrol)rendertype = 2;
				GlStateManager._pushMatrix();
				if(count<360F){
					++count;
				}else{
					count = 0;
				}
				if(!glow && iii<10F){
					++iii;
				}else{
					glow = true;
				}
				if(glow && iii>0){
					--iii;
				}else{
					glow = false;
				}
				GL11.glTranslatef((float) this.posXm+0.5F-(float)this.xo, (float) this.posYm-0.5F-(float)this.yo, (float) this.posZm+0.5F-(float)this.zo);
				Minecraft.getInstance().getTextureManager().bind(icon);
				if(rendertype==1){
					obj.renderPart("attack1");
				}else if(rendertype==2){
					obj.renderPart("fattack1");
				}else if(rendertype==3){
					obj.renderPart("no1");
				}
				if(rendertype==0)GL11.glRotatef(count, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(1+iii*0.02F, 1+iii*0.02F, 1+iii*0.02F);
				if(rendertype==1){
					obj.renderPart("attack");
				}else if(rendertype==2){
					obj.renderPart("fattack");
				}else if(rendertype==3){
					obj.renderPart("no");
				}else{
					obj.renderPart("move");
				}
				GlStateManager._popMatrix();
				
				if(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS){
					if(this.starttime <20)++this.starttime;
					if(this.starttime>3){
						RenderSystem.pushMatrix();
						RenderSystem.enableBlend();
						RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
						RenderSystem.color4f(0.0F, 1.0F, 0.0F, 0.75F);
						RenderSystem.disableTexture();
						//AxisAlignedBB range = (new AxisAlignedBB(this.posX1, this.posY1, this.posZ1, this.posX2, this.posY2, this.posZ2)).inflate(1);
						DebugRenderer.renderFilledBox((new AxisAlignedBB(this.posX1, this.posY1, this.posZ1, this.posX2, this.posY2, this.posZ2)).move(-this.xo, -this.yo, -this.zo), 0.0F, 1.0F, 0.0F, 0.5F);
						RenderSystem.color4f(0.0F, 1.0F, 0.0F, 0.75F);
						RenderSystem.enableTexture();
						RenderSystem.disableBlend();
						RenderSystem.popMatrix();
					}
				}else{
					if(this.starttime>0)this.starttime = 0;
				}
				GlStateManager._popMatrix();
			}
		}
    }
	
	public float recoil_shock = 0;
	
	//@OnlyIn(Dist.CLIENT)
    @SubscribeEvent//(priority = EventPriority.HIGHEST)
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event)
    {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null || minecraft.level == null)return;
		
		if(expPitch>0.1F){
			if (expPitch < 0.01F) expPitch = 0;
			if (Math.random() > 0.5f) {
				event.setRoll(event.getRoll() - expPitch* 0.1F * (float)WMConfig.CLIENT.display.shock_size.get()/100F);
			} else {
				event.setRoll(event.getRoll() + expPitch* 0.1F * (float)WMConfig.CLIENT.display.shock_size.get()/100F);
			}
		}
		ClientPlayerEntity player = minecraft.player;
		if (player.getVehicle() instanceof EntityWMSeat && player.getVehicle() != null) {
			EntityWMSeat seat = (EntityWMSeat)player.getVehicle();
			if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
				EntityWMVehicleBase ve = (EntityWMVehicleBase) seat.getVehicle();
				if(minecraft.options.getCameraType() != PointOfView.THIRD_PERSON_BACK)event.setRoll(event.getRoll() + ve.flyRoll);
				//Config.CLIENT.display.cameraRollEffect.set(false);
			}
		}
    }
	
	@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseScrollEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.level == null)return;
		ClientPlayerEntity player = mc.player;
		if(mc.screen != null)
		{
			return;
		}
		if (player.getVehicle() != null && player.getVehicle() instanceof EntityWMSeat) {
			EntityWMSeat ve = (EntityWMSeat) player.getVehicle();
			event.setCanceled(true);//关闭滚动
			//ve.onMouseMoved(event.getMouseX(), event.getMouseY());
		}
	}
	
    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        //if(event.phase != TickEvent.Phase.END)return;
        //onClientTickEnd(Minecraft.getInstance());
        switch (event.phase) {
            case START:
				onClientTickStart(Minecraft.getInstance());
				break;
			case END:
				onClientTickEnd(Minecraft.getInstance());
		}
    }
	
	public float flapsPitchLeft = 0;
	public float flapsPitchRight = 0;
	public static boolean tick = false;
	public static double LASTX = 0;
	public static double LASTY = 0;
	
	public static double FORWARD = 0;
	public static double STRAF = 0;
	
	public void MoveMouse(double mouseX, double mouseY){
		float sensitivity = 0.02F;//转速
		if(this.tick && this.LASTX != mouseX){
			this.LASTX = mouseX;
			this.LASTY = mouseY;
			this.tick = false;//关闭存储
		}
		if(!this.tick && this.LASTX != mouseX){
			this.flapsPitchLeft -= sensitivity * (this.LASTY-mouseY);
			this.flapsPitchRight -= sensitivity * (this.LASTY-mouseY);
			this.flapsPitchLeft -= sensitivity * (this.LASTX-mouseX);
			this.flapsPitchRight += sensitivity * (this.LASTX-mouseX);
			this.tick = true;//存储
		}
	}
	
	int cool;
    @SubscribeEvent
    public void onPostClientTick(TickEvent.ClientTickEvent event)
    {
		if(event.phase != TickEvent.Phase.END)
            return;
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null)
            return;
		Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = Minecraft.getInstance().player;
		if (mc.screen==null && player.getVehicle() instanceof EntityWMSeat && player.getVehicle() != null) {
			EntityWMSeat seat = (EntityWMSeat) player.getVehicle();
			if (seat.canBeSteered() && seat.getControllingPassenger() != null && seat.getHealth() > 0.0F){
				if(seat.getControllingPassenger() instanceof PlayerEntity)
				{
					if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
						EntityWMVehicleBase vehicle = (EntityWMVehicleBase) seat.getVehicle();
						int i = vehicle.getPassengers().indexOf(seat);
						if(i==0){
							if(cool<20)++cool;
							if(cool>10 && !vehicle.seatNoThird){
								if(mc.options.getCameraType() == PointOfView.THIRD_PERSON_BACK/*||GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_C) == GLFW.GLFW_PRESS*/) {//FIRST_PERSON
									if(vehicle.getChange()==0){
										PacketHandler.getPlayChannel().sendToServer(new MessageCame(1));
										cool = 0;
									}
								}else{
									if(vehicle.getChange()==1){
										PacketHandler.getPlayChannel().sendToServer(new MessageCame(0));
										cool = 0;
									}
								}
								
							}
							PlayerEntity player2 = (PlayerEntity) seat.getControllingPassenger();
							vehicle.setForwardMove(player2.zza);
							vehicle.setStrafingMove(player2.xxa);
							PacketHandler.getPlayChannel().sendToServer(new MessageThrottle(player2.zza,player2.xxa));
							if(vehicle.VehicleType>2){
								float limit = 15;
								this.MoveMouse(mc.mouseHandler.xpos(),mc.mouseHandler.ypos());
								float power = 2F;
								power = vehicle.throttle;
								flapsPitchLeft *= 0.9F;
								flapsPitchRight *= 0.9F;
								if(flapsPitchRight > limit)
									flapsPitchRight = limit;
								if(flapsPitchRight < -limit)
									flapsPitchRight = -limit;
								if(flapsPitchLeft > limit)
									flapsPitchLeft = limit;
								if(flapsPitchLeft < -limit)
									flapsPitchLeft = -limit;
								float sensitivityAdjust = 2.00677104758f - (float)Math.exp(-2.0f * power) / (4.5f * (power + 0.1f));
								sensitivityAdjust = MathHelper.clamp(sensitivityAdjust, 0.0f, 1.0f);
								sensitivityAdjust *= 0.125F;
								float flapsPitch = (flapsPitchLeft + flapsPitchRight) / 2F;
								float pitch = flapsPitch * (flapsPitch > 0 ? 1.2F : 1.0F) * sensitivityAdjust;
								float flapsRoll = (flapsPitchRight - flapsPitchLeft) / 2F;
								float roll = flapsRoll * (flapsRoll > 0 ? 1.0F : 1.0F) * sensitivityAdjust;
								/*if(vehicle.getArmyType1()==0)*/PacketHandler.getPlayChannel().sendToServer(new MessageRote(pitch,roll));//飞行类
							}
						}
					}
				}
			}
		}
        if(player != null)
        {
			ItemStack heldItem = player.getMainHandItem();
			if(heldItem.getItem() instanceof ItemGun)
			{
				int power_time_nbt = 0;
				CompoundNBT nbt = heldItem.getTag();
				if(nbt!=null)power_time_nbt = nbt.getInt("PowerTime");
				ItemGun gun = (ItemGun) heldItem.getItem();
				if(heldItem.getDamageValue() < heldItem.getMaxDamage() - 1)
				{
					if(mc.screen==null && GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS)
					{
					 if(sr_fire){
						 fire(player, heldItem);
						 //System.out.println("客1");
					 }
					 power_fire = false;
					}else{
					 sr_fire = true;
					 power_fire = true;
					}
					if(power_time_nbt>0 && power_fire)fire(player, heldItem);
				}
				/*if(WarMachineLib.proxy.isZooming())*/if(mc.screen==null && GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS){
					if(gun.aim_time<gun.time_aim)++gun.aim_time;//枪械瞄准动画
				}else{
					if(gun.aim_time>0)--gun.aim_time;//枪械瞄准动画
				}
			}
		}
	}

    public static boolean sr_fire = false;
	public static boolean power_fire = false;
    public static void fire(PlayerEntity player, ItemStack heldItem)
    {
        if(!(heldItem.getItem() instanceof ItemGun))
            return;
        if(player.isSpectator())
            return;
        CooldownTracker tracker = player.getCooldowns();
		int power_time_nbt = 0;
		CompoundNBT nbt = heldItem.getTag();
		if(nbt!=null)power_time_nbt = nbt.getInt("PowerTime");
        if(!tracker.isOnCooldown(heldItem.getItem()))
        {
            ItemGun gun = (ItemGun) heldItem.getItem();
			//int li = gun.getMaxDamage() - heldItem.getDamageValue();
			/*if(li>0)*/{
				/*if(gun.powergun){
					//WMLPacketHandler.INSTANCE.sendToServer(new MessagePower());
					PacketHandler.getPlayChannel().sendToServer(new MessagePower());//
					if(power_time_nbt>0 && power_fire){
						tracker.setCooldown(heldItem.getItem(), gun.cycle);
						gun.Fire_AR_Left(heldItem, player.level, player, li, true, false);//后座
						//WMLPacketHandler.INSTANCE.sendToServer(new MessageShoot());
						PacketHandler.getPlayChannel().sendToServer(new MessageShoot());//
					}
				}else*/{
					tracker.addCooldown(heldItem.getItem(), gun.cycle);
					if(gun.is_sr){
						gun.Fire_SR_Left(heldItem, player.level, player, true, false);//后座
						PacketHandler.getPlayChannel().sendToServer(new MessageShoot());//
						sr_fire = false;
					}else{
						gun.Fire_AR_Left(heldItem, player.level, player, true, false);//后座
						PacketHandler.getPlayChannel().sendToServer(new MessageShoot());//
						//System.out.println("客3");
						
					}
				}
			}
        }
    }

    public void onClientTickStart(Minecraft minecraft) {
        if (minecraft.player == null || minecraft.level == null)
            return;
		
        GUN_ROT_X_LAST = GUN_ROT_X;
        GUN_ROT_Y_LAST = GUN_ROT_Y;
        GUN_ROT_Z_LAST = GUN_ROT_Z;
		
		//Minecraft mc = FMLClientHandler.instance().getClient();
		Minecraft mc = Minecraft.getInstance();
        if (mc.getCameraEntity() != null) {
            if (mc.getCameraEntity().getYHeadRot() > mc.getCameraEntity().yRotO) {
                GUN_ROT_X += (mc.getCameraEntity().getYHeadRot() - mc.getCameraEntity().yRotO) / 1.5;
            } else if (mc.getCameraEntity().getYHeadRot() < mc.getCameraEntity().yRotO) {
                GUN_ROT_X -= (mc.getCameraEntity().yRotO - mc.getCameraEntity().getYHeadRot()) / 1.5;
            }
            if (mc.getCameraEntity().xRot > prevPitch) {
                GUN_ROT_Y += (mc.getCameraEntity().xRot - prevPitch) / 5;
            } else if (mc.getCameraEntity().xRot < prevPitch) {
                GUN_ROT_Y -= (prevPitch - mc.getCameraEntity().xRot) / 5;
            }
            prevPitch = mc.getCameraEntity().xRot;
        }

        GUN_ROT_X *= .2F;
        GUN_ROT_Y *= .2F;
        GUN_ROT_Z *= .2F;

        if (GUN_ROT_X > 20) {
            GUN_ROT_X = 20;
        } else if (GUN_ROT_X < -20) {
            GUN_ROT_X = -20;
        }

        if (GUN_ROT_Y > 20) {
            GUN_ROT_Y = 20;
        } else if (GUN_ROT_Y < -20) {
            GUN_ROT_Y = -20;
        }
		//ItemGun.fireButtonHeld = Mouse.isButtonDown(0);
    }
	
    public void onClientTickEnd(Minecraft minecraft) {
        if (minecraft.player == null || minecraft.level == null)return;
		if(twotime>0)--twotime;
        ClientPlayerEntity player = minecraft.player;

        if (playerRecoilPitch > 0)playerRecoilPitch *= 0.8F;

        if (playerRecoilYaw > 0)playerRecoilYaw *= 0.8F;

        player.xRot -= playerRecoilPitch;
        player.yRot -= playerRecoilYaw;
        antiRecoilPitch += playerRecoilPitch;
        antiRecoilYaw += playerRecoilYaw;

        player.xRot += antiRecoilPitch * 0.25F;
        player.yRot += antiRecoilYaw * 0.25F;
        antiRecoilPitch *= 0.75F;
        antiRecoilYaw *= 0.75F;

		if(expPitch>0)expPitch *= 0.8F;
		recoil_shock -= expPitch;
		
		//if(recoil_shock>1F)recoil_shock=1F;
		antiexpPitch += expPitch;
		recoil_shock += antiexpPitch * 0.25F;
		antiexpPitch *= 0.75F;
		InstantBulletRenderer.UpdateAllTrails();
    }
}

package wmlib.client;

import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.glfw.GLFW;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import wmlib.client.obj.SAObjModel;
import wmlib.common.network.message.MessageChoose;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageThrottle;
import wmlib.common.network.message.MessageRote;
import wmlib.common.network.message.MessageCame;
import wmlib.common.network.message.MessageCommander;
import wmlib.common.network.message.MessageShoot;
import wmlib.common.item.ItemGun;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.item.ItemMouse;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.client.render.InstantBulletRenderer;
//import com.mrcrayfish.guns.WMConfig;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer; // ClientPlayer -> LocalPlayer
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.ViewportEvent; // 替代 EntityViewRenderEvent
import net.minecraftforge.client.event.InputEvent;
import net.minecraft.client.MouseHandler; // MouseHelper -> MouseHandler
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraft.client.CameraType; // PointOfView -> CameraType
import net.minecraftforge.client.event.RenderLevelStageEvent; // RenderWorldLastEvent -> RenderLevelStageEvent
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.Camera; // 替代 ActiveRenderInfo
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemCooldowns;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderRote;
import net.minecraft.client.renderer.MultiBufferSource;
import wmlib.api.ITool;
import wmlib.WarMachineLib;
import wmlib.rts.network.F9Message;
import wmlib.rts.RtsMoShiScreen;
import wmlib.rts.WorldRenderHandler;
import org.joml.Matrix4f;
import wmlib.WMConfig;

import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ClientTickHandler{
    public ClientTickHandler() {
    }
	public int fxtime=0;
    private Player player;
    private double xo;
    private double yo;
    private double zo;
	public static int starttime = 0;
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/marker/point1.obj");
    private static final ResourceLocation icon = ResourceLocation.tryParse("wmlib:textures/marker/point1.png");
	static final RenderType rticon = SARenderState.getBlendDepthWrite(icon);//getBlendWrite_NoLight
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
    public void renderWorld(RenderLevelStageEvent event) {
		if(fxtime<200)++fxtime;
		if(cooltime<10){
			++cooltime;
		}
		Minecraft mc = Minecraft.getInstance();
		PoseStack stack = event.getPoseStack();
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)return;
		Camera camera = event.getCamera();
		Vec3 came_vec = camera.getPosition();
		
		this.player = mc.player;
		this.xo = came_vec.x();
		this.yo = came_vec.y()-1.5;
		this.zo = came_vec.z();
		/*this.xo = this.player.xOld + (this.player.getX() - this.player.xOld) * (double)event.getPartialTick();
		this.yo = this.player.yOld + (this.player.getY() - this.player.yOld) * (double)event.getPartialTick();
		this.zo = this.player.zOld + (this.player.getZ() - this.player.zOld) * (double)event.getPartialTick();*/
		if(fxtime>=200)InstantBulletRenderer.RenderAllTrails(stack, event.getPartialTick(),this.xo,this.yo,this.zo);
		if(player != null){
			ItemStack heldItem1 = this.player.getMainHandItem();
			ItemStack heldItem2 = this.player.getOffhandItem();
			if(!heldItem1.isEmpty() && heldItem1.getItem() instanceof ItemMouse||!heldItem2.isEmpty() && heldItem2.getItem() instanceof ItemMouse){
				/*if(cooltime>2 && GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_C) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageCommander(31));
					cooltime = 0;
				}*/
				stack.pushPose();
				int rendertype = 0;
				boolean lctrol = GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS;
				//boolean shiftctrol = GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS;
				{
					if(this.time>0 && mc.screen==null && player.getVehicle()==null){
						Vec3 lock = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
						int ix = 0;
						int iy = 0;
						int iz = 0;
						for(int x2 = 0; x2 < 120; ++x2) {
							BlockPos blockpos = new BlockPos((int)(player.getX() + lock.x * x2),(int)(player.getY() + 1.5 + lock.y * x2),(int)(player.getZ() + lock.z * x2));
							BlockState iblockstate = player.level().getBlockState(blockpos);
							if (!iblockstate.isAir()){
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
							if(mc.screen==null){
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
					double range = 0.5;
					if(mc.screen!=null && mc.screen instanceof RtsMoShiScreen){
						if(WorldRenderHandler.highlightedBlock!=null){
							this.posXm = WorldRenderHandler.highlightedBlock.getX();
							this.posYm = WorldRenderHandler.highlightedBlock.getY()+1;
							this.posZm = WorldRenderHandler.highlightedBlock.getZ();
							AABB axisalignedbb = (new AABB(posXm-range, posYm-range, posZm-range, posXm+range, posYm+range, posZm+range)).inflate(1D);
							List<Entity> llist = player.level().getEntities(player,axisalignedbb);
							if (llist != null) {
								for (int lj = 0; lj < llist.size(); lj++) {
									Entity entity1 = (Entity) llist.get(lj);
									if (entity1 != null && entity1 instanceof LivingEntity && !(entity1 instanceof ITool)) {
										this.posXm = entity1.getX();
										this.posYm = entity1.getY();
										this.posZm = entity1.getZ();
										if(entity1 instanceof Enemy){
											rendertype = 1;
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
					}else{
						// 优化后的射线检测，替代原循环
						Vec3 startVec = new Vec3(player.getX(), player.getY() + 1.5, player.getZ());
						Vec3 direction = Vec3.directionFromRotation(player.getXRot(), player.getYRot()); // 视线方向
						int maxDist = 120;
						Vec3 endVec = startVec.add(direction.scale(maxDist));
						// 1. 使用 ClipContext 获取第一个方块碰撞点（只考虑非空气且碰撞箱非空的方块）
						ClipContext clipCtx = new ClipContext(startVec, endVec,
								ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
						BlockHitResult blockHit = player.level().clip(clipCtx);
						Vec3 hitPoint = blockHit.getType() == HitResult.Type.MISS ? endVec : blockHit.getLocation();

						// 2. 在射线路径上检测实体（从起点到碰撞点，步长 0.5）
						double step = 0.5;
						int steps = (int) Math.ceil(hitPoint.distanceTo(startVec) / step);
						Entity hitEntity = null;
						boolean isEnemy = false; // 记录实体是否为敌人

						for (int i = 1; i <= steps; i++) {
							Vec3 current = startVec.add(direction.scale(i * step));
							// 以当前点为中心，半径 0.5 的 AABB（避免遗漏小实体）
							AABB box = new AABB(current.x - 0.5, current.y - 0.5, current.z - 0.5,
												current.x + 0.5, current.y + 0.5, current.z + 0.5);
							List<Entity> nearby = player.level().getEntities(player, box);
							for (Entity e : nearby) {
								if (e == player) continue;
								if (!(e instanceof LivingEntity)) continue;
								if (e instanceof ITool) continue; // 原代码中排除 ITool 类型
								hitEntity = e;
								isEnemy = e instanceof Enemy;      // 判断是否为敌人
								break;
							}
							if (hitEntity != null) break; // 找到实体，停止检测
						}
						// 3. 根据检测结果设置字段
						if (hitEntity != null) {
							// 命中实体：记录实体坐标，并设置 rendertype
							this.posXm = hitEntity.getX();
							this.posYm = hitEntity.getY();
							this.posZm = hitEntity.getZ();
							rendertype = isEnemy ? 1 : 3;
						} else {
							// 未命中实体：如果有方块碰撞点，记录碰撞点前一个空气位置（原逻辑：记录非空气方块的前一个空气位置）
							if (blockHit.getType() != HitResult.Type.MISS) {
								// 原代码：遇到非空气方块时，记录的是上一个空气位置的坐标（即射线进入方块前的点）
								// 这里模拟原行为：从碰撞点回退一步（方向相反，步长 1）
								Vec3 beforeBlock = hitPoint.subtract(direction.scale(1.0));
								this.posXm = beforeBlock.x;
								this.posYm = beforeBlock.y;
								this.posZm = beforeBlock.z;
							} else {
								// 未命中任何方块：记录射线终点（可选，原代码可能保持原值，这里设为终点）
								this.posXm = endVec.x;
								this.posYm = endVec.y;
								this.posZm = endVec.z;
							}
							// 原代码中 rendertype 在没有实体时可能保持原值或为 0，这里显式设为 0
							rendertype = 0;
						}
					}
				}
				if(lctrol)rendertype = 2;
				stack.pushPose();
				if(count<720F){
					++count;
				}else{
					count = 0;
				}
				if(!glow && iii<20F){
					++iii;
				}else{
					glow = true;
				}
				if(glow && iii>0){
					--iii;
				}else{
					glow = false;
				}
				stack.translate((float) this.posXm+0.5F-(float)this.xo, (float) this.posYm-1F-(float)this.yo, (float) this.posZm+0.5F-(float)this.zo);
				obj.setRender(rticon,null,stack,0xF000F0);
				if(rendertype==1){
					obj.renderPart("attack1");
				}else if(rendertype==2){
					obj.renderPart("fattack1");
				}else if(rendertype==3){
					obj.renderPart("no1");
				}
				if(rendertype==0)RenderRote.setRote(stack,count*0.5F, 0.0F, 1.0F, 0.0F);
				stack.scale(1+iii*0.01F, 1+iii*0.01F, 1+iii*0.01F);
				if(rendertype==1){
					obj.renderPart("attack");
				}else if(rendertype==2){
					obj.renderPart("fattack");
				}else if(rendertype==3){
					obj.renderPart("no");
				}else{
					obj.renderPart("move");
				}
				stack.popPose();
				
				if(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS && this.select){
					if(this.starttime <20)++this.starttime;
					if(this.starttime>3){
						MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
						stack.pushPose();
						RenderSystem.enableBlend();
						RenderSystem.blendFuncSeparate(
							GlStateManager.SourceFactor.SRC_ALPHA, 
							GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, 
							GlStateManager.SourceFactor.ONE, 
							GlStateManager.DestFactor.ZERO
						);
						RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 0.75F);
						Vec3 v3 = camera.getPosition().reverse();
						AABB box = new AABB(this.posX1, this.posY1, this.posZ1, this.posX2, this.posY2, this.posZ2);
						DebugRenderer.renderFilledBox(stack, bufferSource, box.move(v3), 0.0F, 1.0F, 0.0F, 0.5F);
						RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
						RenderSystem.disableBlend();
						stack.popPose();
						bufferSource.endBatch();
					}
				}else{
					if(this.starttime>0)this.starttime = 0;
				}
				stack.popPose();
			}
		}
    }
	
	public float recoil_shock = 0;
	
	@OnlyIn(Dist.CLIENT)
    @SubscribeEvent//(priority = EventPriority.HIGHEST)
    public void onCameraSetup(ViewportEvent.ComputeCameraAngles event)
    {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null || minecraft.level == null)return;
		
		if(expPitch>0.1F){
			if (expPitch < 0.01F) expPitch = 0;
			if (Math.random() > 0.5f) {
				event.setRoll(event.getRoll() - expPitch* 0.1F*(float)WMConfig.shock_size/100F);
			} else {
				event.setRoll(event.getRoll() + expPitch* 0.1F*(float)WMConfig.shock_size/100F);
			}
		}
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player.getVehicle() instanceof EntityWMSeat && player.getVehicle() != null && mc.options.getCameraType() != CameraType.THIRD_PERSON_BACK) {
			EntityWMSeat seat = (EntityWMSeat)player.getVehicle();
			if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
				EntityWMVehicleBase ve = (EntityWMVehicleBase) seat.getVehicle();
				event.setRoll(event.getRoll() + ve.flyRoll);
				//WMConfig.CLIENT.display.cameraRollEffect.set(false);
			}
		}
    }
	
	@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseScrollingEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.level == null)return;
		LocalPlayer player = mc.player;
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
		float sensitivity = 0.01F;//转速
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
        LocalPlayer player = Minecraft.getInstance().player;
		if (mc.screen==null && player.getVehicle() instanceof EntityWMSeat && player.getVehicle() != null) {
			EntityWMSeat seat = (EntityWMSeat) player.getVehicle();
			if (seat.canBeSteered() && seat.getControllingPassenger() != null && seat.getHealth() > 0.0F){
				if(seat.getControllingPassenger() instanceof Player)
				{
					if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
						EntityWMVehicleBase vehicle = (EntityWMVehicleBase) seat.getVehicle();
						int i = vehicle.getPassengers().indexOf(seat);
						if(i==0){
							if(cool<20)++cool;
							if(cool>10 && !vehicle.seatNoThird){
								if(mc.options.getCameraType() == CameraType.THIRD_PERSON_BACK/*||GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_C) == GLFW.GLFW_PRESS*/) {//FIRST_PERSON
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
							Player player2 = (Player) seat.getControllingPassenger();
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
								//sensitivityAdjust = Math.clamp(sensitivityAdjust, 0.0f, 1.0f);
								sensitivityAdjust *= 0.125F;
								float flapsPitch = (flapsPitchLeft + flapsPitchRight) / 2F;
								float pitch = flapsPitch * (flapsPitch > 0 ? 1.2F : 1.0F) * sensitivityAdjust;
								float flapsRoll = (flapsPitchRight - flapsPitchLeft) / 2F;
								float roll = flapsRoll * (flapsRoll > 0 ? 1.0F : 1.0F) * sensitivityAdjust;
								if(vehicle.getArmyType1()==0)PacketHandler.getPlayChannel().sendToServer(new MessageRote(pitch,roll));//飞行类
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
				CompoundTag nbt = heldItem.getTag();
				if(nbt!=null)power_time_nbt = nbt.getInt("PowerTime");
				ItemGun gun = (ItemGun) heldItem.getItem();
				if(heldItem.getDamageValue() < heldItem.getMaxDamage() - 1)
				{
					if(mc.screen==null && GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS)
					{
					 if(sr_fire){
						 fire(player, heldItem);
					 }
					 power_fire = false;
					}else{
					 sr_fire = true;
					 power_fire = true;
					}
					if(power_time_nbt>0 && power_fire)fire(player, heldItem);
				}
				if(mc.screen==null && GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS){
					if(gun.aim_time<gun.time_aim)++gun.aim_time;
				}else{
					if(gun.aim_time>0)--gun.aim_time;
				}
			}
		}
	}

    public static boolean sr_fire = false;
	public static boolean power_fire = false;
    public static void fire(Player player, ItemStack heldItem)
    {
        if(!(heldItem.getItem() instanceof ItemGun))
            return;
        if(player.isSpectator())
            return;
        ItemCooldowns tracker = player.getCooldowns();
		int power_time_nbt = 0;
		CompoundTag nbt = heldItem.getTag();
		if(nbt!=null)power_time_nbt = nbt.getInt("PowerTime");
        if(!tracker.isOnCooldown(heldItem.getItem())){
            ItemGun gun = (ItemGun) heldItem.getItem();
			int li = gun.getMaxDamage() - heldItem.getDamageValue();
			if(li>0){
				/*if(gun.powergun){
					//WMLPacketHandler.INSTANCE.sendToServer(new MessagePower());
					PacketHandler.getPlayChannel().sendToServer(new MessagePower());//
					if(power_time_nbt>0 && power_fire){
						tracker.setCooldown(heldItem.getItem(), gun.cycle);
						gun.Fire_AR_Left(heldItem, player.level(), player, li, true, false);
						//WMLPacketHandler.INSTANCE.sendToServer(new MessageShoot());
						PacketHandler.getPlayChannel().sendToServer(new MessageShoot());//
					}
				}else*/{
					tracker.addCooldown(heldItem.getItem(), gun.cycle);
					if(gun.is_sr){
						gun.Fire_SR_Left(heldItem, player.level(), player, true, false);
						PacketHandler.getPlayChannel().sendToServer(new MessageShoot());//
						sr_fire = false;
					}else{
						gun.Fire_AR_Left(heldItem, player.level(), player, true, false);
						PacketHandler.getPlayChannel().sendToServer(new MessageShoot());//
					}
				}
			}
        }
    }

    public void onClientTickStart(Minecraft mc) {
        if (mc.player == null || mc.level == null)
            return;
        GUN_ROT_X_LAST = GUN_ROT_X;
        GUN_ROT_Y_LAST = GUN_ROT_Y;
        GUN_ROT_Z_LAST = GUN_ROT_Z;
        if (mc.getCameraEntity() != null) {
            if (mc.getCameraEntity().getYHeadRot() > mc.getCameraEntity().yRotO) {
                GUN_ROT_X += (mc.getCameraEntity().getYHeadRot() - mc.getCameraEntity().yRotO) / 1.5;
            } else if (mc.getCameraEntity().getYHeadRot() < mc.getCameraEntity().yRotO) {
                GUN_ROT_X -= (mc.getCameraEntity().yRotO - mc.getCameraEntity().getYHeadRot()) / 1.5;
            }
            if (mc.getCameraEntity().getXRot() > prevPitch) {
                GUN_ROT_Y += (mc.getCameraEntity().getXRot() - prevPitch) / 5;
            } else if (mc.getCameraEntity().getXRot() < prevPitch) {
                GUN_ROT_Y -= (prevPitch - mc.getCameraEntity().getXRot()) / 5;
            }
            prevPitch = mc.getCameraEntity().getXRot();
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
    }
	
    public void onClientTickEnd(Minecraft minecraft) {
        if (minecraft.player == null || minecraft.level == null)return;
		
		if(twotime>0)--twotime;
		
        LocalPlayer player = minecraft.player;

        if (playerRecoilPitch > 0)playerRecoilPitch *= 0.8F;

        if (playerRecoilYaw > 0)playerRecoilYaw *= 0.8F;

        player.setXRot(player.getXRot()-playerRecoilPitch);
		player.setYRot(player.getYRot()-playerRecoilYaw);

        antiRecoilPitch += playerRecoilPitch;
        antiRecoilYaw += playerRecoilYaw;

        player.setXRot(player.getXRot()+antiRecoilPitch * 0.25F);
		player.setYRot(player.getYRot()+antiRecoilYaw * 0.25F);

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

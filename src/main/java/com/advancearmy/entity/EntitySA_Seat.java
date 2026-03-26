package advancearmy.entity;
import java.util.List;
import javax.annotation.Nullable;
import java.util.Random;
import org.lwjgl.glfw.GLFW;
import net.minecraftforge.fml.ModList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level; // Level 替代 World
import net.minecraft.server.level.ServerLevel; // 原 ServerLevel
import net.minecraft.world.InteractionHand; // InteractionHand 重命名
import net.minecraft.sounds.SoundEvent; // SoundEvent 路径变更
import net.minecraft.core.BlockPos; // BlockPos 路径变更
import net.minecraft.util.Mth; // Mth 更名为 Mth
import net.minecraft.world.phys.Vec3; // Vec3 更名为 Vec3
import net.minecraft.sounds.SoundEvents; // SoundEvents 路径变更
import net.minecraft.world.scores.Team; // Scoreboard Team
// 实体和属性
import net.minecraft.world.entity.ai.attributes.AttributeSupplier; // 替代 AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player; // Player -> Player
import net.minecraft.world.entity.Mob; // Mob 替代部分 PathfinderMob
import net.minecraft.world.entity.Entity; // 基础 Entity 类
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityDimensions; // EntityDimensions 更名为 EntityDimensions
import net.minecraft.world.entity.EntityType;
// 路径查找
import net.minecraft.world.entity.ai.navigation.PathNavigation; // PathNavigation 更名
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation; // GroundPathNavigation
// 网络和交互
import net.minecraftforge.network.NetworkHooks; // 网络包路径变更
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
// 杂项
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB; // AABB 更名为 AABB
import net.minecraft.world.phys.Vec2; // Vec2 更名为 Vec2
import net.minecraft.world.entity.ai.targeting.TargetingConditions; // TargetingConditions 更名
import net.minecraft.world.InteractionResult; // InteractionResult 更名
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Enemy;
import advancearmy.init.ModEntities;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import wmlib.common.world.WMExplosionBase;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageTrail;
import wmlib.common.network.message.MessageFire;
import wmlib.common.network.message.MessageThrottle;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.api.ITool;
import wmlib.common.living.AI_MissileLock;
import advancearmy.entity.ai.AI_EntityWeapon;
import wmlib.util.ThrowBullet;
import safx.SagerFX;

import wmlib.common.living.ai.LivingLockGoal;
import advancearmy.event.SASoundEvent;
import advancearmy.item.ItemSpawn;
import advancearmy.item.ItemCapture;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import advancearmy.util.TargetSelect;
public class EntitySA_Seat extends EntityWMSeat{
	//protected PathNavigation navigationsa;
	public EntitySA_Seat(EntityType<EntitySA_Seat>type, Level worldIn) {
		super(type, worldIn);
		//this.navigationsa = this.createNavigation(worldIn);
		this.seatTurret=true;
		this.lockTargetSound=SASoundEvent.growler_lock.get();
	}

	public boolean ridding_rotemgPitch = false;
	public boolean seatRotePitch = false;
	public int seatMaxCount = 1;
	public double[] seatPosX = new double[1];
	public double[] seatPosY = new double[1];
	public double[] seatPosZ = new double[1];
	public double[] seatRoteX = new double[1];
	public double[] seatRoteY = new double[1];
	public double[] seatRoteZ = new double[1];
	//public boolean[] weaponThreeFire = new boolean[2];
	public boolean changeThrow = false;
	public boolean onlythrow = false;
	public EntitySA_Seat(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_SEAT.get(), worldIn);
	}

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.FOLLOW_RANGE, 50)
                .add(Attributes.KNOCKBACK_RESISTANCE, 20)
                .add(Attributes.MOVEMENT_SPEED, 0);
    }

	protected boolean canAddPassenger(Entity passenger) {//
		return this.getPassengers().size() < 1;
	}

	@Override
	public boolean isPushable() {
		return false;  // 防止被其他实体推动
	}
	/*@Override
	protected void pushEntities() {
		// 不执行任何操作，防止推动其他实体
	}*/
	@Override
	public boolean canBeCollidedWith() {
		return false;  // 防止与其他实体碰撞
	}
	@Override
	public boolean isPushedByFluid() {
		return false;  // 防止被流体推动
	}
	@Override
	public boolean canCollideWith(Entity entity) {
		return false;  // 防止与任何实体碰撞
	}
	public void push(Entity entity) {
	}
	
	
	public boolean canRid() {//
		return this.getPassengers().size() < 1;
	}

	public int interact_time = 0;
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if(player.getVehicle()==null){
			{
				if(this.canAddPassenger(player)&&!player.isSecondaryUseActive()){
					if(this.getAnyPassenger()==null && player.isCreative()){
						ItemStack heldItem = player.getItemInHand(hand);
						Item item = heldItem.getItem();
						if(item instanceof ItemSpawn spawn){
							spawn.spawnAddSeat(player.level(), player, this.getX(), this.getY(), this.getZ(),this);
							return InteractionResult.sidedSuccess(this.level().isClientSide);
						}
						if(item instanceof ItemCapture spawn){
							spawn.spawnAddSeat(player.level(), player, this.getX(), this.getY(), this.getZ(),this);
							return InteractionResult.sidedSuccess(this.level().isClientSide);
						}
					}
					if(this.getVehicle()!=null){
						if(this.getVehicle() instanceof WeaponVehicleBase ve){
							ve.setSeat = true;
						}
					}
					if (!this.level().isClientSide) {
						player.startRiding(this);
					}
					return InteractionResult.sidedSuccess(this.level().isClientSide);
				}else{
					if(this.getAnyPassenger()!=null&&this.getAnyPassenger() instanceof TamableAnimal){
						if(((TamableAnimal)this.getAnyPassenger()).getOwner()==player||player.isCreative()){
							this.getAnyPassenger().unRide();
						}
					}
					return InteractionResult.sidedSuccess(this.level().isClientSide);
				}
			}
		}
		return super.mobInteract(player, hand);
    }

	public static float clampCount(float f1, float f2, float min, float max) {
		float x = f1-f2;
		if (x < min) {
			return (f2+min);
		} else {
			return x > max ? (f2+max) : f1;
		}
	}
	@OnlyIn(Dist.CLIENT)
	public void applyOrientationToEntity(Entity passenger) {//乘客方向
		this.applyYawToTurret(passenger);
	}
	@OnlyIn(Dist.CLIENT)
	public void onPassengerTurned(Entity passenger) {
		this.applyYawToTurret(passenger);
	}
	
	public int startRidTime = 0;
	protected void applyYawToTurret(Entity passenger){
		if(this.getVehicle()!=null){
			int i = this.getVehicle().getPassengers().indexOf(this);
			if(i==0){
				if(this.getVehicle() instanceof WeaponVehicleBase){
					WeaponVehicleBase ve = (WeaponVehicleBase)this.getVehicle();
					if(this.getNpcPassenger()!=null && ve.isAttacking()){
						PathfinderMob gunner = (PathfinderMob)this.getNpcPassenger();
						this.yHeadRot=ve.turretYaw;
						gunner.yHeadRot=this.yHeadRot;
						gunner.setYRot(this.yHeadRot);
					}
					if(startRidTime<10&&this.getControllingPassenger()!=null){
						float currentYaw = passenger.getYRot();
						float targetYaw = ve.turretYaw;
						float angleDiff = targetYaw - currentYaw;
						if (angleDiff > 180.0F) {
							angleDiff -= 360.0F;
						} else if (angleDiff < -180.0F) {
							angleDiff += 360.0F;
						}
						if (Mth.abs(angleDiff) > 170.0F && Math.abs(angleDiff) < 190.0F) {
							passenger.setYRot(targetYaw);
						} else {
							passenger.setYRot(Mth.lerp(0.5F, currentYaw, targetYaw));
						}
						passenger.setXRot(Mth.lerp(0.5F, passenger.getXRot(), ve.turretPitch));
					}else{
						if(ve.VehicleType>2){
							if(ve.getArmyType1()==0){
								float currentYaw = passenger.getYRot();
								float targetYaw = ve.getYRot();
								float angleDiff = targetYaw - currentYaw;
								if (angleDiff > 180.0F) {
									angleDiff -= 360.0F;
								} else if (angleDiff < -180.0F) {
									angleDiff += 360.0F;
								}
								if (Mth.abs(angleDiff) > 170.0F && Math.abs(angleDiff) < 190.0F) {
									passenger.setYRot(targetYaw);
								} else {
									passenger.setYRot(Mth.lerp(0.5F, currentYaw, targetYaw));
								}
								passenger.setXRot(Mth.lerp(0.5F, passenger.getXRot(), ve.flyPitch));
							}
						}else{
							if(!ve.isthrow || ve.getMoveMode()==0){
								setPassengerYaw(passenger);
								float f2 = Mth.wrapDegrees(passenger.getXRot() - 0);//this.getXRot()
								float f22 = Mth.clamp(f2, this.turretPitchMax, this.turretPitchMin);
								passenger.xRotO += f22 - f2;
								passenger.setXRot(passenger.getXRot()+f22 - f2);
								passenger.setXRot(passenger.getXRot());
							}
						}
					}
				}
			}else{
				if(this.getVehicle() instanceof WeaponVehicleBase){
					WeaponVehicleBase ve = (WeaponVehicleBase)this.getVehicle();
					setPassengerYaw(passenger);
					float f2 = Mth.wrapDegrees(passenger.getXRot() - ve.flyPitch);//this.getXRot()
					float f22 = Mth.clamp(f2, this.turretPitchMax, this.turretPitchMin);
					passenger.xRotO += f22 - f2;
					passenger.setXRot(passenger.getXRot()+f22 - f2);
					passenger.setXRot(passenger.getXRot());
				}
			}
		}
    }

	void setPassengerYaw(Entity passenger){
		if (Math.abs(this.maxyaw - this.minyaw) < 360.0F){
			float baseYaw = this.getYRot();
			float passengerYaw = passenger.getYRot();
			float minRel = this.minyaw;
			float maxRel = this.maxyaw;
			// 原始相对角度（未标准化）
			float rawRel = passengerYaw - baseYaw;
			// 将 minRel 和 maxRel 归一化到 [0,360)
			float minRelNorm = (float) normalizeAngle(minRel);
			float maxRelNorm = (float) normalizeAngle(maxRel);
			// 将 rawRel 归一化到 [0,360)
			float relNorm = (float) normalizeAngle(rawRel);
			// 判断 relNorm 是否在允许的弧段内
			boolean inRange;
			if (minRelNorm <= maxRelNorm) {
				// 弧段不跨越 0°
				inRange = relNorm >= minRelNorm && relNorm <= maxRelNorm;
			} else {
				// 弧段跨越 0°，实际为 [minRelNorm, 360) ∪ [0, maxRelNorm]
				inRange = relNorm >= minRelNorm || relNorm <= maxRelNorm;
			}
			float clampedRel;
			if (inRange) {
				clampedRel = rawRel; // 保持原值
			} else {
				// 计算两个端点最接近 rawRel 的表示（考虑周期性）
				float minClosest = minRel + 360.0F * Math.round((rawRel - minRel) / 360.0F);
				float maxClosest = maxRel + 360.0F * Math.round((rawRel - maxRel) / 360.0F);
				// 选择距离更近的端点
				if (Math.abs(minClosest - rawRel) <= Math.abs(maxClosest - rawRel)) {
					clampedRel = minClosest;
				} else {
					clampedRel = maxClosest;
				}
			}
			// 计算最终绝对角度并标准化到 [-180,180]
			float newYaw = Mth.wrapDegrees(baseYaw + clampedRel);
			passenger.yRotO = newYaw;
			passenger.setYRot(newYaw);
			passenger.setYHeadRot(newYaw);
		}
	}
	
	private static double normalizeAngle(double angle) {
		angle = angle % 360.0;
		if (angle < 0) {
			angle += 360.0;
		}
		return angle;
	}
	/**
	 * 将当前角度平滑地转向目标角度，最大步长为 maxDelta。
	 */
	private float rotateTowards(float current, float target, float maxDelta) {
		float delta = Mth.wrapDegrees(target - current);
		if (Math.abs(delta) <= maxDelta) {
			return target;
		}
		return current + (delta > 0 ? maxDelta : -maxDelta);
	}


	public static boolean powerfire = false;
	public void setFire(int id)
    {
		if(id==1){
			this.fire1 = true;
		}
		if(id==2){
			this.fire2 = true;
		}
		if(id==3){
			this.keyf = true;
		}
		if(id==4){
			this.keylook = true;
		}
		if(id==5){
			this.keyg = true;
		}
		if(id==6){
			this.keyx = true;
		}
		if(id==7){
			this.keyv = true;
		}
		if(id==8){
			this.powerfire = true;
		}
		if(id==9){
			this.powerfire = false;
		}
		if(id==10){
			this.keyr = true;
		}
		if(id==11){
			this.keyc = true;
		}
		if(id==12){
			this.keyrun = true;
		}
		if(id==13){
			this.keyj = true;
		}
		if(id==14){
			this.keyk = true;
		}
	}

	int changetime = 0;
	int aimtime = 0;
	int keytime = 0;
	@OnlyIn(Dist.CLIENT)
    public void onClientUpdate()
    {
		Minecraft mc = Minecraft.getInstance();
        if (this.getControllingPassenger()!=null && mc.screen==null){
			if(aimtime<10)++aimtime;
			if(keytime<15)++keytime;
			if(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS){
				PacketHandler.getPlayChannel().sendToServer(new MessageFire(1));
				if(!this.powerfire)PacketHandler.getPlayChannel().sendToServer(new MessageFire(8));
			}else{
				if(this.powerfire)PacketHandler.getPlayChannel().sendToServer(new MessageFire(9));
			}
			if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS){
				PacketHandler.getPlayChannel().sendToServer(new MessageFire(2));
			}
			if(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS){
				if(aimtime>8){
					if(this.isZoom){
						this.isZoom = false;
						this.getControllingPassenger().playSound(SoundEvents.BARREL_CLOSE, 1.0F, 1.0F);
					}else{
						this.isZoom = true;	
						this.getControllingPassenger().playSound(SoundEvents.BARREL_OPEN, 1.0F, 1.0F);
					}
					aimtime=0;
				}
			}
			if(keytime>8){
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_F) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(3));
				}
				
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(12));
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(4));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_G) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(5));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(6));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_V) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(7));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_R) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(10));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_C) == GLFW.GLFW_PRESS){
					//PacketHandler.getPlayChannel().sendToServer(new MessageFire(11));
					if(this.showhelp){
						this.showhelp = false;
					}else{
						this.showhelp = true;
					}
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_N) == GLFW.GLFW_PRESS && this.canNightV){
					if(this.openNightV){
						this.openNightV = false;
						this.getControllingPassenger().playSound(SoundEvents.BARREL_CLOSE, 1.0F, 1.0F);
					}else{
						this.openNightV = true;	
						this.getControllingPassenger().playSound(SoundEvents.BARREL_OPEN, 1.0F, 1.0F);
					}
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_J) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(13));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_K) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(14));
					keytime=0;
				}
			}
		}else{
			if(this.isZoom){
				this.isZoom = false;
			}
			/*if(this.showhelp){
				this.showhelp = false;
			}*/
		}
	}
	
	public boolean NotFriend(Entity target){
		if(!(target instanceof EntityWMSeat)){
			if(this.getVehicle()!=null && target == this.getVehicle())return false;
			Team team = this.getTeam();
			Team team1 = target.getTeam();
			boolean attack = true;
			if(target instanceof TamableAnimal && this.getAnyPassenger()!=null){
				TamableAnimal soldier = (TamableAnimal)target;
				if(soldier.getOwner()!=null && soldier.getOwner()==this.getAnyPassenger())return false;
				if(this.getAnyPassenger() instanceof TamableAnimal){
					TamableAnimal rider = (TamableAnimal)target;
					if(rider.getOwner()!=null && rider.getOwner()==soldier.getOwner())return false;
				}
			}
			if(team != null && team1 == team)return false;
			if(this.getTargetType()==2){
				if(target instanceof Enemy && (team == null||team != team1))return false;
			}
			return attack;
    	}else{
			return false;
		}
	}
	
    public void onFireAnimation(float count, float roll) {
		if(roll!=0)RenderParameters.expPitch += roll;
		if(count!=0){
			RenderParameters.rate = Math.min(RenderParameters.rate + 0.07f, 1);
			float recoilPitchGripFactor = 1.0f;
			float recoilYawGripFactor = 1.0f;
			float recoilPitchBarrelFactor = 1.0f;
			float recoilYawBarrelFactor = 1.0f;
			float recoilPitchStockFactor = 1.0f;
			float recoilYawStockFactor = 1.0f;
			float recoil = count;
			float recoilPitch1 = recoil;//垂直后座
			float recoilPitch2 = recoil/4;//垂直浮动
			float recoilYaw1 = recoil/10;//水平后座
			float recoilYaw2 = recoil/20;//水平浮动
			float offsetYaw = 0;
			float offsetPitch = 0;
			offsetPitch = recoilPitch1;
			offsetPitch += ((recoilPitch2 * 2) - recoilPitch2);
			offsetPitch *= (recoilPitchGripFactor * recoilPitchBarrelFactor * recoilPitchStockFactor);
			offsetYaw = recoilYaw1;
			offsetYaw *= new Random().nextFloat() * (recoilYaw2 * 2) - recoilYaw2;
			offsetYaw *= recoilYawGripFactor * recoilYawBarrelFactor * recoilYawStockFactor;
			offsetYaw *= RenderParameters.rate;
			offsetYaw *= RenderParameters.phase ? 1 : -1;
			RenderParameters.playerRecoilPitch += offsetPitch;
			if (Math.random() > 0.5f) {
				RenderParameters.playerRecoilYaw += offsetYaw;
			} else {
				RenderParameters.playerRecoilYaw -= offsetYaw;
			}
			RenderParameters.phase = !RenderParameters.phase;
		}
    }
	
	public void setSize(float w,float h){
		this.boxwidth = w;
		this.boxheight = h;
	}

	public int[] bulletid = new int[2];
	public int[] bullettype = new int[2];
	public int[] bulletdamage = new int[2];
	public int[] bulletcount = new int[2];
	public int[] bullettime = new int[2];
	public int reloadSoundStart1 = 20;
	public int reloadSoundStart2 = 20;
	public float[] bulletspeed = new float[2];
	public float[] bulletspread = new float[2];
	public float[] bulletexp = new float[2];
	public float[] bulletgravity = new float[2];
	public float[] fireposX = new float[2];
	public float[] fireposY = new float[2];
	public float[] fireposZ = new float[2];
	public float[] firebaseX = new float[2];
	public float[] firebaseZ = new float[2];
	public boolean[] bulletdestroy = new boolean[2];
	public boolean[] followvehicle = new boolean[2];
	public boolean[] weaponcross = new boolean[2];
	public String bulletmodel1 = "advancearmy:textures/entity/bullet/bullet.obj";
	public String bullettex1 = "advancearmy:textures/entity/bullet/bullet.png";
	public String firefx1 = "SmokeGun";
	public String bulletfx1 = null;
	public String bulletmodel2 = "advancearmy:textures/entity/bullet/bullet.obj";
	public String bullettex2 = "advancearmy:textures/entity/bullet/bullet.png";
	public String firefx2 = "SmokeGun";
	public String bulletfx2 = null;
	public SoundEvent reloadSound1;
	public SoundEvent reloadSound2;
	public SoundEvent firesound1;
	public SoundEvent firesound1_3p=null;;
	public SoundEvent firesound2;
	
	public boolean[] laserweapon = new boolean[2];
	public int[] connect_cout = new int[2];
	public boolean[] can_connect = new boolean[2];
	public float[] laserwidth = new float[2];
	public String laser_model_tex1 = "advancearmy:textures/entity/flash/aa_beam";
	public String laser_model_tex2 = "advancearmy:textures/entity/flash/aa_beam";
	public String laserfxfire1 = "LaserFlashGun";
	public String laserfxhit1 = "LaserHit";
	public String laserfxfire2 = "LaserFlashGun";
	public String laserfxhit2 = "LaserHit";
	
	public boolean isthrow = false;
	
	public void setWeapon(int wpid,int id,
		String model, String tex, String fx1, String fx2, SoundEvent sound, 
		float w, float h, float z, float bx, float bz,
		int damage, float speed, float recoil, float ex, boolean extrue, int count,  float gra, int maxtime, int typeid){
		if(wpid<2){
			this.bulletid[wpid] = id;
			this.bullettype[wpid] = typeid;
			this.bulletdamage[wpid] = damage;
			this.bulletspeed[wpid] = speed;
			this.bulletspread[wpid] = recoil;
			this.bulletexp[wpid] = ex;
			this.bulletdestroy[wpid] = extrue;
			this.bulletcount[wpid] = count;
			this.bulletgravity[wpid] = gra;
			this.bullettime[wpid] = maxtime;
			if(wpid==0){
				this.bulletmodel1 = model;
				this.bullettex1 = tex;
				this.firefx1 = fx1;
				this.bulletfx1 = fx2;
				this.firesound1 = sound;
			}else{
				this.bulletmodel2 = model;
				this.bullettex2 = tex;
				this.firefx2 = fx1;
				this.bulletfx2 = fx2;
				this.firesound2 = sound;
			}
			this.fireposX[wpid] = w;
			this.fireposY[wpid] = h;
			this.fireposZ[wpid] = z;
			this.firebaseX[wpid] = bx;
			this.firebaseZ[wpid] = bz;
		}
	}
	
	public void weaponActive1(){
		float side = 1.57F;
		if(this.weaponcross[0]){
			if(this.getRemain1()%2==0){
				side = -1.57F;
			}else{
				side = 1.57F;
			}
		}
		Entity locktarget = null;
		if(this.mitarget!=null){
			locktarget = this.mitarget;
		}else{
			locktarget = this.getTarget();
		}
		LivingEntity shooter = (WeaponVehicleBase)this.getVehicle();
		if(this.getAnyPassenger()!=null){
			shooter = this.getAnyPassenger();
		}
		double px = this.getX();
		double py = this.getY();
		double pz = this.getZ();
		float wx = this.turretPitch;
		float wy = this.turretYaw;
		if(followvehicle[0]){
			px = shooter.getX();
			py = shooter.getY();
			pz = shooter.getZ();
			wx = shooter.getXRot();
			wy = shooter.getYRot();
		}
		if(this.firesound1_3p!=null && this.getNpcPassenger()!=null){
			this.firesound1=this.firesound1_3p;
		}
		if(shooter!=null){
			if(this.laserweapon[0]){
				Attacklaser(this, shooter, locktarget, this.bulletid[0], this.laser_model_tex1, this.laserfxfire1, this.laserfxhit1, 
				this.firesound1,side, this.fireposX[0],this.fireposY[0],this.fireposZ[0],this.firebaseX[0],this.firebaseZ[0],px, py, pz,wy, wx,
				this.bulletdamage[0], this.bulletspeed[0], this.laserwidth[0], this.bulletexp[0], this.can_connect[0], this.connect_cout[0], this.bullettime[0]);
			}else{
				AI_EntityWeapon.Attacktask(this, shooter, locktarget, this.bulletid[0], this.bulletmodel1, this.bullettex1, this.firefx1, this.bulletfx1, this.firesound1,side, this.fireposX[0],this.fireposY[0],this.fireposZ[0],this.firebaseX[0],this.firebaseZ[0],px, py, pz,wy, wx,this.bulletdamage[0], this.bulletspeed[0], this.bulletspread[0], this.bulletexp[0], this.bulletdestroy[0], this.bulletcount[0], this.bulletgravity[0], this.bullettime[0], this.bullettype[0]);
			}
		}
	}
	public void weaponActive2(){
		float side = 1.57F;
		if(this.weaponcross[1]){
			if(this.getRemain2()%2==0){
				side = -1.57F;
			}else{
				side = 1.57F;
			}
		}
		Entity locktarget = null;
		if(this.mitarget!=null){
			locktarget = this.mitarget;
		}else{
			locktarget = this.getTarget();
		}
		LivingEntity shooter = (WeaponVehicleBase)this.getVehicle();
		if(this.getAnyPassenger()!=null){
			shooter = this.getAnyPassenger();
		}
		double px = this.getX();
		double py = this.getY();
		double pz = this.getZ();
		float wx = this.turretPitch;
		float wy = this.turretYaw;
		if(followvehicle[1]){
			px = shooter.getX();
			py = shooter.getY();
			pz = shooter.getZ();
			wx = 0;//shooter.getXRot()
			wy = shooter.getYRot();
		}
		if(shooter!=null){
			if(this.laserweapon[1]){
				Attacklaser(this, shooter, locktarget, this.bulletid[1], this.laser_model_tex2, this.laserfxfire2, this.laserfxhit2, 
				this.firesound2,side, this.fireposX[1],this.fireposY[1],this.fireposZ[1],this.firebaseX[1],this.firebaseZ[1],px, py, pz,wy, wx,
				this.bulletdamage[1], this.bulletspeed[1], this.laserwidth[1], this.bulletexp[1], this.can_connect[1], this.connect_cout[1], this.bullettime[1]);
			}else{
				AI_EntityWeapon.Attacktask(this, shooter, locktarget, this.bulletid[1], this.bulletmodel2, this.bullettex2, this.firefx2, this.bulletfx2, this.firesound2,side, this.fireposX[1],this.fireposY[1],this.fireposZ[1],this.firebaseX[1],this.firebaseZ[1],px, py, pz,wy, wx,this.bulletdamage[1], this.bulletspeed[1], this.bulletspread[1], this.bulletexp[1], this.bulletdestroy[1], this.bulletcount[1], this.bulletgravity[1], this.bullettime[1], this.bullettype[1]);
			}
		}
	}
	public Vec2 getLockVector() {
	  return new Vec2(this.turretPitch, this.turretYaw);
	}
	LivingEntity laserTarget = null;
	LivingEntity rangeTarget = null;
	public void Attacklaser(Entity living, LivingEntity shooter, Entity target, int id,
		String modeltex, String fxfire, String fxhit, SoundEvent sound, 
		float f, double w, double h, double z, double bx, double bz, double px, double py, double pz, float rotey, float rotex,
		int damage, float speed, float width, float exlevel, boolean isconnect, int maxcount, int maxtime){
    	int ra = living.level().random.nextInt(10) + 1;
    	float val = ra * 0.02F;
    	if(sound != null)living.playSound(sound, 5.0F, 0.9F + val);
		double xx11 = 0;
		double zz11 = 0;
		double term = (z - bz) * (z - bz) + (w - bx) * (w - bx);
		if (term < 0)term = 0;
		rotex = rotex % 360;
		double radians = -rotex * Math.PI / 180;
		double sinVal = Math.sin(radians);
		double base = Math.sqrt(term) * sinVal;
		xx11 -= Mth.sin(rotey * 0.01745329252F) * z;
		zz11 += Mth.cos(rotey * 0.01745329252F) * z;
		xx11 -= Mth.sin(rotey * 0.01745329252F + 1.57F) * w;
		zz11 += Mth.cos(rotey * 0.01745329252F + 1.57F) * w;
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX(fxfire, null, px+xx11, py+h+base, pz+zz11, 0, 0, 0, exlevel);
		Vec3 locken = Vec3.directionFromRotation(this.getLockVector());
		float d = speed*maxtime;
		if(width<1)width=1;
		int ix = 0;
		int iy = 0;
		int iz = 0;
		boolean stop = false;
		int count = 0;
		int pierce = 0;
		for(int xxx = 0; xxx < 120; ++xxx) {
			ix = (int) (px+xx11 + locken.x * xxx);
			iy = (int) (py+h+base + locken.y * xxx);
			iz = (int) (pz+zz11 + locken.z * xxx);
			BlockPos blockpos = new BlockPos(ix, iy, iz);
			BlockState iblockstate = living.level().getBlockState(blockpos);
			if (!iblockstate.isAir()){
				break;
			}else{
				AABB axisalignedbb = (new AABB(ix-width, iy-width, iz-width, 
						ix+width, iy+width, iz+width)).inflate(1D);
				List<Entity> llist = living.level().getEntities(living,axisalignedbb);
				if (llist != null) {
					for (int lj = 0; lj < llist.size(); lj++) {
						Entity entity1 = (Entity) llist.get(lj);
						if (entity1 != null && entity1 instanceof LivingEntity) {
							if (NotFriend(entity1) && entity1 != shooter && entity1 != living) {
								laserTarget = (LivingEntity)entity1;
								if(laserTarget.getVehicle()!=null && laserTarget.getVehicle() instanceof LivingEntity){
									LivingEntity ve = (LivingEntity)laserTarget.getVehicle();
									ve.invulnerableTime = 0;
									ve.hurt(this.damageSources().thrown(living, shooter), damage);
									ve.setSecondsOnFire(8);
								}else{
									laserTarget.invulnerableTime = 0;
									laserTarget.hurt(this.damageSources().thrown(living, shooter), damage);
									laserTarget.setSecondsOnFire(8);
								}
								if(laserTarget!=null && isconnect){
									rangeTarget = laserTarget.level().getNearestEntity(Mob.class, TargetingConditions.forCombat().range(exlevel*8).selector((attackentity) -> {return NotFriend(attackentity);}), 
									laserTarget, laserTarget.getX(), laserTarget.getEyeY(), laserTarget.getZ(), laserTarget.getBoundingBox().inflate(exlevel*8, exlevel*4, exlevel*8));
									if(rangeTarget!=null){
										rangeTarget.hurt(this.damageSources().thrown(living, shooter), damage*0.5F);
										{
											if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX(fxhit, null, rangeTarget.getX(), rangeTarget.getBoundingBox().minY + rangeTarget.getEyeHeight()/2F+1F, rangeTarget.getZ(), 0, 0, 0, 1);
											MessageTrail messageBulletTrail = new MessageTrail(true, id, modeltex, laserTarget.getX(), laserTarget.getBoundingBox().minY + laserTarget.getEyeHeight()/2F - 0.1, laserTarget.getZ(), rangeTarget.getDeltaMovement().x, rangeTarget.getDeltaMovement().z, rangeTarget.getX(), rangeTarget.getBoundingBox().minY + rangeTarget.getEyeHeight()/2F - 0.1, rangeTarget.getZ(), maxtime, 0.8F);
											PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(rangeTarget.getX(), rangeTarget.getY(), rangeTarget.getZ(), 50, rangeTarget.level().dimension())), messageBulletTrail);
										}
										WMExplosionBase.createExplosionDamage(living, laserTarget.getX()+1.0D, laserTarget.getY()+1.5D, laserTarget.getZ()+1.0D,20, 2, false,  false);
									}
									++count;
									if(count>maxcount){
										stop = true;
										break;
									}
								}
								stop = true;
								break;
							}
						}
					}
				}
				if(stop){
					++pierce;
					if(pierce>2)break;
				}
			}
		}
		if(exlevel>0)WMExplosionBase.createExplosionDamage(living, ix, iy+1.5D, iz,damage, exlevel, false,  false);
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX(fxhit, null, ix, iy+1.5D, iz, 0, 0, 0, exlevel);
		MessageTrail messageBulletTrail = new MessageTrail(true, id, modeltex, px+xx11, py+h-1.5F+base, pz+zz11, living.getDeltaMovement().x, living.getDeltaMovement().z, ix, iy+0.5D, iz, maxtime, 1);
		PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(px, py, pz, 80, living.level().dimension())), messageBulletTrail);
	}
	
	public void turretFollow(float targetYaw,float targetPitch){
		float speedy = 2+this.turretSpeed*2;
		float speedx = 1+this.turretSpeed;
		//this.turretYaw=(Mth.lerp(this.turretSpeed*0.3F, this.turretYaw, targetYaw));
		float f3 = (float) (targetYaw - this.turretYaw);// -180 ~ 0 ~ 180
		f3 = this.clampYaw(f3);
		if(f3>speedy){// +1
			if(f3>180F){
				this.turretYawMove-=speedy;
			}else{
				this.turretYawMove+=speedy;
			}
		}else if(f3<-speedy){// -1
			if(f3<-180F){
				this.turretYawMove+=speedy;
			}else{
				this.turretYawMove-=speedy;
			}
		}
		if(f3>-2*speedy&&f3<2*speedy){
			aimYawTrue = true;
			this.turretYawMove = targetYaw;
		}else{
			aimYawTrue = false;
		}
		this.turretYaw = this.turretYawMove;//yaw
		
		float f2 = (float) (targetPitch - this.turretPitch);// -180 ~ 0 ~ 180
		if(this.turretPitchMove<targetPitch){
			if(targetPitch<this.turretPitchMin)this.turretPitchMove+=speedx;
		}else if(this.turretPitchMove>targetPitch){
			if(targetPitch>this.turretPitchMax)this.turretPitchMove-=speedx;
		}
		if(f2<2*speedx&&f2>-2*speedx){
			aimPitchTrue = true;
			this.turretPitchMove = targetPitch;
		}else{
			aimPitchTrue = false;
		}
		this.turretPitch = this.turretPitchMove;//pitch
	}
	boolean hurtPassenger = true;
	int time = 0;
	public boolean aimYawTrue = false;
	public boolean aimPitchTrue = false;
	public boolean turret_speed = false;
	public boolean hudfollow = false;
	
	int randomAimType = 0;
	int randomAimTime = 0;
	boolean turn = false;
	float aim = 0;
	float aimY = 0;
	float aimX = 0;
	
    public boolean CanAttack(Entity target){
		return TargetSelect.seatCanAttack(this, target, this.getTargetType(),this.attack_range_max, this.is_aa);
    }
	
	int flarecd;
	public void tick() {
		super.tick();
		if(this.level().isClientSide())
        {
            this.onClientUpdate();
        }
		if(changetime<10)++changetime;
		if(this.turretYaw>360F||this.turretYaw<-360F){
			this.turretYaw = 0;
		}
		boolean fire_flag = false;
		
		if(time<20)++time;
		if(this.getVehicle()!=null/* && this.time <10*/){
			int type = 0;
			int gun_cycle = 4;
			int i = this.getVehicle().getPassengers().indexOf(this);
			float fire_x = 0.89F;
			if(this.getRemain1()==2){
				fire_x = 0;
			}else if(this.getRemain1()==1){
				fire_x = -0.89F;
			}
			if(this.getVehicle() instanceof WeaponVehicleBase){
				WeaponVehicleBase vehicle = (WeaponVehicleBase)this.getVehicle();
				if(vehicle.getHealth()>0){
					if(vehicle.seatTurret[i]){
						this.setYRot(vehicle.turretYaw);
					}else{
						this.setYRot(vehicle.getYRot());
					}
					if(i ==0){
						this.turretSpeed = vehicle.turretSpeed;
						this.turretYaw = vehicle.turretYaw;
						//this.setYHeadRot(this.turretYaw);
						this.turretPitch = vehicle.getXRot();
						this.canlock = vehicle.canlock;
						this.is_aa = vehicle.is_aa;
						this.minyaw = vehicle.minyaw;
						this.maxyaw = vehicle.maxyaw;
						this.turretPitchMax = vehicle.turretPitchMax;
						this.turretPitchMin = vehicle.turretPitchMin;
						this.seatProtect = vehicle.seatProtect-vehicle.enc_protect*0.1F;
						this.seatHide = vehicle.seatHide[0];
						this.canNightV=vehicle.canNightV;
					}
					//this.seatHide = vehicle.seatHide[i];
					this.seatCanFire = vehicle.seatCanFire[i];
					
					if(i ==0||hudfollow){
						this.render_hud_box = vehicle.render_hud_box;
						this.hud_box_obj = vehicle.hud_box_obj;
						this.hud_box_tex = vehicle.hud_box_tex;
						this.renderHudIcon = vehicle.renderHudIcon;
						this.hudIcon = vehicle.hudIcon;
						this.renderHudOverlay = vehicle.renderHudOverlay;
						this.hudOverlay = vehicle.hudOverlay;
						this.renderHudOverlayZoom = vehicle.renderHudOverlayZoom;
						this.hudOverlayZoom = vehicle.hudOverlayZoom;
					}
					
					if(this.getRemain1() <= 0){
						++reload1;
						if(reload1 == reload_time1 - reloadSoundStart1)this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), reloadSound1, SoundSource.WEATHER, 2.0F, 1.0F);
						if(reload1 >= reload_time1){
							this.setRemain1(this.magazine);
							reload1 = 0;
						}
					}
					if(this.getRemain2() <= 0){
						++reload2;
						if(reload2 == reload_time2 - reloadSoundStart2)this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), reloadSound2, SoundSource.WEATHER, 2.0F, 1.0F);
						if(reload2 >= reload_time2){
							this.setRemain2(this.magazine2);
							reload2 = 0;
						}
					}
					boolean weaponfire1 = false;
					boolean weaponfire2 = false;
					
					float yaw = vehicle.deltaRotation * vehicle.sensitivityAdjust;
					this.yHeadRot += yaw;
					//this.setYRot(this.getYRot()+rt);
					this.turretYaw += yaw;
			
					if (this.getControllingPassenger() != null) {
						if(startRidTime<15)++startRidTime;
						Player player = (Player) this.getControllingPassenger();
						/*float fix = 1.0F;
						if(vehicle.flyPitch!=0)fix = 1.4F;*/
						if(this.canlock && this.cooltime>this.ammo1 && this.cooltime2>this.ammo2){
							AI_MissileLock.SeatLock(this,player,this.is_aa, 1);
						}

						if(player.yHeadRot > 360F || player.yHeadRot < -360F){
							player.yHeadRot = 0;
							player.setYRot(0);
							player.yRotO = 0;
							player.yHeadRotO = 0;
						}
						if(i > 0){
							player.yHeadRot += yaw;
							player.yBodyRot += yaw;
							player.setYRot(player.getYRot()+yaw);
						}
						if(vehicle.VehicleType>2){
							if(vehicle.movePower>2){
								player.setXRot(player.getXRot() + vehicle.getMovePitch() * (vehicle.turnSpeed) * ((180F-Math.abs(vehicle.flyRoll)*2)/180));
							}
						}
						
						if(vehicle.getChange()>0||turret_speed){
							turretFollow(player.yHeadRot, player.getXRot());
						}else{
							this.turretYaw = player.getYHeadRot();
							this.turretPitch = player.getXRot();
						}
						this.setYHeadRot(this.turretYaw);
						//if(i>0)
						{
							if(this.fire1){
								weaponfire1 = true;
								this.fire1 = false;
							}
							if(this.fire2){
								weaponfire2 = true;
								this.fire2 = false;
							}
							if(this.keyr){
								if(changetime>8){
									int seat = i;
									if(i == vehicle.seatMaxCount-1){
										seat = -1;
									}
									for(int i1 = 0; i1 < vehicle.seatMaxCount; ++i1) {
										if(i1>seat && vehicle.getAnySeat(i1)!=null && ((EntityWMSeat)vehicle.getAnySeat(i1)).canDrive()){
											vehicle.setSeat=true;
											this.playSound(SoundEvents.IRON_DOOR_OPEN, 3.0F, 1.0F);
											if (!player.level().isClientSide){
												player.startRiding(vehicle.getAnySeat(i1));
											}
											this.playSound(SoundEvents.IRON_DOOR_CLOSE, 3.0F, 1.0F);
											changetime = 0;
											break;
										}
									}
									changetime = 0;
								}
								this.keyr = false;
							}
						}
					}else{
						startRidTime = 0 ;
						if(this.getNpcPassenger()!=null){
							if(this.getNpcPassenger() instanceof Enemy){
								if(this.getTargetType()!=2)this.setTargetType(2);
							}else{
								if(this.getTargetType()!=3)this.setTargetType(3);
							}
							if(i>0 && this.weaponCount>0){
								this.targetSerch();
								if(this.getTarget()!=null)fire_flag=true;
								float targetpitch = this.getXRot();//gunner
								float targetyaw = this.getYHeadRot();//gunner
								if(this.getTarget()!=null){
									LivingEntity target = this.getTarget();
									if(target.isAlive() && target!=null){
										double xx11 = 0;
										double zz11 = 0;
										xx11 -= Math.sin(this.turretYaw * 0.01745329252F) * fireposZ[0];
										zz11 += Math.cos(this.turretYaw * 0.01745329252F) * fireposZ[0];
										float height = target.getBbHeight()*0.7F;
										if(height>3)height=3;
										double dx = target.getX()+target.getDeltaMovement().x*0.2F - this.getX()-xx11;
										double dz = target.getZ()+target.getDeltaMovement().z*0.2F - this.getZ()-zz11;
										double dyy = this.getY()+this.fireposY[0] - target.getY()-height-target.getDeltaMovement().y*0.2F;
										if(target.getVehicle()!=null && target.getVehicle() instanceof EntityWMSeat){
											EntityWMSeat seat = (EntityWMSeat)target.getVehicle();
											if(seat.getVehicle()!=null){
												dx = seat.getVehicle().getX()+seat.getDeltaMovement().x*0.2F - this.getX()-xx11;
												dz = seat.getVehicle().getZ()+seat.getDeltaMovement().z*0.2F - this.getZ()-zz11;
												dyy = this.getY()+this.fireposY[0] - seat.getVehicle().getY()-seat.getVehicle().getBbHeight()*0.5F-seat.getVehicle().getDeltaMovement().y*0.2F;
											}
										}
										double dis = Math.sqrt(dx * dx + dz * dz);
										targetpitch = (float) (Math.atan2(dyy, dis) * 180.0D / Math.PI);
										targetyaw = -((float) Math.atan2(dx, dz)) * 180.0F / (float) Math.PI;
										targetyaw = this.clampYaw(targetyaw);
										if(this.isthrow){
											boolean canChangeAim = true;
											if(this.onlythrow || vehicle.getMoveType()==3&&!this.changeThrow){
												canChangeAim=false;
											}
											double[] angles = new double[2];
											boolean flag = ThrowBullet.canReachTarget(this.bulletspeed[0], this.bulletgravity[0], 0.99,
													(int) (this.getX()+xx11), (int) (this.getY()+this.fireposY[0]), (int) (this.getZ()+zz11),
													(int) target.getX(), (int) target.getEyeY(), (int) target.getZ(),
													angles, canChangeAim);/*false 为强制高抛*/
											if (flag) {
												targetpitch = (float)-angles[1];
											}
										}
									}
									//randomAim();
								}else{
									this.aimY=this.aimX=this.aim=0;
								}
								this.setXRot(targetpitch+aimX);
								if(turret_speed){
									turretFollow(targetyaw+aimY,this.getXRot());
								}else{
									this.turretYaw=(Mth.lerp(this.turretSpeed*0.3F, this.turretYaw, targetyaw+aimY));
									this.turretPitch=(Mth.lerp(this.turretSpeed*0.3F, this.turretPitch, this.getXRot()));
								}
								
								if(this.getNpcPassenger() instanceof LivingEntity living){
									living.setYHeadRot(this.turretYaw);
									living.setYRot(this.turretYaw);
									living.setXRot(this.turretPitch);
								}
								this.setYHeadRot(this.turretYaw);
								if(fire_flag && (aimYawTrue && aimPitchTrue||!turret_speed)){
									{
										weaponfire1 = true;
										weaponfire2 = true;
										this.fire1 = true;
										this.fire2 = true;
									}
								}else{
									this.fire1 = false;
									this.fire2 = false;
								}
							}
						}else{
							if(this.getTargetType()!=0)this.setTargetType(0);
						}
					}
					
					if(weaponfire1){
						if(this.cooltime >= this.ammo1){
							this.counter1 = true;
							this.cooltime = 0;
						}
						if(this.counter1 && this.gun_count1 >= this.w1cycle && this.getRemain1() > 0){
							vehicle.setAnimFire(2+i);
							this.weaponActive1();
							this.setRemain1(this.getRemain1() - 1);
							this.gun_count1 = 0;
							this.counter1 = false;
							if(this.getControllingPassenger()!=null)this.onFireAnimation(0.5F,1F);
						}
					}
					if(weaponfire2){
						if(cooltime2 >= this.ammo2){
							this.counter2 = true;
							this.cooltime2 = 0;
						}
						if(this.counter2 && this.gun_count2 >= this.w2cycle && this.getRemain2()>0){
							this.weaponActive2();
							this.setRemain2(this.getRemain2() - 1);
							this.gun_count2 = 0;
							this.counter2 = false;
						}
					}
				}else{
					if(time>2){
						if (!this.level().isClientSide){
							this.dead = true;
							this.discard();
						}
						time = 0;
					}
				}
			}
		}else if(time>10){
			if (!this.level().isClientSide){
				this.dead = true;
				this.discard();
			}
			time = 0;
		}
	}
	
	void randomAim(){
		if(randomAimTime<50){
			++randomAimTime;
		}else{
			randomAimTime=0;
			randomAimType= this.level().random.nextInt(2);
		}
		if(this.randomAimType>0){
			if(!turn && this.aim<8){
				this.aim+=0.2F;
			}
			if(this.aim>=8)turn = true;
			if(turn && this.aim>-8){
				this.aim-=0.2F;
			}
			if(this.aim<=-8)turn = false;
		}else{
			this.aimY=this.aimX=this.aim=0;
		}
		this.aimX=aim*0.5F;
		this.aimY=aim*0.5F;
	}
	
	@Override
    protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
        if (this.hasPassenger(passenger))
        {
        	 int i = this.getPassengers().indexOf(passenger);
             {
				double ix = 0;
				double iy = 0;
				double iz = 0;
				float rote = 0;
				if (this.seatTurret) {
					rote = this.turretYaw;
				}else{
					rote = this.getYRot();
				}
				float rpitch = this.turretPitch;
				double mgheight = 0;
				if(this.ridding_rotemgPitch)mgheight = /*this.firebaseZ[0] */ Mth.sin(passenger.getXRot()  * (1 * (float) Math.PI / 180)) *  0.38D;
				if(this.onGround()||!seatRotePitch)rpitch = 0;
				float f1 = rote * (2 * (float) Math.PI / 360);
				ix -= Mth.sin(f1) * seatPosZ[i];
				iz += Mth.cos(f1) * seatPosZ[i];
				ix -= Mth.sin(f1 - 1.57F) * seatPosX[i];
				iz += Mth.cos(f1 - 1.57F) * seatPosX[i];
				double ix2 = 0;
				double iz2 = 0;
				float f12 = this.turretYaw * (2 * (float) Math.PI / 360);
				ix2 -= Mth.sin(f12) * seatRoteZ[i];
				iz2 += Mth.cos(f12) * seatRoteZ[i];
				ix2 -= Mth.sin(f12 - 1.57F) * seatRoteX[i];
				iz2 += Mth.cos(f12 - 1.57F) * seatRoteX[i];
				double b = 0;
				double b2 = 0;
				double a = 0;
				double ax = 0;
				double az = 0;
				{
					b =  seatPosZ[i] * Mth.sin(rpitch  * (1 * (float) Math.PI / 180)) *  1.0D;
					a =  seatPosZ[i] * Math.abs(Math.cos(rpitch  * (1 * (float) Math.PI / 180))) *  1.0D;
					ax -= Mth.sin(rote * (2 * (float) Math.PI / 360)) * a;
					az += Mth.cos(rote * (2 * (float) Math.PI / 360)) * a;
					ax -= Mth.sin(rote * (2 * (float) Math.PI / 360) - 1.57F) * seatPosX[i];
					az += Mth.cos(rote * (2 * (float) Math.PI / 360) - 1.57F) * seatPosX[i];
				}
				Vec3 vec3d = new Vec3(ax + ix2, seatPosY[i] + seatRoteY[i] + passenger.yRotO - b, az + iz2);
				float f11 = (float)(seatPosY[i] + passenger.getMyRidingOffset());
				//passenger.setPos(this.getX() + vec3d.x,this.getY() + f11 + mgheight, this.getZ() + vec3d.z);
				callback.accept(passenger, this.getX() + vec3d.x,this.getY() + f11 + mgheight, this.getZ() + vec3d.z);
				this.applyYawToTurret(passenger);
             }
        }
    }
}
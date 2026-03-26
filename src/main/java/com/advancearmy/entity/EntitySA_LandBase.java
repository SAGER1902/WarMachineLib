package advancearmy.entity;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level; // 原 Level -> Level
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent; // SoundEvent 包路径变更
import net.minecraft.core.BlockPos; // BlockPos 移动到 core 包
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation; // ResourceLocation 包路径变更
import net.minecraft.world.entity.player.Player; // Player -> Player
import net.minecraft.world.entity.Mob; // Mob -> Mob
import net.minecraft.world.entity.Entity; // 基础 Entity 类
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
 // FML 网络包路径变更
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal; // 目标类包路径变更
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
//import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import advancearmy.init.ModEntities;
//import advancearmy.event.SASoundEvent;
import wmlib.common.living.PL_LandMove;
import wmlib.common.living.AI_TankSet;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.common.living.ai.VehicleSearchTargetGoalSA;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageVehicleAnim;
import wmlib.client.obj.SAObjModel;
import wmlib.api.IArmy;
import wmlib.api.ITool;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.tags.BlockTags;
import advancearmy.event.SASoundEvent;
import wmlib.util.ThrowBullet;
import advancearmy.AAConfig;
import advancearmy.util.TargetSelect;

import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import wmlib.api.IEnemy;
import advancearmy.entity.EntitySA_SoldierBase;
import net.minecraft.world.entity.PathfinderMob;

public abstract class EntitySA_LandBase extends WeaponVehicleBase implements IArmy{
	public EntitySA_LandBase(EntityType<? extends EntitySA_LandBase> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.lockAlertSound=SASoundEvent.laser_lock.get();
		this.destroySoundStart=SASoundEvent.tank_explode.get();
		this.destroySoundEnd=SASoundEvent.wreck_explosion.get();
		this.antibullet_0 = 0.1F;
		this.antibullet_1 = 1F;
		this.antibullet_2 = 1.5F;
		this.antibullet_3 = 0.5F;
		this.antibullet_4 = 0.5F;
		//this.turretYaw = this.getYRot();
		//if(this.attack_range_max==0)this.attack_range_max = (float)this.getAttributeValue(Attributes.FOLLOW_RANGE);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_LandBase.createMobAttributes();
    }
	public void stopUnitPassenger(){
		this.stopPassenger();
	}
	public boolean noturret = false;
	public ResourceLocation getIcon1(){
		return this.icon1tex;
	}
	public ResourceLocation getIcon2(){
		return this.icon2tex;
	}
	public LivingEntity getLockTarget(){
		return this.firstTarget;
	}
	public void setAttack(LivingEntity target){
		this.setTarget(target);
		if(target==null){
			this.firstTarget=null;
			this.clientTarget=null;
		}else{
			this.firstTarget=target;
		}
	}
	public void setSelect(boolean stack){
		this.setChoose(stack);
	}
	public void setMove(int id, int x, int y, int z){
		this.setMoveType(id);
		this.setMovePosX(x);
		this.setMovePosY(y);
		this.setMovePosZ(z);
	}
	public boolean getSelect(){
		return this.getChoose() && this.getTargetType()==3;
	}
	public boolean isDrive(){
		return this.getVehicle()!=null || this.getTargetType()<2;
	}
	public boolean isCommander(LivingEntity owner){
		return this.getOwner() == owner;
	}
	public LivingEntity getArmyOwner(){
		return this.getOwner();
	}
	public int getArmyMoveT(){
		return this.getMoveType();
	}

	public int getTeamCount(){
		return getTeamC();
	}
	public void setTeamCount(int id){
		setTeamC(id);
	}
	public int getArmyMoveX(){
		return this.getMovePosX();
	}
	public int getArmyMoveY(){
		return this.getMovePosY();
	}
	public int getArmyMoveZ(){
		return this.getMovePosZ();
	}
	
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new VehicleLockGoal(this, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new VehicleSearchTargetGoalSA<>(this, Mob.class, 10, false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new VehicleSearchTargetGoalSA<>(this, Player.class, 10, false, (attackentity) -> {return this.CanAttack(attackentity);}));
	}

    public boolean CanAttack(Entity target){
		return TargetSelect.landVehicleCanAttack(this, target, this.getTargetType(),
		this.attack_range_min, this.attack_range_max, this.attack_height_min, this.attack_height_max, this.is_aa);
    }
	
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.IRON_GOLEM_HURT;
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.IRON_GOLEM_HURT;
    }
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.IRON_GOLEM_DEATH;
    }	

	public boolean custom_weapon1 = false;
	public boolean custom_weapon2 = false;
	public boolean custom_fire1 = false;
	public boolean custom_fire2 = false;

	public float w1recoilp = 1;
	public float w1recoilr = 1;

	public float w1barrelsize = 1F;

	public float fireposX1 = 0;
	public float fireposY1 = 0;
	public float fireposZ1 = 0;
	public float fireposX2 = 0;
	public float fireposY2 = 0;
	public float fireposZ2 = 0;
	public float firebaseX = 0;
	public float firebaseZ = 0;
	
	public float posXmove=0;
	public float posZmove=0;
	
	public int wheelcount = 0;
	public float wheelangle = 40F;
	
	public boolean[] wheelturn = new boolean[12];
	
	public float[] wheelx = new float[12];
	public float[] wheely = new float[12];
	public float[] wheelz = new float[12];
	public float wheel_rotex = 1;
	public float wheel_rotey = 0;
	public float wheel_rotez = 0;
	
	public int radercount = 0;
	public float[] raderx = new float[6];
	public float[] radery = new float[6];
	public float[] raderz = new float[6];
	
	public float mgx = 0;
	public float mgy = 0;
	public float mgz = 0;
	public float mgbz = 0;
	public int w1pitchlimit = -90;
	public float turretYawO1;
	public float turretPitchO1;
	public SoundEvent firesound1;
	public SoundEvent firesound2;
	public SoundEvent startsound;
	public SoundEvent movesound;
	public boolean ammo = false;
	public static int count = 0;
	public int healtime = 0;
	
	public SAObjModel mgobj = null;
	public ResourceLocation enemytex = null;
	public ResourceLocation mgtex = null;
	public ResourceLocation tracktex = null;
	
	public void setRader(int id, float x,float y,float z){
		this.raderx[id] = x;
		this.radery[id] = y;
		this.raderz[id] = z;
	}
	public void setWheel(int id, float x,float y,float z){
		this.wheelx[id] = x;
		this.wheely[id] = y;
		this.wheelz[id] = z;
	}
	public void setMg(float x,float y,float z, float bz){
		this.mgx = x;
		this.mgy = y;
		this.mgz = z;
		this.mgbz= bz;
	}
	
    public void setAnimFire(int id)
    {
        if(this != null && !this.level().isClientSide)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageVehicleAnim(this.getId(), id));
        }
    }
	
	
	private boolean wasInWaterLastTick = false;
	private float floatPhase = 0.0f; // 用于上下浮动的相位
	public float waterTargetDepth; // 自定义：实体浸入水中的深度比例 (0-1)
	public float buoyancyStrength = 0.08f; // 自定义：浮力强度
	public float floatAmplitude = 0.005f; // 自定义：上下浮动幅度
	public float floatSpeed = 0.05f; // 自定义：浮动速度
	public void updateAmphibiousMovement() {
		this.waterTargetDepth = 0.2f;
		this.checkAndPrepareToExitWater();
		
		Level level = this.level();
		Vec3 currentMotion = this.getDeltaMovement();
		double motionX = currentMotion.x;
		double motionZ = currentMotion.z;
		double motionY;
		// 1. 计算目标Y位置（考虑自定义浸没深度）
		// 获取实体脚部位置的水面高度
		BlockPos feetPos = BlockPos.containing(this.getX(), this.getY(), this.getZ());
		double waterSurfaceY = this.getWaterSurfaceY(feetPos); // 需要实现此方法
		if (waterSurfaceY != -1) {
			// 计算实体底部的目标Y坐标
			double targetBottomY = waterSurfaceY - (this.getBbHeight() * this.waterTargetDepth);
			double currentBottomY = this.getBoundingBox().minY;
			double depthDifference = targetBottomY - currentBottomY;
			// 2. 应用浮力（抵消重力，并向目标深度调整）
			// 浮力方向与深度差方向一致，但受浮力强度限制
			motionY = currentMotion.y + (depthDifference > 0 ? this.buoyancyStrength : -this.buoyancyStrength) * 0.1;
			// 3. 叠加上下浮动效果（基于游戏时间的正弦波）
			this.floatPhase += this.floatSpeed;
			// 确保相位在合理范围内，避免过大
			if (this.floatPhase > 2 * Math.PI) {
				this.floatPhase -= 2 * Math.PI;
			}
			double floatOffset = Math.sin(this.floatPhase) * this.floatAmplitude;
			motionY += floatOffset;
			// 4. 限制水中垂直速度，避免过快
			double maxWaterVerticalSpeed = 0.15;
			if (Math.abs(motionY) > maxWaterVerticalSpeed) {
				motionY = motionY > 0 ? maxWaterVerticalSpeed : -maxWaterVerticalSpeed;
			}
		} else {
			// 无法获取水面高度时的回退逻辑：简单浮力
			motionY = currentMotion.y + this.buoyancyStrength;
		}
		// 应用计算出的新速度
		this.setDeltaMovement(motionX, motionY, motionZ);
	}
	private double getWaterSurfaceY(BlockPos pos) {
		Level level = this.level();
		// 从实体脚部向上搜索，找到最顶部的水方块
		int searchHeight = 10; // 搜索高度范围，可根据需要调整
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
		boolean foundWater = false;
		for (int i = 0; i < searchHeight; i++) {
			mutablePos.setY(pos.getY() + i);
			BlockState state = level.getBlockState(mutablePos);
			
			if (state.liquid()) { // 是液体方块（水）
				foundWater = true;
			} else if (foundWater) {
				// 之前找到了水，现在遇到了非水方块，说明这就是水面顶部
				return mutablePos.getY() - 1; // 返回水方块顶面
			}
		}
		return -1; // 未找到有效水面
	}
	private boolean hasGroundAhead() {
		Level level = this.level();
		float yawRad = this.getYRot() * ((float) Math.PI / 180F);
		double lookX = -Math.sin(yawRad);
		double lookZ = Math.cos(yawRad);
		double checkDistance = 0.1+this.getBbWidth(); // 检查距离
		double aheadX = this.getX() + lookX * checkDistance;
		double aheadZ = this.getZ() + lookZ * checkDistance;
		BlockPos checkPos = BlockPos.containing(aheadX, this.getY(), aheadZ);
		for (int i = 0; i < 3; i++) {
			BlockPos groundPos = checkPos.below(i);
			if (level.getBlockState(groundPos).isSolidRender(level, groundPos)) {
				return true; // 找到可站立地面
			}
		}
		return false;
	}
	private void checkAndPrepareToExitWater() {
		BlockPos belowPos = BlockPos.containing(this.getX(), this.getBoundingBox().minY - 0.1, this.getZ());
		BlockState belowState = this.level().getBlockState(belowPos);
		/*if (belowState.isSolidRender(this.level(), belowPos))*/{
			if (hasGroundAhead()) {
				//Vec3 currentMotion = this.getDeltaMovement();
				//this.setDeltaMovement(currentMotion.x, 0.3, currentMotion.z); // 较强的向上推力
				this.waterTargetDepth=0F;
			}
		}
	}
	public float rote_wheelx = 0;
	public float rote_wheelz = 0;
	public float rote_wheel = 0;
	
	public float currentTilt = 0F;
	public boolean canWater =false;
	public int cracktime = 0;
	public float soundspeed=1F;
	
	//public int rote_tick=0;
	
	int move_time = 0;
	float bodyTargetYaw = 0;
	int control_tick = 0;
	Player drivePlayer = null;
	
	public boolean changeThrow = false;
	
	int r_time;
	public boolean canFloat = false;
	public void tick() {
		super.tick();
		//++rote_tick;
		if(this.getHealth()>0){
			/*if(checkseatspawn)*/{
				if(this.canAddPassenger(null)){
					if (!this.level().isClientSide){
						EntitySA_Seat seat = new EntitySA_Seat(ModEntities.ENTITY_SEAT.get(),this.level());
						seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
						this.level().addFreshEntity(seat);
						seat.startRiding(this);
					}
					//checkseatspawn = false;
				}
			}
			
			double movex = this.getX() - this.xo;
			double movez = this.getZ() - this.zo;
			boolean isMoving = Math.sqrt(movex * movex + movez * movez) > 0.01;
			float moveAngle = (float)Math.toDegrees(Math.atan2(movez, movex));
			float entityAngle = this.yHeadRot;
			float angleDiff = Mth.wrapDegrees(moveAngle - entityAngle);
			boolean isMovingForward = Math.abs(angleDiff) < 90;
			if(isMoving){
				if(move_time<30)++move_time;
			}
			float horizontalVelocity = (float)Math.sqrt(movex * movex + movez * movez)*40F*(30-move_time)/30F;
			if(!isMovingForward)horizontalVelocity=-horizontalVelocity;
			if(this.throttle>0.1F||this.throttle<-0.1F){
				if(currentTilt<horizontalVelocity){
					currentTilt+=0.2F;
				}else{
					currentTilt-=0.2F;
				}
			}else{
				if(!isMoving)move_time=0;
			}
			
			if(cracktime<20)++cracktime;
			if(r_time<400){
				++r_time;
			}else{
				if(this.getOwner()==null && this.getArmyMoveT()!=3 && this.getTarget()==null && !this.isAttacking()){
					r_time=0;
					Vec3 vpos;
					if (this.random.nextInt(3)==1 && (vpos = DefaultRandomPos.getPosTowards(this, 15, 4, Vec3.atBottomCenterOf(this.blockPosition()), 1.5)) != null) {
						this.setMove(this.getArmyMoveT(), (int)vpos.x, (int)vpos.y, (int)vpos.z);
					}
					if(this.random.nextInt(2)==1){
						int i1 = this.getArmyMoveX() + this.random.nextInt(20)-10;
						int k1 = this.getArmyMoveZ() + this.random.nextInt(20)-10;
						List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(40, 20, 40));
						for(int k2 = 0; k2 < list.size(); ++k2) {
							Entity ent = list.get(k2);
							if(this.random.nextInt(2)==1){
								if(this.getTargetType()==3){
									if(ent instanceof EntitySA_SoldierBase){
										EntitySA_SoldierBase unit = (EntitySA_SoldierBase)ent;
										if(unit.getHealth()>0&&(unit.getMoveType()!=2 && unit.getOwner()==null)){
											unit.setMovePosX(i1);
											unit.setMovePosZ(k1);
											unit.setMoveType(4);
										}
									}
								}else{
									if(ent instanceof IEnemy){
										if(ent instanceof PathfinderMob find)find.getNavigation().moveTo(i1, this.getArmyMoveY(), k1, 2);
									}
								}
							}
						}
					}
					
				}
			}
			
			while(this.turretYaw1 - this.turretYawO1 < -180.0F) {
				this.turretYawO1 -= 360.0F;
			}
			while(this.turretPitch1 - this.turretPitchO1 >= 180.0F) {
				this.turretPitchO1 += 360.0F;
			}
			this.turretYawO1 = this.turretYaw1;
			this.turretPitchO1 = this.turretPitch1;
			
			if(this.canWater){
				canFloat = false;
				if(this.isInWater()){
					updateAmphibiousMovement();
					canFloat=true;
				}
			}
			
			if(this.startTime==1 && this.startsound!=null)this.playSound(this.startsound, 5.0F, 1.0F);
			if(this.getOwner()==null && this.getMovePosX()==0&&this.getMovePosY()==0 && this.getTargetType()>1 && this.getMoveType()!=4){
				this.setMoveType(4);
				this.setMovePosX((int)this.getX());
				this.setMovePosY((int)this.getY());
				this.setMovePosZ((int)this.getZ());
			}

			float f1 = this.yHeadRot * (2 * (float) Math.PI / 360);//
			AI_TankSet.set(this, this.movesound,this.soundspeed, f1, this.MoveSpeed, 0.1F, canFloat);//
			
			this.setentityrote(this);
			boolean fire1 = false;
			boolean fire2 = false;
			custom_fire1=false;
			custom_fire2=false;
			float speedy = 2+this.turretSpeed*2;
			float speedx = 1+this.turretSpeed;
			if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
				if(this.getTargetType()>0)this.setTargetType(0);//
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				drivePlayer = (Player)seat.getAnyPassenger();
				if(drivePlayer!=null){
					if(this.VehicleType == 2){
						PL_LandMove.moveCarMode(drivePlayer, this, this.MoveSpeed, turnSpeed);
					}
					if(seat.startRidTime>10){
						if(this.getChange()>0){
							float follow = drivePlayer.yHeadRot;
							follow = this.clampYaw(follow);
							this.setMoveYaw(follow);
							this.setXRot(drivePlayer.getXRot());
							float f2 = (float) (this.getXRot() - this.turretPitch);// -180 ~ 0 ~ 180
							if(this.turretPitchMove<this.getXRot()){
								if(this.getXRot()<turretPitchMin)this.turretPitchMove+=speedx;
							}else if(this.turretPitchMove>this.getXRot()){
								if(this.getXRot()>turretPitchMax)this.turretPitchMove-=speedx;
							}
							if(f2<2&&f2>-2)this.turretPitchMove = this.getXRot();
							this.turretPitch = this.turretPitchMove;//pitch
						}else{
							//this.turretPitch = drivePlayer.xRotO;
							this.turretPitch = this.turretPitchMove = drivePlayer.getXRot();
							this.setXRot(this.turretPitch);
							if(drivePlayer.yHeadRot>=0){
								this.turretYaw = drivePlayer.yHeadRot*0.995F;
							}else{
								this.turretYaw = drivePlayer.yHeadRot*1.005F;
							}
						}
					}

					if(seat.keyg && this.isthrow){
						if(cooltime4 >10){
							if(this.getMoveMode()==0){
								this.playSound(SoundEvents.STONE_BUTTON_CLICK_ON, 2.0F,1);
								this.setMoveMode(1);
							}else{
								this.playSound(SoundEvents.STONE_BUTTON_CLICK_OFF, 2.0F,1);
								this.setMoveMode(0);
							}
							cooltime4=0;
						}
						seat.keyg = false;
					}
					if(seat.fire1 && !this.custom_weapon1){
						fire1 = true;
						seat.fire1 = false;
					}
					if(seat.fire2 && !this.custom_weapon2){
						fire2 = true;
						seat.fire2 = false;
					}
				}
			}else{
				drivePlayer = null;
				if(this.getTargetType()==0){
					this.setTargetType(1);
				}
				if(this.getTargetType()==1){
					if(this.getMoveType()!=3){
						//this.setMoveType(3);
						if(this.yHeadRot==0)this.yHeadRot=this.getYRot();
						this.setMoveYaw(this.yHeadRot);
						this.setForwardMove(0);
						this.setStrafingMove(0);
					}
				}
			}

			if(this.control_tick<20)++this.control_tick;
			if(this.getTargetType()>0){//非空
				if (this.getFirstSeat() != null) {
					EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
					if(seat.getTargetType()==0&&this.enc_soul==0){
						if(this.getTargetType()!=1)this.setTargetType(1);
					}
					if(seat.getTargetType()==2){
						if(this.getTargetType()!=2)this.setTargetType(2);
					}
					if(seat.getTargetType()==3||this.enc_soul>0){
						if(this.getTargetType()!=3)this.setTargetType(3);
					}
				}
				if(this.getTargetType()>1){
					this.setYRot(this.yHeadRot);
					//this.bodyTargetYaw = this.getYRot();
					if(this.getMoveType()==0 && this.getOwner()!=null && followTime>30){
						if(this.distanceTo(this.getOwner())>12){
							this.setMovePosX((int)this.getOwner().getX());
							this.setMovePosY((int)this.getOwner().getY());
							this.setMovePosZ((int)this.getOwner().getZ());
							followTime=0;
						}
					}
					if(this.control_tick>2){
						/*if(this.getChoose()&&this.getMoveType()==4 && this.isAttacking()){
							this.setForwardMove(0);
						}*/
						if((this.getMovePosX()!=0||this.getMovePosZ()!=0)&&
						(this.getMoveType()==0||this.getMoveType()==2||!this.isAttacking()&&this.getMoveType()==4)){
							double dx = this.getMovePosX() - this.getX();
							double dz = this.getMovePosZ() - this.getZ();
							double dis = Math.sqrt((float)(dx * dx + dz * dz));
							int min = 5;
							if(this.getMoveType()==0)min=10;
							if(dis>min){
								this.bodyTargetYaw = (float) Math.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
								this.bodyTargetYaw = this.clampYaw(this.bodyTargetYaw);
								if(!this.isAttacking()){
									this.setMoveYaw(this.bodyTargetYaw);//barrel
								}
								float f3 = this.yHeadRot - this.bodyTargetYaw;
								f3 = this.clampYaw(f3);
								if (f3 > this.turnSpeed*1.5F) {
									this.setStrafingMove(1);
								} else if (f3 < -this.turnSpeed*1.5F) {
									this.setStrafingMove(-1);
								}else{
									this.setStrafingMove(0);
								}
								if(f3>-this.turnSpeed*2F&&f3<this.turnSpeed*2F){
									this.setForwardMove(2);
								}
							}else{
								if(!this.isAttacking())this.setStrafingMove(0);
								this.setForwardMove(0);
								if(this.getMoveType()==2){
									this.setMoveType(3);
									this.setMovePosX(0);
									//this.setMovePosY(0);
									this.setMovePosZ(0);
								}
							}
						}
						if (this.getTarget() != null) {
							LivingEntity target = this.getTarget();
							if(target.isAlive() && target!=null){
								this.setAttacking(true);
								double xx11 = 0;
								double zz11 = 0;
								xx11 -= Math.sin(this.turretYaw * 0.01745329252F) * fireposZ1;
								zz11 += Math.cos(this.turretYaw * 0.01745329252F) * fireposZ1;
								
								float height = target.getBbHeight()*0.7F;
								if(height>3)height=3;
								double dx = target.getX()+target.getDeltaMovement().x*0.2F - this.getX()-xx11-this.posXmove;
								double dz = target.getZ()+target.getDeltaMovement().z*0.2F - this.getZ()-zz11-this.posZmove;
								//double ddy = Math.abs(target.getY() - this.getY());
								double dyy = this.getY()+this.fireposY1 - target.getY()-height-target.getDeltaMovement().y*0.2F;
								if(target.getVehicle()!=null && target.getVehicle() instanceof EntityWMSeat){
									EntityWMSeat seat = (EntityWMSeat)target.getVehicle();
									if(seat.getVehicle()!=null){
										dx = seat.getVehicle().getX()+seat.getDeltaMovement().x*0.2F - this.getX()-xx11-this.posXmove;
										dz = seat.getVehicle().getZ()+seat.getDeltaMovement().z*0.2F - this.getZ()-zz11-this.posZmove;
										//ddy = Math.abs(seat.getVehicle().getZ() - this.getY());
										dyy = this.getY()+this.fireposY1 - seat.getVehicle().getY()-seat.getVehicle().getBbHeight()*0.5F-seat.getVehicle().getDeltaMovement().y*0.2F;
									}
								}
								double dis = Math.sqrt(dx * dx + dz * dz);
								this.targetYaw = (float) Math.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
								this.targetYaw = this.clampYaw(this.targetYaw);
								this.setMoveYaw(this.targetYaw);
								float f11 = (float) (Math.atan2(dyy, dis) * 180.0D / Math.PI);
								int angle = 0;
								boolean crash = false;
								if(this.getMoveType() == 1||this.getOwner()==null && this.getMoveType()!=2){
									if(dis < 5){//
										crash = true;
									}else if(this.getOwner()==null){
										if (this.getAIType()>3){
											if(this.getAIType()==4){//
												angle = 25;
											}else if(this.getAIType()==5){
												angle = -25;
											}
											float moveRange = this.attack_range_max;
											if(moveRange==0)moveRange = (float)this.getAttributeValue(Attributes.FOLLOW_RANGE);
											if (dis>this.getAIType()*moveRange/10F){
												if(target.getMaxHealth()<100 && !this.isthrow)this.setForwardMove(2);
											}
										}
									}
								}
								if(crash){//
									if (this.getAIType()<3||target.getMaxHealth()>100){
										this.setForwardMove(-2);
									}else{
										this.setForwardMove(2);
									}
								}
								float targetpitch = f11;
								if(this.isthrow){
									boolean canChangeAim = true;
									if(this.onlythrow || this.getMoveType()==3&&!this.changeThrow){
										canChangeAim=false;
									}
									double[] angles = new double[2];
									boolean flag = ThrowBullet.canReachTarget(this.throwspeed, this.throwgrav, 0.99,
											(int) (this.getX()+xx11), (int) (this.getY()+this.fireposY1), (int) (this.getZ()+zz11),
											(int) target.getX(), (int) target.getEyeY(), (int) target.getZ(),
											angles, canChangeAim);/*false 为强制高抛*/
									if (flag) {
										targetpitch = (float)-angles[1];
									}
								}else{
									targetpitch = f11;
								}
								float f2 = (float) (targetpitch - this.turretPitch);// -180 ~ 0 ~ 180
								if (f2 > speedx) {
									this.setMovePitch(1);
								} else if (f2 < -speedx) {
									this.setMovePitch(-1);
								}else{
									this.setMovePitch(0);
								}
								float f4 = this.turretYaw - this.getMoveYaw();
								f4 = this.clampYaw(f4);
								if(f4 < speedy*1.1F && f4 > -speedy*1.1F){
									if(f2 < speedx && f2 > -speedx){
										if(!custom_weapon1 && this.turretPitch>this.w1pitchlimit)fire1 = true;
										if(!custom_weapon2){
											if(this.level().getRandom().nextInt(6) > 4){
												fire2 = true;
											}
										}
										custom_fire1=true;
										custom_fire2=true;
									}
								}
								if(this.getMoveType()!=2/*||(this.getMoveType()==4&&this.isAttacking())*/){
									float f3 = this.yHeadRot - this.getMoveYaw()+angle;
									f3 = this.clampYaw(f3);
									if (f3 > this.turnSpeed*1.5F) {
										this.setStrafingMove(1);
									} else if (f3 < -this.turnSpeed*1.5F) {
										this.setStrafingMove(-1);
									}else{
										this.setStrafingMove(0);
									}
								}
								this.aiChange();
							}
						}else{
							if(!this.isAttacking()){
								if(this.getMoveType()==1||this.getMoveType()==3){
									this.setMoveYaw(this.yHeadRot);//turret
									this.setForwardMove(0);
									this.setStrafingMove(0);
									this.setXRot(0);
								}
							}
						}
						if(canFloat){
							if (this.getForwardMove()>0.1F)this.setForwardMove(this.getForwardMove()-0.1F);
							if (this.getForwardMove()<-0.1F)this.setForwardMove(this.getForwardMove()+0.1F);
						}else{
							if (this.getForwardMove()>0.1F)this.setForwardMove(this.getForwardMove()-0.05F);
							if (this.getForwardMove()<-0.1F)this.setForwardMove(this.getForwardMove()+0.05F);
						}
					}
				}
			}
			if(this.noturret){
				this.turretYaw=this.getYRot();
			}else{
				if(this.getMovePitch() > 0){
					if(this.turretPitch<this.turretPitchMin)this.turretPitchMove+=speedx;
				}else if(this.getMovePitch() < 0){
					if(this.turretPitch>this.turretPitchMax)this.turretPitchMove-=speedx;
				}
				this.turretPitch = this.turretPitchMove;
				this.setXRot(this.turretPitch);
				if(this.getChange()>0||this.getTargetType()>1){
					float f4 = this.turretYaw - this.getMoveYaw();
					f4 = this.clampYaw(f4);
					if (f4 > speedy) {
						this.turretYaw-=speedy;
					} else if (f4 < -speedy) {
						this.turretYaw+=speedy;
					}else{
						this.turretYaw = this.getMoveYaw();
					}
					//this.turretYaw=(Mth.lerp(speedy*0.1F, this.turretYaw, this.getMoveYaw()));
				}
			}
			if (this.getForwardMove()>0){
				if(rote_wheelx<40)++rote_wheelx;
				if(this.startTime>100)this.startTime=0;
				rotecrawler(this, 0, true);
				rotecrawler(this, 1, true);
				if(this.throttle < this.throttleMax){
					this.throttle = this.throttle + this.thFrontSpeed;
				}
			}
			if (this.getForwardMove()<0){
				if(rote_wheelx>-40)--rote_wheelx;
				rotecrawler(this, 0, false);
				rotecrawler(this, 1, false);
				if(this.throttle > this.throttleMin){
					this.throttle = this.throttle + this.thBackSpeed;
				}
			}
			if (this.getForwardMove()==0){
				if(rote_wheelx>0){
					--rote_wheelx;
				}else if(rote_wheelx<0){
					++rote_wheelx;
				}
			}
			if(this.throttle != 0){
				if(this.getForwardMove()==0 && this.throttle < 0.09 && this.throttle > -0.09) {
					this.throttle = 0;
				}
				if(this.throttle > 0){
					this.throttle = this.throttle - 0.1F;
					rotecrawler(this, 0, true);
					rotecrawler(this, 1, true);
				}
				if(this.throttle < 0){
					this.throttle = this.throttle + 0.1F;
					rotecrawler(this, 0, false);
					rotecrawler(this, 1, false);
				}
			}
			if (this.getStrafingMove() < 0){
				this.deltaRotation += this.turnSpeed;
				if(rote_wheel>-40)--rote_wheel;
				if(rote_wheelz<40)++rote_wheelz;
				if(this.throttle > 0){
					rotecrawler(this, 1, true);
					rotecrawler(this, 0, false);
				}else {
					rotecrawler(this, 0, true);
					rotecrawler(this, 1, false);
				}
			}
			if (this.getStrafingMove() > 0){
				this.deltaRotation -= this.turnSpeed;
				if(rote_wheel<40)++rote_wheel;
				if(rote_wheelz>-40)--rote_wheelz;
				if(this.throttle > 0){
					rotecrawler(this, 0, true);
					rotecrawler(this, 1, false);
				}else {
					rotecrawler(this, 1, true);
					rotecrawler(this, 0, false);
				}
			}
			if(this.getStrafingMove()==0F){
				if(rote_wheel>0){
					--rote_wheel;
				}else if(rote_wheel<0){
					++rote_wheel;
				}
				if(rote_wheelz>0){
					--rote_wheelz;
				}else if(rote_wheelz<0){
					++rote_wheelz;
				}
			}
			if(this.VehicleType != 2||this.getTargetType()!=0){
				if(this.deltaRotation > 20)this.deltaRotation = 20;
				if(this.deltaRotation < -20)this.deltaRotation = -20;
				sensitivityAdjust = 2f - (float)Math.exp(-2.0f *(this.throttle*0.1F+15)) / (4.5f *(this.throttle*0.1F+15));
				sensitivityAdjust *= 0.125F;
				float rt = this.deltaRotation * sensitivityAdjust;
				this.deltaRotation *= 0.9F;
				this.yHeadRot = Mth.lerp(0.5F, this.yHeadRot, this.yHeadRot+rt);
				this.yBodyRot = Mth.lerp(0.5F, this.yBodyRot, this.yBodyRot+rt);
				this.setYRot(Mth.lerp(0.5F, this.getYRot(), this.getYRot()+rt));
				this.turretYaw += rt;
				if(drivePlayer!=null){
					drivePlayer.yHeadRot = Mth.lerp(0.5F, drivePlayer.yHeadRot, drivePlayer.yHeadRot + rt);
					drivePlayer.yBodyRot = Mth.lerp(0.5F, drivePlayer.yBodyRot, drivePlayer.yBodyRot + rt);
					drivePlayer.setYRot(Mth.lerp(0.5F, drivePlayer.getYRot(), drivePlayer.getYRot() + rt));
				}
			}
			
			if(fire1){
				if(this.cooltime >= this.ammo1){
					this.counter1 = true;
					this.cooltime = 0;
				}
				if(this.counter1 && this.gun_count1 >= this.w1cycle && this.getRemain1() > 0){
					this.setAnimFire(1);
					this.weaponActive1();
					this.setRemain1(this.getRemain1() - 1);
					this.gun_count1 = 0;//
					this.counter1 = false;
					if(this.getTargetType()==0 && drivePlayer!=null)this.onFireAnimation(this.w1recoilp,this.w1recoilr);
				}
			}
			if(fire2){
				if(cooltime2 >= this.ammo2){
					this.counter2 = true;
					this.cooltime2 = 0;
				}
				if(this.counter2 && this.gun_count2 >= this.w2cycle && this.getRemain2()>0){
					this.setAnimFire(2);
					this.weaponActive2();
					this.setRemain2(this.getRemain2() - 1);
					this.gun_count2 = 0;//
					this.counter2 = false;
				}
			}
			if(this.getTargetType()!=1)crashEnemy();
		}
	}
	
	public void aiChange(){
		if(this.getOwner()==null){
			if(this.isAttacking() && this.find_time<40)++this.find_time;
			if(this.level().getRandom().nextInt(6) > 3 && this.find_time > 20){
				this.find_time = 0;
				this.setAIType(0);
			}else if(this.level().getRandom().nextInt(6) < 3 && this.find_time > 20){
				this.find_time = 0;
				this.setAIType(this.level().getRandom().nextInt(7));
			}
		}
		/*if(this.getChoose()&&this.getAIType()!=0){
			this.setAIType(0);
		}*/
	}
	
	public boolean canBreakLog = true;
	
	public void crashEnemy(){
		if(AAConfig.vehicleDestroy && cracktime>10 && this.throttle>4){
			cracktime=0;
			breakNearbyFragileBlocks();
		}
		if(this.tracktick % 5 == 0 && this.throttle>0){//
			List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(2D));
			for(int k2 = 0; k2 < list.size(); ++k2) {
				Entity attackentity = list.get(k2);
				if(attackentity instanceof LivingEntity && ((LivingEntity)attackentity).getHealth()>0){
					if(this.CanAttack(attackentity))attackentity.hurt(this.damageSources().thrown(this, this), this.getBbHeight()*this.getBbWidth()+this.throttle);
				}
			}
		}
	}
	
    private void breakNearbyFragileBlocks() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        //AABB expandedBoundingBox = this.getBoundingBox().inflate(1.0);
		AABB expandedBoundingBox = (new AABB(
		this.getX()-this.getBbWidth(), this.getY()+2, this.getZ()-this.getBbWidth(), 
		this.getX()+this.getBbWidth(), this.getY()+this.getBbHeight(), this.getZ()+this.getBbWidth())).inflate(1D);
        // 你也可以用 .inflate(horizontal, vertical, horizontal) 分别控制各方向扩展
        BlockPos.betweenClosedStream(expandedBoundingBox).forEach(pos -> {
            BlockState state = serverLevel.getBlockState(pos);
            if (state.isAir() || state.getDestroySpeed(serverLevel, pos) < 0) {
                return;
            }
            boolean isFragile = state.is(BlockTags.LEAVES) // 所有树叶
                    || state.is(BlockTags.FLOWERS) // 所有花
                    || state.is(BlockTags.CROPS) // 所有农作物
                    || state.is(BlockTags.REPLACEABLE) // 其他可替换方块（如草、雪）
					|| state.is(BlockTags.MUSHROOM_GROW_BLOCK)
					|| (state.is(BlockTags.PLANKS)|| state.is(BlockTags.LOGS)) && canBreakLog;
            if (isFragile) {
                // 参数解释：null 表示无实体原因（自然破坏），
                // 若改为 this 则破坏算由此实体造成（可能影响战利品掉落等）。
                // true 表示破坏后是否掉落物品，对于树叶通常为false，但这里我们设为true。
                boolean dropItems = true;
                serverLevel.destroyBlock(pos, dropItems, this);
            }
        });
    }
	
	public static void setentityrote(EntitySA_LandBase entity) {
		if(entity.yHeadRot > 360F || entity.yHeadRot < -360F){
			entity.yHeadRot = 0;
			entity.setYRot(0);
			entity.yRotO = 0;
			entity.yHeadRotO = 0;
		}
		if(entity.yHeadRot > 180F){
			entity.yHeadRot = -179F;
			entity.setYRot(-179F);
			entity.yRotO = -179F;
			entity.yHeadRotO = -179F;
		}
		if(entity.yHeadRot < -180F){
			entity.yHeadRot = 179F;
			entity.setYRot(179F);
			entity.yRotO = 179F;
			entity.yHeadRotO = 179F;
		}
		if(entity.getChange()>0){
			if(entity.turretYaw > 360F || entity.turretYaw < -360F){
				entity.turretYaw = 0;
				entity.turretYawO = 0;
				entity.turretYawMove = 0;
			}
			if(entity.turretYaw > 180F){
				entity.turretYaw = -179F;
				entity.turretYawO = -179F;
				entity.turretYawMove = -179F;
			}
			if(entity.turretYaw < -180F){
				entity.turretYaw = 179F;
				entity.turretYawO = 179F;
				entity.turretYawMove = 179F;
			}
		}
	}
	public static void rotecrawler(EntitySA_LandBase entity, int id, boolean on) {
		if (id == 0) {
			if (!on) {
				if (entity.throttleRight <= 1) {
					entity.throttleRight = entity.throttleRight + 0.01F;
				} else {
					entity.throttleRight = 0;
				}
				if (entity.trackr <= 8) {
					entity.trackr = entity.trackr + 1;
				} else {
					entity.trackr = 0;
				}
			} else {
				if (entity.throttleRight > 0) {
					entity.throttleRight = entity.throttleRight - 0.01F;
				} else {
					entity.throttleRight = 1;
				}
				if (entity.trackr > 0) {
					entity.trackr = entity.trackr - 1;
				} else {
					entity.trackr = 8;
				}
			}
		}
		if (id == 1) {
			if (!on) {
				if (entity.throttleLeft <= 1) {
					entity.throttleLeft = entity.throttleLeft + 0.01F;
				} else {
					entity.throttleLeft = 0;
				}
				if (entity.trackl <= 8) {
					entity.trackl = entity.trackl + 1;
				} else {
					entity.trackl = 0;
				}
			} else {
				if (entity.throttleLeft > 0) {
					entity.throttleLeft = entity.throttleLeft - 0.01F;
				} else {
					entity.throttleLeft = 1;
				}
				if (entity.trackl > 0) {
					entity.trackl = entity.trackl - 1;
				} else {
					entity.trackl = 8;
				}
			}
		}
	}
}
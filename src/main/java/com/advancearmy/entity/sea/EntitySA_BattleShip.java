package advancearmy.entity.sea;

import java.util.List;

import javax.annotation.Nullable;

import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
import advancearmy.event.SASoundEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PlayMessages;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraft.tags.FluidTags;

import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;

import wmlib.common.living.PL_LandMove;
import wmlib.common.living.AI_TankSet;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import advancearmy.entity.EntitySA_Seat;
import safx.SagerFX;
import net.minecraftforge.fml.ModList;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageVehicleAnim;
import net.minecraftforge.network.PacketDistributor;
import wmlib.util.ThrowBullet;
import advancearmy.init.ModEntities;
import wmlib.api.IArmy;

import wmlib.util.TurretAngle;
import advancearmy.util.TargetSelect;
public class EntitySA_BattleShip extends WeaponVehicleBase implements IArmy{
	public EntitySA_BattleShip(EntityType<? extends EntitySA_BattleShip> sodier, Level worldIn) {
		super(sodier, worldIn);
		fireproduct=true;
		seatPosX[0] = 0F;
		seatPosY[0] = 4.15F;
		//this.rider_height = 4F;
		seatPosZ[0] = 10.82F;
		//seatTurret[0] = true;
		seatHide[0] = true;
		this.selfheal = true;
		seatMaxCount = 8;
		seatPosX[1] = 0F;
		seatPosY[1] = 3.1F-1.2F;
		seatPosZ[1] = 17.89F;
		//seatTurret[1] = true;
		this.minyaw = -120F;
		this.maxyaw = 120F;
		this.throwspeed = 6F;
		this.throwgrav = 0.15F;
		this.canNightV=true;
		this.armor_front = 120;
		this.armor_side = 100;
		this.armor_back = 70;
		this.armor_top = 40;
		this.armor_bottom = 40;
		this.haveTurretArmor = true;
		this.armor_turret_height = 3;
		this.armor_turret_front = 120;
		this.armor_turret_side = 100;
		this.armor_turret_back = 70;
		
		seatPosX[2] = 0F;
		seatPosY[2] = 3.1F;
		seatPosZ[2] = -10.45F;
		
		seatPosX[3] = -4.38F;
		seatPosY[3] = 2.29F;
		seatPosZ[3] = 4.37F;
		
		seatPosX[4] = 4.38F;
		seatPosY[4] = 2.29F;
		seatPosZ[4] = 4.37F;
		
		seatPosX[5] = -3.63F;
		seatPosY[5] = 6F;
		seatPosZ[5] = -1.3F;
		
		seatPosX[6] = 3.63F;
		seatPosY[6] = 6F;
		seatPosZ[6] = -1.3F;
		
		seatPosX[7] = 0F;
		seatPosY[7] = 5.33F;
		seatPosZ[7] = -5.57F;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/box.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/shiphead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/shipbody.png");
		
		this.renderHudIcon = false;
		this.renderHudOverlay = true;
		this.hudOverlay = "wmlib:textures/misc/cannon_scope.png";
		this.renderHudOverlayZoom = true;
		this.hudOverlayZoom = "wmlib:textures/misc/tank_scope.png";
		
		this.w1name = "200毫米重炮";
		
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		seatView3X=0F;
		seatView3Y=-6F;
		seatView3Z=-12F;
		this.seatProtect = 0.01F;
		this.turretPitchMax = -80;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.03F;
        this.turnSpeed = 1F;
		this.turretSpeed = 0.2F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		
		this.magazine = 3;
		this.reload_time1 = 100;
		this.reloadSound1 = SASoundEvent.reload_m1a2.get();
		
		this.weaponCount = 1;
		this.w1icon="wmlib:textures/hud/he120mm.png";
		this.attack_range_min=15;
	}

	public EntitySA_BattleShip(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_BSHIP.get(), worldIn);
	}
	public ResourceLocation getIcon1(){
		return this.icon1tex;
	}
	public ResourceLocation getIcon2(){
		return this.icon2tex;
	}
	public void stopUnitPassenger(){
		this.stopPassenger();
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
		return this.getVehicle()!=null;
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
		//this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new VehicleLockGoal(this, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 10F, true, false, (attackentity) -> {
			return this.CanAttack(attackentity) && this.getTargetType()>1;
		},100F,10F));
	}

    public boolean CanAttack(Entity target){
		double px = 0;
		double pz = 0;
		px -= Mth.sin(this.yHeadRot * 0.01745329252F) * this.seatPosZ[0];
		pz += Mth.cos(this.yHeadRot * 0.01745329252F) * this.seatPosZ[0];
		double d5 = target.getX() - this.getX()-px;
		double d7 = target.getZ() - this.getZ()-pz;
		float yaw= -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
		if((TurretAngle.isAngleInRange(this.getYHeadRot(),yaw, this.minyaw, this.maxyaw)||this.getMoveType()==1 && this.isInWater()) && this.distanceTo(target)>this.attack_range_min){
			if(this.getTargetType()==2){
				return TargetSelect.mobCanAttack(this,target,this.getTarget());
			}else{
				return TargetSelect.friendCanAttack(this,target,this.getTarget());
			}
		}else{
			return false;
		}
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
	
	protected void tickDeath() {
	  ++this.deathTime;
	  if (this.deathTime == 1){
		  this.playSound(SASoundEvent.tank_explode.get(), 3.0F, 1.0F);
		  this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3, false, Level.ExplosionInteraction.NONE);
	  }
	  if (this.deathTime == 120) {
		 this.discard(); //Forge keep data until we revive player
		 this.playSound(SASoundEvent.wreck_explosion.get(), 3.0F, 1.0F);
		 this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2, false, Level.ExplosionInteraction.NONE);
		 for(int i = 0; i < 20; ++i) {
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			double d2 = this.random.nextGaussian() * 0.02D;
			this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
		 }
	  }
	}
    public void setAnimFire(int id)
    {
        if(this != null && !this.level().isClientSide)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageVehicleAnim(this.getId(), id));
        }
    }
	public int seat2shell;
	public int seat3shell;
	public int cf1;
	public int cf2;
	public int cf3;
	public int cf4;
	public int cf5;
	public int cf6;
	public int cf7;
	public int cf8;
	public int cf9;
	public void setVehicleAnim(int x){
		if(x==2)this.anim2=0;
		if(x==5)this.anim5=0;
		if(x==6)this.anim6=0;
		if(x==7)this.anim7=0;
		if(x==8)this.anim8=0;
		if(x==9)this.anim9=0;
		if(x==1){
			this.anim1=0;
			if(this.getRemain1()==2){
				cf3=0;
			}else if(this.getRemain1()==1){
				cf2=0;
			}else{
				cf1=0;
			}
		}
		if(x==3){
			this.anim3=0;
			if(this.seat2shell==2){
				cf6=0;
			}else if(this.seat2shell==1){
				cf5=0;
			}else{
				cf4=0;
			}
		}
		if(x==4){
			this.anim4=0;
			if(this.seat3shell==2){
				cf9=0;
			}else if(this.seat3shell==1){
				cf8=0;
			}else{
				cf7=0;
			}
		}
	}
	
	
	float bodyTargetYaw = 0;
	
	public float rotation_3 = 0;
	public float rotationp_3 = 0;
	public float rotation_4 = 0;
	public float rotationp_4 = 0;
	public float rotation_5 = 0;
	public float rotationp_5 = 0;
	public float rotation_6 = 0;
	public float rotationp_6 = 0;
	public float rotation_7 = 0;
	public float rotationp_7 = 0;
	public float fix = 0;
	public float turretYawO1;
	public float turretPitchO1;
	public boolean ammo = false;
	public static int count = 0;
	int x = 1;

	public boolean aimYawTrue = false;
	public boolean aimPitchTrue = false;

	public void tick() {
		super.tick();
		if(cf1<16)++cf1;
		if(cf2<16)++cf2;
		if(cf3<16)++cf3;
		if(cf4<16)++cf4;
		if(cf5<16)++cf5;
		if(cf6<16)++cf6;
		if(cf7<16)++cf7;
		if(cf8<16)++cf8;
		if(cf9<16)++cf9;
		if(this.getHealth()<=0)return;
		if(this.canAddPassenger(null)){
			if (!this.level().isClientSide){
				EntitySA_Seat seat = new EntitySA_Seat(ModEntities.ENTITY_SEAT.get(), this.level());
				seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
				this.level().addFreshEntity(seat);
				seat.startRiding(this);
			}
		}
		if(this.startTime==1){
			this.playSound(SASoundEvent.start_m1a2.get(), 5.0F, 1.0F);
		}

		float f1 = this.yHeadRot * (2 * (float) Math.PI / 360);//
		AI_TankSet.set(this, null,1, f1, this.MoveSpeed, 0.1F,true);//
		if (this.getAnySeat(1) != null){//炮位1
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
			if(this.setSeat){
				seat.minyaw = -120F;
				seat.maxyaw = 120F;
			}
			this.seatWeapon1(seat);
			this.turretYaw1=seat.turretYaw;
			/*if(seat.turretPitch<15 && seat.turretPitch>-75F)*/this.turretPitch1=seat.turretPitch;
			seat2shell=seat.getRemain1();
		}
		if (this.getAnySeat(2) != null){//炮位2
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(2);
			if(this.setSeat){
				seat.minyaw = -300F;
				seat.maxyaw = -120F;
			}
			if(seat.getTargetType()==0){
				seat.turretYaw = -180F;
				seat.yHeadRot = -180F;
				//seat.setYRot(-180F);
			}
			this.seatWeapon1(seat);
			this.turretYaw2=seat.turretYaw;
			/*if(seat.turretPitch<15 && seat.turretPitch>-75F)*/this.turretPitch2=seat.turretPitch;
			seat3shell=seat.getRemain1();
		}
		if (this.getAnySeat(3) != null){//机炮位3
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(3);
			if(this.setSeat){
				seat.minyaw = -10F;
				seat.maxyaw = 190F;
				this.seatWeapon2(seat);
			}
			this.rotation_3=seat.turretYaw;
			if(seat.turretPitch<15)this.rotationp_3=seat.turretPitch;
		}
		if (this.getAnySeat(4) != null){//机炮位4
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(4);
			if(this.setSeat){
				seat.minyaw = -190F;
				seat.maxyaw = 10F;
				this.seatWeapon2(seat);
			}
			this.rotation_4=seat.turretYaw;
			if(seat.turretPitch<15)this.rotationp_4=seat.turretPitch;
		}
		if (this.getAnySeat(5) != null){//位5
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(5);
			if(this.setSeat)this.seatWeapon3(seat);
			this.rotation_5=seat.turretYaw;
			if(seat.turretPitch<15)this.rotationp_5=seat.turretPitch;
		}
		if (this.getAnySeat(6) != null){//位6
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(6);
			if(this.setSeat)this.seatWeapon3(seat);
			this.rotation_6=seat.turretYaw;
			if(seat.turretPitch<15)this.rotationp_6=seat.turretPitch;
		}
		if (this.getAnySeat(7) != null){//导弹位5
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(7);
			if(this.setSeat)this.seatWeapon4(seat);
			this.rotation_7=seat.turretYaw;
			if(seat.turretPitch<15)this.rotationp_7=seat.turretPitch;
		}
		float fire_x = 0.89F;
		if(this.getRemain1()==2){
			fire_x = 0;
		}else if(this.getRemain1()==1){
			fire_x = -0.89F;
		}
		
		if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
			if(this.getTargetType()>0)this.setTargetType(0);//]
			EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
			Player player = (Player)seat.getControllingPassenger();
			if(player!=null)PL_LandMove.moveTankMode(player, this, this.MoveSpeed, turnSpeed);
			if(this.getChange()>0){
				turretFollow(player.yHeadRot,player.getXRot());
			}else{
				if(this.getMoveMode()==0){
					this.turretPitch = this.turretPitchMove = player.getXRot();
					this.setXRot(this.turretPitch);
				}
				if(player.yHeadRot>=0){
					this.turretYaw = player.yHeadRot*0.995F;
				}else{
					this.turretYaw = player.yHeadRot*1.005F;
				}
			}
			{
				if(seat.fire1)
				{
					if(cooltime >= 10){
						this.counter1 = true;
						cooltime = 0;
					}
					if(this.counter1 && this.getRemain1() > 0){
						this.setAnimFire(1);
						this.AIWeapon1(fire_x,4.25F,7.73F,0,2.6F);
						this.playSound(SASoundEvent.fire_jp.get(), 5.0F, 1.0F);
						this.setRemain1(this.getRemain1() - 1);
						//this.setRemain2(this.getRemain2()-1);//
						this.gun_count1 = 0;//
						this.counter1 = false;
						this.onFireAnimation(0,5);
					}
					seat.fire1 = false;
				}
			}
		}else{
			if(this.getTargetType()==0)this.setTargetType(1);//
			if(this.getTargetType()==1){
				if(this.getMoveType()!=3){
					if(this.yHeadRot==0)this.yHeadRot=this.getYRot();
					this.setMoveYaw(this.yHeadRot);
					this.setForwardMove(0);
					this.setStrafingMove(0);
				}
			}
		}
		
		if(this.isInWater()|| this.isInLava()){
			updateAmphibiousMovement();
			if (this.getForwardMove()>0){
				if(this.throttle < this.throttleMax){
					this.throttle = this.throttle + this.thFrontSpeed;
				}
			}
			if (this.getForwardMove()<0){
				if(this.throttle > this.throttleMin){
					this.throttle = this.throttle + this.thBackSpeed;
				}
			}
			if(this.throttle != 0){
				if(this.getForwardMove()==0 && this.throttle < 0.09 && this.throttle > -0.09) {
					this.throttle = 0;
				}
				if(this.throttle > 0){
					this.throttle = this.throttle - 0.1F;
				}
				if(this.throttle < 0){
					this.throttle = this.throttle + 0.1F;
				}
			}
		}
		
		if(this.getTargetType()>0){
			if (this.getFirstSeat() != null){
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				if(seat.getTargetType()==0&&this.enc_soul==0 && this.getTargetType()!=1)this.setTargetType(1);//空
				if(seat.getTargetType()==2 && this.getTargetType()!=2)this.setTargetType(2);//敌
				if(seat.getTargetType()==3 && this.getTargetType()!=3)this.setTargetType(3);//友
			}
			if(this.getTargetType()>1){
				turretFollow(this.getMoveYaw(),this.getMovePitch());
				this.ai_move(this, MoveSpeed, 30, 30);
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
				
				if(this.getTarget()!=null && aimYawTrue && aimPitchTrue){
					LivingEntity livingentity = this.getTarget();
					if(livingentity.isAlive())
					{
						if(this.cooltime > 10 && this.getRemain1() > 0)
						{
							this.counter1 = true;
							this.cooltime = 0;
						}
						if(this.counter1 && this.getRemain1() > 0){
							this.setAnimFire(1);
							this.AIWeapon1(fire_x,4.25F,7.73F,0,2.6);
							this.playSound(SASoundEvent.fire_jp.get(), 5.0F, 1.0F);
							this.setRemain1(this.getRemain1() - 1);
							this.gun_count1 = 0;//
							this.counter1 = false;
						}
					}
					if(this.isAttacking() && this.find_time<40/* && this.getRemain1()>0*/){
						++this.find_time;
					}
					if(this.level().random.nextInt(6) > 3 && this.find_time > 20){
						this.find_time = 0;
						this.setAIType(0);
					}else if(this.level().random.nextInt(6) < 3 && this.find_time > 20){
						this.find_time = 0;
						this.setAIType(3+this.level().random.nextInt(5));
					}
				}
				if(!this.isAttacking()){
					if(this.getMoveType()==1||this.getMoveType()==3)this.setStrafingMove(0);
				}
			}
		}
		
		if (this.getForwardMove()>0.1F)this.setForwardMove(this.getForwardMove()-0.1F);
		if (this.getForwardMove()<-0.1F)this.setForwardMove(this.getForwardMove()+0.1F);
		if (this.getStrafingMove() < 0){
			this.deltaRotation += this.turnSpeed;
		}
		if (this.getStrafingMove() > 0){
			this.deltaRotation -= this.turnSpeed;
		}
		if(this.deltaRotation > 20)this.deltaRotation = 20;
		if(this.deltaRotation < -20)this.deltaRotation = -20;
		sensitivityAdjust = 2f - (float)Math.exp(-2.0f *(this.throttle*0.1F+15)) / (4.5f *(this.throttle*0.1F+15));
		sensitivityAdjust = Mth.clamp(sensitivityAdjust, 0.0f, 1.0f);
		sensitivityAdjust *= 0.125F;
		float rt = this.deltaRotation * sensitivityAdjust;
		this.deltaRotation *= 0.7F;
		this.yHeadRot += rt;
		this.turretYaw += rt;
		this.yBodyRot = this.yHeadRot;
		this.setYRot(this.yHeadRot);
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
		if(this.turretPitch<targetPitch){
			if(targetPitch<this.turretPitchMin)this.turretPitch+=speedx;
		}else if(this.turretPitch>targetPitch){
			if(targetPitch>this.turretPitchMax)this.turretPitch-=speedx;
		}
		if(f2<2*speedx&&f2>-2*speedx){
			aimPitchTrue = true;
			this.turretPitch = targetPitch;//pitch
		}else{
			aimPitchTrue = false;
		}
	}

	public void seatWeapon1(EntitySA_Seat seat){
		seat.seatProtect = 0.01F;
		float fire_x = 0.89F;
		if(seat.getRemain1()==2){
			fire_x = 0;
		}else if(seat.getRemain1()==1){
			fire_x = -0.89F;
		}
		/*if(this.setSeat)*/{
			seat.turretSpeed = 0.2F;
			seat.seatPosY[0] = 1F;
			if(seat.getBbHeight()!=1.1F)seat.setSize(6F, 1.1F);
			seat.hudfollow = true;
			seat.seatHide = true;
			seat.weaponCount = 1;
			seat.isthrow = true;
			//seat.changeThrow = true;
			//seat.weaponThreeFire[0]=true;
			seat.turret_speed = true;
			seat.w1name="200毫米重炮";
			seat.ammo1 = 10;
			seat.magazine = 3;
			seat.reload_time1 = 100;
			seat.reloadSound1 = SASoundEvent.reload_m1a2.get();
		}
		seat.attack_range_max = 100;
		String model = "advancearmy:textures/entity/bullet/bulletcannon_big.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet.png";
		String fx1 = "AdvTankFire";
		//String fx2 = "SAAPTrail";
		seat.setWeapon(0, 3, model, tex, fx1, null, SASoundEvent.fire_jp.get(), fire_x,0.25F,7.73F,0,2.6F,
		80, this.throwspeed, 1.5F, 10, false, 1, this.throwgrav, 400, 1);
	}
	public void seatWeapon2(EntitySA_Seat seat){
		seat.seatProtect = 0.01F;
		seat.turretSpeed = 0.4F;
		seat.render_hud_box = true;
		seat.hud_box_obj = "wmlib:textures/hud/box.obj";
		seat.hud_box_tex = "wmlib:textures/hud/box.png";
		seat.renderHudIcon = true;
		seat.renderHudOverlay = false;
		seat.hudIcon = "wmlib:textures/hud/tankm1.png";
		seat.attack_range_max = 40;
		seat.turret_speed = true;
		seat.turretPitchMax = -50;
		seat.turretPitchMin = 10;
		seat.w1name="76毫米舰炮";
		if(seat.getBbHeight()!=1.1F)seat.setSize(1.8F, 1.1F);
		seat.seatPosY[0] = 1F;
		seat.seatHide = true;
		seat.weaponCount = 1;
		seat.ammo1 = 20;
		seat.magazine = 10;
		seat.reload_time1 = 100;
		seat.reloadSound1 = SASoundEvent.reload_m1a2.get();
		String model = "advancearmy:textures/entity/bullet/bulletcannon_small.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet.png";
		String fx1 = "AdvTankFire";
		String fx2 = null;
		seat.setWeapon(0, 3, model, tex, fx1, fx2, SASoundEvent.fire_m1a2.get(), 0F,0.8F,3F,0,2.6F,
		50, 4F, 1.5F, 2, false, 1, 0.015F, 25, 1);
	}
	public void seatWeapon3(EntitySA_Seat seat){
		seat.seatProtect = 0.01F;
		seat.turretSpeed = 0.6F;
		seat.render_hud_box = true;
		seat.hud_box_obj = "wmlib:textures/hud/box2.obj";
		seat.hud_box_tex = "wmlib:textures/hud/box.png";
		seat.renderHudOverlay = false;
		if(seat.getBbHeight()!=2.2F)seat.setSize(1.2F, 2.2F);
		seat.seatPosX[0] = 0.6F;
		seat.seatPosZ[0] = 0.4F;
		seat.attack_range_max = 80;
		seat.attack_height_max = 100;
		seat.attack_height_min = -5;
		seat.turretPitchMax = -75;
		seat.turretPitchMin = 10;
		seat.w1name="密集阵防空系统";
		seat.seatHide = true;
		seat.weaponCount = 1;
		seat.ammo1 = 3;
		seat.magazine = 400;
		seat.reload_time1 = 100;
		seat.reloadSound1 = SASoundEvent.reload_m1a2.get();
		String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet1.png";
		String fx1 = "SmokeGun";
		String fx2 = null;
		seat.setWeapon(0, 3, model, tex, fx1, fx2, SASoundEvent.fire_minigun.get(), 0F,1F,1.55F,0,0,
		15, 5F, 1.5F, 1, false, 1, 0.01F, 25, 1);
	}
	public void seatWeapon4(EntitySA_Seat seat){
		seat.seatProtect = 0.01F;
		//seat.weaponThreeFire[0]=true;
		seat.weaponcross[0]=true;
		seat.is_aa = true;
		seat.canlock = true;
		seat.turretSpeed = 0.4F;
		seat.render_hud_box = true;
		seat.hud_box_obj = "wmlib:textures/hud/box3.obj";
		seat.hud_box_tex = "wmlib:textures/hud/box.png";
		seat.renderHudOverlay = false;
		if(seat.getBbHeight()!=2.2F)seat.setSize(4F, 2.2F);
		seat.seatPosY[0] = 2F;
		seat.seatPosZ[0] = 1F;
		seat.attack_range_max = 80;
		seat.turret_speed = true;
		seat.attack_height_max = 100;
		seat.attack_height_min = 10;
		seat.turretPitchMax = -75;
		seat.turretPitchMin = 10;
		seat.seatHide = true;
		seat.w1name="轻型爱国者导弹系统";
		seat.weaponCount = 1;
		seat.ammo1 = 40;
		seat.magazine = 8;
		seat.reload_time1 = 100;
		seat.reloadSound1 = SASoundEvent.reload_missile.get();
		String model = "advancearmy:textures/entity/bullet/bulletrocket.obj";
		String tex = "advancearmy:textures/entity/bullet/bulletrocket.png";
		String fx1 = "SmokeGun";
		String fx2 = "SAMissileTrail";
		seat.setWeapon(0, 4, model, tex, fx1, fx2, SASoundEvent.fire_missile.get(), 0.6F,1F,3F,0,2.6F,
		65, 6F, 1, 2, false, 1, 0.01F, 250, 1);
	}
	
	public void AIWeapon1(double w, double h, double z, double bx, double bz){
		double px = 0;
		double pz = 0;
		px -= Mth.sin(this.getYRot() * 0.01745329252F) * this.seatPosZ[0];
		pz += Mth.cos(this.getYRot() * 0.01745329252F) * this.seatPosZ[0];
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = Mth.sqrt((float)((z - bz)* (z - bz) + (w - bx)*(w - bx))) * Mth.sin(-this.turretPitch  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * z;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * z;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1) * w;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1) * w;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		EntityShell bullet = new EntityShell(this.level(), shooter);
		bullet.hitEntitySound=SASoundEvent.artillery_impact.get();
		bullet.hitBlockSound=SASoundEvent.artillery_impact.get();
		bullet.power = 80;
		bullet.setGravity(this.throwgrav);
		bullet.setExLevel(10);
		//bullet.setFX("SAAPTrail");
		bullet.setModel("advancearmy:textures/entity/bullet/bulletcannon_big.obj");
		bullet.setTex("advancearmy:textures/entity/bullet/bullet.png");
		bullet.moveTo(this.getX()+px + xx11, this.getY()+h+base, this.getZ()+pz + zz11, this.getYRot(), this.getXRot());
		bullet.shootFromRotation(this, this.turretPitch, this.turretYaw, 0.0F, this.throwspeed, 1.5F);
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("AdvTankFire", null, this.getX()+px + xx11, this.getY()+h+base, this.getZ()+pz + zz11, 0, 0, 0, 2);
		if (!this.level().isClientSide) this.level().addFreshEntity(bullet);
	}

	public void ai_move(EntitySA_BattleShip entity, float MoveSpeed, double max, double range1) {
		boolean move = false;
		boolean crash = false;
		float angle = 0;
		{//索敌
			if (entity.getTarget() != null) {
				boolean flag = entity.getSensing().hasLineOfSight(entity.getTarget());
				if (!entity.getTarget().isInvisible()) {//target
					if (flag)entity.setAttacking(true);
					LivingEntity target = this.getTarget();
					if (target.isAlive() && target!=null) {
						double px = 0;
						double pz = 0;
						px -= Mth.sin(entity.yHeadRot * 0.01745329252F) * entity.seatPosZ[0];
						pz += Mth.cos(entity.yHeadRot * 0.01745329252F) * entity.seatPosZ[0];
						double d5 = target.getX() - entity.getX()-px;
						double d7 = target.getZ() - entity.getZ()-pz;
						double d6 = target.getY() - entity.getY();
						double d1 = entity.getEyeY() - (target.getEyeY());
						double d3 = (double) Math.sqrt(d5 * d5 + d7 * d7);
						float f11 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
						double ddx = Math.abs(d5);
						double ddz = Math.abs(d7);
						float f12 = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
						entity.setMoveYaw(f12);
						float targetpitch = -f11;
						
						double xx11 = 0;
						double zz11 = 0;
						xx11 -= Math.sin(entity.turretYaw * 0.01745329252F) * entity.fireposZ1;
						zz11 += Math.cos(entity.turretYaw * 0.01745329252F) * entity.fireposZ1;
						
						double[] angles = new double[2];
						boolean flag1 = ThrowBullet.canReachTarget(entity.throwspeed, entity.throwgrav, 0.99,
								(int) (entity.getX()+xx11), (int) (entity.getY()+entity.fireposY1), (int) (entity.getZ()+xx11),
								(int) target.getX(), (int) target.getEyeY(), (int) target.getZ(),
								angles, entity.getMoveType()!=3);
						if (flag1) {
							targetpitch = (float)-angles[1];
						}
						entity.setMovePitch(targetpitch);//
						if(entity.getMoveType() == 1){//
							if(entity.getAIType()==4){//偏转
								angle = 25;
							}else if(entity.getAIType()==5){
								angle = -25;
							}
							if((ddx>15F||ddz>15F)){
								float f3 = 100-(f12-entity.yHeadRot)+angle;// -180 ~ 0 ~ 180
								entity.bodyTargetYaw=f3;
								f3 = entity.clampYaw(f3);
								if (f3 > entity.turnSpeed*1.5F) {
									entity.setStrafingMove(1);
								} else if (f3 < -entity.turnSpeed*1.5F) {
									entity.setStrafingMove(-1);
								}else{
									entity.setStrafingMove(0);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private boolean wasInWaterLastTick = false;
	private float floatPhase = 0.0f; // 用于上下浮动的相位
	public float waterTargetDepth; // 自定义：实体浸入水中的深度比例 (0-1)
	public float buoyancyStrength = 0.08f; // 自定义：浮力强度
	public float floatAmplitude = 0.001f; // 自定义：上下浮动幅度
	public float floatSpeed = 0.05f; // 自定义：浮动速度
	public void updateAmphibiousMovement() {
		this.waterTargetDepth = -0.2f;
		//this.checkAndPrepareToExitWater();
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
}
package advancearmy.entity.land;

import java.util.List;
import java.util.Random;
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
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import wmlib.common.living.PL_LandMove;
import wmlib.common.living.WeaponVehicleBase;
import net.minecraft.core.particles.ParticleTypes;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import net.minecraftforge.fml.ModList;
import net.minecraft.sounds.SoundSource;
import advancearmy.entity.EntitySA_Seat;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageVehicleAnim;
import net.minecraftforge.network.PacketDistributor;
import safx.SagerFX;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.common.living.ai.VehicleSearchTargetGoalSA;
import wmlib.api.IArmy;
import advancearmy.init.ModEntities;
import advancearmy.util.TargetSelect;
public class EntitySA_Reaper extends WeaponVehicleBase implements IArmy{
	public EntitySA_Reaper(EntityType<? extends EntitySA_Reaper> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 1.07F;
		seatPosY[0] = 3.4F;
		seatPosZ[0] = 2.7F;
		seatHide[0] = true;
		this.selfheal = true;
		seatPosX[1] = -1.1F;
		seatPosY[1] = 3.3F;
		seatPosZ[1] = -1.06F;
		this.canNightV=true;
		this.armor_front = 30;
		this.armor_side = 20;
		this.armor_back = 10;
		this.armor_top = 20;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 30;
		this.armor_turret_side = 20;
		this.armor_turret_back = 20;
		this.turretPitchMax = -25;
		this.turretPitchMin = 10;
		this.seatTurret[0] = true;
		this.seatTurret[1] = true;
		this.seatTurret[2] = true;
		this.seatTurret[3] = true;
		this.turretSpeed = 0.5F;
		seatPosX[2] = 1.1F;
		seatPosY[2] = 3.3F;
		seatPosZ[2] = -1.06F;
		
		seatPosX[3] = 0F;
		seatPosY[3] = 4.4F;
		seatPosZ[3] = -0.8F;
		seatMaxCount = 4;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/tankru.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/reaperhead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/reaperbody.png");
		
		this.w1name = "120mm Cannon";
		//this.w2name = "Jump";
		
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		seatView3X=0F;
		seatView3Y=-1.5F;
		seatView3Z=-4F;
        this.MoveSpeed = 0.05F;
        this.turnSpeed = 3F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(1.5F);
		
		this.magazine = 1;
		this.reload_time1 = 70;
		
		this.weaponCount = 1;
		this.w1icon="wmlib:textures/hud/heat120mm.png";
		//this.w2icon="advancearmy:textures/hud/jump.png";
		//this.is_aa=true;
		this.attack_range_max = 55;
		//this.attack_height_max = 90;
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
	
    public float playerJumpPendingScale;
	
	public EntitySA_Reaper(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_REAPER.get(), worldIn);
	}
	
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new VehicleLockGoal(this, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new VehicleSearchTargetGoalSA<>(this, Mob.class, 10, false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new VehicleSearchTargetGoalSA<>(this, Player.class, 10, false, (attackentity) -> {return this.CanAttack(attackentity);}));
	}
	public float rotation_3 = 0;
	public float rotationp_3 = 0;
    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && this.getTargetType()!=1){
			boolean can = false;
				double ddy = Math.abs(entity.getY()-this.getY());
				if(ddy>15){
					can = true;
				}else{
					if(this.distanceTo(entity)<=this.attack_range_max||this.distanceTo(entity)<=this.getAttributeValue(Attributes.FOLLOW_RANGE)){
						can = true;
					}
				}
			if(can){
			if(this.getTargetType()==2){
				return TargetSelect.mobCanAttack(this,entity,this.getTarget());
			}else{
				return entity instanceof Enemy;
			}
			}else{
				return false;
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
	
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource damageSource) {
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("DropRing", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F);	
		List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(5D, 3.0D, 5D));
		for(int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity = list.get(k2);
			if(entity!=null && entity instanceof LivingEntity && entity!=this){
				if(entity instanceof LivingEntity && this.CanAttack(entity)){
					entity.hurt(this.damageSources().thrown(this, this), 6);
					this.setMovePitch(0);
					this.playSound(SASoundEvent.sickle_land.get(), 5.0F,1);
				}
				((LivingEntity)entity).knockback(0.8F, 3F, 3);
			}
		}
		return false;
	}
	
	int flytime = 0;
	float moveyaw = 0;
	public void tick() {
		super.tick();
		if(this.canAddPassenger(null)){
			EntitySA_Seat seat = new EntitySA_Seat(ModEntities.ENTITY_SEAT.get(), this.level());
			seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
			this.level().addFreshEntity(seat);
			seat.startRiding(this);
		}

		if (this.getAnySeat(1) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
			if(this.setSeat){
			seat.minyaw = 5F;
			seat.maxyaw = 170F;
			seat.seatPosZ[0]=0.35F;
			this.seatWeapon1(seat);
			}
			this.turretYaw1=seat.getYHeadRot();
			if(seat.getXRot()<15)this.turretPitch1=seat.getXRot();
		}
		if (this.getAnySeat(2) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(2);
			if(this.setSeat){
			seat.minyaw = -170F;
			seat.maxyaw = -5F;
			seat.seatPosZ[0]=0.35F;
			this.seatWeapon1(seat);
			}
			this.turretYaw2=seat.getYHeadRot();
			if(seat.getXRot()<15)this.turretPitch2=seat.getXRot();
		}
		
		if (this.getAnySeat(3) != null){
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(3);
			seat.attack_height_max = 80;
			this.seatWeapon2(seat);
			this.rotation_3=seat.getYHeadRot();
			if(seat.getXRot()<15)this.rotationp_3=seat.getXRot();
		}
		
		
		if(this.flytime<100)++this.flytime;
		boolean fire1 = false;
		boolean is_aim = false;
		float speedy = 2;
		if(this.tracktick % 25 == 0 && (this.getX() != this.xo || this.getZ() != this.zo) && this.onGround()){
			if(!this.level().isClientSide)this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SASoundEvent.sickle_move.get(), SoundSource.WEATHER, 3.0F, 1.0F);
		}
		Player player = null;
		if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
			if(this.getTargetType()>0)this.setTargetType(0);//
			EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
			 player = (Player)seat.getControllingPassenger();

			float follow = player.yHeadRot;
			follow = this.clampYaw(follow);
			this.setMoveYaw(follow);
			if(player.getXRot()>0){
				this.turretPitch = player.getXRot()*0.8F;
				this.setXRot(this.turretPitch);
			}else{
				this.turretPitch = player.getXRot()*1.2F;
				this.setXRot(this.turretPitch);
			}
			if(player.yHeadRot>=0){
				this.turretYaw = player.yHeadRot*0.995F;
			}else{
				this.turretYaw = player.yHeadRot*1.005F;
			}
			
			{
				
				if(seat.fire2){
					if(this.getRemain2() > 0){
						this.playerJumpPendingScale = 1;
						this.setRemain2(0);
					}
					seat.fire2 = false;
				}
				if(seat.fire1)
				{
					fire1=true;
					seat.fire1 = false;
				}
				float f = Math.abs(this.yHeadRot - this.getMoveYaw());
				float f2 = this.yHeadRot - this.getMoveYaw();
				f2 = this.clampYaw(f2);
				boolean is_move = false;
				if (this.getStrafingMove() < 0.0F) {
					is_move = true;
				}
				if (this.getStrafingMove() > 0.0F) {
					is_move = true;
				}
				if (this.getForwardMove() > 0.5F) {
					is_move = true;
				}
				if (this.getForwardMove() < -0.5F) {
					is_move = true;
				}
				if(is_move) {
					if(f <= turnSpeed*2 && f >= -turnSpeed*2){
						this.yHeadRot = this.getMoveYaw();
						this.yBodyRot = this.yHeadRot;
						this.setYRot(this.yHeadRot);
					}else if(f2 > 0.1F){
						this.yHeadRot -= turnSpeed;
						this.yBodyRot -= turnSpeed;
						this.setYRot(this.getYRot()-turnSpeed);
					}else if(f2 < -0.1F){
						this.yHeadRot += turnSpeed;
						this.yBodyRot += turnSpeed;
						this.setYRot(this.getYRot()+turnSpeed);
					}
				}
			}
		}else{
			if(this.getTargetType()==0)this.setTargetType(1);//
			if(this.getTargetType()==1){
				if(this.getForwardMove()!=0||this.getStrafingMove()!=0){
					this.setForwardMove(0);
					this.setStrafingMove(0);
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
				
				if(this.getMoveType()==0 && this.getOwner()!=null && followTime>30){
					if(this.distanceTo(this.getOwner())>12){
					this.setMovePosX((int)this.getOwner().getX());
					this.setMovePosY((int)this.getOwner().getY());
					this.setMovePosZ((int)this.getOwner().getZ());
						followTime=0;
					}
				}
				
				if((this.getMovePosX()!=0||this.getMovePosZ()!=0)&&(this.getMoveType()==0||this.getMoveType()==2||!this.isAttacking()&&this.getMoveType()==4)){
					double dx = this.getMovePosX() - this.getX();
					double dz = this.getMovePosZ() - this.getZ();
					double dis = Math.sqrt(dx * dx + dz * dz);
					if(dis>5){
						if(dis>20 && this.flytime > 50){
							if(this.flytime > 60)this.flytime = 0;
							this.playerJumpPendingScale = 1;
							this.setRemain2(0);
						}
						this.moveyaw = (float) Mth.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
						this.moveyaw = this.clampYaw(this.moveyaw);
						if(!this.isAttacking()){
							this.setMoveYaw(this.moveyaw);
						}
						float f3 = this.yHeadRot - this.moveyaw;
						f3 = this.clampYaw(f3);
						if (f3 > this.turnSpeed*1.5F) {
							this.yHeadRot -= turnSpeed;
							this.yBodyRot -= turnSpeed;
							this.setYRot(this.getYRot()-turnSpeed);
						} else if (f3 < -this.turnSpeed*1.5F) {
							this.yHeadRot += turnSpeed;
							this.yBodyRot += turnSpeed;
							this.setYRot(this.getYRot()+turnSpeed);
						}else{
							//this.setStrafingMove(0);
						}
						if(f3>-this.turnSpeed*2F&&f3<this.turnSpeed*2F){
							this.yBodyRot=this.yHeadRot=this.moveyaw;
							this.setYRot(this.moveyaw);
							this.setForwardMove(2);
						}else{
							this.setForwardMove(-2);
							is_aim=true;
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
						boolean crash = false;
						double dx = target.getX() - this.getX();
						double dz = target.getZ() - this.getZ();
						double ddy = Math.abs(target.getY() - this.getY());
						double dyy = this.getY()+3.29F - target.getEyeY();
						double dis = Math.sqrt(dx * dx + dz * dz);
						this.targetYaw = (float) Mth.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
						this.targetYaw = this.clampYaw(this.targetYaw);
						this.setMoveYaw(this.targetYaw);
						float f11 = (float) (Math.atan2(dyy, dis) * 180.0D / Math.PI);
						this.setAttacking(true);
						if(this.getMoveType() == 1||this.getTargetType()==2 && this.getMoveType()!=2&&this.getMoveType()!=4){
							if(this.find_time<40)++this.find_time;
							if(this.level().random.nextInt(6) > 3 && this.find_time > 20){
								this.find_time = 0;
								this.setAIType(this.level().random.nextInt(7));
							}else if(this.level().random.nextInt(6) < 3 && this.find_time > 20){
								this.find_time = 0;
								this.setAIType(0);
							}
							if (dis>40F) {//
								float f3 = this.yHeadRot - this.getMoveYaw();
								f3 = this.clampYaw(f3);
								if(f3>-4F&&f3<4F && this.getAIType()>3){
									if(target.getMaxHealth()<100)this.setForwardMove(2);
								}
							}else if(dis < 6){//
								crash = true;
							}else{
								this.setForwardMove(0);
							}
							if(crash){//
								is_aim=false;
								if (this.getAIType()<3||target.getMaxHealth()>100){
									this.setForwardMove(-2);
								}else{
									this.setForwardMove(2);
								}
								if(this.flytime > 50){
									if(this.flytime > 60)this.flytime = 0;
									this.playerJumpPendingScale = 1;
									this.setRemain2(0);
								}
							}
						}
						this.turretPitch = f11;
						this.setXRot(this.turretPitch);
						float f4 = this.turretYaw - this.getMoveYaw();
						f4 = this.clampYaw(f4);
						if(f4 < speedy*1.1F && f4 > -speedy*1.1F){
							fire1 = true;
						}
						if(this.getMoveType()==1){
							float f3 = this.yHeadRot - this.getMoveYaw();
							f3 = this.clampYaw(f3);
							if (f3 > this.turnSpeed*1.5F) {
								this.yHeadRot -= turnSpeed;
								this.yBodyRot -= turnSpeed;
								this.setYRot(this.getYRot()-turnSpeed);
							} else if (f3 < -this.turnSpeed*1.5F) {
								this.yHeadRot += turnSpeed;
								this.yBodyRot += turnSpeed;
								this.setYRot(this.getYRot()+turnSpeed);
							}else{
								//this.setStrafingMove(0);
							}
							if(f3>-this.turnSpeed*2F&&f3<this.turnSpeed*2F){
								this.yBodyRot=this.yHeadRot=this.getMoveYaw();
								this.setYRot(this.getMoveYaw());
								//this.setForwardMove(0);
							}else{
								this.setForwardMove(-2);
								is_aim=true;
							}
						}
					}
				}else{
					if(!this.isAttacking()){
						if(this.getMoveType()==1||this.getMoveType()==3){
							this.setForwardMove(0);
							this.setStrafingMove(0);
							this.setXRot(0);
						}
					}
				}
				if (this.getForwardMove()>0.1F)this.setForwardMove(this.getForwardMove()-0.05F);
				if (this.getForwardMove()<-0.1F)this.setForwardMove(this.getForwardMove()+0.05F);
			}
		}
		
		if(fire1){
			if(cooltime>2){
				this.counter1 = true;
				this.cooltime = 0;
			}
			if(this.counter1 && this.getRemain1() > 0){
				this.setAnimFire(1);
				this.AIWeapon1(0F, 3.2F, 3.29F,0,0);
				this.playSound(SASoundEvent.fire_t90.get(), 5.0F, 1.0F);
				this.setRemain1(this.getRemain1() - 1);
				this.gun_count1 = 0;//
				this.counter1 = false;
				if(this.getTargetType()==0)this.onFireAnimation(6,6);
			}
		}
		
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
		}
		float f1 = this.yHeadRot * (2 * (float) Math.PI / 360);
		double x = 0;
		double y = this.getDeltaMovement().y();
		double z = 0;
		if (this.getStrafingMove() < -0.5F) {
			x += Mth.sin(this.yHeadRot * 0.01745329252F - 1.57F) * MoveSpeed * 5;
			z -= Mth.cos(this.yHeadRot * 0.01745329252F - 1.57F) * MoveSpeed * 5;
		}
		if (this.getStrafingMove() > 0.5F) {
			x += Mth.sin(this.yHeadRot * 0.01745329252F + 1.57F) * MoveSpeed * 5;
			z -= Mth.cos(this.yHeadRot * 0.01745329252F + 1.57F) * MoveSpeed * 5;
		}
		if (this.getForwardMove() > 0.5F) {
			x -= Mth.sin(f1) * MoveSpeed * 5;
			z += Mth.cos(f1) * MoveSpeed * 5;
		}
		if (this.getForwardMove() < -0.5F) {
			x -= Mth.sin(f1) * MoveSpeed * -5;
			z += Mth.cos(f1) * MoveSpeed * -5;
		}
		{
			float aim_sp = 1;
			if(is_aim)aim_sp=0.5F;
			float enc_sp = 1+this.enc_power*0.1F;
			this.setDeltaMovement(x*enc_sp*aim_sp, y, z*enc_sp*aim_sp);
		}
	}
	
	public void seatWeapon1(EntitySA_Seat seat){
		seat.attack_range_max = 35;
		seat.hudfollow = true;
		seat.seatHide = true;
		seat.weaponCount = 1;
		seat.ammo1 = 3;
		seat.magazine = 200;
		seat.reload_time1 = 80;
		seat.reloadSound1 = SASoundEvent.reload_mag.get();
		String model = "advancearmy:textures/entity/bullet/bullet3.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet3.png";
		String fx1 = "SmokeGun";
		String fx2 = null;
		seat.setWeapon(0, 0, model, tex, fx1, fx2, SASoundEvent.fire_ruvg.get(), 0,0F,0.8F,0,0.2F,
		4, 6F, 1.25F, 0, false, 1, 0.01F, 20, 0);
	}
	
	public void seatWeapon2(EntitySA_Seat seat){
		float fire_x = 0.6F;
		if(seat.getRemain1()==2){
			fire_x = 0;
		}else if(seat.getRemain1()==1){
			fire_x = -0.6F;
		}
		if(this.setSeat){
		seat.canlock = true;
		seat.is_aa = true;
		seat.turretSpeed = 0.4F;
		seat.render_hud_box = true;
		seat.hud_box_obj = "wmlib:textures/hud/aa.obj";
		seat.hud_box_tex = "wmlib:textures/hud/box.png";
		seat.renderHudOverlay = false;
		if(seat.getBbHeight()!=2.2F)seat.setSize(4F, 2.2F);
		seat.seatPosY[0] = 2F;
		seat.seatPosZ[0] = 1F;
		seat.attack_range_max = 100;
		seat.turret_speed = true;
		seat.changeThrow = true;
		seat.attack_height_max = 100;
		//seat.attack_height_min = 10;
		seat.turretPitchMax = -90;
		seat.turretPitchMin = 45;
		seat.seatHide = true;
		seat.isthrow = true;
		seat.w1name="猛犸牙导弹系统";
		seat.weaponCount = 1;
		seat.ammo1 = 8;
		seat.magazine = 10;
		seat.reload_time1 = 100;
		seat.reloadSound1 = SASoundEvent.reload_missile.get();
		}
		String model = "advancearmy:textures/entity/bullet/bulletrocket.obj";
		String tex = "advancearmy:textures/entity/bullet/bulletrocket.png";
		String fx1 = "SmokeGun";
		String fx2 = "SAMissileSmoke";
		seat.setWeapon(0, 4, model, tex, fx1, fx2, SASoundEvent.fire_rpg7.get(), fire_x,1F,3F,0,2.6F,
		45, 4F, 1, 2, false, 1, 0.1F, 250, 2);
	}
	
	public void AIWeapon1(double w, double h, double z, double bx, double bz){
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = Mth.sqrt((float)((z - bz)* (z - bz) + (w - bx)*(w - bx))) * Mth.sin(-this.getXRot()  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * z;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * z;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1) * w;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1) * w;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		EntityShell bullet = new EntityShell(this.level(), shooter);
		bullet.power = 80;
		bullet.setExLevel(2);
		bullet.setModel("wmlib:textures/entity/flare.obj");
		bullet.setTex("wmlib:textures/entity/flare.png");
		bullet.setGravity(0.01F);
		bullet.moveTo(this.getX() + xx11, this.getY()+h+base, this.getZ() + zz11, this.yHeadRot, this.getXRot());
		bullet.shootFromRotation(this, this.turretPitch, this.turretYaw, 0.0F, 4F, 2);
		if (!this.level().isClientSide) this.level().addFreshEntity(bullet);
	}
}
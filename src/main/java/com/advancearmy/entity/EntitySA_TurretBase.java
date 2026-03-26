package advancearmy.entity;

import java.util.List;

import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PlayMessages;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.fml.ModList;

import advancearmy.AdvanceArmy;
import advancearmy.init.ModEntities;
import advancearmy.event.SASoundEvent;
import wmlib.common.living.PL_LandMove;

import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.common.living.ai.VehicleSearchTargetGoalSA;

import wmlib.common.living.EntityWMSeat;
import wmlib.util.ThrowBullet;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageVehicleAnim;
import net.minecraftforge.network.PacketDistributor;
import wmlib.client.obj.SAObjModel;
import net.minecraft.resources.ResourceLocation;
import wmlib.api.ITool;
import wmlib.api.IArmy;

import net.minecraft.world.level.block.state.BlockState;
import advancearmy.util.TargetSelect;
public abstract class EntitySA_TurretBase extends WeaponVehicleBase implements IArmy{
	public EntitySA_TurretBase(EntityType<? extends EntitySA_TurretBase> sodier, Level worldIn) {
		super(sodier, worldIn);
	}
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
		//this.setMoveType(id);
	}
	public boolean getSelect(){
		return this.getChoose() && this.getTargetType()==3;
	}
	public boolean isDrive(){
		return /*this.getVehicle()!=null*/true;
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
	public void stopUnitPassenger(){
		this.stopPassenger();
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
		this.goalSelector.addGoal(2, new VehicleLockGoal(this, false));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new VehicleSearchTargetGoalSA<>(this, Mob.class, 10, false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new VehicleSearchTargetGoalSA<>(this, Player.class, 10, false, (attackentity) -> {return this.CanAttack(attackentity);}));
	}

    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && this.getTargetType()!=1){
			boolean can = false;
			if(this.is_aa){
				double ddy = Math.abs(entity.getY()-this.getY());
				if(ddy>15){
					can = true;
				}else{
					if(this.distanceTo(entity)<=this.attack_range_max){
						can = true;
					}
				}
			}else{
				can = true;
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
	public boolean hidebarrel1 = false;
	public float w1recoilp = 1;
	public float w1recoilr = 1;

	public float w1barrelsize = 1F;

	public float fireposX1 = 0;
	public float fireposY1 = 0;
	public float fireposZ1 = 0;
	public float fireposX2 = 0;
	public float fireposY2 = 0;
	public float fireposZ2 = 0;
	public float firebaseY = 0;
	public float firebaseZ = 0;
	
	public int radercount = 0;
	public float[] raderx = new float[6];
	public float[] radery = new float[6];
	public float[] raderz = new float[6];

	public ResourceLocation guntex = null;

	public float turretYawO1;
	public float turretPitchO1;
	public SoundEvent firesound1;
	public SoundEvent firesound2;
	public boolean ammo = false;
	public static int count = 0;
	public int healtime = 0;
	
	public void setRader(int id, float x,float y,float z){
		this.raderx[id] = x;
		this.radery[id] = y;
		this.raderz[id] = z;
	}
	
    public void setAnimFire(int id)
    {
        if(this != null && !this.level().isClientSide)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageVehicleAnim(this.getId(), id));
        }
    }
	
	protected void tickDeath() {
	  ++this.deathTime;
	  if (this.deathTime == 1){
		  this.playSound(SASoundEvent.tank_explode.get(), 3.0F+this.getBbWidth()*0.1F, 1.0F);
		  this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3+this.getBbWidth()*0.1F, false, Level.ExplosionInteraction.NONE);
	  }
	  if (this.deathTime >= 20) {
		 this.discard(); //Forge keep data until we revive player
		 this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2+this.getBbWidth()*0.1F, false, Level.ExplosionInteraction.NONE);
		 for(int i = 0; i < this.getBbWidth()*5; ++i) {
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			double d2 = this.random.nextGaussian() * 0.02D;
			this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
		 }
	  }
	}
	
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource damageSource) {
		return false;
	}
	
	LivingEntity deployer = null;
	public boolean can_hand_deploy = true;
	public boolean isDeploying = false;
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if(can_hand_deploy){
			if(player.isCrouching() && this.getOwner()==player && player.getVehicle()==null){
				if(!isDeploying){
					isDeploying=true;
					deployer=player;
					ridcool = 20;
				}
				return InteractionResult.PASS;//SUCCESS
			}else{
				if(isDeploying||deployer!=null){
					return InteractionResult.PASS;
				}else{
					return super.mobInteract(player, hand);
				}
			}
		}else{
			return super.mobInteract(player, hand);
		}
    }
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_TurretBase.createMobAttributes();
    }
	float roteyaw = 0;
	public int control_tick = 0;
	Player player = null;
	
	public boolean selfAttack = false;
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	/*public boolean canCollideWith(Entity entity) {
		return false;
	}
	public boolean canBeCollidedWith() {//
		return false;
	}*/
	
	public boolean changeThrow = false;
	
	public void tick() {
		super.tick();
		if(this.getVehicle()==null){
			if(can_hand_deploy){
				if(deployer!=null && isDeploying){
					this.setx=0;
					deployer.setDeltaMovement(deployer.getDeltaMovement().x * 0.5F, deployer.getDeltaMovement().y, deployer.getDeltaMovement().z * 0.5F);
					float f1 = deployer.getYRot() * (2 * (float) Math.PI / 360);
					double ix = 0;
					double iz = 0;
					ix -= Math.sin(f1) * 1.5F;
					iz += Math.cos(f1) * 1.5F;
					this.setPos(deployer.getX() + ix, deployer.getY()+0.5F, deployer.getZ() + iz);
					//roteyaw=-deployer.yHeadRot;
					roteyaw=deployer.getYRot();
					/*if(roteyaw >= 360F || roteyaw <= -360F){
						roteyaw = 0;
					}*/
					this.setYRot(roteyaw);
					this.turretYaw = this.yHeadRot = this.yBodyRot = this.getYRot();

					if(ridcool==0 && deployer.isCrouching()||deployer.getHealth()==0){
						isDeploying=false;
						deployer=null;
					}
				}
			}
			if(!isDeploying){
				if(this.setx == 0) {
					this.setx=((int)this.getX());
					this.sety=((int)this.getY());
					this.setz=((int)this.getZ());
				}
				{
					BlockPos blockpos = new BlockPos((int)(this.setx + 0.5),(int)(this.sety - 1),(int)(this.setz + 0.5));
					BlockState iblockstate = this.level().getBlockState(blockpos);
					if (this.setx != 0 && !iblockstate.isAir()){
						this.moveTo(this.setx,this.sety,this.setz);
					}else{
						this.moveTo(this.setx,this.getY(), this.setz);
					}
				}
			}
		}

		
		if(this.getHealth()>0){
			if(this.canAddPassenger(null) && !selfAttack){
				if (!this.level().isClientSide){
					EntitySA_Seat seat = new EntitySA_Seat(ModEntities.ENTITY_SEAT.get(),this.level());
					seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
					this.level().addFreshEntity(seat);
					seat.startRiding(this);
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
			
			boolean fire1 = false;
			boolean fire2 = false;
			float speedy = 2+this.turretSpeed*2;
			float speedx = 1+this.turretSpeed;
			if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
				if(this.getTargetType()>0)this.setTargetType(0);//
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				player = (Player)seat.getControllingPassenger();
				if(seat.startRidTime>10){
					if(this.getChange()>0){
						float follow = player.yHeadRot;
						follow = this.clampYaw(follow);
						this.setMoveYaw(follow);
						if(this.getMoveMode()==0){
							this.setXRot(player.getXRot());
							float f2 = (float) (this.getXRot() - this.turretPitch);// -180 ~ 0 ~ 180
							if(this.turretPitchMove<this.getXRot()){
								if(this.getXRot()<turretPitchMin)this.turretPitchMove+=speedx;
							}else if(this.turretPitchMove>this.getXRot()){
								if(this.getXRot()>turretPitchMax)this.turretPitchMove-=speedx;
							}
							if(f2<2&&f2>-2)this.turretPitchMove = this.getXRot();
							this.turretPitch = this.turretPitchMove;//pitch
						}
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
				if(seat.fire1){
					fire1 = true;
					seat.fire1 = false;
				}
				if(seat.fire2){
					fire2 = true;
					seat.fire2 = false;
				}
			}else{
				if(this.getTargetType()==0 && !selfAttack){
					this.setTargetType(1);
					player = null;
				}
				if(this.getTargetType()==1){
					if(this.getMoveType()!=3){
						this.setMoveType(3);
						this.setMoveYaw(0);
					}
				}
			}
			
			if(this.control_tick<20)++this.control_tick;
			if(this.getTargetType()>0){//非空
				if (this.getFirstSeat() != null){
					EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
					if(seat.getTargetType()==0&&this.enc_soul==0 && this.getTargetType()!=1)this.setTargetType(1);
					if(seat.getTargetType()==2 && this.getTargetType()!=2)this.setTargetType(2);
					if(seat.getTargetType()==3 && this.getTargetType()!=3)this.setTargetType(3);
				}
				if(this.getTargetType()>1){
					this.targetYaw = this.yHeadRot = this.getYRot();
					if (this.getTarget() != null) {
						LivingEntity target = this.getTarget();
						if(target.isAlive() && target!=null){
							boolean crash = false;
							float angle = 0;
							float height = target.getBbHeight()*0.5F;
							if(height>3)height=3;
							double dx = target.getX()+target.getDeltaMovement().x*0.2F - this.getX();
							double dz = target.getZ()+target.getDeltaMovement().z*0.2F - this.getZ();
							double dyy = this.getY()+this.fireposY1 - target.getY()-height-target.getDeltaMovement().y*0.2F;
							double dis = Math.sqrt(dx * dx + dz * dz);
							this.targetYaw = (float) Mth.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
							this.targetYaw = this.clampYaw(this.targetYaw);
							this.setMoveYaw(this.targetYaw);
							float f11 = (float) (Math.atan2(dyy, dis) * 180.0D / Math.PI);
							this.setAttacking(true);
							float targetpitch = f11;
							if(this.isthrow){
								boolean canChangeAim = true;
								if(this.onlythrow || this.getMoveType()==3&&!this.changeThrow){
									canChangeAim=false;
								}
								double[] angles = new double[2];
								boolean flag = ThrowBullet.canReachTarget(this.throwspeed, this.throwgrav, 0.99,
										(int) this.getX(), (int) (this.getY()+this.fireposY1), (int) this.getZ(),
										(int) target.getX(), (int) target.getEyeY(), (int) target.getZ(),
										angles, canChangeAim);
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
									fire1 = true;
									if(this.level().random.nextInt(6) > 4){
										fire2 = true;
									}
								}
							}
							if(this.getMoveType()!=2){
								float f3 = this.targetYaw - this.getMoveYaw();
								f3 = this.clampYaw(f3);
								if (f3 > this.turnSpeed*1.5F) {
									this.setStrafingMove(1);
								} else if (f3 < -this.turnSpeed*1.5F) {
									this.setStrafingMove(-1);
								}else{
									this.setStrafingMove(0);
								}
							}
						}
					}else{
						this.targetYaw = this.yHeadRot;
						if(!this.isAttacking()){
							this.setMoveYaw(0);
							this.setXRot(0);
						}
					}
				}
			}
			if(this.getMoveMode()==1){
				if (this.getForwardMove()>0){
					if(this.turretPitchMove>this.turretPitchMax)this.turretPitchMove-=speedx;
				}
				if (this.getForwardMove()<0){
					if(this.turretPitchMove<this.turretPitchMin)this.turretPitchMove+=speedx;
				}
			}
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
			}
			/*this.setYRot(this.turretYaw);
			this.yHeadRot = this.turretYaw;*/
			
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
					if(this.getTargetType()==0)this.onFireAnimation(this.w1recoilp,this.w1recoilr);
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
		}
	}
}
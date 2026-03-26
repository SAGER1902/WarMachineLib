package advancearmy.entity;

import java.util.List;

import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;


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

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.fml.ModList;
//import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import net.minecraft.server.level.ServerLevel;
import advancearmy.AdvanceArmy;

import advancearmy.event.SASoundEvent;
import wmlib.common.living.PL_LandMove;

import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.common.living.ai.VehicleSearchTargetGoalSA;

import net.minecraft.sounds.SoundSource;
import wmlib.common.living.EntityWMSeat;

import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageVehicleAnim;
import net.minecraftforge.network.PacketDistributor;
import wmlib.client.obj.SAObjModel;
import net.minecraft.resources.ResourceLocation;

import wmlib.common.living.PL_AirCraftMove;
import wmlib.common.living.AI_AirCraftSet;

import wmlib.common.bullet.EntityFlare;
import wmlib.WarMachineLib;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.nbt.CompoundTag;
import wmlib.api.IArmy;
import wmlib.api.ITool;
import safx.SagerFX;
import advancearmy.init.ModEntities;
import wmlib.init.WMModEntities;
import net.minecraft.world.level.chunk.LevelChunk; // 具体的区块实现
import net.minecraft.world.level.chunk.ChunkAccess; // 区块访问接口
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.tags.BlockTags;
import advancearmy.util.TargetSelect;
import advancearmy.AAConfig;
public abstract class EntitySA_AirBase extends WeaponVehicleBase implements IArmy{
	public EntitySA_AirBase(EntityType<? extends EntitySA_AirBase> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.lockAlertSound=SASoundEvent.laser_lock.get();
		this.destroySoundStart=SASoundEvent.air_explosion.get();
		this.destroySoundEnd=SASoundEvent.wreck_explosion.get();
		this.armor_front = 10;
		this.armor_side = 9;
		this.armor_back = 8;
		this.armor_top = 7;
		this.armor_bottom = 6;
		this.antibullet_0 = 0.2F;
		this.antibullet_1 = 0.8F;
		this.antibullet_2 = 1.5F;
		this.antibullet_3 = 1.5F;
		this.antibullet_4 = 2F;
		this.seatProtect = 0.2F;
		this.attack_height_max=100;
	}

	public int getUnitType(){
		return 1;
	}
	public void stopUnitPassenger(){
		this.stopPassenger();
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
	public boolean choose = false;
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
		this.targetSelector.addGoal(1, new VehicleSearchTargetGoalSA<>(this, Mob.class, 10,false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new VehicleSearchTargetGoalSA<>(this, Player.class, 10,false, (attackentity) -> {return this.CanAttack(attackentity);}));
	}

    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && this.getTargetType()!=1){
			if(this.getTargetType()==2){
				return TargetSelect.mobCanAttack(this,entity,this.getTarget());
			}else{
				return entity instanceof Enemy;
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
	public int rotorcount = 0;
	public float[] rotorx = new float[2];
	public float[] rotory = new float[2];
	public float[] rotorz = new float[2];
	public float[] rotor_rotex = new float[2];
	public float[] rotor_rotey = new float[2];
	public float[] rotor_rotez = new float[2];
	
	public int trailcount = 0;
	public float[] trailx = new float[5];
	public float[] traily = new float[5];
	public float[] trailz = new float[5];
	
	public float mgx = 0;
	public float mgy = 0;
	public float mgz = 0;
	public float mgbz = 0;
	
	public float turretYawO1;
	public float turretPitchO1;
	public SoundEvent firesound1;
	public SoundEvent firesound2;
	public SoundEvent firesound4;
	public SoundEvent startsound;
	public SoundEvent movesound;
	public int healtime = 0;
	
	public SAObjModel mgobj = null;
	
	public ResourceLocation trailtex = null;
	public ResourceLocation duntex = null;
	public ResourceLocation drivetex = null;
	public ResourceLocation enemytex = null;
	public ResourceLocation mgtex = null;
	public ResourceLocation rotortex = null;
	public ResourceLocation w1tex = null;
	public ResourceLocation w2tex = null;
	public ResourceLocation w3tex = null;
	public ResourceLocation w4tex = null;
	
	public boolean w1showammo = false;
	public boolean w2showammo = false;
	public boolean w3showammo = false;
	public boolean w4showammo = false;
	
	public void setRotor(int id, float x,float y,float z){
		this.rotorx[id] = x;
		this.rotory[id] = y;
		this.rotorz[id] = z;
	}
	public void setTrail(int id, float x,float y,float z){
		this.trailx[id] = x;
		this.traily[id] = y;
		this.trailz[id] = z;
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
	
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
	  return false;
	}
	public boolean canDespawn(double distanceToClosestPlayer) {
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
	   return true;
	}
	@OnlyIn(Dist.CLIENT)
	public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
	   return true;
	}
	
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 1){
		  this.playSound(SASoundEvent.air_explosion.get(), 3.0F+this.getBbWidth()*0.1F, 1.0F);
		  this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3+this.getBbWidth()*0.1F, false, Level.ExplosionInteraction.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("VehicleExp1", null, this.getX(), this.getY(), this.getZ(), 0,0,0,1+this.getBbWidth()*0.1F);
		}
		if (this.deathTime >= 120||this.onGround()) {
			this.discard(); //Forge keep data until we revive player
			this.playSound(SASoundEvent.wreck_explosion.get(), 2.0F+this.getBbWidth()*0.1F, 1.0F);
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2+this.getBbWidth()*0.1F, false, Level.ExplosionInteraction.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("VehicleExp2", null, this.getX(), this.getY(), this.getZ(), 0,0,0,1+this.getBbWidth()*0.1F);
		}
	}
	public boolean w1aa = false;
	public int w1max = 50;
	public int w1min = 0;
	public int w1aim = 0;
	public boolean w2aa = false;
	public int w2max = 100;
	public int w2min = 0;
	public int w2aim = 5;
	public boolean w4aa = false;
	public int w4max = 50;
	public int w4min = 0;
	public int w4aim = 5;
	public int stayrange = 50;
	public int fly_height = 55;
	public int min_height = 15;

	public int control_tick = 0;
	public int cracktime = 0;
	public int random_height = 1;
	int flarecd = 0;
	
	public boolean onGround(){
		return (this.getY()-this.block_height)<2;
	}
	
	public int aiStartTime;
	
	public int drop = 0;
	float moveyaw = 0;
	float moveptich = 0;
	public void tick() {
		super.tick();
		if(this.canAddPassenger(null)){
			if (!this.level().isClientSide){
				EntitySA_Seat seat = new EntitySA_Seat(ModEntities.ENTITY_SEAT.get(), this.level());
				seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
				this.level().addFreshEntity(seat);
				seat.startRiding(this);
			}
		}
		
		if(this.getTargetType()>1){
			if (this.level() instanceof ServerLevel) {
				ServerLevel serverLevel = (ServerLevel) this.level();
				BlockPos entityPos = this.blockPosition();
				int chunkX = entityPos.getX() >> 4;
				int chunkZ = entityPos.getZ() >> 4;
				int range = 2;
				for (int x = chunkX - range; x <= chunkX + range; x++) {
					for (int z = chunkZ - range; z <= chunkZ + range; z++) {
						serverLevel.setChunkForced(x, z, true);
					}
				}
			}
		}
		
		if(this.onGround() && this.getTargetType()!=1 && this.getHealth()>0){
			if(flyPitch<0)flyPitch+=0.5F;
			if(flyPitch>0)flyPitch-=0.5F;
			if(flyRoll<0)flyRoll+=0.5F;
			if(flyRoll>0)flyRoll-=0.5F;
		}
		
		float f1 = this.yHeadRot * (2 * (float) Math.PI / 360);//
		this.block_height = this.level().getHeight(
                Heightmap.Types.WORLD_SURFACE, 
                (int)Math.floor(this.getX()), 
                (int)Math.floor(this.getZ())
            );

		float speed = this.MoveSpeed;
		if(this.getAIType()!=4){
			speed = this.MoveSpeed*1.5F;
		}
		if(this.drop<200)++this.drop;
		
		if(this.getY()>120+block_height+this.fly_height && this.getTargetType()>0){
			this.drop=0;
			this.movePower=0;
			this.throttle=0;
		}
		
		if(fastFly && (this.getY()-this.block_height)>10 && this.movePower<10){
			this.movePower=10;
			this.throttle=10;
			this.drop = 200;
			aiStartTime = 500;
			fastFly=false;
		}

		
		/*if(this.drop>50||this.onGround()||this.getTargetType()==0)*/{
			SoundEvent sound = this.movesound;
			if(this.onGround()){
				sound = SASoundEvent.air_ground.get();
			}
			if(this.isSpaceShip){
				if(this.getMoveMode()==0){
					AI_AirCraftSet.setFighterMode(this, sound, f1, speed, 0.1F);
				}else{
					AI_AirCraftSet.setHeliCopterMode(this, sound, f1, speed, 0.1F);
				}
			}else{
				AI_AirCraftSet.setFighterMode(this, sound, f1, speed, 0.02F);
			}
		}
		if(this.getHealth()>0){
			
			while(this.turretYaw1 - this.turretYawO1 < -180.0F) {
				this.turretYawO1 -= 360.0F;
			}
			while(this.turretPitch1 - this.turretPitchO1 >= 180.0F) {
				this.turretPitchO1 += 360.0F;
			}
			this.turretYawO1 = this.turretYaw1;
			this.turretPitchO1 = this.turretPitch1;
			
			if(this.startTime==1 && this.startsound!=null)this.playSound(this.startsound, 5.0F, 1.0F);
			if(this.getMovePosX()==0&&this.getMovePosZ()==0&&this.getTargetType()>1){
				this.setMovePosX((int)this.getX());
				this.setMovePosY((int)this.getY());
				this.setMovePosZ((int)this.getZ());
			}
			int genY = block_height + 8;
			if (this.getY() <= genY && this.movePower>=this.throttleMax/2) {
				if(this.tracktick % 25 == 0)
				{
					this.playSound(SASoundEvent.jet_distant.get(), 2.0F,1);
				}
			}
			boolean fire1 = false;
			boolean fire2 = false;
			boolean fire4 = false;
			float speedy = this.turnSpeed*0.8F+this.MoveSpeed*5;
			float speedHeight = 1+this.MoveSpeed*5;
			if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
				if(cracktime<10)++cracktime;
				if(this.onGround() && this.flyPitch>5/* && this.throttle>5*/){
					if(cracktime>8){
						cracktime = 0;
						this.hurt(this.damageSources().explosion(null,null), this.flyPitch/12F*this.throttle);
					}
				}
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				Player player = (Player)seat.getControllingPassenger();
				if(this.isSpaceShip){
					if(this.getMoveMode()==0){
						PL_AirCraftMove.moveFighterMode(player, this, this.MoveSpeed, this.turnSpeed);
						this.VehicleType = 4;
					}else{
						PL_AirCraftMove.moveHeliCopterMode(player, this, this.MoveSpeed, this.turnSpeed);
						this.VehicleType = 3;
					}
				}else{
					PL_AirCraftMove.moveFighterMode(player, this, this.MoveSpeed, this.turnSpeed);
				}
				if(this.getTargetType()>0)this.setTargetType(0);//
				{
					if(this.getMoveMode()==0){
						if(this.getForwardMove()<0.1F && this.throttle>this.throttleMax*0.7F)--this.throttle;
					}
					if(seat.keylook){
						if(cooltime5 >10){
							if(this.getArmyType1()==0){
								this.playSound(SoundEvents.ARMOR_EQUIP_CHAIN, 2.0F,1);
								this.setArmyType1(1);
							}else{
								this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 2.0F,1);
								this.setArmyType1(0);
							}
							cooltime5=0;
						}
						seat.keylook = false;
					}
					if(seat.keyf && !this.isSpaceShip){
						if(cooltime3 >10){
							if(this.getArmyType2()==0){
								this.playSound(SoundEvents.PISTON_EXTEND, 2.0F,1);
								this.setArmyType2(1);
							}else{
								this.playSound(SoundEvents.PISTON_CONTRACT, 2.0F,1);
								this.setArmyType2(0);
							}
							cooltime3=0;
						}
						seat.keyf = false;
					}
					if(this.isSpaceShip){
						if(seat.keyv){
							if(cooltime3>150)cooltime3=0;
							if(this.getRemain4() > 0){
								if(this.getHealth() < this.getMaxHealth() && this.getHealth() > 0.0F) {
									++healtime;
									if(healtime > 2){
										this.setHealth(this.getHealth() + 1);
										this.playSound(SASoundEvent.fix.get(), 1.0F, 1.0F);
										healtime=0;
									}
								}
							}
							if(cooltime3>80){
								this.setRemain4(0);
								seat.keyv = false;
							}
						}
					}
					if(seat.keyg){
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
					if(seat.keyx){
						if(flaretime>90)flaretime=0;
						seat.keyx = false;
					}
					if(seat.fire1){
						fire1 = fire4 = true;
						seat.fire1 = false;
					}
					if(seat.fire2){
						fire2 = true;
						seat.fire2 = false;
					}
				}
				if(!this.onGround()){
					if(this.getArmyType1()==0){
						if(this.movePower>5){
							this.flyPitch += this.getMovePitch() * (this.turnSpeed*0.6F) * ((180F-Math.abs(this.flyRoll)*2)/180);
							this.flyRoll -= this.getMoveYaw()*(1+this.turnSpeed);
						}
						this.setXRot(this.flyPitch);
						this.turretPitch = this.getXRot();
						this.turretYaw = this.yHeadRot;
					}else{
						if(this.enc_control>0||this.can_follow){
							this.turretYaw=player.yHeadRot;
							this.turretPitch=player.getXRot();
							this.setYRot(this.yHeadRot);
							this.moveyaw = this.getYRot();
							float f4 = this.flyPitch - player.getXRot();
							if (f4 > speedy) {
								this.flyPitch-=speedy;
							} else if (f4 < -speedy) {
								this.flyPitch+=speedy;
							}else{
								this.flyPitch = player.getXRot();
							}
							this.setXRot(this.flyPitch);
							this.moveptich = this.getXRot();
							
							float follow = player.yHeadRot;
							follow = this.clampYaw(follow);
							float f3 = this.moveyaw - follow;
							f3 = this.clampYaw(f3);
							if (f3 > this.turnSpeed) {
								this.setStrafingMove(1);
							} else if (f3 < -this.turnSpeed) {
								this.setStrafingMove(-1);
							}
							if(f3 < this.turnSpeed*1.5F && f3 > -this.turnSpeed*1.5F){
								//this.moveyaw = this.targetYaw;
								this.setStrafingMove(0);
							}
						}
					}
				}
			}else{
				//player = null;
				if(this.getTargetType()==0)this.setTargetType(1);//
			}
			if(flarecd<20)++flarecd;
			if(flaretime<100)++flaretime;
			if(flarecd >2){
				flarecd = 0;
				if(this.getRemain3() > 0){
					if(!this.isSpaceShip){
						if(flaretime<40){
							float angle = 90;
							float firex = 2F;
							if(this.getRemain3()%2==0){
								angle = 75;
								firex = 2F;
							}else{
								angle = -75;
								firex = -2F;
							}
							double xx11 = 0;
							double zz11 = 0;
							xx11 -= Mth.sin(this.getYRot() * 0.01745329252F) * -2;
							zz11 += Mth.cos(this.getYRot() * 0.01745329252F) * -2;
							xx11 -= Mth.sin(this.getYRot() * 0.01745329252F + 1) * firex;
							zz11 += Mth.cos(this.getYRot() * 0.01745329252F + 1) * firex;
							EntityFlare bullet = new EntityFlare(WMModEntities.ENTITY_FLARE.get(), this.level());
							bullet.moveTo(this.getX() + xx11, this.getY(), this.getZ() + zz11, this.getYRot(), this.getXRot());
							bullet.shootFromRotation(this, -25, this.getYRot()+angle, 0, 1F, 1);
							if (!this.level().isClientSide) this.level().addFreshEntity(bullet);
							this.playSound(SoundEvents.LAVA_EXTINGUISH, 2.0F, 1.0F);
							this.setRemain3(this.getRemain3() - 1);
						}
					}else{
						if(flaretime<80){
							this.startShield=true;
							this.setRemain3(this.getRemain3() - 1);
						}else{
							this.startShield=false;
						}
					}
				}else{
					this.startShield=false;
				}
			}
			
			if(this.control_tick<20)++this.control_tick;
			if(this.getTargetType()>0){
				if(cracktime<10)++cracktime;
				if(this.onGround() && this.flyPitch>5/* && this.throttle>5*/){
					if(cracktime>8){
						cracktime = 0;
						this.hurt(this.damageSources().explosion(null,null), 5);
					}
				}
				if (this.getFirstSeat() != null){
					if(aiStartTime<500)++aiStartTime;
					if(aiStartTime>450||this.hurtTime>0||fastFly||this.getOwner()!=null){
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
				}
				if(this.getTargetType()>1){
					if(this.can_follow){
						this.setYRot(this.yHeadRot);
					}else{
						this.setYRot(this.yHeadRot);
						this.turretYaw = this.getYRot();
					}
					if(this.getAIType()!=4)this.setForwardMove(1);
					float heightY = block_height + this.random_height+this.fly_height;
					float heightMin = block_height + this.min_height * speedHeight;
					if(this.getY() > heightY + 35){
						if(!this.isAttacking())this.setAIType(3);
					}
					if (this.getY() < heightY - 35){
						if(this.movePower >= this.throttleMax) {
							if(!this.isAttacking())this.setAIType(3);
						}
					}
					
					if(this.control_tick>2){
						if(this.getStrafingMove()!=0)this.control_tick = 0;
						float angle = 0;
						if(this.hurtTime>0){
							if(this.flyPitch<-30)this.setMoveYaw(2);
							if(flaretime>60 && this.getHealth()<this.getMaxHealth()*0.7)flaretime=0;
						}
						if(this.getMoveMode()!=5){
							if(this.getAIType()!=4){
								if(this.getY()<heightY - this.fly_height*0.3F||this.getY()<heightMin){
									if(this.isAttacking()){
										angle = -35;
									}else{
										angle = -20;
									}
									//float xxx = (angle-this.flyPitch)*0.02F;
									this.setMovePitch(angle);
									if(this.throttle>19 && !this.isAttacking()){
										this.setStrafingMove(1);
									}else{
										this.setStrafingMove(0);
									}
								}
								if(this.getY()>heightY + this.fly_height*0.3F && !this.isAttacking()){
									angle = 15;
									float xxx = (angle-this.flyPitch)*0.02F;
									this.setMovePitch(angle);
								}
								if(this.isAttacking()&&this.getY()>heightY-this.fly_height*0.1F && this.getAIType()!=1  && this.getY()>heightMin){
									this.setAIType(4);
								}
								if(this.getAIType()==5){
									this.setMovePitch(-10);
								}
								if(this.getY()-this.block_height>this.fly_height*0.5F){
									if(this.getAIType()==6){
										this.setStrafingMove(1);
									}
									if(this.getAIType()==7){
										this.setStrafingMove(-1);
									}
									if(this.isAttacking()){
										if(this.getY()<heightY + this.fly_height*0.15F){
											this.setStrafingMove(0);
										}else{
											this.setStrafingMove(1);
										}
									}else{
										if(this.getAIType()==1){
											this.setStrafingMove(0);
										}else{
											this.setStrafingMove(1);
										}
									}
								}
							}
							
							if(this.getMoveType()==0 && this.getOwner()!=null && followTime>30){
								if(this.distanceTo(this.getOwner())>12){
								this.setMovePosX((int)this.getOwner().getX());
								this.setMovePosY((int)this.getOwner().getY());
								this.setMovePosZ((int)this.getOwner().getZ());
									followTime=0;
								}
							}
							
							if(this.getMovePosX()!=0 || this.getMovePosZ()!=0) {
								double dx = this.getMovePosX() - this.getX();
								double dz = this.getMovePosZ() - this.getZ();
								double dis = (double)Math.sqrt(dx * dx + dz * dz);
								double min = this.stayrange;
								if(this.getChoose()/*||this.getMoveType()==2||this.getMoveType()==4*/)min = 5;
								if(dis > min && (this.getAIType()!=1||!this.isAttacking()||this.getMoveType()==2||this.getMoveType()==4)) {
									this.setAIType(2);
									if(this.getAIType()==2 && this.getY()-this.block_height>this.fly_height*0.5F){
										this.targetYaw = (float) Math.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
										this.targetYaw = this.clampYaw(this.targetYaw);
										float f3 = this.yHeadRot - this.targetYaw;
										f3 = this.clampYaw(f3);
										if (f3 > this.turnSpeed) {
											this.setStrafingMove(1);
										} else if (f3 < -this.turnSpeed) {
											this.setStrafingMove(-1);
										}
										if(f3 < this.turnSpeed*1.5F && f3 > -this.turnSpeed*1.5F){
											this.setStrafingMove(0);
										}
									}
								} else if(dis > 5){
									if(!this.isAttacking())this.setAIType(1);
								} else {
									if(this.getMoveType()==2||this.getMoveType()==4)this.setMoveType(1);
									if(this.getAIType()!=4)this.setAIType(3);
								}
							}
							if (this.getTarget() != null && this.movePower >= 5F && !this.onGround()) {
								LivingEntity target = this.getTarget();
								if(target.isAlive() && target!=null){
									aiChange();
									this.setAttacking(true);
									double dx = target.getX()+target.getDeltaMovement().x - this.getX()-this.getDeltaMovement().x;
									double dz = target.getZ()+target.getDeltaMovement().z - this.getZ()-this.getDeltaMovement().z;
									double ddy = Math.abs(target.getY()+target.getDeltaMovement().y - this.getY()-this.getDeltaMovement().y);
									double dyy = this.getY()+this.fireposY1+this.getDeltaMovement().y - target.getY()-target.getDeltaMovement().y;
									double dis = Math.sqrt(dx * dx + dz * dz);
									if(this.getAIType()==1&&dis>60 && this.getY()>heightMin+30*speedHeight-dis)this.setAIType(4);
									float f11 = (float) (Math.atan2(dyy, dis) * 180.0D / Math.PI);
									this.targetYaw = (float) Math.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
									angle = f11;
									if(angle>89)angle = 89;
									if(angle<-89)angle = -89;
									float aim = angle-this.flyPitch;
									if(this.getAIType()==4 && this.getMoveType()!=2){
										this.targetYaw = this.clampYaw(this.targetYaw);
										float f3 = this.yHeadRot - this.targetYaw;
										f3 = this.clampYaw(f3);
										if (f3 > this.turnSpeed * 1.5F) {
											this.setStrafingMove(1);
										} else if (f3 < -this.turnSpeed * 1.5F) {
											this.setStrafingMove(-1);
										} else {
											this.setStrafingMove(0);
										}
										this.setMovePitch(angle);
										this.movePower=this.throttleMax*0.7F;
										this.throttle=this.throttleMax*0.7F;
									}
									
									if(this.can_follow){
										this.turretYaw=this.targetYaw;
										this.turretPitch=f11;
									}
									
									float firediff = this.yHeadRot - this.targetYaw;
									if(firediff>-this.turnSpeed*(15+w1aim)&&firediff<this.turnSpeed*(15+w1aim)){
										if(aim<10+w1aim&&aim>-10-w1aim){
											if(!w1aa && dis<w1max && target.getY()<w1min+this.getY()||w1aa && ddy<w1max && target.getY()>w1min+block_height)fire1 = true;
										}
									}else{
										if(this.hurtTime>0){
											if(dis>12){
												this.setAIType(5+this.random.nextInt(3));
											}else{
												this.movePower=this.throttleMax*0.6F;
												this.throttle=this.throttleMax*0.6F;
											}
										}
									}
									if(firediff>-this.turnSpeed*(15+w2aim)&&firediff<this.turnSpeed*(15+w2aim)){
										if(aim<10+w2aim&&aim>-10-w2aim){
											if(!w2aa && dis<w2max && target.getY()<w2min+this.getY()||w2aa && ddy<w2max && target.getY()>w2min+block_height)fire2 = true;
										}
									}
									if(firediff>-this.turnSpeed*(15+w4aim)&&firediff<this.turnSpeed*(15+w4aim)){
										if(aim<10+w4aim&&aim>-10-w4aim){
											if(!w4aa && dis<w4max && target.getY()<w4min+this.getY()||w4aa && ddy<w4max && target.getY()>w4min+block_height)fire4 = true;
										}
									}
									if(this.getY()<heightMin+30*speedHeight-dis){
										this.setAIType(3);
									}else{
										if(dis<15 && ddy<this.min_height/2){
											this.setAIType(1);
											//this.setAIType(5+this.random.nextInt(3));
										}
									}
								}
							}else{
								if(this.getY()<heightMin){
									this.setAIType(3);
								}
							}
						}
					}else{
						this.setMoveYaw(this.getMoveYaw()*0.8F);
					}
				}
				if(this.movePower>5){
					float f4 = this.flyPitch - this.getMovePitch();
					if (f4 > speedy) {
						this.flyPitch-=speedy;
					} else if (f4 < -speedy) {
						this.flyPitch+=speedy;
					}else{
						this.flyPitch = this.getMovePitch();
					}
					if(this.can_follow){
						this.setXRot(this.flyPitch);
					}else{
						this.setXRot(this.flyPitch);
						this.turretPitch = this.getXRot();
					}
					this.flyRoll -= this.getMoveYaw()*(1+this.turnSpeed);
				}
			}
			if(this.throttle >= 0){
				if(this.movePower < this.throttle){
					if( this.throttle > this.throttleMax*0.8F){
						this.movePower = this.movePower + this.thFrontSpeed;
					}else{
						this.movePower = this.movePower + this.thFrontSpeed*0.5F;
					}
				}else{
					this.movePower = this.movePower + this.thBackSpeed;
				}
				if(this.throttle <= 0 && this.movePower > 0)this.movePower = this.movePower + this.thBackSpeed * 2;
			}

			if (this.getForwardMove() > 0.0F) {
				if(this.throttle < this.throttleMax)this.throttle+=1;
			}
			if (this.getForwardMove() < 0.0F) {
				if(this.throttle >= 1)this.throttle-=1;
			}
			if(this.movePower>5){
				if (this.getStrafingMove() < 0.0F) {
					if(this.flyRoll < 50)this.flyRoll =  this.flyRoll + (float)(this.turnSpeed * 0.15F);
					this.deltaRotation = this.deltaRotation + this.turnSpeed* (180F+this.flyRoll)/180F;
				}
				if (this.getStrafingMove() > 0.0F) {
					if(this.flyRoll > -50)this.flyRoll = this.flyRoll - (float)(this.turnSpeed * 0.15F);
					this.deltaRotation = this.deltaRotation - this.turnSpeed* (180F-this.flyRoll)/180F;
				}
			}
			//this.deltaRotation += this.getMovePitch() * -this.flyRoll/60F;
			this.deltaRotation *= 0.9F;
			if(this.deltaRotation > 20)this.deltaRotation = 20;
			if(this.deltaRotation < -20)this.deltaRotation = -20;
			sensitivityAdjust = 2f - (float)Math.exp(-2.0f * this.throttle) / (4.5f * (this.throttle + 0.1f));
			sensitivityAdjust = Mth.clamp(sensitivityAdjust, 0.0f, 1.0f);
			sensitivityAdjust *= 0.125F;
			float yaw = this.deltaRotation * sensitivityAdjust;
			this.yHeadRot += yaw;
			this.yBodyRot += yaw;
			this.setYRot(this.getYRot()+yaw);
			if(!this.can_follow){
				this.turretYaw += yaw;
				this.turretYawMove += yaw;
			}
			/*if(flarecd<20)++flarecd;
			if(flarecd>2){
				System.out.println("==================");
				if(this.getTarget()!=null){
					System.out.println("target="+this.getTarget().getName().getString());
				}else{
					System.out.println("target=null!!!!");
				}
				//System.out.println("this.getArmyType1()"+this.getArmyType1());
				//System.out.println("this.getForwardMove()"+this.getForwardMove());
				System.out.println("distanceToTarget"+this.dis);
				System.out.println("minDistance"+this.min);
				System.out.println("this.getStrafingMove()"+this.getStrafingMove());
				System.out.println("this.turretPitch"+this.turretPitch);
				System.out.println("this.getMoveType()"+this.getMoveType());
				System.out.println("this.getAIType()"+this.getAIType());
				System.out.println("this.targetYaw"+this.targetYaw);
				System.out.println("this.turretYaw"+this.turretYaw);
				System.out.println("this.yHeadRot"+this.yHeadRot);
				System.out.println("------------------");
				flarecd = 0;
			}*/
			
			if(fire1||this.attack1){
				if(this.getArmyType2()==0||this.getTargetType()>0){
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
			}
			if(fire4||this.attack4){
				if(this.getArmyType2()==1||this.getTargetType()>0){
					if(this.cooltime4 >= this.ammo4){
						this.counter4 = true;
						this.cooltime4 = 0;
					}
					if(this.counter4 && this.gun_count4 >= this.w4cycle && this.getRemain4() > 0){
						this.setAnimFire(1);
						this.weaponActive4();
						this.setRemain4(this.getRemain4() - 1);
						this.gun_count4 = 0;//
						this.counter4 = false;
					}
				}
			}
			if(fire2||this.attack2){
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
					if(this.getTargetType()==0)this.onFireAnimation(this.w1recoilp,this.w1recoilr);
				}
			}
			if(this.getTargetType()!=1)crashEnemy();
		}
	}
	
	public void aiChange(){
		if(this.isAttacking() && this.find_time<40)++this.find_time;
		if(this.level().random.nextInt(6) > 3 && this.find_time > 20){
			this.find_time = 0;
			this.random_height = this.level().random.nextInt(6);
			//this.setAIType(this.level().random.nextInt(5));
		}else if(this.level().random.nextInt(6) < 3 && this.find_time > 20){
			this.find_time = 0;
			if(this.level().random.nextInt(6)<1){
				this.random_height = this.level().random.nextInt(3);
				//this.setAIType(3);
			}else{
				this.random_height = -this.level().random.nextInt(3);
				//this.setAIType(4);
			}
		}
	}
	int crash = 0;
	public void crashEnemy(){
		if(crash<20)++crash;
		if(AAConfig.vehicleDestroy && crash>10 && this.throttle>4){
			breakNearbyFragileBlocks();
			crash=0;
		}
		if(this.tracktick % 5 == 0 && this.throttle>0){//
			List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(2D));
			for(int k2 = 0; k2 < list.size(); ++k2) {
				Entity attackentity = list.get(k2);
				if(attackentity instanceof LivingEntity && ((LivingEntity)attackentity).getHealth()>0){
					if(this.CanAttack(attackentity))attackentity.hurt(this.damageSources().thrown(this, this), this.getBbHeight()*this.getBbWidth()+10*(float)this.movePower);
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
					//|| state.is(BlockTags.MUSHROOM_GROW_BLOCK)
					|| state.is(BlockTags.PLANKS)
					|| state.is(BlockTags.LOGS);
            if (isFragile) {
                // 参数解释：null 表示无实体原因（自然破坏），
                // 若改为 this 则破坏算由此实体造成（可能影响战利品掉落等）。
                // true 表示破坏后是否掉落物品，对于树叶通常为false，但这里我们设为true。
                boolean dropItems = true;
                serverLevel.destroyBlock(pos, dropItems, this);
            }
        });
    }
}
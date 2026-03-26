package advancearmy.entity.land;

import java.util.List;

import javax.annotation.Nullable;

import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityBulletBase;
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
import java.util.Random;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageVehicleAnim;
import net.minecraftforge.network.PacketDistributor;

import wmlib.common.living.PL_LandMove;
import wmlib.common.living.WeaponVehicleBase;
import net.minecraft.core.particles.ParticleTypes;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import net.minecraftforge.fml.ModList;
import net.minecraft.sounds.SoundSource;
import advancearmy.entity.EntitySA_Seat;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.common.living.ai.VehicleSearchTargetGoalSA;
import wmlib.api.IArmy;
import safx.SagerFX;
import advancearmy.init.ModEntities;
import org.joml.Vector3f;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.core.particles.DustParticleOptions;
import wmlib.common.world.WMExplosionBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import wmlib.common.network.message.MessageTrail;

import net.minecraft.client.Minecraft;
import advancearmy.util.TargetSelect;
public class EntitySA_Ember extends WeaponVehicleBase implements IArmy{
	public EntitySA_Ember(EntityType<? extends EntitySA_Ember> sodier, Level worldIn) {
		super(sodier, worldIn);
		fireproduct=true;
		seatPosX[0] = 0F;
		seatPosY[0] = 6.2F;
		seatPosZ[0] = 1.9F;
		this.canNightV=true;
		this.armor_front = 90;
		this.armor_side = 60;
		this.armor_back = 50;
		this.armor_top = 30;
		this.armor_bottom = 30;
		this.haveTurretArmor = true;
		this.armor_turret_height = 4;
		this.armor_turret_front = 100;
		this.armor_turret_side = 80;
		this.armor_turret_back = 40;
		seatTurret[0] = true;
		seatHide[0] = true;
		this.is_aa=true;
		this.attack_range_max = 50;
		this.attack_height_max = 90;
		this.selfheal = true;
		this.seatProtect = 0.01F;
		seatMaxCount = 1;
		this.renderHudIcon = false;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/mech_ui1.obj";
		this.hud_box_tex = "wmlib:textures/hud/mech_ui1.png";
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/hjhead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/hjbody.png");
		this.w1name = "25毫米SWUN机炮";
		this.w2name = "燃烧能量炮";
		this.w4name = "赤翔动力双刀";
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		seatView3X=-5F;
		seatView3Y=-5F;
		seatView3Z=-8F;
        this.MoveSpeed = 0.06F;
        this.turnSpeed = 5F;
		this.turretSpeed = 0.4F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(3F);
		this.magazine = 400;
		this.reload_time1 = 150;
		this.magazine2 = 6;
		this.reload_time2 = 100;
		this.magazine3 = 100;
		this.reload_time3 = 200;
		this.magazine4 = 1;
		this.reload_time4 = 10;
		
		this.reloadSound1 = SASoundEvent.reload_chaingun.get();
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/hjw1.png";
		this.w2icon="advancearmy:textures/hud/hjw2.png";
		this.w3icon="advancearmy:textures/hud/jump.png";
		this.w4icon="advancearmy:textures/hud/hjw4.png";
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
	
	public EntitySA_Ember(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_EMBER.get(), worldIn);
	}
	
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new VehicleLockGoal(this, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new VehicleSearchTargetGoalSA<>(this, Mob.class, 10, false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new VehicleSearchTargetGoalSA<>(this, Player.class, 10, false, (attackentity) -> {return this.CanAttack(attackentity);}));
	}
	
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
	
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource damageSource) {
		List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(5D, 3.0D, 5D));
		for(int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity = list.get(k2);
			if(entity!=null && entity instanceof LivingEntity && entity!=this){
				if(entity instanceof LivingEntity && entity instanceof Enemy){
					entity.hurt(this.damageSources().thrown(this, this), 50);
				}
				((LivingEntity)entity).knockback(0.8F, 3F, 3);
			}
		}
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("DropRing", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F);
		this.playSound(SASoundEvent.shell_impact.get(), 10.0F,1);
		//this.strongAttack=true;
		if(this.getMoveMode()!=1 && this.getRemain2()>0)this.setMoveMode(1);
		return false;
	}
	float size = 1.2F;
	boolean change = true;
	public void updateSwingTime() {
		float i = 14/size;
		if (this.swinging) {
			++this.swingTime;
			if(!change && this.attackAnim<12){
				this.attackAnim+=1.2F*size;
			}
			if(this.attackAnim>=12)change = true;
			if(change && this.attackAnim>8){
				this.attackAnim-=1F*size;
			}
			//if(this.attackAnim<=8)change = false;
			if (this.swingTime >= i) {
				this.swingTime = 0;
				this.swinging = false;
				change=false;
			}
		} else {
			this.swingTime = 0;
			this.attackAnim=0;
			change=false;
			/*if(this.attackAnim>0)this.attackAnim-=0.5F;
			if(this.attackAnim<0)this.attackAnim+=0.5F;*/
		}
	}
	
    public void setAnimFire(int id)
    {
        if(this != null && !this.level().isClientSide)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageVehicleAnim(this.getId(), id));
        }
    }
	
	protected void checkAndPerformAttack(LivingEntity living, double range) {
		if(range <= 8){
			if (this.attack_time > 10) {
				this.setArmyType2(1);
			}
		}else{
			if(this.getArmyType2()!=2 && this.attack_time > 9)this.setArmyType2(0);;
		}
	}
	
	int random_fire = 0;
	
	int flytime = 0;
	int control_tick = 0;
	float moveyaw = 0;
	public int move_mode = 0;
	public int attack_time = 0;
	public boolean run = false;
	public boolean up = false;

	boolean turn = false;
	int aim = 0;
	int aimY = 0;
	int aimX = 0;
	int specialMove = 0;
	
	public int shieldHealth = 0;
	int shieldTime = 0;
	
    public boolean hurt(DamageSource source, float par2)
    {
		if(shieldHealth>0){
			if(par2>shieldHealth){
				shieldHealth=0;
				if(this.getMoveMode()!=1)this.setMoveMode(1);
			}else{
				shieldHealth-=par2;
			}
			par2=0;
			this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 3.0F, 1F);
		}
		return super.hurt(source, par2);
	}
	
	public float moveAnim = 0;
	
	//public boolean strongAttack = true;
	/*public static final DustParticleOptions RED = new DustParticleOptions(new Vector3f(1F, 0, 0F), 4F);
	public static final DustParticleOptions WHITE = new DustParticleOptions(new Vector3f(1F, 1, 1F), 3F);
	public static final DustParticleOptions GREEN = new DustParticleOptions(new Vector3f(0F, 1, 0F), 2F);*/
	public void tick() {
		super.tick();
		if(this.canAddPassenger(null)){
			EntitySA_Seat seat = new EntitySA_Seat(ModEntities.ENTITY_SEAT.get(), this.level());
			seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
			this.level().addFreshEntity(seat);
			seat.startRiding(this);
		}
		
		if(shieldHealth<200 && this.hurtTime<2){
			if(shieldTime<200){
				++shieldTime;
			}else{
				shieldTime=0;
				shieldHealth+=40;
			}
		}
		if(this.getArmyType2()==0){
			this.hud_box_obj = "wmlib:textures/hud/mech_ui1.obj";
		}else if(this.getArmyType2()==1){
			this.hud_box_obj = "wmlib:textures/hud/mech_ui2.obj";
		}else{
			this.hud_box_obj = "wmlib:textures/hud/mech_ui3.obj";
			if(this.getMoveMode()==1){
				this.w2icon="advancearmy:textures/hud/mast.png";
			}else{
				this.w2icon="advancearmy:textures/hud/hjw2.png";
			}
		}
		if(this.flytime<100)++this.flytime;
		if(this.attack_time<20)++this.attack_time;
		this.updateSwingTime();
		
		/*if(this.attack_time>140){
			this.playSound(SASoundEvent.hjswing.get(), 3.0F, 1.3F);
			this.attack_time=0;
			this.swingTime = 0;
			this.attackAnim=0;
			this.swing(InteractionHand.MAIN_HAND);
			this.setMovePosY(1+getMovePosY());
			if(getMovePosY()>4)setMovePosY(0);
			if(this.level().isClientSide()){
				Minecraft.getInstance().player.sendSystemMessage(Component.translatable("getMovePosY="+this.getMovePosY()));
			}
		}*/
		
		{
			if(this.tracktick % 25 == 0 && (this.getX() != this.xo || this.getZ() != this.zo) && this.onGround()){
				if(!this.level().isClientSide)this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SASoundEvent.hjmove.get(), SoundSource.WEATHER, 3.0F, 1.0F);
			}
		}
		Player player = null;
		boolean fire1 = false;
		if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
			EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
			if(this.getTargetType()>0)this.setTargetType(0);//
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
				if(seat.keyf){
					if(cooltime3 >10){
						if(this.getArmyType2()==0){
							this.playSound(SoundEvents.PISTON_EXTEND, 2.0F,1);
							this.setArmyType2(2);
						}else if(this.getArmyType2()==2){
							this.playSound(SoundEvents.PISTON_CONTRACT, 2.0F,1);
							this.setArmyType2(1);
						}else{
							this.playSound(SoundEvents.PISTON_EXTEND, 2.0F,1);
							this.setArmyType2(0);
						}
						cooltime3=0;
					}
					seat.keyf = false;
				}
				if(seat.keyrun){
					this.run = true;
					seat.keyrun = false;
				}
				if(seat.fire2){
					this.up = true;
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
		
		boolean is_aim = false;
		float speedy = 4;
		if(this.getTargetType()>0){
			if (this.getFirstSeat() != null){
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				if(seat.getTargetType()==0&&this.enc_soul==0 && this.getTargetType()!=1)this.setTargetType(1);
				if(seat.getTargetType()==2 && this.getTargetType()!=2)this.setTargetType(2);
				if(seat.getTargetType()==3 && this.getTargetType()!=3)this.setTargetType(3);
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
							this.up = true;
							this.run = true;
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
						double dyy = this.getY()+5.84F - target.getY()- target.getBbHeight()*0.5F;
						if(this.getArmyType2()==2)dyy = this.getY()+9.17F - target.getY() - target.getBbHeight()*0.5F;
						double dis = Math.sqrt(dx * dx + dz * dz);
						
						if(this.getAIType()>0){
							if(!turn && this.aim<8){
								++this.aim;
							}
							if(this.aim>=8)turn = true;
							if(turn && this.aim>-8){
								--this.aim;
							}
							if(this.aim<=-8)turn = false;
						}else{
							this.aimY=this.aimX=this.aim=0;
						}
						if(this.turretPitch<-5){
							this.aimX=aim;
							this.aimY=0;
						}else{
							this.aimY=aim;
							this.aimX=0;
						}
						this.targetYaw = (float) Mth.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
						this.targetYaw = this.clampYaw(this.targetYaw);
						this.setMoveYaw(this.targetYaw+this.aimY);
						float f11 = (float) (Math.atan2(dyy, dis) * 180.0D / Math.PI);
						this.setAttacking(true);
						
						if(this.find_time<40)++this.find_time;
						if(this.level().random.nextInt(6) > 2 && this.find_time > 20){
							this.find_time = 0;
							this.setAIType(this.level().random.nextInt(7));
							if(this.getMoveType() == 1||this.getOwner()==null && this.getMoveType()!=2){
								this.setStrafingMove((this.level().random.nextFloat()-0.5F)*2);
								if(this.level().random.nextInt(4)==1 && specialMove==0 && flytime>50){
									specialMove=3;
									flytime=0;
								}
							}
						}else if(this.level().random.nextInt(6) < 3 && this.find_time > 20){
							this.find_time = 0;
							if(this.getArmyType2()==0 && this.getRemain2()>0){
								this.setArmyType2(2);
							}
							this.setAIType(0);
						}
						
						if(this.getMoveType() == 1||this.getOwner()==null && this.getMoveType()!=2){
							if(this.specialMove>0){
								boolean fly = false;
								if(this.specialMove==3){
									if(dis>5){
										this.setForwardMove(2);
										fly = true;
									}else{
										if(this.flytime > 30)this.specialMove=2;
									}
								}
								if(this.specialMove==2){
									if(dis<50){
										this.setForwardMove(-2);
										fly = true;
									}else{
										this.setForwardMove(0);
										this.specialMove=0;
									}
								}
								if(fly && this.flytime > 60){
									if(this.flytime > 65)this.flytime = 0;
									this.up = true;
									this.run = true;
								}
							}else{
								if (dis>40F) {	
									float f3 = this.yHeadRot - this.getMoveYaw();
									f3 = this.clampYaw(f3);
									if(f3>-4F&&f3<4F && this.getAIType()>3){
										if(target.getMaxHealth()<100)this.setForwardMove(2);
									}
								}else if(dis < 20){
									crash = true;
								}else{
									if(this.flytime > 90)this.setForwardMove(0);
								}
								if(crash){
									is_aim=false;
									if (this.getAIType()<3&&this.getMoveMode()==0){
										this.setForwardMove(-2);
									}else{
										this.setForwardMove(2);
									}
									if((this.getAIType()==1||this.getAIType()==5)&&this.flytime > 30){
										if(this.flytime > 40)this.flytime = 0;
										this.up = true;
										this.run = true;
									}
								}
								if(this.getHealth()<400 && dis<30){
									this.setForwardMove(-2);
									if((this.getAIType()==1||this.getAIType()==5)&&this.flytime > 50){
										if(this.flytime > 70)this.flytime = 0;
										this.up = true;
										this.run = true;
									}
								}
							}
							if(this.getStrafingMove()!=0 && (this.getAIType()==2||this.getAIType()==4||this.hurtTime>0)){
								if(this.flytime > 40){
									if(this.flytime > 60)this.flytime = 0;
									this.up = true;
									this.run = true;
								}
							}
							{
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
									if(this.getForwardMove()==0)this.setForwardMove(-2);
									is_aim=true;
								}
							}
						}
						this.turretPitch = f11 + this.aimX;
						this.setXRot(this.turretPitch);
						float f4 = this.turretYaw - this.getMoveYaw();
						f4 = this.clampYaw(f4);
						if(f4 < speedy*1.1F && f4 > -speedy*1.1F){
							fire1 = true;
						}
						this.checkAndPerformAttack(target, this.distanceTo(target));
						if(this.getArmyType2()==2 && this.getRemain2()<=0){
							this.setArmyType2(0);
						}
					}
				}
				if(!this.isAttacking()){
					if(this.getMoveType()==1||this.getMoveType()==3){
						//this.setForwardMove(0);
						//this.setStrafingMove(0);
						this.setXRot(0);
					}
				}
				if (this.getForwardMove()>0.1F)this.setForwardMove(this.getForwardMove()-0.05F);
				if (this.getForwardMove()<-0.1F)this.setForwardMove(this.getForwardMove()+0.05F);
				if (this.getStrafingMove()>0.1F)this.setStrafingMove(this.getForwardMove()-0.01F);
				if (this.getStrafingMove()<-0.1F)this.setStrafingMove(this.getForwardMove()+0.01F);
			}
		}
		if(this.getMoveMode()==2 && attackAnim>8)this.setMoveMode(0);
		if(fire1){
			if(this.getArmyType2()==0){
				if(cooltime >= 2){
					this.counter1 = true;
					cooltime = 0;
				}
				if(this.counter1 && this.getRemain1() > 0){
					this.setAnimFire(1);
					this.AIWeapon1(2.21F, 5.84F, 5.41F,0,0.09F);
					this.AIWeapon1(-2.21F, 5.84F, 5.41F,0,0.09F);
					this.playSound(SASoundEvent.knightf.get(), 3.0F, 1.0F);
					this.setRemain1(this.getRemain1() - 1);
					this.counter1 = false;
					if(player!=null)this.onFireAnimation(0,3);
				}
			}else if(this.getArmyType2()==2){
				if(cooltime2 >= 10){
					this.counter2 = true;
					cooltime2 = 0;
				}
				if(this.counter2 && this.getRemain2() > 0){
					this.setAnimFire(2);
					this.setRemain2(this.getRemain2() - 1);
					this.counter2 = false;
					if(this.getMoveMode()==1){
						this.AIWeapon3(1.31F, 9.17F, 0.37F,0,-2.13F);
						this.AIWeapon3(-1.31F, 9.17F, 0.37F,0,-2.13F);
						this.playSound(SASoundEvent.moqiuli.get(), 5.0F, 1.0F);
						this.setMoveMode(0);
						this.setRemain2(0);
						if(this.getTargetType()>1)this.setArmyType2(0);
					}else{
						this.AIWeapon2(1.31F, 9.17F, 0.57F,0,-2.13F);
						this.AIWeapon2(-1.31F, 9.17F, 0.57F,0,-2.13F);
						this.playSound(SASoundEvent.powercannon.get(), 5.0F, 1.0F);
					}
					if(player!=null)this.onFireAnimation(0,3);
				}
			}else{
				if(cooltime4 >= 2){
					this.counter4 = true;
					cooltime4 = 0;
				}
				if(this.counter4 && this.getRemain4() > 0){
					if (this.attack_time > 10) {
						this.setRemain4(this.getRemain4() - 1);
						this.counter4 = false;
						this.setMovePosY(1+this.random.nextInt(4));
						if(this.getMoveMode()==1){
							if(this.getMovePosY()==1){
								this.playSound(SoundEvents.ENDER_DRAGON_SHOOT, 3.0F, 1F);
							}else if(this.getMovePosY()==2){
								this.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, 3.0F, 1.3F);
							}else{
								this.playSound(SoundEvents.ANVIL_LAND, 3.0F, 1F);
							}
						}
						boolean have = false;
						float x1 = 0;
						float z1 = 0;
						float ff = this.yHeadRot * 0.01745329252F;
						x1 -= Mth.sin(ff -1.57F) * 5.72D;
						z1 += Mth.cos(ff -1.57F) * 5.72D;
						x1 -= Mth.sin(ff) * -5.4D;
						z1 += Mth.cos(ff) * -5.4D;
						List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(8D, 6.0D, 8D));
						for(int k2 = 0; k2 < list.size(); ++k2) {
							Entity ent = list.get(k2);
							if(ent!=null){
								if(ent instanceof EntityBulletBase){
									ent.discard();
									this.level().addParticle(ParticleTypes.CLOUD, ent.getX(), ent.getY(), ent.getZ(), 0, 0, 0);
								}
								if(this.NotFriend(ent)&&ent!=this){
									this.setHealth(this.getHealth()+5);
									LivingEntity shooter = this;
									if(player!=null)shooter=player;
									ent.hurt(this.damageSources().thrown(this, shooter), 60);
									this.level().addParticle(ParticleTypes.SWEEP_ATTACK, ent.getX(), ent.getY(), ent.getZ(), 0, 0, 0);
									if(ent instanceof LivingEntity){
										LivingEntity living = (LivingEntity)ent;
										if(this.getMoveMode()==1){
											if(this.getMovePosY()==1){
												living.setHealth(living.getHealth()-(living.getMaxHealth()-living.getHealth())*0.08F);
											}else if(this.getMovePosY()==2){
												living.setHealth(living.getHealth()-living.getHealth()*0.06F);
											}else{
												ent.hurt(this.damageSources().thrown(this, shooter), living.getMaxHealth()*0.08F);
											}
										}
										living.knockback(x1, 1F, z1);
										living.invulnerableTime = 0;
									}
									have=true;
								}
							}
						}
						if(have && this.getMoveMode()==1){
							this.setHealth(this.getHealth()+(this.getMaxHealth()-this.getHealth())*0.1F);
							this.playSound(SoundEvents.PLAYER_LEVELUP, 3.0F, 1.3F);
							//this.level().addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
						}
						if(this.getMoveMode()==1)this.setMoveMode(2);
					}
					if(this.getMovePosY()>0 && this.attack_time>11){
						this.playSound(SASoundEvent.hjswing.get(), 3.0F, 1.3F);
						this.attack_time=0;
						this.swingTime = 0;
						this.attackAnim=0;
						this.swing(InteractionHand.MAIN_HAND);
					}
				}
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
			float enc_sp = 1+this.enc_power*0.5F;
			this.setDeltaMovement(x*enc_sp*aim_sp, y, z*enc_sp*aim_sp);
		}
		if(this.run||this.up){
			if(cooltime4 >= 2){
				cooltime4 = 0;
				if(this.getRemain3() > 0){
					Vec3 vector3d = this.getDeltaMovement();
					double fly = vector3d.y;
					float forward = 1;
					if(this.up){
						fly = 1F+this.enc_power*0.1F;
						if(this.getRemain3()%5==0)this.playSound(SASoundEvent.hjup.get(), 5.0F, 1.0F);
						this.setRemain3(this.getRemain3() - 2);
						forward = 5F;
					}
					if(this.run && !this.onGround())forward = 10F;
					this.setDeltaMovement(vector3d.x*forward, fly, vector3d.z*forward);
				}
			}
			this.run = false;
			this.up = false;
		}
		if(this.onGround()){
			move_mode = 0;
			if(this.getRemain3()<this.magazine3)this.setRemain3(this.getRemain3() + 1);
		}else{
			if (this.getStrafingMove() < 0.0F) {
				move_mode = 3;
			}else
			if (this.getStrafingMove() > 0.0F) {
				move_mode = 4;
			}else
			if (this.getForwardMove() > 0.0F) {
				move_mode = 1;
			}else
			if (this.getForwardMove() < 0.0F) {
				move_mode = 2;
			}
		}
		if(move_mode>0){
			//if(moveAnim<20)++moveAnim;
			moveAnim=20;
		}else{
			//if(moveAnim>0)--moveAnim;
			moveAnim=0;
		}
	}
	
	public void AIWeapon1(double w, double h, double z, double bx, double bz){
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = Mth.sqrt((float)((z - bz)* (z - bz) + (w - bx)*(w - bx))) * Mth.sin(-this.getXRot()  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * z;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * z;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1.57F) * w;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1.57F) * w;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		EntityShell bullet = new EntityShell(this.level(), shooter);
		bullet.power = 15;
		bullet.setExLevel(1);
		bullet.setModel("advancearmy:textures/entity/bullet/bullet30mm.obj");
		bullet.setTex("advancearmy:textures/entity/bullet/bullet.png");
		bullet.setGravity(0.01F);
		bullet.moveTo(this.getX() + xx11, this.getY()+h+base, this.getZ() + zz11, this.yHeadRot, this.getXRot());
		bullet.shootFromRotation(this, this.turretPitch, this.turretYaw, 0.0F, 6F, 2);
		if (!this.level().isClientSide) this.level().addFreshEntity(bullet);
	}
	
	public void AIWeapon2(double w, double h, double z, double bx, double bz){
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = Mth.sqrt((float)((z - bz)* (z - bz) + (w - bx)*(w - bx))) * Mth.sin(-this.getXRot()  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * z;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * z;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1.57F) * w;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1.57F) * w;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		EntityShell bullet = new EntityShell(this.level(), shooter);
		bullet.power = 100;
		bullet.setGravity(0.01F);
		bullet.moveTo(this.getX() + xx11, this.getY()+h+base, this.getZ() + zz11, this.yHeadRot, this.getXRot());
		bullet.shootFromRotation(this, this.turretPitch, this.turretYaw, 0.0F, 6F, 2);
		bullet.setExLevel(3);
		bullet.setModel("wmlib:textures/entity/flare.obj");
		bullet.setTex("wmlib:textures/entity/flare.png");
		//bullet.setBulletType(9);
		bullet.flame=true;
		if (!this.level().isClientSide) this.level().addFreshEntity(bullet);
	}
	
	public Vec2 getLockVector() {
	  return new Vec2(this.turretPitch, this.turretYaw);
	}
	
	public void AIWeapon3(float w, float h, float z, float by, float bz){
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = Mth.sqrt((z - bz)* (z - bz) + (h - by)*(h - by)) * Mth.sin(-this.turretPitch  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * z;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * z;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1) * w;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1) * w;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		Vec3 locken = Vec3.directionFromRotation(this.getLockVector());//getLookAngle
		float d = 120;
		LivingEntity lockTarget = null;
		int range = 2;
		double ix = 0;
		double iy = 0;
		double iz = 0;
		boolean stop = false;
		for(int xxx = 0; xxx < 120; ++xxx) {
			ix = (this.getX()+xx11 + locken.x * xxx);
			iy = (this.getY()+h+base + locken.y * xxx);
			iz = (this.getZ()+zz11 + locken.z * xxx);
			BlockPos blockpos = new BlockPos((int)ix, (int)iy, (int)iz);
			BlockState iblockstate = this.level().getBlockState(blockpos);
			if (!iblockstate.isAir()&& !iblockstate.getCollisionShape(this.level(), blockpos).isEmpty()){
				break;
			}else{
				AABB axisalignedbb = (new AABB(ix-range, iy-range, iz-range, 
						ix+range, iy+range, iz+range)).inflate(1D);
				List<Entity> llist = this.level().getEntities(this,axisalignedbb);
				if (llist != null) {
					for (int lj = 0; lj < llist.size(); lj++) {
						Entity entity1 = (Entity) llist.get(lj);
						if (entity1 != null && entity1 instanceof LivingEntity) {
							if (NotFriend(entity1) && entity1 != shooter && entity1 != this) {
								lockTarget = (LivingEntity)entity1;
								if(lockTarget.getVehicle()!=null){
									Entity ve = lockTarget.getVehicle();
									ve.invulnerableTime = 0;
									ve.hurt(this.damageSources().thrown(this, shooter), 100);
									//ve.setHealth(lockTarget.getHealth()-lockTarget.getMaxHealth()*0.04F);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(this.damageSources().thrown(this, shooter), 100);
									lockTarget.setHealth(lockTarget.getHealth()-lockTarget.getMaxHealth()*0.04F);
								}
								ix=lockTarget.getX();
								iy=lockTarget.getY();
								iz=lockTarget.getZ();
								stop=true;
								break;
							}
						}
					}
				}
				if(stop)break;
			}
		}
		MessageTrail messageBulletTrail = new MessageTrail(true,7,"advancearmy:textures/entity/flash/ember_beam" ,this.getX()+xx11, this.getY()+h+base, this.getZ()+zz11, this.getDeltaMovement().x, this.getDeltaMovement().z, ix, iy+0.5D, iz, 25F, 1);
		PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 100, this.level().dimension())), messageBulletTrail);
		WMExplosionBase.createExplosionDamage(this, ix, iy+1.5D, iz, 20, 3, false,  false);
	}
}
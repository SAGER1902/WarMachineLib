package advancearmy.entity;

import java.util.List;

import advancearmy.AdvanceArmy;

import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.scores.Team;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.EntityDataAccessor;  
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.network.PacketDistributor;
import wmlib.common.network.message.MessageFirstTarget;
import wmlib.common.network.PacketHandler;
import wmlib.common.living.EntityWMVehicleBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.AgeableMob;
import java.util.UUID;

public abstract class EntitySA_SoldierBase extends TamableAnimal{
	public EntitySA_SoldierBase(EntityType<? extends EntitySA_SoldierBase> sodier, Level worldIn) {
		super(sodier, worldIn);
	}

	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return 1.8F;
	}
	
	public double getMountedYOffset() {
		return 0.6D;//0.12D
	}
	
	protected void registerGoals() {
	}
	
	public Team getTeam() {
	  if (this.getOwner()!=null) {
		 LivingEntity livingentity = this.getOwner();
		 if (livingentity != null) {
			return livingentity.getTeam();
		 }
	  }
	  return super.getTeam();
	}
	public void checkDespawn() {
	}
	
	public void setWeapon(int stack) {
	}
	
	public EntitySA_SoldierBase getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
	  return null;
	}
	
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource damageSource) {
		if(this.getVehicle()!=null){
			return false;
		}else{
			return true;
		}
	}

	private static final EntityDataAccessor<Integer> tc = SynchedEntityData.<Integer>defineId(EntitySA_SoldierBase.class, EntityDataSerializers.INT);
	
    private static final EntityDataAccessor<Integer> remain1 = 
    		SynchedEntityData.<Integer>defineId(EntitySA_SoldierBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> remain2 = 
    		SynchedEntityData.<Integer>defineId(EntitySA_SoldierBase.class, EntityDataSerializers.INT);
	/*private static final EntityDataAccessor<Boolean> isattack = 
    		SynchedEntityData.<Boolean>defineId(EntitySA_SoldierBase.class, EntityDataSerializers.BOOLEAN);*/
	private static final EntityDataAccessor<Boolean> choose = 
    		SynchedEntityData.<Boolean>defineId(EntitySA_SoldierBase.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> movetype = SynchedEntityData.<Integer>defineId(EntitySA_SoldierBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> movex = SynchedEntityData.<Integer>defineId(EntitySA_SoldierBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> movey = SynchedEntityData.<Integer>defineId(EntitySA_SoldierBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> movez = SynchedEntityData.<Integer>defineId(EntitySA_SoldierBase.class, EntityDataSerializers.INT);

	public int cooltime;
	public int cooltime2;
	public int reload1 = 0;
	public int reload_time1;
	
	public float turretYaw;
	public float turretPitch;
	public float rotation_max = 60;
	public float turretPitchMax = -90;
	public float turretPitchMin = 90;
	
	public float attack_range_max = 35;
	public float attack_range_min = 0;
	public float attack_height_max = 40;
	public float attack_height_min = -20;
	public boolean is_aa = false;
	
	public int magazine = 5;
	public boolean counter1 = false;
	public boolean counter2 = false;

	public int countlimit1 = 0;
	public boolean firetrue = false;
	
	public LivingEntity firstTarget=null;
	public LivingEntity clientTarget=null;
	
	public int startTime = 0;
	public float rote =0;
	public float yaw =0;
	public float throttle;//
	public float throttleRight;//
	public float throttleLeft;//
	public float thpera;
	public boolean sneak_aim = false;//
	public int aim_time = 0;
	public int anim1 = 0;
	public int anim2 = 0;
	public float cooltime5 = 0;
	public float cooltime6 = 0;
	public int find_time = 0;	 
	int clear_time = 0;
	public void tick() {
		super.tick();
		if(this.firstTarget!=null){
			if(this.firstTarget.getHealth()<=0){
				this.firstTarget=null;
			}else{
				this.setTarget(this.firstTarget);
			}
		}
		
		if(this.getChoose()){
			if(this.firstTarget!=null){
				if(this.clientTarget==null){
					if(this != null && !this.level().isClientSide)PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageFirstTarget(this.getId(), this.firstTarget.getId()));
				}
			}else{
				if(this.clientTarget!=null){
					if(this != null && !this.level().isClientSide)PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageFirstTarget(this.getId(), -1));
				}
			}
		}
		if(this.isAttacking()){
			if(aim_time<100)++aim_time;
		}else{
			if(aim_time>0)--aim_time;
		}
		
		if(this.anim1<25)++this.anim1;
		if(this.anim2<25)++this.anim2;
	}
    public boolean CanAttack(Entity entity){
    	return false;
    }
	
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		{
			compound.putInt("movetype", this.getMoveType());
			compound.putInt("movex", this.getMovePosX());
			compound.putInt("movey", this.getMovePosY());
			compound.putInt("movez", this.getMovePosZ());
			compound.putInt("remain2", this.getRemain2());
			compound.putInt("remain1", this.getRemain1());
			//compound.putBoolean("isattack", this.isAttacking());
			compound.putBoolean("choose", this.getChoose());
			compound.putInt("tc", getTeamC());
		}
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
		{
			this.setMoveType(compound.getInt("movetype"));
			this.setMovePosX(compound.getInt("movex"));
			this.setMovePosY(compound.getInt("movey"));
			this.setMovePosZ(compound.getInt("movez"));
			this.setRemain2(compound.getInt("remain2"));
			this.setRemain1(compound.getInt("remain1"));
			//this.setAttacking(compound.getBoolean("isattack"));
			this.setChoose(compound.getBoolean("choose"));
			this.setTeamC(compound.getInt("tc"));
		}
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(movetype, Integer.valueOf(0));
		this.entityData.define(movex, Integer.valueOf(0));
		this.entityData.define(movey, Integer.valueOf(0));
		this.entityData.define(movez, Integer.valueOf(0));
		this.entityData.define(remain1, Integer.valueOf(0));
		this.entityData.define(remain2, Integer.valueOf(0));
		//this.entityData.define(isattack, Boolean.valueOf(false));
		this.entityData.define(choose, Boolean.valueOf(false));
		this.entityData.define(tc, Integer.valueOf(0));
	}
	
	public int getTeamC() {
		return ((this.entityData.get(tc)).intValue());
	}
	public void setTeamC(int stack) {
		this.entityData.set(tc, Integer.valueOf(stack));
	}
	
	public int getMoveType() {
			return ((this.entityData.get(movetype)).intValue());
	}
	public void setMoveType(int stack) {
		this.entityData.set(movetype, Integer.valueOf(stack));
	}
	public int getMovePosX() {
		return ((this.entityData.get(movex)).intValue());
	}
	public void setMovePosX(int stack) {
	this.entityData.set(movex, Integer.valueOf(stack));
	}
	public int getMovePosY() {
	return ((this.entityData.get(movey)).intValue());
	}
	public void setMovePosY(int stack) {
	this.entityData.set(movey, Integer.valueOf(stack));
	}
	public int getMovePosZ() {
	return ((this.entityData.get(movez)).intValue());
	}
	public void setMovePosZ(int stack) {
	this.entityData.set(movez, Integer.valueOf(stack));
	}
	public int getRemain2() {
		return ((this.entityData.get(remain2)).intValue());
	}
	public void setRemain2(int stack) {
		this.entityData.set(remain2, Integer.valueOf(stack));
	}
	public int getRemain1() {
		return ((this.entityData.get(remain1)).intValue());
	}
	public void setRemain1(int stack) {
		this.entityData.set(remain1, Integer.valueOf(stack));
	}
	public boolean isAttacking() {
		//return ((this.entityData.get(isattack)).booleanValue());
		return this.isAggressive();
	}
	public void setAttacking(boolean stack) {
		//this.entityData.set(isattack, Boolean.valueOf(stack));
        this.setAggressive(stack);
	}
	public boolean getChoose() {
		return ((this.entityData.get(choose)).booleanValue());
	}
	public void setChoose(boolean stack) {
		this.entityData.set(choose, Boolean.valueOf(stack));
	}
}
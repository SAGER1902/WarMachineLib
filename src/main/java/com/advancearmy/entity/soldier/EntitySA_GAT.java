package advancearmy.entity.soldier;

import java.util.List;

import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
import advancearmy.init.ModEntities;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

import net.minecraft.network.chat.Component;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Team;
import net.minecraft.network.protocol.Packet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;  
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;

import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.TamableAnimal;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import java.util.function.Predicate;
import wmlib.common.living.EntityWMVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import wmlib.common.living.ai.LivingLockGoal;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import wmlib.common.living.EntityWMSeat;
import advancearmy.entity.EntitySA_SquadBase;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.ai.SoldierSearchTargetGoalSA;
import wmlib.common.living.WeaponVehicleBase;
import net.minecraftforge.fml.ModList;
import wmlib.api.IArmy;
import advancearmy.entity.EntitySA_LandBase;

import advancearmy.util.TargetSelect;
public class EntitySA_GAT extends EntitySA_SquadBase{
	public EntitySA_GAT(EntityType<? extends EntitySA_GAT> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.unittex = ResourceLocation.tryParse("advancearmy:textures/item/item_spawn_minigunner.png");
		this.attack_height_max = 110;
		this.is_aa = true;
	}
	public EntitySA_GAT(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_GAT.get(), worldIn);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(6, new LivingLockGoal(this, 1.0D, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new SoldierSearchTargetGoalSA<>(this, Mob.class, 10, true, (attackentity) -> {
			{
				return this.CanAttack(attackentity);
			}
		}));
	}
	
    protected SoundEvent getAmbientSound()
    {
        return SASoundEvent.gt_say.get();
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SASoundEvent.apa_hurt.get();
    }

    protected SoundEvent getDeathSound()
    {
        return SASoundEvent.apa_die.get();
    }
	
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(this.getSame())par2 = par2*0.5F;
		if(entity != null){
			if(entity instanceof LivingEntity){
				LivingEntity entity1 = (LivingEntity) entity;
				boolean flag = this.getSensing().hasLineOfSight(entity1);
				if(this.getOwner()==entity||this.getVehicle()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null||this.getTeam()==null && entity.getTeam()==null && entity instanceof EntitySA_SquadBase){
					return false;
				}else{
					if(entity instanceof TamableAnimal){
						TamableAnimal soldier = (TamableAnimal)entity;
						if(this.getOwner()!=null && this.getOwner()==soldier.getOwner()){
							return false;
						}else{
							if(this.distanceTo(entity1)>8D && flag){
								if(this.groundtime>50){
									this.setRemain2(3);
									this.setTarget(entity1);
								}
							}
							if(this.getRemain2()==1)par2 = par2*0.4F;//
							if(this.sit_aim)par2 = par2*0.8F;//
							return super.hurt(source, par2);
						}
					}else{
						if(this.distanceToSqr(entity)>4D && flag){
							if(this.groundtime>50){
								this.setRemain2(3);
								this.setTarget(entity1);
								this.groundtime = 0;
							}
						}
						if(this.getRemain2()==1)par2 = par2*0.4F;//
						if(this.sit_aim)par2 = par2*0.8F;//
						return super.hurt(source, par2);
					}
				}
			}else{
				if(this.getRemain2()==1)par2 = par2*0.4F;//
				if(this.sit_aim)par2 = par2*0.8F;//
				return super.hurt(source, par2);
			}
		}else {
			if(this.getRemain2()==1)par2 = par2*0.4F;//
			if(this.sit_aim)par2 = par2*0.8F;//
			return super.hurt(source, par2);
		}
    }
	
    private static final EntityDataAccessor<Boolean> same = 
    		SynchedEntityData.<Boolean>defineId(EntitySA_GAT.class, EntityDataSerializers.BOOLEAN);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		{
			compound.putBoolean("same", getSame());
		}
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
		{
			this.setSame(compound.getBoolean("same"));
		}
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(same, Boolean.valueOf(false));
	}
	public boolean getSame() {
		return ((this.entityData.get(same)).booleanValue());
	}
	public void setSame(boolean stack) {
		this.entityData.set(same, Boolean.valueOf(stack));
	}
	
	public float defend = 0;
	public boolean isSame(Entity ent){
		if(ent instanceof EntitySA_GAT && this.getOwner()!=null && this.getOwner()==((EntitySA_GAT)ent).getOwner()
			||(this.getTeam()!=null && this.getTeam()==ent.getTeam())){
			return true;
		}else{
			return false;
		}
	}
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
		int count=0;
		List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().inflate(18D, 18.0D, 18D));
		for (Entity living : entities) {
			if(living!=null && this.isSame(living))++count;
		}
		if(count>2){
			this.setSame(true);
		}else{
			this.setSame(false);
		}
		float moveSpeed = 0.4F;
		if(this.getRemain2()==1){
			moveSpeed = 0.1F;
			if(this.getBbHeight()!=0.5F)this.setSize(0.5F, 0.5F);
			this.height = 0.5F;
		}else if(this.sit_aim){
			moveSpeed = 0.05F;
			if(this.getBbHeight()!=0.8F)this.setSize(0.5F, 0.8F);
			this.height = 1.2F;
		}else{
			if(this.getRemain2()==2){
				if(this.getBbHeight()!=1.6F)this.setSize(0.5F, 1.7F);
			}else{
				if(this.getBbHeight()!=1.8F)this.setSize(0.5F, 1.8F);
			}
			this.height = 1.8F;
			moveSpeed = 0.2F;
		}
		if(this.getRemain2()==3){
			this.groundtime = 0;
		}
		if(groundtime<200)++groundtime;
		if(this.groundtime>5 && this.groundtime<8){
			this.ground_time = 0;
		}
		if(this.ground_time<60)++ground_time;
		
		if(this.ground_time<50){
			this.setRemain2(1);
			moveSpeed = 0.02F;
		}else{
			if(this.getRemain2()==1)this.setRemain2(0);
			moveSpeed = 0.2F;
		}
		
		{
			this.attack_range_max = 45;
			this.needaim = false;
			this.magazine = 300;
			this.w1cycle = 2;
			this.reload_time1 = 60;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.fire_gat.get(), 0.3F,1.1F,2.5F,0,0,
			7, 7F, 2F, 0, false, 1, 0.01F, 20, 0);
		}
		float movesp = moveSpeed;
		if(this.move_type==5 && this.getMoveType()!=2)movesp = moveSpeed*1.5F;
		if(this.isInWater()|| this.isInLava())movesp = moveSpeed*3F;
		this.moveway(this, movesp, this.attack_range_max);
		boolean isAttackVehicle = false;
		if(this.getTarget()!=null && this.isAttacking() && (this.getVehicle()==null||this.canfire)){
			if(movecool>99){
				this.move_type=this.level().random.nextInt(6);
				movecool = 0;
			}
			if(this.getMoveType()==3 && this.move_type!=0)this.move_type=0;
			LivingEntity livingentity = this.getTarget();
			if(livingentity.getMaxHealth()>this.getMaxHealth()||livingentity.getVehicle()!=null||livingentity instanceof EntityWMVehicleBase)isAttackVehicle = true;
			if(this.isAttacking() && this.find_time<40 && this.getRemain1()>0){
				++this.find_time;
			}
			if(this.getMoveType()!=3){
				if(this.level().random.nextInt(6) >= 3 && this.find_time > 20){
					this.setRemain2(2);
					this.find_time = 0;
				}else if(this.level().random.nextInt(6) < 3 && this.find_time > 20){
					this.setRemain2(0);
				}
			}

			if(livingentity.isAlive() && livingentity!=null && (this.aim_time>15)){
				if(this.cooltime >= this.ammo1 && this.cooltime > 2){
					this.counter1 = true;
					this.cooltime = 0;
				}
				if(this.counter1 && this.guncyle >= this.w1cycle && this.getRemain1() > 0){
					this.setAnimFire(1);
					float side = 1.57F;
					if(this.weaponcross){
						if(this.getRemain1()%2==0){
							side = -1.57F;
						}else{
							side = 1.57F;
						}
					}
					double px = this.getX();
					double py = this.getY();
					double pz = this.getZ();
					AI_EntityWeapon.Attacktask(this, this, this.getTarget(), this.bulletid, this.bulletmodel1, this.bullettex1, this.firefx1, this.bulletfx1, this.firesound1,side, this.fireposX,this.fireposY,this.fireposZ,this.firebaseX,this.firebaseZ,px, py, pz,this.getYRot(), this.getXRot(),this.bulletdamage, this.bulletspeed, this.bulletspread, this.bulletexp, this.bulletdestroy, this.bulletcount, this.bulletgravity, this.bullettime, this.bullettype);
					this.setRemain1(this.getRemain1() - 1);
					this.guncyle = 0;
					this.gun_count1 = 0;
					++this.countlimit1;
					if(this.countlimit1>(1+this.level().random.nextInt(8))){
						this.counter1 = false;
						this.countlimit1 = 0;
					}
				}
			}
		}
		}
	}
}
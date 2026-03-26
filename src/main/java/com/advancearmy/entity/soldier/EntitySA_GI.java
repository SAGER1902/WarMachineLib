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
import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import java.util.function.Predicate;

import net.minecraftforge.fml.ModList;
import net.minecraft.world.entity.TamableAnimal;

import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.ai.LivingLockGoal;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import advancearmy.entity.EntitySA_SquadBase;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.ai.SoldierSearchTargetGoalSA;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import wmlib.common.living.EntityWMVehicleBase;
public class EntitySA_GI extends EntitySA_SquadBase{
	public EntitySA_GI(EntityType<? extends EntitySA_GI> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.unittex = ResourceLocation.tryParse("advancearmy:textures/item/item_spawn_gi.png");
	}
	public EntitySA_GI(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_GI.get(), worldIn);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_GI.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 35.0D)
					.add(Attributes.ARMOR, (double) 4D);
	}
	
    protected SoundEvent getAmbientSound()
    {
        return SASoundEvent.gi_say.get();
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SASoundEvent.gi_hurt.get();
    }

    protected SoundEvent getDeathSound()
    {
        return SASoundEvent.gi_die.get();
    }
	
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
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
	
	public float defend = 0;
	
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
		this.weaponidmax = 2;
		if(cheack){
			if(this.getWeaponId()==0)this.setWeaponId(1+this.level().random.nextInt(this.weaponidmax));
			cheack = false;
		}
		float bure = 1.0F;
		if(this.getMoveType()==3&&!this.isPassenger()){
			if(this.getNavigation()!=null)this.getNavigation().stop();
			if(this.defend<1){
				this.defend+=0.1F;
			}else{
				if(this.getRemain2()!=0)this.setRemain2(0);
				this.sit_aim = true;
			}
		}else{
			if(this.defend>0){
				this.defend-=0.1F;
			}else{
				this.sit_aim = false;
			}
		}
		
		if(this.getWeaponId()==1){//m9
			this.needaim = false;
			this.changeWeaponId=2;
			this.mainWeaponId=2;
			this.magazine = 30;
			this.fire_tick = 20;
			this.w1cycle = 3;
			this.reload_time1 = 40;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.fire_para.get(), 0.5F,1.5F,1F,0,0,
			4, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==2){//dsr80
			if(this.sit_aim){
				bure = 0.6F;
				this.attack_range_max = 45;
			}else{
				this.attack_range_max = 40;
			}
			this.needaim = false;
			this.magazine = 200;
			this.fire_tick = 20;
			this.changeWeaponId=2;
			this.mainWeaponId=2;
			this.w1cycle = 2;
			this.reload_time1 = 60;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.fire_dsr80.get(), 0.5F,1.5F,1F,0,0,
			6, 6F, 3F*bure, 0, false, 1, 0.01F, 20, 0);
		}
		this.attack_height_max = this.attack_range_max;
		
		float moveSpeed = 0.18F;
		if(this.getRemain2()==1){
			moveSpeed = 0.1F;
			if(this.getBbHeight()!=0.5F)this.setSize(0.5F, 0.5F);
			this.height = 0.5F;
		}else if(this.sit_aim){
			moveSpeed = 0F;
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
		
		if(!this.sit_aim){
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
		}
		
		float movesp = moveSpeed;
		if(this.move_type==5 && this.getMoveType()!=2)movesp = moveSpeed*1.5F;
		this.moveway(this, movesp, this.attack_range_max);
		boolean isAttackVehicle = false;
		if(this.getTarget()!=null && this.isAttacking() && (this.getVehicle()==null||this.canfire)){
			if(movecool>99){
				this.move_type=this.level().random.nextInt(6);
				movecool = 0;
			}
			if(this.move_type>3&&this.getMoveType()==1)this.setMoveType(3);
			if(this.getMoveType()==3 && this.move_type!=0)this.move_type=0;
			LivingEntity livingentity = this.getTarget();
			if(livingentity.getMaxHealth()>this.getMaxHealth()||livingentity.getVehicle()!=null||livingentity instanceof EntityWMVehicleBase)isAttackVehicle = true;
			if(this.isAttacking() && this.find_time<40 && this.getRemain1()>0){
				++this.find_time;
			}
			if(this.getMoveType()!=3){
				if(this.level().random.nextInt(6) >= 3 && this.find_time > 20){
					if((this.mainWeaponId!=this.getWeaponId()||!isAttackVehicle) && this.changeWeaponId!=0 && this.aim_time>60)this.setWeaponId(this.mainWeaponId);//main
					this.setRemain2(2);
					if(this.getRemain1()>this.magazine)this.setRemain1(this.magazine);
					this.find_time = 0;
				}else if(this.level().random.nextInt(6) < 3 && this.find_time > 20){
					if(this.changeWeaponId!=0 && !isAttackVehicle&&this.getRemain1()>0 && this.aim_time>60){
						if(this.soldierType!=2||this.soldierType==2&&this.distanceTo(livingentity)<this.attack_range_max*0.2F)this.setWeaponId(this.changeWeaponId);
					}
					this.setRemain2(0);
					if(this.getRemain1()>this.magazine)this.setRemain1(this.magazine);
					this.find_time = 0;
				}
			}else{
				if(this.mainWeaponId!=this.getWeaponId())this.setWeaponId(this.mainWeaponId);//main
			}

			if(livingentity.isAlive() && livingentity!=null && (this.aim_time>50||!this.needaim && this.aim_time>30)){
				if(this.cooltime >= this.ammo1 && this.cooltime > this.fire_tick){
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
					if(this.countlimit1>(1+this.level().random.nextInt(8))||this.needaim){
						this.counter1 = false;
						this.countlimit1 = 0;
					}
				}
			}
		}
		}
	}
}
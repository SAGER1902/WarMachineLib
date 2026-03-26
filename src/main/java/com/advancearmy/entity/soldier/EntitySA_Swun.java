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
import advancearmy.entity.EntitySA_SquadBase;
import advancearmy.entity.EntitySA_Seat;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import wmlib.common.living.EntityWMVehicleBase;
import safx.SagerFX;
public class EntitySA_Swun extends EntitySA_SquadBase{
	public EntitySA_Swun(EntityType<? extends EntitySA_Swun> sodier, Level worldIn) {
		super(sodier, worldIn);
		canDrop = true;
		//this.unittex = ResourceLocation.tryParse("advancearmy:textures/item/item_spawn_swun.png");
	}
	public EntitySA_Swun(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_SWUN.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Swun.createMobAttributes().add(Attributes.MAX_HEALTH, 80.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 35.0D)
					.add(Attributes.ARMOR, (double) 12D);
    }
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource damageSource) {
		if(ModList.get().isLoaded("safx") && this.fallDistance>10)SagerFX.proxy.createFX("DropRing", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F);
	  return false;
	}
	
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
		this.weaponidmax = 10;
		if(cheack){
			if(this.getWeaponId()==0)this.setWeaponId(1+this.level().random.nextInt(this.weaponidmax));
			cheack = false;
		}
		if(this.getWeaponId()==1||this.getWeaponId()==6||this.getWeaponId()==7){//gun4
			this.needaim = false;
			this.magazine = 30;
			this.fire_tick = 35;
			this.changeWeaponId=0;
			this.mainWeaponId=0;
			this.w1cycle = 4;
			this.reload_time1 = 40;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet4.obj", "advancearmy:textures/entity/bullet/bullet4_y.png",
			"SmokeGun", null, SASoundEvent.gun1.get(), 0.5F,1.5F,1F,0,0,
			8, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==3){//gun1
			this.soldierType=2;
			this.attack_range_max = 55;
			this.needaim = true;
			this.changeWeaponId=4;
			this.mainWeaponId=3;
			this.magazine = 5;
			this.fire_tick = 35;
			this.w1cycle = 20;
			this.reload_time1 = 100;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet4.obj", "advancearmy:textures/entity/bullet/bullet4_y.png",
			"SmokeGun", "YellowBulletTrail", SASoundEvent.gun3.get(), 0.5F,1.5F,1F,0,0,
			40, 5F, 1.2F, 0, false, 1, 0.01F, 40, 0);
		}
		if(this.getWeaponId()==4){//gun2
			this.needaim = false;
			this.changeWeaponId=3;
			this.mainWeaponId=3;
			this.magazine = 10;
			this.fire_tick = 20;
			this.w1cycle = 3;
			this.reload_time1 = 40;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet4.obj", "advancearmy:textures/entity/bullet/bullet4_y.png",
			"SmokeGun", null, SASoundEvent.gun8.get(), 0.5F,1.5F,1F,0,0,
			7, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==5){//gun10
			this.attack_range_max = 40;
			this.needaim = false;
			this.magazine = 80;
			this.fire_tick = 20;
			this.changeWeaponId=0;
			this.mainWeaponId=0;
			this.w1cycle = 2;
			this.reload_time1 = 60;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet4.obj", "advancearmy:textures/entity/bullet/bullet4_y.png",
			"SmokeGun", null, SASoundEvent.gun4.get(), 0.5F,1.5F,1F,0,0,
			8, 6F, 3F, 0, false, 1, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==2){//sr1
			this.needaim = true;
			this.attack_height_max = 80;
			this.changeWeaponId=8;
			this.mainWeaponId=2;
			this.magazine = 2;
			this.fire_tick = 35;
			this.w1cycle = 15;
			this.reload_time1 = 150;
			this.setWeapon(0, 3, "advancearmy:textures/entity/bullet/daodan4.obj", "advancearmy:textures/entity/bullet/daodan4.png",
			"SmokeGun", "gun5FuheTrail", SASoundEvent.rocket1.get(), 0.5F,1.5F,1F,0,0,
			100, 3F, 2F, 3, false, 1, 0.01F, 40, 0);
		}
		if(this.getWeaponId()==8){//gun3
			this.attack_range_max = 35;
			this.needaim = false;
			this.changeWeaponId=2;
			this.mainWeaponId=2;
			this.magazine = 30;
			this.fire_tick = 35;
			this.w1cycle = 8;
			this.reload_time1 = 40;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet4.obj", "advancearmy:textures/entity/bullet/bullet4_y.png",
			"SmokeGun", null, SASoundEvent.gun2.get(), 0.5F,1.5F,1F,0,0,
			6, 5F, 4F, 0, false, 8, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==9){//sci
			this.needaim = false;
			this.changeWeaponId=10;
			this.mainWeaponId=10;
			this.magazine = 30;
			this.fire_tick = 20;
			this.w1cycle = 3;
			this.reload_time1 = 40;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet4.obj", "advancearmy:textures/entity/bullet/bullet4_y.png",
			"SmokeGun", null, SASoundEvent.gun1.get(), 0.5F,1.5F,1F,0,0,
			7, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==10){//sr5
			this.attack_range_max=45;
			this.needaim = true;
			this.changeWeaponId=9;
			this.mainWeaponId=10;
			this.magazine = 2;
			this.fire_tick = 35;
			this.w1cycle = 15;
			this.reload_time1 = 150;
			this.setWeapon(0, 4, "advancearmy:textures/entity/bullet/daodan4.obj", "advancearmy:textures/entity/bullet/daodan4.png",
			"SmokeGun", "gun5FuheTrail", SASoundEvent.rocket1.get(), 0.5F,1.5F,1F,0,0,
			150, 3F, 2F, 2, false, 1, 0.01F, 40, 0);
		}
		this.attack_height_max = this.attack_range_max;
		
		float moveSpeed = 0.5F;
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
		
		float movesp = moveSpeed;
		if(this.move_type==5 && this.getMoveType()!=2)movesp = moveSpeed*1.5F;
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
			if(this.level().random.nextInt(6) >= 3 && this.find_time > 20){
				if((this.mainWeaponId!=this.getWeaponId()||!isAttackVehicle) && this.changeWeaponId!=0 && this.aim_time>30)this.setWeaponId(this.mainWeaponId);//main
				this.setRemain2(2);
				if(this.getRemain1()>this.magazine)this.setRemain1(this.magazine);
				this.find_time = 0;
			}else if(this.level().random.nextInt(6) < 3 && this.find_time > 20){
				if(this.changeWeaponId!=0 && !isAttackVehicle&&this.getRemain1()>0 && this.aim_time>30){
					if(this.soldierType!=2||this.soldierType==2&&this.distanceTo(livingentity)<this.attack_range_max*0.2F)this.setWeaponId(this.changeWeaponId);
				}
				this.setRemain2(0);
				if(this.getRemain1()>this.magazine)this.setRemain1(this.magazine);
				this.find_time = 0;
			}
			
			if(livingentity.isAlive() && livingentity!=null && (this.aim_time>15||!this.needaim)){
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
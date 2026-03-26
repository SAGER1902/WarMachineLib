package advancearmy.entity.soldier;

import java.util.List;

import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import advancearmy.event.SASoundEvent;
import advancearmy.init.ModEntities;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

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

import wmlib.util.ThrowBullet;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import java.util.function.Predicate;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.EntityWMVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import wmlib.common.living.ai.LivingLockGoal;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import advancearmy.entity.EntitySA_SquadBase;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.ai.SoldierSearchTargetGoalSA;
import wmlib.common.living.WeaponVehicleBase;
import net.minecraftforge.fml.ModList;
import wmlib.api.IArmy;
import advancearmy.entity.EntitySA_LandBase;
import wmlib.common.bullet.EntityGrenade;

import javax.annotation.Nullable;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import advancearmy.util.SummonEntity;
import advancearmy.util.GunDatas;
public class EntitySA_Soldier extends EntitySA_SquadBase{
	public EntitySA_Soldier(EntityType<? extends EntitySA_Soldier> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.unittex = ResourceLocation.tryParse("advancearmy:textures/item/item_spawn_soldier.png");
	}
	public EntitySA_Soldier(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_SOLDIER.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Soldier.createMobAttributes()
		.add(Attributes.MAX_HEALTH, 40.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 35.0D)
					.add(Attributes.ARMOR, (double) 4D);
    }
	/*private void dropItemStack(ItemStack item) {
	  ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), item);
	  this.level().addFreshEntity(itementity);
	}*/
	int summonTime = 0;
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance diff, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
		data = super.finalizeSpawn(level, diff, reason, data, nbt);
		this.setWeaponId(-1);
		return data;
	}
	public void startSummon(){
		if (this.level().random.nextInt(3) == 1)SummonEntity.wildSummon(level(), this.getX(), this.getY(), this.getZ() + 5, 31, true, this.getTeam(), 3);
		if (this.level().random.nextInt(4) == 1)SummonEntity.wildSummon(level(), this.getX(), this.getY(), this.getZ() + 5, 31, true, this.getTeam(), 5);
		int summonType = -1;
		float rand = this.level().random.nextFloat(); // 生成一个[0, 1)的随机浮点数
		if (rand < 0.02f) {          // 原 1/50
			summonType = 5;
		} else if (rand < 0.05f) {   // 0.02 + 0.03 (原1/35)
			summonType = 4;
		} else if (rand < 0.10f) {   // 0.05 + 0.05 (原1/20)
			summonType = 3;
		} else if (rand < 0.20f) {   // 0.10 + 0.10 (原1/10)
			summonType = 2;
		} else if (rand < 0.4667f) { // 0.20 + 0.2667 (原1/3.75≈0.2667)
			summonType = 1;
		} else if (rand < 0.80f) {   // 0.4667 + 0.3333 (原1/3≈0.3333)
			summonType = 0;
		}
		if (summonType > -1) {
			int nearbyAllies = 0;
			boolean stop = false;
			List<Entity> nearbyEntities = level().getEntities(this, this.getBoundingBox().inflate(60.0, 80.0, 60.0));
			for (Entity entity : nearbyEntities) {
				if (entity instanceof WeaponVehicleBase && ((WeaponVehicleBase) entity).getTargetType() == 3) {
					nearbyAllies++;
					if (nearbyAllies > 5) {
						stop=true;
						break;
					}
				}
			}
			if(!stop){
				double offsetX = this.level().random.nextDouble() * 6.0 - 3.0; // -3到+3
				double offsetZ = this.level().random.nextDouble() * 6.0 - 3.0;
				switch (summonType) {
					case 0:
						SummonEntity.wildSummon(level(), this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 24, true, this.getTeam(), 1);
						SummonEntity.wildSummon(level(), this.getX(), this.getY(), this.getZ() + 5, 31, true, this.getTeam(), 3);
						break;
					case 1:
						SummonEntity.wildSummon(level(), this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 25, true, this.getTeam(), 1);
						break;
					case 2:
						SummonEntity.wildSummon(level(), this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 1, true, this.getTeam(), 1);
						break;
					case 3:
						SummonEntity.wildSummon(level(), this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 19, true, this.getTeam(), 1);
						break;
					case 4:
						SummonEntity.wildSummon(level(), this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 9, true, this.getTeam(), 1);
						break;
					case 5:
						SummonEntity.wildSummon(level(), this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 2, true, this.getTeam(), 1);
						break;
				}
			}
		}
	}
	/*public float roteXanim = 0;
	public float roteYanim = 0;
	public float roteZanim = 0;
	public float posXanim = 0;
	public float posYanim = 0;
	public float posZanim = 0;*/
	
    protected SoundEvent getAmbientSound()
    {
        return SASoundEvent.ran_say.get();
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SASoundEvent.ran_hurt.get();
    }

    protected SoundEvent getDeathSound()
    {
        return SASoundEvent.ran_die.get();
    }
	
	public void AIWeapon(double w, double h, double z, float bure, float speed, LivingEntity target){
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= Mth.sin(this.getYRot() * 0.01745329252F) * z;
		zz11 += Mth.cos(this.getYRot() * 0.01745329252F) * z;
		xx11 -= Mth.sin(this.getYRot() * 0.01745329252F + 1) * w;
		zz11 += Mth.cos(this.getYRot() * 0.01745329252F + 1) * w;
		EntityBullet bullet = new EntityBullet(this.level(), this);
		float targetpitch = this.getXRot();
		if(this.getTarget()!=null){
			double[] angles = new double[2];
			boolean flag = ThrowBullet.canReachTarget(speed, 0.06F, 0.99,
					(int) this.getX(), (int) this.getEyeY(), (int) this.getZ(),
					(int) target.getX(), (int) target.getEyeY(), (int) target.getZ(),
					angles, true);
			if (flag) {
				targetpitch = (float)-angles[1];
			}
		}
		bullet.power = 5;
		bullet.timemax = 500;
		bullet.setExLevel(2);
		bullet.setModel("advancearmy:textures/entity/bullet/grenade.obj");
		bullet.setTex("advancearmy:textures/entity/bullet/grenade.png");
		bullet.setFX("SAGrenadeTrail");
		bullet.setGravity(0.06F);
		bullet.moveTo(this.getX() + xx11, this.getY()+h, this.getZ() + zz11, this.getYRot(), targetpitch);
		bullet.shootFromRotation(this, targetpitch, this.getYRot(), 0.0F, speed, bure);
		if (!this.level().isClientSide) this.level().addFreshEntity(bullet);
	}
	
	public void specialAttack(double w, double h, double z, float bure, float speed, LivingEntity target){
		this.playSound(SoundEvents.EGG_THROW, 4.0F, 1.0F);
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= Mth.sin(this.getYRot() * 0.01745329252F) * z;
		zz11 += Mth.cos(this.getYRot() * 0.01745329252F) * z;
		xx11 -= Mth.sin(this.getYRot() * 0.01745329252F + 1) * w;
		zz11 += Mth.cos(this.getYRot() * 0.01745329252F + 1) * w;
		EntityGrenade bullet = new EntityGrenade(this.level(), this);
		bullet.selfExpSound=SASoundEvent.exp_m67.get();
		float targetpitch = this.getXRot();
		if(this.getTarget()!=null){
			double[] angles = new double[2];
			boolean flag = ThrowBullet.canReachTarget(speed, 0.06F, 0.99,
					(int) this.getX(), (int) this.getEyeY(), (int) this.getZ(),
					(int) target.getX(), (int) target.getEyeY(), (int) target.getZ(),
					angles, true);
			if (flag) {
				targetpitch = (float)-angles[1];
			}
		}
		if(this.getWeaponId()==1||this.getWeaponId()==11||this.getWeaponId()==12){
			bullet.power = 200;
			bullet.timemax = 80;
			bullet.setExLevel(6);
			bullet.setModel("advancearmy:textures/gun/cfour.obj");
			bullet.setTex("advancearmy:textures/gun/gun.png");
		}else{
			bullet.power = 10;
			bullet.timemax = 80;
			bullet.setExLevel(5);
			bullet.setModel("advancearmy:textures/gun/tool.obj");
			bullet.setTex("advancearmy:textures/gun/gun.png");
		}
		bullet.setGravity(0.06F);
		bullet.moveTo(this.getX() + xx11, this.getY()+h, this.getZ() + zz11, this.getYRot(), targetpitch);
		bullet.shootFromRotation(this, targetpitch, this.getYRot(), 0.0F, speed, bure);
		if (!this.level().isClientSide) this.level().addFreshEntity(bullet);
	}
	
	public GunDatas useGunData;
	
	boolean wp2true = false;
	int wp2time = 0;
	public void tick() {
		super.tick();
		if(this.getHealth()<=0)return;
		if(summonTime<20)++summonTime;
		if(this.getWeaponId()==-1){
			if(summonTime>15){
				startSummon();
				this.setWeaponId(0);
			}
		}else{
			this.weaponidmax = 14;
			if(cheack){
				if(this.getWeaponId()==0)this.setWeaponId(1+this.level().random.nextInt(this.weaponidmax));
				cheack = false;
			}
		}

		if(this.getWeaponId()==1||this.getWeaponId()==11||this.getWeaponId()==12){//m16a4
			specialReload=500;
			this.wp2true = true;
			this.needaim = false;
			this.magazine = 30;
			this.fire_tick = 35;
			this.changeWeaponId=0;
			this.mainWeaponId=0;
			this.w1cycle = 4;
			this.reload_time1 = 60;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.fire_ran.get(), 0.5F,1.5F,1F,0,0,
			6, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
		}/*else{
			specialReload=350;
		}*/
		if(this.getWeaponId()==2){//smaw
			this.soldierType=1;
			this.needaim = true;
			this.changeWeaponId=14;
			this.mainWeaponId=2;
			this.magazine = 1;
			this.fire_tick = 35;
			this.w1cycle = 15;
			this.reload_time1 = 150;
			this.setWeapon(0, 3, "advancearmy:textures/entity/bullet/smawrocket.obj", "advancearmy:textures/gun/gun.png",
			"SmokeGun", "RocketTrail", SASoundEvent.fire_smaw.get(), 0.5F,1.5F,1F,0,0,
			70, 3F, 2F, 2, false, 1, 0.01F, 40, 0);
		}
		if(this.getWeaponId()==3){//l96
			this.soldierType=2;
			this.attack_range_max = 50;
			this.needaim = true;
			this.changeWeaponId=4;
			this.mainWeaponId=3;
			this.magazine = 5;
			this.fire_tick = 35;
			this.w1cycle = 20;
			this.reload_time1 = 100;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.l96a1_fire.get(), 0.5F,1.5F,1F,0,0,
			30, 5F, 1.2F, 0, false, 1, 0.01F, 40, 0);
		}
		if(this.getWeaponId()==4||this.getWeaponId()==13){//m9
			this.soldierType=2;
			this.needaim = false;
			if(this.getWeaponId()==4){
				this.changeWeaponId=3;
				this.mainWeaponId=3;
			}else{
				this.changeWeaponId=6;
				this.mainWeaponId=6;
			}
			this.magazine = 10;
			this.fire_tick = 20;
			this.w1cycle = 3;
			this.reload_time1 = 40;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.fire_92fs.get(), 0.5F,1.5F,1F,0,0,
			4, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
			/*this.useGunData.chooseWeapon(GunDatas.Guns.PISTOL);
			this.fireposZ = this.useGunData.fireposZ;*/
		}
		if(this.getWeaponId()==5){//m60
			this.soldierType=3;
			this.attack_range_max = 40;
			this.needaim = false;
			this.magazine = 100;
			this.fire_tick = 20;
			this.changeWeaponId=0;
			this.mainWeaponId=0;
			this.w1cycle = 2;
			this.reload_time1 = 60;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.m249_fire.get(), 0.5F,1.5F,1F,0,0,
			6, 6F, 3F, 0, false, 1, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==6){//m24
			this.soldierType=2;
			this.attack_range_max = 50;
			this.needaim = true;
			this.changeWeaponId=4;
			this.mainWeaponId=6;
			this.magazine = 5;
			this.fire_tick = 35;
			this.w1cycle = 20;
			this.reload_time1 = 60;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.m24_fire.get(), 0.5F,1.5F,1F,0,0,
			28, 6F, 1.2F, 0, false, 1, 0.01F, 40, 0);
		}
		if(this.getWeaponId()==7){//at4
			this.soldierType=4;
			this.needaim = true;
			this.changeWeaponId=7;
			this.mainWeaponId=8;
			this.magazine = 1;
			this.fire_tick = 35;
			this.w1cycle = 15;
			this.reload_time1 = 150;
			this.setWeapon(0, 3, "advancearmy:textures/entity/bullet/bulletrocket.obj", "advancearmy:textures/entity/bullet/bulletrocket.png",
			"SmokeGun", "RocketTrail", SASoundEvent.fire_smaw.get(), 0.5F,1.5F,1F,0,0,
			65, 3F, 2F, 2, false, 1, 0.01F, 40, 0);
		}
		if(this.getWeaponId()==8){//m16a2
			this.soldierType=4;
			this.needaim = false;
			this.changeWeaponId=7;
			this.mainWeaponId=8;
			this.magazine = 30;
			this.fire_tick = 35;
			this.w1cycle = 4;
			this.reload_time1 = 40;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.m16_fire.get(), 0.5F,1.5F,1F,0,0,
			5, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==14){//shotgun
			this.soldierType=1;
			this.attack_range_max = 25;
			this.needaim = false;
			this.changeWeaponId=2;
			this.mainWeaponId=2;
			this.magazine = 8;
			this.fire_tick = 35;
			this.w1cycle = 8;
			this.reload_time1 = 40;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.remington_fire.get(), 0.5F,1.5F,1F,0,0,
			4, 5F, 4F, 0, false, 8, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==9){//mp5
			this.soldierType=1;
			this.needaim = false;
			this.changeWeaponId=10;
			this.mainWeaponId=10;
			this.magazine = 30;
			this.fire_tick = 20;
			this.w1cycle = 3;
			this.reload_time1 = 40;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.mp5_fire.get(), 0.5F,1.5F,1F,0,0,
			5, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==10){//javelin
			this.soldierType=1;
			this.attack_range_max=40;
			this.needaim = true;
			this.changeWeaponId=9;
			this.mainWeaponId=10;
			this.magazine = 1;
			this.fire_tick = 35;
			this.w1cycle = 15;
			this.reload_time1 = 150;
			this.setWeapon(0, 4, "advancearmy:textures/entity/bullet/javelinmissile.obj", "advancearmy:textures/entity/bullet/rocket152mm.png",
			"SmokeGun", "SAMissileSmoke", SASoundEvent.fire_javelin.get(), 0.5F,1.5F,1F,0,0,
			130, 3F, 2F, 2, false, 1, 0.01F, 100, 1);
		}
		float moveSpeed = 0.2F;
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
			if(this.soldierType==1&& this.getRemain1()<=0 && this.getWeaponId()==this.mainWeaponId)this.setWeaponId(this.changeWeaponId);
			if(this.soldierType==4&& this.getRemain1()<=0 && this.getWeaponId()!=this.mainWeaponId)this.setWeaponId(this.mainWeaponId);
			if(livingentity.isAlive() && livingentity!=null && (this.aim_time>50||!this.needaim && this.aim_time>30)){
				if(this.wp2true)++wp2time;
				if(wp2time>100){
					AIWeapon(0,this.height,0.8F, 2.5F, 2.5F, livingentity);
					this.wp2time = 0;
					this.playSound(SASoundEvent.fire_grenade.get(), 4.0F, 1.0F);
				}
				
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
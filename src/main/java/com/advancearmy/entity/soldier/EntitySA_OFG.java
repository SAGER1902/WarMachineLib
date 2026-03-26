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
import wmlib.common.living.ai.LivingLockGoal;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import advancearmy.entity.EntitySA_SquadBase;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.ai.SoldierSearchTargetGoalSA;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import wmlib.common.living.EntityWMVehicleBase;

import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageTrail;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import advancearmy.entity.map.ParticlePoint;
import safx.SagerFX;
public class EntitySA_OFG extends EntitySA_SquadBase{
	public EntitySA_OFG(EntityType<? extends EntitySA_OFG> sodier, Level worldIn) {
		super(sodier, worldIn);
		canDrop = true;
		this.fireposX=0.5F;
		this.fireposZ=2F;
		this.unittex = ResourceLocation.tryParse("advancearmy:textures/item/item_spawn_ofg.png");
	}
	public EntitySA_OFG(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_OFG.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_OFG.createMobAttributes().add(Attributes.MAX_HEALTH, 70.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 55.0D)
					.add(Attributes.ARMOR, (double) 14D);
    }
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource damageSource) {
		if(ModList.get().isLoaded("safx") && this.fallDistance>10)SagerFX.proxy.createFX("DropRing", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F);
	  return false;
	}
	
	/*int summonTime = 0;
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance diff, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
		data = super.finalizeSpawn(level, diff, reason, data, nbt);
		ParticlePoint bullet = new ParticlePoint(ModEntities.ENTITY_P.get(), this.level());
		bullet.moveTo(this.getX(), this.getY(), this.getZ(), 0, 0);
		level.addFreshEntity(bullet);
		bullet.setPType(2);
		bullet.setSTime(40);
		this.playSound(SASoundEvent.csk.get(), 3.0F, 1.0F);
		return data;
	}*/
	
	protected void tickDeath() {
		int maxLife = 50;
		if (deathTime < maxLife) {
			float progress = (float) deathTime / maxLife;
			// 原有的透明度逻辑（前40%增加，后60%减少）
			if (progress < 0.4f) {
				particleAlpha = progress / 0.4f;
			} else {
				particleAlpha = 1.0f - (progress - 0.4f) / 0.6f;
			}
			// 修正后的粒子大小逻辑
			if (progress < 0.25f) {
				// 前25%：0 -> 1
				size = progress / 0.25f;
			} else if (progress < 0.7f) {
				//particleSize = 1.0f;
			} else {
				// 70% ~ 100%：1 -> 0
				size = 1.0f - (progress - 0.7f) / 0.3f;
			}
			particleSize = size*0.2F;
			++this.deathTime;
		}
		if (this.deathTime == 5){
			this.playSound(SASoundEvent.csk.get(), 3.0F, 1.0F);
		}
		if(this.deathTime >= maxLife){
			this.discard();
		}
	}
	
	public boolean NotFriend(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F){//Living
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = this.getTeam();
			Team team1 = entity1.getTeam();
			if(team != null && team1 != team && team1 != null){
				return true;
			}else if(entity instanceof Enemy && ((LivingEntity) entity).getHealth() > 0.0F && (team == null||team != team1)){
				return true;
			}else{
				return false;
			}
    	}else{
			return false;
		}
	}

	LivingEntity lockTarget = null;
	public void weaponActive1(){
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = Mth.sqrt((this.fireposZ - this.firebaseZ)* (this.fireposZ - this.firebaseZ) + (this.fireposX - 0)*(this.fireposX - 0)) * Mth.sin(-this.getXRot()  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.yHeadRot * 0.01745329252F) * this.fireposZ;
		zz11 += Mth.cos(this.yHeadRot * 0.01745329252F) * this.fireposZ;
		xx11 -= Mth.sin(this.yHeadRot * 0.01745329252F + 1.57F) * fireposX;
		zz11 += Mth.cos(this.yHeadRot * 0.01745329252F + 1.57F) * fireposX;
		LivingEntity shooter = this;
		{
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("PrismGun", null, this.getX()+xx11, this.getY()+this.fireposY+base, this.getZ()+zz11, 0, 0, 0, 1);
		}
		Vec3 locken = Vec3.directionFromRotation(this.getRotationVector());
		float d = 120;
		int range = 1;
		int ix = 0;
		int iy = 0;
		int iz = 0;
		boolean stop = false;
		int pierce = 0;
		for(int xxx = 0; xxx < 120; ++xxx) {
			ix = (int) (this.getX()+xx11 + locken.x * xxx);
			iy = (int) (this.getY()+this.fireposY+base + locken.y * xxx);
			iz = (int) (this.getZ()+zz11 + locken.z * xxx);
			BlockPos blockpos = new BlockPos(ix, iy, iz);
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
									ve.hurt(this.damageSources().inFire(), 8);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(this.damageSources().inFire(), 8);
								}
								stop = true;
								ix=(int)lockTarget.getX();
								iy=(int)lockTarget.getY();
								iz=(int)lockTarget.getZ();
								break;
							}
						}
					}
				}
				if(stop)break;
			}
		}
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("PrismHit", null, ix, iy+1.5D, iz, 0, 0, 0, 1);
		MessageTrail messageBulletTrail = new MessageTrail(true, 2, "advancearmy:textures/entity/flash/reguang_beam" ,this.getX()+xx11, this.getY()+this.fireposY-1.5F+base, this.getZ()+zz11, this.getDeltaMovement().x, this.getDeltaMovement().z, ix, iy, iz, 15F, 1);
		PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messageBulletTrail);
	}
	
	
	public int time = 0;
	public float particleSize = 0;
	public float particleAlpha = 0;
	float size;
	
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
		this.weaponidmax = 4;
		if(cheack){
			if(this.getWeaponId()==0)this.setWeaponId(1+this.level().random.nextInt(this.weaponidmax));
			cheack = false;
		}
		if(this.getWeaponId()==1||this.getWeaponId()==4){//gun4
			this.needaim = false;
			this.magazine = 30;
			this.fire_tick = 35;
			this.changeWeaponId=0;
			this.mainWeaponId=0;
			this.w1cycle = 4;
			this.reload_time1 = 40;
		}
		if(this.getWeaponId()==2){//sci
			this.needaim = false;
			this.changeWeaponId=3;
			this.mainWeaponId=3;
			this.magazine = 30;
			this.fire_tick = 20;
			this.w1cycle = 3;
			this.reload_time1 = 40;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet4.obj", "advancearmy:textures/entity/bullet/bullet4_y.png",
			"SmokeGun", null, SASoundEvent.gun1.get(), 0.5F,1.5F,1F,0,0,
			7, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
		}
		if(this.getWeaponId()==3){//m
			this.attack_range_max=55;
			this.needaim = true;
			this.changeWeaponId=2;
			this.mainWeaponId=3;
			this.magazine = 30;
			this.fire_tick = 35;
			this.w1cycle = 25;
			this.reload_time1 = 150;
			this.setWeapon(0, 4, "advancearmy:textures/entity/bullet/teshu4_s2.obj", "advancearmy:textures/entity/bullet/bullet4_y.png",//fireflash3_4
			null, "maopaoTrail", SASoundEvent.maopao.get(), 0.5F,1.5F,1F,0,0,
			60, 2F, 2F, 2, false, 1, 0.01F, 400, 0);
		}
		this.attack_height_max = this.attack_range_max;
		
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
					if(this.getWeaponId()==1||this.getWeaponId()==4){
						this.weaponActive1();
						this.playSound(SASoundEvent.laser16.get(),3,1);
					}else{
						AI_EntityWeapon.Attacktask(this, this, this.getTarget(), this.bulletid, this.bulletmodel1, this.bullettex1, this.firefx1, this.bulletfx1, this.firesound1,side, this.fireposX,this.fireposY,this.fireposZ,this.firebaseX,this.firebaseZ,px, py, pz,this.getYRot(), this.getXRot(),this.bulletdamage, this.bulletspeed, this.bulletspread, this.bulletexp, this.bulletdestroy, this.bulletcount, this.bulletgravity, this.bullettime, this.bullettype);
					}
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
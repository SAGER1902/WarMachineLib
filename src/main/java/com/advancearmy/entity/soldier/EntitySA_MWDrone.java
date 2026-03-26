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

import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageTrail;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.world.level.block.state.BlockState;
import wmlib.common.bullet.EntityRad;
import wmlib.init.WMModEntities;
import wmlib.api.IRadSoldier;
import net.minecraft.world.level.levelgen.Heightmap;
import safx.SagerFX;
import wmlib.common.world.WMExplosionBase;
public class EntitySA_MWDrone extends EntitySA_SquadBase implements IRadSoldier{
	public EntitySA_MWDrone(EntityType<? extends EntitySA_MWDrone> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.fireposX=0.66F;
		this.fireposY=1.83F;
		this.fireposZ=0.02F;
		this.attack_height_max = this.attack_range_max=50;
		this.unittex = ResourceLocation.tryParse("advancearmy:textures/item/item_spawn_mwd.png");
		canVehicle=false;
		straightMove=true;
		canFloat=true;
		this.needaim = false;
		this.magazine = 4;
		this.fire_tick = 5;
		this.reload_time1 = 6;
		this.is_aa = true;
		this.attack_height_max = 110;
		this.setMaxUpStep(1.5F);
	}
	public EntitySA_MWDrone(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_MWD.get(), worldIn);
	}
	
	protected void registerGoals() {
		//this.goalSelector.addGoal(1, new FloatGoal(this));
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
	
	//@Override
	public void setMove(int id, int x, int y, int z){
		this.setMoveType(id);
		this.setMovePosX(x);
		this.setMovePosY(y);
		this.setMovePosZ(z);
		if(id==3){
			if(this.getNavigation()!=null)this.getNavigation().stop();
		}
	}
	
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime >= 1){
			this.playSound(SASoundEvent.wreck_explosion.get(), 3F, 1.0F);
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3F, false, Level.ExplosionInteraction.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("VehicleExp1", null, this.getX(), this.getY(), this.getZ(), 0,0,0,0.8F);
			this.discard();
		}
	}
	
	int block_height;
	public float defend = 0;
	int radtime = 0;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			block_height = 1+this.level().getHeight(Heightmap.Types.WORLD_SURFACE,(int)this.getX(),(int)this.getZ());
			if(this.getY()<block_height){
				Vec3 vector3d = this.getDeltaMovement();
				this.setDeltaMovement(vector3d.x, 0.1D, vector3d.z);
			}
			float movesp = 0.5F;
			this.moveway(this, movesp, this.attack_range_max);
			if(this.isInWater()|| this.isInLava())movesp = 1.5F;
			if(this.getTarget()!=null && this.isAttacking() && (this.getVehicle()==null||this.canfire)){
				LivingEntity livingentity = this.getTarget();
				if(livingentity.isAlive() && livingentity!=null && (this.aim_time>20)){
					{
						if(this.cooltime > this.fire_tick){
							this.counter1 = true;
							this.cooltime = 0;
						}
						if(this.counter1 && this.getRemain1() > 0){
							this.setAnimFire(1);
							{
								this.playSound(SASoundEvent.mwdronef.get(),3,1);
								weaponActive1();
							}
							this.setRemain1(this.getRemain1() - 1);
							this.guncyle = 0;
							this.gun_count1 = 0;
							this.counter1 = false;
						}		
					}
				}
			}
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
		float fireX = this.fireposX;
		if(this.getRemain1()%2==0){
			fireX = this.fireposX;
		}else{
			fireX = -this.fireposX;
		}
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = Mth.sqrt((this.fireposZ - this.firebaseZ)* (this.fireposZ - this.firebaseZ) + (fireX - 0)*(fireX - 0)) * Mth.sin(-this.getXRot()  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.yHeadRot * 0.01745329252F) * this.fireposZ;
		zz11 += Mth.cos(this.yHeadRot * 0.01745329252F) * this.fireposZ;
		xx11 -= Mth.sin(this.yHeadRot * 0.01745329252F + 1.57F) * fireX;
		zz11 += Mth.cos(this.yHeadRot * 0.01745329252F + 1.57F) * fireX;
		LivingEntity shooter = this;
		//if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("RadGun", null, this.getX()+xx11, this.getY()+this.fireposY+base, this.getZ()+zz11, 0, 0, 0, 1);
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
									ve.hurt(this.damageSources().thrown(this, shooter), 40);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(this.damageSources().thrown(this, shooter), 40);
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
		//WMExplosionBase.createExplosionDamage(this, ix, iy+1.5D, iz,10, 2, false,  false);
		MessageTrail messageBulletTrail = new MessageTrail(true, 2, "advancearmy:textures/entity/flash/reguang_beam" ,this.getX()+xx11, this.getY()+this.fireposY-1.5F+base, this.getZ()+zz11, this.getDeltaMovement().x, this.getDeltaMovement().z, ix, iy, iz, 15F, 1);
		PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messageBulletTrail);
	}
}
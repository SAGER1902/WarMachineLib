package advancearmy.entity.mob;

import java.util.Collection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
//import net.minecraft.world.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
//import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Mob;
import advancearmy.AdvanceArmy;

import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.fml.ModList;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import advancearmy.entity.ai.CreeperSwellGoalSA;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;

import wmlib.api.ITool;

import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import advancearmy.init.ModEntities;
import wmlib.api.IEnemy;
import net.minecraft.world.Difficulty;
import advancearmy.util.TargetSelect;

import net.minecraft.world.entity.monster.Monster;
public class ERO_Creeper extends Monster implements IEnemy, PowerableMob {
   private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(ERO_Creeper.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(ERO_Creeper.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(ERO_Creeper.class, EntityDataSerializers.BOOLEAN);
   private int oldSwell;
   private int swell;
   private int maxSwell = 30;
   private int explosionRadius = 3;
   private int droppedSkulls;

	public ERO_Creeper(EntityType<? extends ERO_Creeper> p_i50213_1_, Level p_i50213_2_) {
	  super(p_i50213_1_, p_i50213_2_);
	  this.xpReward = 2;
	}
	
   public void updateSwingTime(){
	   
   }
	
	public void checkDespawn(){
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }
	}
	
	public MobType getMobType() {
	  return MobType.UNDEAD;
	}
	public ERO_Creeper(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_CREEPER.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return ERO_Creeper.createMobAttributes();
    }
	
   	public float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
   protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new CreeperSwellGoalSA(this));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoalSA(this, 0.8D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 10F, false, false, (attackentity) -> {
			return this.CanAttack(attackentity);
		}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, Player.class, 10, 10F, false, false, (attackentity) -> {
			return true;
		}));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
   }
   
	public boolean hurt(DamageSource source, float par2)
    {
		Entity entity = source.getEntity();
		if(entity != null){
			if(entity instanceof IEnemy){
				return false;
			}
		}
		if(source.is(DamageTypes.EXPLOSION)){
			this.setHealth(this.getHealth()+par2*0.5F);
			return false;
		}
		return super.hurt(source, par2);
	}
	
    public boolean CanAttack(Entity entity){
		return TargetSelect.mobCanAttack(this,entity,this.getTarget());
    }

   public int getMaxFallDistance() {
      return this.getTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1);
   }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        boolean $$3 = super.causeFallDamage(fallDistance, multiplier, source);
        this.swell += (int)(fallDistance * 1.5f);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }
        return $$3;
    }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(DATA_SWELL_DIR, -1);
      this.entityData.define(DATA_IS_POWERED, false);
      this.entityData.define(DATA_IS_IGNITED, false);
   }

   public void addAdditionalSaveData(CompoundTag p_213281_1_) {
      super.addAdditionalSaveData(p_213281_1_);
      if (this.entityData.get(DATA_IS_POWERED)) {
         p_213281_1_.putBoolean("powered", true);
      }

      p_213281_1_.putShort("Fuse", (short)this.maxSwell);
      p_213281_1_.putByte("ExplosionRadius", (byte)this.explosionRadius);
      p_213281_1_.putBoolean("ignited", this.isIgnited());
   }

   public void readAdditionalSaveData(CompoundTag p_70037_1_) {
      super.readAdditionalSaveData(p_70037_1_);
      this.entityData.set(DATA_IS_POWERED, p_70037_1_.getBoolean("powered"));
      if (p_70037_1_.contains("Fuse", 99)) {
         this.maxSwell = p_70037_1_.getShort("Fuse");
      }

      if (p_70037_1_.contains("ExplosionRadius", 99)) {
         this.explosionRadius = p_70037_1_.getByte("ExplosionRadius");
      }

      if (p_70037_1_.getBoolean("ignited")) {
         this.ignite();
      }

   }

	public float cooltime6 = 0;
	public Vec3 motions = this.getDeltaMovement();
   public void tick() {
      if (this.isAlive()) {
         this.oldSwell = this.swell;
         /*if (this.isIgnited()) {
            this.setSwellDir(1);
         }*/

		if(this.getHealth()<this.getMaxHealth() && this.cooltime6>45){
			this.setHealth(this.getHealth()+1);
			this.cooltime6 = 0;
		}
		if(cooltime6<50)++cooltime6;
		float sp = 0.20F;//
		this.moveway(this, sp, 25, 25);//
		
         int i = this.getSwellDir();
         if (i > 0 && this.swell == 0) {
            this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
         }

         this.swell += i;
         if (this.swell < 0) {
            this.swell = 0;
         }

         if (this.swell >= this.maxSwell) {
			//if(cooltime < 200)++cooltime;
            this.explodeCreeper();
			this.swell =0/* = this.maxSwell*/;
			setSwellDir(-1);
         }
      }

      super.tick();
   }

	public float face = 0;
	public void moveway(ERO_Creeper entity, float sp, double max, double range1) {
		boolean ta = false;
		{//
			if (entity.getTarget() != null) {
				if (!entity.getTarget().isInvisible()) {//target
					if (entity.getTarget().getHealth() > 0.0F) {
						double d5 = entity.getTarget().getX() - entity.getX();
						double d7 = entity.getTarget().getZ() - entity.getZ();
						double d6 = entity.getTarget().getY() - entity.getY();
						double d1 = entity.getEyeY() - (entity.getTarget().getEyeY());
						double d3 = (double) Math.sqrt(d5 * d5 + d7 * d7);
						float f11 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
						double ddx = Math.abs(d5);
						double ddz = Math.abs(d7);
						float f12 = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
						{
							if ((ddx>2F||ddz>2F)) {//
								{
									MoveS(entity, sp, 1, entity.getTarget().getX(), entity.getTarget().getY(), entity.getTarget().getZ(), (LivingEntity)entity.getTarget());
								}
							}else if((ddx < 2 || ddz < 2)){//
								MoveS(entity, sp, 1, entity.getTarget().getX(), entity.getTarget().getY(), entity.getTarget().getZ(), (LivingEntity)entity.getTarget());
							}
						}
						entity.setYRot(f12);
						entity.setYHeadRot(f12);//
						entity.setXRot(-f11);//
					}
				}
			}
		}
	}
	
	public void MoveS(ERO_Creeper entity, double speed, double han, double ex, double ey, double ez, LivingEntity en){
		if(!entity.level().isClientSide)
		{
			double d5 = ex - entity.getX();
			double d7 = ez - entity.getZ();
			float yawoffset = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
			float yaw = yawoffset * (2 * (float) Math.PI / 360);
			double mox = 0;
			double moy = -1D;
			double moz = 0;
			//entity.stepHeight = entity.height * 0.8F;
			
			if (entity.distanceToSqr(en) < 4) {//8 * 8
					mox -= Math.sin(yaw) * speed * -1;
					moz += Math.cos(yaw) * speed * -1;
					entity.setDeltaMovement(mox, moy, moz);
			}else{
				{
					mox -= Math.sin(yaw) * speed * 0.5F;
					moz += Math.cos(yaw) * speed * 0.5F;
				}
			}
			
			boolean flag = true;
			//Vec3 vector3d1 = this.getDeltaMovement().scale(0.75D);
			if(flag){
				{
					entity.getNavigation().moveTo(ex, ey, ez, 1.6);
					//entity.move(MoverType.PLAYER, entity.getDeltaMovement());
				}
			}
		}
	}
   
   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.CREEPER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.CREEPER_DEATH;
   }

   protected void dropCustomDeathLoot(DamageSource p_213333_1_, int p_213333_2_, boolean p_213333_3_) {
      super.dropCustomDeathLoot(p_213333_1_, p_213333_2_, p_213333_3_);
      Entity entity = p_213333_1_.getEntity();
      if (entity != this && entity instanceof ERO_Creeper) {
         ERO_Creeper ERO_Creeper = (ERO_Creeper)entity;
         if (ERO_Creeper.canDropMobsSkull()) {
            ERO_Creeper.increaseDroppedSkulls();
            this.spawnAtLocation(Items.CREEPER_HEAD);
         }
      }

   }

   public boolean doHurtTarget(Entity p_70652_1_) {
      return true;
   }

   public boolean isPowered() {
      return this.entityData.get(DATA_IS_POWERED);
   }

   @OnlyIn(Dist.CLIENT)
   public float getSwelling(float p_70831_1_) {
      return Mth.lerp(p_70831_1_, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
   }

   public int getSwellDir() {
      return this.entityData.get(DATA_SWELL_DIR);
   }

   public void setSwellDir(int p_70829_1_) {
      this.entityData.set(DATA_SWELL_DIR, p_70829_1_);
   }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        super.thunderHit(level, lightning);
        this.entityData.set(DATA_IS_POWERED, true);
    }

   private void explodeCreeper() {
      if (!this.level().isClientSide) {
         float f = this.isPowered() ? 5.0F : 1.0F;
		 if(this.getHealth()<=2F){
			this.dead = true;
			this.discard();
		 }
		 /*if(cooltime>20)*/{
			//cooltime = 0;
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, Level.ExplosionInteraction.NONE);
			//this.spawnLingeringCloud();
		 }
      }
   }

   public boolean isIgnited() {
      return this.entityData.get(DATA_IS_IGNITED);
   }

   public void ignite() {
      this.entityData.set(DATA_IS_IGNITED, true);
   }

   public boolean canDropMobsSkull() {
      return this.isPowered() && this.droppedSkulls < 1;
   }

   public void increaseDroppedSkulls() {
      ++this.droppedSkulls;
   }
}

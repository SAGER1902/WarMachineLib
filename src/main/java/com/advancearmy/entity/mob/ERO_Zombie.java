package advancearmy.entity.mob;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;


import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Mob;

import advancearmy.AdvanceArmy;


import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;

import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import net.minecraft.world.item.ArmorItem;
//import com.mrcrayfish.guns.item.GunItem;

import net.minecraftforge.fml.ModList;
import net.minecraft.world.entity.PathfinderMob;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import advancearmy.entity.EntitySA_LandBase;
import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;
import advancearmy.entity.ai.ZombieAttackGoalSA;
import advancearmy.init.ModEntities;
import wmlib.api.ITool;

import net.minecraft.world.entity.monster.Monster;
import advancearmy.util.TargetSelect;
public class ERO_Zombie extends Monster implements IEnemy{
	private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = (p_213697_0_) -> {
	  return true;
	};
	private final BreakDoorGoal breakDoorGoal = new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE);
	private final OpenDoorGoal opDoorGoal = new OpenDoorGoal(this, true);
	private boolean canBreakDoors;
	private boolean canSummon=false;
	public ERO_Zombie(EntityType<? extends ERO_Zombie> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.xpReward = 2;
	}
	
	public static AttributeSupplier.Builder createAttributes() {
        return ERO_Zombie.createMobAttributes();
    }
	
	public void checkDespawn(){
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }
	}
	public ERO_Zombie(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_EZOMBIE.get(), worldIn);
		//((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
	}
	public float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
	protected void registerGoals() {
	  this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
	  this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	  this.addBehaviourGoals();
	}

	protected void addBehaviourGoals() {
		this.goalSelector.addGoal(2, new ZombieAttackGoalSA(this, 1.0D, false));
		this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::canBreakDoors));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoalSA(this, 1.0D));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 10F, false, false, (attackentity) -> {
			return this.CanAttack(attackentity);
		}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, Player.class, 10, 10F, false, false, (attackentity) -> {
			return true;
		}));
	}

    public boolean CanAttack(Entity entity){
		return TargetSelect.mobCanAttack(this,entity,this.getTarget());
    }

	public boolean canBreakDoors() {
	  return this.canBreakDoors;
	}

	public void setCanBreakDoors(boolean p_146070_1_) {
	  if (this.supportsBreakDoorGoal() && GoalUtils.hasGroundPathNavigation(this)) {
		 if (this.canBreakDoors != p_146070_1_) {
			this.canBreakDoors = p_146070_1_;
			((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(p_146070_1_);
			if (p_146070_1_) {
				this.goalSelector.addGoal(1, this.opDoorGoal);
			   this.goalSelector.addGoal(1, this.breakDoorGoal);
			} else {
			   this.goalSelector.removeGoal(this.breakDoorGoal);
			   this.goalSelector.removeGoal(this.opDoorGoal);
			}
		 }
	  } else if (this.canBreakDoors) {
		 this.goalSelector.removeGoal(this.breakDoorGoal);
		 this.canBreakDoors = false;
	  }
	}

	protected boolean supportsBreakDoorGoal() {
	  return true;
	}

	public void onSyncedDataUpdated(EntityDataAccessor<?> p_184206_1_) {
	  super.onSyncedDataUpdated(p_184206_1_);
	}
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if(entity instanceof IEnemy){
				return false;
			}else{
				return super.hurt(source, par2);
			}
		}else{
			return super.hurt(source, par2);
		}
	}
	public float move_type = 0;
	public float movecool = 0;
	public float summontime = 0;
	public float cooltime6 = 0;
	public Vec3 motions = this.getDeltaMovement();
   public void aiStep() {
      if (this.isAlive()) {
		if(this.getHealth()<this.getMaxHealth() && this.cooltime6>45){
			this.setHealth(this.getHealth()+1);
			this.cooltime6 = 0;
		}
		if(cooltime6<50)++cooltime6;
		
		if(movecool<100)++movecool;
		this.updateSwingTime();

		if (!(this.level() instanceof ServerLevel)) {
			//return false;
		} else {
		 ServerLevel serverworld = (ServerLevel)this.level();
		 LivingEntity livingentity = this.getTarget();
		 if (livingentity == null && this.getTarget() instanceof LivingEntity) {
			livingentity = (LivingEntity)this.getTarget();
		 }
			if(movecool>99){
				this.move_type=this.level().random.nextInt(3);
				movecool = 0;
			}
			int i = Mth.floor(this.getX());
			int j = Mth.floor(this.getY());
			int k = Mth.floor(this.getZ());
			if(summontime<100)++summontime;
			if(summontime>99 && canSummon){
				int count = 0;
				int husk = 0;
				int ve = 0;
				List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(40D, 50.0D, 40D));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity friend = list.get(k2);
					if(friend instanceof ERO_Zombie && ((LivingEntity)friend).getHealth()>0){
						++count;
					}
					if(friend instanceof ERO_Husk && ((LivingEntity)friend).getHealth()>0){
						++husk;
					}
					if(friend instanceof EntitySA_T72 && ((LivingEntity)friend).getHealth()>0){
						++ve;
					}
					if(friend instanceof EntitySA_Plane1 && ((LivingEntity)friend).getHealth()>0){
						++ve;
					}
					if(friend instanceof EntitySA_Plane2 && ((LivingEntity)friend).getHealth()>0){
						++ve;
					}
				}
				if(count<25){
					if(summontime>50+count*50){
						for(int l = 0; l < 50; ++l) {
							int i1 = i + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
							int j1 = j + Mth.nextInt(this.random, 1, 3);
							int k1 = k + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
							BlockPos blockpos = new BlockPos(i1, j1, k1);
							EntityType<?> entitytype = ModEntities.ENTITY_EHUSK.get();
							ERO_Husk army = new ERO_Husk(ModEntities.ENTITY_EHUSK.get(), serverworld);
							SpawnPlacements.Type entityspawnplacementregistry$placementtype = SpawnPlacements.getPlacementType(entitytype);
							if (NaturalSpawner.isSpawnPositionOk(entityspawnplacementregistry$placementtype, this.level(), blockpos, entitytype)) {
								if (!this.level().hasNearbyAlivePlayer((double)i1, (double)j1, (double)k1, 7.0D) && this.level().isUnobstructed(army) && this.level().noCollision(army) && !this.level().containsAnyLiquid(army.getBoundingBox())) {
									army.setPos(i1, j1, k1);
									if(this.level().random.nextInt(5)==1){
										army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
									}else if(this.level().random.nextInt(5)==2){
										army.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
									}else if(this.level().random.nextInt(5)==2){
										army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
										army.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
									}
									if (livingentity != null)army.setTarget(livingentity);
									army.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(army.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
									serverworld.addFreshEntity(army);
									if(this.level().random.nextInt(4)==1 && count<25){
										ERO_Phantom pha = new ERO_Phantom(ModEntities.ENTITY_PHA.get(), serverworld);
										pha.setPos((double)i1, (double)j+30, (double)k1);
										pha.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(pha.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
										serverworld.addFreshEntity(pha);//addFreshEntity
										if (livingentity != null)pha.setTarget(livingentity);
									}
									if(count>10){
										if(this.level().random.nextInt(8)==1 && ve<1){
											EntitySA_T72 pha = new EntitySA_T72(ModEntities.ENTITY_T72.get(), serverworld);
											pha.setTargetType(2);
											pha.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(pha);
											if (livingentity != null)pha.setTarget(livingentity);
											{
												ERO_REB reb = new ERO_REB(ModEntities.ENTITY_REB.get(), serverworld);
												reb.setPos((double)i1, (double)j1, (double)k1);
												reb.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(reb.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
												serverworld.addFreshEntity(reb);
												pha.catchPassenger(reb);
											}
										}
										if(this.level().random.nextInt(7)==1 && ve<4){
											EntitySA_Plane1 pha = new EntitySA_Plane1(ModEntities.ENTITY_PLANE1.get(), serverworld);
											pha.setTargetType(2);
											pha.setPos((double)i1, (double)j1+25, (double)k1);
											serverworld.addFreshEntity(pha);
											if (livingentity != null)pha.setTarget(livingentity);
											{
												ERO_Pillager reb = new ERO_Pillager(ModEntities.ENTITY_PI.get(), serverworld);
												reb.setPos((double)i1, (double)j1+25, (double)k1);
												reb.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(reb.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
												serverworld.addFreshEntity(reb);
												pha.catchPassenger(reb);
											}
										}
										if(this.level().random.nextInt(8)==1 && ve<3){
											EntitySA_Plane2 pha = new EntitySA_Plane2(ModEntities.ENTITY_PLANE2.get(), serverworld);
											pha.setTargetType(2);
											pha.setPos((double)i1, (double)j1+25, (double)k1);
											serverworld.addFreshEntity(pha);
											if (livingentity != null)pha.setTarget(livingentity);
											{
												ERO_Pillager reb = new ERO_Pillager(ModEntities.ENTITY_PI.get(), serverworld);
												reb.setPos((double)i1, (double)j1+25, (double)k1);
												reb.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(reb.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
												serverworld.addFreshEntity(reb);
												pha.catchPassenger(reb);
											}
										}
										if(this.level().random.nextInt(5)==4){
											ERO_Skeleton pha = new ERO_Skeleton(ModEntities.ENTITY_SKELETON.get(), serverworld);
											pha.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
											pha.setPos((double)i1, (double)j1, (double)k1);
											pha.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(pha.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
											serverworld.addFreshEntity(pha);
											if (livingentity != null)pha.setTarget(livingentity);
										}
										if(this.level().random.nextInt(5)==3){
											ERO_Creeper pha = new ERO_Creeper(ModEntities.ENTITY_CREEPER.get(), serverworld);
											pha.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
											pha.setPos((double)i1, (double)j1, (double)k1);
											pha.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(pha.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
											serverworld.addFreshEntity(pha);
											if (livingentity != null)pha.setTarget(livingentity);
										}
										if(this.level().random.nextInt(5)==2){
											ERO_Pillager pha = new ERO_Pillager(ModEntities.ENTITY_PI.get(), serverworld);
											pha.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
											pha.setPos((double)i1, (double)j1, (double)k1);
											pha.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(pha.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
											serverworld.addFreshEntity(pha);
											if (livingentity != null)pha.setTarget(livingentity);
										}
										if(this.level().random.nextInt(4)==1){
											ERO_REB pha = new ERO_REB(ModEntities.ENTITY_REB.get(), serverworld);
											pha.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
											pha.setPos((double)i1, (double)j1, (double)k1);
											pha.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(pha.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
											serverworld.addFreshEntity(pha);
											if (livingentity != null)pha.setTarget(livingentity);
										}
										if(husk>25){
											if(this.level().random.nextInt(4)==1){
												ERO_Zombie pha = new ERO_Zombie(ModEntities.ENTITY_EZOMBIE.get(), serverworld);
												pha.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
												pha.canSummon=true;
												pha.setPos((double)i1, (double)j1, (double)k1);
												pha.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(pha.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
												serverworld.addFreshEntity(pha);
												if (livingentity != null)pha.setTarget(livingentity);
											}
										}
									}
									break;
								}
							}
						}
						this.summontime = 0;
					}
				}
			}
		}
		float sp = 0.20F;//移速
		this.moveway(this, sp);//看目标+移动
      }
      super.aiStep();
   }

	public void moveway(ERO_Zombie entity, float sp) {
		boolean ta = false;
		{//索敌
			if (entity.getTarget() != null) {
				//boolean flag = entity.getSensing().canSee(entity.getTarget());
				if (!entity.getTarget().isInvisible()) {//target
					/*if (flag) */{
						//entity.setattacktask(true);
					}
					if (entity.getTarget().getHealth() > 0.0F/* && flag*/) {
						{
							/*if (entity.distanceToSqr(entity.getTarget()) < 4)*/{//
								if(entity.move_type==1 && entity.cooltime6>40){//跳跃模式
									Vec3 vector3d = entity.getDeltaMovement();
									entity.setDeltaMovement(1.5F*vector3d.x, 0.3D+entity.level().random.nextInt(2)*0.1D, 1.5F*vector3d.z);
									entity.cooltime6 = 0;
								}
								if(entity.move_type!=0){//高速模式
									entity.getNavigation().moveTo(entity.getTarget().getX(), entity.getTarget().getY(), entity.getTarget().getZ(), 3.5);
								}
							}
						}
					}
				}
			}
		}
	}
   

   protected void convertToZombieType(EntityType<? extends ERO_Zombie> p_234341_1_) {
      ERO_Zombie ERO_Zombie = this.convertTo(p_234341_1_, true);
      if (ERO_Zombie != null) {
         ERO_Zombie.handleAttributes(ERO_Zombie.level().getCurrentDifficultyAt(ERO_Zombie.blockPosition()).getSpecialMultiplier());
         ERO_Zombie.setCanBreakDoors(ERO_Zombie.supportsBreakDoorGoal() && this.canBreakDoors());
         net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, ERO_Zombie);
      }
   }

   protected boolean isSunSensitive() {
      return true;
   }

   /*public boolean doHurtTarget(Entity p_70652_1_) {
      boolean flag = super.doHurtTarget(p_70652_1_);
      if (flag) {
		  this.swing(InteractionHand.MAIN_HAND);
         float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
         if (this.getMainInteractionHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
            p_70652_1_.setSecondsOnFire(2);
         }
      }
      return flag;
   }*/

   protected SoundEvent getAmbientSound() {
      return SoundEvents.ZOMBIE_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.ZOMBIE_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.ZOMBIE_DEATH;
   }

   protected SoundEvent getStepSound() {
      return SoundEvents.ZOMBIE_STEP;
   }

   protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
      this.playSound(this.getStepSound(), 0.15F, 1.0F);
   }

   public MobType getMobType() {
      return MobType.UNDEAD;
   }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource $$0, DifficultyInstance diff) {
        //super.populateDefaultEquipmentSlots($$0, diff);
      if (this.random.nextFloat() < 0.5F) {
         int i = this.random.nextInt(3);
         if (i == 0) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
         } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
         }
      }
      if (this.random.nextFloat() < 0.55F) {
         int i = this.random.nextInt(2);
         float f = 0.5F;
         if (this.random.nextFloat() < 0.095F)++i;
         if (this.random.nextFloat() < 0.095F)++i;
         if (this.random.nextFloat() < 0.095F)++i;
         boolean flag = true;
         for(EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
            if (equipmentslottype.getType() == EquipmentSlot.Type.ARMOR) {
               ItemStack itemstack = this.getItemBySlot(equipmentslottype);
               if (!flag && this.random.nextFloat() < f) {
                  break;
               }
               flag = false;
               if (itemstack.isEmpty()) {
                  Item item = getEquipmentForSlot(equipmentslottype, i);
                  if (item != null) {
                     this.setItemSlot(equipmentslottype, new ItemStack(item));
                  }
               }
            }
         }
      }
    }

   public void addAdditionalSaveData(CompoundTag p_213281_1_) {
      super.addAdditionalSaveData(p_213281_1_);
      p_213281_1_.putBoolean("CanBreakDoors", this.canBreakDoors());
   }
   public void readAdditionalSaveData(CompoundTag p_70037_1_) {
      super.readAdditionalSaveData(p_70037_1_);
      this.setCanBreakDoors(true);
   }
	int destroyCount = 0;
   public boolean killedEntity(ServerLevel p_241847_1_, LivingEntity p_241847_2_) {
      boolean flag = super.killedEntity(p_241847_1_, p_241847_2_);
		++destroyCount;
		if(destroyCount==5){
			/*this.setCanBreakDoors(true);
			this.setCanPickUpLoot(true);*/
			this.populateDefaultEquipmentSlots(null,null);
		}
		if(destroyCount==10){
			canSummon=true;
			if (this.random.nextFloat() < 0.1F){
				this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
				this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getMaxHealth()*2);
			}else{
				this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SOUL_TORCH));
				this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getMaxHealth()*1.5F);
			}
		}
		return flag;
   }

   protected float getStandingEyeHeight(Pose p_213348_1_, EntityDimensions p_213348_2_) {
      return this.isBaby() ? 0.93F : 1.74F;
   }

   public boolean canHoldItem(ItemStack p_175448_1_) {
      return p_175448_1_.getItem() == Items.EGG && this.isBaby() && this.isPassenger() ? false : super.canHoldItem(p_175448_1_);
   }

   public static boolean getSpawnAsBabyOdds(Random p_241399_0_) {
      return p_241399_0_.nextFloat() < net.minecraftforge.common.ForgeConfig.SERVER.zombieBabyChance.get();
   }

   protected void handleAttributes(float count) {
      this.randomizeReinforcementsChance();
      this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Random spawn bonus", this.random.nextDouble() * (double)0.05F, AttributeModifier.Operation.ADDITION));
      double d0 = this.random.nextDouble() * 1.5D * (double)count;
      if (d0 > 1.0D) {
         this.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random zombie-spawn bonus", d0, AttributeModifier.Operation.MULTIPLY_TOTAL));
      }
      if (this.random.nextFloat() < count * 0.05F) {
         this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 0.25D + 0.5D, AttributeModifier.Operation.ADDITION));
         this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 13.0D + 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
         this.setCanBreakDoors(true);
      }

   }

   protected void randomizeReinforcementsChance() {
      this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.random.nextDouble() * net.minecraftforge.common.ForgeConfig.SERVER.zombieBaseSummonChance.get());
   }

   public double getMyRidingOffset() {
      return this.isBaby() ? 0.0D : -0.45D;
   }

   protected ItemStack getSkull() {
      return new ItemStack(Items.ZOMBIE_HEAD);
   }

   public static class GroupData implements SpawnGroupData {
      public final boolean isBaby;
      public final boolean canSpawnJockey;
      public GroupData(boolean p_i231567_1_, boolean p_i231567_2_) {
         this.isBaby = p_i231567_1_;
         this.canSpawnJockey = p_i231567_2_;
      }
   }
}

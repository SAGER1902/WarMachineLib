package advancearmy.entity.mob;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;

import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
//import com.mrcrayfish.guns.item.GunItem;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import advancearmy.AdvanceArmy;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import wmlib.api.ITool;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import advancearmy.util.TargetSelect;
public abstract class AbstractERO_Skeleton extends Monster implements RangedAttackMob{//, Enemy
   private final RangedBowAttackGoal<AbstractERO_Skeleton> bowGoal = new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
   private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false) {
      public void stop() {
         super.stop();
         AbstractERO_Skeleton.this.setAggressive(false);
      }
      public void start() {
         super.start();
         AbstractERO_Skeleton.this.setAggressive(true);
      }
   };
   
   protected AbstractERO_Skeleton(EntityType<? extends AbstractERO_Skeleton> p_i48555_1_, Level p_i48555_2_) {
      super(p_i48555_1_, p_i48555_2_);
      this.reassessWeaponGoal();
   }

   protected void registerGoals() {
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoalSA(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 15F, false, false, (attackentity) -> {
			return this.CanAttack(attackentity);
		}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, Player.class, 10, 15F, false, false, (attackentity) -> {
			return true;
		}));
   }

    public boolean CanAttack(Entity entity){
		return TargetSelect.mobCanAttack(this,entity,this.getTarget());
    }

   protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
      this.playSound(this.getStepSound(), 0.15F, 1.0F);
   }

   protected abstract SoundEvent getStepSound();

   public MobType getMobType() {
      return MobType.UNDEAD;
   }
	public int cooltime;
   public void aiStep() {
	   /*if(this.sneak_aim && !this.isCrouching())this.setPose(Pose.CROUCHING);
	   if(!this.sneak_aim && this.isCrouching())this.setPose(Pose.STANDING);*/
      /*boolean flag = this.isSunBurnTick();
      if (flag) {
         ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
         if (!itemstack.isEmpty()) {
            if (itemstack.isDamageableItem()) {
               itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
               if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                  this.broadcastBreakEvent(EquipmentSlot.HEAD);
                  this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
               }
            }

            flag = false;
         }

         if (flag) {
            this.setSecondsOnFire(8);
         }
      }*/
      super.aiStep();
   }

   public void rideTick() {
      super.rideTick();
      if (this.getVehicle() instanceof PathfinderMob) {
         PathfinderMob creatureentity = (PathfinderMob)this.getVehicle();
         this.yBodyRot = creatureentity.yBodyRot;
      }
   }


    @Override
    protected void populateDefaultEquipmentSlots(RandomSource $$0, DifficultyInstance $$1) {
        super.populateDefaultEquipmentSlots($$0, $$1);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

	@Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        RandomSource $$5 = level.getRandom();
        this.populateDefaultEquipmentSlots($$5, difficulty);
        this.populateDefaultEquipmentEnchantments($$5, difficulty);
        this.reassessWeaponGoal();
        this.setCanPickUpLoot($$5.nextFloat() < 0.55f * difficulty.getSpecialMultiplier());
        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate $$6 = LocalDate.now();
            int $$7 = $$6.get(ChronoField.DAY_OF_MONTH);
            int $$8 = $$6.get(ChronoField.MONTH_OF_YEAR);
            if ($$8 == 10 && $$7 == 31 && $$5.nextFloat() < 0.25f) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack($$5.nextFloat() < 0.1f ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0f;
            }
        }
        return spawnData;
    }

    public void reassessWeaponGoal() {
        if (this.level() == null || this.level().isClientSide) {
            return;
        }
        this.goalSelector.removeGoal(this.meleeGoal);
        this.goalSelector.removeGoal(this.bowGoal);
        ItemStack $$0 = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
        if ($$0.is(Items.BOW)) {
            int $$1 = 20;
            if (this.level().getDifficulty() != Difficulty.HARD) {
                $$1 = 40;
            }
            this.bowGoal.setMinAttackInterval($$1);
            this.goalSelector.addGoal(4, this.bowGoal);
        } else {
            this.goalSelector.addGoal(4, this.meleeGoal);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        ItemStack $$2 = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow $$3 = this.getArrow($$2, velocity);
        double $$4 = target.getX() - this.getX();
        double $$5 = target.getY(0.3333333333333333) - $$3.getY();
        double $$6 = target.getZ() - this.getZ();
        double $$7 = Math.sqrt($$4 * $$4 + $$6 * $$6);
        $$3.shoot($$4, $$5 + $$7 * (double)0.2f, $$6, 1.6f, 14 - this.level().getDifficulty().getId() * 4);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity($$3);
    }

    protected AbstractArrow getArrow(ItemStack arrowStack, float velocity) {
        AbstractArrow $$2 = ProjectileUtil.getMobArrow(this, arrowStack, velocity);
        if ($$2 instanceof Arrow) {
            ((Arrow)$$2).addEffect(new MobEffectInstance(MobEffects.WITHER, 300));
        }
        return $$2;
    }
    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeapon) {
        return projectileWeapon == Items.BOW;
    }

   public void readAdditionalSaveData(CompoundTag p_70037_1_) {
      super.readAdditionalSaveData(p_70037_1_);
      this.reassessWeaponGoal();
   }

   public void setItemSlot(EquipmentSlot p_184201_1_, ItemStack p_184201_2_) {
      super.setItemSlot(p_184201_1_, p_184201_2_);
      if (!this.level().isClientSide) {
         this.reassessWeaponGoal();
      }

   }

   protected float getStandingEyeHeight(Pose p_213348_1_, EntityDimensions p_213348_2_) {
      return 1.74F;
   }

   public double getMyRidingOffset() {
      return -0.6D;
   }
}

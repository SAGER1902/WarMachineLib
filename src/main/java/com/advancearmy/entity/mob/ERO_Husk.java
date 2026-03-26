package advancearmy.entity.mob;

import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import advancearmy.AdvanceArmy;
import net.minecraft.world.entity.Pose;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.world.entity.PathfinderMob;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import advancearmy.entity.ai.ZombieAttackGoalSA;
import advancearmy.init.ModEntities;
import wmlib.api.ITool;

import advancearmy.util.TargetSelect;
public class ERO_Husk extends PathfinderMob implements Enemy,IEnemy{
	public ERO_Husk(EntityType<? extends ERO_Husk> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.xpReward = 1;
	}
	public ERO_Husk(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_EHUSK.get(), worldIn);
	}
	public float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(2, new ZombieAttackGoalSA(this, 1.0D, false));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 10F, false, false, (attackentity) -> {
		return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, Player.class, 10, 10F, false, false, (attackentity) -> {
		return true;}));
	}

    public boolean CanAttack(Entity entity){
		return TargetSelect.mobCanAttack(this,entity,this.getTarget());
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

	protected boolean isSunSensitive() {
	  return true;
	}
	protected SoundEvent getAmbientSound() {
	  return SoundEvents.HUSK_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
	  return SoundEvents.HUSK_HURT;
	}

	protected SoundEvent getDeathSound() {
	  return SoundEvents.HUSK_DEATH;
	}

	protected SoundEvent getStepSound() {
	  return SoundEvents.HUSK_STEP;
	}
	protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
	  this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}

	public MobType getMobType() {
	  return MobType.UNDEAD;
	}

	protected float getStandingEyeHeight(Pose p_213348_1_, EntityDimensions p_213348_2_) {
	  return this.isBaby() ? 0.93F : 1.74F;
	}
	public double getMyRidingOffset() {
	  return this.isBaby() ? 0.0D : -0.45D;
	}
	
    public static AttributeSupplier.Builder createAttributes() {
        return ERO_Husk.createMobAttributes().add(Attributes.FOLLOW_RANGE, 100.0D).add(Attributes.MAX_HEALTH, 60.0D)
					.add(Attributes.MOVEMENT_SPEED, (double)0.15F)
					.add(Attributes.ATTACK_DAMAGE, 3.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.ARMOR, 3.0D);
    }
}

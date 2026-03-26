package advancearmy.entity.mob;
import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.scores.Team;
import advancearmy.AdvanceArmy;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.world.entity.PathfinderMob;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.common.living.ai.LivingLockGoal;
import wmlib.api.IEnemy;
import advancearmy.entity.ai.ZombieAttackGoalSA;
import advancearmy.init.ModEntities;
import wmlib.api.ITool;
import wmlib.api.IHealthBar;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import net.minecraftforge.fml.ModList;
import safx.SagerFX;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.Difficulty;
import advancearmy.util.TargetSelect;
public class ERO_Ravager extends Raider implements Enemy,IEnemy/*,IHealthBar*/{
    private static final Predicate<Entity> NO_RAVAGER_AND_ALIVE = entity -> entity.isAlive() && !(entity instanceof ERO_Ravager);
    private static final double BASE_MOVEMENT_SPEED = 0.3;
    private static final double ATTACK_MOVEMENT_SPEED = 0.35;
    private static final int STUNNED_COLOR = 8356754;
    private static final double STUNNED_COLOR_BLUE = 0.5725490196078431;
    private static final double STUNNED_COLOR_GREEN = 0.5137254901960784;
    private static final double STUNNED_COLOR_RED = 0.4980392156862745;
    private static final int ATTACK_DURATION = 10;
    public static final int STUN_DURATION = 40;
    private int attackTick;
    private int stunnedTick;
    private int roarTick;
    public ERO_Ravager(EntityType<? extends ERO_Ravager> entityType, Level level) {
        super(entityType, level);
        this.setMaxUpStep(1.0f);
        this.xpReward = 20;
        this.setPathfindingMalus(BlockPathTypes.LEAVES, 0.0f);
    }
	public ERO_Ravager(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ERO_RAV.get(), worldIn);
	}
	
    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }
    @Override
    public void applyRaidBuffs(int wave, boolean unusedFalse) {
    }
	
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new ERO_RavagerMeleeAttackGoal());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.4));
		this.goalSelector.addGoal(6, new LivingLockGoal(this, 1.0D, true));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 10F, false, false, (attackentity) -> {
		return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, Player.class, 10, 10F, false, false, (attackentity) -> {
		return true;}));
        //this.targetSelector.addGoal(2, new HurtByTargetGoal(this, Raider.class).setAlertOthers(new Class[0]));
    }
    public boolean CanAttack(Entity entity){
		return TargetSelect.mobCanAttack(this,entity,this.getTarget());
    }
	
    @Override
    protected void updateControlFlags() {
        //boolean $$0 = !(this.getControllingPassenger() instanceof Mob) || this.getControllingPassenger().getType().is(EntityTypeTags.RAIDERS);
        boolean $$0 = true;
		boolean $$1 = true/*!(this.getVehicle() instanceof Boat)*/;
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, $$0);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, $$0 && $$1);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, $$0);
        this.goalSelector.setControlFlag(Goal.Flag.TARGET, $$0);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return ERO_Ravager.createMobAttributes().add(Attributes.MAX_HEALTH, 600.0)
		.add(Attributes.MOVEMENT_SPEED, 0.2)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
		.add(Attributes.ATTACK_DAMAGE, 120.0)
		.add(Attributes.ATTACK_KNOCKBACK, 1.5)
		.add(Attributes.FOLLOW_RANGE, 30.0);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AttackTick", this.attackTick);
        compound.putInt("StunTick", this.stunnedTick);
        compound.putInt("RoarTick", this.roarTick);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.attackTick = compound.getInt("AttackTick");
        this.stunnedTick = compound.getInt("StunTick");
        this.roarTick = compound.getInt("RoarTick");
    }
    /*@Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.RAVAGER_CELEBRATE;
    }*/
    /*@Override
    public int getMaxHeadYRot() {
        return 45;
    }*/
    @Override
    public double getPassengersRidingOffset() {
        return 4;
    }
    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        LivingEntity $$0;
        Entity entity;
        return !this.isNoAi() && (entity = this.getFirstPassenger()) instanceof LivingEntity ? ($$0 = (LivingEntity)entity) : null;
    }
	
	boolean summoned = false;
	
    @Override
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity = source.getEntity();
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
	
	int raidMoveTime = 0;
	
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.isAlive()) {
            return;
        }
		
		if(this.getControllingPassenger()==null && !summoned){
			if (!this.level().isClientSide){
				if(this.level().random.nextInt(3)==1){
					ERO_AA seat = new ERO_AA(ModEntities.E_AA.get(),this.level());
					seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
					this.level().addFreshEntity(seat);
					seat.startRiding(this);
				}else if(this.level().random.nextInt(3)==1){
					ERO_Mortar seat = new ERO_Mortar(ModEntities.E_MORTAR.get(),this.level());
					seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
					this.level().addFreshEntity(seat);
					seat.startRiding(this);
				}else{
					ERO_ROCKET seat = new ERO_ROCKET(ModEntities.E_ROCKET.get(),this.level());
					seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
					this.level().addFreshEntity(seat);
					seat.startRiding(this);
				}
				summoned=true;
			}
		}
		
		if(raidMoveTime<100){
			++raidMoveTime;
		}
		if(raidMoveTime>80 && this.getTarget() == null && this.hasActiveRaid() && !this.getCurrentRaid().isOver() 
		/*&& !((ServerLevel)((Entity)this.mob).level()).isVillage(((Entity)this.mob).blockPosition());*/){
			Vec3 vpos;
			Raid ra = this.getCurrentRaid();
			/*if (((Raider)this.mob).tickCount > this.recruitmentTick) {
				this.recruitmentTick = ((Raider)this.mob).tickCount + 20;
				this.recruitNearby(ra);
			}*/
			if (!this.isPathFinding() && (vpos = DefaultRandomPos.getPosTowards(this, 15, 4, Vec3.atBottomCenterOf(ra.getCenter()), 1.5)) != null) {
				this.getNavigation().moveTo(vpos.x, vpos.y, vpos.z, 1.0);
			}
			raidMoveTime=0;
		}
		
        if (this.isImmobile()) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);
        } else {
            double $$0 = this.getTarget() != null ? 0.35 : 0.3;
            double $$1 = this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.lerp(0.1, $$1, $$0));
        }
        if (this.horizontalCollision && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            boolean $$2 = false;
            AABB $$3 = this.getBoundingBox().inflate(0.2);
            for (BlockPos $$4 : BlockPos.betweenClosed(Mth.floor($$3.minX), Mth.floor($$3.minY), Mth.floor($$3.minZ), Mth.floor($$3.maxX), Mth.floor($$3.maxY), Mth.floor($$3.maxZ))) {
                BlockState $$5 = this.level().getBlockState($$4);
                Block $$6 = $$5.getBlock();
                if (!($$6 instanceof LeavesBlock)) continue;
                $$2 = this.level().destroyBlock($$4, true, this) || $$2;
            }
            if (!$$2 && this.onGround()) {
                this.jumpFromGround();
            }
        }
        if (this.roarTick > 0) {
            --this.roarTick;
            if (this.roarTick == 10) {
                this.roar();
            }
        }
        if (this.attackTick > 0) {
            --this.attackTick;
        }
        if (this.stunnedTick > 0) {
            --this.stunnedTick;
            this.stunEffect();
            if (this.stunnedTick == 0) {
                this.playSound(SoundEvents.RAVAGER_ROAR, 1.0f, 1.0f);
                this.roarTick = 20;
            }
        }
    }
    private void stunEffect() {
        if (this.random.nextInt(6) == 0) {
            double $$0 = this.getX() - (double)this.getBbWidth() * Math.sin(this.yBodyRot * ((float)Math.PI / 180)) + (this.random.nextDouble() * 0.6 - 0.3);
            double $$1 = this.getY() + (double)this.getBbHeight() - 0.3;
            double $$2 = this.getZ() + (double)this.getBbWidth() * Math.cos(this.yBodyRot * ((float)Math.PI / 180)) + (this.random.nextDouble() * 0.6 - 0.3);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, $$0, $$1, $$2, 0.4980392156862745, 0.5137254901960784, 0.5725490196078431);
        }
    }
    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.attackTick > 0 || this.stunnedTick > 0 || this.roarTick > 0;
    }
    @Override
    public boolean hasLineOfSight(Entity entity) {
        if (this.stunnedTick > 0 || this.roarTick > 0) {
            return false;
        }
        return super.hasLineOfSight(entity);
    }
    @Override
    protected void blockedByShield(LivingEntity defender) {
        if (this.roarTick == 0) {
            if (this.random.nextDouble() < 0.5) {
                this.stunnedTick = 40;
                this.playSound(SoundEvents.RAVAGER_STUNNED, 1.0f, 1.0f);
                this.level().broadcastEntityEvent(this, (byte)39);
                defender.push(this);
            } else {
                this.strongKnockback(defender);
            }
            defender.hurtMarked = true;
        }
    }
    private void roar() {
        if (this.isAlive()) {
            int var3_5=0;
            /*List<Entity> $$0 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0), NO_RAVAGER_AND_ALIVE);
            for (LivingEntity livingEntity : $$0) {
                if (!(livingEntity instanceof AbstractIllager)) {
                    livingEntity.hurt(this.damageSources().mobAttack(this), 6.0f);
                }
                this.strongKnockback(livingEntity);
            }*/
            Vec3 $$2 = this.getBoundingBox().getCenter();
            boolean bl = false;
            while (var3_5 < 40) {
                double $$4 = this.random.nextGaussian() * 0.2;
                double $$5 = this.random.nextGaussian() * 0.2;
                double $$6 = this.random.nextGaussian() * 0.2;
                this.level().addParticle(ParticleTypes.POOF, $$2.x, $$2.y, $$2.z, $$4, $$5, $$6);
                ++var3_5;
            }
            this.gameEvent(GameEvent.ENTITY_ROAR);
        }
    }
    private void strongKnockback(Entity entity) {
        double $$1 = entity.getX() - this.getX();
        double $$2 = entity.getZ() - this.getZ();
        double $$3 = Math.max($$1 * $$1 + $$2 * $$2, 0.001);
        entity.push($$1 / $$3 * 4.0, 0.2, $$2 / $$3 * 4.0);
    }
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackTick = 10;
            this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0f, 1.0f);
        } else if (id == 39) {
            this.stunnedTick = 40;
        }
        super.handleEntityEvent(id);
    }
    public int getAttackTick() {
        return this.attackTick;
    }
    public int getStunnedTick() {
        return this.stunnedTick;
    }
    public int getRoarTick() {
        return this.roarTick;
    }
	
	public boolean fireImmune() {
		return true;
	}
	public void checkDespawn(){
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }
	}
	
    @Override
    public boolean doHurtTarget(Entity target) {
        this.attackTick = 10;
        this.level().broadcastEntityEvent(this, (byte)4);
        this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0f, 1.0f);
		this.strongKnockback(target);
		if(target.getVehicle()!=null){
			target.getVehicle().hurt(this.damageSources().mobAttack(this), 125);
		}
        return super.doHurtTarget(target);
    }
    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.RAVAGER_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.RAVAGER_DEATH;
    }
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.RAVAGER_STEP, 0.15f, 1.0f);
    }
    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return !level.containsAnyLiquid(this.getBoundingBox());
    }
    /*@Override
    public void applyRaidBuffs(int wave, boolean unusedFalse) {
    }
    @Override
    public boolean canBeLeader() {
        return false;
    }*/
    class ERO_RavagerMeleeAttackGoal
    extends MeleeAttackGoal {
        public ERO_RavagerMeleeAttackGoal() {
            super(ERO_Ravager.this, 1.0, true);
        }
        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            float $$1 = ERO_Ravager.this.getBbWidth() - 0.1f;
            return $$1 * 2.0f * ($$1 * 2.0f) + attackTarget.getBbWidth();
        }
    }
}

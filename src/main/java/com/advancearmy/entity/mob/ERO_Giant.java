package advancearmy.entity.mob;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.scores.Team;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.stats.Stats;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import advancearmy.AdvanceArmy;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.state.BlockState;
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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.fml.ModList;
import safx.SagerFX;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.tags.BlockTags;

import net.minecraft.world.entity.animal.WaterAnimal;
public class ERO_Giant extends PathfinderMob implements Enemy,IEnemy,IHealthBar{
	public ERO_Giant(EntityType<? extends ERO_Giant> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.xpReward = 1000;
	  this.setMaxUpStep(4F);
	}
	public ERO_Giant(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_GIANT.get(), worldIn);
	}
	public float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.5F;
	}
	protected void registerGoals() {
		//this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		//this.goalSelector.addGoal(2, new ZombieAttackGoalSA(this, 1.0D, false));
		this.goalSelector.addGoal(6, new LivingLockGoal(this, 1.0D, true));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 10F, false, false, (attackentity) -> {
		return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, Player.class, 10, 10F, false, false, (attackentity) -> {
		return true;}));
	}

    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && (this.getTeam()!=null && this.getTeam()!=entity.getTeam()||this.getTeam()==null)){
			if(!(entity instanceof IEnemy||entity instanceof ITool||entity instanceof WaterAnimal)||entity==this.getTarget()){
				return true;
			}else{
				return false;
			}
    	}else{
			return false;
		}
    }
	private static final EntityDataAccessor<Integer> lrange = SynchedEntityData.<Integer>defineId(ERO_Giant.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> roteyaw = SynchedEntityData.<Integer>defineId(ERO_Giant.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> rotepitch = SynchedEntityData.<Integer>defineId(ERO_Giant.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> armorc = SynchedEntityData.<Integer>defineId(ERO_Giant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> attacktype = SynchedEntityData.<Integer>defineId(ERO_Giant.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		{
			compound.putInt("lrange", getRange());
			compound.putInt("roteyaw", getRoteYaw());
			compound.putInt("rotepitch", getRotePitch());
			compound.putInt("attacktype", getAttackType());
			compound.putInt("armorc", getArmorC());
		}
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
		{
			this.setRange(compound.getInt("lrange"));
			this.setRoteYaw(compound.getInt("roteyaw"));
			this.setRotePitch(compound.getInt("rotepitch"));
			this.setAttackType(compound.getInt("attacktype"));
			this.setArmorC(compound.getInt("armorc"));
		}
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(lrange, Integer.valueOf(0));
		this.entityData.define(roteyaw, Integer.valueOf(0));
		this.entityData.define(rotepitch, Integer.valueOf(0));
		this.entityData.define(attacktype, Integer.valueOf(0));
		this.entityData.define(armorc, Integer.valueOf(0));
	}
	
    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, 
                                       @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        RandomSource random = level.getRandom();
        this.setArmorC(random.nextInt(11)*200);
        return spawnData;
    }
	
	public int getAttackType() {
		return ((this.entityData.get(attacktype)).intValue());
	}
	public void setAttackType(int stack) {
		this.entityData.set(attacktype, Integer.valueOf(stack));
	}
	public int getRoteYaw() {
		return ((this.entityData.get(roteyaw)).intValue());
	}
	public void setRoteYaw(int stack) {
		this.entityData.set(roteyaw, Integer.valueOf(stack));
	}
	public int getRotePitch() {
		return ((this.entityData.get(rotepitch)).intValue());
	}
	public void setRotePitch(int stack) {
		this.entityData.set(rotepitch, Integer.valueOf(stack));
	}
	
	public int getRange() {
		return ((this.entityData.get(lrange)).intValue());
	}
	public void setRange(int stack) {
		this.entityData.set(lrange, Integer.valueOf(stack));
	}
	public int getArmorC() {
		return ((this.entityData.get(armorc)).intValue());
	}
	public void setArmorC(int stack) {
		this.entityData.set(armorc, Integer.valueOf(stack));
	}
	
	public boolean NotFriend(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F){
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = this.getTeam();
			Team team1 = entity1.getTeam();
			boolean canattack = true;
			if(team != null && team1 == team)canattack= false;
			if((entity instanceof IEnemy) && (team == null||team != team1))canattack= false;
			return canattack;
    	}else{
			return true;
		}
	}
	int totalTime = 90;
	float animSpeed = 2F;
	
	boolean change = false;
	public void updateSwingTime() {
		if (this.swinging) {
			++this.swingTime;
			if(!change && this.attackAnim>-12){//60
				this.attackAnim-=0.2F*animSpeed;
				//this.setRotePitch(0);
			}
			if(this.attackAnim<-11)change=true;
			if(change && this.attackAnim<12){//30
				this.attackAnim+=0.4F*animSpeed;
			}
			if(this.attackAnim>7)change=false;
			if (this.swingTime >= totalTime/animSpeed) {
				this.swingTime = 0;
				this.swinging = false;
				change=false;
			}
		} else {
			this.swingTime = 0;
			if(this.attackAnim>0)this.attackAnim-=0.1F*animSpeed;
			if(this.attackAnim<0)this.attackAnim+=0.1F*animSpeed;
		}
	}
	public boolean doHurtTarget(Entity entity) {
		boolean flag = entity.hurt(this.damageSources().mobAttack(this), 300);
		return flag;
	}

	public int attack_time = 0;
	protected void handAttack(LivingEntity living) {
		if(this.attack_time<totalTime/animSpeed){
			if(this.getAttackType()!=1)this.setAttackType(1/*this.random.nextInt(4)*/);
			++this.attack_time;
			if(this.attack_time==1 && !this.swinging)this.swing(InteractionHand.MAIN_HAND);
			if (this.attack_time == (totalTime/animSpeed)-1) {
				this.playSound(SoundEvents.HUSK_HURT, 10.0F, 1.0F);
				//this.setHealth(this.getHealth()+(this.getMaxHealth()-this.getHealth())*0.02F);
				living.invulnerableTime = 0;
				this.doHurtTarget(living);
				this.level().explode(this, living.getX(), living.getY()+1, living.getZ(), 4F, Level.ExplosionInteraction.NONE);
				if(ModList.get().isLoaded("safx")){
					SagerFX.proxy.createFX("DropRing", null, living.getX(), living.getY()+1, living.getZ(), 0F, 0F, 0F, 2F);
					SagerFX.proxy.createFX("ShockWave", null, living.getX(), living.getY()+1, living.getZ(), 0F, 0F, 0F, 2F);
					SagerFX.proxy.createFX("ManyStone", null, living.getX(), living.getY()+1, living.getZ(), 0F, 0F, 0F, 2F);
				}
				List<Entity> list = living.level().getEntities(living, living.getBoundingBox().inflate(10D, 10D, 10D));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity attackentity = list.get(k2);
					if(this.NotFriend(attackentity)){
						attackentity.hurt(this.damageSources().thrown(this, this), 250);
						if(attackentity instanceof LivingEntity)((LivingEntity)attackentity).knockback(2F, 1F, 2);
					}
				}
				this.shockPlayer();
			}
		}else{
			this.attack_time=0;
		}
	}
	
    public void shockPlayer() {
		List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(50, 10, 50));
		for(int k2 = 0; k2 < list.size(); ++k2) {
			Entity attackentity = list.get(k2);
			if(attackentity instanceof Player){
				Player players = (Player)attackentity;
				if(players != null){
					if(players.getVehicle()!=null){
						expPitch = expPitch+18;
					}else{
						expPitch = expPitch+25;
					}
				}
			}
		}
    }
	
    public static float clampYaw(float yaw) {
        while (yaw < -180.0f) {
            yaw += 360.0f;
        }
        while (yaw >= 180.0f) {
            yaw -= 360.0f;
        }
        return yaw;
    }
	
	int showbartime = 0;
	public boolean isShow(){
		return this.showbartime>0;
	}
	public int getBarType(){
		return 1;
	}
	public LivingEntity getBarOwner(){
		return null;
	}
	
	public void checkDespawn() {
	}
	public float bodyYaw;
	public float bodyYawO;
	double dis;
	float pitchMove;
	float targetYaw = 0;
	float targetPitch = 0;
	public int lasercool;
	public int lasettime;
	public int anim1;
	public boolean fireImmune() {
		return true;
	}
	
    private void breakNearbyFragileBlocks() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        //AABB expandedBoundingBox = this.getBoundingBox().inflate(1.0);
		AABB expandedBoundingBox = (new AABB(
		this.getX()-3, this.getY(), this.getZ()-3, 
		this.getX()+3, this.getY()+19, this.getZ()+3)).inflate(1D);
        // 你也可以用 .inflate(horizontal, vertical, horizontal) 分别控制各方向扩展
        BlockPos.betweenClosedStream(expandedBoundingBox).forEach(pos -> {
			int count=0;
            BlockState state = serverLevel.getBlockState(pos);
            if (state.isAir() || state.getDestroySpeed(serverLevel, pos) < 0) {
                return;
            }
            boolean isFragile = state.is(BlockTags.LEAVES) // 所有树叶
                    || state.is(BlockTags.FLOWERS) // 所有花
                    || state.is(BlockTags.CROPS) // 所有农作物
                    || state.is(BlockTags.REPLACEABLE) // 其他可替换方块（如草、雪）
					|| state.is(BlockTags.MUSHROOM_GROW_BLOCK)
					|| state.is(BlockTags.LOGS);
            if (isFragile || pos.getY()>this.getY()+3 && this.isAggressive()) {
                // 参数解释：null 表示无实体原因（自然破坏），
                // 若改为 this 则破坏算由此实体造成（可能影响战利品掉落等）。
                // true 表示破坏后是否掉落物品，对于树叶通常为false，但这里我们设为true。
                boolean dropItems = true;
                serverLevel.destroyBlock(pos, dropItems, this);
				this.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, 5.0F, 1.0F);
				++count;
				if(count>20)return;
            }
        });
    }
	
	int alertTime = 0;
	int crash_time = 0;
	private float lastDamageAmount = 0.0f;
	public void tick(){
		super.tick();
		this.updateSwingTime();
		if(this.hurtTime>0){
			if(showbartime<1)showbartime = 70;
		}else{
			//lastDamageAmount=0;
		}
		if(alertTime<500)++alertTime;
		if(showbartime>0)--showbartime;
		if(this.getHealth()>0){
			/*if(this.getAttackType()>0&& this.getAttackType()<5 && this.attack_time>65){
				this.attack_time=0;
				this.swing(InteractionHand.MAIN_HAND);
			}*/
			//boolean attack = false;
			if(this.getTarget()!=null){
				LivingEntity target = this.getTarget();
				if(target.isAlive() && target!=null){
					this.setAggressive(true);
					double dx = target.getX() - this.getX();
					double dz = target.getZ() - this.getZ();
					double dyy = this.getY()+17 - target.getY()-target.getBbHeight()*0.5F;
					dis = Math.sqrt(dx * dx + dz * dz);
					this.targetYaw = (float) Math.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
					this.setRoteYaw((int)targetYaw);
					this.targetPitch = (float) (Math.atan2(dyy, dis) * 180.0D / Math.PI);
					this.setRotePitch((int)targetPitch);
					if(dis <= 15){
						float f2 = this.bodyYaw - targetYaw;
						if(f2>-20&&f2<20 && lasercool<190)this.handAttack(target);
					}else{
						if(this.getAttackType()!=5){
							this.setAttackType(5);
							this.attack_time=0;
						}
					}
				}
				

				if(alertTime>450){
					int i1 = (int)this.getTarget().getX() + this.random.nextInt(10)-5;
					int k1 = (int)this.getTarget().getZ() + this.random.nextInt(10)-5;
					List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(80, 80, 80));
					int count = 0;
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity ent = list.get(k2);
						if(ent instanceof IEnemy && !(ent instanceof ERO_Giant) && ent instanceof Mob mob){
							mob.setTarget(this.getTarget());
							mob.setAggressive(true);
							if(ent instanceof PathfinderMob find && find.getNavigation().isDone())find.getNavigation().moveTo(i1, this.getTarget().getY(), k1, 2);
							++count;
							if(count>10)break;
						}
					}
					alertTime=0;
				}				
			}
			if(!this.isAggressive()){
				this.setAttackType(0);
				//this.setRoteYaw((int)this.yHeadRot);
				this.setRotePitch(0);
				this.attack_time=0;
				this.swinging = false;
			}
			/*if(this.getAttackType()>0)*/{
				float f3 = this.pitchMove - this.getRotePitch();
				if (f3 > 1F) {
					this.pitchMove-=1F;
				} else if (f3 < -1F) {
					this.pitchMove+=1F;
				}else{
					this.pitchMove = this.getRotePitch();
				}
				this.setXRot(this.pitchMove);
				
				if(bodyYaw>360||bodyYaw<-360)bodyYaw=0;
				float f4 = this.bodyYaw - this.getRoteYaw();
				f4 = this.clampYaw(f4);
				if (f4 > 1F) {
					this.bodyYaw-=1F;
				} else if (f4 < -1F) {
					this.bodyYaw+=1F;
				}else{
					this.bodyYaw = this.getRoteYaw();
				}
				this.setYRot(bodyYaw);
				this.yBodyRot=bodyYaw;
				
				if(yHeadRot>360||yHeadRot<-360)yHeadRot=0;
				float f5 = this.yHeadRot - this.getRoteYaw();
				f5 = this.clampYaw(f5);
				if (f4 > 2F) {
					this.yHeadRot-=2F;
				} else if (f4 < -2F) {
					this.yHeadRot+=2F;
				}else{
					this.yHeadRot = this.getRoteYaw();
				}
				if(this.getAttackType()==5||this.yBodyRot!=this.getRoteYaw()){
					float f1 = this.yBodyRot * (2 * (float) Math.PI / 360);
					double mox = 0;
					double moy = -1D;
					double moz = 0;
					if(dis<15 && this.yBodyRot!=this.getRoteYaw()){
						mox -= Math.sin(f1) * -0.1F;
						moz += Math.cos(f1) * -0.1F;
					}else{
						mox -= Math.sin(f1) * 0.1F;
						moz += Math.cos(f1) * 0.1F;
					}
					this.setDeltaMovement(mox, moy, moz);
				}
			}
			
			if(/*this.getAttackType()==5 && */this.isAggressive()){
				++lasercool;
				if(lasercool>200){
					this.attack_time=0;
					this.swinging = false;
					if(lasettime<100){
						++lasettime;
						this.playSound(SoundEvents.BLAZE_SHOOT, 5.0F, 1.0F);
						this.weaponActive1();
					}else{
						lasercool=0;
						lasettime=0;
					}
				}
			}else{
				lasercool=0;
				lasettime=0;
			}
			
			if(crash_time<50){
				++crash_time;
			}else{
				breakNearbyFragileBlocks();
				List<Entity> list = this.level().getEntities(this, (new AABB(
				this.getX()-this.getBbWidth(), this.getY(), this.getZ()-this.getBbWidth(), 
				this.getX()+this.getBbWidth(), this.getY()+4, this.getZ()+this.getBbWidth())).inflate(1D));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity attackentity = list.get(k2);
					if(attackentity instanceof LivingEntity && !(attackentity instanceof ERO_Giant) && ((LivingEntity)attackentity).getHealth()>0){
						attackentity.hurt(this.damageSources().thrown(this, this), 100);
					}
				}
				crash_time=0;
			}
		}else{
			lasettime=0;
			if(this.attackAnim<0)this.attackAnim+=1;
		}
	}
	float fireposZ = 2.5F;
	float firebaseZ = 0;
	float fireposX = 0;
	float fireposY = 17F;
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
		Vec3 locken = Vec3.directionFromRotation(this.getRotationVector());
		float d = 120;
		int range = 2;
		int ix = 0;
		int iy = 0;
		int iz = 0;
		boolean stop = false;
		int pierce = 0;
		int dis = 0;
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos mutablePos1 = new BlockPos.MutableBlockPos();
		for(int xxx = 0; xxx < 120; ++xxx) {
			ix = (int) (this.getX()+xx11 + locken.x * xxx);
			iy = (int) (this.getY()+this.fireposY+base + locken.y * xxx);
			iz = (int) (this.getZ()+zz11 + locken.z * xxx);
			mutablePos.set(ix, iy, iz);
			mutablePos1.set(ix, iy+1, iz);
			BlockState iblockstate = this.level().getBlockState(mutablePos);
			BlockState iblockstate1 = this.level().getBlockState(mutablePos1);
			//this.level().addParticle(ParticleTypes.DRAGON_BREATH, ix, iy, iz, 0, 0, 0);
			if (!iblockstate.isAir()&& !iblockstate.getCollisionShape(this.level(), mutablePos).isEmpty()){
				if(iblockstate1.isAir()||iblockstate1.getCollisionShape(this.level(), mutablePos).isEmpty())this.level().setBlockAndUpdate(mutablePos1, BaseFireBlock.getState(this.level(), mutablePos1));
				dis=xxx-1;
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
								if(lockTarget.getVehicle()!=null && lockTarget.getVehicle() instanceof LivingEntity){
									LivingEntity ve = (LivingEntity)lockTarget.getVehicle();
									ve.invulnerableTime = 0;
									ve.hurt(this.damageSources().inFire(), 60);
									ve.setSecondsOnFire(8);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(this.damageSources().fellOutOfWorld(), 1);
									lockTarget.hurt(this.damageSources().inFire(), 20);
									lockTarget.setSecondsOnFire(8);
								}
								this.setHealth(this.getHealth()+2);
								/*++pierce;
								if(pierce>3)*/{
									dis=xxx+1;
									stop = true;
								}
								break;
							}
						}
					}
				}
				if(stop)break;
			}
		}
		this.setRange(dis);
	}
	
	protected void tickDeath() {
	  ++this.deathTime;
	  if (this.deathTime == 25){
		  this.playSound(SoundEvents.ZOMBIE_HURT, 5.0F, 1.0F);
		  this.createExplosionsBehindEntity();
		  this.shockPlayer();
	  }
	  if (this.deathTime == 120) {
		 this.discard(); //Forge keep data until we revive player
		 this.playSound(SoundEvents.ZOMBIE_DEATH, 5.0F, 1.0F);
		 this.createExplosionsBehindEntity();
		 this.shockPlayer();
	  }
	}

	public void createExplosionsBehindEntity() {
		if (this.level().isClientSide) return;
		float yawRadians = this.getYRot() * ((float) Math.PI / 180F);
		double backDirectionX = -Math.sin(yawRadians);
		double backDirectionZ = Math.cos(yawRadians);
		float explosionSpacing = 6.0f;
		for (int i = 1; i <= 4; i++) {
			double explosionX = this.getX() - backDirectionX * (explosionSpacing * i);
			double explosionY = this.getY()+2.5;
			double explosionZ = this.getZ() - backDirectionZ * (explosionSpacing * i);
			this.level().explode(this,explosionX, explosionY, explosionZ,8,Level.ExplosionInteraction.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("DropRing", null, explosionX, explosionY, explosionZ, 0F, 0F, 0F, 2F);
		}
	}

    @Override
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		lastDamageAmount=par2;
		if(par2>50){
			par2=par2-50;
		}else{
			par2=par2*0.5F;
		}
		
		if(this.getArmorC()>0){
			if(par2>this.getArmorC()){
				this.setArmorC(0);
			}else{
				this.setArmorC(this.getArmorC()-(int)par2);
			}
			par2=0;
		}
		
		if(entity != null){
			if(entity instanceof IEnemy){
				return false;
			}else{
				if(entity instanceof LivingEntity){
					LivingEntity entity1 = (LivingEntity) entity;
					if(this.hurtTime<2 && this.random.nextInt(3)==0)this.setTarget(entity1);
				}
				return super.hurt(source, par2);
			}
		}else{
			return super.hurt(source, par2);
		}
	}
    @Override
    public void handleDamageEvent(DamageSource source) {
        if(lastDamageAmount>5)this.walkAnimation.setSpeed(1.5f);
        this.invulnerableTime = 20;
        this.hurtTime = this.hurtDuration = 10;
        SoundEvent $$1 = this.getHurtSound(source);
        if ($$1 != null) {
            this.playSound($$1, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        }
        this.hurt(this.damageSources().generic(), 0.0f);
        //this.lastDamageSource = source;
        //this.lastDamageStamp = this.level().getGameTime();
    }

	protected boolean isSunSensitive() {
	  return true;
	}
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
	  this.playSound(this.getStepSound(), 0.15F, 5.0F);
	}
	protected float getSoundVolume() {
	  return 5.0F;
	}
	public MobType getMobType() {
	  return MobType.UNDEAD;
	}

	protected float getStandingEyeHeight(Pose p_213348_1_, EntityDimensions p_213348_2_) {
	  return 17F;
	}
	public double getMyRidingOffset() {
	  return -4.5D;
	}
	
    public static AttributeSupplier.Builder createAttributes() {
        return ERO_Giant.createMobAttributes().add(Attributes.FOLLOW_RANGE, 100.0D).add(Attributes.MAX_HEALTH, 1000.0D)
					.add(Attributes.MOVEMENT_SPEED, (double)0.02F)
					.add(Attributes.ATTACK_DAMAGE, 20.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.ARMOR, 10.0D);
    }
}

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
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Mob;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.entity.PathfinderMob;
import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.land.EntitySA_T55;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;
import advancearmy.entity.EntitySA_LandBase;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import net.minecraft.server.level.ServerPlayer;
import wmlib.api.ITool;
import advancearmy.init.ModEntities;
import net.minecraft.world.level.ServerLevelAccessor;
import advancearmy.util.TargetSelect;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.event.SASoundEvent;
import safx.SagerFX;

import net.minecraft.world.entity.animal.WaterAnimal;
public class EvilPortalOnce extends Mob implements ITool{
	public EvilPortalOnce(EntityType<? extends EvilPortalOnce> p_i48549_1_, Level p_i48549_2_) {
		super(p_i48549_1_, p_i48549_2_);
		this.xpReward = 100;
	}
	private static final EntityDataAccessor<Integer> ID_TYPE = SynchedEntityData.defineId(EvilPortalOnce.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound){
		super.addAdditionalSaveData(compound);
		compound.putInt("id_type", getPortalType());
	}
	public void readAdditionalSaveData(CompoundTag compound){
		super.readAdditionalSaveData(compound);
		this.setPortalType(compound.getInt("id_type"));
	}
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(ID_TYPE, Integer.valueOf(0));
	}
	public void setPortalType(int p_203034_1_) {
		this.entityData.set(ID_TYPE, p_203034_1_);
	}
	public int getPortalType() {
		return this.entityData.get(ID_TYPE);
	}
	
	public EvilPortalOnce(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_POR1.get(), worldIn);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
        return EvilPortalOnce.createMobAttributes();
    }
	
	public void checkDespawn(){
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }
	}
	

	public int summonTime = 0;
	public float startTime = 0;
	public float cooltime6 = 0;
	float summonCyc = 0;
	int stayTime = 0;
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance diff, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
		data = super.finalizeSpawn(level, diff, reason, data, nbt);
		this.setPortalType(5+this.random.nextInt(5));//5 mob 6 spider 7 zombie 8 vehicle 9 airmob
		return data;
	}
    @Override
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}
	public boolean fireImmune() {
		return true;
	}

	@Override
	public boolean isNoGravity() {
		return this.getPortalType()>4;
	}
	@Override
	public boolean isPushable() {
		return false;  // 防止被其他实体推动
	}
	@Override
	protected void pushEntities() {
		// 不执行任何操作，防止推动其他实体
	}
	@Override
	public boolean canBeCollidedWith() {
		return false;  // 防止与其他实体碰撞
	}
	@Override
	public boolean isPushedByFluid() {
		return false;  // 防止被流体推动
	}
	@Override
	public boolean isPickable() {
		return false;  // 防止被玩家点击选中
	}
	@Override
	public boolean canCollideWith(Entity entity) {
		return false;  // 防止与任何实体碰撞
	}
	public void push(Entity entity) {
	}
	
	int alertTime = 0;
	
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public float rote = 0;
	public void aiStep() {
		if(rote<360){
			++rote;
		}else{
			rote=0;
		}
		if(startTime<60)++startTime;
		//if(startTime==1)this.setPortalType(this.random.nextInt(3));
    	if(this.setx == 0) {
    		this.setx=((int)this.getX());
    		this.sety=((int)this.getY());
    		this.setz=((int)this.getZ());
    	}
    	{
			BlockPos blockpos = new BlockPos(this.setx,this.sety - 1,this.setz);
			BlockState iblockstate = this.level().getBlockState(blockpos);
			if (this.setx != 0 && (!iblockstate.isAir()||this.getPortalType()>4)){
				this.moveTo(this.setx,this.sety,this.setz);
			}else{
				this.moveTo(this.setx,this.getY(), this.setz);
			}
    	}
		
		if(startTime<59)return;
		if(alertTime<500)++alertTime;
		if (this.isAlive()){
			if(this.getPortalType()>4){
				++stayTime;
				if(stayTime>300){
					this.setHealth(0);
				}
				//5 mob 6 spider 7 zombie 8 vehicle 9 airmob
				if(this.getPortalType()==5)summonCyc = 20;
				if(this.getPortalType()==6)summonCyc = 15;
				if(this.getPortalType()==7)summonCyc = 12;
				if(this.getPortalType()==8)summonCyc = 30;
				if(this.getPortalType()==9)summonCyc = 20;
			}
			if (!(this.level() instanceof ServerLevel)) {
				//return false;
			} else {
				ServerLevel serverworld = (ServerLevel)this.level();
				int i = Mth.floor(this.getX());
				int j = Mth.floor(this.getY());
				int k = Mth.floor(this.getZ());
				if(summonTime<100)++summonTime;
				if(summonTime>summonCyc){//5 mob 6 spider 7 zombie 8 vehicle 9 airmob
					int count = 0;
					int ghost = 0;
					int ve = 0;
					int i1 = i;
					int j1 = j+2;
					int k1 = k;
					int tx =(this.level().random.nextInt(10)-5)*4;
					int ty =this.level().random.nextInt(5);
					int tz =(this.level().random.nextInt(10)-5)*4;
					if(alertTime>200){
						LivingEntity livingentity = null;
						List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(60D, 80D, 60D));
						for(int k2 = 0; k2 < list.size(); ++k2) {
							Entity ent = list.get(k2);
							if(ent != null){
								if(!(ent instanceof Enemy||ent instanceof ITool||ent instanceof WaterAnimal) && ent instanceof LivingEntity){
									livingentity=(LivingEntity)ent;
								}
								if(ent instanceof WeaponVehicleBase){
									WeaponVehicleBase unit = (WeaponVehicleBase)ent;
									if(unit.getTargetType()==2 && unit.getTarget()==null){
										if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMoveType()!=2){
											unit.setMoveType(4);
											if (livingentity!=null){
												unit.setTarget(livingentity);
												unit.setMovePosX((int)livingentity.getX()+tx);
												unit.setMovePosZ((int)livingentity.getZ()+tz);
											}else{
												unit.setMovePosX(i+tx);
												unit.setMovePosZ(k+tz);
											}
										}
									}
								}
								if(ent instanceof IEnemy){
									if(ent instanceof PathfinderMob){
										++count;
										PathfinderMob friend = (PathfinderMob)ent;
										if (livingentity!=null){
											if(friend.getTarget()==null){
												friend.setTarget(livingentity);
												if(friend.getNavigation().isDone())friend.getNavigation().moveTo(livingentity.getX()+tx, livingentity.getY(), livingentity.getZ()+tz, 1.2F);
												if(count>10)break;
											}
										}else{
											if(friend.getNavigation().isDone())friend.getNavigation().moveTo(i+tx, j+ty, k+tz, 1.2F);
										}
									}
								}
							}
						}
						alertTime=0;
					}
					
					{
						{
							for(int l = 0; l < 50; ++l) {
							   BlockPos blockpos = new BlockPos(i1, j1, k1);
							   {
									{
										if(ghost<5){
											if((this.getPortalType()==9) && this.level().random.nextInt(4)==2){
												EvilPortal.spawnMob(serverworld,9,i1, j1, k1);
											}
										}
										if((this.getPortalType()==9) && this.level().random.nextInt(4)==1){
											EvilPortal.spawnMob(serverworld,8,i1, j1, k1);
										}
										if((this.getPortalType()==8) && this.level().random.nextInt(14)==1){
											EvilPortal.spawnMob(serverworld,10,i1, j1, k1);
										}
										if((this.getPortalType()==8) && this.level().random.nextInt(8)==1){
											EvilPortal.spawnMob(serverworld,11,i1, j1, k1);
										}
										if((this.getPortalType()==8) && this.level().random.nextInt(12)==1){
											EvilPortal.spawnMob(serverworld,12,i1, j1, k1);
										}
										if((this.getPortalType()==8) && this.level().random.nextInt(7)==1){
											EvilPortal.spawnMob(serverworld,13,i1, j1, k1);
										}
										if((this.getPortalType()==8) && this.level().random.nextInt(8)==1){
											EvilPortal.spawnMob(serverworld,14,i1, j1, k1);
										}
										
										if((this.getPortalType()==6) && this.level().random.nextInt(3)==1){
											EvilPortal.spawnMob(serverworld,5,i1, j1, k1);
										}
										if(this.getPortalType()==7){
											EvilPortal.spawnMob(serverworld,2,i1, j1, k1);
										}else{
											if(this.level().random.nextInt(4)==1){
												EvilPortal.spawnMob(serverworld,6,i1, j1, k1);
											}
											if(this.level().random.nextInt(5)==2){
												EvilPortal.spawnMob(serverworld,4,i1, j1, k1);
											}
											if(this.level().random.nextInt(6)==2){
												EvilPortal.spawnMob(serverworld,7,i1, j1, k1);
											}
											if(this.level().random.nextInt(6)==2){
												EvilPortal.spawnMob(serverworld,3,i1, j1, k1);
											}
											if(this.level().random.nextInt(4)==2){
												EvilPortal.spawnMob(serverworld,1,i1, j1, k1);
											}
										}
										break;
									}
							   }
							}
							this.summonTime = 0;
						}
					}
				}
			}
      }
      super.aiStep();
	}
	
	protected SoundEvent getAmbientSound() {
		return SoundEvents.WITHER_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.WITHER_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.WITHER_DEATH;
	}
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime >= 60) {
			this.discard();
		}
	}
}

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
import net.minecraft.core.Direction;
import safx.SagerFX;
public class EvilPortal extends PathfinderMob implements Enemy,IEnemy{
	public EvilPortal(EntityType<? extends EvilPortal> p_i48549_1_, Level p_i48549_2_) {
		super(p_i48549_1_, p_i48549_2_);
		this.xpReward = 100;
	}
	private static final EntityDataAccessor<Integer> ID_TYPE = SynchedEntityData.defineId(EvilPortal.class, EntityDataSerializers.INT);
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
	
	private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
	public EvilPortal(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_POR.get(), worldIn);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
        return EvilPortal.createMobAttributes();
    }
	
	public void checkDespawn(){
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }
	}
	
	protected void registerGoals() {
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 25F, false, false, (attackentity) -> {
			return this.CanAttack(attackentity);
		}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, Player.class, 10, 25F, false, false, (attackentity) -> {
			return true;
		}));
	}
    public boolean CanAttack(Entity entity){
		return TargetSelect.mobCanAttack(this,entity,this.getTarget());
    }
	public void startSeenByPlayer(ServerPlayer p_184178_1_) {
		super.startSeenByPlayer(p_184178_1_);
		if(this.getPortalType()<4)this.bossEvent.addPlayer(p_184178_1_);
	}
	public void stopSeenByPlayer(ServerPlayer p_184203_1_) {
		super.stopSeenByPlayer(p_184203_1_);
		this.bossEvent.removePlayer(p_184203_1_);
	}
	public void setCustomName(@Nullable Component p_200203_1_) {
		super.setCustomName(p_200203_1_);
		this.bossEvent.setName(this.getDisplayName());
	}
	public int summonTime = 0;
	public float startTime = 0;
	public float cooltime6 = 0;
	float summonCyc = 30;
	int stayTime = 0;

    @Override
	public boolean hurt(DamageSource source, float par2)
    {
		if(startTime <99){
			return false;
		}else{
			return super.hurt(source, par2);
		}
	}
	public boolean fireImmune() {
		return true;
	}
	void spawnOnce(int id,int x,int y,int z){
		BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos();
		blockpos.set(x, y, z);
		int air =0;
		for(int i = 0; i < 25; ++i){
			BlockState groundState = this.level().getBlockState(blockpos);
			blockpos.move(Direction.UP);
			if (groundState.isAir()){
				++air;
				if(air>1)break;
			}
			++y;
		}
		
		EvilPortalOnce ev = new EvilPortalOnce(ModEntities.ENTITY_POR1.get(), this.level());
		ev.setPos(x, y, z);
		ev.setPortalType(id);
		this.level().addFreshEntity(ev);
		LightningBolt lt = EntityType.LIGHTNING_BOLT.create(this.level());
		if (lt != null) {
			lt.setVisualOnly(true);
			lt.moveTo(x, y, z, 0, 0.0f);
			this.level().addFreshEntity(lt);
		}
	}

	int missionTime = 0;
	@Override
	public boolean isNoGravity() {
		return this.getPortalType()>4;
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
		if(startTime<120)++startTime;
		if(startTime==1)this.setPortalType(this.random.nextInt(6));
		
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
		
		if(startTime<100)return;
		if(alertTime<500)++alertTime;
		if (this.isAlive()){
			if(this.getPortalType()<5)this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
			//5 mob 6 spider 7 zombie 8 vehicle 9 airmob
			if(this.getPortalType()<5){
				//this.setDeltaMovement(Vec3.ZERO);
				int rx=(int)this.getX();
				int ry=(int)this.getY()+(this.level().random.nextInt(10)-5)*2;
				int rz=(int)this.getZ();
				/*double xx11 = 0;
				double zz11 = 0;
				xx11 -= Math.sin(this.turretYaw * 0.01745329252F) * fireposZ1;
				zz11 += Math.cos(this.turretYaw * 0.01745329252F) * fireposZ1;*/
				if(missionTime<2000){
					++missionTime;
				}else{
					missionTime=0;
				}
				if(missionTime==150){
					spawnOnce(7,rx+(this.level().random.nextInt(10)-5)*10, ry+5, rz+(this.level().random.nextInt(2)-1)*10);
				}
				if(missionTime==350){
					spawnOnce(6,rx+(this.level().random.nextInt(8)-4)*10, ry+10, rz-(this.level().random.nextInt(4)-2)*10);
				}
				if(missionTime==550){
					spawnOnce(5,rx+(this.level().random.nextInt(12)-6)*10, ry+5, rz+(this.level().random.nextInt(6)-3)*10);
				}
				if(missionTime==750){
					spawnOnce(9,rx+(this.level().random.nextInt(6)-3)*10, ry+25, rz-(this.level().random.nextInt(4)-2)*10);
				}
				if(missionTime==950){
					spawnOnce(8,rx+(this.level().random.nextInt(6)-3)*10, ry+5, rz+(this.level().random.nextInt(4)-2)*10);
				}
			}
			
			
			if(cooltime6<50)++cooltime6;
			if (!(this.level() instanceof ServerLevel)) {
				//return false;
			} else {
			ServerLevel serverworld = (ServerLevel)this.level();
			LivingEntity livingentity = this.getTarget();
			int i = Mth.floor(this.getX());
			int j = Mth.floor(this.getY());
			int k = Mth.floor(this.getZ());
			if(summonTime<100)++summonTime;
			if(summonTime>summonCyc){
				int count = 0;
				int ghost = 0;
				int ve = 0;
				int i1 = i + Mth.nextInt(this.random, 2, 10) * Mth.nextInt(this.random, -1, 1);
				int j1 = j + Mth.nextInt(this.random, 2, 10);
				int k1 = k + Mth.nextInt(this.random, 2, 10) * Mth.nextInt(this.random, -1, 1);
				
				int tx =(this.level().random.nextInt(10)-5)*4;
				int ty =this.level().random.nextInt(5);
				int tz =(this.level().random.nextInt(10)-5)*4;
				
				List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(60D, 80D, 60D));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					if(alertTime>200){
						if(ent instanceof IEnemy){
							if(ent instanceof PathfinderMob){
								PathfinderMob friend = (PathfinderMob)ent;
								if (livingentity!=null){
									if(friend.getTarget()==null){
										friend.setTarget(livingentity);
										if(friend.getNavigation().isDone())friend.getNavigation().moveTo(livingentity.getX()+tx, livingentity.getY(), livingentity.getZ()+tz, 1.2F);
										//if(count>10)break;
									}
								}else{
									if(friend.getNavigation().isDone())friend.getNavigation().moveTo(i+tx, j+ty, k+tz, 1.2F);
								}
							}
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
						alertTime=0;
					}
					if(ent instanceof ERO_Ghast && ((LivingEntity)ent).getHealth()>0){
						++ghost;
					}
					if(ent instanceof EntitySA_T72 && ((LivingEntity)ent).getHealth()>0){
						++ve;
					}
					if(ent instanceof EntitySA_Plane1 && ((LivingEntity)ent).getHealth()>0){
						++ve;
					}
					if(ent instanceof EntitySA_Plane2 && ((LivingEntity)ent).getHealth()>0){
						++ve;
					}
				}
				if(missionTime==1500 && this.level().random.nextInt(3)==1){
					spawnMob(serverworld,15,i1, j1, k1);
					missionTime=1;
				}
				if(count<20){
					if(summonTime>50+count*2){
						for(int l = 0; l < 50; ++l) {
						   {
								/*if (!this.level().hasNearbyAlivePlayer((double)i1, (double)j1, (double)k1, 7.0D)/* && this.level().isUnobstructed(army) && this.level().noCollision(army) && !this.level().containsAnyLiquid(army.getBoundingBox())) */{
									if(ghost<5){
										if(this.level().random.nextInt(4)==1){
											spawnMob(serverworld,9,i1, j1, k1);
										}
									}
									
									if(this.level().random.nextInt(4)==1){
										spawnMob(serverworld,8,i1, j1, k1);
									}
									if(this.level().random.nextInt(16)==1 && ve<3){
										spawnMob(serverworld,11,i1, j1, k1);
									}
									if(this.level().random.nextInt(16)==2 && ve<3){
										spawnMob(serverworld,16,i1, j1, k1);
									}
									if(this.level().random.nextInt(8)==1 && ve<3){
										spawnMob(serverworld,11,i1, j1, k1);
									}
									if(this.level().random.nextInt(15)==1 && ve<2){
										spawnMob(serverworld,12,i1, j1, k1);
									}
									if(this.level().random.nextInt(7)==1 && ve<4){
										spawnMob(serverworld,13,i1, j1, k1);
									}
									if(this.level().random.nextInt(8)==1 && ve<3){
										spawnMob(serverworld,14,i1, j1, k1);
									}
									if(this.level().random.nextInt(6)==1){
										spawnMob(serverworld,5,i1, j1, k1);
									}

									if(this.level().random.nextInt(4)==1){
										spawnMob(serverworld,6,i1, j1, k1);
									}
									if(this.level().random.nextInt(5)==2){
										spawnMob(serverworld,4,i1, j1, k1);
									}
									if(this.level().random.nextInt(6)==2){
										spawnMob(serverworld,7,i1, j1, k1);
									}
									if(this.level().random.nextInt(6)==2){
										spawnMob(serverworld,3,i1, j1, k1);
									}
									if(this.level().random.nextInt(4)==2){
										spawnMob(serverworld,1,i1, j1, k1);
									}
									if(this.level().random.nextInt(7)==2)spawnMob(serverworld,2,i1, j1, k1);
									//if (livingentity != null)army.setTarget(livingentity);
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
	
	public static void spawnMob(ServerLevel serverworld, int id,int x,int y,int z){
		BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos();
		blockpos.set(x, y, z);
		int air =0;
		for(int i = 0; i < 25; ++i){
			BlockState groundState = serverworld.getBlockState(blockpos);
			blockpos.move(Direction.UP);
			if (groundState.isAir()){
				++air;
				if(air>1)break;
			}
			++y;
		}
		PathfinderMob mob = null;
		WeaponVehicleBase vehicle = null;
		if(id==1){
			mob = new ERO_Zombie(ModEntities.ENTITY_EZOMBIE.get(), serverworld);
			setArmor(serverworld,mob);
		}else if(id==2){
			mob = new ERO_Husk(ModEntities.ENTITY_EHUSK.get(), serverworld);
			setArmor(serverworld,mob);
		}else if(id==3){
			mob = new ERO_Skeleton(ModEntities.ENTITY_SKELETON.get(), serverworld);
			mob.finalizeSpawn(serverworld, serverworld.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
			setArmor(serverworld,mob);
		}else if(id==4){
			mob = new ERO_Creeper(ModEntities.ENTITY_CREEPER.get(), serverworld);
		}else if(id==5){
			mob = new ERO_Spider(ModEntities.ERO_SPIDER.get(), serverworld);
		}else if(id==6){
			mob = new ERO_REB(ModEntities.ENTITY_REB.get(), serverworld);
		}else if(id==7){
			mob = new ERO_Pillager(ModEntities.ENTITY_PI.get(), serverworld);
		}else if(id==8){
			mob = new ERO_Phantom(ModEntities.ENTITY_PHA.get(), serverworld);
			y+=8;
			mob.finalizeSpawn(serverworld, serverworld.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
		}else if(id==9){
			mob = new ERO_Ghast(ModEntities.ENTITY_GST.get(), serverworld);
			y+=8;
			mob.finalizeSpawn(serverworld, serverworld.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
		}else if(id==10){
			vehicle = new EntitySA_T55(ModEntities.ENTITY_T55.get(), serverworld);
		}else if(id==11){
			vehicle = new EntitySA_T72(ModEntities.ENTITY_T72.get(), serverworld);
		}else if(id==12){
			vehicle = new EntitySA_T90(ModEntities.ENTITY_T90.get(), serverworld);
		}else if(id==13){
			vehicle = new EntitySA_Plane1(ModEntities.ENTITY_PLANE1.get(), serverworld);
			y+=3;
		}else if(id==14){
			vehicle = new EntitySA_Plane2(ModEntities.ENTITY_PLANE2.get(), serverworld);
			y+=3;
		}else if(id==15){
			mob = new ERO_Giant(ModEntities.ENTITY_GIANT.get(), serverworld);
			mob.finalizeSpawn(serverworld, serverworld.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
		}else if(id==16){
			mob = new ERO_Ravager(ModEntities.ERO_RAV.get(), serverworld);
			mob.finalizeSpawn(serverworld, serverworld.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
		}
		if(vehicle!=null){
			vehicle.setPos(x, y, z);
			serverworld.addFreshEntity(vehicle);
			for(int k2 = 0; k2 < vehicle.seatMaxCount; ++k2){
				ERO_Pillager pilot = new ERO_Pillager(ModEntities.ENTITY_PI.get(),serverworld);
				pilot.setPos(x, y, z);
				pilot.setMoveType(1);
				serverworld.addFreshEntity(pilot);
				pilot.fastRid=true;
			}
		}
		if(mob!=null){
			mob.setPos(x, y, z);
			serverworld.addFreshEntity(mob);
		}
	}
	
	public static void setArmor(ServerLevel serverworld, LivingEntity army){
		if(serverworld.random.nextInt(11)==1){
			army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
		}else if(serverworld.random.nextInt(11)==2){
			army.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
		}else if(serverworld.random.nextInt(11)==3){
			army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			army.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
		}else if(serverworld.random.nextInt(11)==4){
			army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			army.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
		}else if(serverworld.random.nextInt(11)==5){
			army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
			army.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
		}else if(serverworld.random.nextInt(11)==6){
			army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
			army.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
		}else if(serverworld.random.nextInt(11)==7){
			army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			army.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
		}else if(serverworld.random.nextInt(11)==8){
			army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
			army.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
		}else if(serverworld.random.nextInt(11)==9){
			army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			army.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
		}else if(serverworld.random.nextInt(11)==10){
			army.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			army.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
		}
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
		if (this.deathTime == 1){
			if(this.getPortalType()<5){
				this.playSound(SASoundEvent.wreck_explosion.get(), 6F, 1.0F);
				this.level().explode(null, this.getX(), this.getY(), this.getZ(), 6F, false, Level.ExplosionInteraction.NONE);
				if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("LargeExplosionFire", null, this.getX(), this.getY(), this.getZ(), 0,0,0,2);
			}
		}
		if (this.deathTime >= 120) {
			this.discard(); //Forge keep data until we revive player
			if(this.getPortalType()<5){
				this.playSound(SASoundEvent.artillery_impact.get(), 12F, 1.0F);
				this.level().explode(null, this.getX(), this.getY(), this.getZ(), 12F, false, Level.ExplosionInteraction.NONE);
				if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("BigMissileExplosion", null, this.getX(), this.getY(), this.getZ(), 0,0,0,2);
			}
		}
	}
}

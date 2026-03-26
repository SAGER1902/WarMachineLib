package advancearmy.entity.map;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.scores.Team;
import net.minecraft.network.syncher.EntityDataAccessor;  
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerPlayer;

import advancearmy.event.SASoundEvent;
import advancearmy.item.ItemSpawn;
import advancearmy.AdvanceArmy;
import advancearmy.init.ModEntities;

import advancearmy.entity.air.EntitySA_Plane;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;
import advancearmy.entity.air.EntitySA_SU33;
import advancearmy.entity.air.EntitySA_MI24;
import advancearmy.entity.air.EntitySA_Fw020;
import advancearmy.entity.air.EntitySA_YouHun;
import advancearmy.entity.air.EntitySA_Yw010;
import advancearmy.entity.air.EntitySA_F35;
import advancearmy.entity.air.EntitySA_Helicopter;
import advancearmy.entity.air.EntitySA_AH1Z;
import advancearmy.entity.air.EntitySA_AH6;
import advancearmy.entity.air.EntitySA_A10a;
import advancearmy.entity.air.EntitySA_Lapear;

import advancearmy.entity.soldier.EntitySA_Soldier;
import advancearmy.entity.soldier.EntitySA_Conscript;
import advancearmy.entity.soldier.EntitySA_GI;
import advancearmy.entity.soldier.EntitySA_Swun;

import advancearmy.entity.EntitySA_SquadBase;
import advancearmy.entity.land.EntitySA_FTK;
import advancearmy.entity.land.EntitySA_Ember;
import advancearmy.entity.land.EntitySA_T55;
import advancearmy.entity.land.EntitySA_Tank;
import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.land.EntitySA_BMP2;
import advancearmy.entity.land.EntitySA_LaserAA;
import advancearmy.entity.land.EntitySA_Prism;
import advancearmy.entity.land.EntitySA_LAV;
import advancearmy.entity.land.EntitySA_LAVAA;
import advancearmy.entity.land.EntitySA_Bike;
import advancearmy.entity.land.EntitySA_M2A2AA;
import advancearmy.entity.land.EntitySA_M2A2;
import advancearmy.entity.land.EntitySA_MMTank;
import advancearmy.entity.land.EntitySA_Reaper;
import advancearmy.entity.land.EntitySA_Car;
import advancearmy.entity.land.EntitySA_Hmmwv;
import advancearmy.entity.EntitySA_LandBase;
import advancearmy.entity.sea.EntitySA_BattleShip;

import advancearmy.entity.mob.ERO_Husk;
import advancearmy.entity.mob.EntityAohuan;
import advancearmy.entity.mob.ERO_Pillager;
import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Phantom;
import advancearmy.entity.mob.ERO_Ghast;
import advancearmy.entity.mob.EvilPortal;
import advancearmy.entity.mob.ERO_REB;
import advancearmy.entity.mob.ERO_Creeper;
import advancearmy.entity.mob.ERO_Spider;
import advancearmy.entity.mob.ERO_Giant;
import advancearmy.entity.mob.EntityMobSquadBase;
import advancearmy.entity.mob.EntityRaiderSquadBase;
import advancearmy.entity.mob.ERO_Ravager;

import wmlib.api.IBuilding;
import wmlib.api.IEnemy;
import wmlib.api.ITool;
import wmlib.api.IHealthBar;
import wmlib.api.IArmy;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityShell;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.WeaponVehicleBase;

import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import net.minecraft.core.Direction;
public class RandomPoint extends TamableAnimal implements ITool{
	public RandomPoint(EntityType<? extends RandomPoint> p_i48549_1_, Level p_i48549_2_) {
		super(p_i48549_1_, p_i48549_2_);
		nextwave=300;
	}
	public RandomPoint getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
	  return null;
	}
	//public ServerBossEvent show = null;
	private ServerBossEvent EnemyCount = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS))/*.setDarkenScreen(true)*/;
	private ServerBossEvent FriendCount = (ServerBossEvent)(new ServerBossEvent(Component.literal("支援部队即将到达"), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS))/*.setDarkenScreen(true)*/;
	public RandomPoint(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_INV.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return RandomPoint.createMobAttributes();
    }
	
	public Team getTeam() {
		if (this.getOwner()!=null) {
			LivingEntity livingentity = this.getOwner();
			if (livingentity != null) {
				return livingentity.getTeam();
			}
		}
		return super.getTeam();
	}
	
	public void checkDespawn() {
	}
	private static final EntityDataAccessor<Integer> SummonID = SynchedEntityData.<Integer>defineId(RandomPoint.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> WaveID = SynchedEntityData.<Integer>defineId(RandomPoint.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ExtraC = SynchedEntityData.<Integer>defineId(RandomPoint.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("SummonID", this.getSummonID());
		compound.putInt("WaveID", this.getWaveID());
		compound.putInt("ExtraC", this.getExtraC());
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setSummonID(compound.getInt("SummonID"));
	   this.setWaveID(compound.getInt("WaveID"));
	   this.setExtraC(compound.getInt("ExtraC"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(SummonID, Integer.valueOf(0));
		this.entityData.define(WaveID, Integer.valueOf(0));
		this.entityData.define(ExtraC, Integer.valueOf(0));
	}
	public int getSummonID() {
		return ((this.entityData.get(SummonID)).intValue());
	}
	public void setSummonID(int stack) {
		this.entityData.set(SummonID, Integer.valueOf(stack));
	}
	public int getWaveID() {
		return ((this.entityData.get(WaveID)).intValue());
	}
	public void setWaveID(int stack) {
		this.entityData.set(WaveID, Integer.valueOf(stack));
	}
	public int getExtraC() {
		return ((this.entityData.get(ExtraC)).intValue());
	}
	public void setExtraC(int stack) {
		this.entityData.set(ExtraC, Integer.valueOf(stack));
	}
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if(player.isCreative()||this.getWaveID()<=1){
			if(player.isCrouching()){
				this.sendMessage("事件已移除");
				this.discard();
			}else{
				this.sendMessage("在攻击到来之前,潜行+右键可以移除该实体");
			}
		}
		return InteractionResult.PASS;
    }

	public boolean isAttack = false;
	public void startSeenByPlayer(ServerPlayer p_184178_1_) {
		super.startSeenByPlayer(p_184178_1_);
		if(this.getSummonID()<11){
			this.EnemyCount.addPlayer(p_184178_1_);
		}else{
			this.FriendCount.addPlayer(p_184178_1_);
		}
	}
	public void stopSeenByPlayer(ServerPlayer p_184203_1_) {
		super.stopSeenByPlayer(p_184203_1_);
		this.EnemyCount.removePlayer(p_184203_1_);
		this.FriendCount.removePlayer(p_184203_1_);
	}
	public void setCustomName(@Nullable Component p_200203_1_) {
		super.setCustomName(p_200203_1_);
		this.EnemyCount.setName(this.getDisplayName());
		this.FriendCount.setName(Component.literal("支援部队即将到达"));
	}
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}

	int count = 30;
	int totalWave = 4;
	int summon_cyc = 20;
	int startTime = 5;
	
	int summontime = 0;
	int summon_ammol = 0;
	int summon_count = 0;
	int nextwave = 0;
	int supportType = 0;

	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public int flag_time =0;
	int fcyc = 0;
	
	/*public void tick() {
		super.tick();
	}*/
	
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance diff, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
		data = super.finalizeSpawn(level, diff, reason, data, nbt);
		if (!this.level().isClientSide()) {
			if(this.getSummonID()==0){
				if(this.level().random.nextFloat()>0.49F){
					this.setSummonID(11+this.level().random.nextInt(5));
				}else{
					this.setSummonID(this.level().random.nextInt(7));
				}
				int count = 0;
				List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().inflate(50D, 50D, 50D));
				for (Entity target : entities) {
					if(target!=null && (target instanceof Player||target instanceof IArmy)){
						++count;
						if(count>20)break;
					}
				}
				this.setExtraC(count);
				ServerLevel server = (ServerLevel) this.level();
				if (server != null) {
					for (ServerPlayer player : server.getServer().getPlayerList().getPlayers()) {
						if(player.distanceTo(this)<80){
							startSeenByPlayer(player);
						}
					}
				}
			}
		}
		return data;
	}
	
	public void aiStep() {
		if(flag_time<5){
			++fcyc;
			if(fcyc>1){
				++flag_time;
				fcyc=0;
			}
		}else{
			flag_time=0;
		}
    	if(this.setx == 0) {
    		this.setx=((int)this.getX());
    		this.sety=((int)this.getY());
    		this.setz=((int)this.getZ());
    	}
		{
			BlockPos blockpos = new BlockPos((int)(this.setx + 0.5),(int)(this.sety - 1),(int)(this.setz + 0.5));
			BlockState iblockstate = this.level().getBlockState(blockpos);
			if (this.setx != 0 && !iblockstate.isAir()){
				this.moveTo(this.setx,this.sety,this.setz);
			}else{
				this.moveTo(this.setx,this.getY(), this.setz);
			}
		}
		if (this.isAlive()){
			if(this.getSummonID()==1){
				totalWave = 4;
				if(this.getWaveID()>2){
					summon_cyc = 10;
					count = 50;
				}
			}else if(this.getSummonID()==2){
				count = 20;
				totalWave = 4;
				summon_cyc = 25;
				startTime = 100;
				if(this.getWaveID()>3){
					summon_cyc = 15;
					count = 40;
				}
			}else if(this.getSummonID()==3){
				count = 30;
				totalWave = 4;
				summon_cyc = 25;
				startTime = 100;
				if(this.getWaveID()>3){
					summon_cyc = 15;
					count = 40;
				}
			}else if(this.getSummonID()==4){
				count = 25;
				totalWave = 4;
				summon_cyc = 25;
				startTime = 100;
				if(this.getWaveID()>3){
					summon_cyc = 40;
					count = 30;
				}
			}else if(this.getSummonID()==5){
				count = 40;
				totalWave = 5;
				summon_cyc = 25;
				startTime = 100;
				if(this.getWaveID()>3){
					summon_cyc = 15;
					count = 50;
				}
			}else if(this.getSummonID()==6){
				count = 25;
				totalWave = 6;
				summon_cyc = 25;
				startTime = 100;
				if(this.getWaveID()>3){
					summon_cyc = 40;
					count = 30;
				}
			}else{
				startTime = 5;
			}
			
			double ry = (this.getY() + this.level().random.nextInt(5));
			double rx = (this.getX() + this.level().random.nextInt(120) - 60);
			double rz = (this.getZ() + this.level().random.nextInt(120) - 60);
			int randomCount = 1+this.level().random.nextInt(5);
			int randomCount2 = this.level().random.nextInt(10)-5;
			double dx = rx - this.getX();
			double dz = rz - this.getZ();
			double distance = Math.sqrt(dx * dx + dz * dz);

			BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos();
			blockpos.set((double)rx, (double)ry, (double)rz);
			int air = 0;
			for(int i = 0; i < 25; ++i){
				BlockState groundState = this.level().getBlockState(blockpos);
				blockpos.move(Direction.UP);
				++ry;
				if (groundState.isAir()){
					++air;
					if(air>4)break;
				}
			}
			
			if(this.getSummonID()<11){

			if(nextwave>0)--nextwave;
			if(nextwave>0 && this.getWaveID()>0){
				this.EnemyCount.setProgress(nextwave / 300F);
				//this.FriendCount.setProgress(nextwave / 300F);
				String customName = "第" + this.getWaveID()+"波攻势即将到来";
				if(nextwave==250)this.setCustomName(Component.literal(customName));
				if(nextwave==1){
					customName = "Wave " + this.getWaveID();
					this.setCustomName(Component.literal(customName));
					this.sendMessage("第"+this.getWaveID()+"波攻势");
				}
			}else{
				this.EnemyCount.setProgress(summon_count / (float)(count+getExtraC()));
			}
			

			if(this.getWaveID()<totalWave){
				if(summontime<startTime+10)++summontime;
				if(summontime==1){
					long worldTime = this.level().getDayTime();
					long currentDay = worldTime / 24000L;
					this.sendMessage(String.format("§c第%d天 - 入侵来袭！", currentDay));
				}
				if(summontime==startTime+1){
					if(this.getSummonID()==1){
						this.sendMessage("警告,检测到不明生物从四周方向靠近！！！");
					}else if(this.getSummonID()==2){
						this.sendMessage("警告,检测到异变的叛军部队从四周方向靠近！！！");
					}else if(this.getSummonID()==3){
						this.sendMessage("警告,检测到异变的灾厄军团从四周方向靠近！！！");
						this.sendMessage("看起来，他们研发出了螺旋桨飞机？");
					}else if(this.getSummonID()==4){
						this.sendMessage("警告,检测到异变叛军装甲部队从四周方向靠近！！！");
					}else if(this.getSummonID()==5){
						this.sendMessage("警告,检测到异变飞行生物从四周方向靠近！！！");
					}else if(this.getSummonID()==6){
						this.sendMessage("警告,检测到叛军空军部队从四周方向靠近！！！");
					}
				}
				if(nextwave<=0){
					++summon_ammol;
					if(summon_ammol%summon_cyc==0){
							if (!(this.level() instanceof ServerLevel)) {
							}else{
								ServerLevel serverworld = (ServerLevel)this.level();
								if (distance > 55){
									++summon_count;
									for(int k2 = 0; k2 <= randomCount; ++k2){
										if(this.getSummonID()==1){
											if(this.getWaveID()>1 && randomCount>2){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 2, 1);
												if(this.getWaveID()<4)break;
											}else{
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 3, 1);
											}
											if(this.getWaveID()>5 && summon_count%6==0 && k2==1){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 9, 1);
											}
											if(this.getWaveID()>1 && randomCount>4){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 7, 1);
												break;
											}
											if(this.getWaveID()>2 && randomCount>4 || this.getWaveID()==3){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 0, 1);
												if(k2==1){
													if(this.getWaveID()==3)this.sendMessage("警告,检测到不明武装部队靠近！！！");
													this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 23, true);
												}
												if(this.getWaveID()<6)break;
											}
										}else if(this.getSummonID()==2){
											if(this.getWaveID()>2 && summon_count==8 && k2==1){
												this.sendMessage("发现敌军坦克！！！");
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 10, true);
											}
											if(this.getWaveID()>3 && summon_count==10 && k2==1){
												this.sendMessage("原来是敌军轻型装甲车啊");
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 13, true);
											}
											if(this.getWaveID()>6 && summon_count==13 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 11, true);
											}
											if(this.getWaveID()>4 && summon_count==18 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 17, true);
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz-randomCount2, true, 23, true);
												this.callVehicle(this.level(), (double)rx-randomCount2, (double)ry, (double)rz+randomCount2, true, 23, true);
											}
											if(this.getWaveID()>1 && randomCount==4){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 2, 1);
												if(this.getWaveID()<4)break;
											}else{
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 0, 1);
												if(this.getWaveID()<6)break;
											}
											if(this.getWaveID()>1 && randomCount>4){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 7, 1);
												break;
											}
											if(this.getWaveID()>2 && randomCount>3){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 3, 1);
												if(k2==1)this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 23, true);
												if(this.getWaveID()<4)break;
											}
										}else if(this.getSummonID()==3){
											if(this.getWaveID()>1 && summon_count%6==0 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 10, true);
											}
											if(this.getWaveID()>5 && summon_count%8==0 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 11, true);
											}
											if((this.getWaveID()>3||this.getWaveID()==1) && summon_count%3==0 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 15, true);
											}
											if((this.getWaveID()>3||this.getWaveID()==2) && summon_count%5==0 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 23, true);
												this.callVehicle(this.level(), (double)rx-randomCount2, (double)ry, (double)rz-randomCount2, true, 16, true);
											}
											if((this.getWaveID()>3||this.getWaveID()==2) && summon_count%6==0 && k2<3){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 10, 1);
											}
											
											if((this.getWaveID()>3||this.getWaveID()==3) && summon_count%6==0 && k2==1){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 9, 1);
											}
											if((this.getWaveID()>4) && summon_count%8==0){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 5, 1);
												break;
											}
											if(this.getWaveID()>1 && randomCount==4){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 0, 1);
												if(this.getWaveID()<4)break;
											}else{
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 1, 1);
												if(this.getWaveID()<6)break;
											}
											if(this.getWaveID()>1 && randomCount>4){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 2, 1);
												break;
											}
											if(this.getWaveID()>2 && randomCount>3){
												if(k2==1)this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 23, true);
												if(k2==1)this.callVehicle(this.level(), (double)rx-randomCount2, (double)ry, (double)rz-randomCount2, true, 23, true);
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 3, 1);
												if(this.getWaveID()<4)break;
											}
											if(this.getWaveID()>2 && randomCount>3){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry+50, (double)rz+randomCount2, true, 4, randomCount);
												if(this.getWaveID()<4)break;
											}
										}else if(this.getSummonID()==4){
											if(summon_count==8 && k2<2){
												this.callVehicle(this.level(), (double)rx+randomCount2+k2*2, (double)ry, (double)rz+randomCount2, true, 10, true);
											}
											if(this.getWaveID()>2 && summon_count==13 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2+k2*2, true, 11, true);
											}
											if(this.getWaveID()>3 && summon_count==18 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2+k2*2, (double)ry, (double)rz+randomCount2, true, 12, true);
											}
											if(this.getWaveID()==7 && summon_count==19 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2+k2*2, true, 5, true);
											}
											if(this.getWaveID()>1 && randomCount==4){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 0, 1);
											}
										}else if(this.getSummonID()==5){
											if(this.getWaveID()>1 && randomCount==4){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry+60, (double)rz+randomCount2, true, 5, 1);
												break;
											}else{
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry+60, (double)rz+randomCount2, true, 4, 1);
												if(this.getWaveID()<3)break;
											}
											if(this.getWaveID()>1 && randomCount>3){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry+60, (double)rz+randomCount2, true, 4, 1);
											}
											if(this.getWaveID()>2 && randomCount>4){
												this.callMob(serverworld, (double)rx+randomCount2, (double)ry+60, (double)rz+randomCount2, true, 5, 1);
												if(this.getWaveID()<4)break;
											}
										}else if(this.getSummonID()==6){
											if(this.getWaveID()>0 && summon_count%6==0 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 16, true);
											}
											if(summon_count%4==0 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2+k2*2, (double)ry, (double)rz+randomCount2, true, 15, true);
											}
											if(this.getWaveID()>1 && summon_count==12 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2+k2*2, true, 4, true);
											}
											if(this.getWaveID()>2 && summon_count==18 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2+k2*2, (double)ry, (double)rz+randomCount2, true, 17, true);
											}
											if(this.getWaveID()>3 && summon_count==19 && k2==1){
												this.sendMessage("天上好大的敌军飞机,我们一定要小心");
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2+k2*2, true, 5, true);
											}
											if(this.getWaveID()>4 && summon_count==14 && k2==1){
												this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2+k2*2, true, 5, true);
											}
										}
									}
								}
							}
					}
					if(summon_count>=count+getExtraC()){
						this.setWaveID(this.getWaveID()+1);
						nextwave=300;
						summon_count=0;
						summon_ammol = 0;
					}
					//if(this.getWaveID()>=totalWave)this.summontime = 0;
				}
			}else{
				this.discard();
			}
			
			}else{
				//this.FriendCount.setName(Component.literal("支援部队即将到达="+this.getSummonID()));
				if(this.getOwner()!=null && this.getOwner() instanceof Player){
					Player player = (Player)this.getOwner();
					if(summon_count==1){
						this.sendMessage("§a一支部队前来支援"+player.getGameProfile().getName());
					}
				}
				if(summon_count<300){
					++summon_count;
					this.FriendCount.setProgress(summon_count / 300F);
				}else{
					if(this.getSummonID()>10)this.spawnSupport(randomCount2,rx,ry,rz);
				}
			}
		}
		super.aiStep();
    }
	
	public void sendMessage(String message) {
		// Check if we are on the server side
		if (!this.level().isClientSide()) {
			// Get the Minecraft server instance from the world
			ServerLevel server = (ServerLevel) this.level();
			// Check if the server instance is not null
			if (server != null) {
				// Iterate over all players on the server
				for (ServerPlayer player : server.getServer().getPlayerList().getPlayers()) {
					if(player.distanceTo(this)<80){
						// Create a translation text component with the message
						Component textComponent = Component.translatable(message);
						// Send the message to the player
						player.sendSystemMessage(textComponent);
						player.playSound(SASoundEvent.command_say.get(), 2.0F, 1.0F);
					}
				}
			}
		}
	}
	
	public void callMob(ServerLevel world1, double ix, double iy, double iz, boolean isAttackMove, int id, int c) {
		if(!world1.isClientSide()){
			for(int k2 = 0; k2 < c; ++k2){
				Mob mob = null;
				EntityMobSquadBase mobs = null;
				EntityRaiderSquadBase radiers = null;
				if(id==0){
					mobs = new ERO_REB(ModEntities.ENTITY_REB.get(), world1);
				}else if(id==1){
					radiers = new ERO_Pillager(ModEntities.ENTITY_PI.get(), world1);
				}else if(id==2){
					mob = new ERO_Zombie(ModEntities.ENTITY_EZOMBIE.get(), world1);
				}else if(id==3){
					mob = new ERO_Husk(ModEntities.ENTITY_EHUSK.get(), world1);
				}else if(id==4){
					mob = new ERO_Phantom(ModEntities.ENTITY_PHA.get(), world1);
					mob.finalizeSpawn(world1, this.level().getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
				}else if(id==5){
					mob = new ERO_Ghast(ModEntities.ENTITY_GST.get(), world1);
					mob.finalizeSpawn(world1, this.level().getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
				}else if(id==6){
					mobs = new EntityAohuan(ModEntities.ENTITY_AOHUAN.get(), world1);
				}else if(id==7){
					mob = new ERO_Creeper(ModEntities.ENTITY_CREEPER.get(), world1);
				}else if(id==8){
					mob = new EvilPortal(ModEntities.ENTITY_POR.get(), world1);
				}else if(id==9){
					mob = new ERO_Spider(ModEntities.ERO_SPIDER.get(), world1);
				}else if(id==10){
					mob = new ERO_Ravager(ModEntities.ERO_RAV.get(), world1);
				}else if(id==11){
					mob = new ERO_Giant(ModEntities.ENTITY_GIANT.get(), world1);
				}
				if(mob!=null){
					int randomCount2 = this.level().random.nextInt(10)-5;
					mob.setPos(ix, iy, iz);
					world1.addFreshEntity(mob);
					mob.getNavigation().moveTo(this.getX()+randomCount2, this.getY(), this.getZ()+randomCount2, 1.2F);
				}
				if(mobs!=null){
					int randomCount2 = this.level().random.nextInt(10)-5;
					mobs.setPos(ix, iy, iz);
					world1.addFreshEntity(mobs);
					if(isAttackMove){
						mobs.setMoveType(4);
					}else{
						mobs.setMoveType(2);
					}
					mobs.canPara = true;
					mobs.setMovePosX((int)this.getX()+randomCount2);
					mobs.setMovePosY((int)this.getY());
					mobs.setMovePosZ((int)this.getZ()+randomCount2);
				}
				if(radiers!=null){
					int randomCount2 = this.level().random.nextInt(10)-5;
					radiers.setPos(ix, iy, iz);
					world1.addFreshEntity(radiers);
					if(isAttackMove){
						radiers.setMoveType(4);
					}else{
						radiers.setMoveType(2);
					}
					radiers.canPara = true;
					radiers.setMovePosX((int)this.getX()+randomCount2);
					radiers.setMovePosY((int)this.getY());
					radiers.setMovePosZ((int)this.getZ()+randomCount2);
				}
			}
		}
	}
	public void callSoldier(Level world1, LivingEntity caller, double ix, double iy, double iz, boolean isAttackMove, int id) {
		if(!world1.isClientSide()){
			EntitySA_SquadBase soldier = null;
			if(id==0){
				soldier = new EntitySA_Soldier(ModEntities.ENTITY_SOLDIER.get(), world1);
			}else if(id==1){
				soldier = new EntitySA_GI(ModEntities.ENTITY_GI.get(), world1);
			}else if(id==2){
				soldier = new EntitySA_Conscript(ModEntities.ENTITY_CONS.get(), world1);
			}
			if(soldier!=null){
				int randomCount2 = this.level().random.nextInt(10)-5;
				soldier.setPos(ix, iy, iz);
				world1.addFreshEntity(soldier);
				if(isAttackMove){
					soldier.setMoveType(4);
				}else{
					soldier.setMoveType(2);
				}
				soldier.canPara = true;
				soldier.setMovePosX((int)this.getX()+randomCount2);
				soldier.setMovePosY((int)this.getY());
				soldier.setMovePosZ((int)this.getZ()+randomCount2);
				if(this.getOwner()!=null && this.getOwner() instanceof Player){
					Player player = (Player)this.getOwner();
					soldier.tame(player);
				}
				if(this.getTeam()!=null && this.getTeam() instanceof PlayerTeam){
					this.level().getScoreboard().addPlayerToTeam(soldier.getUUID().toString(), (PlayerTeam)this.getTeam());
				}
			}
		}
	}
	
	public void callVehicle(Level world1, double ix, double iy, double iz, boolean isAttackMove, int id, boolean isEnemy) {
		if(world1.isClientSide())return;
		int height = 1;
		int driver_type = 0;
		boolean have_driver = true;
		boolean center = false;
		int driver_count = 1;
		WeaponVehicleBase vehicle=null;
		if(id == 1) {
			vehicle = new EntitySA_Tank(ModEntities.ENTITY_TANK.get(), world1);
		}else if(id == 2){
			vehicle = new EntitySA_AH1Z(ModEntities.ENTITY_AH1Z.get(), world1);
			height = 80;
		}else if(id == 3){
			vehicle = new EntitySA_FTK(ModEntities.ENTITY_FTK.get(), world1);
		}else if(id == 4){
			vehicle = new EntitySA_MI24(ModEntities.ENTITY_MI24.get(), world1);
			driver_type=2;
			height = 80;
		}else if(id == 5){
			vehicle = new EntitySA_SU33(ModEntities.ENTITY_SU33.get(), world1);
			height = 80;
		}else if(id == 6){
			vehicle = new EntitySA_A10a(ModEntities.ENTITY_A10A.get(), world1);
			height = 80;
		}else if(id == 7){
			vehicle = new EntitySA_F35(ModEntities.ENTITY_F35.get(), world1);
			height = 80;
		}else if(id == 8){
			vehicle = new EntitySA_Prism(ModEntities.ENTITY_PRISM.get(), world1);
		}else if(id == 9){
			vehicle = new EntitySA_M2A2AA(ModEntities.ENTITY_M2A2AA.get(), world1);
		}else if(id == 10){
			vehicle = new EntitySA_T55(ModEntities.ENTITY_T55.get(), world1);
			driver_type=2;
		}else if(id == 11){
			vehicle = new EntitySA_T72(ModEntities.ENTITY_T72.get(), world1);
			driver_type=2;
		}else if(id == 12){
			vehicle = new EntitySA_T90(ModEntities.ENTITY_T90.get(), world1);
			driver_type=2;
		}else if(id == 13){
			vehicle = new EntitySA_BMP2(ModEntities.ENTITY_BMP2.get(), world1);
			driver_type=2;
		}else if(id == 14){
			vehicle = new EntitySA_Lapear(ModEntities.ENTITY_LAPEAR.get(), world1);
			height = 80;
		}else if(id == 15){
			vehicle = new EntitySA_Plane1(ModEntities.ENTITY_PLANE1.get(), world1);
			driver_type=1;
			height = 80;
		}else if(id == 16){
			vehicle = new EntitySA_Plane2(ModEntities.ENTITY_PLANE2.get(), world1);
			driver_type=1;
			height = 80;
		}else if(id == 17){
			vehicle = new EntitySA_AH6(ModEntities.ENTITY_AH6.get(), world1);
			height = 80;
		}else if(id == 18){
			vehicle = new EntitySA_Plane(ModEntities.ENTITY_PLANE.get(), world1);
			height = 80;
		}else if(id == 19){
			vehicle = new EntitySA_LAVAA(ModEntities.ENTITY_LAVAA.get(), world1);
		}else if(id == 20){
			vehicle = new EntitySA_Helicopter(ModEntities.ENTITY_HELI.get(), world1);
			height = 80;
		}else if(id == 21){
			vehicle = new EntitySA_LaserAA(ModEntities.ENTITY_LAA.get(), world1);
			center = true;
		}else if(id == 23){
			vehicle = new EntitySA_Car(ModEntities.ENTITY_CAR.get(), world1);
		}else if(id == 24){
			vehicle = new EntitySA_Hmmwv(ModEntities.ENTITY_HMMWV.get(), world1);
		}else if(id == 25){
			vehicle = new EntitySA_MMTank(ModEntities.ENTITY_MMTANK.get(), world1);
			driver_type=2;
		}else if(id == 26){
			vehicle = new EntitySA_Reaper(ModEntities.ENTITY_REAPER.get(), world1);
			driver_type=2;
		}else if(id == 27){
			vehicle = new EntitySA_LAV(ModEntities.ENTITY_LAV.get(), world1);
		}else if(id == 28){
			vehicle = new EntitySA_Bike(ModEntities.ENTITY_BIKE.get(), world1);
		}
		
		if(vehicle!=null){
			int randomCount2 = this.level().random.nextInt(10)-5;
			if(center){
				if(height==1)this.playSound(SASoundEvent.csk.get(), 6.0F, 1.0F);
				vehicle.moveTo(this.getX()+randomCount2, this.getY()+height, this.getZ()+randomCount2, vehicle.yHeadRot, vehicle.getXRot());
			}else{
				vehicle.setPos(ix, iy+height, iz);
			}
			world1.addFreshEntity(vehicle);
			if(isEnemy){
				if (have_driver){//
					for(int k2 = 0; k2 < vehicle.seatMaxCount; ++k2){
						if(driver_type==1){
							ERO_Pillager pilot = new ERO_Pillager(ModEntities.ENTITY_PI.get(),world1);
							pilot.setPos(ix, iy+height, iz);
							pilot.setMoveType(1);
							world1.addFreshEntity(pilot);
							pilot.fastRid=true;
						}else{
							ERO_REB pilot = new ERO_REB(ModEntities.ENTITY_REB.get(),world1);
							pilot.setPos(ix, iy+height, iz);
							pilot.setMoveType(1);
							world1.addFreshEntity(pilot);
							pilot.fastRid=true;
						}
					}
				}
				//vehicle.setTarget(null);
				//vehicle.setAttacking(false);
				vehicle.setTargetType(2);
			}else{
				if(this.getOwner()!=null && this.getOwner() instanceof Player){
					Player player = (Player)this.getOwner();
					vehicle.tame(player);
				}
				if (have_driver){
					for(int k2 = 0; k2 < vehicle.seatMaxCount; ++k2){
						if(driver_type==2){
							EntitySA_Conscript pilot = new EntitySA_Conscript(ModEntities.ENTITY_CONS.get(),world1);
							pilot.setPos(ix, iy+height, iz);
							pilot.setMoveType(1);
							world1.addFreshEntity(pilot);
							pilot.fastRid=true;
							if(this.getTeam()!=null && this.getTeam() instanceof PlayerTeam){
								this.level().getScoreboard().addPlayerToTeam(pilot.getUUID().toString(), (PlayerTeam)this.getTeam());
							}
							if(this.getOwner()!=null && this.getOwner() instanceof Player){
								Player player = (Player)this.getOwner();
								pilot.tame(player);
							}
							//vehicle.catchPassenger(pilot);
						}else{
							EntitySA_Soldier pilot = new EntitySA_Soldier(ModEntities.ENTITY_SOLDIER.get(),world1);
							pilot.setPos(ix, iy+height, iz);
							pilot.setMoveType(1);
							world1.addFreshEntity(pilot);
							pilot.fastRid=true;
							if(this.getTeam()!=null && this.getTeam() instanceof PlayerTeam){
								this.level().getScoreboard().addPlayerToTeam(pilot.getUUID().toString(), (PlayerTeam)this.getTeam());
							}
							if(this.getOwner()!=null && this.getOwner() instanceof Player){
								Player player = (Player)this.getOwner();
								pilot.tame(player);
							}
							//vehicle.catchPassenger(pilot);
						}
					}
				}
				if(this.getTeam()!=null && this.getTeam() instanceof PlayerTeam){
					this.level().getScoreboard().addPlayerToTeam(vehicle.getUUID().toString(), (PlayerTeam)this.getTeam());
				}
				vehicle.setTargetType(3);
			}
			if(isAttackMove){
				vehicle.setMoveType(4);
			}else{
				vehicle.setMoveType(2);
			}
			if(height>40)vehicle.setMoveType(0);
			vehicle.setMovePosX((int)this.getX()+randomCount2);
			vehicle.setMovePosY((int)this.getY());
			vehicle.setMovePosZ((int)this.getZ()+randomCount2);
			vehicle.setRemain1(vehicle.magazine);
			vehicle.setRemain2(vehicle.magazine2);
			vehicle.setRemain3(vehicle.magazine3);
			vehicle.setRemain4(vehicle.magazine4);
			vehicle.movePower=vehicle.throttleMax-2;
			vehicle.throttle=vehicle.throttleMax-2;
		}
	}
	
	void spawnSupport(int randomCount2, double rx, double ry, double rz){
		{
			float randomF = this.level().random.nextFloat();
			if (randomF < 0.02f) {          // 原 1/50
				supportType = 5;
			} else if (randomF < 0.05f) {   // 0.02 + 0.03 (原1/35)
				supportType = 4;
			} else if (randomF < 0.10f) {   // 0.05 + 0.05 (原1/20)
				supportType = 3;
			} else if (randomF < 0.20f) {   // 0.10 + 0.10 (原1/10)
				supportType = 2;
			} else if (randomF < 0.4667f) { // 0.20 + 0.2667 (原1/3.75≈0.2667)
				supportType = 1;
			} else if (randomF < 0.80f) {   // 0.4667 + 0.3333 (原1/3≈0.3333)
				supportType = 0;
			}
				
			if(this.getSummonID()==11){//soldier + car
				if(supportType==0){
					{
						this.sendMessage("4名游骑兵加入了你的军队");
						for(int k2 = 0; k2 < 4; ++k2){
							this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 0);
						}
					}
				}
				if(supportType==1){
					{
						this.sendMessage("一队盟军大兵加入了你的军队");
						for(int k2 = 0; k2 < 6; ++k2){
							this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 1);
						}
					}
				}
				if(supportType==2){
					{
						this.sendMessage("一队动员兵精英部队已经加入战场进行支援");
						for(int k2 = 0; k2 < 10; ++k2){
							this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 2);
						}
					}
				}
				if(supportType==3){
					{
						this.sendMessage("机动部队加入了你的军队");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 24, false);
						this.callVehicle(this.level(), (double)rx-randomCount2, (double)ry, (double)rz-randomCount2, true, 24, false);
					}
				}
				if(supportType==4||supportType==5){
					{
						this.sendMessage("游骑兵小队已经加入战场进行支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 1, false);
						for(int k2 = 0; k2 < 8; ++k2){
							this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 0);
						}
					}
				}
				this.discard();
			}
			if(this.getSummonID()==12){//apc
				if(supportType==0){
					{
						this.sendMessage("一辆BMP2装甲车已经前往支援");
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 13, false);
					}
				}
				if(supportType==1){
					{
						this.sendMessage("LAV25防空型装甲车小队已经前往支援");
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 19, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 19, false);
						for(int k2 = 0; k2 < 12; ++k2){
							this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 0);
						}
					}
				}
				if(supportType==2){
					{
						this.sendMessage("我们的苏军盟友派出了一支装甲小队进行支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 11, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 13, false);
						for(int k2 = 0; k2 < 12; ++k2){
							this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 2);
						}
					}
				}
				if(supportType==3){
					{
						this.sendMessage("LAV25防空型装甲车小队已经前往支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 19, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 19, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*-5, (double)ry, (double)rz+randomCount2*-5, true, 19, false);
						for(int k2 = 0; k2 < 12; ++k2){
							this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 0);
						}
					}
				}
				if(supportType==4){
					{
						this.sendMessage("M2A2防空型装甲车小队已经前往支援");
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 9, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 9, false);
						for(int k2 = 0; k2 < 12; ++k2){
							this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 0);
						}
					}
				}
				if(supportType==5){
					{
						this.sendMessage("两辆光棱坦克已经前往支援！");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz, true, 8, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+10, true, 8, false);
					}
				}
				this.discard();
			}
			if(this.getSummonID()==13){//tank
				if(supportType==1||supportType==0){
					{
						this.sendMessage("一辆M1坦克已经前往进行支援");
						this.callVehicle(this.level(), (double)rx-randomCount2*5, (double)ry, (double)rz-randomCount2*5, true, 1, false);
					}
				}
				if(supportType==2){
					{
						this.sendMessage("两辆T72坦克已经前往进行支援");
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 11, false);
						this.callVehicle(this.level(), (double)rx-randomCount2*5, (double)ry, (double)rz-randomCount2*5, true, 11, false);
					}
				}
				if(supportType==3){
					{
						this.sendMessage("一支装甲小队已经前往进行支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 1, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 9, false);
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 8, false);
						for(int k2 = 0; k2 < 12; ++k2){
							this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 0);
						}
					}
				}
				if(supportType==4){
					{
						this.sendMessage("一支犀牛坦克小队前来支援你");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 3, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 3, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*-5, (double)ry, (double)rz+randomCount2*-5, true, 3, false);
						for(int k2 = 0; k2 < 12; ++k2){
							this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 2);
						}
					}
				}
				if(supportType==5){
					this.sendMessage("游骑兵部队已经加入战场进行支援");
					for(int k2 = 0; k2 < 10; ++k2){
						this.callSoldier(this.level(), null, (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 0);
					}
					this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 24, false);
					this.callVehicle(this.level(), (double)rx-randomCount2, (double)ry, (double)rz-randomCount2, true, 24, false);
				}
				this.discard();
			}
			if(this.getSummonID()==14){//heli
				if(supportType==1||supportType==0){
					{
						this.sendMessage("一架AH6直升机已经加入战场进行支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 17, false);
					}
				}
				if(supportType==2){
					{
						this.sendMessage("一架AH1Z直升机已经加入战场进行支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 2, false);
					}
				}
				if(supportType==3){
					{
						this.sendMessage("一架MI24直升机已经加入战场进行支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 4, false);
					}
				}
				if(supportType==4||supportType==5){
					{
						this.sendMessage("两架AH-64直升机已经前往支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 20, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 20, false);
					}
				}
				this.discard();
			}
			if(this.getSummonID()==15){//plane
				if(supportType==1||supportType==0){
					{
						this.sendMessage("一架黑鹰战机已经加入战场进行支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, false, 18, false);
					}
				}
				if(supportType==2){
					{
						this.sendMessage("两架A-10A型攻击机已经赶到战场！该开罐头了！！！");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 6, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 6, false);
					}
				}
				if(supportType==3){
					{
						this.sendMessage("三架黑鹰战机已经前往支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 18, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 18, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*10, (double)ry, (double)rz+randomCount2*10, true, 18, false);
					}
				}
				if(supportType==4){
					{
						this.sendMessage("两架F35战机已经前往支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 7, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 7, false);
					}
				}
				if(supportType==5){
					{
						this.sendMessage("三架SU33战机已经前往支援");
						this.callVehicle(this.level(), (double)rx+randomCount2, (double)ry, (double)rz+randomCount2, true, 5, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*5, (double)ry, (double)rz+randomCount2*5, true, 5, false);
						this.callVehicle(this.level(), (double)rx+randomCount2*10, (double)ry, (double)rz+randomCount2*10, true, 5, false);
					}
				}
				this.discard();
			}
		}
	}
}

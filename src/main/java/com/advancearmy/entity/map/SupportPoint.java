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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

import advancearmy.item.ItemSpawn;
import advancearmy.AdvanceArmy;
import advancearmy.init.ModEntities;
import wmlib.api.IHealthBar;
import wmlib.api.IBuilding;
import wmlib.api.IEnemy;

import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;

import advancearmy.entity.soldier.EntitySA_Soldier;
import advancearmy.entity.soldier.EntitySA_Conscript;
import advancearmy.entity.soldier.EntitySA_GI;
import advancearmy.entity.soldier.EntitySA_OFG;
import advancearmy.entity.soldier.EntitySA_MWDrone;
import advancearmy.entity.land.EntitySA_RockTank;
import advancearmy.entity.land.EntitySA_STAPC;

import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.sea.EntitySA_BattleShip;
import advancearmy.entity.land.EntitySA_FTK_H;
import advancearmy.entity.air.EntitySA_Fw020;
import advancearmy.entity.land.EntitySA_Ember;
import advancearmy.entity.air.EntitySA_YouHun;
import advancearmy.entity.air.EntitySA_Yw010;
import advancearmy.entity.air.EntitySA_F35;
import advancearmy.entity.air.EntitySA_A10a;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.api.ITool;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityShell;
import advancearmy.entity.soldier.EntitySA_Swun;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.event.SASoundEvent;
import advancearmy.entity.map.ParticlePoint;
public class SupportPoint extends TamableAnimal implements ITool{
	public SupportPoint(EntityType<? extends SupportPoint> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.noCulling = true;
	}
	public SupportPoint getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
	  return null;
	}
	/*
      PINK("pink", TextFormatting.RED),
      BLUE("blue", TextFormatting.BLUE),
      RED("red", TextFormatting.DARK_RED),
      GREEN("green", TextFormatting.GREEN),
      YELLOW("yellow", TextFormatting.YELLOW),
      PURPLE("purple", TextFormatting.DARK_BLUE),
      WHITE("white", TextFormatting.WHITE);
	*/
	//private final ServerBossEvent FriendCount = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS))/*.setDarkenScreen(true)*/;
	//private final ServerBossEvent EnemyCount = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS))/*.setDarkenScreen(true)*/;
	public SupportPoint(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_SPT.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return SupportPoint.createMobAttributes();
    }
	
	public void checkDespawn() {
	}
	private static final EntityDataAccessor<Integer> SummonID = SynchedEntityData.<Integer>defineId(SupportPoint.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("SummonID", this.getSummonID());
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setSummonID(compound.getInt("SummonID"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(SummonID, Integer.valueOf(0));
	}
	public int getSummonID() {
	return ((this.entityData.get(SummonID)).intValue());
	}
	public void setSummonID(int stack) {
	this.entityData.set(SummonID, Integer.valueOf(stack));
	}
	public boolean canCollideWith(Entity entity) {
		return false;
	}
	/*public boolean canBeCollidedWith() {//
		return false;
	}*/
	public void push(Entity entity) {
		
	}
	
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		return InteractionResult.PASS;
    }

	public boolean isAttack = false;
	
	/*public void startSeenByPlayer(ServerPlayer p_184178_1_) {
	  super.startSeenByPlayer(p_184178_1_);
	  if(isEnemyRespawn){
		  this.EnemyCount.addPlayer(p_184178_1_);
	  }else{
		  this.FriendCount.addPlayer(p_184178_1_);
	  }
	}
	public void stopSeenByPlayer(ServerPlayer p_184203_1_) {
	  super.stopSeenByPlayer(p_184203_1_);
	  if(isEnemyRespawn){
			this.EnemyCount.removePlayer(p_184203_1_);
	  }else{
			this.FriendCount.removePlayer(p_184203_1_);
	  }
	}
	public void setCustomName(@Nullable Component p_200203_1_) {
		super.setCustomName(p_200203_1_);
		//this.EnemyCount.setName(this.getDisplayName());
		if(isEnemyRespawn){
			this.EnemyCount.setName(this.getDisplayName());
		}else{
			this.FriendCount.setName(this.getDisplayName());
		}
	}*/
	
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}
	
	/*protected void tickDeath() {

	}*/
	public boolean needaim = false;
	int count = 1;
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public int staytime = 0;
	public int startTime = 50;
	public int summontime = 0;
	public int summon_ammol = 0;
	public int summon_count = 0;
	public int summon_cyc = 10;
	public void aiStep() {
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
			++summontime;
			++staytime;
			if(this.getSummonID() == 8){
				summon_cyc = 30;
				count = 4;
				isAttack = true;
			}else if(this.getSummonID() == 9){
				count = 4;
				isAttack = true;
			}else if(this.getSummonID() == 10){
				summon_cyc = 15;
				count = 20;
				isAttack = true;
			}
			if(this.getSummonID()>10 && this.getSummonID()<15){
				needaim = true;
				isAttack = true;
			}
			
			{
				if(summontime>startTime/* && this.getSummonID()>0*/){
					int ve = 0;
					if (!(this.level() instanceof ServerLevel)) {
					}else{
						ServerLevel serverworld = (ServerLevel)this.level();
						LivingEntity caller = this;
						if(this.getOwner()!=null)caller = this.getOwner();
						++summon_ammol;
						if(summon_ammol>summon_cyc){
							if(this.getSummonID()==10){
								int rax = this.level().random.nextInt(50);
								int raz = this.level().random.nextInt(50);
								this.callShell(serverworld,caller, this.getX() - 25 + rax, this.getY() + 80+summon_count*5, this.getZ() - 25 + raz, this.getSummonID());
							}
							if(this.getSummonID()==15){
								int rax = this.level().random.nextInt(10);
								int raz = this.level().random.nextInt(10);
								this.callShell(serverworld,caller, this.getX() - 5 + rax, this.getY(), this.getZ() - 5 + raz, this.getSummonID());
							}
							if(this.getSummonID()==17){
								int rax = this.level().random.nextInt(10);
								int raz = this.level().random.nextInt(10);
								this.callShell(serverworld,caller, this.getX() - 5 + rax, this.getY(), this.getZ() - 5 + raz, this.getSummonID());
							}
							if(this.getSummonID()==18){
								this.callSupport(serverworld,caller,this.getX()+5,this.getY()+count*2,this.getZ(),0,51, summon_count);
								this.callSupport(serverworld,caller,this.getX()-5,this.getY()+count*2,this.getZ(),0,51, summon_count);
								
								this.callSupport(serverworld,caller,this.getX()+5,this.getY()+count*2,this.getZ()-10,0,52, summon_count);
								this.callSupport(serverworld,caller,this.getX()-5,this.getY()+count*2,this.getZ()-10,0,52, summon_count);
								
								this.callSupport(serverworld,caller,this.getX()+15,this.getY()+count*2,this.getZ(),10,53, summon_count);
								this.callSupport(serverworld,caller,this.getX()-15,this.getY()+count*2,this.getZ(),10,53, summon_count);
								this.callSupport(serverworld,caller,this.getX()+20,this.getY()+count*2,this.getZ()-10,0,53, summon_count);
								this.callSupport(serverworld,caller,this.getX()-20,this.getY()+count*2,this.getZ()-10,0,53, summon_count);
							}else{
								this.callSupport(serverworld,caller,this.getX(),this.getY()+count*2,this.getZ(),80,this.getSummonID(), summon_count);
							}

							++summon_count;
							summon_ammol = 0;
						}
						if(summon_count>=count)this.summontime = 0;
					}
				}
			}
			if(staytime>count*summon_cyc+startTime||needaim && summontime>startTime)this.discard();
		}
		super.aiStep();
    }
	public void callShell(Level world1, LivingEntity caller, double ix, double iy, double iz, int id) {
		if(id==15){
			EntityMissile bullet = new EntityMissile(world1, caller, ix, iy-1, iz, caller);
			bullet.modid="advancearmy";
			bullet.fly_sound="advancearmy.missile_fly1";
			bullet.isRad = true;
			bullet.timemax=1000;
			bullet.power = 2000;
			bullet.setGravity(0.01F);
			bullet.setExLevel(35);
			bullet.hitEntitySound=SASoundEvent.nuclear_exp.get();
			bullet.hitBlockSound=SASoundEvent.nuclear_exp.get();
			bullet.setBulletType(8);
			bullet.setModel("advancearmy:textures/entity/bullet/nuclear_missile.obj");
			bullet.setTex("advancearmy:textures/entity/bullet/nuclear.png");
			bullet.setFX("BigMissileTrail");
			//bullet.flame = true;
			bullet.moveTo(caller.getX(), caller.getY()+100, caller.getZ(), caller.getYRot(), caller.getXRot());
			bullet.shootFromRotation(caller, caller.getXRot(), caller.getYRot(), 0.0F, 7F, 1F);
			if (!world1.isClientSide) world1.addFreshEntity(bullet);
		}else if(id==17){
			EntityMissile bullet = new EntityMissile(world1, caller, ix, iy-1, iz, caller);
			bullet.modid="advancearmy";
			bullet.fly_sound="advancearmy.missile_fly1";
			bullet.isRad = true;
			bullet.radType = 2;
			bullet.timemax=700;
			bullet.power = 60;
			bullet.setGravity(0.5F);
			bullet.setExLevel(1);
			bullet.setBulletType(8);
			bullet.setModel("advancearmy:textures/entity/bullet/nuclear_missile2.obj");
			bullet.setTex("advancearmy:textures/entity/bullet/nuclear.png");
			bullet.setFX("BigMissileTrail");
			//bullet.flame = true;
			bullet.moveTo(caller.getX(), caller.getY()+100, caller.getZ(), caller.getYRot(), caller.getXRot());
			bullet.shootFromRotation(caller, caller.getXRot(), caller.getYRot(), 0.0F, 9F, 1F);
			if (!world1.isClientSide) world1.addFreshEntity(bullet);
		}else{
			EntityShell shell = new EntityShell(world1, caller);
			shell.moveTo(ix, iy, iz, caller.getYRot(), caller.getXRot());
			shell.modid="advancearmy";
			shell.fly_sound="advancearmy.shell_fly";
			shell.timemax=700;
			shell.power = 80;
			shell.setGravity(0.1F);
			shell.setExLevel(6);
			shell.hitEntitySound=SASoundEvent.artillery_impact.get();
			shell.hitBlockSound=SASoundEvent.artillery_impact.get();
			shell.setModel("advancearmy:textures/entity/bullet/bulletcannon.obj");
			shell.setTex("advancearmy:textures/entity/bullet/bullet.png");
			//shell.shootFromRotation(caller, caller.getXRot(), caller.getYRot(), 0.0F, 9F, 1F);
			if (!world1.isClientSide) world1.addFreshEntity(shell);
		}
	}
	
	public void callSupport(Level world1, LivingEntity caller, double ix, double iy, double iz, double range, int id, int count) {
		if(caller != null){
			int height = 80;
			boolean isone = false;
			boolean have_driver = false;
			boolean center = false;
			
			WeaponVehicleBase attacker=null;
			WeaponVehicleBase trans=null;
			if(id == 1) {
				trans = new EntitySA_Yw010(ModEntities.ENTITY_YW010.get(), world1);
			}else if(id == 2){
				attacker = new EntitySA_Fw020(ModEntities.ENTITY_FW020.get(), world1);
				center = true;
				height = 1;
			}else if(id == 3){
				attacker = new EntitySA_YouHun(ModEntities.ENTITY_YOUHUN.get(), world1);
				center = true;
				height = 1;
			}else if(id == 4){
				attacker = new EntitySA_Ember(ModEntities.ENTITY_EMBER.get(), world1);
				center = true;
			}else if(id == 5){
				attacker = new EntitySA_FTK_H(ModEntities.ENTITY_FTK_H.get(), world1);
				center = true;
			}else if(id == 6){
				attacker = new EntitySA_A10a(ModEntities.ENTITY_A10A.get(), world1);
				have_driver = true;
				attacker.setArmyType2(250);
				isone = true;
			}else if(id == 7){
				attacker = new EntitySA_F35(ModEntities.ENTITY_F35.get(), world1);
				have_driver = true;
				attacker.setArmyType2(100);
				isone = true;
			}else if(id == 8){
				attacker = new EntitySA_A10a(ModEntities.ENTITY_A10A.get(), world1);
				have_driver = true;
				attacker.setArmyType2(250);
				isone = true;
			}else if(id == 9){
				attacker = new EntitySA_F35(ModEntities.ENTITY_F35.get(), world1);
				have_driver = true;
				attacker.setArmyType2(100);
			}else if(id == 16){
				attacker = new EntitySA_BattleShip(ModEntities.ENTITY_BSHIP.get(), world1);
				height = 1;
				center = true;
			}else if(id == 51){
				attacker = new EntitySA_RockTank(ModEntities.ENTITY_RCTANK.get(), world1);
				height = 1;
				center = true;
			}else if(id == 52){
				attacker = new EntitySA_STAPC(ModEntities.ENTITY_STAPC.get(), world1);
				height = 1;
				center = true;
			}else if(id == 53){
				EntitySA_MWDrone mwd = new EntitySA_MWDrone(ModEntities.ENTITY_MWD.get(), world1);
				height = 1;
				center = true;
				{
					ParticlePoint bullet = new ParticlePoint(ModEntities.ENTITY_P.get(), world1);
					bullet.moveTo(ix, iy+height, iz, 0, 0);
					if(!world1.isClientSide)world1.addFreshEntity(bullet);
					bullet.setPType(1);
					bullet.setSTime(80);
					this.playSound(SASoundEvent.csk.get(), 6.0F, 1.0F);
				}
				mwd.moveTo(ix, iy+height, iz, mwd.yHeadRot, mwd.getXRot());
				mwd.setMoveType(3);
				if(caller instanceof Player){
					Player owner = (Player)caller;
					mwd.tame(owner);
				}
				if(!world1.isClientSide){
					world1.addFreshEntity(mwd);
				}
			}
			
			double xx11 = 0;
			double zz11 = 0;
			xx11 -= Mth.sin((caller.getYRot()+count*10) * 0.01745329252F - 1.57F) * range;
			zz11 += Mth.cos((caller.getYRot()+count*10) * 0.01745329252F - 1.57F) * range;
			
			double xx = ix - xx11;
			double yy = iy + height;
			double zz = iz - zz11;
			
			if(attacker!= null){
				attacker.setRemain1(attacker.magazine);
				attacker.setRemain2(attacker.magazine2);
				attacker.setRemain3(attacker.magazine3);
				attacker.setRemain4(attacker.magazine4);
				attacker.yHeadRot = attacker.yRotO = -((float) Math.atan2(ix - xx, iz - zz)) * 180.0F/ (float) Math.PI;
				attacker.setYRot(attacker.yHeadRot);
				if(center){
					if(height==1){
						ParticlePoint bullet = new ParticlePoint(ModEntities.ENTITY_P.get(), world1);
						bullet.moveTo(ix, iy+height, iz, 0, 0);
						if(!world1.isClientSide)world1.addFreshEntity(bullet);
						bullet.setPType(1);
						bullet.setSTime(80);
						this.playSound(SASoundEvent.csk.get(), 6.0F, 1.0F);
					}
					attacker.moveTo(ix, iy+height, iz, attacker.yHeadRot, attacker.getXRot());
					attacker.setMoveMode(5);
					if (!world1.isClientSide) {
						world1.addFreshEntity(attacker);
						for(int k2 = 0; k2 <attacker.seatMaxCount; ++k2){
							EntitySA_OFG pilot = new EntitySA_OFG(ModEntities.ENTITY_OFG.get(),world1);
							if(caller instanceof Player){
								Player owner = (Player)caller;
								pilot.tame(owner);
							}
							pilot.moveTo(xx, yy, zz, 0, 0.0F);
							pilot.setMoveType(1);
							pilot.fastRid=true;
							world1.addFreshEntity(pilot);
							attacker.catchPassenger(pilot);
						}
					}
				}else{
					attacker.moveTo(xx, yy, zz, attacker.yHeadRot, attacker.getXRot());
					attacker.setTargetType(3);
					attacker.setMoveType(1);
					/*if(id == 2)*/{
						attacker.setMoveMode(5);
					}/*else{
						attacker.setAIType(1);
					}*/
				}
				if(caller instanceof Player){
					Player owner = (Player)caller;
					attacker.tame(owner);
				}
				//attacker.setcanDespawn(1);
				//if(caller.getTeam()!=null)attacker.world.getScoreboard().addPlayerToTeam(var9.getCachedUniqueIdString(), caller.getTeam().getName());
				if(!world1.isClientSide){
					world1.addFreshEntity(attacker);
					if (have_driver){//
						EntitySA_Soldier pilot = new EntitySA_Soldier(ModEntities.ENTITY_SOLDIER.get(),world1);
						/*if(caller instanceof Player){
							Player owner = (Player)caller;
							pilot.tame(owner);
						}*/
						pilot.moveTo(xx, yy, zz, 0, 0.0F);
						pilot.setMoveType(1);
						world1.addFreshEntity(pilot);
						attacker.catchPassenger(pilot);
					}
				}
			}
			if(trans!= null){
				//trans.fri = caller;
				trans.yHeadRot = trans.yRotO = -((float) Math.atan2(ix - xx, iz - zz)) * 180.0F/ (float) Math.PI;
				trans.setYRot(trans.yHeadRot);
				trans.moveTo(xx, yy, zz, trans.yHeadRot, trans.getXRot());
				//trans.setMoveMode(id);
				trans.setMoveType(5);
				trans.setMovePosX((int)ix);
				trans.setMovePosY((int)iy);
				trans.setMovePosZ((int)iz);
				trans.movePower=trans.throttleMax-2;
				trans.throttle=trans.throttleMax-2;
				
				trans.setTargetType(3);
				
				if(caller instanceof Player){
					Player owner = (Player)caller;
					trans.tame(owner);
				}
				if (!world1.isClientSide) {
					world1.addFreshEntity(trans);
					for(int k2 = 0; k2 <trans.seatMaxCount; ++k2){
						EntitySA_Swun pilot = new EntitySA_Swun(ModEntities.ENTITY_SWUN.get(),world1);
						if(caller instanceof Player){
							Player owner = (Player)caller;
							pilot.tame(owner);
						}
						pilot.moveTo(xx, yy, zz, 0, 0.0F);
						pilot.setMoveType(1);
						pilot.fastRid=true;
						world1.addFreshEntity(pilot);
						trans.catchPassenger(pilot);
					}
					
					//pilot.startRiding(attacker);
					/*if (id < 4){//
						EntitySA_Soldier pilot = new EntitySA_Soldier(ModEntities.ENTITY_SOLDIER.get(),world1);
						if(caller instanceof Player){
							Player owner = (Player)caller;
							pilot.tame(owner);
						}
						pilot.moveTo(xx, yy, zz, 0, 0.0F);
						pilot.setMoveType(1);
						//pilot.setcanDespawn(1);
						world1.addFreshEntity(pilot);
						pilot.startRiding(trans);
					}else
					if (id == 5){//
						EntitySA_GI pilot = new EntitySA_GI(ModEntities.ENTITY_GI.get(),world1);
						if(caller instanceof Player){
							Player owner = (Player)caller;
							pilot.tame(owner);
						}
						pilot.moveTo(xx, yy, zz, 0, 0.0F);
						pilot.setMoveType(1);
						//pilot.setcanDespawn(1);
						world1.addFreshEntity(pilot);
						pilot.startRiding(trans);
					}else
					if (id == 6){//
						EntitySA_Conscript pilot = new EntitySA_Conscript(ModEntities.ENTITY_CONS.get(),world1);
						if(caller instanceof Player){
							Player owner = (Player)caller;
							pilot.tame(owner);
						}
						pilot.moveTo(xx, yy, zz, 0, 0.0F);
						pilot.setMoveType(1);
						//pilot.setcanDespawn(1);
						world1.addFreshEntity(pilot);
						pilot.startRiding(trans);
					}*/
				}
			}
		}
	}
}

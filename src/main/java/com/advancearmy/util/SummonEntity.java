package advancearmy.util;

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

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import net.minecraft.core.Direction;
public class SummonEntity{
	public static void wildSummon(Level world1, double ix, double iy, double iz, int id, boolean isEnemy, Team team, int c) {
		if(!world1.isClientSide()&&world1.getGameTime()>500){
			BlockPos pos = new BlockPos((int)ix,(int)iy,(int)iz);
			if (world1.canSeeSky(pos)) {
				int driver_type = 0;
				boolean have_driver = true;
				int driver_count = 1;
				WeaponVehicleBase vehicle=null;
				if(id == 1) {
					vehicle = new EntitySA_Tank(ModEntities.ENTITY_TANK.get(), world1);
				}else if(id == 2){
					vehicle = new EntitySA_AH1Z(ModEntities.ENTITY_AH1Z.get(), world1);
				}else if(id == 3){
					vehicle = new EntitySA_FTK(ModEntities.ENTITY_FTK.get(), world1);
				}else if(id == 4){
					vehicle = new EntitySA_MI24(ModEntities.ENTITY_MI24.get(), world1);
				}else if(id == 5){
					vehicle = new EntitySA_SU33(ModEntities.ENTITY_SU33.get(), world1);
				}else if(id == 6){
					vehicle = new EntitySA_A10a(ModEntities.ENTITY_A10A.get(), world1);
				}else if(id == 7){
					vehicle = new EntitySA_F35(ModEntities.ENTITY_F35.get(), world1);
				}else if(id == 8){
					vehicle = new EntitySA_Prism(ModEntities.ENTITY_PRISM.get(), world1);
				}else if(id == 9){
					vehicle = new EntitySA_M2A2AA(ModEntities.ENTITY_M2A2AA.get(), world1);
				}else if(id == 10){
					vehicle = new EntitySA_T55(ModEntities.ENTITY_T55.get(), world1);
				}else if(id == 11){
					vehicle = new EntitySA_T72(ModEntities.ENTITY_T72.get(), world1);
				}else if(id == 12){
					vehicle = new EntitySA_T90(ModEntities.ENTITY_T90.get(), world1);
				}else if(id == 13){
					vehicle = new EntitySA_BMP2(ModEntities.ENTITY_BMP2.get(), world1);
				}else if(id == 14){
					vehicle = new EntitySA_Lapear(ModEntities.ENTITY_LAPEAR.get(), world1);
				}else if(id == 15){
					vehicle = new EntitySA_Plane1(ModEntities.ENTITY_PLANE1.get(), world1);
					driver_type=1;
				}else if(id == 16){
					vehicle = new EntitySA_Plane2(ModEntities.ENTITY_PLANE2.get(), world1);
					driver_type=1;
				}else if(id == 17){
					//vehicle = new EntitySA_AH6(ModEntities.ENTITY_AH6.get(), world1);
				}else if(id == 18){
					vehicle = new EntitySA_Plane(ModEntities.ENTITY_PLANE.get(), world1);
				}else if(id == 19){
					vehicle = new EntitySA_LAV(ModEntities.ENTITY_LAV.get(), world1);
				}else if(id == 20){
					vehicle = new EntitySA_Helicopter(ModEntities.ENTITY_HELI.get(), world1);
				}else if(id == 21){
					vehicle = new EntitySA_LaserAA(ModEntities.ENTITY_LAA.get(), world1);
				}else if(id == 23){
					vehicle = new EntitySA_Car(ModEntities.ENTITY_CAR.get(), world1);
				}else if(id == 24){
					vehicle = new EntitySA_Hmmwv(ModEntities.ENTITY_HMMWV.get(), world1);
				}else if(id == 25){
					vehicle = new EntitySA_M2A2(ModEntities.ENTITY_M2A2.get(), world1);
				}else if(id == 26){
					vehicle = new EntitySA_M2A2AA(ModEntities.ENTITY_M2A2AA.get(), world1);
				}else if(id == 27){
					vehicle = new EntitySA_LAVAA(ModEntities.ENTITY_LAVAA.get(), world1);
				}
				
				if(vehicle!=null){
					vehicle.moveTo(ix, iy, iz, vehicle.yHeadRot, vehicle.getXRot());
					world1.addFreshEntity(vehicle);
				}
				for(int k2 = 0; k2 < c; ++k2){
					EntitySA_SquadBase soldier = null;
					if(id==31){
						soldier = new EntitySA_Soldier(ModEntities.ENTITY_SOLDIER.get(), world1);
					}else if(id==32){
						soldier = new EntitySA_Conscript(ModEntities.ENTITY_CONS.get(), world1);
					}
					if(soldier!=null){
						soldier.canPara = true;
						soldier.setPos(ix+k2, iy, iz);
						world1.addFreshEntity(soldier);
					}
				}
				for(int k2 = 0; k2 < c; ++k2){
					if(id==33){
						EntityMobSquadBase ent = new ERO_REB(ModEntities.ENTITY_REB.get(), world1);
						ent.canPara = true;
						ent.setPos(ix+k2, iy, iz);
						world1.addFreshEntity(ent);
					}else if(id==34){
						EntityRaiderSquadBase ent = new ERO_Pillager(ModEntities.ENTITY_PI.get(), world1);
						ent.canPara = true;
						ent.setPos(ix+k2, iy, iz);
						world1.addFreshEntity(ent);
					}
				}
			}
		}
	}
}

package advancearmy.init;
import advancearmy.entity.EntitySA_Seat;

import advancearmy.entity.soldier.EntitySA_GI;
import advancearmy.entity.soldier.EntitySA_Conscript;
import advancearmy.entity.soldier.EntitySA_ConscriptX;
import advancearmy.entity.soldier.EntitySA_Soldier;
import advancearmy.entity.soldier.EntitySA_Swun;
import advancearmy.entity.soldier.EntitySA_OFG;
import advancearmy.entity.soldier.EntitySA_RADS;
import advancearmy.entity.soldier.EntitySA_GAT;
import advancearmy.entity.soldier.EntitySA_MWDrone;

import advancearmy.entity.mob.EntityAohuan;
import advancearmy.entity.mob.ERO_Pillager;
import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Husk;
import advancearmy.entity.mob.ERO_Spider;
import advancearmy.entity.mob.ERO_Giant;
import advancearmy.entity.mob.ERO_Ravager;
import advancearmy.entity.mob.ERO_Skeleton;
import advancearmy.entity.mob.ERO_Creeper;
//import advancearmy.entity.mob.DragonTurret;
import advancearmy.entity.mob.ERO_REB;
import advancearmy.entity.mob.ERO_ROCKET;
import advancearmy.entity.mob.ERO_AA;
import advancearmy.entity.mob.ERO_Mortar;

import advancearmy.entity.mob.ERO_Phantom;
import advancearmy.entity.mob.ERO_Ghast;
import advancearmy.entity.mob.EvilPortal;
import advancearmy.entity.mob.EvilPortalOnce;

import advancearmy.entity.map.CreatureRespawn;
import advancearmy.entity.map.ArmyMovePoint;
import advancearmy.entity.map.VehicleRespawn;
import advancearmy.entity.map.SupportPoint;
import advancearmy.entity.map.RewardBox;
import advancearmy.entity.map.DefencePoint;
import advancearmy.entity.map.RandomPoint;
import advancearmy.entity.map.ParticlePoint;

import advancearmy.entity.air.EntitySA_MI24;
import advancearmy.entity.air.EntitySA_Plane;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;
import advancearmy.entity.air.EntitySA_Fw020;
import advancearmy.entity.air.EntitySA_Lapear;
import advancearmy.entity.air.EntitySA_YouHun;
import advancearmy.entity.air.EntitySA_A10c;
import advancearmy.entity.air.EntitySA_SU33;
import advancearmy.entity.air.EntitySA_Helicopter;

import advancearmy.entity.air.EntitySA_F35;
import advancearmy.entity.air.EntitySA_A10a;
import advancearmy.entity.air.EntitySA_AH1Z;

import advancearmy.entity.air.EntitySA_AH6;
import advancearmy.entity.air.EntitySA_Yw010;
import advancearmy.entity.sea.EntitySA_BattleShip;
import advancearmy.entity.land.EntitySA_99G;
import advancearmy.entity.land.EntitySA_FTK_H;
import advancearmy.entity.land.EntitySA_Ember;
import advancearmy.entity.land.EntitySA_Sickle;
import advancearmy.entity.land.EntitySA_Reaper;
import advancearmy.entity.land.EntitySA_Tesla;
import advancearmy.entity.land.EntitySA_Mirage;
import advancearmy.entity.land.EntitySA_APAGAT;
import advancearmy.entity.land.EntitySA_MMTank;

import advancearmy.entity.land.EntitySA_BMP2;
import advancearmy.entity.land.EntitySA_BMPT;
import advancearmy.entity.land.EntitySA_M109;
import advancearmy.entity.land.EntitySA_M2A2;
import advancearmy.entity.land.EntitySA_M2A2AA;
import advancearmy.entity.land.EntitySA_Car;
import advancearmy.entity.land.EntitySA_T55;
import advancearmy.entity.land.EntitySA_FTK;
import advancearmy.entity.land.EntitySA_Hmmwv;
import advancearmy.entity.land.EntitySA_RockTank;
import advancearmy.entity.land.EntitySA_STAPC;

import advancearmy.entity.land.EntitySA_LaserAA;
import advancearmy.entity.land.EntitySA_MASTDOM;
import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.land.EntitySA_LAV;
import advancearmy.entity.land.EntitySA_LAVAA;
import advancearmy.entity.land.EntitySA_Tank;
import advancearmy.entity.land.EntitySA_Prism;
import advancearmy.entity.land.EntitySA_Bike;
import advancearmy.entity.turret.EntitySA_Mortar;
import advancearmy.entity.turret.EntitySA_TOW;
import advancearmy.entity.turret.EntitySA_STIN;
import advancearmy.entity.turret.EntitySA_M2hb;
import advancearmy.entity.turret.EntitySA_Kord;
import advancearmy.entity.building.SandBag;
import advancearmy.entity.building.SoldierMachine;
import advancearmy.entity.building.VehicleMachine;
import advancearmy.entity.EntitySA_LandBase;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraftforge.fml.common.Mod;
import advancearmy.AAConfig;
import advancearmy.world.SpawnChecker;
import advancearmy.AdvanceArmy;
@Mod.EventBusSubscriber(modid = AdvanceArmy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "advancearmy");
    
	public static final RegistryObject<EntityType<EntitySA_Seat>> ENTITY_SEAT = register("entityseat",
            EntityType.Builder.<EntitySA_Seat>of(EntitySA_Seat::new, MobCategory.MISC).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Seat::new).sized(1F, 1F));
	/*public static final RegistryObject<EntityType<EntitySA_Seat>> ENTITY_SEAT = register("entityseat",
            EntityType.Builder.<EntitySA_Seat>of(EntitySA_Seat::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Seat::new).fireImmune().sized(1f, 1f));*/
			
	public static final RegistryObject<EntityType<EntitySA_Tank>> ENTITY_TANK = register("entitytank",
            EntityType.Builder.<EntitySA_Tank>of(EntitySA_Tank::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Tank::new).fireImmune().sized(4f, 3f));
			
	public static final RegistryObject<EntityType<EntitySA_Car>> ENTITY_CAR = register("entitycar",
            EntityType.Builder.<EntitySA_Car>of(EntitySA_Car::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Car::new).fireImmune().sized(2.5F, 2F));
			
	public static final RegistryObject<EntityType<EntitySA_Hmmwv>> ENTITY_HMMWV = register("entityhmmwv",
            EntityType.Builder.<EntitySA_Hmmwv>of(EntitySA_Hmmwv::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Hmmwv::new).fireImmune().sized(2.5F, 2F));
			
	public static final RegistryObject<EntityType<EntitySA_BMP2>> ENTITY_BMP2 = register("entitybmp2",
            EntityType.Builder.<EntitySA_BMP2>of(EntitySA_BMP2::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_BMP2::new).fireImmune().sized(3f, 2f));
			
	public static final RegistryObject<EntityType<EntitySA_Bike>> ENTITY_BIKE = register("entitybike",
            EntityType.Builder.<EntitySA_Bike>of(EntitySA_Bike::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Bike::new).fireImmune().sized(1.5F, 1));
			
	public static final RegistryObject<EntityType<EntitySA_T55>> ENTITY_T55 = register("entityt55",
            EntityType.Builder.<EntitySA_T55>of(EntitySA_T55::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_T55::new).fireImmune().sized(3.5F, 2.4F));
			
	public static final RegistryObject<EntityType<EntitySA_M2A2AA>> ENTITY_M2A2AA = register("entitym6aa",
            EntityType.Builder.<EntitySA_M2A2AA>of(EntitySA_M2A2AA::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_M2A2AA::new).fireImmune().sized(3F, 2F));

	public static final RegistryObject<EntityType<EntitySA_M2A2>> ENTITY_M2A2 = register("entitym2a2",
            EntityType.Builder.<EntitySA_M2A2>of(EntitySA_M2A2::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_M2A2::new).fireImmune().sized(3f, 3f));

	public static final RegistryObject<EntityType<EntitySA_M109>> ENTITY_M109 = register("entitym109",
            EntityType.Builder.<EntitySA_M109>of(EntitySA_M109::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_M109::new).fireImmune().sized(3F, 3.8F));

	public static final RegistryObject<EntityType<EntitySA_BMPT>> ENTITY_BMPT = register("entitybmpt",
            EntityType.Builder.<EntitySA_BMPT>of(EntitySA_BMPT::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_BMPT::new).fireImmune().sized(4F, 2.5F));

	public static final RegistryObject<EntityType<EntitySA_Prism>> ENTITY_PRISM = register("entityprism",
            EntityType.Builder.<EntitySA_Prism>of(EntitySA_Prism::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Prism::new).fireImmune().sized(3.5F, 2.6F));
			
	public static final RegistryObject<EntityType<EntitySA_LaserAA>> ENTITY_LAA = register("entityskyfire",
            EntityType.Builder.<EntitySA_LaserAA>of(EntitySA_LaserAA::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_LaserAA::new).fireImmune().sized(3F, 2F));

	public static final RegistryObject<EntityType<EntitySA_LAV>> ENTITY_LAV = register("entitylav",
            EntityType.Builder.<EntitySA_LAV>of(EntitySA_LAV::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_LAV::new).fireImmune().sized(3F, 2.5F));

	public static final RegistryObject<EntityType<EntitySA_LAVAA>> ENTITY_LAVAA = register("entitylavaa",
            EntityType.Builder.<EntitySA_LAVAA>of(EntitySA_LAVAA::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_LAVAA::new).fireImmune().sized(3F, 2.5F));

	public static final RegistryObject<EntityType<EntitySA_T72>> ENTITY_T72 = register("entityt72",
            EntityType.Builder.<EntitySA_T72>of(EntitySA_T72::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_T72::new).fireImmune().sized(4f, 2.5F));

	public static final RegistryObject<EntityType<EntitySA_T90>> ENTITY_T90 = register("entityt90",
            EntityType.Builder.<EntitySA_T90>of(EntitySA_T90::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_T90::new).fireImmune().sized(4f, 2.5F));

	public static final RegistryObject<EntityType<EntitySA_FTK>> ENTITY_FTK = register("entityftk",
            EntityType.Builder.<EntitySA_FTK>of(EntitySA_FTK::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_FTK::new).fireImmune().sized(4F, 2.5F));
	
	
	public static final RegistryObject<EntityType<EntitySA_GI>> ENTITY_GI = register("entitygi",
            EntityType.Builder.<EntitySA_GI>of(EntitySA_GI::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_GI::new).sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<EntitySA_Conscript>> ENTITY_CONS = register("entityconscript",
            EntityType.Builder.<EntitySA_Conscript>of(EntitySA_Conscript::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Conscript::new).sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<EntitySA_ConscriptX>> ENTITY_CONSX = register("entityconsx",
            EntityType.Builder.<EntitySA_ConscriptX>of(EntitySA_ConscriptX::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_ConscriptX::new).fireImmune().sized(1.5F, 3F));
    public static final RegistryObject<EntityType<EntitySA_Soldier>> ENTITY_SOLDIER = register("entitysoldier",
            EntityType.Builder.<EntitySA_Soldier>of(EntitySA_Soldier::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Soldier::new).sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<EntitySA_Swun>> ENTITY_SWUN = register("entityswun",
            EntityType.Builder.<EntitySA_Swun>of(EntitySA_Swun::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Swun::new).fireImmune().sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<EntitySA_OFG>> ENTITY_OFG = register("entityofg",
            EntityType.Builder.<EntitySA_OFG>of(EntitySA_OFG::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_OFG::new).fireImmune().sized(0.5F, 1.8F));
	public static final RegistryObject<EntityType<EntitySA_RADS>> ENTITY_RADS = register("entityrads",
		EntityType.Builder.<EntitySA_RADS>of(EntitySA_RADS::new, MobCategory.CREATURE).setTrackingRange(60)
		.setUpdateInterval(3).setCustomClientFactory(EntitySA_RADS::new).sized(0.5F, 1.8F));
	public static final RegistryObject<EntityType<EntitySA_GAT>> ENTITY_GAT = register("entityminigunner",
		EntityType.Builder.<EntitySA_GAT>of(EntitySA_GAT::new, MobCategory.CREATURE).setTrackingRange(60)
		.setUpdateInterval(3).setCustomClientFactory(EntitySA_GAT::new).sized(0.5F, 1.8F));
	
	
	public static final RegistryObject<EntityType<EntityAohuan>> ENTITY_AOHUAN = register("entityaohuan",
            EntityType.Builder.<EntityAohuan>of(EntityAohuan::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntityAohuan::new).fireImmune().sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<ERO_Pillager>> ENTITY_PI = register("ero_pillager",
            EntityType.Builder.<ERO_Pillager>of(ERO_Pillager::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Pillager::new).sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<ERO_Husk>> ENTITY_EHUSK = register("ero_husk",
            EntityType.Builder.<ERO_Husk>of(ERO_Husk::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Husk::new).sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<ERO_Giant>> ENTITY_GIANT = register("ero_giant",
            EntityType.Builder.<ERO_Giant>of(ERO_Giant::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Giant::new).sized(5F, 20F));
    public static final RegistryObject<EntityType<ERO_Spider>> ERO_SPIDER= register("ero_spider",
            EntityType.Builder.<ERO_Spider>of(ERO_Spider::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Spider::new).sized(1.4f, 0.9f));
    public static final RegistryObject<EntityType<ERO_Ravager>> ERO_RAV= register("ero_ravager",
            EntityType.Builder.<ERO_Ravager>of(ERO_Ravager::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Ravager::new).sized(4f, 4f));
			
    public static final RegistryObject<EntityType<ERO_REB>> ENTITY_REB = register("ero_reb",
            EntityType.Builder.<ERO_REB>of(ERO_REB::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_REB::new).sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<ERO_Skeleton>> ENTITY_SKELETON = register("ero_skeleton",
            EntityType.Builder.<ERO_Skeleton>of(ERO_Skeleton::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Skeleton::new).sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<ERO_Creeper>> ENTITY_CREEPER = register("ero_creeper",
            EntityType.Builder.<ERO_Creeper>of(ERO_Creeper::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Creeper::new).sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<ERO_Phantom>> ENTITY_PHA = register("ero_phantom",
            EntityType.Builder.<ERO_Phantom>of(ERO_Phantom::new, MobCategory.MONSTER).setTrackingRange(80)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Phantom::new).sized(0.5F, 0.5F));
    public static final RegistryObject<EntityType<ERO_Ghast>> ENTITY_GST = register("ero_ghast",
            EntityType.Builder.<ERO_Ghast>of(ERO_Ghast::new, MobCategory.MONSTER).setTrackingRange(80)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Ghast::new).sized(4F, 4F));
    public static final RegistryObject<EntityType<ERO_Zombie>> ENTITY_EZOMBIE = register("ero_zombie",
            EntityType.Builder.<ERO_Zombie>of(ERO_Zombie::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Zombie::new).sized(0.5F, 1.8F));
    public static final RegistryObject<EntityType<EvilPortal>> ENTITY_POR = register("evilportal",
            EntityType.Builder.<EvilPortal>of(EvilPortal::new, MobCategory.MONSTER).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EvilPortal::new).sized(1F, 1F));
    public static final RegistryObject<EntityType<EvilPortalOnce>> ENTITY_POR1 = register("evilportal1",
            EntityType.Builder.<EvilPortalOnce>of(EvilPortalOnce::new, MobCategory.MONSTER).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EvilPortalOnce::new).sized(1F, 2F));
			
    public static final RegistryObject<EntityType<ERO_ROCKET>> E_ROCKET = register("ero_spg",
            EntityType.Builder.<ERO_ROCKET>of(ERO_ROCKET::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_ROCKET::new).sized(3F, 3F));
    public static final RegistryObject<EntityType<ERO_AA>> E_AA = register("ero_aa",
            EntityType.Builder.<ERO_AA>of(ERO_AA::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_AA::new).sized(3F, 3F));
    public static final RegistryObject<EntityType<ERO_Mortar>> E_MORTAR = register("ero_mortar",
            EntityType.Builder.<ERO_Mortar>of(ERO_Mortar::new, MobCategory.MONSTER).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ERO_Mortar::new).sized(3F, 3F));
			
			
    public static final RegistryObject<EntityType<EntitySA_F35>> ENTITY_F35 = register("entityf35",
            EntityType.Builder.<EntitySA_F35>of(EntitySA_F35::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_F35::new).sized(6F,3F));
    public static final RegistryObject<EntityType<EntitySA_A10a>> ENTITY_A10A = register("entitya10a",
            EntityType.Builder.<EntitySA_A10a>of(EntitySA_A10a::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_A10a::new).sized(6F,3F));
    public static final RegistryObject<EntityType<EntitySA_AH1Z>> ENTITY_AH1Z = register("entityah1z",
            EntityType.Builder.<EntitySA_AH1Z>of(EntitySA_AH1Z::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_AH1Z::new).sized(6F,3F));
		
    public static final RegistryObject<EntityType<EntitySA_A10c>> ENTITY_A10C = register("entitya10c",
            EntityType.Builder.<EntitySA_A10c>of(EntitySA_A10c::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_A10c::new).sized(6F,3F));
		
    public static final RegistryObject<EntityType<EntitySA_Helicopter>> ENTITY_HELI = register("entityheli",
            EntityType.Builder.<EntitySA_Helicopter>of(EntitySA_Helicopter::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Helicopter::new).sized(3F, 2F));
		
    public static final RegistryObject<EntityType<EntitySA_SU33>> ENTITY_SU33 = register("entitysu33",
            EntityType.Builder.<EntitySA_SU33>of(EntitySA_SU33::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_SU33::new).sized(6F,3F));
			
	public static final RegistryObject<EntityType<EntitySA_YouHun>> ENTITY_YOUHUN = register("entityyouhun",
            EntityType.Builder.<EntitySA_YouHun>of(EntitySA_YouHun::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_YouHun::new).sized(4F, 2F));
	
	public static final RegistryObject<EntityType<EntitySA_Lapear>> ENTITY_LAPEAR = register("entitylapear",
            EntityType.Builder.<EntitySA_Lapear>of(EntitySA_Lapear::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Lapear::new).sized(6F,3F));
	
	public static final RegistryObject<EntityType<EntitySA_Fw020>> ENTITY_FW020 = register("entityfw020",
            EntityType.Builder.<EntitySA_Fw020>of(EntitySA_Fw020::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Fw020::new).sized(6F,3F));
	
	public static final RegistryObject<EntityType<EntitySA_Plane2>> ENTITY_PLANE2 = register("entityplane2",
            EntityType.Builder.<EntitySA_Plane2>of(EntitySA_Plane2::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Plane2::new).sized(3F, 2F));
	
	public static final RegistryObject<EntityType<EntitySA_Plane1>> ENTITY_PLANE1 = register("entityplane1",
            EntityType.Builder.<EntitySA_Plane1>of(EntitySA_Plane1::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Plane1::new).sized(3F, 2F));
	
	public static final RegistryObject<EntityType<EntitySA_Plane>> ENTITY_PLANE = register("entityplane",
            EntityType.Builder.<EntitySA_Plane>of(EntitySA_Plane::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Plane::new).sized(5F, 2F));
			
	public static final RegistryObject<EntityType<EntitySA_MI24>> ENTITY_MI24 = register("entitymi24",
            EntityType.Builder.<EntitySA_MI24>of(EntitySA_MI24::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_MI24::new).sized(3.2F, 2.5F));
			
	public static final RegistryObject<EntityType<EntitySA_Mortar>> ENTITY_MORTAR = register("entitymortar",
            EntityType.Builder.<EntitySA_Mortar>of(EntitySA_Mortar::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Mortar::new).sized(1F, 1F));
			
	public static final RegistryObject<EntityType<EntitySA_TOW>> ENTITY_TOW = register("entitytow",
            EntityType.Builder.<EntitySA_TOW>of(EntitySA_TOW::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_TOW::new).sized(1F, 1F));
			
	public static final RegistryObject<EntityType<EntitySA_STIN>> ENTITY_STIN = register("entitystin",
            EntityType.Builder.<EntitySA_STIN>of(EntitySA_STIN::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_STIN::new).sized(1F, 1F));
			
	public static final RegistryObject<EntityType<EntitySA_M2hb>> ENTITY_M2HB = register("entitym2hb",
            EntityType.Builder.<EntitySA_M2hb>of(EntitySA_M2hb::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_M2hb::new).sized(1F, 1F));
			
	public static final RegistryObject<EntityType<EntitySA_Kord>> ENTITY_KORD = register("entitykord",
            EntityType.Builder.<EntitySA_Kord>of(EntitySA_Kord::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Kord::new).sized(1F, 1F));

	public static final RegistryObject<EntityType<EntitySA_MASTDOM>> ENTITY_MAST = register("entitymast",
            EntityType.Builder.<EntitySA_MASTDOM>of(EntitySA_MASTDOM::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_MASTDOM::new).sized(7F, 3.2F));
			
	public static final RegistryObject<EntityType<EntitySA_AH6>> ENTITY_AH6 = register("entityah6",
            EntityType.Builder.<EntitySA_AH6>of(EntitySA_AH6::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_AH6::new).sized(2.5F, 2.5F));
	public static final RegistryObject<EntityType<EntitySA_Yw010>> ENTITY_YW010 = register("entityyw010",
            EntityType.Builder.<EntitySA_Yw010>of(EntitySA_Yw010::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Yw010::new).sized(3.2F, 2.5F));
	public static final RegistryObject<EntityType<EntitySA_BattleShip>> ENTITY_BSHIP = register("entitybattleship",
            EntityType.Builder.<EntitySA_BattleShip>of(EntitySA_BattleShip::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_BattleShip::new).sized(6F, 6F));
	public static final RegistryObject<EntityType<EntitySA_99G>> ENTITY_99G = register("entity99g",
            EntityType.Builder.<EntitySA_99G>of(EntitySA_99G::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_99G::new).sized(4F, 2.5F));
	public static final RegistryObject<EntityType<EntitySA_FTK_H>> ENTITY_FTK_H = register("entityftkh",
            EntityType.Builder.<EntitySA_FTK_H>of(EntitySA_FTK_H::new, MobCategory.CREATURE).setTrackingRange(100)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_FTK_H::new).sized(8F, 4F));
	public static final RegistryObject<EntityType<EntitySA_Ember>> ENTITY_EMBER = register("entityember",
            EntityType.Builder.<EntitySA_Ember>of(EntitySA_Ember::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Ember::new).sized(5F, 9F));
	public static final RegistryObject<EntityType<EntitySA_Sickle>> ENTITY_SICKLE = register("entitysickle",
            EntityType.Builder.<EntitySA_Sickle>of(EntitySA_Sickle::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Sickle::new).sized(3F, 4.6F));
	public static final RegistryObject<EntityType<EntitySA_Reaper>> ENTITY_REAPER = register("entityreaper",
            EntityType.Builder.<EntitySA_Reaper>of(EntitySA_Reaper::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Reaper::new).sized(3F, 6F));
	public static final RegistryObject<EntityType<EntitySA_Tesla>> ENTITY_TESLA = register("entitytesla",
            EntityType.Builder.<EntitySA_Tesla>of(EntitySA_Tesla::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Tesla::new).sized(3F, 3F));
	public static final RegistryObject<EntityType<EntitySA_Mirage>> ENTITY_MIRAGE = register("entitymirage",
            EntityType.Builder.<EntitySA_Mirage>of(EntitySA_Mirage::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_Mirage::new).sized(3F, 3F));
	public static final RegistryObject<EntityType<EntitySA_APAGAT>> ENTITY_APAGAT = register("entityapagat",
            EntityType.Builder.<EntitySA_APAGAT>of(EntitySA_APAGAT::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_APAGAT::new).sized(3F, 3F));
	public static final RegistryObject<EntityType<EntitySA_MMTank>> ENTITY_MMTANK = register("entitymmtank",
            EntityType.Builder.<EntitySA_MMTank>of(EntitySA_MMTank::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_MMTank::new).sized(4F, 3F));
			
	public static final RegistryObject<EntityType<EntitySA_MWDrone>> ENTITY_MWD = register("entitymwd",
            EntityType.Builder.<EntitySA_MWDrone>of(EntitySA_MWDrone::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_MWDrone::new).sized(3F, 2F));
	public static final RegistryObject<EntityType<EntitySA_STAPC>> ENTITY_STAPC = register("entitystapc",
            EntityType.Builder.<EntitySA_STAPC>of(EntitySA_STAPC::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_STAPC::new).sized(4F, 3F));
	public static final RegistryObject<EntityType<EntitySA_RockTank>> ENTITY_RCTANK = register("entityrctank",
            EntityType.Builder.<EntitySA_RockTank>of(EntitySA_RockTank::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(EntitySA_RockTank::new).sized(4F, 3F));
			
	public static final RegistryObject<EntityType<VehicleMachine>> ENTITY_VMAC = register("entitymachinev",
            EntityType.Builder.<VehicleMachine>of(VehicleMachine::new, MobCategory.CREATURE).setTrackingRange(120)
			.setUpdateInterval(3).setCustomClientFactory(VehicleMachine::new).sized(2F, 2F));
	public static final RegistryObject<EntityType<SoldierMachine>> ENTITY_SMAC = register("entitymachines",
            EntityType.Builder.<SoldierMachine>of(SoldierMachine::new, MobCategory.CREATURE).setTrackingRange(120)
			.setUpdateInterval(3).setCustomClientFactory(SoldierMachine::new).sized(2F, 2F));
	public static final RegistryObject<EntityType<SandBag>> ENTITY_SANDBAG = register("entitysandbag",
            EntityType.Builder.<SandBag>of(SandBag::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(SandBag::new).sized(1F, 0.9F));
			
	public static final RegistryObject<EntityType<SupportPoint>> ENTITY_SPT = register("entitysupport",
            EntityType.Builder.<SupportPoint>of(SupportPoint::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(SupportPoint::new).sized(1F, 0.9F));
			
	public static final RegistryObject<EntityType<RewardBox>> ENTITY_RBOX = register("entityrewardbox",
            EntityType.Builder.<RewardBox>of(RewardBox::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(RewardBox::new).sized(1F, 1F));
			
	public static final RegistryObject<EntityType<DefencePoint>> ENTITY_DPT = register("entitydefence",
            EntityType.Builder.<DefencePoint>of(DefencePoint::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(DefencePoint::new).sized(1F, 3.2F));
			
	public static final RegistryObject<EntityType<RandomPoint>> ENTITY_INV = register("entityinv",
            EntityType.Builder.<RandomPoint>of(RandomPoint::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(RandomPoint::new).sized(1F, 3.2F));
			
	public static final RegistryObject<EntityType<CreatureRespawn>> ENTITY_CRES = register("entityrespawnc",
            EntityType.Builder.<CreatureRespawn>of(CreatureRespawn::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(CreatureRespawn::new).sized(1F, 0.2F));
			
	public static final RegistryObject<EntityType<VehicleRespawn>> ENTITY_VRES = register("entityrespawnv",
            EntityType.Builder.<VehicleRespawn>of(VehicleRespawn::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(VehicleRespawn::new).sized(1F, 0.2F));
			
	public static final RegistryObject<EntityType<ArmyMovePoint>> ENTITY_MOVEP = register("entitymovep",
            EntityType.Builder.<ArmyMovePoint>of(ArmyMovePoint::new, MobCategory.CREATURE).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ArmyMovePoint::new).sized(1F, 0.2F));
			
	public static final RegistryObject<EntityType<ParticlePoint>> ENTITY_P = register("entityp",
            EntityType.Builder.<ParticlePoint>of(ParticlePoint::new, MobCategory.MISC).setTrackingRange(60)
			.setUpdateInterval(3).setCustomClientFactory(ParticlePoint::new).sized(1F, 0.2F));
			
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTER.register(name, () -> entityTypeBuilder.build(name));
    }
	
    /*@SubscribeEvent
    public static void onRegisterSpawnPlacement(SpawnPlacementRegisterEvent event) {//神秘失效
		if(AAConfig.spawnSoldier){
			event.register(ModEntities.ENTITY_SOLDIER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canSunSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
			event.register(ModEntities.ENTITY_CONS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canSunSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
		}
		if(AAConfig.spawnMob){
			event.register(ModEntities.ENTITY_EHUSK.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)),
					SpawnPlacementRegisterEvent.Operation.OR);
			event.register(ModEntities.ENTITY_EZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canNightSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
			event.register(ModEntities.ENTITY_CREEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canNightSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
			event.register(ModEntities.ENTITY_SKELETON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canNightSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);			
			event.register(ModEntities.ENTITY_REB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canNightSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
			event.register(ModEntities.ENTITY_BIKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canGroundNightSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
			event.register(ModEntities.ENTITY_PI.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canGroundNightSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
			event.register(ModEntities.ENTITY_GST.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canGroundNightSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
			event.register(ModEntities.ENTITY_PHA.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canNightSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
			event.register(ModEntities.ERO_SPIDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canNightSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
			event.register(ModEntities.ERO_RAV.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canGroundNightSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
			event.register(ModEntities.ENTITY_GIANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SpawnChecker::canHardSpawn,
					SpawnPlacementRegisterEvent.Operation.REPLACE);
		}
	}*/
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ENTITY_TANK.get(), EntitySA_Tank.createAttributes().build());
		//event.put(ENTITY_SEAT.get(), EntitySA_Seat.createAttributes().build());

		event.put(ENTITY_CAR.get(), EntitySA_Car.createAttributes().build());
		event.put(ENTITY_HMMWV.get(), EntitySA_Hmmwv.createAttributes().build());
		
		event.put(ENTITY_BMP2.get(), EntitySA_BMP2.createAttributes().build());
		event.put(ENTITY_T55.get(), EntitySA_T55.createAttributes().build());
		event.put(ENTITY_FTK.get(), EntitySA_FTK.createAttributes().build());
		event.put(ENTITY_M2A2AA.get(), EntitySA_M2A2AA.createAttributes().build());
		event.put(ENTITY_M2A2.get(), EntitySA_M2A2.createAttributes().build());
		event.put(ENTITY_M109.get(), EntitySA_M109.createAttributes().build());
		event.put(ENTITY_BMPT.get(), EntitySA_BMPT.createAttributes().build());
		event.put(ENTITY_PRISM.get(), EntitySA_Prism.createAttributes().build());
		event.put(ENTITY_LAA.get(), EntitySA_LaserAA.createAttributes().build());
		event.put(ENTITY_LAV.get(), EntitySA_LAV.createAttributes().build());
		event.put(ENTITY_LAVAA.get(), EntitySA_LAVAA.createAttributes().build());
		event.put(ENTITY_T72.get(), EntitySA_T72.createAttributes().build());
		event.put(ENTITY_T90.get(), EntitySA_T90.createAttributes().build());
		
		event.put(ENTITY_F35.get(), EntitySA_F35.createAttributes().build());
		event.put(ENTITY_A10A.get(), EntitySA_A10a.createAttributes().build());
		event.put(ENTITY_AH1Z.get(), EntitySA_AH1Z.createAttributes().build());
		
		event.put(ENTITY_GI.get(), EntitySA_GI.createAttributes().build());
		event.put(ENTITY_CONS.get(), EntitySA_Conscript.createAttributes().build());
		event.put(ENTITY_CONSX.get(), EntitySA_ConscriptX.createAttributes().build());
		event.put(ENTITY_SOLDIER.get(), EntitySA_Soldier.createAttributes().build());
		event.put(ENTITY_OFG.get(), EntitySA_OFG.createAttributes().build());
		event.put(ENTITY_SWUN.get(), EntitySA_Swun.createAttributes().build());
		event.put(ENTITY_AOHUAN.get(), EntityAohuan.createAttributes().build());
		event.put(ENTITY_PI.get(), ERO_Pillager.createAttributes().build());
		
		event.put(ENTITY_EHUSK.get(), ERO_Husk.createAttributes().build());
		event.put(ERO_SPIDER.get(), ERO_Spider.createAttributes().build());
		event.put(ENTITY_GIANT.get(), ERO_Giant.createAttributes().build());
		event.put(ERO_RAV.get(), ERO_Ravager.createAttributes().build());
		event.put(ENTITY_REB.get(), ERO_REB.createAttributes().build());
		event.put(ENTITY_SKELETON.get(), ERO_Skeleton.createAttributes().add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 35.0D)
					.add(Attributes.ATTACK_DAMAGE, 3.0D)
					.add(Attributes.ARMOR, (double) 4D).build());
		event.put(ENTITY_CREEPER.get(), ERO_Creeper.createAttributes().add(Attributes.MAX_HEALTH, 100.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 35.0D)
					.add(Attributes.ATTACK_DAMAGE, 3.0D)
					.add(Attributes.ARMOR, (double) 4D).build());
		event.put(ENTITY_PHA.get(), ERO_Phantom.createAttributes().add(Attributes.MAX_HEALTH, 40.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)4F).add(Attributes.ATTACK_DAMAGE, 8.0D)
					.add(Attributes.FOLLOW_RANGE, 80.0D)
					.add(Attributes.ARMOR, (double) 5D).build());
		event.put(ENTITY_GST.get(), ERO_Ghast.createAttributes().add(Attributes.MAX_HEALTH, 240.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)4F).add(Attributes.ATTACK_DAMAGE, 8.0D)
					.add(Attributes.FOLLOW_RANGE, 80.0D)
					.add(Attributes.ARMOR, (double) 5D).build());
		event.put(ENTITY_EZOMBIE.get(), ERO_Zombie.createAttributes().add(Attributes.FOLLOW_RANGE, 100.0D).add(Attributes.MAX_HEALTH, 25.0D)
					.add(Attributes.MOVEMENT_SPEED, (double)0.12F).add(Attributes.ATTACK_DAMAGE, 3.0D)
					.add(Attributes.ARMOR, 2.0D).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE).build());
		event.put(ENTITY_POR.get(), EvilPortal.createAttributes().add(Attributes.MAX_HEALTH, 1000.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 50.0F)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 10D).build());
		event.put(ENTITY_POR1.get(), EvilPortalOnce.createAttributes().add(Attributes.MAX_HEALTH, 1000.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 50.0F)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 10D).build());
					
		event.put(ENTITY_MWD.get(), EntitySA_LandBase.createAttributes().add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 50.0F)
					.add(Attributes.FOLLOW_RANGE, 55.0D)
					.add(Attributes.ARMOR, (double) 20D).build());
		event.put(ENTITY_STAPC.get(), EntitySA_LandBase.createAttributes().add(Attributes.MAX_HEALTH, 400.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 50.0F)
					.add(Attributes.FOLLOW_RANGE, 40.0D)
					.add(Attributes.ARMOR, (double) 30D).build());
		event.put(ENTITY_RCTANK.get(), EntitySA_LandBase.createAttributes().add(Attributes.MAX_HEALTH, 800.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 50.0F)
					.add(Attributes.FOLLOW_RANGE, 60.0D)
					.add(Attributes.ARMOR, (double) 40D).build());
					
		event.put(ENTITY_PLANE.get(), EntitySA_Plane.createAttributes().build());
		event.put(ENTITY_PLANE1.get(), EntitySA_Plane1.createAttributes().build());
		event.put(ENTITY_PLANE2.get(), EntitySA_Plane2.createAttributes().build());
		event.put(ENTITY_SU33.get(), EntitySA_SU33.createAttributes().build());
		event.put(ENTITY_A10C.get(), EntitySA_A10c.createAttributes().build());
		event.put(ENTITY_LAPEAR.get(), EntitySA_Lapear.createAttributes().build());
		event.put(ENTITY_FW020.get(), EntitySA_Fw020.createAttributes().build());
		event.put(ENTITY_YOUHUN.get(), EntitySA_YouHun.createAttributes().build());
		event.put(ENTITY_HELI.get(), EntitySA_Helicopter.createAttributes().build());
		event.put(ENTITY_MI24.get(), EntitySA_MI24.createAttributes().build());
		
		event.put(E_MORTAR.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 15.0D)
					.add(Attributes.MAX_HEALTH, 600.0D)
					.add(Attributes.FOLLOW_RANGE, 90.0D)
					.add(Attributes.ARMOR, (double) 5D).build());
		event.put(E_AA.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 15.0D)
					.add(Attributes.MAX_HEALTH, 600.0D)
					.add(Attributes.FOLLOW_RANGE, 90.0D)
					.add(Attributes.ARMOR, (double) 6D).build());
		event.put(E_ROCKET.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 15.0D)
					.add(Attributes.MAX_HEALTH, 600.0D)
					.add(Attributes.FOLLOW_RANGE, 90.0D)
					.add(Attributes.ARMOR, (double) 7D).build());
		
		event.put(ENTITY_MORTAR.get(), EntitySA_Mortar.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 15.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 90.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
		event.put(ENTITY_TOW.get(), EntitySA_TOW.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 15.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 75.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
		event.put(ENTITY_STIN.get(), EntitySA_STIN.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 15.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 90.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
		event.put(ENTITY_M2HB.get(), EntitySA_M2hb.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 15.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
		event.put(ENTITY_KORD.get(), EntitySA_Kord.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 15.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
		event.put(ENTITY_MAST.get(), EntitySA_MASTDOM.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 20.0D)
					.add(Attributes.MAX_HEALTH, 1200.0D)
					.add(Attributes.FOLLOW_RANGE, 65.0D)
					.add(Attributes.ARMOR, (double) 25D).build());
		event.put(ENTITY_AH6.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 120.0D)
					.add(Attributes.FOLLOW_RANGE, 75.0D)
					.add(Attributes.ARMOR, (double) 4D).build());
		event.put(ENTITY_YW010.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 600.0D)
					.add(Attributes.FOLLOW_RANGE, 75.0D)
					.add(Attributes.ARMOR, (double) 10D).build());
		event.put(ENTITY_BSHIP.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 6000.0D)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 100D).build());
		event.put(ENTITY_99G.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 800.0D)
					.add(Attributes.FOLLOW_RANGE, 60.0D)
					.add(Attributes.ARMOR, (double) 25D).build());
		event.put(ENTITY_FTK_H.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 3000.0D)
					.add(Attributes.FOLLOW_RANGE, 60.0D)
					.add(Attributes.ARMOR, (double) 50D).build());
		event.put(ENTITY_SICKLE.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 55.0D)
					.add(Attributes.ARMOR, (double) 8D).build());
		event.put(ENTITY_REAPER.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 350.0D)
					.add(Attributes.FOLLOW_RANGE, 55.0D)
					.add(Attributes.ARMOR, (double) 12D).build());
		event.put(ENTITY_TESLA.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 450.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 14D).build());
		event.put(ENTITY_MIRAGE.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 350.0D)
					.add(Attributes.FOLLOW_RANGE, 55.0D)
					.add(Attributes.ARMOR, (double) 12D).build());
		event.put(ENTITY_APAGAT.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 250.0D)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 10D).build());
		event.put(ENTITY_MMTANK.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 650.0D)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 22D).build());
		event.put(ENTITY_EMBER.get(), EntitySA_LandBase.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 1500.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 20D).build());
		event.put(ENTITY_RADS.get(), EntitySA_LandBase.createAttributes().add(Attributes.MAX_HEALTH, 80.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 45.0D)
					.add(Attributes.ARMOR, (double) 6D).build());
		event.put(ENTITY_GAT.get(), EntitySA_LandBase.createAttributes().add(Attributes.MAX_HEALTH, 65.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 110.0D)
					.add(Attributes.ARMOR, (double) 13D).build());
					
		event.put(ENTITY_BIKE.get(), EntitySA_LandBase.createAttributes().add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 8.0F)
					.add(Attributes.ARMOR, (double) 2D).build());
		
		//event.put(ENTITY_P.get(), ParticlePoint.createAttributes().build());
		event.put(ENTITY_VMAC.get(), VehicleMachine.createAttributes().add(Attributes.MAX_HEALTH, 500.0D).add(Attributes.ARMOR, (double) 5D).build());
		event.put(ENTITY_SMAC.get(), SoldierMachine.createAttributes().add(Attributes.MAX_HEALTH, 500.0D).add(Attributes.ARMOR, (double) 5D).build());
		event.put(ENTITY_SANDBAG.get(), SandBag.createAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.KNOCKBACK_RESISTANCE, (double) 10F).build());
		
		event.put(ENTITY_SPT.get(), SupportPoint.createAttributes().add(Attributes.MAX_HEALTH, 1000.0D).build());
		event.put(ENTITY_RBOX.get(), RewardBox.createAttributes().add(Attributes.MAX_HEALTH, 1000.0D).add(Attributes.KNOCKBACK_RESISTANCE, (double) 10F).build());
		event.put(ENTITY_DPT.get(), DefencePoint.createAttributes().add(Attributes.MAX_HEALTH, 1000.0D).build());
		
		event.put(ENTITY_INV.get(), RandomPoint.createAttributes().add(Attributes.MAX_HEALTH, 1000.0D).build());
		
		event.put(ENTITY_CRES.get(), CreatureRespawn.createAttributes().add(Attributes.MAX_HEALTH, 1000.0D).build());
		event.put(ENTITY_VRES.get(), VehicleRespawn.createAttributes().add(Attributes.MAX_HEALTH, 1000.0D).build());
		event.put(ENTITY_MOVEP.get(), ArmyMovePoint.createAttributes().add(Attributes.MAX_HEALTH, 1000.0D).build());
	}
}

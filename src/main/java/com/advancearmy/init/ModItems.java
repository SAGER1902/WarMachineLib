package advancearmy.init;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import advancearmy.item.ItemSpawn;
import advancearmy.item.ItemSpawnMob;
import advancearmy.item.ItemSupport;
import advancearmy.item.ItemDefence;
import advancearmy.item.ItemGun_Target;
import advancearmy.item.ItemRemove;
import advancearmy.item.ItemTeam;
import wmlib.client.obj.SAObjModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advancearmy.item.ItemCapture;
import advancearmy.item.ItemTrans;
@SuppressWarnings("unused")
public class ModItems {
	public static final DeferredRegister<Item> MOBS = DeferredRegister.create(ForgeRegistries.ITEMS, "advancearmy");
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "advancearmy");
	public static final DeferredRegister<Item> UNITS = DeferredRegister.create(ForgeRegistries.ITEMS, "advancearmy");
    static int id = 0;
	static int mobid = 0;
	static int spid = 0;
	static int dfid = 0;

	public static final RegistryObject<ItemSpawn> item_spawn_soldier = registerSpawn("item_spawn_soldier", ++id,
				1,300,200,"advancearmy.infor.item1.desc","","","为突击兵职业时能够额外装备榴弹发射器",null);
	
	public static final RegistryObject<ItemSpawn> item_spawn_tank = registerSpawn("item_spawn_tank", ++id,
				0,1000,700,"advancearmy.infor.item2.desc","","","",new SAObjModel("advancearmy:textures/mob/m1tank.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_conscript = registerSpawn("item_spawn_conscript", ++id,
				1,200,150,"advancearmy.infor.item1.desc","","动员兵中的精锐","",null);
	
	public static final RegistryObject<ItemSpawn> item_spawn_ftk = registerSpawn("item_spawn_ftk", ++id,
				0,900,700,"advancearmy.infor.item2.desc","","加强了装甲，暂时没有配装磁能炮弹","",new SAObjModel("advancearmy:textures/mob/ftk_new.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_t55 = registerSpawn("item_spawn_t55", ++id,
				0,600,500,"advancearmy.infor.item2.desc","","","",new SAObjModel("advancearmy:textures/mob/t55.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_gltk = registerSpawn("item_spawn_gltk", ++id,
				0,1000,800,"advancearmy.infor.item13.desc","","","",new SAObjModel("advancearmy:textures/mob/gltk.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_heli = registerSpawn("item_spawn_heli", ++id,
				0,1000,800,"advancearmy.infor.item4.desc","","","",new SAObjModel("advancearmy:textures/mob/ah64.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_egal = registerSpawn("item_spawn_egal", ++id,
				0,1000,800,"advancearmy.infor.item3.desc","","","",new SAObjModel("advancearmy:textures/mob/egal.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_t72b3 = registerSpawn("item_spawn_t72b3", ++id,
				0,900,600,"advancearmy.infor.item2.desc","","","",new SAObjModel("advancearmy:textures/mob/t72b3.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_t90 = registerSpawn("item_spawn_t90", ++id,
				0,1050,700,"advancearmy.infor.item2.desc","","","",new SAObjModel("advancearmy:textures/mob/t90.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_lavaa = registerSpawn("item_spawn_lavaa", ++id,
				0,800,700,"advancearmy.infor.item12.desc","","","",new SAObjModel("advancearmy:textures/mob/lav25aa.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_skyfire = registerSpawn("item_spawn_skyfire", ++id,
				0,1200,800,"advancearmy.infor.item12.desc","","经过改进后，武器角度可以对地","",new SAObjModel("advancearmy:textures/mob/skyfire.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_sickle = registerSpawn("item_spawn_sickle", ++id,
				0,800,700,"四足机甲","经过SWUN军团的改造","装甲和火力都获得了升级，武器角度可以对空","",new SAObjModel("advancearmy:textures/mob/sickle.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_mast = registerSpawn("item_spawn_mast", ++id,
				0,2000,1000,"超重型坦克","","经过改进后，可以选择在短暂蓄力后快速开火","",new SAObjModel("advancearmy:textures/mob/mast.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_m2a2 = registerSpawn("item_spawn_m2a2", ++id,
				0,800,800,"advancearmy.infor.item11.desc","","","",new SAObjModel("advancearmy:textures/mob/m2a2.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_99g = registerSpawn("item_spawn_99g", ++id,
				0,1800,1200,"advancearmy.infor.item2.desc","","","",new SAObjModel("advancearmy:textures/mob/99.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_m109 = registerSpawn("item_spawn_m109", ++id,
				0,1000,1000,"advancearmy.infor.item13.desc","","","",new SAObjModel("advancearmy:textures/mob/m109.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_ftk_heavy = registerSpawn("item_spawn_ftk_heavy", ++id,
				0,4000,2000,"超重型坦克","","这辆犀牛坦克经过了十分疯狂的改造","",new SAObjModel("advancearmy:textures/mob/ftk_heavy.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_ah1z = registerSpawn("item_spawn_ah1z", ++id,
				0,900,1200,"advancearmy.infor.item4.desc","","","",new SAObjModel("advancearmy:textures/mob/ah1z.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_car = registerSpawn("item_spawn_car", ++id,
				0,400,400,"advancearmy.infor.item14.desc","","","",new SAObjModel("advancearmy:textures/mob/car.obj"));

	public static final RegistryObject<ItemSpawn> item_spawn_hmmwv = registerSpawn("item_spawn_hmmwv", ++id,
				0,500,500,"advancearmy.infor.item14.desc","","","",new SAObjModel("advancearmy:textures/mob/hmmwv.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_f35 = registerSpawn("item_spawn_f35", ++id,
				0,2000,1000,"advancearmy.infor.item5.desc","","","",new SAObjModel("advancearmy:textures/mob/f35.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_trident = registerSpawn("item_spawn_trident", ++id,
				0,3000,2000,"advancearmy.infor.item15.desc","","","",new SAObjModel("advancearmy:textures/mob/battleship.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_m6aa = registerSpawn("item_spawn_m6aa", ++id,
				0,800,800,"advancearmy.infor.item12.desc","","","",new SAObjModel("advancearmy:textures/mob/m2a2aa.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_lav = registerSpawn("item_spawn_lav", ++id,
				0,700,700,"advancearmy.infor.item11.desc","","","",new SAObjModel("advancearmy:textures/mob/lav25.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_gattank = registerSpawn("item_spawn_gattank", ++id,
				0,2500,1500,"advancearmy.infor.item12.desc","","","",new SAObjModel("advancearmy:textures/mob/apagat.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_bmp2 = registerSpawn("item_spawn_bmp2", ++id,
				0,700,700,"advancearmy.infor.item11.desc","","","",new SAObjModel("advancearmy:textures/mob/bmp2.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_bmpt = registerSpawn("item_spawn_bmpt", ++id,
				0,1200,900,"坦克支援车","","","",new SAObjModel("advancearmy:textures/mob/bmpt.obj"));
	
	public static final RegistryObject<ItemSpawn> item_spawn_a10a = registerSpawn("item_spawn_a10a", ++id,
				0,1200,900,"advancearmy.infor.item3.desc","","","",new SAObjModel("advancearmy:textures/mob/a10a.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_a10c = registerSpawn("item_spawn_a10c", ++id,
				0,1500,900,"advancearmy.infor.item3.desc","","","",new SAObjModel("advancearmy:textures/mob/a10c.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_ah6 = registerSpawn("item_spawn_ah6", ++id,
				0,700,700,"advancearmy.infor.item4.desc","","","",new SAObjModel("advancearmy:textures/mob/ah6.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_mi24 = registerSpawn("item_spawn_mi24", ++id,
				0,1100,900,"advancearmy.infor.item4.desc","","","",new SAObjModel("advancearmy:textures/mob/mi24.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_su33 = registerSpawn("item_spawn_su33", ++id,
				0,1600,900,"advancearmy.infor.item5.desc","","","",new SAObjModel("advancearmy:textures/mob/su33.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_ofg = registerSpawn("item_spawn_ofg", ++id,
				1,1200,900,"专业突击兵","","装备高科技热光步枪","",null);
	public static final RegistryObject<ItemSpawn> item_spawn_mortar = registerSpawn("item_spawn_mortar", ++id,
				0,700,700,"","","","",new SAObjModel("advancearmy:textures/mob/mortar.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_m2hb = registerSpawn("item_spawn_m2hb", ++id,
				0,500,500,"advancearmy.infor.item7.desc","","","",new SAObjModel("advancearmy:textures/mob/m2hb_t.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_kord = registerSpawn("item_spawn_kord", ++id,
				0,500,500,"advancearmy.infor.item7.desc","","","",new SAObjModel("advancearmy:textures/mob/kord_t.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_gi = registerSpawn("item_spawn_gi", ++id,
		1,320,320,"专业支援兵","","防守状态下能够部署沙袋架设轻机枪","",null);
	public static final RegistryObject<ItemSpawn> item_spawn_conscript_big = registerSpawn("item_spawn_conscript_big", ++id,
		1,1500,1000,"特种兵","","看起来十分强壮的动员兵，没有人知道他们经历了什么","",null);
	public static final RegistryObject<ItemSpawn> item_spawn_tow = registerSpawn("item_spawn_tow", ++id,
		0,700,700,"advancearmy.infor.item8.desc","","","",new SAObjModel("advancearmy:textures/mob/tow.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_stin = registerSpawn("item_spawn_stin", ++id,
		0,700,700,"advancearmy.infor.item9.desc","","","",new SAObjModel("advancearmy:textures/mob/stin.obj"));
	public static final RegistryObject<ItemSpawn> soldier_machine = registerSpawn("soldier_machine", ++id,
		3,-1,0,"advancearmy.infor.item6.desc","advancearmy.infor.building1.desc","advancearmy.infor.building_rote.desc","advancearmy.infor.pickaxe_fix.desc",null);
	public static final RegistryObject<ItemSpawn> vehicle_machine = registerSpawn("vehicle_machine", ++id,
		3,-1,0,"advancearmy.infor.item6.desc","advancearmy.infor.building2.desc","advancearmy.infor.building_rote.desc","advancearmy.infor.pickaxe_fix.desc",null);
				
	public static final RegistryObject<ItemSpawn> item_spawn_rads = registerSpawn("item_spawn_rads", ++id,
				1,600,500,"专业工程兵","","防守状态下能够部署制造大范围辐射场","",null);
	
	public static final RegistryObject<ItemSpawn> item_spawn_minigunner = registerSpawn("item_spawn_minigunner", ++id,
				1,450,350,"专业工程兵","","","",null);
	
	public static final RegistryObject<ItemSpawn> item_spawn_reaper = registerSpawn("item_spawn_reaper", ++id,
				0,1100,900,"四足机甲","换装穿甲主炮与猛犸牙导弹","由于设备造价提高,现在禁止进行跳跃","",new SAObjModel("advancearmy:textures/mob/reaper.obj"));
				
	public static final RegistryObject<ItemSpawn> item_spawn_mmtank = registerSpawn("item_spawn_mmtank", ++id,
				0,1400,1000,"重型坦克","翻新后的老旧坦克","","",new SAObjModel("advancearmy:textures/mob/mmtank.obj"));
				
	public static final RegistryObject<ItemSpawn> item_spawn_sandbag = registerSpawn("item_spawn_sandbag", ++id,
				3,-1,0,"防御建筑","对低威力子弹抗性高,可以让友军子弹穿过","可以蹲下右键搬起","advancearmy.infor.pickaxe_fix.desc",null);
	public static final RegistryObject<ItemSpawn> item_spawn_sandbag2 = registerSpawn("item_spawn_sandbag2", ++id,
				3,-1,0,"防御建筑","对低威力子弹抗性高,可以让友军子弹穿过","可以蹲下右键搬起","advancearmy.infor.pickaxe_fix.desc",null);
	public static final RegistryObject<ItemSpawn> item_spawn_sandbag3 = registerSpawn("item_spawn_sandbag3", ++id,
				3,-1,0,"防御建筑","对低威力子弹抗性高,可以让友军子弹穿过","可以蹲下右键搬起","advancearmy.infor.pickaxe_fix.desc",null);
				
	public static final RegistryObject<ItemSpawn> item_spawn_mirage = registerSpawn("item_spawn_mirage", ++id,
				0,1600,1000,"advancearmy.infor.item2.desc","改进型幻影突击坦克","将裂缝发射器换成了防御护盾","",new SAObjModel("advancearmy:textures/mob/mirage.obj"));
				
	public static final RegistryObject<ItemSpawn> item_spawn_tesla = registerSpawn("item_spawn_tesla", ++id,
				0,1400,1000,"advancearmy.infor.item2.desc","先进的改进型号","可以短暂进入高频模式进行大范围攻击","",new SAObjModel("advancearmy:textures/mob/tesla.obj"));
				
	public static final RegistryObject<ItemSpawn> item_spawn_bike = registerSpawn("item_spawn_bike", ++id,
				0,200,100,"","","","",new SAObjModel("advancearmy:textures/mob/bike.obj"));
				
	public static final RegistryObject<ItemSpawn> item_spawn_mwd = registerSpawn("item_spawn_mwd", ++id,
				1,4500,350,"悬浮无人防空车","","","",null);
	public static final RegistryObject<ItemSpawn> item_spawn_rctank = registerSpawn("item_spawn_rctank", ++id,
				0,5500,2000,"advancearmy.infor.item2.desc","悬浮主战坦克","可以生成投影来干扰敌军","",new SAObjModel("advancearmy:textures/mob/rocktank.obj"));
	public static final RegistryObject<ItemSpawn> item_spawn_stapc = registerSpawn("item_spawn_stapc", ++id,
				0,5000,2000,"advancearmy.infor.item2.desc","悬浮装甲运兵车","","",new SAObjModel("advancearmy:textures/mob/stapc.obj"));
				
	/*public static final RegistryObject<ItemSpawn> item_spawn_soldier = registerSpawn("", ++id,
				0,600,500,"","","","",null);*/
	
	public static final RegistryObject<ItemSpawnMob> maptool_respawnc = registerSpawn_TOOL("maptool_respawnc", ++mobid);
	public static final RegistryObject<ItemSpawnMob> maptool_respawnv = registerSpawn_TOOL("maptool_respawnv", ++mobid);
	public static final RegistryObject<ItemSpawnMob> maptool_movepoint = registerSpawn_TOOL("maptool_movepoint", ++mobid);
	
	public static final RegistryObject<ItemSpawnMob> mob_spawn_skeleton = registerSpawn_MOB("mob_spawn_skeleton", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_pillager = registerSpawn_MOBS("mob_spawn_pillager", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_phantom = registerSpawn_MOB("mob_spawn_phantom", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_ghost = registerSpawn_MOB("mob_spawn_ghost", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_creeper = registerSpawn_MOB("mob_spawn_creeper", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_dragonturret = registerSpawn_MOB("mob_spawn_dragonturret", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_reb = registerSpawn_MOBS("mob_spawn_reb", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_aohuan = registerSpawn_MOBS("mob_spawn_aohuan", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_evilportal = registerSpawn_MOB("mob_spawn_evilportal", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_zombie = registerSpawn_MOB("mob_spawn_zombie", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_husk = registerSpawn_MOB("mob_spawn_husk", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_evilspawner = registerSpawn_MOB("mob_spawn_evilspawner", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_spider = registerSpawn_MOB("mob_spawn_spider", ++mobid);	
	public static final RegistryObject<ItemSpawnMob> mob_spawn_bug = registerSpawn_MOB("mob_spawn_bug", ++mobid);	
	public static final RegistryObject<ItemSpawnMob> mob_spawn_giant = registerSpawn_MOB("mob_spawn_giant", ++mobid);
	public static final RegistryObject<ItemSpawnMob> mob_spawn_ravager = registerSpawn_MOB("mob_spawn_ravager", ++mobid);
	/*public static final RegistryObject<ItemSpawnMob> mob_spawn_monster2 = registerSpawn_MOB("mob_spawn_monster2", ++mobid);*/
	
	public static final RegistryObject<ItemDefence> challenge_mob = registerSpawn_Challenge("challenge_mob", ++dfid,"挑战1-怪物之围");
	public static final RegistryObject<ItemDefence> challenge_reb = registerSpawn_Challenge("challenge_reb", ++dfid,"挑战2-GLA入侵");
	public static final RegistryObject<ItemDefence> challenge_pillager = registerSpawn_Challenge("challenge_pillager", ++dfid,"挑战3-灾厄入侵");
	public static final RegistryObject<ItemDefence> challenge_tank = registerSpawn_Challenge("challenge_tank", ++dfid,"挑战4-GLA装甲部队入侵");
	public static final RegistryObject<ItemDefence> challenge_mobair = registerSpawn_Challenge("challenge_mobair", ++dfid,"挑战5-铺天盖地");
	public static final RegistryObject<ItemDefence> challenge_air = registerSpawn_Challenge("challenge_air", ++dfid,"挑战6-GLA空军入侵");
	public static final RegistryObject<ItemDefence> challenge_sea = registerSpawn_Challenge("challenge_sea", ++dfid,"挑战7-守卫战舰");
	public static final RegistryObject<ItemDefence> challenge_aohuan = registerSpawn_Challenge("challenge_aohuan", ++dfid,"挑战8-太空船入侵");
	public static final RegistryObject<ItemDefence> challenge_portal = registerSpawn_Challenge("challenge_portal", ++dfid,"挑战9-邪恶传送门");
	//public static final RegistryObject<ItemDefence> challenge_ziao114514 = registerSpawn_Challenge("challenge_ziao114514", ++dfid,"挑战-屑猫");
	//public static final RegistryObject<ItemDefence> challenge_sager = registerSpawn_Challenge("challenge_sager", ++dfid,"挑战-SAGER");
	//public static final RegistryObject<ItemDefence> challenge_monkey = registerSpawn_Challenge("challenge_monkey", ++dfid,"挑战-Monkey");
	
	public static final RegistryObject<ItemSupport> support_swun = registerSpawn_Support("support_swun", ++spid,1,0,0);
	public static final RegistryObject<ItemSupport> support_fw020 = registerSpawn_Support("support_fw020", ++spid,1,0,0);
	public static final RegistryObject<ItemSupport> support_youhun = registerSpawn_Support("support_youhun", ++spid,1,0,0);
	public static final RegistryObject<ItemSupport> support_ember = registerSpawn_Support("support_ember", ++spid,1,0,0);
	public static final RegistryObject<ItemSupport> support_ftkh = registerSpawn_Support("support_ftkh", ++spid,1,0,0);
	public static final RegistryObject<ItemSupport> support_a10a = registerSpawn_Support("support_a10a", ++spid,0,300,400);
	public static final RegistryObject<ItemSupport> support_f35bomb = registerSpawn_Support("support_f35bomb", ++spid,0,400,500);
	public static final RegistryObject<ItemSupport> support_a10ax3 = registerSpawn_Support("support_a10ax3", ++spid,0,600,500);
	public static final RegistryObject<ItemSupport> support_f35bombx3 = registerSpawn_Support("support_f35bombx3", ++spid,0,600,600);
	public static final RegistryObject<ItemSupport> support_155 = registerSpawn_Support("support_155", ++spid,0,400,700);
	
	public static final RegistryObject<ItemSupport> support_kh29l = registerSpawn_Support("support_kh29l", ++spid,0,800,900);
	public static final RegistryObject<ItemSupport> support_3m22 = registerSpawn_Support("support_3m22", ++spid,0,1200,1200);
	public static final RegistryObject<ItemSupport> support_agm158 = registerSpawn_Support("support_agm158", ++spid,0,1300,1500);
	public static final RegistryObject<ItemSupport> support_kh58 = registerSpawn_Support("support_kh58", ++spid,0,1600,2500);
	public static final RegistryObject<ItemSupport> support_nuke = registerSpawn_Support("support_nuke", ++spid,1,0,0);
	public static final RegistryObject<ItemSupport> support_trident = registerSpawn_Support("support_trident", ++spid,1,0,0);
	public static final RegistryObject<ItemSupport> support_nuke2 = registerSpawn_Support("support_nuke2", ++spid,1,0,0);
	
	public static final RegistryObject<ItemSupport> support_ziao114514 = registerSpawn_Support("support_ziao114514", ++spid,1,0,0);
	
	//public static final RegistryObject<ItemSupport> support_cons = registerSpawn_Support("support_cons", ++spid,1,0,0);

	public static final RegistryObject<ItemSupport> portal_star = registerSpawn_Support("portal_star", 0,0,0,0);
	public static final RegistryObject<ItemGun_Target> targetgun = registerSpawn_TargetGun("targetgun");
	public static final RegistryObject<ItemCapture> unit_capture = registerSpawn_Capture("unit_capture");
	public static final RegistryObject<ItemRemove> unit_remove = registerNormal("unit_remove");
	
	public static final RegistryObject<ItemTrans> unit_trans = ITEMS.register("unit_trans", () -> new ItemTrans(new Item.Properties().stacksTo(1).durability(200), false));
	public static final RegistryObject<ItemTrans> vehicle_trans = ITEMS.register("vehicle_trans", () -> new ItemTrans(new Item.Properties().stacksTo(1).durability(200), true));
	
	public static final RegistryObject<ItemTeam> team_tool = ITEMS.register("team_tool", () -> new ItemTeam(new Item.Properties().stacksTo(1).durability(200)));
	
	private static RegistryObject<ItemSpawn> registerSpawn(String name,int id, int time, int xp, int cool,
	String infor1,String infor2,String infor3,String infor4, SAObjModel obj){
		return UNITS.register(name, () -> new ItemSpawn(new Item.Properties().stacksTo(1), id, time, xp, cool,
		infor1,infor2,infor3,infor4,obj));
	}
	
	private static RegistryObject<ItemSpawnMob> registerSpawn_TOOL(String name,int id){
		return ITEMS.register(name, () -> new ItemSpawnMob(new Item.Properties(), id, 0xFFFFFF,0xFFFFFF, false));
	}
	private static RegistryObject<ItemSpawnMob> registerSpawn_MOB(String name,int id){
		return MOBS.register(name, () -> new ItemSpawnMob(new Item.Properties(), id, 0xFFFFFF,0xFFFFFF, false));
	}
	private static RegistryObject<ItemSpawnMob> registerSpawn_MOBS(String name,int id){
		return MOBS.register(name, () -> new ItemSpawnMob(new Item.Properties(), id, 0x660000,0x8B0000, true));
	}
	private static RegistryObject<ItemSupport> registerSpawn_Support(String name,int id, int time, int xp, int cool){
		return ITEMS.register(name, () -> new ItemSupport(new Item.Properties(), id, time, xp, cool));
	}
	private static RegistryObject<ItemDefence> registerSpawn_Challenge(String name,int id, String name1){
		return MOBS.register(name, () -> new ItemDefence(new Item.Properties(), id, name1));
	}
	private static RegistryObject<ItemGun_Target> registerSpawn_TargetGun(String name){
		return ITEMS.register(name, () -> new ItemGun_Target(new Item.Properties().stacksTo(1).durability(200)));
	}
	private static RegistryObject<ItemCapture> registerSpawn_Capture(String name){
		return ITEMS.register(name, () -> new ItemCapture(new Item.Properties().stacksTo(1).durability(200)));
	}
	private static RegistryObject<ItemRemove> registerNormal(String name){
		return ITEMS.register(name, () -> new ItemRemove(new Item.Properties().stacksTo(1).durability(200)));
	}

    public static void register(IEventBus bus) {
		MOBS.register(bus);
		UNITS.register(bus);
		ITEMS.register(bus);
    }
}

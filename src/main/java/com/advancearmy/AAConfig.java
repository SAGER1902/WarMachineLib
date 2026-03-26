package advancearmy;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import java.util.List;
import com.google.common.collect.Lists;
@Mod.EventBusSubscriber(modid = AdvanceArmy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AAConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    // 配置项定义
    public static final ForgeConfigSpec.ConfigValue<Boolean> VEHICLE_DESTROY = BUILDER
            .comment("开启后的载具移动时会摧毁脆弱方块")
            .define("vehicleDestroy", true);
    
    public static final ForgeConfigSpec.ConfigValue<Boolean> SPAWN_TEAM = BUILDER
            .comment("开启后自然生成的士兵会与玩家同一队伍以避免友军伤害")
            .define("spawnFriendTeam", false);
    
    public static final ForgeConfigSpec.ConfigValue<Boolean> STRUCTURE_TEAM = BUILDER
            .comment("开启后结构中的士兵会与玩家同一队伍以避免友军伤害")
            .define("structureFriendTeam", true);
    
    public static final ForgeConfigSpec.ConfigValue<Boolean> SPAWN_SOLIDER = BUILDER
            .comment("开启后会自然生成士兵,详细调整生成可修改[模组包/data/advancearmy/forge/biome_modifier/中的生物生成json文件]")
            .define("spawnSoldier", true);
    
    public static final ForgeConfigSpec.ConfigValue<Boolean> SPAWN_MOB = BUILDER
            .comment("开启后会自然生成侵蚀怪物,详细调整生成可修改[模组包/data/advancearmy/forge/biome_modifier/中的生物生成json文件]")
            .define("spawnMob", true);
    
    public static final ForgeConfigSpec.ConfigValue<Boolean> ERO_RAID = BUILDER
            .comment("开启后侵蚀掠夺者和侵蚀劫掠兽会加入原版袭击事件")
            .define("eroRaid", true);
    
    public static final ForgeConfigSpec.ConfigValue<Boolean> ADD_STRUCTURE = BUILDER
            .comment("开启后会自然生成建筑结构,详细调整生成可修改[模组包/data/advancearmy/worldgen/中的结构json文件]")
            .define("addStructure", true);
    
    public static final ForgeConfigSpec.ConfigValue<Boolean> FIRE_LIGHT = BUILDER
            .comment("开启后武器开火会生成闪光方块")
            .define("useFireLight", true);
	
	
    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_MOB_SPAWN_DAY = BUILDER
            .comment("达到最大敌军生成频率所需要的天数")
            .defineInRange("maxMobSpawnDay", 20, 1, 1000);
    
    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_FRIEND_SPAWN_DAY = BUILDER
            .comment("达到最大友军生成频率所需要的天数")
            .defineInRange("maxFriendSpawnDay", 30, 1, 1000);

    public static final ForgeConfigSpec.ConfigValue<Integer> CYC_MOB_EVENT = BUILDER
            .comment("敌军入侵事件检测间隔天数")
            .defineInRange("cycleMobEvent", 5, 1, 100);
    public static final ForgeConfigSpec.ConfigValue<Integer> CYC_FRIEND_EVENT = BUILDER
            .comment("友军支援事件检测间隔天数")
            .defineInRange("cycleFriendEvent", 6, 1, 100);
    public static final ForgeConfigSpec.ConfigValue<Integer> CHANCE_MOB_EVENT = BUILDER
            .comment("敌军入侵事件触发几率")
            .defineInRange("mobEventChance", 40, 0, 100);
    public static final ForgeConfigSpec.ConfigValue<Integer> CHANCE_FRIEND_EVENT = BUILDER
            .comment("友军支援事件触发几率")
            .defineInRange("friendEventChance", 20, 0, 100);

    public static final ForgeConfigSpec.ConfigValue<Integer> CHANCE_MOB_SPAWN = BUILDER
            .comment("敌军生成总频率")
            .defineInRange("mobSpawnChance", 100, 0, 100);
    public static final ForgeConfigSpec.ConfigValue<Integer> CHANCE_FRIEND_SPAWN = BUILDER
            .comment("友军生成总频率")
            .defineInRange("friendSpawnChance", 100, 0, 100);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOBS_NOT_TARGETED = BUILDER
	.comment("不会被侵蚀生物主动攻击的生物")
	.defineList("Mobs Not Targeted",
			Lists.newArrayList(
					"minecraft:snow_golem",
					"minecraft:bat"
			),
			o -> o instanceof String
	);
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOBS_TARGETED = BUILDER
	.comment("会被侵蚀生物主动攻击的生物")
	.defineList("Mobs Targeted",
			Lists.newArrayList(
					"minecraft:ender_dragon",
					"minecraft:silverfish"
			),
			o -> o instanceof String
	);
	
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> FRIEND_NOT_TARGETED = BUILDER
	.comment("不会被友军士兵主动攻击的生物")
	.defineList("Friends Not Targeted",
			Lists.newArrayList(
					"minecraft:endermite"
			),
			o -> o instanceof String
	);
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> FRIEND_TARGETED = BUILDER
	.comment("会被友军士兵主动攻击的生物")
	.defineList("Friends Targeted",
			Lists.newArrayList(),
			o -> o instanceof String
	);
	
    // 静态字段，供代码直接访问
    public static List<String> mobsNotTargeted;
	public static List<String> mobsTargeted;
    public static List<String> friendNotTargeted;
	public static List<String> friendTargeted;
    // 存储配置规范的实例
    public static final ForgeConfigSpec SPEC = BUILDER.build();
    // 静态字段，用于在代码中访问配置值
    public static boolean vehicleDestroy;
    public static boolean spawnFriendTeam;
    public static boolean structureFriendTeam;
    public static boolean spawnSoldier;
    public static boolean spawnMob;
    public static boolean eroRaid;
    public static boolean addStructure;
	public static boolean useFireLight;
	
    public static int maxMobSpawnDay;
    public static int maxFriendSpawnDay;
    
	public static int cycleFriendEvent;
	public static int cycleMobEvent;
	public static int friendEventChance;
	public static int mobEventChance;
	
	public static int friendSpawnChance;
	public static int mobSpawnChance;
	
    // 获取所有配置类别（用于GUI）
    public static ForgeConfigSpec getSpec() {
        return SPEC;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        // 更新静态字段的值
        syncConfig();
    }
    
    // 同步配置值到静态字段
    public static void syncConfig() {
		mobsNotTargeted = List.copyOf(MOBS_NOT_TARGETED.get());
		mobsTargeted = List.copyOf(MOBS_TARGETED.get());
		
		friendNotTargeted = List.copyOf(FRIEND_NOT_TARGETED.get());
		friendTargeted = List.copyOf(FRIEND_TARGETED.get());
		
        vehicleDestroy = VEHICLE_DESTROY.get();
        spawnFriendTeam = SPAWN_TEAM.get();
        structureFriendTeam = STRUCTURE_TEAM.get();
        spawnSoldier = SPAWN_SOLIDER.get();
        spawnMob = SPAWN_MOB.get();
        eroRaid = ERO_RAID.get();
        addStructure = ADD_STRUCTURE.get();
		useFireLight = FIRE_LIGHT.get();
		
        maxMobSpawnDay = MAX_MOB_SPAWN_DAY.get();
        maxFriendSpawnDay = MAX_FRIEND_SPAWN_DAY.get();
		
		cycleFriendEvent = CYC_FRIEND_EVENT.get();
		cycleMobEvent = CYC_MOB_EVENT.get();
		
		friendEventChance = CHANCE_FRIEND_EVENT.get();
		mobEventChance = CHANCE_MOB_EVENT.get();
		
		friendSpawnChance = CHANCE_FRIEND_SPAWN.get();
		mobSpawnChance = CHANCE_MOB_SPAWN.get();
    }
    
    // 注册配置
    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "advancearmy-common.toml");
    }
}
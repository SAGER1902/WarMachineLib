package wmlib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = WarMachineLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WMConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue BULLET_DESTROY = BUILDER
            .comment("开启后子弹命中会摧毁脆弱方块")
            .define("bulletDestroy", true);
    public static final ForgeConfigSpec.BooleanValue EXP_DESTROY = BUILDER
            .comment("开启后爆炸会摧毁方块")
            .define("explosionDestroy", true);
    public static final ForgeConfigSpec.BooleanValue EXP_FLASH = BUILDER
            .comment("开启后爆炸会生成闪光方块")
            .define("explosionFlash", true);
			
    public static final ForgeConfigSpec.BooleanValue GRENADE_HIT_FRIEND = BUILDER
            .comment("开启后手榴弹会被友军单位阻挡")
            .define("grenadeHitFriend", false);
    public static final ForgeConfigSpec.BooleanValue USE_HIT = BUILDER
            .comment("开启后会显示命中信息同时播放命中音效")
            .define("hitInformation", true);
    public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_SHADERS = BUILDER
            .comment("开启后会启用自定义的着色器,关闭则会将一些渲染更改为原版的着色器以更好的兼容光影")
            .define("useCustomShaders", true);

    public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_COLOR = BUILDER
            .comment("开启后会启用自定义颜色光照方法,关闭则会使模型渲染无法正常受方块亮度影响")
            .define("useCustomModelLight", true);

	//@OnlyIn(Dist.CLIENT)
	public static boolean clientRender = false;
			
    public static final ForgeConfigSpec.IntValue HIT_X1 = BUILDER.comment("命中信息显示的 X坐标偏移量").defineInRange("hit_icon_x", 0, -8000, 8000);
	public static final ForgeConfigSpec.IntValue HIT_Y1 = BUILDER.comment("命中信息显示的 Y坐标偏移量").defineInRange("hit_icon_y", 0, -8000, 8000);
	public static final ForgeConfigSpec.IntValue HIT_X2 = BUILDER.comment("命中图标的 X坐标偏移量").defineInRange("hit_infor_x", 0, -8000, 8000);
	public static final ForgeConfigSpec.IntValue HIT_Y2 = BUILDER.comment("命中图标的 Y坐标偏移量").defineInRange("hit_infor_y", 0, -8000, 8000);
	public static final ForgeConfigSpec.IntValue SHOCK_SIZE = BUILDER.comment("视角震动效果的总幅度").defineInRange("shock_size", 100, 0, 1000);
    /*public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");
    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), WMConfig::validateItemName);*/

    static final ForgeConfigSpec SPEC = BUILDER.build();

    // 获取所有配置类别（用于GUI）
    public static ForgeConfigSpec getSpec() {
        return SPEC;
    }

    public static boolean bulletDestroy;
	public static boolean explosionDestroy;
	public static boolean explosionFlash;
	public static boolean grenadeHitFriend;
	public static int hit_icon_x;
	public static int hit_infor_x;
	public static int hit_icon_y;
	public static int hit_infor_y;
	public static boolean hitInformation;
	public static int shock_size;
	public static boolean useCustomShaders;
	public static boolean useCustomModelLight;
    /*public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(ResourceLocation.tryParse(itemName));
    }*/

    public static void syncConfig() {
        bulletDestroy = BULLET_DESTROY.get();
		explosionDestroy = EXP_DESTROY.get();
		explosionFlash = EXP_FLASH.get();
		hit_icon_x = HIT_X1.get();
		hit_icon_y = HIT_Y1.get();
		hit_infor_x = HIT_X2.get();
		hit_infor_y = HIT_Y2.get();
		hitInformation = USE_HIT.get();
		shock_size = SHOCK_SIZE.get();
		useCustomShaders = USE_CUSTOM_SHADERS.get();
		useCustomModelLight = USE_CUSTOM_COLOR.get();
        /*magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();
        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(itemName)))
                .collect(Collectors.toSet());*/
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
		syncConfig();
    }
}

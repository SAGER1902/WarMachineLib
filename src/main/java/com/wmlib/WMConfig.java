package wmlib;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import java.util.Collections;
import java.util.List;

public class WMConfig
{
    /**
     * Common related config options
     */
    public static class Common
    {
        public final Hit hit;
        public Common(ForgeConfigSpec.Builder builder)
        {
            builder.push("common");
            {
                this.hit = new Hit(builder);
            }
            builder.pop();
        }
    }

    public static class Hit
    {
        public final ForgeConfigSpec.BooleanValue hitInformation;
		
        public Hit(ForgeConfigSpec.Builder builder)
        {
			this.hitInformation = builder.comment("开启后会显示命中信息同时播放命中音效").define("hitInformation", true);
        }
    }
	
    /**
     * Client related config options
     */
    public static class Client
    {
        public final Display display;
        public Client(ForgeConfigSpec.Builder builder)
        {
            builder.push("client");
            {
                this.display = new Display(builder);
            }
            builder.pop();
        }
    }

    public static class Display
    {
		public final ForgeConfigSpec.IntValue hit_icon_x;
		public final ForgeConfigSpec.IntValue hit_icon_y;
		public final ForgeConfigSpec.IntValue hit_infor_x;
		public final ForgeConfigSpec.IntValue hit_infor_y;
		
		public final ForgeConfigSpec.IntValue shock_size;
        public Display(ForgeConfigSpec.Builder builder)
        {
			hit_icon_x = builder.comment("命中信息显示的 X坐标偏移量").defineInRange("hit_icon_x", 0, -8000, 8000);
			hit_icon_y = builder.comment("命中信息显示的 Y坐标偏移量").defineInRange("hit_icon_y", 0, -8000, 8000);
			hit_infor_x = builder.comment("命中图标的 X坐标偏移量").defineInRange("hit_infor_x", 0, -8000, 8000);
			hit_infor_y = builder.comment("命中图标的 Y坐标偏移量").defineInRange("hit_infor_y", 0, -8000, 8000);
			shock_size = builder.comment("视角震动效果的总幅度").defineInRange("shock_size", 100, 0, 1000);
        }
    }
    static final ForgeConfigSpec commonSpec;
    public static final WMConfig.Common COMMON;
    static final ForgeConfigSpec clientSpec;
    public static final WMConfig.Client CLIENT;
    static
    {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(WMConfig.Client::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static void saveClientConfig()
    {
        clientSpec.save();
    }
}

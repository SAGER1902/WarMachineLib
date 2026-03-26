package wmlib;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import net.minecraftforge.fml.loading.FMLLoader;
import wmlib.common.event.TeamEntityEvent;
import wmlib.client.event.RenderGunEvent;
import wmlib.client.event.RenderEntityEvent;
import wmlib.common.network.PacketHandler;
import wmlib.client.ClientTickHandler;
import wmlib.client.OverlayHandler;
import wmlib.init.ModEnchantments;
import wmlib.init.WMModEntities;
import wmlib.init.ModItems;
import wmlib.init.ModTabs;
import wmlib.common.block.BlockRegister;
import wmlib.common.tileentity.TileEntityRegister;

//import wmlib.rts.F9AnXiaAnJianShiProcedure;
import wmlib.rts.RtsmodeModMenus;
//import wmlib.rts.network.RightClickInteractMessage;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraft.network.FriendlyByteBuf;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraftforge.fml.DistExecutor;
import wmlib.client.config.GuiWMConfig;
import net.minecraftforge.client.ConfigScreenHandler;

import wmlib.client.ClientSetup;
@Mod(WarMachineLib.MOD_ID)
public class WarMachineLib
{
    public static final String MOD_ID = "wmlib";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));

    public WarMachineLib()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WMConfig.SPEC);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        WMModEntities.REGISTER.register(modEventBus);
		ModItems.register(modEventBus);
		BlockRegister.register(modEventBus);
		ModTabs.TABS.register(modEventBus);
		ModEnchantments.REGISTER.register(modEventBus);
		TileEntityRegister.BLOCK_ENTITIES.register(modEventBus);
		
        modEventBus.addListener(this::commonSetup);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
		
		RtsmodeModMenus.REGISTRY.register(modEventBus);
		
        MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new TeamEntityEvent());
		
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::init);
		/*DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        });*/
		
		if (FMLLoader.getDist().isClient()) {
			MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
			MinecraftForge.EVENT_BUS.register(new OverlayHandler());
			MinecraftForge.EVENT_BUS.register(new RenderEntityEvent());
			MinecraftForge.EVENT_BUS.register(new RenderGunEvent());
		}
    }

    /*private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 注册配置屏幕
            ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (client, parent) -> new GuiWMConfig(parent)
                )
            );
        });
    }*/

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("呵唉！！！！");
		
		PacketHandler.registerPlayMessage();
		
		LOGGER.info("哽！！！！");
    }
}

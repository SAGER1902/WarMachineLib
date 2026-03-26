package advancearmy;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.entity.raid.Raid;
//import advancearmy.init.ModEnchantments;
import advancearmy.init.ModEntities;
import advancearmy.init.ModItems;
import advancearmy.init.ModTabs;

import advancearmy.registry.ModStructureTypes;
import advancearmy.registry.ModStructurePieceTypes;

import advancearmy.event.RandomEventHandler;
import advancearmy.event.SASoundEvent;
// The value here should match an entry in the META-INF/mods.toml file
import advancearmy.item.ItemSpawn;
import wmlib.client.obj.SAObjModel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import advancearmy.world.SpawnChecker;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import advancearmy.client.config.GuiAdvanceArmyConfig;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.client.ConfigScreenHandler;
import advancearmy.client.ClientSetup;
@Mod(AdvanceArmy.MODID)
public class AdvanceArmy
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "advancearmy";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public AdvanceArmy()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AAConfig.SPEC);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.REGISTER.register(modEventBus);
		ModItems.register(modEventBus);
		ModTabs.TABS.register(modEventBus);
		ModStructureTypes.STRUCTURES.register(modEventBus);
		ModStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(modEventBus);
		SASoundEvent.REGISTER.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new RandomEventHandler());
		
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::init);
		/*DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        });*/
    }

    /*private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 注册配置屏幕
            ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (client, parent) -> new GuiAdvanceArmyConfig(parent)
                )
            );
        });
    }*/

    private void commonSetup(final FMLCommonSetupEvent event)
    {
		event.enqueueWork(() -> {
			if(AAConfig.eroRaid){
				Raid.RaiderType.create("ERO_PILLAGER", ModEntities.ENTITY_PI.get(), new int[]{1, 2, 3, 4, 5, 6, 7, 8});
				Raid.RaiderType.create("ERO_RAVAGER", ModEntities.ERO_RAV.get(), new int[]{0, 0, 1, 0, 1, 0, 2, 3});
			}

			SpawnPlacements.register((EntityType)ModEntities.ENTITY_SOLDIER.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.friendSpawn(livingType, worldIn, reason, pos, rand, 0));
			SpawnPlacements.register((EntityType)ModEntities.ENTITY_CONS.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.friendSpawn(livingType, worldIn, reason, pos, rand, 0.4F));

			SpawnPlacements.register((EntityType)ModEntities.ENTITY_EHUSK.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canNormalSpawn(livingType, worldIn, reason, pos, rand, 0.1F));
			
			SpawnPlacements.register((EntityType)ModEntities.ENTITY_EZOMBIE.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canNightSpawn(livingType, worldIn, reason, pos, rand, 0.2F));
			
			SpawnPlacements.register((EntityType)ModEntities.ENTITY_CREEPER.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canNightSpawn(livingType, worldIn, reason, pos, rand, 0.3F));
			
			SpawnPlacements.register((EntityType)ModEntities.ENTITY_SKELETON.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canNightSpawn(livingType, worldIn, reason, pos, rand, 0.2F));
			
			SpawnPlacements.register((EntityType)ModEntities.ENTITY_REB.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canNightSpawn(livingType, worldIn, reason, pos, rand, 0.4F));
			
			SpawnPlacements.register((EntityType)ModEntities.ENTITY_BIKE.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canGroundNightSpawn(livingType, worldIn, reason, pos, rand, 0.6F));
			
			SpawnPlacements.register((EntityType)ModEntities.ENTITY_PI.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canGroundNightSpawn(livingType, worldIn, reason, pos, rand, 0.5F));
			
			SpawnPlacements.register((EntityType)ModEntities.ENTITY_GST.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canGroundNightSpawn(livingType, worldIn, reason, pos, rand, 0.7F));
			
			SpawnPlacements.register((EntityType)ModEntities.ENTITY_PHA.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canNightSpawn(livingType, worldIn, reason, pos, rand, 0.3F));
			
			SpawnPlacements.register((EntityType)ModEntities.ERO_SPIDER.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canNightSpawn(livingType, worldIn, reason, pos, rand, 0.5F));
			
			SpawnPlacements.register((EntityType)ModEntities.ERO_RAV.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canGroundNightSpawn(livingType, worldIn, reason, pos, rand, 0.8F));
			
			SpawnPlacements.register((EntityType)ModEntities.ENTITY_GIANT.get(), 
			SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (livingType, worldIn, reason, pos, rand)->SpawnChecker.canHardSpawn(livingType, worldIn, reason, pos, rand, 0.9F));
		});
    }
}

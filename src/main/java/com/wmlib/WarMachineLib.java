package wmlib;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.eventbus.api.EventPriority;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wmlib.common.item.ItemMouse;
import wmlib.common.item.ItemBless;
import wmlib.common.enchantment.EnchantmentTypes;
import wmlib.common.enchantment.ModEnchantments;
import wmlib.common.event.TeamEntityEvent;
import wmlib.client.event.RenderGunZoom;
import wmlib.client.event.RenderGunEvent;
import wmlib.client.event.RenderEntityEvent;
import wmlib.client.OverlayHandler;
import wmlib.client.ClientTickHandler;
//import wmlib.common.world.BiomeRegister;
import wmlib.common.network.PacketHandler;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityGrenade;
import wmlib.common.bullet.EntityShell;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityBlock;
import wmlib.common.bullet.EntityFlare;
import wmlib.client.render.RenderBulletBase;
import wmlib.client.render.RenderFlare;
import wmlib.client.render.LayerItemGun;
import wmlib.common.bullet.EntityMine;
import wmlib.common.bullet.EntityRad;
import wmlib.client.render.RenderRad;
import wmlib.client.render.RenderMine;
import wmlib.common.block.BlockRegister;
import wmlib.common.tileentity.TileEntityRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;

import wmlib.client.ClientProxy;
import wmlib.common.CommonProxy;
@Mod(WarMachineLib.MOD_ID)
public class WarMachineLib {
    public static final String MOD_ID = "wmlib";
	public static final Logger LOGGER = LogManager.getLogger(WarMachineLib.MOD_ID);
    public static final ItemGroup GROUP = new ItemGroup(MOD_ID)
    {
        @Override
        public ItemStack makeIcon()
        {
            ItemStack stack = new ItemStack(WarMachineLib.item_squad);
            return stack;
        }
        @Override
        public void fillItemList(NonNullList<ItemStack> items)
        {
            super.fillItemList(items);
        }
    }.setEnchantmentCategories(EnchantmentTypes.CREATURE, EnchantmentTypes.VEHICLE);
	public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public static Item item_squad;
	public static Item item_bless_tool;

    public static EntityType<EntityBullet> ENTITY_BULLET;
    public static EntityType<EntityGrenade> ENTITY_GRENADE;
    public static EntityType<EntityShell> ENTITY_SHELL;
	public static EntityType<EntityMissile> ENTITY_MISSILE;
    public static EntityType<EntityBlock> ENTITY_BLOCK;
	public static EntityType<EntityFlare> ENTITY_FLARE;
	public static EntityType<EntityRad> ENTITY_RAD;
	public static EntityType<EntityMine> ENTITY_MINE;
    public WarMachineLib() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, WMConfig.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WMConfig.commonSpec);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WMConfig.serverSpec);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new TeamEntityEvent());
        modEventBus.addListener(this::initClient);
		modEventBus.addListener(this::onCommonSetup);
		ModEnchantments.REGISTER.register(modEventBus);
		BlockRegister.BLOCKS.register(modEventBus);
		TileEntityRegister.TILE_ENTITY_TYPES.register(modEventBus);
		
		//WMSoundEvent.REGISTER.register(modEventBus);
        if (FMLLoader.getDist().isClient()) {
			MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
			MinecraftForge.EVENT_BUS.register(new OverlayHandler());
			MinecraftForge.EVENT_BUS.register(new RenderEntityEvent());
        	MinecraftForge.EVENT_BUS.register(new RenderGunEvent());
        	MinecraftForge.EVENT_BUS.register(new RenderGunZoom());
        }
        DistExecutor.runWhenOn(Dist.CLIENT,()->()->{
            modEventBus.addListener(this::doClientStuff);
        });
		
    }
	
    @OnlyIn(Dist.CLIENT)
    private void doClientStuff(final FMLClientSetupEvent event) {
        /*Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().values().stream()
                .forEach((lr)->lr.addLayer(new LayerItemGun(lr)));*/
	}
	
	private void onCommonSetup(FMLCommonSetupEvent event)
    {
        PacketHandler.registerPlayMessage();
    }	
    private void initClient(FMLClientSetupEvent event) {
		TileEntityRegister.bindRenderers(event);
        RenderingRegistry.registerEntityRenderingHandler(ENTITY_BULLET, RenderBulletBase::new);
        RenderingRegistry.registerEntityRenderingHandler(ENTITY_SHELL, RenderBulletBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_MISSILE, RenderBulletBase::new);
        RenderingRegistry.registerEntityRenderingHandler(ENTITY_FLARE, RenderFlare::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_RAD, RenderRad::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_BLOCK, RenderBulletBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_GRENADE, RenderBulletBase::new);
		//RenderingRegistry.registerEntityRenderingHandler(ENTITY_MINE, RenderMine::new);
    }
    //static int id = 0;
    @EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
    	@SubscribeEvent
    	public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
    		{
    			item_bless_tool =  new ItemBless(new Item.Properties().stacksTo(1).tab(GROUP))
    					.setRegistryName(new ResourceLocation(MOD_ID,"item_bless_tool"));
    			event.getRegistry().register(item_bless_tool);
    		}
    		{
    			item_squad =  new ItemMouse(new Item.Properties().tab(GROUP))
    					.setRegistryName(new ResourceLocation(MOD_ID,"item_squad"));
    			event.getRegistry().register(item_squad);
    		}
    	}

    	@SubscribeEvent
        public static void registerEntityTypes(RegistryEvent.Register<EntityType<?>> event) {
			ENTITY_BULLET = EntityType.Builder.<EntityBullet>of(EntityBullet::new,EntityClassification.MISC)/*.setCustomClientFactory(EntityBullet::new)*/.sized(0.15F, 0.15F).clientTrackingRange(60).noSave()/*.setUpdateInterval(10)*/.build("wmlib:entitybullet");
        	ENTITY_BULLET.setRegistryName(new ResourceLocation("wmlib", "entitybullet"));

			ENTITY_GRENADE = EntityType.Builder.<EntityGrenade>of(EntityGrenade::new,EntityClassification.MISC).setCustomClientFactory(EntityGrenade::new).sized(0.15F, 0.15F).clientTrackingRange(60).noSave()/*.setUpdateInterval(10)*/.build("wmlib:entitygrenade");
        	ENTITY_GRENADE.setRegistryName(new ResourceLocation("wmlib", "entitygrenade"));

        	ENTITY_SHELL = EntityType.Builder.<EntityShell>of(EntityShell::new,EntityClassification.MISC)/*.setCustomClientFactory(EntityShell::new)*/.sized(0.2F, 0.2F).setShouldReceiveVelocityUpdates(false).clientTrackingRange(60).noSave()/*.setUpdateInterval(20)*/.build("wmlib:entityshell");
        	ENTITY_SHELL.setRegistryName(new ResourceLocation("wmlib", "entityshell"));
			
        	ENTITY_MISSILE = EntityType.Builder.<EntityMissile>of(EntityMissile::new,EntityClassification.MISC).setCustomClientFactory(EntityMissile::new).sized(0.15F, 0.15F).clientTrackingRange(60).noSave().build("wmlib:entitymissile");
        	ENTITY_MISSILE.setRegistryName(new ResourceLocation("wmlib", "entitymissile"));
			
    		ENTITY_FLARE = EntityType.Builder.<EntityFlare>of(EntityFlare::new,EntityClassification.CREATURE).setCustomClientFactory(EntityFlare::new).sized(1F, 1F).setShouldReceiveVelocityUpdates(true).build("wmlib:entityflare");
    				GlobalEntityTypeAttributes.put(ENTITY_FLARE, EntityFlare.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).build());
    		ENTITY_FLARE.setRegistryName(new ResourceLocation("wmlib", "entityflare"));
			
    		ENTITY_RAD = EntityType.Builder.<EntityRad>of(EntityRad::new,EntityClassification.CREATURE).setCustomClientFactory(EntityRad::new).sized(1F, 1F).setShouldReceiveVelocityUpdates(true).build("wmlib:entityrad");
    				GlobalEntityTypeAttributes.put(ENTITY_RAD, EntityRad.createMobAttributes().build());
    		ENTITY_RAD.setRegistryName(new ResourceLocation("wmlib", "entityrad"));
			
    		/*ENTITY_MINE = EntityType.Builder.<EntityMine>of(EntityMine::new,EntityClassification.CREATURE).setCustomClientFactory(EntityMine::new).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(true).build("wmlib:entitymine");
    				GlobalEntityTypeAttributes.put(ENTITY_MINE, EntityMine.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D).build());
    		ENTITY_MINE.setRegistryName(new ResourceLocation("wmlib", "entitymine"));*/
			
        	ENTITY_BLOCK = EntityType.Builder.<EntityBlock>of(EntityBlock::new,EntityClassification.MISC).setCustomClientFactory(EntityBlock::new).sized(0.15F, 0.15F).setShouldReceiveVelocityUpdates(false).setTrackingRange(61).setUpdateInterval(61).build("wmlib:entityblock");
 	        ENTITY_BLOCK.setRegistryName(new ResourceLocation("wmlib", "entityblock"));
			
        	event.getRegistry().registerAll(ENTITY_BULLET, ENTITY_SHELL, ENTITY_MISSILE, ENTITY_FLARE, ENTITY_RAD, ENTITY_BLOCK, ENTITY_GRENADE/*, ENTITY_MINE*/);
        }
    }
}//end
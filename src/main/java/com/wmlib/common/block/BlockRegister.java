package wmlib.common.block;

import wmlib.WarMachineLib;
import wmlib.common.block.MelonBlock;
import wmlib.common.block.MuzzleFlashBlock;
import wmlib.common.item.ItemGlint;
import java.util.Arrays;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod.EventBusSubscriber(modid = WarMachineLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegister {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WarMachineLib.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WarMachineLib.MOD_ID);
    
    // 注册方块
    public static final RegistryObject<MelonBlock> IRON_MELON = BLOCKS.register("iron_melon", () -> new MelonBlock(Block.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), 0));
    public static final RegistryObject<MelonBlock> GOLD_MELON = BLOCKS.register("gold_melon", () -> new MelonBlock(Block.Properties.copy(Blocks.GOLD_BLOCK).noOcclusion(), 0));
    public static final RegistryObject<MelonBlock> EMERALD_MELON = BLOCKS.register("emerald_melon", () -> new MelonBlock(Block.Properties.copy(Blocks.EMERALD_BLOCK).noOcclusion(), 0));
    public static final RegistryObject<MelonBlock> DIAMOND_MELON = BLOCKS.register("diamond_melon", () -> new MelonBlock(Block.Properties.copy(Blocks.DIAMOND_BLOCK).noOcclusion(), 0));
    public static final RegistryObject<MelonBlock> IRON_MELON1 = BLOCKS.register("iron_melon1", () -> new MelonBlock(Block.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), 1));
    public static final RegistryObject<MelonBlock> GOLD_MELON1 = BLOCKS.register("gold_melon1", () -> new MelonBlock(Block.Properties.copy(Blocks.GOLD_BLOCK).noOcclusion().lightLevel(i -> 14), 1));
    public static final RegistryObject<MelonBlock> EMERALD_MELON1 = BLOCKS.register("emerald_melon1", () -> new MelonBlock(Block.Properties.copy(Blocks.EMERALD_BLOCK).noOcclusion().lightLevel(i -> 14), 1));
    public static final RegistryObject<MelonBlock> DIAMOND_MELON1 = BLOCKS.register("diamond_melon1", () -> new MelonBlock(Block.Properties.copy(Blocks.DIAMOND_BLOCK).noOcclusion().lightLevel(i -> 14), 1));
    public static final RegistryObject<MelonBlock> ASSULT = BLOCKS.register("assult", () -> new MelonBlock(Block.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), 5));
    public static final RegistryObject<MelonBlock> RECON = BLOCKS.register("recon", () -> new MelonBlock(Block.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), 6));
    public static final RegistryObject<MelonBlock> ENGINEER = BLOCKS.register("engineer", () -> new MelonBlock(Block.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), 7));
    public static final RegistryObject<MelonBlock> MEDIC = BLOCKS.register("medic", () -> new MelonBlock(Block.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), 8));
    public static final RegistryObject<MelonBlock> SUPPORT = BLOCKS.register("support", () -> new MelonBlock(Block.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), 9));
    public static final RegistryObject<MuzzleFlashBlock> MUZZB = BLOCKS.register("muzzb", () -> new MuzzleFlashBlock(7,3));
	public static final RegistryObject<MuzzleFlashBlock> CANNONB = BLOCKS.register("cannonb", () -> new MuzzleFlashBlock(14,4));
	
    // 注册普通方块物品
    public static final RegistryObject<Item> IRON_MELON_ITEM = ITEMS.register("iron_melon", () -> new BlockItem(IRON_MELON.get(), new Item.Properties()));
    public static final RegistryObject<Item> GOLD_MELON_ITEM = ITEMS.register("gold_melon", () -> new BlockItem(GOLD_MELON.get(), new Item.Properties()));
    public static final RegistryObject<Item> EMERALD_MELON_ITEM = ITEMS.register("emerald_melon", () -> new BlockItem(EMERALD_MELON.get(), new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_MELON_ITEM = ITEMS.register("diamond_melon", () -> new BlockItem(DIAMOND_MELON.get(), new Item.Properties()));
    public static final RegistryObject<Item> ASSULT_ITEM = ITEMS.register("assult", () -> new BlockItem(ASSULT.get(), new Item.Properties()));
    public static final RegistryObject<Item> RECON_ITEM = ITEMS.register("recon", () -> new BlockItem(RECON.get(), new Item.Properties()));
    public static final RegistryObject<Item> ENGINEER_ITEM = ITEMS.register("engineer", () -> new BlockItem(ENGINEER.get(), new Item.Properties()));
    public static final RegistryObject<Item> MEDIC_ITEM = ITEMS.register("medic", () -> new BlockItem(MEDIC.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUPPORT_ITEM = ITEMS.register("support", () -> new BlockItem(SUPPORT.get(), new Item.Properties()));
    public static final RegistryObject<Item> MUZZB_ITEM = ITEMS.register("muzzb", () -> new BlockItem(MUZZB.get(), new Item.Properties()));
	public static final RegistryObject<Item> CANNONB_ITEM = ITEMS.register("cannonb", () -> new BlockItem(CANNONB.get(), new Item.Properties()));
	
    // 注册特殊物品（带发光效果）
    public static final RegistryObject<Item> IRON_MELON1_ITEM = ITEMS.register("iron_melon1", () -> new ItemGlint(IRON_MELON1.get(), new Item.Properties()));
    public static final RegistryObject<Item> GOLD_MELON1_ITEM = ITEMS.register("gold_melon1", () -> new ItemGlint(GOLD_MELON1.get(), new Item.Properties()));
    public static final RegistryObject<Item> EMERALD_MELON1_ITEM = ITEMS.register("emerald_melon1", () -> new ItemGlint(EMERALD_MELON1.get(), new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_MELON1_ITEM = ITEMS.register("diamond_melon1", () -> new ItemGlint(DIAMOND_MELON1.get(), new Item.Properties()));
    
    // 在主Mod类中注册的方法
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
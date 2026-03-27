package wmlib.common.block;

import wmlib.WarMachineLib;
import wmlib.common.block.MelonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import wmlib.common.item.ItemGlint;
import java.util.Arrays;

@Mod.EventBusSubscriber(modid=WarMachineLib.MOD_ID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegister {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WarMachineLib.MOD_ID);
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
	/**
	 * register block items.
	 */
	@SubscribeEvent
	public static void registerBlockItem(RegistryEvent.Register<Item> ev){
		IForgeRegistry<Item> itemgs = ev.getRegistry();
		Arrays.asList(
				IRON_MELON1, GOLD_MELON1, EMERALD_MELON1, DIAMOND_MELON1
		).forEach(block -> {
			itemgs.register(new ItemGlint(block.get(), new Item.Properties().tab(WarMachineLib.GROUP)).setRegistryName(block.get().getRegistryName()));
		});
		IForgeRegistry<Item> items = ev.getRegistry();
		Arrays.asList(
				IRON_MELON, GOLD_MELON, EMERALD_MELON, DIAMOND_MELON, ASSULT, RECON, ENGINEER, MEDIC, SUPPORT
		).forEach(block -> {
			items.register(new BlockItem(block.get(), new Item.Properties().tab(WarMachineLib.GROUP)).setRegistryName(block.get().getRegistryName()));
		});
	}
	
}

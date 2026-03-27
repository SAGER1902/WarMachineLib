package wmlib.common.tileentity;

import wmlib.WarMachineLib;
import wmlib.common.tileentity.MelonBlockTileEntity;
import wmlib.common.block.BlockRegister;
import wmlib.client.render.RenderMelonBlock;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegister {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, WarMachineLib.MOD_ID);
	public static final RegistryObject<TileEntityType<MelonBlockTileEntity>> MELON = TILE_ENTITY_TYPES.register("watermelon", () -> {
		return TileEntityType.Builder.of(MelonBlockTileEntity::new , BlockRegister.IRON_MELON.get(), BlockRegister.GOLD_MELON.get(),BlockRegister.EMERALD_MELON.get(), BlockRegister.DIAMOND_MELON.get(),
		BlockRegister.IRON_MELON1.get(), BlockRegister.GOLD_MELON1.get(),BlockRegister.EMERALD_MELON1.get(), BlockRegister.DIAMOND_MELON1.get(),
		BlockRegister.ASSULT.get(), BlockRegister.RECON.get(), BlockRegister.ENGINEER.get(), BlockRegister.MEDIC.get(), BlockRegister.SUPPORT.get()).build(null);
	});
	
	@OnlyIn(Dist.CLIENT)
	public static void bindRenderers(FMLClientSetupEvent ev) {
		ev.getMinecraftSupplier().get().tell(() -> {
			ClientRegistry.bindTileEntityRenderer(TileEntityRegister.MELON.get(), RenderMelonBlock::new);
		});
	}
	
}

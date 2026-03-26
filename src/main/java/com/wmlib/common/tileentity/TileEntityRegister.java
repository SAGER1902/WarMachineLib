package wmlib.common.tileentity;

import wmlib.WarMachineLib;

import wmlib.common.block.BlockRegister;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityRegister {
    // 修改注册表名称
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WarMachineLib.MOD_ID);
    
    public static final RegistryObject<BlockEntityType<MelonBlockTileEntity>> MELON = 
        BLOCK_ENTITIES.register("watermelon", () -> 
            BlockEntityType.Builder.of(
                MelonBlockTileEntity::new,
                BlockRegister.IRON_MELON.get(),
                BlockRegister.GOLD_MELON.get(),
                BlockRegister.EMERALD_MELON.get(),
                BlockRegister.DIAMOND_MELON.get(),
                BlockRegister.IRON_MELON1.get(),
                BlockRegister.GOLD_MELON1.get(),
                BlockRegister.EMERALD_MELON1.get(),
                BlockRegister.DIAMOND_MELON1.get(),
                BlockRegister.ASSULT.get(),
                BlockRegister.RECON.get(),
                BlockRegister.ENGINEER.get(),
                BlockRegister.MEDIC.get(),
                BlockRegister.SUPPORT.get()
            ).build(null)
        );
}

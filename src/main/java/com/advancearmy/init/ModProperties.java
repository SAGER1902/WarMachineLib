package advancearmy.init;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.minecraft.resources.ResourceLocation;
@net.minecraftforge.fml.common.Mod.EventBusSubscriber(bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModProperties {
    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(ModItems.unit_capture.get(), new ResourceLocation("advancearmy", "unit_capture_have"),
                (itemStack, clientWorld, livingEntity, seed) -> {
					if (!itemStack.isEmpty() && itemStack.getOrCreateTag().contains("StoreInfo")) {
						return 1;
					}else{
						return 0;
					}
				}));
    }
}
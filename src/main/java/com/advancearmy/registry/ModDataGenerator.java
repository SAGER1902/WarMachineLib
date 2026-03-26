package advancearmy.registry;

import advancearmy.AdvanceArmy;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import advancearmy.AAConfig;
@Mod.EventBusSubscriber(modid = AdvanceArmy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {

    private ModDataGenerator() {
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
		if(AAConfig.addStructure){
			DataGenerator generator = event.getGenerator();
			boolean server = event.includeServer();
			generator.addProvider(server, (DataProvider.Factory<ModDatapackBuiltinEntriesProvider>) output ->
					new ModDatapackBuiltinEntriesProvider(output, event.getLookupProvider())
			);
		}
    }

}

package advancearmy.client;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import advancearmy.client.config.GuiAdvanceArmyConfig;
import net.minecraftforge.fml.ModLoadingContext;
public class ClientSetup {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::clientSetup);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (client, parent) -> new GuiAdvanceArmyConfig(parent)
                )
            );
        });
    }
}
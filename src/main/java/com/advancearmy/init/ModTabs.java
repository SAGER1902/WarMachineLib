package advancearmy.init;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;
@net.minecraftforge.fml.common.Mod.EventBusSubscriber(bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "advancearmy");
    public static final RegistryObject<CreativeModeTab> ITEM_TAB = TABS.register("support",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.advancearmy.item"))
                    .icon(() -> new ItemStack(ModItems.targetgun.get()))
                    .displayItems((param, output) -> ModItems.ITEMS.getEntries().forEach(registryObject -> {
						output.accept(registryObject.get());
                    }))
                    .build());
    public static final RegistryObject<CreativeModeTab> UNIT_TAB = TABS.register("unit",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.advancearmy.unit"))
                    .icon(() -> new ItemStack(ModItems.item_spawn_tank.get()))
                    .displayItems((param, output) -> ModItems.UNITS.getEntries().forEach(registryObject -> {
						if (registryObject.get() != ModItems.item_spawn_conscript_big.get()){
							output.accept(registryObject.get());
						}
                    }))
                    .build());
    public static final RegistryObject<CreativeModeTab> MOB_TAB = TABS.register("mob",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.advancearmy.mob"))
                    .icon(() -> new ItemStack(ModItems.challenge_mob.get()))
                    .displayItems((param, output) -> ModItems.MOBS.getEntries().forEach(registryObject -> {
						if (registryObject.get() != ModItems.mob_spawn_dragonturret.get()
						&&registryObject.get() != ModItems.mob_spawn_bug.get() && registryObject.get() != ModItems.challenge_sea.get()){
							output.accept(registryObject.get());
						}
                    }))
                    .build());
}

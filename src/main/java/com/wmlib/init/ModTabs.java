package wmlib.init;
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
import wmlib.common.block.BlockRegister;
@net.minecraftforge.fml.common.Mod.EventBusSubscriber(bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "wmlib");
    public static final RegistryObject<CreativeModeTab> ITEM_TAB = TABS.register("item",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.wmlib"))
                    .icon(() -> new ItemStack(ModItems.item_squad.get()))
					.displayItems((parameters, output) -> {
						// 一次性添加所有物品
						ModItems.ITEMS.getEntries().forEach(registryObject -> {
							output.accept(registryObject.get());
						});
						BlockRegister.ITEMS.getEntries().forEach(registryObject -> {
							if (registryObject.get() != BlockRegister.MUZZB_ITEM.get()&&
							registryObject.get() != BlockRegister.CANNONB_ITEM.get()){
								output.accept(registryObject.get());
							}
						});
						BlockRegister.BLOCKS.getEntries().forEach(registryObject -> {
							output.accept(registryObject.get());
						});
					})
                    .build());
}

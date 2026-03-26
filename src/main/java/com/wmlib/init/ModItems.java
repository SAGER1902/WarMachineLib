package wmlib.init;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wmlib.client.obj.SAObjModel;
import wmlib.common.item.ItemMouse;
import wmlib.common.item.ItemBless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "wmlib");
    static int id = 0;
	static int mobid = 0;
	static int spid = 0;
	static int dfid = 0;
	public static final RegistryObject<ItemMouse> item_squad = ITEMS.register("item_squad", () -> new ItemMouse(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<ItemBless> item_bless_tool = ITEMS.register("item_bless_tool", () -> new ItemBless(new Item.Properties().stacksTo(1)));
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}

package wmlib.rts;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wmlib.rts.RtsMoShiMenu;

public class RtsmodeModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "wmlib");
	public static final RegistryObject<MenuType<RtsMoShiMenu>> RTS_MO_SHI = REGISTRY.register("rts_mo_shi", () -> IForgeMenuType.create(RtsMoShiMenu::new));
}

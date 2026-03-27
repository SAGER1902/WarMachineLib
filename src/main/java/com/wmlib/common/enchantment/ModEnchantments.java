package wmlib.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEnchantments
{
    public static final DeferredRegister<Enchantment> REGISTER = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "wmlib");
    public static final RegistryObject<Enchantment> MACHINE_SOUL = REGISTER.register("machine_soul", SoulEnchantment::new);
    public static final RegistryObject<Enchantment> SELF_FIX = REGISTER.register("self_fix", HealEnchantment::new);
    public static final RegistryObject<Enchantment> ARMOR = REGISTER.register("armor", ArmorEnchantment::new);
    public static final RegistryObject<Enchantment> RELOAD = REGISTER.register("reload", ReloadEnchantment::new);
    public static final RegistryObject<Enchantment> POWER = REGISTER.register("power", PowerEnchantment::new);
    public static final RegistryObject<Enchantment> PROTECT = REGISTER.register("protect", ProtectEnchantment::new);
	public static final RegistryObject<Enchantment> CONNECT = REGISTER.register("connect", ControlEnchantment::new);
    //Gravity Impulse (3 levels) - nearby entities will get knocked away from the target location
}

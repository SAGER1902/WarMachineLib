package wmlib.common.enchantment;
import wmlib.common.item.ItemBless;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
public class EnchantmentTypes
{
    public static final EnchantmentCategory VEHICLE = EnchantmentCategory.create("wmlib:vehicle", item -> item instanceof ItemBless);
	public static final EnchantmentCategory CREATURE = EnchantmentCategory.create("wmlib:creature", item -> item instanceof ItemBless);
}

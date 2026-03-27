package wmlib.common.enchantment;

import wmlib.common.item.ItemBless;
import net.minecraft.enchantment.EnchantmentType;
public class EnchantmentTypes
{
    public static final EnchantmentType VEHICLE = EnchantmentType.create("wmlib:vehicle", item -> item.getItem() instanceof ItemBless);
	public static final EnchantmentType CREATURE = EnchantmentType.create("wmlib:creature", item -> item.getItem() instanceof ItemBless);
}

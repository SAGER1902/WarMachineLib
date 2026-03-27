package wmlib.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.enchantment.Enchantment.Rarity;
import wmlib.common.item.ItemBless;
import net.minecraft.item.ItemStack;
public abstract class VehicleEnchantment extends Enchantment
{
    private Type type;

    protected VehicleEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots, Type type)
    {
        super(rarityIn, typeIn, slots);
        this.type = type;
    }

	/*public boolean canEnchant(ItemStack p_92089_1_) {
	  return p_92089_1_.getItem() instanceof ItemBless ? true : super.canEnchant(p_92089_1_);
	}*/

    /*@Override
    protected boolean checkCompatibility(Enchantment enchantment)
    {
        if(enchantment instanceof VehicleEnchantment)
        {
            return ((VehicleEnchantment) enchantment).type != this.type;
        }
        return super.checkCompatibility(enchantment);
    }*/

    public enum Type
    {
        WEAPON, AMMO, PROJECTILE
    }
}

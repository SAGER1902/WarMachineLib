package wmlib.common.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;;
import net.minecraft.world.item.enchantment.Enchantment.Rarity; // 新路径
import wmlib.common.item.ItemBless;
import net.minecraft.world.item.ItemStack;     
public abstract class VehicleEnchantment extends Enchantment
{
    private Type type;

    protected VehicleEnchantment(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots, Type type)
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

package wmlib.common.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

import net.minecraft.enchantment.Enchantment.Rarity;

public class ControlEnchantment extends VehicleEnchantment
{
    public ControlEnchantment()
    {
        super(Rarity.RARE, EnchantmentTypes.VEHICLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.PROJECTILE);
    }
	  
    @Override
    public int getMinCost(int level)
    {
        return 15;
    }

    @Override
    public int getMaxCost(int level)
    {
        return super.getMinCost(level) + 30;
    }
}

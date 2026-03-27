package wmlib.common.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

import net.minecraft.enchantment.Enchantment.Rarity;

public class ArmorEnchantment extends VehicleEnchantment
{
    public ArmorEnchantment()
    {
        super(Rarity.COMMON, EnchantmentTypes.VEHICLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.PROJECTILE);
    }
	  
    @Override
    public int getMinCost(int level)
    {
        return 10;
    }

    @Override
    public int getMaxLevel()
    {
        return 5;
    }

    @Override
    public int getMaxCost(int level)
    {
        return super.getMinCost(level) + 20;
    }
}

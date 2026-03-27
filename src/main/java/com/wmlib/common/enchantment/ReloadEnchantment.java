package wmlib.common.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

import net.minecraft.enchantment.Enchantment.Rarity;

public class ReloadEnchantment extends VehicleEnchantment
{
    public ReloadEnchantment()
    {
        super(Rarity.COMMON, EnchantmentTypes.VEHICLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.PROJECTILE);
    }
	  
    @Override
    public int getMinCost(int level)
    {
        return 8;
    }

    @Override
    public int getMaxLevel()
    {
        return 4;
    }

    @Override
    public int getMaxCost(int level)
    {
        return super.getMinCost(level) + 16;
    }
}

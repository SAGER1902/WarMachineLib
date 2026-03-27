package wmlib.common.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

import net.minecraft.enchantment.Enchantment.Rarity;

public class ProtectEnchantment extends VehicleEnchantment
{
    public ProtectEnchantment()
    {
        super(Rarity.UNCOMMON, EnchantmentTypes.VEHICLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.PROJECTILE);
    }
	  
    @Override
    public int getMinCost(int level)
    {
        return 15;
    }

    @Override
    public int getMaxLevel()
    {
        return 4;
    }

    @Override
    public int getMaxCost(int level)
    {
        return super.getMinCost(level) + 30;
    }
}

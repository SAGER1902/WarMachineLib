package wmlib.common.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

import net.minecraft.enchantment.Enchantment.Rarity;

public class SoulEnchantment extends VehicleEnchantment
{
    public SoulEnchantment()
    {
        super(Rarity.RARE, EnchantmentTypes.VEHICLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.PROJECTILE);
    }
	
	/*COMMON(10),
	UNCOMMON(5),
	RARE(2),
	VERY_RARE(1);*/
	  
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

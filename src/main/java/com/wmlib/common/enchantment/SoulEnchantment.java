package wmlib.common.enchantment;

import net.minecraft.world.entity.EquipmentSlot;;

import net.minecraft.world.item.enchantment.Enchantment.Rarity; // 新路径

public class SoulEnchantment extends VehicleEnchantment
{
    public SoulEnchantment()
    {
        super(Rarity.RARE, EnchantmentTypes.VEHICLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND}, Type.PROJECTILE);
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

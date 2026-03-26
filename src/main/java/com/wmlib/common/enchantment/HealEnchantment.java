package wmlib.common.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment.Rarity; // 新路径

public class HealEnchantment extends VehicleEnchantment
{
    public HealEnchantment()
    {
        super(Rarity.UNCOMMON, EnchantmentTypes.VEHICLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND}, Type.AMMO);
    }
	
	/*COMMON(10),
	UNCOMMON(5),
	RARE(2),
	VERY_RARE(1);*/
	  
    @Override
    public int getMinCost(int level)
    {
        return 10;
    }

    @Override
    public int getMaxLevel()
    {
        return 4;
    }

    @Override
    public int getMaxCost(int level)
    {
        return super.getMinCost(level) + 20;
    }
}

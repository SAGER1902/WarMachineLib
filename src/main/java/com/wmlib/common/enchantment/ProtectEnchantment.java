package wmlib.common.enchantment;

import net.minecraft.world.entity.EquipmentSlot;;

import net.minecraft.world.item.enchantment.Enchantment.Rarity; // 新路径

public class ProtectEnchantment extends VehicleEnchantment
{
    public ProtectEnchantment()
    {
        super(Rarity.UNCOMMON, EnchantmentTypes.VEHICLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND}, Type.PROJECTILE);
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

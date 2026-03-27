package wmlib.util;

import wmlib.common.enchantment.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
public class WMEnchantmentHelper
{
    public static int getSoulEnchantment(ItemStack stack)
    {
        int count = 0;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MACHINE_SOUL.get(), stack);
        if(level > 0){
            count = level;
        }
        return count;
    }
	
    public static int getConnectEnchantment(ItemStack stack)
    {
        int count = 0;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.CONNECT.get(), stack);
        if(level > 0){
            count = level;
        }
        return count;
    }
	
    public static int getHealEnchantment(ItemStack stack)
    {
        int count = 0;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SELF_FIX.get(), stack);
        if(level > 0){
            count = level;
        }
        return count;
    }

    public static int getArmorEnchantment(ItemStack stack)
    {
		int count = 0;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.ARMOR.get(), stack);
        if(level > 0){
			if(level>10)level=10;
            count = level;
        }
        return count;
    }

    public static float getReloadEnchantment(ItemStack stack)
    {
        float count = 1;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.RELOAD.get(), stack);
        if(level > 0){
            count = 1-level*0.1F;
        }
		if(count<0.1F)count = 0.1F;
        return count;
    }

    public static int getPowerEnchantment(ItemStack stack)
    {
        int count = 0;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.POWER.get(), stack);
        if(level > 0){
            count = level;
        }
        return count;
    }

    public static int getProtectEnchantment(ItemStack stack)
    {
        int count = 0;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.PROTECT.get(), stack);
        if(level > 0){
            count = level;
        }
        return count;
    }

    /*public static int getLoyaltyEnchantment(ItemStack stack)
    {
        int count = 0;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LOYALTY.get(), stack);
        if(level > 0){
            count = level;
        }
        return count;
    }*/
}

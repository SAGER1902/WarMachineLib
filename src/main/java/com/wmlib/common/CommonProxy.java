package wmlib.common;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.client.world.ClientWorld;
import safx.util.EntityCondition;

//@Mod.EventBusSubscriber
public class CommonProxy{
    public void onShootAnimation(PlayerEntity player, ItemStack itemstack) {}
    public void onFireAnimation(float count, float roll) {}
}

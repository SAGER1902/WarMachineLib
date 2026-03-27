package wmlib.client;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.world.ClientWorld;
import wmlib.common.CommonProxy;
import java.util.Random;
@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void onFireAnimation(float count, float roll) {
		if(roll!=0)RenderParameters.expPitch += roll;
		if(count!=0){
			RenderParameters.rate = Math.min(RenderParameters.rate + 0.07f, 1f);
			float recoilPitchGripFactor = 1.0f;
			float recoilYawGripFactor = 1.0f;
			float recoilPitchBarrelFactor = 1.0f;
			float recoilYawBarrelFactor = 1.0f;
			float recoilPitchStockFactor = 1.0f;
			float recoilYawStockFactor = 1.0f;
			float recoil = count;
			float recoilPitch1 = recoil;//
			float recoilPitch2 = recoil/4;//
			float recoilYaw1 = recoil/10;//
			float recoilYaw2 = recoil/20;//
			float offsetYaw = 0;
			float offsetPitch = 0;
			offsetPitch = recoilPitch1;
			offsetPitch += ((recoilPitch2 * 2) - recoilPitch2);
			offsetPitch *= (recoilPitchGripFactor * recoilPitchBarrelFactor * recoilPitchStockFactor);
			offsetYaw = recoilYaw1;
			offsetYaw *= new Random().nextFloat() * (recoilYaw2 * 2) - recoilYaw2;
			offsetYaw *= recoilYawGripFactor * recoilYawBarrelFactor * recoilYawStockFactor;
			offsetYaw *= RenderParameters.rate;
			offsetYaw *= RenderParameters.phase ? 1 : -1;
			RenderParameters.playerRecoilPitch += offsetPitch;
			if (Math.random() > 0.5f) {
				RenderParameters.playerRecoilYaw += offsetYaw;
			} else {
				RenderParameters.playerRecoilYaw -= offsetYaw;
			}
			RenderParameters.phase = !RenderParameters.phase;
		}
    }
	
    @Override
    public void onShootAnimation(PlayerEntity player, ItemStack itemstack) {
        /*if(player != null &&! itemstack.isEmpty() && itemstack.getItem() instanceof ItemGunBase){
			ItemGunBase gun = (ItemGunBase) itemstack.getItem();
            RenderParameters.rate = Math.min(RenderParameters.rate + 0.07f, 1f);
            float recoilPitchGripFactor = 1.0f;
            float recoilYawGripFactor = 1.0f;
            float recoilPitchBarrelFactor = 1.0f;
            float recoilYawBarrelFactor = 1.0f;
            float recoilPitchStockFactor = 1.0f;
            float recoilYawStockFactor = 1.0f;

			float recoilPitch1 = (float)gun.recoil;
			float recoilPitch2 = (float)gun.recoil/4;
			
			float recoilYaw1 = (float)gun.recoil_ads;
			float recoilYaw2 = (float)gun.recoil_ads/4;
			float extra_nbt_recoil = 0;
			NBTTagCompound nbt = itemstack.getTagCompound();
			if(itemstack.hasTagCompound()) {
				extra_nbt_recoil = nbt.getFloat("extra_recoil")+nbt.getFloat("extra_recoil2");
				if(nbt.getBoolean("am_grip")){
					double nbt_recoil = nbt.getDouble("recoil");
					double nbt_recoil_ads = nbt.getDouble("recoil_ads");
					recoilPitch1 = (float)nbt_recoil;
					recoilPitch2 = (float)nbt_recoil/4;
					
					recoilYaw1 = (float)nbt_recoil_ads;
					recoilYaw2 = (float)nbt_recoil_ads/4;
				}
			}
			boolean left_key = WarMachineLib.proxy.leftclick();
            float offsetYaw = 0;
            float offsetPitch = 0;
            {
                offsetYaw *= RenderParameters.phase ? 1 : -1;
                offsetPitch = recoilPitch1 * (1 + extra_nbt_recoil);
                offsetPitch += ((recoilPitch2 * 2) - recoilPitch2);
                offsetPitch *= (recoilPitchGripFactor * recoilPitchBarrelFactor * recoilPitchStockFactor);
				if(player.isSneaking())offsetPitch *= 0.8F;
                if(WarMachineLib.proxy.isZooming())offsetPitch *= 0.6F;

                offsetYaw = recoilYaw1 * (1 + extra_nbt_recoil);
                offsetYaw *= new Random().nextFloat() * (recoilYaw2 * 2) - recoilYaw2;
                offsetYaw *= recoilYawGripFactor * recoilYawBarrelFactor * recoilYawStockFactor;
                offsetYaw *= RenderParameters.rate;
				if(player.isSneaking())offsetYaw *= 0.8F;
                if(WarMachineLib.proxy.isZooming())offsetYaw *= 0.6F;
                offsetYaw *= RenderParameters.phase ? 1 : -1;
            }
			float cock_size = 1.05F;
            RenderParameters.playerRecoilPitch += offsetPitch * cock_size;
			if(RenderParameters.cockRecoil < 2F)RenderParameters.cockRecoil += 2F;
            if (Math.random() > 0.5f) {
                RenderParameters.playerRecoilYaw += offsetYaw;
            } else {
                RenderParameters.playerRecoilYaw -= offsetYaw;
            }
            RenderParameters.phase = !RenderParameters.phase;
			RenderParameters.bulletstart = true;
        }*/
    }
}

package wmlib.common.network.play;
import wmlib.common.network.message.MessageVehicleAnim;
import wmlib.common.network.message.MessageSoldierAnim;
import wmlib.common.network.message.MessageHit;
import wmlib.common.network.message.MessageHit;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.EntityWMSeat;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.ModList;
import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.mob.EntityMobSoldierBase;
/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class ClientPlayHandler
{
	public static void handleBulletMove(int id, double mx,double my,double mz)
    {
		World level = Minecraft.getInstance().level;
		if (level == null) return;
		Entity entity = level.getEntity(id);
		if (entity != null) {
			entity.lerpMotion(mx, my, mz);
		}
	}
    public static void handleHit(MessageHit message)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
			if(message.getAnimId() == 1){
				if(player.getId() == message.getEntityId())
				{
					CompoundNBT nbt = player.getPersistentData();
					nbt.putInt("hitentitydead", 200);
					nbt.putFloat("hitdamage", message.getCountId());
					nbt.putInt("hitentity_id", message.getNameId());
					player.playSound(SoundEvents.ANVIL_PLACE, 1.0F, 1.2F);
				}
			}
			if(message.getAnimId() == 2){
				if(player.getId() == message.getEntityId())
				{
					CompoundNBT nbt = player.getPersistentData();
					nbt.putInt("hitentity", 100);
					nbt.putFloat("hitdamage", message.getCountId());
					nbt.putInt("hitentity_id", message.getNameId());
					{
						player.playSound(SoundEvents.STONE_BUTTON_CLICK_ON, 1.0F, 1.2F);
					}
				}
			}
			if(message.getAnimId() == 3){
				if(player.getId() == message.getEntityId())
				{
					CompoundNBT nbt = player.getPersistentData();
					nbt.putInt("hitentity_headshot", 200);
					nbt.putFloat("hitdamage", message.getCountId());
					nbt.putInt("hitentity_id", message.getNameId());
					{
						player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.2F);
					}
				}
			}
		}
	}
    public static void handleVehicleFireAnim(MessageVehicleAnim message)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            Entity entity = player.getCommandSenderWorld().getEntity(message.getEntityId());
            if(entity instanceof EntityWMVehicleBase)
            {
                EntityWMVehicleBase vehicle = (EntityWMVehicleBase) entity;
                if(message.getAnimId()==1)vehicle.anim1=0;
				if(message.getAnimId()==2)vehicle.anim2=0;
				if(message.getAnimId()==3)vehicle.anim3=0;
                if(message.getAnimId()==4)vehicle.anim4=0;
				if(message.getAnimId()==5)vehicle.anim5=0;
				if(message.getAnimId()==6)vehicle.anim6=0;
                if(message.getAnimId()==7)vehicle.anim7=0;
				if(message.getAnimId()==8)vehicle.anim8=0;
				if(message.getAnimId()==9)vehicle.anim9=0;
				//if(message.getAnimId()==4)vehicle.anim4=0;
            }
        }
    }
    public static void handleSoldierFireAnim(MessageSoldierAnim message)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null && ModList.get().isLoaded("advancearmy"))
        {
            Entity entity = player.getCommandSenderWorld().getEntity(message.getEntityId());
			if(message.getAnimId()<3){
				if(entity instanceof EntitySA_SoldierBase){
					EntitySA_SoldierBase ent = (EntitySA_SoldierBase) entity;
					if(message.getAnimId()==1)ent.anim1=0;
					if(message.getAnimId()==2)ent.anim2=0;
				}
			}else{
				if(entity instanceof EntityMobSoldierBase){
					EntityMobSoldierBase mob = (EntityMobSoldierBase) entity;
					if(message.getAnimId()==3)mob.anim1=0;
					if(message.getAnimId()==4)mob.anim2=0;
				}
			}
        }
    }
}

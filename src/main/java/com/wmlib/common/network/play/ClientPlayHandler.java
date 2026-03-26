package wmlib.common.network.play;
import wmlib.common.network.message.MessageVehicleAnim;
import wmlib.common.network.message.MessageSoldierAnim;
import wmlib.common.network.message.MessageFirstTarget;
import wmlib.common.network.message.MessageBulletMove;
import wmlib.common.network.message.MessageHit;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.EntityWMSeat;
import wmlib.api.IAnimPacket;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.PathfinderMob;
import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.mob.EntityMobSoldierBase;

import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
@OnlyIn(Dist.CLIENT)
public class ClientPlayHandler
{
	public static void handleBulletMove(int id, double mx,double my,double mz)
    {
		Level level = Minecraft.getInstance().level;
		if (level == null) return;
		Entity entity = level.getEntity(id);
		if (entity != null) {
			entity.lerpMotion(mx, my, mz);
		}
	}
    public static void handleHit(MessageHit message)
    {
        Player player = Minecraft.getInstance().player;
        if(player != null)
        {
			if(message.getAnimId() == 1){
				if(player.getId() == message.getEntityId())
				{
					CompoundTag nbt = player.getPersistentData();
					nbt.putInt("hitentitydead", 200);
					nbt.putFloat("hitdamage", message.getCountId());
					nbt.putInt("hitentity_id", message.getNameId());
					player.playSound(SoundEvents.ANVIL_PLACE, 1.0F, 1.2F);
				}
			}
			if(message.getAnimId() == 2){
				if(player.getId() == message.getEntityId())
				{
					CompoundTag nbt = player.getPersistentData();
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
					CompoundTag nbt = player.getPersistentData();
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
        Player player = Minecraft.getInstance().player;
        if(player != null && message.getEntityId()!=-1)
        {
            Entity entity = player.getCommandSenderWorld().getEntity(message.getEntityId());
            if(entity instanceof EntityWMVehicleBase)
            {
                EntityWMVehicleBase vehicle = (EntityWMVehicleBase) entity;
                vehicle.setVehicleAnim(message.getAnimId());
            }
        }
    }
    public static void handleSoldierFireAnim(MessageSoldierAnim message)
    {
		/*if(ModList.get().isLoaded("advancearmy"))*/{
			Player player = Minecraft.getInstance().player;
			if(player != null && message.getEntityId()!=-1)
			{
				Entity entity = player.getCommandSenderWorld().getEntity(message.getEntityId());
				if(entity instanceof IAnimPacket){
					IAnimPacket ent = (IAnimPacket) entity;
					ent.setAnim(message.getAnimId());
				}
				/*if(message.getAnimId()<3){
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
				}*/
			}
		}
    }
	public static void handleFirstTarget(MessageFirstTarget message)
    {
        Player player = Minecraft.getInstance().player;
        if(player != null && message.getEntityId()!=-1)
        {
            Entity entity = player.getCommandSenderWorld().getEntity(message.getEntityId());
			if(entity instanceof EntityWMVehicleBase){
				EntityWMVehicleBase ent = (EntityWMVehicleBase) entity;
				if(message.getTargetId()==-1){
					ent.setTarget(null);
					ent.firstTarget=null;
					ent.clientTarget=null;
				}else{
					Entity target = player.getCommandSenderWorld().getEntity(message.getTargetId());
					if(target instanceof LivingEntity){
						ent.setTarget((LivingEntity)target);
						ent.firstTarget=(LivingEntity)target;
						ent.clientTarget=(LivingEntity)target;
					}
				}
			}
			if(entity instanceof EntitySA_SoldierBase){
				EntitySA_SoldierBase ent = (EntitySA_SoldierBase) entity;
				if(message.getTargetId()==-1){
					ent.setTarget(null);
					ent.firstTarget=null;
					ent.clientTarget=null;
				}else{
					Entity target = player.getCommandSenderWorld().getEntity(message.getTargetId());
					if(target instanceof LivingEntity){
						ent.setTarget((LivingEntity)target);
						ent.firstTarget=(LivingEntity)target;
						ent.clientTarget=(LivingEntity)target;
					}
				}
			}
        }
    }
}

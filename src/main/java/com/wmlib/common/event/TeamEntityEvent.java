package wmlib.common.event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import net.minecraft.entity.passive.TameableEntity;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.bullet.EntityBulletBase;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageHit;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import wmlib.WMConfig;
public class TeamEntityEvent {
    /*@SubscribeEvent
    public void canupdate(EntityEvent.CanUpdate event){
        if(event.getEntity() instanceof EntityBulletBase/* && !((EntityBulletBase) event.entity).chunkLoaderBullet*){
            event.getEntity().remove();
        }
        /*if(event.entity instanceof EntityWMVehicleBase && ((EntityWMVehicleBase) event.entity).canDespawn){
            event.entity.remove();
        }*
    }*/

	@SubscribeEvent
	public void onPlayerLoad(EntityJoinWorldEvent event) {
		if(ModList.get().isLoaded("advancearmy")){
			if (event.getWorld() instanceof ServerWorld) {
				ServerWorld serverWorld = (ServerWorld) event.getWorld();
				if(event.getEntity() instanceof PlayerEntity){
					PlayerEntity player = (PlayerEntity) event.getEntity();
					if (serverWorld.getGameTime()<1000 && player.getTeam()==null) {
						if(player.level.getScoreboard().getPlayerTeam("AdvanceArmy")!=null){
							player.sendMessage(new TranslationTextComponent("已加入默认队伍AdvanceArmy", new Object[0]), player.getUUID());
							player.level.getScoreboard().addPlayerToTeam(player.getGameProfile().getName(), player.level.getScoreboard().getPlayerTeam("AdvanceArmy"));
							player.sendMessage(new TranslationTextComponent("获取一个西瓜以开启先遣部队模组生存游玩！", new Object[0]), player.getUUID());
						}
						//player.inventory.addItemStackToInventory(new ItemStack(Items.DIAMOND));
					}
				}
			}
		}
	}

	@SubscribeEvent()
    public static void onLivingKnockBack(LivingKnockBackEvent event) {
		LivingEntity target = event.getEntityLiving();
        if (target instanceof PlayerEntity) {
            if(target.getVehicle()!=null && target.getVehicle() instanceof EntityWMSeat)event.setCanceled(true);
        }
    }

	@SubscribeEvent
	public void loadWorld(WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            Scoreboard scoreboard = serverWorld.getScoreboard();
			ITextComponent prefix = new StringTextComponent(TextFormatting.GREEN.toString());
			if (scoreboard.getPlayerTeam("AdvanceArmy") == null) {
				ScorePlayerTeam team = scoreboard.addPlayerTeam("AdvanceArmy");
				team.setSeeFriendlyInvisibles(true);
				team.setAllowFriendlyFire(false);
				team.setPlayerPrefix(prefix);
				team.setColor(TextFormatting.GREEN);
				//event.getWorld().getScoreboard().broadcastTeamInfoUpdate(team);
			}else if(scoreboard.getPlayerTeam("AdvanceArmy") != null){
				ScorePlayerTeam team = scoreboard.getPlayerTeam("AdvanceArmy");//
				team.setPlayerPrefix(prefix);
				team.setColor(TextFormatting.GREEN);
				//event.getWorld().getScoreboard().broadcastTeamInfoUpdate(team);
			}
			/*ITextComponent prefix2 = new StringTextComponent(TextFormatting.DARK_PURPLE.toString());
			if (scoreboard.getPlayerTeam("Aohuan") == null) {
				ScorePlayerTeam team = scoreboard.addPlayerTeam("Aohuan");
				team.setSeeFriendlyInvisibles(true);
				team.setAllowFriendlyFire(false);
				team.setPlayerPrefix(prefix2);
				team.setColor(TextFormatting.DARK_PURPLE);
				//event.getWorld().getScoreboard().broadcastTeamInfoUpdate(team);
			}else if(scoreboard.getPlayerTeam("Aohuan") != null){
				ScorePlayerTeam team = scoreboard.getPlayerTeam("Aohuan");//
				team.setPlayerPrefix(prefix2);
				team.setColor(TextFormatting.DARK_PURPLE);
				//event.getWorld().getScoreboard().broadcastTeamInfoUpdate(team);
			}*/
		}
	}
	
	@SubscribeEvent
	public void onHurtEvent(LivingHurtEvent event) {
		LivingEntity target = event.getEntityLiving();
		DamageSource source = event.getSource();
		float damage = event.getAmount();
		if (/*target instanceof PlayerEntity && */target != null) 
		{
			//PlayerEntity player = (PlayerEntity)target;
			Entity entity = source.getEntity();
			if (target.getVehicle() instanceof EntityWMSeat && target.getVehicle() != null) {
				EntityWMSeat seat = (EntityWMSeat)target.getVehicle();
				if(seat.seatProtect<=0){
					event.setAmount(0);
				}else{
					event.setAmount(damage*seat.seatProtect);
				}
			}
	    	if(entity != null && entity.getTeam() == target.getTeam() && target.getTeam() !=null){
	    		event.setAmount(0);
	            event.setCanceled(true);
	    	}
			if(entity != null && entity instanceof TameableEntity){
				TameableEntity soldier = (TameableEntity) entity;
				if(soldier.getOwner() == target){
					event.setAmount(0);
					event.setCanceled(true);
				}
				if(ModList.get().isLoaded("pvz")){
					if(target instanceof PVZPlantEntity){
						/*IPAZEntity plant = (IPAZEntity)target;
						if (plant.getOwnerUUID().isPresent() && soldier.getOwner()!=null) {
							if (plant.getOwnerUUID().get().equals(soldier.getOwner().getUUID())) {
								event.setAmount(0);
								event.setCanceled(true);
							}
						}*/
						event.setAmount(0);
						event.setCanceled(true);
					}
				}
			}
	    	if(WMConfig.COMMON.hit.hitInformation.get() && entity != null && entity instanceof PlayerEntity){
	    		PlayerEntity player = (PlayerEntity) entity;
	    		if(player != null){
	    			float hp = target.getHealth() - event.getAmount();
					float health = target.getMaxHealth()/2;
	    			if(hp <= 0F) {
	    				if(event.getAmount() > 0) {
							if(!player.level.isClientSide){
								//PacketHandler.getPlayChannel().sendToServer(
								PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> target), new MessageHit(player.getId(), 1, target.getId(), health));
							}
	    				}
	    			}else {
	    				if(event.getAmount() > 0) {
							if(!player.level.isClientSide){
								PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> target), new MessageHit(player.getId(), 2, target.getId(), damage));
							}
	    				}
	    			}
	    		}
	    	}
		}
	}
	
	@SubscribeEvent
	public void onDeadEvent(LivingDeathEvent event) {
		LivingEntity target = event.getEntityLiving();
		DamageSource source = event.getSource();
		if (WMConfig.COMMON.hit.hitInformation.get() && target instanceof LivingEntity && target != null) 
		{
			float health = target.getMaxHealth()/2;
			Entity entity = source.getEntity();
			if(entity != null && entity instanceof PlayerEntity){
	    		PlayerEntity player = (PlayerEntity) entity;
	    		if(player != null){
					if(!player.level.isClientSide){
						PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> target), new MessageHit(player.getId(), 1, target.getId(), health));
					}
	    		}
	    	}
		}
	}
	
	@SubscribeEvent
	public void onHeadShotEvent(LivingHurtEvent event) {//HEAD
		LivingEntity target = event.getEntityLiving();
		DamageSource source = event.getSource();
		float damage = event.getAmount() * 1.75F;
		if (target instanceof LivingEntity && target != null) 
		{
			Entity entity = source.getEntity();
	    	if(source.getDirectEntity() instanceof EntityBulletBase && entity != null)
	        {
	    		EntityBulletBase bullet = (EntityBulletBase) source.getDirectEntity();
	    		double target_eye = target.getEyeHeight() + target.getY();
	    		if(target.getBbHeight() >= 1.6F && target.getBbHeight() <= 2.0F && target.getBbWidth() >= 0.4F && target.getBbWidth() <= 0.7F) {
	    			if(bullet.getY() >= target_eye - 0.15F) {
	    				event.setAmount(event.getAmount() * 1.75F);
	    				if(WMConfig.COMMON.hit.hitInformation.get() && entity != null && entity instanceof PlayerEntity){
	    		    		PlayerEntity player = (PlayerEntity) entity;
	    		    		if(player != null){
								if(!player.level.isClientSide){
									PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> target), new MessageHit(player.getId(), 3, target.getId(), damage));
								}
	    		    		}
	    		    	}
	    			}
	    		}
	        }
		}
	}
	
	@SubscribeEvent
	public void onHurtEventDamage(LivingDamageEvent event) {
		LivingEntity target = event.getEntityLiving();
		DamageSource source = event.getSource();
		float damage = event.getAmount();
		if (/*target instanceof PlayerEntity && */target != null) 
		{
			//PlayerEntity player = (PlayerEntity)target;
			Entity entity = source.getEntity();//
			if (target.getVehicle() instanceof EntityWMSeat && target.getVehicle() != null) {
				EntityWMSeat seat = (EntityWMSeat)target.getVehicle();
				if(seat.seatProtect<=0){
					event.setAmount(0);
				}else{
					event.setAmount(damage*seat.seatProtect);
				}
			}
	    	if(entity != null && entity.getTeam() == target.getTeam() && target.getTeam() !=null){
	    		event.setAmount(0);
	            event.setCanceled(true);
	    	}
			if(entity != null && entity instanceof TameableEntity){
				TameableEntity soldier = (TameableEntity) entity;
				if(soldier.getOwner() == target){
					event.setAmount(0);
					event.setCanceled(true);
				}
				if(ModList.get().isLoaded("pvz")){
					if(target instanceof PVZPlantEntity){
						/*IPAZEntity plant = (IPAZEntity)target;
						if (plant.getOwnerUUID().isPresent() && soldier.getOwner()!=null) {
							if (plant.getOwnerUUID().get().equals(soldier.getOwner().getUUID())) {
								event.setAmount(0);
								event.setCanceled(true);
							}
						}*/
						event.setAmount(0);
						event.setCanceled(true);
					}
				}
			}
		}
	}
}

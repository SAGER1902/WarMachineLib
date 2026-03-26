package wmlib.common.event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.level.block.state.BlockState;          
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;                 // Player → Player
import net.minecraftforge.fml.ModList;
// 物品和交互
import net.minecraft.world.item.Item;                            // 路径更新
import net.minecraft.world.item.ItemStack;                       // 路径更新
import net.minecraft.world.item.context.UseOnContext;            // ItemUseContext → UseOnContext
import net.minecraft.world.InteractionResult;                    // 路径更新
import net.minecraft.world.InteractionHand;                      // Hand → InteractionHand

// NBT 和统计
import net.minecraft.nbt.CompoundTag;                            // CompoundNBT → CompoundTag
import net.minecraft.stats.Stats;                                // 路径更新

// 方块实体（TileEntity → BlockEntity）
import net.minecraft.world.level.block.entity.SpawnerBlockEntity; // MobSpawnerTileEntity → SpawnerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity;        // TileEntity → BlockEntity

// 文本和本地化
import net.minecraft.network.chat.Component;                     // ITextComponent → Component
import net.minecraft.ChatFormatting;                             // ChatFormatting → ChatFormatting
import net.minecraft.network.chat.MutableComponent;              // 替代 TranslationTextComponent（通过 Component.translatable()）

// 工具提示和附魔
import net.minecraft.world.item.TooltipFlag;                     // ITooltipFlag → TooltipFlag
import net.minecraft.world.entity.EquipmentSlot;                 // EquipmentSlotType → EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment;         // 路径更新

import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.level.Level;                          // World → Level
import net.minecraft.world.entity.LivingEntity;                  // 路径更新
import net.minecraft.world.item.Items;                           // 路径更新
import net.minecraft.world.InteractionResultHolder;
import wmlib.common.enchantment.EnchantmentTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.network.PacketDistributor;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.bullet.EntityBulletBase;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageHit;
import wmlib.WMConfig;
public class TeamEntityEvent {
	boolean show = false;
	@SubscribeEvent
	public void onPlayerLoad(EntityJoinLevelEvent event) {
		{
			if (!this.show && event.getLevel() instanceof ServerLevel) {
				ServerLevel serverWorld = (ServerLevel) event.getLevel();
				if(event.getEntity() instanceof Player){
					{
						Player player = (Player) event.getEntity();
						player.sendSystemMessage(Component.translatable("-----------------[WarMacineLib]-------------------"));
						player.sendSystemMessage(Component.translatable("§a战争机器库:配置文件为config/wmlib-common.toml,可在游戏内mod设置更改"));
						if(WMConfig.bulletDestroy){
							player.sendSystemMessage(Component.translatable("§c战争机器库配置选项:子弹直击破坏方块已经开启!!!"));
						}else{
							player.sendSystemMessage(Component.translatable("§b战争机器库配置选项:子弹直击破坏方块已经关闭"));
						}
						if(WMConfig.explosionDestroy){
							player.sendSystemMessage(Component.translatable("§c战争机器库配置选项:爆炸破坏方块已经开启!!!"));
						}else{
							player.sendSystemMessage(Component.translatable("§b战争机器库配置选项:爆炸破坏方块已经关闭"));
						}
						if(WMConfig.useCustomShaders){
							player.sendSystemMessage(Component.translatable("§c战争机器库配置选项:自定义着色器已经开启!!!"));
						}else{
							player.sendSystemMessage(Component.translatable("§b战争机器库配置选项:自定义着色器已经关闭"));
						}
						player.sendSystemMessage(Component.translatable("---------------------------------------"));
						this.show=true;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void loadWorld(LevelEvent.Load event) {
		if (event.getLevel() instanceof ServerLevel) {
			ServerLevel serverLevel = (ServerLevel) event.getLevel();
			Scoreboard scoreboard = serverLevel.getScoreboard();
			
			MutableComponent prefix = Component.literal("").withStyle(ChatFormatting.GREEN);
			
			PlayerTeam team = scoreboard.getPlayerTeam("AdvanceArmy");
			if (team == null) {
				team = scoreboard.addPlayerTeam("AdvanceArmy");
				team.setSeeFriendlyInvisibles(true);
				team.setAllowFriendlyFire(false);
				team.setPlayerPrefix(prefix);
				team.setColor(ChatFormatting.GREEN);
			} else {
				team.setPlayerPrefix(prefix);
				team.setColor(ChatFormatting.GREEN);
			}
			/*ITextComponent prefix2 = new StringTextComponent(TextFormatting.DARK_PURPLE.toString());
			if (scoreboard.getPlayerTeam("Aohuan") == null) {
				PlayerTeam team = scoreboard.addPlayerTeam("Aohuan");
				team.setSeeFriendlyInvisibles(true);
				team.setAllowFriendlyFire(false);
				team.setPlayerPrefix(prefix2);
				team.setColor(TextFormatting.DARK_PURPLE);
				//event.getLevel().getScoreboard().broadcastTeamInfoUpdate(team);
			}else if(scoreboard.getPlayerTeam("Aohuan") != null){
				PlayerTeam team = scoreboard.getPlayerTeam("Aohuan");//
				team.setPlayerPrefix(prefix2);
				team.setColor(TextFormatting.DARK_PURPLE);
				//event.getWorld().getScoreboard().broadcastTeamInfoUpdate(team);
			}*/
		}
	}
	
	@SubscribeEvent
	public void onHurtEvent(LivingHurtEvent event) {
		LivingEntity target = event.getEntity();
		DamageSource source = event.getSource();
		float damage = event.getAmount();
		if (target != null) 
		{
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
			if(entity != null && entity instanceof TamableAnimal){
				TamableAnimal soldier = (TamableAnimal) entity;
				if(soldier.getOwner() == target){
					event.setAmount(0);
					event.setCanceled(true);
				}
			}
	    	if(WMConfig.hitInformation && entity != null && entity instanceof Player){
	    		Player player = (Player) entity;
	    		if(player != null){
	    			float hp = target.getHealth() - event.getAmount();
					float health = target.getMaxHealth()/2;
	    			if(hp <= 0F) {
	    				if(event.getAmount() > 0) {
							if(!player.level().isClientSide){
								PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> target), new MessageHit(player.getId(), 1, target.getId(), health));
							}
	    				}
	    			}else {
	    				if(event.getAmount() > 0) {
							if(!player.level().isClientSide){
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
		LivingEntity target = event.getEntity();
		DamageSource source = event.getSource();
		if (WMConfig.hitInformation && target instanceof LivingEntity && target != null) 
		{
			float health = target.getMaxHealth()/2;
			Entity entity = source.getEntity();
			if(entity != null && entity instanceof Player){
	    		Player player = (Player) entity;
	    		if(player != null){
					if(!player.level().isClientSide){
						PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> target), new MessageHit(player.getId(), 1, target.getId(), health));
					}
	    		}
	    	}
		}
	}
	
	@SubscribeEvent
	public void onHeadShotEvent(LivingHurtEvent event) {//HEAD
		LivingEntity target = event.getEntity();
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
	    				if(WMConfig.hitInformation && entity != null && entity instanceof Player){
	    		    		Player player = (Player) entity;
	    		    		if(player != null){
								if(!player.level().isClientSide){
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
		LivingEntity target = event.getEntity();
		DamageSource source = event.getSource();
		float damage = event.getAmount();
		if (/*target instanceof Player && */target != null) 
		{
			//Player player = (Player)target;
			Entity entity = source.getEntity();//
			if (target.getVehicle() instanceof EntityWMSeat && target.getVehicle() != null) {
				EntityWMSeat seat = (EntityWMSeat)target.getVehicle();
				if(seat.seatProtect<=0){
					event.setAmount(0);
				}else{
					if(source.is(DamageTypes.IN_WALL)){
						event.setAmount(0);
					}else{
						event.setAmount(damage*seat.seatProtect);
					}
				}
			}
	    	if(entity != null && entity.getTeam() == target.getTeam() && target.getTeam() !=null){
	    		event.setAmount(0);
	            event.setCanceled(true);
	    	}
			if(entity != null && entity instanceof TamableAnimal){
				TamableAnimal soldier = (TamableAnimal) entity;
				if(soldier.getOwner() == target){
					event.setAmount(0);
					event.setCanceled(true);
				}
				/*if(ModList.get().isLoaded("pvz")){
					if(target instanceof PVZPlantEntity){
						/*IPAZEntity plant = (IPAZEntity)target;
						if (plant.getOwnerUUID().isPresent() && soldier.getOwner()!=null) {
							if (plant.getOwnerUUID().get().equals(soldier.getOwner().getUUID())) {
								event.setAmount(0);
								event.setCanceled(true);
							}
						}*
						event.setAmount(0);
						event.setCanceled(true);
					}
				}*/
			}
		}
	}
}

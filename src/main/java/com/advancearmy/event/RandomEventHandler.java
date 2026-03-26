package advancearmy.event;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.network.chat.Component;
import advancearmy.entity.map.RandomPoint;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import wmlib.api.IArmy;
import advancearmy.AAConfig;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import advancearmy.entity.building.SandBag;
import advancearmy.entity.soldier.EntitySA_OFG;
//@Mod.EventBusSubscriber
public class RandomEventHandler {
    /*@SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof SandBag){
			 event.setCanceled(true);
		}
        if (event.getEntity() instanceof IArmy && event.getEntity() instanceof TamableAnimal tamableAnimal) {
            // 2. 获取主人
            LivingEntity owner = tamableAnimal.getOwner();
            // 3. 确保主人存在并且是玩家
            if (owner instanceof Player playerOwner) {
                // 4. 取消原版事件，阻止原版死亡消息的发送
                //    注意：取消事件可能会影响其他模组的行为，但在这里是必要的，以避免消息重复。
                event.setCanceled(true);
                // 5. 构建并发送自定义死亡消息
                //    你可以根据喜好自由定制消息内容和格式
                String customMessage = String.format("§e[系统] §6你的伙伴 %s 在战斗中倒下了...", tamableAnimal.getDisplayName().getString());
                // 如果希望消息包含击杀者信息，可以从 event.getSource() 中获取
                // 例如: event.getSource().getEntity() 可能返回击杀者
                if (event.getSource().getEntity() != null) {
                    String killerName = event.getSource().getEntity().getDisplayName().getString();
                    customMessage = String.format("§c[警戒] §6你的 %s 被 %s 击败了！", tamableAnimal.getDisplayName().getString(), killerName);
                }
                // 发送消息给玩家主人
                playerOwner.sendSystemMessage(Component.literal(customMessage));
                // 可选：你也可以发送一条消息到全局聊天，或者执行其他逻辑
                // 例如，给主人一些经验补偿，或者播放一个悲伤的音效
            }
        }
    }*/
	
    private static final int MIN_Y = 40; // 最低生成Y坐标（低于此值视为地下）
    private static final int MAX_Y = 180; // 最高生成Y坐标（高于此值视为高空）
    private static final int UNDERGROUND_CHECK_RADIUS = 5; // 检查周围方块来判断是否在地下
    private static final int UNDERGROUND_BLOCK_THRESHOLD = 10; // 周围非空气方块数量阈值
    //int time=0;
    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        // 只在服务器端执行，且只在主世界
        if (event.phase == TickEvent.Phase.START && 
            event.level instanceof ServerLevel serverLevel &&
            serverLevel.dimension() == net.minecraft.world.level.Level.OVERWORLD){
			//++time;
            long worldTime = serverLevel.getDayTime();
            {
                long currentDay = worldTime / 24000L;
                float spawnChance = calculateSpawnChance(currentDay,false);
                for (ServerPlayer player : serverLevel.players()) {
					/*if(time%20==0){
						player.sendSystemMessage(net.minecraft.network.chat.Component.literal("时间="+worldTime));
						player.sendSystemMessage(net.minecraft.network.chat.Component.literal("time="+time));
						player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§7呵哎！！！！"));
					}*/
                    /*if (isValidLocationForInvasion(serverLevel, player)) */
					if (currentDay>1  && AAConfig.cycleMobEvent>0 && currentDay % AAConfig.cycleMobEvent == 0 && worldTime % 24000 == 0){
                        if (serverLevel.random.nextFloat() < spawnChance * AAConfig.mobEventChance/100F){
                            spawnInvasionSummoner(serverLevel, player, currentDay, false);
							break;
                        }
                    }/* else {
                        // player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§7当前位置不适合入侵生成"));
                    }*/
					
					if (currentDay>1  && AAConfig.cycleFriendEvent>0 && currentDay % AAConfig.cycleFriendEvent == 0 && worldTime % 12000 == 0){
                        if (serverLevel.random.nextFloat() < spawnChance * AAConfig.friendEventChance/100F){
                            spawnInvasionSummoner(serverLevel, player, currentDay, true);
							break;
                        }
                    }
                }
            }
        }
    }
    
    // 计算基于天数的生成概率
    private static float calculateSpawnChance(long currentDay, boolean isFriend) {
		int day = 5;
		if(isFriend){
			day=AAConfig.cycleFriendEvent;
		}else{
			day=AAConfig.cycleMobEvent;
		}
		
        if (currentDay <= 0) {
            return 0.1f;
        } else if (currentDay >= day) {
            return 1.0f;
        } else {
            // 线性插值：从0.1到1.0
            float progress = (float) currentDay / (float) day;
            return 0.1f + (0.9f * progress);
        }
    }
    
    // 判断玩家是否在地下（被大量方块包围）
    private static boolean isUnderground(ServerLevel level, BlockPos pos) {
        int solidBlockCount = 0;
        // 检查玩家周围一定范围内的方块
        for (int x = -UNDERGROUND_CHECK_RADIUS; x <= UNDERGROUND_CHECK_RADIUS; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -UNDERGROUND_CHECK_RADIUS; z <= UNDERGROUND_CHECK_RADIUS; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    // 如果不是空气方块，则计数
                    if (!level.isEmptyBlock(checkPos)) {
                        solidBlockCount++;
                    }
                    // 如果非空气方块数量超过阈值，认为是地下
                    if (solidBlockCount >= UNDERGROUND_BLOCK_THRESHOLD) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    // 判断玩家是否在高空（远离地面）
    private static boolean isHighAboveGround(ServerLevel level, BlockPos pos) {
        // 获取当前位置的地面高度
        BlockPos groundPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
        int groundY = groundPos.getY();
        // 计算玩家离地面的高度差
        int heightAboveGround = pos.getY() - groundY;
        // 如果高度差超过30格，认为是高空
        return heightAboveGround > 30;
    }
    
    // 检查是否有足够的开放空间（避免在封闭的小空间）
    private static boolean hasEnoughOpenSpace(ServerLevel level, BlockPos centerPos) {
        // 检查以玩家为中心的5x5x3区域
        int openSpaceCount = 0;
        int totalSpaces = 0;
        for (int x = -2; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos checkPos = centerPos.offset(x, y, z);
                    totalSpaces++;
                    
                    if (level.isEmptyBlock(checkPos)) {
                        openSpaceCount++;
                    }
                }
            }
        }
        // 如果开放空间比例低于60%，认为空间不足
        float openSpaceRatio = (float) openSpaceCount / totalSpaces;
        return openSpaceRatio >= 0.6f;
    }
    
    // 生成入侵召唤物
    private static void spawnInvasionSummoner(ServerLevel level, ServerPlayer player, long currentDay, boolean isFriend) {
        /*try */{
            // 发送入侵警告消息
            /*player.sendSystemMessage(
                net.minecraft.network.chat.Component.literal(
                    String.format("§c第%d天 - 入侵来袭！", currentDay)
                )
            );*/
            // 寻找生成位置（确保也在合适的位置）
            BlockPos spawnPos = findSuitableSpawnPosition(level, player);
            if (spawnPos == null) {
                // 找不到合适位置，取消生成
                /*player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal("§7入侵被取消 - 周围环境不适合")
                );*/
                return;
            }
            RandomPoint summoner = new RandomPoint(ModEntities.ENTITY_INV.get(),level);
            summoner.setPos(
                spawnPos.getX() + 0.5,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5
            );
			{
				if(isFriend){
					summoner.setSummonID(11+level.random.nextInt(5));
				}else{
					summoner.setSummonID(level.random.nextInt(7));
				}
				int count = 0;
				List<Entity> entities = level.getEntities(summoner, summoner.getBoundingBox().inflate(50D, 50D, 50D));
				for (Entity target : entities) {
					if(target!=null && (target instanceof Player||target instanceof IArmy)){
						++count;
						if(count>20)break;
					}
				}
				summoner.setExtraC(count);
				summoner.tame(player);
			}
            if (level.addFreshEntity(summoner)){
				if(isFriend){
					level.playSound(
						null, 
						player.blockPosition(), 
						net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP, 
						net.minecraft.sounds.SoundSource.HOSTILE, 
						5f, 
						1.0f + level.random.nextFloat() * 0.2f
					);
				}else{
					level.playSound(
						null, 
						player.blockPosition(), 
						net.minecraft.sounds.SoundEvents.LIGHTNING_BOLT_THUNDER, 
						net.minecraft.sounds.SoundSource.HOSTILE, 
						5f, 
						1.0f + level.random.nextFloat() * 0.2f
					);
				}
                /*player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal("§e入侵召唤物已在附近生成！")
                );*/
            }
        }/* catch (Exception e) {
            System.err.println("生成事件召唤物时出错: " + e.getMessage());
            e.printStackTrace();
        }*/
    }
    
    // 寻找合适的生成位置
    private static BlockPos findSuitableSpawnPosition(ServerLevel level, ServerPlayer player) {
        BlockPos playerPos = player.blockPosition();
        boolean isFlatWorld = level.getChunkSource().getGenerator() instanceof FlatLevelSource;
        // 尝试多个位置
        for (int attempt = 0; attempt < 20; attempt++) {
            // 随机角度和距离（8-15格）
            double angle = level.random.nextDouble() * Math.PI * 2;
            double distance = 8 + level.random.nextDouble() * 7;
            int x = (int)(playerPos.getX() + Math.cos(angle) * distance);
            int z = (int)(playerPos.getZ() + Math.sin(angle) * distance);
            // 获取该位置的地表高度
            BlockPos surfacePos = level.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
                new BlockPos(x, 0, z)
            );
            // 检查位置是否适合生成
            if (isValidSpawnPosition(level, surfacePos)||isFlatWorld) {
                return surfacePos.above();
            }
        }
        // 如果找不到合适位置，返回null
        return null;
    }
    
    // 检查位置是否适合生成召唤物
    private static boolean isValidSpawnPosition(ServerLevel level, BlockPos pos) {
        // 检查Y坐标范围
        if (pos.getY() < MIN_Y || pos.getY() > MAX_Y) {
            return false;
        }
        // 检查位置是否有足够空间
        if (!level.isEmptyBlock(pos.above()) || !level.isEmptyBlock(pos.above(2))) {
            return false;
        }
        // 检查是否在液体中
        if (level.getBlockState(pos).liquid() || level.getBlockState(pos.above()).liquid()) {
            return false;
        }
        // 检查下方方块是否坚固
        if (level.getBlockState(pos.below()).isAir()) {
            return false;
        }
        // 检查是否远离玩家（避免生成在玩家脸上）
        // 这个检查在findSuitableSpawnPosition中通过距离参数已经实现
        return true;
    }
	
	boolean show = false;
	@SubscribeEvent
	public void onPlayerLoad(EntityJoinLevelEvent event) {
		{
			if (!this.show && event.getLevel() instanceof ServerLevel) {
				ServerLevel serverWorld = (ServerLevel) event.getLevel();
				if(event.getEntity() instanceof Player){
					Player player = (Player) event.getEntity();
					if (serverWorld.getGameTime()<1000 && player.getTeam()==null) {
						if(player.level().getScoreboard().getPlayerTeam("AdvanceArmy")!=null){
							player.sendSystemMessage(Component.translatable("已加入默认队伍AdvanceArmy"));
							player.level().getScoreboard().addPlayerToTeam(player.getGameProfile().getName(), player.level().getScoreboard().getPlayerTeam("AdvanceArmy"));
							player.sendSystemMessage(Component.translatable("获取一个西瓜以开启先遣部队模组生存游玩！"));
						}
						//player.inventory.addItemStackToInventory(new ItemStack(Items.DIAMOND));
					}
					player.sendSystemMessage(Component.translatable("=================[AdvanceArmy]==================="));
					player.sendSystemMessage(Component.translatable("§a先遣部队:配置文件为config/advancearmy-common.toml,可在游戏内mod设置更改"));
					if(AAConfig.vehicleDestroy){
						player.sendSystemMessage(Component.translatable("§c先遣部队:载具移动破坏方块已经开启!!!"));
					}else{
						player.sendSystemMessage(Component.translatable("§b先遣部队:载具移动破坏方块已经关闭"));
					}
					
					if(AAConfig.spawnMob){
						player.sendSystemMessage(Component.translatable("§c先遣部队:生成怪物已经开启!!!"));
					}else{
						player.sendSystemMessage(Component.translatable("§b先遣部队:生成怪物已经关闭"));
					}
					
					if(AAConfig.eroRaid){
						player.sendSystemMessage(Component.translatable("§c先遣部队:侵蚀怪物加入灾厄袭击事件已经开启!!!"));
					}else{
						player.sendSystemMessage(Component.translatable("§b先遣部队:自定义着色器已经关闭"));
					}
					player.sendSystemMessage(Component.translatable("======================================"));
					show=true;
				}
			}
		}
	}
    // 检查玩家位置是否适合生成入侵
    /*private static boolean isValidLocationForInvasion(ServerLevel level, ServerPlayer player) {
        BlockPos playerPos = player.blockPosition();
        int playerY = playerPos.getY();
        // 检查1：Y坐标范围
        if (playerY < MIN_Y) {
            // 玩家在地下深处
            return false;
        }
        if (playerY > MAX_Y) {
            // 玩家在高空
            return false;
        }
        // 检查2：是否在地下（被大量方块包围）
        if (isUnderground(level, playerPos)) {
            return false;
        }
        // 检查3：是否在高空建筑或平台上（远离地面）
        if (isHighAboveGround(level, playerPos)) {
            return false;
        }
        // 检查4：是否有足够的开放空间（避免在封闭空间）
        if (!hasEnoughOpenSpace(level, playerPos)) {
            return false;
        }
        // 所有检查通过
        return true;
    }*/
    
}
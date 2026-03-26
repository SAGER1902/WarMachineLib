package wmlib.rts;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkHooks;
import wmlib.init.WMModEntities;
import wmlib.rts.RtsMoShiMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 处理F9按键按下事件的类，用于实现RTS模式的相机控制和菜单打开功能
 */
public class F9AnXiaAnJianShiProcedure {

    // 存储玩家UUID与其对应相机实体的映射
    public static final Map<UUID, Entity> playerCameras = new HashMap<>();

    /**
     * 执行F9按键按下时的逻辑
     * @param world 游戏世界
     * @param x 触发位置的x坐标
     * @param y 触发位置的y坐标
     * @param z 触发位置的z坐标
     * @param entity 触发事件的实体(玩家)
     */
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        // 检查实体是否为有效的服务器玩家
        if (entity == null || !(entity instanceof ServerPlayer serverPlayer))
            return;
            
        // 在服务器端处理相机逻辑
        if (world instanceof ServerLevel _level) {
            UUID playerId = serverPlayer.getUUID();
            
            // 检查玩家是否已有相机实体
            Entity existingCamera = playerCameras.get(playerId);
            if (existingCamera != null && existingCamera.isAlive()) {
                // 如果已有存活相机，则将玩家视角切换到该相机
                serverPlayer.connection.send(new ClientboundSetCameraPacket(existingCamera));
            } else {
                // 如果没有相机，则创建一个新的相机实体
                Entity xiangji = WMModEntities.XIANG_JI.get().spawn(_level,
                    BlockPos.containing(x + 0, y + 10, z + 0), 
                    MobSpawnType.MOB_SUMMONED);
                    
                if (xiangji != null) {
                    // 存储相机实体并设置初始视角角度
                    playerCameras.put(playerId, xiangji);
                    xiangji.setYRot(315.0F);  // 设置偏航角(水平旋转)
                    xiangji.setXRot(45.0F);   // 设置俯仰角(垂直旋转)
                    serverPlayer.connection.send(new ClientboundSetCameraPacket(xiangji));
                }
            }
        }

        // 为玩家打开RTS模式菜单
        if (entity instanceof ServerPlayer _ent) {
            BlockPos _bpos = BlockPos.containing(x, y, z);
            NetworkHooks.openScreen(_ent, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.literal("RtsMoShi");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                    return new RtsMoShiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
                }
            }, _bpos);
        }
    }

    /**
     * 获取玩家对应的相机实体
     * @param playerId 玩家UUID
     * @return 相机实体，如果没有则返回null
     */
    public static Entity getPlayerCamera(UUID playerId) {
        return playerCameras.get(playerId);
    }

    /**
     * 移除并销毁玩家对应的相机实体
     * @param playerId 玩家UUID
     */
    public static void removePlayerCamera(UUID playerId) {
        Entity camera = playerCameras.remove(playerId);
        if (camera != null) {
            camera.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    /**
     * 清理所有相机实体
     */
    public static void cleanupAllCameras() {
        playerCameras.values().forEach(camera -> {
            if (camera != null && camera.isAlive()) {
                camera.remove(Entity.RemovalReason.DISCARDED);
            }
        });
        playerCameras.clear();
    }
}

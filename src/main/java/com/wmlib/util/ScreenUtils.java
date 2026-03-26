package wmlib.util;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import wmlib.rts.XiangJiEntity;
import org.joml.Vector3d;

public class ScreenUtils {
    private static final double GRID_SIZE = 16.0;

    public static class RaycastResult {
        public final Vec3 hitPosition;
        public final BlockPos blockPos;
        public final Entity hitEntity;

        public RaycastResult(Vec3 hitPosition, BlockPos blockPos, Entity hitEntity) {
            this.hitPosition = hitPosition;
            this.blockPos = blockPos;
            this.hitEntity = hitEntity;
        }
    }
    
    public static RaycastResult screenToWorld(Minecraft mc, double mouseX, double mouseY) {
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        
        // 获取相机朝向
        float pitch = camera.getXRot();
        float yaw = camera.getYRot();
        
        // 获取屏幕尺寸并计算宽高比
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        float aspectRatio = (float)screenWidth / screenHeight;
        
        // 计算标准化设备坐标 (NDC)
        float fov = 70.0f; // 视场角
        float tanHalfFov = (float)Math.tan(Math.toRadians(fov * 0.5f));
        float ndcX = (((float)mouseX / screenWidth) * 2.0f - 1.0f) * aspectRatio * tanHalfFov;
        float ndcY = -(1.0f - ((float)mouseY / screenHeight) * 2.0f) * tanHalfFov;
        
        // 计算视角矩阵
        float f = Mth.cos(-yaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float f1 = Mth.sin(-yaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float f2 = -Mth.cos(-pitch * ((float)Math.PI / 180F));
        float f3 = Mth.sin(-pitch * ((float)Math.PI / 180F));
        
        // 创建基础向量
        Vector3d forward = new Vector3d(f1 * f2, f3, f * f2);
        Vector3d right = new Vector3d(f, 0, -f1);
        Vector3d up = forward.cross(right, new Vector3d());
        
        // 使用NDC坐标计算射线方向
        Vector3d rayDir = new Vector3d(forward.x, forward.y, forward.z)
            .add(right.mul(ndcX))
            .add(up.mul(ndcY))
            .normalize();
        
        // 射线检测
        double maxDistance = 1000.0;
        Vector3d currentPos = new Vector3d(cameraPos.x, cameraPos.y, cameraPos.z);
        Vector3d step = new Vector3d(rayDir.x, rayDir.y, rayDir.z).mul(0.5);
        
        // 进行射线检测
        for (double distance = 0; distance < maxDistance; distance += 0.5) {
            BlockPos blockPos = new BlockPos(
                (int)Math.floor(currentPos.x),
                (int)Math.floor(currentPos.y),
                (int)Math.floor(currentPos.z)
            );
            
            // 检查是否击中实体
            if (mc.level != null) {
                AABB searchBox = new AABB(
                    currentPos.x - 0.5, currentPos.y - 0.5, currentPos.z - 0.5,
                    currentPos.x + 0.5, currentPos.y + 0.5, currentPos.z + 0.5
                );
                
                for (Entity entity : mc.level.getEntities(null, searchBox)) {
                    if (entity instanceof LivingEntity && !(entity instanceof XiangJiEntity)) {
                        return new RaycastResult(
                            new Vec3(currentPos.x, currentPos.y, currentPos.z),
                            null,
                            entity
                        );
                    }
                }
                
                // 检查方块
                if (!mc.level.getBlockState(blockPos).isAir()) {
                    return new RaycastResult(
                        new Vec3(currentPos.x, currentPos.y, currentPos.z),
                        blockPos,
                        null
                    );
                }
            }
            
            // 向前移动
            currentPos.add(step);
        }
        
        // 如果没有击中任何东西，返回网格位置
        double gridY = Math.floor(currentPos.y / GRID_SIZE) * GRID_SIZE;
        Vector3d hitPos = new Vector3d(
            Math.floor(currentPos.x / GRID_SIZE) * GRID_SIZE,
            gridY,
            Math.floor(currentPos.z / GRID_SIZE) * GRID_SIZE
        );
        
        return new RaycastResult(
            new Vec3(hitPos.x, hitPos.y, hitPos.z),
            null,
            null
        );
    }
    
    public static BlockPos getGridPos(Vec3 worldPos) {
        // 将世界坐标转换为网格坐标
        int gridX = (int) ((int) Math.floor(worldPos.x / GRID_SIZE) * GRID_SIZE);
        int gridZ = (int) ((int) Math.floor(worldPos.z / GRID_SIZE) * GRID_SIZE);
        int gridY = (int) worldPos.y;
        
        return new BlockPos(gridX, gridY, gridZ);
    }
} 
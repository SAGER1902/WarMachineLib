package wmlib.rts;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wmlib.WarMachineLib;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@Mod.EventBusSubscriber(modid = WarMachineLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class WorldRenderHandler {
    public static BlockPos highlightedBlock = null;
    private static final java.util.Set<Entity> highlightedEntities = new java.util.HashSet<>();
    private static Entity selectedEntity = null;
    private static Entity cameraEntity = null;
    public static void setHighlightedBlock(BlockPos pos) {
        highlightedBlock = pos;
    }

    public static void setHighlightedEntity(Entity entity) {
        if (entity != null) {
            highlightedEntities.add(entity);
            highlightedBlock = null;
        }
    }

    public static void clearHighlight() {
        highlightedBlock = null;
        highlightedEntities.clear();
    }

    public static Entity getCameraEntity() {
        return cameraEntity;
    }

    public static void setSelectedEntity(Entity entity) {
        selectedEntity = entity;
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        if (highlightedBlock != null) {
            renderBlockHighlight(poseStack, bufferSource, camera);
        }

        // 渲染所有高亮实体
        for (Entity entity : highlightedEntities) {
            if (entity != null && entity.isAlive()) {
                AABB boundingBox = entity.getBoundingBox();
                
                poseStack.pushPose();
                poseStack.translate(-camera.x, -camera.y, -camera.z);

                float r = entity == selectedEntity ? 0.0F : 1.0F;
                float b = entity == selectedEntity ? 1.0F : 0.0F;
                
                LevelRenderer.renderLineBox(
                    poseStack,
                    bufferSource.getBuffer(RenderType.LINES),
                    boundingBox,
                    r, 0.0F, b, 1.0F
                );

                poseStack.popPose();
            }
        }
        
        if (!highlightedEntities.isEmpty()) {
            bufferSource.endBatch();
        }
    }

    private static void renderBlockHighlight(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec3 camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        BlockState state = level.getBlockState(highlightedBlock);
        if (state.isAir()) return;  // 如果是空气方块，直接返回

        // 获取方块形状
        VoxelShape shape = state.getShape(level, highlightedBlock);
        if (shape.isEmpty()) return;  // 如果形状为空，直接返回

        try {
            // 获取方块边界
            AABB bounds = shape.bounds();
            if (bounds == null) return;  // 额外的空值检查

            poseStack.pushPose();
            poseStack.translate(
                highlightedBlock.getX() - camera.x,
                highlightedBlock.getY() - camera.y,
                highlightedBlock.getZ() - camera.z
            );

            // 渲染边框
            LevelRenderer.renderLineBox(
                poseStack,
                bufferSource.getBuffer(RenderType.lines()),
                bounds,
                1.0F, 1.0F, 0.0F, 1.0F  // 黄色
            );

            poseStack.popPose();
            bufferSource.endBatch();
        } catch (Exception e) {
            // 记录错误但不中断渲染
            System.err.println("渲染方块高亮时发生错误: " + e.getMessage());
        }
    }
} 
package advancearmy.render;
/*import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import wmlib.client.obj.SAObjModel;
import net.minecraft.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderType;
import wmlib.client.render.SARenderState;
import wmlib.client.render.RenderTypeVehicle;*/
//@OnlyIn(Dist.CLIENT) // 仅在客户端使用
public class CustomItemStackRenderer/* extends BlockEntityWithoutLevelRenderer */{

	/*public ResourceLocation tex = ResourceLocation.tryParse("advancearmy:textures/mob/stapc.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/stapc.obj");
	RenderType rt = RenderTypeVehicle.objrender(tex);
	RenderType hide = SARenderState.getBlendDepthWrite(tex);//getBlendDepthWrite_NoLight
	RenderType light = SARenderState.getBlendDepthWrite(tex);
	
    // 通常我们会使用一个单例，避免重复创建
    public static final CustomItemStackRenderer INSTANCE = new CustomItemStackRenderer();

    public CustomItemStackRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext,
                             PoseStack poseStack, MultiBufferSource bufferSource,
                             int combinedLight, int combinedOverlay) {
								 
		System.out.println("=======???========");
								 
        // 在这里编写你的自定义渲染逻辑
        // 示例：绘制一个简单的彩色立方体（请确保你已理解 OpenGL 矩阵操作）
		
		// Hack to remove transforms created by ItemRenderer#render
        poseStack.popPose();
		
        poseStack.pushPose(); // 保存当前变换状态
		
		obj.setRender(rt,null,poseStack,combinedLight);
		obj.renderPart("turret");
		obj.renderPart("body");
		
        // 根据上下文进行一些调整（可选）
        // 例如：在 GUI 中可能想要放大一点，或者调整旋转
        if (displayContext == ItemDisplayContext.GUI) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        }

        // 获取一个 VertexConsumer 用于渲染
        // 这里我们使用一个简单的白色纹理，并应用顶点颜色
        ResourceLocation texture = new ResourceLocation("minecraft", "textures/block/white_concrete.png");
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));

        // 获取当前的矩阵
        Matrix4f pose = poseStack.last().pose();

        // 定义立方体的顶点（简单的一个单位立方体，中心在原点）
        float size = 1.0f;
        float r = 1.0f, g = 0.5f, b = 0.0f, a = 1.0f; // 橙色

        // 绘制一个面（正面：Z 正方向）
        vertexConsumer.vertex(pose, -size, -size, size).color(r, g, b, a).uv(0, 0).uv2(combinedLight).endVertex();
        vertexConsumer.vertex(pose,  size, -size, size).color(r, g, b, a).uv(1, 0).uv2(combinedLight).endVertex();
        vertexConsumer.vertex(pose,  size,  size, size).color(r, g, b, a).uv(1, 1).uv2(combinedLight).endVertex();
        vertexConsumer.vertex(pose, -size,  size, size).color(r, g, b, a).uv(0, 1).uv2(combinedLight).endVertex();

        // 其他面类似（为了简洁省略，实际应该绘制全部六个面）

        poseStack.popPose(); // 恢复变换状态
		
		// Push the stack again since we popped the pose prior
        poseStack.pushPose();
    }*/
}
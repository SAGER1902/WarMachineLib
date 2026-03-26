package wmlib.mixin;
/*import wmlib.client.render.RenderTypeVehicle;
import wmlib.client.render.EntityRenderManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.model.EntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(EntityRenderer.class)
public abstract class WMVisionMixin1<T extends Entity> {
	
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderPre(T entity, float yaw, float tickDelta, 
                            PoseStack matrices, MultiBufferSource vertexConsumers, 
                            int light, CallbackInfo ci) {
        // 检查是否应对此实体应用效果
        /*if (shouldApplyEffect(entity))*{
            //EntityRenderManager.applyMode();
			//RenderSystem.disableDepthTest(); // 可选：使其无视深度遮挡
			//RenderSystem.setShader(GameRenderer::getRendertypeEyesShader);
        }
    }
    
    @Inject(method = "render", at = @At("RETURN"))
    private void onRenderPost(T entity, float yaw, float tickDelta,
                             PoseStack matrices, MultiBufferSource vertexConsumers,
                             int light, CallbackInfo ci) {
        // 渲染完成后清除着色器状态
        /*if (shouldApplyEffect(entity))*{
            //RenderSystem.setShader(() -> null);
        }
    }
    
    /*private boolean shouldApplyEffect(Entity entity) {
        // 根据配置判断是否渲染此实体
        /*if (!ThermalConfig.ENABLED) return false;
        if (entity.distanceToSqr(Minecraft.getInstance().player) > 
            ThermalConfig.maxDistance * ThermalConfig.maxDistance) {
            return false;
        }
        // 实体类型过滤
        if (entity instanceof Player && !ThermalConfig.affectPlayers) return false;
        // 添加更多过滤条件...*
        return true;
    }*
	
    @Shadow public abstract ResourceLocation getTextureLocation(T entity);
}*/
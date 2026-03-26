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
@Mixin(LivingEntityRenderer.class)
public abstract class WMVisionMixin<T extends LivingEntity, M extends EntityModel<T>> extends WMVisionMixin1<T> {
	/*@Inject(method = "getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;",
	at = @At("HEAD"),cancellable = true)
    protected void onGetRenderType(T livingEntity, boolean bodyVisible, boolean translucent, boolean glowing, CallbackInfoReturnable<RenderType> cir) {
		//EntityRenderManager.applyMode();
		ResourceLocation tex = this.getTextureLocation(livingEntity);
        cir.setReturnValue(RenderTypeVehicle.normal(tex));
    }*

    /*private boolean shouldApplyThermal(T entity) {
        // 复用或编写你的过滤逻辑，例如：
        // - 配置总开关
        // - 实体类型过滤（排除玩家、盔甲架等）
        // - 距离检查
        return ThermalConfig.ENABLED 
            && entity != Minecraft.getInstance().player // 通常不渲染自己
            && entity.distanceToSqr(Minecraft.getInstance().player) < ThermalConfig.maxDistance * ThermalConfig.maxDistance;
    }*
}*/
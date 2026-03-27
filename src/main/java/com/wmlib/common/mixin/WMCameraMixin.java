package wmlib.mixin;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.living.EntityWMSeat;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector2f;
@Mixin(ActiveRenderInfo.class)
public abstract class WMCameraMixin {
    
    @Shadow
    protected abstract void setRotation(float yaw, float pitch);
    
    @Shadow
    protected abstract void setPosition(double x, double y, double z);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;setRotation(FF)V", ordinal = 0),
            method = "setup(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/entity/Entity;ZZF)V",
            cancellable = true)
    private void onSetup(IBlockReader level, Entity entity, boolean detached, boolean mirrored, float partialTicks, CallbackInfo info) {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player != null && player.getVehicle() instanceof EntityWMSeat) {
            EntityWMSeat seat = (EntityWMSeat) player.getVehicle();
            if (seat.getVehicle() != null && seat.getVehicle() instanceof WeaponVehicleBase) {
                WeaponVehicleBase vehicle = (WeaponVehicleBase) seat.getVehicle();
                
                PointOfView cameraType = mc.options.getCameraType();
                boolean thirdPersonBack = cameraType == PointOfView.THIRD_PERSON_BACK;
                boolean firstPerson = cameraType.isFirstPerson();
                
                Vector2f turretYaw = vehicle.getCameraRotation(partialTicks, player, seat.gunner_aim, thirdPersonBack, firstPerson);
                if (turretYaw != null) {
                    setRotation((float)turretYaw.x, (float)turretYaw.y);
                }
                
                Vector3d position = vehicle.getCameraPosition(partialTicks, player, seat.isZoom, seat.gunner_aim, thirdPersonBack, firstPerson);
                if (position != null) {
                    setPosition((float)position.x, (float)position.y, (float)position.z);
                }
                
                if (turretYaw != null || position != null) {
                    info.cancel();
                }
            }
        }
    }
}
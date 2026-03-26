package wmlib.mixin;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.living.EntityWMSeat;


/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)*/
@Mixin(Camera.class)
public abstract class WMCameraMixin {
    @Shadow(aliases = "Lnet/minecraft/client/Camera;setRotation(FF)V")
    protected abstract void setRotation(float p_90573_, float p_90574_);

    @Shadow(aliases = "Lnet/minecraft/client/Camera;setPosition(DDD)V")
    protected abstract void setPosition(double p_90585_, double p_90586_, double p_90587_);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 0),
            method = "setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V",
            cancellable = true)
    private void onSetup(BlockGetter level, Entity entity, boolean detached, boolean mirrored, float partialTicks, CallbackInfo info) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
			if (player.getVehicle() instanceof EntityWMSeat && player.getVehicle() != null) {/// 1
				EntityWMSeat seat = (EntityWMSeat)player.getVehicle();
				if(seat.getVehicle()!=null && seat.getVehicle() instanceof WeaponVehicleBase){
					WeaponVehicleBase vehicle = (WeaponVehicleBase)seat.getVehicle();
					var turretYaw = vehicle.getCameraRotation(partialTicks, player, seat.gunner_aim, 
					mc.options.getCameraType() == CameraType.THIRD_PERSON_BACK, mc.options.getCameraType().isFirstPerson());
					if (turretYaw != null) {
						setRotation(turretYaw.x, turretYaw.y);
					}
					var position = vehicle.getCameraPosition(partialTicks, player, seat.isZoom, seat.gunner_aim, 
					mc.options.getCameraType() == CameraType.THIRD_PERSON_BACK, mc.options.getCameraType().isFirstPerson());
					if (position != null) {
						setPosition(position.x, position.y, position.z);
					}
					if (turretYaw != null || position != null) {
						info.cancel();
					}
				}
			}
        }
    }
}
package wmlib.client.render;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.EntityWMVehicleBase;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import wmlib.WMConfig;
@OnlyIn(Dist.CLIENT)
public class RenderRote
{
	public static void setRote(PoseStack stack, float count, float x, float y, float z){
		if(x!=0)stack.mulPose(Axis.XP.rotationDegrees(count*x));
		if(y!=0)stack.mulPose(Axis.YP.rotationDegrees(count*y));
		if(z!=0)stack.mulPose(Axis.ZP.rotationDegrees(count*z));
	}
	
    public static void renderPassengr(Minecraft mc, EntityWMVehicleBase vehicle, ResourceLocation Textures,
	float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
		//stack.pushPose();
		/*if(vehicle.flyPitch != 0 || vehicle.flyRoll != 0){
			stack.translate(0.0F, -0.4F, 0.0F);
		}
		for (Entity ent : vehicle.getPassengers()){
			if (ent instanceof EntityWMSeat){
				EntityWMSeat seat = (EntityWMSeat)ent;
				Entity passenger = seat.getAnyPassenger();
				if(passenger!=null && passenger instanceof LivingEntity && (passenger!=mc.player||mc.options.getCameraType() != CameraType.FIRST_PERSON)){
					WMConfig.clientRender=true;
					stack.pushPose();
					stack.mulPose(Axis.YP.rotationDegrees(entityYaw));
					if(!seat.seatHide)Minecraft.getInstance().getEntityRenderDispatcher().render(passenger,
							passenger.getX() - vehicle.getX(), passenger.getY() - vehicle.getY(), passenger.getZ() - vehicle.getZ(),
							entityYaw, partialTicks, stack, buffer, packedLight);
					stack.popPose();
				}
			}
			mc.getEntityRenderDispatcher().textureManager.getTexture(Textures);
		}*/
		//stack.popPose();
    }
}
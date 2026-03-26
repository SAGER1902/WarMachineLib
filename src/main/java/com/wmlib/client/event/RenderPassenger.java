/*import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.EntityWMSeat;
import wmlib.WMConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderPassenger {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRender(RenderPlayerEvent.Pre event) {
        if (event.getEntity() != (Minecraft.getInstance()).player) {
            return;
        }
        if ((Minecraft.getInstance()).screen instanceof InventoryScreen) {
            WMConfig.clientRender = true;
        }
    }
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void renderRidding(RenderLivingEvent.Pre event)//
	{
		Minecraft mc = Minecraft.getInstance();
		LivingEntity living = event.getEntity();
		if(living != null){
			if (living.getVehicle() instanceof EntityWMSeat && living.getVehicle() != null) {
				EntityWMSeat seat = (EntityWMSeat) living.getVehicle();
				if(seat.getVehicle()!=null && seat.getVehicle() instanceof EntityWMVehicleBase){
					EntityWMVehicleBase ve = (EntityWMVehicleBase) (seat.getVehicle());
					//PoseStack stack = event.getPoseStack();
					//stack.pushPose();//angle rote
					*stack.translate(0, (float)ve.seatPosY[0]+1.32F, (float)ve.seatPosZ[0]);
					stack.mulPose(Axis.XP.rotationDegrees(ve.flyPitch));
					stack.mulPose(Axis.ZP.rotationDegrees(ve.flyRoll));
					stack.translate(0, (float)-ve.seatPosY[0]-1.32F, (float)-ve.seatPosZ[0]);*
					if(seat.seatHide)event.setCanceled(true);
					
					if(WMConfig.clientRender){
						WMConfig.clientRender=false;
						return;
					}
					if(ve.VehicleType>2)event.setCanceled(true);
				}
			}
		}
	}
}*/
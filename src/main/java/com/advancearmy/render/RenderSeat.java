package advancearmy.render;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.RenderType;
import advancearmy.entity.EntitySA_Seat;
import wmlib.client.obj.SAObjModel;
import wmlib.client.render.SARenderState;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

@OnlyIn(Dist.CLIENT)
public class RenderSeat extends EntityRenderer<EntitySA_Seat>
{
	private static final ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/marker/seat.png");
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/marker/seat.obj");
	
    public RenderSeat(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
    }
    public ResourceLocation getTextureLocation(EntitySA_Seat entity)
    {
		return tex;
    }
    public void render(EntitySA_Seat entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight)
    {
	    stack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
		RenderType rt = SARenderState.getBlendDepthWrite(tex);
		obj.setRender(rt, null, stack, 0xF000F0);
		if(mc.player.getVehicle()==null){
			if(entity.getVehicle()!=null && entity.canRid()){
				if(entity.getVehicle() instanceof LivingEntity && ((LivingEntity)entity.getVehicle()).getHealth()>0){
					//stack.pushPose();
					
					if(entity.distanceTo(mc.player)<3){
						obj.renderPart("box");
					}
					//stack.popPose();
					
					stack.translate(0, (float)entity.seatPosY[0], 0);
					stack.mulPose(Axis.YP.rotationDegrees(-dispatcher.camera.getYRot() + 180F));
					stack.mulPose(Axis.XP.rotationDegrees(-dispatcher.camera.getXRot()));
					int i = entity.getVehicle().getPassengers().indexOf(entity);
					if(i==0)obj.renderPart("mat1");
					if(i>0){
						if(entity.weaponCount==0){
							obj.renderPart("mat3");
						}else{
							obj.renderPart("mat2");
						}
					}
				}
			}
		}
	    stack.popPose();
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
    }
}
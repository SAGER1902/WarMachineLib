package wmlib.client.render;

import wmlib.common.block.AbstractFacingBlock;
import wmlib.common.block.MelonBlock;
import wmlib.common.tileentity.MelonBlockTileEntity;
import wmlib.client.obj.SAObjModel;
import wmlib.client.render.SARenderState;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

@OnlyIn(Dist.CLIENT)
public class RenderMelonBlock implements BlockEntityRenderer<MelonBlockTileEntity> {
	private static final ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/marker/soldier_type.png");
	private static final SAObjModel soldier_type = new SAObjModel("wmlib:textures/marker/soldier_type.obj");
	public SAObjModel obj = new SAObjModel("wmlib:textures/block/block.obj");
	//private static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("textures/misc/enchanted_item_glint.png");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_glint.png");
    
	RenderType rt1 = SARenderState.getBlendDepthWrite(tex);
	RenderType rt2 = SARenderState.getBlendDepthWrite(ENCHANT_GLINT_LOCATION);

	public RenderMelonBlock(BlockEntityRendererProvider.Context context) {

	}
	
	@Override
    public void render(MelonBlockTileEntity entity, float partialTicks, PoseStack stack,
                      MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		MelonBlock block = (MelonBlock) entity.getBlockState().getBlock();
		if(block == null) return ;
		stack.pushPose();
		stack.translate(0.5F, 0, 0.5F);
		Direction direction = entity.getBlockState().getValue(AbstractFacingBlock.FACING);
        switch (direction) {
            case NORTH:
                stack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case EAST:
                stack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            case WEST:
                stack.mulPose(Axis.YP.rotationDegrees(-90));
                break;
            case SOUTH:
            default:
                break;
        }
		if(block.lvl > 4){
			soldier_type.setRender(rt1, null, stack, 15728880);
			//soldier_type.renderPart("box");
			//stack.translate(0, 0.5F, 0);
			if(block.lvl==5)soldier_type.renderPart("assult");
			if(block.lvl==6)soldier_type.renderPart("recon");
			if(block.lvl==7)soldier_type.renderPart("engineer");
			if(block.lvl==8)soldier_type.renderPart("medic");
			if(block.lvl==9)soldier_type.renderPart("support");
		}
		VertexConsumer vb = buffer.getBuffer(rt2);
		if(block.lvl == 1){
			renderEnchantGlint(stack, "enc", vb);
		}
		stack.popPose();
	}
	
	private void renderEnchantGlint(PoseStack stack, String name, VertexConsumer buffer) {
		obj.setRender(null, buffer, stack, 15728880);
        //stack.scale(1.01001F, 1.01001F, 1.01001F);
        long time = (long)((double)Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 8.0);
        float u = (float)(time % 110000L) / 110000.0f;
        float v = (float)(time % 30000L) / 30000.0f;
		obj.setMoveTex(u,v);
        obj.renderPart(name);
		obj.setMoveTex(0,0);
    }
}

package advancearmy.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;

import advancearmy.entity.mob.ERO_Ghast;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import wmlib.client.obj.SAObjModel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.math.Axis;
import org.lwjgl.opengl.GL12;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;

@OnlyIn(Dist.CLIENT)
public class EvilGhastRenderer extends MobRenderer<ERO_Ghast, GhastModel<ERO_Ghast>> {
   private static final ResourceLocation GHAST_LOCATION = ResourceLocation.tryParse("advancearmy:textures/mob/ero/ghast.png");
   private static final ResourceLocation GHAST_BOOMER = ResourceLocation.tryParse("advancearmy:textures/mob/ero/ghast2.png");
   private static final ResourceLocation GHAST_LASER = ResourceLocation.tryParse("advancearmy:textures/mob/ero/ghast3.png");
   private static final ResourceLocation GHAST_SHOOTING_LOCATION = ResourceLocation.tryParse("advancearmy:textures/mob/ero/ghast_shooting.png");

   public EvilGhastRenderer(EntityRendererProvider.Context context) {
      super(context, new GhastModel(context.bakeLayer(ModelLayers.GHAST)), 1.5f);
   }

   public ResourceLocation getTextureLocation(ERO_Ghast entity) {
	   if(entity.getAIType()<4){
		   return entity.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_LOCATION;
	   }else if(entity.getAIType()<6){
		   return entity.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_BOOMER;
	   }else{
		   return entity.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_LASER;
	   }
      ///return entity.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_LOCATION;
   }

   protected void scale(ERO_Ghast p_225620_1_, PoseStack p_225620_2_, float p_225620_3_) {
      float f = 1.0F;
      float f1 = 4.5F;
      float f2 = 4.5F;
      p_225620_2_.scale(4.5F, 4.5F, 4.5F);
   }
}
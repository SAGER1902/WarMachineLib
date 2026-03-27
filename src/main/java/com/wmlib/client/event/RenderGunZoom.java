package wmlib.client.event;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wmlib.common.item.ItemGun;
public class RenderGunZoom {
	public String ads;
	@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
	  public void renderfov(FOVUpdateEvent event)
	  {
		Minecraft minecraft = Minecraft.getInstance();
		PlayerEntity entityplayer = minecraft.player;
		ItemStack itemstack = ((PlayerEntity) (entityplayer)).getMainHandItem();
		if(!itemstack.isEmpty() && itemstack.getItem() instanceof ItemGun){
			ItemGun gun = (ItemGun) itemstack.getItem();
			if(gun.scopezoom > 1.0F) {
				if (gun.aim_time>=gun.time_aim) {
					{
						event.setNewfov(event.getFov() / gun.scopezoom);
					}
				}
			}
		}//item
	  }
	
	public boolean zoomtype;
	
	@OnlyIn(Dist.CLIENT)
	  @SubscribeEvent
	  public void onEvent(RenderGameOverlayEvent.Pre event)
	  {
		  Minecraft minecraft = Minecraft.getInstance();
		  World world = minecraft.level;
		  MainWindow window = minecraft.getWindow();
	        int i = window.getGuiScaledWidth();
	        int j = window.getGuiScaledHeight();
	        PlayerEntity entityplayer = minecraft.player;
			ItemStack itemstack = ((PlayerEntity)(entityplayer)).getMainHandItem();
			FontRenderer fontrenderer = minecraft.font;
//	        minecraft.fontRenderer.;
	        //OpenGlHelper.
	        
	        GL11.glEnable(GL11.GL_BLEND);
     //      if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
		{
			if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemGun) {
				ItemGun gun = (ItemGun) itemstack.getItem();
				if (gun.aim_time>=gun.time_aim) {
					if(gun.zoomrender) {
						ads = "wmlib:textures/misc/tank_scope.png";
						this.renderPumpkinBlur(minecraft, window, ads);
					}
					ForgeIngameGui.renderCrosshairs = false;
				} else {
					ForgeIngameGui.renderCrosshairs = true;
				}
		//		ForgeIngameGui.renderCrosshairs = true;
				this.zoomtype = true;

			} else {
				if (this.zoomtype == true) {
					ForgeIngameGui.renderCrosshairs = true;
					this.zoomtype = false;
				}

			}
		}
	  }

	@OnlyIn(Dist.CLIENT)
	protected void renderPumpkinBlur(Minecraft minecraft, MainWindow window, String adss)
	{
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableAlphaTest();
	      minecraft.getTextureManager().bind(new ResourceLocation(adss));
	      Tessellator tessellator = Tessellator.getInstance();
	      BufferBuilder bufferbuilder = tessellator.getBuilder();
	      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
	      bufferbuilder.vertex(0.0D, (double)window.getGuiScaledHeight(), -90.0D).uv(0.0F, 1.0F).endVertex();
	      bufferbuilder.vertex((double)window.getGuiScaledWidth(), (double)window.getGuiScaledHeight(), -90.0D).uv(1.0F, 1.0F).endVertex();
	      bufferbuilder.vertex((double)window.getGuiScaledWidth(), 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
	      bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
	      tessellator.end();
	      RenderSystem.depthMask(true);
	      RenderSystem.enableDepthTest();
	      RenderSystem.enableAlphaTest();
	      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
}

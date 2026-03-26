package wmlib.client;
import wmlib.common.tileentity.TileEntityRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wmlib.client.render.RenderMelonBlock;
import wmlib.WarMachineLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import java.io.IOException;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderStateShard;
@net.minecraftforge.fml.common.Mod.EventBusSubscriber(bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRenderHandler {
	
    private static ShaderInstance entityPortalShader;
    private static ShaderInstance entityTexTrackShader;
    public static final RenderStateShard.ShaderStateShard CUSTOM_PORTAL_SHADER_STATE = new RenderStateShard.ShaderStateShard(() -> entityPortalShader);
    public static final RenderStateShard.ShaderStateShard CUSTOM_TRACK_SHADER_STATE = new RenderStateShard.ShaderStateShard(() -> entityTexTrackShader);

	public static ShaderInstance getRendertypePortalShader() {
		return entityPortalShader;
	}
	public static ShaderInstance getRendertypeTexTrackShader() {
		return entityTexTrackShader;
	}

    /*public static ShaderInstance CUSTOM_PORTAL_SHADER;
	//public static ShaderInstance THERMAL_IMAGING_SHADER;
	public static ShaderInstance TEX_TRACK_SHADER;*/
    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) {
		ResourceProvider provider = event.getResourceProvider();
        try {
            event.registerShader(new ShaderInstance(provider,
			ResourceLocation.tryParse("wmlib:rendertype_custom_portal"),
			DefaultVertexFormat.POSITION),
			(createdShader) -> entityPortalShader = createdShader);
			
            event.registerShader(new ShaderInstance(provider,
			ResourceLocation.tryParse("wmlib:rendertype_custom_track"),
			DefaultVertexFormat.NEW_ENTITY),
			(createdShader) -> entityTexTrackShader = createdShader);
        } catch (IOException e) {
            WarMachineLib.LOGGER.error("Failed to load custom shader", e);
            e.printStackTrace();
        }
	}
	@SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TileEntityRegister.MELON.get(), RenderMelonBlock::new);
		event.registerBlockEntityRenderer(TileEntityRegister.MELON.get(), RenderMelonBlock::new);
    }
}

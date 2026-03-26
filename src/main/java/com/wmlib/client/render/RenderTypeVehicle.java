package wmlib.client.render;
import wmlib.client.ClientRenderHandler;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.renderer.ShaderInstance;
import wmlib.util.ShaderCompatHelper;
public class RenderTypeVehicle extends RenderType {
    public RenderTypeVehicle(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_
            , int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }
	
    /*static RenderStateShard.ShaderStateShard customShaderStateTest = new RenderStateShard.ShaderStateShard(
        () -> ClientRenderHandler.THERMAL_IMAGING_SHADER
    );
    public static RenderType normal(ResourceLocation tex) {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(customShaderStateTest)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true);
        return RenderType.create("wmlib_blend_glint", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, state);
    }*/
	
    private static final Function<ResourceLocation, RenderType> OBJ_RENDER = Util.memoize((tex) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)//RENDERTYPE_ENTITY_SOLID_SHADER
                .setTextureState(new RenderStateShard.TextureStateShard(tex, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true);
        return create("obj_render"
                , DefaultVertexFormat.NEW_ENTITY
                , VertexFormat.Mode.TRIANGLES
                , 256
                , true
                , false
                , rendertype$compositestate);
    });
	
    private static final Function<ResourceLocation, RenderType> OBJ_RENDER_BLEND = Util.memoize((tex) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER)//RENDERTYPE_ENTITY_CUTOUT_SHADER
                .setTextureState(new RenderStateShard.TextureStateShard(tex, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true);
        return create("obj_render_blend"
                , DefaultVertexFormat.NEW_ENTITY
                , VertexFormat.Mode.TRIANGLES
                , 256
                , true
                , false
                , rendertype$compositestate);
    });
	
	/*static RenderStateShard.ShaderStateShard customShaderStateTrack = new RenderStateShard.ShaderStateShard(
        () -> ClientRenderHandler.TEX_TRACK_SHADER
    );*/
	protected static final RenderStateShard.ShaderStateShard CUSTOM_TRACK_SHADER_STATE = new RenderStateShard.ShaderStateShard(ClientRenderHandler::getRendertypeTexTrackShader);

    private static final Function<ResourceLocation, RenderType> OBJ_RENDER_TRACK = Util.memoize((tex) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(ShaderCompatHelper.isShaderPackLoaded() ? RENDERTYPE_ENERGY_SWIRL_SHADER:CUSTOM_TRACK_SHADER_STATE)
                .setTextureState(new RenderStateShard.TextureStateShard(tex, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true);
        return create("obj_render_track"
                , DefaultVertexFormat.NEW_ENTITY
                , VertexFormat.Mode.TRIANGLES
                , 256
                , true
                , false
                , rendertype$compositestate);
    });
	
    public static RenderType objrender(ResourceLocation tex) {
        return OBJ_RENDER.apply(tex);
    }
    public static RenderType objrender_blend(ResourceLocation tex) {
        return OBJ_RENDER_BLEND.apply(tex);
    }
    public static RenderType objrender_track(ResourceLocation tex) {
        return OBJ_RENDER_TRACK.apply(tex);
    }
}

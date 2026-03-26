package wmlib.init;
import wmlib.init.WMModEntities;
import wmlib.client.render.RenderBulletBase;
import wmlib.client.render.RenderFlare;
import wmlib.client.render.RenderRad;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wmlib.rts.XiangJiRenderer;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(WMModEntities.ENTITY_BULLET.get(), RenderBulletBase::new);
        event.registerEntityRenderer(WMModEntities.ENTITY_SHELL.get(), RenderBulletBase::new);
		event.registerEntityRenderer(WMModEntities.ENTITY_MISSILE.get(), RenderBulletBase::new);
		event.registerEntityRenderer(WMModEntities.ENTITY_GRENADE.get(), RenderBulletBase::new);
		event.registerEntityRenderer(WMModEntities.ENTITY_FLARE.get(), RenderFlare::new);
		event.registerEntityRenderer(WMModEntities.ENTITY_RAD.get(), RenderRad::new);
		event.registerEntityRenderer(WMModEntities.XIANG_JI.get(), XiangJiRenderer::new);
    }
}

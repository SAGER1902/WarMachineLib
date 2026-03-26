package advancearmy.init;
import advancearmy.init.ModEntities;
import advancearmy.render.RenderSeat;
import advancearmy.render.RenderTankBase;
import advancearmy.render.RenderHeliBase;
import advancearmy.render.RenderAirBase;
import advancearmy.render.RenderTurretBase;

import advancearmy.render.vehicle.RenderTank;
import advancearmy.render.vehicle.RenderLAV;
import advancearmy.render.vehicle.RenderAH6;
import advancearmy.render.vehicle.RenderEmber;
import advancearmy.render.vehicle.RenderYw010;
import advancearmy.render.vehicle.RenderBike;
import advancearmy.render.vehicle.RenderFTK_H;
import advancearmy.render.vehicle.RenderBattleShip;
import advancearmy.render.vehicle.RenderSickle;
import advancearmy.render.vehicle.Render99;
import advancearmy.render.vehicle.RenderMirage;
import advancearmy.render.vehicle.RenderReaper;
import advancearmy.render.vehicle.RenderMASTDOM;

import advancearmy.render.vehicle.RenderRockTank;
import advancearmy.render.vehicle.RenderSTAPC;

import advancearmy.render.map.BoxRenderer;
import advancearmy.render.map.SupportRenderer;
import advancearmy.render.map.InvasionRender;
import advancearmy.render.map.DefenceRenderer;
import advancearmy.render.map.DefaultRender;
import advancearmy.render.map.ParticleRender;

import advancearmy.render.MachineRendererV;
import advancearmy.render.MachineRendererS;

import advancearmy.render.soldier.RenderConscript;
import advancearmy.render.soldier.RenderConscriptX;
import advancearmy.render.soldier.RenderSoldier;
import advancearmy.render.soldier.RenderSwun;
import advancearmy.render.soldier.RenderGI;
import advancearmy.render.soldier.RenderOFG;
import advancearmy.render.soldier.RenderRADS;
import advancearmy.render.soldier.RenderGAT;
import advancearmy.render.soldier.RenderMWDrone;

//import advancearmy.render.RenderDragonTurret;
import advancearmy.render.mob.RenderAohuan;
import advancearmy.render.mob.RenderREB;
import advancearmy.render.mob.RenderPillager;
import advancearmy.render.mob.PortalRenderer;
import advancearmy.render.mob.PortalRenderer1;
import advancearmy.render.mob.EvilPhantomRenderer;
import advancearmy.render.mob.EvilGhastRenderer;
import advancearmy.render.mob.CreeperRenderer;
import advancearmy.render.mob.SkeletonRenderer;
import advancearmy.render.mob.ZombieRenderer;
import advancearmy.render.mob.SpiderRenderer;
import advancearmy.render.mob.RavagerRenderer;
import advancearmy.render.mob.RenderGiant;
import advancearmy.AdvanceArmy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.renderer.entity.EntityRenderers;

//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@Mod.EventBusSubscriber(modid= AdvanceArmy.MODID, value= Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityRenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ENTITY_SEAT.get(), RenderSeat::new);
        event.registerEntityRenderer(ModEntities.ENTITY_TANK.get(), RenderTank::new);
		
        event.registerEntityRenderer(ModEntities.ENTITY_CAR.get(), RenderTankBase::new);
        event.registerEntityRenderer(ModEntities.ENTITY_HMMWV.get(), RenderTankBase::new);
		
        event.registerEntityRenderer(ModEntities.ENTITY_BMP2.get(), RenderTankBase::new);
        event.registerEntityRenderer(ModEntities.ENTITY_T55.get(), RenderTankBase::new);
        event.registerEntityRenderer(ModEntities.ENTITY_FTK.get(), RenderTankBase::new);
        event.registerEntityRenderer(ModEntities.ENTITY_M2A2AA.get(), RenderTankBase::new);
        event.registerEntityRenderer(ModEntities.ENTITY_M2A2.get(), RenderTankBase::new);
        event.registerEntityRenderer(ModEntities.ENTITY_M109.get(), RenderTankBase::new);
        event.registerEntityRenderer(ModEntities.ENTITY_BMPT.get(), RenderTankBase::new);
        event.registerEntityRenderer(ModEntities.ENTITY_PRISM.get(), RenderTankBase::new);
        event.registerEntityRenderer(ModEntities.ENTITY_LAA.get(), RenderTankBase::new);
        event.registerEntityRenderer(ModEntities.ENTITY_LAV.get(), RenderLAV::new);
		event.registerEntityRenderer(ModEntities.ENTITY_LAVAA.get(), RenderLAV::new);
        event.registerEntityRenderer(ModEntities.ENTITY_T72.get(), RenderTankBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_T90.get(), RenderTankBase::new);
		
		EntityRenderers.register(ModEntities.ENTITY_GI.get(), RenderGI::new);
		EntityRenderers.register(ModEntities.ENTITY_CONS.get(), RenderConscript::new);
		EntityRenderers.register(ModEntities.ENTITY_CONSX.get(), RenderConscriptX::new);
		EntityRenderers.register(ModEntities.ENTITY_SOLDIER.get(), RenderSoldier::new);
		EntityRenderers.register(ModEntities.ENTITY_OFG.get(), RenderOFG::new);
		EntityRenderers.register(ModEntities.ENTITY_SWUN.get(), RenderSwun::new);
		EntityRenderers.register(ModEntities.ENTITY_AOHUAN.get(), RenderAohuan::new);
		EntityRenderers.register(ModEntities.ENTITY_PI.get(), RenderPillager::new);
		EntityRenderers.register(ModEntities.ENTITY_EHUSK.get(), ZombieRenderer::new);
		EntityRenderers.register(ModEntities.ERO_SPIDER.get(), SpiderRenderer::new);
		EntityRenderers.register(ModEntities.ENTITY_GIANT.get(), RenderGiant::new);
		EntityRenderers.register(ModEntities.ERO_RAV.get(), RavagerRenderer::new);
		EntityRenderers.register(ModEntities.E_AA.get(), RenderTurretBase::new);
		EntityRenderers.register(ModEntities.E_MORTAR.get(), RenderTurretBase::new);
		EntityRenderers.register(ModEntities.E_ROCKET.get(), RenderTurretBase::new);
		
		EntityRenderers.register(ModEntities.ENTITY_REB.get(), RenderREB::new);
		EntityRenderers.register(ModEntities.ENTITY_SKELETON.get(), SkeletonRenderer::new);
		EntityRenderers.register(ModEntities.ENTITY_CREEPER.get(), CreeperRenderer::new);
		EntityRenderers.register(ModEntities.ENTITY_GST.get(), EvilGhastRenderer::new);
		EntityRenderers.register(ModEntities.ENTITY_PHA.get(), EvilPhantomRenderer::new);
		EntityRenderers.register(ModEntities.ENTITY_EZOMBIE.get(), ZombieRenderer::new);
		EntityRenderers.register(ModEntities.ENTITY_POR.get(), PortalRenderer::new);
		EntityRenderers.register(ModEntities.ENTITY_POR1.get(), PortalRenderer1::new);
		
		EntityRenderers.register(ModEntities.ENTITY_BIKE.get(), RenderBike::new);
		EntityRenderers.register(ModEntities.ENTITY_AH6.get(), RenderAH6::new);
		EntityRenderers.register(ModEntities.ENTITY_YW010.get(), RenderYw010::new);
		EntityRenderers.register(ModEntities.ENTITY_BSHIP.get(), RenderBattleShip::new);
		EntityRenderers.register(ModEntities.ENTITY_99G.get(), Render99::new);
		EntityRenderers.register(ModEntities.ENTITY_FTK_H.get(), RenderFTK_H::new);
		EntityRenderers.register(ModEntities.ENTITY_SICKLE.get(), RenderSickle::new);
		EntityRenderers.register(ModEntities.ENTITY_REAPER.get(), RenderReaper::new);
		EntityRenderers.register(ModEntities.ENTITY_TESLA.get(), RenderTankBase::new);
		EntityRenderers.register(ModEntities.ENTITY_MIRAGE.get(), RenderMirage::new);
		EntityRenderers.register(ModEntities.ENTITY_APAGAT.get(), RenderTankBase::new);
		EntityRenderers.register(ModEntities.ENTITY_MMTANK.get(), RenderTankBase::new);
		EntityRenderers.register(ModEntities.ENTITY_EMBER.get(), RenderEmber::new);
		EntityRenderers.register(ModEntities.ENTITY_RADS.get(), RenderRADS::new);
		EntityRenderers.register(ModEntities.ENTITY_GAT.get(), RenderGAT::new);
		
		event.registerEntityRenderer(ModEntities.ENTITY_A10A.get(), RenderAirBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_AH1Z.get(), RenderHeliBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_PLANE.get(), RenderAirBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_PLANE1.get(), RenderAirBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_PLANE2.get(), RenderAirBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_F35.get(), RenderAirBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_SU33.get(), RenderAirBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_A10C.get(), RenderAirBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_LAPEAR.get(), RenderAirBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_FW020.get(), RenderAirBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_YOUHUN.get(), RenderHeliBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_HELI.get(), RenderHeliBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_MI24.get(), RenderHeliBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_MORTAR.get(), RenderTurretBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_TOW.get(), RenderTurretBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_STIN.get(), RenderTurretBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_M2HB.get(), RenderTurretBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_KORD.get(), RenderTurretBase::new);
		event.registerEntityRenderer(ModEntities.ENTITY_MAST.get(), RenderMASTDOM::new);
		event.registerEntityRenderer(ModEntities.ENTITY_VMAC.get(), MachineRendererV::new);
		event.registerEntityRenderer(ModEntities.ENTITY_SMAC.get(), MachineRendererS::new);
		event.registerEntityRenderer(ModEntities.ENTITY_SANDBAG.get(), DefaultRender::new);
		
		event.registerEntityRenderer(ModEntities.ENTITY_MWD.get(), RenderMWDrone::new);
		event.registerEntityRenderer(ModEntities.ENTITY_STAPC.get(), RenderSTAPC::new);
		event.registerEntityRenderer(ModEntities.ENTITY_RCTANK.get(), RenderRockTank::new);
		
		event.registerEntityRenderer(ModEntities.ENTITY_RBOX.get(), BoxRenderer::new);
		event.registerEntityRenderer(ModEntities.ENTITY_SPT.get(), SupportRenderer::new);
		event.registerEntityRenderer(ModEntities.ENTITY_DPT.get(), DefenceRenderer::new);
    	event.registerEntityRenderer(ModEntities.ENTITY_MOVEP.get(), DefaultRender::new);
		event.registerEntityRenderer(ModEntities.ENTITY_VRES.get(), DefaultRender::new);
		event.registerEntityRenderer(ModEntities.ENTITY_CRES.get(), DefaultRender::new);
		event.registerEntityRenderer(ModEntities.ENTITY_P.get(), ParticleRender::new);
		event.registerEntityRenderer(ModEntities.ENTITY_INV.get(), InvasionRender::new);
    }
}

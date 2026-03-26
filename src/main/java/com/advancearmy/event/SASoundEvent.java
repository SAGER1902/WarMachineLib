package advancearmy.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import advancearmy.AdvanceArmy;
public class SASoundEvent {
	public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AdvanceArmy.MODID);
	public static final RegistryObject<SoundEvent> maopao = registerSound("advancearmy.maopao");
	public static final RegistryObject<SoundEvent> laser16 = registerSound("advancearmy.laser16");
	public static final RegistryObject<SoundEvent> laser10 = registerSound("advancearmy.laser10");
	public static final RegistryObject<SoundEvent> gun18 = registerSound("advancearmy.gun18");
	public static final RegistryObject<SoundEvent> gun8 = registerSound("advancearmy.gun8");
	public static final RegistryObject<SoundEvent> gun10 = registerSound("advancearmy.gun10");
	public static final RegistryObject<SoundEvent> reload1 = registerSound("advancearmy.reload1");
	public static final RegistryObject<SoundEvent> rocket1 = registerSound("advancearmy.rocket1");
	public static final RegistryObject<SoundEvent> gun3 = registerSound("advancearmy.gun3");
	public static final RegistryObject<SoundEvent> gun4 = registerSound("advancearmy.gun4");
	public static final RegistryObject<SoundEvent> gun14 = registerSound("advancearmy.gun14");
	public static final RegistryObject<SoundEvent> gun2 = registerSound("advancearmy.gun2");
	public static final RegistryObject<SoundEvent> gun1 = registerSound("advancearmy.gun1");
	public static final RegistryObject<SoundEvent> fire_flame = registerSound("advancearmy.fire_flame");
	public static final RegistryObject<SoundEvent> box_heal = registerSound("advancearmy.box_heal");
	public static final RegistryObject<SoundEvent> mirage_fire = registerSound("advancearmy.mirage_fire");
	
	public static final RegistryObject<SoundEvent> mirage_move = registerSound("advancearmy.mirage_move");
	
	public static final RegistryObject<SoundEvent> mwdronef = registerSound("advancearmy.mwdronef");
	public static final RegistryObject<SoundEvent> stapcf = registerSound("advancearmy.stapcf");
	
	public static final RegistryObject<SoundEvent> nuke2_exp = registerSound("advancearmy.nuke2_exp");
	public static final RegistryObject<SoundEvent> nuke2_rad = registerSound("advancearmy.nuke2_rad");

	public static final RegistryObject<SoundEvent> moqiuli = registerSound("advancearmy.moqiuli");

	public static final RegistryObject<SoundEvent> fs_say = registerSound("advancearmy.fs_say");
	public static final RegistryObject<SoundEvent> fs_hurt = registerSound("advancearmy.fs_hurt");
	public static final RegistryObject<SoundEvent> fs_die = registerSound("advancearmy.fs_die");
	public static final RegistryObject<SoundEvent> fire_fsp = registerSound("advancearmy.fire_fsp");
	public static final RegistryObject<SoundEvent> deploy_fsp = registerSound("advancearmy.deploy_fsp");
	public static final RegistryObject<SoundEvent> open_box = registerSound("advancearmy.open_box");
	public static final RegistryObject<SoundEvent> throw_m67 = registerSound("advancearmy.throw_m67");
	public static final RegistryObject<SoundEvent> exp_m67 = registerSound("advancearmy.exp_m67");

	public static final RegistryObject<SoundEvent> fire_92fs = registerSound("advancearmy.fire_92fs");
	public static final RegistryObject<SoundEvent> m16_fire = registerSound("advancearmy.m16_fire");
	public static final RegistryObject<SoundEvent> svd_fire = registerSound("advancearmy.svd_fire");
	public static final RegistryObject<SoundEvent> ak47_fire = registerSound("advancearmy.ak47_fire");
	public static final RegistryObject<SoundEvent> pkm_fire = registerSound("advancearmy.pkm_fire");
	public static final RegistryObject<SoundEvent> l96a1_fire = registerSound("advancearmy.l96a1_fire");
	public static final RegistryObject<SoundEvent> m4_fire = registerSound("advancearmy.m4_fire");
	public static final RegistryObject<SoundEvent> mp5_fire = registerSound("advancearmy.mp5_fire");
	public static final RegistryObject<SoundEvent> m249_fire = registerSound("advancearmy.m249_fire");
	public static final RegistryObject<SoundEvent> m24_fire = registerSound("advancearmy.m24_fire");
	public static final RegistryObject<SoundEvent> remington_fire = registerSound("advancearmy.remington_fire");

	public static final RegistryObject<SoundEvent> reload_rpg = registerSound("advancearmy.reload_rpg");
	public static final RegistryObject<SoundEvent> reload_smaw = registerSound("advancearmy.reload_smaw");
	public static final RegistryObject<SoundEvent> m24_reload = registerSound("advancearmy.m24_reload");
	public static final RegistryObject<SoundEvent> m249_reload = registerSound("advancearmy.m249_reload");
	public static final RegistryObject<SoundEvent> mp5_reload = registerSound("advancearmy.mp5_reload");
	public static final RegistryObject<SoundEvent> svd_reload = registerSound("advancearmy.svd_reload");
	public static final RegistryObject<SoundEvent> ak47_reload = registerSound("advancearmy.ak47_reload");
	public static final RegistryObject<SoundEvent> pkm_reload = registerSound("advancearmy.pkm_reload");
	public static final RegistryObject<SoundEvent> l96a1_reload = registerSound("advancearmy.l96a1_reload");
	public static final RegistryObject<SoundEvent> m4_reload = registerSound("advancearmy.m4_reload");

	public static final RegistryObject<SoundEvent> fire_81mm_m1 = registerSound("advancearmy.fire_81mm_m1");
	public static final RegistryObject<SoundEvent> reload_81mm_m1 = registerSound("advancearmy.reload_81mm_m1");
	public static final RegistryObject<SoundEvent> fire_100mm = registerSound("advancearmy.fire_100mm");
	public static final RegistryObject<SoundEvent> fire_agm114 = registerSound("advancearmy.fire_agm114");
	public static final RegistryObject<SoundEvent> fire_autocannon = registerSound("advancearmy.fire_autocannon");
	public static final RegistryObject<SoundEvent> fire_gatling = registerSound("advancearmy.fire_gatling");
	public static final RegistryObject<SoundEvent> fire_javelin = registerSound("advancearmy.fire_javelin");
	public static final RegistryObject<SoundEvent> fire_m230 = registerSound("advancearmy.fire_m230");
	public static final RegistryObject<SoundEvent> fire_smaw = registerSound("advancearmy.fire_smaw");
	public static final RegistryObject<SoundEvent> move_ah64 = registerSound("advancearmy.move_ah64");
	public static final RegistryObject<SoundEvent> fire_rpg7 = registerSound("advancearmy.fire_rpg7");
	public static final RegistryObject<SoundEvent> fire_gat = registerSound("advancearmy.fire_gat");
	public static final RegistryObject<SoundEvent> gt_say = registerSound("advancearmy.gt_say");
	public static final RegistryObject<SoundEvent> apa_hurt = registerSound("advancearmy.apa_hurt");
	public static final RegistryObject<SoundEvent> apa_die = registerSound("advancearmy.apa_die");

	public static final RegistryObject<SoundEvent> fire_grenade = registerSound("advancearmy.fire_grenade");
	public static final RegistryObject<SoundEvent> throw_grenade = registerSound("advancearmy.throw_grenade");
	public static final RegistryObject<SoundEvent> fire_skyfire = registerSound("advancearmy.fire_skyfire");
	public static final RegistryObject<SoundEvent> fire_mast = registerSound("advancearmy.fire_mast");
	public static final RegistryObject<SoundEvent> chaincrash = registerSound("advancearmy.chaincrash");
	public static final RegistryObject<SoundEvent> reload_mast = registerSound("advancearmy.reload_mast");
		
	public static final RegistryObject<SoundEvent> command_say = registerSound("advancearmy.command_say");
	public static final RegistryObject<SoundEvent> nuclear_worn = registerSound("advancearmy.nuclear_worn");
	public static final RegistryObject<SoundEvent> nuclear_exp = registerSound("advancearmy.nuclear_exp");
	public static final RegistryObject<SoundEvent> csk_move = registerSound("advancearmy.csk_move");
	public static final RegistryObject<SoundEvent> csk = registerSound("advancearmy.csk");
	public static final RegistryObject<SoundEvent> missile_fire1 = registerSound("advancearmy.missile_fire1");
	public static final RegistryObject<SoundEvent> missile_fly1 = registerSound("advancearmy.missile_fly1");
	public static final RegistryObject<SoundEvent> missile_fly2 = registerSound("advancearmy.missile_fly2");
	public static final RegistryObject<SoundEvent> missile_fly3 = registerSound("advancearmy.missile_fly3");
	public static final RegistryObject<SoundEvent> missile_hit1 = registerSound("advancearmy.missile_hit1");
	public static final RegistryObject<SoundEvent> shell_fly = registerSound("advancearmy.shell_fly");
	
	public static final RegistryObject<SoundEvent> hjmove = registerSound("advancearmy.hjmove");
	public static final RegistryObject<SoundEvent> hjup = registerSound("advancearmy.hjup");
	public static final RegistryObject<SoundEvent> hjswing = registerSound("advancearmy.hjswing");
	public static final RegistryObject<SoundEvent> knightf = registerSound("advancearmy.knightf");
	public static final RegistryObject<SoundEvent> teslafire1 = registerSound("advancearmy.teslafire1");
	public static final RegistryObject<SoundEvent> teslafire2 = registerSound("advancearmy.teslafire2");
	public static final RegistryObject<SoundEvent> teslareload = registerSound("advancearmy.teslareload");
	
	public static final RegistryObject<SoundEvent> a10_move = registerSound("advancearmy.a10_move");
	public static final RegistryObject<SoundEvent> car_move = registerSound("advancearmy.car_move");
	public static final RegistryObject<SoundEvent> car_start = registerSound("advancearmy.car_start");
	public static final RegistryObject<SoundEvent> fire_a10 = registerSound("advancearmy.fire_a10");
	public static final RegistryObject<SoundEvent> fire_a10_3p = registerSound("advancearmy.fire_a10_3p");

	public static final RegistryObject<SoundEvent> move_hmmwv = registerSound("advancearmy.move_hmmwv");
	public static final RegistryObject<SoundEvent> start_vodnik = registerSound("advancearmy.start_vodnik");
	public static final RegistryObject<SoundEvent> move_vodnik = registerSound("advancearmy.move_vodnik");
	public static final RegistryObject<SoundEvent> start_hmmwv = registerSound("advancearmy.start_hmmwv");
	public static final RegistryObject<SoundEvent> fire_d30 = registerSound("advancearmy.fire_d30");

	public static final RegistryObject<SoundEvent> fire_lw155 = registerSound("advancearmy.fire_lw155");
	public static final RegistryObject<SoundEvent> fire_tow = registerSound("advancearmy.fire_tow");
	public static final RegistryObject<SoundEvent> fire_stin = registerSound("advancearmy.fire_stin");
	public static final RegistryObject<SoundEvent> fire_2a42 = registerSound("advancearmy.fire_2a42");
	
	public static final RegistryObject<SoundEvent> fire_kornet = registerSound("advancearmy.fire_kornet");
	public static final RegistryObject<SoundEvent> fire_98 = registerSound("advancearmy.fire_98");
	public static final RegistryObject<SoundEvent> reload_98 = registerSound("advancearmy.reload_98");
	public static final RegistryObject<SoundEvent> start_98 = registerSound("advancearmy.start_98");
	public static final RegistryObject<SoundEvent> fire_m6 = registerSound("advancearmy.fire_m6");
	public static final RegistryObject<SoundEvent> reload_m6 = registerSound("advancearmy.reload_m6");
	public static final RegistryObject<SoundEvent> start_m6 = registerSound("advancearmy.start_m6");
	
	public static final RegistryObject<SoundEvent> smoke_reload = registerSound("advancearmy.smoke_reload");
	public static final RegistryObject<SoundEvent> smoke_fire = registerSound("advancearmy.smoke_fire");
	public static final RegistryObject<SoundEvent> laser_lock = registerSound("advancearmy.laser_lock");
	public static final RegistryObject<SoundEvent> growler_lock = registerSound("advancearmy.growler_lock");
	public static final RegistryObject<SoundEvent> powercannon = registerSound("advancearmy.powercannon");
	public static final RegistryObject<SoundEvent> air_autocannon = registerSound("advancearmy.air_autocannon");
	public static final RegistryObject<SoundEvent> air_ground = registerSound("advancearmy.air_ground");
	public static final RegistryObject<SoundEvent> air_move = registerSound("advancearmy.air_move");
	public static final RegistryObject<SoundEvent> air_start = registerSound("advancearmy.air_start");
	public static final RegistryObject<SoundEvent> bomb_release = registerSound("advancearmy.bomb_release");
	public static final RegistryObject<SoundEvent> bomb_reload = registerSound("advancearmy.bomb_reload");
	public static final RegistryObject<SoundEvent> jet_distant = registerSound("advancearmy.jet_distant");
	public static final RegistryObject<SoundEvent> turret_move = registerSound("advancearmy.turret_move");
	public static final RegistryObject<SoundEvent> air_explosion = registerSound("advancearmy.air_explosion");
	public static final RegistryObject<SoundEvent> helicopter_explosion = registerSound("advancearmy.helicopter_explosion");
	public static final RegistryObject<SoundEvent> lcac_explosion = registerSound("advancearmy.lcac_explosion");
	public static final RegistryObject<SoundEvent> tank_explode = registerSound("advancearmy.tank_explode");
	public static final RegistryObject<SoundEvent> wreck_explosion = registerSound("advancearmy.wreck_explosion");
	
	public static final RegistryObject<SoundEvent> shell_impact = registerSound("advancearmy.shell_impact");
	public static final RegistryObject<SoundEvent> tank_shell = registerSound("advancearmy.tank_shell");
	public static final RegistryObject<SoundEvent> tank_shell_metal = registerSound("advancearmy.tank_shell_metal");
	public static final RegistryObject<SoundEvent> metal_impact = registerSound("advancearmy.metal_impact");
	public static final RegistryObject<SoundEvent> artillery_impact = registerSound("advancearmy.artillery_impact");
	
	public static final RegistryObject<SoundEvent> mg_impact_metal = registerSound("advancearmy.mg_impact_metal");
	public static final RegistryObject<SoundEvent> mg_impact = registerSound("advancearmy.mg_impact");
	public static final RegistryObject<SoundEvent> gun_impact_metal = registerSound("advancearmy.gun_impact_metal");
	public static final RegistryObject<SoundEvent> gun_impact_mud = registerSound("advancearmy.gun_impact_mud");
	public static final RegistryObject<SoundEvent> gun_impact_flesh = registerSound("advancearmy.gun_impact_flesh");
	public static final RegistryObject<SoundEvent> ag_metal = registerSound("advancearmy.25mm_metal");
	public static final RegistryObject<SoundEvent> ag_impact = registerSound("advancearmy.25mm_impact");
	
	public static final RegistryObject<SoundEvent> fire_minigun = registerSound("advancearmy.fire_minigun");
	public static final RegistryObject<SoundEvent> fire_auto_cannon = registerSound("advancearmy.fire_auto_cannon");
	public static final RegistryObject<SoundEvent> fire_missile = registerSound("advancearmy.fire_missile");
	public static final RegistryObject<SoundEvent> heli_move = registerSound("advancearmy.heli_move");
	public static final RegistryObject<SoundEvent> start_ah = registerSound("advancearmy.start_ah");
	public static final RegistryObject<SoundEvent> reload_missile = registerSound("advancearmy.reload_missile");
	public static final RegistryObject<SoundEvent> fire_lav = registerSound("advancearmy.fire_lav");
	public static final RegistryObject<SoundEvent> reb_say = registerSound("advancearmy.reb_say");
	public static final RegistryObject<SoundEvent> reb_hurt = registerSound("advancearmy.reb_hurt");
	public static final RegistryObject<SoundEvent> reb_die = registerSound("advancearmy.reb_die");
	public static final RegistryObject<SoundEvent> ran_say = registerSound("advancearmy.ran_say");
	public static final RegistryObject<SoundEvent> ran_hurt = registerSound("advancearmy.ran_hurt");
	public static final RegistryObject<SoundEvent> ran_die = registerSound("advancearmy.ran_die");
		
	public static final RegistryObject<SoundEvent> fire_reb = registerSound("advancearmy.fire_reb");
	public static final RegistryObject<SoundEvent> fire_ran = registerSound("advancearmy.fire_ran");
	
	public static final RegistryObject<SoundEvent> reload_mag = registerSound("advancearmy.reload_mag");//
	public static final RegistryObject<SoundEvent> fire_m2hb = registerSound("advancearmy.fire_m2hb");
	public static final RegistryObject<SoundEvent> fire_kord = registerSound("advancearmy.fire_kord");
	public static final RegistryObject<SoundEvent> fire_type85 = registerSound("advancearmy.fire_type85");
	public static final RegistryObject<SoundEvent> fire_type85_3p = registerSound("advancearmy.fire_type85_3p");
	public static final RegistryObject<SoundEvent> fire_m2hb_3p = registerSound("advancearmy.fire_m2hb_3p");
	public static final RegistryObject<SoundEvent> fire_kord_3p = registerSound("advancearmy.fire_kord_3p");
	
	public static final RegistryObject<SoundEvent> fire_jp = registerSound("advancearmy.fire_jp");
	public static final RegistryObject<SoundEvent> fire_usvg = registerSound("advancearmy.fire_usvg");
	public static final RegistryObject<SoundEvent> fire_usvg_3p = registerSound("advancearmy.fire_usvg_3p");
	public static final RegistryObject<SoundEvent> fire_ruvg = registerSound("advancearmy.fire_ruvg");
	public static final RegistryObject<SoundEvent> fire_ruvg_3p = registerSound("advancearmy.fire_ruvg_3p");
	public static final RegistryObject<SoundEvent> fire_cnvg = registerSound("advancearmy.fire_cnvg");
	public static final RegistryObject<SoundEvent> fire_cnvg_3p = registerSound("advancearmy.fire_cnvg_3p");
	
	public static final RegistryObject<SoundEvent> fire_m1a2 = registerSound("advancearmy.fire_m1a2");
	public static final RegistryObject<SoundEvent> reload_m1a2 = registerSound("advancearmy.reload_m1a2");
	public static final RegistryObject<SoundEvent> start_m1a2 = registerSound("advancearmy.start_m1a2");
	public static final RegistryObject<SoundEvent> move_track1 = registerSound("advancearmy.move_track1");
	public static final RegistryObject<SoundEvent> move_track2 = registerSound("advancearmy.move_track2");
	
	public static final RegistryObject<SoundEvent> fire_t90 = registerSound("advancearmy.fire_t90");
	public static final RegistryObject<SoundEvent> reload_t90 = registerSound("advancearmy.reload_t90");
	public static final RegistryObject<SoundEvent> start_t90 = registerSound("advancearmy.start_t90");
	
	public static final RegistryObject<SoundEvent> reload_chaingun = registerSound("advancearmy.reload_chaingun");
	public static final RegistryObject<SoundEvent> start_lav = registerSound("advancearmy.start_lav");
	public static final RegistryObject<SoundEvent> move_lav = registerSound("advancearmy.move_lav");

	public static final RegistryObject<SoundEvent> fix = registerSound("advancearmy.fix");	
	public static final RegistryObject<SoundEvent> gi_say = registerSound("advancearmy.gi_say");
	public static final RegistryObject<SoundEvent> gi_hurt = registerSound("advancearmy.gi_hurt");
	public static final RegistryObject<SoundEvent> gi_die = registerSound("advancearmy.gi_die");
	public static final RegistryObject<SoundEvent> cons_say = registerSound("advancearmy.cons_say");
	public static final RegistryObject<SoundEvent> cons_hurt = registerSound("advancearmy.cons_hurt");
	public static final RegistryObject<SoundEvent> cons_die = registerSound("advancearmy.cons_die");
	public static final RegistryObject<SoundEvent> fire_gltk = registerSound("advancearmy.fire_gltk");
	public static final RegistryObject<SoundEvent> ppsh41f = registerSound("advancearmy.ppsh41f");
    public static final RegistryObject<SoundEvent> fire_dsr80 = registerSound("advancearmy.fire_dsr80");
	public static final RegistryObject<SoundEvent> fire_para = registerSound("advancearmy.fire_para");

	public static final RegistryObject<SoundEvent> sickle_fire = registerSound("advancearmy.sickle_fire");
	public static final RegistryObject<SoundEvent> sickle_move = registerSound("advancearmy.sickle_move");
	public static final RegistryObject<SoundEvent> sickle_jump = registerSound("advancearmy.sickle_jump");
	public static final RegistryObject<SoundEvent> sickle_land = registerSound("advancearmy.sickle_land");
    private static RegistryObject<SoundEvent> registerSound(String id)
    {
		return REGISTER.register(id, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryParse(AdvanceArmy.MODID+":"+id)));
	}
}
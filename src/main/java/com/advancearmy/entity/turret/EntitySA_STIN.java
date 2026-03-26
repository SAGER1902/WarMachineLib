package advancearmy.entity.turret;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PlayMessages;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
import safx.SagerFX;
import net.minecraft.resources.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import advancearmy.entity.EntitySA_TurretBase;
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.EntitySA_Seat;

public class EntitySA_STIN extends EntitySA_TurretBase{
	public EntitySA_STIN(EntityType<? extends EntitySA_STIN> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0.63F;
		seatPosY[0] = 1.1F;
		seatPosZ[0] = 0;
		this.isturret=true;
		this.renderHudOverlay = false;
		this.renderHudIcon = false;
		this.renderHudOverlayZoom = false;
		seatHide[0] = false;
		
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/boxstin.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.hidebarrel1 = true;
		this.attack_range_max = 10;
		this.attack_height_max = 90;
		this.canlock = true;
		this.is_aa = true;
		this.w1name = "防空导弹";
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		seatTurret[0] = true;
		this.w1recoilp = 1;
		this.w1recoilr = 3;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 1F;
		this.turretPitchMax = -90;
		this.turretPitchMin = 10;
		/*this.minyaw = -75F;
		this.maxyaw = 75F;*/
		this.turretSpeed = 0.4F;
		this.ammo1=8;
		this.fireposX1 = 0;
		this.fireposY1 = 2.26F;
		this.fireposZ1 = 0.84F;
		this.firebaseY = 0;
		this.firebaseZ = 0F;
		this.w1barrelsize = 0.2F;
		this.obj = new SAObjModel("advancearmy:textures/mob/stin.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/stin.png");
		this.guntex = ResourceLocation.tryParse("advancearmy:textures/gun/fim92.png");
		this.magazine = 2;
		this.reload_time1 = 95;
		this.reloadSound1 = SASoundEvent.reload_missile.get();
		this.firesound1 = SASoundEvent.fire_stin.get();
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/towhead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/towbody.png");
		this.weaponCount = 1;
		this.w1icon="advancearmy:textures/hud/aim9x.png";
	}

	public EntitySA_STIN(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_STIN.get(), worldIn);
	}
	
	public void weaponActive1(){
		String model = "advancearmy:textures/entity/bullet/stinmissile.obj";
		String tex = "advancearmy:textures/gun/fim92.png";
		String fx1 = "SmokeGun";
		LivingEntity shooter = this;
		Entity locktarget = null;
		if(this.getFirstSeat() != null && this.getFirstSeat().mitarget!=null){
			locktarget = this.getFirstSeat().mitarget;
		}else{
			locktarget = this.getTarget();
		}
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, fx1, "SAMissileTrail", firesound1,
		1F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseY,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		50, 4, 1F, 2, false, 1, 0.01F, 100, 0);
	}
}
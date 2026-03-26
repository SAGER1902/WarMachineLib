package advancearmy.entity.land;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PlayMessages;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
import safx.SagerFX;
import net.minecraft.resources.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import advancearmy.entity.EntitySA_LandBase;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.network.chat.Component;
import advancearmy.init.ModEntities;
public class EntitySA_99G extends EntitySA_LandBase{
	public EntitySA_99G(EntityType<? extends EntitySA_99G> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 1.07F;
		seatPosY[0] = 1.1F;
		seatPosZ[0] = 2.7F;
		seatTurret[0] = true;
		seatHide[0] = true;
		seatMaxCount = 3;
		seatPosX[1] = -0.98F;
		seatPosY[1] = 3.1F;
		seatPosZ[1] = -1.4F;
		seatTurret[1] = true;
		seatPosX[2] = 1.23F;
		seatPosY[2] = 3.1F;
		seatPosZ[2] = -1.2F;
		seatTurret[2] = true;
		
		this.armor_front = 80;
		this.armor_side = 45;
		this.armor_back = 35;
		this.armor_top = 15;
		this.armor_bottom = 15;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2.4F;
		this.armor_turret_front = 80;
		this.armor_turret_side = 70;
		this.armor_turret_back = 35;
		
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/tankru.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.w1name = Component.translatable("advancearmy.weapon.125cannon.desc").getString();
		this.w2name = Component.translatable("advancearmy.weapon.762gun.desc").getString();
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.w1recoilp = 6;
		this.w1recoilr = 6;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -25;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.035F;
        this.turnSpeed = 1.25F;
		this.turretSpeed = 0.2F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(1.5F);
		this.canNightV=true;
		this.ammo1=5;
		this.ammo2=3;
		this.fireposX1 = 0;
		this.fireposY1 = 2.5F;
		this.fireposZ1 = 7.65F;
		this.fireposX2 = 0.42F;
		this.fireposY2 = 2.5F;
		this.fireposZ2 = 2.58F;
		this.firebaseX = 0;
		this.firebaseZ = 2F;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/99.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/99g.png");
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/99head.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/99body.png");
		this.magazine = 1;
		this.reload_time1 = 95;
		this.reloadSound1 = SASoundEvent.reload_98.get();
		this.firesound1 = SASoundEvent.fire_98.get();
		
		this.magazine2 = 100;
		this.reload_time2 = 95;
		this.reloadSound2 = SASoundEvent.reload_mag.get();
		this.firesound2 = SASoundEvent.fire_cnvg.get();
		
		this.startsound = SASoundEvent.start_98.get();
		this.movesound = SASoundEvent.move_track1.get();
		
		this.weaponCount = 4;
		this.w1icon="wmlib:textures/hud/heat125mm.png";
		this.w2icon="wmlib:textures/hud/7.62mm.png";
		this.w3icon="wmlib:textures/hud/cloud.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		
		this.wheelcount = 8;
		this.setWheel(0,0, 1.14F, 3.96F);
		this.setWheel(1,0, 0.68F, 3.02F);
		this.setWheel(2,0, 0.68F, 1.85F);
		this.setWheel(3,0, 0.68F, 0.64F);
		this.setWheel(4,0, 0.68F, -0.67F);
		this.setWheel(5,0, 0.68F, -1.96F);
		this.setWheel(6,0, 0.68F, -3.14F);
		this.setWheel(7,0, 1.06F, -4.24F);
	}

	public EntitySA_99G(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_99G.get(), worldIn);
	}
	
	public float turretYawO1;
	public float turretPitchO1;
	public float turretYawO2;
	public float turretPitchO2;
	
	public void tick() {
		super.tick();
		while(this.turretPitch1 - this.turretPitchO1 < -180.0F) {
			this.turretPitchO1 -= 360.0F;
		}
		while(this.turretPitch2 - this.turretPitchO2 >= 180.0F) {
			this.turretPitchO2 += 360.0F;
		}
		this.turretYawO1 = this.turretYaw1;
		this.turretPitchO1 = this.turretPitch1;
		this.turretYawO2 = this.turretYaw2;
		this.turretPitchO2 = this.turretPitch2;
		if (this.getAnySeat(1) != null){
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
			if(this.setSeat){
				String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
				String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
				String fx1 = "SmokeGun";
				String fx2 = null;
				seat.hud_box_obj = "wmlib:textures/hud/gunner.obj";
				seat.hud_box_tex = "wmlib:textures/hud/box.png";
				seat.seatPosX[0] = 0.7F;
				seat.seatProtect = 0F;
				seat.attack_range_max = 35;
				seat.attack_height_max = 75;
				seat.render_hud_box = true;
				seat.seatHide = true;
				seat.ridding_rotemgPitch = true;
				seat.w1name = Component.translatable("advancearmy.weapon.127gun.desc").getString();
				seat.weaponCount = 1;
				seat.ammo1 = 4;
				seat.magazine = 100;
				seat.reload_time1 = 80;
				seat.reloadSound1 = SASoundEvent.reload_mag.get();
				seat.firesound1_3p=SASoundEvent.fire_type85_3p.get();
				seat.setWeapon(0, 0, model, tex, fx1, fx2, SASoundEvent.fire_type85.get(), 0,1,3,0,0.38F,
				10, 6F, 1.25F, 1, false, 1, 0.01F, 20, 0);
			}
			this.turretYaw1=seat.getYHeadRot();
			if(seat.turretPitch<15)this.turretPitch1=seat.turretPitch;
			while(this.turretYaw1 - this.turretYawO1 < -180.0F) {
				this.turretYawO1 -= 360.0F;
			}
			while(this.turretPitch1 - this.turretPitchO1 >= 180.0F) {
				this.turretPitchO1 += 360.0F;
			}
			this.turretYawO1 = this.turretYaw1;
			this.turretPitchO1 = this.turretPitch1;
		}
		
		if (this.getAnySeat(2) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(2);
			if(this.setSeat){
				seat.turretSpeed = 0.6F;
				seat.render_hud_box = true;
				seat.hud_box_obj = "wmlib:textures/hud/laseraa.obj";
				seat.hud_box_tex = "wmlib:textures/hud/box.png";
				seat.renderHudOverlay = false;
				seat.seatPosZ[0] = 0.7F;
				seat.laserweapon[0] = true;
				seat.laserwidth[0] = 1;
				seat.laser_model_tex1 = "advancearmy:textures/entity/flash/99_beam";
				seat.laserfxfire1 = "LaserFlashGun";
				seat.laserfxhit1 = "LaserHit";
				seat.turretPitchMax = -75;
				seat.turretPitchMin = 10;
				seat.w1name="激光压制系统";
				seat.magazine = 100;
				seat.seatProtect = 0F;
				seat.seatHide = true;
				seat.weaponCount = 1;
				seat.ammo1 = 25;
				seat.reload_time1 = 100;
				seat.attack_range_max = 65;
				seat.turret_speed = true;
				seat.attack_height_max = 95;
				seat.reloadSound1 = SASoundEvent.reload_chaingun.get();
				seat.setWeapon(0, 2, null, null, null, null, SASoundEvent.powercannon.get(), 0F,0.8F,1F,0,0.6F,
				25, 6F, 0, 2, false, 0, 0, 15, 0);
			}
			this.turretYaw2=seat.getYHeadRot();
			if(seat.turretPitch<15)this.turretPitch2=seat.turretPitch;
		}
		
		
		if(this.getTargetType()==0){
			this.firesound2=SASoundEvent.fire_cnvg.get();
		}else{
			this.firesound2=SASoundEvent.fire_cnvg_3p.get();
		}
		if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
			if (this.getFirstSeat() != null){
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				if(seat.keyv){
					if(cooltime3>150)cooltime3=0;
					if(this.getRemain4() > 0){
						if(this.getHealth() < this.getMaxHealth() && this.getHealth() > 0.0F) {
							++healtime;
							if(healtime > 2){
								this.setHealth(this.getHealth() + 1);
								this.playSound(SASoundEvent.fix.get(), 1.0F, 1.0F);
								healtime=0;
							}
						}
					}
					if(cooltime3>80){
						this.setRemain4(0);
						seat.keyv = false;
					}
				}
				if(seat.keyx){
					if(this.getRemain3() > 0){
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("TankSmoke", null, this.getX(), this.getY(), this.getZ(), 0, 0, 0, 1);
						this.playSound(SASoundEvent.shell_impact.get(), 3.0F, 1.0F);
						this.setRemain3(0);
					}
					seat.keyx = false;
				}
			}
		}
	}

	public void weaponActive1(){
		String model = "advancearmy:textures/entity/bullet/bulletcannon.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet.png";
		String fx1 = "AdvTankFire";
		String fx2 = null;//SAAPTrail
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, fx2, firesound1,
		1F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		120, 4F, 1.1F, 4, false, 1, 0.025F, 40, 3);
	}
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/bullet.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet.png";
		String fx1 = "SmokeGun";
		String fx2 = null;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 0, model, tex, fx1, fx2, firesound2,
		1F, this.fireposX2,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		3, 6F, 1.1F, 0, false, 1, 0.01F, 20, 0);
	}
}
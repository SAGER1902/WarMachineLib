package advancearmy.entity.land;
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
import advancearmy.entity.EntitySA_LandBase;
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_Seat;

public class EntitySA_BMPT extends EntitySA_LandBase{
	public EntitySA_BMPT(EntityType<? extends EntitySA_BMPT> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0F;
		seatPosY[0] = 2.5F;
		seatPosZ[0] = -0.8F;
		this.attack_height_max = 45;
		this.canlock = true;
		seatTurret[0] = true;
		seatHide[0] = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/apc.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.w1name = "30mm2a42";
		this.w2name = "9M120";
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.setMaxUpStep(1.5F);
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -50;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.03F;
        this.turnSpeed = 1.25F;
		this.turretSpeed = 0.5F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.armor_front = 55;
		this.armor_side = 30;
		this.armor_back = 30;
		this.armor_top = 10;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 60;
		this.armor_turret_side = 60;
		this.armor_turret_back = 30;
		this.w1barrelsize = 0.5F;
		this.ammo1=6;
		this.ammo2=3;
		this.fireposX1 = 0.18F;
		this.fireposY1 = 2.95F;
		this.fireposZ1 = 1.9F;
		this.fireposX2 = 1.36F;
		this.fireposY2 = 2.95F;
		this.fireposZ2 = 1.07F;
		this.firebaseX = 0;
		this.firebaseZ = -1.13F;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/bmpthead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/bmptbody.png");
		this.obj = new SAObjModel("advancearmy:textures/mob/bmpt.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/bmpt.png");
		this.tracktex = ResourceLocation.tryParse("advancearmy:textures/mob/track.png");
		this.magazine = 100;
		this.reload_time1 = 80;
		this.reloadSound1 = SASoundEvent.reload_chaingun.get();
		this.firesound1 = SASoundEvent.fire_2a42.get();
		this.canNightV=true;
		this.magazine2 = 4;
		this.reload_time2 = 320;
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		this.firesound2 = SASoundEvent.fire_kornet.get();
		
		this.startsound = SASoundEvent.start_t90.get();
		this.movesound = SASoundEvent.move_track1.get();
		
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/2a42.png";
		this.w2icon="advancearmy:textures/hud/rocket152mm.png";
		this.w3icon="wmlib:textures/hud/cloud.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		
		this.wheelcount = 8;
		this.setWheel(0,0, 1.02F, 4.02F);
		this.setWheel(1,0, 0.57F, 3.12F);
		this.setWheel(2,0, 0.57F, 1.92F);
		this.setWheel(3,0, 0.57F, 0.71F);
		this.setWheel(4,0, 0.57F, -0.49F);
		this.setWheel(5,0, 0.57F, -1.69F);
		this.setWheel(6,0, 0.57F, -2.89F);
		this.setWheel(7,0, 1.06F, -3.9F);
	}

	public EntitySA_BMPT(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_BMPT.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_BMPT.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 500.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 15D);
    }
	
	public void tick() {
		super.tick();
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
		String model = "advancearmy:textures/entity/bullet/bullet30mm.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet.png";
		String fx1 = "SmokeGun";
		String fx2 = null;//PlaneTrail

		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, fx2, firesound1,
		1F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		20, 4F, 1.1F, 1, false, 1, 0.01F, 40, 0);
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, fx2, firesound1,
		1F, -this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		20, 4F, 1.1F, 1, false, 1, 0.01F, 40, 0);
	}
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/rocket152mm.obj";
		String tex = "advancearmy:textures/entity/bullet/rocket152mm.png";
		String fx1 = "SmokeGun";
		String fx2 = "SAMissileSmoke";
		
		float fireX = 0;
		if(this.getRemain2()%2==0){
			fireX = this.fireposX2;
		}else{
			fireX = -this.fireposX2;
		}
		
		LivingEntity shooter = this;
		Entity locktarget = null;
		if(this.getFirstSeat() != null && this.getFirstSeat().mitarget!=null){
			locktarget = this.getFirstSeat().mitarget;
		}else{
			locktarget = this.getTarget();
		}
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, fx1, fx2, firesound2,
		1F, fireX,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch-5,
		350, 2F, 1.5F, 3, false, 1, 0.01F, 150, 0);
	}
}
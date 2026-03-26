package advancearmy.entity.land;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.network.chat.Component;
public class EntitySA_BMP2 extends EntitySA_LandBase{
	public EntitySA_BMP2(EntityType<? extends EntitySA_BMP2> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0.6F;
		seatPosY[0] = 1.1F;
		seatPosZ[0] = 1.2F;
		
		seatMaxCount = 7;
		
		/*seatPosX[1] = 0.81F;
		seatPosY[1] = 0.5F;
		seatPosZ[1] = 2F;*/
		
		seatPosX[2] = -0.74F;
		seatPosY[2] = 0.3F;
		seatPosZ[2] = -1.2F;
		
		seatPosX[3] = -0.74F;
		seatPosY[3] = 0.3F;
		seatPosZ[3] = -1.68F;
		
		seatPosX[4] = -0.74F;
		seatPosY[4] = 0.3F;
		seatPosZ[4] = -2.17;
		
		seatPosX[5] = 0.74F;
		seatPosY[5] = 0.3F;
		seatPosZ[5] = -1.2F;
		
		seatPosX[6] = 0.74F;
		seatPosY[6] = 0.3F;
		seatPosZ[6] = -1.68F;
		
		seatPosX[1] = 0.74F;
		seatPosY[1] = 0.3F;
		seatPosZ[1] = -2.17;
		
		this.canlock = true;
		this.armor_front = 30;
		this.armor_side = 10;
		this.armor_back = 10;
		this.armor_top = 10;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 1.9F;
		this.armor_turret_front = 30;
		this.armor_turret_side = 10;
		this.armor_turret_back = 10;
		this.attack_height_max = 45;
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
		this.canNightV=true;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -50;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.04F;
        this.turnSpeed = 1.25F;
		this.turretSpeed = 0.5F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(1.5F);
		this.w1barrelsize = 0.5F;
		this.ammo1=8;
		this.ammo2=3;
		this.fireposX1 = 0F;
		this.fireposY1 = 2.18F;
		this.fireposZ1 = 3.3F;
		this.fireposX2 = 0.07F;
		this.fireposY2 = 2.81F;
		this.fireposZ2 = 0.55F;
		this.firebaseX = 0;
		this.firebaseZ = 0.99F;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/bmp2head.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/bmp2body.png");
		this.obj = new SAObjModel("advancearmy:textures/mob/bmp2.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/bmp2.png");
		this.magazine = 100;
		this.reload_time1 = 80;
		this.reloadSound1 = SASoundEvent.reload_chaingun.get();
		this.firesound1 = SASoundEvent.fire_2a42.get();
		
		this.magazine2 = 1;
		this.reload_time2 = 250;
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		this.firesound2 = SASoundEvent.fire_kornet.get();
		this.soundspeed=0.7F;
		this.startsound = SASoundEvent.start_t90.get();
		this.movesound = SASoundEvent.move_track2.get();
		canWater=true;
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/2a42.png";
		this.w2icon="advancearmy:textures/hud/rocket152mm.png";
		this.w3icon="wmlib:textures/hud/cloud.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		
		this.wheelcount = 8;
		this.setWheel(0,0, 0.6F, 3.18F);
		this.setWheel(1,0, 0.39F, 2.47F);
		this.setWheel(2,0, 0.39F, 1.65F);
		this.setWheel(3,0, 0.39F, 0.84F);
		this.setWheel(4,0, 0.39F, 0.02F);
		this.setWheel(5,0, 0.39F, -0.79F);
		this.setWheel(6,0, 0.39F, -1.61F);
		this.setWheel(7,0, 0.61F, -2.46F);
	}

	public EntitySA_BMP2(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_BMP2.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_BMP2.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 250.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 7D);
    }
	
	public void tick() {
		super.tick();
		if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
			{
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

		double b = 0;
		double a = 0;
		double ax = 0;
		double az = 0;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, fx2, firesound1,
		1F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseX,this.firebaseZ,
		ax + this.getX(), this.getY()+b, az + this.getZ(),this.turretYaw, this.turretPitch,
		20, 4F, 1.1F, 1, false, 1, 0.01F, 40, 3);
	}
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/rocket152mm.obj";
		String tex = "advancearmy:textures/entity/bullet/rocket152mm.png";
		String fx1 = "SmokeGun";
		String fx2 = "SAMissileSmoke";
		double b = 0;
		double a = 0;
		double ax = 0;
		double az = 0;
		LivingEntity shooter = this;
		Entity locktarget = null;
		if(this.getFirstSeat() != null && this.getFirstSeat().mitarget!=null){
			locktarget = this.getFirstSeat().mitarget;
		}else{
			locktarget = this.getTarget();
		}
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, fx1, fx2, firesound2,
		1F, this.fireposX2,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		ax + this.getX(), this.getY()+b, az + this.getZ(),this.turretYaw, this.turretPitch,
		400, 5F, 1.5F, 4, false, 1, 0.01F, 150, 1);
	}
}
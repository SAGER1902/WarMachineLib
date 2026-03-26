package advancearmy.entity.land;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PlayMessages;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.Attributes;
import safx.SagerFX;
import net.minecraft.resources.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import advancearmy.entity.EntitySA_LandBase;
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
public class EntitySA_MMTank extends EntitySA_LandBase{
	public EntitySA_MMTank(EntityType<? extends EntitySA_MMTank> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0F;
		seatPosY[0] = 2.6F;
		seatPosZ[0] = 0.8F;
		this.attack_height_max = 115;
		this.attack_range_max = 50;
		this.canlock = true;
		seatTurret[0] = true;
		seatHide[0] = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/tankru.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.w1name = "120mm Cannon x 2";
		this.w2name = "MammothTeeth Missile";
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.setMaxUpStep(1.5F);
		this.canNightV=true;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -90;
		this.turretPitchMin = 10;
		
		this.w1pitchlimit = -15;
		
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
		this.is_aa = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 60;
		this.armor_turret_side = 60;
		this.armor_turret_back = 30;
		this.w1barrelsize = 0.5F;
		this.ammo1=12;
		this.ammo2=18;
		this.fireposX1 = 0.91F;
		this.fireposY1 = 2.61F;
		this.fireposZ1 = 7.35F;
		this.fireposX2 = 2.13F;
		this.fireposY2 = 3.85F;
		this.fireposZ2 = -0.78F;
		this.firebaseX = 0;
		this.firebaseZ = 1.7F;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/mmhead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/mmbody.png");
		this.obj = new SAObjModel("advancearmy:textures/mob/mmtank.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/mmtank.png");
		this.tracktex = ResourceLocation.tryParse("advancearmy:textures/mob/track.png");
		this.magazine = 2;
		this.reload_time1 = 80;
		this.reloadSound1 = SASoundEvent.reload_t90.get();
		this.firesound1 = SASoundEvent.fire_t90.get();
		
		this.magazine2 = 12;
		this.reload_time2 = 300;
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		this.firesound2 = SASoundEvent.fire_kornet.get();
		
		this.startsound = SASoundEvent.start_t90.get();
		this.movesound = SASoundEvent.move_track1.get();
		
		this.weaponCount = 4;
		this.w1icon="wmlib:textures/hud/heat120mm.png";
		this.w2icon="advancearmy:textures/hud/rocket152mm.png";
		this.w3icon="wmlib:textures/hud/cloud.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		
		this.wheelcount = 4;
		this.setWheel(0,0, 0.82F, 3.77F);
		this.setWheel(1,0, 0.82F, 1.96F);
		this.setWheel(2,0, 0.82F, -0.4F);
		this.setWheel(3,0, 0.82F, -2.37F);
	}

	public EntitySA_MMTank(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_MMTANK.get(), worldIn);
	}
	
	public void tick() {
		super.tick();
		if(this.anim1>9){
			if(this.getRemain1()%2==0){
				this.fireposX1 = 0.91F;
			}else{
				this.fireposX1 = -0.91F;
			}
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
		float pitch = this.turretPitch;
		if(pitch<this.w1pitchlimit)pitch=this.w1pitchlimit;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, fx2, firesound1,
		1F, -this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, pitch,
		80, 4F, 1.1F, 4, false, 1, 0.01F, 40, 0);
	}
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/bulletrocket.obj";
		String tex = "advancearmy:textures/entity/bullet/bulletrocket.png";
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
		45, 4F, 1.5F, 2, false, 1, 0.1F, 250, 0);
	}
}
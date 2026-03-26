package advancearmy.entity.air;
import advancearmy.init.ModEntities;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
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
import advancearmy.entity.EntitySA_HeliBase;
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
public class EntitySA_Yw010 extends EntitySA_HeliBase{
	public EntitySA_Yw010(EntityType<? extends EntitySA_Yw010> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatTurret[1] = false;
		VehicleType = 3;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/yw010icon.png");
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.canNightV=true;
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-10F;
		seatMaxCount = 8;
		seatPosX[0] = 0;
		seatPosY[0] = 0.85F;
		seatPosZ[0] = 0;
		seatPosX[1] = -1.6F;
		seatPosY[1] = -0.1F;
		seatPosZ[1] = -1.9F;
		seatPosX[2] = 1.6F;
		seatPosY[2] = -0.1F;
		seatPosZ[2] = -1.9F;
		seatPosX[3] = -0.8F;
		seatPosY[3] = 0.73F;
		seatPosZ[3] = -2.55F;
		seatPosX[4] = 0.8F;
		seatPosY[4] = 0.4F;
		seatPosZ[4] = -2.55F;
		
		seatCanFire[3]=true;
		seatCanFire[4]=true;
		seatCanFire[5]=true;
		seatCanFire[6]=true;
		this.canNightV=true;
		seatPosX[5] = -0.8F;
		seatPosY[5] = 0.73F;
		seatPosZ[5] = -5.1F;
		seatPosX[6] = 0.8F;
		seatPosY[6] = 0.73F;
		seatPosZ[6] = -5.1F;
		seatPosX[7] = 0F;
		seatPosY[7] = 0.73F;
		seatPosZ[7] = -5.1F;
		
        this.MoveSpeed = 0.032F;
        this.turnSpeed = 2.1F;
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
		this.magazine = 200;
		this.reload_time1 = 380;
		this.reloadSound1 = SASoundEvent.reload_missile.get();
		this.magazine3 = 20;
		this.reload_time3 = 300;
		this.startsound = SASoundEvent.start_ah.get();
		this.movesound = SASoundEvent.heli_move.get();
		this.firesound1 = SASoundEvent.gun14.get();
		this.ammo1=5;
		this.ammo2=5;
		this.fireposX1 = 0.06F;
		this.fireposY1 = 0.62F;
		this.fireposZ1 = -1.2F;
		this.firebaseX = 0;
		this.firebaseZ = 0;
		this.change_roter = false;
		
		this.w1aim =360;
		this.isSpaceShip = true;
		this.can_follow= true;
		this.rotorcount = 2;
		this.rotor_rotey[0]=5;
		this.rotor_rotey[1]=5;
		this.setRotor(0,-4.02F, 3.42F, -3.91F);
		this.setRotor(1,4.02F, 3.42F, -3.91F);
		this.weaponCount = 3;
		this.w1icon="advancearmy:textures/hud/yw010w1.png";
		this.w3icon="advancearmy:textures/hud/dun2.png";
		this.w4icon="wmlib:textures/hud/repair.png";
	}
	public EntitySA_Yw010(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_YW010.get(), worldIn);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Yw010.createMobAttributes();
    }
	
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if (this.getAnySeat(1) != null){//机枪位1
				EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
				seat.minyaw = -15F;
				seat.maxyaw = 190F;
				seat.canNightV=true;
				seat.gunner_aim=true;

				this.seatWeapon1(seat);
				this.turretYaw1=seat.getYHeadRot();
				this.turretPitch1=seat.turretPitch;
				if(seat.getRemain1()>0){
					if(seat.fire1)this.setAnimFire(2);
				}
			}
			if (this.getAnySeat(2) != null){//机枪位2
				EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(2);
				seat.minyaw = -190F;
				seat.maxyaw = 15F;
				seat.canNightV=true;
				seat.gunner_aim=true;

				this.seatWeapon1(seat);
				this.turretYaw2=seat.getYHeadRot();
				this.turretPitch2=seat.turretPitch;
				if(seat.getRemain1()>0){
					if(seat.fire1)this.setAnimFire(3);
				}
			}
		}
	}
	public void weaponActive1(){
		float fireX = 0;
		if(this.getRemain1()%2==0){
			fireX = this.fireposX1;
		}else{
			fireX = -this.fireposX1;
		}
		String model = "advancearmy:textures/entity/bullet/bullet30mm.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet.png";
		String fx1 = "SmokeGun";
		String fx2 = "YellowBulletTrail";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, fx2, firesound1,
		1F, fireX,this.fireposY1,this.fireposZ1,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		30, 4F, 1.1F, 1, false, 1, 0.001F, 50, 3);
	}
	public void seatWeapon1(EntitySA_Seat seat){
		seat.attack_range_max = 50;
		seat.attack_height_max = 10;
		seat.attack_height_min = -90;
		//seat.hudfollow = true;
		seat.render_hud_box = true;
		seat.hud_box_obj = "wmlib:textures/hud/gunner.obj";
		seat.hud_box_tex = "wmlib:textures/hud/box.png";
		seat.w1icon="advancearmy:textures/hud/gun17.png";
		seat.seatHide = true;
		seat.weaponCount = 1;
		seat.ammo1 = 3;
		seat.w1name = "机载型旋风机枪";
		seat.magazine = 200;
		seat.reload_time1 = 80;
		seat.reloadSound1 = SASoundEvent.reload_chaingun.get();
		String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
		String fx1 = "SmokeGun";
		String fx2 = "YellowBulletTrail";
		seat.setWeapon(0, 0, model, tex, fx1, fx2, SASoundEvent.gun4.get(), 0,1F,2.5F,0,0.2F,
		10, 4F, 1.25F, 1, false, 1, 0.01F, 20, 0);
	}
}
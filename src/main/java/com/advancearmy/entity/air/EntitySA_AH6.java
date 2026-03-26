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
public class EntitySA_AH6 extends EntitySA_HeliBase{
	public EntitySA_AH6(EntityType<? extends EntitySA_AH6> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatTurret[1] = false;
		VehicleType = 3;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/ah6icon.png");
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		this.armor_front = 5;
		this.armor_side = 5;
		this.armor_back = 5;
		this.armor_top = 5;
		this.armor_bottom = 5;
		
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-10F;
		seatMaxCount = 4;
		seatPosX[0] = 0.37F;
		seatPosY[0] = 1F;
		seatPosZ[0] = 0.3F;
		seatPosX[1] = -0.37F;
		seatPosY[1] = 1F;
		seatPosZ[1] = 0.3F;
		seatPosX[2] = -0.37F;
		seatPosY[2] = 1.1F;
		seatPosZ[2] = -1F;
		
		seatCanFire[2]=true;
		seatCanFire[3]=true;
		seatPosX[3] = 0.37F;
		seatPosY[3] = 1.1F;
		seatPosZ[3] = -1F;

        this.w2aa = false;
        this.w2max = 100;
        this.w2min = 5;
        this.w2aim = 10;

        this.MoveSpeed = 0.035F;
        this.turnSpeed = 2.5F;
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
		this.magazine = 500;
		this.reload_time1 = 380;
		this.reloadSound1 = SASoundEvent.reload_chaingun.get();
		
		this.magazine2 = 12;
		this.reload_time2 = 300;
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		this.magazine3 = 20;
		this.reload_time3 = 300;
		this.startsound = SASoundEvent.start_ah.get();
		this.movesound = SASoundEvent.heli_move.get();
		
		this.firesound1 = SASoundEvent.fire_minigun.get();
		this.firesound2 = SASoundEvent.fire_missile.get();
		
		this.ammo1=1;
		this.ammo2=5;
		this.fireposX1 = 1.4F;
		this.fireposY1 = 0.67F;
		this.fireposZ1 = 0.2F;
		this.fireposX2 = 2F;
		this.fireposY2 = 0.63F;
		this.fireposZ2 = 0.4F;
		this.firebaseX = 0;
		this.firebaseZ = 0;

		this.rotorcount = 2;
		this.rotor_rotey[0]=10;
		this.rotor_rotex[1]=10;
		this.setRotor(0,0, 0, -1.03F);
		this.setRotor(1,0, 2.72F, -7.34F);
		this.rotortex1 = ResourceLocation.tryParse("advancearmy:textures/mob/ah6rotor.png");
		this.rotortex2 = ResourceLocation.tryParse("advancearmy:textures/mob/ah6rotor2.png");
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/m134.png";
		this.w2icon="advancearmy:textures/hud/hy70.png";
		this.w3icon="wmlib:textures/hud/flare.png";
		this.w4icon="wmlib:textures/hud/repair.png";
	}
	public EntitySA_AH6(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_AH6.get(), worldIn);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_AH6.createMobAttributes();
    }
	
	public void weaponActive1(){
		String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
		String fx1 = "SmokeGun";
		String fx2 = null;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 0, model, tex, fx1, fx2, firesound1,
		1.57F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseY,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		5, 4F, 3F, 1, false, 1, 0.001F, 50, 0);
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 0, model, tex, fx1, fx2, firesound1,
		-1.57F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseY,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		8, 4F, 3F, 1, false, 1, 0.001F, 50, 0);
	}
	public void weaponActive2(){
		float side = 1.57F;
		if(this.getRemain2()%2==0){
			side = 1.57F;
		}else{
			side = -1.57F;
		}
		String model = "advancearmy:textures/entity/bullet/hy70.obj";
		String tex = "advancearmy:textures/entity/bullet/hy70.png";
		String fx1 = "SmokeGun";
		String fx2 = "SAMissileTrail";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, fx2, firesound2,
		side, this.fireposX2,this.fireposY2,this.fireposZ2,this.firebaseY,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		45, 3F, 1.1F, 2, false, 1, 0.001F, 50, 3);
	}
}
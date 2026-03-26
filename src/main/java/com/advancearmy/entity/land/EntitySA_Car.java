package advancearmy.entity.land;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
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
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.network.chat.Component;
public class EntitySA_Car extends EntitySA_LandBase{
	public EntitySA_Car(EntityType<? extends EntitySA_Car> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0.58F;
		seatPosY[0] = 1.1F;
		seatPosZ[0] = 0.1F;
		seatHide[0] = false;
		seatMaxCount = 5;
		seatPosX[2] = -0.58F;
		seatPosY[2] = 1.1F;
		seatPosZ[2] = 0.1F;
		
		seatPosX[1] = 0F;
		seatPosY[1] = 2F;
		seatPosZ[1] = -2.1F;
		
		seatPosX[3] = 0.54F;
		seatPosY[3] = 1.43F;
		seatPosZ[3] = -1.5F;
		
		seatPosX[4] = -0.54F;
		seatPosY[4] = 1.43F;
		seatPosZ[4] = -1.5F;
		canBreakLog=false;
		seatCanFire[3]=true;
		seatCanFire[4]=true;
		this.seatNoThird = true;
		this.noturret=true;
		this.seatProtect = 0.8F;
        this.MoveSpeed = 0.04F;
        this.turnSpeed = 1.25F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(1.5F);
		this.VehicleType = 2;
		this.obj = new SAObjModel("advancearmy:textures/mob/car.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/car.png");
		this.mgobj = new SAObjModel("advancearmy:textures/mob/kord2.obj");
		this.mgtex = ResourceLocation.tryParse("advancearmy:textures/mob/kord.png");
		this.startsound = SASoundEvent.start_vodnik.get();
		this.movesound = SASoundEvent.move_lav.get();
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/carbody.png");
		this.setMg(0F, 3F, -2.1F, 0.18F);
		this.wheelcount = 4;
		this.wheelturn[0]=true;
		this.setWheel(0,1.18F, 0.59F, 2.09F);
		this.wheelturn[1]=true;
		this.setWheel(1,-1.18F, 0.59F, 2.09F);
		this.setWheel(2,0, 0.59F, -2.36F);
	}

	public EntitySA_Car(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_CAR.get(), worldIn);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Car.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 100.0D)
					.add(Attributes.FOLLOW_RANGE, 30.0D)
					.add(Attributes.ARMOR, (double) 3D);
	}
	
	
	public void tick() {
		super.tick();
		if (this.getAnySeat(1) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
			if(this.setSeat){
				String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
				String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
				String fx1 = "SmokeGun";
				String fx2 = null;
				seat.seatProtect = 0.8F;
				seat.seatHide = false;
				seat.ridding_rotemgPitch = true;
				seat.attack_range_max = 35;
				seat.weaponCount = 1;
				seat.stand = true;
				seat.w1name = Component.translatable("advancearmy.weapon.127gun.desc").getString();
				seat.ammo1 = 4;
				seat.magazine = 100;
				seat.reload_time1 = 80;
				seat.seatPosZ[0] = -0.8F;
				seat.reloadSound1 = SASoundEvent.reload_mag.get();
				seat.firesound1_3p=SASoundEvent.fire_kord_3p.get();
				seat.setWeapon(0, 0, model, tex, fx1, fx2, SASoundEvent.fire_kord.get(), 0,1,3,0,0.38F,
				10, 6F, 1.25F, 1, false, 1, 0.01F, 20, 0);
			}
			//render
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
			
			if(seat.getRemain1()>0){
				ammo = true;
				if(seat.fire1){
					if(count<2){
						++count;
					}else{
						count = 0;
					}
				}
			}else{
				ammo = false;
			}
			if(seat.getRemain2()>0){
				if(count<2){
					++count;
				}else{
					count = 0;
				}
			}
		}
	}
}
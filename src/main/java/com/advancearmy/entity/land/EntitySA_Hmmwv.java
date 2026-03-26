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
public class EntitySA_Hmmwv extends EntitySA_LandBase{
	public EntitySA_Hmmwv(EntityType<? extends EntitySA_Hmmwv> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0.85F;
		seatPosY[0] = 1F;
		seatPosZ[0] = 0.25F;
		seatMaxCount = 5;
		seatPosX[2] = -0.85F;
		seatPosY[2] = 1F;
		seatPosZ[2] = 0.25F;
		seatPosX[1] = 0;
		seatPosY[1] = 2.2F;
		seatPosZ[1] = -0.36F;
		
		seatPosX[3] = 0.52F;
		seatPosY[3] = 1.3F;
		seatPosZ[3] = -2.6F;
		
		seatPosX[4] = -0.52F;
		seatPosY[4] = 1.3F;
		seatPosZ[4] = -2.6F;
		seatCanFire[3]=true;
		seatCanFire[4]=true;
		canBreakLog=false;
		this.armor_front = 8;
		this.armor_side = 8;
		this.armor_back = 8;
		this.armor_top = 8;
		this.armor_bottom = 8;
		this.seatNoThird = true;
		this.noturret=true;
		this.seatProtect = 0.7F;
        this.MoveSpeed = 0.04F;
        this.turnSpeed = 1.3F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(1.5F);
		this.VehicleType = 2;
		this.obj = new SAObjModel("advancearmy:textures/mob/hmmwv.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/hmmwv.png");
		this.mgobj = new SAObjModel("advancearmy:textures/mob/m2hb.obj");
		this.mgtex = ResourceLocation.tryParse("advancearmy:textures/mob/m2hb.png");
		this.startsound = SASoundEvent.start_hmmwv.get();
		this.movesound = SASoundEvent.move_lav.get();
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/hmmwvbody.png");
		this.setMg(0F, 3F, -0.36F, 0.18F);
		this.wheelcount = 4;
		this.wheelturn[0]=true;
		this.setWheel(0,1.2F, 0.6F, 2.08F);
		this.wheelturn[1]=true;
		this.setWheel(1,-1.2F, 0.6F, 2.08F);
		this.setWheel(2,0, 0.6F, -2.51F);
	}

	public EntitySA_Hmmwv(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_HMMWV.get(), worldIn);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Hmmwv.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 120.0D)
					.add(Attributes.FOLLOW_RANGE, 30.0D)
					.add(Attributes.ARMOR, (double) 4D);
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
				seat.w1name = Component.translatable("advancearmy.weapon.127gun.desc").getString();
				seat.ammo1 = 4;
				seat.magazine = 100;
				seat.reload_time1 = 80;
				seat.reloadSound1 = SASoundEvent.reload_mag.get();
				seat.firesound1_3p=SASoundEvent.fire_m2hb_3p.get();
				seat.setWeapon(0, 0, model, tex, fx1, fx2, SASoundEvent.fire_m2hb.get(), 0,1,3,0,0.38F,
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
package advancearmy.entity.land;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import advancearmy.init.ModEntities;
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
public class EntitySA_FTK extends EntitySA_LandBase{
	public EntitySA_FTK(EntityType<? extends EntitySA_FTK> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 1.07F;
		seatPosY[0] = 1.1F;
		seatPosZ[0] = 2.7F;
		seatTurret[0] = true;
		seatHide[0] = true;
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
		this.fireposY1 = 2.42F;
		this.fireposZ1 = 7.33F;
		this.fireposX2 = 0.51F;
		this.fireposY2 = 2.42F;
		this.fireposZ2 = 2.26F;
		this.firebaseX = 0;
		this.firebaseZ = 1.93F;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/ftk_new.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/ftk_new.png");
		this.tracktex = ResourceLocation.tryParse("advancearmy:textures/mob/track.png");
		this.magazine = 1;
		this.reload_time1 = 95;
		this.reloadSound1 = SASoundEvent.reload_t90.get();
		this.firesound1 = SASoundEvent.fire_t90.get();
		
		this.magazine2 = 100;
		this.reload_time2 = 95;
		this.reloadSound2 = SASoundEvent.reload_mag.get();
		this.firesound2 = SASoundEvent.fire_ruvg.get();
		
		this.startsound = SASoundEvent.start_t90.get();
		this.movesound = SASoundEvent.move_track1.get();
		
		this.weaponCount = 4;
		this.w1icon="wmlib:textures/hud/heat120mm.png";
		this.w2icon="wmlib:textures/hud/7.62mm.png";
		this.w3icon="wmlib:textures/hud/cloud.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		
		this.wheelcount = 8;
		this.setWheel(0,0, 0.73F, 3.2F);
		this.setWheel(1,0, 0.46F, 2.37F);
		this.setWheel(2,0, 0.46F, 1.48F);
		this.setWheel(3,0, 0.46F, 0.53F);
		this.setWheel(4,0, 0.46F, -0.37F);
		this.setWheel(5,0, 0.46F, -1.33F);
		this.setWheel(6,0, 0.46F, -2.23F);
		this.setWheel(7,0, 0.84F, -3.14F);
	}

	public EntitySA_FTK(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_FTK.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_FTK.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 550.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 16D);
    }
	
	public void tick() {
		super.tick();
		/*if (this.getAnySeat(1) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
			String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
			String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
			String fx1 = "SmokeGun";
			String fx2 = null;
			seat.seatProtect = 0.8F;
			seat.attack_range_max = 35;
			seat.seatHide = false;
			seat.ridding_rotemgPitch = true;
			seat.weaponCount = 1;
			seat.ammo1 = 4;
			seat.magazine = 100;
			seat.reload_time1 = 80;
			seat.reloadSound1 = SASoundEvent.reload_mag.get();
			seat.firesound1_3p=SASoundEvent.fire_kord_3p.get();
			seat.setWeapon(0, 0, model, tex, fx1, fx2, SASoundEvent.fire_kord.get(), 0,1,3,0,0.38F,
			10, 6F, 1.25F, 1, false, 1, 0.01F, 20, 0);
			
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
		}*/
		
		if(this.getTargetType()==0){
			this.firesound2=SASoundEvent.fire_ruvg.get();
		}else{
			this.firesound2=SASoundEvent.fire_ruvg_3p.get();
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
		60, 4F, 1.1F, 4, false, 1, 0.025F, 40, 3);
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
		3, 6F, 1.5F, 0, false, 1, 0.01F, 20, 0);
	}
}
package advancearmy.entity.air;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import net.minecraft.network.chat.Component;
public class EntitySA_MI24 extends EntitySA_HeliBase{
	
	public EntitySA_MI24(EntityType<? extends EntitySA_MI24> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0;
		seatPosY[0] = 1.5F;
		seatPosZ[0] = 0;
		seatPosX[1] = 0;
		seatPosY[1] = 0.6F;
		seatPosZ[1] = 1.8F;
		this.canNightV=true;
		seatPosX[2] = 0.8F;
		seatPosY[2] = 1.1F;
		seatPosZ[2] = -2.63F;
		seatPosX[3] = -0.8F;
		seatPosY[3] = 1.1F;
		seatPosZ[3] = -2.63F;
		
		seatCanFire[2]=true;
		seatCanFire[3]=true;
		
		seatPosX[4] = 0.7F;
		seatPosY[4] = 1F;
		seatPosZ[4] = -3.88F;
		seatPosX[5] = -0.7F;
		seatPosY[5] = 1F;
		seatPosZ[5] = -3.88F;
		
		seatPosX[6] = 0.7F;
		seatPosY[6] = 1F;
		seatPosZ[6] = -4.48F;
		seatPosX[7] = -0.7F;
		seatPosY[7] = 1F;
		seatPosZ[7] = -4.48F;
		VehicleType = 3;
		seatTurret[1] = false;
		this.canlock = true;
		this.is_aa = false;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/line.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/mi24icon.png");
		this.armor_front = 10;
		this.armor_side = 9;
		this.armor_back = 9;
		this.armor_top = 9;
		this.armor_bottom = 9;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-10F;
		seatMaxCount = 8;
        this.MoveSpeed = 0.028F;
        this.turnSpeed = 2F;
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
		
        this.w2aa = false;
        this.w2max = 100;
        this.w2min = 5;
        this.w2aim = 20;
		
		this.magazine = 24;
		this.reload_time1 = 400;
		this.reloadSound1 = SASoundEvent.reload_missile.get();
		
		this.magazine2 = 4;
		this.reload_time2 = 400;
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		
		this.magazine3 = 20;
		this.reload_time3 = 200;
		
		this.startsound = SASoundEvent.start_ah.get();
		this.movesound = SASoundEvent.heli_move.get();
		
		this.firesound1 = SASoundEvent.fire_missile.get();
		this.firesound2 = SASoundEvent.fire_kornet.get();
		
		this.ammo1=5;
		this.ammo2=45;
		this.fireposX1 = 2.47F;
		this.fireposY1 = 1.3F;
		this.fireposZ1 = -4F;
		this.fireposX2 = 4.13F;
		this.fireposY2 = 0.9F;
		this.fireposZ2 = -4F;
		this.firebaseX = 0;
		this.firebaseZ = 0;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/mi24.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/mi24.png");
		
		this.mgobj = new SAObjModel("advancearmy:textures/mob/gsh30.obj");
		this.rotortex1 = ResourceLocation.tryParse("advancearmy:textures/mob/mi24rotor.png");
		this.rotortex2 = ResourceLocation.tryParse("advancearmy:textures/mob/mi24rotor2.png");

		this.setMg(0F, 0.5F, 2F, 0.21F);
		this.rotorcount = 2;
		this.rotor_rotey[0]=10;
		this.rotor_rotex[1]=10;
		this.setRotor(0,0, 5.23F, -4.13F);
		this.setRotor(1,1.02F, 5.40F, -17.39F);
		
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/s8.png";
		this.w2icon="advancearmy:textures/hud/9k121.png";
		this.w3icon="wmlib:textures/hud/flare.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		this.w1name = Component.translatable("advancearmy.weapon.s8.desc").getString();
		this.w2name = Component.translatable("advancearmy.weapon.9k121.desc").getString();
		this.w4name = Component.translatable("advancearmy.weapon.fix.desc").getString();
	}
	
	public EntitySA_MI24(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_MI24.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_MI24.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 350.0D)
					.add(Attributes.FOLLOW_RANGE, 75.0D)
					.add(Attributes.ARMOR, (double) 9D);
    }
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if (this.getAnySeat(1) != null){//
				EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
				if(this.setSeat){
					String model = "advancearmy:textures/entity/bullet/bullet30mm.obj";
					String tex = "advancearmy:textures/entity/bullet/bullet.png";
					String fx1 = "SmokeGun";
					String fx2 = null;
					seat.seatProtect = 0.2F;
					seat.attack_range_max = 50;
					seat.attack_height_max = 10;
					seat.attack_height_min = -90;
					seat.render_hud_box = true;
					seat.w1name=Component.translatable("advancearmy.weapon.gsh30_1.desc").getString();
					seat.w1icon="advancearmy:textures/hud/gsh30.png";
					seat.hud_box_obj = "wmlib:textures/hud/gunner.obj";
					seat.hud_box_tex = "wmlib:textures/hud/box.png";
					seat.seatHide = false;
					
					seat.gunner_aim=true;
					seat.turretPitchMax = 0;
					seat.minyaw=-90;
					seat.maxyaw=90;
					seat.canNightV=true;
					seat.weaponCount = 1;
					seat.ammo1 = 4;
					seat.magazine = 200;
					seat.reload_time1 = 100;
					seat.reloadSound1 = SASoundEvent.reload_chaingun.get();
					seat.reloadSound2 = SASoundEvent.reload_missile.get();
					seat.setWeapon(0, 3, model, tex, fx1, fx2, SASoundEvent.fire_2a42.get(), 0,-1,2,0,0.38F,
					25, 6F, 1.25F, 1, false, 1, 0.01F, 20, 0);
				}
				this.turretYaw1=seat.getYHeadRot();
				this.turretPitch1=seat.turretPitch;
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
		String model = "advancearmy:textures/entity/bullet/hy70.obj";
		String tex = "advancearmy:textures/entity/bullet/hy70.png";
		String fx1 = "SmokeGun";
		String fx2 = "SAMissileTrail";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, fx2, firesound1,
		1F, fireX,this.fireposY1,this.fireposZ1,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		55, 3F, 1.1F, 2, false, 1, 0.001F, 50, 3);
	}
	public void weaponActive2(){
		float fireX = 0;
		if(this.getRemain2()%2==0){
			fireX = this.fireposX2;
		}else{
			fireX = -this.fireposX2;
		}
		String model = "advancearmy:textures/entity/bullet/aim9x.obj";
		String tex = "advancearmy:textures/entity/bullet/aim9x.png";

		String fx2 = "SAMissileSmoke";
		LivingEntity shooter = this;
		
		
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		Entity locktarget = null;
		if(this.getFirstSeat() != null && this.getFirstSeat().mitarget!=null){
			locktarget = this.getFirstSeat().mitarget;
		}else{
			locktarget = this.getTarget();
		}
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, null, fx2, firesound2,
		1F, fireX,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		235, 3F, 1.5F, 2, false, 1, 0.01F, 250, 0);
	}
}
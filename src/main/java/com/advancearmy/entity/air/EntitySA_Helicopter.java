package advancearmy.entity.air;
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
import advancearmy.init.ModEntities;
public class EntitySA_Helicopter extends EntitySA_HeliBase{
    /*protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYaw;
    protected double lerpPitch;*/
	public EntitySA_Helicopter(EntityType<? extends EntitySA_Helicopter> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0;
		seatPosY[0] = 0.8F;
		seatPosZ[0] = 0;
		seatPosX[1] = 0;
		seatPosY[1] = 0.4F;
		seatPosZ[1] = 1.7F;
		seatTurret[1] = false;
		this.canlock = true;
		this.is_aa = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/line.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.armor_front = 8;
		this.armor_side = 8;
		this.armor_back = 8;
		this.armor_top = 8;
		this.armor_bottom = 8;
		VehicleType = 3;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/ah64icon.png");
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.canNightV=true;
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-10F;
		seatMaxCount = 2;
        this.MoveSpeed = 0.032F;
        this.turnSpeed = 2.1F;
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
	
		this.magazine = 16;
		this.reload_time1 = 380;
		this.reloadSound1 = SASoundEvent.reload_missile.get();
		
		this.magazine2 = 4;
		this.reload_time2 = 150;
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		
		this.magazine3 = 20;
		this.reload_time3 = 300;
		
		this.startsound = SASoundEvent.start_ah.get();
		this.movesound = SASoundEvent.move_ah64.get();
		
		this.firesound1 = SASoundEvent.fire_missile.get();
		this.firesound2 = SASoundEvent.fire_missile.get();
		
		this.ammo1=5;
		this.ammo2=5;
		this.fireposX1 = 2.13F;
		this.fireposY1 = 0.62F;
		this.fireposZ1 = -1.2F;
		this.fireposX2 = 1.47F;
		this.fireposY2 = 0.69F;
		this.fireposZ2 = -0.8F;
		this.firebaseX = 0;
		this.firebaseZ = 0;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/ah64.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/ah64.png");
		
		this.mgobj = new SAObjModel("advancearmy:textures/mob/30mm.obj");
		this.rotortex1 = ResourceLocation.tryParse("advancearmy:textures/mob/ah64rotor.png");
		this.rotortex2 = ResourceLocation.tryParse("advancearmy:textures/mob/ah64rotor2.png");
		
		this.setMg(0F, -0.07F, 1.23F, 0F);
		this.rotorcount = 2;
		this.rotor_rotey[0]=10;
		this.rotor_rotex[1]=10;
		this.setRotor(0,0, 3.2F, -1.3F);
		this.setRotor(1,0.48F, 2.96F, -9.37F);

		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/hy70.png";
		this.w2icon="advancearmy:textures/hud/aim9x.png";
		this.w3icon="wmlib:textures/hud/flare.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		
		this.w1name = "70毫米九头蛇火箭弹";
		this.w2name = "AIM9X空空导弹";
		this.w4name = "紧急维修";
	}

	public EntitySA_Helicopter(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_HELI.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Helicopter.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 80.0D)
					.add(Attributes.ARMOR, (double) 8D);
    }
    /**
     * Smooths the rendering on servers
     */
    /*private void tickLerp()
    {
        if(this.isControlledByLocalInstance())
        {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }

        if(this.lerpSteps > 0)
        {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
            double d3 = Mth.wrapDegrees(this.lerpYaw - (double) this.yRot);
            this.yRot = (float) ((double) this.yRot + d3 / (double) this.lerpSteps);
            this.xRot = (float) ((double) this.xRot + (this.lerpPitch - (double) this.xRot) / (double) this.lerpSteps);
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRot(this.getYRot(), this.getXRot());
        }
    }
    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = (double) yaw;
        this.lerpPitch = (double) pitch;
        this.lerpSteps = 10;
    }*/
	
	public void tick() {
		super.tick();
		//this.tickLerp();

		if(this.getHealth()>0){
			if (this.getAnySeat(1) != null){//
				EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
				if(this.setSeat){
					String model = "advancearmy:textures/entity/bullet/bullet30mm.obj";
					String tex = "advancearmy:textures/entity/bullet/bullet.png";
					String model2 = "advancearmy:textures/entity/bullet/agm114.obj";
					String tex2 = "advancearmy:textures/entity/bullet/agm114.png";
					String fx1 = "SmokeGun";
					String fx2 = null;
					seat.seatProtect = 0.2F;
					seat.attack_range_max = 50;
					seat.attack_height_max = 10;
					seat.attack_height_min = -90;
					seat.render_hud_box = true;
					seat.w1name="30毫米机炮/AGM114地狱火导弹";
					seat.w1icon="advancearmy:textures/hud/cannon30mm.png";
					seat.w2icon="advancearmy:textures/hud/agm114.png";
					seat.hud_box_obj = "wmlib:textures/hud/gunner.obj";
					seat.hud_box_tex = "wmlib:textures/hud/box.png";
					seat.seatHide = false;
					seat.weaponCount = 2;
					seat.canlock = true;
					
					seat.gunner_aim=true;
					seat.turretPitchMax = 0;
					seat.minyaw=-100;
					seat.maxyaw=100;
					seat.canNightV=true;
					seat.ammo1 = 5;
					seat.magazine = 100;
					seat.reload_time1 = 100;
					seat.ammo2 = 60;
					seat.magazine2 = 4;
					seat.reload_time2 = 100;
					seat.reloadSound1 = SASoundEvent.reload_chaingun.get();
					seat.reloadSound2 = SASoundEvent.reload_missile.get();
					seat.setWeapon(0, 3, model, tex, fx1, fx2, SASoundEvent.fire_lav.get(), 0,-1,2,0,0.38F,
					25, 6F, 1.05F, 1, false, 1, 0.01F, 20, 0);
					
					seat.weaponcross[1] = true;
					seat.followvehicle[1] = true;
					seat.setWeapon(1, 4, model2, tex2, null, "SAMissileSmoke", SASoundEvent.fire_agm114.get(), this.fireposX2,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
					125, 3F, 1.05F, 4, false, 1, 0.01F, 200, 0);
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
		50, 3F, 1.1F, 2, false, 1, 0.001F, 50, 3);
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
		60, 3F, 1.5F, 2, false, 1, 0.01F, 250, 0);
	}
}
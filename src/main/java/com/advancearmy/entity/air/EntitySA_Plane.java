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
import advancearmy.init.ModEntities;
import net.minecraft.resources.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import advancearmy.entity.EntitySA_AirBase;
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.world.entity.player.Player;
import safx.util.EntityCondition;
import safx.SagerFX;
public class EntitySA_Plane extends EntitySA_AirBase{
	public EntitySA_Plane(EntityType<? extends EntitySA_Plane> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0;
		seatPosY[0] = 2F;
		seatPosZ[0] = 0F;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		seatMaxCount = 1;
		//this.is_aa = true;
		this.canlock = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/line.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.canNightV=true;
		VehicleType = 4;

		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-12F;
		this.noCulling = true;
		this.fly_height = 88;
		this.w1aa = false;
		this.w1max = 60;
		this.w1min = -10;
		this.w1aim = 15;
		this.w2aa = false;
		this.w2max = 100;
		this.w2min = 5;
		this.w2aim = 5;
		this.w4aa = true;
		this.w4max = 40;
		this.w4min = 10;
		this.w4aim = 15;
		this.ammo4=8;
        this.MoveSpeed = 0.045F;
        this.turnSpeed = 2.2F;
		this.setMaxUpStep(1.5F);
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
		this.obj = new SAObjModel("advancearmy:textures/mob/egal.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/egal.png");
		this.reloadSound1 = SASoundEvent.bomb_reload.get();
		this.reloadSound2 = SASoundEvent.reload_chaingun.get();
		this.reloadSound4 = SASoundEvent.reload_missile.get();
		this.magazine = 2;
		this.reload_time1 = 400;
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/egalicon.png");
		this.magazine2 = 600;
		this.reload_time2 = 400;
		this.magazine4 = 4;
		this.reload_time4 = 300;
		
		this.fireposX2 = 0.69F;
		this.fireposY2 = 2.32F;
		this.fireposZ2 = 1.64F;
		this.movesound = SASoundEvent.air_move.get();
		
		this.firesound1 = SASoundEvent.fire_missile.get();
		this.firesound2 = SASoundEvent.fire_autocannon.get();
		this.firesound4 = SASoundEvent.fire_missile.get();
		
		this.magazine3 = 15;
		this.reload_time3 = 200;
		
		this.w1showammo = true;
		this.w4showammo = true;

		this.trailcount = 2;
		this.setTrail(0,-0.49F, 2.15F, -10.05F);
		this.setTrail(1,0.49F, 2.15F, -10.05F);
		
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/agm65.png";
		this.w2icon="wmlib:textures/hud/ap30mm.png";
		this.w3icon="wmlib:textures/hud/flare.png";
		this.w4icon="wmlib:textures/hud/aamissile.png";
		this.w1name = "AGM65反坦克导弹";
		this.w2name = "30毫米航空机炮";
		this.w4name = "轻型防空导弹";
	}

	public EntitySA_Plane(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_PLANE.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Plane.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 200.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 6D);
    }
	boolean trail = false;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(!this.onGround() && this.getMovePitch()<-1F && this.movePower>10){
				if(!trail){
					if(ModList.get().isLoaded("safx")){
						if(this.level().isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, 5f, -0.5f, -3.0f, true, EntityCondition.ENTITY_PLANE);
						if(this.level().isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, -5f, -0.5f, -3.0f, true, EntityCondition.ENTITY_PLANE);
					}
					trail = true;
				}
			}else{
				if(trail)trail = false;
			}
			if(this.getHealth()>0){
				if(this.getArmyType2()==0){
					this.is_aa = false;
				}else{
					this.is_aa = true;
				}
			}
		}
	}

	public void weaponActive1(){
		float fireX = 0;
		if(this.getRemain1()%2==0){
			fireX = 2.83F;
		}else{
			fireX = -2.83F;
		}
		String model = "advancearmy:textures/entity/bullet/egalmissile_at.obj";
		String tex = "advancearmy:textures/mob/egal.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		Entity locktarget = null;
		if(this.getFirstSeat() != null && this.getFirstSeat().mitarget!=null){
			locktarget = this.getFirstSeat().mitarget;
		}else{
			locktarget = this.getTarget();
		}
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, null, "RocketTrail", firesound1,
		1F, fireX,0,0,1.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		200, 4F, 1.1F, 5, false, 1, 0.001F, 500, 0);
	}
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/bullet20mm.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
		LivingEntity shooter = this;
		
		
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, null, firesound2,
		1F, this.fireposX2,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		15, 8F, 1.5F, 1, false, 1, 0.01F, 50, 0);
	}
	public void weaponActive4(){
		float fireX = 0;
		if(this.getRemain4()%2==0){
			fireX = 3;
		}else{
			fireX = -3;
		}
		String model = "advancearmy:textures/entity/bullet/egalmissile.obj";
		String tex = "advancearmy:textures/mob/egal.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		Entity locktarget = null;
		if(this.getFirstSeat() != null && this.getFirstSeat().mitarget!=null){
			locktarget = this.getFirstSeat().mitarget;
		}else{
			locktarget = this.getTarget();
		}
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, null, "SAMissileSmoke", firesound4,
		1F, fireX,0,0,0.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		30, 5F, 1.1F, 2, false, 1, 0.001F, 150, 0);
	}
}
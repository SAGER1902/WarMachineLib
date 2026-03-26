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
public class EntitySA_Plane1 extends EntitySA_AirBase{
	public EntitySA_Plane1(EntityType<? extends EntitySA_Plane1> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0;
		seatPosY[0] = 1.4F;
		seatPosZ[0] = 0F;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		seatMaxCount = 1;
		this.render_hud_box = false;
		VehicleType = 4;
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.fly_height = 50;
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-12F;
		this.noCulling = true;
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/plane1icon.png");
		this.w1aa = false;
		this.w1max = 16;
		this.w1min = -10;
		this.w1aim = 5;
		this.w2aa = false;
		this.w2max = 100;
		this.w2min = 5;
		this.w2aim = 5;
		this.stayrange = 40;
		this.min_height = 10;
		this.can_follow = true;
        this.MoveSpeed = 0.034F;
        this.turnSpeed = 1.8F;
		this.setMaxUpStep(1.5F);
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
		this.obj = new SAObjModel("advancearmy:textures/mob/plane1.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/plane1.png");
		this.firesound1 = SASoundEvent.bomb_release.get();
		this.firesound2 = SASoundEvent.fire_ruvg.get();
		this.ammo2=3;
		this.reloadSound1 = SASoundEvent.bomb_reload.get();
		this.reloadSound2 = SASoundEvent.reload_mag.get();
		this.magazine = 2;
		this.reload_time1 = 200;
		this.magazine2 = 400;
		this.weaponCount = 2;
		this.w1icon="wmlib:textures/hud/bomb.png";
		this.w2icon="wmlib:textures/hud/7.62mm.png";
		
		this.w1showammo = true;
		this.fireposX2 = 0.38F;
		this.fireposY2 = 2.32F;
		this.fireposZ2 = 2.46F;
		this.w2cross = true;
		
		this.rotorcount = 1;
		this.rotor_rotez[0]=10;
		this.setRotor(0,0, 1.61F, 4.7F);
		this.rotortex = ResourceLocation.tryParse("advancearmy:textures/mob/plane1rotor.png");
		
	}
	public EntitySA_Plane1(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_PLANE1.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Plane1.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 3D);
    }
	/*boolean trail = false;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(!this.onGround() && this.getMovePitch()<-1F && this.movePower>10){
				if(!trail){
					if(ModList.get().isLoaded("safx")){
						if(this.level().isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, 3f, -0.5f, -3.0f, true, EntityCondition.ENTITY_PLANE);
						if(this.level().isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, -3f, -0.5f, -3.0f, true, EntityCondition.ENTITY_PLANE);
					}
					trail = true;
				}
			}else{
				if(trail)trail = false;
			}
		}
	}*/
	
	public void weaponActive1(){
		float fireX = 0;
		if(this.getRemain1()%2==0){
			fireX = 3;
		}else{
			fireX = -3;
		}
		String model = "advancearmy:textures/entity/bullet/smallbomb.obj";
		String tex = "advancearmy:textures/entity/bullet/bomb.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		
		
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, null, firesound1,
		1F, fireX,0,0,0.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		50, 1F, 1.1F, 3, false, 1, 0.04F, 500, 3);
	}
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
		LivingEntity shooter = this;
		float fireX = 0;
		if(this.getRemain2()%2==0){
			fireX = this.fireposX2;
		}else{
			fireX = -this.fireposX2;
		}
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 0, model, tex, "SmokeGun", null, firesound2,
		1F, fireX,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		9, 6F, 1.5F, 1, false, 1, 0.01F, 50, 0);
	}
}
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
public class EntitySA_A10c extends EntitySA_AirBase{
	public EntitySA_A10c(EntityType<? extends EntitySA_A10c> sodier, Level worldIn) {
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
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/a10icon.png");
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-12F;
		this.noCulling = true;
		this.fly_height = 78;
		this.w1aa = false;
		this.w1max = 50;
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
		this.stayrange = 50;
		this.min_height = 15;
		
        this.MoveSpeed = 0.04F;
        this.turnSpeed = 2.2F;
		this.setMaxUpStep(1.5F);
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
		this.obj = new SAObjModel("advancearmy:textures/mob/a10c.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/a10us.png");
		
		this.w1tex = ResourceLocation.tryParse("advancearmy:textures/entity/bullet/agm65.png");
		this.w4tex = ResourceLocation.tryParse("advancearmy:textures/entity/bullet/aim9x.png");
		
		this.reloadSound1 = SASoundEvent.reload_missile.get();
		this.reloadSound2 = SASoundEvent.reload_chaingun.get();
		this.reloadSound4 = SASoundEvent.bomb_reload.get();
		this.magazine = 4;
		this.reload_time1 = 300;
		this.ammo1=5;
		this.ammo2=1;
		this.ammo4=8;
		this.magazine2 = 750;
		this.reload_time2 = 300;
		this.magazine4 = 4;
		this.reload_time4 = 300;
		
		this.fireposX2 = -0.18F;
		this.fireposY2 = 1.49F;
		this.fireposZ2 = 3.52F;
		this.movesound = SASoundEvent.a10_move.get();
		
		this.firesound1 = SASoundEvent.fire_missile.get();
		this.firesound2 = SASoundEvent.fire_a10.get();
		this.firesound4 = SASoundEvent.fire_missile.get();
		
		this.magazine3 = 12;
		this.reload_time3 = 200;
		
		this.w1showammo = true;
		this.w4showammo = true;
		
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/agm65.png";
		this.w2icon="advancearmy:textures/hud/gau8.png";
		this.w3icon="wmlib:textures/hud/flare.png";
		this.w4icon="advancearmy:textures/hud/aim9x.png";
		this.w1name = "AGM65反坦克导弹";
		this.w2name = "25毫米GAU8机炮";
		this.w4name = "AIM9X空空导弹";
	}

	public EntitySA_A10c(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_A10C.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_A10c.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 8D);
    }
	boolean trail = false;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(!this.onGround() && this.getMovePitch()<-1F && this.movePower>10){
				if(!trail){
					if(ModList.get().isLoaded("safx")){
						if(this.level().isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, 9f, -0.5f, -3.0f, true, EntityCondition.ENTITY_PLANE);
						if(this.level().isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, -9f, -0.5f, -3.0f, true, EntityCondition.ENTITY_PLANE);
					}
					trail = true;
				}
			}else{
				if(trail)trail = false;
			}
			if(this.getArmyType2()==0){
				this.is_aa = false;
			}else{
				this.is_aa = true;
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
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, null, "RocketTrail", firesound1,
		1F, fireX,0,0,1.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		200, 3F, 1.1F, 5, false, 1, 0.001F, 50, 1);
	}
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/bullet30mm.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, null, firesound2,
		1F, this.fireposX2,this.seatPosY[0],this.fireposZ2,this.firebaseX,this.firebaseZ,
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
		String model = "advancearmy:textures/entity/bullet/aim9x.obj";
		String tex = "advancearmy:textures/entity/bullet/aim9x.png";
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
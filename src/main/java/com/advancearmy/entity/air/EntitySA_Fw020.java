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
public class EntitySA_Fw020 extends EntitySA_AirBase{
	public EntitySA_Fw020(EntityType<? extends EntitySA_Fw020> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0;
		seatPosY[0] = 2F;
		seatPosZ[0] = 0F;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		seatMaxCount = 1;
		this.is_aa = true;
		this.canlock = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/line.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.canNightV=true;
		VehicleType = 4;
		this.fly_height = 90;
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.w1aa = false;
		this.w1max = 100;
		this.w1min = 2;
		this.w1aim = 90;
		this.w2aa = false;
		this.w2max = 100;
		this.w2min = 2;
		this.w2aim = 5;
		
		this.w4aa = false;
		this.w4max = 15;
		this.w4min = -10;
		this.w4aim = 180;
		this.stayrange = 50;
		this.min_height = 25;
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/fw020icon.png");
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-12F;
		this.noCulling = true;
		
        this.MoveSpeed = 0.07F;
        this.turnSpeed = 4F;
		this.setMaxUpStep(1.5F);
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
		this.obj = new SAObjModel("advancearmy:textures/mob/fw020.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/fw020_t.png");
		this.drivetex = ResourceLocation.tryParse("advancearmy:textures/mob/drive.png");
		this.reloadSound1 = SASoundEvent.reload_missile.get();
		this.reloadSound2 = SASoundEvent.reload_chaingun.get();
		this.reloadSound4 = SASoundEvent.bomb_reload.get();
		this.magazine = 600;
		this.reload_time1 = 100;
		this.trailtex = ResourceLocation.tryParse("advancearmy:textures/entity/flash/thruster_b.png");
		this.magazine2 = 6;
		this.reload_time2 = 100;
		this.magazine4 = 2;
		this.reload_time4 = 250;
		this.ammo1=2;
		this.magazine3 = 20;
		this.reload_time3 = 300;
		this.weaponCount = 4;
		this.w1icon="wmlib:textures/hud/ap30mm.png";
		this.w2icon="advancearmy:textures/hud/aim9x.png";
		this.w3icon="advancearmy:textures/hud/dun1.png";
		this.w4icon="wmlib:textures/hud/bomb.png";
		this.w1name = "SWUN25毫米机炮";
		this.w2name = "重型能量炮";
		this.w4name = "护盾系统";
		this.w1showammo = true;
		this.w4showammo = true;
		this.can_follow= true;
		this.trailcount = 1;
		this.setTrail(0,0, 2.31F, -7.5F);
		this.isSpaceShip = true;
		this.can_follow= true;
		
		this.fireposX2 = 0.68F;
		this.fireposY2 = 2.38F;
		this.fireposZ2 = 2.14F;
		this.movesound = SASoundEvent.air_move.get();
		
		this.firesound1 = SASoundEvent.knightf.get();
		this.firesound2 = SASoundEvent.powercannon.get();
		this.firesound4 = SASoundEvent.bomb_release.get();
	}

	public EntitySA_Fw020(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_FW020.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Fw020.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 500.0D).add(Attributes.FOLLOW_RANGE, 200.0D).add(Attributes.ARMOR, (double) 8D);
    }
	boolean trail = false;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(!this.onGround() && this.getMovePitch()<-1F && this.movePower>10){
				if(!trail){
					if(ModList.get().isLoaded("safx")){
						if(this.level().isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, 6.44f, -0.5f, -6.71f, true, EntityCondition.ENTITY_PLANE);
						if(this.level().isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, -6.44f, -0.5f, -6.71f, true, EntityCondition.ENTITY_PLANE);
					}
					trail = true;
				}
			}else{
				if(trail)trail = false;
			}
			if(this.getArmyType2()==0){
				this.is_aa = true;
			}else{
				this.is_aa = false;
			}
		}
	}
	
	public void weaponActive1(){
		String model = "advancearmy:textures/entity/bullet/bulletcannon_small.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, null, firesound1,
		1.57F, 2.63F,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		35, 6F, 1.5F, 1, false, 1, 0.01F, 50, 0);
		
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, null, firesound1,
		1.57F, -2.63F,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		35, 6F, 1.5F, 1, false, 1, 0.01F, 50, 0);
	}
	public void weaponActive2(){
		String model = "wmlib:textures/entity/flare.obj";
		String tex = "wmlib:textures/entity/flare.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		Entity locktarget = null;
		if(this.getFirstSeat() != null && this.getFirstSeat().mitarget!=null){
			locktarget = this.getFirstSeat().mitarget;
		}else{
			locktarget = this.getTarget();
		}
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 3, model, tex, null, null, firesound2,
		1.57F, -2.83F, 0, 0, 1.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		75, 5F, 1.1F, 3, false, 1, 0.001F, 200, 5);
		
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 3, model, tex, null, null, firesound2,
		1.57F, 2.83F, 0, 0, 1.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.getYRot()/*this.turretYaw*/, this.turretPitch,
		75, 5F, 1.1F, 3, false, 1, 0.001F, 200, 5);
	}
}
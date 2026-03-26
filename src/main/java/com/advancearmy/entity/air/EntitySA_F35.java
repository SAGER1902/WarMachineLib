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
public class EntitySA_F35 extends EntitySA_AirBase{
	public EntitySA_F35(EntityType<? extends EntitySA_F35> sodier, Level worldIn) {
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
		/*spwingz = +22.5F;
		spwingx = +20F;*/
		
		VehicleType = 4;
		this.fly_height = 90;
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		this.w1aa = true;
		this.w1max = 50;
		this.w1min = 10;
		this.w1aim = 5;
		this.w2aa = false;
		this.w2max = 100;
		this.w2min = 15;
		this.w2aim = 5;
		this.w4aa = false;
		this.w4max = 30;
		this.w4min = -10;
		this.w4aim = 10;
		this.stayrange = 50;
		this.min_height = 20;
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/f35icon.png");
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
		this.obj = new SAObjModel("advancearmy:textures/mob/f35.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/f35.png");
		this.trailtex = ResourceLocation.tryParse("advancearmy:textures/mob/planetrail_r.png");
		
		this.reloadSound1 = SASoundEvent.reload_missile.get();
		this.reloadSound2 = SASoundEvent.reload_chaingun.get();
		this.reloadSound4 = SASoundEvent.bomb_reload.get();
		this.magazine = 6;
		this.reload_time1 = 100;
		
		this.magazine2 = 600;
		this.reload_time2 = 400;
		this.magazine4 = 2;
		this.reload_time4 = 250;
		this.ammo1=8;
		this.magazine3 = 20;
		this.reload_time3 = 200;
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/aim9x.png";
		this.w2icon="wmlib:textures/hud/ap30mm.png";
		this.w3icon="wmlib:textures/hud/flare.png";
		this.w4icon="wmlib:textures/hud/bomb.png";
		this.w1name = "AIM9X空空导弹";
		this.w2name = "25毫米航空机炮";
		this.w4name = "重型航空炸弹";
		
		this.w1showammo = true;
		this.w4showammo = true;
		
		this.trailcount = 1;
		this.setTrail(0,0, 2.11F, -12.18F);
		
		this.fireposX2 = 0.68F;
		this.fireposY2 = 2.38F;
		this.fireposZ2 = 2.14F;
		this.movesound = SASoundEvent.air_move.get();
		
		this.firesound1 = SASoundEvent.fire_missile.get();
		this.firesound2 = SASoundEvent.fire_autocannon.get();
		this.firesound4 = SASoundEvent.bomb_release.get();
	}

	public EntitySA_F35(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_F35.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_F35.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 250.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 5D);
    }
	int attack_start = 0;
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
			
			if(this.getMoveMode()==5){
				this.movePower = throttleMax;
				this.throttle = throttleMax;
				this.drop = 200;
				++attack_start;
				if(attack_start==5)this.setMovePitch(75);
				if(attack_start>15 && attack_start<50){
					this.attack4=true;
				}else{
					this.attack4=false;
				}
				if(attack_start==15)this.setMovePitch(-30);
				if(attack_start==25)this.setMovePitch(0);
				if(this.getArmyType2()>0){
					this.setArmyType2(this.getArmyType2()-1);
				}else{
					this.removePassenger();
					this.discard();
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
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, null, "SAMissileSmoke", firesound1,
		1F, fireX,0,0,1.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		50, 5F, 1.1F, 3, false, 1, 0.001F, 200, 0);
	}
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/bullet20mm.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, null, firesound2,
		1F, this.fireposX2,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		15, 16F, 1.5F, 1, false, 1, 0.01F, 50, 0);
	}
	public void weaponActive4(){
		float fireX = 0;
		if(this.getRemain4()%2==0){
			fireX = 3;
		}else{
			fireX = -3;
		}
		String model = "advancearmy:textures/entity/bullet/egalbomb.obj";
		String tex = "advancearmy:textures/mob/f35.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, null, firesound4,
		1F, fireX,0,0,0.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		250, 1.2F, 1.1F, 9, false, 1, 0.05F, 500, 3);
	}
}
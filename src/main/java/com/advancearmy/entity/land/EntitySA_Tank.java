package advancearmy.entity.land;
import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import advancearmy.AdvanceArmy;
import advancearmy.entity.EntitySA_LandBase;
import advancearmy.event.SASoundEvent;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.init.ModEntities;
import safx.SagerFX;
public class EntitySA_Tank extends EntitySA_LandBase{
	public EntitySA_Tank(EntityType<EntitySA_Tank>type, Level worldIn) {
		super(type, worldIn);
		this.setMaxUpStep(1.5F);
		seatPosX[0] = 1.07F;
		seatPosY[0] = 1.1F;
		seatPosZ[0] = 2F;
		seatTurret[0] = true;
		seatHide[0] = true;
		seatMaxCount = 2;
		seatPosX[1] = -0.64F;
		seatPosY[1] = 2.5F;
		seatPosZ[1] = -1F;
		seatTurret[1] = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/tankus.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/m1head.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/m1body.png");
		this.w1name = "120mmHEAT";
		this.w2name = "7,62mmMG";
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
        this.MoveSpeed = 0.03F;
        this.turnSpeed = 1.25F;
		this.turretSpeed = 0.2F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.canNightV=true;
		this.armor_front = 60;
		this.armor_side = 50;
		this.armor_back = 30;
		this.armor_top = 10;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 60;
		this.armor_turret_side = 60;
		this.armor_turret_back = 30;

		this.ammo1=5;
		this.ammo2=3;
		this.fireposX1 = 0;
		this.fireposY1 = 2.41F;
		this.fireposZ1 = 6.6F;
		this.fireposX2 = 0.27F;
		this.fireposY2 = 2.21F;
		this.fireposZ2 = 2.96F;
		this.firebaseX = 0;
		this.firebaseZ = 2F;
		this.magazine = 1;
		this.reload_time1 = 95;
		this.reloadSound1 = SASoundEvent.reload_m1a2.get();
		this.firesound1 = SASoundEvent.fire_m1a2.get();
		
		this.magazine2 = 100;
		this.reload_time2 = 95;
		this.reloadSound2 = SASoundEvent.reload_mag.get();
		this.firesound2 = SASoundEvent.fire_usvg.get();
		
		this.startsound = SASoundEvent.start_m1a2.get();
		this.movesound = SASoundEvent.move_track1.get();
		
		this.weaponCount = 4;
		this.w1icon="wmlib:textures/hud/heat120mm.png";
		this.w2icon="wmlib:textures/hud/7.62mm.png";
		this.w3icon="wmlib:textures/hud/cloud.png";
		this.w4icon="wmlib:textures/hud/repair.png";
	}

	public EntitySA_Tank(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_TANK.get(), worldIn);
	}

    public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Tank.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 400)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.FOLLOW_RANGE, 50)
                .add(Attributes.KNOCKBACK_RESISTANCE, 20)
                .add(Attributes.MOVEMENT_SPEED, 0);
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
				seat.attack_range_max = 35;
				seat.seatHide = false;
				seat.ridding_rotemgPitch = true;
				seat.weaponCount = 1;
				seat.ammo1 = 5;
				seat.magazine = 100;
				seat.reload_time1 = 80;
				seat.reloadSound1 = SASoundEvent.reload_mag.get();
				seat.firesound1_3p=SASoundEvent.fire_m2hb_3p.get();
				seat.setWeapon(0, 0, model, tex, fx1, fx2, SASoundEvent.fire_m2hb.get(), 0,1,3,0,0.38F,
				10, 6F, 1.25F, 1, false, 1, 0.01F, 20, 0);
			}
			this.turretYaw1=seat.getYHeadRot();
			if(seat.turretPitch<15)this.turretPitch1=seat.turretPitch;
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
		}
		
		if(this.getTargetType()==0){
			this.firesound2=SASoundEvent.fire_usvg.get();
		}else{
			this.firesound2=SASoundEvent.fire_usvg_3p.get();
		}
		if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
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
					List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(6D, 3.0D, 6D));
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity attackentity = list.get(k2);
						if(attackentity instanceof Mob && ((Mob)attackentity).getHealth()>0 && attackentity!=this){
							Mob living = (Mob)attackentity;
							if(this.CanAttack(attackentity)){
								living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200,5));
								living.setTarget(null);
								living.setDeltaMovement(0,0,0);
							}
						}
					}
					this.playSound(SASoundEvent.shell_impact.get(), 3.0F, 1.0F);
					this.setRemain3(0);
				}
				seat.keyx = false;
			}
		}
	}

	public void weaponActive1(){
		String model = "advancearmy:textures/entity/bullet/bulletcannon.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet.png";
		String fx1 = "AdvTankFire";
		String fx2 = null;//PlaneTrail
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
		String fx2 = /*"PlaneTrail"*/null;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 1, model, tex, fx1, fx2, firesound2,
		1F, this.fireposX2,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		3, 6F, 1.5F, 0, false, 1, 0.01F, 20, 0);
	}
}
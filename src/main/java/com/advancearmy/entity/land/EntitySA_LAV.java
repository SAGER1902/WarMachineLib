package advancearmy.entity.land;

import java.util.List;

import javax.annotation.Nullable;

import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
import wmlib.common.bullet.EntityMissile;
import advancearmy.event.SASoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import safx.SagerFX;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PlayMessages;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import safx.util.EntityCondition;
import wmlib.common.living.PL_LandMove;
import wmlib.common.living.WeaponVehicleBase;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.fml.ModList;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.EntitySA_LandBase;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.client.obj.SAObjModel;
public class EntitySA_LAV extends EntitySA_LandBase{
	public EntitySA_LAV(EntityType<? extends EntitySA_LAV> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0.5F;
		seatPosY[0] = 1.7F;
		seatPosZ[0] = 1F;
		seatRoteX[0]=0.15F;
		seatRoteZ[0]=-1.16F;
		seatTurret[0] = true;
		seatHide[0] = true;
		this.attack_height_max = 100;
		this.attack_range_max = 50;
		seatMaxCount = 1;
		this.canlock = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/apc.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.VehicleType = 2;
		this.w1name = "25mmAP";
		this.w2name = "TOW";
		this.canNightV=true;
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		canWater=true;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/lavhead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/lavbody.png");
		this.obj = new SAObjModel("advancearmy:textures/mob/lav25.obj");
		seatView3X=0F;
		seatView3Y=-2F;
		seatView3Z=-4.5F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -60;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.05F;
        this.turnSpeed = 1.5F;
		this.turretSpeed = 0.6F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(1.5F);
		
		this.armor_front = 30;
		this.armor_side = 10;
		this.armor_back = 10;
		this.armor_top = 10;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 30;
		this.armor_turret_side = 10;
		this.armor_turret_back = 10;
		
		this.ammo1=5;
		this.ammo2=4;
		
		this.fireposX1 = 0.09F;
		this.fireposY1 = 2.7F;
		this.fireposZ1 = 0.4F;
		
		this.firebaseX = 0.11F;
		this.firebaseZ = 0F;
		
		this.posXmove= 0.15F;
		this.posZmove=-1.16F;
		
		this.magazine = 100;
		this.reload_time1 = 95;
		this.reloadSound1 = SASoundEvent.reload_chaingun.get();
		this.firesound1 = SASoundEvent.fire_lav.get();
		
		this.magazine2 = 2;
		this.reload_time2 = 295;
		this.reloadSound2 = SASoundEvent.reload_m6.get();
		this.firesound2 = SASoundEvent.fire_missile.get();
		
		this.startsound = SASoundEvent.start_lav.get();
		this.movesound = SASoundEvent.move_lav.get();
		this.soundspeed=0.7F;
		
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/2a42.png";
		this.w2icon="advancearmy:textures/hud/agm114.png";
		this.w3icon="wmlib:textures/hud/cloud.png";
		this.w4icon="wmlib:textures/hud/repair.png";
	}

	public EntitySA_LAV(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_LAV.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_LAV.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 8D);
    }
	public void tick() {
		super.tick();
		if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
			{
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				//PL_LandMove.moveCarMode(player, this, this.MoveSpeed, turnSpeed);
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
		this.playSound(firesound1, 3.0F, 1.0F);
		double xx11 = 0;
		double zz11 = 0;
		double px = 0;
		double pz = 0;
		px -= Mth.sin(this.getYRot() * 0.01745329252F) * this.posZmove;
		pz += Mth.cos(this.getYRot() * 0.01745329252F) * this.posZmove;
		px -= Mth.sin(this.getYRot() * 0.01745329252F -1.57F) * this.posXmove;
		pz += Mth.cos(this.getYRot() * 0.01745329252F -1.57F) * this.posXmove;
		
		float base = 0;
		base = Mth.sqrt((fireposZ1 - 0)* (fireposZ1 - 0) + (0.09F - fireposX1)*(0.09F - fireposX1)) * Mth.sin(-this.turretPitch  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * fireposZ1;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * fireposZ1;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1.57F) * 0.09F;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1.57F) * 0.09F;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		EntityShell bullet = new EntityShell(this.level(), shooter);
		bullet.hitEntitySound=SASoundEvent.ag_metal.get();
		bullet.hitBlockSound=SASoundEvent.ag_impact.get();
		bullet.power = 18;
		bullet.timemax = 100;
		bullet.setBulletType(1);
		bullet.setExLevel(1);
		bullet.setModel("advancearmy:textures/entity/bullet/bullet30mm.obj");
		bullet.setTex("advancearmy:textures/entity/bullet/bullet1.png");
		bullet.setGravity(0.01F);
		bullet.moveTo(this.getX()+px + xx11, this.getY()+fireposY1+base, this.getZ()+pz + zz11, this.getYRot(), this.turretPitch);
		bullet.shootFromRotation(this, this.turretPitch, this.turretYaw, 0.0F, 4F, 1);
		if (!this.level().isClientSide) this.level().addFreshEntity(bullet);
	}
	public void weaponActive2(){
		this.playSound(firesound2, 3.0F, 1.0F);
		float fire_x = -1.25F;
		if(this.getRemain2()%2==0){
			fire_x = 1.25F;
		}
		double xx11 = 0;
		double zz11 = 0;
		double px = 0;
		double pz = 0;
		px -= Mth.sin(this.getYRot() * 0.01745329252F) * this.posZmove;
		pz += Mth.cos(this.getYRot() * 0.01745329252F) * this.posZmove;
		px -= Mth.sin(this.getYRot() * 0.01745329252F -1.57F) * this.posXmove;
		pz += Mth.cos(this.getYRot() * 0.01745329252F -1.57F) * this.posXmove;
		
		float base = 0;
		base = Mth.sqrt((0.88F - 0)* (0.88F - 0) + (fire_x - 0)*(fire_x - 0)) * Mth.sin(-this.turretPitch  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * 0.88F;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * 0.88F;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1.57F) * fire_x;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1.57F) * fire_x;
		LivingEntity shooter = this;
		Entity locktarget = null;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		if(this.getFirstSeat() != null && this.getFirstSeat().mitarget!=null){
			locktarget = this.getFirstSeat().mitarget;
		}else{
			locktarget = this.getTarget();
		}
		EntityMissile bullet = new EntityMissile(this.level(), shooter, locktarget, shooter);
		bullet.power = 250;
		bullet.setGravity(0.01F);
		bullet.setExLevel(5);
		bullet.hitEntitySound=SASoundEvent.tank_shell_metal.get();
		bullet.hitBlockSound=SASoundEvent.tank_shell.get();
		//bullet.flame = true;
		bullet.setBulletType(1);
		bullet.setModel("advancearmy:textures/entity/bullet/bulletrocket.obj");
		bullet.setTex("advancearmy:textures/entity/bullet/bulletrocket.png");
		bullet.moveTo(this.getX()+px + xx11, this.getY()+2.9F+base, this.getZ()+pz + zz11, this.getYRot(), this.turretPitch);
		bullet.shootFromRotation(this, this.turretPitch, this.turretYaw, 0.0F, 4F, 1);
		if (!this.level().isClientSide) this.level().addFreshEntity(bullet);
		bullet.friend = this;
		bullet.setFX("SAMissileSmoke");
	}
}
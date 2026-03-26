package advancearmy.entity.land;

import java.util.List;

import javax.annotation.Nullable;

import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
import advancearmy.event.SASoundEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.Mob;

import net.minecraft.world.entity.EntityType;

import net.minecraftforge.network.PlayMessages;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.tags.FluidTags;

import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;

import advancearmy.init.ModEntities;
import wmlib.common.living.AI_TankSet;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;

import net.minecraft.core.particles.ParticleTypes;

import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.EntitySA_LandBase;
import safx.SagerFX;
import net.minecraftforge.fml.ModList;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageVehicleAnim;
import net.minecraftforge.network.PacketDistributor;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
public class EntitySA_FTK_H extends EntitySA_LandBase{
	public EntitySA_FTK_H(EntityType<? extends EntitySA_FTK_H> sodier, Level worldIn) {
		super(sodier, worldIn);
		fireproduct=true;
		seatTurret[0] = true;
		seatHide[0] = true;
		this.isthrow = true;
		this.changeThrow = true;
		this.throwspeed = 8F;
		this.throwgrav = 0.1F;
		this.selfheal = true;
		this.armor_front = 120;
		this.armor_side = 100;
		this.armor_back = 70;
		this.armor_top = 40;
		this.armor_bottom = 40;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 120;
		this.armor_turret_side = 100;
		this.armor_turret_back = 70;
		this.canNightV=true;
		seatPosX[0] = 1F;
		seatPosY[0] = 3.7F;
		seatPosZ[0] = 0F;
		
		seatMaxCount = 7;
		seatPosX[1] = 1.05F;
		seatPosY[1] = 2F;
		seatPosZ[1] = 6;

		seatPosX[2] = -1.05F;
		seatPosY[2] = 2F;
		seatPosZ[2] = 3.5F;
		
		seatPosX[3] = -3.74F;
		seatPosY[3] = 2.42F;
		seatPosZ[3] = 0.78F;
		
		seatPosX[4] = 3.74F;
		seatPosY[4] = 2.42F;
		seatPosZ[4] = 0.78F;
		
		seatPosX[5] = -3.74F;
		seatPosY[5] = 1.05F;
		seatPosZ[5] = 0.78F;
		
		seatPosX[6] = 3.74F;
		seatPosY[6] = 1.05F;
		seatPosZ[6] = 0.78F;
		this.setMaxUpStep(1.5F);
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/box.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/ftkhhead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/ftkhbody.png");
		this.renderHudIcon = false;
		this.renderHudOverlay = true;
		this.hudOverlay = "wmlib:textures/misc/cannon_scope.png";
		this.renderHudOverlayZoom = true;
		this.hudOverlayZoom = "wmlib:textures/misc/tank_scope.png";
		this.w1name = Component.translatable("advancearmy.weapon.152cannon.desc").getString();
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		/*this.maxyaw=150F;
		this.minyaw=-150F;*/
		
		this.fireposY1 = 2.6F;
		this.fireposZ1 = 7.73F;
		
		seatView3X=0F;
		seatView3Y=-6F;
		seatView3Z=-12F;
		this.seatProtect = 0.01F;
		this.turretPitchMax = -89;
		this.turretPitchMin = 15;
        this.MoveSpeed = 0.03F;
        this.turnSpeed = 1F;
		this.turretSpeed = 0.2F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.magazine = 1;
		this.reload_time1 = 100;
		this.reloadSound1 = SASoundEvent.reload_t90.get();
		this.weaponCount = 1;
		this.w1icon="wmlib:textures/hud/he120mm.png";
	}

	public EntitySA_FTK_H(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_FTK_H.get(), worldIn);
	}
	
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.IRON_GOLEM_HURT;
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.IRON_GOLEM_DEATH;
    }	
	
	protected void tickDeath() {
	  ++this.deathTime;
	  if (this.deathTime == 1){
		  this.playSound(SASoundEvent.tank_explode.get(), 3.0F, 1.0F);
		  this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3, false, Level.ExplosionInteraction.NONE);
	  }
	  if (this.deathTime == 120) {
		 this.discard(); //Forge keep data until we revive player
		 this.playSound(SASoundEvent.wreck_explosion.get(), 3.0F, 1.0F);
		 this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2, false, Level.ExplosionInteraction.NONE);
		 for(int i = 0; i < 20; ++i) {
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			double d2 = this.random.nextGaussian() * 0.02D;
			this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
		 }
	  }
	}
    public void setAnimFire(int id)
    {
        if(this != null && !this.level().isClientSide)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageVehicleAnim(this.getId(), id));
        }
    }

	public float rotation_3 = 0;
	public float rotationp_3 = 0;
	public float rotation_4 = 0;
	public float rotationp_4 = 0;
	public float rotation_5 = 0;
	public float rotationp_5 = 0;
	public float rotation_6 = 0;
	public float rotationp_6 = 0;
	public float rotation_7 = 0;
	public float rotationp_7 = 0;
	public float fix = 0;
	public float turretYawO1;
	public float turretPitchO1;
	public boolean ammo = false;
	public static int count = 0;
	int x = 1;
	
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
		List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(15D, 15.0D, 15D));
		for(int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity = list.get(k2);
			if(entity!=null && entity instanceof LivingEntity && entity!=this){
				if(entity instanceof LivingEntity && entity instanceof Enemy){
					entity.hurt(this.damageSources().thrown(this, this), 100);
				}
				if(entity instanceof Player){
					Player players = (Player)entity;
					this.shockPlayer(players, 55);
				}
				((LivingEntity)entity).knockback(2F, 6F, 6);
			}
		}
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("DropRing", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 2F);
		this.playSound(SASoundEvent.shell_impact.get(), 10.0F,1);
		return false;
	}
	
    public void shockPlayer(Player player, float count) {
        if(player != null){
			if(player.getVehicle()!=null){
				expPitch = expPitch+count*0.6F;
			}else{
				expPitch = expPitch+count;
			}
        }
    }
	
	public float turretX1 = -0.2F;
	public float turretX2 = -0.2F;
	public float turretX3 = -0.2F;
	public void tick() {
		super.tick();
		if(this.getHealth()<=0)return;

		if (this.getAnySeat(1) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
			if(this.setSeat){
				seat.minyaw = -35F;
				seat.maxyaw = 35F;
				this.seatWeaponTesla(seat);
			}
			this.turretYaw1=seat.getYHeadRot();
			if(seat.turretPitch<15)this.turretPitch1=seat.turretPitch;
		}
		if (this.getAnySeat(2) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(2);
			if(this.setSeat){
				seat.minyaw = -35F;
				seat.maxyaw = 60F;
				this.seatWeapon1(seat);
			}
			if(seat.getRemain1()%2==0)turretX1=-turretX1;
			this.turretYaw2=seat.getYHeadRot();
			if(seat.turretPitch<15)this.turretPitch2=seat.turretPitch;
		}
		if (this.getAnySeat(3) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(3);
			if(this.setSeat){
				seat.minyaw = -20F;
				seat.maxyaw = 180F;
				this.seatWeaponLaser(seat);
			}
			this.rotation_3=seat.getYHeadRot();
			if(seat.turretPitch<15)this.rotationp_3=seat.turretPitch;
		}
		if (this.getAnySeat(4) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(4);
			if(this.setSeat){
				seat.minyaw = -180F;
				seat.maxyaw = 20F;
				this.seatWeaponLaser(seat);
			}
			this.rotation_4=seat.getYHeadRot();
			if(seat.turretPitch<15)this.rotationp_4=seat.turretPitch;
		}
		if (this.getAnySeat(5) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(5);
			if(this.setSeat){
				seat.minyaw = 0F;
				seat.maxyaw = 100F;
				this.seatWeapon1(seat);
			}
			if(seat.getRemain1()%2==0)turretX2=-turretX2;
			this.rotation_5=seat.getYHeadRot();
			if(seat.turretPitch<15)this.rotationp_5=seat.turretPitch;
		}
		if (this.getAnySeat(6) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(6);
			if(this.setSeat){
				seat.minyaw = -100F;
				seat.maxyaw = 0F;
				this.seatWeapon1(seat);
			}
			if(seat.getRemain1()%2==0)turretX3=-turretX3;
			this.rotation_6=seat.getYHeadRot();
			if(seat.turretPitch<15)this.rotationp_6=seat.turretPitch;
		}
	}
	
	public void seatWeapon1(EntitySA_Seat seat){
		seat.turretSpeed = 0.4F;
		seat.seatPosZ[0] = 0.7F;
		seat.render_hud_box = true;
		seat.hud_box_obj = "wmlib:textures/hud/gunner.obj";
		seat.hud_box_tex = "wmlib:textures/hud/box.png";
		seat.turretPitchMax = -50;
		seat.turretPitchMin = 5;
		seat.seatHide = true;
		seat.weaponCount = 1;
		seat.ammo1 = 4;
		seat.magazine = 200;
		seat.reload_time1 = 100;
		seat.attack_range_max = 45;
		seat.turret_speed = true;
		seat.attack_height_max = 80;
		seat.w1name = "30毫米双联机炮";
		seat.reloadSound1 = SASoundEvent.reload_chaingun.get();
		String model = "advancearmy:textures/entity/bullet/bullet30mm.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet.png";
		String fx1 = "SmokeGun";
		String fx2 = null;
		seat.seatProtect = 0.01F;
		seat.weaponcross[0]=true;
		seat.setWeapon(0, 3, model, tex, fx1, fx2, SASoundEvent.fire_2a42.get(), -0.2F,0.3F,1.6F,0,0.6F,
		18, 4F, 1.5F, 1, false, 1, 0.05F, 25, 1);
	}
	public void seatWeaponLaser(EntitySA_Seat seat){
		seat.turretSpeed = 0.6F;
		seat.render_hud_box = true;
		seat.hud_box_obj = "wmlib:textures/hud/laseraa.obj";
		seat.hud_box_tex = "wmlib:textures/hud/box.png";
		seat.renderHudOverlay = false;
		seat.seatPosZ[0] = 0.7F;
		seat.laserweapon[0] = true;
		seat.laserwidth[0] = 1;
		seat.laser_model_tex1 = "advancearmy:textures/entity/flash/aa_beam";
		seat.laserfxfire1 = "LaserFlashGun";
		seat.laserfxhit1 = "LaserHit";
		seat.turretPitchMax = -75;
		seat.turretPitchMin = 10;
		seat.w1name="轻型激光炮";
		seat.magazine = 10;
		seat.seatHide = true;
		seat.weaponCount = 1;
		seat.ammo1 = 15;
		seat.reload_time1 = 100;
		seat.attack_range_max = 65;
		seat.turret_speed = true;
		seat.attack_height_max = 80;
		seat.seatProtect = 0.01F;
		seat.reloadSound1 = SASoundEvent.reload_chaingun.get();
		seat.setWeapon(0, 2, null, null, null, null, SASoundEvent.powercannon.get(), 0F,0.5F,2F,0,0.6F,
		45, 6F, 0, 2, false, 0, 0, 15, 0);
	}
	public void seatWeaponTesla(EntitySA_Seat seat){
		seat.turretSpeed = 0.6F;
		seat.render_hud_box = true;
		seat.hud_box_obj = "wmlib:textures/hud/tanklaser.obj";
		seat.hud_box_tex = "wmlib:textures/hud/box.png";
		seat.renderHudOverlay = false;
		seat.laserweapon[0] = true;
		seat.connect_cout[0] = 6;
		seat.can_connect[0] = true;
		seat.laserwidth[0] = 4;
		seat.w1name = "轻型磁暴线圈";
		seat.laserfxfire1 = "TeslaFlashGun";
		seat.laserfxhit1 = "TeslaHit";
		seat.turretPitchMax = -60;
		seat.turretPitchMin = 10;
		seat.attack_range_max = 35;
		seat.attack_height_max = 35;
		seat.magazine = 1;
		seat.seatHide = true;
		seat.weaponCount = 1;
		seat.reload_time1 = 50;
		seat.followvehicle[0] = true;
		seat.seatProtect = 0.01F;
		seat.reloadSound1 = SASoundEvent.teslareload.get();
		seat.setWeapon(0, 3, null, null, null, null, SASoundEvent.teslafire1.get(), 0F,0.2F,0F,0,0F,
		60, 8F, 0, 2, true, 0, 0, 10, 0);
	}
	
	public void weaponActive1(){
		this.playSound(SASoundEvent.fire_jp.get(), 5.0F, 1.0F);
		double px = 0;
		double pz = 0;
		px -= Mth.sin(this.getYRot() * 0.01745329252F) * this.seatPosZ[0];
		pz += Mth.cos(this.getYRot() * 0.01745329252F) * this.seatPosZ[0];
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = Mth.sqrt((7.73F - 2.6F)* (7.73F - 2.6F) + (0 - 0)*(0 - 0)) * Mth.sin(-this.turretPitch  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * 7.73F;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * 7.73F;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1.57F) * 0;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1.57F) * 0;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		EntityShell bullet = new EntityShell(this.level(), shooter);
		bullet.hitEntitySound=SASoundEvent.artillery_impact.get();
		bullet.hitBlockSound=SASoundEvent.artillery_impact.get();
		bullet.power = 160;
		bullet.setGravity(throwgrav);
		bullet.setExLevel(10);
		bullet.setModel("advancearmy:textures/entity/bullet/bulletcannon_big.obj");
		bullet.setTex("advancearmy:textures/entity/bullet/bullet.png");
		bullet.moveTo(this.getX()+px + xx11, this.getY()+4.25F+base, this.getZ()+pz + zz11, this.getYRot(), this.getXRot());
		bullet.shootFromRotation(this, this.turretPitch, this.turretYaw, 0.0F, throwspeed, 1.5F);
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("AdvTankFire", null, this.getX()+px + xx11, this.getY()+4.25F+base, this.getZ()+pz + zz11, 0, 0, 0, 2);
		if (!this.level().isClientSide) this.level().addFreshEntity(bullet);
	}
}
package advancearmy.entity.land;

import java.util.List;

import javax.annotation.Nullable;

import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.network.PlayMessages;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.lwjgl.glfw.GLFW;
import wmlib.common.living.PL_LandMove;
import wmlib.common.living.WeaponVehicleBase;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Explosion;
import wmlib.common.world.WMExplosionBase;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageTrail;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.fml.ModList;
import safx.SagerFX;
import net.minecraft.world.phys.Vec2;
import net.minecraft.core.particles.DustParticleOptions;
import advancearmy.entity.EntitySA_LandBase;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.init.ModEntities;
import org.joml.Vector3f;
public class EntitySA_MASTDOM extends EntitySA_LandBase{
	
	public EntitySA_MASTDOM(EntityType<? extends EntitySA_MASTDOM> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0F;
		seatPosY[0] = 3.1F;
		seatPosZ[0] = 2.3F;
		seatTurret[0] = true;
		seatHide[0] = true;
		seatMaxCount = 1;
		fireproduct=true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/tanklaser.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.canNightV=true;
		this.renderHudIcon = true;
		this.hudIcon = "wmlib:textures/hud/tankm1.png";
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = true;
		this.hudOverlayZoom = "wmlib:textures/misc/tank_scope.png";
		
		this.w1name = "RailGun";
		this.w2name = "namifix";
		
		this.armor_front = 100;
		this.armor_side = 80;
		this.armor_back = 60;
		this.armor_top = 40;
		this.armor_bottom = 40;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 90;
		this.armor_turret_side = 80;
		this.armor_turret_back = 60;

		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
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
		this.setMaxUpStep(1.5F);
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/masthead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/mastbody.png");
		this.magazine = 1;
		this.reload_time1 = 50;
		this.reloadSound1 = SASoundEvent.reload_m1a2.get();
		this.fireposY1=2.18F;
		this.weaponCount = 1;
		this.w1icon="advancearmy:textures/hud/mast.png";
		this.w1power = true;
		custom_weapon1=true;
	}
	public EntitySA_MASTDOM(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_MAST.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_MASTDOM.createMobAttributes();
    }
	public float fix = 0;
	public float rotationO1;
	public float rotationpO1;
	public boolean ammo = false;
	public static int count = 0;
	int x = 1;
	public static final DustParticleOptions NAMI = new DustParticleOptions(new Vector3f(0.2F, 1, 0.5F), 1.2F);
	public int healtime = 0;
	public void tick() {
		super.tick();
		boolean fire_flag = false;

		if(this.getHealth()<this.getMaxHealth()*0.5F && this.getRemain4() == 0){
			this.setRemain4(1);
		}
		if(this.getRemain4() > 0){
			if(this.getHealth() < this.getMaxHealth() && this.getHealth() > 0.0F) {
				++healtime;
				if(healtime > 2){
					this.setHealth(this.getHealth() + 2);
					this.playSound(SASoundEvent.fix.get(), 1.0F, 1.0F);
					healtime=0;
				}
				int ry = this.level().random.nextInt(3);
				this.level().addParticle(NAMI, this.getX()-2, this.getY() + 3D +ry, this.getZ()+2, 0.0D, 0.0D, 0.0D);
				this.level().addParticle(NAMI, this.getX()+2, this.getY() + 3D +ry, this.getZ()-1, 0.0D, 0.0D, 0.0D);
				int rx = this.level().random.nextInt(9);
				int rz = this.level().random.nextInt(9);
				this.level().addParticle(NAMI, this.getX()-4+rx, this.getY() + 3D +ry, this.getZ()-4+rz, 0.0D, 0.0D, 0.0D);
			}
			if(this.getHealth()>this.getMaxHealth()*0.8F){
				this.setRemain4(0);
			}
		}
		
		if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
			if(this.getTargetType()>0)this.setTargetType(0);//
			EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
			Player player = (Player)seat.getControllingPassenger();
			if(seat.powerfire){
				if(this.cooltime >= 10){
					this.cooltime = 0;
					if(this.getRemain1()>0 && this.getRemain1()<20){
						this.setRemain1(this.getRemain1() + 1);
						if(this.getRemain1()%4==0)this.playSound(SASoundEvent.reload_mast.get(), 4.0F, 1.0F);
					}
				}
			}else{
				if(this.getRemain1() > 5){
					this.setAnimFire(1);
					this.AIWeapon1(0F,2.18F,3.8F,0,2.2F, this.getRemain1()/10F);
					this.playSound(SASoundEvent.fire_mast.get(), 5.0F, 1.15F-this.getRemain1()*0.01F);
					this.gun_count1 = 0;//
					this.onFireAnimation(1F,6);
					this.setRemain1(0);
				}else{
					if(this.getRemain1() >1)this.setRemain1(this.getRemain1()-1);
				}
			}
		}
		
		if(this.getTargetType()>0){
			if(this.getTargetType()>1){
				if(this.getTarget()!=null){
					if(this.cooltime >= 10){
						this.cooltime = 0;
						if(this.getRemain1()>0 && this.getRemain1()<20){
							this.setRemain1(this.getRemain1() + 1);
							if(this.getRemain1()%4==0)this.playSound(SASoundEvent.reload_mast.get(), 4.0F, 1.0F);
						}
					}
				}
				if(this.getTarget()!=null && this.isAttacking() && custom_fire1){
					LivingEntity livingentity = this.getTarget();
					if(this.isAttacking() && this.find_time<40/* && this.getRemain1()>0*/){
						++this.find_time;
					}
					if(this.level().random.nextInt(6) > 3 && this.find_time > 20){
						this.find_time = 0;
						this.setAIType(0);
					}else if(this.level().random.nextInt(6) < 3 && this.find_time > 20){
						this.find_time = 0;
						this.setAIType(3+this.level().random.nextInt(1));
					}
					if(this.getRemain1() > 16+this.getAIType()||this.getRemain1()*5F>livingentity.getHealth()){
						this.setAnimFire(1);
						this.AIWeapon1(0F,2.18F,3.8F,0,2.2F, this.getRemain1()/10F);
						this.playSound(SASoundEvent.fire_mast.get(), 5.0F, 1.15F-this.getRemain1()*0.01F);
						this.gun_count1 = 0;//
						this.onFireAnimation(1F,6);
						this.setRemain1(0);
					}else{
						if(this.getRemain1() >1&&!this.isAttacking())this.setRemain1(this.getRemain1()-1);
					}
				}
			}
		}
	}
	
	public Vec2 getLockVector() {
	  return new Vec2(this.turretPitch, this.turretYaw);
	}
	
	public void AIWeapon1(float w, float h, float z, float by, float bz, float power){
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = Mth.sqrt((z - bz)* (z - bz) + (h - by)*(h - by)) * Mth.sin(-this.turretPitch  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * z;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * z;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1) * w;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1) * w;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("MastHit", null, this.getX()+xx11, this.getY()+h+base+1.5F, this.getZ()+zz11, 0, 0, 0, 2F*power);
		Vec3 locken = Vec3.directionFromRotation(this.getLockVector());//getLookAngle
		float d = 120;
		LivingEntity lockTarget = null;
		int range = 2;
		double ix = 0;
		double iy = 0;
		double iz = 0;
		boolean stop = false;
		int pierce = 0;
		for(int xxx = 0; xxx < 120; ++xxx) {
			ix = (this.getX()+xx11 + locken.x * xxx);
			iy = (this.getY()+this.fireposY1+base + locken.y * xxx);
			iz = (this.getZ()+zz11 + locken.z * xxx);
			BlockPos blockpos = new BlockPos((int)ix, (int)iy, (int)iz);
			BlockState iblockstate = this.level().getBlockState(blockpos);
			if (!iblockstate.isAir()&& !iblockstate.getCollisionShape(this.level(), blockpos).isEmpty()){
				break;
			}else{
				AABB axisalignedbb = (new AABB(ix-range, iy-range, iz-range, 
						ix+range, iy+range, iz+range)).inflate(1D);
				List<Entity> llist = this.level().getEntities(this,axisalignedbb);
				if (llist != null) {
					for (int lj = 0; lj < llist.size(); lj++) {
						Entity entity1 = (Entity) llist.get(lj);
						if (entity1 != null && entity1 instanceof LivingEntity) {
							if (NotFriend(entity1) && entity1 != shooter && entity1 != this) {
								lockTarget = (LivingEntity)entity1;
								if(lockTarget.getVehicle()!=null){
									Entity ve = lockTarget.getVehicle();
									ve.invulnerableTime = 0;
									ve.hurt(this.damageSources().thrown(this, shooter), 200*power-pierce*10);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(this.damageSources().thrown(this, shooter), 200*power-pierce*10);
								}
								++pierce;
								if(lockTarget.getHealth()>(200*power-pierce*10)){
									stop = true;
									ix=lockTarget.getX();
									iy=lockTarget.getY();
									iz=lockTarget.getZ();
									break;
								}
							}
						}
					}
				}
				if(stop)break;
			}
		}
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("MastHit", null, ix, iy+1.5D, iz, 0, 0, 0, 2F*power);
		MessageTrail messageBulletTrail = new MessageTrail(true,4,"advancearmy:textures/entity/flash/mast_beam" ,this.getX()+xx11, this.getY()+h+base, this.getZ()+zz11, this.getDeltaMovement().x, this.getDeltaMovement().z, ix, iy+0.5D, iz, 25F, power);
		PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 100, this.level().dimension())), messageBulletTrail);
		WMExplosionBase.createExplosionDamage(this, ix, iy+1.5D, iz, 20*power, 5*power, false,  false);
	}
}
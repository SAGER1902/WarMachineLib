package advancearmy.entity.land;
import java.util.List;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.Level;
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
import advancearmy.entity.EntitySA_LandBase;
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.world.phys.Vec2;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.damagesource.DamageSource;
import wmlib.common.world.WMExplosionBase;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageTrail;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.entity.Mob;
import net.minecraft.network.chat.Component;
import advancearmy.init.ModEntities;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
public class EntitySA_Tesla extends EntitySA_LandBase{
	public EntitySA_Tesla(EntityType<? extends EntitySA_Tesla> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0F;
		seatPosY[0] = 2.2F;
		seatPosZ[0] = 1F;
		seatTurret[0] = true;
		seatHide[0] = true;
		seatMaxCount = 1;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/tanklaser.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.w1name = Component.translatable("Tesla MKII").getString();
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.w1recoilp = 0;
		this.w1recoilr = 6;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -10;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.045F;
        this.turnSpeed = 1.5F;
		this.turretSpeed = 0.6F;
		
		this.armor_front = 55;
		this.armor_side = 30;
		this.armor_back = 30;
		this.armor_top = 10;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 1.6F;
		this.armor_turret_front = 50;
		this.armor_turret_side = 50;
		this.armor_turret_back = 30;
		
        this.throttleMax = 5F;
		this.throttleMin = -4F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(1.5F);
		this.w1barrelsize = 0.4F;
		this.fireposX1 = 1.72F;
		this.fireposY1 = 1.5F;
		this.fireposZ1 = 3.7F;
		this.firebaseZ = 3.7F;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/teslahead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/teslabody.png");
		this.tracktex = ResourceLocation.tryParse("advancearmy:textures/mob/track.png");
		this.obj = new SAObjModel("advancearmy:textures/mob/tesla.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/tesla.png");
		this.magazine = 2;
		this.reload_time1 = 60;
		this.reloadSound1 = SASoundEvent.teslareload.get();
		this.firesound1 = SASoundEvent.teslafire1.get();
		
		this.magazine2 = 1;
		this.reload_time2 = 250;
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		this.firesound2 = SASoundEvent.teslareload.get();
		
		this.canNightV=true;
		this.startsound = SASoundEvent.start_t90.get();
		this.movesound = SASoundEvent.move_track1.get();
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/teslaw.png";
		
		this.w3icon="wmlib:textures/hud/cloud.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		
		this.wheelcount = 6;
		this.setWheel(0,0, 0.73F, 2.63F);
		this.setWheel(1,0, 0.63F, 1.68F);
		this.setWheel(2,0, 0.63F, 0.64F);
		this.setWheel(3,0, 0.63F, -0.39F);
		this.setWheel(4,0, 0.63F, -1.43F);
		this.setWheel(5,0, 0.91F, -2.31F);
	}

	public EntitySA_Tesla(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_TESLA.get(), worldIn);
	}
	public Vec2 getLockVector() {
	  return new Vec2(this.turretPitch, this.turretYaw);
	}

	LivingEntity lockTarget = null;
	LivingEntity rangeTarget = null;
	boolean fastfire = false;
	int times;
	int cools;
	float damage;
	int teslaid = 5;
	int connect = 2;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(fastfire){
				if(times>0){
					--times;
					++cools;
					this.reload_time1 = 15;
					this.damage=40;
					this.firesound1 = SASoundEvent.teslafire2.get();
					teslaid=6;
					connect=4;
				}else{
					fastfire=false;
				}
			}else{
				this.magazine = 2;
				this.reload_time1 = 60;
				this.damage=80;
				this.firesound1 = SASoundEvent.teslafire1.get();
				teslaid=3;
				connect=2;
			}
			
			if (this.getFirstSeat() != null){
				if (this.getFirstSeat() != null){
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
							this.playSound(SASoundEvent.shell_impact.get(), 3.0F, 1.0F);
							this.setRemain3(0);
						}
						seat.keyx = false;
					}
				}
			}
		}else{
			fastfire=false;
		}
	}
	
	public void weaponActive2(){
		this.playSound(firesound2, 3.0F, 1.0F);
		this.fastfire=true;
		times=100;
	}
	
	public void weaponActive1(){
		float fireX = this.fireposX1;
		if(this.getRemain1()%2==0){
			fireX = this.fireposX1;
		}else{
			fireX = -this.fireposX1;
		}
		
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * this.fireposZ1;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * this.fireposZ1;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1.57F) * fireX;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1.57F) * fireX;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		{
			if(ModList.get().isLoaded("safx") && teslaid==3)SagerFX.proxy.createFX("TeslaFlashGun", null, this.getX()+xx11, this.getBoundingBox().minY + this.fireposY1+1.5F, this.getZ()+zz11, 0, 0, 0, 2);
		}
		Vec3 locken = Vec3.directionFromRotation(this.getLockVector());//getLookAngle
		float d = 120;
		int range = 3;
		double ix = 0;
		double iy = 0;
		double iz = 0;
		boolean stop = false;
		int pierce = 0;
		this.playSound(firesound1, 5.0F, 1.0F);
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
									ve.hurt(this.damageSources().thrown(this, shooter), damage);
									//ve.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100,10));
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(this.damageSources().thrown(this, shooter), damage);
									lockTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100,10));
								}
								if(lockTarget!=null){
									int count=0;
									List<Entity> entities = lockTarget.level().getEntities(lockTarget, lockTarget.getBoundingBox().inflate(18D, 10.0D, 18D));
									for (Entity living : entities) {
										if(this.NotFriend(living) && living instanceof LivingEntity){
											LivingEntity target = (LivingEntity)living;
											target.hurt(this.damageSources().thrown(this, shooter), damage*0.5F);
											target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50,10));
											{
												if(ModList.get().isLoaded("safx") && teslaid==3)SagerFX.proxy.createFX("TeslaHit", null, target.getX(), target.getBoundingBox().minY + target.getEyeHeight()/2F+1F, target.getZ(), 0, 0, 0, 0.5F);
												MessageTrail messageBulletTrail = new MessageTrail(true, teslaid, "advancearmy:textures/entity/flash/mirage" , 
												lockTarget.getX(), lockTarget.getY()+lockTarget.getBbHeight()*0.25F, lockTarget.getZ(),
												target.getDeltaMovement().x, target.getDeltaMovement().z, 
												target.getX(), target.getY()+target.getBbHeight()*0.25F, target.getZ(),
												20F, 0.5F);
												PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(target.getX(), target.getY(), target.getZ(), 80, target.level().dimension())), messageBulletTrail);
											}
											++count;
											if(count>connect)break;
										}
									}
								}
								stop = true;
								ix=lockTarget.getX();
								iy=lockTarget.getY();
								iz=lockTarget.getZ();
								break;
							}
						}
					}
				}
				if(stop){
					break;
				}
			}
		}
		WMExplosionBase.createExplosionDamage(this, ix, iy+1.5D, iz,10, 2, false,  false);
		if(ModList.get().isLoaded("safx") && teslaid==3)SagerFX.proxy.createFX("TeslaHit", null, ix, iy+1.5D, iz, 0, 0, 0, 2);
		MessageTrail messageBulletTrail = new MessageTrail(true, teslaid, "advancearmy:textures/entity/flash/prism_beam" ,this.getX()+xx11, this.getY()+this.fireposY1+base, this.getZ()+zz11, this.getDeltaMovement().x, this.getDeltaMovement().z, ix, iy+0.5D, iz, 20F, 1);
		PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messageBulletTrail);
	}
}
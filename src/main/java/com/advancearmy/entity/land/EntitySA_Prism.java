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
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.damagesource.DamageSource;
import wmlib.common.world.WMExplosionBase;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageTrail;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.Mob;
import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EntitySA_Prism extends EntitySA_LandBase{
	public EntitySA_Prism(EntityType<? extends EntitySA_Prism> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0.7F;
		seatPosY[0] = 2.1F;
		seatPosZ[0] = 0.1F;
		seatTurret[0] = true;
		seatHide[0] = true;
		seatMaxCount = 1;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/tanklaser.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.w1name = Component.translatable("advancearmy.weapon.prism.desc").getString();
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.w1recoilp = 0;
		this.w1recoilr = 6;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -25;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.03F;
        this.turnSpeed = 1.5F;
		this.turretSpeed = 0.6F;
        this.throttleMax = 5F;
		this.throttleMin = -4F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(1.5F);
		this.w1barrelsize = 0.4F;
		
		this.armor_front = 15;
		this.armor_side = 10;
		this.armor_back = 10;
		this.armor_top = 10;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 15;
		this.armor_turret_side = 10;
		this.armor_turret_back = 10;
		
		this.ammo1=5;
		this.fireposX1 = 0;
		this.fireposY1 = 2F;
		this.fireposZ1 = 1.2F;
		this.firebaseX = 0F;
		this.firebaseZ = 0F;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/prismhead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/prismbody.png");
		this.tracktex = ResourceLocation.tryParse("advancearmy:textures/mob/track.png");
		this.obj = new SAObjModel("advancearmy:textures/mob/gltk.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/gltk.png");
		this.magazine = 1;
		this.reload_time1 = 95;
		this.reloadSound1 = SASoundEvent.reload_t90.get();
		this.firesound1 = SASoundEvent.fire_gltk.get();
		this.canNightV=true;
		this.startsound = SASoundEvent.start_m6.get();
		this.movesound = SASoundEvent.move_track2.get();
		this.soundspeed=0.7F;
		this.weaponCount = 1;
		this.w1icon="advancearmy:textures/hud/prism.png";
		this.wheelcount = 6;
		this.setWheel(0,0, 0.79F, 2.38F);
		this.setWheel(1,0, 0.61F, 1.46F);
		this.setWheel(2,0, 0.61F, 0.2F);
		this.setWheel(3,0, 0.61F, -1.07F);
		this.setWheel(4,0, 0.61F, -2.33F);
		this.setWheel(5,0, 0.79F, -3.23F);
	}

	public EntitySA_Prism(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_PRISM.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_Prism.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 200.0D)
					.add(Attributes.FOLLOW_RANGE, 80.0D)
					.add(Attributes.ARMOR, (double) 8D);
    }
	
	public Vec2 getLockVector() {
	  return new Vec2(this.turretPitch, this.turretYaw);
	}

	LivingEntity lockTarget = null;
	LivingEntity rangeTarget = null;
	
	public void weaponActive1(){
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = Mth.sqrt((this.fireposZ1 - this.firebaseZ)* (this.fireposZ1 - this.firebaseZ) + (this.fireposX1 - 0)*(this.fireposX1 - 0)) * Mth.sin(-this.turretPitch  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * this.fireposZ1;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * this.fireposZ1;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1.57F) * this.fireposX1;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1.57F) * this.fireposX1;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		{
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("PrismFlashGun", null, this.getX()+xx11, this.getBoundingBox().minY + this.fireposY1+1.5F, this.getZ()+zz11, 0, 0, 0, 2);
		}
		Vec3 locken = Vec3.directionFromRotation(this.getLockVector());//getLookAngle
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
									ve.hurt(this.damageSources().thrown(this, shooter), 40);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(this.damageSources().thrown(this, shooter), 40);
								}
								if(lockTarget!=null){
									int count=0;
									List<Entity> entities = lockTarget.level().getEntities(lockTarget, lockTarget.getBoundingBox().inflate(25D, 15.0D, 25D));
									for (Entity living : entities) {
										if(this.NotFriend(living) && living instanceof LivingEntity){
											LivingEntity target = (LivingEntity)living;
											target.hurt(this.damageSources().thrown(this, shooter), 20);
											{
												if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("MirageHit", null, target.getX(), target.getBoundingBox().minY + target.getEyeHeight()/2F+1F, target.getZ(), 0, 0, 0, 0.5F);
												MessageTrail messageBulletTrail = new MessageTrail(true, 2, "advancearmy:textures/entity/flash/prism_beam" , 
												lockTarget.getX(), lockTarget.getY()+lockTarget.getBbHeight()*0.25F, lockTarget.getZ(),
												target.getDeltaMovement().x, target.getDeltaMovement().z, 
												target.getX(), target.getY()+target.getBbHeight()*0.25F, target.getZ(),
												20F, 0.5F);
												PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(target.getX(), target.getY(), target.getZ(), 80, target.level().dimension())), messageBulletTrail);
											}
											++count;
											if(count>3)break;
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
				if(stop)break;
			}
		}
		WMExplosionBase.createExplosionDamage(this, ix, iy+1.5D, iz,10, 2, false,  false);
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("PrismHit", null, ix, iy+1.5D, iz, 0, 0, 0, 2);
		
		MessageTrail messageBulletTrail = new MessageTrail(true, 2, "advancearmy:textures/entity/flash/prism_beam" ,
		this.getX()+xx11, this.getY()+this.fireposY1+base, this.getZ()+zz11, 
		this.getDeltaMovement().x, this.getDeltaMovement().z, 
		ix, iy, iz, 20F, 1);
		
		PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messageBulletTrail);
	}
}
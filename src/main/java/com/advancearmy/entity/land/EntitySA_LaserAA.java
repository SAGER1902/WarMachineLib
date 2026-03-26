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
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import wmlib.common.living.ai.LivingLockGoal;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import net.minecraft.network.chat.Component;
public class EntitySA_LaserAA extends EntitySA_LandBase{
	public EntitySA_LaserAA(EntityType<? extends EntitySA_LaserAA> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0F;
		seatPosY[0] = 2.1F;
		seatPosZ[0] = 0.1F;
		seatTurret[0] = true;
		seatHide[0] = true;
		seatMaxCount = 1;
		this.attack_height_max = 130;
		this.attack_range_max = 60;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/laseraa.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.w1name = Component.translatable("advancearmy.weapon.aalaser.desc").getString();
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.w1recoilp = 0;
		this.w1recoilr = 6;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -90;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.03F;
        this.turnSpeed = 1.5F;
		this.turretSpeed = 0.8F;
        this.throttleMax = 5F;
		this.throttleMin = -4F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.setMaxUpStep(1.5F);
		this.w1barrelsize = 0.2F;
		this.ammo1=5;
		this.fireposX1 = 1.12F;
		this.fireposY1 = 2.36F;
		this.fireposZ1 = 3.8F;
		this.firebaseX = 0F;//2.36F
		this.firebaseZ = 0F;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/skyfhead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/skyfbody.png");
		this.tracktex = ResourceLocation.tryParse("advancearmy:textures/mob/track.png");
		this.obj = new SAObjModel("advancearmy:textures/mob/skyfire.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/skyfire.png");
		this.canNightV=true;
		this.magazine = 2;
		this.reload_time1 = 25;
		this.reloadSound1 = null;
		this.firesound1 = SASoundEvent.fire_skyfire.get();
		this.soundspeed=0.7F;
		this.startsound = SASoundEvent.start_m6.get();
		this.movesound = SASoundEvent.move_track2.get();
		this.weaponCount = 1;
		this.w1icon="advancearmy:textures/hud/skyfaa.png";
		
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
		
		this.radercount = 1;
		this.setRader(0,0, 0, -1.3F);
		
		this.wheelcount = 9;
		this.setWheel(0,0, 0.99F, 4.03F);
		this.setWheel(1,0, 0.49F, 3.34F);
		this.setWheel(2,0, 0.49F, 2.45F);
		this.setWheel(3,0, 0.49F, 1.55F);
		this.setWheel(4,0, 0.49F, 0.65F);
		this.setWheel(5,0, 0.49F, -0.24F);
		this.setWheel(6,0, 0.49F, -1.14F);
		this.setWheel(7,0, 0.49F, -2.04F);
		this.setWheel(8,0, 1.02F, -2.74F);
		this.is_aa = true;
		this.canlock = true;
	}

	public EntitySA_LaserAA(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_LAA.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_LaserAA.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 200.0D)
					.add(Attributes.FOLLOW_RANGE, 120.0D)
					.add(Attributes.ARMOR, (double) 10D);
    }
	
	public Vec2 getLockVector() {
	  return new Vec2(this.turretPitch, this.turretYaw);
	}
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new LivingLockGoal(this, 1.0D, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 125F, true, false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, Player.class, 10, 125F, true, false, (attackentity) -> {return this.CanAttack(attackentity);}));
	}
	LivingEntity lockTarget = null;
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
		base = Mth.sqrt((this.fireposZ1 - this.firebaseZ)* (this.fireposZ1 - this.firebaseZ) + (this.fireposX1 - 0)*(this.fireposX1 - 0)) * Mth.sin(-this.turretPitch  * (1 * (float) Math.PI / 180));
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F) * this.fireposZ1;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F) * this.fireposZ1;
		xx11 -= Mth.sin(this.turretYaw * 0.01745329252F + 1.57F) * fireX;
		zz11 += Mth.cos(this.turretYaw * 0.01745329252F + 1.57F) * fireX;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		{
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("LaserFlashGun", null, this.getX()+xx11, this.getY()+this.fireposY1+base, this.getZ()+zz11, 0, 0, 0, 2);
		}
		Vec3 locken = Vec3.directionFromRotation(this.getLockVector());//getLookAngle
		float d = 120;
		int range = 1;
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
									ve.hurt(this.damageSources().thrown(this, shooter), 30);
									ve.setSecondsOnFire(8);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(this.damageSources().thrown(this, shooter), 30);
									lockTarget.setSecondsOnFire(8);
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
					++pierce;
					if(pierce>2)break;
				}
			}
		}
		WMExplosionBase.createExplosionDamage(this, ix, iy+1.5D, iz,10, 2, false,  false);
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("LaserHit", null, ix, iy+1.5D, iz, 0, 0, 0, 2);
		MessageTrail messageBulletTrail = new MessageTrail(true, 2, "advancearmy:textures/entity/flash/aa_beam" ,this.getX()+xx11, this.getY()+this.fireposY1-1.5F+base, this.getZ()+zz11, this.getDeltaMovement().x, this.getDeltaMovement().z, ix, iy, iz, 15F, 1);
		PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messageBulletTrail);
	}
}
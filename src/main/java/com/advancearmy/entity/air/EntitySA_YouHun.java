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
import safx.SagerFX;
import net.minecraft.resources.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import advancearmy.entity.EntitySA_HeliBase;
import net.minecraft.util.Mth;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.world.entity.player.Player;
import advancearmy.init.ModEntities;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageTrail;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;
import wmlib.common.world.WMExplosionBase;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
public class EntitySA_YouHun extends EntitySA_HeliBase{
    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYaw;
    protected double lerpPitch;
	
	public EntitySA_YouHun(EntityType<? extends EntitySA_YouHun> sodier, Level worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0;
		seatPosY[0] = 0.8F;
		seatPosZ[0] = 0;
		seatTurret[1] = false;
		this.canlock = true;
		this.is_aa = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/line.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.canNightV=true;
		VehicleType = 3;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.icon1tex = null;
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/youhunicon.png");
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-10F;
		seatMaxCount = 1;
        this.MoveSpeed = 0.032F;
        this.turnSpeed = 2.1F;
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
	
		this.magazine = 20;
		this.reload_time1 = 200;
		this.reloadSound1 = SASoundEvent.reload_missile.get();
		
		this.magazine2 = 8;
		this.reload_time2 = 200;
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		
		this.magazine3 = 20;
		this.reload_time3 = 300;
		
		this.startsound = SASoundEvent.start_ah.get();
		this.movesound = SASoundEvent.heli_move.get();
		
		this.firesound1 = SASoundEvent.powercannon.get();
		this.firesound2 = SASoundEvent.fire_missile.get();
		
		this.ammo1=6;
		this.ammo2=5;
		this.fireposX1 = 0.1F;
		this.fireposY1 = 0.2F;
		this.fireposZ1 = -1.2F;
		this.fireposX2 = 1.47F;
		this.fireposY2 = 0.69F;
		this.fireposZ2 = -0.8F;
		this.firebaseX = 0;
		this.firebaseZ = 0;
		
		this.w1aim =360;
		this.w2aim =180;
		this.w2aa = false;
		this.obj = new SAObjModel("advancearmy:textures/mob/youhun.obj");
		this.tex = ResourceLocation.tryParse("advancearmy:textures/mob/youhun_laser.png");
		this.isSpaceShip = true;
		this.can_follow= true;
		
		this.mgobj = new SAObjModel("advancearmy:textures/mob/btr_pao_t.obj");
		this.mgtex = ResourceLocation.tryParse("advancearmy:textures/mob/btr_pao_t.png");

		this.change_roter = false;
		this.setMg(0F, 0.8F, 0.8F, 0.4F);
		this.rotorcount = 4;
		this.rotor_rotey[0]=10;
		this.rotor_rotey[1]=10;
		this.rotor_rotey[2]=10;
		this.rotor_rotey[3]=10;
		this.setRotor(0,-2.38F, 1.8F, -1.55F);
		this.setRotor(1,2.38F, 1.8F, -1.55F);
		this.setRotor(2,-1.84F, 1.8F, -6.17F);
		this.setRotor(3,1.84F, 1.8F, -6.17F);
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/hy70.png";
		this.w2icon="advancearmy:textures/hud/aim9x.png";
		this.w3icon="advancearmy:textures/hud/dun2.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		this.w1name = "轻型双联激光炮";
		this.w2name = "智能火箭弹";
		this.w4name = "能量护盾系统";
	}

	public EntitySA_YouHun(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_HELI.get(), worldIn);
	}
	public static AttributeSupplier.Builder createAttributes() {
        return EntitySA_YouHun.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 400.0D).add(Attributes.FOLLOW_RANGE, 75.0D).add(Attributes.ARMOR, (double) 8D);
    }

	public Vec2 getLockVector() {
	  return new Vec2(this.turretPitch, this.turretYaw);
	}

	LivingEntity lockTarget = null;
	public void weaponActive1(){
		float fireX = 0;
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
		int range = 3;
		double ix = 0;
		double iy = 0;
		double iz = 0;
		boolean stop = false;
		int pierce = 0;
		this.playSound(firesound1, 5.0F, 1.0F);
		for(int xxx = 0; xxx < 120; ++xxx) {
			ix = (int) (this.getX()+xx11 + locken.x * xxx);
			iy = (int) (this.getY()+this.fireposY1+base + locken.y * xxx);
			iz = (int) (this.getZ()+zz11 + locken.z * xxx);
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
									ve.hurt(this.damageSources().thrown(this, shooter), 35);
									ve.setSecondsOnFire(8);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(this.damageSources().thrown(this, shooter), 35);
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
	public void weaponActive2(){
		float fireX = 0;
		if(this.getRemain2()%2==0){
			fireX = this.fireposX2;
		}else{
			fireX = -this.fireposX2;
		}
		String model = "advancearmy:textures/entity/bullet/aim9x.obj";
		String tex = "advancearmy:textures/entity/bullet/aim9x.png";

		String fx2 = "SAMissileSmoke";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		Entity locktarget = null;
		if(this.getFirstSeat() != null && this.getFirstSeat().mitarget!=null){
			locktarget = this.getFirstSeat().mitarget;
		}else{
			locktarget = this.getTarget();
		}
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, null, fx2, firesound2,
		1F, fireX,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.getYRot(), this.turretPitch,
		35, 3F, 1.5F, 2, false, 1, 0.01F, 250, 0);
	}
}
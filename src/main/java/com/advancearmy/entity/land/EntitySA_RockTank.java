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

import net.minecraft.world.entity.Mob;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import advancearmy.init.ModEntities;
import net.minecraft.world.level.levelgen.Heightmap;

public class EntitySA_RockTank extends EntitySA_LandBase{
	public EntitySA_RockTank(EntityType<? extends EntitySA_RockTank> sodier, Level worldIn) {
		super(sodier, worldIn);
		startShield=true;
		seatPosX[0] = 1.2F;
		seatPosY[0] = 1.6F;
		seatPosZ[0] = 2F;
		seatTurret[0] = true;
		seatHide[0] = true;
		seatMaxCount = 1;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/tanklaser.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.w1name = Component.translatable("Mirage Laser").getString();
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.w1recoilp = 0;
		this.w1recoilr = 6;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -15;
		this.turretPitchMin = 15;
        this.MoveSpeed = 0.06F;
        this.turnSpeed = 4F;
		this.turretSpeed = 0.6F;
        this.throttleMax = 5F;
		this.throttleMin = -4F;
		this.thFrontSpeed = 1F;
		this.thBackSpeed = -1F;
		this.setMaxUpStep(1.5F);
		canFloat=true;
		this.armor_front = 65;
		this.armor_side = 50;
		this.armor_back = 50;
		this.armor_top = 20;
		this.armor_bottom = 20;
		
		this.w1barrelsize = 0.1F;
		this.ammo1=5;
		this.fireposX1 = 0;
		this.fireposY1 = 1.5F;
		this.fireposZ1 = 3F;
		this.firebaseZ = 0F;
		this.icon1tex = ResourceLocation.tryParse("advancearmy:textures/hud/rchead.png");
		this.icon2tex = ResourceLocation.tryParse("advancearmy:textures/hud/rcbody.png");

		this.magazine = 1;
		this.reload_time1 = 80;
		//this.reloadSound1 = SASoundEvent.reload_m1a2.get();
		this.firesound1 = SASoundEvent.moqiuli.get();
		
		this.magazine2 = 1;
		this.reload_time2 = 400;
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		this.firesound2 = SoundEvents.BEACON_ACTIVATE;
		
		this.canNightV=true;
		this.startsound = SASoundEvent.start_m1a2.get();
		this.movesound = SASoundEvent.mirage_move.get();
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/rcwp.png";
		this.w2icon="advancearmy:textures/item/item_spawn_rctank.png";
		this.w3icon="wmlib:textures/hud/cloud.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		custom_weapon2=true;
	}

	public EntitySA_RockTank(PlayMessages.SpawnEntity packet, Level worldIn) {//
		super(ModEntities.ENTITY_RCTANK.get(), worldIn);
	}
	public Vec2 getLockVector() {
	  return new Vec2(this.turretPitch, this.turretYaw);
	}

	LivingEntity lockTarget = null;
	LivingEntity rangeTarget = null;
	int times;
	int cools;
	int block_height;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			block_height = 2+this.level().getHeight(Heightmap.Types.WORLD_SURFACE,(int)this.getX(),(int)this.getZ());
			if(this.getY()<block_height){
				Vec3 vector3d = this.getDeltaMovement();
				this.setDeltaMovement(vector3d.x, 0.05D, vector3d.z);
			}
			if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null) {
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				if(seat.fire2){
					if(this.getRemain2()>0){
						this.weaponActive2();
						this.setRemain2(0);
					}
					seat.fire2 = false;
				}
			}
			if(this.getTargetType()>0){
				if(this.getTargetType()>1){
					if(this.getTarget()!=null && this.isAttacking() && this.getHealth()<this.getMaxHealth()*0.75F){
						if(this.getRemain2()>0){
							this.weaponActive2();
							this.setRemain2(0);
						}
					}
				}
			}
			
			if(startShield){
				if(times>0){
					if(this.getArmyType2()!=1)this.setArmyType2(1);
					--times;
					++cools;
					if(cools>20){
						/*List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().inflate(18D, 10.0D, 18D));
						for (Entity ent : entities) {
							if(ent instanceof LivingEntity){
								LivingEntity living = (LivingEntity)ent;
								boolean buff = false;
								if(this.getTeam()!=null && this.getTeam() == living.getTeam())buff = true;
								if(living instanceof TamableAnimal){
									TamableAnimal soldier = (TamableAnimal) living;
									if(soldier.getOwner() == this.getOwner()){
										buff = true;
									}
								}
								if(buff){
									//living.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 50,5));
									living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 50,5));
								}
							}
						}*/
						this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 50,5));
						this.playSound(SoundEvents.BEACON_AMBIENT, 5.0F, 1.0F);
						cools=0;
					}
				}else{
					startShield=false;
					this.setArmyType2(0);
				}
			}else{
				//if(this.getArmyType2()>0)this.setArmyType2(0);
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
		}
	}

	public void weaponActive2(){
		this.playSound(firesound2, 3.0F, 1.0F);
		this.startShield=true;
		this.setArmyType2(1);
		times=200;
	}
	
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
		Vec3 locken = Vec3.directionFromRotation(this.getLockVector());//getLookAngle
		int range = 3;
		float ix = 0;
		float iy = 0;
		float iz = 0;
		boolean stop = false;
		int pierce = 0;
		this.playSound(firesound1, 5.0F, 1.0F);
		for(int xxx = 0; xxx < 120; ++xxx) {
			ix = (float)(this.getX()+xx11 + locken.x * xxx);
			iy = (float)(this.getY()+this.fireposY1+base + locken.y * xxx);
			iz = (float)(this.getZ()+zz11 + locken.z * xxx);
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
									ve.hurt(this.damageSources().fellOutOfWorld(), 80);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(this.damageSources().fellOutOfWorld(), 80);
								}
								stop = true;
								ix=(float)lockTarget.getX();
								iy=(float)lockTarget.getY();
								iz=(float)lockTarget.getZ();
								break;
							}
						}
					}
				}
				if(stop)break;
			}
		}
		WMExplosionBase.createExplosionDamage(this, ix, iy+1.5D, iz, 40, 8, false,  false);
		//if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("MirageHit", null, ix, iy+1.5D, iz, 0, 0, 0, 1.5F);
		MessageTrail messageBulletTrail = new MessageTrail(true, 8,"advancearmy:textures/entity/flash/ember_beam",
		this.getX()+xx11, this.getY()+this.fireposY1+base, this.getZ()+zz11, 
		this.getDeltaMovement().x, this.getDeltaMovement().z, 
		ix, iy, iz, 20F, 1F);
		PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messageBulletTrail);
	}
}
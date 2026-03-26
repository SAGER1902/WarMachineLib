package advancearmy.entity.mob;

import java.util.List;

import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
import advancearmy.event.SASoundEvent;
import advancearmy.init.ModEntities;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Team;


import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;

import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*import com.mrcrayfish.guns.common.Gun;
//import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IAmmo;
import com.mrcrayfish.guns.init.ModSounds;*/

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import java.util.function.Predicate;

import advancearmy.entity.EntitySA_Seat;
import wmlib.api.ITool;
import advancearmy.entity.ai.AI_EntityWeapon;

import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.MobSearchTargetGoalSA;
import wmlib.common.living.ai.LivingLockGoal;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import wmlib.api.IEnemy;
import wmlib.api.ITool;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageSoldierAnim;
import net.minecraftforge.network.PacketDistributor;
import wmlib.api.IPara;
import wmlib.api.IHealthBar;
import wmlib.api.IAnimPacket;
import advancearmy.util.TargetSelect;
public abstract class EntityMobSquadBase extends EntityMobSoldierBase implements IEnemy,IHealthBar,IPara,IAnimPacket{
	public EntityMobSquadBase(EntityType<? extends EntityMobSquadBase> sodier, Level worldIn) {
		super(sodier, worldIn);
		this.setMaxUpStep(1.5F);
	}

    private static final EntityDataAccessor<Integer> weaponid = SynchedEntityData.<Integer>defineId(EntityMobSquadBase.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound){
		super.addAdditionalSaveData(compound);
		compound.putInt("weaponid", getWeaponId());
	}
	public void readAdditionalSaveData(CompoundTag compound){
	   super.readAdditionalSaveData(compound);
		this.setWeaponId(compound.getInt("weaponid"));
	}
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(weaponid, Integer.valueOf(0));
	}
	public int getWeaponId() {
		return ((this.entityData.get(weaponid)).intValue());
	}
	public void setWeaponId(int stack) {
		this.entityData.set(weaponid, Integer.valueOf(stack));
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoalSA(this, 1.0D, 1.0000001E-5F));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(6, new LivingLockGoal(this, 1.0D, true));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new MobSearchTargetGoalSA<>(this, Mob.class, 10, 75F, false, false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new MobSearchTargetGoalSA<>(this, Player.class, 10, 75F, false, false, (attackentity) -> {return this.CanAttack(attackentity);}));
	}
	
    public boolean CanAttack(Entity entity){
		return TargetSelect.mobSoldierCanAttack(this,entity,this.attack_range_min,this.attack_height_min,this.attack_height_max);
    }
	int alertTime = 0;
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if(entity instanceof LivingEntity){
				LivingEntity entity1 = (LivingEntity) entity;
				boolean flag = this.getSensing().hasLineOfSight(entity1);
				if(this.getVehicle()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null){
					return false;
				}else{
					if(entity instanceof IEnemy){
						return false;
					}else{
						if(this.distanceToSqr(entity1)>16D && flag){
							if(this.groundtime>50){
								this.setRemain2(3);
								this.groundtime = 0;
							}
							if(this.distanceToSqr(entity1)<this.attack_range_max*this.attack_range_max){
								this.setTarget(entity1);
							}else{
								if(this.getTarget()==null && alertTime>80){
									int i1 = (int)entity.getX() + this.random.nextInt(10)-5;
									int k1 = (int)entity.getZ() + this.random.nextInt(10)-5;
									List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(this.attack_range_max, this.attack_range_max, this.attack_range_max));
									for(int k2 = 0; k2 < list.size(); ++k2) {
										Entity ent = list.get(k2);
										if(ent instanceof EntityMobSoldierBase){
											EntityMobSoldierBase unit = (EntityMobSoldierBase)ent;
											if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMoveType()!=2){
												unit.setMovePosX(i1);
												unit.setMovePosZ(k1);
												unit.setMoveType(4);
											}
										}
										if(ent instanceof WeaponVehicleBase){
											WeaponVehicleBase unit = (WeaponVehicleBase)ent;
											if(unit.getTargetType()==2 && unit.getTarget()==null){
												if(unit.getHealth()>0 && unit.getMoveType()!=2){
													unit.setMoveType(4);
													unit.setMovePosX(i1);
													unit.setMovePosZ(k1);
													break;
												}
											}
										}
									}
									if(this.getMoveType()!=2){
										this.setMovePosX(i1);
										this.setMovePosZ(k1);
										this.setMoveType(4);
									}
									alertTime=0;
								}
							}
						}
						if(this.getRemain2()==1)par2 = par2*0.4F;//
						return super.hurt(source, par2);
					}
				}
			}else{
				if(this.getRemain2()==1)par2 = par2*0.4F;//
				return super.hurt(source, par2);
			}
		}else {
			if(this.getRemain2()==1)par2 = par2*0.4F;//
			return super.hurt(source, par2);
		}
    }
	
	public EntityDimensions dimensions_s;
	public void setSize(float w,float h){
		dimensions_s = EntityDimensions.scalable(w,h);
		double d0 = (double)dimensions_s.width / 2.0D;
        this.setBoundingBox(new AABB(this.getX() - d0, this.getY(), this.getZ() - d0, this.getX() + d0, this.getY() + (double)dimensions_s.height, this.getZ() + d0));
	}
	
	public void setAnim(int x){
		if(x==3)this.anim1=0;
		if(x==4)this.anim2=0;
	}
    public void setAnimFire(int id)
    {
        if(this != null && !this.level().isClientSide)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageSoldierAnim(this.getId(), id));
        }
    }
	public int w1cycle;
	public int ammo1;
	protected Entity find_seat;
	public boolean sit_aim = false;//坐下
	public int ground_time = 50;
	public int groundtime =50;
	public float height = 1.8F;
	//public float cooltime6 = 0;
	public Vec3 motions = this.getDeltaMovement();
	public int fire_tick = 46;
	public int find_time = 0;	 
	public static int gun_count1 = 0;
	public int guncyle = 0;
	
	public float fireposX = 0.5F;
	public float fireposY = 1.5F;
	public float fireposZ = 1F;
	public float firebaseX = 0;
	public float firebaseZ = 0;
	public int bulletid = 0;
	public int bullettype = 0;
	public int bulletdamage = 5;
	public int bulletcount = 1;
	public int bullettime = 50;
	public int reloadSoundStart1 = 20;
	public float bulletspeed = 4;
	public float bulletspread = 2;
	public float bulletexp = 0;
	public float bulletgravity = 0.01F;
	public boolean bulletdestroy = false;
	public boolean weaponcross = false;
	public String bulletmodel1 = "advancearmy:textures/entity/bullet/bullet.obj";
	public String bullettex1 = "advancearmy:textures/entity/bullet/bullet.png";
	public String firefx1 = "SmokeGun";
	public String bulletfx1 = null;
	public SoundEvent reloadSound1 = SASoundEvent.reload1.get();
	public SoundEvent firesound1;
	public void setWeapon(int wpid,int id,
		String model, String tex, String fx1, String fx2, SoundEvent sound, 
		float w, float h, float z, float bx, float bz,
		int damage, float speed, float recoil, float ex, boolean extrue, int count,  float gra, int maxtime, int typeid){
		this.bulletid = id;
		this.bullettype = typeid;
		this.bulletdamage = damage;
		this.bulletspeed = speed;
		this.bulletspread = recoil;
		this.bulletexp = ex;
		this.bulletdestroy = extrue;
		this.bulletcount = count;
		this.bulletgravity = gra;
		this.bullettime = maxtime;
		this.bulletmodel1 = model;
		this.bullettex1 = tex;
		this.firefx1 = fx1;
		this.bulletfx1 = fx2;
		this.firesound1 = sound;
		this.fireposX = w;
		this.fireposY = h;
		this.fireposZ = z;
		this.firebaseX = bx;
		this.firebaseZ = bz;
	}
	
	public int weaponidmax = 6;
	public int soldierType = 0;//1 assult 2 recon 3 engineer 4 support 5 medic
	public int changeWeaponId=0;
	public int mainWeaponId=0;
	public boolean needaim = false;
	public float move_type = 0;
	public float movecool = 0;
	public boolean hide = false;
	public boolean canfire = false;
	public boolean stand = false;
	boolean cheack = true;
	
	public void weaponActive1() {}
	public void weaponActive2() {}
	
	int showbartime = 0;
	public boolean isShow(){
		return this.showbartime>0;
	}
	public int getBarType(){
		return 1;
	}
	public LivingEntity getBarOwner(){
		return null;
	}
	
	public void setDrop(){
		this.canPara = true;
	}
	public boolean isDrop(){
		return this.canPara && this.getVehicle()==null && !canDrop;
	}
	
	public boolean fastRid = false;
	public void specialAttack(double w, double h, double z, float bure, float speed, LivingEntity target){}
	public int special_cool = 0;
	
	public boolean canPara = false;
	public boolean canDrop = false;
	public void tick() {
		super.tick();
		if(canPara && !canDrop){
			if(!this.onGround()){
				Vec3 vector3d = this.getDeltaMovement();
				this.setDeltaMovement(vector3d.x, -0.5D, vector3d.z);
				this.fallDistance = 0.0F;
			}else{
				canPara =false;
			}
		}
		if(this.hurtTime>0){
			if(showbartime<1)showbartime = 70;
		}
		if(showbartime>0)--showbartime;
		if(movecool<100)++movecool;
		if (this.getVehicle() != null && this.getVehicle() instanceof EntityWMSeat) {
			this.canPara = true;
			EntityWMSeat seat = (EntityWMSeat) this.getVehicle();
			this.attack_range_max = seat.attack_range_max;
			this.attack_range_min = seat.attack_range_min;
			this.attack_height_max = seat.attack_height_max;
			this.attack_height_min = seat.attack_height_min;
			this.stand=seat.stand;
			this.hide=seat.seatHide;
			if(seat.seatCanFire) {
				this.canfire = true;
			}else{
				this.canfire = false;
				this.setMoveType(1);
			}
		}else{
			this.hide = false;
			this.canfire = true;
		}
		if(alertTime<100)++alertTime;
		if(guncyle<50)++guncyle;
		if(cooltime < 200)++cooltime;
		if(gun_count1 < 200)++gun_count1;
		if(cooltime6<50)++cooltime6;
		if(this.getHealth()>0 && this.getHealth()<this.getMaxHealth() && this.cooltime6>45){
			this.setHealth(this.getHealth()+1);
			this.cooltime6 = 0;
		}
		
		if(this.getRemain1() <= 0){
			++reload1;
			if(reload1 == reload_time1 - reloadSoundStart1)this.playSound(reloadSound1, 2.0F, 1.0F);
			if(reload1 >= reload_time1){
				this.setRemain1(this.magazine);
				this.aim_time = 0;
				reload1 = 0;
			}
		}
		
		if(special_cool<400)++special_cool;
		
		if(this.level().random.nextInt(6) == 0 && this.getVehicle()==null && !this.isPassenger() && this.getMoveType()!=3||fastRid){//上车
			if((soldierType==0||soldierType==3||soldierType==4)&&canfire){
				if(special_cool>350 && this.getTarget()!=null && this.distanceToSqr(this.getTarget())>16){
					specialAttack(0,this.height,0.8F, 2.5F, 1.5F, this.getTarget());
					this.swing(InteractionHand.MAIN_HAND);
					special_cool = 0;
				}
			}
			
			int cheackCount = 0;
			double nearestDistance = 400D;
			Entity nearestEntity = null;
			List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().inflate(18D, 18.0D, 18D));
			for (Entity target : entities) {
				if (TargetSelect.canDriveEntity(target, true)) {
					double dist = this.distanceToSqr(target); // 平方距离，避免开方
					if (dist < nearestDistance) {
						nearestDistance = dist;
						nearestEntity = target;
						++cheackCount;
						if(cheackCount>10)break;
					}
				}
			}
			this.find_seat = nearestEntity;
			
			if(this.getVehicle()==null&&this.find_seat!=null){
				if (this.distanceToSqr(this.find_seat) > 36){
					this.getNavigation().moveTo(this.find_seat.getX(), this.find_seat.getY(), this.find_seat.getZ(), 1.6);
				}else{
					this.getNavigation().stop();
					this.playSound(SoundEvents.IRON_DOOR_OPEN, 3.0F, 1.0F);
					if (!this.level().isClientSide){
						this.startRiding(this.find_seat);
					}
					this.playSound(SoundEvents.IRON_DOOR_CLOSE, 3.0F, 1.0F);
					fastRid=false;
				}
			}
		}
	}
	public void moveway(EntityMobSquadBase entity, float moveSpeed, double max) {
		if (entity.getTarget() != null) {
			LivingEntity living = entity.getTarget();
			if(living.isAlive() && living!=null){
				boolean flag = entity.getSensing().hasLineOfSight(living);
				if(!flag)entity.setAttacking(false);
				if (!living.isInvisible()) {//target
					if(living.getHealth() > 0.0F  && entity.getMoveType()!=2){
						float height = living.getBbHeight()*0.75F;
						if(height>3)height=3;
						double dx = living.getX()+living.getDeltaMovement().x - entity.getX();
						double dz = living.getZ()+living.getDeltaMovement().y - entity.getZ();
						double d1 = entity.getEyeY() - living.getY() - height -living.getDeltaMovement().y;
						double dis = Math.sqrt(dx * dx + dz * dz);
						if (flag){
							float f11 = (float) (-(Math.atan2(d1, dis) * 180.0D / Math.PI));
							float f12 = -((float) Math.atan2(dx, dz)) * 180.0F / (float) Math.PI;
							entity.setYRot(f12);
							entity.yRotO = entity.getYRot();
							entity.setYHeadRot(f12);
							entity.setXRot(-f11);
							entity.setAttacking(true);
							if(entity.getMoveType()==4)entity.setMoveType(1);
						}
						if(entity.getVehicle()==null && !entity.isPassenger() && entity.getMoveType()!=3){
							if (dis>max*0.5F && entity.soldierType!=2||dis>max) {//
								if(entity.getMoveType()==1)MoveS(entity, moveSpeed, living.getX(), living.getY(), living.getZ(), flag);
							}else{
								if(entity.move_type>1 && entity.move_type!=5)MoveS(entity, moveSpeed, living.getX(), living.getY(), living.getZ(), flag);
							}
							if(dis < 4&&(entity.getHealth()<entity.getMaxHealth()*0.5F||entity.soldierType==2)){//
								MoveS(entity, -moveSpeed, living.getX(), living.getY(), living.getZ(), flag);
							}
							if(entity.move_type==1 && entity.cooltime6>40){
								Vec3 vector3d = entity.getDeltaMovement();
								entity.setDeltaMovement(3F*vector3d.x, 0.3D+entity.level().random.nextInt(2)*moveSpeed, 3F*vector3d.z);
								entity.cooltime6 = 0;
							}
						}
					}
				}
			}
		}
		if(entity.getVehicle()==null && !entity.isPassenger()){
			if(entity.getMovePosX()==0&&entity.getMovePosZ()==0){
				entity.setMovePosX((int)entity.getX());
				entity.setMovePosZ((int)entity.getZ());
			}
			if(entity.getMovePosX()!=0&&entity.getMovePosZ()!=0 && (entity.getMoveType()==2||!entity.isAttacking())){
				double dx = entity.getMovePosX() - entity.getX();
				double dz = entity.getMovePosZ() - entity.getZ();
				double dis = Math.sqrt(dx * dx + dz * dz);
				float f12 = -((float) Math.atan2(dx, dz)) * 180.0F / (float) Math.PI;
				int min = 5;
				if(entity.getMoveType()==3)min=1;
				if(entity.getMoveType()==1)min=18;
				if(dis>min){
					if(entity.move_type!=0)entity.move_type=0;
					if(entity.find_seat!=null)entity.find_seat=null;
					MoveS(entity, moveSpeed, entity.getMovePosX(), entity.getMovePosY(), entity.getMovePosZ(), false);
				}else{
					if(entity.getMoveType()==2){
						entity.setMoveType(3);
					}else{
						entity.setMoveType(1);
					}
				}
			}
		}else{
			if(entity.getMovePosX()!=0||entity.getMovePosZ()!=0){
				entity.setMovePosX(0);
				entity.setMovePosZ(0);
			}
		}
	}
	public void MoveS(EntityMobSquadBase entity, double speed, double targetx, double targety, double targetz, boolean hasLineOfSight){
		double d5 = targetx - entity.getX();
		double d7 = targetz - entity.getZ();
		float yawoffset = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
		float yaw = yawoffset * (2 * (float) Math.PI / 360);
		double mox = 0;
		double moy = -1D;
		double moz = 0;

		if(entity.getMoveType()!=2){
			if(entity.move_type == 2) {
				mox -= Mth.sin(yaw + 1.57F) * speed;
				moz += Mth.cos(yaw + 1.57F) * speed;
			}else if(entity.move_type == 3) {
				mox -= Mth.sin(yaw - 1.57F) * speed;
				moz += Mth.cos(yaw - 1.57F) * speed;
			}else if(entity.move_type == 4 && speed>0) {
				mox -= Mth.sin(yaw) * speed*-0.7F;
				moz += Mth.cos(yaw) * speed*-0.7F;
			}else{
				mox -= Mth.sin(yaw) * speed;
				moz += Mth.cos(yaw) * speed;
			}
		}else{
			mox -= Mth.sin(yaw) * speed;
			moz += Mth.cos(yaw) * speed;
		}
		if((hasLineOfSight || speed<0) || entity.move_type>0 && entity.move_type<5 && entity.getMoveType()!=2){
			entity.setDeltaMovement(mox, moy, moz);
		}else{
			entity.getNavigation().moveTo(targetx, targety, targetz, speed*8);
		}
	}
}
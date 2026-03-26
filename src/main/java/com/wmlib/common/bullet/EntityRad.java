package wmlib.common.bullet;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Mob;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.world.phys.Vec3;
import net.minecraft.network.syncher.EntityDataAccessor;  
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
import wmlib.WarMachineLib;
import safx.SagerFX;
import net.minecraft.resources.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.api.ITool;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import wmlib.api.IRadSoldier;
import net.minecraft.world.scores.Team;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import wmlib.init.WMModEntities;
import advancearmy.event.SASoundEvent;
import net.minecraft.world.entity.monster.Monster;
public class EntityRad extends Mob implements ITool{
	public EntityRad(EntityType<? extends EntityRad> p_i48549_1_, Level p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.noCulling = true;
	}
	public EntityRad(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(WMModEntities.ENTITY_RAD.get(), worldIn);
	}
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1);
    }
	/*public void checkDespawn() {
	}*/
	public boolean canBeCollidedWith() {//
		return false;
	}
	public boolean isFriendRad = false;
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}
	public int max_range = 35;
	public int range = 25;
	
    public float iii;
	public float size;
	static boolean glow = true;
	static float shock =0;

	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public int stay_time = 0;
	public int max_time = 500;
	public float summontime = 0;
	public float cooltime6 = 0;
	
	public boolean NotFriend(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && entity!=this){//Living
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = this.getTeam();
			Team team1 = entity1.getTeam();
			boolean canattack = true;
			/*if(entity instanceof TamableAnimal){
				TamableAnimal soldier = (TamableAnimal)entity;
				if(this.getOwner()!=null && this.getOwner()==soldier.getOwner()){
					canattack=false;
				}
			}*/
			if(team != null && team1 == team)canattack= false;
			/*if(this.getTargetType()==2){
				if(entity instanceof Enemy && ((LivingEntity) entity).getHealth() > 0.0F && (team == null||team != team1))canattack= false;
			}*/
			return canattack;
    	}else{
			return false;
		}
	}
	@Override
	public boolean canCollideWith(Entity entity) {
		return false;  // 防止与任何实体碰撞
	}
	private static final EntityDataAccessor<Integer> stayTime = SynchedEntityData.<Integer>defineId(EntityRad.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> RadType = SynchedEntityData.<Integer>defineId(EntityRad.class, EntityDataSerializers.INT);
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("RadType", this.getRadType());
		compound.putInt("stayTime", this.getStayTime());
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setRadType(compound.getInt("RadType"));
	   this.setStayTime(compound.getInt("stayTime"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(RadType, Integer.valueOf(0));
		this.entityData.define(stayTime, Integer.valueOf(0));
	}
	public int getRadType() {
	return ((this.entityData.get(RadType)).intValue());
	}
	public void setRadType(int stack) {
	this.entityData.set(RadType, Integer.valueOf(stack));
	}
	
	public int getStayTime() {
	return ((this.entityData.get(stayTime)).intValue());
	}
	public void setStayTime(int stack) {
	this.entityData.set(stayTime, Integer.valueOf(stack));
	}
	
	int particeTime;
	public void aiStep() {
		this.setDeltaMovement(0, 0, 0);
		if(!glow && iii<50F){
			++iii;
		}else{
			glow = true;
		}
		if(glow && iii>0){
			--iii;
		}else{
			glow = false;
		}
		
		if (this.isAlive()){
			this.setStayTime(this.getStayTime()+1);
			size=(max_time*1.5F-this.getStayTime())/(float)max_time;
			range = (int)(max_range*size);
			if(this.getStayTime()>max_time)this.discard();
			if(this.getRadType()==2){
				if(ModList.get().isLoaded("advancearmy")){
					if(this.getStayTime()==4)this.playSound(SASoundEvent.nuke2_exp.get(), 8.0F, 1.0F);
					if(this.getStayTime()>5 && this.getStayTime()%10==0)this.playSound(SASoundEvent.nuke2_rad.get(), 6.0F, 1.0F);
				}
				if(this.getStayTime()==4 && ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("NukeExplosion2", null, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D, 0.8F + this.range * 0.05F);
				if ((this.getStayTime()==5||this.getStayTime()%50==0) && ModList.get().isLoaded("safx")) {
					//SagerFX.proxy.createFX("TankSmoke", null, this.getX(), this.getY(), this.getZ(), 0, 0, 0, 2);
					SagerFX.proxy.createFX("NukeExplosionSmoke", null, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D, 0.8F + this.range * 0.05F);
				}
			}
			if(summontime<300)++summontime;
			if(summontime>2){//
				int count = 0;
				int ve = 0;
				float height = range*0.2F;
				if(this.getRadType()==2){
					height=80;
					range=50;
				}
				List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(range, height, range));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					{
						if((ent instanceof LivingEntity && !this.isAggressive()||NotFriend(ent)) && !(ent instanceof IRadSoldier)){
							LivingEntity living = (LivingEntity)ent;
							if(this.getRadType()==2){
								double dx = this.getX() - living.getX();
								double dy = this.getY()+5 - living.getY();
								double dz = this.getZ() - living.getZ();
								double dis = Math.sqrt(dx * dx + dy * dy + dz * dz);
								//double strength = 2 * (1.0 - (dis / range));
								living.hurt(this.damageSources().inWall(), 20*30/(float)dis);
								if(dis<8){
									living.moveTo(this.getX(), this.getY()+5, this.getZ());
									living.hurt(this.damageSources().fellOutOfWorld(), 100);
									living.hurt(this.damageSources().outOfBorder(), 100);
									living.hurt(this.damageSources().genericKill(), 100);
								}else{
									lockTo(living,this.getX(),this.getY()+5,this.getZ());
								}
								double lookX = this.getX() - living.getX();
								double lookZ = this.getZ() - living.getZ();
								float targetYaw = (float)(Math.atan2(lookZ, lookX) * (180 / Math.PI)) - 90.0f;
								if(targetYaw>360||targetYaw<-360)targetYaw=0;
								living.yHeadRot=targetYaw;
								living.setYRot(targetYaw);
								living.yRotO = targetYaw;
							}else{
								if(living.getHealth()>0){
									if(this.level().random.nextInt(2)==1){
										living.hurt(this.damageSources().wither(), size*(2+0.01F*(living.getMaxHealth()-living.getHealth())));
									}else{
										living.hurt(this.damageSources().inFire(), 5*size);
									}
								}
							}
						}
					}
				}
				summontime = 0;
			}
		}
		super.aiStep();
	}
	
	public void lockTo(Entity ent, double x, double y, double z){
		Vec3 vector3d = ent.getDeltaMovement();
		//ent.setDeltaMovement(vector3d.x,0.5,vector3d.z);
		double d6 = x - ent.getX();
        double d7 = y - ent.getY();
        double d4 = z - ent.getZ();
        double d5 = (double)Math.sqrt(d6 * d6 + d7 * d7 + d4 * d4);
		double dx = Math.abs(ent.getX() - ent.xo);
		double dz = Math.abs(ent.getZ() - ent.zo);
		double targetDeltaX;
		double targetDeltaY;
		double targetDeltaZ;
        if (d5 == 0.0D)
        {
            targetDeltaX = 0.0D;
            targetDeltaY = 0.0D;
            targetDeltaZ = 0.0D;
        }
        else
        {
        	double speeded = 6F;
            targetDeltaX = d6 / d5 * speeded;
            targetDeltaY = 3*d7 / d5 * speeded;
            targetDeltaZ = d4 / d5 * speeded;
        }
		targetDeltaX = Mth.clamp(targetDeltaX * 1.025D, -1.0D, 1.0D);
		targetDeltaY = Mth.clamp(targetDeltaY * 1.025D, -1.0D, 1.0D);
		targetDeltaZ = Mth.clamp(targetDeltaZ * 1.025D, -1.0D, 1.0D);
		ent.setDeltaMovement(vector3d.add((targetDeltaX - vector3d.x) * 0.2D, (targetDeltaY - vector3d.y) * 0.2D, (targetDeltaZ - vector3d.z) * 0.2D));
	}
}

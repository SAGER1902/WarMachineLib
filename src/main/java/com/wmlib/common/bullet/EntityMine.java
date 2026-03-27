package wmlib.common.bullet;
import net.minecraftforge.fml.ModList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import wmlib.WarMachineLib;
import safx.SagerFX;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.util.math.vector.Vector3d;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import java.util.UUID;
import wmlib.common.world.WMExplosionBase;
import wmlib.api.ITool;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.server.ServerWorld;
import java.util.List;
import net.minecraft.util.SoundEvents;
public class EntityMine extends CreatureEntity implements ITool{
	public EntityMine(EntityType<? extends EntityMine> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	}
	public EntityMine(FMLPlayMessages.SpawnEntity packet, World worldIn) { 
		super(WarMachineLib.ENTITY_MINE, worldIn); 
	}

   private UUID ownerUUID;
   private int ownerNetworkId;
   private boolean leftOwner;
   
   public void setOwner(@Nullable Entity p_212361_1_) {
      if (p_212361_1_ != null) {
         this.ownerUUID = p_212361_1_.getUUID();
         this.ownerNetworkId = p_212361_1_.getId();
      }
   }
   
   @Nullable
   public Entity getOwner() {
      if (this.ownerUUID != null && this.level instanceof ServerWorld) {
         return ((ServerWorld)this.level).getEntity(this.ownerUUID);
      } else {
         return this.ownerNetworkId != 0 ? this.level.getEntity(this.ownerNetworkId) : null;
      }
   }

	private static final DataParameter<Integer> BoxID = EntityDataManager.<Integer>defineId(EntityMine.class, DataSerializers.INT);
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("BoxID", this.getMineID());
		
      if (this.ownerUUID != null) {
         compound.putUUID("Owner", this.ownerUUID);
      }
      if (this.leftOwner) {
         compound.putBoolean("LeftOwner", true);
      }
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setMineID(compound.getInt("BoxID"));
		if (compound.hasUUID("Owner")) {
		 this.ownerUUID = compound.getUUID("Owner");
		}
		this.leftOwner = compound.getBoolean("LeftOwner");
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(BoxID, Integer.valueOf(0));
	}
	public int getMineID() {
		return ((this.entityData.get(BoxID)).intValue());
	}
	public void setMineID(int stack) {
	this.entityData.set(BoxID, Integer.valueOf(stack));
	}

   public void shootFromRotation(Entity entity, float pitch, float yaw, float count, float speed, float bure) {
		float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
		float f1 = -MathHelper.sin((pitch + count) * ((float)Math.PI / 180F));
		float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
		if(speed>4F)speed = 4F;
		this.shoot((double)f, (double)f1, (double)f2, speed, bure);
   }
   
	public void shoot(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_) {
		Vector3d vector3d = (new Vector3d(p_70186_1_, p_70186_3_, p_70186_5_)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)p_70186_8_, this.random.nextGaussian() * (double)0.0075F * (double)p_70186_8_, this.random.nextGaussian() * (double)0.0075F * (double)p_70186_8_).scale((double)p_70186_7_);
		this.setDeltaMovement(vector3d);
		float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
		float x = (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI));
		this.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
		this.xRot = x;
		this.yRotO = this.yRot;
		this.xRotO = this.xRot;
	}

   private boolean checkLeftOwner() {
      Entity entity = this.getOwner();
      if (entity != null) {
         for(Entity entity1 : this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), (p_234613_0_) -> {
            return !p_234613_0_.isSpectator() && p_234613_0_.isPickable();
         })) {
            if (entity1.getRootVehicle() == entity.getRootVehicle()) {
               return false;
            }
         }
      }
      return true;
   }
   
	public Team getTeam() {
		if (this.getOwner()!=null) {
			Entity livingentity = this.getOwner();
			if (livingentity != null) {
				return livingentity.getTeam();
			}
		}
		return super.getTeam();
	}

	public boolean NotFriend(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && entity!=this && entity!=this.getOwner()){//Living
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = this.getTeam();
			Team team1 = entity1.getTeam();
			boolean canattack = true;
			
			if(entity instanceof TameableEntity){
				TameableEntity soldier = (TameableEntity)entity;
				if(this.getOwner()!=null && this.getOwner()==soldier.getOwner()){
					canattack=false;
				}
			}
			
			if(team != null && team1 != team && team1 != null){
				
			}else/* if(entity instanceof IMob)*/{
				/*if(this.getMineID()==2||this.getMineID()==4){
					canattack= false;
				}*/
				canattack= false;
			}
			
			if(this.getMineID()==1||this.getMineID()==2){
				
			}else{
				if(entity.getBbHeight()<2.1F&&entity.getBbWidth()<2.1F)canattack= false;
			}
			return canattack;
    	}else{
			return false;
		}
	}
	
	
	protected void tickDeath() {
	  ++this.deathTime;
	  if (this.deathTime > 1){
		  this.exp();
	  }
	}
	
	public void exp(){
		if (!this.level.isClientSide) {
			if(this.getMineID()==2||this.getMineID()==4){
				if(ModList.get().isLoaded("safx")){
					SagerFX.proxy.createFX("AdvExplosion",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1.2F);
				}
				//this.playSound(SASoundEvent.artillery_impact.get(), 3, 1.0F);
			}else{
				if(ModList.get().isLoaded("safx")){
					SagerFX.proxy.createFX("AdvExplosion",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 3F);
				}
				//this.playSound(SASoundEvent.tank_shell.get(), 5, 1.0F);
			}
			
			this.playSound(SoundEvents.GENERIC_EXPLODE, 3, 1.0F);
			
			if(!ModList.get().isLoaded("safx")){
				this.level.addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
				this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
			if(this.getMineID()==1||this.getMineID()==2){
				WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), 25, 3, false, false);
			}else{
				WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), 250, 5, false, false);
			}
			this.remove();
		}
	}
	
	public void tick() {
		if (!this.leftOwner) {
		 this.leftOwner = this.checkLeftOwner();
		}
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.05D, 0.0D));
		}
		this.move(MoverType.SELF, this.getDeltaMovement());
		/*this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
		if (this.onGround) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
		}*/
		this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
		List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().inflate(2D, 2D, 2D));
		for (Entity target : entities) {
			if(entities !=this && entities instanceof LivingEntity)
			/*if(NotFriend(target))*/{
				exp();
			}
		}
	}
   
   @Override
   public IPacket<?> getAddEntityPacket() {
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

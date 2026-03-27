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

import net.minecraft.util.ResourceLocation;
import wmlib.client.obj.SAObjModel;
public class EntityBlock extends EntityBulletBase {	
	private int fuse = 80;
	public EntityBlock(FMLPlayMessages.SpawnEntity packet, World worldIn) { 
		super(WarMachineLib.ENTITY_BLOCK, worldIn); 
	}
   public EntityBlock(EntityType<? extends EntityBlock> p_i50159_1_, World p_i50159_2_) {
      super(p_i50159_1_, p_i50159_2_);
   }
   
   public EntityBlock(World worldIn, LivingEntity throwerIn) {
      super(WarMachineLib.ENTITY_BLOCK, throwerIn, worldIn);
   }

   public EntityBlock(World worldIn, double x, double y, double z) {
      super(WarMachineLib.ENTITY_BLOCK, x, y, z, worldIn);
   }

   /**
    * Gets the amount of gravity to apply to the thrown entity with each tick.
    */
   protected float getGravityVelocity() {
      return 0.03F;
   }
   
   /**
    * Called to update the entity's position/logic.
    */
   public void tick() {
		if (!this.isNoGravity()) {
			 this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
		  }
		  this.move(MoverType.SELF, this.getDeltaMovement());
		  this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
		  if (this.onGround) {
			 this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
		  }

		--this.fuse;
		if (this.fuse <= 0) {
		  this.remove();
		  if (!this.level.isClientSide) {
			 this.explode();
		  }
		} else {
		  this.updateInWaterStateAndDoFluidPushing();
		  if (this.level.isClientSide) {
			 this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
		  }
		}
   }
   
	protected void explode() {
		float f = 3.0F;
		//this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 3.0F, Explosion.Mode.BREAK);
		this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 4.0F, Explosion.Mode.BREAK);
	}

   /**
    * Called when the arrow hits an entity
    */
   protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
	      super.onHitEntity(p_213868_1_);
	      Entity entity = p_213868_1_.getEntity();
	      entity.invulnerableTime = 0;
	      int i = power;
	      //entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), (float)i);
	      entity.hurt(DamageSource.thrown(this, this.getOwner()), (float)i);
	}
   /**
    * Called when this EntityFireball hits a block or entity.
    */
   protected void onHit(RayTraceResult result) {
      super.onHit(result);
      if (!this.level.isClientSide) {
    	  this.level.broadcastEntityEvent(this, (byte)3);
         if(exlevel > 0) {
        	 //this.world.createExplosion((Entity)null, this.getPosX(), this.getPosY(), this.getPosZ(), exlevel, false, Explosion.Mode.NONE);
        	 this.level.explode(this, this.getX(), this.getY(), this.getZ(), exlevel, false, Explosion.Mode.NONE);
         }
         //this.world.createExplosion((Entity)null, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, false, Explosion.Mode.NONE);
         this.remove();
      }
   }
   
   @Override
   public IPacket<?> getAddEntityPacket() {
	   //return new SSpawnObjectPacket(this);
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

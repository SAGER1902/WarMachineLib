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
/*import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;*/
import wmlib.api.ITool;
public class EntityFlare extends CreatureEntity implements ITool{
	private int fuse = 80;
	public EntityFlare(EntityType<? extends EntityFlare> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	}
	public EntityFlare(FMLPlayMessages.SpawnEntity packet, World worldIn) { 
		super(WarMachineLib.ENTITY_FLARE, worldIn); 
	}
   /*public EntityFlare(EntityType<? extends EntityFlare> p_i50159_1_, World p_i50159_2_) {
      super(p_i50159_1_, p_i50159_2_);
   }
   
   public EntityFlare(World worldIn, LivingEntity throwerIn) {
      super(WarMachineLib.ENTITY_FLARE, throwerIn, worldIn);
   }

   public EntityFlare(World worldIn, double x, double y, double z) {
      super(WarMachineLib.ENTITY_FLARE, x, y, z, worldIn);
   }*/

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


   /**
    * Gets the amount of gravity to apply to the thrown entity with each tick.
    */
   /*protected float getGravityVelocity() {
      return 0.03F;
   }*/
   /**
    * Called to update the entity's position/logic.
    */
	public int time = 100;
	public static final RedstoneParticleData MISSILE = new RedstoneParticleData(1F, 1F, 1F, 4F);
	public void tick() {
		if(time>0)--time;
		if(time<=0)this.remove();
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.02D, 0.0D));
		}
		this.move(MoverType.SELF, this.getDeltaMovement());
		this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
		if (this.onGround) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
		}
		if(ModList.get().isLoaded("safx")){
			if(time==99)SagerFX.proxy.createFXOnEntity("FlamethrowerTrail", this, 1);
		}else{
			if (this.level.isClientSide) {
				this.level.addParticle(ParticleTypes.CLOUD, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
   
   @Override
   public IPacket<?> getAddEntityPacket() {
	   //return new SSpawnObjectPacket(this);
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

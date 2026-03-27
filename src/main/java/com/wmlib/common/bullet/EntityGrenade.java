package wmlib.common.bullet;
import net.minecraftforge.fml.ModList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
import wmlib.common.world.WMExplosionBase;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.particles.BlockParticleData;
import wmlib.common.WMSoundEvent;
import wmlib.api.IEnemy;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import wmlib.common.living.EntityWMSeat;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.particles.RedstoneParticleData;
import safx.SagerFX;
import net.minecraft.util.ResourceLocation;
import wmlib.client.obj.SAObjModel;

import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.api.paz.IZombieEntity;
public class EntityGrenade extends EntityBulletBase {
	public EntityGrenade(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(WarMachineLib.ENTITY_GRENADE, worldIn); 
	}
	
   public EntityGrenade(EntityType<? extends EntityGrenade> p_i50159_1_, World p_i50159_2_) {
      super(p_i50159_1_, p_i50159_2_);

   }
   
   public EntityGrenade(World worldIn, LivingEntity throwerIn) {
      super(WarMachineLib.ENTITY_GRENADE, throwerIn, worldIn);
	  this.shooter = throwerIn;
   }

   public EntityGrenade(World worldIn, double x, double y, double z) {
      super(WarMachineLib.ENTITY_GRENADE, x, y, z, worldIn);
   }

   /**
    * Called to update the entity's position/logic.
    */
	public void tick() {
		super.tick();
	}
   
   
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte p_70103_1_) {
	}
   /**
    * Called when the arrow hits an entity
    */
   protected void onHitEntity(EntityRayTraceResult result) {
		//super.onHitEntity(result);
		float size = 1.0F;
		boolean hurt = true;
		Entity entity = result.getEntity();
		if(entity instanceof EntityWMSeat){
			hurt = false;
		}else
		if(this.shooter != null){
			if(this.shooter instanceof TameableEntity){
				TameableEntity soldier = (TameableEntity) this.shooter;
				if(soldier.getOwner() == entity||soldier.getTeam()==entity.getTeam() && soldier.getTeam()!=null||entity == this.shooter){
					hurt = false;
				}
				if(ModList.get().isLoaded("pvz")){
					if(entity instanceof PVZPlantEntity){
						hurt = false;
					}
				}
			}else if(this.shooter instanceof IEnemy && entity instanceof IEnemy){
				hurt = false;
			}
		}
		if(hurt){
			entity.invulnerableTime = 0;
			if(this.flame)entity.setSecondsOnFire(8);
			int i = power;
			entity.hurt(DamageSource.thrown(this, this.getOwner()), 2);
		}
		this.setDeltaMovement(this.getDeltaMovement().x*0.2F, this.getDeltaMovement().y*0.2F, this.getDeltaMovement().z*0.2F);
		float rx = this.random.nextFloat() - 0.5F;
		float ry = this.random.nextFloat();
		float rz = this.random.nextFloat() - 0.5F;
		this.setDeltaMovement(this.getDeltaMovement().x+rx, ry+this.getDeltaMovement().y - (double)this.getGravity(), this.getDeltaMovement().z+rz);
   }

	public void expblock(BlockState blockstate, BlockPos pos){
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("SmallGrenadeExp", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+this.getExLevel()*0.1F);//AAExplosion
		this.playSound(this.selfExpSound, 1+exlevel*2, 1.0F);
		if (!this.level.isClientSide) {
			WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, exlevel, this.flame, false);
		}
	}

   /**
    * Called when this EntityFireball hits a block or entity.
    */
	public boolean spawn = true;
	protected void onHitBlock(BlockRayTraceResult pos) {
		BlockState blockstate = this.level.getBlockState(pos.getBlockPos());
		if (!blockstate.isAir(this.level, pos.getBlockPos())){
			stopRote=true;
			this.setDeltaMovement(0, 0, 0);
			//this.setPos(d2, d0, d1);
		}
	}
   @Override
   public IPacket<?> getAddEntityPacket() {
	   //return new SSpawnObjectPacket(this);
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

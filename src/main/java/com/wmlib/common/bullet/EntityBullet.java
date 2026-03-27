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
import advancearmy.event.SASoundEvent;
import net.minecraft.util.ResourceLocation;
import wmlib.client.obj.SAObjModel;

import wmlib.common.living.EntityWMVehicleBase;

import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.api.paz.IZombieEntity;
public class EntityBullet extends EntityBulletBase {
	public EntityBullet(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(WarMachineLib.ENTITY_BULLET, worldIn); 
	}
	
   public EntityBullet(EntityType<? extends EntityBullet> p_i50159_1_, World p_i50159_2_) {
      super(p_i50159_1_, p_i50159_2_);

   }
   
   public EntityBullet(World worldIn, LivingEntity throwerIn) {
      super(WarMachineLib.ENTITY_BULLET, throwerIn, worldIn);
	  this.shooter = throwerIn;
   }

   public EntityBullet(World worldIn, double x, double y, double z) {
      super(WarMachineLib.ENTITY_BULLET, x, y, z, worldIn);
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
		if (entity != null){
			if(entity instanceof EntityWMSeat){
				hurt = false;
			}else if(this.shooter != null){
				if(shooter.getTeam()==entity.getTeam() && shooter.getTeam()!=null||entity == this.shooter){
					hurt = false;
				}else if(this.shooter instanceof TameableEntity){
					TameableEntity soldier = (TameableEntity) this.shooter;
					if(soldier.getOwner() == entity){
						hurt = false;
					}
					if(ModList.get().isLoaded("pvz")){
						if(entity instanceof PVZPlantEntity){
							//size=0.5F;
							/*IPAZEntity plant = (IPAZEntity)entity;
							if (plant.getOwnerUUID().isPresent() && soldier.getOwner()!=null) {
								if (plant.getOwnerUUID().get().equals(soldier.getOwner().getUUID())) {
									hurt = false;
								}
							}*/
							hurt = false;
						}
					}
				}else if(this.shooter instanceof IEnemy && (entity instanceof IEnemy||entity instanceof EntityWMVehicleBase && ((EntityWMVehicleBase)entity).getTargetType()==2)){
						hurt = false;
					}
			}
			if(ModList.get().isLoaded("pvz")){
				if(entity instanceof IZombieEntity)size=0.5F;
			}
			if(hurt){
				if(entity instanceof LivingEntity) {
					LivingEntity hitentity = (LivingEntity)entity;
					if(this.getExLevel()>1){
						this.playSound(this.hitEntitySound, 2+this.getExLevel(), 1.0F);
					}else if(ModList.get().isLoaded("advancearmy")){
						if(hitentity.getArmorValue()>5D){
							if(this.random.nextInt(4)==0){
								this.playSound(SASoundEvent.gun_impact_metal.get(), 1.0F, 1.0F);
							}else{
								this.playSound(SASoundEvent.mg_impact_metal.get(), 1.0F, 1.0F);
							}
						}else{
							this.playSound(SASoundEvent.gun_impact_flesh.get(), 1.0F, 1.0F);
						}
					}
				}
				if(this.getExLevel()>1){
					if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("SmallGrenadeExp", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+this.getExLevel()*0.1F);//AAExplosion
					//if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("AAExplosion", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+this.getExLevel()*0.1F);
				}else
				if(this.getBulletType()==1){
					{
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("GunHit", null, this.getX(), this.getY(), this.getZ(), 0.1F, 0.1F, 0.1F, 1.6F);
					}
				}else if(this.getBulletType()==3){
					this.level.addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);//FLAME
				}else if(this.getBulletType()==5){
					this.level.addParticle(RedstoneParticleData.REDSTONE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
				}else{
					{
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("GunHit", null, this.getX(), this.getY(), this.getZ(), 0.1F, 0.1F, 0.1F, 1F+this.getExLevel()*0.4F);
					}
				}
				if(!ModList.get().isLoaded("safx")){
					if(this.getExLevel()==0){
						this.level.addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
					}else{
						if (this.getExLevel() < 1.0F) {
							this.level.addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
							this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
						} else {
							this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
						}
					}
				}
				entity.invulnerableTime = 0;
				if(this.flame)entity.setSecondsOnFire(8);
				int i = power;
				entity.hurt(DamageSource.thrown(this, this.getOwner()), (float)i*size);
				if (!this.level.isClientSide) {
					this.level.broadcastEntityEvent(this, (byte)3);
					if(this.getExLevel() > 0) {
						WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, exlevel, this.flame, false);
					}
					if(this.getBulletType()!=5)this.remove();
				}
			}
		}
   }

	public void expblock(BlockState blockstate, BlockPos pos){
		if(this.getExLevel()>1){
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("SmallGrenadeExp", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+this.getExLevel()*0.1F);//AAExplosion
			this.playSound(this.hitBlockSound, 1+this.getExLevel()*2, 1.0F);
		}else
		if(this.getBulletType()==1){
			//if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("AdvExpSmall", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.5F);
			if (!blockstate.isAir(this.level, pos)) {
				if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("GunExp", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1.5F);
			}else{
				if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("GunHit", null, this.getX(), this.getY(), this.getZ(), 0.1F, 0.1F, 0.1F, 1.6F);
			}
		}else if(this.getBulletType()==3){
			this.level.addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);//FLAME
		}else if(this.getBulletType()==5){
			this.level.addParticle(RedstoneParticleData.REDSTONE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
		}else{
			if (!blockstate.isAir(this.level, pos)) {
				if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("GunExp", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F+this.getExLevel()*0.4F);
			}else{
				if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("GunHit", null, this.getX(), this.getY(), this.getZ(), 0.1F, 0.1F, 0.1F, 1F+this.getExLevel()*0.4F);
			}
			if(ModList.get().isLoaded("advancearmy")){
				if(this.random.nextInt(4)==0){
					this.playSound(SASoundEvent.gun_impact_mud.get(), 1.0F, 1.0F);
				}else{
					this.playSound(SASoundEvent.mg_impact.get(), 1.0F, 1.0F);
				}
			}
		}
		if(!ModList.get().isLoaded("safx")){
			if(this.getExLevel()==0){
				for(int i = 0; i < 3; ++i) {
				   this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), this.getY() + 0.1D, this.getZ() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), 4.0D * ((double)this.random.nextFloat() - 0.5D), 3.5D, ((double)this.random.nextFloat() - 0.5D) * 4.0D);
				}
			}else{
				if (this.getExLevel() > 1.0F) {
					this.level.addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
					this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				} else {
					this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				}
			}
		}
		if (!this.level.isClientSide) {
			if(this.getExLevel() > 0)WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, exlevel, this.flame, false);
		}
	}

   /**
    * Called when this EntityFireball hits a block or entity.
    */
	public boolean spawn = true;
	protected void onHitBlock(BlockRayTraceResult pos) {
		BlockState blockstate = this.level.getBlockState(pos.getBlockPos());
		//blockstate.onProjectileHit(this.level, blockstate, pos, this);
		if(spawn){
			this.expblock(blockstate,pos.getBlockPos());
			spawn = false;
		}
		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte)3);
			this.remove();
		}
	}
   @Override
   public IPacket<?> getAddEntityPacket() {
	   //return new SSpawnObjectPacket(this);
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

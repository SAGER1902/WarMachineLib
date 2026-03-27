package wmlib.common.bullet;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraftforge.fml.ModList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
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
import wmlib.api.IEnemy;
import wmlib.common.living.EntityWMVehicleBase;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.math.BlockRayTraceResult;
import wmlib.common.WMSoundEvent;
import wmlib.common.living.EntityWMSeat;
import safx.SagerFX;
import net.minecraft.util.ResourceLocation;
import wmlib.client.obj.SAObjModel;

import java.util.List;

import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.api.paz.IZombieEntity;

import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageFX;
import net.minecraftforge.fml.network.PacketDistributor;

import wmlib.common.living.EntityWMVehicleBase;
public class EntityMissile extends EntityBulletBase {
	@Nullable
	private UUID targetId;
	private double targetDeltaX;
    private double targetDeltaY;
    private double targetDeltaZ;

    public boolean autoaim = true;
    public boolean aim_lock = false;
	public Entity finalTarget;
	public LivingEntity friend = null;
	public EntityMissile(FMLPlayMessages.SpawnEntity packet, World worldIn) { 
		super(WarMachineLib.ENTITY_MISSILE, worldIn); 
	}
	
   public EntityMissile(EntityType<? extends EntityMissile> p_i50159_1_, World p_i50159_2_) {
      super(p_i50159_1_, p_i50159_2_);
   }
   
   double moveX = 0;
   double moveY = 0;
   double moveZ = 0;
   
   public EntityMissile(World worldIn, LivingEntity throwerIn, double mx, double my, double mz, Entity ve) {
      super(WarMachineLib.ENTITY_MISSILE, throwerIn, worldIn);
	  this.setNoGravity(true);
	  this.shooter = throwerIn;
	   this.moveX = mx;
	   this.moveY = my;
	   this.moveZ = mz;
   }
   public EntityMissile(World worldIn, LivingEntity throwerIn, Entity target, Entity ve) {
      super(WarMachineLib.ENTITY_MISSILE, throwerIn, worldIn);
	  this.setNoGravity(true);
	  this.shooter = throwerIn;
	  this.finalTarget = target;
   }

   public EntityMissile(World worldIn, double x, double y, double z) {
      super(WarMachineLib.ENTITY_MISSILE, x, y, z, worldIn);
   }
   
	public static final RedstoneParticleData MISSILE = new RedstoneParticleData(1F, 1F, 1F, 4F);
	public void tick() {
		super.tick();
		if(ModList.get().isLoaded("safx")){
			
		}else{
			this.level.addParticle(MISSILE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
		}
		if (!this.level.isClientSide){
			if(this.getBulletType()>5){
				if(this.shooter!=null){
					LivingEntity living = this.shooter;
					Vector3d lock = Vector3d.directionFromRotation(living.getRotationVector());
					/*double x = 150D;
					double ix = (int) (living.getX() + lock.x * x);
					double iy = (int) (living.getY() + 1.5 + lock.y * x);
					double iz = (int) (living.getZ() + lock.z * x);
					this.lockblock(ix, iy, iz);*/
					int ix = 0;
					int iy = 0;
					int iz = 0;
					for (int x = 0; x < 100; ++x) {
						ix = (int) (living.getX() + lock.x * x);
						iy = (int) (living.getY() + 1.5 + lock.y * x);
						iz = (int) (living.getZ() + lock.z * x);
						if(this.getBulletType()>6){
							BlockPos blockpos = new BlockPos(ix, iy, iz);
							BlockState iblockstate = this.level.getBlockState(blockpos);
							if (!iblockstate.isAir(this.level, blockpos)){
								break;
							}
						}
					}
					if(this.getBulletType()==8){
						this.lockblock(this.moveX, this.moveY, this.moveZ);
					}else{
						this.lockblock(ix, iy, iz);
					}
				}
			}else{
				if (this.finalTarget == null && this.targetId != null) {
					this.finalTarget = ((ServerWorld)this.level).getEntity(this.targetId);
					if (this.finalTarget == null) {
					   this.targetId = null;
					}
				}
				if (this.finalTarget == null || !this.finalTarget.isAlive() || this.finalTarget instanceof PlayerEntity && ((PlayerEntity)this.finalTarget).isSpectator()) {
					Vector3d vector3d1 = this.getDeltaMovement();
					this.setDeltaMovement(vector3d1.x, vector3d1.y - (double)this.getGravity(), vector3d1.z);
				} else {
					if(this.time<(2+this.distanceTo(this.finalTarget)/2F) && this.getBulletType()==1){//1攻顶
						if(this.finalTarget!=null)this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.08D, 0.0D));
					}else{
						if(this.time>6)this.mitarget(this.finalTarget);
					}
				}
			}
		}
	}
	public void lockblock(double x, double y, double z){
		double d6 = x - this.getX();
        double d7 = y - this.getY();
        double d4 = z - this.getZ();
        double d5 = (double)MathHelper.sqrt(d6 * d6 + d7 * d7 + d4 * d4);
		
		double dx = Math.abs(this.getX() - this.xo);
		double dz = Math.abs(this.getZ() - this.zo);
        if (d5 == 0.0D)
        {
            this.targetDeltaX = 0.0D;
            this.targetDeltaY = 0.0D;
            this.targetDeltaZ = 0.0D;
        }
        else
        {
        	double speeded = this.speedfly*0.3F;
            this.targetDeltaX = d6 / d5 * speeded;
            this.targetDeltaY = d7 / d5 * speeded;
            this.targetDeltaZ = d4 / d5 * speeded;
        }
		this.targetDeltaX = MathHelper.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
		this.targetDeltaY = MathHelper.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
		this.targetDeltaZ = MathHelper.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
		Vector3d vector3d = this.getDeltaMovement();
		this.setDeltaMovement(vector3d.add((this.targetDeltaX - vector3d.x) * 0.2D, (this.targetDeltaY - vector3d.y) * 0.2D, (this.targetDeltaZ - vector3d.z) * 0.2D));
	}
	public void mitarget(Entity entity1){
		//entity1.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 10, 10));
		double d6 = entity1.getX() - this.getX();
        double d7 = entity1.getY() - this.getY();
        double d4 = entity1.getZ() - this.getZ();
        double d5 = (double)MathHelper.sqrt(d6 * d6 + d7 * d7 + d4 * d4);
		
		double dx = Math.abs(this.getX() - this.xo);
		double dz = Math.abs(this.getZ() - this.zo);
		float dd = 0.2F;
		if(time<2)dd = (float)Math.sqrt((dx * dx) + (dz * dz))*20;
		
        if (d5 == 0.0D)
        {
            this.targetDeltaX = 0.0D;
            this.targetDeltaY = 0.0D;
            this.targetDeltaZ = 0.0D;
        }
        else
        {
        	double speeded = 1+dd;
            this.targetDeltaX = d6 / d5 * speeded;
            this.targetDeltaY = d7 / d5 * speeded;
            this.targetDeltaZ = d4 / d5 * speeded;
        }
		this.targetDeltaX = MathHelper.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
		this.targetDeltaY = MathHelper.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
		this.targetDeltaZ = MathHelper.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
		Vector3d vector3d = this.getDeltaMovement();
		this.setDeltaMovement(vector3d.add((this.targetDeltaX - vector3d.x) * 0.2D, (this.targetDeltaY - vector3d.y) * 0.2D, (this.targetDeltaZ - vector3d.z) * 0.2D));
	}
   
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte p_70103_1_) {
	}

	protected void onHitEntity(EntityRayTraceResult result) {
		float size = 1.0F;
		boolean hurt = true;
		Entity entity = result.getEntity();
		if(entity!=null){
			if(entity instanceof EntityWMSeat){
				hurt = false;
			}else
			if(this.shooter != null){
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
				if(this.flame){
					entity.setSecondsOnFire(8);
					//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
					if(ModList.get().isLoaded("safx")){
						SagerFX.proxy.createFX("LargeExplosionFire",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+exlevel*0.1F);
						//PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messagefx);
					}
				}else{
					if(ModList.get().isLoaded("safx")&&this.exlevel>3)SagerFX.proxy.createFX("ShockWave", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+exlevel*0.3F);
					if (this.exlevel > 8.0F)
					{
					  if (this.exlevel > 30.0F)
					  {
						if (ModList.get().isLoaded("safx")) {
						  SagerFX.proxy.createFX("NukeExplosion", null, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D, 0.8F + this.exlevel * 0.05F);
						}
						//this.playSound(SASoundEvent.nuclear_exp.get(), 2.0F + this.exlevel, 1.0F);
					  }
					  else
					  {
						if (ModList.get().isLoaded("safx")) {
						  SagerFX.proxy.createFX("BigMissileExplosion", null, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D, 0.8F + this.exlevel * 0.05F);
						}
						if (this.exlevel > 15.0F) {
						  //this.playSound(SASoundEvent.missile_hit1.get(), 2.0F + this.exlevel, 1.0F);
						} else {
						  //this.playSound(SASoundEvent.artillery_impact.get(), 2.0F + this.exlevel, 1.0F);
						}
					  }
					}
					else{
						if(ModList.get().isLoaded("safx")){
							if(this.getExLevel()>1){
								SagerFX.proxy.createFX("AdvExpHit",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+exlevel*0.3F);
								//PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messagefx);
							}else{
								SagerFX.proxy.createFX("AdvExpSmall",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.5F);
								//PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messagefx);
							}
						}
						/*if(entity instanceof LivingEntity) {
							LivingEntity hitentity = (LivingEntity)entity;
							if(hitentity.getArmorValue()>5D){
								//this.playSound(SASoundEvent.tank_shell_metal.get(), 2+this.getExLevel(), 1.0F);
							}else{
								//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
							}
						}*/
					}
				}
				entity.invulnerableTime = 0;
				int i = power;
				entity.hurt(DamageSource.thrown(this, this.getOwner()), (float)i*size);
				if(!ModList.get().isLoaded("safx")){
					if(this.getExLevel() >= 2){
						this.level.addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
						this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
					} else {
						this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
					}
				}
				if (!this.level.isClientSide) {
					this.level.broadcastEntityEvent(this, (byte)3);
					if(this.getExLevel() > 0) {
						WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, exlevel, this.flame, false);
					}
					this.remove();
					  if (this.isRad)
					  {
						EntityRad rad = new EntityRad(WarMachineLib.ENTITY_RAD, this.level);
						rad.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
						this.level.addFreshEntity(rad);
					  }
				}
				if(this.getExLevel()>0){
					List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(this.getExLevel()*5, this.getExLevel()*5, this.getExLevel()*5));
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity attackentity = list.get(k2);
						if(attackentity instanceof PlayerEntity){
							PlayerEntity players = (PlayerEntity)attackentity;
							this.shockPlayer(players);
						}
					}
				}
				
				this.playSound(this.hitEntitySound, 2+this.getExLevel(), 1.0F);
			}
		}
	}

	public void expblock(BlockState blockstate, BlockPos pos){
			if(this.flame){
				//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
				if(ModList.get().isLoaded("safx")){
					SagerFX.proxy.createFX("LargeExplosionFire",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+exlevel*0.1F);
					//PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messagefx);
				}
			}else{
				if(ModList.get().isLoaded("safx")&&this.exlevel>3)SagerFX.proxy.createFX("ShockWave", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+exlevel*0.3F);
				if (this.exlevel > 8.0F)
				{
				  if (this.exlevel > 30.0F)
				  {
					if (ModList.get().isLoaded("safx")) {
					  SagerFX.proxy.createFX("NukeExplosion", null, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D, 0.8F + this.exlevel * 0.05F);
					}
					//this.playSound(SASoundEvent.nuclear_exp.get(), 2.0F + this.exlevel, 1.0F);
				  }
				  else
				  {
					if (ModList.get().isLoaded("safx")) {
					  SagerFX.proxy.createFX("BigMissileExplosion", null, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D, 0.8F + this.exlevel * 0.05F);
					}
					if (this.exlevel > 15.0F) {
					  //this.playSound(SASoundEvent.missile_hit1.get(), 2.0F + this.exlevel, 1.0F);
					} else {
					  //this.playSound(SASoundEvent.artillery_impact.get(), 2.0F + this.exlevel, 1.0F);
					}
				  }
				}else{
					if (!blockstate.isAir(this.level, pos)) {
						if(ModList.get().isLoaded("safx")){
							if(this.getExLevel()>1){
								SagerFX.proxy.createFX("LessStone",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+exlevel*0.1F);
								SagerFX.proxy.createFX("AdvExplosion",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+exlevel*0.3F);
								//PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messagefx);
							}else{
								SagerFX.proxy.createFX("AdvExpSmall",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.5F);
							}
						}
					}else{
						if(ModList.get().isLoaded("safx")){
							SagerFX.proxy.createFX("AdvExpHit",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F);
							//PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messagefx);
						}
					}
					//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
				}
			}
			if(!ModList.get().isLoaded("safx")){
				if(this.getExLevel() >= 2){
					this.level.addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
					this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				} else {
					this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				}
			}
			if(this.getExLevel()>0){
				List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(this.getExLevel()*5, this.getExLevel()*5, this.getExLevel()*5));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity attackentity = list.get(k2);
					if(attackentity instanceof PlayerEntity){
						PlayerEntity players = (PlayerEntity)attackentity;
						this.shockPlayer(players);
					}
				}
			}
			this.playSound(this.hitBlockSound, 2+this.getExLevel(), 1.0F);
			if (!this.level.isClientSide) {
				if(this.getExLevel() > 0)WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, exlevel, this.flame, false);
				if (this.isRad)
				{
					EntityRad rad = new EntityRad(WarMachineLib.ENTITY_RAD, this.level);
					rad.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
					this.level.addFreshEntity(rad);
				}
			}
	}

	public boolean spawn = true;
	protected void onHitBlock(BlockRayTraceResult pos) {
		BlockState blockstate = this.level.getBlockState(pos.getBlockPos());
		if(spawn){
			this.expblock(blockstate, pos.getBlockPos());
			if (!this.level.isClientSide) {
				this.level.broadcastEntityEvent(this, (byte)3);
				this.remove();
			}
			spawn = false;
		}
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

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
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.particles.BlockParticleData;
import wmlib.common.WMSoundEvent;
import wmlib.common.living.EntityWMSeat;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.monster.IMob;
import wmlib.api.IEnemy;
import net.minecraft.util.math.BlockRayTraceResult;
import safx.SagerFX;
import net.minecraft.util.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.api.paz.IZombieEntity;
import java.util.List;

import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageFX;
import net.minecraftforge.fml.network.PacketDistributor;
import wmlib.common.network.message.MessageBulletMove;

import wmlib.common.living.EntityWMVehicleBase;
public class EntityShell extends EntityBulletBase {
	public EntityShell(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(WarMachineLib.ENTITY_SHELL, worldIn); 
	}
	
   public EntityShell(EntityType<? extends EntityShell> p_i50159_1_, World p_i50159_2_) {
      super(p_i50159_1_, p_i50159_2_);
   }
   
   public EntityShell(World worldIn, LivingEntity throwerIn) {
      super(WarMachineLib.ENTITY_SHELL, throwerIn, worldIn);
	  this.shooter = throwerIn;
   }
   public EntityShell(World worldIn, double x, double y, double z) {
      super(WarMachineLib.ENTITY_SHELL, x, y, z, worldIn);
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   /**
    * Called to update the entity's position/logic.
    */
	public void tick() {
		super.tick();
        if (this.speedfly>4F && this!=null && !this.level.isClientSide) {
			MessageBulletMove messageMove = new MessageBulletMove(this.getId(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
			PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messageMove);
        }
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
					if(ModList.get().isLoaded("safx")&&this.getExLevel()>4)SagerFX.proxy.createFX("ShockWave", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+exlevel*0.1F);
					if(this.getExLevel()>5){
						//this.playSound(SASoundEvent.artillery_impact.get(), 2+this.getExLevel(), 1.0F);
						if(ModList.get().isLoaded("safx")){
							SagerFX.proxy.createFX("AdvExplosion",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+exlevel*0.1F);
							//PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messagefx);
						}
					}else{
						if(ModList.get().isLoaded("safx")){
							if(this.getExLevel()>1){
								SagerFX.proxy.createFX("AdvExpHit",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+exlevel*0.1F);
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
				
				this.playSound(this.hitEntitySound, 2+this.getExLevel(), 1.0F);
				
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
			}
		}
	}

	public boolean spawn = true;
	protected void onHitBlock(BlockRayTraceResult pos) {
		BlockState blockstate = this.level.getBlockState(pos.getBlockPos());
		if(spawn){
			if(this.getBulletType()==9){
				//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
				if(ModList.get().isLoaded("safx")){
					SagerFX.proxy.createFX("LargeExplosionFire",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+exlevel*0.1F);
					//PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messagefx);
				}
			}else{
				if(ModList.get().isLoaded("safx")&&this.getExLevel()>4)SagerFX.proxy.createFX("ShockWave", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+exlevel*0.1F);
				if(this.getExLevel()>5){
					if(ModList.get().isLoaded("safx")){
						SagerFX.proxy.createFX("ManyStone",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+exlevel*0.1F);
						SagerFX.proxy.createFX("AdvExplosion",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+exlevel*0.1F);
						//PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messagefx);
					}
					//this.playSound(SASoundEvent.artillery_impact.get(), 2+this.getExLevel(), 1.0F);
				}else{
					if (!blockstate.isAir(this.level, pos.getBlockPos())) {
						if(ModList.get().isLoaded("safx")){
							if(this.getExLevel()>1){
								SagerFX.proxy.createFX("LessStone",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+exlevel*0.1F);
								SagerFX.proxy.createFX("AdvExplosion",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+exlevel*0.1F);
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
			
			this.playSound(this.hitBlockSound, 2+this.getExLevel(), 1.0F);
			
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
					WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, exlevel, this.getBulletType()==9, false);
				}
				this.remove();
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
			spawn = false;
		}
	}
   
	@Override
	public IPacket<?> getAddEntityPacket() {
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

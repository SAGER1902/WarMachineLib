package wmlib.common.bullet;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.core.particles.ParticleTypes;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;

import wmlib.WarMachineLib;
import wmlib.common.world.WMExplosionBase;
import net.minecraft.world.scores.Team;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import wmlib.api.IEnemy;
import wmlib.common.living.EntityWMVehicleBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

import wmlib.common.living.EntityWMSeat;
import safx.SagerFX;
import net.minecraft.resources.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult; 
import java.util.List;
import net.minecraftforge.network.NetworkHooks;
/*import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.api.paz.IZombieEntity;*/
import wmlib.init.WMModEntities;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageFX;

import com.atsuishio.superbwarfare.init.ModDamageTypes;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Vector3f;

import wmlib.WMConfig;
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
	public EntityMissile(PlayMessages.SpawnEntity packet, Level worldIn) { 
		super(WMModEntities.ENTITY_MISSILE.get(), worldIn); 
	}
	
   public EntityMissile(EntityType<? extends EntityMissile> p_i50159_1_, Level p_i50159_2_) {
      super(p_i50159_1_, p_i50159_2_);
   }
   
   double moveX = 0;
   double moveY = 0;
   double moveZ = 0;
   
   public EntityMissile(Level worldIn, LivingEntity throwerIn, double mx, double my, double mz, Entity ve) {
      super(WMModEntities.ENTITY_MISSILE.get(), throwerIn, worldIn);
	  this.setNoGravity(true);
	  this.shooter = throwerIn;
	   this.moveX = mx;
	   this.moveY = my;
	   this.moveZ = mz;
   }
   public EntityMissile(Level worldIn, LivingEntity throwerIn, Entity target, Entity ve) {
      super(WMModEntities.ENTITY_MISSILE.get(), throwerIn, worldIn);
	  this.setNoGravity(true);
	  this.shooter = throwerIn;
	  this.finalTarget = target;
   }

   public EntityMissile(Level worldIn, double x, double y, double z) {
      super(WMModEntities.ENTITY_MISSILE.get(), x, y, z, worldIn);
   }
   
	public static final DustParticleOptions MISSILE = new DustParticleOptions(new Vector3f(1.0F, 1.0F, 1.0F), 4F);
	public void tick() {
		super.tick();
		if(ModList.get().isLoaded("safx")){
		}else{
			this.level().addParticle(MISSILE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
		}
		if (!this.level().isClientSide){
			if(this.getBulletType()>5){
				if(this.shooter!=null){
					LivingEntity living = this.shooter;
					Vec3 lock = Vec3.directionFromRotation(living.getRotationVector());
					int ix = 0;
					int iy = 0;
					int iz = 0;
					for (int x = 0; x < 100; ++x) {
						ix = (int) (living.getX() + lock.x * x);
						iy = (int) (living.getY() + 1.5 + lock.y * x);
						iz = (int) (living.getZ() + lock.z * x);
						if(this.getBulletType()>6){
							BlockPos blockpos = new BlockPos(ix, iy, iz);
							BlockState iblockstate = this.level().getBlockState(blockpos);
							if (!iblockstate.isAir()){
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
					this.finalTarget = ((ServerLevel)this.level()).getEntity(this.targetId);
					if (this.finalTarget == null) {
					   this.targetId = null;
					}
				}
				if (this.finalTarget == null || !this.finalTarget.isAlive() || this.finalTarget instanceof Player && ((Player)this.finalTarget).isSpectator()) {
					this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.08D, 0.0D));
				} else {
					if(this.time<(2+this.distanceTo(this.finalTarget)/2F)&&this.getBulletType()==1){//1攻顶
						this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.08D, 0.0D));
					}else{
						if(this.time<3&&this.getBulletType()!=1)this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.08D, 0.0D));
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
        double d5 = (double)Math.sqrt(d6 * d6 + d7 * d7 + d4 * d4);
		
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
		this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
		this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
		this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
		Vec3 vector3d = this.getDeltaMovement();
		this.setDeltaMovement(vector3d.add((this.targetDeltaX - vector3d.x) * 0.2D, (this.targetDeltaY - vector3d.y) * 0.2D, (this.targetDeltaZ - vector3d.z) * 0.2D));
	}
	public void mitarget(Entity entity1){
		//entity1.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 10, 10));
		double d6 = entity1.getX() - this.getX();
        double d7 = entity1.getY() - this.getY();
        double d4 = entity1.getZ() - this.getZ();
        double d5 = (double)Math.sqrt(d6 * d6 + d7 * d7 + d4 * d4);
		
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
		this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
		this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
		this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
		Vec3 vector3d = this.getDeltaMovement();
		this.setDeltaMovement(vector3d.add((this.targetDeltaX - vector3d.x) * 0.2D, (this.targetDeltaY - vector3d.y) * 0.2D, (this.targetDeltaZ - vector3d.z) * 0.2D));
	}
   
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte p_70103_1_) {
	}

	protected void onHitEntity(EntityHitResult result) {
		float size = 1.0F;
		boolean hurt = true;
		Entity entity = result.getEntity();
		if(entity!=null){
			if(entity instanceof EntityWMSeat){
				hurt = false;
			}else if(this.shooter != null){
				hurt = this.CanAttack(shooter,entity);
			}
			if(hurt){
				if(this.flame){
					entity.setSecondsOnFire(8);
					//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
					if(ModList.get().isLoaded("safx")){
						SagerFX.proxy.createFX("LargeExplosionFire",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+getExLevel()*0.1F);
						//PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messagefx);
					}
				}else{
					if(ModList.get().isLoaded("safx")&&this.getExLevel()>3)SagerFX.proxy.createFX("ShockWave", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+getExLevel()*0.3F);
					if (this.getExLevel() > 8.0F)
					{
					  if (this.getExLevel() > 30.0F)
					  {
						if (ModList.get().isLoaded("safx")) {
						  SagerFX.proxy.createFX("NukeExplosion", null, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D, 0.8F + this.getExLevel() * 0.05F);
						}
						//this.playSound(SASoundEvent.nuclear_exp.get(), 2.0F + this.getExLevel(), 1.0F);
					  }
					  else
					  {
						if (ModList.get().isLoaded("safx")) {
						  SagerFX.proxy.createFX("BigMissileExplosion", null, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D, 0.8F + this.getExLevel() * 0.05F);
						}
						if (this.getExLevel() > 15.0F) {
						  //this.playSound(SASoundEvent.missile_hit1.get(), 2.0F + this.getExLevel(), 1.0F);
						} else {
						  //this.playSound(SASoundEvent.artillery_impact.get(), 2.0F + this.getExLevel(), 1.0F);
						}
					  }
					}
					else{
						if(ModList.get().isLoaded("safx")){
							if(this.getExLevel()>1){
								SagerFX.proxy.createFX("AdvExpHit",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+getExLevel()*0.3F);
								//PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messagefx);
							}else{
								SagerFX.proxy.createFX("AAExplosion",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F);
								//PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messagefx);
							}
						}
						if(entity instanceof LivingEntity) {
							LivingEntity hitentity = (LivingEntity)entity;
							if(hitentity.getArmorValue()>5D){
								//this.playSound(SASoundEvent.tank_shell_metal.get(), 2+this.getExLevel(), 1.0F);
							}else{
								//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
							}
						}
					}
				}
				entity.invulnerableTime = 0;
				int i = power;
				if(ModList.get().isLoaded("superbwarfare")){
					if(this.getExLevel()<5) {
						i = (int)(power * 5);
					}else{
						i = (int)(power * 2);
					}
					entity.hurt(ModDamageTypes.causeProjectileHitDamage(this.level().registryAccess(), this, this.getOwner()), i);
				}else{
					entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)i*size);
				}
				
				if(!ModList.get().isLoaded("safx")){
					if(this.getExLevel() >= 2){
						this.level().addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
						this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
					} else {
						this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
					}
				}
				
				this.playSound(this.hitEntitySound, 2+this.getExLevel(), 1.0F);
				
				if (!this.level().isClientSide) {
					this.level().broadcastEntityEvent(this, (byte)3);
					if(this.getExLevel() > 0) {
						WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, getExLevel(), this.flame, true);
					}
					this.discard();
					  if (this.isRad)
					  {
						EntityRad rad = new EntityRad(WMModEntities.ENTITY_RAD.get(), this.level());
						rad.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
						this.level().addFreshEntity(rad);
					  }
				}
				if(this.getExLevel()>0){
					List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(this.getExLevel()*5, this.getExLevel()*5, this.getExLevel()*5));
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity attackentity = list.get(k2);
						if(attackentity instanceof Player){
							Player players = (Player)attackentity;
							this.shockPlayer(players);
						}
					}
				}
			}
		}
	}
	public int radType = 0;
	public boolean spawn = true;
	protected void onHitBlock(BlockHitResult pos) {
		BlockState blockstate = this.level().getBlockState(pos.getBlockPos());
		if(spawn){
			if (WMConfig.bulletDestroy && blockstate.getDestroySpeed(this.level(), pos.getBlockPos()) >= 0 && blockstate.getDestroySpeed(this.level(), pos.getBlockPos()) < 1.5F) {
				boolean dropItems = true;
				this.level().destroyBlock(pos.getBlockPos(), dropItems, this);
			}
			if(this.flame){
				//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
				if(ModList.get().isLoaded("safx")){
					SagerFX.proxy.createFX("LargeExplosionFire",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+getExLevel()*0.1F);
					//PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messagefx);
				}
			}else{
				if(ModList.get().isLoaded("safx")&&this.getExLevel()>3)SagerFX.proxy.createFX("ShockWave", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+getExLevel()*0.3F);
				if (this.getExLevel() > 8.0F)
				{
				  if (this.getExLevel() > 30.0F)
				  {
					if (ModList.get().isLoaded("safx")) {
					  SagerFX.proxy.createFX("NukeExplosion", null, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D, 0.8F + this.getExLevel() * 0.05F);
					}
					//this.playSound(SASoundEvent.nuclear_exp.get(), 2.0F + this.getExLevel(), 1.0F);
				  }
				  else
				  {
					if (ModList.get().isLoaded("safx")) {
					  SagerFX.proxy.createFX("BigMissileExplosion", null, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D, 0.8F + this.getExLevel() * 0.05F);
					}
					if (this.getExLevel() > 15.0F) {
					  //this.playSound(SASoundEvent.missile_hit1.get(), 2.0F + this.getExLevel(), 1.0F);
					} else {
					  //this.playSound(SASoundEvent.artillery_impact.get(), 2.0F + this.getExLevel(), 1.0F);
					}
				  }
				}else{
					if (!blockstate.isAir()) {
						if(ModList.get().isLoaded("safx")){
							if(this.getExLevel()>1){
								SagerFX.proxy.createFX("LessStone",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+getExLevel()*0.1F);
								SagerFX.proxy.createFX("AdvExplosion",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+getExLevel()*0.3F);
								//PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messagefx);
							}else{
								SagerFX.proxy.createFX("AAExplosion",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F);
							}
						}
					}else{
						if(ModList.get().isLoaded("safx")){
							SagerFX.proxy.createFX("AdvExpHit",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F);
							//PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messagefx);
						}
					}
					//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
				}
			}
			if(!ModList.get().isLoaded("safx")){
				if(this.getExLevel() >= 2){
					this.level().addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
					this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				} else {
					this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				}
			}
			
			this.playSound(this.hitBlockSound, 2+this.getExLevel(), 1.0F);
			
			if (!this.level().isClientSide) {
				this.level().broadcastEntityEvent(this, (byte)3);
				if(this.getExLevel() > 0) {
					WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, getExLevel(), this.flame, true);
				}
				this.discard();
				if (this.isRad)
				{
					EntityRad rad = new EntityRad(WMModEntities.ENTITY_RAD.get(), this.level());
					rad.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
					rad.setRadType(radType);
					this.level().addFreshEntity(rad);
				}
			}
			if(this.getExLevel()>0){
				List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(this.getExLevel()*5, this.getExLevel()*5, this.getExLevel()*5));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity attackentity = list.get(k2);
					if(attackentity instanceof Player){
						Player players = (Player)attackentity;
						this.shockPlayer(players);
					}
				}
			}
			spawn = false;
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

package wmlib.common.bullet;
// Forge 和 Mod 相关
import net.minecraftforge.fml.ModList;                          // 路径不变
import net.minecraftforge.api.distmarker.Dist;                 // 路径不变
import net.minecraftforge.api.distmarker.OnlyIn;               // 路径不变
// 实体相关
import net.minecraft.world.entity.Entity;                      // Entity
import net.minecraft.world.entity.EntityType;                  // EntityType
import net.minecraft.world.entity.LivingEntity;                // LivingEntity
import net.minecraft.world.entity.TamableAnimal;               // TamableAnimal → TamableAnimal
import net.minecraft.world.entity.monster.Enemy;               // Enemy → Enemy（标记接口）
// 物品和方块
import net.minecraft.world.level.block.state.BlockState;        // BlockState
import net.minecraft.core.BlockPos;                            // BlockPos
// NBT 和数据同步
import net.minecraft.nbt.CompoundTag;                          // CompoundTag → CompoundTag
// 网络和包
import net.minecraft.network.protocol.Packet;                  // Packet → Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener; // 客户端包监听接口
import net.minecraftforge.network.NetworkHooks;                // 路径更新
// 粒子效果
import net.minecraft.core.particles.ParticleTypes;             // ParticleTypes
// 数学和工具
import net.minecraft.util.Mth;                                 // Mth → Mth
import net.minecraft.world.phys.Vec3;                          // Vec3 → Vec3
import net.minecraft.world.phys.HitResult;                     // HitResult → HitResult
import net.minecraft.world.phys.EntityHitResult;               // EntityHitResult → EntityHitResult
import net.minecraft.world.phys.BlockHitResult;                // BlockHitResult → BlockHitResult
// 世界和伤害
import net.minecraft.world.level.Level;                        // Level → Level
import net.minecraft.world.damagesource.DamageSource;          // DamageSource
import net.minecraft.world.level.Explosion;                    // Explosion
// 玩家和队伍
import net.minecraft.world.entity.player.Player;               // Player → Player
import net.minecraft.world.scores.Team;                        // Team 路径更新
// 其他
import net.minecraftforge.network.PlayMessages;                // PlayMessages → PlayMessages
import net.minecraft.core.particles.BlockParticleOption;
import wmlib.WarMachineLib;
import wmlib.common.world.WMExplosionBase;
////import wmlib.common.WMSoundEvent;
import wmlib.common.living.EntityWMSeat;
import wmlib.api.IEnemy;
import safx.SagerFX;
import wmlib.client.obj.SAObjModel;
import wmlib.init.WMModEntities;
import java.util.List;
import advancearmy.event.SASoundEvent;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import net.minecraftforge.network.PacketDistributor;
import wmlib.common.network.message.MessageBulletMove;
import wmlib.common.network.PacketHandler;
import net.minecraft.core.particles.DustParticleOptions;
import org.joml.Vector3f;
import wmlib.WMConfig;

public class EntityBullet extends EntityBulletBase {
	public EntityBullet(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(WMModEntities.ENTITY_BULLET.get(), worldIn); 
	}
	
   public EntityBullet(EntityType<? extends EntityBullet> p_i50159_1_, Level p_i50159_2_) {
      super(p_i50159_1_, p_i50159_2_);

   }
   
   public EntityBullet(Level worldIn, LivingEntity throwerIn) {
      super(WMModEntities.ENTITY_BULLET.get(), throwerIn, worldIn);
	  this.shooter = throwerIn;
   }

   public EntityBullet(Level worldIn, double x, double y, double z) {
      super(WMModEntities.ENTITY_BULLET.get(), x, y, z, worldIn);
   }

   /**
    * Called to update the entity's position/logic.
    */
	public void tick() {
		super.tick();
        /*if (this!=null && !this.level().isClientSide) {
			MessageBulletMove messageMove = new MessageBulletMove(this.getId(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
			PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messageMove);
        }*/
	}
   
   
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte p_70103_1_) {
	}

   protected void onHitEntity(EntityHitResult result) {
		boolean hurt = true;
		Entity entity = result.getEntity();
		if(entity!=null){
			if(entity instanceof EntityWMSeat){
				hurt = false;
			}else if(this.shooter != null){
				hurt = this.CanAttack(shooter,entity);
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
					if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("AAExplosion", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+this.getExLevel()*0.1F);
				}else
				if(this.getBulletType()==1){
					if(this.getExLevel()>0){
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("AdvExpSmall", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.5F);
					}else{
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("GunHit", null, this.getX(), this.getY(), this.getZ(), 0.1F, 0.1F, 0.1F, 1.6F);
					}
				}else if(this.getBulletType()==3){
					this.level().addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);//FLAME
				}else if(this.getBulletType()==5){
					DustParticleOptions particleOptions = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);
					this.level().addParticle(particleOptions, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
				}else{
					{
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("GunHit", null, this.getX(), this.getY(), this.getZ(), 0.1F, 0.1F, 0.1F, 1F+this.getExLevel()*0.4F);
					}
				}
				if(!ModList.get().isLoaded("safx")){
					if(this.getExLevel()==0){
						this.level().addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
					}else{
						if (this.getExLevel() > 1.0F) {
							this.level().addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
							this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
						} else {
							this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
						}
					}
				}
				entity.invulnerableTime = 0;
				if(this.flame)entity.setSecondsOnFire(8);
				int i = power;
				if(ModList.get().isLoaded("superbwarfare")){
					if(this.getExLevel()<1){
						entity.hurt(ModDamageTypes.causeGunFireDamage(this.level().registryAccess(), this, this.getOwner()), i);
					}else{
						entity.hurt(ModDamageTypes.causeProjectileExplosionDamage(this.level().registryAccess(), this, this.getOwner()), i);
					}
					
				}else{
					entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)i);
				}
				if (!this.level().isClientSide) {
					this.level().broadcastEntityEvent(this, (byte)3);
					if(this.getExLevel() > 0) {
						WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, getExLevel(), this.flame, true);
					}
					if(this.getBulletType()!=5)this.discard();
				}
				//DustParticleOptions particleOptions = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);
				//this.level().addParticle(particleOptions, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
			}
		}
   }

   /**
    * Called when this EntityFireball hits a block or entity.
    */
	public boolean spawn = true;
	protected void onHitBlock(BlockHitResult pos) {
		BlockState blockstate = this.level().getBlockState(pos.getBlockPos());
		//blockstate.onProjectileHit(this.level(), blockstate, pos, this);
		if(spawn){
			if (WMConfig.bulletDestroy && blockstate.getDestroySpeed(this.level(), pos.getBlockPos()) >= 0 && blockstate.getDestroySpeed(this.level(), pos.getBlockPos()) < 0.5F && (this.getExLevel()>0||this.random.nextInt(3)==0)) {
				boolean dropItems = true;
				this.level().destroyBlock(pos.getBlockPos(), dropItems, this);
			}
			if(this.getExLevel()>1){
				if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("SmallGrenadeExp", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+this.getExLevel()*0.1F);//AAExplosion
				this.playSound(this.hitBlockSound, 1+this.getExLevel(), 1.0F);
			}else
			if(this.getBulletType()==1){
				if(this.getExLevel()>0){
					if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("AdvExpSmall", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.5F);
				}else{
					if (!blockstate.isAir()) {
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("GunExp", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1.5F);
					}else{
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("GunHit", null, this.getX(), this.getY(), this.getZ(), 0.1F, 0.1F, 0.1F, 1.6F);
					}
				}
			}else if(this.getBulletType()==3){
				this.level().addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);//FLAME
			}else if(this.getBulletType()==5){
				this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
			}else{
				if (!blockstate.isAir()) {
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
					   this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos.getBlockPos()), this.getX() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), this.getY() + 0.1D, this.getZ() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), 4.0D * ((double)this.random.nextFloat() - 0.5D), 3.5D, ((double)this.random.nextFloat() - 0.5D) * 4.0D);
					}
				}else{
					if (this.getExLevel() > 1.0F) {
						this.level().addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
						this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
					}/* else {
						this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
					}*/
				}
			}
			spawn = false;
		}
		if (!this.level().isClientSide) {
			this.level().broadcastEntityEvent(this, (byte)3);
			if(this.getExLevel() > 0) {
				WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, getExLevel(), this.flame, true);
			}
			this.discard();
		}
	}
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

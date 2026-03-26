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
import net.minecraft.world.item.Item;                          // Item
import net.minecraft.world.item.ItemStack;                     // ItemStack
import net.minecraft.world.item.Items;                         // Items
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

import wmlib.WarMachineLib;
import wmlib.common.world.WMExplosionBase;
//import wmlib.common.WMSoundEvent;
import wmlib.common.living.EntityWMSeat;
import wmlib.api.IEnemy;
import safx.SagerFX;
import wmlib.client.obj.SAObjModel;
import wmlib.init.WMModEntities;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import net.minecraftforge.network.PacketDistributor;
import wmlib.common.network.message.MessageBulletMove;
import wmlib.common.network.PacketHandler;
import java.util.List;
import wmlib.WMConfig;

public class EntityShell extends EntityBulletBase {
	public EntityShell(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(WMModEntities.ENTITY_SHELL.get(), worldIn); 
	}
	
   public EntityShell(EntityType<? extends EntityShell> p_i50159_1_, Level p_i50159_2_) {
      super(p_i50159_1_, p_i50159_2_);
   }
   
   public EntityShell(Level worldIn, LivingEntity throwerIn) {
      super(WMModEntities.ENTITY_SHELL.get(), throwerIn, worldIn);
	  this.shooter = throwerIn;
   }
   public EntityShell(Level worldIn, double x, double y, double z) {
      super(WMModEntities.ENTITY_SHELL.get(), x, y, z, worldIn);
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   /**
    * Called to update the entity's position/logic.
    */
	public void tick() {
		super.tick();
         if (this.speedfly>4F && this!=null && !this.level().isClientSide) {
			MessageBulletMove messageMove = new MessageBulletMove(this.getId(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
			PacketHandler.getPlayChannel_Client().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level().dimension())), messageMove);
        }
	}
   
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte p_70103_1_) {
	}

   /**
    * Called when the arrow hits an entity
    */
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
				if(this.flame){
					entity.setSecondsOnFire(8);
					//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
					if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("LargeExplosionFire", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+getExLevel()*0.1F);
				}else{
					if(ModList.get().isLoaded("safx")&&this.getExLevel()>4)SagerFX.proxy.createFX("ShockWave", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+getExLevel()*0.1F);
					if(this.getExLevel()>5){
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("AdvExplosion", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+getExLevel()*0.1F);
						//this.playSound(SASoundEvent.artillery_impact.get(), 2+this.getExLevel(), 1.0F);
					}else{
						if(ModList.get().isLoaded("safx")){
							if(this.getExLevel()>1){
								SagerFX.proxy.createFX("AdvExpHit", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+getExLevel()*0.1F);
							}else{
								SagerFX.proxy.createFX("AdvExpSmall", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.5F);
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
					entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)i);
				}
				//System.out.println("this.getExLevel()1="+this.getExLevel());
				if(!ModList.get().isLoaded("safx")){
					if(this.getExLevel() >= 2){
						this.level().addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
						this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
					} else {
						this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
					}
					//System.out.println("this.getExLevel()2="+this.getExLevel());
				}
				
				this.playSound(this.hitEntitySound, 2+this.getExLevel(), 1.0F);
				
				if (!this.level().isClientSide) {
					this.level().broadcastEntityEvent(this, (byte)3);
					if(this.getExLevel() > 0) {
						WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power*2, getExLevel(), this.flame, true);
					}
					this.discard();
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

   /**
    * Called when this EntityFireball hits a block or entity.
    */
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
				if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("LargeExplosionFire", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+getExLevel()*0.1F);
			}else{
				if(ModList.get().isLoaded("safx")&&this.getExLevel()>4)SagerFX.proxy.createFX("ShockWave", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+getExLevel()*0.1F);
				if(this.getExLevel()>5){
					
					if(ModList.get().isLoaded("safx")){
						SagerFX.proxy.createFX("ManyStone",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+getExLevel()*0.1F);
						SagerFX.proxy.createFX("AdvExplosion", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+getExLevel()*0.1F);
					}
					//this.playSound(SASoundEvent.artillery_impact.get(), 2+this.getExLevel(), 1.0F);
				}else{
					if (!blockstate.isAir()) {
						if(ModList.get().isLoaded("safx")){
							if(this.getExLevel()>1){
								SagerFX.proxy.createFX("LessStone",null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.8F+getExLevel()*0.1F);
								SagerFX.proxy.createFX("AdvExplosion", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+getExLevel()*0.1F);
							}else{
								SagerFX.proxy.createFX("AdvExpSmall", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.5F);
							}//AAExplosion
						}
					}else{
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("AdvExpHit", null, this.getX(), this.getY(), this.getZ(), 0.1F, 0.1F, 0.1F, 1F);
					}
					//this.playSound(SASoundEvent.tank_shell.get(), 2+this.getExLevel(), 1.0F);
				}
			}
			////System.out.println("this.getExLevel()1block="+this.getExLevel());
			if(!ModList.get().isLoaded("safx")){
				if(this.getExLevel() >= 2){
					this.level().addParticle(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
					this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				} else {
					this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				}
				//System.out.println("this.getExLevel()2block="+this.getExLevel());
			}
			if (!this.level().isClientSide) {
				this.level().broadcastEntityEvent(this, (byte)3);
				if(this.getExLevel() > 0) {
					WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power*2, getExLevel(), this.flame, true);
				}
				this.discard();
			}
			
			this.playSound(this.hitBlockSound, 2+this.getExLevel(), 1.0F);
			
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
	   //return new SSpawnObjectPacket(this);
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

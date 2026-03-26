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
import wmlib.WMConfig;
import safx.SagerFX;
import wmlib.client.obj.SAObjModel;
import wmlib.init.WMModEntities;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import java.util.List;
import net.minecraft.tags.BlockTags;
public class EntityGrenade extends EntityBulletBase {
	public EntityGrenade(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(WMModEntities.ENTITY_GRENADE.get(), worldIn); 
	}
	
   public EntityGrenade(EntityType<? extends EntityGrenade> p_i50159_1_, Level p_i50159_2_) {
      super(p_i50159_1_, p_i50159_2_);
   }
   
   public EntityGrenade(Level worldIn, LivingEntity throwerIn) {
      super(WMModEntities.ENTITY_GRENADE.get(), throwerIn, worldIn);
	  this.shooter = throwerIn;
   }
   public EntityGrenade(Level worldIn, double x, double y, double z) {
      super(WMModEntities.ENTITY_GRENADE.get(), x, y, z, worldIn);
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
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
   protected void onHitEntity(EntityHitResult result) {
		boolean hurt = true;
		Entity entity = result.getEntity();
		if(entity!=null){
			if(entity instanceof EntityWMSeat){
				hurt = false;
			}else
			if(this.shooter != null){
				hurt = this.CanAttack(shooter,entity);
			}
			if(hurt){
				entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)2);
			}
			if(WMConfig.grenadeHitFriend||hurt){
				this.setDeltaMovement(this.getDeltaMovement().x*0.2F, this.getDeltaMovement().y*0.2F, this.getDeltaMovement().z*0.2F);
				float rx = this.random.nextFloat() - 0.5F;
				float ry = this.random.nextFloat();
				float rz = this.random.nextFloat() - 0.5F;
				this.setDeltaMovement(this.getDeltaMovement().x+rx, ry+this.getDeltaMovement().y - (double)this.getGravity(), this.getDeltaMovement().z+rz);
			}
		}
   }

	public void expblock(BlockState blockstate, BlockPos pos){
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("SmallGrenadeExp", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 0.6F+this.getExLevel()*0.1F);//AAExplosion
		this.playSound(this.selfExpSound, 1+getExLevel()*2, 1.0F);
		if (!this.level().isClientSide) {
			WMExplosionBase.createExplosionDamage(this, this.getX(), this.getY(), this.getZ(), power, getExLevel(), this.flame, false);
		}
	}


   /**
    * Called when this EntityFireball hits a block or entity.
    */
	public boolean spawn = true;
	protected void onHitBlock(BlockHitResult pos) {
		BlockState blockstate = this.level().getBlockState(pos.getBlockPos());
		if (!blockstate.isAir()) {
			stopRote=true;
			this.setDeltaMovement(0, 0, 0);
		}
	}
   
   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
	   //return new SSpawnObjectPacket(this);
	   return NetworkHooks.getEntitySpawningPacket(this);
	}
}

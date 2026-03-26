package wmlib.common.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;

import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.damagesource.DamageSource;                 
import net.minecraft.world.damagesource.DamageTypes;

import net.minecraft.sounds.SoundEvent;                               
import net.minecraft.sounds.SoundEvents;                            
import net.minecraft.sounds.SoundSource;

import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;

import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;  
import wmlib.common.bullet.ProjectileBase;
import net.minecraft.tags.BlockTags;
import wmlib.WMConfig;

import wmlib.common.block.MuzzleFlashBlock;
import wmlib.common.block.BlockRegister;
import net.minecraft.world.level.block.Block;

public class WMExplosion extends Explosion
{
    private final boolean causesFire;
	private final boolean isRad;
	
    private final boolean destroy;
    private final Random random;
    private final Level level;
    private final double x;
    private final double y;
    private final double z;
    private final Entity exploder;
	private final LivingEntity shooeter=null;
    private final float size;
	private final float damage;
    private final List<BlockPos> affectedBlockPositions;
    /** Maps players to the knockback vector applied by the explosion, to send to the client */
    private final Map<Player, Vec3> playerKnockbackMap;
    private final Vec3 position;

    @OnlyIn(Dist.CLIENT)
    public WMExplosion(Level worldIn, Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions)
    {
        this(worldIn, entityIn, x, y, z, size, false, true, affectedPositions);
    }

    @OnlyIn(Dist.CLIENT)
    public WMExplosion(Level worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean destroy, List<BlockPos> affectedPositions)
    {
        this(worldIn, entityIn, x, y, z, size, size, causesFire, destroy, false);
        this.affectedBlockPositions.addAll(affectedPositions);
    }

    public WMExplosion(Level worldIn, Entity entityIn, double x, double y, double z, float damage, float size, boolean flaming, boolean exp, boolean rad)
    {
    	super(worldIn, entityIn, x, y, z, size, flaming, BlockInteraction.KEEP);
        this.random = new Random();
        this.affectedBlockPositions = Lists.<BlockPos>newArrayList();
        this.playerKnockbackMap = Maps.<Player, Vec3>newHashMap();
        this.level = worldIn;
        this.exploder = entityIn;
		this.damage = damage;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.causesFire = flaming;
        this.destroy = exp;
		this.isRad = rad;
        this.position = new Vec3(this.x, this.y, this.z);
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    public void doExplosionA()
    {
		/*if(this.destroy||this.causesFire||this.isRad)*/
		if(this.size>2 && WMConfig.explosionDestroy && destroy){
			Set<BlockPos> set = Sets.<BlockPos>newHashSet();
			int i = 16;
			for (int j = 0; j < 16; ++j)
			{
				for (int k = 0; k < 16; ++k)
				{
					for (int l = 0; l < 16; ++l)
					{
						if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15)
						{
							double d0 = (double)((float)j / 15.0F * 2.0F - 1.0F);
							double d1 = (double)((float)k / 15.0F * 2.0F - 1.0F);
							double d2 = (double)((float)l / 15.0F * 2.0F - 1.0F);
							double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
							d0 = d0 / d3;
							d1 = d1 / d3;
							d2 = d2 / d3;
							float f = this.size * (0.7F + this.level.random.nextFloat() * 0.6F);
							double d4 = this.x;
							double d6 = this.y;
							double d8 = this.z;
							for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F)
							{
								BlockPos blockpos = BlockPos.containing(d4, d6, d8);
								BlockState iblockstate = this.level.getBlockState(blockpos);
								FluidState fluidstate = this.level.getFluidState(blockpos);
								if (!iblockstate.isAir())
								{
									float f2 =/* this.exploder != null ? this.exploder.getBlockExplosionResistance(this, this.level, blockpos, iblockstate, fluidstate) : */iblockstate.getBlock().getExplosionResistance();
									f -= (f2 + 0.3F) * 0.3F;
								}
								if (f > 0.0F && (this.exploder == null || this.exploder.shouldBlockExplode(this, this.level, blockpos, iblockstate, f)))
								{
									set.add(blockpos);
								}
								d4 += d0 * 0.30000001192092896D;
								d6 += d1 * 0.30000001192092896D;
								d8 += d2 * 0.30000001192092896D;
							}
						}
					}
				}
			}
			this.affectedBlockPositions.addAll(set);
		}
		
		BlockPos pos1 = new BlockPos((int)(this.x), (int)(this.y), (int)(this.z));
		BlockState current = this.level.getBlockState(pos1);
		if(WMConfig.explosionFlash && current.isAir()){
			if(size>1){
				this.level.setBlock(pos1, BlockRegister.CANNONB.get().defaultBlockState(),Block.UPDATE_CLIENTS);
			}else if(size==1){
				this.level.setBlock(pos1, BlockRegister.MUZZB.get().defaultBlockState(),Block.UPDATE_CLIENTS);
			}
		}
		
        float f3 = this.size * 2.0F;
        int k1 = Mth.floor(this.x - (double)f3 - 1.0D);
        int l1 = Mth.floor(this.x + (double)f3 + 1.0D);
        int i2 = Mth.floor(this.y - (double)f3 - 1.0D);
        int i1 = Mth.floor(this.y + (double)f3 + 1.0D);
        int j2 = Mth.floor(this.z - (double)f3 - 1.0D);
        int j1 = Mth.floor(this.z + (double)f3 + 1.0D);
		List<Entity> list = this.level.getEntities(this.exploder, new AABB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
		net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.level, this, list, f3);
        Vec3 vec3d = new Vec3(this.x, this.y, this.z);

        for (int k2 = 0; k2 < list.size(); ++k2)
        {
            Entity entity = list.get(k2);

            if (!entity.ignoreExplosion())
            {
            	if (entity instanceof ItemEntity) continue;
                double range = (double)(Mth.sqrt((float)(entity.distanceToSqr(vec3d))) / (double)f3);

                if (range <= 1.0D)
                {
                    double d5 = entity.getX() - this.x;
                    double d7 = entity.getY() + (double)entity.getEyeHeight() - this.y;
                    double d9 = entity.getZ() - this.z;
                    double dis = (double)Mth.sqrt((float)(d5 * d5 + d7 * d7 + d9 * d9));

                    if (dis != 0.0D)
                    {
                        d5 = d5 / dis;
                        d7 = d7 / dis;
                        d9 = d9 / dis;
                        double d14 = (double)getSeenPercent(vec3d, entity);
                        double d10 = (1.0D - range) * d14;
						if (this.damage != 0) {
							Entity source = this.getExplosivePlacedBy();
							DamageSource damageSource;
							if (source != null) {
								damageSource = entity.damageSources().explosion(source, source instanceof LivingEntity ? (LivingEntity) source : null);
							} else {
								damageSource = entity.damageSources().explosion(null, null); // 无来源的通用爆炸伤害
							}
							float damageValue = (float) ((int) (d10 * this.damage * (0.3F + f3 * 0.1F) + 1.0D));
							entity.hurt(damageSource, damageValue);
						}
						
						//entity.hurtResistantTime = 0;//add
                        double d11 = d10;

                        if (entity instanceof LivingEntity) {
							d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)entity, d10);
						}
						if (this.causesFire)entity.setSecondsOnFire(8);
						entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                    }
                }
            }
        }
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB()
    {
        if(this.size>12F)this.level.playSound(null, this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, this.size, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F);
        int explevel = (int)this.size;
		if(explevel>12)explevel=12;
		//if (this.destroy)
		if (this.size>2 && WMConfig.explosionDestroy && destroy)
        {
			ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
            for (BlockPos blockpos : this.affectedBlockPositions)
            {
                BlockState iblockstate = this.level.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
				/*if (block.getSoundType() == SoundType.GROUND) continue;
				if (block.getSoundType() == SoundType.SAND) continue;
				if (block == Blocks.STONE && block.getMetaFromState(iblockstate) == 0) continue;*/
				if (block == Blocks.DIRT||iblockstate.isAir()) continue;
				//if (iblockstate.is(BlockTags.DIRT))continue;
				if (!iblockstate.isAir() && (this.random.nextInt(14-explevel) == 1||
				iblockstate.getDestroySpeed(this.level, blockpos) >= 0&&iblockstate.getDestroySpeed(this.level, blockpos) < 0.6F)) {
					BlockPos blockpos1 = blockpos.immutable();
					this.level.getProfiler().push("explosion_blocks");
					if (iblockstate.canDropFromExplosion(this.level, blockpos, this) && this.level instanceof ServerLevel serverLevel) {
						BlockEntity tileentity = iblockstate.hasBlockEntity() ? this.level.getBlockEntity(blockpos) : null;
						LootParams.Builder lootparams$builder = new LootParams.Builder(serverLevel)
							.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos))
							.withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
							.withOptionalParameter(LootContextParams.BLOCK_ENTITY, tileentity)
							.withOptionalParameter(LootContextParams.THIS_ENTITY, this.exploder)
							.withParameter(LootContextParams.EXPLOSION_RADIUS, this.size);
							
						if(block != Blocks.GRASS_BLOCK&&block != Blocks.SAND){
							iblockstate.getDrops(lootparams$builder).forEach((item) -> {
								addBlockDrops(objectarraylist, item, blockpos1);
							});
						}
					}
					iblockstate.onBlockExploded(this.level, blockpos, this);
					this.level.getProfiler().pop();
				}
            }
			spawnDroppedItems(objectarraylist);
        }
    }
	private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> p, ItemStack item, BlockPos pos) {
		int i = p.size();
		for(int j = 0; j < i; ++j) {
			Pair<ItemStack, BlockPos> pair = p.get(j);
			ItemStack itemstack = pair.getFirst();
			if (ItemEntity.areMergable(itemstack, item)) {
				ItemStack itemstack1 = ItemEntity.merge(itemstack, item, 16);
				p.set(j, Pair.of(itemstack1, pair.getSecond()));
				if (item.isEmpty()) {
				   return;
				}
			}
		}
		p.add(Pair.of(item, pos));
	}
	private void spawnDroppedItems(ObjectArrayList<Pair<ItemStack, BlockPos>> drops) {
		if (this.level.isClientSide || drops.isEmpty()) {
			return;
		}
		
		for (Pair<ItemStack, BlockPos> pair : drops) {
			ItemStack stack = pair.getFirst();
			BlockPos pos = pair.getSecond();
			
			if (!stack.isEmpty()) {
				// 在方块中心生成掉落物
				double x = pos.getX() + 0.5;
				double y = pos.getY() + 0.5;
				double z = pos.getZ() + 0.5;
				
				// 添加一些随机偏移，使掉落物更自然
				x += (this.level.random.nextDouble() - 0.5) * 0.2;
				z += (this.level.random.nextDouble() - 0.5) * 0.2;
				
				ItemEntity itemEntity = new ItemEntity(this.level, x, y, z, stack);
				
				// 给掉落物一个小的随机速度，模拟爆炸效果
				itemEntity.setDeltaMovement(
					(this.level.random.nextDouble() - 0.5) * 0.1,
					this.level.random.nextDouble() * 0.2,
					(this.level.random.nextDouble() - 0.5) * 0.1
				);
				
				// 设置短暂的拾取延迟，防止立即被玩家拾取
				itemEntity.setPickUpDelay(10);
				
				this.level.addFreshEntity(itemEntity);
			}
		}
	}

    /**
     * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
     */
    @Nullable
    public LivingEntity getExplosivePlacedBy()
    {
		if (this.exploder == null) {
		 return null;
		} else if (this.exploder instanceof LivingEntity) {
		 return (LivingEntity)this.exploder;
		} else {
		 if (this.exploder instanceof ProjectileBase) {
			Entity entity = ((ProjectileBase)this.exploder).getOwner();
			if (entity instanceof LivingEntity) {
			   return (LivingEntity)entity;
			}
		 }
		 return null;
		}
    }

    public void clearAffectedBlockPositions()
    {
        this.affectedBlockPositions.clear();
    }

    public List<BlockPos> getAffectedBlockPositions()
    {
        return this.affectedBlockPositions;
    }

    public Vec3 getPosition(){ return this.position; }
}
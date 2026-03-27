package wmlib.common.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;

import wmlib.common.bullet.ProjectileBase;

public class WMExplosion/* extends Explosion*/
{
    private final boolean causesFire;
	private final boolean isRad;
	
    private final boolean destroy;
    private final Random random;
    private final World level;
    private final double x;
    private final double y;
    private final double z;
    private final Entity exploder;
	private final LivingEntity shooeter=null;
    private final float size;
	private final float damage;
    private final List<BlockPos> affectedBlockPositions;
    /** Maps players to the knockback vector applied by the explosion, to send to the client */
    private final Map<PlayerEntity, Vector3d> playerKnockbackMap;
    private final Vector3d position;

    @OnlyIn(Dist.CLIENT)
    public WMExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions)
    {
        this(worldIn, entityIn, x, y, z, size, false, true, affectedPositions);
    }

    @OnlyIn(Dist.CLIENT)
    public WMExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean destroy, List<BlockPos> affectedPositions)
    {
        this(worldIn, entityIn, x, y, z, size, size, causesFire, destroy, false);
        this.affectedBlockPositions.addAll(affectedPositions);
    }

    public WMExplosion(World worldIn, Entity entityIn, double x, double y, double z, float damage, float size, boolean flaming, boolean exp, boolean rad)
    {
    	//super(worldIn, entityIn, x, y, z, size, flaming, Explosion.Mode.NONE);
        this.random = new Random();
        this.affectedBlockPositions = Lists.<BlockPos>newArrayList();
        this.playerKnockbackMap = Maps.<PlayerEntity, Vector3d>newHashMap();
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
        this.position = new Vector3d(this.x, this.y, this.z);
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    public void doExplosionA()
    {
		if(this.destroy||this.causesFire||this.isRad){
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
								BlockPos blockpos = new BlockPos(d4, d6, d8);
								BlockState iblockstate = this.level.getBlockState(blockpos);
								FluidState fluidstate = this.level.getFluidState(blockpos);
								if (!iblockstate.isAir(this.level, blockpos))
								{
									float f2 =/* this.exploder != null ? this.exploder.getBlockExplosionResistance(this, this.level, blockpos, iblockstate, fluidstate) : */iblockstate.getBlock().getExplosionResistance();
									f -= (f2 + 0.3F) * 0.3F;
								}
								if (f > 0.0F && (this.exploder == null /*|| this.exploder.shouldBlockExplode(this, this.level, blockpos, iblockstate, f)*/))
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

        float f3 = this.size * 2.0F;
        int k1 = MathHelper.floor(this.x - (double)f3 - 1.0D);
        int l1 = MathHelper.floor(this.x + (double)f3 + 1.0D);
        int i2 = MathHelper.floor(this.y - (double)f3 - 1.0D);
        int i1 = MathHelper.floor(this.y + (double)f3 + 1.0D);
        int j2 = MathHelper.floor(this.z - (double)f3 - 1.0D);
        int j1 = MathHelper.floor(this.z + (double)f3 + 1.0D);
		List<Entity> list = this.level.getEntities(this.exploder, new AxisAlignedBB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
		//net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.level, this, list, f3);
        Vector3d vec3d = new Vector3d(this.x, this.y, this.z);
        for (int k2 = 0; k2 < list.size(); ++k2)
        {
            Entity entity = list.get(k2);

            if (!entity.ignoreExplosion())
            {
            	if (entity instanceof ItemEntity) continue;
                double range = (double)(MathHelper.sqrt(entity.distanceToSqr(vec3d)) / (double)f3);

                if (range <= 1.0D)
                {
                    double d5 = entity.getX() - this.x;
                    double d7 = entity.getY() + (double)entity.getEyeHeight() - this.y;
                    double d9 = entity.getZ() - this.z;
                    double dis = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (dis != 0.0D)
                    {
                        d5 = d5 / dis;
                        d7 = d7 / dis;
                        d9 = d9 / dis;
                        double d14 = (double)getSeenPercent(vec3d, entity);
                        double d10 = (1.0D - range) * d14;
                        if(this.damage!=0)entity.hurt(DamageSource.explosion(this.getExplosivePlacedBy()), (float)((int)(d10 * this.damage * (0.3F+(double)f3*0.1F) + 1.0D)));
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

   public static float getSeenPercent(Vector3d p_222259_0_, Entity p_222259_1_) {
      AxisAlignedBB axisalignedbb = p_222259_1_.getBoundingBox();
      double d0 = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0D + 1.0D);
      double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0D + 1.0D);
      double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0D + 1.0D);
      double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
      double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
      if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
         int i = 0;
         int j = 0;

         for(float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0)) {
            for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1)) {
               for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2)) {
                  double d5 = MathHelper.lerp((double)f, axisalignedbb.minX, axisalignedbb.maxX);
                  double d6 = MathHelper.lerp((double)f1, axisalignedbb.minY, axisalignedbb.maxY);
                  double d7 = MathHelper.lerp((double)f2, axisalignedbb.minZ, axisalignedbb.maxZ);
                  Vector3d vector3d = new Vector3d(d5 + d3, d6, d7 + d4);
                  if (p_222259_1_.level.clip(new RayTraceContext(vector3d, p_222259_0_, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, p_222259_1_)).getType() == RayTraceResult.Type.MISS) {
                     ++i;
                  }

                  ++j;
               }
            }
         }

         return (float)i / (float)j;
      } else {
         return 0.0F;
      }
   }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB()
    {
        if(this.size>12F)this.level.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundCategory.BLOCKS, this.size, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F);
        /*if (this.destroy)
        {
			ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
            for (BlockPos blockpos : this.affectedBlockPositions)
            {
				
            }
        }*/

        if (this.causesFire)
        {
			//if (this.destroy)
			{
				for (BlockPos blockpos2 : this.affectedBlockPositions)
				{
					if (this.random.nextInt(8) == 1 && this.level.getBlockState(blockpos2).isAir() && this.level.getBlockState(blockpos2.below()).isSolidRender(this.level, blockpos2.below())) {
					   this.level.setBlockAndUpdate(blockpos2, AbstractFireBlock.getState(this.level, blockpos2));
					}
				}
			}
        }
    }

	private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> p_229976_0_, ItemStack p_229976_1_, BlockPos p_229976_2_) {
	}

    public Map<PlayerEntity, Vector3d> getPlayerKnockbackMap()
    {
        return this.playerKnockbackMap;
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

    public Vector3d getPosition(){ return this.position; }
}
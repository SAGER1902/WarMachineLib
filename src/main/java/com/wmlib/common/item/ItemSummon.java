package wmlib.common.item;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.entity.LivingEntity;
import wmlib.common.enchantment.EnchantmentTypes;
import net.minecraft.enchantment.Enchantment;
public class ItemSummon extends Item{

	public ItemSummon(Item.Properties builder) {
		super(builder);
	}
	
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity target, LivingEntity entity) {
	  return false;
	}
	public void spawnCreature(World worldIn, PlayerEntity playerIn, double par4, double par5, double par6)
	{
	}
   
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        if(enchantment.category == EnchantmentTypes.CREATURE||enchantment.category == EnchantmentTypes.VEHICLE)
        {
            return true;
        }else{
			return false;
		}
        //return super.canApplyAtEnchantingTable(stack, enchantment);
    }
    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return this.getItemStackLimit(stack) == 1;
    }

    @Override
    public int getEnchantmentValue()
    {
        return 5;
    }

   /**
    * Called when this item is used when targetting a Block
    */
   public ActionResultType useOn(ItemUseContext context) {
      World world = context.getLevel();
      if (world.isClientSide) {
         return ActionResultType.SUCCESS;
      } else {
         ItemStack itemstack = context.getItemInHand();
     	/*if (!context.getPlayer().abilities.instabuild)
        {
     		itemstack.shrink(1);
        }*/
     	BlockPos pos = context.getClickedPos();
		
    	spawnCreature(world, context.getPlayer(), (double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        return ActionResultType.SUCCESS;
      }
   }

   /**
    * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
    * {@link #onItemUse}.
    */
   public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      if (worldIn.isClientSide) {
         return new ActionResult<>(ActionResultType.PASS, itemstack);
      } else {
		 return new ActionResult<>(ActionResultType.FAIL, itemstack);
      }
   }
}
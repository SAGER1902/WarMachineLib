package wmlib.common.item;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
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
import wmlib.api.IRaderItem;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.client.util.ITooltipFlag;
public class ItemMouse extends Item implements IRaderItem {

	public ItemMouse(Item.Properties builder) {
		super(builder);
	}
	public String infor1 = "wmlib.infor.mouse1.desc";
	public String infor2 = "wmlib.infor.mouse2.desc";
	public String infor3 = "wmlib.infor.mouse3.desc";
	public String infor4 = "wmlib.infor.mouse4.desc";
	public String infor5 = "wmlib.infor.mouse5.desc";
	public String infor6 = "wmlib.infor.mouse6.desc";
	public String infor7 = "wmlib.infor.mouse7.desc";
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(infor1!=null)tooltip.add(new TranslationTextComponent(infor1).withStyle(TextFormatting.GREEN));//name
		if(infor2!=null)tooltip.add(new TranslationTextComponent(infor2).withStyle(TextFormatting.RED));//create
		if(infor3!=null)tooltip.add(new TranslationTextComponent(infor3).withStyle(TextFormatting.YELLOW));//describe1
		if(infor4!=null)tooltip.add(new TranslationTextComponent(infor4).withStyle(TextFormatting.YELLOW));//describe2
		if(infor5!=null)tooltip.add(new TranslationTextComponent(infor5).withStyle(TextFormatting.YELLOW));//describe3
		if(infor6!=null)tooltip.add(new TranslationTextComponent(infor6).withStyle(TextFormatting.RED));//weapon
		if(infor7!=null)tooltip.add(new TranslationTextComponent(infor7).withStyle(TextFormatting.GREEN));//weapon
	}
	
	public static double posX1 = 0;
	public static double posY1 = 0;
	public static double posZ1 = 0;
	public static double posX2 = 0;
	public static double posY2 = 0;
	public static double posZ2 = 0;
	boolean chosse = false;
	public int starttime = 0;
	public int cooltime=0;
	
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
	  /*if(isSelected) {
		  entityIn.setDeltaMovement(entityIn.getDeltaMovement().x * movespeed, entityIn.getDeltaMovement().y, entityIn.getDeltaMovement().z * movespeed);
	  }*/
	  

		if(entityIn instanceof PlayerEntity && isSelected){
			PlayerEntity player = (PlayerEntity)entityIn;
			{//

			}
		}
	  /*if(stack.hasTag()) {
			this.Gun_Reload(stack, worldIn, entityplayer, itemSlot, isSelected);
			this.gunbase_recoil(stack, worldIn, entityplayer, itemSlot, isSelected, 0);
	  }else {
		  CompoundNBT ltags = new CompoundNBT();
		  stack.setTag(ltags);
	  }*/
	}
	
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
    	//spawnCreature(world, context.getPlayer(), (double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
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
         RayTraceResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
         if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(ActionResultType.PASS, itemstack);
         } else {
            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
            BlockPos blockpos = blockraytraceresult.getBlockPos();
            if (!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
               return new ActionResult<>(ActionResultType.PASS, itemstack);
            } else if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos, blockraytraceresult.getDirection(), itemstack)) {
            	//spawnCreature(worldIn, playerIn, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
            	/*if (!playerIn.abilities.instabuild) {
                    itemstack.shrink(1);
                }*/
				playerIn.awardStat(Stats.ITEM_USED.get(this));
				return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
            } else {
               return new ActionResult<>(ActionResultType.FAIL, itemstack);
            }
         }
      }
   }
}
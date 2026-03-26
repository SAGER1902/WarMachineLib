package wmlib.common.item;
import java.util.List;

// 方块相关
import net.minecraft.world.level.block.state.BlockState;          // BlockState 路径更新
import net.minecraft.world.level.block.Blocks;                   // Blocks 包路径更新

// 实体和生成
import net.minecraft.world.entity.EntityType;                    // 路径更新
import net.minecraft.world.entity.MobSpawnType;                  // SpawnReason → MobSpawnType
import net.minecraft.world.entity.player.Player;                 // Player → Player

// 物品和交互
import net.minecraft.world.item.Item;                            // 路径更新
import net.minecraft.world.item.ItemStack;                       // 路径更新
import net.minecraft.world.item.context.UseOnContext;            // ItemUseContext → UseOnContext
import net.minecraft.world.InteractionResult;                    // 路径更新
import net.minecraft.world.InteractionHand;                      // Hand → InteractionHand

// NBT 和统计
import net.minecraft.nbt.CompoundTag;                            // CompoundNBT → CompoundTag
import net.minecraft.stats.Stats;                                // 路径更新

// 方块实体（TileEntity → BlockEntity）
import net.minecraft.world.level.block.entity.SpawnerBlockEntity; // MobSpawnerTileEntity → SpawnerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity;        // TileEntity → BlockEntity

// 文本和本地化
import net.minecraft.network.chat.Component;                     // ITextComponent → Component
import net.minecraft.ChatFormatting;                             // TextFormatting → ChatFormatting
import net.minecraft.network.chat.MutableComponent;              // 替代 TranslationTextComponent（通过 Component.translatable()）

// 工具提示和附魔
import net.minecraft.world.item.TooltipFlag;                     // ITooltipFlag → TooltipFlag
import net.minecraft.world.entity.EquipmentSlot;                 // EquipmentSlotType → EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment;         // 路径更新

// 其他
import net.minecraft.world.level.Level;                          // World → Level
import net.minecraft.world.entity.LivingEntity;                  // 路径更新
import net.minecraft.world.item.Items;                           // 路径更新
import net.minecraft.world.InteractionResultHolder;
import wmlib.common.enchantment.EnchantmentTypes;
public class ItemBless extends Item{
	public ItemBless(Item.Properties builder) {
		super(builder);
	}
	
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity target, LivingEntity entity) {
	  return false;
	}
	public String infor1 = "wmlib.infor.bless1.desc";
	public String infor3 = "wmlib.infor.bless2.desc";
	public String infor2 = "wmlib.infor.bless3.desc";
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if (infor1 != null) tooltip.add(Component.translatable(infor1).withStyle(ChatFormatting.GREEN));
		if (infor2 != null) tooltip.add(Component.translatable(infor2).withStyle(ChatFormatting.RED));
		if (infor3 != null) tooltip.add(Component.translatable(infor3).withStyle(ChatFormatting.YELLOW));
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
        return this.getMaxStackSize() == 1;
    }
    /*@Override
    public int getMaxStackSize() {
        return 1;
    }*/

    @Override
    public int getEnchantmentValue()
    {
        return 5;
    }

   /**
    * Called when this item is used when targetting a Block
    */
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		return InteractionResult.FAIL;
	}

   /**
    * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
    * {@link #onItemUse}.
    */
   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      if (worldIn.isClientSide) {
         return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
      } else {
		 return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
      }
   }
}
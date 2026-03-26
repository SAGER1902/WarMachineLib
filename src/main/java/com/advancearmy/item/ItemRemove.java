package advancearmy.item;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;          // BlockState 路径更新
import net.minecraft.world.level.block.Blocks;                   // Blocks 包路径更新
import net.minecraft.world.entity.EntityType;                    // 路径更新
import net.minecraft.world.entity.MobSpawnType;                  // SpawnReason → MobSpawnType
import net.minecraft.world.entity.player.Player;                 // Player → Player
import net.minecraft.world.item.Item;                            // 路径更新
import net.minecraft.world.item.ItemStack;                       // 路径更新
import net.minecraft.world.item.context.UseOnContext;            // ItemUseContext → UseOnContext
import net.minecraft.world.InteractionResult;                    // 路径更新
import net.minecraft.world.InteractionHand;                      // Hand → InteractionHand
import net.minecraft.nbt.CompoundTag;                            // CompoundNBT → CompoundTag
import net.minecraft.stats.Stats;                                // 路径更新
import net.minecraft.world.level.block.entity.SpawnerBlockEntity; // MobSpawnerTileEntity → SpawnerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity;        // TileEntity → BlockEntity
import net.minecraft.network.chat.Component;                     // ITextComponent → Component
import net.minecraft.ChatFormatting;                             // ChatFormatting → ChatFormatting
import net.minecraft.network.chat.MutableComponent;              // 替代 TranslationTextComponent（通过 Component.translatable()）
import net.minecraft.world.item.TooltipFlag;                     // ITooltipFlag → TooltipFlag
import net.minecraft.world.entity.EquipmentSlot;                 // EquipmentSlotType → EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment;         // 路径更新
import net.minecraft.world.level.Level;                          // World → Level
import net.minecraft.world.entity.LivingEntity;                  // 路径更新
import net.minecraft.world.item.Items;                           // 路径更新
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.server.level.ServerLevel;

import wmlib.common.enchantment.EnchantmentTypes;
import advancearmy.AdvanceArmy;
import wmlib.common.item.ItemSummon;
import wmlib.client.obj.SAObjModel;
public class ItemRemove extends Item{
	public ItemRemove(Item.Properties builder) {
		super(builder);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(Component.translatable("advancearmy.interact.itemremove.desc").withStyle(ChatFormatting.RED));//name
	}
   
	public boolean enc = false;
	@Override
  	public boolean isFoil(ItemStack p_77636_1_) {
	  return enc;
	}
	
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.SUCCESS;
		}
	}

	public boolean hurtEnemy(ItemStack itemstack, LivingEntity target, LivingEntity entity) {
		return false;
	}

	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		if (worldIn.isClientSide) {
			return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
		} else {
			return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
		}
	}
}
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
public class ItemSupport extends ItemSummon{
	public SAObjModel obj_model = null;
	public String obj_tex = "wmlib:textures/misc/vehicle_glint.png";
	public int id = 0;
	public int xp = 10;
	public int cool = 20;
	public int type = 0;// 1 once
	public ItemSupport(Item.Properties builder, int i, int t, int x, int c) {
		super(builder);
		this.id = i;
		this.type = t;
		this.xp=x;
		this.cool=c;
		this.infor3="经验值消耗:"+x;
		this.infor4="冷却时间:"+c/20F+"s";
		if(i==0){
			isSummon=true;
			enc=true;
			infor1="advancearmy.infor.portal_star.desc";
		}
		if(i==4)enc=true;
		if(i==5)enc=true;
		if(i==15)enc=true;
		if(i==16)enc=true;
		if(i==17)enc=true;
	}
	public boolean isSummon = false;
	public String infor1 = null;
	public String infor2 = null;
	public String infor3 = null;
	public String infor4 = null;
	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(!isSummon)tooltip.add(Component.translatable("advancearmy.infor.support1.desc").withStyle(ChatFormatting.GREEN));//name
		if(type==0){
			if(infor1!=null)tooltip.add(Component.translatable(infor1).withStyle(ChatFormatting.GREEN));//name
			if(infor2!=null)tooltip.add(Component.translatable(infor2).withStyle(ChatFormatting.RED));//create
			if(infor3!=null)tooltip.add(Component.translatable(infor3).withStyle(ChatFormatting.YELLOW));//describe1
			if(infor4!=null)tooltip.add(Component.translatable(infor4).withStyle(ChatFormatting.YELLOW));//describe2
		}else{
			tooltip.add(Component.translatable("advancearmy.infor.support2.desc").withStyle(ChatFormatting.RED));//create
		}
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
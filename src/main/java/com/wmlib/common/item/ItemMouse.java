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
import net.minecraft.world.InteractionResultHolder;
// 其他
import net.minecraft.world.level.Level;                          // World → Level
import net.minecraft.world.entity.LivingEntity;                  // 路径更新
import net.minecraft.world.item.Items;                           // 路径更新
import net.minecraft.world.entity.Entity;   
import wmlib.api.IRaderItem;
import wmlib.init.WMModEntities;
import wmlib.rts.XiangJiEntity;
public class ItemMouse extends Item implements IRaderItem {
	public ItemMouse(Item.Properties builder) {
		super(builder);
	}
	public int getType(){//1 rts
		return 1;
	}
	public String infor1 = "wmlib.infor.mouse1.desc";
	public String infor2 = "wmlib.infor.mouse2.desc";
	public String infor3 = "wmlib.infor.mouse3.desc";
	public String infor4 = "wmlib.infor.mouse4.desc";
	public String infor5 = "wmlib.infor.mouse5.desc";
	public String infor6 = "wmlib.infor.mouse6.desc";
	public String infor7 = "wmlib.infor.mouse7.desc";
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(infor1 != null)tooltip.add(Component.translatable(infor1).withStyle(ChatFormatting.GREEN));
		if(infor2 != null)tooltip.add(Component.translatable(infor2).withStyle(ChatFormatting.RED));
		if(infor3 != null)tooltip.add(Component.translatable(infor3).withStyle(ChatFormatting.YELLOW));
		if(infor4 != null)tooltip.add(Component.translatable(infor4).withStyle(ChatFormatting.YELLOW));
		if(infor5 != null)tooltip.add(Component.translatable(infor5).withStyle(ChatFormatting.RED));
		if(infor6 != null)tooltip.add(Component.translatable(infor6).withStyle(ChatFormatting.GREEN));
		if(infor7 != null)tooltip.add(Component.translatable(infor7).withStyle(ChatFormatting.YELLOW));
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
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
	}
	public InteractionResult useOn(UseOnContext context) {
	  Level world = context.getLevel();
	  if (world.isClientSide) {
		 return InteractionResult.SUCCESS;
	  } else {
		 ItemStack itemstack = context.getItemInHand();
		return InteractionResult.SUCCESS;
	  }
	}
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
	  ItemStack itemstack = playerIn.getItemInHand(handIn);
	  if (worldIn.isClientSide) {
		 return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
	  } else if(playerIn.isCrouching()||playerIn.getVehicle()!=null){
			boolean have = false;
			List<Entity> list = playerIn.level().getEntities(playerIn, playerIn.getBoundingBox().inflate(100D, 100D, 100D));
			for(int k2 = 0; k2 < list.size(); ++k2) {
				Entity ent = list.get(k2);
				if(ent instanceof XiangJiEntity){
					XiangJiEntity xj = (XiangJiEntity)ent;
					if(xj.getOwner()==playerIn){
						//xj.moveTo(playerIn.getX(), playerIn.getY()+10, playerIn.getZ(), 0, 0);
						if(!xj.isOpen()){
							xj.setChoose(true);
							xj.setOpen(true);
						}
						have = true;
						break;
					}
				}
			}
			if(!have){
				XiangJiEntity xj = new XiangJiEntity(WMModEntities.XIANG_JI.get(), worldIn);
				xj.moveTo(playerIn.getX(), playerIn.getY(), playerIn.getZ(), 0, 0);
				xj.tame(playerIn);
				worldIn.addFreshEntity(xj);
				playerIn.getCooldowns().addCooldown(this, 50);
			}
			return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
		}else{
			return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
		}
	}
}
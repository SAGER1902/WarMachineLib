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
import net.minecraft.world.level.Level;                          // Level → Level
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
import advancearmy.init.ModEntities;
import wmlib.common.enchantment.EnchantmentTypes;
import advancearmy.AdvanceArmy;
import advancearmy.entity.map.DefencePoint;
import wmlib.common.item.ItemSummon;
import wmlib.client.obj.SAObjModel;
public class ItemDefence extends ItemSummon{
	public int id = 0;
	public int xp = 10;
	public int cool = 20;
	public int type = 0;//0 vehicle 1 soldier
	public String name = "天呐，那是接近的";
	public ItemDefence(Item.Properties builder, int i, String n) {
		super(builder);
		this.id = i;
		this.name=n;
	}
	public String infor1 = "advancearmy.infor.defence1.desc";
	public String infor2 = "advancearmy.infor.defence2.desc";
	public String infor3 = "advancearmy.infor.defence3.desc";
	public String infor4 = "advancearmy.infor.defence4.desc";
	public String infor5 = "advancearmy.infor.defence5.desc";
	public String infor6 = "advancearmy.infor.defence6.desc";
	public String infor7 = "advancearmy.infor.defence7.desc";
	public String infor8 = "advancearmy.infor.defence8.desc";
	public String infor9 = "advancearmy.infor.defence9.desc";
	
	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(infor1!=null)tooltip.add(Component.translatable(infor1).withStyle(ChatFormatting.GREEN));//name
		if(infor2!=null)tooltip.add(Component.translatable(infor2).withStyle(ChatFormatting.RED));//create
		if(infor3!=null)tooltip.add(Component.translatable(infor3).withStyle(ChatFormatting.YELLOW));//describe1
		if(infor4!=null)tooltip.add(Component.translatable(infor4).withStyle(ChatFormatting.YELLOW));//describe2
		if(infor5!=null)tooltip.add(Component.translatable(infor5).withStyle(ChatFormatting.RED));//weapon
		if(infor6!=null)tooltip.add(Component.translatable(infor6).withStyle(ChatFormatting.RED));//health/seat
		if(infor7!=null)tooltip.add(Component.translatable(infor7).withStyle(ChatFormatting.GREEN));//armor
		if(infor8!=null)tooltip.add(Component.translatable(infor8).withStyle(ChatFormatting.AQUA));//turret_armor
		if(infor8!=null)tooltip.add(Component.translatable(infor9).withStyle(ChatFormatting.RED));//turret_armor
	}
	
	public void spawnCreature(Level worldIn, Player playerIn, int weaponid, boolean summon, double x, double y, double z, int summonid)
	{
		if (worldIn.isClientSide) return;
		if(id!=7){
			DefencePoint point = new DefencePoint(ModEntities.ENTITY_DPT.get(), worldIn);
			if(playerIn!=null){
				/*if(playerIn.isCrouching() && playerIn.isCreative()){
				}else*/{
					point.tame(playerIn);
					if(playerIn.getTeam()!=null && playerIn.getTeam() instanceof PlayerTeam){
						playerIn.level().getScoreboard().addPlayerToTeam(point.getUUID().toString(), (PlayerTeam)playerIn.getTeam());
					}
				}
			}
			point.setSummonID(id);
			point.moveTo(x + 0.5, y+1, z + 0.5, 0, 0);
			worldIn.addFreshEntity(point);
			point.setCustomName(Component.translatable(name));
		}else{
			playerIn.sendSystemMessage(Component.translatable("这个挑战还没做完!"));
		}
	}

	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			if(context.getPlayer().getTeam()!=null){
				ItemStack itemstack = context.getItemInHand();
				if (!context.getPlayer().isCreative())itemstack.shrink(1);
				BlockPos pos = context.getClickedPos();
				spawnCreature(world, context.getPlayer(), 0, false, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(),0);
				return InteractionResult.SUCCESS;
			}else{
				context.getPlayer().sendSystemMessage(Component.translatable("advancearmy.infor.needteam.desc"));
				return InteractionResult.PASS;
			}
		}
	}
}
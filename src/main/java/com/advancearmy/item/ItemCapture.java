package advancearmy.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.EntityType;
import java.util.List;

public class ItemCapture extends AbstractEntityStorageItem {
    
    public ItemCapture(Item.Properties builder) {
        super(builder);
    }
    
	public void spawnAddSeat(Level worldIn, Player playerIn,double x, double y, double z, Entity seat){
		/*ItemStack stack = playerIn.getMainHandItem();
		Entity released = releaseEntity(stack, worldIn, x, y, z);
		if (released != null)released.startRiding(seat);*/
	}
	
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        // 如果已经存储了生物，右击空气可以释放
        /*if (hasStoredEntity(stack))*/{
            if (!level.isClientSide) {
                BlockPos spawnPos = player.blockPosition().relative(player.getDirection());
                Entity released = releaseEntity(stack, level, 
                    spawnPos.getX() + 0.5, 
                    spawnPos.getY(), 
                    spawnPos.getZ() + 0.5);
                
                if (released != null) {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS,
                        1.0F, 1.0F);
                    player.getCooldowns().addCooldown(this, 20);
					stack.shrink(1);
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        
        //return InteractionResultHolder.pass(stack);
    }
    
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack1, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.level();
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        // 尝试存储生物
        if (stack.getItem() == this && !hasStoredEntity(stack) && canStoreEntity(player,target)) {
            if (storeEntity(stack, target, player)) {
                if (!level.isClientSide) {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS,
                        1.0F, 0.8F);
                    player.getCooldowns().addCooldown(this, 40);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        
        return InteractionResult.PASS;
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        
        // 在地面上释放生物
        /*if (hasStoredEntity(stack))*/{
            BlockPos spawnPos = pos.relative(direction);
            
            if (level.getBlockState(spawnPos).isAir() && 
                level.getBlockState(spawnPos.above()).isAir()) {
                
                if (!level.isClientSide) {
                    Entity released = releaseEntity(stack, level, 
                        spawnPos.getX() + 0.5, 
                        spawnPos.getY(), 
                        spawnPos.getZ() + 0.5);
                    
                    if (released != null) {
                        level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, 
                            SoundSource.BLOCKS, 1.0F, 1.0F);
                        player.getCooldowns().addCooldown(this, 20);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, 
                               List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("advancearmy.interact.itemcapture.desc").withStyle(ChatFormatting.GREEN));
		tooltip.add(Component.translatable("advancearmy.interact.itemcapture1.desc").withStyle(ChatFormatting.BLUE));
		
        if (hasStoredEntity(stack)) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains(DISPLAY_NAME_TAG)) {
                String name = tag.getString(DISPLAY_NAME_TAG);
                tooltip.add(Component.literal("Contains: " + name)
                    .withStyle(ChatFormatting.AQUA));
				
				String nameO = stack.getTag().getString(OWNER_TAG);
				if(nameO!=null){
					tooltip.add(Component.literal("Owner: " + nameO)
						.withStyle(ChatFormatting.GREEN));
				}
				
                EntityType<?> type = getStoredEntityType(stack);
                if (type != null) {
                    tooltip.add(Component.literal("Type: " + type.getDescription().getString())
                        .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                }
            }
        } else {
            tooltip.add(Component.literal("Empty")
                .withStyle(ChatFormatting.GRAY));
        }
    }
}
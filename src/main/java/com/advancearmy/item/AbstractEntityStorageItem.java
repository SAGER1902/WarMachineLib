package advancearmy.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.Containers;
import advancearmy.init.ModItems;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.Util;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
public abstract class AbstractEntityStorageItem extends Item {
    protected static final String STORE_INFO = "StoreInfo";
    
    protected static final String DISPLAY_TYPE_TAG = "DisplayType";
    protected static final String DISPLAY_NAME_TAG = "DisplayName";
    protected static final String OWNER_TAG = "Owner";
	
    public AbstractEntityStorageItem(Item.Properties builder) {
        super(builder);
    }
    
    // 检查是否有存储的生物
    public static boolean hasStoredEntity(ItemStack stack) {
		return stack.hasTag() && !Objects.requireNonNull(stack.getTag()).getCompound(STORE_INFO).isEmpty();
    }
    
    // 获取存储的生物数据
    public static CompoundTag getStoredEntityData(ItemStack stack) {
        if (hasStoredEntity(stack)) {
            return Objects.requireNonNull(stack.getTag()).getCompound(STORE_INFO);
        }
        return new CompoundTag();
    }

    @Nullable
    public static EntityType<?> getStoredEntityType(ItemStack stack) {
        if (!hasStoredEntity(stack)) return null;
        String typeStr = stack.getTag().getString(DISPLAY_TYPE_TAG);
        return EntityType.byString(typeStr).orElse(null);
    }

    // 存储生物到物品
    public static boolean storeEntity(ItemStack stack, LivingEntity entity, @Nullable Player player) {
        if (entity.level().isClientSide) return false;
        //if (!player.isCreative()&&!canStoreEntity(entity)) return false;
		
        CompoundTag entityTag = new CompoundTag();
		CompoundTag itemTag = new CompoundTag()/*stack.getOrCreateTag()*/;
        entity.saveWithoutId(entityTag);
		entityTag.putString("id", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())).toString());
        itemTag.put(STORE_INFO, entityTag);
		
        itemTag.putString(DISPLAY_TYPE_TAG, EntityType.getKey(entity.getType()).toString());
        if (entity.hasCustomName()) {
            itemTag.putString(DISPLAY_NAME_TAG, entity.getCustomName().getString());
        } else {
            itemTag.putString(DISPLAY_NAME_TAG, entity.getDisplayName().getString());
        }
		
		if(entity instanceof TamableAnimal ent){
			if(ent.getOwner()!=null){
				if(ent.getOwner() instanceof Player owner){
					itemTag.putString(OWNER_TAG, owner.getName().getString());
				}else{
					if (ent.getOwner().hasCustomName()) {
						itemTag.putString(DISPLAY_NAME_TAG, ent.getOwner().getCustomName().getString());
					} else {
						itemTag.putString(DISPLAY_NAME_TAG, ent.getOwner().getDisplayName().getString());
					}
				}
			}
		}
		
        stack.setTag(itemTag);
		
		/*ItemStack once = ModItems.unit_capture.get().getDefaultInstance();
		once.setTag(itemTag);
        Containers.dropItemStack(worldIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), once);*/

        //onEntityStored(entity, stack);
        entity.discard();
        
        return true;
    }
	
    // 释放生物
    @Nullable
    public static Entity releaseEntity(ItemStack stack, Level level, double x, double y, double z) {
        if (!hasStoredEntity(stack)) return null;
        
		CompoundTag entityData = getStoredEntityData(stack);
        Optional<Entity> entityOptional = Util.ifElse(EntityType.by(entityData).map(type -> type.create(level)), entity -> {
            entity.load(entityData);
        }, null);
        if (entityOptional.isPresent() && entityOptional.get() instanceof LivingEntity ent) {
            ent.setPos(x, y, z);
            if (!level.isClientSide) {
                level.addFreshEntity(ent);
            }
            //stack.shrink(1);
            stack.removeTagKey(STORE_INFO);
			//stack.setTag(null);
            //onEntityReleased(entity, stack);
            return ent;
        }
        return null;
    }

    protected static boolean canStoreEntity(Player player, LivingEntity entity) {
        return player.isCreative()||entity instanceof TamableAnimal ent && ent.getOwner()==player;
    }
	
    @Override
    public boolean isFoil(ItemStack stack) {
        return hasStoredEntity(stack);
    }
}
package wmlib.common.item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
public class ItemGlint extends BlockItem{

	public ItemGlint(Block p_i50041_1_, Item.Properties p_i50041_2_) {
	  super(p_i50041_1_, p_i50041_2_);
	}

	public boolean isFoil(ItemStack p_77636_1_) {
	  return true;
	}
}
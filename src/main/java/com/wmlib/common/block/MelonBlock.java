package wmlib.common.block;

import wmlib.common.tileentity.MelonBlockTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
//import net.minecraft.tileentity.ITickableTileEntity;
public class MelonBlock extends AbstractFacingBlock/* implements ITickableTileEntity*/{

	public final int lvl;
	
	public MelonBlock(Properties properties, int lvl) {
		super(properties);
		this.lvl = lvl;
	}

	/*public int iii = 0;
	
	@Override
	public void tick() {
		if(this.lvl>4){
			if(iii<360){
				++iii;
			}else{
				iii=0;
			}
		}
	}*/
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new MelonBlockTileEntity();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
}

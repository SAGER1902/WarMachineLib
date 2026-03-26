package wmlib.common.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MelonBlockTileEntity extends BlockEntity {

    public MelonBlockTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityRegister.MELON.get(), pos, state);
    }
}
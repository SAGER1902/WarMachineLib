package wmlib.common.block;

import wmlib.common.tileentity.MelonBlockTileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
public class MelonBlock extends AbstractFacingBlock {

    public final int lvl;
    
    public MelonBlock(Properties properties, int lvl) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH));
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
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MelonBlockTileEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // 如果需要tick功能，可以在这里实现
        // 例如：return level.isClientSide ? null : createTickerHelper(type, TileEntityRegister.MELON.get(), MelonBlockTileEntity::serverTick);
        return null;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL; // 或者 RenderShape.ENTITYBLOCK_ANIMATED
    }
    
    // 如果方块不是完整方块，需要设置形状
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0, 0, 16, 16, 16); // 完整方块
    }

}
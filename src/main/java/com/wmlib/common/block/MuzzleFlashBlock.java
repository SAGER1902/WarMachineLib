package wmlib.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
public class MuzzleFlashBlock extends Block {
	public int time = 3;
    public MuzzleFlashBlock(int light, int t) {
        super(BlockBehaviour.Properties.copy(Blocks.AIR)
            .lightLevel(state -> light)
			.strength(0,0)
            .noCollission()
            .noOcclusion()
            .air()
            .noLootTable()
            .instabreak()
            .isValidSpawn((state, getter, pos, type) -> false)
            .isRedstoneConductor((state, getter, pos) -> false)
            .isSuffocating((state, getter, pos) -> false)
            .isViewBlocking((state, getter, pos) -> false)
        );
		this.time=t;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 1.0F;
    }

    /*public static void activateBlock(Level world, BlockPos pos) {
        BlockState currentState = world.getBlockState(pos);
        if (currentState.getBlock() instanceof MuzzleFlashBlock) {
            // 重置生命周期和亮度
            BlockState newState = currentState
                .setValue(LIFETIME, 0)
                .setValue(BRIGHTNESS, PEAK_BRIGHTNESS);
            // 使用 Block.UPDATE_CLIENTS 减少开销
            world.setBlock(pos, newState, Block.UPDATE_CLIENTS);
            world.scheduleTick(pos, currentState.getBlock(), 1);
        }
    }*/

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isClientSide()) {
            world.scheduleTick(pos, this, time);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!world.isClientSide()) {
            world.removeBlock(pos, false);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }
}
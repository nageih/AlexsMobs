package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateFlag;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateShipWheel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockEndPirateFlag extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape AABB = Block.box(6, 0, 6, 10, 16, 10);

    public BlockEndPirateFlag() {
        super(Properties.of(Material.EGG).noOcclusion().sound(SoundType.WOOD).strength(1F).lightLevel((i) -> 15).noCollission().requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos p_52801_) {
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, state2, level, pos, p_52801_);
    }

    public VoxelShape getShape(BlockState p_54561_, BlockGetter p_54562_, BlockPos p_54563_, CollisionContext p_54564_) {
        return AABB;
    }

    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return canSupportCenter(world, pos.below(), Direction.UP) || canSupportCenter(world, pos.above(), Direction.DOWN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityEndPirateFlag(pos, state);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152180_, BlockState p_152181_, BlockEntityType<T> p_152182_) {
        return createTickerHelper(p_152182_, AMTileEntityRegistry.END_PIRATE_FLAG.get(), TileEntityEndPirateFlag::commonTick);
    }

    public void animateTick(BlockState p_53094_, Level p_53095_, BlockPos p_53096_, Random p_53097_) {
        double d0 = (double)p_53096_.getX() + 0.55D - (double)(p_53097_.nextFloat() * 0.1F);
        double d1 = (double)p_53096_.getY() + 0.55D - (double)(p_53097_.nextFloat() * 0.1F);
        double d2 = (double)p_53096_.getZ() + 0.55D - (double)(p_53097_.nextFloat() * 0.1F);
        double d3 = (double)(0.4F - (p_53097_.nextFloat() + p_53097_.nextFloat()) * 0.4F);
        if (p_53097_.nextInt(5) == 0) {
            p_53095_.addParticle(ParticleTypes.END_ROD, d0, d1 +  d3, d2, p_53097_.nextGaussian() * 0.005D, p_53097_.nextGaussian() * 0.005D, p_53097_.nextGaussian() * 0.005D);
        }

    }
}

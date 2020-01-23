package com.github.taylort7147.tmod.block;

import com.github.taylort7147.tmod.TMod;
import com.github.taylort7147.tmod.teleporter.Endpoint;
import com.github.taylort7147.tmod.teleporter.event.TeleporterEvent;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeBlockState;

public class BlockTeleporter extends Block
{
    public static final String NAME = "teleporter";
    public static final String BLOCK_NAME = NAME + "_block";
    public static final String TEXTURE_NAME = TMod.MODID + ":" + BLOCK_NAME;

    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
    public static final int TICKS_BEFORE_ACTIVATION = 10;

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0f, 0f, 0f, 1.0f, 0.15f, 1.0f);

    public BlockTeleporter(final Properties properties)
    {
        super(properties);
        setDefaultState(stateContainer.getBaseState().with(ACTIVATED, Boolean.valueOf(false)));
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(ACTIVATED);
    }

    @Override
    public boolean ticksRandomly(BlockState state)
    {
//		return super.ticksRandomly(state);
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(ACTIVATED, false);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return VoxelShapes.create(BOUNDING_BOX);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos,
            boolean isMoving)
    {
        // TODO Auto-generated method stub
        super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
        updatePowerState((World) world, pos, state);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor)
    {
        // TODO Auto-generated method stub
        super.onNeighborChange(state, world, pos, neighbor);
        System.out.println("onNeighborChange");
        updatePowerState((World) world, pos, state);
    }

    private void updatePowerState(World world, BlockPos pos, IForgeBlockState state)
    {
        if(!world.isRemote())
        {
            boolean isActivated = state.getBlockState().get(ACTIVATED);
            boolean isPowered = world.isBlockPowered(pos);
            if(isPowered && !isActivated)
            {
                // Toggled on - activate
                System.out.println("Toggle on");
                world.setBlockState(pos, state.getBlockState().with(ACTIVATED, Boolean.valueOf(true)), 3);
                world.getPendingBlockTicks().scheduleTick(pos, this, TICKS_BEFORE_ACTIVATION);

                Endpoint origin = new Endpoint(pos, world.getDimension().getType().getId());
                TeleporterEvent event = new TeleporterEvent.TeleporterActivated(world, origin);
                MinecraftForge.EVENT_BUS.post(event);
            }
            else if(!isPowered && isActivated)
            {
                // Toggled off - reset
                System.out.println("Toggle off");
                world.setBlockState(pos, state.getBlockState().with(ACTIVATED, Boolean.valueOf(false)), 3);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        updatePowerState(worldIn, pos, state);
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state)
    {
        super.onPlayerDestroy(worldIn, pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int eventId, int eventParam)
    {
        super.eventReceived(state, worldIn, pos, eventId, eventParam);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        return tileEntity == null ? false : tileEntity.receiveClientEvent(eventId, eventParam);
    }

//	@Override
//	public void updateTick(World worldIn, BlockPos pos, BlockState state, Random random)
//	{
//		super.updateTick(worldIn, pos, state, random);
//		if (!worldIn.isRemote)
//		{
//			// Is this teleporter linked?
//			int dimension = worldIn.provider.getDimension();
//			ITeleporterLinkContainer linkContainer = TMod.Instance.getTeleporterLinkContainer();
//			if (linkContainer.containsLinkWithPos(dimension, pos))
//			{
//				BlockPos destination = linkContainer.getDestination(dimension, pos);
//								
//				// Teleport player(s?)
//				List<EntityPlayer> playersToTeleport = worldIn.getPlayers(EntityPlayer.class,
//						player -> player.getPosition().equals(pos));
//				for(EntityPlayer player : playersToTeleport)
//				{
//					int x = destination.getX();
//					int y = destination.getY();
//					int z = destination.getZ();
//					PlayerUtilities.Teleport(player, player.dimension, x, y, z);
//				}
//			}
//
//		}
//	}

}

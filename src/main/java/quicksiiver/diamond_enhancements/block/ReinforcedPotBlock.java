package quicksiiver.diamond_enhancements.block;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import quicksiiver.diamond_enhancements.DiamondEnhancements;

public class ReinforcedPotBlock extends BaseEntityBlock {
    public ReinforcedPotBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(ReinforcedPotBlock::new);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ReinforcedPotBlockEntity(pos, state);
	}

	// @Override
    // protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    //     if (!(level.getBlockEntity(pos) instanceof ReinforcedPotBlockEntity reinforcedPotBlockEntity)) {
    //         return super.useWithoutItem(state, level, pos, player, hit);
    //     }

	// 	DiamondEnhancements.LOGGER.info("useWithoutItem called");

	// 	ItemStack items = reinforcedPotBlockEntity.takeItems();
	// 	player.setItemInHand(InteractionHand.MAIN_HAND, items);

    //     return InteractionResult.SUCCESS;
    // }

	@Override
	protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!(level.getBlockEntity(pos) instanceof ReinforcedPotBlockEntity reinforcedPotBlockEntity)) {
            return super.useItemOn(itemStack, state, level, pos, player, hand, hit);
        }

		if (itemStack.isEmpty()) {
			// take an item out if the hand has nothing in it, and give it to the hand
			ItemStack items = reinforcedPotBlockEntity.takeItems();
			player.setItemInHand(hand, items);
		} else {
			// otherwise, try to put the item into the pot
			int leftover = reinforcedPotBlockEntity.addItems(itemStack);
			if (leftover == 0) {
				// empty hand (none left over)
				player.setItemInHand(hand, ItemStack.EMPTY);
			} else if (leftover != player.getItemInHand(hand).getCount()) { // only change it if there was a difference
				player.setItemInHand(hand, new ItemStack(itemStack.getItem(), leftover));
			}
		}

		return InteractionResult.SUCCESS;
	}
}

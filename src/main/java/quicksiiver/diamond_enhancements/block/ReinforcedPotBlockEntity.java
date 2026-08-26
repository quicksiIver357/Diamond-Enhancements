package quicksiiver.diamond_enhancements.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import quicksiiver.diamond_enhancements.ModBlockEntities;

public class ReinforcedPotBlockEntity extends BlockEntity {
	private int itemCount = 0;
	private Item storedItem = null;

	// wobble stuff for animation
	public DecoratedPotBlockEntity.@Nullable WobbleStyle lastWobbleStyle;
	public long wobbleStartedAtTick;

    public ReinforcedPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REINFORCED_POT, pos, state);
    }

    
	// getters
	public int getItemCount() {
		return this.itemCount;
	}
	public Item getStoredItem() {
		return this.storedItem;
	}

	// setters
	private void setStoredItem(Item item) {
		this.storedItem = item;
		this.setChanged(); // notify the server that block entity data changed
	}
	private void setItemCount(int count) {
		this.itemCount = count;
        this.setChanged(); // notify the server that block entity data changed
	}

	// modifiers
	public int addItems(ItemStack itemStack) {
		// if there is no item inside, set the item to the new item
		if (this.getStoredItem() == null) {
			// set the item and the stack
			this.setStoredItem(itemStack.getItem());
			this.setItemCount(itemStack.getCount());
            return 0;
		} else if (itemStack.is(this.storedItem)) {
			// add items to the stack if they can be added
            if (this.getItemCount() + itemStack.getCount() <= 1024) {
			    this.setItemCount(this.getItemCount() + itemStack.getCount());
                return 0;
            } else {
                int amountToReturn = -(1024 - itemStack.getCount() - this.getItemCount());
                this.setItemCount(1024); // set max count
                return amountToReturn;
            }
		} else {
            // return the itemStack's count (no change)
            return itemStack.getCount();
        }
	}

    public ItemStack takeItems() {
        // if there is an item inside, proceed
        // DiamondEnhancements.LOGGER.info("Stored Item: " + this.getStoredItem());
        if (this.getStoredItem() != null) {
            return new ItemStack(this.storedItem, this.itemCount);
        }
        // no item inside
        return ItemStack.EMPTY;
    }

    
}

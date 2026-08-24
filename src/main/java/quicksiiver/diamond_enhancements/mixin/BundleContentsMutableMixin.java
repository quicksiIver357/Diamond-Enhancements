package quicksiiver.diamond_enhancements.mixin;

import java.util.List;

import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.serialization.DataResult;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Bees;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import quicksiiver.diamond_enhancements.component.BundleContentsMutableInterface;

@Mixin(BundleContents.Mutable.class)
public class BundleContentsMutableMixin implements BundleContentsMutableInterface {
    @Shadow
    private Fraction weight;

    @Shadow
    private List<ItemStack> items;

    private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);
    private static final DataResult<Fraction> BEEHIVE_WEIGHT = DataResult.success(Fraction.ONE);

    @Shadow
    private int findStackIndex(final ItemStack itemsToAdd) {
        throw new AssertionError(); // will be replaced with vanilla code at runtime
    }

    


    @Override
    public int getMaxAmountToAdd(final Fraction itemWeight, int bundleSize) {
        Fraction remainingWeight = Fraction.getFraction(bundleSize).subtract(this.weight);
        return Math.max(remainingWeight.divideBy(itemWeight).intValue(), 0);
    }

    @Override
    public int tryInsert(final ItemStack itemsToAdd, int bundleSize) {
        if (!BundleContents.canItemBeInBundle(itemsToAdd)) {
            return 0;
        }

        DataResult<Fraction> maybeItemWeight = getWeight(itemsToAdd);
        if (maybeItemWeight.isError()) {
            return 0;
        }

        Fraction itemWeight = maybeItemWeight.getOrThrow();

        int maxAmountToAdd = Math.max(
            Fraction.getFraction(bundleSize, 1)
                .subtract(this.weight)
                .divideBy(itemWeight)
                .intValue(),
            0
        );

        int amountToAdd = Math.min(itemsToAdd.getCount(), maxAmountToAdd);

        if (amountToAdd == 0) {
            return 0;
        }

        // Update total bundle weight
        this.weight = this.weight.add(
            itemWeight.multiplyBy(Fraction.getFraction(amountToAdd, 1))
        );

        int stackIndex = this.findStackIndex(itemsToAdd);

        if (stackIndex != -1) {
            ItemStack existingStack = this.items.get(stackIndex);

            // Fill the existing stack without exceeding its max stack size
            int spaceInStack =
                existingStack.getMaxStackSize() - existingStack.getCount();

            int amountToMerge = Math.min(amountToAdd, spaceInStack);

            existingStack.grow(amountToMerge);
            itemsToAdd.shrink(amountToMerge);

            int remaining = amountToAdd - amountToMerge;

            // If there is still more, put it into a separate stack
            if (remaining > 0) {
                this.items.add(0, itemsToAdd.split(remaining));
            }
        } else {
            // No matching stack, so create a new one
            this.items.add(0, itemsToAdd.split(amountToAdd));
        }

        return amountToAdd;
    }

    @Override
    public int tryTransfer(final Slot slot, final Player player, int bundleSize) {
        ItemStack other = slot.getItem();
         DataResult<Fraction> itemWeight = getWeight(other);
         if (itemWeight.isError()) {
            return 0;
         } else {
            int maxAmount = this.getMaxAmountToAdd((Fraction)itemWeight.getOrThrow(), bundleSize);
            return BundleContents.canItemBeInBundle(other) ? this.tryInsert(slot.safeTake(other.getCount(), maxAmount, player), bundleSize) : 0;
         }
    }

    
    private static DataResult<Fraction> getWeight(final ItemInstance item) {
      BundleContents bundle = (BundleContents)item.get(DataComponents.BUNDLE_CONTENTS);
      if (bundle != null) {
         return bundle.weight().map((nestedWeight) -> nestedWeight.add(BUNDLE_IN_BUNDLE_WEIGHT));
      } else {
         List<BeehiveBlockEntity.Occupant> bees = ((Bees)item.getOrDefault(DataComponents.BEES, Bees.EMPTY)).bees();
         return !bees.isEmpty() ? BEEHIVE_WEIGHT : DataResult.success(Fraction.getFraction(1, item.getMaxStackSize()));
      }
    }

}

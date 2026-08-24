package quicksiiver.diamond_enhancements.component;

import org.apache.commons.lang3.math.Fraction;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface BundleContentsMutableInterface {
    int tryInsert(ItemStack stack, int bundleSize);
    int tryTransfer(Slot slot, Player player, int bundleSize);
    int getMaxAmountToAdd(Fraction itemWeight, int bundleSize);
}

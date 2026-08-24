package quicksiiver.diamond_enhancements.item;

import java.util.Optional;

import org.apache.commons.lang3.math.Fraction;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.TooltipDisplay;
import quicksiiver.diamond_enhancements.component.BundleContentsMutableInterface;
import quicksiiver.diamond_enhancements.tooltip.DiamondBundleTooltip;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DiamondBundle extends BundleItem {
    private static final int FULL_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
    private static final int BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);

    // inherit constructor
    public DiamondBundle(Item.Properties properties) {
    super(properties);
    }

    private static void playRemoveOneSound(final Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertSound(final Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertFailSound(final Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 1.0F, 1.0F);
    }

    private void broadcastChangesOnContainerMenu(final Player player) {
        AbstractContainerMenu containerMenu = player.containerMenu;
        if (containerMenu != null) {
            containerMenu.slotsChanged(player.getInventory());
        }
    }

    @Override
    public boolean overrideStackedOnOther(final ItemStack self, final Slot slot, final ClickAction clickAction, final Player player) {
    BundleContents initialContents = (BundleContents)self.get(DataComponents.BUNDLE_CONTENTS);
    if (initialContents == null) {
    return false;
    } else {
    ItemStack other = slot.getItem();
    BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
    if (clickAction == ClickAction.PRIMARY && !other.isEmpty()) {
        if (((BundleContentsMutableInterface) contents).tryTransfer(slot, player, 4) > 0) {
            playInsertSound(player);
        } else {
            playInsertFailSound(player);
        }

        self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
        this.broadcastChangesOnContainerMenu(player);
        return true;
    } else if (clickAction == ClickAction.SECONDARY && other.isEmpty()) {
        ItemStack itemStack = contents.removeOne();
        if (itemStack != null) {
            ItemStack remainder = slot.safeInsert(itemStack);
            if (remainder.getCount() > 0) {

                ((BundleContentsMutableInterface) contents).tryInsert(remainder, 4);
            } else {
                playRemoveOneSound(player);
            }
        }

        self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
        this.broadcastChangesOnContainerMenu(player);
        return true;
    } else {
        return false;
    }
    }
    }

    @Override
    public boolean overrideOtherStackedOnMe(final ItemStack self, final ItemStack other, final Slot slot, final ClickAction clickAction, final Player player, final SlotAccess carriedItem) {
      if (clickAction == ClickAction.PRIMARY && other.isEmpty()) {
         toggleSelectedItem(self, -1);
         return false;
      } else {
         BundleContents initialContents = (BundleContents)self.get(DataComponents.BUNDLE_CONTENTS);
         if (initialContents == null) {
            return false;
         } else {
            BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
            if (clickAction == ClickAction.PRIMARY && !other.isEmpty()) {
               if (slot.allowModification(player) && ((BundleContentsMutableInterface) contents).tryInsert(other, 4) > 0) {
                  playInsertSound(player);
               } else {
                  playInsertFailSound(player);
               }

               self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
               this.broadcastChangesOnContainerMenu(player);
               return true;
            } else if (clickAction == ClickAction.SECONDARY && other.isEmpty()) {
               if (slot.allowModification(player)) {
                  ItemStack removed = contents.removeOne();
                  if (removed != null) {
                     playRemoveOneSound(player);
                     carriedItem.set(removed);
                  }
               }

               self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
               this.broadcastChangesOnContainerMenu(player);
               return true;
            } else {
               toggleSelectedItem(self, -1);
               return false;
            }
         }
      }
   }

    // @Override
    // public static float getFullnessDisplay(final ItemStack itemStack) {
    //     BundleContents contents = (BundleContents)itemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
    //     return contents.weight().getOrThrow().floatValue() / 4;
    // }

    @Override
    public int getBarWidth(final ItemStack stack) {
        BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return Math.min(1 + Mth.mulAndTruncate(contents.weight().getOrThrow(), 12), 52) / 4;
    }

    @Override
    public int getBarColor(final ItemStack stack) {
        BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return contents.weight().getOrThrow().compareTo(Fraction.getFraction(4)) >= 0 ? FULL_BAR_COLOR : BAR_COLOR;
    }

    @Override
	public Optional<TooltipComponent> getTooltipImage(final ItemStack bundle) {
		TooltipDisplay display = bundle.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
		return !display.shows(DataComponents.BUNDLE_CONTENTS)
			? Optional.empty()
			: Optional.ofNullable(bundle.get(DataComponents.BUNDLE_CONTENTS)).map(DiamondBundleTooltip::new);
	}
}

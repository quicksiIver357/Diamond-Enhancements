package quicksiiver.diamond_enhancements.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import quicksiiver.diamond_enhancements.client.tooltip.ClientDiamondBundleTooltip;
import quicksiiver.diamond_enhancements.tooltip.DiamondBundleTooltip;

@Mixin(ClientTooltipComponent.class)
public interface ClientTooltipComponentMixin {

    @Inject(method = "create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;", at = @At("HEAD"), cancellable = true)
    private static void creatDiamondBundleTooltip(TooltipComponent component, CallbackInfoReturnable<ClientTooltipComponent> cir) {
        if (component instanceof DiamondBundleTooltip tooltip) { cir.setReturnValue(new ClientDiamondBundleTooltip(tooltip.contents())); }
    }
}

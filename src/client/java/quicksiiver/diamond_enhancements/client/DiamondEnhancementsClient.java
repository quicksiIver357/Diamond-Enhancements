package quicksiiver.diamond_enhancements.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import quicksiiver.diamond_enhancements.ModBlockEntities;
import quicksiiver.diamond_enhancements.rendering.ReinforcedPotRenderer;

public class DiamondEnhancementsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		// register the reinforced pot renderer
		BlockEntityRenderers.register(ModBlockEntities.REINFORCED_POT, ReinforcedPotRenderer::new);
	}
}
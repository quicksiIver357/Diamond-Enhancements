package quicksiiver.diamond_enhancements;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiamondEnhancements implements ModInitializer {
	public static final String MOD_ID = "diamond_enhancements";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		ModItems.initialize();
		ModBlocks.initialize();
		ModBlockEntities.initialize();

		// do stuff every server tick
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			// do stuff for each player here
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {

				// every ten ticks (1/2 second) (to save performance)
				if (server.getTickCount() % 10 == 0) {
					// give the player a effect if they have a diamond totem in their offhand / mainhand
					applyTotemEffect(player);
				}
			}
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	private static void applyTotemEffect(ServerPlayer player) {
		// check mainhand and offhand for the totem 
		ItemStack offhandItem = player.getOffhandItem();
		ItemStack mainhandItem = player.getMainHandItem();

		if (offhandItem.is(ModItems.HASTE_TOTEM) || mainhandItem.is(ModItems.HASTE_TOTEM)) {
			player.addEffect(new MobEffectInstance(MobEffects.HASTE, 25, 0, true, true, false));
		} 
		if (offhandItem.is(ModItems.JUMP_BOOST_TOTEM) || mainhandItem.is(ModItems.JUMP_BOOST_TOTEM)) {
			player.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 25, 0, true, true, false));
		} 
		if (offhandItem.is(ModItems.RESISTANCE_TOTEM) || mainhandItem.is(ModItems.RESISTANCE_TOTEM)) {
			player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 25, 0, true, true, false));
		} 
		if (offhandItem.is(ModItems.SPEED_TOTEM) || mainhandItem.is(ModItems.SPEED_TOTEM)) {
			player.addEffect(new MobEffectInstance(MobEffects.SPEED, 25, 0, true, true, false));
		} 
		if (offhandItem.is(ModItems.STRENGTH_TOTEM) || mainhandItem.is(ModItems.STRENGTH_TOTEM)) {
			player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 25, 0, true, true, false));
		}
	}
}

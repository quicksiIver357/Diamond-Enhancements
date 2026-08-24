package quicksiiver.diamond_enhancements;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import quicksiiver.diamond_enhancements.item.DiamondBundle;

public class ModItems {
    public static final Item DIAMOND_TOTEM = register(ModItemIds.DIAMOND_TOTEM, Item::new, new Item.Properties());
    public static final Item STARDUST = register(ModItemIds.STARDUST, Item::new, new Item.Properties());
    public static final Item DIAMOND_BUNDLE = register(ModItemIds.DIAMOND_BUNDLE, DiamondBundle::new, new Item.Properties().component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));


	public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
		// Create the item instance.
		Item item = itemFactory.apply(settings.setId(itemKey));

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}

    public static void initialize() {
        /*  Calling a method on a class statically initializes 
            it if it hasn't been previously loaded
            this means that all static fields are evaluated. 
            This is what this dummy initialize method is for. 
        */

        // Still, this method is useful for adding code to add items to creative menu and such.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> creativeTab.insertAfter(Items.TOTEM_OF_UNDYING, ModItems.DIAMOND_TOTEM));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.insertAfter(Items.NETHER_STAR, ModItems.STARDUST));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((creativeTab) -> creativeTab.insertAfter(Items.BUNDLE, ModItems.DIAMOND_BUNDLE));
    }
}
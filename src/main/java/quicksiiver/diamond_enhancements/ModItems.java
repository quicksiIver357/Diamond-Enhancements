package quicksiiver.diamond_enhancements;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import quicksiiver.diamond_enhancements.item.DiamondBundle;

public class ModItems {
    // food components
    public static final Consumable DIAMOND_APPLE_CONSUMABLE_COMPONENT = Consumables.defaultFood()
    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.ABSORPTION, 180 * 20, 2), 1.0f))
    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 15 * 20, 1), 1.0f))
    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60 * 20, 0), 1.0f))
    .build();

    // food properties (different somehow ig?)
    public static final FoodProperties DIAMOND_APPLE_FOOD_COMPONENT = new FoodProperties.Builder()
    .alwaysEdible()
    .nutrition(8)
    .saturationModifier(20)
    .build();

    // items registered
    public static final Item DIAMOND_TOTEM = register(ModItemIds.DIAMOND_TOTEM, Item::new, new Item.Properties());
    public static final Item STARDUST = register(ModItemIds.STARDUST, Item::new, new Item.Properties());
    public static final Item DIAMOND_BUNDLE = register(ModItemIds.DIAMOND_BUNDLE, DiamondBundle::new, new Item.Properties().component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Item DIAMOND_APPLE = register(ModItemIds.DIAMOND_APPLE, Item::new, new Item.Properties().food(DIAMOND_APPLE_FOOD_COMPONENT, DIAMOND_APPLE_CONSUMABLE_COMPONENT));

    // register function, used to create a new item
	public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
		// Create the item instance.
		Item item = itemFactory.apply(settings.setId(itemKey));

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}

    // initializes the class for reason below
    public static void initialize() {
        /*  Calling a method on a class statically initializes 
            it if it hasn't been previously loaded
            this means that all static fields are evaluated. 
            This is what this dummy initialize method is for. 
        */

        // Still, this method is useful for adding code to add items to creative menu and such.

        // creative menu code
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> creativeTab.insertAfter(Items.TOTEM_OF_UNDYING, ModItems.DIAMOND_TOTEM));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.insertAfter(Items.NETHER_STAR, ModItems.STARDUST));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((creativeTab) -> creativeTab.insertAfter(Items.BUNDLE, ModItems.DIAMOND_BUNDLE));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register((creativeTab) -> creativeTab.insertAfter(Items.GOLDEN_APPLE, ModItems.DIAMOND_APPLE));
    }
}
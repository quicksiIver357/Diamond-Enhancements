package quicksiiver.diamond_enhancements;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIds {
    public static final ResourceKey<Item> DIAMOND_TOTEM = create("diamond_totem");
    public static final ResourceKey<Item> HASTE_TOTEM = create("haste_totem");
    public static final ResourceKey<Item> SPEED_TOTEM = create("speed_totem");
    public static final ResourceKey<Item> JUMP_BOOST_TOTEM = create("jump_boost_totem");
    public static final ResourceKey<Item> RESISTANCE_TOTEM = create("resistance_totem");
    public static final ResourceKey<Item> STRENGTH_TOTEM = create("strength_totem");
    public static final ResourceKey<Item> STARDUST = create("stardust");
    public static final ResourceKey<Item> DIAMOND_BUNDLE = create("diamond_bundle");
    public static final ResourceKey<Item> DIAMOND_APPLE = create("diamond_apple");
    public static final ResourceKey<Item> DIAMOND_SHEET = create("diamond_sheet");


    public static ResourceKey<Item> create(String name) {
        // create the item key.
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DiamondEnhancements.MOD_ID, name));
    }
}

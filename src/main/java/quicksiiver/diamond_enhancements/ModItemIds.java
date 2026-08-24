package quicksiiver.diamond_enhancements;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIds {
    public static final ResourceKey<Item> DIAMOND_TOTEM = create("diamond_totem");
    public static final ResourceKey<Item> STARDUST = create("stardust");
    public static final ResourceKey<Item> DIAMOND_BUNDLE = create("diamond_bundle");


    public static ResourceKey<Item> create(String name) {
        // create the item key.
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DiamondEnhancements.MOD_ID, name));
    }
}

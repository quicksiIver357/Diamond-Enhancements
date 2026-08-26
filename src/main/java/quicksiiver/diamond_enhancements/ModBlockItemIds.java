package quicksiiver.diamond_enhancements;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public class ModBlockItemIds {
    // create the block ids
    public static final BlockItemId DIAMOND_PRESSURE_PLATE = create("diamond_pressure_plate");
    public static final BlockItemId REINFORCED_POT = create("reinforced_pot");

    // method to create block item ids
    private static BlockItemId create(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(DiamondEnhancements.MOD_ID, name);
        return BlockItemId.create(id, id);
    }
}

package quicksiiver.diamond_enhancements;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import quicksiiver.diamond_enhancements.block.ReinforcedPotBlockEntity;

public class ModBlockEntities {
    public static final BlockEntityType<ReinforcedPotBlockEntity> REINFORCED_POT = register("reinforced_pot", ReinforcedPotBlockEntity::new, ModBlocks.REINFORCED_POT);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(DiamondEnhancements.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void initialize() {
        // blank initialize method to be called and init the class in DiamondEnhancements.java
    }
}

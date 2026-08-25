package quicksiiver.diamond_enhancements;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import quicksiiver.diamond_enhancements.block.DiamondPressurePlate;

public class ModBlocks {
    // register the blocks
    // public static final Block DIAMOND_PRESSURE_PLATE = register(ModBlockItemIds.DIAMOND_PRESSURE_PLATE, Block::new, BlockBehaviour.Properties.of().sound(SoundType.METAL));
    public static final Block DIAMOND_PRESSURE_PLATE = register(ModBlockItemIds.DIAMOND_PRESSURE_PLATE, properties -> new DiamondPressurePlate(BlockSetType.IRON, properties), BlockBehaviour.Properties.of().sound(SoundType.METAL));

    // register blocks with items
	private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
		// Create the block instance
		Block block = register(id.block(), blockFactory, properties);

		// Create the block item instance
		BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
		Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);

		return block;
	}

    // overload for blocks without items
    // REQUIRED! Do not remove
    private static Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
		// Create the block instance
		Block block = blockFactory.apply(properties.setId(id));

		return Registry.register(BuiltInRegistries.BLOCK, id, block);
	}

    // init for loading the class in onInitialize in DiamondEnhancements.java
    public static void initialize() {
        // add things to the creative tabs
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((creativeTab) -> creativeTab.insertAfter(Items.HEAVY_WEIGHTED_PRESSURE_PLATE, ModBlocks.DIAMOND_PRESSURE_PLATE.asItem()));
    }
}
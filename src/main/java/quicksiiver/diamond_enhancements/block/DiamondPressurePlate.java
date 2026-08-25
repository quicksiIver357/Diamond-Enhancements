package quicksiiver.diamond_enhancements.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class DiamondPressurePlate extends PressurePlateBlock {
    // constructor (super)
    public DiamondPressurePlate(final BlockSetType type, final BlockBehaviour.Properties properties) {
        super(type, properties);
    }   

    @Override
    protected int getSignalStrength(final Level level, final BlockPos pos) {
		return getEntityCount(level, TOUCH_AABB.move(pos), Player.class) > 0 ? 15 : 0;
	}
}

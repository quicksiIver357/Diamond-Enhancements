package quicksiiver.diamond_enhancements.rendering;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.renderer.blockentity.state.DecoratedPotRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import quicksiiver.diamond_enhancements.DiamondEnhancements;
import quicksiiver.diamond_enhancements.block.ReinforcedPotBlockEntity;

public class ReinforcedPotRenderer implements BlockEntityRenderer<ReinforcedPotBlockEntity, DecoratedPotRenderState> {
    public final SpriteGetter sprites;

    private static final SpriteId REINFORCED_POT_BASE = Sheets.BLOCKS_MAPPER.apply(DiamondEnhancements.id("reinforced_pot_base"));
    private static final SpriteId REINFORCED_POT_SIDE = Sheets.BLOCKS_MAPPER.apply(DiamondEnhancements.id("reinforced_pot_side"));

	// private static final SpriteId REINFORCED_POT_BASE = Sheets.DECORATED_POT_BASE;
	// private static final SpriteId REINFORCED_POT_SIDE = Sheets.DECORATED_POT_SIDE;

    private final ModelPart neck;
	private final ModelPart frontSide;
	private final ModelPart backSide;
	private final ModelPart leftSide;
	private final ModelPart rightSide;
	private final ModelPart top;
	private final ModelPart bottom;

    public ReinforcedPotRenderer(final BlockEntityRendererProvider.Context context) {
		this(context.entityModelSet(), context.sprites());
	}

	public ReinforcedPotRenderer(final SpecialModelRenderer.BakingContext context) {
		this(context.entityModelSet(), context.sprites());
	}

	public ReinforcedPotRenderer(final EntityModelSet entityModelSet, final SpriteGetter sprites) {
		this.sprites = sprites;
		ModelPart baseRoot = entityModelSet.bakeLayer(ModelLayers.DECORATED_POT_BASE);
		this.neck = baseRoot.getChild("neck");
		this.top = baseRoot.getChild("top");
		this.bottom = baseRoot.getChild("bottom");
		ModelPart sidesRoot = entityModelSet.bakeLayer(ModelLayers.DECORATED_POT_SIDES);
		this.frontSide = sidesRoot.getChild("front");
		this.backSide = sidesRoot.getChild("back");
		this.leftSide = sidesRoot.getChild("left");
		this.rightSide = sidesRoot.getChild("right");
	}

    public DecoratedPotRenderState createRenderState() {
		return new DecoratedPotRenderState();
	}

    public void extractRenderState(final ReinforcedPotBlockEntity blockEntity, final DecoratedPotRenderState state, final float partialTicks, final Vec3 cameraPosition, final ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

		state.direction = Direction.NORTH;

		WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;
		if (wobbleStyle != null && blockEntity.getLevel() != null) {
			state.wobbleProgress = ((float)(blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTicks) / wobbleStyle.duration;
		} else {
			state.wobbleProgress = 0.0F;
		}
	}

    public void submit(final DecoratedPotRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.mulPose(DecoratedPotRenderer.modelTransformation(state.direction));
		if (state.wobbleProgress >= 0.0F && state.wobbleProgress <= 1.0F) {
			if (state.wobbleStyle == WobbleStyle.POSITIVE) {
				float amplitude = 0.015625F;
				float deltaTime = state.wobbleProgress * (float) (Math.PI * 2);
				float tiltX = -1.5F * (Mth.cos(deltaTime) + 0.5F) * Mth.sin(deltaTime / 2.0F);
				poseStack.rotateAround(Axis.XP.rotation(tiltX * 0.015625F), 0.5F, 0.0F, 0.5F);
				float tiltZ = Mth.sin(deltaTime);
				poseStack.rotateAround(Axis.ZP.rotation(tiltZ * 0.015625F), 0.5F, 0.0F, 0.5F);
			} else {
				float turnAngle = Mth.sin(-state.wobbleProgress * 3.0F * (float) Math.PI) * 0.125F;
				float linearDecayFactor = 1.0F - state.wobbleProgress;
				poseStack.rotateAround(Axis.YP.rotation(turnAngle * linearDecayFactor), 0.5F, 0.0F, 0.5F);
			}
		}

		this.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		poseStack.popPose();
	}

    public void submit(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final int overlayCoords, final int outlineColor) {
		RenderType renderType = Sheets.DECORATED_POT_BASE.renderType(RenderTypes::entitySolid);
		TextureAtlasSprite sprite = this.sprites.get(REINFORCED_POT_BASE);

        // draw the bottom, top, and neck
		submitNodeCollector.submitModelPart(this.neck, poseStack, renderType, lightCoords, overlayCoords, sprite, -1, null, outlineColor);
		submitNodeCollector.submitModelPart(this.top, poseStack, renderType, lightCoords, overlayCoords, sprite, -1, null, outlineColor);
		submitNodeCollector.submitModelPart(this.bottom, poseStack, renderType, lightCoords, overlayCoords, sprite, -1, null, outlineColor);

        // draw the sides
		submitNodeCollector.submitModelPart(this.frontSide, poseStack, REINFORCED_POT_SIDE.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(REINFORCED_POT_SIDE), -1, null, outlineColor);
		submitNodeCollector.submitModelPart(this.backSide, poseStack, REINFORCED_POT_SIDE.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(REINFORCED_POT_SIDE), -1, null, outlineColor);
		submitNodeCollector.submitModelPart(this.leftSide, poseStack, REINFORCED_POT_SIDE.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(REINFORCED_POT_SIDE), -1, null, outlineColor);
		submitNodeCollector.submitModelPart(this.rightSide, poseStack, REINFORCED_POT_SIDE.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(REINFORCED_POT_SIDE), -1, null, outlineColor);
	}
}

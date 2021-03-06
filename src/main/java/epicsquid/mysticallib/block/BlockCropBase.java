package epicsquid.mysticallib.block;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;

public class BlockCropBase extends BlockCrops implements IBlock, IModeledObject {

  private final @Nonnull EnumPlantType plantType;

  /**
   * Used for arbitrary crops
   * @param name Name of the crop
   */
  public BlockCropBase(@Nonnull String name, @Nonnull EnumPlantType plantType) {
    super();
    setUnlocalizedName(name);
    setRegistryName(LibRegistry.getActiveModid(), name);

    this.plantType = plantType;
  }

  /**
   * Controls the type of plant
   */
  @Override
  @Nonnull
  public EnumPlantType getPlantType(@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return plantType;
  }

  /**
   * Scales the bounding box of the crop with it's age
   */
  @Override
  @Nonnull
  public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos){
    return new AxisAlignedBB(0,0,0,1,0.125f*(state.getValue(this.AGE)+1),1);
  }

  @Override
  public void initModel(){
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName().toString(),"inventory"));
  }

  /**
   * Return null since we don't want an item for this block
   */
  @Override
  public Item getItemBlock() {
    return null;
  }
}

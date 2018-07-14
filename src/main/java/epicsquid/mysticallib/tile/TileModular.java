package epicsquid.mysticallib.tile;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.gui.GuiHandler;
import epicsquid.mysticallib.tile.module.FaceConfig;
import epicsquid.mysticallib.tile.module.IModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TileModular extends TileBase {

  public Map<String, IModule> modules = new HashMap<String, IModule>();
  public FaceConfig faceConfig = new FaceConfig();

  public TileModular(@Nonnull IModule... modules) {
    super();
    for (IModule m : modules) {
      this.modules.put(m.getModuleName(), m);
    }
  }

  /**
   * Used to dynamically add modules to the tile after creation.
   * @param module The module to append to the tile
   * @return This tile
   */
  public TileModular addModule(@Nonnull IModule module) {
    modules.put(module.getModuleName(), module);
    return this;
  }

  @Override
  @Nonnull
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag) {
    super.writeToNBT(tag);
    for (IModule m : modules.values()) {
      tag.setTag(m.getModuleName(), m.writeToNBT());
    }
    tag.setTag("faceConfig", faceConfig.writeToNBT());
    return tag;
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound tag) {
    super.readFromNBT(tag);
    for (IModule m : modules.values()) {
      if (tag.hasKey(m.getModuleName())) {
        m.readFromNBT(tag.getCompoundTag(m.getModuleName()));
      }
    }
    faceConfig.readFromNBT(tag.getCompoundTag("faceConfig"));
  }

  @Override
  public boolean hasCapability(@Nonnull Capability capability, @Nullable EnumFacing facing) {
    for (IModule m : modules.values()) {
      if (m.hasCapability(capability, facing)) {
        return true;
      }
    }
    return super.hasCapability(capability, facing);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    for (IModule m : modules.values()) {
      if (m.hasCapability(capability, facing)) {
        return (T) m.getCapability(capability, facing);
      }
    }
    return super.getCapability(capability, facing);
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand,
      @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    if (hasGui() && GuiHandler.hasGui(getTileName(getClass()))) {
      player.openGui(MysticalLib.INSTANCE, GuiHandler.getGuiID(getTileName(getClass())), world, getPos().getX(), getPos().getY(), getPos().getZ());
      return true;
    }
    return false;
  }

  @Override
  public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player) {
    for (IModule m : modules.values()) {
      m.onBroken(world, pos, player);
    }
    super.breakBlock(world, pos, state, player);
  }

  protected boolean hasGui() {
    return false;
  }

  @Nonnull
  public FaceConfig getFaceConfig() {
    return faceConfig;
  }
}

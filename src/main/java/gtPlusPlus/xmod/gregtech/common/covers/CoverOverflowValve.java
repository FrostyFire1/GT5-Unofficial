package gtPlusPlus.xmod.gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverBehaviorBase;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class CoverOverflowValve extends CoverBehaviorBase<CoverOverflowValve.OverflowValveData> {

    private final int minOverflowPoint = 0;
    private final int maxOverflowPoint;

    public CoverOverflowValve(CoverContext context, int maxOverflowPoint) {
        super(context, null);
        this.maxOverflowPoint = maxOverflowPoint;
        Object initializer = context.getCoverInitializer();
        if (initializer == null || initializer instanceof ItemStack) {
            // Re-initialization required until we merge covers and their data objects,
            // since this relies on an instance field.
            coverData = initializeData();
        }
    }

    public int getMinOverflowPoint() {
        return minOverflowPoint;
    }

    public int getMaxOverflowPoint() {
        return maxOverflowPoint;
    }

    public int getOverflowPoint() {
        return coverData.overflowPoint;
    }

    public CoverOverflowValve setOverflowPoint(int overflowPoint) {
        this.coverData.overflowPoint = overflowPoint;
        return this;
    }

    public int getVoidingRate() {
        return coverData.voidingRate;
    }

    public CoverOverflowValve setVoidingRate(int voidingRate) {
        this.coverData.voidingRate = voidingRate;
        return this;
    }

    public boolean canFluidInput() {
        return coverData.canFluidInput;
    }

    public CoverOverflowValve setCanFluidInput(boolean canFluidInput) {
        this.coverData.canFluidInput = canFluidInput;
        return this;
    }

    public boolean canFluidOutput() {
        return coverData.canFluidOutput;
    }

    public CoverOverflowValve setCanFluidOutput(boolean canFluidOutput) {
        this.coverData.canFluidOutput = canFluidOutput;
        return this;
    }

    @Override
    protected OverflowValveData initializeData() {
        return new CoverOverflowValve.OverflowValveData(maxOverflowPoint, maxOverflowPoint / 10, true, true);
    }

    private FluidStack doOverflowThing(FluidStack fluid, OverflowValveData coverData) {
        if (fluid != null && fluid.amount > coverData.overflowPoint)
            fluid.amount = Math.max(fluid.amount - coverData.voidingRate, coverData.overflowPoint);
        return fluid;
    }

    private void doOverflowThings(FluidStack[] fluids, OverflowValveData coverData) {
        for (FluidStack fluid : fluids) doOverflowThing(fluid, coverData);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        if (coverData.overflowPoint == 0 || coverData.voidingRate == 0) return;

        if (coveredTile.get() instanceof IGregTechTileEntity gregTE) {
            IMetaTileEntity tile = gregTE.getMetaTileEntity();
            if (tile instanceof MTEBasicTank fluidTank) {
                fluidTank.setDrainableStack(doOverflowThing(fluidTank.getDrainableStack(), coverData));
            } else if (tile instanceof MTEFluidPipe fluidPipe && fluidPipe.isConnectedAtSide(coverSide)) {
                doOverflowThings(fluidPipe.mFluids, coverData);
            }
        }
    }

    // Overrides methods

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 5;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return coverData.canFluidOutput;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return coverData.canFluidInput;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) {
            coverData.overflowPoint += (int) (maxOverflowPoint * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        } else {
            coverData.overflowPoint -= (int) (maxOverflowPoint * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        }

        if (coverData.overflowPoint > maxOverflowPoint) coverData.overflowPoint = minOverflowPoint;
        if (coverData.overflowPoint <= minOverflowPoint) coverData.overflowPoint = maxOverflowPoint;

        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocalFormatted(
                "GTPP.chat.text.cover_overflow_valve_overflow_point",
                coverData.overflowPoint));
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int amount = aPlayer.isSneaking() ? 128 : 8;
        if (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) {
            coverData.overflowPoint += amount;
        } else {
            coverData.overflowPoint -= amount;
        }

        if (coverData.overflowPoint > maxOverflowPoint) coverData.overflowPoint = minOverflowPoint;
        if (coverData.overflowPoint <= minOverflowPoint) coverData.overflowPoint = maxOverflowPoint;

        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocalFormatted(
                "GTPP.chat.text.cover_overflow_valve_overflow_point",
                coverData.overflowPoint));
        return true;
    }
    // GUI

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new OverflowUIFactory(buildContext).createWindow();
    }

    private static final class OverflowUIFactory extends UIFactory<CoverOverflowValve> {

        // width and height of text input for "Overflow Point" and "Voiding Rate"
        private static final int width = 71;
        private static final int height = 10;

        // fluid input buttons coordinates
        private static final int xFI = 43;
        private static final int yFI = 81;

        // fluid output buttons coordinates
        private static final int xFO = 6;
        private static final int yFO = 81;

        // Overflow Point 2x text + input coordinates
        private static final int xOP = 6;
        private static final int yOP = 27;

        // Voiding Rate 2x text + input coordinates
        private static final int xVR = 6;
        private static final int yVR = 53;

        public OverflowUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected CoverOverflowValve adaptCover(Cover cover) {
            if (cover instanceof CoverOverflowValve adapterCover) {
                return adapterCover;
            }
            return null;
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder
                .widget(
                    new TextWidget(StatCollector.translateToLocal("GTPP.gui.text.cover_overflow_valve_overflow_point"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(xOP, yOP))
                .widget(
                    new TextWidget(StatCollector.translateToLocal("GTPP.gui.text.cover_overflow_valve_liter"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(xOP + width + 3, yOP + 11))
                .widget(
                    new CoverDataControllerWidget<>(this::getCover, getUIBuildContext()).addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) coverData.getOverflowPoint(),
                        (coverData, state) -> coverData.setOverflowPoint(state.intValue()),
                        widget -> ifCoverValid(
                            c -> widget.setBounds(c.getMinOverflowPoint(), c.getMaxOverflowPoint())
                                .setScrollValues(1000, 144, 100000)
                                .setFocusOnGuiOpen(true)
                                .setPos(xOP, yOP + 10)
                                .setSize(width, height))))
                .widget(
                    new TextWidget(StatCollector.translateToLocal("GTPP.gui.text.cover_overflow_valve_voiding_rate"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(xVR + 6, yVR))
                .widget(
                    new TextWidget(StatCollector.translateToLocal("GTPP.gui.text.cover_overflow_valve_l_per_update"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(xVR + width + 3, yVR + 11))
                .widget(
                    new CoverDataControllerWidget<>(this::getCover, getUIBuildContext()).addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) coverData.getVoidingRate(),
                        (coverData, state) -> coverData.setVoidingRate(state.intValue()),
                        widget -> ifCoverValid(
                            c -> widget.setBounds(c.getMinOverflowPoint(), c.getMaxOverflowPoint())
                                .setScrollValues(1000, 144, 100000)
                                .setFocusOnGuiOpen(true)
                                .setPos(xVR, yVR + 10)
                                .setSize(width, height))))
                .widget(
                    new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                        this::getCover,
                        this::getClickable,
                        this::updateData,
                        getUIBuildContext())
                            .addToggleButton(
                                0,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ALLOW_INPUT)
                                    .addTooltip(
                                        StatCollector
                                            .translateToLocal("GTPP.gui.text.cover_overflow_valve_allow_fluid_input"))
                                    .setPos(xFI + 18, yFI))
                            .addToggleButton(
                                1,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT)
                                    .addTooltip(
                                        StatCollector
                                            .translateToLocal("GTPP.gui.text.cover_overflow_valve_block_fluid_input"))
                                    .setPos(xFI, yFI))
                            .addToggleButton(
                                2,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ALLOW_OUTPUT)
                                    .addTooltip(
                                        StatCollector
                                            .translateToLocal("GTPP.gui.text.cover_overflow_valve_allow_fluid_output"))
                                    .setPos(xFO, yFO))
                            .addToggleButton(
                                3,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_OUTPUT)
                                    .addTooltip(
                                        StatCollector
                                            .translateToLocal("GTPP.gui.text.cover_overflow_valve_block_fluid_output"))
                                    .setPos(xFO + 18, yFO)));
        }

        private boolean getClickable(int id, CoverOverflowValve coverData) {
            return switch (id) {
                case 0 -> coverData.canFluidInput();
                case 1 -> !coverData.canFluidInput();
                case 2 -> coverData.canFluidOutput();
                case 3 -> !coverData.canFluidOutput();
                default -> throw new IllegalStateException("Wrong button id: " + id);
            };
        }

        private CoverOverflowValve updateData(int id, CoverOverflowValve coverData) {
            return switch (id) {
                case 0 -> {
                    coverData.setCanFluidInput(true);
                    yield coverData;
                }
                case 1 -> {
                    coverData.setCanFluidInput(false);
                    yield coverData;
                }
                case 2 -> {
                    coverData.setCanFluidOutput(true);
                    yield coverData;
                }
                case 3 -> {
                    coverData.setCanFluidOutput(false);
                    yield coverData;
                }
                default -> throw new IllegalStateException("Wrong button id: " + id);
            };
        }
    }

    public static class OverflowValveData implements ISerializableObject {

        private int overflowPoint;
        private int voidingRate;
        private boolean canFluidInput;
        private boolean canFluidOutput;

        public OverflowValveData(int overflowPoint, int voidingRate, boolean canFluidInput, boolean canFluidOutput) {
            this.overflowPoint = overflowPoint;
            this.voidingRate = voidingRate;
            this.canFluidInput = canFluidInput;
            this.canFluidOutput = canFluidOutput;
        }

        @Override
        @NotNull
        public ISerializableObject copy() {
            return new OverflowValveData(overflowPoint, voidingRate, canFluidInput, canFluidOutput);
        }

        @Override
        @NotNull
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("overflowPoint", overflowPoint);
            tag.setInteger("voidingRate", voidingRate);
            tag.setBoolean("canFluidInput", canFluidInput);
            tag.setBoolean("canFluidOutput", canFluidOutput);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeInt(overflowPoint)
                .writeInt(voidingRate)
                .writeBoolean(canFluidInput)
                .writeBoolean(canFluidOutput);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (aNBT instanceof NBTTagCompound tag) {
                overflowPoint = tag.getInteger("overflowPoint");
                voidingRate = tag.getInteger("voidingRate");
                canFluidInput = tag.getBoolean("canFluidInput");
                canFluidOutput = tag.getBoolean("canFluidOutput");
            }
        }

        @Override
        public void readFromPacket(ByteArrayDataInput aBuf) {
            overflowPoint = aBuf.readInt();
            voidingRate = aBuf.readInt();
            canFluidInput = aBuf.readBoolean();
            canFluidOutput = aBuf.readBoolean();
        }
    }
}

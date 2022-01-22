package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.CONTAINER_1by1_Turbine;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.GUI_1by1_Turbine;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.turbine.LargeTurbineTextureHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GregtechMetaTileEntity_LargerTurbineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings("deprecation")
public class GT_MetaTileEntity_Hatch_Turbine extends GT_MetaTileEntity_Hatch {

	public boolean mHasController = false;
	public boolean mUsingAnimation = true;
	private String mControllerLocation;
	public int mEUt = 0;

	public GT_MetaTileEntity_Hatch_Turbine(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 16, "Turbine Rotor holder for XL Turbines");
	}

	public GT_MetaTileEntity_Hatch_Turbine(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 1, aDescription, aTextures);
	}

	public GT_MetaTileEntity_Hatch_Turbine(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 1, aDescription[0], aTextures);
	}
	
	@Override
	public String[] getDescription() {
		return new String[]{
				this.mDescription,
				"Right Click with a soldering iron to reset controller link",
				"Right Click with a wrench to remove turbine",
				"Right Click with a screwdriver for technical information",
				"Sneak + Right Click with a wrench to rotate",
				"Sneak + Right Click with a screwdriver to disable animations",
                CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexturesActive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, getFrontFacingTurbineTexture()};
	}

	@Override
	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, getFrontFacingTurbineTexture()};
	}

	public int getEU() {
		return this.mEUt;
	}

	public void setEU(int aEU) {
		this.mEUt = aEU;
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean isValidSlot(int aIndex) {
		return false;
	}

	public boolean hasTurbine() {
		ItemStack aStack = this.mInventory[0];
		boolean aIsValid = GregtechMetaTileEntity_LargerTurbineBase.isValidTurbine(aStack);
		return aIsValid;
	}

	public ItemStack getTurbine() {
		if (hasTurbine()) {
			return this.mInventory[0];
		}
		return null;
	}

	public boolean canWork() {
		return hasTurbine();
	}

	public boolean insertTurbine(ItemStack aTurbine) {
		if (GregtechMetaTileEntity_LargerTurbineBase.isValidTurbine(aTurbine)) {
			this.mInventory[0] = aTurbine;
			return true;
		}
		return false;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_Turbine(mName, mTier, StaticFields59.getDescriptionArray(this), mTextures);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		else {			
			aBaseMetaTileEntity.openGUI(aPlayer);	
		}
		return true;
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_1by1_Turbine(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_1by1_Turbine(aPlayerInventory, aBaseMetaTileEntity, "Turbine Rotor Hatch");
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	
	public void damageTurbine(int aEUt, int damageFactorLow, float damageFactorHigh) {
		if (hasTurbine() && MathUtils.randInt(0, 1) == 0) {
			ItemStack aTurbine = getTurbine();                  
			((GT_MetaGenerated_Tool) aTurbine.getItem()).doDamage(aTurbine, (long)getDamageToComponent(aTurbine) * (long) Math.min(aEUt / damageFactorLow, Math.pow(aEUt, damageFactorHigh)));
			if (aTurbine.stackSize == 0) {
				aTurbine = null;
			}
		}
	}
    
	private final int getDamageToComponent(ItemStack aStack) {
		return 1;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setBoolean("mHasController", mHasController);
		aNBT.setBoolean("mUsingAnimation", mUsingAnimation);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mHasController = aNBT.getBoolean("mHasController");
		mUsingAnimation = aNBT.getBoolean("mUsingAnimation");
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {		
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (this.mHasController) {		
			if (aTick % 20 == 0) {
				if (isControllerActive()) {
					this.getBaseMetaTileEntity().setActive(true);
				}
				else {
					this.getBaseMetaTileEntity().setActive(false);				
				}
			}	
		}
		else if (!this.mHasController && this.mControllerLocation != null) {
			//Weird Invalid State
			if (setController(BlockPos.generateBlockPos(mControllerLocation))) {
				//Valid
			}
		}
		else {
			//No Controller
		}
	}

	public boolean isControllerActive() {
		GregtechMetaTileEntity_LargerTurbineBase x = getController();
		if (x != null) {
			//Logger.INFO("Checking Status of Controller. Running? "+(x.mEUt > 0));
			return x.mEUt > 0;
		}
		//Logger.INFO("Status of Controller failed, controller is null.");
		return false;
	}

	public GregtechMetaTileEntity_LargerTurbineBase getController() {
		if (this.mHasController && this.mControllerLocation != null && this.mControllerLocation.length() > 0) {
			BlockPos p = BlockPos.generateBlockPos(mControllerLocation);
			if (p != null) {
				//Logger.INFO(p.getLocationString());
				IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntity(p.xPos, p.yPos,
						p.zPos);
				if (tTileEntity != null && tTileEntity.getMetaTileEntity() instanceof GregtechMetaTileEntity_LargerTurbineBase) {
					return (GregtechMetaTileEntity_LargerTurbineBase) tTileEntity.getMetaTileEntity();
				}
				else {
					if (tTileEntity == null) {
						Logger.INFO("Controller MTE is null, somehow?");
					}
					else {
						Logger.INFO("Controller is a different MTE to expected");						
					}				
				}
			}
		}
		//Logger.INFO("Failed to Get Controller.");
		return null;
	}

	public boolean canSetNewController() {		
		if ((mControllerLocation != null && mControllerLocation.length() > 0) || this.mHasController) {
			return false;
		}		
		return true;
	}

	public boolean setController(BlockPos aPos) {	
		clearController();	
		if (canSetNewController()) {
			mControllerLocation = aPos.getUniqueIdentifier();
			mHasController = true;	
			Logger.INFO("Successfully injected controller into this Turbine Assembly Hatch.");
		}
		return mHasController;
	}

	public void clearController() {
		this.mControllerLocation = null;
		this.mHasController = false;
	}

	public boolean usingAnimations() {
		return mUsingAnimation;
	}

	private ITexture getFrontFacingTurbineTexture() {
		if (!mHasController) {						
			return this.getBaseMetaTileEntity().isActive() ? new GT_RenderedTexture(LargeTurbineTextureHandler.OVERLAY_LP_TURBINE_ACTIVE[4] ) : new GT_RenderedTexture(LargeTurbineTextureHandler.OVERLAY_LP_TURBINE[4] );
		}
		else {
			if (usingAnimations()) {
				if (isControllerActive()) {
					return getController().frontFaceActive;
				}
			}
			return getController().frontFace;
		}		
	}	

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int aSide) {
		return new int[] {};
	}

	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		return false;
	}

	public void setActive(boolean b) {
		this.getBaseMetaTileEntity().setActive(b);
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
		return false;
	}
	
	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {	
		if (!aPlayer.isSneaking()) {
			PlayerUtils.messagePlayer(aPlayer, "Using Animations? "+usingAnimations());
			PlayerUtils.messagePlayer(aPlayer, "Has Controller? "+this.mHasController);
			if (mHasController) {
				PlayerUtils.messagePlayer(aPlayer, "Controller Location: "+BlockPos.generateBlockPos(mControllerLocation).getLocationString());
				PlayerUtils.messagePlayer(aPlayer, "Controller Active? "+this.isControllerActive());
			}
			PlayerUtils.messagePlayer(aPlayer, "Active? "+this.getBaseMetaTileEntity().isActive());
			PlayerUtils.messagePlayer(aPlayer, "Has Turbine inserted? "+this.hasTurbine());
			if (this.hasTurbine()) {
				Materials aMat = GT_MetaGenerated_Tool.getPrimaryMaterial(getTurbine());
				String aSize = GregtechMetaTileEntity_LargerTurbineBase.getTurbineSizeString(GregtechMetaTileEntity_LargerTurbineBase.getTurbineSize(getTurbine()));
				PlayerUtils.messagePlayer(aPlayer, "Using: "+aMat.mLocalizedName+" "+aSize);				
			}
		}
		else {
			this.mUsingAnimation = Utils.invertBoolean(mUsingAnimation);
			if (this.mUsingAnimation) {
				PlayerUtils.messagePlayer(aPlayer, "Using Animated Turbine Texture.");
			}
			else {
				PlayerUtils.messagePlayer(aPlayer, "Using Static Turbine Texture.");			
			}
		}		
	}

	@Override
	public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (this.getBaseMetaTileEntity().isServerSide() && !aPlayer.isSneaking()) {
			ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
			if (tCurrentItem != null) {	
				if (tCurrentItem.getItem() instanceof GT_MetaGenerated_Tool) {
					return onToolClick(tCurrentItem, aPlayer, aWrenchingSide);		
				}
			}
		}	
		return super.onWrenchRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public boolean onSolderingToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {		
		if (this.getBaseMetaTileEntity().isServerSide()) {
			ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
			if (tCurrentItem != null) {	
				if (tCurrentItem.getItem() instanceof GT_MetaGenerated_Tool) {
					return onToolClick(tCurrentItem, aPlayer, aWrenchingSide);		
				}
			}
		}		
		return false;
	}

	public boolean onToolClick(ItemStack tCurrentItem, EntityPlayer aPlayer, byte aSide) {
		if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWrenchList)) {	
			boolean aHasTurbine = this.hasTurbine();
			if (aPlayer.inventory.getFirstEmptyStack() >= 0 && aHasTurbine) {
				if (PlayerUtils.isCreative(aPlayer) || GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
					aPlayer.inventory.addItemStackToInventory((this.getTurbine()));
					this.mInventory[0] = null;
					GT_Utility.sendChatToPlayer(aPlayer, "Removed turbine with wrench.");
					return true;
				}
			}
			else {
				GT_Utility.sendChatToPlayer(aPlayer, aHasTurbine ? "Cannot remove turbine, no free inventory space." : "No turbine to remove.");
			}
		}
		else if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSolderingToolList)) {			
			if (mControllerLocation != null && mControllerLocation.length() > 0) {
				if (setController(BlockPos.generateBlockPos(mControllerLocation))) {
					if (PlayerUtils.isCreative(aPlayer) || GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
						String tChat = "Trying to Reset linked Controller";
						IGregTechTileEntity g = this.getBaseMetaTileEntity();
						GT_Utility.sendChatToPlayer(aPlayer, tChat);
						GT_Utility.sendSoundToPlayers(g.getWorld(), GregTech_API.sSoundList.get(101), 1.0F, -1,	g.getXCoord(), g.getYCoord(), g.getZCoord());
						return true;
					}
				}
			}
		}
		return false;
	}

}

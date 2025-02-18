package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.util.GTModHandler.getIC2Item;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

public class ExtractorRecipes implements Runnable {

    @Override
    public void run() {
        addExtractionRecipe(new ItemStack(Blocks.bookshelf, 1, WILDCARD), new ItemStack(Items.book, 3, 0));
        addExtractionRecipe(
            new ItemStack(Items.slime_ball, 1),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 2L));
        addExtractionRecipe(
            ItemList.IC2_Resin.get(1L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L));
        addExtractionRecipe(
            getIC2Item("rubberSapling", 1L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        addExtractionRecipe(
            getIC2Item("rubberLeaves", 16L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));

        addExtractionRecipe(ItemList.Cell_Air.get(1L), ItemList.Cell_Empty.get(1L));
        addExtractionRecipe(ItemList.IC2_Food_Can_Filled.get(1L), ItemList.IC2_Food_Can_Empty.get(1L));
        addExtractionRecipe(new ItemStack(Blocks.clay, 1), new ItemStack(Items.clay_ball, 4));
        addExtractionRecipe(new ItemStack(Blocks.brick_block, 1), new ItemStack(Items.brick, 4));
        addExtractionRecipe(new ItemStack(Blocks.nether_brick, 1), new ItemStack(Items.netherbrick, 4));
        addExtractionRecipe(new ItemStack(Blocks.snow, 1), new ItemStack(Items.snowball, 4));
        addExtractionRecipe(ItemList.Cell_Water.get(1), getIC2Item("hydratingCell", 1));

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_LV_SulfuricAcid.get(1L))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_LV_Mercury.get(1L))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_MV_SulfuricAcid.get(1L))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_MV_Mercury.get(1L))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_HV_SulfuricAcid.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_SU_HV_Mercury.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_LV_Cadmium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_LV_Lithium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_LV_Sodium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_MV_Cadmium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_MV_Lithium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_MV_Sodium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_HV_Cadmium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_HV_Lithium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Battery_RE_HV_Sodium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_EV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_EV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_IV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_IV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_LuV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_LuV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_ZPM_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_ZPM.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UHV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UHV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UEV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UEV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UIV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UIV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UMV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UMV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.BatteryHull_UxV_Full.get(1L))
            .itemOutputs(ItemList.BatteryHull_UxV.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
    }

    public void addExtractionRecipe(ItemStack input, ItemStack output) {
        output = GTOreDictUnificator.get(true, output);
        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(output)
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.impl.paper.used.p2025ICDEPtolemaiosLimited;

import vm.colour.StandardColours;
import vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.random.PlotPtolemaiosRandomData5Tech10_40;

/**
 *
 * @author au734419
 */
public class PlotPtolemaiosICDEDeCAF1M5Tech extends PlotPtolemaiosRandomData5Tech10_40 {

    public PlotPtolemaiosICDEDeCAF1M5Tech(boolean plotOnlySvg) {
        super(plotOnlySvg);
    }

    @Override
    public String[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return strings(
                "DeCAF_1M"
        );
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        return strings(
                "Triangle Ineq.",
                "Data-dep. Metric Filtering",
                "Four Point Prop.",
                "Ptolemaic Filtering",
                "Ptolemaic with Dyn. Pivots",
                "Data-dep. Ptolemaic Filering",
                "Sequential Brute Force"
        );
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "2025_06_128_pivots_30NN_triangle_inequality",
                "2025_06_128_pivots_30NN_data-dependent_metric_filtering",
                "2025_06_128_pivots_30NN_FourPointBasedFiltering",
                "2025_06_128_pivots_30NN_ptolemaios_randomPivots_128LB_random_pivots",
                "2025_06_128_pivots_30NN_ptolemaios_128LB",
                "2025_06_128_pivots_30NN_data-dependent_ptolemaic_filtering_128LB",
                "ground_truth"
        );
    }

    @Override
    protected StandardColours.COLOUR_NAME[] getVoluntaryColoursForTracesOrNull() {
        return new StandardColours.COLOUR_NAME[]{
            StandardColours.COLOUR_NAME.C1_BLUE,
            StandardColours.COLOUR_NAME.C2_RED,
            StandardColours.COLOUR_NAME.C3_GREEN,
            StandardColours.COLOUR_NAME.C4_ORANGE,
            StandardColours.COLOUR_NAME.C6_BROWN,
            StandardColours.COLOUR_NAME.C5_VIOLET,
            StandardColours.COLOUR_NAME.CX_BLACK};
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "decaf_1m__decaf_1m__30__decaf_1m__decaf_1m"
        );
    }

    @Override
    public String getXAxisLabel() {
        return "";
    }

}

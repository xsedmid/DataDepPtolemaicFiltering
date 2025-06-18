/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.impl.paper.used.p2024VLDBPtolemaiosLimited;

import vm.colour.StandardColours;
import vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.random.PlotPtolemaiosRandomData5Tech10_40;

/**
 *
 * @author au734419
 */
public class PlotPtolemaiosDeCAF1M5Tech extends PlotPtolemaiosRandomData5Tech10_40 {

    public PlotPtolemaiosDeCAF1M5Tech(boolean plotOnlySvg) {
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
                "2024_03_256_pivots_30NN_seq_triangle_inequality",
                "2024_03_256_pivots_30NN_seq_data-dependent_metric_filtering",
                "2024_03_256_pivots_30NN_seq_FourPointBasedFiltering",
                "2024_08_256_pivots_30NN_seq_ptolemaios_256LB_random_pivots",
                "2024_03_256_pivots_30NN_seq_ptolemaios",
                "2024_03_256_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection",
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

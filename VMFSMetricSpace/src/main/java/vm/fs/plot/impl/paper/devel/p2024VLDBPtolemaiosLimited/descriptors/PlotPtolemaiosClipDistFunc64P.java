/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.descriptors;

import vm.colour.StandardColours;
import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotXCategoriesPlotter;

/**
 *
 * @author Vlada
 */
public class PlotPtolemaiosClipDistFunc64P extends FSAbstractPlotterFromResults {

    public PlotPtolemaiosClipDistFunc64P(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        return strings(
                "Triangle Ineq.",
                "Data-dep. Metric Filtering",
                "Four Point Property",
                "Ptolemaic Filtering",
                "Data-dep. Ptolemaic Filering",
                "Sequential Brute Force"
        );
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "2024_05_64_pivots_30NN_seq_triangle_inequality",
                "2024_05_64_pivots_30NN_seq_data-dependent_metric_filtering",
                "2024_05_64_pivots_30NN_seq_FourPointBasedFiltering",
                "2024_05_64_pivots_30NN_seq_ptolemaios_64LB",
                "2024_05_64_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_64LB",
                "ground_truth"
        );
    }

    @Override
    public String[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return strings(
                "Dot product",
                "Euclidean",
                "Angular"
        );
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "laion2B-en-clip768v2-n=10M.h5DotPro__public-queries-10k-clip768v2.h5__30",
                "laion2B-en-clip768v2-n=10M.h5euclid__public-queries-10k-clip768v2.h5__30",
                "laion2B-en-clip768v2-n=10M.h5Angular__public-queries-10k-clip768v2.h5__30"
        );
    }

    @Override
    protected Float transformAdditionalStatsForQueryToFloat(float firstValue) {
        return firstValue / 10120191;
    }

    @Override
    public String getXAxisLabel() {
        return null;
    }

    @Override
    public AbstractPlotter getPlotter() {
        return new BoxPlotXCategoriesPlotter();
    }

    @Override
    public String getResultName() {
        return "Filterings_Clip_DF";
    }

    @Override
    public String getFolderForPlots() {
        return FSPlotFolders.Y2024_PTOLEMAIOS_LIMITED_DF;
    }

    @Override
    protected String getYAxisNameForAdditionalParams() {
        return "# LBs checked per distance";
    }

    @Override
    protected StandardColours.COLOUR_NAME[] getVoluntaryColoursForTracesOrNull() {
        return new StandardColours.COLOUR_NAME[]{
            StandardColours.COLOUR_NAME.C1_BLUE,
            StandardColours.COLOUR_NAME.C2_RED,
            StandardColours.COLOUR_NAME.C3_GREEN,
            StandardColours.COLOUR_NAME.C4_ORANGE,
            StandardColours.COLOUR_NAME.C5_VIOLET,
            StandardColours.COLOUR_NAME.CX_BLACK
        };
    }

}

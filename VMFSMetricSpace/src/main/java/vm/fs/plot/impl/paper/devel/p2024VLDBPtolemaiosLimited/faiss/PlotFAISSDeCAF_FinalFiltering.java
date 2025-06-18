package vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.faiss;

import vm.colour.StandardColours;
import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotXCategoriesPlotter;
import vm.search.algorithm.SearchingAlgorithm;

/**
 *
 * @author Vlada
 */
public class PlotFAISSDeCAF_FinalFiltering extends FSAbstractPlotterFromResults {

    public static final Integer PIVOTS = 64;
    public static final Integer LB = PIVOTS;

    public PlotFAISSDeCAF_FinalFiltering(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return strings(
                "DeCAF_100M"
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
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_triangle_inequality",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_data-dependent_metric_filtering",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_FourPointBasedFiltering",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_ptolemaios_" + LB + "LB_random_pivots",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_ptolemaios_" + LB + "LB",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_" + LB + "LB",
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
                "Faiss_DeCAF_100M_Candidates__decaf_100m__30__Faiss_DeCAF_100M_Candidates__decaf_100m__"
        );
    }

    @Override
    public String getXAxisLabel() {
        return "";
    }

    @Override
    public AbstractPlotter getPlotter() {
        return new BoxPlotXCategoriesPlotter();
    }

    @Override
    public String getResultName() {
        return "Filterings_DeCAF";
    }

    @Override
    public String getFolderForPlots() {
        return FSPlotFolders.Y2024_PTOLEMAIOS_LIMITED_FILTERING;
    }

    @Override
    protected String getYAxisNameForAdditionalParams() {
        return "# LBs checked per distance";
    }

    @Override
    protected Float transformAdditionalStatsForQueryToFloat(float firstValue) {
        return firstValue / (100000 - SearchingAlgorithm.K_IMPLICIT_FOR_QUERIES);
    }

}

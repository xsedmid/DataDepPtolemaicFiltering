package vm.fs.plot.implForPapers.p2024PtolemaiosLimited.faiss;

import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotPlotter;

/**
 *
 * @author Vlada
 */
public class PlotFAISSDeCAF_PCA256_FinalFiltering extends FSAbstractPlotterFromResults {

    public static final Integer PIVOTS = 32;
    public static final Integer LB = PIVOTS;

    public PlotFAISSDeCAF_PCA256_FinalFiltering(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return strings(
                "DeCAF_PCA256"
        );
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
                "2024_06_" + PIVOTS + "_pivots_30NN_seq_triangle_inequality",
                "2024_06_" + PIVOTS + "_pivots_30NN_seq_data-dependent_metric_filtering",
                "2024_06_" + PIVOTS + "_pivots_30NN_seq_FourPointBasedFiltering",
                "2024_06_" + PIVOTS + "_pivots_30NN_seq_ptolemaios_" + LB + "LB",
                "2024_06_" + PIVOTS + "_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_" + LB + "LB",
                "ground_truth"
        );
    }

    @Override
    protected AbstractPlotter.COLOUR_NAMES[] getVoluntaryColoursForTracesOrNull() {
        return new AbstractPlotter.COLOUR_NAMES[]{
            AbstractPlotter.COLOUR_NAMES.C1_BLUE,
            AbstractPlotter.COLOUR_NAMES.C2_RED,
            AbstractPlotter.COLOUR_NAMES.C3_GREEN,
            AbstractPlotter.COLOUR_NAMES.C4_ORANGE,
            AbstractPlotter.COLOUR_NAMES.C5_VIOLET,
            AbstractPlotter.COLOUR_NAMES.CX_BLACK};
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "Faiss_DeCAF_100M_PCA256_Candidates__decaf_100m_PCA256__30__Faiss_DeCAF_100M_PCA256_Candidates__decaf_100m_PCA256__"
        );
    }

    @Override
    public String getXAxisLabel() {
        return "";
    }

    @Override
    public AbstractPlotter getPlotter() {
        return new BoxPlotPlotter();
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
        return firstValue / 10000;
    }

}

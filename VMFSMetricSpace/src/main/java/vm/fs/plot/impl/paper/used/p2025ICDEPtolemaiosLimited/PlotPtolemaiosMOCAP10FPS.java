package vm.fs.plot.impl.paper.used.p2025ICDEPtolemaiosLimited;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import org.jfree.chart.JFreeChart;
import vm.colour.StandardColours;
import vm.fs.plot.FSAbstractPlotterFromResults;
import static vm.fs.plot.FSAbstractPlotterFromResults.strings;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotXCategoriesPlotter;

/**
 *
 * @author xmic
 */
public class PlotPtolemaiosMOCAP10FPS extends FSAbstractPlotterFromResults {

    protected static int pivotCount;
    protected static String month = "06";

    public static final int WIDTH = 535;
    public static final int HEIGHT = 470;

    public PlotPtolemaiosMOCAP10FPS(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    protected void storePDF(AbstractPlotter plotter, String path, JFreeChart plot) {
        plotter.storePlotPDF(path, plot, WIDTH, HEIGHT);
    }

    @Override
    protected void storePNG(AbstractPlotter plotter, String path, JFreeChart plot) {
        plotter.storePlotPNG(path, plot, WIDTH, HEIGHT);
    }

    public static void setPivotCount(int pivotCount) {
        PlotPtolemaiosMOCAP10FPS.pivotCount = pivotCount;
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
                "2025_" + month + "_" + pivotCount + "_pivots_30NN_triangle_inequality",
                "2025_" + month + "_" + pivotCount + "_pivots_30NN_data-dependent_metric_filtering",
                "2025_" + month + "_" + pivotCount + "_pivots_30NN_FourPointBasedFiltering",
                "2025_" + month + "_" + pivotCount + "_pivots_30NN_ptolemaios_randomPivots_" + pivotCount + "LB_random_pivots",
                "2025_" + month + "_" + pivotCount + "_pivots_30NN_ptolemaios_" + pivotCount + "LB",
                "2025_" + month + "_" + pivotCount + "_pivots_30NN_data-dependent_ptolemaic_filtering_" + pivotCount + "LB",
                "ground_truth"
        );
    }

    @Override
    public String[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return null;
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "actions-single-subject-all-POS-fps10.data_selected.txt__actions-single-subject-all-POS-fps10.data_selected.txt__30__actions-single-subject-all-POS-fps10.data_selected.txt__actions-single-subject-all-POS-fps10.data_selected.txt__.csv"
        );
    }

    @Override
    protected Float transformAdditionalStatsForQueryToFloat(float firstValue) {
        return firstValue / 17311;
    }

    @Override
    public String getXAxisLabel() {
        return null;
    }

    @Override
    public AbstractPlotter getPlotter() {
        AbstractPlotter ret = new BoxPlotXCategoriesPlotter();
        ret.setShowLegend(false);
        return ret;
    }

    @Override
    public String getResultName() {
        return "Filterings " + pivotCount + "pivots_10FPS";
    }

    @Override
    public String getFolderForPlots() {
        return FSPlotFolders.Y2025_PTOLEMAIOS_LIMITED10FPS;
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
            StandardColours.COLOUR_NAME.C6_BROWN,
            StandardColours.COLOUR_NAME.C5_VIOLET,
            StandardColours.COLOUR_NAME.CX_BLACK};
    }

}

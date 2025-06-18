/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.impl.paper.used.p2025ICDEPtolemaiosLimited;

import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.impl.paper.devel.p2025ICDEPtolemaiosLimited.PlotPtolemaiosClips30MPCA5Tech;
import static vm.fs.plot.impl.paper.used.p2024VLDBPtolemaiosLimited.FSPtolemaiosPlottingMain.PLOT_ONLY_PDF;

/**
 *
 * @author xmic
 */
public class FSICDEPtolemaiosPlottingMain {

    public static final FSAbstractPlotterFromResults Y2025_PlotPtolemaiosTransformedClips5Tech = new PlotPtolemaiosClips30MPCA5Tech(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2025_PlotPtolemaiosDeCAF1M = new PlotPtolemaiosICDEDeCAF1M5Tech(PLOT_ONLY_PDF);

    public static void main(String[] args) {
        int[] pivots = new int[]{64};
        for (int pivot : pivots) {
            PlotPtolemaiosMOCAP10FPS.setPivotCount(pivot);
            new PlotPtolemaiosMOCAP10FPS(PLOT_ONLY_PDF).makePlots();
            new PlotPtolemaiosMOCAP30FPS(PLOT_ONLY_PDF).makePlots();
        }
//        Y2025_PlotPtolemaiosTransformedClips5Tech.makePlots();
//        Y2025_PlotPtolemaiosDeCAF1M.makePlots();

    }
}

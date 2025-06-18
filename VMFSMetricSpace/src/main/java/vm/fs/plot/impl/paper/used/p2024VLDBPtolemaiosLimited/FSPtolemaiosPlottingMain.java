/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.impl.paper.used.p2024VLDBPtolemaiosLimited;

import vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.descriptors.PlotPtolemaiosTransformedClipsAbblation;
import vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.random.PlotPtolemaiosRandomDataPivotSelectionForPtolemaios;
import vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.random.PlotPtolemaiosRandomData5Tech50_100;
import vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.random.PlotPtolemaiosRandomData5Tech40_100Recall;
import vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.random.PlotPtolemaiosRandom100Abblation;
import vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.random.PlotPtolemaiosRandomData5Tech10_40;
import vm.fs.plot.FSAbstractPlotterFromResults;

/**
 *
 * @author au734419
 */
public class FSPtolemaiosPlottingMain {

    public static final Boolean PLOT_ONLY_PDF = false;

    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosRandomData5Tech10_40 = new PlotPtolemaiosRandomData5Tech10_40(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosRandomData5Tech40_100Recall = new PlotPtolemaiosRandomData5Tech40_100Recall(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosRandomData5Tech50_100 = new PlotPtolemaiosRandomData5Tech50_100(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosTransformedClips5Tech = new PlotPtolemaiosClips10MPCA5Tech(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosDeCAF1M5Tech = new PlotPtolemaiosDeCAF1M5Tech(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosPivotSelection = new PlotPtolemaiosRandomDataPivotSelectionForPtolemaios(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosCLIP_DF = new PlotPtolemaiosTransformedClipsAbblation(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosRandom100D_AbblationStudy = new PlotPtolemaiosRandom100Abblation(PLOT_ONLY_PDF);

    public static void main(String[] args) {
//        Y2024_PlotPtolemaiosRandomData5Tech10_40.makePlots();
//        Y2024_PlotPtolemaiosRandomData5Tech50_100.makePlots();
//        Y2024_PlotPtolemaiosRandomData5Tech40_100Recall.makePlots();
//        Y2024_PlotPtolemaiosTransformedClips5Tech.makePlots();
        Y2024_PlotPtolemaiosDeCAF1M5Tech.makePlots();
//        
//        Y2024_PlotPtolemaiosPivotSelection.makePlots();
//        Y2024_PlotPtolemaiosCLIP_DF.makePlots();
//        Y2024_PlotPtolemaiosRandom100D_AbblationStudy.makePlots();
    }
}

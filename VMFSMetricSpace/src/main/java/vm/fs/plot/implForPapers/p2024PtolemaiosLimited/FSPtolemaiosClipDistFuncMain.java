/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.implForPapers.p2024PtolemaiosLimited;

import vm.fs.plot.FSAbstractPlotterFromResults;

/**
 *
 * @author au734419
 */
public class FSPtolemaiosClipDistFuncMain {

    public static final Boolean PLOT_ONLY_PDF = true;
    
    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosCLIP_AbblationStudy64P = new PlotPtolemaiosClipDistFunc64P(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosCLIP_AbblationStudy128P = new PlotPtolemaiosClipDistFunc128P(PLOT_ONLY_PDF);

    public static void main(String[] args) {
        Y2024_PlotPtolemaiosCLIP_AbblationStudy64P.makePlots();
        Y2024_PlotPtolemaiosCLIP_AbblationStudy128P.makePlots();
    }
}

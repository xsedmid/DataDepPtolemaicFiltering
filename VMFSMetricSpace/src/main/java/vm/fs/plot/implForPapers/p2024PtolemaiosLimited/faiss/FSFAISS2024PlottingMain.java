package vm.fs.plot.implForPapers.p2024PtolemaiosLimited.faiss;

import vm.fs.plot.FSAbstractPlotterFromResults;

/**
 *
 * @author Vlada
 */
public class FSFAISS2024PlottingMain {

    public static final Boolean PLOT_ONLY_PDF = true;

    public static final FSAbstractPlotterFromResults Y2024_PlotFaissCLIPIndexes = new PlotFAISSCLIPIndexes2024(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotFaissCLIPConfig = new PlotFAISSCLIPIndexConfig2024(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotFaissDeCAFConfig = new PlotFAISSDeCAFIndexConfig2024(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotFaissDeCAFSimulatedCandSetSizes = new PlotFAISSDeCAFSimulatedCandSetSizes2024(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotFaissCLIPSimulatedCandSetSizes = new PlotFAISSCLIPSimulatedCandSetSizes2024(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotFaiss_CLIP_PCA256_FinalFiltering = new PlotFAISSCLIP_PCA256_FinalFiltering(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotFaissDeCAF_PCA256_UnusedFiltering = new PlotFAISSDeCAF_PCA256_FinalFiltering(PLOT_ONLY_PDF);
    public static final FSAbstractPlotterFromResults Y2024_PlotFaissDeCAF_FinalFiltering = new PlotFAISSDeCAF_FinalFiltering(PLOT_ONLY_PDF);

    public static void main(String[] args) {
//        Y2024_PlotFaissCLIPIndexes.makePlots();
//        Y2024_PlotFaissCLIPConfig.makePlots();
//        Y2024_PlotFaissDeCAFConfig.makePlots();
//        Y2024_PlotFaissCLIPSimulatedCandSetSizes.makePlots();
//        Y2024_PlotFaissDeCAFSimulatedCandSetSizes.makePlots();
//        Y2024_PlotFaissDeCAF_PCA256_FinalFiltering.makePlots();
        Y2024_PlotFaiss_CLIP_PCA256_FinalFiltering.makePlots();
//        Y2024_PlotFaissDeCAF_FinalFiltering.makePlots();
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.main.datatools;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import vm.fs.FSGlobal;
import vm.fs.dataset.FSDatasetInstances;
import vm.fs.main.datatools.storage.VMMVStorageInsertMain;
import vm.fs.main.precomputeDistances.FSEvalAndStoreObjectsToPivotsDistsMain;
import vm.fs.main.precomputeDistances.FSEvalAndStoreSampleOfSmallestDistsMain;
import vm.fs.main.search.filtering.learning.FSLearnCoefsForDataDepenentMetricFilteringMain;
import vm.fs.main.search.filtering.learning.FSLearnCoefsForDataDependentPtolemyFilteringMain;
import vm.metricSpace.Dataset;
import vm.metricSpace.DatasetOfCandidates;
import vm.search.algorithm.impl.GroundTruthEvaluator;

/**
 * No not run this in paralel for more datasets It is already paralelised in
 * many ways so your PC would stuck.
 *
 * @author au734419
 */
public class FSPrepareNewDatasetForPivotFilterings {

    private static Boolean skipEverythingEvaluated = true;

    public static void setSkipEverythingEvaluated(Boolean skipEverythingEvaluated) {
        FSPrepareNewDatasetForPivotFilterings.skipEverythingEvaluated = skipEverythingEvaluated;
    }
    public static final Integer MIN_NUMBER_OF_OBJECTS_TO_CREATE_KEY_VALUE_STORAGE = 10 * 1000 * 1000; // decide by yourself, smaller datasets can be kept as a map in the main memory only, and creation of the map is efficient. This is implemented, e.g., in FSFloatVectorDataset and FSHammingSpaceDataset in class FSDatasetInstanceSingularizator
    public static final Logger LOG = Logger.getLogger(FSPrepareNewDatasetForPivotFilterings.class.getName());

    public static void main(String[] args) throws FileNotFoundException {
        boolean publicQueries = true;
        Dataset[] datasets = {
//            new FSDatasetInstances.LAION_30M_PCA256Dataset(),
//            new FSDatasetInstances.LAION_100M_PCA256Dataset()
//            new FSDatasetInstances.MOCAP10FPS(),
//            new FSDatasetInstances.MOCAP30FPS()
//            FSLayersKasperStorage.createDataset(FSLayersKasperStorage.TYPE_0_small, FSLayersKasperStorage.DIMENSION_0_small)
//            new FSDatasetInstances.DeCAF20M_PCA256Dataset(),
            new FSDatasetInstances.Yahoo100M_1MSubset_Dataset()
//            new FSDatasetInstanceSingularizator.DeCAF100M_Dataset(),
//            new FSDatasetInstanceSingularizator.DeCAF100M_PCA256Dataset(),
        //            new FSDatasetInstanceSingularizator.Faiss_Clip_100M_PCA256_Candidates(),
        //            new FSDatasetInstanceSingularizator.Faiss_DeCAF_100M_Candidates(),
        //            new FSDatasetInstanceSingularizator.FaissDyn_Clip_100M_PCA256_Candidates(300),
        //            new FSDatasetInstanceSingularizator.Faiss_DeCAF_100M_PCA256_Candidates(),
        //            new FSDatasetInstanceSingularizator.RandomDataset10Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset15Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset25Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset30Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset35Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset40Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset50Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset60Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset70Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset80Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset90Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset100Uniform()
        //            new FSDatasetInstanceSingularizator.MPEG7dataset(),
        //            new FSDatasetInstanceSingularizator.SIFTdataset(),
        //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(publicQueries),
        //            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(publicQueries),
        //            new FSDatasetInstanceSingularizator.LAION_100M_PCA256Dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Euclid(publicQueries),
        //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset_Euclid(publicQueries)
        //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Dot(true)
        //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Euclid(true)
        //                    new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Angular(true)
        };
        for (Dataset dataset : datasets) {
            run(dataset);
        }

//        int dim = 8;
//        while (dim < 1500) {
//            for (int type = 0; type < 4; type++) {
//                Dataset<float[]> dataset = FSMetricSpaceKasperStorage.createDataset(type, dim);
//                if (dataset != null) {
//                    run(dataset);
//                }
//            }
//            dim = dim * 2;
//        }
    }

    private static void run(Dataset dataset) throws FileNotFoundException {
        precomputeDatasetSize(dataset);
        Dataset origDataset = dataset;
        if (dataset instanceof DatasetOfCandidates) {
            origDataset = ((DatasetOfCandidates) dataset).getOrigDataset();
            plotDistanceDensity(origDataset);
        } else {
            plotDistanceDensity(dataset);
        }
        selectRandomPivotsAndQueryObjects(origDataset);
        evaluateGroundTruth(dataset, GroundTruthEvaluator.K_IMPLICIT_FOR_GROUND_TRUTH);
        evaluateSampleOfSmallestDistances(dataset);
        precomputeObjectToPivotDists(origDataset);
        createKeyValueStorageForBigDataset(origDataset);
        learnDataDependentMetricFiltering(dataset);
        learnDataDependentPtolemaicFiltering(dataset);
        evaluateGroundTruth(dataset, GroundTruthEvaluator.K_IMPLICIT_FOR_QUERIES);
    }

    /**
     *
     * @param type
     * @param dataset
     * @return true iff the file CANNOT be overwritten
     */
    private static boolean askForRewriting(String type, Dataset dataset) {
        if (skipEverythingEvaluated) {
            return true;
        }
        if (!FSGlobal.askWhenGoingToOverrideFile) {
            return false;
        }
        try {
            String question = type + " for " + dataset.getDatasetName() + " already exists. Do you want to delete its content? (NEED TO BE CONFIRMED AGAIN AFTER EVALUATION)";
            Object[] options = new String[]{"Yes", "No"};
            LOG.log(Level.WARNING, "Asking for a question, waiting for the reply: {0} for {1}", new Object[]{type, dataset.getDatasetName()});
            int add = JOptionPane.showOptionDialog(null, question, "New file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, JOptionPane.NO_OPTION);
            return add == 1;
//            return add != 0;
        } catch (Throwable e) {
            LOG.log(Level.WARNING, "File exists and I cannot ask you for a permission to delete it. Skipping step.");
            return true;
        }
    }

    public static final int precomputeDatasetSize(Dataset dataset) {
        int precomputedDatasetSize = dataset.getPrecomputedDatasetSize();
        if (precomputedDatasetSize < 0) {
            LOG.log(Level.INFO, "Dataset {0} -- going to recompute number of stored objects", new Object[]{dataset.getDatasetName()});
            return dataset.updateDatasetSize();
        }
        LOG.log(Level.INFO, "Dataset {0} has the precomputed dataset size {1} objects", new Object[]{dataset.getDatasetName(), precomputedDatasetSize});
        return precomputedDatasetSize;
    }

    public static final void plotDistanceDensity(Dataset dataset) {
        boolean prohibited = FSPrintAndPlotDDOfDatasetMain.existsForDataset(dataset);
        if (!prohibited) {
            LOG.log(Level.INFO, "Dataset: {0}, printing distance density plots", dataset.getDatasetName());
            FSPrintAndPlotDDOfDatasetMain.run(dataset);
        } else {
            LOG.log(Level.INFO, "Dataset: {0}, distance density plot already exists", dataset.getDatasetName());
        }
    }

    private static void selectRandomPivotsAndQueryObjects(Dataset dataset) {
        boolean existsPivots = dataset.getPivots(1) != null;
        String datasetName = dataset.getDatasetName();
        if (!existsPivots) {
            LOG.log(Level.INFO, "Dataset: {0}, trying to select pivots", datasetName);
            FSSelectRandomQueryObjectsAndPivotsFromDatasetMain.run(dataset);
        } else {
            LOG.log(Level.INFO, "Dataset: {0}, pivots already preselected", datasetName);
            List queryObjects = dataset.getQueryObjects(1);
            boolean existsQueries = queryObjects != null && !queryObjects.isEmpty();
            if (!existsQueries) {
                LOG.log(Level.INFO, "Dataset: {0}, trying to select queries", datasetName);
                FSSelectRandomQueryObjectsAndPivotsFromDatasetMain.run(dataset, FSSelectRandomQueryObjectsAndPivotsFromDatasetMain.IMPLICIT_NUMBER_OF_QUERIES, 0);
            }
        }
    }

    public static final void evaluateGroundTruth(Dataset dataset, int k) {
        boolean prohibited = FSEvaluateGroundTruthMain.existsForDataset(dataset, k);
        String datasetName = dataset.getDatasetName();
        if (prohibited) {
            LOG.log(Level.WARNING, "Ground already exists for dataset {0}", datasetName);
            prohibited = askForRewriting("Ground truth", dataset);
        }
        if (!prohibited) {
            LOG.log(Level.INFO, "Dataset: {0}, evaluating ground truth", datasetName);
            FSEvaluateGroundTruthMain.run(dataset, k);
        }
    }

    public static final void evaluateSampleOfSmallestDistances(Dataset dataset) {
        boolean prohibited = FSEvalAndStoreSampleOfSmallestDistsMain.existsForDataset(dataset);
        String datasetName = dataset.getDatasetName();
        if (prohibited) {
            LOG.log(Level.WARNING, "Smallest distances already evaluated for dataset {0}", datasetName);
            prohibited = askForRewriting("Smallest distance", dataset);
        }
        if (!prohibited) {
            LOG.log(Level.INFO, "Dataset: {0}, evaluating smallest distances", dataset);
            FSEvalAndStoreSampleOfSmallestDistsMain.run(dataset);
        }
    }

    public static final void precomputeObjectToPivotDists(Dataset dataset) {
        String datasetName = dataset.getDatasetName();
        boolean prohibited = FSEvalAndStoreObjectsToPivotsDistsMain.existsForDataset(dataset, dataset.getRecommendedNumberOfPivotsForFiltering());
        if (prohibited) {
            LOG.log(Level.WARNING, "Dists to pivots already evaluated for dataset {0}", datasetName);
            prohibited = askForRewriting("Dists to pivots", dataset);
        }
        if (!prohibited && dataset.shouldStoreDistsToPivots()) {
            LOG.log(Level.INFO, "Dataset: {0}, evaluating objects to pivot distances", datasetName);
            FSEvalAndStoreObjectsToPivotsDistsMain.run(dataset, dataset.getRecommendedNumberOfPivotsForFiltering());
        }
    }

    public static final void learnDataDependentMetricFiltering(Dataset dataset) {
        String datasetName = dataset.getDatasetName();
        boolean prohibited = FSLearnCoefsForDataDepenentMetricFilteringMain.existsForDataset(dataset);
        if (prohibited) {
            LOG.log(Level.WARNING, "Coefs for Data-dependent Metric Filtering already evaluated for dataset {0}", datasetName);
            prohibited = askForRewriting("Coefs for Data-dependent Metric Filtering", dataset);
        }
        if (!prohibited) {
            LOG.log(Level.INFO, "Dataset: {0}, learning data dependent metric filtering", datasetName);
            FSLearnCoefsForDataDepenentMetricFilteringMain.run(dataset);
        }
    }

    public static final void learnDataDependentPtolemaicFiltering(Dataset dataset) {
        String datasetName = dataset.getDatasetName();
        boolean prohibited = FSLearnCoefsForDataDependentPtolemyFilteringMain.existsForDataset(dataset);
        if (prohibited) {
            LOG.log(Level.WARNING, "Coefs for Data-dependent Generalised Ptolemaic Filtering already evaluated for dataset {0}", datasetName);
            prohibited = askForRewriting("Coefs for Data-dependent Generalised Ptolemaic Filtering", dataset);
        }
        if (!prohibited) {
            LOG.log(Level.INFO, "Dataset: {0}, learning coefs for data dependent ptolemaic filtering", datasetName);
            FSLearnCoefsForDataDependentPtolemyFilteringMain.run(dataset);
        }
    }

    private static void createKeyValueStorageForBigDataset(Dataset dataset) {
        String datasetName = dataset.getDatasetName();
        if (dataset.shouldCreateKeyValueStorage()) {
            boolean prohibited = dataset.hasKeyValueStorage();
            if (prohibited) {
                LOG.log(Level.WARNING, "The key value storage already exists for dataset {0}", datasetName);
                prohibited = askForRewriting("The key value storage", dataset);
            }
            if (!prohibited) {
                dataset.deleteKeyValueStorage();
                LOG.log(Level.INFO, "Dataset: {0}, creating key-value storage", datasetName);
                VMMVStorageInsertMain.run(dataset);
            }
        } else {
            LOG.log(Level.INFO, "Dataset: {0} is too small to create the key-value storage", datasetName);
        }
    }
}

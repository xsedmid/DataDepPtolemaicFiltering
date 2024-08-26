package vm.fs.main.queryResults.recallEvaluation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.fs.FSGlobal;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.queryResults.FSNearestNeighboursStorageImpl;
import vm.fs.store.queryResults.FSQueryExecutionStatsStoreImpl;
import vm.fs.store.queryResults.recallEvaluation.FSRecallOfCandidateSetsStorageImpl;
import vm.metricSpace.Dataset;
import vm.queryResults.recallEvaluation.RecallOfCandsSetsEvaluator;

/**
 *
 * @author Vlada
 */
public class FSEvaluateRecallsOfApproximateDatasetMain {

//    public static final Integer[] kCands = new Integer[]{500, 750, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000}; // null if dynamic, otherwise fixed number
    public static final Integer[] kCands = new Integer[]{641}; // {null} if dynamic, otherwise fixed number
//    public static final Integer[] kCands = new Integer[]{71066}; // {null} if dynamic, otherwise fixed number

    public static void main(String[] args) throws InterruptedException {
        directFiles();
//        forDatasets(); // just if the results are in the ground truth folder
    }

    public static final void run(String folder, String groundTDatasetName, String groundTQuerySetName, String approxDatasetName, String approxQuerySetName) {
        int k = 30;
        for (Integer kCand : kCands) {
            evaluateRecallOfTheCandidateSet(groundTDatasetName, groundTQuerySetName, k, approxDatasetName, approxQuerySetName, folder, kCand);
        }
    }

    public static final void run(Dataset groundTruthDataset, Dataset... approximatedDatasets) {
        int k = 30;
        String resultName = "ground_truth";

        for (Dataset approximatedDataset : approximatedDatasets) {
            if (kCands == null) {
                evaluateRecallOfTheCandidateSet(groundTruthDataset.getDatasetName(), groundTruthDataset.getQuerySetName(), k,
                        approximatedDataset.getDatasetName(), approximatedDataset.getQuerySetName(), resultName, null);
            } else {
                for (Integer kCand : kCands) {
                    evaluateRecallOfTheCandidateSet(groundTruthDataset.getDatasetName(), groundTruthDataset.getQuerySetName(), k,
                            approximatedDataset.getDatasetName(), approximatedDataset.getQuerySetName(), resultName, kCand);
                }
            }
        }
    }

    public static final void evaluateRecallOfTheCandidateSet(String groundTruthDatasetName, String groundTruthQuerySetName, int groundTruthNNCount,
            String candSetName, String candSetQuerySetName, String resultSetName, Integer candidateNNCount) {

        FSNearestNeighboursStorageImpl groundTruthStorage = new FSNearestNeighboursStorageImpl();
        Map<FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME, String> attributesForFileName = new HashMap<>();
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.ground_truth_name, groundTruthDatasetName);
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.ground_truth_query_set_name, groundTruthQuerySetName);
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.ground_truth_nn_count, Integer.toString(groundTruthNNCount));
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.cand_set_name, candSetName);
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.cand_set_query_set_name, candSetQuerySetName);
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.storing_result_name, resultSetName);
        if (candidateNNCount != null) {
            attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.cand_set_fixed_size, candidateNNCount.toString());
        }
        try {
            FSRecallOfCandidateSetsStorageImpl recallStorage = new FSRecallOfCandidateSetsStorageImpl(attributesForFileName);
            RecallOfCandsSetsEvaluator evaluator = new RecallOfCandsSetsEvaluator(groundTruthStorage, recallStorage);
            evaluator.evaluateAndStoreRecallsOfQueries(groundTruthDatasetName, groundTruthQuerySetName, groundTruthNNCount, candSetName, candSetQuerySetName, resultSetName, candidateNNCount);
            recallStorage.save();
        } catch (Exception e) {
            Logger.getLogger(FSEvaluateRecallsOfApproximateDatasetMain.class.getName()).log(Level.WARNING, "File skipped", e);
        }
    }

    private static void directFiles() {
        String[] folderNames = {
            //            "faiss-100M_DeCAF-IVFPQ-tr1000000-cc262144-m32-nbits8-qc-1000-k100000"
//            "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k100000"
            "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k750"
//            "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc-1-k30"
        //                                    "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k100000"
        //            "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k100000",
        //                        "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k75000",
        //                        "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k50000"
        //                        "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m64-nbits16-qc1000-k10000"
        //            "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k50000",
        //            "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k75000",
        //            "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k100000",
        //            "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k200000"
        //            "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k10000"
        };

        for (String folderName : folderNames) {
            File folder = new File(FSGlobal.RESULT_FOLDER, folderName);
            String[] files = folder.list((File file, String string) -> string.toLowerCase().endsWith(".gz"));
            if (files == null) {
                Logger.getLogger(FSEvaluateRecallsOfApproximateDatasetMain.class.getName()).log(Level.INFO, "Wrong folder name{0}", folder.getAbsolutePath());
            }
            for (String fileName : files) {
                Logger.getLogger(FSEvaluateRecallsOfApproximateDatasetMain.class.getName()).log(Level.INFO, "Processing file {0}", fileName);
                fileName = fileName.trim().substring(0, fileName.length() - 3);
                run(folderName, "laion2B-en-clip768v2-n=100M.h5_PCA256", "laion2B-en-clip768v2-n=100M.h5_PCA256", fileName, "");
////                run(folderName, "decaf_100m_PCA256", "decaf_100m_PCA256", fileName, "");
//                run(folderName, "decaf_100m", "decaf_100m", fileName, "");
            }
        }
    }

    private static void forDatasets() {
        boolean publicQueries = true;
        Dataset groundTruthDataset = new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Euclid(publicQueries);
        Dataset[] approximatedDatasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset()
        };
        run(groundTruthDataset, approximatedDatasets);

//        groundTruthDataset = new FSDatasetInstanceSingularizator.DeCAF100M_PCA256Dataset();
//        approximatedDatasets = new Dataset[]{
//            new FSDatasetInstanceSingularizator.Faiss_DeCAF_100M_PCA256_Candidates()
//        };
//        run(groundTruthDataset, approximatedDatasets);
    }
}

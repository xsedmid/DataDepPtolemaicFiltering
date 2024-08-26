package vm.fs.store.queryResults.recallEvaluation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.datatools.DataTypeConvertor;
import vm.fs.store.queryResults.FSQueryExecutionStatsStoreImpl;
import vm.queryResults.errorOnDistEvaluation.ErrorsOnDistStoreInterface;
import vm.queryResults.recallEvaluation.RecallOfCandsSetsStoreInterface;

/**
 *
 * @author Vlada
 */
public class FSRecallOfCandidateSetsStorageImpl extends FSQueryExecutionStatsStoreImpl implements RecallOfCandsSetsStoreInterface, ErrorsOnDistStoreInterface {

    private final Logger LOG = Logger.getLogger(FSRecallOfCandidateSetsStorageImpl.class.getName());

    /**
     *
     * @param attributesForFileName: ground_truth_name,
     * ground_truth_query_set_name, ground_truth_nn_count, cand_set_name,
     * cand_set_query_set_name, storing_result_name. Voluntary:
     * cand_set_fixed_size
     */
    public FSRecallOfCandidateSetsStorageImpl(Map<FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME, String> attributesForFileName) {
        super(attributesForFileName);
    }

    public FSRecallOfCandidateSetsStorageImpl(File file) {
        super(file);
    }

    /**
     *
     * @param groundTruthDatasetName
     * @param groundTruthQuerySetName
     * @param groundTruthNNCount
     * @param candSetName
     * @param candSetQuerySetName
     * @param resultSetName
     * @param candidateNNCount
     */
    public FSRecallOfCandidateSetsStorageImpl(String groundTruthDatasetName, String groundTruthQuerySetName, int groundTruthNNCount, String candSetName, String candSetQuerySetName, String resultSetName, Integer candidateNNCount) {
        this(transformFileNameParamsToMap(groundTruthDatasetName, groundTruthQuerySetName, groundTruthNNCount, candSetName, candSetQuerySetName, resultSetName, candidateNNCount));
    }
    
    public static Map<String, TreeSet<Map.Entry<Object, Float>>> getGroundTruthForDataset(String datasetName, String querySetName) {
        return getGroundTruthForDataset(datasetName, querySetName);
    }

    public static final Map<FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME, String> transformFileNameParamsToMap(String groundTruthDatasetName, String groundTruthQuerySetName, int groundTruthNNCount, String candSetName, String candSetQuerySetName, String resultSetName, Integer candidateSetFixedSize) {
        Map<FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME, String> attributesForFileName = new HashMap<>();
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.ground_truth_name, groundTruthDatasetName);
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.ground_truth_query_set_name, groundTruthQuerySetName);
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.ground_truth_nn_count, Integer.toString(groundTruthNNCount));
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.cand_set_name, candSetName);
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.cand_set_query_set_name, candSetQuerySetName);
        attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.storing_result_name, resultSetName);
        if (candidateSetFixedSize != null) {
            attributesForFileName.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.cand_set_fixed_size, candidateSetFixedSize.toString());
        }
        return attributesForFileName;
    }
    
    @Override
    public void storeRecallForQuery(Object queryObjId, float recall, Object... additionalParametersToStore) {
        TreeMap<QUERY_STATS, String> line = content.get(queryObjId.toString());
        if (line == null) {
            LOG.log(Level.INFO, "Statistics not found for the query {0}", queryObjId.toString());
            line = new TreeMap<>();
            line.put(QUERY_STATS.query_obj_id, queryObjId.toString());
            content.put(queryObjId.toString(), line);
        }
        line.put(QUERY_STATS.recall, Float.toString(recall));
        String candidateNNCount = DataTypeConvertor.objectsToString(additionalParametersToStore, ",");
        if (line.containsKey(QUERY_STATS.additional_stats)) {
            String prefix = line.get(QUERY_STATS.additional_stats);
            candidateNNCount = prefix + ",," + candidateNNCount;
        }
        line.put(QUERY_STATS.additional_stats, candidateNNCount);
    }

    @Override
    public void storeErrorOnDistForQuery(Object queryObjId, float errorOnDist, Object... additionalParametersToStore) {
        TreeMap<QUERY_STATS, String> line = content.get(queryObjId.toString());
        if (line == null) {
            LOG.log(Level.INFO, "Statistics not found for the query {0}", queryObjId.toString());
            line = new TreeMap<>();
            line.put(QUERY_STATS.query_obj_id, queryObjId.toString());
            content.put(queryObjId.toString(), line);
        }
        line.put(QUERY_STATS.error_on_dist, Float.toString(errorOnDist));
        String additional = DataTypeConvertor.objectsToString(additionalParametersToStore, ",");
        if (line.containsKey(QUERY_STATS.additional_stats)) {
            String prefix = line.get(QUERY_STATS.additional_stats);
            additional = prefix + ",," + additional;
        }
        line.put(QUERY_STATS.additional_stats, additional);
    }

    @Override
    public void save() {
        super.save();
    }

}

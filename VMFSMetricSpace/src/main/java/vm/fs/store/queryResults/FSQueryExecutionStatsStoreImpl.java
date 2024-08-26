package vm.fs.store.queryResults;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.datatools.DataTypeConvertor;
import vm.datatools.Tools;
import vm.fs.FSGlobal;
import vm.queryResults.QueryExecutionStatsStoreInterface;

/**
 *
 * @author Vlada
 */
public class FSQueryExecutionStatsStoreImpl extends QueryExecutionStatsStoreInterface {

    private static final Logger LOG = Logger.getLogger(FSQueryExecutionStatsStoreImpl.class.getName());
    protected final StatsAttributesComparator statsComp = new StatsAttributesComparator();
    private final File output;
    protected final Map<String, TreeMap<QUERY_STATS, String>> content;

    public static enum DATA_NAMES_IN_FILE_NAME {
        ground_truth_name, ground_truth_query_set_name, ground_truth_nn_count,
        cand_set_name, cand_set_query_set_name, storing_result_name,
        cand_set_fixed_size
    }

    public static enum QUERY_STATS {
        query_obj_id, recall,
        cand_set_dynamic_size, query_execution_time,
        additional_stats, error_on_dist, frr
    }

    public FSQueryExecutionStatsStoreImpl(String groundTruthName, String groundTruthQuerySetName, int groundTruthNNCount,
            String candSetName, String candSetQuerySetName, String resultName,
            Integer candSetFixedSize) {
        Map<FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME, String> attributesForFileName = new HashMap<>();
        attributesForFileName.put(DATA_NAMES_IN_FILE_NAME.ground_truth_name, groundTruthName);
        attributesForFileName.put(DATA_NAMES_IN_FILE_NAME.ground_truth_query_set_name, groundTruthQuerySetName);
        attributesForFileName.put(DATA_NAMES_IN_FILE_NAME.ground_truth_nn_count, Integer.toString(groundTruthNNCount));
        attributesForFileName.put(DATA_NAMES_IN_FILE_NAME.cand_set_name, candSetName);
        attributesForFileName.put(DATA_NAMES_IN_FILE_NAME.cand_set_query_set_name, candSetQuerySetName);
        attributesForFileName.put(DATA_NAMES_IN_FILE_NAME.storing_result_name, resultName);
        if (candSetFixedSize != null) {
            attributesForFileName.put(DATA_NAMES_IN_FILE_NAME.cand_set_fixed_size, candSetFixedSize.toString());
        }
        output = getFileForStats(attributesForFileName);
        content = parseAsMap();
    }

    /**
     *
     * @param attributesForFileName: ground_truth_name,
     * ground_truth_query_set_name, ground_truth_nn_count, cand_set_name,
     * storing_result_name. Voluntary: cand_set_query_set_name
     * cand_set_fixed_size
     */
    public FSQueryExecutionStatsStoreImpl(Map<FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME, String> attributesForFileName) {
        output = getFileForStats(attributesForFileName);
        content = parseAsMap();
    }

    public FSQueryExecutionStatsStoreImpl(File file) {
        output = file;
        content = parseAsMap();
    }

    @Override
    public void storeStatsForQuery(Comparable queryObjId, Integer distanceComputationsCount, long time, Object... additionalParametersToStore) {
        TreeMap<FSQueryExecutionStatsStoreImpl.QUERY_STATS, String> treeMap = new TreeMap(new StatsAttributesComparator());
        treeMap.put(QUERY_STATS.query_obj_id, queryObjId.toString());
        treeMap.put(QUERY_STATS.cand_set_dynamic_size, Integer.toString(distanceComputationsCount));
        if (time != -1) {
            treeMap.put(QUERY_STATS.query_execution_time, Long.toString(time));
        } else {
            treeMap.put(QUERY_STATS.query_execution_time, "null");
        }
        if (additionalParametersToStore != null && additionalParametersToStore.length != 0) {
            String additionalStats = DataTypeConvertor.objectsToString(additionalParametersToStore, ",");
            treeMap.put(QUERY_STATS.additional_stats, additionalStats);
        } else {
            treeMap.put(QUERY_STATS.additional_stats, "null");
        }
        content.put(treeMap.get(QUERY_STATS.query_obj_id), treeMap);
    }

    @Override
    public void save() {
        BufferedWriter bw = null;
        try {
            FileOutputStream datasetOutputStream = new FileOutputStream(output, false);
            bw = new BufferedWriter(new OutputStreamWriter(datasetOutputStream));
            for (TreeMap<QUERY_STATS, String> map : content.values()) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<QUERY_STATS, String> entry : map.entrySet()) {
                    sb.append(statsComp.getName(entry.getKey()));
                    sb.append(";");
                    sb.append(entry.getValue());
                    sb.append(";");
                }
                try {
                    bw.write(sb.toString());
                    bw.write("\n");
                } catch (IOException ex) {
                    Logger.getLogger(FSQueryExecutionStatsStoreImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.flush();
                bw.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * returns list of rows. Each row is represented by a map
     *
     * @return
     */
    public final Map<String, TreeMap<QUERY_STATS, String>> parseAsMap() {
        Map<String, TreeMap<QUERY_STATS, String>> ret = new HashMap<>();
        if (output == null || !output.exists()) {
            return ret;
        }
        List<String[]> lines = Tools.parseCsvRowOriented(output.getAbsolutePath(), ";");
        Iterator<String[]> it = lines.iterator();
        while (it.hasNext()) {
            String[] line = it.next();
            TreeMap<QUERY_STATS, String> lineAsMap = new TreeMap<>(statsComp);
            for (int i = 0; i + 1 < line.length; i += 2) {
                lineAsMap.put(statsComp.indexToStats(line[i]), line[i + 1]);
            }
            ret.put(lineAsMap.get(QUERY_STATS.query_obj_id), lineAsMap);
        }
        return ret;
    }

    public final File getFileForStats(Map<FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME, String> dataNamesInFileName) {
        TreeMap<FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME, String> treeMap = new TreeMap(new FileNameAttributesComparator());
        treeMap.putAll(dataNamesInFileName);
        StringBuilder path = new StringBuilder();
        for (Map.Entry<DATA_NAMES_IN_FILE_NAME, String> entry : treeMap.entrySet()) {
            if (entry.getKey() != FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.storing_result_name) {
                path.append(entry.getValue()).append("__");
            }
        }
        File folder = new File(FSGlobal.RESULT_FOLDER, treeMap.get(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.storing_result_name));
        folder = new File(folder, FSGlobal.RESULT_STATS_FOLDER);
        String fileName = path.toString() + ".csv";
        File ret = new File(folder, fileName);
        ret = FSGlobal.checkFileExistence(ret, false);
        return ret;
    }

    private class FileNameAttributesComparator implements Comparator<DATA_NAMES_IN_FILE_NAME> {

        @Override
        public int compare(DATA_NAMES_IN_FILE_NAME o1, DATA_NAMES_IN_FILE_NAME o2) {
            int order1 = getOrder(o1);
            int order2 = getOrder(o2);
            return Integer.compare(order1, order2);
        }

        private int getOrder(DATA_NAMES_IN_FILE_NAME o) {
            switch (o) {
                case ground_truth_name: {
                    return 0;
                }
                case ground_truth_query_set_name: {
                    return 1;
                }
                case ground_truth_nn_count: {
                    return 2;
                }
                case cand_set_name: {
                    return 3;
                }
                case cand_set_query_set_name: {
                    return 4;
                }
                case storing_result_name: {
                    return -1;
                }
                case cand_set_fixed_size: {
                    return 6;
                }
            }
            return -1;
        }
    }

    public class StatsAttributesComparator implements Comparator<QUERY_STATS> {

        @Override
        public int compare(QUERY_STATS o1, QUERY_STATS o2) {
            String order1 = getName(o1);
            String order2 = getName(o2);
            return order2.compareTo(order1);
        }

        public QUERY_STATS indexToStats(String index) {
            switch (index) {
                case "query_obj_id": {
                    return QUERY_STATS.query_obj_id;
                }
                case "recall": {
                    return QUERY_STATS.recall;
                }
                case "cand_set_dynamic_size": {
                    return QUERY_STATS.cand_set_dynamic_size;
                }
                case "query_execution_time": {
                    return QUERY_STATS.query_execution_time;
                }
                case "additional_stats": {
                    return QUERY_STATS.additional_stats;
                }
                case "error_on_dist": {
                    return QUERY_STATS.error_on_dist;
                }
            }
            return null;
        }

        public String getName(QUERY_STATS o) {
            if (o == null) {
                return "null";
            }
            switch (o) {
                case query_obj_id: {
                    return "query_obj_id";
                }
                case recall: {
                    return "recall";
                }
                case cand_set_dynamic_size: {
                    return "cand_set_dynamic_size";
                }
                case query_execution_time: {
                    return "query_execution_time";
                }
                case additional_stats: {
                    return "additional_stats";
                }
                case error_on_dist: {
                    return "error_on_dist";
                }
            }
            return "null";
        }
    }

    public Map<String, TreeMap<QUERY_STATS, String>> getContent() {
        return Collections.unmodifiableMap(content);
    }

    @Override
    public Map<Object, Integer> getDistComps() {
        return returnStat(QUERY_STATS.cand_set_dynamic_size, false);
    }

    @Override
    public Map<Object, Long> getQueryTimes() {
        return returnStat(QUERY_STATS.query_execution_time, true);
    }

    private Map returnStat(QUERY_STATS keyStat, boolean islong) {
        Map ret = new HashMap<>();
        for (Map.Entry<String, TreeMap<QUERY_STATS, String>> entry : content.entrySet()) {
            String qID = entry.getKey();
            TreeMap<QUERY_STATS, String> stats = entry.getValue();
            String valueString = stats.get(keyStat);
            Object value = islong ? Long.parseLong(valueString) : Integer.parseInt(valueString);
            ret.put(qID, value);
        }
        return ret;

    }

}

package vm.fs.store.queryResults;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import vm.datatools.Tools;
import vm.fs.FSGlobal;
import vm.queryResults.QueryNearestNeighboursStoreInterface;
import vm.search.algorithm.impl.GroundTruthEvaluator;

/**
 *
 * @author Vlada
 */
public class FSNearestNeighboursStorageImpl extends QueryNearestNeighboursStoreInterface {

    private final Logger LOG = Logger.getLogger(FSNearestNeighboursStorageImpl.class.getName());
    private final boolean compress;

    public FSNearestNeighboursStorageImpl() {
        this(true);
    }

    public FSNearestNeighboursStorageImpl(boolean compress) {
        this.compress = compress;
    }

    public File getFileWithResults(String resultsName, String datasetName, String querySetName, Integer k, boolean willBeDeleted) {
        File ret = new File(FSGlobal.RESULT_FOLDER, resultsName);
        String n = datasetName;
        if (!querySetName.trim().equals("")) {
            n = n + "_" + querySetName;
        }
        if (k != null) {
            n += "_" + k;
        }
        n += compress ? ".gz" : ".csv";
        ret = new File(ret, n);
        ret = FSGlobal.checkFileExistence(ret, willBeDeleted);
        if (!ret.exists() && !willBeDeleted) {
            try {
                if (k != null) {
                    return getFileWithResults(resultsName, datasetName, querySetName, null, willBeDeleted);
                }
                if (k != null && k != 50000) {
                    ret = getFileWithResults(resultsName, datasetName, querySetName, 50000, willBeDeleted);
                    if (ret.exists()) {
                        return ret;
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("File with results " + ret.getAbsolutePath() + " does not exist");
            }
        }
        return ret;
    }

    @Override
    public void storeQueryResults(List<Comparable> queryObjectsIDs, TreeSet<Map.Entry<Comparable, Float>>[] queryResults, Integer k, String datasetName, String querySetName, String resultsName) {
        OutputStream os = null;
        try {
            File file = getFileWithResults(resultsName, datasetName, querySetName, k, true);
            os = new FileOutputStream(file, false);
            if (compress) {
                os = new GZIPOutputStream(os, true);
            }
            for (int i = 0; i < Math.min(queryObjectsIDs.size(), queryResults.length); i++) {
                if (queryResults[i] == null) {
                    continue;
                }
                String queryId = queryObjectsIDs.get(i).toString();
                store(os, queryId, queryResults[i]);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
    private boolean ask = true;

    @Override
    public void storeQueryResult(Comparable queryObjectID, TreeSet<Map.Entry<Comparable, Float>> queryResults, Integer k, String datasetName, String querySetName, String resultsName) {
        OutputStream os = null;
        try {
            File file = getFileWithResults(resultsName, datasetName, querySetName, k, ask);
            ask = false;
            os = new FileOutputStream(file, true);
            if (compress) {
                os = new GZIPOutputStream(os, true);
            }
            String queryId = queryObjectID.toString();
            store(os, queryId, queryResults);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    private final Map<Comparable, Map<Comparable, TreeSet<Map.Entry<Comparable, Float>>>> cache = new HashMap();

    @Override
    public Map<Comparable, TreeSet<Map.Entry<Comparable, Float>>> getQueryResultsForDataset(String queryResultsName, String datasetName, String querySetName, Integer k) {
        try {
            File file = getFileWithResults(queryResultsName, datasetName, querySetName, k, false);
            if (!file.exists()) {
                LOG.log(Level.SEVERE, "The file with the results does not exist: {0}", file.getAbsolutePath());
                file = getFileWithResults(queryResultsName, datasetName, querySetName, GroundTruthEvaluator.K_IMPLICIT_FOR_GROUND_TRUTH, false);
                if (!file.exists()) {
                    throw new IllegalArgumentException("The file does not exist");
                } else {
                    LOG.log(Level.INFO, "But the file with the results {0} exists, so taking the implicit number of NNs", file.getAbsolutePath());
                }
            }
            String key = queryResultsName + datasetName + querySetName;
            if (k != null) {
                key += k;
            }
            if (cache.containsKey(key)) {
                return cache.get(key);
            }
            Map<Comparable, TreeSet<Map.Entry<Comparable, Float>>> ret = getQueryResultsForDataset(file);
            cache.put(key, ret);
            return ret;
        } catch (IOException ex) {
            Logger.getLogger(FSNearestNeighboursStorageImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void store(OutputStream os, String queryId, TreeSet<Map.Entry<Comparable, Float>> queryResult) throws IOException {
        if (!compress && queryId.startsWith("Q")) {
            queryId = queryId.substring(1);
        }
        StringBuilder buffer = new StringBuilder(queryResult.size() * 16);
        if (compress) {
            buffer.append(queryId);
            buffer.append(";");
        }
        Iterator<Map.Entry<Comparable, Float>> it = queryResult.iterator();
        while (it.hasNext()) {
            Map.Entry<Comparable, Float> nn = it.next();
            buffer.append(nn.getKey().toString());
            buffer.append(":");
            buffer.append(nn.getValue().toString());
            buffer.append(";");
        }
        String s = buffer.toString();
        s = s.substring(0, s.length() - 1);
        os.write(s.getBytes());
        os.write('\n');
    }

    public Map<Comparable, TreeSet<Map.Entry<Comparable, Float>>> getQueryResultsForDataset(File file) throws IOException {
        Map<Comparable, TreeSet<Map.Entry<Comparable, Float>>> ret = new HashMap<>();
        InputStream s = new FileInputStream(file);
        if (compress) {
            s = new GZIPInputStream(s);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(s));
        String line = br.readLine();
        while (line != null) {
            String[] pairsOfNearestNeighbours = line.split(";");
            String queryObjId = pairsOfNearestNeighbours[0];
            TreeSet<Map.Entry<Comparable, Float>> nearestNeighbours = new TreeSet<>(new Tools.MapByFloatValueComparator());
            for (int i = 1; i < pairsOfNearestNeighbours.length; i++) {
                String nearestNeighbourPair = pairsOfNearestNeighbours[i];
                if (nearestNeighbourPair.isEmpty()) {
                    continue;
                }
                String[] idDistPair = nearestNeighbourPair.split(":");
                if (idDistPair.length >= 2) {
                    nearestNeighbours.add(new AbstractMap.SimpleEntry<>(idDistPair[0], Float.valueOf(idDistPair[1])));
                } else {
                    nearestNeighbours.add(new AbstractMap.SimpleEntry<>(idDistPair[0], Float.valueOf(i)));
                }
            }
            ret.put(queryObjId, nearestNeighbours);
            line = br.readLine();
        }
        return ret;
    }

}

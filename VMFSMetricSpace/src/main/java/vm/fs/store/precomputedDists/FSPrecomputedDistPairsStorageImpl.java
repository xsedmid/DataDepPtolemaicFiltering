package vm.fs.store.precomputedDists;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.datatools.Tools;
import vm.fs.FSGlobal;
import vm.metricSpace.distance.storedPrecomputedDistances.AbstractPrecomputedPairsOfDistancesStorage;

/**
 *
 * @author Vlada
 */
public class FSPrecomputedDistPairsStorageImpl extends AbstractPrecomputedPairsOfDistancesStorage {

    public final Logger LOG = Logger.getLogger(FSPrecomputedDistPairsStorageImpl.class.getName());

    private final String resultsName;
    private final int oSize;
    private final int qSize;

    public FSPrecomputedDistPairsStorageImpl(String datasetName, int oSize, int qSize) {
        this.resultsName = datasetName;
        this.oSize = oSize;
        this.qSize = qSize;
    }

    @Override
    public void storePrecomputedDistances(TreeSet<Map.Entry<String, Float>> dists) {
        OutputStreamWriter w = null;
        try {
            w = new OutputStreamWriter(new FileOutputStream(getFileForResults(true), false));
            Iterator<Map.Entry<String, Float>> it = dists.iterator();
            while (it.hasNext()) {
                Map.Entry<String, Float> next = it.next();
                String key = next.getKey();
                w.write(key + ";");
                w.write(next.getValue() + "\n");
            }
            System.out.flush();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                w.flush();
                w.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    public File getFileForResults(boolean willBeDeleted) {
        String fileName = resultsName + "__sample_" + oSize + "__ queries_" + qSize + ".csv";
        File ret = new File(FSGlobal.SMALLEST_DISTANCES, fileName);
        ret = FSGlobal.checkFileExistence(ret, willBeDeleted);
        return ret;
    }

    @Override
    public TreeSet<Map.Entry<String, Float>> loadPrecomputedDistances() {
        BufferedReader br = null;
        try {
            File file = getFileForResults(false);
            if (!file.exists()) {
                throw new Error("File with the precomputed distances does no exists for resultsName " + resultsName + ", o count" + oSize + ", q count " + qSize);
            }
            Comparator<Map.Entry<String, Float>> comp = new Tools.MapByFloatValueComparator<>();
            TreeSet<Map.Entry<String, Float>> ret = new TreeSet(comp);
            br = new BufferedReader(new FileReader(file));
            try {
                while (true) {
                    String line = br.readLine();
                    String[] split = line.split(";");
                    String key = split[0] + ";" + split[1];
                    float value = Float.parseFloat(split[2]);
                    AbstractMap.SimpleEntry<String, Float> e = new AbstractMap.SimpleEntry<>(key, value);
                    ret.add(e);
                }
            } catch (NullPointerException ex) {
            }
            return ret;
        } catch (IOException ex) {
            Logger.getLogger(FSPrecomputedDistPairsStorageImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(FSPrecomputedDistPairsStorageImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

}

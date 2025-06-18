package vm.fs.store.partitioning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import vm.datatools.DataTypeConvertor;
import vm.datatools.Tools;
import vm.fs.FSGlobal;

/**
 *
 * @author Vlada
 */
public class FSVoronoiPartitioningStorage extends FSStorageDatasetPartitionsInterface {

    public final Logger LOG = Logger.getLogger(FSVoronoiPartitioningStorage.class.getName());

    @Override
    public void store(Map<Comparable, Collection<Comparable>> mapping, String datasetName, String filterName, int origPivotCount) {
        if (mapping == null || mapping.isEmpty()) {
            LOG.log(Level.WARNING, "Nothing to store: {0}", mapping);
            return;
        }
        GZIPOutputStream os = null;
        try {
            File file = getFile(datasetName, filterName, origPivotCount, true);
            os = new GZIPOutputStream(new FileOutputStream(file, false), true);
            for (Map.Entry<Comparable, Collection<Comparable>> cell : mapping.entrySet()) {
                String pivotID = cell.getKey().toString();
                Collection<Comparable> ids = cell.getValue();
                os.write(pivotID.getBytes());
                os.write(';');
                for (Object id : ids) {
                    os.write(id.toString().getBytes());
                    os.write(';');
                }
                os.write('\n');
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

    public File getFile(String datasetName, int pivotCount, boolean willBeDeleted) {
        return getFile(datasetName, null, pivotCount, willBeDeleted);
    }

    @Override
    public File getFile(String datasetName, String filterName, int pivotCount, boolean willBeDeleted) {
        String name = datasetName;
        if (filterName != null && !filterName.trim().isEmpty()) {
            name += "_" + filterName;
        }
        name += "_" + pivotCount + "pivots.csv.gz";
        File ret = new File(FSGlobal.VORONOI_PARTITIONING_STORAGE, name);
        ret = FSGlobal.checkFileExistence(ret, willBeDeleted);
        return ret;
    }

    @Override
    public Map<Comparable, Collection<Comparable>> load(String datasetName, String suffix, int origPivotCount) {
        File f = getFile(datasetName, suffix, origPivotCount, false);
        return load(f);
    }

    public File[] filesWithApproximatePartitionings(String groundTruthDatasetName, int pivotCount, int clustersCount) {
        File folder = new File(FSGlobal.VORONOI_PARTITIONING_STORAGE);
        String pivotString = clustersCount < 0 ? pivotCount + "pivots" : pivotCount + "pivotsFilt";
        String clustersString = clustersCount < 0 ? "" : clustersCount + "clusters";
        File[] ret = folder.listFiles((File file, String string) -> {
            boolean start = string.startsWith(groundTruthDatasetName + "_" + pivotString);
            boolean clusters = string.contains(clustersString);
            boolean log = !string.contains("log");
            boolean end = string.endsWith("gz");
            return start && clusters && log && end;
        });
        return ret;
    }

    public Map<Comparable, TreeSet<Comparable>> loadAsTreeSets(File f) {
        Map<Comparable, Collection<Comparable>> load = load(f);
        return transformToTreeSets(load);
    }

    public Map<Comparable, Collection<Comparable>> load(File f) {
        SortedMap<String, String[]> keyValueMap = Tools.parseCsvMapKeyValues(f.getAbsolutePath());
        Map<Comparable, Collection<Comparable>> ret = new HashMap<>();
        Iterator<Map.Entry<String, String[]>> it = keyValueMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            Object[] values = entry.getValue();
            List<Object> list = DataTypeConvertor.arrayToList(values);
            list.remove(0);
            ret.put(entry.getKey(), new TreeSet(list));
        }
        LOG.log(Level.INFO, "The Voronoi partitioning in {1} has {0} non empty cells", new Object[]{ret.size(), f.getName()});
        return ret;
    }

}

package vm.fs.store.partitioning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import vm.datatools.Tools;
import vm.fs.FSGlobal;
import vm.metricSpace.datasetPartitioning.StorageDatasetPartitionsInterface;

/**
 *
 * @author Vlada
 */
public class FSVoronoiPartitioningStorage implements StorageDatasetPartitionsInterface {
    
    public final Logger LOG = Logger.getLogger(FSVoronoiPartitioningStorage.class.getName());
    
    @Override
    public void store(Map<Comparable, SortedSet<Comparable>> mapping, String datasetName, int origPivotCount) {
        if (mapping == null || mapping.isEmpty()) {
            LOG.log(Level.WARNING, "Nothing to store: {0}", mapping);
            return;
        }
        GZIPOutputStream os = null;
        try {
            File file = getFile(datasetName, origPivotCount, true);
            os = new GZIPOutputStream(new FileOutputStream(file, false), true);
            for (Map.Entry<Comparable, SortedSet<Comparable>> cell : mapping.entrySet()) {
                String pivotID = cell.getKey().toString();
                Set<Comparable> ids = cell.getValue();
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
        String name = datasetName + "_" + pivotCount + "pivots.csv.gz";
        File ret = new File(FSGlobal.VORONOI_PARTITIONING_STORAGE, name);
        ret = FSGlobal.checkFileExistence(ret, willBeDeleted);
        return ret;
    }
    
    @Override
    public Map<Comparable, TreeSet<Comparable>> load(String datasetName, int origPivotCount) {
        File f = getFile(datasetName, origPivotCount, false);
        SortedMap<String, String[]> keyValueMap = Tools.parseCsvMapKeyValues(f.getAbsolutePath());
        Map<Comparable, TreeSet<Comparable>> ret = new HashMap<>();
        Iterator<Map.Entry<String, String[]>> it = keyValueMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            Object[] values = entry.getValue();
            List<Object> list = Tools.arrayToList(values);
            list.remove(0);
            ret.put(entry.getKey(), new TreeSet(list));
        }
        LOG.log(Level.INFO, "The Voronoi partitioning has {0} non empty cells", ret.size());
        return ret;
    }
    
}

package vm.fs.store.dataTransforms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.fs.FSGlobal;
import vm.fs.store.precomputedDists.FSPrecomputedDistPairsStorageImpl;
import vm.metricSpace.AbstractMetricSpace;
import vm.objTransforms.storeLearned.PivotPairsStoreInterface;

/**
 *
 * @author Vlada
 * @param <T>
 */
public class FSGHPSketchesPivotPairsStorageImpl<T> implements PivotPairsStoreInterface<T> {

    private static final Logger LOG = Logger.getLogger(FSGHPSketchesPivotPairsStorageImpl.class.getName());

    @Override
    public void storePivotPairs(String resultName, AbstractMetricSpace<T> metricSpace, List<Object> pivots, Object... additionalInfoToStoreWithLearningSketching) {
        OutputStreamWriter w = null;
        try {
            w = new OutputStreamWriter(new FileOutputStream(getFileForResults(resultName, true), false));
            for (int i = 0; i < pivots.size(); i += 2) {
                Object p1 = metricSpace.getIDOfMetricObject(pivots.get(i));
                Object p2 = metricSpace.getIDOfMetricObject(pivots.get(i + 1));
                w.write(p1 + ";" + p2 + "\n");
            }
            if (additionalInfoToStoreWithLearningSketching.length != 0) {
                for (Object object : additionalInfoToStoreWithLearningSketching) {
                    w.write(object.toString() + "\n");
                }
            }
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

    protected File getFileForResults(String sketchesName, boolean willBeDeleted) {
        File ret = new File(FSGlobal.BINARY_SKETCHES, sketchesName + ".csv");
        ret = FSGlobal.checkFileExistence(ret, willBeDeleted);
        return ret;
    }

    @Override
    public List<String[]> loadPivotPairsIDs(String sketchesName) {
        BufferedReader br = null;
        try {
            File file = getFileForResults(sketchesName, false);
            if (!file.exists()) {
                throw new IllegalArgumentException("File with pivot pairs does no exists. File with name: " + sketchesName);
            }
            List<String[]> ret = new ArrayList<>();
            br = new BufferedReader(new FileReader(file));
            try {
                while (true) {
                    String line = br.readLine();
                    String[] split = line.split(";");
                    if (split.length == 2) {
                        ret.add(split);
                    }
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

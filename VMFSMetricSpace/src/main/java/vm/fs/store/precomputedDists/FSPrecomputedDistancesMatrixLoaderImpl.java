package vm.fs.store.precomputedDists;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import vm.fs.FSGlobal;
import vm.metricSpace.Dataset;
import vm.metricSpace.distance.storedPrecomputedDistances.AbstractPrecomputedDistancesMatrixLoader;

/**
 *
 * @author xmic
 */
public class FSPrecomputedDistancesMatrixLoaderImpl extends AbstractPrecomputedDistancesMatrixLoader {

    private static final Logger LOG = Logger.getLogger(FSPrecomputedDistancesMatrixLoaderImpl.class.getName());

    public float[][] loadPrecomPivotsToObjectsDists(File file, Dataset dataset, int maxColumnCount) {
        List<float[]> retList = new ArrayList<>();
        int maxPivots = maxColumnCount > 0 ? maxColumnCount : Integer.MAX_VALUE;
        float[][] ret = dataset == null ? null : new float[dataset.getPrecomputedDatasetSize()][maxPivots];
        try {
            BufferedReader br;
            if (file.getName().toLowerCase().endsWith(".gz")) {
                br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
            } else {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            }
            try {
                String line = br.readLine();
                String[] columns = line.split(";");
                columnHeaders = new HashMap<>();
                for (int i = 1; i < columns.length; i++) {
                    columnHeaders.put(columns[i], i - 1);
                }
                rowHeaders = new HashMap<>();
                for (int counter = 1; line != null; counter++) {
                    line = br.readLine();
                    String[] split = line.split(";");
                    maxPivots = Math.min(split.length - 1, maxPivots);
                    float[] lineFloats = new float[maxPivots];
                    rowHeaders.put(split[0], counter - 1);
                    for (int i = 0; i < lineFloats.length; i++) {
                        lineFloats[i] = Float.parseFloat(split[i + 1]);
                    }
                    if (ret != null) {
                        ret[counter - 1] = lineFloats;
                    } else {
                        retList.add(lineFloats);
                    }
                    if (counter % 50000 == 0) {
                        LOG.log(Level.INFO, "Parsed precomputed distances between pivots and {0} objects", counter);
                        if (vm.javatools.Tools.getRatioOfConsumedRam(true) >= 0.9) {
                            System.gc();
                        }
                    }
                }
            } catch (NullPointerException ex) {
            }
            if (ret == null) {
                ret = new float[retList.size()][maxPivots];
                for (int i = 0; i < retList.size(); i++) {
                    ret[i] = retList.get(i);
                }
            }
            if (dataset != null) {
                checkOrdersOfPivots(dataset.getPivots(maxColumnCount), dataset.getMetricSpace());
            }
            return ret;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Override
    public float[][] loadPrecomPivotsToObjectsDists(Dataset dataset, int pivotCount) {
        String datasetName = dataset.getDatasetName();
        String pivotSetName = dataset.getPivotSetName();
        File file = deriveFileForDatasetAndPivots(datasetName, pivotSetName, pivotCount, false);
        if (!file.exists()) {
            LOG.log(Level.WARNING, "No precomputed distances found for dataset {0} pivot set {1} and {2} pivots", new Object[]{datasetName, pivotSetName, pivotCount});
            return null;
        }
        return loadPrecomPivotsToObjectsDists(file, dataset, pivotCount);
    }

    public File deriveFileForDatasetAndPivots(String datasetName, String pivotSetName, int pivotCount, boolean willBeDeleted) {
        File ret = new File(FSGlobal.PRECOMPUTED_DISTS_FOLDER, datasetName + "_" + pivotSetName + "_" + pivotCount + "pivots.csv.gz");
        ret = FSGlobal.checkFileExistence(ret, willBeDeleted);
        if (!willBeDeleted && !ret.exists()) {
            FilenameFilter filter = (File file, String string) -> string.contains(datasetName + "_" + pivotSetName);
            File folder = new File(FSGlobal.PRECOMPUTED_DISTS_FOLDER);
            File[] candidates = folder.listFiles(filter);
            int bestCount = Integer.MAX_VALUE;
            for (File candidate : candidates) {
                int pivotCountInFile = parsePivotCountFromFileName(candidate.getName());
                if (pivotCountInFile >= pivotCount && pivotCountInFile < bestCount) {
                    bestCount = pivotCountInFile;
                    ret = candidate;
                }
            }
            if (bestCount == Integer.MAX_VALUE) {
                LOG.log(Level.WARNING, "File with precomputed distances does not exist: {0}", ret.getAbsolutePath());
            } else {
                LOG.log(Level.INFO, "Since the file with precomputed distances to {0} pivots does not exist, returning file with distances to {1} pivots", new Object[]{pivotCount, bestCount});
            }
        }
        return ret;
    }

    private int parsePivotCountFromFileName(String name) {
        name = name.substring(name.lastIndexOf("_") + 1);
        name = name.substring(0, name.indexOf("pivot"));
        return Integer.parseInt(name);
    }

}

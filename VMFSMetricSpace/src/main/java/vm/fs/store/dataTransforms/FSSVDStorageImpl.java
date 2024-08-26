package vm.fs.store.dataTransforms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import vm.fs.FSGlobal;
import vm.metricSpace.data.toStringConvertors.SingularisedConvertors;
import vm.objTransforms.storeLearned.SVDStoreInterface;

/**
 *
 * @author Vlada
 */
public class FSSVDStorageImpl implements SVDStoreInterface {

    private final Logger LOG = Logger.getLogger(FSSVDStorageImpl.class.getName());
    private final File output;
    private Map<String, String> fileContent = null;

    public FSSVDStorageImpl(String datasetName, int sampleCount, boolean willBeLearnt) {
        output = getFileWithSVD(datasetName, sampleCount, willBeLearnt);
    }

    @Override
    public void storeSVD(float[] meansOverColumns, float[] singularValues, float[][] matrixU, float[][] matrixVT, Object... additionalInfoToStoreWithPCA) {
        BufferedWriter bw = null;
        try {
            GZIPOutputStream datasetOutputStream = new GZIPOutputStream(new FileOutputStream(output, true), true);
            bw = new BufferedWriter(new OutputStreamWriter(datasetOutputStream));

            bw.write("Means\n");
            bw.write(SingularisedConvertors.FLOAT_VECTOR_SPACE.metricObjectDataToString(meansOverColumns));
            bw.write("\n");
            LOG.log(Level.INFO, "Stored sizes of arrays: meansOverColumns: {0}", meansOverColumns.length);

            bw.write("Singular values\n");
            bw.write(SingularisedConvertors.FLOAT_VECTOR_SPACE.metricObjectDataToString(singularValues));
            bw.write("\n");
            LOG.log(Level.INFO, "Stored sizes of arrays: singularValues: {0}", singularValues.length);

            bw.write("matrixVT\n");
            bw.write(SingularisedConvertors.FLOAT_MATRIX_SPACE.metricObjectDataToString(matrixVT));
            bw.write("\n");
            LOG.log(Level.INFO, "Stored sizes of arrays: matrixVT: {0} * {1}", new Object[]{matrixVT.length, matrixVT[0].length});

            bw.write("matrixU\n");
            bw.write(SingularisedConvertors.FLOAT_MATRIX_SPACE.metricObjectDataToString(matrixU));
            bw.write("\n");
            LOG.log(Level.INFO, "Stored sizes of arrays: matrixU: {0} * {1}", new Object[]{matrixU.length, matrixU[0].length});

            LOG.log(Level.INFO, "Stored");
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

    private Map<String, String> parseFile() {
        try {
            Map<String, String> ret = new HashMap<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(output))));
            String key = null;
            StringBuilder value = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (line.equals("Means") || line.equals("Singular values") || line.equals("matrixVT") || line.equals("matrixU")) {
                    if (key != null) {
                        ret.put(key, value.toString().trim());
                    }
                    key = line;
                    value = new StringBuilder();
                    continue;
                }
                value.append(line).append("\n");
            }
            ret.put(key, value.toString().trim());
            return ret;
        } catch (IOException ex) {
            Logger.getLogger(FSSVDStorageImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public float[][] getVTMatrix(Object... params) {
        if (fileContent == null) {
            fileContent = parseFile();
        }
        String matrixAsString = fileContent.get("matrixVT");
        return SingularisedConvertors.FLOAT_MATRIX_SPACE.parseString(matrixAsString);
    }

    @Override
    public float[][] getUMatrix(Object... params) {
        if (fileContent == null) {
            fileContent = parseFile();
        }
        String matrixAsString = fileContent.get("matrixU");
        return SingularisedConvertors.FLOAT_MATRIX_SPACE.parseString(matrixAsString);
    }

    @Override
    public float[] getSingularValues(Object... params) {
        if (fileContent == null) {
            fileContent = parseFile();
        }
        String matrixAsString = fileContent.get("Singular values");
        return SingularisedConvertors.FLOAT_VECTOR_SPACE.parseString(matrixAsString);
    }

    @Override
    public float[] getMeansOverColumns(Object... params) {
        if (fileContent == null) {
            fileContent = parseFile();
        }
        String matrixAsString = fileContent.get("Means");
        return SingularisedConvertors.FLOAT_VECTOR_SPACE.parseString(matrixAsString);
    }

    public final File getFileWithSVD(String datasetName, int sampleCount, boolean willBeDeleted) {
        String fileName = datasetName + "_" + sampleCount;
        File ret = FSGlobal.checkFileExistence(new File(FSGlobal.AUXILIARY_FOR_SVD_TRANSFORMS, fileName + ".gz"), willBeDeleted);
        if (willBeDeleted) {
            FSGlobal.checkFileExistence(ret);
        }
        return ret;
    }

}

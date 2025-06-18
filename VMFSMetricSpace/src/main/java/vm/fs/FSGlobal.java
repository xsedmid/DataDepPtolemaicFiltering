package vm.fs;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import vm.javatools.Tools;

/**
 *
 * @author xmic
 */
public class FSGlobal {

    public static Boolean askWhenGoingToOverrideFile = null;
    public static final boolean SLEEP_MY_COMP = true;
    private static final Logger LOG = Logger.getLogger(FSGlobal.class.getName());

    /**
     * N drive is the tertiary storage with slow reading. If dataset is stored
     * there and should be read, the flag decides whether the Exception is fired
     * instead.
     */
    public static final Boolean STOP_ON_READING_FROM_NAS = true;

    private static String initRoot() {
        String separator = System.getProperty("file.separator");
        if (separator == null || separator.trim().isEmpty()) {
            separator = "/";
        }
        String[] paths = new String[]{
            "Similarity_search" + separator,
            "h:\\Similarity_search\\",
            "c:\\Data\\Similarity_search\\"
        };
        for (String path : paths) {
            File f = new File(path);
            if (f.exists()) {
                String env = System.getenv("USERDOMAIN");
                if (env != null && SLEEP_MY_COMP) {
                    Tools.setSleepDuringTheNight(env.equals("VLADUV-POCITAC"));
                }
                return path;
            }
        }
        return paths[0];
    }

    public static final String ROOT = initRoot();
    public static final Boolean UNIX = ROOT.contains("/");

    public static final String TRIALS_FOLDER = ROOT + "Trials\\";

    public static final String DATA_FOLDER = ROOT + "Dataset\\";
    public static final String DATASET_MVSTORAGE_FOLDER = DATA_FOLDER + "MV_storage\\";
    public static final String DATASET_FOLDER = DATA_FOLDER + "Dataset\\";
    public static final String DATASET_METADATA = DATA_FOLDER + "Metadata\\";
    public static final String PIVOT_FOLDER = DATA_FOLDER + "Pivot\\";
    public static final String QUERY_FOLDER = DATA_FOLDER + "Query\\";
    public static final String PRECOMPUTED_DISTS_FOLDER = DATA_FOLDER + "DistsToPivots";
    public static final String PRECOMPUTED_PIVOT_PERMUTATIONS_FOLDER = DATA_FOLDER + "PivotPermutations";

    public static final String RESULT_FOLDER = ROOT + "Result\\";
    public static final String RESULT_STATS_FOLDER = "Processed_stats\\";
    public static final String GROUND_TRUTH_FOLDER = RESULT_FOLDER + "Ground_truth\\";

    public static final String AUXILIARY_FOR_DATA_TRANSFORMS = ROOT + "Auxiliary_for_transforms\\";
    public static final String AUXILIARY_FOR_SVD_TRANSFORMS = AUXILIARY_FOR_DATA_TRANSFORMS + "SVD\\";
    public static final String BINARY_SKETCHES = AUXILIARY_FOR_DATA_TRANSFORMS + "Sketches\\";

    public static final String AUXILIARY_FOR_DATA_FILTERING = ROOT + "Auxiliary_for_filtering\\";
    public static final String SECONDARY_FILTERING_WITH_SKETCHES_AUXILIARY = AUXILIARY_FOR_DATA_FILTERING + "Secondary_filtering_with_sk_auxiliary\\";
    public static final String SECONDARY_FILTERING_WITH_SKETCHES_FINAL_MAPPING = AUXILIARY_FOR_DATA_FILTERING + "Secondary_filtering_with_sk_mapping\\";
    public static final String PIVOT_PAIRS_FOR_DATA_DEPENDENT_PTOLEMAIC_FILTERING = AUXILIARY_FOR_DATA_FILTERING + "PivotPairsForPtolemaicFiltering\\";
    public static final String SMALLEST_DISTANCES = AUXILIARY_FOR_DATA_FILTERING + "Smallest_distances";
    public static final String AUXILIARY_FOR_TRIANGULAR_FILTERING_WITH_LIMITED_ANGLES = AUXILIARY_FOR_DATA_FILTERING + "Triangle_ineq_with_limited_angles\\";
    public static final String AUXILIARY_FOR_PTOLEMAIOS_WITH_LIMITED_ANGLES = AUXILIARY_FOR_DATA_FILTERING + "Ptolemaios_limited_angles\\";
    public static final String AUXILIARY_FOR_PTOLEMAIOS_COEFS_WITH_LIMITED_ANGLES = AUXILIARY_FOR_PTOLEMAIOS_WITH_LIMITED_ANGLES + "Simple_coefs\\";

    public static final String PARTITIONED_DATASETS = DATA_FOLDER + "Partitioning\\";
    public static final String VORONOI_PARTITIONING_STORAGE = PARTITIONED_DATASETS + "Voronoi_partitioning\\";
    public static final String GRAPPLE_PARTITIONING_STORAGE = AUXILIARY_FOR_DATA_FILTERING + "GRAPPLE_partitioning\\";

    public static final String SIMREL_TOMEGA_THRESHOLDS = AUXILIARY_FOR_DATA_FILTERING + "SimRel_tOmega_thresholds\\";
    public static final String FOLDER_PLOTS = ROOT + "Plots\\";
    public static final String FOLDER_DATA_FOR_PLOTS = FOLDER_PLOTS + "Data\\";
    public static final String DIST_DISTRIBUTION_PLOTS_FOLDER = DATA_FOLDER + "DD_Plots\\";

    public static final File checkFileExistence(File file, boolean willBeDeleted) {
        Object[] options = new String[]{"Yes", "No"};
        file = new File(checkUnixPath(file.getAbsolutePath()));
        file.getParentFile().mkdirs();
        if (file.exists() && willBeDeleted && askWhenGoingToOverrideFile == null) {
            askRewritting();
        }
        if (file.exists() && willBeDeleted && askWhenGoingToOverrideFile) {
            LOG.log(Level.WARNING, "Asking for a question, waiting for the reply: {0}", file.getAbsolutePath());
            String question = "File " + file.getName() + " at " + file.getAbsolutePath() + " already exists. Do you want to delete its content? Answer no causes immediate stop.";
            int add = JOptionPane.showOptionDialog(null, question, "Override file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, JOptionPane.NO_OPTION);
            if (add == 1) {
                System.exit(1);
            }
            file.delete();
            LOG.log(Level.INFO, "File returned ({0})", file.getAbsolutePath());
            return file;
        } else {
            LOG.log(Level.INFO, "File pointer created ({0})", file.getAbsolutePath());
        }
        return file;
    }

    private static void askRewritting() {
        String question = "Should I ask when going to rewrite existing file?";
        boolean answer = true;
        try {
            Object[] options = new String[]{"Yes", "No"};
            int banOverloadingFiles = JOptionPane.showOptionDialog(null, question, "Ask when overriding?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, JOptionPane.NO_OPTION);
            answer = banOverloadingFiles != 1;
        } catch (Exception e) {
        }
        askWhenGoingToOverrideFile = answer;
        LOG.log(Level.INFO, "Will ask when rewritting a file? {0}", askWhenGoingToOverrideFile);
    }

    public static final File checkFileExistence(File file) {
        return checkFileExistence(file, true);
    }

    private static String checkUnixPath(String path) {
        if (UNIX) {
            return path.replace("\\", "/");
        }
        return path;
    }
}

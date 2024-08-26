/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.JFreeChart;
import vm.datatools.DataTypeConvertor;
import vm.datatools.Tools;
import vm.fs.FSGlobal;
import vm.fs.store.queryResults.FSQueryExecutionStatsStoreImpl;
import vm.fs.store.queryResults.FSQueryExecutionStatsStoreImpl.QUERY_STATS;
import vm.fs.store.queryResults.recallEvaluation.FSRecallOfCandidateSetsStorageImpl;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotPlotter;
import vm.plot.impl.BoxPlotXYPlotter;

/**
 *
 * @author au734419
 */
public abstract class FSAbstractPlotterFromResults {

    private static final Logger LOG = Logger.getLogger(FSAbstractPlotterFromResults.class.getName());

    private final boolean plotOnlyPDF;
    private AbstractPlotter plotter = getPlotter();
    private final Object[] xTicks;
    private final AbstractPlotter.COLOUR_NAMES[] colourIndexesForTraces;
    private final String[] folderNames;

    protected FSAbstractPlotterFromResults(boolean plotOnlyPDF, Object[] xTicks, String[] folderNames) {
        this.plotOnlyPDF = plotOnlyPDF;
        this.xTicks = xTicks == null ? new Object[]{"X"} : xTicks;
        this.folderNames = folderNames;
        this.colourIndexesForTraces = getVoluntaryColoursForTracesOrNull();
        check();
    }

    public FSAbstractPlotterFromResults(boolean plotOnlyPDF) {
        this.folderNames = getFolderNamesForDisplayedTraces();
        this.colourIndexesForTraces = getVoluntaryColoursForTracesOrNull();
        this.xTicks = getDisplayedNamesOfGroupsThatMeansFiles();
        this.plotOnlyPDF = plotOnlyPDF;
        check();
    }

    private void check() {
        if (plotter instanceof BoxPlotPlotter && Tools.isParseableToFloats(xTicks)) {
            plotter = new BoxPlotXYPlotter();
        }
        if (colourIndexesForTraces != null && colourIndexesForTraces.length < folderNames.length) {
            throw new IllegalArgumentException("Incosistent specification of colours and folders. The counts do not match. Colours: " + colourIndexesForTraces.length + ", folders: " + folderNames.length);
        }
    }

    public abstract String[] getDisplayedNamesOfTracesThatMatchesFolders();

    public abstract String[] getFolderNamesForDisplayedTraces();

    public abstract Object[] getDisplayedNamesOfGroupsThatMeansFiles();

    public abstract String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup();

    public abstract String getXAxisLabel();

    public abstract AbstractPlotter getPlotter();

    public abstract String getResultName();

    public abstract String getFolderForPlots();

    protected abstract String getYAxisNameForAdditionalParams();

    protected abstract Float transformAdditionalStatsForQueryToFloat(float firstValue);

    protected abstract AbstractPlotter.COLOUR_NAMES[] getVoluntaryColoursForTracesOrNull();

    public FilenameFilter getFilenameFilterStatsFiles() {
        String[] array = getUniqueArtifactIdentifyingFileNameForDisplaydGroup();
        return getFileNameFilterOR(true, array);
    }

    public FilenameFilter getFilenameFilterFolders() {
        return getFileNameFilterArray(folderNames);
    }

    protected String getResultFullNameWithDate(QUERY_STATS statName) {
        int datasetsCount = xTicks.length;
        int techCount = folderNames.length;
        String plotName = plotter.getSimpleName();
        String className = getClass().getCanonicalName();
        className = className.substring(className.lastIndexOf(".") + 1);

        String fileName = Tools.getDateYYYYMM() + "_" + getResultName() + "_" + className;
        fileName += "_" + datasetsCount + "data_" + techCount + "techs_" + plotName + "_" + statName;
        File file = new File(getFolderForPlots(), fileName);
        FSGlobal.checkFileExistence(file, false);
        return file.getAbsolutePath();
    }

    protected QUERY_STATS[] getStatsToPrint() {
        return new QUERY_STATS[]{QUERY_STATS.recall, QUERY_STATS.frr, QUERY_STATS.cand_set_dynamic_size, QUERY_STATS.query_execution_time, QUERY_STATS.error_on_dist, QUERY_STATS.additional_stats};
    }

    private List<File> getFilesWithResultsToBePlotted(int groupsCount, int boxplotsCount) {
        File resultsRoot = new File(FSGlobal.RESULT_FOLDER);
        File[] folders = resultsRoot.listFiles(getFilenameFilterFolders());
        if (folders.length != boxplotsCount) {
            String[] artifactsWithoutAMatchingFolder = getArtifactsWithoutAFolder(getFolderNamesForDisplayedTraces(), folders);
            for (String artifact : artifactsWithoutAMatchingFolder) {
                if (artifact != null) {
                    throw new IllegalArgumentException("\nYou have wrong filename filter as number of result folders " + folders.length + " differs from the number of name artifacts " + boxplotsCount + ". Wrong folder name is " + artifact);
                }
            }
        } else {
            LOG.log(Level.INFO, "Correct, there are {0} folders matching the rule", folders.length);
        }

        String[] uniqueArtifactsForFiles = getUniqueArtifactIdentifyingFileNameForDisplaydGroup();
        folders = reorder(folders, folderNames, true);
        FilenameFilter filenameFilterFiles = getFilenameFilterStatsFiles();
        List<File> ret = new ArrayList<>();
        for (File folder : folders) {
            File folderWithStats = new File(folder, FSGlobal.RESULT_STATS_FOLDER);
            File[] files = folderWithStats.listFiles(filenameFilterFiles);
            if (files.length != groupsCount) {
                System.err.println();
                for (File file : files) {
                    System.err.println("File: " + file.getName());
                }
                System.err.println();
                String message = "You have wrong uniqueArtifactIdentifyingFileNameForDisplaydGroup filter as number of files after the filtering " + files.length + " of folder " + folderWithStats.getAbsolutePath() + " differs from the number of name artifacts " + groupsCount;
                LOG.log(Level.SEVERE, message);
                for (int i = 0; i < Math.max(0, groupsCount - files.length); i++) {
                    ret.add(null);
                }
                if (files.length > groupsCount) {
                    for (File file : files) {
                        System.err.println(file.getName());
                    }
                    throw new IllegalArgumentException(message);
                }
            }
            if (files.length != 0) {
                files = reorder(files, uniqueArtifactsForFiles, false);
                LOG.log(Level.INFO, "Folder {0} contains {1} matching files", new Object[]{folder.getName(), files.length});
                List list = Tools.arrayToList(files);
                ret.addAll(list);
            }
        }
        LOG.log(Level.INFO, "The final plot will have {0} values in {1} traces which means {2} values per trace on average", new Object[]{ret.size(), folders.length, (ret.size() / folders.length)});
        return ret;
    }

    private Map<QUERY_STATS, List<Float>[][]> loadStatsFromFileAsListOfXYValues(List<File> files, int groupsCount, int boxplotsCount) {
        QUERY_STATS[] statsToPrint = getStatsToPrint();

        Map<QUERY_STATS, List<Float>[][]> ret = initRet(groupsCount, boxplotsCount, statsToPrint);
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            int groupIdx = (int) (i % groupsCount);
            int traceIdx = (int) (i / groupsCount);

            if (file != null) {
                System.out.println("XXX: groupIdx" + groupIdx + " | traceIdx " + traceIdx + " | " + file.getAbsolutePath());
            }

            FSQueryExecutionStatsStoreImpl storage = new FSRecallOfCandidateSetsStorageImpl(file);
            Map<String, TreeMap<QUERY_STATS, String>> results = storage.getContent();
            for (QUERY_STATS stat : statsToPrint) {
                List<Float>[][] listOfValues = ret.get(stat);
                List<Float> values = listOfValues[traceIdx][groupIdx];
                update(values, results, stat);
                if (stat.equals(QUERY_STATS.recall) && !values.isEmpty()) {
                    float min = (float) vm.math.Tools.getMin(DataTypeConvertor.floatToPrimitiveArray(values));
                    plotter.updateMinRecall(min);
                }
                if (values.isEmpty()) {
                    listOfValues[traceIdx][groupIdx] = null;
                }
            }
        }
        return ret;
    }

    public void makePlots() {
        int groupsCount = xTicks.length;
        int boxplotsCount = getDisplayedNamesOfTracesThatMatchesFolders().length;
        if (boxplotsCount < folderNames.length) {
            throw new IllegalArgumentException("Inconsistent numbers: the number of folders returned by getFolderNamesForDisplayedTraces() " + folderNames.length + " does not match the number of names given by getDisplayedNamesOfTracesThatMatchesFolders() " + boxplotsCount);
        } else if (boxplotsCount > folderNames.length) {
            LOG.log(Level.WARNING, "Inconsistent numbers: the number of folders returned by getFolderNamesForDisplayedTraces() {0} does not match the number of names given by getDisplayedNamesOfTracesThatMatchesFolders() {1}", new Object[]{folderNames.length, boxplotsCount});
        }

        List<File> files = getFilesWithResultsToBePlotted(groupsCount, boxplotsCount);
        Map<QUERY_STATS, List<Float>[][]> dataForStats = loadStatsFromFileAsListOfXYValues(files, groupsCount, boxplotsCount);
        Set<QUERY_STATS> keyForPlots = dataForStats.keySet();
        Map<QUERY_STATS, String> yLabels = queryStatsToYAxisLabels(dataForStats.get(QUERY_STATS.query_execution_time));
        for (QUERY_STATS key : keyForPlots) {
            makePlotsForQueryStats(key, dataForStats, plotter, yLabels.get(key));
        }
    }

    private void makePlotsForQueryStats(QUERY_STATS key, Map<QUERY_STATS, List<Float>[][]> dataForStats, AbstractPlotter plotter, String yAxisLabel) {
        List<Float>[][] values = dataForStats.get(key);
        if (isEmpty(values)) {
            return;
        }
        String path = getResultFullNameWithDate(key);
        LOG.log(Level.INFO, "Path for future plot: {0}", path);
        String xAxisLabel = getXAxisLabel();
        JFreeChart plot = plotter.createPlot("", xAxisLabel, yAxisLabel, getDisplayedNamesOfTracesThatMatchesFolders(), colourIndexesForTraces, xTicks, values);
        plotter.storePlotPDF(path, plot);
        if (!plotOnlyPDF) {
            plotter.storePlotPNG(path, plot);
        }
    }

    public FilenameFilter getFileNameFilterArray(String... substrings) {
        return (File dir, String name) -> {
            for (String substring : substrings) {
                if (name.toLowerCase().equals(substring.toLowerCase())) {
                    return true;
                }
            }
            return false;
        };
    }

    public FilenameFilter getFileNameFilterOR(boolean checkSuffixCSV, String... substrings) {
        return (File dir, String name) -> {
            for (String substring : substrings) {
                if (name.contains(substring)) {
                    if (checkSuffixCSV) {
                        return name.endsWith(".csv");
                    } else {
                        return true;
                    }
                }
            }
            return false;
        };
    }

    public FilenameFilter getFileNameFilterAND(boolean checkSuffixCSV, String... substrings) {
        return (File dir, String name) -> {
            for (String substring : substrings) {
                if (!name.contains(substring)) {
                    return false;
                }
            }
            if (checkSuffixCSV) {
                return name.endsWith(".csv");
            }
            return true;
        };
    }

    protected String[] strings(String... strings) {
        return strings;
    }

    protected Object[] array(Object... objects) {
        return objects;
    }

    private String unitForTime;

    private Map<QUERY_STATS, String> queryStatsToYAxisLabels(List<Float>[][] timeValues) {
        Map<QUERY_STATS, String> ret = new HashMap<>();
        ret.put(QUERY_STATS.cand_set_dynamic_size, "CandSet(q) size");
        ret.put(QUERY_STATS.cand_set_dynamic_size, "Remaining objects");
        ret.put(QUERY_STATS.error_on_dist, "Error on Dist");
        unitForTime = setUnitForTime(timeValues);
        if (unitForTime == null) {
            unitForTime = "";
        } else {
            unitForTime = " (" + unitForTime + ")";
        }
        ret.put(QUERY_STATS.query_execution_time, "Time" + unitForTime);
        ret.put(QUERY_STATS.recall, "Recall");
        ret.put(QUERY_STATS.frr, "False Reject Rate");
        String yAxisNameForAdditionalParams = getYAxisNameForAdditionalParams();
        if (yAxisNameForAdditionalParams != null) {
            ret.put(QUERY_STATS.additional_stats, yAxisNameForAdditionalParams);
        }
        return ret;
    }

    private File[] reorder(File[] files, String[] uniqueArtifactsForFiles, boolean directMatch) {
        File[] ret = new File[uniqueArtifactsForFiles.length];
        for (File file : files) {
            int idx = getIndex(uniqueArtifactsForFiles, file.getName().toLowerCase(), directMatch);
            if (idx != -1) {
                if (idx > ret.length) {
                    throw new IllegalArgumentException("You have wrong file/folder name filter as number of files/folders " + ret.length + " is smaller than the number of name artifacts " + uniqueArtifactsForFiles.length);
                } else {
                    ret[idx] = file;
                }
            }
        }
        return ret;
    }

    private int getIndex(String[] uniqueArtifactsForFiles, String name, boolean directMatch) {
        int ret = -1;
        for (int i = 0; i < uniqueArtifactsForFiles.length; i++) {
            String artifact = uniqueArtifactsForFiles[i].toLowerCase();
            if ((!directMatch && name.contains(artifact)) || (directMatch && name.equals(artifact))) {
                if (ret == -1) {
                    ret = i;
                } else {
                    throw new IllegalArgumentException("Unique artifacts of the file/folder names are not unique: " + name + " matches both, " + uniqueArtifactsForFiles[ret] + " and " + uniqueArtifactsForFiles[i]);
                }
            }
        }
        return ret;
    }

    private void update(List<Float> list, Map<String, TreeMap<QUERY_STATS, String>> stats, QUERY_STATS stat) {
        for (Map.Entry<String, TreeMap<QUERY_STATS, String>> statsForQueryEntry : stats.entrySet()) {
            TreeMap<QUERY_STATS, String> statsForQuery = statsForQueryEntry.getValue();
            String valueString;
            if (stat.equals(QUERY_STATS.frr)) {
                valueString = statsForQuery.get(QUERY_STATS.recall);
            } else {
                valueString = statsForQuery.get(stat);
            }
            if (valueString != null) {
                Float fValue = Tools.parseFloat(valueString);
                if (fValue != null) {
                    if (stat.equals(QUERY_STATS.additional_stats)) {
                        fValue = transformAdditionalStatsForQueryToFloat(fValue);
                    }
                    if (stat.equals(QUERY_STATS.frr)) {
                        fValue = 1 - fValue;
                    }
                    list.add(fValue);
                } else { // more additional stats than 1
                    String[] split = valueString.split(",");
                    fValue = Tools.parseFloat(split[0]);
                    if (split.length > 0 && fValue != null) {
                        fValue = transformAdditionalStatsForQueryToFloat(fValue);
                        if (fValue != null) {
                            list.add(fValue);
                        }
                    }
                }
            }
        }
    }

    private Map<QUERY_STATS, List<Float>[][]> initRet(int groupsCount, int boxplotsCount, QUERY_STATS[] statsToPrint) {
        Map<QUERY_STATS, List<Float>[][]> ret = new HashMap<>();
        for (QUERY_STATS stat : statsToPrint) {
            List[][] lists = new List[boxplotsCount][groupsCount];
            for (int g = 0; g < groupsCount; g++) {
                for (int t = 0; t < boxplotsCount; t++) {
                    lists[t][g] = new ArrayList();
                }
            }
            ret.put(stat, lists);
        }
        return ret;
    }

    private String setUnitForTime(List<Float>[][] timeValues) {
        if (timeValues == null) {
            return "";
        }
        List<Float> all = new ArrayList<>();
        for (List<Float>[] timeValue : timeValues) {
            for (List<Float> list : timeValue) {
                if (list != null) {
                    all.addAll(list);
                }
            }
        }
        if (all.isEmpty()) {
            return "s";
        }
        double max = vm.math.Tools.getMax(DataTypeConvertor.floatToPrimitiveArray(all));
        if (max >= 1300) {
            for (List<Float>[] timeValue : timeValues) {
                for (List<Float> list : timeValue) {
                    if (list == null || list.isEmpty()) {
                        continue;
                    }
                    List<Float> listNew = new ArrayList<>();
                    for (int k = 0; k < list.size(); k++) {
                        Float value = list.get(k);
                        listNew.add(value / 1000);
                    }
                    list.clear();
                    list.addAll(listNew);
                }
            }
            return "s";
        }
        return "ms";
    }

    private boolean isEmpty(List<Float>[][] values) {
        for (List<Float>[] value : values) {
            for (List<Float> list : value) {
                if (list != null && !list.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private String[] getArtifactsWithoutAFolder(String[] foldersArtifacts, File[] folders) {
        String[] ret = new String[foldersArtifacts.length];
        System.arraycopy(foldersArtifacts, 0, ret, 0, foldersArtifacts.length);
        for (int i = 0; i < foldersArtifacts.length; i++) {
            String artifactL = foldersArtifacts[i].toLowerCase();
            for (File folder : folders) {
                String name = folder.getName().toLowerCase();
                if (name.equals(artifactL)) {
                    ret[i] = null;
                }
            }
        }
        return ret;
    }
}

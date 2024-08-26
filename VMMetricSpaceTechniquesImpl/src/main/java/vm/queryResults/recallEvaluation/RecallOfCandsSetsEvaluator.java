package vm.queryResults.recallEvaluation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.queryResults.QueryNearestNeighboursStoreInterface;

/**
 *
 * @author Vlada
 */
public class RecallOfCandsSetsEvaluator {

    private final QueryNearestNeighboursStoreInterface resultsStorage;
    private final RecallOfCandsSetsStoreInterface recallStorage;

    public RecallOfCandsSetsEvaluator(QueryNearestNeighboursStoreInterface resultsStorage, RecallOfCandsSetsStoreInterface recallStorage) {
        this.resultsStorage = resultsStorage;
        this.recallStorage = recallStorage;
    }

    public Map<Comparable, Float> evaluateAndStoreRecallsOfQueries(String groundTruthDatasetName, String groundTruthQuerySetName, int groundTruthNNCount,
            String candSetName, String candSetQuerySetName, String resultSetName, Integer candidateNNCount) {

        Map<Comparable, TreeSet<Map.Entry<Comparable, Float>>> groundTruthForDataset = resultsStorage.getGroundTruthForDataset(groundTruthDatasetName, groundTruthQuerySetName);
        Map<Comparable, TreeSet<Map.Entry<Comparable, Float>>> candidateSets = resultsStorage.getQueryResultsForDataset(resultSetName, candSetName, candSetQuerySetName, candidateNNCount);

        Map<Comparable, Float> ret = new HashMap<>();
        Set<Comparable> queryIDs = groundTruthForDataset.keySet();
        for (Comparable queryID : queryIDs) {
            if (!candidateSets.containsKey(queryID)) {
                Logger.getLogger(RecallOfCandsSetsEvaluator.class.getName()).log(Level.WARNING, "Query object {0} not evaluated in the candidates", queryID);
                continue;
            }
            Set<Comparable> groundTruthForQuery = getFirstIDs(queryID, groundTruthForDataset.get(queryID), groundTruthNNCount);
            Set<Comparable> candidatesForQuery = getFirstIDs(queryID, candidateSets.get(queryID), candidateNNCount);
            int hits = 0;
            for (Comparable id : groundTruthForQuery) {
                if (candidatesForQuery.contains(id)) {
                    hits++;
                }
            }
            float recall = ((float) hits) / groundTruthForQuery.size();
            recallStorage.storeRecallForQuery(queryID, recall, groundTruthDatasetName, groundTruthQuerySetName, groundTruthNNCount, candSetName, candSetQuerySetName, candidateNNCount, resultSetName);
            System.out.println("Query ID: " + queryID + ", recall: " + recall);
            ret.put(queryID, recall);
        }
        recallStorage.save();
        return ret;
    }

    public static final Set<Comparable> getFirstIDs(Comparable queryID, TreeSet<Map.Entry<Comparable, Float>> evaluatedQuery, Integer count) {
        Set<Comparable> ret = new HashSet<>();
        if (evaluatedQuery == null) {
            return ret;
        }
        int limit = count == null ? Integer.MAX_VALUE : count;
        Iterator<Map.Entry<Comparable, Float>> it = evaluatedQuery.iterator();
        while (it.hasNext() && ret.size() < limit) {
            Map.Entry<Comparable, Float> nn = it.next();
            ret.add(nn.getKey().toString());
        }
        if (count != null && count > ret.size()) {
            Logger.getLogger(RecallOfCandsSetsEvaluator.class.getName()).log(Level.WARNING, "The candidate set evaluated the query {0} does not contain so many objects. Required: {1}, found: {2}", new Object[]{queryID, count, ret.size()});
            ret.clear();
        }
        return ret;
    }
}

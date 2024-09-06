# DataDepPtolemaicFiltering
The objective of this repository is to reproduce the experiments of the "Data-Dependent Ptolemaic Filtering for High-Dimensional Vector Databases" paper.

## Reproducibility Steps

* Clone the three Java-based projects (VMFSMetricSpace, VMGenericTools, VMMetricSpaceTechniquesImpl)
* If you want to also test datasets of image embeddings and not only the artificial vectors (synthetical data), download the folder '[Similarity_search](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search.zip)' (~24 GB) and store it locally on your system
* Change the path in method ```vm.fs.FSGlobal/initRoot()``` (project VMFSMetricSpace) to provide the path to the downloaded 'Similarity_search' folder. If interested only in synthetical data, create somewhere an empty folder Similarity_search and update ```vm.fs.FSGlobal/initRoot()``` to provide it.

### Reproducibility Steps -- Synthetical Data

* Set up the main Java class in ```vm.fs.papers.impl.main.vldb2024.VLDB24DataDepPtolemaicFiltering``` (project VMFSMetricSpace).
* By running the main Java class in ```vm.fs.papers.impl.main.vldb2024.VLDB24DataDepPtolemaicFiltering``` (project VMFSMetricSpace), vectors of uniformly distributed numbers are generated, filtering techniques are learned, the learned techniques are evaluated, and plots are generated from the evaluated results.
* The class produces results that are consistent with the article. pdf files with the plots are stored in the folder Similarity_search/Plots/VLDB24_DataDepPtolemaicFilering/
* Results of the search in .gz format and the csv file with statistics are stored in respective folders in Similarity_search/Result/ ... The statistics are stored in the folder Processed_stats

### Reproducibility Steps -- CLIP / DeCAF Datasets (Filtering of whole datasets in main memory)

* Download the corresponding datasets you would like to evaluate (see the download links below)
* Set up the main Java class ```vm.fs.main.search.perform.FSKNNQueriesSeqScanWithFilteringMain``` (project VMFSMetricSpace) by selecting the datasets to be tested. The instances of datasets tested in the article are: 
  * new FSDatasetInstanceSingularizator.DeCAFDataset()
  * new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset()
* Define these datasets in the main class ```vm.fs.main.search.perform.FSKNNQueriesSeqScanWithFilteringMain``` and run it.

### Reproducibility Steps -- CLIP / Yahoo Datasets (Query Candidates Identified by FAISS)

* If not downloaded yet, download the corresponding datasets and key-value storages from the previous section
* Set up the main Java class ```vm.fs.main.search.perform.FSKNNQueriesSeqScanWithFilteringMain``` (project VMFSMetricSpace) by selecting the datasets to be tested. The instances of datasets tested in the article are: 
  * new FSDatasetInstanceSingularizator.Faiss_Clip_100M_PCA256_Candidates()
  * new FSDatasetInstanceSingularizator.Faiss_DeCAF_100M_Candidates()
* Define these datasets in the main class ```vm.fs.main.search.perform.FSKNNQueriesSeqScanWithFilteringMain``` and run it.
  
## Datasets
* 10M CLIP vectors reduced by PCA to 256-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/laion2B-en-clip768v2-n=10M.h5_PCA256.gz) (~13 GB)
* 102M CLIP vectors reduced by PCA to 256-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/laion2B-en-clip768v2-n=100M.h5_PCA256.gz) (~128 GB)
* 1M DeCAF 4,096-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/decaf_1m.gz) (~7 GB)
* 96M Yahoo 4,096-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/decaf_100m.gz) (~559 GB)

## Key-value Storages
Due to the limited size of main memory, the following key-value storages manage the content (coordinates) of dataset vectors within secondary storage, while IDs of objects are kept in main memory.
* Key-value storage of 102M CLIP vectors reduced by PCA to 256-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/MV_storage/laion2B-en-clip768v2-n=100M.h5_PCA256) (~103 GB)
* Key-value storage of 96M Yahoo 4,096-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/MV_storage/decaf_100m) (~770 GB)

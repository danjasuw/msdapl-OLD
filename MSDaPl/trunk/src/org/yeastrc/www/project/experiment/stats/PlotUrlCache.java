/**
 * PlotUrlCache.java
 * @author Vagisha Sharma
 * Jan 6, 2010
 * @version 1.0
 */
package org.yeastrc.www.project.experiment.stats;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yeastrc.experiment.stats.FileStats;
import org.yeastrc.experiment.stats.PercolatorPsmDeltaMassDistribution;

/**
 * 
 */
public class PlotUrlCache {

    private LinkedHashMap<Key, Value> psmRtPlotUrlStore;
    private LinkedHashMap<Key, Value> spectraRtPlotUrlStore;
    private LinkedHashMap<Key, PercolatorPsmDeltaMassDistribution> psmDeltaMassPlotUrlStore;
    private final int size = 20;
    
    private static PlotUrlCache instance = null;
    
    private PlotUrlCache() {
        int capacity = (int)Math.ceil(size/0.75f) + 1;
        psmRtPlotUrlStore = new LinkedHashMap<Key, Value>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry (Map.Entry<Key, Value> eldest) {
                // This method is invoked by put and putAll after inserting a new entry into the map.
                return psmRtPlotUrlStore.size() > size;  
            }
        };
        
        spectraRtPlotUrlStore = new LinkedHashMap<Key, Value>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry (Map.Entry<Key, Value> eldest) {
                // This method is invoked by put and putAll after inserting a new entry into the map.
                return spectraRtPlotUrlStore.size() > size;  
            }
        };
        
        psmDeltaMassPlotUrlStore = new LinkedHashMap<Key, PercolatorPsmDeltaMassDistribution>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry (Map.Entry<Key, PercolatorPsmDeltaMassDistribution> eldest) {
                // This method is invoked by put and putAll after inserting a new entry into the map.
                return psmDeltaMassPlotUrlStore.size() > size;  
            }
        };
    }
    
    
    public String getPsmRtPlotUrl(int analysisId, double qvalue) {
        Value value = psmRtPlotUrlStore.get(new Key(analysisId, qvalue));
        if(value != null)
            return value.plotUrl;
        else
            return null;
    }
    
    public List<FileStats> getPsmRtFileStats(int analysisId, double qvalue) {
        Value value = psmRtPlotUrlStore.get(new Key(analysisId, qvalue));
        if(value != null)
            return value.fileStats;
        else
            return null;
    }
    
    public String getSpectraRtPlotUrl(int analysisId, double qvalue) {
        Value value = spectraRtPlotUrlStore.get(new Key(analysisId, qvalue));
        if(value != null)
            return value.plotUrl;
        else
            return null;
    }
    
    public List<FileStats> getSpectraRtFileStats(int analysisId, double qvalue) {
        Value value = spectraRtPlotUrlStore.get(new Key(analysisId, qvalue));
        if(value != null)
            return value.fileStats;
        else
            return null;
    }
    
    public void addPsmRtPlotUrl(int analysisId, double qvalue, String url, List<FileStats> fileStats) {
        Key key = new Key(analysisId, qvalue);
        Value value = new Value(url, fileStats);
        psmRtPlotUrlStore.put(key, value);
    }
    
    public void addSpectraRtPlotUrl(int analysisId, double qvalue, String url, List<FileStats> fileStats) {
        Key key = new Key(analysisId, qvalue);
        Value value = new Value(url, fileStats);
        spectraRtPlotUrlStore.put(key, value);
    }
    
    
    public PercolatorPsmDeltaMassDistribution getPsmDeltaMassResult(int analysisId, double qvalue) {
    	PercolatorPsmDeltaMassDistribution value = psmDeltaMassPlotUrlStore.get(new Key(analysisId, qvalue));
        if(value != null)
            return value;
        else
            return null;
    }
    
    public void setPsmDeltaMassResult(int analysisId, double qvalue, PercolatorPsmDeltaMassDistribution result) {
        Key key = new Key(analysisId, qvalue);
        psmDeltaMassPlotUrlStore.put(key, result);
    }
    
    public static PlotUrlCache getInstance() {
        if(instance == null)
            instance = new PlotUrlCache();
        return instance;
    }
    

    private static class Key {
        int analysisId;
        double qvalue;
        
        public Key() {}
        public Key(int analysisId, double qvalue) {
            this.analysisId = analysisId;
            this.qvalue = qvalue;
        }
        
        public boolean equals(Object o) {
            if(!(o instanceof Key))
                return false;
            Key that = (Key)o;
            return (this.analysisId == that.analysisId) && (this.qvalue == that.qvalue);
        }
        
        public int hashCode() {
            String key = analysisId+"_"+qvalue;
            return key.hashCode();
        }
    }
    
    private static class Value {
        
        String plotUrl;
        List<FileStats> fileStats;
        
        public Value() {}
        public Value(String plotUrl, List<FileStats> fileStats) {
            this.plotUrl = plotUrl;
            this.fileStats = fileStats;
        }
        public String getPlotUrl() {
            return plotUrl;
        }
        public void setPlotUrl(String plotUrl) {
            this.plotUrl = plotUrl;
        }
        public List<FileStats> getFileStats() {
            return fileStats;
        }
        public void setFileStats(List<FileStats> fileStats) {
            this.fileStats = fileStats;
        }
    }
}
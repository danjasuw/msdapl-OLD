package edu.uwpr.protinfer.idpicker;

import java.util.List;

import org.apache.log4j.Logger;
import org.yeastrc.ms.dao.DAOFactory;
import org.yeastrc.ms.domain.search.Program;

public class IdPickerInputGetter {

    private static final Logger log = Logger.getLogger(IdPickerInputGetter.class);
    
    private static IdPickerInputGetter instance = new IdPickerInputGetter();
    
    private IdPickerInputGetter() {}
    
    public static final IdPickerInputGetter instance() {
        return instance;
    }
    
    public int getUnfilteredInputCount(int inputId, Program program) {
        if(program == Program.PERCOLATOR) {
            List<Integer> resultIds = DAOFactory.instance().getPercolatorResultDAO().loadResultIdsForRunSearchAnalysis(inputId);
            return resultIds.size();
        }
        else if( Program.isSearchProgram(program)) {
            List<Integer> resultIds = DAOFactory.instance().getMsSearchResultDAO().loadResultIdsForRunSearch(inputId);
            return resultIds.size();
        }
        else {
            log.warn("Don't know how to get unfiltered result count for program : "+program);
            return 0;
        }
    }
    
    public List<PeptideSpectrumMatchNoFDR> getInputNoFdr(int inputId, IDPickerParams params, 
                                            Program program) {
        
        log.info("Reading search/analysis results for inputId: "+inputId+"; Program: "+program.displayName());
        if(program == Program.PERCOLATOR) {
           PercolatorResultsGetter percResGetter = PercolatorResultsGetter.instance();
           return percResGetter.getResultsNoFdr(inputId, params);
        }
        else {
            log.error("Don't know how to get IDPicker input for: "+program);
            return null;
        }
    }
    
    
    public List<PeptideSpectrumMatchIDP> getInput(int inputId, IDPickerParams params,
            Program program) {
        
        log.info("Reading search/analysis results for inputId: "+inputId+"; Program: "+program.displayName());
        
        if (program == Program.SEQUEST || program == Program.EE_NORM_SEQUEST) {
            SequestResultsGetter seqResGetter = SequestResultsGetter.instance();
            return seqResGetter.getResults(inputId, params);
        }
        else if (program == Program.PROLUCID) {
            ProlucidResultsGetter plcidResGetter = ProlucidResultsGetter.instance();
            return plcidResGetter.getResults(inputId, params);
        }
        else {
            log.error("Don't know how to get IDPicker input for: "+program);
            return null;
        }
    }
    
}
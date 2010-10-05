/**
 * PercolatorPsmFilteredResultDAOImpl.java
 * @author Vagisha Sharma
 * Sep 29, 2010
 */
package org.yeastrc.ms.dao.analysis.percolator.ibatis;

import java.util.ArrayList;
import java.util.List;

import org.yeastrc.ms.dao.analysis.MsRunSearchAnalysisDAO;
import org.yeastrc.ms.dao.analysis.percolator.PercolatorFilteredPsmResultDAO;
import org.yeastrc.ms.dao.ibatis.BaseSqlMapDAO;
import org.yeastrc.ms.domain.analysis.percolator.impl.PercolatorBinnedPsmResult;
import org.yeastrc.ms.domain.analysis.percolator.impl.PercolatorFilteredPsmResult;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 */
public class PercolatorFilteredPsmResultDAOImpl extends BaseSqlMapDAO implements
		PercolatorFilteredPsmResultDAO {

	private static final String namespace = "PercolatorFilteredPsmResult";
	private final MsRunSearchAnalysisDAO rsaDao;
	
	public PercolatorFilteredPsmResultDAOImpl(SqlMapClient sqlMap, MsRunSearchAnalysisDAO rsaDao) {
		super(sqlMap);
		this.rsaDao = rsaDao;
	}

	
	@Override
	public double getAverageFilteredPercent() {
		Double avg = (Double)queryForObject(namespace+".averagePercentFiltered", null);
		if(avg == null)
			return 0;
		return avg;
	}

	@Override
	public double getAverageFilteredPercentForInstrument(int instrumentId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getStdevFilteredPercent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getStdevFilteredPercentForInstrument(int instrumentId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<PercolatorFilteredPsmResult> loadForAnalysis(int searchAnalysisId) {
		
		List<Integer> runSearchAnalysisIds = rsaDao.getRunSearchAnalysisIdsForAnalysis(searchAnalysisId);
		List<PercolatorFilteredPsmResult> list = new ArrayList<PercolatorFilteredPsmResult>(runSearchAnalysisIds.size());
		for(Integer runSearchAnalysisId: runSearchAnalysisIds) {
			PercolatorFilteredPsmResult res = this.load(runSearchAnalysisId);
			if(res != null)
				list.add(res);
		}
		
		return list;
	}
	
	@Override
	public PercolatorFilteredPsmResult load(int runSearchAnalysisId) {
		
		return (PercolatorFilteredPsmResult) queryForObject(namespace+".select", runSearchAnalysisId);
	}

	@Override
	public void save(PercolatorFilteredPsmResult result) {
		
		int percPsmResultId = 0;
		try {
			percPsmResultId = saveAndReturnId(namespace+".insert",result);
		
			for(PercolatorBinnedPsmResult binnedResult: result.getBinnedResults()) {
				binnedResult.setPercolatorFilteredPsmId(percPsmResultId);
				save(namespace+".insertBinnedResult",binnedResult);
			}
		}
		catch(RuntimeException e) {
			delete(percPsmResultId);
			throw e;
		}
	}

	@Override
	public void delete(int runSearchAnalysisId) {
		delete(namespace+".delete",runSearchAnalysisId);
	}

}
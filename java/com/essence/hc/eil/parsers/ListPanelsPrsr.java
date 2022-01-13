package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Panel;

public class ListPanelsPrsr implements IParser<List<Panel>> {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	List<PanelPrsr> panelList;
	
	public ListPanelsPrsr() {
	}

	@Override
	public List<Panel> parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
		List<Panel> panels = new ArrayList <Panel>();
		
		try { 
			if(panelList != null)		 
				for(PanelPrsr d: panelList){
					Panel pan = (Panel) d.parse(); 
					panels.add(pan);					
				}
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
			
		return panels;
	}


	public List<PanelPrsr> getPanelList() {
		return panelList;
	}


	public void setPanelList(List<PanelPrsr> deviceList) {
		this.panelList = deviceList;
	}
	
}
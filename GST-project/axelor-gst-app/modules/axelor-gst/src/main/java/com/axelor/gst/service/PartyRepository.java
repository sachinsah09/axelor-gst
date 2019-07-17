package com.axelor.gst.service;

import java.util.Map;

import com.axelor.db.JPA;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.AbstractPartyRepository;
import com.axelor.meta.db.MetaModel;

public class PartyRepository extends AbstractPartyRepository{
	  
	@Override
	  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
	    if (!context.containsKey("json-enhance")) {
	      return json;
	    }
	    try {
	      Long id = (Long) json.get("id");
	      Party party = find(id);
	      json.put("address", party.getAddressList().get(0));
	      System.out.println( party.getAddressList().get(4));
	      json.put("contact",party.getContactList().get(4));
	    } catch (Exception e) {
	    }
	    return json;
	  }	
}

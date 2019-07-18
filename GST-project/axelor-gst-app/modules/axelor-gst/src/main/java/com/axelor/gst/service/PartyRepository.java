package com.axelor.gst.service;

import java.util.Map;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.repo.AbstractPartyRepository;

public class PartyRepository extends AbstractPartyRepository{
	  
	@Override
	  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
	    if (!context.containsKey("json-enhance")) {
	      return json;
	    }
	    try {
	      Long id = (Long) json.get("id");
	      Party party = find(id);
	      Address address=party.getAddressList().get(0);
	      System.out.println( address.getLine1());
	      json.put("address",  address.getLine1()+address.getLine2());	
	      
	      Contact contact=party.getContactList().get(0);
	      json.put("contact",contact.getPrimaryEmail());	
		     
	    } catch (Exception e) {
	    }
	    return json;
	  }	
}

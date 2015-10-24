package pt.tecnico.bubbledocs.sdstore.cli;

import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;

public class MACDictionary {
	Map<DocUserPair, byte[]> dicMAC = new HashMap<DocUserPair, byte[]>(); 

	private static MACDictionary instance = null;
	   protected MACDictionary() {
	      
	   }
	   public static MACDictionary getInstance() {
	      if(instance == null) {
	         instance = new MACDictionary();
	      }
	      return instance;
	   }

	 public void addToDic(DocUserPair docUserPair, byte[] bytesMAC){
		 dicMAC.put(docUserPair, bytesMAC); 
	 }
	 
	 public byte[] getFromDic(DocUserPair docUserPair){
		byte[] key =  dicMAC.get(docUserPair); 
		return key;
	 }
}

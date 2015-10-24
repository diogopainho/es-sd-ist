package pt.tecnico.bubbledocs.sdstore.cli;

import java.security.Key;

import pt.tecnico.bubbledocs.sdstore.security.SymKey;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class SDStoreSecureClient {
	SDStoreClient sdstoreclient;
	Key key;
	MACDictionary dicMAC = MACDictionary.getInstance(); 

	public void setKey(Key key) {
		this.key = key;
	}

	public SDStoreSecureClient(Key clientKey) {
		this.key = clientKey;
		sdstoreclient = new SDStoreClient();
	}
	
	public void createDoc(DocUserPair pair) throws DocAlreadyExists_Exception {
		sdstoreclient.createDoc(pair);
	}

	public byte[] loadSecure(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {

		byte[] docCipherBytes = sdstoreclient.load(docUserPair);
		byte[] newPlainBytes = null;
		byte[] keyMAC = dicMAC.getFromDic(docUserPair);

		try {
			if(SymKey.verifyMAC(keyMAC, docCipherBytes, key)){ 
			newPlainBytes = SymKey.decryptBytesWithKey(key, docCipherBytes).getBytes();
			} else {
				System.out.println("O MAC nao esta correto");
			}
		} catch (Exception e) {
			System.out.println("Nao consequi fazer o decryt do documento"); 
			
		} 

		return newPlainBytes;
	}

	public void storeSecure(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {

		byte[] encriptedBytes = null;
		byte[] bytesMAC = null; 

			try {
				encriptedBytes = SymKey.encryptBytesWithKey(key, contents);
				bytesMAC = SymKey.makeMAC(encriptedBytes, key);
				dicMAC.addToDic(docUserPair, bytesMAC);
			} catch (Exception e) {
				System.out.println("Nao consequi encriptar e assinar os bytes"); 
			}		 

		sdstoreclient.store(docUserPair, encriptedBytes); 
	}

	public SDStore getPort() {
		return sdstoreclient.getClientPort();
	}

}

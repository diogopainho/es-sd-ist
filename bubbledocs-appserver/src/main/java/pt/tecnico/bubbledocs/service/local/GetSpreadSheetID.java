package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class GetSpreadSheetID extends BubbleDocsService{
	private int documentId;
	private String documentName; 
	
	public GetSpreadSheetID(int docId) {
		this.documentId = docId;
	}
	
	public String getSpreadSheetName(){
		return this.documentName;
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		this.documentName = Integer.toString(documentId);
	}

}

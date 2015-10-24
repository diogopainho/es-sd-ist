package pt.tecnico.bubbledocs.sdstore.domain;

public class Document {
	private String docID;
	private int docSize;
	private byte[] content;
	private long cid;
	private int seq;

	public Document(String nameid) {
		this.setDocID(nameid);
		this.setDocSize(0);

		// O identificador do cliente nunca deve ser 0, pelo que "0"zero
		// assinala que o documento ainda nao recebeu nenhuma alteracao por
		// parte de um qualquer utilizador
		cid = 0;
		seq = 0;
	}

	public long getCid() {
		return cid;
	}

	public byte[] getContent() {
		return content;
	}

	public String getDocID() {
		return docID;
	}

	public int getDocSize() {
		return docSize;
	}

	public int getSeq() {
		return seq;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public void setContent(byte[] content) {
		this.content = content;
		this.docSize = content.length;
	}

	public void setDocID(String docid) {
		this.docID = docid;
	}

	public void setDocSize(int newsize) {
		this.docSize = newsize;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}

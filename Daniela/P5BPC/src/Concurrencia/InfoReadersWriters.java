package Concurrencia;

public class InfoReadersWriters {

	private int nr, nw, dr, dw;
	public InfoReadersWriters () {
		nr =0;
		nw=0;
		dr=0;
		dw=0;
	}
	
	public int numReaders() {
		return nr;
	}
	
	public int numWriters() {
		return nw;
	}
	
	public void addReader() {
		nr++;
	}
	
	public void addWriter() {
		nw++;
	}
	
	public void subReader() {
		nr--;
	}
	
	public void subWriter() {
		nw--;
	}
	
	public int numReadersWaiting() {
		return dr;
	}
	
	public int numWritersWaiting() {
		return dw;
	}
	
	public void addReaderWaiting() {
		dr++;
	}
	
	public void addWriterWaiting() {
		dw++;
	}
	
	public void subReaderWaiting() {
		dr++;
	}
	
	public void subWriterWaiting() {
		dw++;
	}
}

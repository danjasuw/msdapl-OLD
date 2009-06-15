package ed.mslib;

import java.io.*;
import ed.javatools.BufferedRaf;
import ed.javatools.PrimitiveTools;
import java.util.zip.*;

public class ReadMS2Comp implements ReadMS2Interface{
	
	public ReadMS2Comp(File file) throws FileNotFoundException, IOException{
		raf = new BufferedRaf(file,"r");
		mzinflater = new Inflater();
		intinflater = new Inflater();		
		readheader();
	}
	
	public MS2Scan getScan(int scannum) throws IOException{
		
		raf.seek(endofheader);
		MS2Scan ms2scan = null;
		while (true){
			ms2scan = readScan(scannum);
			if (ms2scan != null){
				return ms2scan;
			}
			if (lastscan == -1){
				return null;
			}
		}
	}
	
	public MS2Scan getNextScan() throws IOException{

		MS2Scan ms2scan = null;
		ms2scan = readScan(-1);
		if (lastscan == -1){	//if EOF, return null
			return null;
		}
		return ms2scan;		
	}

	private MS2Scan readScan(int scan) throws IOException{

		if (raf.length()-raf.getFilePointer()<56){ //unknown extra bytes at the end of file, so this is to notify that EOF reached
			lastscan = -1;
			return null;
		}
		
		int scan1 = raf.readLEInt();
		lastscan = scan1;

		int scan2 = raf.readLEInt();
		double fragmass = raf.readLEDouble();
		float rtime = raf.readLEFloat();
		float bpi = -1; //base peak intensity
		double bpm = -1;//base peak mass
		double convA = -1; //conversion factor A
		double convB = -1; //conversion factor B
		double tic = -1; //total ion current
		float iit = -1; //ion injection time
		if (version == 2){
			bpi = raf.readLEFloat();
			bpm = raf.readLEDouble();
			convA = raf.readLEDouble();
			convB = raf.readLEDouble();
			tic = raf.readLEDouble();
			iit = raf.readLEFloat();
		}
		int numz = raf.readLEInt();
		int numdatapts = raf.readLEInt();

		int[] chargearray = new int[numz];
		double[] mpharray = new double[numz];
		for (int i=0; i<numz; i++){
			chargearray[i] = raf.readLEInt();
			mpharray[i] = raf.readLEDouble();
		}		
		int mzlength = raf.readLEInt();
		int intlength = raf.readLEInt();
		
		MS2Scan result = null;
		
		if (scan==scan1 || scan == -1){ //if scan found or just want next scan, then read and uncomp data
		
			byte[] mzcomp = new byte[mzlength];
			for (int i=0; i<mzlength; i++){
				mzcomp[i]=(byte)raf.read();
			}
			
			byte[] intcomp = new byte[intlength];
			for (int i=0; i<intlength; i++){
				intcomp[i]=(byte)raf.read();
			}

			result = new MS2Scan();			
			result.setscan(scan1);
			result.setendscan(scan2);
			result.setprecursor((float)fragmass);
			result.addIfield("RTime\t"+rtime);
			
			if (version == 2){
				result.addIfield("BPI\t"+bpi);
				result.addIfield("BPM\t"+bpm);
				result.addIfield("ConvA\t"+convA);
				result.addIfield("ConvB\t"+convB);
				result.addIfield("TIC\t"+tic);
				result.addIfield("IIT\t"+iit);
			}
			
			for (int i=0; i<numz;i++){
				result.addchargemass(chargearray[i],mpharray[i]);
			}
			
			mzinflater.reset();
			intinflater.reset();
			mzinflater.setInput(mzcomp);
			intinflater.setInput(intcomp);
			byte[] mzb = new byte[8];
			byte[] intb = new byte[4];
			
			try {
			
				//read all data at once
				byte[] mzbbig = new byte[8*numdatapts];
				byte[] intbbig = new byte[4*numdatapts];
				mzinflater.inflate(mzbbig);
				intinflater.inflate(intbbig);
				
				//then read data into smaller byte arrays and translate values
				for (int i=0; i<numdatapts; i++){
					for (int j=0; j<8; j++){
						mzb[j] = mzbbig[i*8 + j];
					}
					for (int j=0; j<4; j++){
						intb[j] = intbbig[i*4 + j];
					}
				
					double mz = PrimitiveTools.LEbyteArrayToDouble(mzb);
					float inten = PrimitiveTools.LEbyteArrayToFloat(intb);
					result.addscan(mz,inten);
				}
			
			} catch (DataFormatException e) {
				return null;
			}
			return result;
		}
		else{//skip ahead of data, if not reading
			raf.seek(raf.getFilePointer()+mzlength+intlength);
			return result; //return null
		}
	}
	
	/**
	 * read uncompressed file header
	 * @throws IOException
	 */
	private void readheader() throws IOException{
		
		raf.seek(0);
		filetype = raf.readLEInt();
		version = raf.readLEInt();

		byte[] b = new byte[2048];
		for (int i=0; i<b.length; i++){
			b[i] = (byte)raf.read();
		}
		
		endofheader = raf.getFilePointer();
		header = new String(b);
	}
	
	public String getheader(){return header;}
	public String getfilename(){
		return file.getName();
	}

	public void closeReader(){
		if (raf != null){
			try{
				raf.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private String header;
	private File file;
	BufferedRaf raf;
	private long version;
	private long filetype;
	private Inflater mzinflater;
	private Inflater intinflater;
	private int lastscan=0;
	private long endofheader=0;
	
}
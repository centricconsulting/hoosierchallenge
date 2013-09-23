package util;

//import collection.JavaConversions.*;

import java.io.ByteArrayOutputStream;//{ByteArrayOutputStream, ByteArrayInputStream, OutputStream, InputStream}
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.Section;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.hl7.datatypes.PN;

public class CCDHelper {
	private ClinicalDocument doc;
	
	public CCDHelper(ClinicalDocument document){
		doc = document;
	}
	
	public CCDHelper(InputStream stream) throws Exception{
		this(CDAUtil.load(stream));
	}
	
	public CCDHelper(String str) throws Exception{
		this(CDAUtil.load(new ByteArrayInputStream(str.getBytes("UTF-8"))));
	}
	
	public ClinicalDocument getDocument(){
		return doc;
	}
	
	public String getTitle(){
		 return doc.getTitle().getText();
	}
	
	public String getPatientName(){
		PN name = doc.getRecordTargets().get(0).getPatientRole().getPatient().getNames().get(0);
		return name.getGivens().get(0).getText() + " " + name.getFamilies().get(0).getText();
	}
	
	public EList<Section> findAllSections(){
		return doc.getAllSections();
	}
	
	public Section findSectionByType(String title){
		EList<Section> sections = findAllSections();
		for(Section section: sections){
			if(section.getTitle().getText() == title) return section;
		}
		return null;
	}
	
	public void writeToStream(OutputStream stream) throws Exception{
		CDAUtil.save(doc,  stream);
	}
	
	@Override
	public String toString(){
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		
		try{
			writeToStream(buf);
			return buf.toString();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				buf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}

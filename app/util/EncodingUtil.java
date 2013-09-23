package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class EncodingUtil {
	public String compressText(String text) throws IOException{
		ByteArrayOutputStream str = new ByteArrayOutputStream(text.length());
		GZIPOutputStream gzip = new GZIPOutputStream(str);
		
		try{
			gzip.write(text.getBytes());
			gzip.close();
			return new String(Base64.encodeBase64(str.toByteArray()));
		}
		catch(Exception e){
			// Swallow the exception here
		}
		finally{
			IOUtils.closeQuietly(gzip);
			IOUtils.closeQuietly(str);
		}
		return null;
	}
	
	public String inflateText(String compressed) throws IOException{
		ByteArrayInputStream str = new ByteArrayInputStream(Base64.decodeBase64(compressed.getBytes()));
		GZIPInputStream gunzip = new GZIPInputStream(str);
		try{
			return IOUtils.toString(gunzip);
		}catch(Exception e){
			// Swallow the exception here
		}
		finally{
			IOUtils.closeQuietly(gunzip);
			IOUtils.closeQuietly(str);
		}
		return null;
	}
}


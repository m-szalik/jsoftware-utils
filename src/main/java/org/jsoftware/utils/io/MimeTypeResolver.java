package org.jsoftware.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Resolve mime type by file name or file extension
 * @author szalik
 */
public class MimeTypeResolver implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String defaultMimeType;
	private final Map<String,String> map;

	/**
	 * @param defaultMimeType default mime type for this resolver - can be <code>null</code>
	 */
	public MimeTypeResolver(String defaultMimeType) {
		this.defaultMimeType = defaultMimeType;
		this.map = new HashMap<String, String>();
		load(new File("/etc/mime.types"));
        InputStream ins = null;
        try {
            ins = getClass().getResourceAsStream("/META-INF/mime.types");
		    load(ins);
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (Exception ex2) {
                    /* do nothing */
                }
            }
        }
        if (map.isEmpty()) {
			throw new IllegalStateException("Mime types map is empty!");
		}
	}
	

	/**
	 * Create new instance with default value <tt>null</tt>.
	 * @return an instance of resolver with no default type
	 */
	public static MimeTypeResolver newInstance() {
		return new MimeTypeResolver(null);
	}
	
	/**
	 * Create new instance with default value <code>application/octet-stream</code>.
	 * @return an instance of resolver with default type <code>application/octet-stream</code>
	 */
	public static MimeTypeResolver newInstanceOctetStream() {
		return new MimeTypeResolver("application/octet-stream");
	}
	
	/**
	 * Register new extension for content-type
	 * @param extension extension to register
	 * @param contentType content type assigned with extension
	 */
	public void registerExtension(String extension, String contentType) {
		map.put(extension.toLowerCase(), contentType);
	}
	
	/**
	 * Get content-type by filename extension
	 * @param filename or extension (example: abc.txt or txt)
	 * @return content-type for extension or {@link #defaultMimeType} if mapping not found
	 */
	public String getContenType(String filename) {
		int lastDot = filename.lastIndexOf('.');
		if (lastDot >= 0) {
			filename = filename.substring(lastDot +1);
		}
		String ct = map.get(filename.toLowerCase());
		return ct == null ? defaultMimeType : ct; 
	}
		
	
	private void load(File file) {
		if (file.canRead()) {
            FileInputStream fis = null;
			try {
                fis = new FileInputStream(file);
				load(fis);
			} catch (FileNotFoundException e) {
				Logger.getLogger(getClass().getName()).log(Level.FINER, "Can not load mime types from " + file);
			} finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ex) {
                        /* ignore */
                    }
                }
            }
        }
	}

	private void load(InputStream inputStream) {
		String s;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((s = br.readLine()) != null) {
				s = s.trim();
				if (s.startsWith("#") || s.length() == 0) {
					continue;
				}
				String parts[] = s.split("[ \\t]");
				for(int i=1; i<parts.length; i++) {
					String ext = parts[i];
					if (ext.length() > 0) {
						ext = ext.toLowerCase();
						map.put(ext, parts[0]);
					}
				}
			}
		} catch(IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.FINER, "Can not load mime types from " + inputStream);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ex2) { /* ignore */ } 
			}
		}
	}
	
}

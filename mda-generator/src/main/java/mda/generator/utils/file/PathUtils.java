package mda.generator.utils.file;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import mda.generator.exceptions.MdaGeneratorException;

public class PathUtils {

	/**
	 * Define a Path object for a relative (inside jar) or absolute path provided as a string
	 * @param classPathOrFilePath
	 * @return
	 */
	public static Path getPathForClassPathAndFs(String classPathOrFilePath) {
		try {
			Path p = null;
			
			URL url = Object.class.getResource(classPathOrFilePath);
			if(url != null) {
				URI uri = Object.class.getResource(classPathOrFilePath).toURI();
	
				
				try {		
					p=Paths.get(uri);
				} catch(FileSystemNotFoundException ex) {
					try(FileSystem fs = FileSystems.newFileSystem(uri, Collections.<String,Object>emptyMap())) {
						p = fs.provider().getPath(uri);
					}
				}
			} else {
				p = Paths.get(classPathOrFilePath);
			}
			
			return p.toAbsolutePath();
		}
		catch(Exception e ) {
			throw new MdaGeneratorException("Unable to understand the path " + classPathOrFilePath,e);
		}
	}

	/**
	 * @return Renvoie l'emplacement sur le fs de l'application
	 */
	public static Path getApplicationPath() {

		URI currentUri;
		try {
			currentUri = PathUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI();
		} catch (URISyntaxException e) {
			throw new MdaGeneratorException("Erreur dans la récupération du path",e);
		}
		return Paths.get(currentUri);
	}

}

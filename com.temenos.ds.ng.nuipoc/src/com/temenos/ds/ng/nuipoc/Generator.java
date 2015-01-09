package com.temenos.ds.ng.nuipoc;

import java.util.List;

import nui.Form;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.eson.resource.EFactoryResource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

public class Generator implements IGenerator {

	// TODO move this entire code into a gen. helper in ESON.. (with a good test)

	@Override
	public void doGenerate(Resource input, IFileSystemAccess fsa) {
		URI uri = input.getURI();
		URI relativeInputURI = getRelativePath(uri);
		URI relativeOutputURI = relativeInputURI.trimFileExtension().appendFileExtension("html"); // TODO let the called generator determine the file extension
		String outputFileName = relativeOutputURI.path();
		
		FormHTMLGenerator generator = new FormHTMLGenerator();
		Form form = EFactoryResource.getEFactoryEObject(input, Form.class);
		CharSequence outputText = generator.html(form);
		
		fsa.generateFile(outputFileName, outputText);
	}

	private URI getRelativePath(URI uri) {
		// TODO This.. must exist already somewhere in Xtext, identically?  (check Xtend!)
		if (uri.isPlatformResource()) {
			String iProjectName = uri.segment(1);
			URI iProjectURI = URI.createPlatformResourceURI(iProjectName + "/", true);
			URI projectRelativeURI = uri.deresolve(iProjectURI);
			// TODO don't hard-code that it's only one level of src/, obtain the actual one from IJavaProject.. to chop it off
			List<String> segments = projectRelativeURI.segmentsList();
			URI sourceFolderRelativeURI = URI.createHierarchicalURI(segments.subList(1, segments.size()).toArray(new String[0]), null, null);
			return sourceFolderRelativeURI;
		} else {
			// TODO Huh? If it's e.g. a File URI in the standalone generator scenario, how do we even obtain the project??
			return uri; // This is probably wrong..
		}
		
	}

}
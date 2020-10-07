package io.transpect.calabash.extensions.subversion;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;

import com.xmlcalabash.core.XMLCalabash;
import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.core.XProcConstants;
import com.xmlcalabash.io.WritablePipe;
import com.xmlcalabash.library.DefaultStep;
import com.xmlcalabash.model.RuntimeValue;
import com.xmlcalabash.runtime.XAtomicStep;
import com.xmlcalabash.util.TreeWriter;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNPropertyData;
import org.tmatesoft.svn.core.SVNPropertyValue;

import io.transpect.calabash.extensions.subversion.XSvnConnect;
import io.transpect.calabash.extensions.subversion.XSvnXmlReport;
/**
 * Getting a Property from an SVN repository
 *
 */
public class XSvnPropGetSet extends DefaultStep {
  private WritablePipe result = null;
    
  public XSvnPropGetSet(XProcRuntime runtime, XAtomicStep step) {
    super(runtime,step);
  }
  @Override
  public void setOutput(String port, WritablePipe pipe) {
    result = pipe;
  }
  @Override
  public void reset() {
    result.resetWriter();
  }
  @Override
  public void run() throws SaxonApiException {
    super.run();
    String username = getOption(new QName("username")).getString();
    String password = getOption(new QName("password")).getString();
	System.out.println("pos1");
    String url = getOption(new QName("repo")).getString();
	System.out.println("pos2");
    String sProperties = getOption(new QName("properties")).getString();
	System.out.println("pos3");	
	String[] aProperties = sProperties.split(" ");
	System.out.println("pos4" + new QName("source"));
	RuntimeValue temp = getOption(new QName("source"));
	
	String source;
	if (temp != null){
		source = temp.toString();
	} else {
		source = null;
	}
	
	System.out.println("pos5");
	if (source !=null) {
		System.out.println(source);
	}
    XSvnXmlReport report = new XSvnXmlReport();
    try{
      XSvnConnect connection = new XSvnConnect(url, username, password);
      SVNClientManager clientmngr = connection.getClientManager();
      SVNWCClient propClient = clientmngr.getWCClient();
      SVNURL svnurl = SVNURL.parseURIEncoded( url );
	  
	  String revision = "";
      SVNRevision svnRevision, svnPegRevision;
      boolean allowUnversionedObstructions = false;
      if(revision.trim().isEmpty()){
        svnRevision = svnPegRevision = SVNRevision.HEAD;
      } else {
        svnRevision = svnPegRevision = SVNRevision.parse(revision);
      }            
	  List<SVNPropertyData> propData = new ArrayList<SVNPropertyData>();
	  for (String property: aProperties){
		System.out.println("pos1: " + property);
		propData.add(propClient.doGetProperty(svnurl,property,svnPegRevision,svnRevision));
	  }
      HashMap<String, String> results = new HashMap<String, String>();
	  for (SVNPropertyData data: propData){
		  if (data != null) {
			results.put(data.getName(), data.getValue().toString());
		}
	  }
      XdmNode xmlResult = report.createXmlResult(results, runtime, step);
      result.write(xmlResult);
    } catch(SVNException svne) {
      System.out.println(svne.getMessage());
      XdmNode xmlError = report.createXmlError(svne.getMessage(), runtime, step);
      result.write(xmlError);
    }
  }
};
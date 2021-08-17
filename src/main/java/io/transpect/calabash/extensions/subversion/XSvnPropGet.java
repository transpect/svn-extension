package io.transpect.calabash.extensions.subversion;

import java.io.File;
import java.util.HashMap;

import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;

import com.xmlcalabash.core.XMLCalabash;
import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.io.WritablePipe;
import com.xmlcalabash.library.DefaultStep;
import com.xmlcalabash.runtime.XAtomicStep;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNPropertyData;

import io.transpect.calabash.extensions.subversion.XSvnConnect;
import io.transpect.calabash.extensions.subversion.XSvnXmlReport;
/**
 * Getting a Property from a SVN repository
 *
 */
public class XSvnPropGet extends DefaultStep {
  private WritablePipe result = null;
    
  public XSvnPropGet(XProcRuntime runtime, XAtomicStep step) {
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
    String repo = getOption(new QName("repo")).getString();
    String revision = getOption(new QName("revision")).getString();
    String property = getOption(new QName("property")).getString();
    XSvnXmlReport report = new XSvnXmlReport();
    SVNRevision svnRevision, svnPegRevision;
    SVNPropertyData propData;
    try{
      if(revision.trim().isEmpty()){
        svnRevision = svnPegRevision = SVNRevision.HEAD;
      } else {
        svnRevision = svnPegRevision = SVNRevision.parse(revision);
      }
      XSvnConnect connection = new XSvnConnect(repo, username, password);
      SVNWCClient client = connection.getClientManager().getWCClient();
      if(connection.isRemote()){
        propData = client.doGetProperty(connection.getSVNURL(), property, svnPegRevision, svnRevision);
      } else {
        propData = client.doGetProperty(new File(repo), property, svnPegRevision, svnRevision);
      }
      HashMap<String, String> results = new HashMap<String, String>();
      results.put("property", propData.getName());
      results.put("value", propData.getValue().toString());
      results.put("repo", repo);
      results.put("revision", svnRevision.toString());
      XdmNode xmlResult = report.createXmlResult(results, runtime, step);
      result.write(xmlResult);
    } catch(SVNException svne) {
      System.out.println(svne.getMessage());
      XdmNode xmlError = report.createXmlError(svne.getMessage(), runtime, step);
      result.write(xmlError);
    }
  }
}

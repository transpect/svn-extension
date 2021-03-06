package io.transpect.calabash.extensions.subversion;

import java.io.File;
import java.io.IOException;

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
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import io.transpect.calabash.extensions.subversion.XSvnConnect;
import io.transpect.calabash.extensions.subversion.XSvnXmlReport;
/**
 * This class provides the svn add command as 
 * XML Calabash extension step for XProc. The class  
 * adds one or multiple files in a SVN working copy
 *
 * @see XSvnAdd
 */
public class XSvnAdd extends DefaultStep {
  private WritablePipe result = null;
    
  public XSvnAdd(XProcRuntime runtime, XAtomicStep step) {
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
    String path = getOption(new QName("path")).getString();
    Boolean parents = getOption(new QName("parents")).getString().equals("yes") ? true : false;

    XSvnXmlReport report = new XSvnXmlReport();
    Boolean force = false;
    Boolean addAndMkdir = false;
    Boolean climbUnversionedParents = false;
    Boolean includeIgnored = false;
    String repo = path.split(" ")[0];
    try{
      XSvnConnect connection = new XSvnConnect(repo, username, password);
      SVNClientManager clientmngr = connection.getClientManager();
      String baseURI = connection.isRemote() ? repo : connection.getPath();
      SVNWCClient client = clientmngr.getWCClient();
      String[] paths = path.split(" ");
      for(int i = 0; i < paths.length; i++) {
        File currentPath = new File( paths[i]);
        client.doAdd(currentPath, force, addAndMkdir, climbUnversionedParents, SVNDepth.IMMEDIATES, includeIgnored, parents);
      }
      XdmNode xmlResult = report.createXmlResult(baseURI, "add", paths, runtime, step);
      result.write(xmlResult);
    } catch(SVNException|IOException svne) {
      System.out.println(svne.getMessage());
      XdmNode xmlError = report.createXmlError(svne.getMessage(), runtime, step);
      result.write(xmlError);
    }
  }
}

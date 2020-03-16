package io.transpect.calabash.extensions.subversion;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import io.transpect.calabash.extensions.subversion.XSvnConnect;
import io.transpect.calabash.extensions.subversion.XSvnXmlReport;
/**
 * This class provides the svn delete command as 
 * XML Calabash extension step for XProc. The class 
 * connects to a Subversion remote repository or a
 * working copy and attempts to delete the selected paths.
 *
 * @see XSvnDelete
 */
public class XSvnCopy extends DefaultStep {
  private WritablePipe result = null;
    
  public XSvnCopy(XProcRuntime runtime, XAtomicStep step) {
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
    String url = getOption(new QName("repo")).getString();
    String username = getOption(new QName("username")).getString();
    String password = getOption(new QName("password")).getString();
    String path = getOption(new QName("path")).getString();
    String target = getOption(new QName("target")).getString();
    Boolean move = getOption(new QName("move")).getString().equals("yes") ? true : false;
    String commitMessage = getOption(new QName("message")).getString();
    Boolean mkdir = false;    
    Boolean makeParents = true;
    Boolean failWhenDestExists = false;
    Boolean force = false;
    Boolean climbUnversionedParents = false;
    Boolean includeIgnored = false;
    Boolean keepLocks = false;
    Boolean keepChangelist = false;
    String[] changelists = null;
    XSvnXmlReport report = new XSvnXmlReport();
    SVNProperties svnProps = new SVNProperties();
    try{
      XSvnConnect connection = new XSvnConnect(url, username, password);
      SVNClientManager clientmngr = connection.getClientManager();
      String baseURI = connection.isRemote() ? url : connection.getPath();
      String[] paths = path.split(" ");
      SVNCopySource[] sources = new SVNCopySource[paths.length];
      if( connection.isRemote() ){
        SVNCopyClient copyClient = clientmngr.getCopyClient();
        SVNURL[] sourceURLs = new SVNURL[paths.length];
        SVNURL targetURL = SVNURL.parseURIEncoded( url + "/" + target );
        for( int i = 0; i < paths.length; i++ ) {
          sourceURLs[i] = SVNURL.parseURIEncoded( url + "/" + paths[i] );
          sources[i] = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, sourceURLs[i]);
        }
        SVNCommitInfo commit = copyClient.doCopy(sources, targetURL, move, makeParents, failWhenDestExists, commitMessage, svnProps);
      } else {        
        SVNWCClient client = clientmngr.getWCClient();
        SVNCommitClient commitClient = clientmngr.getCommitClient();
        SVNStatusClient statusClient = clientmngr.getStatusClient();
        File targetPath = new File( url + "/" + target );
        File[] commitPaths = new File[paths.length + 1];
        File[] sourcePaths = new File[paths.length];
        String[] commitFilenames = new String[commitPaths.length];
        for( int i = 0; i < paths.length; i++ ) {
          File sourcePath = new File(url + "/" + paths[i]);
          if( move ) {
            commitPaths[i+1] = sourcePath;
            Files.move(sourcePath.toPath(), targetPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
            client.doDelete(sourcePath, true, false);
          } else {
            Files.copy(sourcePath.toPath(), targetPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
          }
          SVNStatus status = statusClient.doStatus(targetPath, true);
          if( status == null ) {
            client.doAdd(targetPath, force, mkdir, climbUnversionedParents, SVNDepth.IMMEDIATES, includeIgnored, makeParents);
          }
          if( status !=  null
              && ( status.getContentsStatus() == SVNStatusType.STATUS_MODIFIED
                   || status.getContentsStatus() == SVNStatusType.STATUS_ADDED )){
            commitClient.doCommit(commitPaths, keepLocks, commitMessage, svnProps, changelists, keepChangelist, force, SVNDepth.IMMEDIATES);
          }
        }
      }
      XdmNode xmlResult = report.createXmlResult(baseURI, "path", paths, runtime, step);
      result.write(xmlResult);
    } catch( SVNException | IOException svne ) {
      System.out.println(svne.getMessage());
      XdmNode xmlError = report.createXmlError(svne.getMessage(), runtime, step);
      result.write(xmlError);
    }
  }
}

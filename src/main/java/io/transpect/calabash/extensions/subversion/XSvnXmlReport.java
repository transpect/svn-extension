package io.transpect.calabash.extensions.subversion;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.om.AttributeMap;
import net.sf.saxon.om.SingletonAttributeMap;
import net.sf.saxon.om.AttributeInfo;
import net.sf.saxon.om.EmptyAttributeMap;

import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.core.XProcConstants;
import com.xmlcalabash.runtime.XAtomicStep;
import com.xmlcalabash.library.DefaultStep;
import com.xmlcalabash.model.RuntimeValue;
import com.xmlcalabash.util.TreeWriter;
import com.xmlcalabash.util.TypeUtils;


import org.tmatesoft.svn.core.SVNDirEntry;

/**
 * Returns XML-based reports or errors as result for
 * further processing in XProc pipelines
 *
 */
public class XSvnXmlReport {
  /**
   * Render a HashMap as XML (c:param-set)
   */
  public XdmNode createXmlResult(HashMap<String, String> results, XProcRuntime runtime, XAtomicStep step){
    TreeWriter tree = new TreeWriter(runtime);
    tree.startDocument(step.getNode().getBaseURI());
    tree.addStartElement(XProcConstants.c_param_set);
    for(String key:results.keySet()) {
      AttributeMap attr = EmptyAttributeMap.getInstance();
      attr = attr.put(TypeUtils.attributeInfo(new QName("name"), key));
      attr = attr.put(TypeUtils.attributeInfo(new QName("value"), results.get(key)));
      tree.addStartElement(XProcConstants.c_param, attr);
      tree.addEndElement();
    }
    tree.addEndElement();
    tree.endDocument();
    return tree.getResult();
  }
  /**
   * Render a String array as XML (c:param-set)
   */
  public XdmNode createXmlResult(String baseURI, String type, String[] results, XProcRuntime runtime, XAtomicStep step){
    TreeWriter tree = new TreeWriter(runtime);
    tree.startDocument(step.getNode().getBaseURI());
    tree.addStartElement(XProcConstants.c_param_set, SingletonAttributeMap.of(TypeUtils.attributeInfo(XProcConstants.xml_base, baseURI)));
    for(int i = 0; i < results.length; i++){
      AttributeMap attr = EmptyAttributeMap.getInstance();
      attr = attr.put(TypeUtils.attributeInfo(new QName("name"), type));
      attr = attr.put(TypeUtils.attributeInfo(new QName("value"), results[i]));      
      tree.addStartElement(XProcConstants.c_param, attr);
      tree.addEndElement();
    }
    tree.addEndElement();
    tree.endDocument();
    return tree.getResult();
  }
  /**
   * Render errors as XML c:errors
   */
  public XdmNode createXmlError(String message, XProcRuntime runtime, XAtomicStep step){
    TreeWriter tree = new TreeWriter(runtime);
    tree.startDocument(step.getNode().getBaseURI());
    tree.addStartElement(XProcConstants.c_errors, SingletonAttributeMap.of(TypeUtils.attributeInfo(new QName("code"), "svn-error")));
    tree.addStartElement(XProcConstants.c_error,  SingletonAttributeMap.of(TypeUtils.attributeInfo(new QName("code"), "error")));
    tree.addText(message);
    tree.addEndElement();
    tree.addEndElement();
    tree.endDocument();
    return tree.getResult();
  }
}

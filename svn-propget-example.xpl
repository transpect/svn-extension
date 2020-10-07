<p:declare-step 
  xmlns:p="http://www.w3.org/ns/xproc" 
  xmlns:c="http://www.w3.org/ns/xproc-step"
  xmlns:tr="http://transpect.io"
  xmlns:svn="http://transpect.io/svn" 
  name="pipeline" 
  version="1.0">

  <p:option name="username" select="''"/>
  <p:option name="password" select="''"/>
  <p:option name="repo" select="'https://subversion.le-tex.de/common/pdf2fxl'"/>
  <p:option name="propname" select="'propname1'"/>
    
  <p:output port="result" sequence="true"/>

  <p:import href="svn-propget-declaration.xpl"/>

  <svn:propget name="svn-propget">
    <p:with-option name="username"  select="$username"/>
    <p:with-option name="password"  select="$password"/>
    <p:with-option name="repo"      select="$repo"/>
    <p:with-option name="propname"  select="$propname"/>
  </svn:propget>

</p:declare-step>
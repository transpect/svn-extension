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
    
  <p:output port="result" sequence="true"/>

  <p:import href="svn-propset-declaration.xpl"/>

  <svn:propset name="svn-propset">
    <p:input port="source"><!-- sequence of property documents -->
      <p:inline>
        <svn:property name="svn:externals">
          tei2hub -r 6 https://github.com/transpect/tei2hub/trunk
          hub2html -r 12 https://github.com/transpect/hub2html/trunk
        </svn:property>
      </p:inline>
      <p:inline>
        <svn:property name="svn:externals2">
          tei2hub -r 6 https://github.com/transpect/tei2hub/trunk
          hub2html -r 12 https://github.com/transpect/hub2html/trunk
        </svn:property>
      </p:inline>
    </p:input>
    <p:with-option name="username"   select="$username"/><!-- optional -->
    <p:with-option name="password"   select="$password"/><!-- optional -->
    <p:with-option name="repo"       select="$repo"/>    <!-- required -->
  </svn:propset>

</p:declare-step>
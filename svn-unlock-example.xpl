<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" 
  xmlns:c="http://www.w3.org/ns/xproc-step"
  xmlns:tr="http://transpect.io"
  xmlns:svn="http://transpect.io/svn" 
  name="unlock-example"
  version="1.0">

  <p:option name="repo" select="'repo'"/>
  <p:option name="username" select="'user'"/>
  <p:option name="password" select="'pass'"/>
  <p:option name="path" select="'MyFile.xml'"/>
  <p:option name="break-lock" select="'no'"/>  
  <p:option name="message" select="'file unlocked'"/>
  
  <p:output port="result" sequence="true"/>

  <p:import href="svn-unlock-declaration.xpl"/>

  <svn:unlock name="svn-unlock">
    <p:with-option name="repo"       select="$repo"/>
    <p:with-option name="username"   select="$username"/>
    <p:with-option name="password"   select="$password"/>
    <p:with-option name="path"       select="$path"/>
    <p:with-option name="break-lock" select="$break-lock"/>
    <p:with-option name="message"    select="$message"/>
  </svn:unlock>

</p:declare-step>

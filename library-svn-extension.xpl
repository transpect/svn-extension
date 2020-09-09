<?xml version="1.0"?>
<p:library xmlns:p="http://www.w3.org/ns/xproc"
  xmlns:svn="http://transpect.io/svn" 
  version="1.0">
  
  <p:documentation xmlns="http://www.w3.org/1999/xhtml">
    <div>
      <h1>Library for svn steps</h1>
      <h2>Version 1.0</h2>
      <p>The steps defined in this library are implemented in
        <a href="https://github.com/transpect/svn-extension">https://github.com/transpect/svn-extension</a>.
      </p>
    </div>
  </p:documentation>
  
  <p:declare-step type="svn:add">
    <p:output port="result" sequence="true"/>
    <p:option name="username"/>
    <p:option name="password"/>
    <p:option name="path"/>
    <p:option name="parents" select="'no'"/>
  </p:declare-step>
  
  <p:declare-step type="svn:checkout">
    <p:output port="result" sequence="true"/>
    <p:option name="username"/>
    <p:option name="password"/>
    <p:option name="repo"/>
    <p:option name="revision"/>
    <p:option name="path"/>
  </p:declare-step>
  
  <p:declare-step type="svn:commit">
    <p:output port="result" sequence="true"/>
    <p:option name="username"/>
    <p:option name="password"/>
    <p:option name="path"/>
    <p:option name="message" select="'svn:commit'"/>
  </p:declare-step>
  
  <p:declare-step type="svn:copy">
    <p:output port="result" sequence="true"/>
    <p:option name="repo"/>
    <p:option name="username"/>
    <p:option name="password"/>
    <p:option name="path"/>
    <p:option name="target"/>
    <p:option name="move" select="'no'"/>
    <p:option name="message" select="'svn:delete removed path'"/>
  </p:declare-step>
  
  <p:declare-step type="svn:delete">
    <p:output port="result" sequence="true"/>
    <p:option name="url"/>
    <p:option name="username"/>
    <p:option name="password"/>
    <p:option name="force" select="'no'"/>
    <p:option name="message" select="'svn:delete removed path'"/>
  </p:declare-step>
  
  <p:declare-step type="svn:info">
    <p:output port="result" sequence="true"/>
    <p:option name="repo"/>
    <p:option name="username"/>
    <p:option name="password"/>
  </p:declare-step>
  
  <p:declare-step type="svn:list">
    <p:output port="result" sequence="true"/>
    <p:option name="repo"/>
    <p:option name="username"/>
    <p:option name="password"/>
    <p:option name="recursive"/>
  </p:declare-step>
  
  
  <p:declare-step type="svn:lock">
    <p:output port="result" sequence="true"/>
    <p:option name="repo"/>
    <p:option name="username"/>
    <p:option name="password"/>
    <p:option name="path"/>
    <p:option name="unlock" select="'no'"/>
    <p:option name="break-lock" select="'no'"/>
    <p:option name="message" select="''"/>
  </p:declare-step>
  
  <p:declare-step type="svn:mkdir">
    <p:output port="result" sequence="true"/>
    <p:option name="username"/>
    <p:option name="password"/>
    <p:option name="dir"/>
    <p:option name="parents" select="'no'"/>
    <p:option name="message" select="'svn:mkdir create dir'"/>
  </p:declare-step>
  
  <p:declare-step type="svn:move">
    <p:output port="result" sequence="true"/>
    <p:option name="repo" select="'https://subversion.le-tex.de/common/'"/>
    <p:option name="username" select="'user'"/>
    <p:option name="password" select="'pass'"/>
    <p:option name="path"     select="'ToBeCopied'"/>
    <p:option name="target"     select="'Output'"/>  
    <p:option name="message" select="'my commit message'"/>
    
    <p:import href="svn-copy-declaration.xpl"/>
    
    <svn:copy name="svn-copy">
      <p:with-option name="repo"     select="$repo"/>
      <p:with-option name="username" select="$username"/>
      <p:with-option name="password" select="$password"/>
      <p:with-option name="path"     select="$path"/>
      <p:with-option name="target"   select="$target"/>
      <p:with-option name="move"     select="'yes'"/>
      <p:with-option name="message"  select="$message"/>
    </svn:copy>
  </p:declare-step>
  
  <p:declare-step name="svn-unlock" type="svn:unlock">
    <p:output port="result" sequence="true"/>
    
    <p:option name="repo" select="'repo'"/>
    <p:option name="username" select="'user'"/>
    <p:option name="password" select="'pass'"/>
    <p:option name="path" select="'MyFile.xml'"/>
    <p:option name="break-lock" select="'no'"/>
    <p:option name="message" select="'file unlocked'"/>
    
    <p:import href="svn-lock-declaration.xpl"/>
    
    <svn:lock name="svn-lock">
      <p:with-option name="repo"       select="$repo"/>
      <p:with-option name="username"   select="$username"/>
      <p:with-option name="password"   select="$password"/>
      <p:with-option name="path"       select="$path"/>
      <p:with-option name="unlock"     select="'yes'"/>
      <p:with-option name="break-lock" select="'no'"/>
      <p:with-option name="message"    select="$message"/>
    </svn:lock>
  </p:declare-step>
  
  <p:declare-step type="svn:update">
    <p:output port="result" sequence="true"/>
    <p:option name="username"/>
    <p:option name="password"/>
    <p:option name="path"/>
    <p:option name="revision" required="false"/>
  </p:declare-step>
  
</p:library>

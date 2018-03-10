# svn-extension
XML Calabash extensions steps to implement Subversion

## svn:info

Provides general information about a repository as `c:param-set`.

```xml
<svn:info name="svn-info" xmlns:svn="http://transpect.io/svn">
  <p:with-option name="username" select="'user'"/>
  <p:with-option name="password" select="'pass'"/>
  <p:with-option name="href"     select="'https://subversion.le-tex.de/common/'"/>
</svn:info>
```

This is the expected output:

```xml
<c:param-set xmlns:c="http://www.w3.org/ns/xproc-step">
   <c:param name="date" value="Thu Mar 08 21:14:24 CET 2018"/>
   <c:param name="path" value=""/>
   <c:param name="rev" value="6610"/>
   <c:param name="author" value="mkraetke"/>
   <c:param name="root-url" value="https://subversion.le-tex.de/common"/>
   <c:param name="uuid" value="d851d441-0421-4803-b127-45cb279a3ef2"/>
   <c:param name="nodekind" value="dir"/>
   <c:param name="url" value="https://subversion.le-tex.de/common"/>
</c:param-set>
```

## svn:mkdir

Creates a directory in a remote repository or working copy like `svn mkdir`.
The dir option expects a single path or a whitespace-separated list of paths.

```xml
<svn:mkdir name="svn-mkdir">
  <p:with-option name="username" select="'user'"/>
  <p:with-option name="password" select="'parents'"/>
  <p:with-option name="href"     select="'https://subversion.le-tex.de/common'"/>
  <p:with-option name="dir"      select="'my-new-dir'"/>
  <p:with-option name="parents"  select="'yes'"/>
  <p:with-option name="message"  select="'my commit message'"/>
</svn:mkdir>
```
This leads to this output:

```xml
<c:param-set xmlns:c="http://www.w3.org/ns/xproc-step"
             xml:base="https://subversion.le-tex.de/common">
   <c:param name="dir" value="my-new-dir"/>
</c:param-set>
```
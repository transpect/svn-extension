# svn-extension
XML Calabash extensions steps to implement Subversion with [SVNKit](https://svnkit.com).

## svn:info

Provides general information about a repository as `c:param-set`.

```xml
<svn:info name="svn-info" xmlns:svn="http://transpect.io/svn">
  <p:with-option name="username" select="'user'"/>
  <p:with-option name="password" select="'pass'"/>
  <p:with-option name="repo"     select="'https://subversion.le-tex.de/common/'"/>
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
  <c:param name="nodekind" value="mkdir"/>
  <c:param name="url" value="https://subversion.le-tex.de/common"/>
</c:param-set>
```

## svn:mkdir

Creates a directory in a remote repository or working copy like `svn mkdir`.
The `dir` option expects a single path or a whitespace-separated list of paths.

```xml
<svn:mkdir name="svn-mkdir">
  <p:with-option name="username" select="'user'"/>
  <p:with-option name="password" select="'parents'"/>
  <p:with-option name="repo"     select="'https://subversion.le-tex.de/common'"/>
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

## svn:delete

Deletes one or multiple paths in a working directory or remote repository. The `path`
option expects a whitespace-separated list of arguments.

```xml
<svn:delete name="svn-delete">
  <p:with-option name="username" select="'user'"/>
  <p:with-option name="password" select="'pass'"/>
  <p:with-option name="repo"     select="'https://subversion.le-tex.de/common'"/>
  <p:with-option name="path"     select="'path-to/file-to-be-deleted.xml'"/>
  <p:with-option name="force"    select="'no'"/>
  <p:with-option name="message"  select="'my commit message'"/>
</svn:delete>
```

On success, this report is generated:

```xml
<c:param-set xmlns:c="http://www.w3.org/ns/xproc-step"
             xml:base="https://subversion.le-tex.de/common">
  <c:param name="delete" value="path-to/file-to-be-deleted.xml"/>
</c:param-set>
```

## svn:add

Add files to a working copy just like `svn add`.

```xml
<svn:add name="svn-add">
  <p:with-option name="username" select="'user'"/>
  <p:with-option name="password" select="'pass'"/>
  <p:with-option name="repo"     select="'path-to-local-working-copy'"/>
  <p:with-option name="path"     select="'MyFile.xml'"/>
  <p:with-option name="parents"  select="'no'"/>
</svn:add>
```

A sample output of `svn:add` is shown below:

```xml
<c:param-set xmlns:c="http://www.w3.org/ns/xproc-step"
             xml:base="file:/home/Martin/path-to-local-working-copy">
  <c:param name="add" value="MyFile.txt"/>
</c:param-set>
```

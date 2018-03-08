# svn-extension
XML Calabash extensions steps to implement Subversion

# svn:info

Provides general information about a repository as `c:param-set`.

```
<svn:info name="svn-info" xmlns:svn="http://transpect.io/svn">
  <p:with-option name="username" select="'user'"/>
  <p:with-option name="password" select="'pass'"/>
  <p:with-option name="href"     select="'https://subversion.le-tex.de/common/'"/>
</svn:info>
```
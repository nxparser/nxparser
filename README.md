# Welcome to NxParser #

NxParser is a Java open source, streaming, non-validating parser for the Nx format, where x = Triples, Quads, or any other number. For more details see the specification for the [NQuads format](http://sw.deri.org/2008/07/n-quads/), a extension for the [N-Triples](http://www.w3.org/TR/rdf-testcases/#ntriples) RDF format. Note that the parser handles any combination or number of N-Triples syntax terms on each line (the number of terms per line can also vary).

It ate 2 mil. quads (~4GB, (~240MB GZIPped)) on a T60p (Win7, 2.16 GHz)  in ~1 min 35 s (1:18min). Overall, it's more than twice as fast as the previous version when it comes to reading Nx.

Compiles are available in a maven repository. For the time being, it is hosted at Google Code. We plan to go to Maven Central soon. To use the repository, add
```xml
<repository>
 <id>nxparser-repo</id>
 <url>
  http://nxparser.googlecode.com/svn/repository
 </url>
</repository>
<repository>
 <id>nxparser-snapshots</id>
 <url>
  http://nxparser.googlecode.com/svn/snapshots
 </url>
</repository>
```
to your pom.xml. The groupId is `org.semanticweb.yars` and the artifactId `nxparser`.

The NxParser is non-validating, meaning that, e.g., it will happily eat non-conformant N-Triples. Also, the NxParser will not parse certain valid N-Triples files where the RDF terms are not separated by whitespace. We pass all positive W3C N-Triples test cases except one, where the RDF terms are not separated by whitespace (surprise!).

## Code Examples ##
### Read Nx from a file ###
```java
FileInputStream is = new FileInputStream("path/to/file.nq");

NxParser nxp = new NxParser(is);

for (Node[] nx : nxp)
  // prints the subject, eg. <http://example.org/>
  System.out.println(nx[0]);
```

### Use a blank node ###
```java
// true means you are supplying proper N-Triples RDF terms that do not need to be processed
Resource subjRes = new Resource("<http://example.org/123>", true);
Resource predRes = new Resource("<http://example.org/123>", true);
BNode bn = new BNode("_:bnodeId", true);

Node[] triple = new Node[]{subjRes, predRes, bn};
// yields <http://example.org/123> <http://example.org/123> _:bnodeId
System.out.println(Arrays.toString(triple));
```

### Use Unicode-characters ###
```java
String japaneseString = ("祝福は、チーズのメーカーです。");
Literal japaneseLiteral = new Literal(japaneseString, "ja");

// yields "\u795D\u798F\u306F\u3001\u30C1\u30FC\u30BA\u306E\u30E1\u30FC\u30AB\u30FC\u3067\u3059\u3002"@ja
System.out.println(japaneseLiteral);

// yields 祝福は、チーズのメーカーです。
System.out.println(japaneseLiteral.getLabel());
```

### Use datatyped literals ###
Example: Get a Calendar object from an `xsd:dateTime`-typed Literal
```java
Literal dtl; // parser-generated
XSDDateTime dt = (XSDDateTime)DatatypeFactory.getDatatype(dtl); 
GregorianCalendar cal = dt.getValue();
```

### Use from Python ###
Provided you use the Jython implementation (thanks to Uldis Bojars, this is saved from his now offline blog).

```python
import sys
sys.path.append("./nxparser.jar")
	 
from org.semanticweb.yars.nx.parser import *
from java.io import FileInputStream
from java.util.zip import GZIPInputStream
	 
def all_triples(fname, use_gzip=False):
  in_file = FileInputStream(fname)
  if use_gzip:
      in_file = GZIPInputStream(in_file)
	 
  nxp = NxParser()
  nxp.parse(in_file)
	 
  while nxp.hasNext():
    triple = nxp.next()
    n3 = ([i.toString() for i in triple])
    yield n3
```
The code above defines a generator function which will yield a stream of NQuad records. We can now add some demo code in order to see it in action:
```python
def main():
  gzfname = "sioc-btc-2009.gz"
 
  for line in all_triples(gzfname, use_gzip=True):
    print line
	 
  if __name__ == "__main__":
    main()
```
results in:
```python
[u'<http://2008.blogtalk.net/node/29>', u'<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>', u'<http://rdfs.org/sioc/ns#Post>', u'<http://2008.blogtalk.net/sioc/node/29>']
[u'<http://2008.blogtalk.net/node/65>', u'<http://rdfs.org/sioc/ns#content>', u'"We\'ve created a map showing the main places of interest (event locations, restaurants, pubs, shopping locations and tourist sights) during BlogTalk 2008.  The conference venue is shown on the left-hand side of the map.  We will also have a hardcopy for all attendees. View Larger Map"', u'<http://2008.blogtalk.net/sioc/node/65>']
```
	

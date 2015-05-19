# Welcome to NxParser #

NxParser is a Java open source, streaming, non-validating parser for the Nx format, where x = Triples, Quads, or any other number. For more details see the specification for the [NQuads format](http://sw.deri.org/2008/07/n-quads/), a extension for the [N-Triples](http://www.w3.org/TR/rdf-testcases/#ntriples) RDF format. Note that the parser handles any combination or number of N-Triples syntax terms on each line (the number of terms per line can also vary).

It ate 2 mil. quads (~4GB, (~240MB GZIPped)) on a T60p (Win7, 2.16 GHz)  in ~1 min 35 s (1:18min). Overall, it's more than twice as fast as the previous version when it comes to reading Nx.

It comes in two versions: lite and not-so-lite. The latter is provided "as-is" and comes with a whole bunch of stuff you probably won't need, though there's some code for batch sorting large-files and various other utilities, which some may find useful. If you just want to parse Nx into memory, go for the lite version.

Due to [Google's change to Google code](http://google-opensource.blogspot.de/2013/05/a-change-to-google-code-download-service.html), the downloads page cannot be maintained any more, so you have to browse the repository for both code and jars. Note that you can use maven with the google code repository. The groupId is `org.semanticweb.yars` and the artifactId `nxparser`.

The NxParser is non-validating, meaning that, e.g., it will happily eat non-conformant N-Triples. Also, the NxParser will not parse certain valid N-Triples files where the RDF terms are not separated by whitespace. See [here](wiki/SpecDeviations) for more info.

For example code on how to use the classes see [here](wiki/CodeExamples).


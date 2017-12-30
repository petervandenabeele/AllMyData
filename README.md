# AllMyData: Event Sourcing All My Data (to never forget it)

This is an Event Sourcing implementation in Scala, based on the
original [Dbd][Dbd] ideas (which was naively reinventing the Event
Sourcing concepts and using them for better storage and distributed
caching of [RDF triples][RDF] to provide better handling of Linked
Data ...).

[Why?][Rationale]

[Design][Dbd]

Licensed under the [MIT License][MIT].

## Building

To build a fat jar using the sbt-assembly plug-in, run:

```
 sbt assembly
```

## Usage

A first file with InFacts can be processed e.g. with:

```
# the metadata for production code should be outside the git repo
mkdir ../metadata

# copy an example of valid_predicates to the metadata dir
cp src/test/resources/predicates/valid_predicates.csv ../metadata

# The InFactsReader takes only 1 file as argument (no schema etc)
java -cp target/scala-2.12/AllMyData-assembly-0.0.3.jar cli.InFactsReader AllMyData/src/test/resources/in_fact_csv/two_in_facts_with_csv_reference.csv
```

[RDF]:          http://www.w3.org/RDF/
[Rationale]:    http://github.com/petervandenabeele/dbd/blob/master/docs/rationale.md
[MIT]:          https://github.com/petervandenabeele/AllMyData/blob/master/LICENSE.txt
[Dbd]:          https://github.com/petervandenabeele/dbd#readme

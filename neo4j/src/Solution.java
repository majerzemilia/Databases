import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution{

    private final GraphDatabase graphDb = new GraphDatabase();
    private enum RelTypes implements RelationshipType{
        ACTED_IN
    }

    private void findDirectorsWhereTitleLike(final String movieName){
        Map<String, Object> params = new HashMap();
        params.put("name", movieName);
        String query = "MATCH (p:Person)-[:DIRECTED]->(m:Movie) WHERE m.title CONTAINS $name RETURN p.name";
        System.out.println(graphDb.executeSql(query, params));
    }

    private void addMovieAndActors(){
        List<String> queries = new ArrayList<>();
        String query = "CREATE (p:Person) SET p.name = \"Tomasz Karolak\"";
        queries.add(query);
        query = "CREATE (p:Person) SET p.name = \"Agnieszka Dygant\"";
        queries.add(query);
        query = "CREATE (m:Movie) SET m.title = \"Listy do M\"";
        queries.add(query);
        query = "MATCH (m:Movie{title: \"Listy do M\"}), (p:Person{name: \"Tomasz Karolak\"}) CREATE (m)<-[:ACTED_IN]-(p)";
        queries.add(query);
        query = "MATCH (m:Movie{title: \"Listy do M\"}), (p:Person{name: \"Agnieszka Dygant\"}) CREATE (m)<-[:ACTED_IN]-(p)";
        queries.add(query);
        for(String sql: queries) System.out.println(graphDb.executeSql(sql, null));
    }

    private void setBirtToNewlyAddedActors(){
        List<String> queries = new ArrayList<>();
        String query = "MATCH (p:Person{name: \"Agnieszka Dygant\"}) " +
                "SET p.birthdate = \"27.03.1973\", p.birthplace = \"Piaseczno\" RETURN p";
        queries.add(query);
        query = "MATCH (p:Person{name: \"Tomasz Karolak\"}) " +
                "SET p.birthdate = \"21.06.1971\", p.birthplace = \"Radom\" RETURN p";
        queries.add(query);
        for(String sql: queries) System.out.println(graphDb.executeSql(sql, null));
    }

    private void changeBirthplace(int birthdate, String birthplace){
        Map<String, Object> params = new HashMap();
        params.put("birthdate", birthdate);
        params.put("birthplace", birthplace);
        String query = "MATCH (p:Person) WHERE p.born < $birthdate SET p.birthplace = $birthplace RETURN p";
        System.out.println(graphDb.executeSql(query, params));
    }

    private void findActorsWithMoreThanTwoMovies(){
        String query = "MATCH (a:Person)-[:ACTED_IN]->(m:Movie) " +
                "WITH a, collect(m.title) as movies " +
                "WHERE length(movies) >= 2 " +
                "RETURN a.name";
        System.out.println(graphDb.executeSql(query, null));
    }

    private void averageAppearanceInMovies(){
        String query = "MATCH (a:Person)-[:ACTED_IN]->(m:Movie) " +
                "WITH a, collect(m.title) as movies " +
                "WHERE length(movies) >= 3 " +
                "RETURN avg(length(movies))";
        System.out.println(graphDb.executeSql(query, null));
    }

    private void changeAttributeBetweenActors(String firstActor, String secondActor){
        Map<String, Object> params = new HashMap();
        params.put("firstActor", firstActor);
        params.put("secondActor", secondActor);
        String query = "MATCH path=shortestpath((p:Person{name: $firstActor})-[:ACTED_IN*]-(r:Person{name: $secondActor})) " +
                "FOREACH (n in filter( p in nodes(path) WHERE NOT \"Movie\" IN LABELS(p))| SET n.born = 2019) " +
                "RETURN nodes(path)";
        System.out.println(graphDb.executeSql(query, params));
    }

    private void showSecondNodesInPaths(String firstActor, String secondActor){
        Map<String, Object> params = new HashMap();
        params.put("firstActor", firstActor);
        params.put("secondActor", secondActor);
        String query = "MATCH path = (p:Person{name: $firstActor})-[*4]-(r:Person{name: $secondActor}) " +
                "RETURN nodes(path)[1]";
        System.out.println(graphDb.executeSql(query, params));
    }

    private void findActorIndexTest(){
        String query = "MATCH (p:Person {name: \"Meg Ryan\"}) RETURN p";
        long startTime, stopTime, indexTime, noIndexTime;
        graphDb.executeSql(query, null); //first execution for cypher compiler which caches exec plan for queries

        String indexQuery = "CREATE INDEX ON :Person(name)";
        System.out.println(graphDb.executeSql(indexQuery, null));

        startTime = System.nanoTime();
        graphDb.executeSql(query, null);
        stopTime = System.nanoTime();
        indexTime = stopTime - startTime;
        System.out.println("Searching time with index in us: " + indexTime/1000);

        indexQuery = "DROP INDEX ON :Person(name)";
        System.out.println(graphDb.executeSql(indexQuery, null));

        startTime = System.nanoTime();
        graphDb.executeSql(query, null);
        stopTime = System.nanoTime();
        noIndexTime = stopTime - startTime;
        System.out.println("Searching time without index in us: " + noIndexTime/1000);

        System.out.println("Time difference in us: " + (noIndexTime - indexTime)/1000);
    }

    private void optimizeFirstQuery(String movieName){
        Map<String, Object> params = new HashMap();
        params.put("name", movieName);
        String query = "MATCH (p:Person)-[:DIRECTED]->(m:Movie) WHERE m.title CONTAINS $name RETURN p.name";
        long startTime, stopTime, indexTime, noIndexTime;
        graphDb.executeSql(query, params); //first execution for cypher compiler which caches exec plan for queries

        String indexQuery = "CREATE INDEX ON :Movie(title)";
        System.out.println(graphDb.executeSql(indexQuery, null));

        startTime = System.nanoTime();
        graphDb.executeSql(query, params);
        stopTime = System.nanoTime();
        indexTime = stopTime - startTime;
        System.out.println("Searching time with index in us: " + indexTime/1000);

        indexQuery = "DROP INDEX ON :Movie(title)";
        System.out.println(graphDb.executeSql(indexQuery, null));

        startTime = System.nanoTime();
        graphDb.executeSql(query, params);
        stopTime = System.nanoTime();
        noIndexTime = stopTime - startTime;
        System.out.println("Searching time without index in us: " + noIndexTime/1000);

        System.out.println("Time difference in us: " + (noIndexTime - indexTime)/1000);

    }

    private void optimizeSecondQuery(String firstActor, String secondActor){
        Map<String, Object> params = new HashMap();
        params.put("firstActor", firstActor);
        params.put("secondActor", secondActor);
        String query = "MATCH path = (p:Person{name: $firstActor})-[*4]-(r:Person{name: $secondActor}) " +
                "RETURN nodes(path)[1]";
        long startTime, stopTime, indexTime, noIndexTime;
        graphDb.executeSql(query, params); //first execution for cypher compiler which caches exec plan for queries

        String indexQuery = "CREATE INDEX ON :Person(name)";
        System.out.println(graphDb.executeSql(indexQuery, null));

        startTime = System.nanoTime();
        graphDb.executeSql(query, params);
        stopTime = System.nanoTime();
        indexTime = stopTime - startTime;
        System.out.println("Searching time with index in us: " + indexTime/1000);

        indexQuery = "DROP INDEX ON :Person(name)";
        System.out.println(graphDb.executeSql(indexQuery, null));

        startTime = System.nanoTime();
        graphDb.executeSql(query, params);
        stopTime = System.nanoTime();
        noIndexTime = stopTime - startTime;
        System.out.println("Searching time without index in us: " + noIndexTime/1000);

        System.out.println("Time difference in us: " + (noIndexTime - indexTime)/1000);
    }


    public static void main(String[] args){
        Solution s = new Solution();
        //s.findDirectorsWhereTitleLike("Stand By Me");
        //s.addMovieAndActors();
        //s.setBirtToNewlyAddedActors();
        //s.changeBirthplace(1940, "Kraków");
        //s.findActorsWithMoreThanTwoMovies();
        //s.averageAppearanceInMovies();
        //s.changeAttributeBetweenActors("Jack Nicholson", "Al Pacino");
        //s.showSecondNodesInPaths("Jack Nicholson", "Kevin Bacon");
        //s.findActorIndexTest();
        //s.optimizeFirstQuery("Stand By Me");
        s.optimizeSecondQuery("Jack Nicholson", "Kevin Bacon");

    }


}

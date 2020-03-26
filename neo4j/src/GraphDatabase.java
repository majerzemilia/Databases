import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.Map;


public class GraphDatabase{

    private final GraphDatabaseService graphDb;

    public GraphDatabase(){
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("C:/Users/majer/Downloads/neo4j-community-3.5.14-windows/neo4j-community-3.5.14/data/databases/graph.db"));
        registerShutdownHook(graphDb);
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb ){
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run(){
                graphDb.shutdown();
            }
        });
    }

    public String executeSql(final String query, final Map<String, Object> params) {
        try (Transaction transaction = graphDb.beginTx()) {
            Result result = params != null ? graphDb.execute(query, params) : graphDb.execute(query);
            transaction.success();
            return result.resultAsString();
        }
    }
}

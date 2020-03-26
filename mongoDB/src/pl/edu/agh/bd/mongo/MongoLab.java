package pl.edu.agh.bd.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;

public class MongoLab {
	private MongoClient mongoClient;
	private DB db;
	
	public MongoLab() throws UnknownHostException {
		mongoClient = new MongoClient();
		db = mongoClient.getDB("EmiliaMajerz2");
	}
	
	private void showCollections(){
		for(String name : db.getCollectionNames()){
			System.out.println("colname: "+name);
		}
	}
	
	private void showCities() {
		List<String> list = db.getCollection("business")
				.distinct("city");
		for(String s: list) System.out.println(s);		
	}

	private void showReviewsCount() {
		ArrayList years = new ArrayList();
		years.add(new BasicDBObject("date", new BasicDBObject("$regex", "^" + Pattern.quote("2011"))));
		years.add(new BasicDBObject("date", new BasicDBObject("$regex", "^" + Pattern.quote("2012"))));
		BasicDBObject query = new BasicDBObject("$or", years);
		System.out.println(db.getCollection("review")
				.find(query).count());		
	}
	
	private void showOpenBusinesses() {
		BasicDBObject query = new BasicDBObject("open", true);
		DBCursor c = db.getCollection("business")
		.find(query, new BasicDBObject("business_id", 1)
				.append("name", 1)
				.append("full_address", 1));
		try{
			while(c.hasNext()) System.out.println(c.next());
		}
		finally{
			c.close();		
		}
	}
	
	private void showUsers() {
		ArrayList categories = new ArrayList();
		categories.add(new BasicDBObject("votes.funny", 
				new BasicDBObject("$gt", 0)));
		categories.add(new BasicDBObject("votes.useful", 
				new BasicDBObject("$gt", 0)));
		categories.add(new BasicDBObject("votes.cool", 
				new BasicDBObject("$gt", 0)));
		BasicDBObject query = new BasicDBObject("$or", categories);
		DBCursor c = db.getCollection("user")
				.find(query);
		c.sort(new BasicDBObject("name", 1));
		try{
			while(c.hasNext()) System.out.println(c.next());
		}
		finally{
			c.close();		
		}
	}
	
	private void showTipsCount() {
		DBObject match = new BasicDBObject("$match", 
				new BasicDBObject("date", 
						new BasicDBObject("$regex", "^" + Pattern.quote("2013"))));
		
		DBObject groupFields = new BasicDBObject("_id",
				"$business_id");
		groupFields.put("count", 
				new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group",
				groupFields);
		DBObject sort = new BasicDBObject("$sort",
				new BasicDBObject("_id", 1));		
		List <DBObject> pipeline = Arrays.asList(match, group, sort);
		AggregationOutput output = db.getCollection("tip")
				.aggregate(pipeline);
		for(DBObject result: output.results()) System.out.println(result);
	}
	
	private void showAverageStars() {		
		DBObject groupFields = new BasicDBObject("_id",
				"$business_id");
		groupFields.put("average", 
				new BasicDBObject("$avg", "$stars"));
		DBObject group = new BasicDBObject("$group",
				groupFields);
		DBObject sort = new BasicDBObject("$sort",
				new BasicDBObject("average", -1));		
		List <DBObject> pipeline = Arrays.asList(group, sort);
		AggregationOutput output = db.getCollection("review")
				.aggregate(pipeline);
		for(DBObject result: output.results()) System.out.println(result);
	}
	
	private void removeCompanies() {
		db.getCollection("business").remove(new BasicDBObject("stars",
				new BasicDBObject("$lt", 3.0)));
	}
	
	public static void main(String[] args) throws UnknownHostException {
		MongoLab mongoLab = new MongoLab();
		mongoLab.showCollections();
		mongoLab.showCities();
		mongoLab.showReviewsCount();
		mongoLab.showOpenBusinesses();
		mongoLab.showTipsCount();
		mongoLab.showUsers();
		mongoLab.showAverageStars();
		mongoLab.removeCompanies();
	}

}

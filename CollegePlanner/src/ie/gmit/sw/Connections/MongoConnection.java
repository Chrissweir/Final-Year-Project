package ie.gmit.sw.Connections;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import ie.gmit.sw.Calendar.CalendarValues;

public class MongoConnection {
	private String defaultImage = "https://www.barfoot.co.nz/images/noprofile-big.png";
	private String image;

	public void setNewUser(String code) {
		final BasicDBObject[] data = createUserData(code, defaultImage);
		MongoClientURI uri = new MongoClientURI("mongodb://Chris:G00309429@ds055945.mlab.com:55945/heroku_nhl6qjlh");
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB(uri.getDatabase());
		DBCollection user = db.getCollection("User");
		user.insert(data);
		client.close();
	}

	public void setUserData(String code, String file) {
		MongoClientURI uri = new MongoClientURI("mongodb://Chris:G00309429@ds055945.mlab.com:55945/heroku_nhl6qjlh");
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB(uri.getDatabase());
		DBCollection user = db.getCollection("User");
		final BasicDBObject[] data = createUserData(code, file);
		BasicDBObject document = new BasicDBObject();
		document.put("Confirmation Code", code);
		user.remove(document);
		user.insert(data);
		client.close();
	}

	public void setCalendar(String code, CalendarValues cal) {
		MongoClientURI uri = new MongoClientURI("mongodb://Chris:G00309429@ds055945.mlab.com:55945/heroku_nhl6qjlh");
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB(uri.getDatabase());
		DBCollection user = db.getCollection("Calendar");
		BasicDBObject document = new BasicDBObject();
		document.put("Confirmation Code", code);
		document.put("Title", cal.getTitle());
		document.put("Start", cal.getStart());
		document.put("Finish", cal.getEnd());
		user.insert(document);
		client.close();

	}

	public void removeUserData(String code) {
		MongoClientURI uri = new MongoClientURI("mongodb://Chris:G00309429@ds055945.mlab.com:55945/heroku_nhl6qjlh");
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB(uri.getDatabase());
		DBCollection user = db.getCollection("User");
		BasicDBObject document = new BasicDBObject();
		document.put("Confirmation Code", code);
		user.remove(document);
		client.close();
	}

	public String getUserImage(String code) {
		MongoClientURI uri = new MongoClientURI("mongodb://Chris:G00309429@ds055945.mlab.com:55945/heroku_nhl6qjlh");
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB(uri.getDatabase());
		DBCollection user = db.getCollection("User");
		BasicDBObject query = new BasicDBObject();
		// Set the key and value of the object to email: email
		query.put(code, null);
		// Search the User collection for the key and value in query
		DBCursor cursor = user.find(query);
		// Convert query data to a String
		while (cursor.hasNext()) {
			image = (String) cursor.next().get("Image");
		}
		client.close();
		return image;
	}

	private BasicDBObject[] createUserData(String code, String file) {

		BasicDBObject ImageDetails = new BasicDBObject();

		ImageDetails.put("Confirmation Code", code);
		ImageDetails.put("Image", file);

		final BasicDBObject[] data = { ImageDetails };

		return data;
	}

	public List getCalender(String code) {

		MongoClientURI uri = new MongoClientURI("mongodb://Chris:G00309429@ds055945.mlab.com:55945/heroku_nhl6qjlh");
		MongoClient client = new MongoClient(uri);
		DB db = client.getDB(uri.getDatabase());
		DBCollection user = db.getCollection("Calendar");
		BasicDBObject query = new BasicDBObject();
		// Set the key and value of the object to email: email
		query.put(code, null);
		// Search the User collection for the key and value in query
		DBCursor cursor = user.find(query);

		// Convert query data to a String
		List l = new ArrayList();
		//while (cursor.hasNext()) {
		int i =0;
		
			for (DBObject dbObject : cursor) {
				String title = (String) dbObject.get("Title");
				String start = (String) dbObject.get("Start");
				String finish = (String) dbObject.get("Finish");
				i++;
				CalendarValues c = new CalendarValues();
				c.setId(i);
				c.setStart(start);
				c.setEnd(finish);
				c.setTitle(title);
				l.add(c);
			/*	List t = new ArrayList();
				
				t.add(title);
				t.add(start);
				t.add(finish);
				l.add(t);
				System.out.println(l);*/
			}
		//}
		client.close();
		return l;

	}
}
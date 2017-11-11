
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class DBController{
	private Connection connection;		
	private Statement statement;		
	private ResultSet myResult;			
	private ResultSetMetaData metaData;	

	private String user = "sa";				
	private String password = "111111";
	private CallableStatement cstmt = null;
	private ArrayList<User> mUsers;
	private ArrayList<Poster> mResumes;
	
	public DBController(){
		mUsers = new ArrayList <> ();
		mResumes = new ArrayList<>();
	}
	
	public void signUp(User user){
		//System.out.println("signUp");
		try {
			
			cstmt = connection.prepareCall("{call signUp(?,?,?,?)}");
			cstmt.setString("name", user.getName());
			cstmt.setString("login", user.getLogin());
			cstmt.setString("password", user.getPassword());
			cstmt.setString("role", user.getRole());
			cstmt.execute();
			
			myResult = cstmt.getResultSet();
			System.out.println(cstmt.getResultSet());
			if(myResult != null && myResult.next()){
				
				user.setId(myResult.getInt(1));
				user.setName(myResult.getString(2));
				user.setLogin(myResult.getString(3));
				user.setPassword(myResult.getString(4));
				user.setRole(myResult.getString(5));
			}
			
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void signIn(User user){
		try {
			cstmt = connection.prepareCall("{call signIn(?,?)}");
			cstmt.setString("login", user.getLogin());
			cstmt.setString("password", user.getPassword());
			cstmt.execute();
			
			myResult = cstmt.getResultSet();
			if(myResult.next()){
				user.setId(myResult.getInt(1));
				user.setName(myResult.getString(2));
				user.setLogin(myResult.getString(3));
				user.setPassword(myResult.getString(4));
				user.setRole(myResult.getString(5));
			}
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getUser(User user){
		try {
			cstmt = connection.prepareCall("{call getUser(?)}");
			cstmt.setInt("id", user.getId());
			cstmt.execute();
			
			myResult = cstmt.getResultSet();
			if(myResult.next()){
				user.setId(myResult.getInt(1));
				user.setName(myResult.getString(2));
				user.setLogin(myResult.getString(3));
				user.setPassword(myResult.getString(4));
				user.setRole(myResult.getString(5));
			}
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveResume(Poster poster){
		try {
			cstmt = connection.prepareCall("{call SaveResume(?,?,?,?,?,?,?)}");
			cstmt.setString("title", poster.getTitle());
			cstmt.setString("body", poster.getBody());
			cstmt.setString("city", poster.getCity());
			cstmt.setString("sallary", poster.getSallary());
			cstmt.setString("category", poster.getCategory());
			cstmt.setInt("idCreator", poster.getIdCreator());
			cstmt.setInt("id", poster.getId());
			cstmt.execute();
			
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteResume(Poster poster){
		try {
			cstmt = connection.prepareCall("{call DeleteResume(?)}");
			cstmt.setInt("id", poster.getId());
			cstmt.execute();
			
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Poster> getResumes(int userId){
		try {
			mResumes.clear();
			cstmt = connection.prepareCall("{call GetResumes(?)}");
			cstmt.setInt("idUser", userId);
			cstmt.execute();
			myResult = cstmt.getResultSet();
			while(myResult.next()){
				mResumes.add(new Poster(myResult.getInt(1), myResult.getInt(2), myResult.getString(3),
						myResult.getString(4), myResult.getString(5), myResult.getString(6),
						myResult.getString(7), myResult.getBoolean(8)));
			}
			
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mResumes;
	}
	public ArrayList<Poster> getMyResumes(int userId){
		try {
			mResumes.clear();
			cstmt = connection.prepareCall("{call GetResumesOfUser(?)}");
			cstmt.setInt("idUser", userId);
			cstmt.execute();
			myResult = cstmt.getResultSet();
			System.out.println(userId);
			while(myResult.next()){
				mResumes.add(new Poster(myResult.getInt(1), myResult.getInt(2), myResult.getString(3),
						myResult.getString(4), myResult.getString(5), myResult.getString(6), myResult.getString(7), false));
			}
			cstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mResumes;
	}
	public ArrayList<Poster> getFavoriteResumes(int userId){
		try {
			mResumes.clear();
			cstmt = connection.prepareCall("{call GetFavoriteResumes(?)}");
			cstmt.setInt("idUser", userId);
			cstmt.execute();
			myResult = cstmt.getResultSet();
			while(myResult.next()){
				mResumes.add(new Poster(myResult.getInt(1), myResult.getInt(2), myResult.getString(3),
						myResult.getString(4), myResult.getString(5), myResult.getString(6), myResult.getString(7), myResult.getBoolean(8)));
			}
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mResumes;
	}
	
	public ArrayList<Poster> findResumes(FindPost findPost, int userId){
		try {
			mResumes.clear();
			cstmt = connection.prepareCall("{call FindResumes(?,?,?,?,?,?)}");
			cstmt.setString("word", findPost.getWord());
			cstmt.setString("city", findPost.getCity());
			cstmt.setString("category", findPost.getCategory());
			cstmt.setString("minSalary", findPost.getSallary());
			cstmt.setInt("idCreator", findPost.getIdCreator());
			cstmt.setInt("idUser", userId);
			cstmt.execute();
			myResult = cstmt.getResultSet();
			while(myResult.next()){
				mResumes.add(new Poster(myResult.getInt(1), myResult.getInt(2), myResult.getString(3),
						myResult.getString(4), myResult.getString(5), myResult.getString(6), myResult.getString(7), myResult.getBoolean(8)));
			}
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mResumes;
	}
	
	public ArrayList<Poster> getFavResumes(FindPost findPost, int userId){
		try {
			mResumes.clear();
			cstmt = connection.prepareCall("{call GetFavResumes(?,?)}");
			cstmt.setString("word", findPost.getWord());
			cstmt.setInt("idUser", userId);
			cstmt.execute();
			
			System.out.println(findPost.getWord() + "and[" + userId + "]");
			
			myResult = cstmt.getResultSet();
			while(myResult.next()){
				mResumes.add(new Poster(myResult.getInt(1), myResult.getInt(2), myResult.getString(3),
						myResult.getString(4), myResult.getString(5), myResult.getString(6), myResult.getString(7), true));
			}
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mResumes;
	}
	
	public ArrayList<String> getCategories(){
		ArrayList<String> data = new ArrayList<>();
		try {
			cstmt = connection.prepareCall("{call GetCategories()}");
			cstmt.execute();
			myResult = cstmt.getResultSet();
			
			while(myResult.next()){
				data.add(myResult.getString(2));
			}
			cstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	public void toggleFavoriteResume(Poster p, int id){
		try {
			cstmt = connection.prepareCall("{call toggleFavRes(?,?)}");
			cstmt.setInt("idRes", p.getId());
			cstmt.setInt("idUser", id);
			cstmt.execute();
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void connect() 
	{
		try {
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			connection = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1; "
			+ "instance = SQLEXPRESS; databaseName = RabotnikDB; user = "+user+"; password = "+password+";");	
		} 
		catch (SQLException e){
			e.printStackTrace();
		}	
	}
}
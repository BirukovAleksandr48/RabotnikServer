import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;

public class UserThread implements Runnable {
	private Socket socket;
	private Scanner sc;
	private PrintWriter pw;
	private String fromClient;
	private DBController mDBController;
	private int userId;
	
	public final static String KEY_COMMAND_FIND_RESUMES = "KEY_COMMAND_FIND_RESUMES";
    public final static String KEY_COMMAND_GET_USER = "KEY_COMMAND_GET_USER";
    public static final String KEY_JSON_RESULT = "KEY_JSON_RESULT";
    public static final String KEY_COMMAND_GET_CATEGORIES = "KEY_COMMAND_GET_CATEGORIES";
    public static final String KEY_MESSAGE_TO_SERVER = "KEY_MESSAGE_TO_SERVER";
    public static final String KEY_COMMAND_ADD_USER = "KEY_COMMAND_ADD_USER";
    public static final String KEY_COMMAND_SIGN_IN = "KEY_COMMAND_SIGN_IN";
    public static final String KEY_COMMAND_TOGGLE_FAV = "KEY_COMMAND_TOGGLE_FAV";
    public static final String KEY_COMMAND_SAVE_RESUME = "KEY_COMMAND_SAVE_RESUME";
    public static final String KEY_COMMAND_DELETE_RESUME = "KEY_COMMAND_DELETE_RESUME";
    public static final String KEY_COMMAND_GET_FAV_RESUMES = "KEY_COMMAND_GET_FAV_RESUMES";
    public static final String KEY_COMMAND_GET_FAV_VACANCIES = "KEY_COMMAND_GET_FAV_VACANCIES";
    public static final String KEY_COMMAND_FIND_VACANCIES = "KEY_COMMAND_FIND_VACANCIES";
    public static final String KEY_COMMAND_TOGGLE_FAV_VACANCY = "KEY_COMMAND_TOGGLE_FAV_VACANCY";
    public static final String KEY_COMMAND_SAVE_VACANCY = "KEY_COMMAND_SAVE_VACANCY";
    public static final String KEY_COMMAND_DELETE_VACANCY = "KEY_COMMAND_DELETE_VACANCY";
    
	public UserThread(Socket s) {
		this.socket = s;
		mDBController = new DBController();
		mDBController.connect();
	}
	
	public void run() {
		try {
			sc = new Scanner(socket.getInputStream());
			pw = new PrintWriter(socket.getOutputStream());
			String message;
			while(sc.hasNextLine()) {
				message = sc.nextLine();
				System.out.println("got a message:" + message);
				
				ArrayList<Poster> mResumes = new ArrayList<>();
				MesFromClient mfc = new Gson().fromJson(message, MesFromClient.class);
				//System.out.println(mfc.getCommand());
				
				if(mfc.getCommand().equals(KEY_COMMAND_GET_CATEGORIES)){
					System.out.println(KEY_COMMAND_GET_CATEGORIES);
					ArrayList<String> data = mDBController.getCategories();
					Gson gson = new Gson();
					String jsonData = gson.toJson(data);
					mfc.setJSONData(jsonData);
					String jsonString = gson.toJson(mfc);
					send(jsonString);
				}else if(mfc.getCommand().equals(KEY_COMMAND_ADD_USER)){
					System.out.println(KEY_COMMAND_ADD_USER);
					User user = new Gson().fromJson(mfc.getJSONData(), User.class);
					mDBController.signUp(user);
					userId = user.getId();//запомнил юзера
					Gson gson = new Gson();
					String jsonData = gson.toJson(user);
					mfc.setJSONData(jsonData);
					String jsonString = gson.toJson(mfc);
					send(jsonString);
				}else if(mfc.getCommand().equals(KEY_COMMAND_GET_USER)){
					System.out.println(KEY_COMMAND_GET_USER);
					User user = new Gson().fromJson(mfc.getJSONData(), User.class);
					mDBController.getUser(user);
					userId = user.getId();//запомнил юзера
					Gson gson = new Gson();
					String jsonData = gson.toJson(user);
					mfc.setJSONData(jsonData);
					String jsonString = gson.toJson(mfc);
					send(jsonString);
				}else if(mfc.getCommand().equals(KEY_COMMAND_SIGN_IN)){
					System.out.println(KEY_COMMAND_SIGN_IN);
					User user = new Gson().fromJson(mfc.getJSONData(), User.class);
					mDBController.signIn(user);
					userId = user.getId();//запомнил юзера
					Gson gson = new Gson();
					String jsonData = gson.toJson(user);
					mfc.setJSONData(jsonData);
					String jsonString = gson.toJson(mfc);
					send(jsonString);
				}else if(mfc.getCommand().equals(KEY_COMMAND_FIND_RESUMES)){
					System.out.println(KEY_COMMAND_FIND_RESUMES);
					FindPost findPost = new Gson().fromJson(mfc.getJSONData(), FindPost.class);
					mResumes = mDBController.findResumes(findPost, userId);
					Gson gson = new Gson();
					String jsonData = gson.toJson(mResumes);
					mfc.setJSONData(jsonData);
					String jsonString = gson.toJson(mfc);
					send(jsonString);
				}else if(mfc.getCommand().equals(KEY_COMMAND_FIND_VACANCIES)){
					System.out.println(KEY_COMMAND_FIND_VACANCIES);
					FindPost findPost = new Gson().fromJson(mfc.getJSONData(), FindPost.class);
					mResumes = mDBController.findVacancies(findPost, userId);
					Gson gson = new Gson();
					String jsonData = gson.toJson(mResumes);
					mfc.setJSONData(jsonData);
					String jsonString = gson.toJson(mfc);
					send(jsonString);
				}else if(mfc.getCommand().equals(KEY_COMMAND_GET_FAV_RESUMES)){
					System.out.println(KEY_COMMAND_GET_FAV_RESUMES);
					FindPost findPost = new Gson().fromJson(mfc.getJSONData(), FindPost.class);
					mResumes = mDBController.getFavResumes(findPost, userId);
					Gson gson = new Gson();
					String jsonData = gson.toJson(mResumes);
					mfc.setJSONData(jsonData);
					String jsonString = gson.toJson(mfc);
					send(jsonString);
				}else if(mfc.getCommand().equals(KEY_COMMAND_GET_FAV_VACANCIES)){
					System.out.println(KEY_COMMAND_GET_FAV_VACANCIES);
					FindPost findPost = new Gson().fromJson(mfc.getJSONData(), FindPost.class);
					mResumes = mDBController.getFavVacancies(findPost, userId);
					Gson gson = new Gson();
					String jsonData = gson.toJson(mResumes);
					mfc.setJSONData(jsonData);
					String jsonString = gson.toJson(mfc);
					send(jsonString);
				}else if(mfc.getCommand().equals(KEY_COMMAND_TOGGLE_FAV)){
					System.out.println(KEY_COMMAND_TOGGLE_FAV);
					Poster mPoster = new Gson().fromJson(mfc.getJSONData(), Poster.class);
					mDBController.toggleFavoriteResume(mPoster, userId);
				}else if(mfc.getCommand().equals(KEY_COMMAND_TOGGLE_FAV_VACANCY)){
					System.out.println(KEY_COMMAND_TOGGLE_FAV_VACANCY);
					Poster mPoster = new Gson().fromJson(mfc.getJSONData(), Poster.class);
					mDBController.toggleFavoriteVacancy(mPoster, userId);
				}else if(mfc.getCommand().equals(KEY_COMMAND_SAVE_RESUME)){
					System.out.println(KEY_COMMAND_SAVE_RESUME);
					Poster mPoster = new Gson().fromJson(mfc.getJSONData(), Poster.class);
					mDBController.saveResume(mPoster);
				}else if(mfc.getCommand().equals(KEY_COMMAND_SAVE_VACANCY)){
					System.out.println(KEY_COMMAND_SAVE_VACANCY);
					Poster mPoster = new Gson().fromJson(mfc.getJSONData(), Poster.class);
					mDBController.saveVacancy(mPoster);
				}else if(mfc.getCommand().equals(KEY_COMMAND_DELETE_VACANCY)){
					System.out.println(KEY_COMMAND_DELETE_VACANCY);
					Poster mPoster = new Gson().fromJson(mfc.getJSONData(), Poster.class);
					mDBController.deleteVacancy(mPoster);
				}else if(mfc.getCommand().equals(KEY_COMMAND_DELETE_RESUME)){
					System.out.println(KEY_COMMAND_DELETE_RESUME);
					Poster mPoster = new Gson().fromJson(mfc.getJSONData(), Poster.class);
					mDBController.deleteResume(mPoster);
				}
			}
		} catch (IOException e){e.printStackTrace();}
		catch(Exception e){
			if(socket.isClosed()){				//вот это никогда не срабатывает
				System.out.println("Disconect");
				return;
			}
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void send(String jsonString){
		if(socket.isConnected()) {
			try {
				System.out.println(jsonString);
				pw = new PrintWriter(socket.getOutputStream());
				pw.write(jsonString + "\n");
				pw.flush(); 
			}catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
} 
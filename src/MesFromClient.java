
public class MesFromClient {
	String command;
    String JSONData;

    public MesFromClient(String command, String JSONData) {
        this.command = command;
        this.JSONData = JSONData;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getJSONData() {
        return JSONData;
    }

    public void setJSONData(String JSONData) {
        this.JSONData = JSONData;
    }
}

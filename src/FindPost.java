public class FindPost {
    String word;
    String city;
    String category;
    String sallary;
    int idCreator;
    
    public FindPost(String word, String city, String category, String sallary, int idCreator) {
        this.word = word;
        this.city = city;
        this.category = category;
        this.sallary = sallary;
        this.idCreator = idCreator;
    }

    public FindPost() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSallary() {
        return sallary;
    }

    public void setSallary(String sallary) {
        this.sallary = sallary;
    }

	public int getIdCreator() {
		return idCreator;
	}

	public void setIdCreator(int idCreator) {
		this.idCreator = idCreator;
	}
    
}
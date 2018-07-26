package achievement;

public enum EnumAchievements {
	ACH_FINISH_CLASS("Is it really over ?","/achievement/resources/ACH_FINISH_CLASS.png"),
	ACH_MID_CLASS("Is the glass half empty or half full ? ","/achievement/resources/ACH_MID_CLASS.png"),
	ACH_WHAT_DOES_THIS_BUTTON_DO("I have no idea what i'm doing","/achievement/resources/ACH_WHAT_DOES_THIS_BUTTON_DO.png"),
	ACH_ONE_MORE("AND ANOTHER ONE !","/achievement/resources/ACH_ONE_MORE.jpg"),
	ACH_PATIENCE("Patience you must have","/achievement/resources/ACH_PATIENCE.jpg");
	
	private String message = "";
	private String imagePath = "";
	private EnumAchievements(String message,String imagePath) {
		this.message = message;
		this.imagePath = imagePath;
	}
	
	public String getImagePath() {
		return getClass().getResource(this.imagePath).toString();
	}
	@Override
	public String toString() {
		return this.message;
	}
	
}

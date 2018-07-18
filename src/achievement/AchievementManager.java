package achievement;


import java.io.Serializable;
import java.util.HashSet;

import javafx.scene.image.Image;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;


public class AchievementManager implements Serializable {
	
	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = -8249840998858189882L;
	HashSet<EnumAchievements> achievements;
	
	public AchievementManager() {
		achievements = new HashSet<>();
	}
	
	public AchievementManager(HashSet<EnumAchievements> achievements) {
		this.achievements = achievements;
	}
	/**
	 * Display a achievement if it was not already display
	 * @param achievement The achievement to display
	 * @return True if the achievement is display
	 */
	public boolean displayAchievement(EnumAchievements achievement) {
		if(!achievements.contains(achievement)) {
			achievements.add(achievement);
			Image image = new Image(achievement.getImagePath());
			String title = "Achievement unlocked";
	        String message = achievement.toString();
	        
	        TrayNotification tray = new TrayNotification();
	        tray.setTitle(title);
	        tray.setAnimationType(AnimationType.POPUP);
	        tray.setMessage(message);
	        tray.setImage(image);
	        tray.showAndDismiss(Duration.seconds(2));
			return true;
		}else {
			return false;
		}
	}
}

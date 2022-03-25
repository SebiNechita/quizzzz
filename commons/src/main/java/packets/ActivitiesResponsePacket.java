package packets;

import commons.questions.Activity;
import commons.utils.HttpStatus;

import java.util.List;

public class ActivitiesResponsePacket extends GeneralResponsePacket {
    private List<Activity> activities;

    /**
     * Empty constructor
     */
    public ActivitiesResponsePacket() {

    }

    /**
     * Constructor
     * @param activities The list of activities
     */
    public ActivitiesResponsePacket(List<Activity> activities) {
        this.activities = activities;
    }

    /**
     * Constructor
     * @param httpCode The HTTP code to send to the receiver of this packet
     * @param activities The list of activities
     */
    public ActivitiesResponsePacket(int httpCode, List<Activity> activities) {
        super(httpCode);
        this.activities = activities;
    }

    /**
     * Constructor
     * @param status The HTTP status to send to the receiver of this packet
     * @param activities The list of activities
     */
    public ActivitiesResponsePacket(HttpStatus status, List<Activity> activities) {
        super(status);
        this.activities = activities;
    }

    /**
     * Constructor
     * @param httpCode The HTTP code to send to the receiver of this packet
     * @param message The message to send to the receiver of this packet
     * @param activities The list of activities
     */
    public ActivitiesResponsePacket(int httpCode, String message, List<Activity> activities) {
        super(httpCode, message);
        this.activities = activities;
    }

    /**
     * Constructor
     * @param status The HTTP status to send to the receiver of this packet
     * @param message The message to send to the receiver of this packet
     * @param activities The list of activities
     */
    public ActivitiesResponsePacket(HttpStatus status, String message, List<Activity> activities) {
        super(status, message);
        this.activities = activities;
    }

    /**
     * Get method
     * @return the list of activities
     */
    public List<Activity> getActivities() {
        return activities;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * Set method
     * @param activities Sets the list of activities
     */
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}

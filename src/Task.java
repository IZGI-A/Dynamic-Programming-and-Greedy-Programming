import java.time.LocalTime;

public class Task implements Comparable {
    public String name;
    public String start;
    public int duration;
    public int importance;
    public boolean urgent;

    public String getName() {
        return this.name;
    }

    public String getStartTime() {
        return this.start;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getImportance() {
        return this.importance;
    }

    public boolean isUrgent() {
        return this.urgent;
    }
    public String getFinishTime() {
        LocalTime startTime =LocalTime.parse( this.start);
        LocalTime finishTime= startTime.plusHours( this.duration); // since duration is an integer, I used plus hours
        return finishTime.toString();
    }
    public double getWeight() {

        double weight = this.importance*(this.urgent ? 2000:1);
        weight /= this.duration;
        return weight;
    }

    @Override
    public int compareTo(Object o) {
        String ofinishTime;
        if( o instanceof Task){
            Task otask = (Task) o;
            ofinishTime =otask.getFinishTime();
            return this.getFinishTime().compareTo(ofinishTime);
        }
        return -1;
    }
}

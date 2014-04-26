public class Locale {

    //
    // Public
    //

    // Constructor
    public Locale(int id) {
        this.id = id;
    }

    // Getters and Setters
    public int getId() {
        return this.id;
    }

    public String getText() {
        return this.name + "\n" + this.desc + "\n You can go: " + this.directions + "\n On Water: " + this.onWater + "\n There is " + this.item + " on the ground.";
    }

    public String getName() {
        return this.name;
    }
    public void setName(String value) {
        this.name = value;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String value) {
        this.desc = value;
    }
    public void setDirections(String value) {
    	this.directions = value;
    }
    public String getDirections() {
    	return this.directions;
    }
    public void setWater(String value) {
    	this.onWater = value;
    }
    public String getWater() {
    	return this.onWater;
    }
    public void setItem(String value) {
    	this.item = value;
	}
    public String getItem() {
    	return this.item;
    }
    public void setNorth(int value) {
    	this.north = value;
    }
    public int getNorth() {
    	return this.north;
    }
    public void setSouth(int value) {
    	this.south = value;
    }
    public int getSouth() {
    	return this.south;
    }
    public void setEast(int value) {
    	this.east = value;
    }
    public int getEast() {
    	return this.east;
    }
    public void setWest(int value) {
    	this.west = value;
    }
    public int getWest() {
    	return this.west;
    }

    // Other methods
    @Override
    public String toString(){
        return "[Locale id="
                + this.id
                + " name="
                + this.name
                + " directions="
                + this.directions
                + " desc=" + this.desc + "]";
    }

    //
    //  Private
    //
    private int     id;
    private String  name;
    private String  desc;
    private String directions;
    private String onWater;
    private String item;
    private int north;
    private int south;
    private int east;
    private int west;
}

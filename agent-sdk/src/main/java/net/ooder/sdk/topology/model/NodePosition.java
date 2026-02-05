package net.ooder.sdk.topology.model;

import com.alibaba.fastjson.annotation.JSONField;

public class NodePosition {
    @JSONField(name = "x")
    private double x;
    
    @JSONField(name = "y")
    private double y;
    
    @JSONField(name = "z")
    private double z;
    
    @JSONField(name = "latitude")
    private double latitude;
    
    @JSONField(name = "longitude")
    private double longitude;
    
    @JSONField(name = "altitude")
    private double altitude;
    
    @JSONField(name = "room")
    private String room;
    
    @JSONField(name = "building")
    private String building;
    
    @JSONField(name = "floor")
    private int floor;
    
    public NodePosition() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.altitude = 0.0;
        this.floor = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "NodePosition{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

package caseStudies.healthcare;

public class RobotAssignment {
	
	public RobotAssignment(String rId, String cId, String rT) {
		roomId = rId;
		capabilityId = cId;
		roomType = rT;
	}
	
	private String roomId;
	private String capabilityId;
	private String roomType;
	

	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getCapabilityId() {
		return capabilityId;
	}
	public void setCapabilityId(String capabilityId) {
		this.capabilityId = capabilityId;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	@Override
	public String toString() {
		return "RobotAssignment [roomId=" + roomId + ", capabilityId=" + capabilityId + ", roomType=" + roomType + "]";
	}
	
}

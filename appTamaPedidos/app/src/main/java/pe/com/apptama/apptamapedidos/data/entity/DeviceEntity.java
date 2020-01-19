package pe.com.apptama.apptamapedidos.data.entity;

public class DeviceEntity {

    private Long  deviceid;

    public DeviceEntity() {
    }

    public DeviceEntity(Long deviceid) {
        this.deviceid = deviceid;
    }

    public Long getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Long deviceid) {
        this.deviceid = deviceid;
    }
}

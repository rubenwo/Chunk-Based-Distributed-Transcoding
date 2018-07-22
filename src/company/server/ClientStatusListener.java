package company.server;

public interface ClientStatusListener {
    void onMasterOnline(ConnectionHandler masterHandler);

    void onMasterOffline(ConnectionHandler masterHandler);

    void onEncoderOnline(ConnectionHandler encoderHandler);

    void onEncoderOffline(ConnectionHandler encoderHandler);
}

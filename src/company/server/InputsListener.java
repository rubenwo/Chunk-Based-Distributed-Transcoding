package company.server;

public interface InputsListener {
    void onInputsAvailable(String[] inputs);

    void onCommandAvailable(String command);
}

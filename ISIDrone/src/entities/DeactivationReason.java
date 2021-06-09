package entities;

public class DeactivationReason {
    private int idClient;
    private int idDeactivationInitiator;
    private String description;

    public DeactivationReason(int idClient, int idDeactivationInitiator, String description) {
        this.idClient = idClient;
        this.idDeactivationInitiator = idDeactivationInitiator;
        this.description = description;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdDeactivationInitiator() {
        return idDeactivationInitiator;
    }

    public void setIdDeactivationInitiator(int idDeactivationInitiator) {
        this.idDeactivationInitiator = idDeactivationInitiator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

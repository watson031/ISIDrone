package entities;

public class User {

    int id;
    int userType;
    String lastName,
            firstName,
            email,
            password;
    Address shipAddress;

    private String userStatus;

    public User() {
    }

    public User(int id, String lastName, String firstName, String email,
            String password, Address shipAddress, int userType, String userStatus) {
        super();
        this.id = id;
        this.userType = userType;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.shipAddress = shipAddress;
        this.userStatus = userStatus;
    }

    public User(String lastName, String firstName, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }

    public User(String lastName, String firstName, String email, String userStatus) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.userStatus = userStatus;
    }

    public User(int id, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public User(int id, String lastName, String firstName, String email, String userStatus) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.userStatus = userStatus;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Address getShipAddress() {
        return shipAddress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setShipAdress(Address adress) {
        this.shipAddress = adress;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

}

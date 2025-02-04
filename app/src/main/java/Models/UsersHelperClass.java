package Models;

public class UsersHelperClass {
    String FirstName, LastName, UserName, Email, PhoneNumber, Password;

    public UsersHelperClass(String firstName, String lastName, String userName, String email, String phoneNumber, String password) {
        FirstName = firstName;
        LastName = lastName;
        UserName = userName;
        Email = email;
        PhoneNumber = phoneNumber;
        Password = password;
    }

    public UsersHelperClass() {
    }

    public String getFirstName() {
        return FirstName;
    }
    public String getUserName() {
        return UserName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

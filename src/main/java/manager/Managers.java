package manager;

public class Managers {

    public static UsersManager getDefaultUsersManager() {
        return new UsersManager();
    }

    public static FilmsManager getDefaultFilmsManager() {
        return new FilmsManager();
    }
}

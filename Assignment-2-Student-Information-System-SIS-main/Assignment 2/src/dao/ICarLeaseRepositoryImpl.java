package dao;

import entity.Vehicle;
import entity.Customer;
import entity.Lease;

import myexceptions.CarNotFoundException;
import myexceptions.CustomerNotFoundException;
import myexceptions.LeaseNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import java.util.Date;
public class ICarLeaseRepositoryImpl implements ICarLeaseRepository {

    private Connection connection;

    public ICarLeaseRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    public ICarLeaseRepositoryImpl() {
        //TODO Auto-generated constructor stub
    }

    @Override
    public void addCar(Vehicle car) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Vehicle (make, model, year, dailyRate, status, passengerCapacity, engineCapacity) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, car.getMake());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setDouble(4, car.getDailyRate());
            statement.setString(5, car.getStatus());
            statement.setInt(6, car.getPassengerCapacity());
            statement.setInt(7, car.getEngineCapacity());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCar(int carID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM Vehicle WHERE vehicleID = ?")) {
            statement.setInt(1, carID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Vehicle> listAvailableCars() {
        List<Vehicle> availableCars = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Vehicle WHERE status = 'available'")) {

            while (resultSet.next()) {
                Vehicle car = createCarFromResultSet(resultSet);
                availableCars.add(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableCars;
    }

    @Override
    public List<Vehicle> listRentedCars() {
        List<Vehicle> rentedCars = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Vehicle WHERE status = 'notAvailable'")) {

            while (resultSet.next()) {
                Vehicle car = createCarFromResultSet(resultSet);
                rentedCars.add(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentedCars;
    }

    @Override
    public Vehicle findCarById(int carID) throws CarNotFoundException {

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Vehicle WHERE vehicleID = ?")) {
            statement.setInt(1, carID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createCarFromResultSet(resultSet);
                } else {
                    throw new CarNotFoundException("Car with ID " + carID + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle or log the exception accordingly
            throw new RuntimeException("Error while finding car by ID", e);
        }
    }
        /*try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Vehicle WHERE vehicleID = ?")) {
            statement.setInt(1, carID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createCarFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    @Override
    public void addCustomer(Customer customer) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Customer (firstName, lastName, email, phoneNumber) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhoneNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCustomer(int customerID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM Customer WHERE customerID = ?")) {
            statement.setInt(1, customerID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> listCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer")) {

            while (resultSet.next()) {
                Customer customer = createCustomerFromResultSet(resultSet);
                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public Customer findCustomerById(int customerID) throws CustomerNotFoundException {

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Customer WHERE customerID = ?")) {
            statement.setInt(1, customerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createCustomerFromResultSet(resultSet);
                } else {
                    throw new CustomerNotFoundException("Customer with ID " + customerID + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle or log the exception accordingly
            throw new RuntimeException("Error while finding customer by ID", e);
        }
    }
        /*try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Customer WHERE customerID = ?")) {
            statement.setInt(1, customerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createCustomerFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    */

    @Override
    public  Lease createLease(int customerID, int carID, Date startDate, Date endDate, String type) throws CustomerNotFoundException, CarNotFoundException{
        Customer customer=findCustomerById(customerID);
        Vehicle car=findCarById(carID);
        try {
            customer = findCustomerById(customerID);
        } catch (CustomerNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            car = findCarById(carID);
        } catch (CarNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (customer != null && car != null) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Lease (vehicleID, customerID, startDate, endDate, type) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, carID);
                statement.setInt(2, customerID);
                statement.setDate(3, startDate);
                statement.setDate(4, endDate);
                statement.setString(5, type);
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int leaseID = generatedKeys.getInt(1);
                    Lease lease = new Lease(leaseID, carID, customerID, startDate, endDate, type);
                    return lease;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
        }
        return null;
    }
    
    

    @Override
    public Lease returnCar(int leaseID) throws LeaseNotFoundException {

        try (PreparedStatement statement = connection.prepareStatement("UPDATE Lease SET endDate = CURRENT_DATE WHERE leaseID = ?")) {
            statement.setInt(1, leaseID);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                Lease lease = getLeaseById(leaseID);
                return lease;
            } else {
                throw new LeaseNotFoundException("Lease with ID " + leaseID + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle or log the exception accordingly
            throw new RuntimeException("Error while returning car", e);
        }
    }
        /*try (PreparedStatement statement = connection.prepareStatement("UPDATE Lease SET endDate = CURRENT_DATE WHERE leaseID = ?")) {
            statement.setInt(1, leaseID);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                Lease lease = getLeaseById(leaseID);
                return lease;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }*/

    @Override
    public List<Lease> listActiveLeases() {
        List<Lease> activeLeases = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Lease WHERE endDate IS NULL")) {

            while (resultSet.next()) {
                Lease lease = createLeaseFromResultSet(resultSet);
                activeLeases.add(lease);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activeLeases;
    }

    @Override
    public List<Lease> listLeaseHistory() {
        List<Lease> leaseHistory = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Lease WHERE endDate IS NOT NULL")) {

            while (resultSet.next()) {
                Lease lease = createLeaseFromResultSet(resultSet);
                leaseHistory.add(lease);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaseHistory;
    }

    @Override
    public void recordPayment(Lease lease, double amount) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Payment (leaseID, paymentDate, amount) VALUES (?, CURRENT_DATE, ?)")) {
            statement.setInt(1, lease.getLeaseID());
            statement.setDouble(2, amount);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Lease> retrievePaymentHistoryForCustomer(int customerID) {
        List<Lease> paymentHistory = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Lease l JOIN Payment p ON l.leaseID = p.leaseID WHERE l.customerID = ?")) {
            statement.setInt(1, customerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Lease lease = createLeaseFromResultSet(resultSet);
                    paymentHistory.add(lease);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentHistory;
    }

    @Override
    public double calculateTotalRevenue() {
        double totalRevenue = 0.0;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT SUM(amount) AS totalRevenue FROM Payment")) {

            if (resultSet.next()) {
                totalRevenue = resultSet.getDouble("totalRevenue");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }

    private Vehicle createCarFromResultSet(ResultSet resultSet) throws SQLException {
        Vehicle car = new Vehicle();
        car.setVehicleID(resultSet.getInt("vehicleID"));
        car.setMake(resultSet.getString("make"));
        car.setModel(resultSet.getString("model"));
        car.setYear(resultSet.getInt("year"));
        car.setDailyRate(resultSet.getDouble("dailyRate"));
        car.setStatus(resultSet.getString("status"));
        car.setPassengerCapacity(resultSet.getInt("passengerCapacity"));
        car.setEngineCapacity(resultSet.getInt("engineCapacity"));
        return car;
    }

    private Customer createCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerID(resultSet.getInt("customerID"));
        customer.setFirstName(resultSet.getString("firstName"));
        customer.setLastName(resultSet.getString("lastName"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPhoneNumber(resultSet.getString("phoneNumber"));
        return customer;
    }

    private Lease createLeaseFromResultSet(ResultSet resultSet) throws SQLException {
        Lease lease = new Lease();
        lease.setLeaseID(resultSet.getInt("leaseID"));
        lease.setVehicleID(resultSet.getInt("vehicleID"));
        lease.setCustomerID(resultSet.getInt("customerID"));
        lease.setStartDate(resultSet.getDate("startDate"));
        lease.setEndDate(resultSet.getDate("endDate"));
        return lease;
    }

    public Lease getLeaseById(int leaseID) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Lease WHERE leaseID = ?")) {
            statement.setInt(1, leaseID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createLeaseFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCarAvailability(Vehicle existingCar) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCarAvailability'");
    }

    public void updateCustomer(Customer existingCustomer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCustomer'");
    }
   


public double calculateLeaseCost(String leaseType, int duration) throws SQLException {
    String query = "SELECT dailyRate,monthlyRate FROM Vehicle WHERE vehicleID = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        // Assuming there is a vehicleID associated with the lease
        int vehicleID = getVehicleIDForLeaseType(leaseType); // You need to implement this method

        preparedStatement.setInt(1, vehicleID);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                double dailyRate = resultSet.getDouble("dailyRate");
                double monthlyRate = resultSet.getDouble("monthlyRate");

                if (leaseType.equalsIgnoreCase("daily")) {
                    return dailyRate * duration;
                } else if (leaseType.equalsIgnoreCase("monthly")) {
                    return monthlyRate * duration;
                } else {
                    throw new IllegalArgumentException("Invalid lease type");
                }
            } else {
                throw new SQLException("Vehicle not found for lease type: " + leaseType);
            }
        }
    }
}

@Override
public int getVehicleIDForLeaseType(String leaseType) throws SQLException {
    String query = "SELECT vehicleID FROM LeaseTypeVehicleMapping WHERE leaseType = ?";
    
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, leaseType);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("vehicleID");
            } else {
                throw new SQLException("No mapping found for lease type: " + leaseType);
            }
        }
    }
}


}

package dao;

import entity.Vehicle;
import myexceptions.CarNotFoundException;
import myexceptions.CustomerNotFoundException;
import myexceptions.LeaseNotFoundException;
import entity.Customer;
import entity.Lease;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ICarLeaseRepository {
    // Car Management
    void addCar(Vehicle car);

    void removeCar(int carID);

    List<Vehicle> listAvailableCars();

    List<Vehicle> listRentedCars();

    Vehicle findCarById(int carID) throws CarNotFoundException;

    // Customer Management
    void addCustomer(Customer customer);

    void removeCustomer(int customerID);

    List<Customer> listCustomers();

    Customer findCustomerById(int customerID) throws CustomerNotFoundException;

    // Lease Management
    Lease createLease(int customerID, int carID, Date startDate, Date endDate, String type) throws CustomerNotFoundException, CarNotFoundException;
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'createLease'");


    int getVehicleIDForLeaseType(String leaseType) throws SQLException;

    Lease returnCar(int leaseID) throws LeaseNotFoundException;

    List<Lease> listActiveLeases();

    List<Lease> listLeaseHistory();

    // Payment Handling
    void recordPayment(Lease lease, double amount);

    double calculateTotalRevenue();

    List<Lease> retrievePaymentHistoryForCustomer(int customerID);

    double calculateLeaseCost(String leaseType, int duration) throws SQLException;
}

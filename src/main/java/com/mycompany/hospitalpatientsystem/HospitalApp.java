/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hospitalpatientsystem;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author emeris
 */
public class HospitalApp {
    private static HospitalSystem system = new HospitalSystem();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args){
        int choice;
        do{
            displayMenu();
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()){
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();
            
            switch(choice) {
                case 1: registerPatient(); break;
                case 2: searchPatient(); break;
                case 3: updatePatient(); break;
                case 4: deletePatient(); break;
                case 5: displayAllPatients(); break;
                case 6: allocateBed(); break;
                case 7: releaseBed(); break;
                case 8: displayWardLayout(); break;
                case 9: displayAvailableBeds(); break;
                case 10: displayOccupiedBeds(); break;
                case 11: generateReports(); break;
                case 12: sortPatients(); break;
                case 0: System.out.println("Existing system. Goodbye!"); break;
                default: System.out.println("Invalid choice. Please try again!");
            }
        } while (choice !=0);
    }
    
    private static void displayMenu(){
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     MEDI-CARE HOSPITAL SYSTEM    ║");
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.println("║  PATIENT MANAGEMENT              ║");
        System.out.println("║    1. Register Patient           ║");
        System.out.println("║    2. Search Patient             ║");
        System.out.println("║    3. Update Patient             ║");
        System.out.println("║    4. Delete Patient             ║");
        System.out.println("║    5. Display All Patients       ║");
        System.out.println("║  BED MANAGEMENT                  ║");
        System.out.println("║    6. Allocate Bed               ║");
        System.out.println("║    7. Release Bed                ║");
        System.out.println("║    8. Display Ward Layout        ║");
        System.out.println("║    9. Display Available Beds     ║");
        System.out.println("║   10. Display Occupied Beds      ║");
        System.out.println("║  REPORTS                         ║");
        System.out.println("║   11. Generate Reports           ║");
        System.out.println("║   12. Sort Patients              ║");
        System.out.println("║  SYSTEM                          ║");
        System.out.println("║    0. Exit                       ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
   
    
    private static void registerPatient(){
        System.out.println("\n--- Register New Patient ---");
        
        System.out.print("Patient ID: ");
        String id = scanner.nextLine().trim();
        
        if(system.findPatient(id) != null){
            System.out.println("Error: Patient ID already exists.");
            return;
        }
        
        System.out.print("First Namr: ");
        String firstName = scanner.nextLine().trim();
        
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();
        
        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Gender: ");
        String gender = scanner.nextLine().trim();
        
        System.out.print("Patient Category");
        System.out.print("1. Inpatient");
        System.out.print("2. Outpatient");
        System.out.print("3. Emergency");
        System.out.print("Select category");
        int catChoice = scanner.nextInt();
        scanner.nextLine();
        
        PatientCategory category;
        switch (catChoice) {
            case 1: category = PatientCategory.INPATIENT;
            break;
            case 2: category = PatientCategory.OUTPATIENT;
            break;
            case 3: category = PatientCategory.EMERGENCY;
            break;
            default:
                System.out.println("Invalid category. Defaulting to Outpatient.");
                category = PatientCategory.OUTPATIENT;
        }
        
        Patient patient;
        if (category == PatientCategory.INPATIENT){
            patient = new Inpatient(id, firstName, lastName, age, gender, condition, category, "Not Assigned", "Not Assigned");
        } else {
            patient = new Patient(id, firstName, lastName, age, gender, condition, category);
        }
        
        if(system.registerPatient(patient)){
            System.out.println("Patient registered successfully.");
        } else {
            System.out.println("Error: Could not register patient.");
        }
    }
    
    private static void searchPatient(){
        System.out.print("\nEnter Patient ID to search: ");
        String id = scanner.nextLine().trim();
        
        Patient p = system.findPatient(id);
        if (p !=null){
            System.out.println("\n--- Patient Found ---");
            p.displayDetails();
        } else {
            System.out.println("Patient not found");
        }
    }
    
    private static void updatePatient(){
        System.out.print("\nEnter Patient ID to update: ");
        String id = scanner.nextLine().trim(); 
        
        Patient p = system.findPatient(id);
        if (p == null){
            System.out.println("Patient not found.");
            return;
        }
        System.out.println("Enter new details (press Enter to kepp current value):");
        
        System.out.println("First Name [" + p.getFirstName() + "]: ");
        String firstName = scanner.nextLine().trim();
        if (firstName.isEmpty()) firstName = p.getFirstName();
        
        System.out.println("Last Name [" + p.getLastName() + "]: ");
        String lastName = scanner.nextLine().trim();
        if (lastName.isEmpty()) lastName = p.getLastName();
        
        System.out.println("Age [" + p.getAge() + "]: ");
        String ageInput = scanner.nextLine().trim();
        int age = ageInput.isEmpty() ? p.getAge() : Integer.parseInt(ageInput);
        
        System.out.println("Gender [" + p.getGender() + "]: ");
        String gender = scanner.nextLine().trim();
        if(gender.isEmpty())gender = p.getGender();
        
        System.out.println("Medical Condition [" + p.getMedicalCondition() + "]: ");
        String condition = scanner.nextLine().trim();
        if (condition.isEmpty()) condition = p.getMedicalCondition();
        
        System.out.println("Category [" + p.getCategory() + "]:");
        System.out.println("1. Inpatient 2. Outpatient 3. Emergency 0. Keep current");
        System.out.print("Select: ");
        String catInput = scanner.nextLine().trim();
        PatientCategory category = p.getCategory();
        if(!catInput.isEmpty()){
            switch (Integer.parseInt(catInput)){
                case 1: category = PatientCategory.INPATIENT; break;
                case 2: category = PatientCategory.OUTPATIENT; break;
                case 3: category = PatientCategory.EMERGENCY; break;
                }
            }
        
        if(system.updatePatient(id, firstName, lastName, age, gender, condition, category)) {
            System.out.println("Patient updated successfully.");            
        } else {
            System.out.println("Error: Could not update patient.");
        }
    }
    
    private static void deletePatient() {
        System.out.print("\nEnter Patient ID to delete");
        
        String id = scanner.nextLine().trim();
        
        if(system.deletePatient(id)) {
            System.out.println("Patient deleted successfully.");
        } else {
            System.out.println("Patient not found");
        }
    } 
    
    private static void displayAllPatients(){
        ArrayList<Patient> patients = system.getAllPatients();
        if (patients.isEmpty()){
            System.out.println("\nNo patients registered.");
            return;
        }
        
        System.out.println("\n========== ALL REGISTERED PATIENTS ==========");
        for (Patient p : patients) {
            System.out.println("---------------------------------------------");
            p.displayDetails();
        }
        
        System.out.println("=============================================");
        System.out.println("Total Patients: " + system.getTotalPatients());
    }
    
    private static void allocateBed(){
        System.out.print("\nEnter Patient ID to allocate bed: ");
        String id = scanner.nextLine().trim();
        
        String result = system.allocateBed(id);
        System.out.println(result);
    }
    
    private static void releaseBed() {
        System.out.print("\nEnter Bed ID to release (e.g., B01): ");
        String bedId = scanner.nextLine().trim();
        
        if (system.releaseBed(bedId)) {
            System.out.println("Bed" + bedId + "released successfully.");
        } else {
            System.out.println("Error: Bed not found or already vacant.");
        }
    }
    
    private static void displayWardLayout(){
        system.displayWardLayout();
    }
    
    private static void displayAvailableBeds(){
        ArrayList<Bed> available = system.getAvailableBeds();
        if(available.isEmpty()){
            System.out.println("\nNo beds available.");
            return;
        }
        
        System.out.println("\n========== AVAILABLE BEDS ==========");
        for (Bed b : available){
            System.out.println(b.getBedId());
        }
        System.out.println("Total Available: " + available.size());
        
        System.out.println("===================================");
    }
    
    private static void displayOccupiedBeds() {
        ArrayList<Bed> occupied = system.getOccupiedBeds();
        if (occupied.isEmpty()){
            System.out.println("\nNo beds occupied.");
            return;
        }
        
        System.out.println("\n========== OCCUPIED BEDS ==========");
        for (Bed b : occupied){
            System.out.println(b.getBedId()+ " - Patient: " + b.getPatientId());
        }
        System.out.println("===================================");
    }
    
    public static void generateReports(){
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           WARD REPORTS                 ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Total Registered Patients: " + String.format("%-13s", system.getTotalPatients())+ "║");
        System.out.println("║ Total Occupied Beds: " + String.format("%-13s", system.getTotalOccupiedBeds())+ "║");
        System.out.println("║ Total Available Beds: " + String.format("%-13s",(20 - system.getTotalOccupiedBeds()))+ "║");
        System.out.println("║ Occupancy Percentage: " + String.format("%-12s", String.format("%.1f%%", system.getOccupancyPercentage()))+ "║");
        
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    private static void sortPatients(){
        System.out.println("\nSort by:");
        System.out.println("1. Surname");
        System.out.println("2. Patient ID");
        System.out.print("Select: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        ArrayList<Patient> sorted;
        if (choice == 1){
            sorted = system.sortPatientsBySurname();
            System.out.println("\n--- Patients Sorted by Surname ---");
        } else {
            sorted = system.sortPatientsById();
            System.out.println("\n--- Patients Sorted by Patient ID ---");
        }
        
        for (Patient p : sorted){
            System.out.println("---------------------------------------------");
            p.displayDetails();
        }
    }
}

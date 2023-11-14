import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This is the GUI class that creates the frame that allows car owners and job owners to interact with the Vehicular Cloud System.
 * @author Software Engineering Capstone Project Group 1
 */
public class VCRTSGUI {
   private JFrame frame = new JFrame();
   private JDialog infoBox = new JDialog();
   private JLabel infoBoxMessage = new JLabel("");
   private final int APP_WIDTH = 480;
   private final int APP_HEIGHT = 600;
   private final String INTRO_PAGE_NAME = "Intro Page";
   private final String SIGNUP_PAGE_NAME = "Sign Up Page";
   private final String LOGIN_PAGE_NAME = "Login Page";
   private final String MAIN_PAGE_NAME = "Main Page";
   private final String CREATE_JOB_REQUEST_PAGE_NAME = "Create Job Request Page";
   private final String CREATE_CAR_RENTAL_PAGE_NAME = "Rent Car Page";
   private JLabel currentUserId = new JLabel("");
   private JLabel currentClientId = new JLabel("");
   private JLabel currentOwnerId = new JLabel("");
   private ArrayList<JButton> pageSwitchButtons = new ArrayList<JButton>();
   private ArrayList<String> screens = new ArrayList<String>();
   private PageSwitcher switcher = new PageSwitcher();
   private UserVerifier verifier = new UserVerifier();
   private JobRequestListener jobRequestListener = new JobRequestListener();
   private CarRentalRequestListener rentalRequestListener = new CarRentalRequestListener();
   private User currentUser;
   private boolean currentUserIsNew;
   //private Server database = new Server();
   //private Controller controller = new Controller();
   private static Socket socket;
   private static DataInputStream inputStream;
   private static DataOutputStream outputStream;
   private Color backgroundColor;
   private Color buttonColor;
   private Color textColor;


   /**
    * Initializes the GUI of the Vehicular Cloud System.
    */
   public VCRTSGUI() {
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new CardLayout());
      frame.setTitle("Vehicular Cloud Real Time System");
      frame.setSize(APP_WIDTH, APP_HEIGHT);
      frame.setResizable(false);
      frame.setLocation(600, 100);
      frame.getContentPane().setBackground(Color.BLUE);

      infoBoxMessage.setHorizontalAlignment(JLabel.CENTER);
      
      infoBox.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      infoBox.setLayout(new BorderLayout());
      infoBox.setSize(300, 200);
      infoBox.setResizable(false);
      infoBox.setLocationRelativeTo(frame);
      infoBox.setModalityType(ModalityType.APPLICATION_MODAL);
      infoBox.add(infoBoxMessage, BorderLayout.CENTER);

      backgroundColor = new Color(245, 195, 194);//background color
      buttonColor = new Color(124,131,188);//button color
      textColor = new Color(255,255,255);//text color

      try {
         socket = new Socket("localhost", 1000);
         inputStream = new DataInputStream(socket.getInputStream());
         outputStream = new DataOutputStream(socket.getOutputStream());
      }
      catch(IOException e) {
         System.out.println("A connection error occurred");
      }

      //start application creates screen output
      startApp(); 
      frame.setVisible(true);
   }

   /**
    * This is the main method that launches the GUI, making it visible to users. 
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      new VCRTSGUI();
   }

   /**
    * Creates the various screens of the GUI that users can navigate to.
    */
   public void startApp() {
      createIntroScreen();
      createLoginScreen();
      createSignUpScreen();
      createMainPage();
      createJobRequestPage();
      createCarRentalPage();
   }

   /**
    * Creates the intro screen for the GUI. This is the first screen that users see upon opening the app.
    */
   public void createIntroScreen() {
      JPanel welcomePanel = new JPanel();
      JLabel welcomeMessage = new JLabel("Welcome to this Vehicular Cloud Real Time System!");
      JTextArea explanation = new JTextArea("If you already have an account with us, select \"Login\" below. If this is your first time using this vehicular cloud system, click the \"Sign Up\" button.");
      JButton signUp = new JButton("Sign Up");
      JButton login = new JButton("Login");

      explanation.setEditable(false);
      explanation.setFocusable(false);
      explanation.setLineWrap(true);
      explanation.setWrapStyleWord(true);
      explanation.setSize(APP_WIDTH - 50, APP_HEIGHT - 50);

      login.setName(LOGIN_PAGE_NAME);
      login.addActionListener(switcher);
      pageSwitchButtons.add(login);
      login.setBackground(buttonColor);
      login.setForeground(textColor);
      login.setBackground(buttonColor);
      login.setBorderPainted(false);
      login.setOpaque(true);

      signUp.setName(SIGNUP_PAGE_NAME);
      signUp.addActionListener(switcher);
      pageSwitchButtons.add(signUp);
      signUp.setBackground(buttonColor);
      signUp.setForeground(textColor);
      signUp.setBackground(buttonColor);
      signUp.setBorderPainted(false);
      signUp.setOpaque(true);

      welcomePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 50));
      welcomePanel.setBounds(0, 0, APP_WIDTH, APP_HEIGHT);
      welcomePanel.setBackground(backgroundColor);
      welcomePanel.add(welcomeMessage);
      welcomePanel.add(explanation);
      welcomePanel.add(signUp);
      welcomePanel.add(login);
      frame.add(welcomePanel, INTRO_PAGE_NAME);
      screens.add(INTRO_PAGE_NAME);
   }

   /**
    * Creates the login screen for the GUI. This is the screen that users will see when logging in with an existing account.
    */
   public void createLoginScreen() {
      JPanel loginPanel = new JPanel();
      JLabel message = new JLabel("Welcome Back User! Please Login Below");
      JPanel usernameSubpanel = new JPanel();
      JLabel usernameLabel = new JLabel("Username: ");
      JTextField username = new JTextField(20);
      JPanel passwordSubpanel = new JPanel();
      JLabel passwordLabel = new JLabel("Password: ");
      JPasswordField password = new JPasswordField(20);
      JButton login = new JButton("Login");
      login.setBackground(buttonColor);
      login.setBorderPainted(false);
      login.setOpaque(true);
      JButton back = new JButton("Back");
      back.setBackground(buttonColor);
      back.setBorderPainted(false);
      back.setOpaque(true);

      usernameSubpanel.setLayout(new BorderLayout(5, 0));
      usernameSubpanel.setBackground(backgroundColor);
      usernameSubpanel.add(usernameLabel, BorderLayout.WEST);
      usernameSubpanel.add(username, BorderLayout.EAST);

      username.addKeyListener(verifier);

      passwordSubpanel.setLayout(new BorderLayout(5, 0));
      passwordSubpanel.setBackground(backgroundColor);
      passwordSubpanel.add(passwordLabel, BorderLayout.WEST);
      passwordSubpanel.add(password, BorderLayout.EAST);

      password.addKeyListener(verifier);

      login.addActionListener(verifier);
      login.setBackground(buttonColor);
      login.setForeground(textColor);

      back.setName(INTRO_PAGE_NAME);
      back.addActionListener(switcher); //back button
      back.setBackground(buttonColor);
      back.setForeground(textColor);
      pageSwitchButtons.add(back);

      loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 50));
      loginPanel.setBackground(backgroundColor);

      loginPanel.add(message);
      loginPanel.add(usernameSubpanel);
      loginPanel.add(passwordSubpanel);
      loginPanel.add(login);
      loginPanel.add(back);
      frame.add(loginPanel, LOGIN_PAGE_NAME);
      screens.add(LOGIN_PAGE_NAME);
   }

   /**
    * Creates the sign up screen for the GUI. This is the screen that users will see when signing up to the 
    * Vehicular Cloud System for the first time.
    */
   public void createSignUpScreen() {
      JPanel signUpPanel = new JPanel();
      JLabel header = new JLabel("Welcome, Please Enter The Following Information");
      JPanel usernameSubpanel = new JPanel();
      JLabel usernameLabel = new JLabel("Enter Username: ");
      JTextField username = new JTextField(20);
      JPanel passwordSubpanel = new JPanel();
      JLabel passwordLabel = new JLabel("Enter Password: ");
      JPasswordField password = new JPasswordField(20);
      JButton signup = new JButton("Sign Up");
      signup.setBackground(buttonColor);
      signup.setBorderPainted(false);
      signup.setOpaque(true);
      JButton back = new JButton("Back");
      back.setBackground(buttonColor);
      back.setBorderPainted(false);
      back.setOpaque(true);

      //sets username of the new sign up
      usernameSubpanel.setLayout(new BorderLayout(5, 0));
      usernameSubpanel.setBackground(backgroundColor);
      usernameSubpanel.add(usernameLabel, BorderLayout.WEST);
      usernameSubpanel.add(username, BorderLayout.EAST);
      username.addKeyListener(verifier);

      passwordSubpanel.setLayout(new BorderLayout(5, 0));
      passwordSubpanel.setBackground(backgroundColor);
      passwordSubpanel.add(passwordLabel, BorderLayout.WEST);
      passwordSubpanel.add(password, BorderLayout.EAST);
      password.addKeyListener(verifier);
      
      signup.addActionListener(verifier);
      signup.setBackground(buttonColor);
      signup.setForeground(textColor);
 
      back.setName(INTRO_PAGE_NAME);
      back.addActionListener(switcher);
      back.setBackground(buttonColor);
      back.setForeground(textColor);
      pageSwitchButtons.add(back);
      
      signUpPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 50));
      signUpPanel.setBackground(backgroundColor);

      signUpPanel.add(header);
      signUpPanel.add(usernameSubpanel);
      signUpPanel.add(passwordSubpanel);
      signUpPanel.add(signup);
      signUpPanel.add(back);
      frame.add(signUpPanel, SIGNUP_PAGE_NAME);
      screens.add(SIGNUP_PAGE_NAME);
   }

   /**
    * Creates the main page of the GUI. From this page, users will be able to rent new cars to the Vehicular Cloud 
    * System or request new jobs. The user's ID (the username that they signed up with or logged in with) is displayed 
    * in the top left corner.
    */
   public void createMainPage() {
      JPanel mainPanel = new JPanel();
      JPanel idPanel = new JPanel();
      JPanel mainPageContentPanel = new JPanel();
      JPanel headerSubPanel = new JPanel();
      JLabel header = new JLabel("Select whether you would like to rent out your car as an owner or");
      JLabel header2 = new JLabel("submit a job request as a client below.");
      JButton owner = new JButton("Rent Car As Owner");
      owner.setBackground(buttonColor);
      owner.setBorderPainted(false);
      owner.setOpaque(true);
      JButton client = new JButton("Request Job As Client");
      client.setBackground(buttonColor);
      client.setBorderPainted(false);
      client.setOpaque(true);
      JButton signout = new JButton("Sign Out");
      signout.setBackground(buttonColor);
      signout.setBorderPainted(false);
      signout.setOpaque(true);

      header2.setHorizontalAlignment(JLabel.CENTER);

      client.setName(CREATE_JOB_REQUEST_PAGE_NAME);
      client.addActionListener(switcher);
      pageSwitchButtons.add(client);
      client.setBackground(buttonColor);
      client.setForeground(textColor);

      signout.setName(INTRO_PAGE_NAME);
      signout.addActionListener(switcher);
      pageSwitchButtons.add(signout);
      signout.setBackground(buttonColor);
      signout.setForeground(textColor);

      owner.setName(CREATE_CAR_RENTAL_PAGE_NAME);
      owner.addActionListener(switcher);
      pageSwitchButtons.add(owner);
      owner.setBackground(buttonColor);
      owner.setForeground(textColor);

      idPanel.setLayout(new BorderLayout());
      idPanel.setBackground(backgroundColor);
      idPanel.add(currentUserId, BorderLayout.WEST);

      headerSubPanel.setLayout(new BorderLayout());
      headerSubPanel.setBackground(backgroundColor);
      headerSubPanel.add(header, BorderLayout.NORTH);
      headerSubPanel.add(header2, BorderLayout.SOUTH);

      mainPageContentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 150, 75));
      mainPageContentPanel.setBackground(backgroundColor);
      mainPageContentPanel.add(headerSubPanel);
      mainPageContentPanel.add(owner);
      mainPageContentPanel.add(client);
      mainPageContentPanel.add(signout);

      mainPanel.setLayout(new BorderLayout());
      mainPanel.setBackground(backgroundColor);
      mainPanel.add(idPanel, BorderLayout.NORTH);
      mainPanel.add(mainPageContentPanel, BorderLayout.CENTER);
      frame.add(mainPanel, MAIN_PAGE_NAME);
      screens.add(MAIN_PAGE_NAME);
   }

   /**
    * Creates the job request page of the GUI. From this page users will be able to submit new jobs to the 
    * Vehicular Cloud System.
    */
   public void createJobRequestPage() {
      JPanel mainPanel = new JPanel();
      JPanel clientIDPanel = new JPanel();
      JPanel jobRequestPanel = new JPanel();
      JLabel header = new JLabel("Fill in the following information to submit a job request.");
      JPanel jobTitleSubPanel = new JPanel();
      JLabel jobTitleLabel = new JLabel("Job Title: ");
      JTextField jobTitle = new JTextField(20);
      JPanel jobDescriptionSubPanel = new JPanel();
      JLabel jobDescriptionLabel = new JLabel("Job Description: ");
      JTextArea jobDescription = new JTextArea(10, 30);
      JPanel approximateJobDurationSubPanel = new JPanel();
      JLabel approximateJobDurationLabel = new JLabel("Job Duration Time: ");
      JTextField approximateJobDuration = new JTextField(1);
      String[] timeOptions = {"hours", "minutes"};
      JComboBox<String> jobDurationTimes = new JComboBox<String>(timeOptions);
      JPanel jobDeadlineSubPanel = new JPanel();
      JLabel jobDeadlineLabel = new JLabel("Job Deadline (mm/dd/yyyy): ");
      JPanel dateSubPanel = new JPanel(new GridLayout(1, 5));
      JTextField month = new JTextField(2);
      JTextField day = new JTextField(2);
      JTextField year = new JTextField(4);
      JLabel divider = new JLabel("/");
      JLabel divider2 = new JLabel("/");
      JButton submit = new JButton("Submit Job");
      submit.setBackground(buttonColor);
      submit.setBorderPainted(false);
      submit.setOpaque(true);
      JButton back = new JButton("Back");
      back.setBackground(buttonColor);
      back.setBorderPainted(false);
      back.setOpaque(true);
      JButton jobTime = new JButton("Caculate Job Time");
      jobTime.setBackground(buttonColor);
      jobTime.setBorderPainted(false);
      jobTime.setOpaque(true);

      //design of Job Request Page
      clientIDPanel.setLayout(new BorderLayout());
      clientIDPanel.setBackground(backgroundColor);
      clientIDPanel.add(currentClientId, BorderLayout.WEST);

      jobTitle.setName("Job Title");
      jobTitle.addKeyListener(jobRequestListener);

      jobTitleSubPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
      jobTitleSubPanel.setBackground(backgroundColor);
      jobTitleSubPanel.add(jobTitleLabel);
      jobTitleSubPanel.add(jobTitle);

      jobDescription.setName("Job Description");
      jobDescription.addKeyListener(jobRequestListener);
      
      jobDescriptionSubPanel.setLayout(new BorderLayout());
      jobDescriptionSubPanel.setBackground(backgroundColor);
      jobDescriptionSubPanel.add(jobDescriptionLabel, BorderLayout.NORTH);
      jobDescriptionSubPanel.add(jobDescription, BorderLayout.SOUTH);

      approximateJobDuration.setName("Job Duration Time");
      approximateJobDuration.addKeyListener(jobRequestListener);

      jobDurationTimes.addItemListener(jobRequestListener);
      
      approximateJobDurationSubPanel.setLayout(new GridLayout(1, 3, 10, 0));
      approximateJobDurationSubPanel.setBackground(backgroundColor);
      approximateJobDurationSubPanel.add(approximateJobDurationLabel);
      approximateJobDurationSubPanel.add(approximateJobDuration);
      approximateJobDurationSubPanel.add(jobDurationTimes);

      month.setName("Month");
      month.addKeyListener(jobRequestListener);
      day.setName("Day");
      day.addKeyListener(jobRequestListener);
      year.setName("Year");
      year.addKeyListener(jobRequestListener);
      
      divider.setHorizontalAlignment(JLabel.CENTER);
      divider.setPreferredSize(new Dimension(1, 1));

      divider2.setHorizontalAlignment(JLabel.CENTER);
      divider2.setPreferredSize(new Dimension(1, 1));

      dateSubPanel.add(month);
      dateSubPanel.add(divider);
      dateSubPanel.add(day);
      dateSubPanel.add(divider2);
      dateSubPanel.add(year);

      submit.setName("Submit");
      submit.addActionListener(jobRequestListener);
      submit.setBackground(buttonColor);
      submit.setForeground(textColor);

      back.setName(MAIN_PAGE_NAME);
      back.addActionListener(switcher);
      pageSwitchButtons.add(back);
      back.setBackground(buttonColor);
      back.setForeground(textColor);

      jobTime.setName("Calculate Job Time");
      jobTime.addActionListener(jobRequestListener);
      jobTime.setBackground(buttonColor);
      jobTime.setForeground(textColor);

      jobDeadlineSubPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
      jobDeadlineSubPanel.setBackground(backgroundColor);
      
      jobDeadlineSubPanel.setSize(40, 40);
      jobDeadlineSubPanel.add(jobDeadlineLabel);
      jobDeadlineSubPanel.add(dateSubPanel);

      jobRequestPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 30));
      jobRequestPanel.setBackground(backgroundColor);
      jobRequestPanel.add(header);
      jobRequestPanel.add(jobTitleSubPanel);
      jobRequestPanel.add(jobDescriptionSubPanel);
      jobRequestPanel.add(approximateJobDurationSubPanel);
      jobRequestPanel.add(jobDeadlineSubPanel);
      jobRequestPanel.add(submit);
      jobRequestPanel.add(back);
      jobRequestPanel.add(jobTime);

      mainPanel.setLayout(new BorderLayout());
      mainPanel.setBackground(backgroundColor);
      mainPanel.add(clientIDPanel, BorderLayout.NORTH);
      mainPanel.add(jobRequestPanel, BorderLayout.CENTER);
      frame.add(mainPanel, CREATE_JOB_REQUEST_PAGE_NAME);
      screens.add(CREATE_JOB_REQUEST_PAGE_NAME);
   }
   
   /**
    * Creates the car rental page of the GUI. From this page, users will be able to rent new cars to the Vehicular Cloud System.
    */
   public void createCarRentalPage() { 
      JPanel mainPanel = new JPanel();
      JPanel currentOwnerPanel = new JPanel();
      JPanel carRentalPanel = new JPanel();
      JLabel header = new JLabel("Fill in the following information to lend your car.");
      JPanel makeSubPanel = new JPanel();
      JLabel makeLabel = new JLabel("Enter Make of Vehicle: ");
      JTextField make = new JTextField(20);
      JPanel modelSubPanel = new JPanel();
      JLabel modelLabel = new JLabel("Enter Model of Vehicle: ");
      JTextField model = new JTextField(20);
      JPanel plateSubPanel = new JPanel();
      JLabel plateLabel = new JLabel("Enter Plate Number of Vehicle: ");
      JTextField plate = new JTextField(20);
      JPanel durationSubPanel = new JPanel();
      JLabel durationLabel = new JLabel("Enter Duration of Vehicle Residency: ");
      JTextField duration = new JTextField(10);
      String[] timeOptions = {"days", "months"};
      JComboBox<String> rentDurationTimes = new JComboBox<String>(timeOptions);
      JButton rentCar = new JButton("Rent Car");
      rentCar.setBackground(buttonColor);
      rentCar.setBorderPainted(false);
      rentCar.setOpaque(true);
      JButton back = new JButton("Back");
      back.setBackground(buttonColor);
      back.setBorderPainted(false);
      back.setOpaque(true);

      currentOwnerPanel.setLayout(new BorderLayout());
      currentOwnerPanel.setBackground(backgroundColor);
      currentOwnerPanel.add(currentOwnerId, BorderLayout.WEST);

      make.setName("Car Make");
      make.addKeyListener(rentalRequestListener);
      
      makeSubPanel.setLayout(new BorderLayout(5,0));
      makeSubPanel.setBackground(backgroundColor);
      makeSubPanel.add(makeLabel, BorderLayout.WEST);
      makeSubPanel.add(make, BorderLayout.EAST);

      model.setName("Car Model");
      model.addKeyListener(rentalRequestListener);
      
      modelSubPanel.setLayout(new BorderLayout(5,0));
      modelSubPanel.setBackground(backgroundColor);
      modelSubPanel.add(modelLabel, BorderLayout.WEST);
      modelSubPanel.add(model, BorderLayout.EAST);

      plate.setName("Car Plate Number");
      plate.addKeyListener(rentalRequestListener);
      
      plateSubPanel.setLayout(new BorderLayout(5, 0));
      plateSubPanel.setBackground(backgroundColor);
      plateSubPanel.add(plateLabel, BorderLayout.WEST);
      plateSubPanel.add(plate,BorderLayout.EAST);

      duration.setName("Residency Duration");
      duration.addKeyListener(rentalRequestListener);

      rentDurationTimes.addItemListener(rentalRequestListener);
      
      durationSubPanel.setLayout(new BorderLayout(5, 0));
      durationSubPanel.setBackground(backgroundColor);
      durationSubPanel.add(durationLabel, BorderLayout.WEST);
      durationSubPanel.add(rentDurationTimes, BorderLayout.EAST);
      durationSubPanel.add(duration,BorderLayout.CENTER);
      
      rentCar.addActionListener(rentalRequestListener);
      rentCar.setBackground(buttonColor);
      rentCar.setForeground(textColor);

      back.setName(MAIN_PAGE_NAME);
      back.addActionListener(switcher);
      pageSwitchButtons.add(back);
      back.setBackground(buttonColor);
      back.setForeground(textColor);

      carRentalPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 50));
      carRentalPanel.setBackground(backgroundColor);
      carRentalPanel.add(header);
      carRentalPanel.add(makeSubPanel);
      carRentalPanel.add(modelSubPanel);
      carRentalPanel.add(plateSubPanel);
      carRentalPanel.add(durationSubPanel);
      carRentalPanel.add(rentCar);
      carRentalPanel.add(back);

      mainPanel.setLayout(new BorderLayout());
      
      mainPanel.setBackground(new Color(245, 195, 194));
      mainPanel.setBackground(backgroundColor);
      mainPanel.add(currentOwnerPanel, BorderLayout.NORTH);
      mainPanel.add(carRentalPanel, BorderLayout.CENTER);
      frame.add(mainPanel, CREATE_CAR_RENTAL_PAGE_NAME);
      screens.add(CREATE_CAR_RENTAL_PAGE_NAME);
   }

   /**
    * An interface to clear the fields for any GUI page with forms to fill out.
    * @author Avery Lagredelle
    */
   interface FieldClearer {
      /**
       * Clears any text boxes, text fields, or other text inputs on a given page of a GUI.
       */
      public void clearFields();
   }

   /**
    * This class listens for when a button is pressed that is meant to switch a page. It works together with 
    * the ArrayList of page switch buttons in the main GUI class, and uses the name of the button to pull up 
    * the corresponding page in the ArrayList of page names in the main GUI class.
    */
   class PageSwitcher implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         String requestedPage = "";

         for(int i = 0; i < pageSwitchButtons.size(); i++) {
            if(e.getSource().equals(pageSwitchButtons.get(i))) {
               requestedPage = pageSwitchButtons.get(i).getName();
            }
         }

         for(int i = 0; i < screens.size(); i++) {
            if(requestedPage.equals(screens.get(i))) {

               if(requestedPage.equals(CREATE_JOB_REQUEST_PAGE_NAME)) {
                  currentClientId.setText("     Client ID: " + currentUser.getUsername());
                  jobRequestListener.clearFields();
               }

               if(requestedPage.equals(CREATE_CAR_RENTAL_PAGE_NAME)) {
                  currentOwnerId.setText("     Owner ID: " + currentUser.getUsername());
                  rentalRequestListener.clearFields();
               }

               ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), screens.get(i));
               verifier.clearFields();
            }
         }
      }
   }

   /**
    * This class is used to verify users who log in or sign up for the first time. It listens for when a user is signing up or 
    * logging in and checks to make sure that their log in information is correct before logging them in or checks to make sure 
    * that the information they are signing up with doesn't already have an account tied to it.
    */
   class UserVerifier extends User implements ActionListener, KeyListener, FieldClearer {
      private JTextField usernameBox;
      private JPasswordField passwordBox;

      /**
       * Initializes UserVerifier object.
       */
      public UserVerifier() {
         super();
         usernameBox = new JTextField();
         passwordBox = new JPasswordField();
      }

      public boolean accountExists(String username) {
         try {
            outputStream.writeUTF("database isUser");
            if(inputStream.readUTF().equals("send username"))
               outputStream.writeUTF(username);
            return inputStream.readBoolean();
         }
         catch(IOException e) {
            System.out.println("An error occurred");
            return false;
         }
      }

      public boolean accountExists(String username, String password) {
         try {
            outputStream.writeUTF("database accountFound");
            if(inputStream.readUTF().equals("send username and password")) {
               outputStream.writeUTF(username + "," + password);
            }
            return inputStream.readBoolean();
         }
         catch(IOException e) {
            return false;
         }
      }

      public boolean addUserToDatabase(String username, String password) {
         try {
            outputStream.writeUTF("database addUser");
            if(inputStream.readUTF().equals("send username and password")) {
               outputStream.writeUTF(username + "," + password);
            }
            return inputStream.readBoolean();
         }
         catch(IOException e) {
            return false;
         }
      }

      public boolean recordNewLogin(String username) {
         try {
            outputStream.writeUTF("recordNewLogin");
            if(inputStream.readUTF().equals("send username")) {
               outputStream.writeUTF(username);
            }
            return inputStream.readBoolean();
         }
         catch(IOException e) {
            System.out.println("An error occurred");
            return false;
         }
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         if(((JButton)e.getSource()).getText().equals("Sign Up")) {
            if(!this.getUsername().equals("") && !this.getPassword().equals("") && !accountExists(this.getUsername())) {
               currentUser = new User(this.getUsername(), this.getPassword());
               currentUserIsNew = true;
               addUserToDatabase(currentUser.getUsername(), currentUser.getPassword());
               clearFields();
               currentUserId.setText("     User ID: " + currentUser.getUsername());
               showMainPage();
            }
            else {
               System.out.println("An error occurred, please try again.");
               infoBoxMessage.setText("An error occurred. Please try again.");
               infoBox.setVisible(true);
            }
         }
         else if(((JButton)e.getSource()).getText().equals("Login")) {
            if(accountExists(this.getUsername(), this.getPassword())) {
               currentUser = new User(this.getUsername(), this.getPassword());
               currentUserIsNew = false;
               recordNewLogin(currentUser.getUsername());
               clearFields();
               currentUserId.setText("     User ID: " + currentUser.getUsername());
               showMainPage();
            }
            else {
               System.out.println("Account not found. Please try again or create a new account.");
               infoBoxMessage.setText("Account not found. Please try again.");
               infoBox.setVisible(true);
            }
         }
      }

      /**
       * Shows the main page of the GUI.
       */
      public void showMainPage() {
         ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), MAIN_PAGE_NAME);
      }

      @Override
      public void keyTyped(KeyEvent e) {
         //Currently Unneeded        
      }

      @Override
      public void keyPressed(KeyEvent e) {
         //Currently Unneeded
      }

      @Override
      public void keyReleased(KeyEvent e) {
         if(e.getSource().getClass().getSimpleName().equals("JTextField")) { 
            usernameBox = (JTextField)e.getSource(); 
            this.setUsername(((JTextField)e.getSource()).getText());;
         }
         if(e.getSource().getClass().getSimpleName().equals("JPasswordField")) {
            passwordBox = (JPasswordField)e.getSource();
            this.setPassword(String.valueOf(((JPasswordField)e.getSource()).getPassword()));
         }
      }

      @Override
      public void clearFields() {
         usernameBox.setText("");
         passwordBox.setText("");

         this.setUsername("");
         this.setPassword("");
      }
   }

   /**
    * This class is used for when users submit new jobs to the Vehicular Cloud System. It listens for events that take place 
    * on the job request page, such as the submit button being pressed, the fields being filled in, or a request for the job 
    * completion time being made.
    */
   class JobRequestListener extends Job implements KeyListener, ActionListener, ItemListener, FieldClearer {
      private boolean timeChoiceHours = true;
      private boolean jobTimeCompletionChecked = false;
      private String month = "";
      private String day = "";
      private String year = "";

      private JTextField titleBox;
      private JTextArea descriptionBox;
      private JTextField durationTimeBox;
      private JTextField monthBox;
      private JTextField dayBox;
      private JTextField yearBox;

      /**
       * Initializes the JobRequestListener object.
       */
      public JobRequestListener() {
         titleBox = new JTextField();
         descriptionBox = new JTextArea();
         durationTimeBox = new JTextField();
         monthBox = new JTextField();
         dayBox = new JTextField();
         yearBox = new JTextField();
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         
         if(timeChoiceHours && !jobTimeCompletionChecked)
            this.setDurationTime(this.getDurationTime() * 60);

         if(!this.getTitle().equals("") && !this.getDescription().equals("") && this.getDurationTime() > 0 && 
         !month.equals("") && !day.equals("") && !year.equals("")) {
            
            String deadline = year + "-" + month + "-" + day;
            this.setDeadline(LocalDate.parse(deadline));

            Client thisClient;
            // if(database.isClient(currentUser.getUsername())) {
            //    //thisClient = database.getClient(currentUser.getUsername());
            // }
            // else {
            //    thisClient = new Client(currentUser.getUsername(), currentUser.getPassword());
            // }

            Job newJob = new Job(this.getTitle(), this.getDescription(), this.getDurationTime(), this.getDeadline());

            if(((JButton)e.getSource()).getName().equals("Calculate Job Time")) {
               jobTimeCompletionChecked = true;
               //infoBoxMessage.setText("Completion Time: " + controller.calculateJobCompletionTime(newJob) + " minutes from app start");
               infoBox.setVisible(true);
            }
            else {
               //thisClient.submitJob(newJob, controller);

               // if(!database.isClient(thisClient.getUsername())) {
               //    //database.addClient(thisClient);
               // }

               //database.updateDatabase("New Job Submitted", thisClient);
               clearFields();
               jobTimeCompletionChecked = false;
               System.out.println("Job submitted successfully");
               infoBoxMessage.setText("Job submitted successfully!");
               infoBox.setVisible(true);
            }
         }
         else {
            System.out.println("An error occurred. Please ensure you filled out all of the text boxes correctly.");
            infoBoxMessage.setText("An error occurred. Please check inputs.");
            infoBox.setVisible(true);
         }
      }

      public boolean sendJobRequest(String jobTitle, String jobDescription, int jobDurationTime, String deadline, String username){
         try{
            outputStream.writeUTF("database sendJobRequest");
            if(inputStream.readUTF().equals("send job fields")){
               outputStream.writeUTF(jobTitle + "," + jobDescription + "," + jobDurationTime + "," + deadline + "," + username);
            }
            return inputStream.readBoolean();
         } catch (IOException e) {
            return false;
         }
      }

      @Override
      public void keyTyped(KeyEvent e) {
         //Currently Unneeded
      }

      @Override
      public void keyPressed(KeyEvent e) {
         //Currently Unneeded
      }

      @Override
      public void keyReleased(KeyEvent e) {
         switch(e.getSource().getClass().getSimpleName()) {
            
            case "JTextArea": {
               descriptionBox = (JTextArea)e.getSource();
               this.setDescription(((JTextArea)e.getSource()).getText());
               break;
            }

            case "JTextField": {
               switch(((JTextField)e.getSource()).getName()) {
                  
                  case "Job Title": {
                     titleBox = (JTextField)e.getSource();
                     this.setTitle(((JTextField)e.getSource()).getText());
                     break;
                  }
                  case "Job Duration Time": {
                     durationTimeBox = (JTextField)e.getSource();
                     try {
                        int time = Integer.parseInt(((JTextField)e.getSource()).getText());
                        this.setDurationTime(time);
                     } 
                     catch(NumberFormatException n) {
                        this.setDurationTime(-1);
                     }
                     break;
                  }
                  case "Month": {
                     monthBox = (JTextField)e.getSource();
                     month = ((JTextField)e.getSource()).getText();
                     break;
                  }
                  case "Day": {
                     dayBox = (JTextField)e.getSource();
                     day = ((JTextField)e.getSource()).getText();
                     break;
                  }
                  case "Year": {
                     yearBox = (JTextField)e.getSource();
                     year = ((JTextField)e.getSource()).getText();
                     break;
                  }
               }
               break;
            }
         }
      }

      @Override
      public void itemStateChanged(ItemEvent e) {
         timeChoiceHours = ((String)((JComboBox)e.getSource()).getSelectedItem()).equals("hours");
      }

      @Override
      public void clearFields() {
         titleBox.setText("");
         descriptionBox.setText("");
         durationTimeBox.setText("");
         monthBox.setText("");
         dayBox.setText("");
         yearBox.setText("");

         this.setTitle("");
         this.setDescription("");
         this.setDurationTime(0);
         this.setDeadline(LocalDate.parse("2000-01-01"));
      }
   }

   /**
    * This class is used for when users  rent new vehicles to the Vehicular Clous System. It listens for events that take place 
    * on the car rental page, such as the fields being filled out or the submit button being pressed.
    */
   class CarRentalRequestListener extends Vehicle implements KeyListener, ActionListener, ItemListener, FieldClearer {
      private boolean monthsSelected = false;
      private JTextField makeBox;
      private JTextField modelBox;
      private JTextField plateNumberBox;
      private JTextField residencyBox;

      /**
       * Initializes the CarRentalRequestListener object.
       */
      public CarRentalRequestListener() {
         makeBox = new JTextField();
         modelBox = new JTextField();
         plateNumberBox = new JTextField();
         residencyBox = new JTextField();
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         
         if(monthsSelected) {
            this.setResidency(this.getResidency() * 30);
         }

         if(!this.getMake().equals("") && !this.getModel().equals("") && 
         !this.getLicensePlateNumber().equals("") && this.getResidency() > 0) {

            Owner thisOwner;

            // if(database.isOwner(currentUser.getUsername())) {
            //    //thisOwner = database.getOwner(currentUser.getUsername());
            // }
            // else {
            //    thisOwner = new Owner(currentUser.getUsername(), currentUser.getPassword());
            // }

            Vehicle newRental = new Vehicle(this.getMake(), this.getModel(), this.getLicensePlateNumber(), this.getResidency());
            //thisOwner.rentVehicle(newRental, controller);

            // if(!database.isOwner(thisOwner.getUsername())) {
            //    //database.addOwner(thisOwner);
            // }

            //database.updateDatabase("New Rental Added", thisOwner);
            clearFields();
            System.out.println("Car Rented Successfully");
            infoBoxMessage.setText("Car rented successfully!");
            infoBox.setVisible(true);
         }
         else {
            System.out.println("An error occurred. Please try again. Be sure to fill out all fields correctly.");
            infoBoxMessage.setText("An error occurred. Please check inputs.");
            infoBox.setVisible(true);
         }
      }

      @Override
      public void keyTyped(KeyEvent e) {
         //Currently Unneeded
      }

      @Override
      public void keyPressed(KeyEvent e) {
         //Currently Unneeded
      }

      @Override
      public void keyReleased(KeyEvent e) {
         if(e.getSource().getClass().getSimpleName().equals("JTextField")) {
            switch(((JTextField)e.getSource()).getName()) {
               case "Car Make": {
                  makeBox = (JTextField)e.getSource();
                  this.setMake(((JTextField)e.getSource()).getText());
                  break;
               }
               case "Car Model": {
                  modelBox = (JTextField)e.getSource();
                  this.setModel(((JTextField)e.getSource()).getText());
                  break;
               }
               case "Car Plate Number": {
                  plateNumberBox = (JTextField)e.getSource();
                  this.setLicensePlateNumber(((JTextField)e.getSource()).getText());
                  break;
               }
               case "Residency Duration": {
                  residencyBox = (JTextField)e.getSource();
                  try {
                     int residencyTime = Integer.parseInt(((JTextField)e.getSource()).getText());
                     this.setResidency(residencyTime);
                  }
                  catch(NumberFormatException n) {
                     this.setResidency(-1);
                  }
                  break;
               }
            }
         }
      }

      @Override
      public void itemStateChanged(ItemEvent e) {
         monthsSelected = ((String)((JComboBox)e.getSource()).getSelectedItem()).equals("months");
      }

      @Override
      public void clearFields() {
         makeBox.setText("");
         modelBox.setText("");
         plateNumberBox.setText("");
         residencyBox.setText("");

         this.setMake("");
         this.setModel("");
         this.setLicensePlateNumber("");
         this.setResidency(0);
      }
   }
}

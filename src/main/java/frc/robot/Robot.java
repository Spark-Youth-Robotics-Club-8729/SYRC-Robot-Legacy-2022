// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
// import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.cscore.UsbCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "Custom Auto";
  private static final String kCustomAuto2 = "Custom Auto 2";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  
  //CAN Pin Constants
  private final int m_storageDevice = 1;
  private final int m_shooterDevice = 2;
  private final int m_intakeDevice = 3;
  private final int m_leftleadDevice = 4;
  private final int m_rightleadDevice = 5;
  private final int m_leftbackDevice = 6;
  private final int m_rightbackDevice = 11;
  private final int m_hanger3 = 10;
  private final int m_hanger4 = 12;
  // private final int m_hanger5 = 12;
  // private final int m_hanger6 = 13;

  //Joystick
  private final Joystick m_xbox = new Joystick(0);//MAKE SURE IN DRIVERSTATION CONTROLLER IS ON 0.
  private final Joystick m_stick = new Joystick(1);//MAKE SURE IN DRIVERSTATION CONTROLLER IS ON 1.
  private final Joystick m_test = new Joystick(2);//MAKE SURE IN DRIVERSTATION CONTROLLER IS ON 2.
  private final Joystick m_test2 = new Joystick(3);//MAKE SURE IN DRIVERSTATION CONTROLLER IS ON 2.


  //Hanging
  private final WPI_VictorSPX OuterLeftClimber = new WPI_VictorSPX(m_hanger3);
  private final WPI_VictorSPX OuterRightClimber = new WPI_VictorSPX(m_hanger4);
  // private final WPI_VictorSPX InnerClimberLateral = new WPI_VictorSPX(m_hanger5);
  // private final WPI_VictorSPX OuterClimberLateral = new WPI_VictorSPX(m_hanger6);
  
  //DifferentialDrive
  private final WPI_VictorSPX frontLeft = new WPI_VictorSPX(m_leftleadDevice);
  private final WPI_VictorSPX frontRight = new WPI_VictorSPX(m_rightleadDevice);
  private final WPI_VictorSPX backRight = new WPI_VictorSPX(m_rightbackDevice);
  private final WPI_VictorSPX backLeft = new WPI_VictorSPX(m_leftbackDevice);
  private final MotorControllerGroup m_left = new MotorControllerGroup(frontLeft, frontRight);
  private final MotorControllerGroup m_right = new MotorControllerGroup(backLeft, backRight);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_left, m_right);

  //Intake
  private final CANSparkMax m_intake = new CANSparkMax(m_intakeDevice, MotorType.kBrushless);

  //Storage/Shooter
  private final CANSparkMax m_shooter = new CANSparkMax(m_shooterDevice, MotorType.kBrushless);
  private final CANSparkMax m_storage = new CANSparkMax(m_storageDevice, MotorType.kBrushless);
  
  // Camera, defined as global variable, change later if necessary
  // private UsbCamera camera = null;

  // Autonomous Variables
  private final Timer timer = new Timer();
  private int phase = 0;
  
  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Reset Shooter and Storage motors
    m_shooter.restoreFactoryDefaults();
    m_storage.restoreFactoryDefaults();

    m_left.setInverted(true);

    // Camera
    // camera = CameraServer.startAutomaticCapture();
    // camera.setResolution(320, 240);

    // Default
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Custom Auto", kCustomAuto);
    m_chooser.addOption("Custom Auto 2", kCustomAuto2);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and
   * test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different
   * autonomous modes using the dashboard. The sendable chooser code works with
   * the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
   * chooser code and
   * uncomment the getString line to get the auto name from the text box below the
   * Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure
   * below with additional strings. If using the SendableChooser make sure to add
   * them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    timer.reset();
    timer.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto2:
        if (phase == 0) {
          if (timer.get() < 3.8) {
            if (timer.get() < 1.0) {
              m_robotDrive.arcadeDrive(0.55, 0.0);
            }
            else if (timer.get() < 2.5){
              m_robotDrive.arcadeDrive(0.0, 0.0);
            }
            else {
              m_robotDrive.arcadeDrive(0.55, 0.0);
            }
          } else {
            phase++;
            timer.reset();
          }
        }
        if (phase == 1) {
          if (timer.get() < 1.0) {
            m_shooter.set(0.575);
          } else {
            phase++;
            timer.reset();
          }
        }
        if (phase == 2) {
          if (timer.get() < 1.3) {
            m_robotDrive.arcadeDrive(-0.55, 0.0);
          } else {
            phase++;
            timer.reset();
            m_robotDrive.arcadeDrive(0.0, 0.0);
          }
        }
        if (phase == 3) {
          if (timer.get() < 3.0) {
            if (timer.get() > 1.0) {
              m_storage.set(-0.95);
            }
          } else {
            phase++;
            m_shooter.set(0.0);
            m_storage.set(0.0);
            timer.reset();
            m_robotDrive.arcadeDrive(0.0, 0.6);

          }
        }
        break;
      case kCustomAuto:
        if (phase == 0) {
          if (timer.get() < 2.3) {
            if (timer.get() < 0.8) {
              m_shooter.set(0.575);
              m_robotDrive.arcadeDrive(0.55, 0.0);
            }
            else if (timer.get() < 1.8){
              m_robotDrive.arcadeDrive(0.0, 0.0);
              m_storage.set(-0.95);
            }
            else {
              m_robotDrive.arcadeDrive(0.55, 0.0);
              m_intake.set(0.90);
              m_storage.stopMotor();
            }
          } else {
            phase = 1;
            timer.reset();
          }
        }
        if (phase == 1) {
          if (timer.get() < 1.4) {
            m_shooter.set(0.575);
          } else {
            phase = 2;
            timer.reset();
          }
        }
        if (phase == 2) {
          if (timer.get() < 0.25) {
            m_robotDrive.arcadeDrive(-0.55, 0.0);
          }
          else {
            phase = 3;
            timer.reset();
            m_intake.set(0.0);
            m_robotDrive.arcadeDrive(0, 0);
          }
        }
        if (phase == 3) {
          if (timer.get() < 3.0) {
            if (timer.get() > 1.0) {
              m_storage.set(-0.95);
            }
          }
          else {
            m_storage.stopMotor();
            m_shooter.stopMotor();
            phase = 4 ;
            m_robotDrive.arcadeDrive(0.0, 0.6);

          }
        }
        break;
      case kDefaultAuto:
      default:
        if (phase == 0) {
          if (timer.get() < 3.8) {
            if (timer.get() < 1.0) {
              m_shooter.set(0.575);
              m_robotDrive.arcadeDrive(0.55, 0.0);
            }
            else if (timer.get() < 2.5){
              m_robotDrive.arcadeDrive(0.0, 0.0);
              m_storage.set(-0.95);
            }
            else {
              m_robotDrive.arcadeDrive(0.55, 0.0);
              m_intake.set(0.90);
              m_storage.stopMotor();
            }
          }
          else {
            phase++;
            timer.reset();
          }
        }
        if (phase == 1) {
          if (timer.get() < 1.0) {
            m_shooter.set(0.575);
          } else {
            phase++;
            timer.reset();
          }
        }
        if (phase == 2) {
          if (timer.get() < 1.3) {
            m_robotDrive.arcadeDrive(-0.55, 0.0);
          }
        else {
          phase = 3;
          timer.reset();
          m_intake.set(0.0);
          m_robotDrive.arcadeDrive(0.0, 0.0);
         }
      }
      if (phase == 3) {
        if (timer.get() < 3.0) {
         if (timer.get() > 1.0) {
            m_storage.set(-0.95);
          }
       }
       else {
         phase=4;
         m_shooter.set(0.0);
         m_storage.set(0.0);
         timer.reset();
        }
      }

      if (phase == 4) {
        m_robotDrive.arcadeDrive(0.0, 0.6);
      }
      break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    m_robotDrive.arcadeDrive(0.0, 0.0);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    
    Intake();
    Hanging1();
    Shooting();
    Movement();
    Storage();

  }
  
  public void Shooting() {
  if (m_xbox.getRawButton(5))
  {
       m_shooter.set(0.585);
  }
  if (m_xbox.getRawButton(7))
  {
       m_shooter.set(0.50);
  
  }if (m_xbox.getRawButton(4))
  {
       m_shooter.set(0.0);
  }
}

  public void Movement() {
  
    double xDrive = -(m_stick.getRawAxis(4));
    double yDrive = -(m_stick.getRawAxis(1));
    m_robotDrive.arcadeDrive(yDrive, xDrive);

    if (m_test2.getRawButton(1)) {
      frontLeft.set(0.80);
    }

    if (m_test2.getRawButton(2)) {
      frontRight.set(0.80);
    }

    if (m_test2.getRawButton(3)) {
      backLeft.set(0.80);
    }

    if (m_test2.getRawButton(4)) {
      backRight.set(0.80);
    }

    if (m_test2.getRawButton(5)) {
      frontLeft.stopMotor();
      frontRight.stopMotor();
      backLeft.stopMotor();
      backRight.stopMotor();
    }
  
  }
  
  public void Storage() {
  
    if (m_xbox.getRawButton(6)) {
      m_storage.set(-0.95); 
    } 
  
    if (m_xbox.getRawButton(8)) { 
      m_storage.set(-0.65); 
    }
    
    if (m_xbox.getRawButton(2)) {
      m_storage.set(0.0);
    }
  
  }
  


  //Intake function to be called in teleop
  public void Intake() {

    if (m_xbox.getRawButtonPressed(1)) {
        m_intake.set(0.80);   
      }
      
    if (m_xbox.getRawButtonPressed(3)) {
        m_intake.set(0.0);   
      }
      
    if (m_xbox.getRawButtonPressed(9)) {
        m_intake.set(-0.95);   
      }
    
  }

  public void Hanging1()
  {
    OuterLeftClimber.set(m_xbox.getRawAxis(1));
    OuterRightClimber.set(m_xbox.getRawAxis(1));

    if (m_test.getRawButton(4)) {
      OuterLeftClimber.set(0.90);
    }

    if (m_test.getRawButton(2)) {
      OuterLeftClimber.set(-0.90);
    }

    if (m_test.getRawButton(5)) {
      OuterLeftClimber.set(0.0);
    }

    if (m_test.getRawButton(3)) {
      OuterRightClimber.set(0.90);
    }

    if (m_test.getRawButton(1)) {
      OuterRightClimber.set(-0.90);
    }

    if (m_xbox.getRawButton(10)) 
    {
      OuterLeftClimber.set(0.0);
      OuterRightClimber.set(0.0);
    }
    
    if (m_xbox.getRawButton(11)) 
    {
      OuterLeftClimber.set(1.0);
      OuterRightClimber.set(1.0);
    }
    
    if (m_xbox.getRawButton(12)) 
    {
      OuterLeftClimber.set(-1.0);
      OuterRightClimber.set(-1.0);
    }
  }
  
  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
  
}
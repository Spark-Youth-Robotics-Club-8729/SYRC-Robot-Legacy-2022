// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

#pragma once

//Libraries
#include "AHRS.h"
#include <string>
#include <frc/Joystick.h>
#include <frc/TimedRobot.h>
#include <frc/smartdashboard/SendableChooser.h>
#include <frc/drive/DifferentialDrive.h>
#include "rev/CANSparkMax.h"
#include <frc/smartdashboard/SmartDashboard.h>
#include <frc/Encoder.h>
#include <frc/AnalogInput.h>
#include <frc/DigitalOutput.h>
#include "frc/RobotController.h"
#include "rev/ColorSensorV3.h"
#include "rev/ColorMatch.h"
#include <frc/util/color.h>
#include <frc/AnalogGyro.h>
#include "cameraserver/CameraServer.h"
#include "frc/motorcontrol/PWMVictorSPX.h"
#include "ctre/Phoenix.h"
#include "networktables/NetworkTable.h"
#include "networktables/NetworkTableInstance.h"
#include "networktables/NetworkTableEntry.h"
#include "networktables/NetworkTableValue.h"
#include "wpi/span.h"
#include "frc/PneumaticsBase.h"
#include "frc/PneumaticsModuleType.h"
#include <frc/DoubleSolenoid.h>
#include <frc/PneumaticsControlModule.h>
#include <frc/Compressor.h>
#include <frc/motorcontrol/MotorControllerGroup.h>
#include <rev/CANEncoder.h>


class Robot : public frc::TimedRobot {

public:

  //Functions
  void RobotInit() override;
  void RobotPeriodic() override;
  void AutonomousInit() override;
  void AutonomousPeriodic() override;
  void TeleopInit() override;
  void TeleopPeriodic() override;
  void DisabledInit() override;
  void DisabledPeriodic() override;
  void TestInit() override;
  void TestPeriodic() override;

private:

  //CAN Pin Constants
  static const int storageID = 1;
  static const int shooterID = 2;
  static const int intakeDeviceID = 3;
  static const int leftLeadDeviceID = 4;
  static const int rightLeadDeviceID = 5;
  static const int leftBackDeviceID = 6;
  static const int rightBackDeviceID = 7;
  static const int Hanger3ID = 10; 
  static const int Hanger4ID = 11;
  static const int Hanger5ID = 12;
  static const int Hanger6ID = 13;



  //Encoder Pin Constants
  static const int EncoderPin1A = 0;
  static const int EncoderPin1B = 1;
  static const int EncoderPin2A = 2;
  static const int EncoderPin2B = 3;

  //Joystick
  frc::Joystick m_xbox{ 0 }; //MAKE SURE IN DRIVERSTATION CONTROLLER IS ON 0.
  frc::Joystick m_stick{ 1 }; //MAKE SURE IN DRIVERSTATION CONTROLLER IS ON 1.
  frc::Joystick m_test{ 2 }; //MAKE SURE IN DRIVERSTATION CONTROLLER IS ON 2.
  frc::Joystick m_test1{ 3 }; //MAKE SURE IN DRIVERSTATION CONTROLLER IS ON 3.

  //Hanging
  WPI_VictorSPX OuterLeftClimber = {Hanger3ID};
  WPI_VictorSPX OuterRightClimber = {Hanger4ID};
  WPI_VictorSPX InnerClimberLateral = {Hanger5ID};
  WPI_VictorSPX OuterClimberLateral = {Hanger6ID};


  //DifferentialDrive
  WPI_VictorSPX frontLeft = {leftLeadDeviceID};
  WPI_VictorSPX frontRight = {rightLeadDeviceID};
  WPI_VictorSPX backRight = {rightBackDeviceID};
  WPI_VictorSPX backLeft = {leftBackDeviceID};
  frc::MotorControllerGroup m_left{frontLeft, backLeft};
  frc::MotorControllerGroup m_right{frontRight, backRight};
  frc::DifferentialDrive m_robotDrive{m_left, m_right};

  //Intake
  rev::CANSparkMax intake {intakeDeviceID, rev::CANSparkMax::MotorType::kBrushed};

  //Storage/Shooter
  rev::CANSparkMax m_shooter{shooterID, rev::CANSparkMax::MotorType::kBrushless};
  rev::CANSparkMax m_storage{storageID, rev::CANSparkMax::MotorType::kBrushless};

  //Camera
  double targetOffsetAngle_Horizontal = 0.0;
  double targetOffsetAngle_Vertical = 0.0;
  double targetArea = 0.0;

  //Encoder Set Up
  frc::Encoder m_encoder1{ EncoderPin1A, EncoderPin1B, true };
  frc::Encoder m_encoder2{ EncoderPin2A, EncoderPin2B, false };
  float encoderAverage;
  rev::SparkMaxRelativeEncoder m_ShooterEncoder = m_shooter.GetEncoder();
  rev::SparkMaxRelativeEncoder m_FeederEncoder = m_storage.GetEncoder();

  // Autonomous Variables
  int time;
  int phase;
  
  //Teleop Periodic
  void Camera();
  void Intake();
  void Storage();
  void Outtake();
  void Movement();
  void Hanging1();
  void SmartDashboard();

  //Default
  frc::SendableChooser<std::string> m_chooser;
  const std::string kAutoNameDefault = "2 ball auto";
  const std::string kAutoNameCustom1 = "2 ball auto SIDE";
  const std::string kAutoNameCustom2 = "Do nothing";
  std::string m_autoSelected;
};
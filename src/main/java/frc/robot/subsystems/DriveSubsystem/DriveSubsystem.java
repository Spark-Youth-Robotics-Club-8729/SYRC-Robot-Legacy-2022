package frc.robot.subsystems.DriveSubsystem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Message;
import frc.robot.RobotManager;

public class DriveSubsystem implements Subsystem {
  private final DifferentialDrive robotDrive;
  private final Encoder encoderLeft;
  private final Encoder encoderRight;
  private final RobotManager robot;

  public DriveSubsystem(Encoder encoderLeft, Encoder encoderRight, DifferentialDrive robotDrive, RobotManager robot) {
    this.robotDrive = robotDrive;

    this.encoderLeft = encoderLeft;
    this.encoderRight = encoderRight;

    this.encoderLeft.setDistancePerPulse((3.14159265358 * 6) / 360.0);
    this.encoderRight.setDistancePerPulse((3.14159265358 * 6) / 360.0);
    this.encoderLeft.reset();
    this.encoderRight.reset();

    this.robot = robot;
  }

  public void reset() {
    encoderLeft.reset();
    encoderRight.reset();
    tankDrive(0.0, 0.0);
  }

  public double getLeftDistance() {
    return encoderLeft.getDistance();
  }

  public double getRightDistance() {
    return encoderRight.getDistance();
  }

  public double getError() {
    return (encoderLeft.getDistance() - encoderRight.getDistance());
  }

  public double getAvgDistance() {
    return (encoderLeft.getDistance() + encoderRight.getDistance()) / 2.0;
  }

  public void tankDrive(double speedLeft, double speedRight) {
    robotDrive.tankDrive(speedLeft, speedRight);
  }

  public double getTime() {
    return robot.getTime();
  }

    public Command parseMessage(Message message) {
    if (message.movementType == Message.MOVE) {
      if (message.measureType == Message.DISTANCE) {
        return new DriveDistance(this, message.measure, message.speed);
      } else if (message.measureType == Message.DURATION) {
          return new DriveDuration(this, message.measure, message.speed);
      } else {
          throw new IllegalArgumentException("Oh NOES message parsing broke OwO");
      }
    } else if (message.movementType == Message.TURN) {
        throw new IllegalArgumentException("Erm awkward, turning doesn't really exist yet");
    }
    return null;
  }

  public Command parseStringMessage(String message) {
      try {
          return parseMessage(new Message(message));
      } catch (ParseException e) {
          throw new RuntimeException(e);
      }
  }
  public Command genDriveSeqCommand(String... messages) {
      return new SequentialCommandGroup((Command[]) Arrays.stream(messages).map(this::parseStringMessage).toArray());
  }
}

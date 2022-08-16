package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import frc.robot.subsystems.DriveSubsystem.DriveSubsystem;

/**
 * The robot manager is designed to track the robot's internal state
 * and function as a wrapper for most robot communication
 */
public class RobotManager {
    private final DriveSubsystem drive;
    private final Timer timer;

    public RobotManager() {
        this.timer = new Timer();

        this.timer.reset();
        this.timer.start();


        MotorControllerGroup motorsLeft = new MotorControllerGroup(
                new PWMSparkMax(Constants.LEFT_MOTOR_0),
                new PWMSparkMax(Constants.LEFT_MOTOR_1));
        Encoder encoderLeft = new Encoder(Constants.LEFT_ENCODER_A, Constants.LEFT_ENCODER_B);
        MotorControllerGroup motorsRight = new MotorControllerGroup(
                new PWMSparkMax(Constants.RIGHT_MOTOR_0),
                new PWMSparkMax(Constants.RIGHT_MOTOR_1));
        Encoder encoderRight = new Encoder(Constants.RIGHT_ENCODER_A, Constants.RIGHT_ENCODER_B);

        this.drive = new DriveSubsystem(encoderLeft, encoderRight, new DifferentialDrive(motorsLeft, motorsRight), this);
    }

    public double getTime() {
        return timer.get();
    }

    public void resetTime() {
        timer.reset();
        timer.start();
    }

    public DriveSubsystem getDrive() {
        return drive;
    }
}

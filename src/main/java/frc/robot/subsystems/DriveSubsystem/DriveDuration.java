package frc.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.HashSet;
import java.util.Set;

public class DriveDuration implements Command {
  private final DriveSubsystem drive;
  private final double duration;
  private final double speed;

  private double travelled;
  private double error;

  /**
   * Constant variables for managing how the robot stabilizes.
   * The convergence rate determines how much the robot will
   * try to correct errors, too low and errors won't be corrected, too high and
   * the robot will overshoot.
   * The variable epsilon determines how much accuracy the robot requires, the
   * lower it is, the more accuracy will be necessary before moving to the next
   * stage.
   */
  private static final double CONVERGENCE_RATE = 1.0;
  private static final double EPSILON = 1.0;

  public DriveDuration(DriveSubsystem drive, double seconds, double speed) {
    this.drive = drive;
    this.duration = seconds;
    this.speed = speed;
    this.travelled = 0.0;

    requirements = new HashSet<>();
    requirements.add(drive);
  }

  public DriveDuration(DriveSubsystem drive, double seconds) {
    this(drive, seconds, 0.5);
  }

  @Override
  public void initialize() { drive.reset(); }

  @Override
  public void execute() {
    error = drive.getError();
    travelled = drive.getTime();

    if (travelled < duration)
      drive.tankDrive(speed - CONVERGENCE_RATE * error, speed + CONVERGENCE_RATE * error);
    else
      drive.tankDrive(-CONVERGENCE_RATE * error, CONVERGENCE_RATE * error);
  }

  @Override
  public void end(boolean interrupted) {
      drive.tankDrive(0.0, 0.0);
  }

  @Override
  public boolean isFinished() {
      return (travelled >= duration && Math.abs(error) < EPSILON);
  }

  private final Set<Subsystem> requirements;
  @Override
  public Set<Subsystem> getRequirements() {
      return requirements;
  }
}

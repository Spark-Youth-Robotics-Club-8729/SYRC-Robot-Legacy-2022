package frc.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.HashSet;
import java.util.Set;

public class DriveDistance implements Command {
  private final DriveSubsystem drive;
  private final double distance;
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

  public DriveDistance(DriveSubsystem drive, double inches, double speed) {
    this.drive = drive;
    this.distance = inches;
    this.speed = speed;
    this.travelled = 0.0;

    requirements = new HashSet<>();
    requirements.add(drive);
  }

  public DriveDistance(DriveSubsystem drive, double inches) {
    this(drive, inches, 0.5);
  }

  @Override
  public void initialize() {
    drive.reset();
  }

  /**
   * This function moves a set distance at a user given speed
   * The robot's movement is also stabilized to ensure that it moves in
   * a relatively straight line, regulated by the constants CONVERGENCE_RATE and
   * EPSILON.
   * If the robot has completed it's task of moving to the right spot and has
   * travelled
   * in a relatively straight line, the function will return false to indicate
   * that it has completed movement.
   *
   */
  @Override
  public void execute() {
    error = drive.getError();
    travelled = drive.getAvgDistance();

    if (travelled < distance)
      drive.tankDrive(speed - CONVERGENCE_RATE * error, speed + CONVERGENCE_RATE * error);
    else
      drive.tankDrive(-CONVERGENCE_RATE * error, CONVERGENCE_RATE * error);
  }

    /**
     * Is run when function ends, either by interruption or naturally. Currently, stops robot.
     * @param interrupted whether the command was interrupted/canceled
     */
  @Override
  public void end(boolean interrupted) {
    drive.tankDrive(0, 0);
  }

  @Override
  public boolean isFinished() {
    return (travelled >= distance && Math.abs(error) < EPSILON);
  }

  private final Set<Subsystem> requirements;
  @Override
  public Set<Subsystem> getRequirements() {
      return requirements;
  }
}

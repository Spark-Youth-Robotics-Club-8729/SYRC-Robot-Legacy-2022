package frc.robot;

import java.text.ParseException;
import java.util.Objects;

public class Message {
  public static final int MOVE = 0;
  public static final int TURN = 1;
  public static final int DISTANCE = 0;
  public static final int DURATION = 1;

  public int movementType;
  public int measureType;
  public double measure;
  public double speed;

  public Message(int movementType, int measureType, double measure) {
    this.movementType = movementType;
    this.measureType = measureType;
    this.measure = measure;
    this.speed = 0.5;
  }

  public Message(int movementType, int measureType, double measure, double speed) {
    this.movementType = movementType;
    this.measureType = measureType;
    this.measure = measure;
    this.speed = speed;
  }

  public Message(String message) throws ParseException {
    String[] parts = message.toLowerCase().split(" ");

    if (Objects.equals(parts[0], "move"))
      this.movementType = MOVE;
    else if (Objects.equals(parts[0], "rotate") || Objects.equals(parts[0], "turn"))
      this.movementType = TURN;
    else
      throw new ParseException(
          "Could not parse string into valid message type: " + parts[0] + ". Found in string: " + message,
          0);

    if (Objects.equals(parts[1], "distance"))
      this.measureType = DISTANCE;
    else if (Objects.equals(parts[1], "duration") || Objects.equals(parts[1], "time"))
      this.measureType = DURATION;
    else
      throw new ParseException(
          "Could not parse string into valid message type: " + parts[1] + ". Found in string: " + message,
          parts[0].length());

    try {
      this.measure = Double.parseDouble(parts[2]);
    } catch (NumberFormatException e) {
      throw new NumberFormatException(
          "Distance given must be a number: " + parts[2] + ". Found in string: " + message + ".");
    }

    if (parts.length >= 4) {
      try {
        this.speed = Double.parseDouble(parts[3]);
      } catch (NumberFormatException e) {
        throw new NumberFormatException(
            "Speed given must be a number or left blank to default to 0.5: " + parts[3] + ". Found in string: "
                + message + ".");
      }
    } else {
      this.speed = 0.5;
    }
  }
}
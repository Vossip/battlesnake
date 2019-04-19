package com.battle.snakes.util;


import com.battle.snakes.game.*;

import java.util.*;

public class SnakeUtil {

  private static final Random RANDOM = new Random();

  public static MoveType getRandomMove(List<MoveType> possibleMoves) {
    /* TODO
     * Given all possible moves, picks a random move
     * */

    //ArrayList<MoveType> allMoves = new ArrayList<>(Arrays.asList(MoveType.RIGHT, MoveType.LEFT, MoveType.UP, MoveType.DOWN));
    int n = RANDOM.nextInt(possibleMoves.size());
    //System.out.println("========= RANDOM MOVE ========");
    //System.out.println(possibleMoves.get(n));
    return possibleMoves.get(n);
  }

  public static boolean isInBounds(Board board, Coordinate coordinate) {
    /* TODO
     * Given the game board, calculates if a coordinate is within the board
     * */
    if (coordinate.getX() < board.getWidth() && coordinate.getX() >= 0
            && coordinate.getY() < board.getHeight() && coordinate.getY() >= 0) {
      return true;
    } else {
      return false;
    }
  }

  public static Coordinate getNextMoveCoords(MoveType moveType, Coordinate start){
    /* TODO
     * Given the move type and the start coordinate, returns the coordinates of the next move
     * */
    switch (moveType){
      case RIGHT:
        return new Coordinate(start.getX() + 1, start.getY());
      case LEFT:
        return new Coordinate(start.getX() - 1, start.getY());
      case UP:
        return new Coordinate(start.getX(), start.getY() - 1);
      case DOWN:
        return new Coordinate(start.getX(), start.getY() + 1);
      default:
        return null;
    }
  }

  public static List<MoveType> getAllowedMoves(MoveRequest request) {
    //System.out.println("======= REQUEST =======");
    //System.out.println(request);


    Snake yourSnake = request.getYou();
    //ArrayList<Coordinate> co = new ArrayList<>(Arrays.asList(new Coordinate(1, 14), new Coordinate(2, 14), new Coordinate(3, 4), new Coordinate(3, 13), new Coordinate(2, 13), new Coordinate(1, 13), new Coordinate(1, 12), new Coordinate(1, 11), new Coordinate(1, 10), new Coordinate(2, 10), new Coordinate(3, 10), new Coordinate(3, 11), new Coordinate(2, 11)));
    //yourSnake.setBody(co);
    Coordinate yourSnakeHead = yourSnake.getBody().get(0);
    Map<Coordinate, MoveType> map = new HashMap<>();

    Coordinate moveRight = getNextMoveCoords(MoveType.RIGHT, yourSnakeHead);
    Coordinate moveLeft = getNextMoveCoords(MoveType.LEFT, yourSnakeHead);
    Coordinate moveUp = getNextMoveCoords(MoveType.UP, yourSnakeHead);
    Coordinate moveDown = getNextMoveCoords(MoveType.DOWN, yourSnakeHead);

    //RIGHT
    if (isInBounds(request.getBoard(), moveRight)){
      if (!yourSnake.getBody().contains(moveRight)){
        map.put(moveRight,MoveType.RIGHT);
      }
    }

    //LEFT
    if (isInBounds(request.getBoard(), moveLeft)){
      if (!yourSnake.getBody().contains(moveLeft)){
        map.put(moveLeft,MoveType.LEFT);
      }
    }

    //UP
    if (isInBounds(request.getBoard(), moveUp)){
      if (!yourSnake.getBody().contains(moveUp)){
        map.put(moveUp,MoveType.UP);
      }
    }

    //DOWN
    if (isInBounds(request.getBoard(), moveDown)){
      if (!yourSnake.getBody().contains(moveDown)){
        map.put(moveDown,MoveType.DOWN);
      }
    }

    List<Snake> snakes = request.getBoard().getSnakes();
    for (Snake snake:snakes){
      List<Coordinate> coordinates = snake.getBody();
      for (Coordinate coordinate:coordinates){
        if (map.containsKey(coordinate)) {
          map.remove(coordinate);
        }
      }
    }

    List<MoveType> moves = new ArrayList<>();
    for (Map.Entry<Coordinate, MoveType> entry: map.entrySet()) {
      moves.add(entry.getValue());
    }

    //System.out.println("===== MOVES ======");
    //System.out.println(moves);


    /* TODO
     * Given the move request, returns a list of all the moves that do not end in the snake dieing
     * Hint: finding all the coordinates leading to the snakes death and
     * comparing it to the potential moves is a good starting point
     * */
    return moves;
  }

  public static double getDistance(Coordinate first, Coordinate second) {
    /* TODO
     * Given two coordinates on a 2D grid, calculates the distance between them
     * */
    return Math.sqrt((second.getY() - first.getY()) *
            (second.getY() - first.getY()) +
            (second.getX() - first.getX()) *
                    (second.getX() - first.getX()));
  }

  public static MoveType  getBestMoveToTarget(Coordinate target, Coordinate head, List<MoveType> moves, int depth, int originalDepth, MoveRequest request) {
    MoveType move = null;
    double nearestDistance = 0;
    Coordinate nextMoveCoords = new Coordinate();

    double distance = 0;
    MoveType selectedMove = null;

    for (MoveType moveType: moves){
      switch (moveType){
        case RIGHT:
          selectedMove = moveType;
          nextMoveCoords = getNextMoveCoords(selectedMove, head);
          distance = getDistance(getNextMoveCoords(selectedMove, head), target);
          break;
        case LEFT:
          selectedMove = moveType;
          nextMoveCoords = getNextMoveCoords(selectedMove, head);
          distance = getDistance(getNextMoveCoords(selectedMove, head), target);
          break;
        case UP:
          selectedMove = moveType;
          nextMoveCoords = getNextMoveCoords(selectedMove, head);
          distance = getDistance(getNextMoveCoords(selectedMove, head), target);
          break;
        case DOWN:
          selectedMove = moveType;
          nextMoveCoords = getNextMoveCoords(selectedMove, head);
          distance = getDistance(getNextMoveCoords(selectedMove, head), target);
          break;
      } if (depth > 0){

          List<Coordinate> body = request.getYou().getBody();
          body.add(0, nextMoveCoords);
          body.remove(body.size() -1);
          //Collections.addAll(body, nextMoveCoords);
          request.getYou().setBody(body);

          List<MoveType> newMoves = SnakeUtil.getAllowedMoves(request);

          MoveType bestMove = getBestMoveToTarget(target, nextMoveCoords, newMoves, depth - 1, originalDepth, request);

          if (bestMove != null) {
            if (move == null || distance < nearestDistance){
              move = selectedMove;
              nearestDistance = distance;
            }
          }
        } else if (move == null || distance < nearestDistance){
          move = selectedMove;
          nearestDistance = distance;
        }
      }

    if (move == null && depth == originalDepth) {
      move = getBestMoveToTarget(target, nextMoveCoords, moves, 0, 0, request);
    }
    return move;
  }

  public static MoveType  getNearestMoveToTarget(Coordinate target, Coordinate head, List<MoveType> moves) {
    /* TODO
     * Given the target coordinate, the current coordinate and a list of moves, returns
     * the nearest move to the target, selected from the moves list
     * */
    //System.out.println("==== NEAREST MOVE TO TARGET ====");
    //System.out.println(target);
    MoveType move = null;
    double nearestDistance = 0;

    double distance = 0;
    MoveType selectedMove = null;

    for (MoveType moveType: moves){
      switch (moveType){
        case RIGHT:
          selectedMove = moveType;
          distance = getDistance(getNextMoveCoords(selectedMove, head), target);
          break;
        case LEFT:
          selectedMove = moveType;
          distance = getDistance(getNextMoveCoords(selectedMove, head), target);
          break;
        case UP:
          selectedMove = moveType;
          distance = getDistance(getNextMoveCoords(selectedMove, head), target);
          break;
        case DOWN:
          selectedMove = moveType;
          distance = getDistance(getNextMoveCoords(selectedMove, head), target);
          break;
      } if (move == null || distance < nearestDistance){
        move = selectedMove;
        nearestDistance = distance;
      }
    }

    //System.out.println(move);
    return move;
  }

  public static Coordinate getNearestCoordinateToTarget(Coordinate head, List<Coordinate> coords) {
    /* TODO
     * Given the target coordinate and a list of coordinates, finds the nearest coordinate to the target
     * */
    Coordinate nearestCoordinate = new Coordinate();
    double nearestDistance = 0;

    for (Coordinate coordinate:coords){
        double distance = getDistance(head, coordinate);

        if (nearestCoordinate.getY() == null){
          nearestCoordinate = coordinate;
          nearestDistance = distance;

        } else if (distance < nearestDistance) {
          nearestCoordinate = coordinate;
          nearestDistance = distance;
        }
    }

    return nearestCoordinate;
  }
}

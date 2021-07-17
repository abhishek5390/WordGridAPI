package org.gudigar.abhishek.wordGridapi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class WordGridService {
		
	private class Coordinate{
		int x;
		int y;
		
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public enum Direction{
		HORIZONTAL,
		VERTICAL,
		DIAGONAL,
		HORIZONTAL_INVERSE,
		VERTICAL_INVERSE,
		DIAGONAL_INVERSE
	}
	
	
	public char[][] generateGrid(int gridSize, List<String> words) {
		List<Coordinate> coordinates = new ArrayList<>();
		char[][] contents = new char[gridSize][gridSize];
		
		for(int i=0 ; i < gridSize ; i++) {
			for(int j=0 ; j < gridSize ; j++) {
				contents[i][j] = '_';
				coordinates.add(new Coordinate(i, j));
			}
		}
		
		Collections.shuffle(coordinates);
		for(String word: words) {
			for(Coordinate coordinate : coordinates) {
				int x = coordinate.x;
				int y = coordinate.y;
				Direction selectedDirection = getDirectionForFit(contents, word, coordinate);
				if (selectedDirection != null) {
					switch (selectedDirection) {
					case HORIZONTAL: 
						for(char c : word.toCharArray()) {
							contents[x][y++] = c;
						}
						break;
					case HORIZONTAL_INVERSE: 
						for(char c : word.toCharArray()) {
							contents[x][y--] = c;
						}
						break;
					case VERTICAL:
						for(char c : word.toCharArray()) {
							contents[x++][y] = c;
						}
						break;
					case VERTICAL_INVERSE:
						for(char c : word.toCharArray()) {
							contents[x--][y] = c;
						}
						break;
					case DIAGONAL:
						for(char c : word.toCharArray()) {
							contents[x++][y++] = c;
						}
						break;
					case DIAGONAL_INVERSE:
						for(char c : word.toCharArray()) {
							contents[x--][y--] = c;
						}
						break;
					}
					break;
				}
			}			
		}
		randomGridFill(contents);
		return contents;
	}
	
	private void randomGridFill(char[][] contents) {
		int gridSize = contents[0].length;
		String allCapLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for(int i=0 ; i < gridSize ; i++) {
			for(int j=0 ; j < gridSize ; j++) {
				int randomIndex = ThreadLocalRandom.current().nextInt(0, allCapLetters.length());
				if(contents[i][j] == '_') {
					contents[i][j] = allCapLetters.charAt(randomIndex);
				}
			}
		}
	}
	
	private Direction getDirectionForFit(char[][] contents, String word, Coordinate coordinate) {
		List<Direction> directions = Arrays.asList(Direction.values());
		Collections.shuffle(directions);
		
		for(Direction direction : directions) {
			if(doFit(contents, word, coordinate, direction)) {
				return direction;
			}
		}
		
		return null;
	}
	
	private boolean doFit(char[][] contents, String word, Coordinate coordinate, Direction direction) {
		int gridSize = contents[0].length;
		int wordLength = word.length();
		switch (direction) {
		case HORIZONTAL: 
			if (coordinate.y + wordLength > gridSize) return false;
			for(int i = 0; i < wordLength; i++) {
				if(contents[coordinate.x][coordinate.y + i] != '_') return false;
			}
			break;
		case HORIZONTAL_INVERSE:
			if(coordinate.y < wordLength) return false;
			for(int i = 0; i < wordLength; i++) {
				if(contents[coordinate.x][coordinate.y - i] != '_') return false;
			}
			break;
		case VERTICAL:
			if (coordinate.x + wordLength > gridSize) return false;
			for(int i = 0; i < wordLength; i++) {
				if(contents[coordinate.x + i][coordinate.y] != '_') return false;
			}
			break;
		case VERTICAL_INVERSE:
			if (coordinate.x < wordLength) return false;
			for(int i = 0; i < wordLength; i++) {
				if(contents[coordinate.x - i][coordinate.y] != '_') return false;
			}
			break;
		case DIAGONAL: 
			if (coordinate.x + wordLength > gridSize || coordinate.y + wordLength > gridSize) return false;
			for(int i = 0; i < wordLength; i++) {
				if(contents[coordinate.x + i][coordinate.y + i] != '_') return false;
			}
			break;
		case DIAGONAL_INVERSE: 
			if (coordinate.x < wordLength || coordinate.y < wordLength) return false;
			for(int i = 0; i < wordLength; i++) {
				if(contents[coordinate.x - i][coordinate.y - i] != '_') return false;
			}
			break;
		}
		return true;
	}
}

package org.gudigar.abhishek.wordGridapi.controller;

import java.util.Arrays;
import java.util.List;

import org.gudigar.abhishek.wordGridapi.service.WordGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class WordGridApiController {
	
	@Autowired
	private WordGridService wordGridService;
	
	@GetMapping("/wordgrid")
	public String createWordGrid(@RequestParam int gridSize, @RequestParam String wordsList) {
		List<String> words = Arrays.asList(wordsList.split(","));
		char[][] grid = wordGridService.generateGrid(gridSize, words);
		String gridToString = "";
		for(int i=0 ; i < gridSize ; i++) {
			for(int j=0 ; j < gridSize ; j++) {
				
				gridToString += grid[i][j] + " ";
				
			}
			gridToString += "\r\n";
		}
		return gridToString;
	}
}

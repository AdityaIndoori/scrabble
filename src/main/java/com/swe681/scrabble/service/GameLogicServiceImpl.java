package com.swe681.scrabble.service;

import com.swe681.scrabble.model.*;
import com.swe681.scrabble.repository.GameMoveRepository;
import com.swe681.scrabble.repository.GameRepository;
import com.swe681.scrabble.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;



@Service
@Slf4j
public class GameLogicServiceImpl implements GameLogicService {

	@Autowired
	GameRepository gameRepository;

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	GameMoveRepository gameMoveRepository;

	static final Map<String, Integer> LETTER_SCORE;
	static final Integer BOARDSIZE = 10;
	static StringBuilder CHARACTERS_PLAYED;

	static {
		LETTER_SCORE = new HashMap<>();
		LETTER_SCORE.put("A", 1);
		LETTER_SCORE.put("B", 3);
		LETTER_SCORE.put("C", 3);
		LETTER_SCORE.put("D", 2);
		LETTER_SCORE.put("E", 1);
		LETTER_SCORE.put("F", 4);
		LETTER_SCORE.put("G", 2);
		LETTER_SCORE.put("H", 4);
		LETTER_SCORE.put("I", 1);
		LETTER_SCORE.put("J", 1);
		LETTER_SCORE.put("K", 5);
		LETTER_SCORE.put("L", 1);
		LETTER_SCORE.put("M", 3);
		LETTER_SCORE.put("N", 1);
		LETTER_SCORE.put("O", 1);
		LETTER_SCORE.put("P", 3);
		LETTER_SCORE.put("Q", 10);
		LETTER_SCORE.put("R", 1);
		LETTER_SCORE.put("S", 1);
		LETTER_SCORE.put("T", 1);
		LETTER_SCORE.put("U", 1);
		LETTER_SCORE.put("V", 4);
		LETTER_SCORE.put("W", 4);
		LETTER_SCORE.put("X", 8);
		LETTER_SCORE.put("Y", 4);
		LETTER_SCORE.put("Z", 10);
	}

	@Override
	public void createGame(Long gameid) throws Exception{
		try {
			Game game = gameRepository.findById(gameid).get();
			if(game!=null) {
				if(game.getP1Username()!=null && game.getP2Username()!=null) {

					initializeBag(gameid);

					initializeBoard(gameid);

					initializePlayer(game.getP1Username(), gameid);
					initializePlayer(game.getP2Username(), gameid);
					fillPlayerRack(game.getP1Username(), gameid);
					fillPlayerRack(game.getP2Username(), gameid);
				}
				else {
					gameRepository.save(game);
				}
			}
		}catch(Exception ex) {
			throw ex;
		}
	}

	@Override
	public String playMove(MoveWS move) throws Exception{
		try {

			if(move.getWord()!= null && move.getWord().length()==0) {
				log.info("MESSAGE: Turn skipped for "+move.getUsername());
				return "MESSAGE: Turn skipped for "+move.getUsername();
			}

			move.setWord(move.getWord().toUpperCase());

			Character matrix2d[][] = create2DBoard(Long.parseLong(move.getGameid()));


			String response = checkWord(move.getUsername(), Long.parseLong(move.getGameid()), move.getWord(), move.getDirection(), Integer.parseInt(move.getRow()), Integer.parseInt(move.getColumn()), matrix2d);

			if(!response.contains("OK")) {
				return response;
			}

			if(placeWord(move)==false) {
				return "ERROR: Could not load matrix";
			}

			String response2 = removeLettersFromPlayerRack(move.getUsername(), Long.parseLong(move.getGameid()));
			if(!response2.contains("SUCCESS")) {
				return "ERROR: Could not clear player rack";
			}

			fillPlayerRack(move.getUsername(), Long.parseLong(move.getGameid()));

			Integer score = calculateScore(move.getWord());

			Player player = playerRepository.findByUsernameAndGameid(move.getUsername(), Long.parseLong(move.getGameid()));
			if(player!=null) {
				Integer localScore = player.getScore();
				player.setScore(localScore+score);
				playerRepository.save(player);
			}

			saveMove(move);


			return "SUCCESS";

		}catch(Exception ex) {
			throw ex;
		}

	}

	@Override
	public OutputMove settingOutputMove(MoveWS move) throws Exception{
		try {

			OutputMove om = new OutputMove();
			Game game = gameRepository.findById(Long.parseLong(move.getGameid())).get();

			if(game!=null) {
				String board = game.getBoard();

				//OutputBoard ob =new OutputBoard();
				Character matrix2d[][] = create2DBoard(Long.parseLong(move.getGameid()));



				JSONArray array = new JSONArray();
				List<List<String>> outputTable = new ArrayList<>();

				for(int i=0; i<BOARDSIZE; i++) {
					List<String> outputRow = new ArrayList<>();
					outputRow.add(""+(i));
					for(int j = 0; j < BOARDSIZE; j++) {
						String col = new String( ""+matrix2d[i][j]);
						outputRow.add(col);
					}
					outputTable.add(outputRow);
				}

				om.setOutputTable(outputTable);


				String p1Username = game.getP1Username();

				Player player = playerRepository.findByUsernameAndGameid(p1Username, Long.parseLong(move.getGameid()));

				if(player!=null) {
					om.setP1Score(player.getScore());
				}

				String p2Username = game.getP2Username();

				Player player2 = playerRepository.findByUsernameAndGameid(p2Username, Long.parseLong(move.getGameid()));

				if(player2!=null) {
					om.setP2Score(player2.getScore());
				}


				return om;
			}
			return null;
		}catch(Exception ex) {
			throw ex;
		}
	}

	@Override
	public String showGameRack(String gameid, String username) throws Exception {
		log.info(String.format("ShowGamrRack: username = %s, gameid = %s",username, gameid));
		List<Player> currentPlayers = playerRepository.findByGameid(Long.parseLong(gameid));
		if (currentPlayers == null)
			throw new Exception("No player with game id and username found");
		else{
			if (currentPlayers.size()==2)
				return String.format("{\"%s\":\"%s\", \"%s\":\"%s\"}", currentPlayers.get(0).getUsername(), currentPlayers.get(0).getRack(), currentPlayers.get(1).getUsername(), currentPlayers.get(1).getRack());
			else
				throw new Exception("Only a single player was found");
		}
	}

	private void saveMove(MoveWS move) throws Exception{
		try {
			GameMove gmove = new GameMove();
			gmove.setUsername(move.getUsername());
			gmove.setGameid(Long.parseLong(move.getGameid()));
			gmove.setWord(move.getWord());
			gmove.setBoardrow(move.getRow());
			gmove.setBoardcolumn(move.getColumn());
			gmove.setDirection(Direction.valueOf(move.getDirection()));


			gameMoveRepository.save(gmove);

		}catch(Exception ex) {
			throw ex;
		}
	}

	private Integer calculateScore(String word) throws Exception{
		try {
			Integer score = 0;
			for(char c: word.toCharArray()) {
				score = score + LETTER_SCORE.get(""+Character.toUpperCase(c));
			}

			return score;
		}catch(Exception ex) {
			throw ex;
		}
	}

	private String removeLettersFromPlayerRack(String username, Long gameid) throws Exception{
		try {

			Player player = playerRepository.findByUsernameAndGameid(username, gameid);

			if(player!=null && player.getRack()!=null) {
				char arr[] = CHARACTERS_PLAYED.toString().toCharArray();

				for(char c: arr) {
					player.setRack(player.getRack().replace(""+Character.toUpperCase(c), ""));
				}
				playerRepository.save(player);
			}

			return "SUCCESS";
		}catch(Exception ex) {
			throw ex;
		}
	}

	private Boolean placeWord(MoveWS move) throws Exception{
		try {
			var i = 0;
			var rIndex = Integer.parseInt(move.getRow());
			var cIndex = Integer.parseInt(move.getColumn());
			var word = move.getWord();

			Game game = gameRepository.findById(Long.parseLong(move.getGameid())).get();

			if(game!=null) {
				Character matrix2d[][]= create2DBoard(game.getId());

				if(move.getDirection().equals(Direction.HORIZONTAL.toString())) {
					for(i=0; i<word.length(); i++) {
						matrix2d[rIndex][i+cIndex] = word.charAt(i);
					}
				}

				if(move.getDirection().equals(Direction.VERTICAL.toString())) {
					for(i=0; i<word.length(); i++) {
						matrix2d[i+rIndex][cIndex] = word.charAt(i);
					}
				}

				game.setBoard(createStringFromMatrix(matrix2d));
				gameRepository.save(game);
				return true;
			}
			return false;
		}catch(Exception ex) {
			throw ex;
		}
	}

	private String createStringFromMatrix(Character matrix2d[][]) throws Exception{
		try {
			int i=0, j=0;
			StringBuilder returningStr = new StringBuilder();
			for(i=0; i<matrix2d.length; i++) {
				for(j=0; j<matrix2d[i].length; j++) {
					returningStr.append(matrix2d[i][j]);
				}
			}
			return returningStr.toString();
		}catch(Exception ex) {
			throw ex;
		}
	}

	private Character[][] create2DBoard(Long gameid){
		try {
			Character matrix2d[][]= new Character[BOARDSIZE][BOARDSIZE];
			Game game = gameRepository.findById(gameid).get();
			
			if(game!=null && game.getBoard()!=null) {
				String localBoard = game.getBoard();
				for(var i=0; i<BOARDSIZE; i++) {
					for(var j=0; j<BOARDSIZE; j++) {
						matrix2d[i][j] = localBoard.charAt(0);
						localBoard = localBoard.substring(1);
					}
				}
			}
			return matrix2d;
		}catch(Exception ex){
			throw ex;
		}
	}
	
	private String checkWord(String username, Long gameid, String word, String direction, Integer rIndex, Integer cIndex, Character boardMatrix[][]) throws Exception {
		try {
			StringBuilder currentBoardArr = new StringBuilder();
			int i=0,j=0;
			boolean overlap = false;
			CHARACTERS_PLAYED = new StringBuilder();
			
			//Checking if it is the correct turn
			String response = checkTurn(username, gameid);
			if(!response.contains("OK")) {
				return response;
			}

			if (username == null || gameid == null || word == null || direction == null || rIndex == null || cIndex == null || boardMatrix == null)
				throw new Exception("Null value!");
			
			// Getting current Situation
			if(direction.equals(Direction.HORIZONTAL.toString())) {
				
				// Check if word is within board bounds
				if((word.length()+cIndex) >= BOARDSIZE) {
					return "ERROR: Location out of bounds.";
				}

				for(i=0; i<word.length(); i++) {

					currentBoardArr.append(boardMatrix[rIndex][i+cIndex]);

				}
			}
			// Getting current Situation
			else if(direction.equals(Direction.VERTICAL.toString())) {

				// Check if word is within board bounds
				if((word.length()+rIndex) >= BOARDSIZE) {
					return "ERROR: Location out of bounds.";
				}

				for(i=0; i<word.length(); i++) {

					currentBoardArr.append(boardMatrix[i+rIndex][cIndex]);

				}
			}
			log.info("CURRENT BOARD ARRAY::-----"+currentBoardArr.toString());


			CHARACTERS_PLAYED = new StringBuilder();
			// Checking word overlapping
			for(i=0; i<word.length(); i++) {
				if(currentBoardArr.charAt(i)==' ') {
					CHARACTERS_PLAYED.append(word.charAt(i));
				}
				else if(word.charAt(i) == currentBoardArr.charAt(i)) {
					overlap = true;
				}
				else {
					return "ERROR: Letters do not overlap, please choose another word.";
				}
			}
			log.info("CHARACTER PLAYED___________"+CHARACTERS_PLAYED.toString());

			// Checking in dictionary
			if(checkWordInDictionary(word).equals(false)) {
				return "ERROR: Please enter a valid dictionary word.";
			}


			//Checking if player has the characters that he played
			String response2 = checkPlayersRackForCurrentMove(username, gameid, CHARACTERS_PLAYED.toString());
			if(!response2.contains("OK")) {
				return response2;
			}

			return "OK";



		}catch(Exception ex) {
			throw ex;
		}

	}

	private String checkTurn(String username, Long gameid) {
		try {
			Game game = gameRepository.findById(gameid).get();

			if(game!=null && game.getLastMove()!=null && game.getLastMove().equals(username)) {
				return "Error: Not your turn!";
			}
			return "OK";
		}catch(Exception ex){
			throw ex;
		}
	}

	private String checkPlayersRackForCurrentMove(String username, Long gameid, String charactersPlayed) throws Exception{
		try {

			Player player = playerRepository.findByUsernameAndGameid(username, gameid);
			if(player!=null) {
				for(char c: charactersPlayed.toCharArray()) {
					if(player.getRack().indexOf(Character.toUpperCase(c)) < 0) {
						log.info(String.format("Player: %s, Word: %s, Current char %c", player.getUsername(), charactersPlayed, c));
						return "Error: Played letters are not present in players rack.";
					}
				}
				return "OK";
			}
			return "ERROR: Can not find player.";
		}catch(Exception ex) {
			throw ex;
		}
	}

	private Boolean checkWordInDictionary(String word) throws Exception {

		Resource resource = new ClassPathResource("dictionary.txt");

		//InputStream input = resource.getInputStream();

		File file = resource.getFile();

		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if(word.toUpperCase().contentEquals(line)) {
					scanner.close();
					return true;
				}
			}
			scanner.close();
			return false;
		} catch(FileNotFoundException e) {
			throw e;
		}
	}

	private void initializeBag(Long gameid) throws Exception{
		try {
			String bag = "AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIJJJJJJJJJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
			String shuffledBag = shuffle(bag);

			Game game = gameRepository.findById(gameid).get();

			//log.info("In GameLogicService------- game before save bag:"+game.toString());

			if(game!=null) {
				game.setBag(shuffledBag);
				game.setStatus(GameStatus.RUN);
				game = gameRepository.save(game);
			}
		}catch(Exception ex){
			throw ex;
		}

	}

	private void initializeBoard(Long gameid) throws Exception{
		// TODO Auto-generated method stub
		try {
			Game game = gameRepository.findById(gameid).get();

			//log.info("In GameLogicService------- game before save board:"+game.toString());

			if(game!=null) {
				StringBuilder localBoard = new StringBuilder();
				for(int i=0; i<BOARDSIZE; i++) {
					StringBuilder row = new StringBuilder();
					for(int j=0; j<BOARDSIZE; j++) {
						row.append(" ");
					}
					localBoard.append(row.toString());
				}

				game.setBoard(localBoard.toString());

				game = gameRepository.save(game);

			}
		}catch(Exception ex){
			throw ex;
		}

	}

	private void fillPlayerRack(String username, Long gameid) throws Exception{
		try {
			Player player = playerRepository.findByUsernameAndGameid(username, gameid);
			Game game = gameRepository.findById(gameid).get();

			//log.info("In GameLogicService------- game before setting rack:"+game.toString());


			if(player!=null && game!=null && (player.getRack().length()<7)) {
				StringBuilder localRack = new StringBuilder();
				localRack.append(player.getRack());
				for(int i=0; i<(7 - player.getRack().length()); i++) {
					localRack.append(game.getBag().substring(0, 1));
					game.setBag(game.getBag().substring(1));
				}

				player.setRack(localRack.toString());

				gameRepository.save(game);
				playerRepository.save(player);
			}
		}catch(Exception ex){
			throw ex;
		}
	}

	private void initializePlayer(String username, Long gameid) throws Exception{
		try {
			Player player = new Player();
			player.setGameid(gameid);
			player.setUsername(username);
			player.setRack("");
			player.setScore(0);
			playerRepository.save(player);
		}catch(Exception ex){
			throw ex;
		}
	}

	private String shuffle(String input){

		List<Character> characters = new ArrayList<Character>();
		for(char c:input.toCharArray()){
			characters.add(c);
		}
		StringBuilder output = new StringBuilder(input.length());
		while(characters.size()!=0){
			int randPicker = (int)(Math.random()*characters.size());
			output.append(characters.remove(randPicker));
		}
		return output.toString();
	}

}

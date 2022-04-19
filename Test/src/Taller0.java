import java.util.Scanner;
import java.util.Random;
import java.io.*;

//Integrantes
//Ellian André Troncoso Torres
//Felipe Alexander Díaz Ávila
//Sebastian Ignacio Vasquez Andrades

public class Taller0 {
	public static void main(String[] args) throws IOException{
		
		//====== PARALLEL LISTS =======
		
		//1.- Player's Data
		String playerList[] = new String [100]; //l0
		String passList[] = new String [100]; //l1
		int hpList[] = new int [100]; //l2
		int adList[] = new int [100]; //l3
		int defList[] = new int [100];//l4
		int velList[] = new int [100]; //l5
		int listNumOfSpells[] = new int [100]; //l6
		int xpList[] = new int [100]; //l7
		
		//2.- Spell's and Power Attack
		String spellsList [] = new String [100];//l8
		int powerAttackList [] = new int [100];//l9
		
		//3.- Enemy's Data
		String enemyList[] = new String [1000]; //l10
		int enemyHPList[] = new int [1000]; //l11
		int enemyADList[] = new int[1000]; //l12
		String enemyClassList[] = new String [1000]; //l13
		int enemyVelList[] = new int [1000]; //l14
		
				
		//Call scanPlayers's function 
		File playersFile = new File ("Jugadores.txt");
		scanPlayers(playersFile, playerList, passList, hpList, adList, defList, velList, listNumOfSpells, xpList);
		
		//Number of players (Reduces iteration of next cycles)
		int playerCount = 0;
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null) {
				playerCount++;
			}
		}
		//System.out.println(playerCount);		
		
		//Call scanSpells's function
		File spellsFile = new File ("Hechizos.txt");
		scanSpells(spellsFile, spellsList, powerAttackList);
		
		//Number of Spells (Reduces iteration of next cycles)
		int spellCount = 0;
		for (int i = 0; i < spellsList.length; i++) {
			if (spellsList[i] != null) {
				spellCount++;
			}
		}
		//System.out.println(spellCount);
		
		File enemiesFile = new File("Enemigos.txt");
		scanEnemies(enemiesFile, enemyList, enemyHPList, enemyADList, enemyClassList, enemyVelList);
		
		//Number of enemies (Reduces iteration of next cycles)
		int enemyCount = 0;
		for (int i = 0; i < enemyList.length; i++) {
			if (enemyList[i] != null) {
				enemyCount++;
			}
		}
		
		//4.- spell matrix for each player
		
		int matrix1 [][] = new int [playerCount][spellCount];
		
		//fill matrix
		File spellForEachPlayerFile = new File ("HechizosJugadores.txt");
		fillMatrix(spellForEachPlayerFile, matrix1, playerList, spellsList,playerCount,spellCount);
		
		//Print matrix
		//System.out.println("spell matrix for each player");
//		for (int fil = 0; fil < playerCount; fil++) {
//			for(int col = 0; col < spellCount; col++) {
//				System.out.print(matrix1[fil][col] + " ");
//			}
//			System.out.println("");
//		}
		
		//Print users and Passwords
//		for (int i = 0; i < playerCount; i++) {
//			System.out.println(playerList[i] +" "+ passList[i]);
//		}
		
		// ==== LOGIN ===== (IMPORTANT: modify .txt files when input a new user)
		int playerPos = -1; 
		playerPos = login(playerCount, playerList, passList, playerPos, playersFile); //Save player position
		//System.out.println(playerList[playerPos]);
		String user = null;
		if (playerPos != -2) {
			user = playerList[playerPos];
		}
		
		//==== ADMIN MENU =====
		if (playerPos == -2) {
			String adminMenuOption = null;
			adminMenuOption = adminMenu(adminMenuOption); //Save option chosen
			
			if (adminMenuOption.equals("a") || adminMenuOption.equals("A")) {
				deleteUser(playersFile, spellForEachPlayerFile, passList, playerList, hpList, adList, defList, velList, listNumOfSpells, xpList, matrix1, playerCount, spellCount);
				playerCount-=1;
			}
			
			else if (adminMenuOption.equals("b") || adminMenuOption.equals("B")) {
				addEnemy(enemiesFile, enemyList, enemyHPList, enemyADList, enemyClassList, enemyVelList);
			}
			
			else if (adminMenuOption.equals("c") || adminMenuOption.equals("C")) {
				//addSpell();
			}
			
			else if (adminMenuOption.equals("d") || adminMenuOption.equals("D")) {
				viewStatistics(playerList, hpList, adList, defList, velList, listNumOfSpells, xpList, playerCount);
			}
		}
		
		if (playerPos != -2) {
		
			// ===== PLAYER MENU ======
			String menuOption = null;
			menuOption = playerMenu(user,menuOption);
			
			if (menuOption.equals("a") || menuOption.equals("A")) {
				String fightOption = null;
				
				fightOption = fightMenu(fightOption); //Save option chosen in fightMenu
	
				if (fightOption.equals("a") || fightOption.equals("A")) { //JcE
					//jcE(playerPos);  PENDING
					System.out.println("Pendiente :(");
				}
				else if(fightOption.equals("b") || fightOption.equals("B")) { //JcJ
					jcJ(playersFile, spellsFile, playerPos, playerCount, spellCount, matrix1, playerList, hpList, adList, defList, velList, listNumOfSpells, xpList, spellsList, powerAttackList);
				}
				
			}
			
			else if (menuOption.equals("b") || menuOption.equals("B")) {
				learnSpell(playersFile, spellForEachPlayerFile, playerPos, playerList, xpList, listNumOfSpells, spellsList, spellCount);
			}
			
			else if (menuOption.equals("c") || menuOption.equals("C")) {
				viewPlayerStatistics(playerList, hpList, adList, defList, velList, listNumOfSpells, xpList, playerCount);
			}
			
			else if (menuOption.equals("d") || menuOption.equals("D")) {
				viewSpellStatistics(matrix1, playerList, spellsList, powerAttackList, spellCount, playerCount);
			}
			
			else if (menuOption.equals("e") || menuOption.equals("E")) {
				viewRanking(playerList, xpList);
				
			}
		}
	}//-------MAIN FINISH
	
	
	// ================ ENEMIES ==================
	//	CLASS S ---> 1000 experience, Probability 1 %
	//	CLASS A ---> 750 experience, Probability 10 %
	//	CLASS B ---> 500 experience, Probability 25 %
	//	CLASS C ---> 250 experience, Probability 50 %
	//	CLASS F ---> 100 experience, Probability 75 %
	
	
	//========================================== FUNCTION AND METHODS ========================================================================
	
	public static void scanPlayers(File archivo, String l0[], String l1[], int l2[], int l3[], int l4[], int l5[], int l6[], int l7[]) throws FileNotFoundException{
		
		Scanner read = new Scanner(archivo);
		int pos = 0;
		
		while (read.hasNextLine()) {
			
			String line = read.nextLine();
			String split [] = line.split(",");
			
			l0[pos] = split[0];
			l1[pos] = split[1];
			l2[pos] = Integer.parseInt(split[2]);
			l3[pos] = Integer.parseInt(split[3]);
			l4[pos] = Integer.parseInt(split[4]);
			l5[pos] = Integer.parseInt(split[5]);
			l6[pos] = Integer.parseInt(split[6]);
			l7[pos] = Integer.parseInt(split[7]);
			
			pos++;
		
		} 
		read.close();
	}
	
	public static void scanSpells(File file, String l8[], int l9[]) throws FileNotFoundException {
		
		Scanner read = new Scanner(file);
		
		int pos = 0;
		while (read.hasNextLine()) {
			
			String line = read.nextLine();
			String split[] = line.split(",");
			
			l8[pos] = split[0];
			l9[pos] = Integer.parseInt(split[1]);
			
			++pos;
			
		}
		
		read.close();
	}
	
	public static void scanEnemies(File file, String l10[], int l11[], int l12[], String l13[], int l14[]) throws FileNotFoundException {
		
		Scanner read = new Scanner(file);
		
		int pos = 0;
		while (read.hasNextLine()) {
			
			String line = read.nextLine();
			String split[] = line.split(",");
			
			l10[pos] = split[0];
			l11[pos] = Integer.parseInt(split[1]);
			l12[pos] = Integer.parseInt(split[2]);
			l13[pos] = split[3];
			l14[pos] = Integer.parseInt(split[4]);
			
			pos++;
		}
		read.close();
	}
	//File = HechizosJugadores.txt. Spell matrix for each player (filled with 1 or 0)
	public static void fillMatrix(File file, int matrix[][], String players[], String spells[], int playerCount, int spellCount) throws FileNotFoundException {
		 
		Scanner read = new Scanner(file);
		 
		while (read.hasNextLine()) {
			String line = read.nextLine();
			String split[] = line.split(",");
			 
			String player = split[0];
			String spell = split[1];
			 
			//Searching index in the list
			int playerPos = -1;
			for (int i = 0; i < players.length; i++) {
				if (player.equals(players[i])) {
					playerPos = i;
					}
			}
			int spellPos = -1;
			for (int i = 0; i < spells.length; i++) {
				if (spell.equals(spells[i])) {
					spellPos = i;	
					}
				}
			 
			matrix[playerPos][spellPos] = 1;
			 
		 }
		
		 read.close(); 
	 }
	
	public static int login(int playerCount, String l0[], String l1[],int playerPos, File players) throws IOException {

		Scanner scan = new Scanner (System.in);
		System.out.println("");
		System.out.println("======== Iniciar Sesión ======== ");
		System.out.print("Usuario: ");
		String user = scan.nextLine();
		System.out.print("Contraseña: ");
		String pass = scan.nextLine();
		
		
		for (int i = 0; i < playerCount; i++) {
			if (user.equals(l0[i])) { //Verify if player exist
				
				if(pass.equals(l1[i])) {
					System.out.println(" ");
					System.out.println("... Inicio de sesión exitoso!");
					System.out.println(" ");
					//===== GAME MENU ======
					return playerPos = i;
				}					 
				else {
					System.out.println("Contraseña incorrecta");
					System.out.println("");
				}
			}
			else if (user.equals("Admin") && pass.equals("Patata19")) {
				return playerPos = -2; //Index -2 is for Admin
			}
		}
		
		System.out.println("Este usuario no existe o la contraseña es incorrecta. ¿Desea agregar un nuevo Usuario?");
		System.out.println("(Y) Yes");
		System.out.println("(N) No");
		System.out.println("Ingrese la opción: ");
		String option1 = scan.nextLine();
		if (option1.equals("Y") || option1.equals("y")) {						
			System.out.print("Ingrese el nuevo usuario: ");
			String newUser = scan.nextLine();
			l0[playerCount] = newUser; //playerCount is use as index because is the last index of list
			
			System.out.print("Ingrese una contraseña para el usuario: ");
			String newPass = scan.nextLine();
			l1[playerCount] = newPass;
			
			playerPos = playerCount; //Save index to return it later
			scan.close();
			Scanner read = new Scanner (players);
			
			String oldTextPlayer = "";
			
			while (read.hasNextLine()) {
				String line = read.nextLine();
				oldTextPlayer += line + "\n"; //Save each line of .txt
				
			}
			read.close();
			
			System.out.println("");
			System.out.println("Usuario agregado correctamente ...");
			System.out.println("");
			
			FileWriter writerPlayer = new FileWriter ("Jugadores.txt");
			writerPlayer.write(oldTextPlayer + newUser + "," + newPass + "," + "10,10,10,10,1,1000");
			
			writerPlayer.close();
//			System.out.println(playerPos);
//			for (int i = 0; i < playerCount; i++) {
//				System.out.println(l0[i]);
//			}
			
		}
		else if (option1.equals("N") || option1.equals("n")) {
			System.out.println("");
			System.out.println("Saliendo del juego...");
		}
		return playerPos;
	 }
	 
	public static String playerMenu (String user, String option) {
		Scanner scan = new Scanner (System.in);
		System.out.println("======== Menú Jugador =========");
		System.out.println("---------------------");
		System.out.println("Bienvenido " + user +"!");
		System.out.println("---------------------");
		System.out.println("¿Qué deseas hacer?");
		System.out.println("a) Pelear contra un enemigo");
		System.out.println("b) Aprender un hechizo");
		System.out.println("c) Ver estadísticas de un jugador");
		System.out.println("d) Ver estadísticas de hechizos de un jugador");
		System.out.println("e) Ver ranking de jugadores con más experiencia");
		System.out.print("Ingrese la opción: ");
		option = scan.nextLine();

		return option;
	}
	
	public static String adminMenu(String option) {
		
		Scanner scan = new Scanner (System.in);
		System.out.println("======== Menú Administrador =========");
		System.out.println("-----------------");
		System.out.println("Bienvenido Admin!");
		System.out.println("-----------------");
		System.out.println("¿Qué deseas hacer?");
		System.out.println("a) Eliminar un jugador");
		System.out.println("b) Agregar enemigos");
		System.out.println("c) Agregar hechizos");
		System.out.println("d) Ver las estadísticas de los jugadores");
		System.out.print("Ingrese la opción: ");
		option = scan.nextLine();

		return option;
	}	

	public static void deleteUser(File players, File playersSpell, String l0[], String l1[], int l2[], int l3[], int l4[], int l5[], int l6[], int l7[], int matrix[][], int playerCount, int spellCount) throws IOException {
		
		Scanner scan = new Scanner(System.in);
		System.out.println("");
		System.out.println("-------------------");
		System.out.println("Eliminar un jugador");
		System.out.println("-------------------");
		System.out.println("");
		System.out.print("Ingrese el usuario para eliminar sus datos: ");
		String user = scan.nextLine();
		
		Scanner read = new Scanner (players);
		Scanner read2 = new Scanner (playersSpell);
		
		String oldTextPlayer = "";
		String oldTextPlayersSpell = "";
		
		while(read.hasNextLine()) {
			String line = read.nextLine();
			if (!line.contains(user)) {
				oldTextPlayer += line + ("\n");
			}
		}
		while (read2.hasNextLine()) {
			String line = read2.nextLine();
			if (!line.contains(user)) {
				oldTextPlayersSpell += line + "\n";
			}
		}
		read.close();
		read2.close();
		
		System.out.println("");
		System.out.println("Datos del usuario " + user + " eliminados correctamente");
		
		FileWriter writerPlayer = new FileWriter ("Jugadores.txt");
		writerPlayer.write(oldTextPlayer);
		
		FileWriter writerPlayersSpell = new FileWriter("HechizosJugadores.txt");
		writerPlayersSpell.write(oldTextPlayersSpell);

		writerPlayer.close();
		writerPlayersSpell.close();
		
	}
	
	public static void addEnemy(File enemies, String l10[], int l11[], int l12[], String l13[], int l14[]) throws IOException {//ADMINOPTION
		
		//Modify "enemigos.txt" (name l10, hp l11, ad l12, class l13, vel l14)
		Scanner scan = new Scanner (System.in);
		System.out.println("");
		System.out.println("------------------");
		System.out.println("Agregar un enemigo");
		System.out.println("------------------");
		System.out.println("");
		System.out.print("Ingrese nombre del enemigo: ");
		String name = scan.nextLine();
		System.out.print("Ingrese la vida del enemigo: ");
		int hp = scan.nextInt();
		System.out.print("Ingrese el ataque del enemigo: ");
		int ad = scan.nextInt();
		
		scan.nextLine();
		
		String enemyText = "";
		
		System.out.print("Ingrese la clase del enemigo (S-A-B-C-F): ");
		String enemyClass = scan.nextLine().toUpperCase();
		System.out.print("Ingrese la velocidad del enemigo: ");
		int vel = scan.nextInt();
		
		scan.close();
		
		Scanner readEnemies = new Scanner (enemies);
		while (readEnemies.hasNextLine()) {
			String line = readEnemies.nextLine();
			enemyText += line + "\n";
		}
		
		enemyText += name + "," + hp + "," + ad + "," + enemyClass + "," + vel;
		
		readEnemies.close();
		
		FileWriter enemiesWriter = new FileWriter ("enemigos.txt");
		
		enemiesWriter.write(enemyText);
		
		enemiesWriter.close();
		
		System.out.println("Enemigo: " + name + "," + hp + "," + ad + "," + enemyClass + "," + vel +", agregado con éxito");
		

	}
	
	public static void addSpell(File spells, String l8[], int l9[]) throws FileNotFoundException {//ADMINOPTION
		
		//Modify "Hechizos.txt" name l8 power attack l9
		Scanner scan = new Scanner (System.in);
		System.out.println("");
		System.out.println("------------------");
		System.out.println("Agregar un Hechizo");
		System.out.println("------------------");
		System.out.println("");
		System.out.print("Ingrese nombre del Hechizo: ");
		String name = scan.nextLine();
		System.out.print("Ingrese el poder de ataque del Hechizo: ");
		int ap = scan.nextInt();
		scan.nextLine();
		
		scan.close();
		
		String oldTextSpells = "";
		
		Scanner readSpells = new Scanner (spells);
		while (readSpells.hasNextLine()) {
			String line = readSpells.nextLine();
			oldTextSpells += line + "\n";
		}
		
		oldTextSpells += name + "," + ap;
	}

	public static void viewStatistics(String l0[], int l2[], int l3[], int l4[], int l5[], int l6[], int l7[], int playerCount) {
		System.out.println("");
		System.out.println("----------------------------");
		System.out.println("Estadísticas de todos los jugadores");
		System.out.println("---------------------------");
		System.out.println("");
		System.out.println("Cargando datos ...");
		System.out.println("");
		for (int i = 0; i < playerCount; i++) {
			System.out.println(i+1 + ".- Nombre: " + l0[i] + " / Vida: " + l2[i] + " / Ataque: " + l3[i] + " / Defensa: " + l4[i] + " / Velocidad: " + l5[i] + " / Cantidad Hechizos: " + l6[i] + " / Experiencia: " + l7[i]);
		}
	}
	
	public static String fightMenu(String option) {
		Scanner scan = new Scanner (System.in);
		System.out.println("");
		System.out.println("-------------------------");
		System.out.println("Pelear contra un enemigo");
		System.out.println("-------------------------");
		System.out.println("");
		System.out.println("Seleccione el tipo de batalla: ");
		System.out.println("a) JcE (Jugador contra Entorno)");
		System.out.println("b) JvJ (Jugador contra Jugador)");
		System.out.print("Ingrese la opción: ");
		option = scan.nextLine();
		System.out.println("");
		return option;
	}
	
	public static void jcE (File enemies, File players, File spells, int pos, int playerCount, int spellCount, int enemyCount, int matrix[][], String l0[], int l2[], int l3[], int l4[], int l5[], int l6[], int l7[], String l8[], int l9[], String l10[], int l11[], int l12[], String l13[], int l14[]) throws FileNotFoundException {//PENDIENTE 
		
		//PENDING
		
		System.out.println("----------------------------");
		System.out.println("JcE (Jugador contra Entorno)");
		System.out.println("----------------------------");
		
		Random rand = new Random();
		int randomNum = rand.nextInt(100);
		System.out.println("Random Int value: " + randomNum);
		
		Scanner readEnemies = new Scanner (enemies);
		
		int sCount = 0;
		int aCount = 0;
		int bCount = 0;
		int cCount = 0;
		int fCount = 0;
		
		while (readEnemies.hasNextLine()) {
			String line = readEnemies.nextLine();
			if (line.contains("S")) {
				sCount++;
			} else if (line.contains("A")) {
				aCount++;
			} else if (line.contains("B")) {
				bCount++;
			} else if (line.contains("C")) {
				cCount++;
			} else if (line.contains("F")) {
				fCount++;
			}
		}
		
		int sIndexList [] = new int [sCount];
		int aIndexList [] = new int [aCount];
		int bIndexList [] = new int [bCount];
		int cIndexList [] = new int [cCount];
		int fIndexList [] = new int [fCount];
		
		int sPos = 0;
		int aPos = 0;
		int bPos = 0;
		int cPos = 0;
		int fPos = 0;
		
		for (int i = 0; i < enemyCount; i++) {
			if (l13[i].equals("S")) {
				sIndexList[sPos] = i;
				sPos++;
			} else if (l13[i].equals("A")) {
				aIndexList[aPos] = i;
				aPos++;
			} else if (l13[i].equals("B")) {
				bIndexList[bPos] = i;
				bPos++;
			} else if (l13[i].equals("C")) {
				cIndexList[cPos] = i;
				cPos++;
			} else if (l13[i].equals("F")) {
				fIndexList[fPos] = i;
				fPos++;
			}
		}
		
		//PlayerA's data
		String playerA = l0[pos];
		int hpA = l2[pos];
		int adA = l3[pos];
		int defA = l4[pos];
		int xpA = l7[pos];
		int velA = l5[pos];
				
		String spellsA [] = new String [spellCount];
		int spellsCountA = 0;
				
		int spellPos = 0;
		for (int col = 0; col < spellCount; col++) { //Fill spellsA list
			if (matrix[pos][col] != 0) {
				spellsA[spellPos] = l8[col];
				spellPos++;
				spellsCountA++;
						
			}
		}
		
		int randomIndex = 0; //For select a random enemy in each class
		
		if(randomNum == 1) {
			System.out.println("Clase S");
			randomIndex = rand.nextInt(0, sPos); //sPos represent the last element in the list
			enemyStatistics(randomIndex, l10,l11,l12,l14);
			
		} else if (randomNum <= 10) {
			System.out.println("Clase A");
		} else if (randomNum <= 25) {
			System.out.println("Clase B");
		} else if (randomNum <= 50) {
			System.out.println("Clase C");
		} else if (randomNum <= 75) {
			System.out.println("Clase F");
			
		}
		else {
			System.out.println("No se ha encontrado ningún enemigo");
		}
	}
	
	public static void enemyStatistics(int randNum, String l10[], int l11[], int l12[], int l14[] ) {
		String enemy = l10[randNum];
		int hpEnemy = l11[randNum];
		int adEnemy = l12[randNum];
		int velEnemy = l14[randNum];
		
		System.out.println("====================================");
		System.out.println("Estadísticas del enemigo: " + enemy );
		System.out.println("====================================");
		System.out.println(" - Puntos de vida: " + hpEnemy + "\n - Ataque: " + adEnemy + "\n - Velocidad: " + velEnemy);
	}
	
	public static void jcJ(File players, File spells, int pos, int playerCount, int spellCount, int matrix[][], String l0[], int l2[], int l3[], int l4[], int l5[], int l6[], int l7[], String l8[], int l9[]) throws IOException {
		
		Random rand = new Random ();
		int randomIndex = rand.nextInt(0, playerCount-1); //Enemy index
		
		while (randomIndex == pos) {
			randomIndex = rand.nextInt(0, playerCount-1);
		}
		
		int random = 0;
		
		String player = l0[pos];
		String enemyPlayer = l0[randomIndex];
		
		String playerA = ""; //First attack
		String playerB = ""; //Second attack
		
		int playerAIndex = 0;
		int playerBIndex = 0;
		
		//Determinate first attack
		if (l5[pos] > l5[randomIndex]) {
			playerA = l0[pos]; playerAIndex = pos;
			playerB = l0[randomIndex]; playerBIndex = randomIndex;
		}
		else if (l5[pos] < l5[randomIndex]) {
			playerA = l0[randomIndex]; playerAIndex = randomIndex;
			playerB = l0[pos]; playerBIndex = pos;
		} else {  //TIE
			if (randomIndex > pos) {
				random = rand.nextInt(pos, randomIndex + 1);
			}
			else {
				random = rand.nextInt(randomIndex, pos+1);
			}
			
			if (random == randomIndex) {
				playerA = l0[randomIndex]; playerAIndex = randomIndex;
				playerB = l0[pos]; playerBIndex = pos; 
			} else {
				playerA = l0[pos]; playerAIndex = pos;
				playerB = l0[randomIndex]; playerBIndex = randomIndex;
			}
			
		}
		
		Scanner scan = new Scanner (System.in);
		
		System.out.println("----------------------------");
		System.out.println("JcJ (Jugador contra Jugador)");
		System.out.println("----------------------------");
		
		//PlayerA's data
		int hpA = l2[playerAIndex];
		int adA = l3[playerAIndex];
		int defA = l4[playerAIndex];
		int xpA = l7[playerAIndex];
		int velA = l5[playerAIndex];
		
		String spellsA [] = new String [spellCount];
		int spellsCountA = 0;
		
		int spellPos = 0;
		for (int col = 0; col < spellCount; col++) { //Fill spellsA list
			if (matrix[playerAIndex][col] != 0) {
				spellsA[spellPos] = l8[col];
				spellPos++;
				spellsCountA++;
				
			}
		}
		
		//playerB's data
		int hpB = l2[playerBIndex];
		int adB = l3[playerBIndex];
		int defB = l4[playerBIndex];
		int xpB = l7[playerBIndex];
		int velB = l5[playerBIndex];
		
		String spellsB [] = new String [spellCount];
		int spellsCountB = 0;
		
		spellPos = 0;
		for (int col = 0; col < spellCount; col++) { //Fill spellsB list
			if (matrix[playerBIndex][col] != 0) {
				spellsB[spellPos] = l8[col];
				spellPos++;
				spellsCountB++;
				
			}
		}
		int round = 1;
		
		while (hpB > 0 && hpA > 0) {
			System.out.println("============= RONDA " + round +" =============");
			System.out.println("====================================");
			System.out.println("Estadísticas del jugador: " + playerA);
			System.out.println("====================================");
			System.out.println(" - Puntos de vida: " + hpA + "\n - Ataque: " + adA + "\n - Defensa: " + defA + "\n - Velocidad: " + velA);
			System.out.println("====================================");
			System.out.println("Estadísticas del jugador: " + playerB );
			System.out.println("====================================");
			System.out.println(" - Puntos de vida: " + hpB + "\n - Ataque: " + adB + "\n - Defensa: " + defB + "\n - Velocidad: " + velB);
			System.out.println("-----------------------------");
			System.out.println(playerA + " qué desea lanzar?");
			System.out.println("");
			System.out.println("a) Ataque básico");
			System.out.println("b) Hechizo");
			System.out.print("Ingrese la opción: ");
			String option = scan.nextLine();
			System.out.println("");
		
			if (option.equals("a") || option.equals("A")) {
				//Basic Attack
				if (defB >= adA) {
					defB -= adA;
					
					System.out.println("Le has quitado " + adA +" de defensa a " + playerB + " => Defensa actual: " + defB);
					System.out.println("");
				} else {
					hpB += defB - adA;
					defB = 0;
					System.out.println("Le has quitado " + (defB-adA) + " de vida a " + playerB + " => Vida actual: " + hpB);
					System.out.println("");
					
				}
				
			} else if (option.equals("b") || option.equals("B")) {
				System.out.println("");
				System.out.println("--------------------");
				System.out.println("Hechizos disponibles");
				System.out.println("--------------------");
				System.out.println("Qué hechizo de sus disponibles desea lanzar?");
				for (int i = 0; i < spellsCountA; i++) {
					System.out.println(i+1 + ".- " + spellsA[i]);
				}
				System.out.println("");
				System.out.print("Ingrese la opción: ");
				int spellOption = scan.nextInt(); //if we subtract 1 to this option, we will get the index of spell (in spellList A or B)
				scan.nextLine();
				
				int apA = l9[spellOption-1];
				
				if (defB >= apA) {
					defB -= apA;
					System.out.println("Le has quitado " + apA +" de defensa a " + playerB + " => Defensa actual: " + defB);
					System.out.println("");
				} else {
					hpB += defB - apA;
					defB = 0;
					System.out.println("Le has quitado " + (defB-apA) + " de vida a " + playerB + " => Vida actual: " + hpB);
					System.out.println("");
					
				}
			}
			if (hpB <= 0) { 
				//PLAYER A WINNER
				System.out.println("El jugador " + playerA + " ha ganado el combate, sumas 250 puntos de XP!");
				xpA += 250;
				round ++;
				Scanner readPlayerA = new Scanner (players);
				String oldTextXP = "";
				while (readPlayerA.hasNextLine()) {
					String line = readPlayerA.nextLine();
					if (line.contains(playerA)) {
						String[] split = line.split(",");
						int xp = Integer.parseInt(split[7]);
						oldTextXP+= split[0] + "," + split[1] + "," + split[2] + "," + split[3] + "," + split[4] + "," + split[5] + "," + split[6] + "," + (xp+250) + "\n";
					} else {
						oldTextXP += line + "\n";
					}
				}
				
				FileWriter writerPlayerXp = new FileWriter ("Jugadores.txt");
				writerPlayerXp.write(oldTextXP);
				
				writerPlayerXp.close();
				
				break;
			}

			System.out.println("====================================");
			System.out.println("Estadísticas del jugador: " + playerA);
			System.out.println("====================================");
			System.out.println(" - Puntos de vida: " + hpA + "\n - Ataque: " + adA + "\n - Defensa: " + defA + "\n - Velocidad: " + velA);
			System.out.println("====================================");
			System.out.println("Estadísticas del jugador: " + playerB );
			System.out.println("====================================");
			System.out.println(" - Puntos de vida: " + hpB + "\n - Ataque: " + adB + "\n - Defensa: " + defB + "\n - Velocidad: " + velB);
			System.out.println("-----------------------------");
			System.out.println(playerB + " qué desea lanzar?");
			System.out.println("");
			System.out.println("a) Ataque básico");
			System.out.println("b) Hechizo");
			System.out.print("Ingrese la opción: ");
			option = scan.nextLine();
			System.out.println("");
		
			if (option.equals("a") || option.equals("A")) {
				//Basic Attack
				if (defA >= adB) {
					defA -= adB;
					System.out.println("Le has quitado " + adB +" de defensa a " + playerA + " => Defensa actual: " + defA);
				} else {
					hpA += defA - adB;
					defA = 0;
					System.out.println("Le has quitado " + (defA - adB) + " de vida a " + playerA + " => Vida actual: " + hpA);
					
				}
				
			} else if (option.equals("b") || option.equals("B")) {
				System.out.println("");
				System.out.println("--------------------");
				System.out.println("Hechizos disponibles");
				System.out.println("--------------------");
				System.out.println("Qué hechizo de sus disponibles desea lanzar?");
				for (int i = 0; i < spellsCountB; i++) {
					System.out.println(i+1 + ".- " + spellsB[i]);
				}
				
				System.out.print("Ingrese la opción: ");
				int spellOption = scan.nextInt(); //if we subtract 1 to this option, we will get the index of spell (in spellList A or B)
				scan.nextLine();
				
				int apB = l9[spellOption-1];
				
				if (defA >= apB) {
					defA -= apB;
					System.out.println("Le has quitado " + apB +" de defensa a " + playerA + " => Defensa actual: " + defA);
					System.out.println("");
				} else {
					hpA += defA - apB;
					defA = 0;
					System.out.println("Le has quitado " + (defA - apB) + " de vida a " + playerA + " => Vida actual: " + hpA);
					System.out.println("");
				}
			}
			
			if (hpA <= 0) {
				//PLAYER B WNNER
				System.out.println("El jugador " + playerB + " ha ganado el combate, sumas 250 puntos de XP!");
				xpB += 250;
				round ++;
				Scanner readPlayerB = new Scanner (players);
				
				String oldTextXP = "";
				while (readPlayerB.hasNextLine()) {
					String line = readPlayerB.nextLine();
					if (line.contains(playerB)) {
						String[] split = line.split(",");
						int xp = Integer.parseInt(split[7]);
						oldTextXP+= split[0] + "," + split[1] + "," + split[2] + "," + split[3] + "," + split[4] + "," + split[5] + "," + split[6] + "," + (xp+250) + "\n";
					} else {
						oldTextXP += line + "\n";
					}
				}
				
				FileWriter writerPlayerXp = new FileWriter ("Jugadores.txt");
				writerPlayerXp.write(oldTextXP);
				
				writerPlayerXp.close();
				
				break;
			}
			round ++;
		}//while finish
	}
	
	
	public static void learnSpell(File players, File playersSpell, int pos, String l0[], int l7[], int l6[], String l8[], int spellCount) throws IOException {
		//add spell in file "HechizosJugador.txt" (name, spell) and file "Jugadores.txt" (modify numOfSpells)
		
		if (l6[pos] * 1000 + 1000 <= l7[pos]) { //Verify if playerspell's quantity is equals to the player xp and if player would could learn a new spell
			//LEARN SPELL
			
			Scanner readPlayers = new Scanner (players);
			Scanner readPlayerSpell = new Scanner (playersSpell);
			
			Random rand = new Random();
			int randomIndex = rand.nextInt(0, spellCount-1);
			
			String spell = l8[randomIndex];
			String player = l0[pos];
			
			String oldTextPlayer = "";
			String oldTextPlayerSpell = "";
			
			while (readPlayers.hasNextLine()) {
				
				String line = readPlayers.nextLine();
			
				if (line.contains(player)) {
					String split[] = line.split(",");
					int numSpells = Integer.parseInt(split[6]);
					oldTextPlayer += split[0] + "," + split[1] + "," + split[2] + "," + split[3] + "," + split[4] + "," + split[5] + "," + Integer.toString(numSpells+1) + "," + split[7] +"\n";
				} else {
					oldTextPlayer += line + "\n";
				}
				
			}
			readPlayers.close();
			
			while (readPlayerSpell.hasNextLine()) {
				
				String line = readPlayerSpell.nextLine();
				oldTextPlayerSpell += line +"\n";
				
			}
			
			oldTextPlayerSpell += player + "," + spell;
			readPlayerSpell.close();
			
			FileWriter playerWriter = new FileWriter("Jugadores.txt");
			FileWriter spellWriter = new FileWriter ("HechizosJugadores.txt");
			
			playerWriter.write(oldTextPlayer);
			playerWriter.close(); 
			
			spellWriter.write(oldTextPlayerSpell);
			spellWriter.close();
			
			//Tests
			//System.out.println(oldTextPlayer);
			//System.out.println("--------------------------");
			//System.out.println(oldTextPlayerSpell);
			//System.out.println("--------------------------");
			
			
			
			System.out.println(player + "has aprendido el hechizo: " + spell +"!");
			
		}
		else {
			System.out.println("Experiencia insuficiente para aprender un nuevo hechizo");
		}
		
	}
	
	
	public static void viewPlayerStatistics (String l0[], int l2[], int l3[], int l4[], int l5[], int l6[], int l7[], int playerCount) {
		Scanner scan = new Scanner (System.in);
		System.out.println("------------------------------");
		System.out.println("Ver estadísticas de un jugador");
		System.out.println("------------------------------");
		System.out.print("Ingrese el usuario para revisar sus estadísticas: ");
		String player = scan.nextLine();
		 
		int playerIndex = -1;
		for (int i = 0; i < playerCount; i++) { //Searching player index
			if (player.equals(l0[i])) {
				playerIndex = i;
			}
		}
		System.out.println("");
		System.out.println("-------------------------");
		System.out.println("Estadísticas de " + player);
		System.out.println("-------------------------");
		System.out.println("");
		System.out.println("Puntos de vida: " + l2[playerIndex]);
		System.out.println("Ataque: " + l3[playerIndex]);
		System.out.println("Defensa: " + l4[playerIndex]);
		System.out.println("Velocidad: " + l5[playerIndex]);
		System.out.println("Numero de Hechizos: " + l6[playerIndex]);
		System.out.println("Experiencia: " + l7[playerIndex]);
	}
	 
	public static void viewSpellStatistics(int matrix[][], String l0[], String l8[], int l9[], int spellCount, int playerCount) {
		
		Scanner scan = new Scanner (System.in);
		System.out.println("------------------------------------------");
		System.out.println("Ver estadísticas de hechizos de un jugador");
		System.out.println("------------------------------------------");
		System.out.print("Ingrese el usuario para revisar su lista de hechizos: ");
		String player = scan.nextLine();
		 
		int playerIndex = -1;
		for (int i = 0; i < playerCount; i++) { //Searching player index
			if (player.equals(l0[i])) {
				playerIndex = i;
			}
		}
				
		System.out.println("");
		System.out.println("-------------------------");
		System.out.println("Lista Hechizos de " + player);
		System.out.println("-------------------------");
		System.out.println("");
		for (int col = 0; col < spellCount; col++) {
			if (matrix[playerIndex][col] != 0) {
				System.out.println("- " + l8[col] + " / Poder de Ataque: " + l9[col]);
			
			}
		}
	}
	
	
	public static void viewRanking(String l0[], int l7[] ) {
		
		System.out.println("------------------------------------");
		System.out.println("TOP 10 Jugadores con más experiencia");
		System.out.println("------------------------------------");
		
		//Bubble Sort
		bubbleSort(l0,l7);
		for (int i = 0; i < 10; i++) {
			System.out.println(i+1 + ".- "+ l0[i] + " " + l7[i]);
		}
	}
	
	
	public static int[] bubbleSort(String l0[], int l7[]){
        boolean flag = true;	
        while (flag) {
        	flag = false;   
        	for (int i = 0;  i < l7.length -1;  i++) {
        		if (l7[i] < l7[i+1]) {
        			intSwap(l7, i, i+1);
        			stringSwap(l0, i, i+1);
        			flag = true; 
        		} 
        	} 
        } 
        return l7;
    }
	
	
	public static void intSwap(int list[], int index1, int index2) {
		int temp = list[index1];
		list[index1] = list[index2];
		list[index2] = temp;
	}
	
	
	public static void stringSwap(String list[], int index1, int index2) {
		String temp = list[index1];
		list[index1] = list[index2];
		list[index2] = temp;
	}
	
}


package classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

	public Validation(){}
	
	public static String validatorSingUp (String username, String email, String password1, String password2){
		
		Pattern usernamePattern = Pattern.compile("[a-zA-Z0-9]*");
		Matcher usernameMatcher = usernamePattern.matcher(username);
		
		Pattern emailPattern = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,3}$");
		Matcher emailMatcher = emailPattern.matcher(email);
		
		Pattern password1Pattern = Pattern.compile("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{7,20})");
		Matcher password1Matcher = password1Pattern.matcher(password1);
						
		if ((username == null)||(username == "")){
			return ("El nombre de usuario est� vac�o");
		} 
		else if (usernameMatcher.matches()!=true){
			return ("El nombre de usuario s�lo debe tener n�meros y letras");
		}
		else if ((email == null)||(email == "")) {
			return ("El email est� vac�o");
		}
		else if (emailMatcher.matches()!=true){
			return ("La direcci�n de correo electr�nico no es correcta");
		}
		else if ((password1 == null)||(password1 == "")) {
			return ("El primer campo de contrase�a est� vac�o");
		}
		else if (password1Matcher.matches()!=true){
			return ("La contrase�a debe tener como entre 7 y 20 caracteres, un n�mero, una letra may�scula y una min�scula");
		}
		else if ((password2 == null)||(password2 == "")) {
			return ("El segundo campo de contrase�a est� vac�o");
		}
		else if (password2.equals(password1)==false){
			return ("Las contrase�as no coinciden");
		}
		else return null;
	}
	
	public static String validatorLogIn (String username, String password){
		
		Pattern usernamePattern = Pattern.compile("[a-zA-Z0-9]*");
		Matcher usernameMatcher = usernamePattern.matcher(username);
		
		Pattern passwordPattern = Pattern.compile("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{7,20})");
		Matcher passwordMatcher = passwordPattern.matcher(password);
						
		if ((username == null)||(username == "")){
			return ("El nombre de usuario est� vac�o");
		} 
		else if (usernameMatcher.matches()!=true){
			return ("El nombre de usuario s�lo debe tener n�meros y letras");
		}
		else if ((password == null)||(password == "")) {
			return ("El primer campo de contrase�a est� vac�o");
		}
		else if (passwordMatcher.matches()!=true){
			return ("La contrase�a debe tener como entre 7 y 20 caracteres, un n�mero, una letra may�scula y una min�scula");
		}
		else return null;
	}
}

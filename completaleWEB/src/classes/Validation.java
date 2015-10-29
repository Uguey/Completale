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
			return ("El nombre de usuario está vacío");
		} 
		else if (usernameMatcher.matches()!=true){
			return ("El nombre de usuario sólo debe tener números y letras");
		}
		else if ((email == null)||(email == "")) {
			return ("El email está vacío");
		}
		else if (emailMatcher.matches()!=true){
			return ("La dirección de correo electrónico no es correcta");
		}
		else if ((password1 == null)||(password1 == "")) {
			return ("El primer campo de contraseña está vacío");
		}
		else if (password1Matcher.matches()!=true){
			return ("La contraseña debe tener como entre 7 y 20 caracteres, un número, una letra mayúscula y una minúscula");
		}
		else if ((password2 == null)||(password2 == "")) {
			return ("El segundo campo de contraseña está vacío");
		}
		else if (password2.equals(password1)==false){
			return ("Las contraseñas no coinciden");
		}
		else return null;
	}
	
	public static String validatorLogIn (String username, String password){
		
		Pattern usernamePattern = Pattern.compile("[a-zA-Z0-9]*");
		Matcher usernameMatcher = usernamePattern.matcher(username);
		
		Pattern passwordPattern = Pattern.compile("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{7,20})");
		Matcher passwordMatcher = passwordPattern.matcher(password);
						
		if ((username == null)||(username == "")){
			return ("El nombre de usuario está vacío");
		} 
		else if (usernameMatcher.matches()!=true){
			return ("El nombre de usuario sólo debe tener números y letras");
		}
		else if ((password == null)||(password == "")) {
			return ("El primer campo de contraseña está vacío");
		}
		else if (passwordMatcher.matches()!=true){
			return ("La contraseña debe tener como entre 7 y 20 caracteres, un número, una letra mayúscula y una minúscula");
		}
		else return null;
	}
}

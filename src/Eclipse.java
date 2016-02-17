/*
 * Eclipse.java
 * This example shows Reflection by 
 * mimicking an autofill popup window from Eclipse.
 * In particular, it prompts the user for the fully qualified
 * name of a class and it prints all the fields and methods of 
 * that class.  
 * 
 * Non-statics entities are printed before statics, and 
 * fields are printed before methods. 
 * Within each group, items are alphabetized by name.
 */

import java.lang.reflect.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;

public class Eclipse {
	// you always want a small main...
	public static void main(String[] args) {
		Eclipse reflect = new Eclipse();
		reflect.interaction();
	}

	/*
	 * Main interaction:  Have the user enter a class
	 * name (fully specified) and then print its information.
	 */
	public void interaction(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the fully specified class to use or 'q' to quit.");
		String className = scan.nextLine();
		try{
			Class<?> theClass = Class.forName(className);
			Field[] fields = theClass.getFields();
			Method[] methods = theClass.getMethods();

			// The boolean argument tells whether or not to look at static
			// or non-static entities.
			printFields(false, fields);
			printMethods(false, methods);
			printFields(true, fields);
			printMethods(true, methods);
		}
		catch(ClassNotFoundException e)
		{
			System.err.println("I don't know that class name.  Quitting...");
			// uncomment this for debugging.  Keep it commented for release.
			//e.printStackTrace();
		}
		scan.close();
	}

	// This method prints all the fields whose static-ness matches the 
	// boolean argument.  It sorts them by name first.  If it prints
	// a static field it prints 'S:' first.
	// Format is:
	// <NAME> : <TYPE> - <DECLARING-CLASS>
	
	public void printFields(boolean staticOnlyPrinted, Field[] fields) {
		// Alphabetically sort the fields
		Arrays.sort(fields, new Comparator<Field>() {
			public int compare(Field o1, Field o2){
				return o1.getName().compareTo(o2.getName());
			}
		});	   
		// Print the information
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers()) == staticOnlyPrinted) {
				if (Modifier.isStatic(f.getModifiers())) {
					System.out.print("S:");
				}
				System.out.print(f.getName() + " : ");
				System.out.print(f.getType().getName() + " - ");
				System.out.println(f.getDeclaringClass().getName());
			}
		}
	}

	// This method prints all the methods whose static-ness matches the 
	// boolean argument.  It sorts them by name first.  If it prints
	// a static method it prints 'S:' first.
	// Format is:
	// <NAME>(<PARA-TYPE>, <PARA-TYPE>, <...> ) : <RETURN-TYPE> - <DEC-CLASS>

	public void printMethods(boolean staticOnlyPrinted, Method[] methods) {
		// Sort alphabetically
		Arrays.sort(methods, new Comparator<Method>() {
			public int compare(Method o1, Method o2){
				return o1.getName().compareTo(o2.getName());
			}
		});	

		// Print the method information
		for (Method m : methods) {
			if (Modifier.isStatic(m.getModifiers()) == staticOnlyPrinted) {
				if (Modifier.isStatic(m.getModifiers())) {
					System.out.print("S:");
				}
				System.out.print(m.getName() + "(");
				Class<?> ret = m.getReturnType();
				Class<?>[] params = m.getParameterTypes();
				for (int i = 0; i < params.length - 1; i++) {
					System.out.print(params[i] + ", ");
				}
				if (params.length != 0) {
					System.out.print(params[params.length - 1]);
				}
				System.out.print(") : " + ret + " - "
						+ m.getDeclaringClass().getName());
				System.out.println();
			}
		}
	}
}

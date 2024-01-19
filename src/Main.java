import java.awt.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static void main(String[] args) {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Window window = new Window(screenSize);
		
		window.start();
		
	}

}

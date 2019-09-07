package skel3;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import skel3.Main2;

@SpringBootApplication
public class ReWriterApp2 implements CommandLineRunner{
	public static void main(String[] args) {
		SpringApplication.run(ReWriterApp2.class,args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main2 m = new Main2();
		m.start(args);		
	}
}

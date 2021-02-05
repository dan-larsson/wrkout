package com.wrkout;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		int i, exitSignal = 1;
		int activityIndex = 2;
		ActivityHandler handler = new ActivityHandler();
		String[] all = handler.oneOfEach();
		BaseActivity current;
		HashMap<String, String> defaults = new HashMap<String, String>();

		// add som initial defaults
		defaults.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		defaults.put("time", "50");
		defaults.put("length", "10");
		defaults.put("weight", "50");
		defaults.put("reps", "10");
		defaults.put("sets", "3");

		handler.read();

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		for (i = 0; i < all.length; i++) {
			System.out.printf("%3s. %s\n", i+1, all[i]);
		}
		exitSignal = i+1;
		System.out.printf("%3s. Save & exit\n", exitSignal);

		while (true) {
			activityIndex = BaseActivity.promptForInt(input, "Enter activity", String.valueOf(exitSignal));
			if (activityIndex == exitSignal) break;
			current = handler.newActivity(all[activityIndex - 1]);
			System.out.printf("> %s\n", current.getName());
			current.prompt(input, defaults);
			handler.add(current);
		}

		handler.write();
		System.out.println("Wrote:");
		handler.print();
    }
}

package com.simon.credit;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.simon.credit.util.CommonUtils;

public class TTT {

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readLines(new File("e:/time.txt"), "UTF-8");

		int sum = 0;
		int count = 0;

		for (String line : lines) {
			String[] times = line.split("	");
			Date registerTime = CommonUtils.parseDate(times[0]);
			Date paymentTime = CommonUtils.parseDate(times[1]);
			sum += ((paymentTime.getTime() - registerTime.getTime()) / 1000 / 60);
			// System.out.println((paymentTime.getTime()-registerTime.getTime())/1000/60);
			if ((paymentTime.getTime() - registerTime.getTime()) / 1000 / 60 > 60) {
				count++;
			}
		}

		System.out.println(sum / 24);
		System.out.println(count);
	}

}

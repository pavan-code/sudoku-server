package com.spring.security;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class Sudoku {

	int[] mat[];
	int N; // number of columns/rows.
	int SRN; // square root of N
	int K; // No. Of missing digits

	public void fillValues() {

		fillDiagonal();
		fillRemaining(0, SRN);
		removeKDigits();
	}

	void fillDiagonal() {

		for (int i = 0; i < N; i = i + SRN)
			fillBox(i, i);
	}

	boolean unUsedInBox(int rowStart, int colStart, int num) {
		for (int i = 0; i < SRN; i++)
			for (int j = 0; j < SRN; j++)
				if (mat[rowStart + i][colStart + j] == num)
					return false;

		return true;
	}

	void fillBox(int row, int col) {
		int num;
		for (int i = 0; i < SRN; i++) {
			for (int j = 0; j < SRN; j++) {
				do {
					num = randomGenerator(N);
				} while (!unUsedInBox(row, col, num));

				mat[row + i][col + j] = num;
			}
		}
	}

	int randomGenerator(int num) {
		return (int) Math.floor((Math.random() * num + 1));
	}

	boolean CheckIfSafe(int i, int j, int num) {
		return (unUsedInRow(i, num) && unUsedInCol(j, num) && unUsedInBox(i - i % SRN, j - j % SRN, num));
	}

	boolean unUsedInRow(int i, int num) {
		for (int j = 0; j < N; j++)
			if (mat[i][j] == num)
				return false;
		return true;
	}

	boolean unUsedInCol(int j, int num) {
		for (int i = 0; i < N; i++)
			if (mat[i][j] == num)
				return false;
		return true;
	}

	boolean fillRemaining(int i, int j) {

		if (j >= N && i < N - 1) {
			i = i + 1;
			j = 0;
		}
		if (i >= N && j >= N)
			return true;

		if (i < SRN) {
			if (j < SRN)
				j = SRN;
		} else if (i < N - SRN) {
			if (j == (int) (i / SRN) * SRN)
				j = j + SRN;
		} else {
			if (j == N - SRN) {
				i = i + 1;
				j = 0;
				if (i >= N)
					return true;
			}
		}

		for (int num = 1; num <= N; num++) {
			if (CheckIfSafe(i, j, num)) {
				mat[i][j] = num;
				if (fillRemaining(i, j + 1))
					return true;

				mat[i][j] = 0;
			}
		}
		return false;
	}

	public void removeKDigits() {
		int count = K;
		while (count != 0) {
			int cellId = randomGenerator(N * N) - 1;

			int i = (cellId / N);
			int j = cellId % 9;
			if (j != 0)
				j = j - 1;

			if (mat[i][j] != 0) {
				count--;
				mat[i][j] = 0;
			}
		}
	}

	@GetMapping("/generate-sudoku")
	public int[][] generate() {
//		System.out.println("controller called");
		int N = 9, K = 30;
		this.N = N;
		this.K = K;

		// Compute square root of N
		Double SRNd = Math.sqrt(N);
		SRN = SRNd.intValue();

		mat = new int[N][N];

		fillValues();
//		System.out.println(mat.toString());
		return mat;
	}

}

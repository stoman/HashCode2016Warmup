#ifndef INPUT_HPP_
#define INPUT_HPP_

#include <iostream>
#include <vector>
#include <set>
#include <fstream>

#define WIDTH 100
#define HEIGHT 100

#define SQUARE 1
#define LINE 2
#define ERASE 3

#define DEBUG 1 // set debug mode

#if DEBUG
#define log(...) {\
    char str[100];\
    sprintf(str, __VA_ARGS__);\
    std::cout << "[" << __FUNCTION__ << "][Line " << __LINE__ << "] " << str << std::endl;\
    }
#else
#define log(...)
#endif

using namespace std;

struct Operation {
	int type; // 1 = PAINT_SQUARE, 2 = PAINT_LINE, 3 = ERASE_CELL
	int values[4];
	Operation(Operation const& out) {
		type = out.type;
		values[0] = out.values[0];
		values[1] = out.values[1];
		values[2] = out.values[2];
		values[3] = out.values[3];
	}
	
	Operation(int v1, int v2, int v3) : type(SQUARE) {
		values[0] = v1;
		values[1] = v2;
		values[2] = v3;
	}
	Operation(int v1, int v2, int v3, int v4) : type(LINE) {
		values[0] = v1;
		values[1] = v2;
		values[2] = v3;
		values[3] = v4;
	}
	Operation(int v1, int v2) : type(ERASE) {
		values[0] = v1;
		values[1] = v2;
	}

	template <typename Stream>
	void print(Stream &fout) {
		if (type == SQUARE) {
			fout << "PAINT_SQUARE " << values[0] << " " << values[1] << " " << values[2] << std::endl;
		} else if (type == LINE) {
			fout << "PAINT_LINE " << values[0] << " " << values[1] << " " << values[2] << " " << values[3] << std::endl;
		} else if (type == ERASE) {
			fout << "ERASE_CELL " << values[0] << " " << values[1] << std::endl;
		} else {
			log("Operation with false type: %d", type);
		}
	}
};

Operation op_paint_square(int r, int c, int s) {
	return Operation(r, c, s);
}

Operation op_paint_line(int r1, int c1, int r2, int c2) {
	return Operation(r1, c1, r2, c2);
}

Operation op_erase_cell(int r, int c) {
	return Operation(r, c);
}

class Input {
private:
public:
	int ROWS, COLUMNS;
	bool is_painted[HEIGHT][WIDTH]; // ROWS x COLUMNS
	template <typename Stream>
	void read_input(Stream &fin);
};

template <typename Stream>
void Input::read_input(Stream &fin) {
	fin >> ROWS >> COLUMNS;
	for (int y = 0; y < ROWS; ++y)
		for (int x = 0; x < COLUMNS; ++x) {
			char c;
			fin >> c;
			is_painted[y][x] = (c == '#');
		}
}

class Output {
private:
	Input input;
	
	vector<Operation> ops;
	vector<Operation> erase;
public:
    bool status[HEIGHT][WIDTH];
    
    Output(Output const& out) {
        input = out.input;
	    for (int i = 0; i < input.ROWS; ++i)
	        for (int j = 0; j < input.COLUMNS; ++j)
	             status[i][j] = out.status[i][j];
	    for (auto& op : out.ops) {
	    	if (op.type == 0) {
	    		log("Operation with false type %d",op.type);
	    	}
	        ops.push_back(op);
	    }
	    for (auto& er : out.erase)
	        erase.push_back(er);
    }
    
	Output(Input const& in) : input(in) {
	    for (int i = 0; i < input.ROWS; ++i) {
	        fill_n(status[i], input.COLUMNS, false);   
	    }
	}
	
	Output clone() {
        return Output(*this);
    }

	int score();
	void add_operation(Operation const& op);
	bool check();
    template <typename Stream>
	void print_output(Stream& sout);
	template <typename Stream>
	void print_output_squared(Stream& sout);

};

void Output::add_operation(Operation const& op) {
	if (op.type == SQUARE) {
		// square
		int fromrow = op.values[0] - op.values[2];
		int fromcol = op.values[1] - op.values[2];
		int size = op.values[2] * 2 + 1;
		for (int r = fromrow; r < fromrow+size; r++) {
			for (int c = fromcol; c < fromcol + size; c++) {
				status[r][c] = true;	
				if (!input.is_painted[r][c]) {
					// cout << "addOperation: Adding ERASE operation" << endl;
				    erase.push_back(op_erase_cell(r, c));
				}
			}
		}
		ops.push_back(op);
	} else if (op.type == LINE) {
		// cout << "addOperation: Add PAINT_LINE operation" << endl;
		// line
		int r1 = op.values[0];
		int c1 = op.values[1];
		int r2 = op.values[2];
		int c2 = op.values[3];
		if (r1 == r2) {
			for (int c = c1; c <= c2; c++) {
				status[r1][c] = true;
			}
		} else if (c1 == c2) {
			for (int r = r1; r <= r2; r++) {
				status[r][c1] = true;
			}
		} else {
			log("ERROR: Invalid line");
		}
		ops.push_back(op);
	} else {
		log("ERROR: Invalid operation");
	}
}

int Output::score() {
	return ops.size() + erase.size();
}


bool Output::check() {
	int count = 0;
	for(int r = 0; r < HEIGHT; r++) {
		for (int c = 0; c < WIDTH; c++) {
			if (input.is_painted[r][c] != status[r][c]) {
				count++;
			}
		}
	}
	if (erase.size() != count) {
		log("check: Found %d deviations and %zu erase operations", count, erase.size());
	}
	return erase.size() == count;
}

template <typename Stream>
void Output::print_output(Stream &sout) {
	sout << (ops.size() + erase.size()) << std::endl;
	for (auto& op : ops)
		op.print(sout);
	for (auto& op : erase)
		op.print(sout);
}


template <typename Stream>
void Output::print_output_squared(Stream &sout) {
	for (int y = 0; y < input.ROWS; ++y) {
		for (int x = 0; x < input.COLUMNS; ++x) {
			if (status[y][x]) {
				sout << "X";
			} else if (input.is_painted[y][x]) {
				sout << "#";
			} else {
				sout << ".";
			}
		}
		sout << endl;
	}
}

#endif

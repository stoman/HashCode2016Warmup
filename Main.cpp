#include "Input.hpp"
#include <iostream>
#include <map>
#include <fstream>
#include <climits>

using namespace std;

// ifstream fin("cases/handcrafted/margin_with_square.in");
ifstream fin("cases/official/learn_and_teach.in");
ofstream fout("example.out");

vector<Output> distributeSquares(Input const& input);
vector<Output> fillRemainingSquares(Input const& input, Output const& output);

int main()
{
	cout << "main: Reading input" << endl;
	
	if (fin.fail()) {
		cout << "File con not be read" << endl;
		return 1;
	}
	
	Input input;
	input.read_input(fin);

	cout << "main: Distributing squares" << endl;
	vector<Output> outputs_dist = distributeSquares(input);
	cout << "main: " << outputs_dist.size() << " solutions found" << endl;

	//  	output.add_operation(op_paint_line(0, 5, 3, 5));

	cout << "main: Filling with fillRemainingSquares" << endl;
	
	vector<Output> outputs_fillrem;
	int i = 1;
	for (auto out_dist : outputs_dist) {
		cout << "main: fillRemaining call #" << i << endl;
		for (auto out_fillrem : fillRemainingSquares(input, out_dist)) {
			outputs_fillrem.push_back(out_fillrem);
		}
	}
    Output output(input);
	for (auto out_fillrem : fillRemainingSquares(input, output)) {
		outputs_fillrem.push_back(out_fillrem);
	}

    cout << "main: " << outputs_fillrem.size() << " solutions found" << endl;
    
  	Output* best = NULL;
  	for (auto& out : outputs_fillrem) {
  		// cout << "found solution with score " << out.ops.size() << endl;
  		if (best == NULL || out.score() < best->score()) {
  			best = &out;
  		}
  	}

  	if (best != NULL) {
  		cout << "main: score of best solution: " << best->score() << endl;
  		best->print_output(fout);
		best->print_output(cout);
  	} else {
  		cout << "main: no best solution" << endl;
  	}

	return 0;
}

#define filledFactor 1

double getBest(int x, int y, int** presumCol, int** presumRow, Input const& input) {
    // cout << "entering getbest at ";
    // cout << x << " " << y << endl;
	int filled = 0;
	int best = -1;
	if (input.is_painted[x][y]) {
		filled = 1;
	}
	int squares = 1;
	for (int size = 3; size < 1000; size += 2) {
	   // cout << "trying size " << size << endl;
		if (x+size > input.COLUMNS || y+size > input.ROWS) {
			break;
		}
		squares += 4*(size-2)+4;
		filled += presumCol[y+size][x+size-2]-presumCol[y][x+size-2];
// 		cout << filled << " after 1" << endl;
		filled += presumCol[y+size][x+size-1]-presumCol[y][x+size-1];
// 		cout << filled << " after 2" << endl;
		filled += presumRow[y+size-2][x+size-2]-presumRow[y+size-2][x];
// 		cout << filled << " after 3" << endl;
		filled += presumRow[y+size-1][x+size-2]-presumRow[y+size-1][x];
// 		cout << filled << " after 4" << endl;
		if (filled > filledFactor*squares) {
			best = size;
		}
// 		cout << squares << " squares" << endl;
// 		cout << filled << " filled" << endl;
	}
	if (best > 0) {
		return best / 2;
	}
	return -1;
}

vector<Output> distributeSquares(Input const& input) {
 	Output sol(input);

	int **presumRow;
    int **presumCol;

    // Allocate memory
    presumCol = new int*[input.ROWS+1];
    presumRow = new int*[input.ROWS];
    for (int i = 0; i < input.ROWS; ++i){
        presumCol[i] = new int[input.COLUMNS];
        presumRow[i] = new int[input.COLUMNS+1];
    }
    presumCol[input.ROWS] = new int[input.COLUMNS];
    
	
	for(int y = 0; y < input.ROWS; y++) {
 		presumRow[0][0] = 0;
		for(int x = 0; x < input.COLUMNS; x++) {
			presumRow[y][x+1]=presumRow[y][x]+(input.is_painted[y][x]?1:0);
		}
	}
	for(int x = 0; x < input.COLUMNS; x++) {
		presumCol[0][x] = 0;
		for(int y = 0; y < input.ROWS; y++) {
			presumCol[y+1][x]=presumCol[y][x]+(input.is_painted[y][x]?1:0);
		}
	}
	
// 	cout << "presumCol" << endl;
// 	printArray(presumCol,input.ROWS+1,input.COLUMNS);
// 	cout << "presumRow" << endl;
// 	printArray(presumRow,input.ROWS,input.COLUMNS+1);
	
	for(int x = 0; x < input.COLUMNS; x++) {
		for(int y = 0; y < input.ROWS; y++) {
			if (sol.status[y][x]) {
				continue;
			}
			int best = getBest(x,y,presumCol,presumRow,input);
			if (best > -1) {
			    cout << "distributeSquares: adding square of size " << best << " at " << y+best << " " << x+best << endl;
				// add square of size best here
				sol.add_operation(op_paint_square(y+best,x+best,best) );
			}
		}
	}

    //de-allocate
    for (int i = 0; i < input.ROWS; ++i){
        delete [] presumCol[i];
        delete [] presumRow[i];
    }
    delete [] presumCol[input.ROWS];
    delete [] presumCol;
    delete [] presumRow;

	vector<Output> ret;
	ret.push_back(sol);
	return ret;
}

vector<Output> fillRemainingSquares(Input const& input, Output const& output) {
	//compute size of the subimages to consider
	int partsX = min(10, 1 + input.ROWS / 5);
	int partsY = min(10, 1 + input.COLUMNS / 5);
	int sizeX = 1 + (input.ROWS - 1) / partsX;
	int sizeY = 1 + (input.COLUMNS - 1) / partsY;
	
	cout << "fillRemainingSquares: partsX = " << partsX << endl;
	cout << "fillRemainingSquares: partsY = " << partsY << endl;

	//arrays to save where lines may begin or end if they are as long as possible
	int lineStartX[input.ROWS][input.COLUMNS], lineEndX[input.ROWS][input.COLUMNS], lineStartY[input.ROWS][input.COLUMNS], lineEndY[input.ROWS][input.COLUMNS];
	for(int x = 0; x < input.ROWS; x++) {
		for(int y = 0; y < input.COLUMNS; y++) {
			if(x == 0) {
				lineStartX[x][y] = input.is_painted[x][y] || output.status[x][y] ? x : -1;
			}
			else if(!input.is_painted[x][y] && !output.status[x][y]) {
				lineStartX[x][y] = -1;
			}
			else {
				lineStartX[x][y] = lineStartX[x-1][y] > -1 ? lineStartX[x-1][y] : x;
			}
			if(y == 0) {
				lineStartY[x][y] = input.is_painted[x][y] || output.status[x][y] ? y : -1;
			}
			else if(!input.is_painted[x][y] && !output.status[x][y]) {
				lineStartY[x][y] = -1;
			}
			else {
				lineStartY[x][y] = lineStartY[x][y-1] > -1 ? lineStartY[x][y-1] : y;
			}
		}
	}
	for(int x = input.ROWS - 1; x >= 0; x--) {
		for(int y = input.COLUMNS - 1; y >= 0; y--) {
			if(x == input.ROWS - 1) {
				lineEndX[x][y] = input.is_painted[x][y] || output.status[x][y] ? x : -1;
			}
			else if(!input.is_painted[x][y] && !output.status[x][y]) {
				lineEndX[x][y] = -1;
			}
			else {
				lineEndX[x][y] = lineEndX[x+1][y] > -1 ? lineEndX[x+1][y] : x;
			}
			if(y == input.COLUMNS - 1) {
				lineEndY[x][y] = input.is_painted[x][y] || output.status[x][y] ? y : -1;
			}
			else if(!input.is_painted[x][y] && !output.status[x][y]) {
				lineEndY[x][y] = -1;
			}
			else {
				lineEndY[x][y] = lineEndY[x][y+1] > -1 ? lineEndY[x][y+1] : y;
			}
		}
	}

	cout << "fillRemainingSquares: input.is_painted/output.status" << endl;
	for(int x = 0; x < input.ROWS; x++) {
		for(int y = 0; y < input.COLUMNS; y++) {
			cout << "\t" << input.is_painted[x][y] << "/" << output.status[x][y];
		}
		cout << endl;
	}
	
	cout << "fillRemainingSquares: lineStartX/lineEndX" << endl;
	for(int x = 0; x < input.ROWS; x++) {
		for(int y = 0; y < input.COLUMNS; y++) {
			cout << "\t" << lineStartX[x][y] << "/" << lineEndX[x][y];
		}
		cout << endl;
	}
	
	cout << "fillRemainingSquares: lineStartY/lineEndY" << endl;
	for(int x = 0; x < input.ROWS; x++) {
		for(int y = 0; y < input.COLUMNS; y++) {
			cout << "\t" << lineStartY[x][y] << "/" << lineEndY[x][y];
		}
		cout << endl;
	}
	
	cout << "fillRemainingSquares: finished computing helper arrays" << endl;
	
	//iterate over modes for quadrants of the algorithm
	vector<Output> ret;
	int countModesQuadrant = 2;
	for(int modeQuadrant = 0; modeQuadrant < 1 << countModesQuadrant; modeQuadrant++) {
		int modesQuadrant[countModesQuadrant], tmpModeQuadrant = modeQuadrant;
		for(int i = 0; i < countModesQuadrant; i++) {
			modesQuadrant[i] = tmpModeQuadrant % 2;
			tmpModeQuadrant /= 2;
		}
		
		cout << "fillRemainingSquares: modeQuadrant = " << modeQuadrant << endl;
		
		//prepare output objects
		Output modeQuadrantOutput(output);
		
		//iterate over squares and fill accordingly
		for (int tmpX = 0; tmpX < partsX; tmpX++) {
			int x = modesQuadrant[0] ? tmpX : partsX - 1 - tmpX;
			for (int tmpY = 0; tmpY < partsY; tmpY++) {
				int y = modesQuadrant[0] ? tmpY : partsY - 1 - tmpY;
				
				int partStartX = sizeX * x;
				int partEndX = min(sizeX * x + sizeX - 1, input.ROWS - 1);
				int partStartY = sizeY * y;
				int partEndY = min(sizeY * y + sizeY - 1, input.COLUMNS - 1);
				
				Output bestModeSquareOutput(output);
				bool bestModeSquareOutputEmpty = true;
				
				//iterate over modes for single squares of the algorithm
				int countModesSquare = 3;
				for(int modeSquare = 0; modeSquare < 1; modeSquare++) { // << countModesSquare
					int modesSquare[countModesSquare], tmpModeSquare = modeSquare;
					for(int i = 0; i < countModesSquare; i++) {
						modesSquare[i] = tmpModeSquare % 2;
						tmpModeSquare /= 2;
					}
					
					cout << "fillRemainingSquares: modeSquare = " << modeSquare << endl;
					
					//prepare output objects
					Output modeSquareOutput(modeQuadrantOutput);
					for(int tmpI = 0; tmpI <= partEndX - partStartX; tmpI++) {
						int i = partStartX + (modesSquare[0] ? tmpI : partEndX - partStartX - tmpI);
						for(int tmpJ = 0; tmpJ <= partEndY - partStartY; tmpJ++) {
							int j = partStartY + (modesSquare[1] ? tmpJ : partEndY - partStartY - tmpJ);
							
							//cout << "fillRemainingSquares: square " << i << " " << j << endl;
							//cout << "fillRemainingSquares: square tmp " << tmpI << " " << tmpJ << endl;
							
							if(!modeSquareOutput.status[i][j] && input.is_painted[i][j]) {
								int lengthX = lineEndX[i][j] - lineStartX[i][j] + 1;
								int lengthY = lineEndY[i][j] - lineStartY[i][j] + 1;
								if(lengthX > lengthY || (lengthX == lengthY && modesSquare[2] == 0)) {
									cout << "fillRemainingSquares: line X " << lineStartX[i][j] << " " << j << " " << lineEndX[i][j] << " " << j << endl;
									modeSquareOutput.add_operation(op_paint_line(lineStartX[i][j], j, lineEndX[i][j], j));
								}
								else {
									cout << "fillRemainingSquares: line Y " << i << " " << lineStartY[i][j] << " " << i << " " << lineEndY[i][j] << endl;
									modeSquareOutput.add_operation(op_paint_line(i, lineStartY[i][j], i, lineEndY[i][j]));
								}
							}
						}
					}
					cout << "fillRemainingSquares: modeSquareOutput.score() = " << modeSquareOutput.score() << endl;
					//keep just the best output over all square modes
					if(bestModeSquareOutputEmpty || bestModeSquareOutput.score() > modeSquareOutput.score()) {
						bestModeSquareOutput = modeSquareOutput;
						bestModeSquareOutputEmpty = false;
					}
				}
				modeQuadrantOutput = bestModeSquareOutput;
			}
		}
		ret.push_back(modeQuadrantOutput);
	}
	return ret;
}

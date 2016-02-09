#include <iostream>
#include <map>
#include <string>
#include <sstream>

#define WIDTH 20
#define HEIGHT 10

using namespace std;

void output(int width, int height, bool test[][HEIGHT], stringstream& ss) {
    ss << height << ' ' << width << '\n';
    for (int r = 0; r < height; r++) {
        for (int c = 0; c < width; c++) {
            ss << (test[c][r] ? '#' : '.');
        }
        ss << '\n';
    }
}

// upper left
void rect(int x, int y, int width, int height, bool test[][HEIGHT]) {
    for (int i = x; i < x + width; i++) {
        for (int j = y; j < y + height; j++) {
            test[i][j] = true;
        }
    }
}

void chessboard(int x, int y, int width, int height, bool test[][HEIGHT]) {
    for (int i = x; i < x + width; i++) {
        for (int j = y; j < y + height; j++) {
            if (i % 2 == 0 && j % 2 == 0) {
                test[i][j] = true;
            }
            if (i % 2 == 1 && j % 2 == 1) {
                test[i][j] = true;
            }
        }
    }
}

void clear(int x, int y, int width, int height, bool test[][HEIGHT]) {
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
            test[i][j] = false;
        }
    }
}

int main()
{
    bool test[WIDTH][HEIGHT];
    clear(0, 0, WIDTH, HEIGHT, test);
    
    // chessboard(0, 0, WIDTH, HEIGHT, test);
    rect(0, 0, WIDTH, 1, test);
    rect(0, HEIGHT - 1, WIDTH, 1, test);
    rect(0, 0, 1, HEIGHT, test);
    rect(WIDTH - 1, 0, 1, HEIGHT, test);
    
    rect (2,2,8,8,test);
    
    // for (int i = 0; i < WIDTH; i++) {
    //     if (i % 2 == 0) {
    //         rect(i, 0, 1, HEIGHT, test);
    //     }    
    // }

    stringstream out;
    output(WIDTH, HEIGHT, test, out);
    
    cout << out.str();
    
	return 0;
}

